/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguroFacultativo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionSeguroFacultativoRolEstudiantiles;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroSeguroFacultativoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteExcelSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class ValidacionSeguroFacultativoEstudiantiles extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -3037593892768164861L;
    @Getter             @Setter                                         private                                         ValidacionSeguroFacultativoRolEstudiantiles                rol;
    @EJB                EjbValidacionSeguroFacultativo                  ejb;
    @EJB                EjbRegistroSeguroFacultativoEstudiante          ejbRegistroSeguroFacultativoEstudiante;
    @EJB                EjbPropiedades                                  ep;
    @EJB                EjbPeriodoEventoRegistro                        ejbPeriodoEventoRegistro;
    @EJB                EjbReporteExcelSeguroFacultativo                ejbReporteExcel;
    
    @Inject             LogonMB                                         logon;
    @Inject             Caster                                          caster;
    @Getter             Boolean                                         tieneAcceso = false;
    
    @Inject             LogonMB logonMB;
    @Getter             private                                         Boolean cargado = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "validacion seguro facultativo";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.VALIDACION_SEGURO_FACULTATIVO);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoJefeDepartamento = ejb.validarJefeDepartamentoEstudiantiles(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEncargadoDepartamento = ejb.validarEncargadoDepartamentoEstudiantiles(logon.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEnfermeria = ejb.validarEnfermeria(logon.getPersonal().getClave());
            if(!resAccesoEnfermeria.getCorrecto() && !resAccesoEncargadoDepartamento.getCorrecto() && !resAccesoJefeDepartamento.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoJefeDepartamento);return;}
            Filter<PersonalActivo> filtro = resAccesoJefeDepartamento.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new ValidacionSeguroFacultativoRolEstudiantiles(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){
                rol.setFiltro(resAccesoEncargadoDepartamento.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
            }
            if(!tieneAcceso){
                rol.setFiltro(resAccesoEnfermeria.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
            }
            if(!tieneAcceso){return;}
            rol.setPersonalEstudiantiles(personal);
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresConSF();
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.SUPERIOR);
            rol.setPeriodoEscolarActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor());
            rol.setListaPeriodosEscolares(resPeriodos.getValor());
            cambiarPeriodo();
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    /*********************************************** Inicializadores *********************************************************/
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public void inicializarListaDtoSegurosFacultativos(){
        rol.setListaDtoSeguroFacultativo(new ArrayList<>());
        rol.setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
    }
    
    public void inicializarCasoFiltro(){
        rol.setEstudianteSeguimientoSeleccionado(null);
        rol.setEstudianteSeleccionado(null);
    }
    
    public void inicializarCasoEstudiante(){
        rol.setPeriodoEscolarSeleccionado(null);
    }
    
    /*********************************************** Filtros *********************************************************/
    public void cambiarPeriodo(){
        if(rol.getPeriodoEscolarSeleccionado() == null){
            mostrarMensaje("No hay periodo escoalar seleccionado");
            rol.setListaProgramasEducativos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<AreasUniversidad>> resListaProgramasEducativos = ejb.obtenerProgramasEducativosPorPeriodoSF(rol.getPeriodoEscolarSeleccionado());
        if(!resListaProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resListaProgramasEducativos);
        else rol.setListaProgramasEducativos(resListaProgramasEducativos.getValor());
        cambiarProgramaEducativo();
        inicializarCasoFiltro();
    }
    
    public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay ningún programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resListaGrupos = ejb.obtenerGruposPorPeriodoProgramaEducativoSF(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
        if(!resListaGrupos.getCorrecto()) mostrarMensajeResultadoEJB(resListaGrupos);
        else rol.setListaGrupos(resListaGrupos.getValor());
        cambiarGrupo();
    }
    
    public void cambiarGrupo(){
        if(rol.getGrupo() == null){
            mostrarMensaje("No hay ningún grupo seleccionado");
            rol.setListaDtoSeguroFacultativo(Collections.EMPTY_LIST);
            return;
        }
        actualizarListaDtoSegurosFacultativos();
    }
    
    public void actualizarListaDtoSegurosFacultativos(){
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSF = ejb.obtenerSFPorPeriodoProgramaEducativoGrupo(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(), rol.getGrupo());
        if(resListaDtoSF.getCorrecto()){
            inicializarListaDtoSegurosFacultativos();
            mostrarMensajeResultadoEJB(resListaDtoSF);
            rol.setListaDtoSeguroFacultativo(resListaDtoSF.getValor());
        }else{
            mostrarMensajeResultadoEJB(resListaDtoSF);
            inicializarListaDtoSegurosFacultativos();
        }
    }
    
    public void actualzarListaDtoSegurosFacultativosEstudiante(){
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSf = ejb.obtenerSeguroFacultativoPorEstudiante(rol.getEstudianteSeleccionado().getMatricula());
        if(resListaDtoSf.getCorrecto()){
            inicializarListaDtoSegurosFacultativos();
            mostrarMensajeResultadoEJB(resListaDtoSf);
            rol.setListaDtoSeguroFacultativo(resListaDtoSf.getValor());
        }else{
            mostrarMensajeResultadoEJB(resListaDtoSf);
            inicializarListaDtoSegurosFacultativos();
        }
    }
    
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantes = ejb.buscarEstudiante(pista);
        if(reslistaEstudiantes.getCorrecto()){
            mostrarMensajeResultadoEJB(reslistaEstudiantes);
            return reslistaEstudiantes.getValor();
        }else{
            mostrarMensajeResultadoEJB(reslistaEstudiantes);
            return Collections.EMPTY_LIST;
        }
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<List<DtoSeguroFacultativo>> resListaSF = ejb.obtenerSeguroFacultativoPorEstudiante(estudiante.getEstudiantes().getMatricula());
            if(resListaSF.getCorrecto()){
                inicializarCasoEstudiante();
                rol.setEstudianteSeleccionado(estudiante.getEstudiantes());
                actualzarListaDtoSegurosFacultativosEstudiante();
            }else{
                mostrarMensajeResultadoEJB(resListaSF);
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    /*********************************************** Método y funciones *********************************************************/
    
    public void registrarComentarios(ValueChangeEvent event){
        if(event == null){mostrarMensaje("No se ha enviado ningún dato para actualizar");return;}
        if(event.getNewValue() == null){mostrarMensaje("Favor de ingresar los cometarios para el estudiante, antes de guardar");return;}
        if("".equals((String) event.getNewValue())) {mostrarMensaje("Favor de ingresar los cometarios para el estudiante, antes de guardar");return;}
        DtoSeguroFacultativo seguroFacultativo = (DtoSeguroFacultativo) event.getComponent().getAttributes().get("dtoSeguroFacultativo");
        String comentariosEnfermeria = (String) event.getNewValue();
        seguroFacultativo.getSeguroFactultativo().setComentariosEnfermeria(comentariosEnfermeria);
        seguroFacultativo.getSeguroFactultativo().setEstatusRegistro(SegurosFacultativosEstatus.EN_OBSERVACIONES.getLabel());
        ResultadoEJB<Boolean> res = ejbRegistroSeguroFacultativoEstudiante.editaRegistroSeguroFacultativo(seguroFacultativo.getSeguroFactultativo());
        if(res.getCorrecto()){
            if (rol.getEstudianteSeleccionado() == null) {
                actualizarListaDtoSegurosFacultativos();
            } else {
                actualzarListaDtoSegurosFacultativosEstudiante();
            }
            mostrarMensajeResultadoEJB(res);
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void eliminarSeguroFacultativo(DtoSeguroFacultativo dtoSeguroFacultativo){
        ResultadoEJB<Boolean> res = ejbRegistroSeguroFacultativoEstudiante.eliminaRegistroSeguroFacultativo(dtoSeguroFacultativo.getSeguroFactultativo(), rol.getPersonalEstudiantiles());
        if(res.getCorrecto()){
            if (rol.getEstudianteSeleccionado() == null) {
                actualizarListaDtoSegurosFacultativos();
            } else {
                actualzarListaDtoSegurosFacultativosEstudiante();
            }
            mostrarMensajeResultadoEJB(res);
        }else{
            mostrarMensajeResultadoEJB(res);
        }
    }
    
    public void validarArchivoTarjetonImss(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
            DtoSeguroFacultativo dtoSeguroFacultativo = (DtoSeguroFacultativo) event.getComponent().getAttributes().get("dtoSeguroFacultativo");
            rol.setDtoSeguroFacultativoSeleccionado(dtoSeguroFacultativo);
            if(rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().getEstatusTarjeton().equals(SegurosFacultativosEstatus.REGISTRADO.getLabel())){
                mostrarMensaje("No se puede validar un archivo si el estudiante aún no envía su registro para validación");
                return;
            }
            rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().setEstatusTarjeton((String)event.getNewValue());
            ResultadoEJB<Boolean> resValidarTarjeton = ejb.validarArchivoEstudiante(rol.getDtoSeguroFacultativoSeleccionado(), rol.getPersonalEstudiantiles());
            if(resValidarTarjeton.getCorrecto()){
                if(rol.getEstudianteSeleccionado() == null){
                    actualizarListaDtoSegurosFacultativos();
                }else{
                    actualzarListaDtoSegurosFacultativosEstudiante();
                }
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }else{
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void validarArchivoComprobanteLocalizacion(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
            DtoSeguroFacultativo dtoSeguroFacultativo = (DtoSeguroFacultativo) event.getComponent().getAttributes().get("dtoSeguroFacultativo");
            rol.setDtoSeguroFacultativoSeleccionado(dtoSeguroFacultativo);
            if(rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().getEstatusComprobanteLocalizacion().equals(SegurosFacultativosEstatus.REGISTRADO.getLabel())){
                mostrarMensaje("No se puede validar un archivo si el estudiante aún no envía su registro para validación");
                return;
            }
            rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().setEstatusComprobanteLocalizacion((String)event.getNewValue());
            ResultadoEJB<Boolean> resValidarTarjeton = ejb.validarArchivoEstudiante(rol.getDtoSeguroFacultativoSeleccionado(), rol.getPersonalEstudiantiles());
            if(resValidarTarjeton.getCorrecto()){
                if(rol.getEstudianteSeleccionado() == null){
                    actualizarListaDtoSegurosFacultativos();
                }else{
                    actualzarListaDtoSegurosFacultativosEstudiante();
                }
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }else{
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void validarArchivoComprobanteVigenciaDeDerechos(ValueChangeEvent event){
        if(event.getNewValue() instanceof String){
            DtoSeguroFacultativo dtoSeguroFacultativo = (DtoSeguroFacultativo) event.getComponent().getAttributes().get("dtoSeguroFacultativo");
            rol.setDtoSeguroFacultativoSeleccionado(dtoSeguroFacultativo);
            if(rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().getEstatusComprobanteVigenciaDerechos().equals(SegurosFacultativosEstatus.REGISTRADO.getLabel())){
                mostrarMensaje("No se puede validar un archivo si el estudiante aún no envía su registro para validación");
                return;
            }
            rol.getDtoSeguroFacultativoSeleccionado().getSeguroFactultativo().setEstatusComprobanteVigenciaDerechos((String)event.getNewValue());
            ResultadoEJB<Boolean> resValidarTarjeton = ejb.validarArchivoEstudiante(rol.getDtoSeguroFacultativoSeleccionado(), rol.getPersonalEstudiantiles());
            if(resValidarTarjeton.getCorrecto()){
                if(rol.getEstudianteSeleccionado() == null){
                    actualizarListaDtoSegurosFacultativos();
                }else{
                    actualzarListaDtoSegurosFacultativosEstudiante();
                }
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }else{
                mostrarMensajeResultadoEJB(resValidarTarjeton);
            }
        }else mostrarMensaje("El valor seleccionado no es valido");
    }
    
    public void validarRegistroSeguroFacultativo(ValueChangeEvent event){
        DtoSeguroFacultativo dtoSeguroFacultativo = (DtoSeguroFacultativo) event.getComponent().getAttributes().get("dtoSeguroFacultativo");
        rol.setDtoSeguroFacultativoSeleccionado(dtoSeguroFacultativo);
        if(rol.getDtoSeguroFacultativoSeleccionado().getValidacionSeguro()){
            ResultadoEJB<Boolean> resBaja = ejb.bajaSeguroFacultativoEstudiante(rol.getDtoSeguroFacultativoSeleccionado(), rol.getPersonalEstudiantiles());
            if(resBaja.getCorrecto()){
                if(rol.getEstudianteSeleccionado() == null){
                    actualizarListaDtoSegurosFacultativos();
                }else{
                    actualzarListaDtoSegurosFacultativosEstudiante();
                }
            }
            mostrarMensajeResultadoEJB(resBaja);
        }else{
            ResultadoEJB<Boolean> resAlta = ejb.altaSeguroFacultativoEstudiante(rol.getDtoSeguroFacultativoSeleccionado(), rol.getPersonalEstudiantiles());
            if(resAlta.getCorrecto()){
                if(rol.getEstudianteSeleccionado() == null){
                    actualizarListaDtoSegurosFacultativos();
                }else{
                    actualzarListaDtoSegurosFacultativosEstudiante();
                }
            }    
            mostrarMensajeResultadoEJB(resAlta);
        }
    }
    
    public void descargarArchivoEstudiante(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
    
    public void descargarReportePorPeriodo() {
        try {
            ResultadoEJB<String> res = ejbReporteExcel.getReporteSfCuatrimestralGeneral(rol.getPeriodoEscolarSeleccionado(), rol.getPersonalEstudiantiles().getPersonal().getClave());
            if (res.getCorrecto()) {
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (IOException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }
    
    public void descargarReportePorPeriodoProgramaEducativo() {
        try {
            ResultadoEJB<String> res = ejbReporteExcel.getReporteSfProgramaEducativo(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(), rol.getPersonalEstudiantiles().getPersonal().getClave());
            if (res.getCorrecto()) {
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (IOException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }
    
    public void descargarReportePorPeriodoProgramaEducativoGrupal() {
        try {
            ResultadoEJB<String> res = ejbReporteExcel.getReporteSfGrupal(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(),rol.getListaDtoSeguroFacultativo(), rol.getPersonalEstudiantiles().getPersonal().getClave());
            if (res.getCorrecto()) {
                File f = new File(res.getValor());
                Faces.sendFile(f, true);
            }
            mostrarMensajeResultadoEJB(res);
        } catch (IOException ex) {
            mostrarMensaje(ex.getMessage());
        }
    }
    
}
