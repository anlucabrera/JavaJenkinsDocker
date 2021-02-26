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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.SeguimientoEstadiaRolVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.event.CellEditEvent;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class SeguimientoEstadiaVinculacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter SeguimientoEstadiaRolVinculacion rol;
    
    @EJB EjbSeguimientoEstadia ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
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
@Getter private SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;

@PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.SEGUIMIENTO_ESTADIA_VINCULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarCoordinadorEstadia(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo coordinadorEstadia = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new SeguimientoEstadiaRolVinculacion(filtro, coordinadorEstadia);
            tieneAcceso = rol.tieneAcceso(coordinadorEstadia);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setCoordinadorEstadia(coordinadorEstadia);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.getInstrucciones().add("Seleccione periodo escolar para consultar bajas registradas durante ese periodo.");
            rol.getInstrucciones().add("Seleccione programa educativo.");
            rol.getInstrucciones().add("En la columna OPCIONES, usted puede: Validar o Invalidar baja, Consultar materias reprobadas, Generar formato de baja y Eliminar el registro.");
            rol.getInstrucciones().add("Dar clic en el botón de Validar/Invalidar baja, para que se cambie la situación académica en sistema.");
            rol.getInstrucciones().add("El botón de Consultar materias reprobadas se habilita únicamente en el caso de que la baja haya sido por reprobación.");
            rol.getInstrucciones().add("Para generar el formato de baja de clic en el botón Generar formato.");
            rol.getInstrucciones().add("Dar clic en el botón Eliminar baja, para eliminar el registro en caso de que se haya equivocado al realizar el trámite.");
           
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
        ResultadoEJB<List<AreasUniversidad>> res = ejbAsignacionRolesEstadia.getProgramasNivelesGeneracionEventosRegistrados(rol.getGeneracion(), rol.getNivelEducativo(), rol.getCoordinadorEstadia().getAreaSuperior().getArea());
        if(res.getCorrecto()){
            rol.setProgramasEducativos(res.getValor());
            rol.setProgramaEducativo(rol.getProgramasEducativos().get(0));
            listaEstudiantesSeguimiento();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado
     */
    public void listaEstudiantesSeguimiento(){
        ResultadoEJB<List<DtoSeguimientoEstadia>> res = ejb.getListaEstudiantesSeguimientoCoordinadorEstadias(rol.getGeneracion(), rol.getNivelEducativo(), rol.getProgramaEducativo());
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
            listaProgramasNivelGeneracion();
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
     * Método que permite validar o invalidar la información de estadía
     * @param dtoSeguimientoEstadia
     */
    public void validarEstadiaCoordinador(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        ResultadoEJB<SeguimientoEstadiaEstudiante> resValidar = ejb.validarInformacionEstadia(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), "coordinadorArea");
        mostrarMensajeResultadoEJB(resValidar);
        listaEstudiantesSeguimiento();
        Ajax.update("frm");
    }
    
     /**
     * Método que permite buscar los comentarios realizados al documento seleccionado
     * @param claveDocumento 
     * @param dtoSeguimientoEstadia 
     * @return Comentario del documento
     */
    public DocumentoSeguimientoEstadia comentariosDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        return ejb.consultarComentarioDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor();
    }
    
     /**
     * Método que permite validar o invalidar el documento del estudiante seleccionado
     * @param claveDocumento
     * @param dtoSeguimientoEstadia
     */
    public void validarDocumento(Integer claveDocumento, DtoSeguimientoEstadia dtoSeguimientoEstadia){
        ResultadoEJB<DocumentoSeguimientoEstadia> resValidar = ejb.validarDocumento(claveDocumento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante(), rol.getCoordinadorEstadia().getPersonal());
        mostrarMensajeResultadoEJB(resValidar);
        listaEstudiantesSeguimiento();
        Ajax.update("frm");
    }
    
     /**
     * Método que permite cancular el número de semanas entre la fecha de inicio y fin de la estadía
     * @param event Evento al seleccionar una fecha de inicio y/o fin
     */
    public void guardarComentarioDocumento(ValueChangeEvent event){
        String comentario = event.getNewValue().toString();
        String claveDocumento = (String) event.getComponent().getAttributes().get("documento");
        DtoSeguimientoEstadia dtoSeguimientoEstadia = (DtoSeguimientoEstadia) event.getComponent().getAttributes().get("seguimientoEstadia");
        Integer documento = Integer.parseInt(claveDocumento);
        ResultadoEJB<DocumentoSeguimientoEstadia> res = ejb.guardarComentarioDocumento(comentario, documento, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
        Messages.addGlobalInfo("Se ha registrado correctamente el comentario");
        Ajax.update("frm");
        
    }
    
    /**
     * Método para verificar si existe evento de estadía activo para la actividad de evaluación/validación seleccionada
     * @param dtoSeguimientoEstadia
     * @param valor
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarValidacion(@NonNull DtoSeguimientoEstadia dtoSeguimientoEstadia, Integer valor){
        Boolean permiso = Boolean.FALSE;
        
        switch (valor) {
                case 1:
                    ResultadoEJB<Boolean> res1 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion carta presentacion", "Coordinador institucional");
                    if (res1.getCorrecto()) {
                        permiso = res1.getValor();
                    } else { mostrarMensajeResultadoEJB(res1);}
                    break;
                case 2:
                    ResultadoEJB<Boolean> res2 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion carta aceptacion", "Coordinador institucional");
                    if (res2.getCorrecto()) {
                        permiso = res2.getValor();
                    } else { mostrarMensajeResultadoEJB(res2);}
                    break;
                case 3:
                    ResultadoEJB<Boolean> res3 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion carta liberacion", "Coordinador institucional");
                    if (res3.getCorrecto()) {
                        permiso = res3.getValor();
                    } else { mostrarMensajeResultadoEJB(res3);}
                    break;
                case 4:
                    ResultadoEJB<Boolean> res4 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion cedula evaluacion empresarial", "Coordinador institucional");
                    if (res4.getCorrecto()) {
                        permiso = res4.getValor();
                    } else { mostrarMensajeResultadoEJB(res4);}
                    break;
                case 5:
                    ResultadoEJB<Boolean> res5 = ejb.buscarEventoActivo(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento(), "Evaluacion cedula evaluacion y acreditacion estadia", "Coordinador institucional");
                    if (res5.getCorrecto()) {
                        permiso = res5.getValor();
                    } else { mostrarMensajeResultadoEJB(res5);}
                    break;
                default:

                    break;
            }
        return permiso;
    }
   
}
