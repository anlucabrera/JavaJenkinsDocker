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
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbConsultaSegurosFacultativos;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteExcelSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionSeguroFacultativo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.RolesConsultaSeguroFacultativo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 * - Acceso para:
 * 
 *  Rol General
 *      1.- Secretaría Académica:                       Muestra todos los programas educativos 
 *          - Consulta mediante periodos escolares obtenidos desde ejbValidacionSeguroFacultativo
 *      3.- Coordinador Institucional estadías:         Muestra todos los programas educativos
 *          - Consulta mediante periodos escolares obtenidos desde ejbValidacionSeguroFacultativo
 *  Rol programaEducativo
 *      2.- Directores:                                 Muestra solo sus programas educativos - Director y encargado director 
 *          - Consulta mediante periodos escolares obtenidos desde ejbConsultaSeguroFacultativos
 *  Rol especificoTutor
 *      5.- Tutores:                                    Muestra solo sus grupos tutorados
 *          - Consulta mediante periodos escolares obtenidos desde ejbConsultaSeguroFacultativos
 *  
 * @author UTXJ
 */
@Named
@ViewScoped
public class ConsultaSegurosFacultativos extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = 8590868880559689528L;
    @Getter             @Setter                                         private                                         ValidacionSeguroFacultativoRolEstudiantiles                rol;
    @EJB                EjbPropiedades                                  ep;
    @EJB                EjbPeriodoEventoRegistro                        ejbPeriodoEventoRegistro;
    @EJB                EjbConsultaSegurosFacultativos                  ejb;
    @EJB                EjbValidacionSeguroFacultativo                  ejbValidacionSeguroFacultativo;
    @EJB                EjbReporteExcelSeguroFacultativo                ejbReporteExcel;
    @EJB                EjbValidacionRol                                ejbValidacionRol;
    
    @Inject             LogonMB                                         logon;
    @Inject             Caster                                          caster;
    @Getter             Boolean                                         tieneAcceso = false;
    
    @Inject             LogonMB logonMB;
    @Getter             private                                         Boolean cargado = false;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "consulta seguro facultativo";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_SEGURO_FACULTATIVO);
            
            ResultadoEJB<Filter<PersonalActivo>> resAccesoDirector = ejbValidacionRol.validarDirector(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEncargado = ejbValidacionRol.validarEncargadoDireccion(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoSecretariaAcademica = ejbValidacionRol.validarSecretariaAcademica(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoCoordinadorInstitucionalEstadias = ejb.validarCoordinadorEstadia(logonMB.getPersonal().getClave());
            ResultadoEJB<Filter<PersonalActivo>> resAccesoTutor = ejb.validarTutor(logonMB.getPersonal().getClave()); 
            
            if(!resAccesoDirector.getCorrecto() && !resAccesoEncargado.getCorrecto() && !resAccesoSecretariaAcademica.getCorrecto() && !resAccesoCoordinadorInstitucionalEstadias.getCorrecto()
                    && !resAccesoTutor.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoDirector);return;}
            Filter<PersonalActivo> filtro = resAccesoDirector.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new ValidacionSeguroFacultativoRolEstudiantiles(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel());
            if(!tieneAcceso){
                rol.setFiltro(resAccesoEncargado.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
                rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel());
            }
            if(!tieneAcceso){
                rol.setFiltro(resAccesoSecretariaAcademica.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
                rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.GENERAL.getLabel());
            }
            if(!tieneAcceso){
                rol.setFiltro(resAccesoCoordinadorInstitucionalEstadias.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
                rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.GENERAL.getLabel());
            }
            if(!tieneAcceso){
                rol.setFiltro(resAccesoTutor.getValor());
                tieneAcceso = rol.tieneAcceso(personal);
                rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel());
            }
            if(!tieneAcceso){return;}
            rol.setPersonalEstudiantiles(personal);
            
//            GENERAL
            if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.GENERAL.getLabel())) {
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejbValidacionSeguroFacultativo.obtenerPeriodosEscolaresConSF();
                if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
                rol.setListaPeriodosEscolares(resPeriodos.getValor());
            }
//            PROGRAMA_EDUCATIVO
            if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel())) {
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresConSFPorDirector(rol.getPersonalEstudiantiles());
                if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
                rol.setListaPeriodosEscolares(resPeriodos.getValor());
            }
//            ESPECIFICO_TUTOR
            if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())) {
                ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresConSFPorTutor(rol.getPersonalEstudiantiles());
                if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
                rol.setListaPeriodosEscolares(resPeriodos.getValor());
            }
            
            if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel()) || rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())){
                List<Integer> listaPeriodosEscolares = rol.getListaPeriodosEscolares()
                        .stream()
                        .map(idPeriodo -> idPeriodo.getPeriodo())
                        .collect(Collectors.toList());
                rol.setListaIdPeriodosEscolares(listaPeriodosEscolares);
            }
            
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.CONSULTA);
            rol.setPeriodoEscolarActivo(ejbPeriodoEventoRegistro.getPeriodoEscolarActivo().getValor());
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
        
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejbValidacionSeguroFacultativo.obtenerProgramasEducativosPorPeriodoSF(rol.getPeriodoEscolarSeleccionado());
//            GENERAL    
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.GENERAL.getLabel())) resProgramasEducativos = ejbValidacionSeguroFacultativo.obtenerProgramasEducativosPorPeriodoSF(rol.getPeriodoEscolarSeleccionado());
//            PROGRAMA_EDUCATIVO    
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel())) resProgramasEducativos = ejb.obtenerProgramasEducativosConSFPorDirector(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado());
//            ESPECIFICO_TUTOR    
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())) resProgramasEducativos = ejb.obtenerProgramasEducativosConSFPorTutor(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado());
        
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setListaProgramasEducativos(resProgramasEducativos.getValor());
        if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel()) || rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())) {
             List<Short> listaProgramasEducativos = rol.getListaProgramasEducativos()
                     .stream()
                     .map(programaEducativo -> programaEducativo.getArea())
                     .collect(Collectors.toList());
             rol.setListaIdProgramasEducativos(listaProgramasEducativos); 
        }
        cambiarProgramaEducativo();
        inicializarCasoFiltro();
    }
     
     public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay ningún programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resGrupos = ejbValidacionSeguroFacultativo.obtenerGruposPorPeriodoProgramaEducativoSF(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
//            GENERAL
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.GENERAL.getLabel())) resGrupos = ejbValidacionSeguroFacultativo.obtenerGruposPorPeriodoProgramaEducativoSF(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
//            PROGRAMA_EDUCATIVO    
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel())) resGrupos = ejb.obtenerGruposConSFPorDirector(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
//            ESPECIFICO_TUTOR    
        if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())) resGrupos = ejb.obtenerGruposConSFPorTutor(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
        if(!resGrupos.getCorrecto()) mostrarMensajeResultadoEJB(resGrupos);
        else rol.setListaGrupos(resGrupos.getValor());
        if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())) {
             List<Integer> listaGrupos = rol.getListaGrupos()
                     .stream()
                     .map(grupo -> grupo.getIdGrupo())
                     .collect(Collectors.toList());
             rol.setListaIdGrupos(listaGrupos); 
        }
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
//            GENERAL
//            PROGRAMA_EDUCATIVO 
//            ESPECIFICO_TUTOR
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSF = ejbValidacionSeguroFacultativo.obtenerSFPorPeriodoProgramaEducativoGrupo(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(), rol.getGrupo());
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
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSf = ejbValidacionSeguroFacultativo.obtenerSeguroFacultativoPorEstudiante(rol.getEstudianteSeleccionado().getMatricula());
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
        if (rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel())) {
            ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantesPeriodoPrograma = ejbValidacionSeguroFacultativo.buscarEstudiantePorPeriodosProgramaEducativos(pista, rol.getListaIdPeriodosEscolares(), rol.getListaIdProgramasEducativos()); 
            if(reslistaEstudiantesPeriodoPrograma.getCorrecto()){
                mostrarMensajeResultadoEJB(reslistaEstudiantesPeriodoPrograma);
                return reslistaEstudiantesPeriodoPrograma.getValor();
            }else{
                mostrarMensajeResultadoEJB(reslistaEstudiantesPeriodoPrograma);
                return Collections.EMPTY_LIST;
            }
        }else if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.ESPECIFICO_TUTOR.getLabel())){
            ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantesPeriodoProgramaGrupo = ejbValidacionSeguroFacultativo.buscarEstudiantePorPeriodosProgramaEducativosGrupos(pista, rol.getListaIdPeriodosEscolares(), rol.getListaIdProgramasEducativos(), rol.getListaIdGrupos()); 
            if(reslistaEstudiantesPeriodoProgramaGrupo.getCorrecto()){
                mostrarMensajeResultadoEJB(reslistaEstudiantesPeriodoProgramaGrupo);
                return reslistaEstudiantesPeriodoProgramaGrupo.getValor();
            }else{
                mostrarMensajeResultadoEJB(reslistaEstudiantesPeriodoProgramaGrupo);
                return Collections.EMPTY_LIST;
            }
        }else{
            ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantes = ejbValidacionSeguroFacultativo.buscarEstudiante(pista); 
            if(reslistaEstudiantes.getCorrecto()){
                mostrarMensajeResultadoEJB(reslistaEstudiantes);
                return reslistaEstudiantes.getValor();
            }else{
                mostrarMensajeResultadoEJB(reslistaEstudiantes);
                return Collections.EMPTY_LIST;
            }
        }
    }
    
    public void cambiarEstudianteSeleccionado(ValueChangeEvent event){
        if(event.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete) event.getNewValue();
            ResultadoEJB<List<DtoSeguroFacultativo>> resListaSF = ejbValidacionSeguroFacultativo.obtenerSeguroFacultativoPorEstudiante(estudiante.getEstudiantes().getMatricula());
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
     
     public void descargarArchivoEstudiante(String rutaArchivo) throws IOException{
        if(rutaArchivo == null) {mostrarMensaje("No se cargó ningún archivo"); return;}
        if("".equals(rutaArchivo)){mostrarMensaje("No se cargó ningún archivo"); return;}
        File f = new File(rutaArchivo);
        Faces.sendFile(f, false);
    }
     
     public void descargarReportePorPeriodo() {
        try {
            if(rol.getRolConsultaSeguroFacultativo().equals(RolesConsultaSeguroFacultativo.PROGRAMA_EDUCATIVO.getLabel())){
                ResultadoEJB<String> res = ejbReporteExcel.getReporteSfCuatrimestralDirector(rol.getPeriodoEscolarSeleccionado(), rol.getPersonalEstudiantiles());
                if (res.getCorrecto()) {
                    File f = new File(res.getValor());
                    Faces.sendFile(f, true);
                }
            mostrarMensajeResultadoEJB(res);
            }else{
                ResultadoEJB<String> res = ejbReporteExcel.getReporteSfCuatrimestralGeneral(rol.getPeriodoEscolarSeleccionado(), rol.getPersonalEstudiantiles().getPersonal().getClave());
                if (res.getCorrecto()) {
                    File f = new File(res.getValor());
                    Faces.sendFile(f, true);
                }
                mostrarMensajeResultadoEJB(res);
            } 
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
