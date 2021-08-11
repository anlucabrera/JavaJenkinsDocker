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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEventosTitulacionRolTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosTitulacion;
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
import java.util.Date;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroEventosTitulacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroEventosTitulacionRolTitulacion rol;
    
    @EJB EjbRegistroEventosTitulacion ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVENTOS_TITULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTitulacion(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEventosTitulacionRolTitulacion(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("AGREGAR NUEVO EVENTO.");
            rol.getInstrucciones().add("Seleccione la generación y nivel educativo que correspondan, a continuación seleccione la fecha de inicio y fin en la que estará activo el evento de integración de expediente de titulación");
            rol.getInstrucciones().add("De clic en el botón de GUARDAR para registrar el evento, posteriormente la tabla se actualizará con la nueva información.");
            rol.getInstrucciones().add("ACTUALIZAR FECHAS - EVENTO YA REGISTRADO");
            rol.getInstrucciones().add("Ubique la fila del evento que corresponda, de clic en los campos de fecha de iniciop y fin, a continuación podrá seleccionar la nueca fecha.");
            rol.getInstrucciones().add("De enter, posreriormente la tabla se actualizará con las nuevas fechas.");
            
            listaEventosRegistradosCE();
            listaEventosRegistradosSAIIUT();
            listaGeneraciones();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro eventos titulacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de eventos de titulación registrados de generaciones en control escolar
     */
    public void listaEventosRegistradosCE(){
        ResultadoEJB<List<DtoEventosTitulacion.GeneracionesControlEscolar>> res = ejb.getEventosTitulacionControlEscolar();
        if(res.getCorrecto()){
            rol.setListEventosTitulacionCE(res.getValor());
            Ajax.update("tbListaEventosTitulacionCE");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de eventos de titulación registrados de generaciones en saiiut
     */
    public void listaEventosRegistradosSAIIUT(){
        ResultadoEJB<List<DtoEventosTitulacion.GeneracionesSAIIUT>> res = ejb.getEventosTitulacionSAIIUT();
        if(res.getCorrecto()){
            rol.setListEventosTitulacionSAIIUT(res.getValor());
            Ajax.update("tbListaEventosTitulacionSAIIUT");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite editar la fechas de inicio y fin de un evento de titulación que ya se encuentra registrado
     * @param event Evento de edición de la celda
     */
    public void onCellEditCE(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoEventosTitulacion.GeneracionesControlEscolar evento = (DtoEventosTitulacion.GeneracionesControlEscolar) dataTable.getRowData();
        ResultadoEJB<EventoTitulacion> resAct = ejb.actualizarEventoRegistradoCE(evento);
        mostrarMensajeResultadoEJB(resAct);
        listaEventosRegistradosCE();
    }
    
     /**
     * Permite editar la fechas de inicio y fin de un evento de titulación que ya se encuentra registrado
     * @param event Evento de edición de la celda
     */
    public void onCellEditSAIIUT(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoEventosTitulacion.GeneracionesSAIIUT evento = (DtoEventosTitulacion.GeneracionesSAIIUT) dataTable.getRowData();
        ResultadoEJB<ProcesosIntexp> resAct = ejb.actualizarEventoRegistradoSAIIUT(evento);
        mostrarMensajeResultadoEJB(resAct);
        listaEventosRegistradosSAIIUT();
    }
    
     /**
     * Permite obtener la lista de generaciones activas en control escolar
     */
    public void listaGeneraciones(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneraciones();
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
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesGeneracion(rol.getGeneracion());
        if(res.getCorrecto()){
            rol.setNiveles(res.getValor());
            rol.setNivel(rol.getNiveles().get(0));
            inicializarFechas();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite inicializar ls fechas de inciio y fin
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
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite registrar un evento escolar en el periodo escolar seleccionado
     */
    public void agregarNuevoEvento(){
        ResultadoEJB<EventoTitulacion> agregar = ejb.agregarEventoTitulacion(rol.getGeneracion(), rol.getNivel(), rol.getFechaInicio(), rol.getFechaFin());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaEventosRegistradosCE();
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite eliminar el evento de titulación seleccionado
     * @param dtoGeneracionesControlEscolar
    */
    public void eliminarEventoTitulacion(DtoEventosTitulacion.GeneracionesControlEscolar dtoGeneracionesControlEscolar){
        ResultadoEJB<Integer> res = ejb.eliminarEventoTitulacion(dtoGeneracionesControlEscolar.getEventoTitulacion());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            listaEventosRegistradosCE();
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite verificar si se habilita la eliminación el evento de titulación seleccionado
     * @param dtoGeneracionesControlEscolar
     * @return Resultado del proceso, true o false
    */
    public Boolean deshabilitarEliminacion(DtoEventosTitulacion.GeneracionesControlEscolar dtoGeneracionesControlEscolar){
        ResultadoEJB<Boolean> res = ejb.getExistenExpedientesEvento(dtoGeneracionesControlEscolar.getEventoTitulacion());
        return res.getValor();
    }
    
}
