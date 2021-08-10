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
            
            rol.getInstrucciones().add("Seleccione periodo escolar.");
            rol.getInstrucciones().add("A continuación visualizará la lista de eventos escolares registrados o por registrar.");
            rol.getInstrucciones().add("Para modificar las fechas de los eventos registrados deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("Para registrar un evento escolar nuevo deberá seleccionar en el calendario la fecha de inicio y fin de cada actividad, al finalizar dar clic en el botón Guardar ubicado en la parte inferior de la tabla.");
            rol.getInstrucciones().add("Si registró por error eventos de estadía, dará clic en el botón eliminar.");
           
            listaEventosRegistradosCE();
            listaEventosRegistradosSAIIUT();
            
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
    
}
