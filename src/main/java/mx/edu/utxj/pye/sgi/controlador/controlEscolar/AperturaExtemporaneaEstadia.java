/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
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
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAperturaExtemporaneaEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AperturaExtemporaneaEstadiaRolMultiple;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAperturaExtemporaneaEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionRolesEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AperturaExtemporaneaEstadia extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AperturaExtemporaneaEstadiaRolMultiple rol;
    
    @EJB EjbAperturaExtemporaneaEstadia ejb;
    @EJB EjbEstadiasServiciosEscolares ejbEstadiasServiciosEscolares;
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
            setVistaControlador(ControlEscolarVistaControlador.APERTURA_EXTEMPORANEA_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarAcceso(logon.getPersonal().getClave());//validar si es director

            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AperturaExtemporaneaEstadiaRolMultiple(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
//            System.out.println("tieneAcceso2 = " + tieneAcceso);
            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setDesactivarRegistro(Boolean.TRUE);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Ingrese la matricula o el nombre del estudiante para realizar la búsqueda.");
            rol.getInstrucciones().add("Seleccionar de la lista el registro del estudiante que corresponda.");
            rol.getInstrucciones().add("Selecciona la actividad del evento de estadía que desea activar.");
            rol.getInstrucciones().add("Seleccione las fechas de inicio y fin en que estará activa la actividad.");
            rol.getInstrucciones().add("Si se equivocó puede eliminar el registro en la columna opciones.");
           
            generacionesEventosRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "apertura extemporanea estadia";
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
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbAsignacionRolesEstadia.getNivelesGeneracionEventosRegistrados(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaActividadesApertura();
            listaAperturasExtemporaneas();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de bajas registradas en el periodo seleccionado
     */
    public void listaActividadesApertura(){
        ResultadoEJB<List<EventoEstadia>> res = ejb.getActividadesEventoEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setActividades(res.getValor());
            rol.setActividad(rol.getActividades().get(0));
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        if(rol.getUsuario().getAreaOperativa().getArea()==10){
            ResultadoEJB<List<DtoEstudianteComplete>> res = ejbEstadiasServiciosEscolares.buscarEstudiante(rol.getGeneracion(), rol.getNivelEducativo(),pista);
            if(res.getCorrecto()){
                return res.getValor();
            }else{
                mostrarMensajeResultadoEJB(res);
                return Collections.emptyList();
            }
        }else{
            ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudianteAreaAcademica(rol.getGeneracion(), rol.getNivelEducativo(), rol.getUsuario().getAreaOficial(), pista);
            if(res.getCorrecto()){
                return res.getValor();
            }else{
                mostrarMensajeResultadoEJB(res);
                return Collections.emptyList();
            }
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un estudiante se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)e.getNewValue();
            rol.setEstudianteSeleccionado(estudiante);
            buscarDatosEstudiante(rol.getEstudianteSeleccionado().getEstudiantes().getIdEstudiante());
            Ajax.update("frm");
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
    
    public void buscarDatosEstudiante(Integer claveEstudiante){
        ResultadoEJB<DtoDatosEstudiante> res = ejbAsignacionRolesEstadia.buscarDatosEstudiante(claveEstudiante);
        if(res.getCorrecto()){
        rol.setEstudianteRegistrado(res.getValor());
        existeSeguimiento(rol.getEstudianteRegistrado());
        Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor academico y evento seleccionado
     */
    public void listaAperturasExtemporaneas(){
        if(rol.getUsuario().getAreaOperativa().getArea()==10){
            ResultadoEJB<List<DtoAperturaExtemporaneaEstadia>> res = ejb.getListaAperturasExtemporaneas(rol.getGeneracion(), rol.getNivelEducativo());
            if(res.getCorrecto()){
             rol.setListaAperturasExtemporaneas(res.getValor());
             Ajax.update("tbListaAperturasExtemporaneas");
            }else mostrarMensajeResultadoEJB(res);
        }else{
            ResultadoEJB<List<DtoAperturaExtemporaneaEstadia>> res = ejb.getListaAperturasExtemporaneasAreaAcademica(rol.getGeneracion(), rol.getNivelEducativo(), rol.getUsuario().getAreaOficial());
            if(res.getCorrecto()){
             rol.setListaAperturasExtemporaneas(res.getValor());
             Ajax.update("tbListaAperturasExtemporaneas");
            }else mostrarMensajeResultadoEJB(res);
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se pueda actualizar la lista de bajas del periodo
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
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de bajas por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            listaAperturasExtemporaneas();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un programa educativo se pueda actualizar la lista de bajas por programa educativo
     * @param e Evento del cambio de valor
     */
    public void cambiarActividadEvento(ValueChangeEvent e){
        if(e.getNewValue() instanceof  EventoEstadia){
            EventoEstadia actividad = (EventoEstadia)e.getNewValue();
            rol.setActividad(actividad);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite guardar apertura extemporánea
     */
    public void guardarAperturaExtemporanea(){
        ResultadoEJB<AperturaExtemporaneaEventoEstadia> resGuardar = ejb.guardarAperturaExtemporanea(rol.getActividad(), rol.getEstudianteRegistrado(), rol.getFechaInicio(), rol.getFechaFin(), rol.getUsuario().getPersonal());
        mostrarMensajeResultadoEJB(resGuardar);
        listaAperturasExtemporaneas();
        listaAperturasRegistradasEvento();
        rol.setEstudianteSeleccionado(null);
    }
    
     /**
     * Permite eliminar apertura extemporánea seleccionada
     * @param dtoAperturaExtemporaneaEstadia
     */
    public void eliminarApertura(DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia){
        ResultadoEJB<Integer> resEliminar = ejb.eliminarAperturaExtemporanea(dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea());
        mostrarMensajeResultadoEJB(resEliminar);
        listaAperturasExtemporaneas();
    }
    
    /**
     * Permite actualizar la apertura extemporánea seleccionada
     * @param event
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoAperturaExtemporaneaEstadia permisoNew = (DtoAperturaExtemporaneaEstadia) dataTable.getRowData();
        ResultadoEJB<AperturaExtemporaneaEventoEstadia> resActualizar = ejb.actualizarAperturaExtemporanea(permisoNew);
        mostrarMensajeResultadoEJB(resActualizar);
        listaAperturasExtemporaneas();
        Ajax.update("frm");
    }
    
      /**
     * Permite validar o invalidar la apertura extemporánea
     * @param dtoAperturaExtemporaneaEstadia 
     */
    public void validarApertura(DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia){
        ResultadoEJB<Integer> resValidar = ejb.validarAperturaExtemporanea(dtoAperturaExtemporaneaEstadia, rol.getUsuario().getPersonal());
        mostrarMensajeResultadoEJB(resValidar);
        listaAperturasExtemporaneas();
        Ajax.update("frm");
    }
    
     /**
     * Permite verificar si el estudiante tiene seguimiento de estadía registrado
     * @param estudiante
     */
    public void existeSeguimiento(DtoDatosEstudiante estudiante){
        ResultadoEJB<DtoSeguimientoEstadiaEstudiante> res = ejbSeguimientoEstadia.getSeguimientoEstudiante(rol.getGeneracion(), rol.getNivelEducativo(), estudiante.getEstudiante().getMatricula());
        if(res.getValor() != null){
            rol.setDesactivarRegistro(Boolean.FALSE);
        }else{
            rol.setDesactivarRegistro(Boolean.TRUE);
            Messages.addGlobalWarn("El estudiante no tiene seguimiento de estadía registrado, por eso no se puede aperturar evento");
        }
    }
    
     /**
     * Permite obtener lista de aperturas registradas de la actividad y estudiante seleccionado
     */
    public void listaAperturasRegistradasEvento(){
        ResultadoEJB<Integer> res = ejb.buscarAperturasRegistradasEvento(rol.getActividad(), rol.getEstudianteRegistrado());
        if(res.getCorrecto()){
             rol.setNumeroAperturasRegistradas(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
        if(rol.getNumeroAperturasRegistradas()>1){
        Messages.addGlobalWarn("El estudiante tiene " + rol.getNumeroAperturasRegistradas() + " aperturas registradas para la misma actividad");}
    }
    
     /**
     * Permite eliminar apertura extemporánea seleccionada
     * @param dtoAperturaExtemporaneaEstadia
     * @return 
     */
    public Boolean desactivarEliminar(DtoAperturaExtemporaneaEstadia dtoAperturaExtemporaneaEstadia){
        Boolean valor = Boolean.TRUE;
        if("Dirección de carrera".equals(dtoAperturaExtemporaneaEstadia.getAperturaExtemporanea().getTipoApertura()) && rol.getUsuario().getPersonal().getAreaOperativa()!= 10)
        {
            valor = Boolean.FALSE;
        }else if(rol.getUsuario().getPersonal().getAreaOperativa()== 10){
            valor = Boolean.FALSE;
        }
        return valor;
    }
    
}

