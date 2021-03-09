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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEvaluacionEventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadiaDescripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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
           
            rol.setDeshabilitarRegistro(Boolean.TRUE);
            System.err.println("Init - deshabilitarRegistro " + rol.getDeshabilitarRegistro());
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante para realizar la búsqueda.");
            rol.getInstrucciones().add("Seleccionar de la lista el registro del estudiante que corresponda.");
            rol.getInstrucciones().add("De clic en el botón para registrar entrega de fotografías.");
            rol.getInstrucciones().add("Si se equivocó puede eliminar el registro en la tabla que se muestra en la parte inferior.");
           
            generacionesEventosRegistrados();
            
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
            verificarRegistro();
            listaEvaluacionesRegistradas();
            listaEvaluacionesEventoEstadia();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de evaluaciones registradas por evento de estadía 
     */
    public void verificarRegistro(){
        ResultadoEJB<EventoEstadia> res =  ejbAsignacionRolesEstadia.buscarEventoSeleccionado(rol.getGeneracion(), rol.getNivelEducativo(), "Registro cedula evaluacion empresarial");
        if(res.getCorrecto()){
            rol.setEventoEstadia(res.getValor());
            if(rol.getEventoEstadia()!= null)
            {
                System.err.println("EventoEstadia NotNull - deshabilitarRegistro " + rol.getDeshabilitarRegistro());
                ResultadoEJB<EvaluacionEstadia> resE =  ejb.buscarRegistroEvaluacionEstadia(rol.getEventoEstadia());
                if(resE.getCorrecto()){
                rol.setEvaluacionEstadia(resE.getValor());
                    if(rol.getEvaluacionEstadia() != null)
                    {
                        rol.setDeshabilitarRegistro(Boolean.TRUE);
                        System.err.println("EvaluacionEstadia NotNull - deshabilitarRegistro " + rol.getDeshabilitarRegistro());
                    }else{
                        rol.setDeshabilitarRegistro(Boolean.FALSE);
                        System.err.println("EvaluacionEstadia Null - deshabilitarRegistro " + rol.getDeshabilitarRegistro());
                    }
                }else mostrarMensajeResultadoEJB(resE);
            }
        }else mostrarMensajeResultadoEJB(res);
        System.err.println("verificarRegistro " + rol.getDeshabilitarRegistro());
        
        
        
    }
    
     /**
     * Permite obtener la lista de evaluaciones registradas por evento de estadía 
     */
    public void listaEvaluacionesRegistradas(){
        ResultadoEJB<List<EvaluacionEstadiaDescripcion>> res = ejb.getListaEvaluacionesEstadia();
        if(res.getCorrecto()){
            rol.setEvaluaciones(res.getValor());
            rol.setEvaluacion(rol.getEvaluaciones().get(0));
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de evaluaciones registradas por evento de estadía 
     */
    public void listaEvaluacionesEventoEstadia(){
        ResultadoEJB<List<DtoEvaluacionEventoEstadia>> res = ejb.getListaEvaluacionesEventoEstadia();
        if(res.getCorrecto()){
            rol.setEvaluacionesEvento(res.getValor());
            Ajax.update("tbListaEvaluacionesEvento");
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
            verificarRegistro();
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
            Ajax.update("frmEvalEst");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite asignar un estudiante al seguimiento de estadía del asesor académico
     */
    public void registrarEvaluacionEvento(){
        ResultadoEJB<EvaluacionEstadia> res = ejb.registrarEvaluacionEvento(rol.getEventoEstadia(), rol.getEvaluacion());
        mostrarMensajeResultadoEJB(res);
        verificarRegistro();
        listaEvaluacionesRegistradas();
        listaEvaluacionesEventoEstadia();
        Ajax.update("frmEvalEst");
    }
    
     /**
     * Permite eliminar asignación del estudiante
     * @param dtoEvaluacionEventoEstadia
     */
    public void eliminarAsignacion(DtoEvaluacionEventoEstadia dtoEvaluacionEventoEstadia){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarAsignacion(dtoEvaluacionEventoEstadia.getEvaluacionEstadia());
        mostrarMensajeResultadoEJB(resEliminar);
        verificarRegistro();
        listaEvaluacionesRegistradas();
        listaEvaluacionesEventoEstadia();
        Ajax.update("frmEvalEst");
    }

     /**
     * Permite actualizar la apertura extemporánea seleccionada
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
}
