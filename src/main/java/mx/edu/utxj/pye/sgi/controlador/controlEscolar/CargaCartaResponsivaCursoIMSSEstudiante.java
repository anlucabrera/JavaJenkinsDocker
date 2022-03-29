/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosCartaRespCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CargaCartaResponsivaCursoIMMSRolEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class CargaCartaResponsivaCursoIMSSEstudiante extends ViewScopedRol implements Desarrollable{
    @Getter @Setter private CargaCartaResponsivaCursoIMMSRolEstudiante rol = new CargaCartaResponsivaCursoIMMSRolEstudiante();
    
    @EJB EjbCargaCartaResponsivaCursoIMSS ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        try {
            if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)){
                cargado = true;
                setVistaControlador(ControlEscolarVistaControlador.CARGA_CARTARESPONSIVA_CURSOIMSS);
                ResultadoEJB<Estudiante> resAcceso = ejb.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
                ResultadoEJB<Estudiante> resValidacion = ejb.validarEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
                if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
                
                Estudiante estudiante = resValidacion.getValor();
                tieneAcceso = rol.tieneAcceso(estudiante, UsuarioTipo.ESTUDIANTE19);
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setEstudiante(estudiante);
                //////////////////////////////////////////////////////////////////////////////////////////////////////////
                if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
                if(!validarIdentificacion()) return;//detener el flujo si la invocación es de otra vista a través del maquetado del menu
                if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

                rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);

                rol.getInstrucciones().add("Selecciona la generación y nivel educativo del que deseas dar seguimiento.");
                rol.getInstrucciones().add("A continuación visualizarás una tabla que contiene la información de los documentos que corresponden.");
                rol.getInstrucciones().add("Los documentos los deberás de cargar en las fechas indicadas.");
                rol.getInstrucciones().add("En la columna OPCIONES, encontrarás el icono para cargar tu documento, descargarlo y eliminarlo.");
                rol.getInstrucciones().add("Podrás visualizar los comentarios y validaciones realizados por la Coordinación de Estadías Institucional.");

                eventosEstudiante();

            }else{
                return;
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "carga carta responsiva y curso imss";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de eventos de vinculación registrados disponibles para el estudiante
     */
    public void eventosEstudiante(){
        ResultadoEJB<List<EventoVinculacion>> res = ejb.getEventosEstudiante(rol.getEstudiante());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
              rol.setEventosVinculacion(res.getValor());
              generacionesEventosRegistrados();
            }
            rol.setEventosVinculacion(Collections.emptyList());
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de vinculación registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesEventos(rol.getEventosVinculacion());
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de niveles educativos en los que existen eventos de vinculación registrados de la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesGeneracionEventos(rol.getEventosVinculacion(), rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            seguimientoEstudiante();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener el seguimiento de vinculación del estudiante
     */
    public void seguimientoEstudiante(){
        ResultadoEJB<SeguimientoVinculacionEstudiante> resSeg = ejb.buscarSeguimientoEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), rol.getEstudiante());
        if(resSeg.getCorrecto()){
            ResultadoEJB<DtoCartaResponsivaCursoIMMSEstudiante> resDto = ejb.getSeguimientosEstudiante(resSeg.getValor());
            if(resDto.getCorrecto()){
                rol.setDtoCartaResponsivaCursoIMMSEstudiante(resDto.getValor());
                Ajax.update("frmSeguimientoEstudiante");
                Ajax.update("frmDocsSegEst");
            }else mostrarMensajeResultadoEJB(resDto);
        }else mostrarMensajeResultadoEJB(resSeg);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar el nivel educativo y la información del seguimiento del estudiante
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la información del seguimiento del estudiante
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            seguimientoEstudiante();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite verificar si se deshabilita la carga del documento cuando el evento se encuentra cerrado y no existe apertura extemporánea activa
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return 
    */
    public Boolean deshabilitarCarga(@NonNull DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<EventoVinculacion> res = ejb.buscarEventoActivoDocumento(rol.getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante);
        if(res.getCorrecto() && res.getValor() != null){
            permiso= Boolean.TRUE;
        }else{
            ResultadoEJB<EventoVinculacion> resA = ejb.buscarAperturaExtemporaneaDocumento(rol.getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante);
            if (resA.getCorrecto() && resA.getValor() != null) {
                permiso = Boolean.TRUE;
            }
        }
        return permiso;
    }
   
    /**
     * Permite verificar si existe evento activo para la carga del documento del estudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return 
    */
    public EventoVinculacion eventoActivoDocumento(@NonNull DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        EventoVinculacion eventoVinculacion =  new EventoVinculacion();
        ResultadoEJB<EventoVinculacion> res = ejb.buscarEventoActivoDocumento(rol.getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante);
        if(res.getCorrecto()){
            eventoVinculacion=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return eventoVinculacion;
    }
    
     /**
     * Permite consultar el evento para la carga del documento del estudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return 
    */
    public EventoVinculacion eventoDocumento(@NonNull DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        EventoVinculacion eventoVinculacion =  new EventoVinculacion();
        ResultadoEJB<EventoVinculacion> res = ejb.buscarEventoDocumento(rol.getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante);
        if(res.getCorrecto()){
            eventoVinculacion=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return eventoVinculacion;
    }
    
     /**
     * Permite verificar si existe apertura extemporánea activa para la carga del documento del estudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return 
    */
    public AperturaExtemporaneaEventoVinculacion aperturaEventoDocumento(@NonNull DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        AperturaExtemporaneaEventoVinculacion aperturaExtemporaneaEventoVinculacion =  new AperturaExtemporaneaEventoVinculacion();
        ResultadoEJB<AperturaExtemporaneaEventoVinculacion> res = ejb.aperturaExtemporaneaDocumento(rol.getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante);
        if(res.getCorrecto()){
            aperturaExtemporaneaEventoVinculacion=res.getValor();
        }
        return aperturaExtemporaneaEventoVinculacion;
    }
    
}
