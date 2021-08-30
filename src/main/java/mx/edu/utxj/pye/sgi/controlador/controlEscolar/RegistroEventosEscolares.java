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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEventosEscolaresRolEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Date;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroEventosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroEventosEscolaresRolEscolares rol;
    
    @EJB EjbRegistroEventosEscolares ejb;
    @EJB EjbEstadiasServiciosEscolares ejbEstadiasServiciosEscolares;
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVENTOS_ESCOLARES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbEstadiasServiciosEscolares.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEventosEscolaresRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
            rol.setExisteRegistro(Boolean.FALSE);
            rol.setAgregarEvento(Boolean.FALSE);
            rol.setDeshabilitarEliminar(Boolean.TRUE);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione periodo escolar.");
            rol.getInstrucciones().add("A continuación visualizará la lista de eventos escolares registrados o por registrar.");
            rol.getInstrucciones().add("ACTUALIZACIÓN FECHAS - EVENTO YA REGISTRADO.");
            rol.getInstrucciones().add("Para modificar las fechas de los eventos registrados deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("AGREGAR NUEVO EVENTO.");
            rol.getInstrucciones().add("De clic en el componente AGREGAR EVENTO, a continuación deberá seleccionar el evento, las fechas de inicio y fin en que estará habilitado, dará clic en GUARDAR para registrar el evento.");
            rol.getInstrucciones().add("ELIMINAR EVENTO.");
            rol.getInstrucciones().add("Ubique la fila que corresponda al evento que desea eliminar, dará clic en el icono de la columna ELIMINAR.");
            rol.getInstrucciones().add("NOTA: No se pueden eliminar eventos del periodo activo o anteriores.");
            
            periodosEscolaresRegistrados();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro eventos escolares";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void periodosEscolaresRegistrados(){
        ResultadoEJB<List<PeriodosEscolares>> res =  ejb.getPeriodosEscolares();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodosEscolares(res.getValor());
                rol.setPeriodoEscolar(ejb.getUltimoPeriodoRegistrado().getValor());
                verificarPermisoEliminar();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite verificar si se habilita o no el permiso para eliminar información
     */
    public void verificarPermisoEliminar(){
        if (rol.getPeriodoEscolar().getPeriodo()>rol.getPeriodoActivo()) {
            rol.setDeshabilitarEliminar(Boolean.FALSE);
        }else{ 
            rol.setDeshabilitarEliminar(Boolean.TRUE);
        }
            Ajax.update("frm");
            existeRegistro();
    }
    
    /**
     * Permite verificar si existe información registrada para el periodo escolar seleccionado
     */
    public void existeRegistro(){
        ResultadoEJB<Boolean> res = ejb.verificarExistenEventos(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setExisteRegistro(res.getValor());
            if(rol.getExisteRegistro()){
                listaEventosEscolaresRegistrados();
            }else{
                listaEventosEscolares();
            }
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaEventosEscolaresRegistrados(){
        ResultadoEJB<List<DtoCalendarioEventosEscolares>> res = ejb.getCalendarioEventosEscolares(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setListaEventosRegistrados(res.getValor());
            listaOpcionesEventos();
            Ajax.update("tbListaEventosEscolaresRegistrados");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de eventos escolares para registrar en el periodo escolar seleccionado
     */
    public void listaEventosEscolares(){
        ResultadoEJB<List<DtoEventosEscolares>> res = ejb.getEventosEscolares(rol.getPeriodoEscolar(), rol.getUsuario().getPersonal());
        if(res.getCorrecto()){
            rol.setListaEventos(res.getValor());
            Ajax.update("tbListaEventosEscolares");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de opciones de eventos escolares
     */
    public void listaOpcionesEventos(){
        ResultadoEJB<List<String>> res = ejb.getListaOpcionesEventosEscolares(rol.getListaEventosRegistrados());
        if(res.getCorrecto()){
            rol.setListaOpcionesEventos(res.getValor());
            rol.setOpcionEvento(rol.getListaOpcionesEventos().get(0));
            rol.setFechaInicio(new Date());
            rol.setFechaFin(new Date());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodoEscolar(periodo);
            verificarPermisoEliminar();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del input para agregar un evento y se muestren los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarEvento(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarEvento(valor);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un evento escolar se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarEvento(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String evento = (String)e.getNewValue();
            rol.setOpcionEvento(evento);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    
     /**
     * Permite editar la fechas de inicio y fin de un evento escolar que ya se encuentra registrado
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoCalendarioEventosEscolares evento = (DtoCalendarioEventosEscolares) dataTable.getRowData();
        ResultadoEJB<EventoEscolar> resAct = ejb.actualizarEventoRegistrado(evento);
        mostrarMensajeResultadoEJB(resAct);
    }
    
     /**
     * Permite guardar la lista de eventos escolares para el periodo escolar seleccionado
     */
    public void guardarEventosEscolares(){
        ResultadoEJB<List<DtoCalendarioEventosEscolares>> res = ejb.guardarEventosEscolares(rol.getPeriodoEscolar(), rol.getListaEventos(), rol.getUsuario().getPersonal());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            verificarPermisoEliminar();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite eliminar la lista de eventos escolares del periodo escolar seleccionado
     */
    public void eliminarEventosEscolares(){
        ResultadoEJB<Integer> res = ejb.eliminarEventosEscolares(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            verificarPermisoEliminar();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite registrar un evento escolar en el periodo escolar seleccionado
     */
    public void agregarNuevoEvento(){
        ResultadoEJB<EventoEscolar> agregar = ejb.agregarEventoEscolar(rol.getPeriodoEscolar(), rol.getOpcionEvento(), rol.getFechaInicio(), rol.getFechaFin(), rol.getUsuario().getPersonal());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaEventosEscolaresRegistrados();
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite eliminar el evento escolar del periodo seleccionado
     * @param dtoCalendarioEventosEscolares
    */
    public void eliminarEventoEscolar(DtoCalendarioEventosEscolares dtoCalendarioEventosEscolares){
        ResultadoEJB<Integer> res = ejb.eliminarEventoEscolar(dtoCalendarioEventosEscolares.getEventoEscolar());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            listaEventosEscolaresRegistrados();
        }else mostrarMensajeResultadoEJB(res);
        
    }
}
