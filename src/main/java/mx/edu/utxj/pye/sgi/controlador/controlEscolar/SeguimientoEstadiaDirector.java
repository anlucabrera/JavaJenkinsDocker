/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEstadiaRolDirector;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoEstadiaDirector extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoEstadiaRolDirector rol;
    
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    @Inject GenerarCedEvalAcredEstadia generarCedEvalAcredEstadia;
       
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    @Getter private SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por dirección de carrera<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
 

@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ESTADIA_DIRECTOR);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbAsignacionRolesEstadia.validarDirector(logon.getPersonal().getClave());//validar si es director

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejbAsignacionRolesEstadia.validarEncargadoDireccion(logon.getPersonal().getClave());//validar si es encargado de dirección
            if(!resValidacion.getCorrecto() && !resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo director = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoEstadiaRolDirector(filtro, director);
           tieneAcceso = rol.tieneAcceso(director);
//            System.out.println("tieneAcceso1 = " + tieneAcceso);
            if(!tieneAcceso){
                rol.setFiltro(resValidacion.getValor());
                tieneAcceso = rol.tieneAcceso(director);
            }
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setDirectorCarrera(director);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccione programa educativo");
            rol.getInstrucciones().add("En la tabla podrá visualizar la información de estadía de los estudiantes que tienen asignados los asesores académicos que pertenecen a su área.");
            rol.getInstrucciones().add("Los datos indicados con * son aquellos en los que interviene.");
            rol.getInstrucciones().add("Dar clic en botón que se habilita para validar o invalidar la información registrada por el asesor académico.");
            rol.getInstrucciones().add("Dar clic en el campo del comentario para capturar las observaciones, en caso contrario dejar con el texto predeterminado.");
            rol.getInstrucciones().add("El botón para generar la cédula evaluación y acreditación de estadía se habilita cuando el estudiante acreditó sus evaluaciones, de lo contrario no podrá generar el formato.");
            
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "seguimiento estudiantes estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesSeguimientoRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de niveles educativos que existen en eventos de estadía registrados dependiendo de la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProgramasNivelGeneracion();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de niveles educativos que existen en eventos de estadía registrados dependiendo de la generación seleccionada
     */
    public void listaProgramasNivelGeneracion(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<List<AreasUniversidad>> res = ejbAsignacionRolesEstadia.getProgramasNivelesGeneracionEventosRegistrados(rol.getGeneracion(), rol.getNivelEducativo(), rol.getDirectorCarrera().getAreaOperativa().getArea());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaEstudiantesSeguimiento();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaEstudiantesSeguimiento(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesSeguimientoArea(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo(), rol.getDirectorCarrera().getPersonal());
        if(res.getCorrecto()){
            rol.setEstudiantesSeguimiento(res.getValor());
            Ajax.update("tbListaSeguimientoEstadia");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de estudiantes asignados
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de estudiantes asignados
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de estudiantes asignados
     * @param e Evento del cambio de valor
     */
    public void cambiarProgramaEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  AreasUniversidad){
            AreasUniversidad programa = (AreasUniversidad)e.getNewValue();
            rol.setProgramaEducativo(programa);
            listaEstudiantesSeguimiento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Método que permite consultar si el documento ha sido registrado en la base de datos
     * @param claveDocumento 
     * @param dtoSeguimientoEstadia 
     * @return  Verdadero o Falso según sea el caso
     */
    public Boolean consultarExisteDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.consultarClaveDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
    /**
     * Método que busca la ruta del documento seleccionado para su descarga
     * @param claveDocumento
     * @param dtoSeguimientoEstadia
     * @throws java.io.IOException
     */
    public void descargarDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia) throws IOException{
        ResultadoEJB<DocumentoSeguimientoEstadia> resBuscarDoc = ejb.buscarDocumentoEstudiante(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), claveDocumento);
        File f = new File(resBuscarDoc.getValor().getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean buscarValidacionDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.buscarValidacionDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
     /**
     * Método que permite validar o invalidar el seguimiento de estadía seleccionado
     * @param dtoSeguimientoEstadia
     */
    public void validarEstadiaDirector(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resValidar = ejb.validarInformacionEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), "directorArea");
        mostrarMensajeResultadoEJB(resValidar);
        listaEstudiantesSeguimiento();
        Ajax.update("frm");
    }
    
    public void seleccionarDocumento(DtoSeguimientoEstadia dtoSeguimientoEstadia) {
        seguimientoEstadiaEstudiante = dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante();
    }
    
    
    public void subirDocumento() {
        try {
            
            EventoEstadia eventoEstadia = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Registro cedula evaluacion y acreditacion estadia").getValor();
            Documento documento = ejb.getDocumentoEstadia("Acreditación de estadía T.S.U.").getValor();
            DocumentoProceso documentoProceso = ejb.getDocumentoProcesoEstadia(documento).getValor();
            DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante = ejb.packDocumento(documentoProceso, seguimientoEstadiaEstudiante).getValor();
            
            DocumentoSeguimientoEstadia nuevoDocumento = new DocumentoSeguimientoEstadia();

            nuevoDocumento.setSeguimientoEstadia(seguimientoEstadiaEstudiante);
            nuevoDocumento.setEvento(eventoEstadia);
            nuevoDocumento.setDocumento(documento);
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoEstadia(file, seguimientoEstadiaEstudiante, dtoDocumentoEstadiaEstudiante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejb.guardarDocumentoSeguimientoEstadia(nuevoDocumento).getValor();
            listaEstudiantesSeguimiento();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosSeguimientoEstadia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarDocumento(DocumentoSeguimientoEstadia docsSeg){
        ResultadoEJB<Integer> resEliminar =  ejb.eliminarDocumentoSeguimiento(docsSeg);
        if(resEliminar.getCorrecto()){
        if(resEliminar.getValor() == 1){ 
            listaEstudiantesSeguimiento();
            Ajax.update("tbListaSeguimientoEstadia");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public Boolean consultarExisteDocumento(DocumentoSeguimientoEstadia documentoSeguimientoEstadia){
        return ejb.consultarDocumento(documentoSeguimientoEstadia.getDocumento(), documentoSeguimientoEstadia.getSeguimientoEstadia()).getValor();
    }
    
    public DocumentoSeguimientoEstadia buscarDocumentoEstudiante(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.buscarDocumentoEstudiante(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), claveDocumento).getValor();
    }
    
    /**
     * Permite generar el formato de cédula de evaluación y acreditación de estadía dele studiante seleccionado
     * @param dtoSeguimientoEstadia 
     */
    public void generarCedEvalAcredEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia) {
        rol.setEstudianteSeguimiento(dtoSeguimientoEstadia);
        if(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante().getPromedioAsesorInterno()== 0)
        {
            Messages.addGlobalWarn("No se ha registrado evaluación de estadía del estudiante seleccionado");
            rol.setEvaluacionRegistrada(Boolean.FALSE);
        }else{
            listaCriteriosEvaluacionEstadiaRegistrada();
            rol.setEvaluacionRegistrada(Boolean.TRUE);
        }
        generarCedEvalAcredEstadia.generarFormato(rol.getEstudianteSeguimiento(), rol.getEvaluacionRegistrada());
    }
    
     /**
     * Permite obtener la lista de criterios de evaluación de estadía del estudiante
     */
    public void listaCriteriosEvaluacionEstadiaRegistrada(){
        ResultadoEJB<List<CalificacionCriterioEstadia>> res = ejb.getListaEvaluacionEstadiaRegistrada(rol.getEstudianteSeguimiento().getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            rol.setListaEvaluacionEstadiaRegistrada(res.getValor());
            Ajax.update("frmModalCapturaEvaluacion");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Método para verificar si el estudiante seleccionado aprobó sus evaluaciones de estadía (asesor externo e interno)
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean aproboEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarAproboEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
     /**
     * Método para verificar si existe evento de estadía activo para la actividad seleccionada
     * @param dtoSeguimientoEstadia
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarCarga(@NonNull DtoSeguimientoEstadia dtoSeguimientoEstadia){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Registro cedula evaluacion y acreditacion estadia","Director de carrera");
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
}
