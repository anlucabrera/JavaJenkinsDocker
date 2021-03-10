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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.GestorEvaluacionesEstadiaRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEvaluacionEventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadiaDescripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;



/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class GestorEvaluacionesEstadiaEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter GestorEvaluacionesEstadiaRolEscolares rol;
    
    @EJB EjbEstadiasServiciosEscolares ejb;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbSeguimientoEstadia ejbSeguimientoEstadia;
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
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.GESTOR_EVALUACION_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new GestorEvaluacionesEstadiaRolEscolares(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setPestaniaActiva(0);
            rol.setRegistroEvaluacion(Boolean.FALSE);
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante para realizar la búsqueda.");
            rol.getInstrucciones().add("Seleccionar de la lista el registro del estudiante que corresponda.");
            rol.getInstrucciones().add("De clic en el botón para registrar entrega de fotografías.");
            rol.getInstrucciones().add("Si se equivocó puede eliminar el registro en la tabla que se muestra en la parte inferior.");
           
            generacionesEventosRegistrados();
            inicializarNumEvaluacion();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "gestor evaluaciones estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesEventosRegistrados(){
        ResultadoEJB<List<Generaciones>> res = ejbAsignacionRolesEstadia.getGeneracionesEventosRegistrados();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de niveles educativos con evento de estadía en la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaEvaluacionesRegistradas();
            listaEvaluacionesEventoEstadia();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite verificar si se puede realizar el registro de evaluación al evento de estadía seleccionado
     */
    public void permitirRegistro(){
        ResultadoEJB<EventoEstadia> res =  ejbAsignacionRolesEstadia.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Registro cedula evaluacion empresarial");
        if(res.getCorrecto()){
            rol.setEventoEstadia(res.getValor());
            if(rol.getEventoEstadia()!= null)
            {
                ResultadoEJB<EvaluacionEstadia> resE =  ejb.buscarRegistroEvaluacionEstadia(rol.getEventoEstadia());
                if(resE.getCorrecto()){
                rol.setEvaluacionEstadia(resE.getValor());
                }else mostrarMensajeResultadoEJB(resE);
            }
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite obtener la lista de evaluaciones registradas por evento de estadía 
     */
    public void listaEvaluacionesRegistradas(){
        ResultadoEJB<List<EvaluacionEstadiaDescripcion>> res = ejb.getListaEvaluacionesEstadia();
        if(res.getCorrecto()){
            rol.setEvaluaciones(res.getValor());
            rol.setEvaluacion(rol.getEvaluaciones().get(0));
            Ajax.update("frmEvalEst");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de evaluaciones registradas por evento de estadía 
     */
    public void listaEvaluacionesEventoEstadia(){
        ResultadoEJB<List<DtoEvaluacionEventoEstadia>> res = ejb.getListaEvaluacionesEventoEstadia();
        if(res.getCorrecto()){
            rol.setEvaluacionesEvento(res.getValor());
            Ajax.update("frmEvalEst");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualice la lista de niveles educativos
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frmEvalEst");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de evaluaciones registradas y la lista de evaluaciones  relacionadas con un evento de estadía
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaEvaluacionesRegistradas();
            listaEvaluacionesEventoEstadia();
            Ajax.update("frmEvalEst");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de evaluaciones registradas y la lista de evaluaciones  relacionadas con un evento de estadía
     * @param e Evento del cambio de valor
     */
    public void cambiarEvaluacion(ValueChangeEvent e){
        if(e.getNewValue() instanceof  EvaluacionEstadiaDescripcion){
            EvaluacionEstadiaDescripcion eval = (EvaluacionEstadiaDescripcion)e.getNewValue();
            rol.setEvaluacion(eval);

        }else mostrarMensaje("");
    }
    
     /**
     * Permite registrar una evaluación a un evento de estadía
     */
    public void registrarEvaluacionEvento(){
        permitirRegistro();
        if(rol.getEventoEstadia()!=null & rol.getEvaluacionEstadia() == null){
            ResultadoEJB<EvaluacionEstadia> res = ejb.registrarEvaluacionEvento(rol.getEventoEstadia(), rol.getEvaluacion());
            mostrarMensajeResultadoEJB(res);
            listaEvaluacionesRegistradas();
            listaEvaluacionesEventoEstadia();
        }else if(rol.getEventoEstadia()!=null & rol.getEvaluacionEstadia()!= null){
            Messages.addGlobalWarn("Ya se asignó una evaluación para el evento de estadía seleccionado.");
        }else if(rol.getEventoEstadia()== null){
            Messages.addGlobalWarn("No existe evento de estadía para la generación y nivel educativo seleccionado.");
        }
    }
    
     /**
     * Permite eliminar asignación de la evaluación al evento de estadía
     * @param dtoEvaluacionEventoEstadia
     */
    public void eliminarAsignacion(DtoEvaluacionEventoEstadia dtoEvaluacionEventoEstadia){
//           ResultadoEJB<List<CalificacionCriterioEstadia>> res =  ejb.buscarRegistroCalificacionEvaluacionEstadia(dtoEvaluacionEventoEstadia.getEvaluacionEstadia().getEvaluacion());
//            if(res.getCorrecto()){
//                rol.setListaCalificacionesEvaluacion(res.getValor());
//                System.err.println("permitirEliminacion - lista " + rol.getListaCalificacionesEvaluacion().size());
//                if(rol.getListaCalificacionesEvaluacion().isEmpty()){
                    ResultadoEJB<Integer> resEliminar = ejb.eliminarAsignacion(dtoEvaluacionEventoEstadia.getEvaluacionEstadia());
                    mostrarMensajeResultadoEJB(resEliminar);
                    listaEvaluacionesRegistradas();
                    listaEvaluacionesEventoEstadia();
//                }else{
//                    Messages.addGlobalWarn("No se puede eliminar la evaluación del evento de estadía ya que contiene calificaciones registradas.");
//                }
//            }else mostrarMensajeResultadoEJB(res);
    }

     /**
     * Permite actualizar la evaluación al evento de estadía
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoEvaluacionEventoEstadia evalNew = (DtoEvaluacionEventoEstadia) dataTable.getRowData();
        ResultadoEJB<EvaluacionEstadia> resActualizar = ejb.actualizarEvaluacionEvento(evalNew);
        mostrarMensajeResultadoEJB(resActualizar);
        listaEvaluacionesEventoEstadia();
        Ajax.update("frmEvalEst");
    }
    
    /**
     * Permite inicializar el número de evaluación que corresponderá
     */
    public void inicializarNumEvaluacion(){
        ResultadoEJB<Integer> res = ejb.getUltimoNumeroEvaluacionRegistrada();
        if(res.getCorrecto()){
            rol.setNumeroEvaluacion(res.getValor()+1);
            rol.setDescripcionEvaluacion("Ingresar descripción de la evaluación");
            rol.setAnioInicioEvaluacion(2021);
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite registrar una nueva evaluación de estadía
     */
    public void registrarEvaluacionEstadia(){
        ResultadoEJB<EvaluacionEstadiaDescripcion> res = ejb.registrarEvaluacionEstadia(rol.getNumeroEvaluacion(), rol.getDescripcionEvaluacion(), rol.getAnioInicioEvaluacion());
        if(res.getCorrecto()){
            rol.setEvaluacionRegistrada(res.getValor());
            rol.setPestaniaActiva(1);
            inicializarNumEvaluacion();
            listaPreguntasRegistrar();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite registrar una nueva evaluación de estadía
     */
    public void listaPreguntasRegistrar(){
        ResultadoEJB<List<String>> res = ejb.getPreguntasRegistrarEvaluacion();
        if(res.getCorrecto()){
            rol.setPreguntasRegistrarEvaluacion(res.getValor());
            rol.setRegistroEvaluacion(Boolean.TRUE);
            Ajax.update("contenederEval");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite registrar lista de preguntas a la evaluación de estadía
     */
    public void registrarPreguntasEvaluacion(){
        ResultadoEJB<List<CriterioEvaluacionEstadia>> res = ejb.registrarPreguntasEvaluacion(rol.getEvaluacionRegistrada(), rol.getPreguntasRegistrarEvaluacion());
        if(res.getCorrecto()){
            rol.setPreguntasRegistradas(res.getValor());
            rol.setPestaniaActiva(1);
        }else mostrarMensajeResultadoEJB(res);
    }
}
