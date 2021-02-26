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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroEventosEstadiaRolEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
public class RegistroEventosEstadiaEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroEventosEstadiaRolEscolares rol;
    
    @EJB EjbEstadiasServiciosEscolares ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EVENTOS_ESTADIA);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroEventosEstadiaRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setExisteRegistro(Boolean.FALSE);
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione generación y nivel educativo.");
            rol.getInstrucciones().add("A continuación visualizará la lista de eventos de estadía registrados o por registrar.");
            rol.getInstrucciones().add("Para modificar las fechas de los eventos registrados deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("Para registrar eventos de estadía nuevo deberá seleccionar en el calendarios la fecha de inicio y fin de cada actividad, al finalizar dar clic en el botón Guardar ubicado en la parte inferior de la tabla.");
            rol.getInstrucciones().add("Si registró por error eventos de estadía, dará clic en el botón eliminar.");
           
            generacionesRegistradas();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro eventos estadia";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones en los que existen eventos de estadía registrados
     */
    public void generacionesRegistradas(){
        ResultadoEJB<List<Generaciones>> res =  ejb.getGeneraciones();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesEducativos();
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            existeRegistro();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de nivele educativos registradas en la generación seleccionada
     */
    public void existeRegistro(){
        ResultadoEJB<Boolean> res = ejb.verificarExistenEventos(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setExisteRegistro(res.getValor());
            if(rol.getExisteRegistro()){
                listaEventosEstadiaRegistrados();
            }else{
                listaEventosEstadia();
            }
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de personal del área para asignar roles de estadía
     */
    public void listaEventosEstadiaRegistrados(){
        ResultadoEJB<List<DtoCalendarioEventosEstadia>> res = ejbSeguimientoEstadia.getCalendarioEventosEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setListaEventosRegistrados(res.getValor());
            Ajax.update("tbListaEventosEstadiaRegistrados");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de personal del área para asignar roles de estadía
     */
    public void listaEventosEstadia(){
        ResultadoEJB<List<DtoEventosEstadia>> res = ejb.getEventosEstadia();
        if(res.getCorrecto()){
            rol.setListaEventos(res.getValor());
            Ajax.update("tbListaEventosEstadia");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite que al cambiar o seleccionar una generación se pueda actualizar la lista de personal
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
     * Permite que al cambiar o seleccionar un nivel educativo se pueda actualizar la lista de personal
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof  ProgramasEducativosNiveles){
            ProgramasEducativosNiveles nivel = (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            existeRegistro();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite editar las fechas de inicio y fin de los eventos de estadía registrados previamente
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoCalendarioEventosEstadia evento = (DtoCalendarioEventosEstadia) dataTable.getRowData();
        ResultadoEJB<EventoEstadia> resAct = ejb.actualizarEventoRegistrado(evento);
        mostrarMensajeResultadoEJB(resAct);
        listaEventosEstadiaRegistrados();
    }
    
     /**
     * Permite guardar la lista de eventos de estadía de la generación y nivel educativo seleccionado
     */
    public void guardarEventosEstadia(){
        ResultadoEJB<List<DtoCalendarioEventosEstadia>> res = ejb.guardarEventosEstadia(rol.getGeneracion(), rol.getNivelEducativo(),rol.getListaEventos());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            existeRegistro();
        }else mostrarMensajeResultadoEJB(res);
        
    }
    
     /**
     * Permite guardar la lista de eventos de estadía de la generación y nivel educativo seleccionado
     */
    public void eliminarEventosEstadia(){
        ResultadoEJB<Integer> res = ejb.eliminarEventosEstadia(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            existeRegistro();
        }else mostrarMensajeResultadoEJB(res);
        
    }
}
