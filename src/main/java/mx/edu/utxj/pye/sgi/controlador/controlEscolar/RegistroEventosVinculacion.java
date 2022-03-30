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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEventosVinculacionRolCoordEstadias;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosVinculacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroEventosVinculacion extends ViewScopedRol implements Desarrollable{
     @Getter @Setter RegistroEventosVinculacionRolCoordEstadias rol;
    
    @EJB EjbRegistroEventosVinculacion ejb;
    @EJB EjbRegistroEventosTitulacion ejbRegistroEventosTitulacion;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol personal del departamento de servicios escolares<br/>
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVENTOS_VINCULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarCoordinacionEstadia(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEventosVinculacionRolCoordEstadias(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("AGREGAR NUEVO EVENTO.");
            rol.getInstrucciones().add("Seleccione la generación y nivel educativo que correspondan, a continuación seleccione la fecha de inicio y fin en la que estará activo el evento de integración de expediente de vinculación.");
            rol.getInstrucciones().add("De clic en el botón de GUARDAR para registrar el evento, posteriormente la tabla se actualizará con la nueva información.");
            rol.getInstrucciones().add("ACTUALIZAR FECHAS - EVENTO YA REGISTRADO");
            rol.getInstrucciones().add("Ubique la fila del evento que corresponda, de clic en los campos de fecha de inicio y fin, a continuación podrá seleccionar la nueva fecha.");
            rol.getInstrucciones().add("De enter, posteriormente la tabla se actualizará con las nuevas fechas.");
            rol.getInstrucciones().add("ELIMINAR EVENTO.");
            rol.getInstrucciones().add("Ubique la fila que corresponda al evento que desea eliminar, dará clic en el icono de la columna ELIMINAR.");
            rol.getInstrucciones().add("NOTA: No se pueden eliminar eventos que tengan expedientes de vinculación registrados.");
            
            listaGeneraciones();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro eventos vinculación";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de eventos de vinculación registrados
     */
    public void listaEventosRegistrados(){
        ResultadoEJB<List<DtoEventosVinculacion>> res = ejb.getEventosVinculacion();
        if(res.getCorrecto()){
            rol.setListEventosVinculacion(res.getValor());
            Ajax.update("tbListaEventosVinculacion");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    
     /**
     * Permite editar la fechas de inicio y fin de un evento de vinculación que ya se encuentra registrado
     * @param event Evento de edición de la celda
     */
    public void onCellEditCE(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoEventosVinculacion evento = (DtoEventosVinculacion) dataTable.getRowData();
        ResultadoEJB<EventoVinculacion> resAct = ejb.actualizarEventoRegistrado(evento);
        mostrarMensajeResultadoEJB(resAct);
        listaEventosRegistrados();
    }
    
     /**
     * Permite obtener la lista de generaciones activas en control escolar
     */
    public void listaGeneraciones(){
        ResultadoEJB<List<Generaciones>> res = ejbRegistroEventosTitulacion.getGeneraciones();
        if(res.getCorrecto()){
            rol.setGeneraciones(res.getValor());
            rol.setGeneracion(rol.getGeneraciones().get(0));
            listaNiveles();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de niveles educativos activos de la generación seleccionada
     */
    public void listaNiveles(){
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejbRegistroEventosTitulacion.getNivelesGeneracion(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNiveles(res.getValor());
            rol.setNivel(rol.getNiveles().get(0));
            listaEventosRegistrados();
            listaActividades();
            inicializarFechas();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Obtiene la lista de actividades
     */
    public void listaActividades(){
        ResultadoEJB<List<String>> res = ejb.getListaActividadesDisponibles(rol.getGeneracion(), rol.getNivel());
        if(res.getCorrecto()){
            rol.setActividades(res.getValor());
            if(rol.getActividades().isEmpty()){
                rol.setActividades(Collections.emptyList());
                rol.setActividad("Ya se han registrado las actividades correspondientes");
            }else{
                rol.setActividad(rol.getActividades().get(0));
            }
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res); 
    }
    
    /**
     * Permite inicializar las fechas de inicio y fin
     */
    public void inicializarFechas(){
       rol.setFechaInicio(new Date());
       rol.setFechaFin(new Date());
       Ajax.update("frm");
    
    }
    
     /**
     * Permite que al cambiar o seleccionar la generación se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones gen = (Generaciones)e.getNewValue();
            rol.setGeneracion(gen);
            listaNiveles();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que al cambiar o seleccionar el nivel educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarNivel(ValueChangeEvent e){
        if(e.getNewValue() instanceof ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivel(nivel);
            listaActividades();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que al cambiar o seleccionar la actividad se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarActividad(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String actividad = (String)e.getNewValue();
            rol.setActividad(actividad);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite registrar un evento escolar en el periodo escolar seleccionado
     */
    public void agregarNuevoEvento(){
        ResultadoEJB<EventoVinculacion> agregar = ejb.agregarEventoVinculacion(rol.getGeneracion(), rol.getNivel(), rol.getActividad() ,rol.getFechaInicio(), rol.getFechaFin());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaEventosRegistrados();
            listaActividades();
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite eliminar el evento de vinculación seleccionado
     * @param dtoEventosVinculacion
    */
    public void eliminarEventoVinculacion(DtoEventosVinculacion dtoEventosVinculacion){
        ResultadoEJB<Integer> res = ejb.eliminarEventoVinculacion(dtoEventosVinculacion.getEventoVinculacion());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            listaEventosRegistrados();
            listaActividades();
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite verificar si se habilita la eliminación el evento de vinculación seleccionado
     * @param dtoEventosVinculacion
     * @return Resultado del proceso, true o false
    */
    public Boolean deshabilitarEliminacion(DtoEventosVinculacion dtoEventosVinculacion){
        ResultadoEJB<Boolean> res = ejb.getExistenDocumentosEvento(dtoEventosVinculacion.getEventoVinculacion());
        return res.getValor();
    }
    
}
