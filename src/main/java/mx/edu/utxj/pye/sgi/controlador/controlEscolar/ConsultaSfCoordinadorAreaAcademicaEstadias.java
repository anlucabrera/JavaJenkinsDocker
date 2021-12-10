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
 *  Rol especificoCoordinadorAreaAcademicaEstadias
 *      4.- Coordinador Área Académica Estadías:        Muestra solo sus programas educativos - Consulta mediante periodo-ciclo-ciclosgen-generación
 *          - Consulta mediante periodos escolares obtenidos desde ejbConsultaSeguroFacultativos
 * TODO: Modificar búsqueda de estudiante, solo debe contener los filtros de área académica
 * 
 * @author UTXJ
 */
@Named
@ViewScoped
public class ConsultaSfCoordinadorAreaAcademicaEstadias extends ViewScopedRol implements Desarrollable{
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
        String valor = "consulta seguro facultativo coordinador area academica estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.CONSULTA_S_F_COORDINADOR_AREA_ACADEMICA_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoCoordinadorAreaAcademicaEstadias = ejb.validarCoordinadorAreaAcademicaEstadia(logonMB.getPersonal().getClave());  
            if(!resAccesoCoordinadorAreaAcademicaEstadias.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoCoordinadorAreaAcademicaEstadias);return;}
            Filter<PersonalActivo> filtro = resAccesoCoordinadorAreaAcademicaEstadias.getValor();
            PersonalActivo personal = filtro.getEntity();
            rol = new ValidacionSeguroFacultativoRolEstudiantiles(filtro);
            tieneAcceso = rol.tieneAcceso(personal);
            rol.setRolConsultaSeguroFacultativo(RolesConsultaSeguroFacultativo.ESPECIFICO_COORDINADOR_AREA_ACADEMICA_ESTADIA.getLabel());
            if(!tieneAcceso){return;}
            rol.setPersonalEstudiantiles(personal);  
            ResultadoEJB<List<PeriodosEscolares>> resPeriodos = ejb.obtenerPeriodosEscolaresConSFPorCoordinadorAreaAcademicaEstadia(rol.getPersonalEstudiantiles());
//            ESPECIFICO_COORDINADOR_AREA_ACADEMICA_ESTADIA
            if(!resPeriodos.getCorrecto()){mostrarMensajeResultadoEJB(resPeriodos); tieneAcceso = false;}
            rol.setListaPeriodosEscolares(resPeriodos.getValor());
            List<Integer> listaPeriodosEscolares = rol.getListaPeriodosEscolares()
                    .stream()
                    .map(idPeriodo -> idPeriodo.getPeriodo())
                    .collect(Collectors.toList());
            rol.setListaIdPeriodosEscolares(listaPeriodosEscolares);
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
        ResultadoEJB<List<AreasUniversidad>> resProgramasEducativos = ejb.obtenerProgramasEducativosConSFPorCoordinadorAreaAcademicaEstadia(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado());
        if(!resProgramasEducativos.getCorrecto()) mostrarMensajeResultadoEJB(resProgramasEducativos);
        else rol.setListaProgramasEducativos(resProgramasEducativos.getValor());
        List<Short> listaProgramasEducativos = rol.getListaProgramasEducativos()
                .stream()
                .map(programaEducativo -> programaEducativo.getArea())
                .collect(Collectors.toList());
        rol.setListaIdProgramasEducativos(listaProgramasEducativos); 
        cambiarProgramaEducativo();
        inicializarCasoFiltro();
    }
     
     public void cambiarProgramaEducativo(){
        if(rol.getProgramaEducativo() == null){
            mostrarMensaje("No hay ningún programa educativo seleccionado");
            rol.setListaGrupos(Collections.EMPTY_LIST);
            return;
        }
        ResultadoEJB<List<Grupo>> resGrupos = ejb.obtenerGruposConSFPorCoordinadorAreaAcademicaEstadia(rol.getPersonalEstudiantiles(), rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo());
        if(!resGrupos.getCorrecto()) mostrarMensajeResultadoEJB(resGrupos);
        else rol.setListaGrupos(resGrupos.getValor());
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
        ResultadoEJB<List<DtoSeguroFacultativo>> resListaDtoSF = ejbValidacionSeguroFacultativo.obtenerSFPorPeriodoProgramaEducativoGrupo(rol.getPeriodoEscolarSeleccionado(), rol.getProgramaEducativo(), rol.getGrupo());
//            GENERAL
//            PROGRAMA_EDUCATIVO    
//            ESPECIFICO_COORDINADOR_AREA_ACADEMICA_ESTADIA    
//            ESPECIFICO_TUTOR
//            ESPECIFICO_ASESOR_ACADEMICO
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
        ResultadoEJB<List<DtoEstudianteComplete>> reslistaEstudiantes = ejbValidacionSeguroFacultativo.buscarEstudiantePorPeriodosProgramaEducativos(pista, rol.getListaIdPeriodosEscolares(), rol.getListaIdProgramasEducativos());
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
