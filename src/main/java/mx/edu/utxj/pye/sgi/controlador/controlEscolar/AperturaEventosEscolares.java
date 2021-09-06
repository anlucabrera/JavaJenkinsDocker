/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAperturaEventosEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AperturaEventosEscolaresRolEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbEstadiasServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolarDetalle;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AperturaEventosEscolares extends ViewScopedRol implements Desarrollable{
     @Getter @Setter AperturaEventosEscolaresRolEscolares rol;
    
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
            setVistaControlador(ControlEscolarVistaControlador.APERTURA_EVENTOS_ESCOLARES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbEstadiasServiciosEscolares.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es director
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AperturaEventosEscolaresRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbPermisoAperturaExtemporanea.getPeriodoActual().getPeriodo());
            rol.setAgregarApertura(Boolean.FALSE);
            rol.setTipoBusqueda("busquedaGeneral");
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione periodo escolar.");
            rol.getInstrucciones().add("A continuación visualizará la lista de aperturas registradas en el periodo seleccionado.");
            rol.getInstrucciones().add("ACTUALIZACIÓN FECHAS - APERTURA REGISTRADA.");
            rol.getInstrucciones().add("Para modificar las fechas de la apertura deberá dar clic en el campo de fecha de inicio o fin, seleccionar la fecha del calendario y dar enter para guardar cambios.");
            rol.getInstrucciones().add("AGREGAR NUEVA APERTURA.");
            rol.getInstrucciones().add("De clic en el componente AGREGAR APERTURA, a continuación deberá indicar si será una apertura por área o personal, indicar el área o la clave del personal, seleccionar el evento, las fechas de inicio y fin en que estará habilitado, dará clic en GUARDAR para registrar la apertura.");
            rol.getInstrucciones().add("ELIMINAR APERTURA.");
            rol.getInstrucciones().add("Ubique la fila que corresponda a la apertura que desea eliminar, dará clic en el icono de la columna ELIMINAR.");
           
            periodosEscolares();
            listaAreas();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "apertura eventos escolares";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de periodos escolares en los que existen eventos escolares registrados
     */
    public void periodosEscolares(){
        ResultadoEJB<List<PeriodosEscolares>> res =  ejb.getPeriodosEscolares();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setPeriodosEscolares(res.getValor());
                rol.setPeriodoEscolar(rol.getPeriodosEscolares().get(0));
                listaAperturasEventosEscolaresRegistrados();
                listaEventos();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaAperturasEventosEscolaresRegistrados(){
        ResultadoEJB<List<DtoAperturaEventosEscolares>> res = ejb.getAperturasEventosEscolares(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setListaAperturasEventosEscolares(res.getValor());
            Ajax.update("tbAperturaEventos");
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaAreas(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getAreasDepartamentos();
        if(res.getCorrecto()){
            rol.setAreas(res.getValor());
            rol.setArea(rol.getAreas().get(0));
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void listaEventos(){
        ResultadoEJB<List<DtoCalendarioEventosEscolares>> res = ejb.getCalendarioEventosEscolares(rol.getPeriodoEscolar());
        if(res.getCorrecto()){
            rol.setEventos(res.getValor().stream().map(p->p.getEventoEscolar()).collect(Collectors.toList()));
            rol.setEvento(rol.getEventos().get(0));
            fechaMinimaApertura();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     */
    public void fechaMinimaApertura(){
        ResultadoEJB<Date> res = ejb.getFechaMinimaApertura(rol.getEvento());
        if(res.getCorrecto()){
            rol.setRangoFechaInicial(res.getValor());
            rol.setFechaInicio(rol.getRangoFechaInicial());
            rol.setFechaFin(rol.getRangoFechaInicial());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<PersonalActivo> completePersonal(String pista){
        ResultadoEJB<List<PersonalActivo>> res = ejbPermisoAperturaExtemporanea.buscarDocente(pista);
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un periodo escolar se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarPeriodo(ValueChangeEvent e){
        if(e.getNewValue() instanceof PeriodosEscolares){
            PeriodosEscolares periodo = (PeriodosEscolares)e.getNewValue();
            rol.setPeriodoEscolar(periodo);
            listaAperturasEventosEscolaresRegistrados();
            listaEventos();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del input para agregar una apertura y se muestren los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarApertura(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarApertura(valor);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite cambiar o seleccionar un tipo de búsqueda se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarTipoBusqueda(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String valor = (String)e.getNewValue();
            rol.setTipoBusqueda(valor);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite cambiar o seleccionar un área se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarArea(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            AreasUniversidad area = (AreasUniversidad)e.getNewValue();
            rol.setArea(area);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite cambiar o seleccionar un evento escolar se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarEvento(ValueChangeEvent e){
        if(e.getNewValue() instanceof EventoEscolar){
            EventoEscolar evento = (EventoEscolar)e.getNewValue();
            rol.setEvento(evento);
            rol.setFechaInicio(null);
            rol.setFechaFin(null);
            fechaMinimaApertura();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar un docente se puedan actualizar las materias asignadas a este docente
     * @param e Evento del cambio de valor
     */
    public void cambiarPersonal(ValueChangeEvent e){
        if(e.getNewValue() instanceof PersonalActivo){
            PersonalActivo docente = (PersonalActivo)e.getNewValue();
            rol.setPersonal(docente);
        }else mostrarMensaje("El valor seleccionado como personal no es del tipo necesario.");
    }
    
     /**
     * Permite guardar una apertura extemporánea por área o departamento
     */
    public void guardarAperturaArea(){
        ResultadoEJB<EventoEscolarDetalle> agregar = ejb.guardarAperturaArea(rol.getEvento(), rol.getArea(), rol.getFechaInicio(), rol.getFechaFin());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaAperturasEventosEscolaresRegistrados();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite guardar una apertura extemporánea por personal
     */
    public void guardarAperturaPersonal(){
        ResultadoEJB<EventoEscolarDetalle> agregar = ejb.guardarAperturaPersonal(rol.getEvento(), rol.getPersonal().getPersonal(), rol.getFechaInicio(), rol.getFechaFin());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaAperturasEventosEscolaresRegistrados();
            Ajax.update("tbAperturaEventos");
        }
    }
    
    /**
     * Permite eliminar la apertura extemporánea seleccionada
     * @param dtoAperturaEventosEscolares
    */
    public void eliminarAperturaEventoEscolar(DtoAperturaEventosEscolares dtoAperturaEventosEscolares){
        ResultadoEJB<Integer> res = ejb.eliminarAperturaEventoEscolar(dtoAperturaEventosEscolares.getEventoEscolarDetalle());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            listaAperturasEventosEscolaresRegistrados();
            Ajax.update("tbAperturaEventos");
        }else mostrarMensajeResultadoEJB(res);
        
    }
 
    
     /**
     * Permite editar la fechas de inicio y fin de la apertura extemporánea seleccionada
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoAperturaEventosEscolares evento = (DtoAperturaEventosEscolares) dataTable.getRowData();
        ResultadoEJB<EventoEscolarDetalle> resAct = ejb.actualizarAperturaEventoRegistrada(evento);
        mostrarMensajeResultadoEJB(resAct);
        listaAperturasEventosEscolaresRegistrados();
        Ajax.update("tbAperturaEventos");
    }
}
