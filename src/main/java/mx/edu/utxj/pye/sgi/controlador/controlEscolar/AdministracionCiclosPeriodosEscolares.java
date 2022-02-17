/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionCiclosPeriodosRolEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionCiclosPeriodosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AdministracionCiclosPeriodosEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AdministracionCiclosPeriodosRolEscolares rol;

    @EJB EjbAdministracionCiclosPeriodosEscolares ejb;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbAsignacionIndicadoresCriterios ejbAsignacionIndicadoresCriterios;
    
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;
    
     /**
     * Inicializa:<br/>
     *      El filtro de rol por area superior y categiría operativa<br/>
     *      La referencia al director si es que el usuario logueado es efectivamente un director por medio del filtro de rol<br/>
     *      El programa educativo al que pertenece el director por medio de operación segura antierror<br/>
     *      El DTO del rol<br/>
     *      La lista de periodos escolares en forma descendente por medio de operación segura antierror<br/>
     *      EL mapa de programas con grupos por medio de operación segura antierror ordenando programas por areas, niveles y nombre del programa y los grupos por grado y letra
     */
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
        try{
        if(!logon.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_CICLOSPERIODOSESCOLARES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionCiclosPeriodosRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR CICLO ESCOLAR Y/O PERIODO ESCOLAR.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda dependiendo la pestaña en la que se encuentra AGREGAR CICLO ESCOLAR o AGREGAR PERIODO ESCOLAR.");
            rol.getInstrucciones().add("CICLO ESCOLAR: Seleccionar año de inicio y fin del ciclo escolar.");
            rol.getInstrucciones().add("PERIODO ESCOLAR: Seleccionar mes de inicio y fin, año, fechas de inicio y fin del periodo escolar.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ELIMINAR CICLO ESCOLAR O PERIODO ESCOLAR.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarCicloEscolar(false);
            rol.setAgregarPeriodoEscolar(false);
            listaCiclosEscolares();
            listaPeriodosEscolares();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración ciclos periodos escolares";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaCiclosEscolares(){
        ResultadoEJB<List<CiclosEscolares>> res = ejb.getCiclosEscolares();
        if(res.getCorrecto()){
            rol.setCiclosEscolares(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaPeriodosEscolares(){
        ResultadoEJB<List<DtoPeriodoEscolarFechas>> res = ejb.getPeriodosEscolares();
        if(res.getCorrecto()){
            rol.setPeriodosEscolares(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    /**
     * Permite que al cambiar de pestaña se muestren las opciones correspondientes
     * @param event Evento del cambio de valor
     */
    public void cambiarPestania(TabChangeEvent event){
        TabView tv = (TabView) event.getComponent();
        rol.setPestaniaActiva(tv.getActiveIndex());
        rol.setAgregarCicloEscolar(false);
        rol.setAgregarPeriodoEscolar(false);
    }
    
     /**
     * Permite que cambiar el valor del ciclo escolar seleccionado
     * @param e Evento del cambio de valor
     */
    public void cambiarCicloEscolar(ValueChangeEvent e){
        if(e.getNewValue() instanceof Date){
            Date anio = (Date)e.getNewValue();
            rol.setAnioInicio(anio);
            rol.setAnioFin(ejb.getAnioFin(rol.getAnioInicio()).getValor());
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
        
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un ciclo escolar y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarCicloEscolar(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarCicloEscolar(valor);
            if(rol.getAgregarCicloEscolar()){
                rol.setAnios(ejb.getAniosInicio().getValor());
                rol.setAnioInicio(rol.getAnios().get(0));
                rol.setAnioFin(ejb.getAnioFin(rol.getAnioInicio()).getValor());
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un periodo escolar y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarPeriodoEscolar(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarPeriodoEscolar(valor);
            if(rol.getAgregarPeriodoEscolar()){
                rol.setCicloEscolar(rol.getCiclosEscolares().get(0));
                rol.setMesesInicio(ejb.getMesesInicio().getValor());
                rol.setMesInicio(rol.getMesesInicio().get(0));
                rol.setMesFin(ejb.getMesFin(rol.getMesInicio()).getValor());
                rol.setAnioPeriodoEscolar(ejb.getAnioPeriodoEscolar().getValor());
                rol.setFechaInicioPeriodoEscolar(new Date());
                rol.setFechaFinPeriodoEscolar(new Date());
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que cambiar el valor del mes de inicio seleccionado
     * @param e Evento del cambio de valor
     */
    public void cambiarMesInicio(ValueChangeEvent e){
        if(e.getNewValue() instanceof Meses){
            Meses mesInicio = (Meses)e.getNewValue();
            rol.setMesInicio(mesInicio);
            rol.setMesFin(ejb.getMesFin(rol.getMesInicio()).getValor());
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite guardar el ciclo escolar
     */
    public void guardarCicloEscolar(){
        Integer coincidencia = (int) rol.getCiclosEscolares().stream().filter(p -> p.getInicio().equals(rol.getAnioInicio())).count();
            if(coincidencia == 0){
                ResultadoEJB<CiclosEscolares> agregar = ejb.guardarCicloEscolar(rol.getAnioInicio(), rol.getAnioFin());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaCiclosEscolares();
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El ciclo escolar que desea agregar ya está registrado.");
            }
    }
    
     /**
     * Permite editar la fechas de inicio y fin de un periodo escolar que ya se encuentra registrado
     * @param event Evento de edición de la celda
     */
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoPeriodoEscolarFechas dtoPeriodoEscolarFechas = (DtoPeriodoEscolarFechas) dataTable.getRowData();
        ResultadoEJB<PeriodoEscolarFechas> resActFechas = ejb.actualizarFechasPeriodoEscolar(dtoPeriodoEscolarFechas.getFechasPeriodoEscolar());
        if (resActFechas.getCorrecto()) {
            mostrarMensajeResultadoEJB(resActFechas);
        }
    }
    
     /**
     * Permite guardar el periodo escolar
     */
    public void guardarPeriodoEscolar(){
        Integer coincidencia = (int) rol.getPeriodosEscolares().stream().filter(p -> p.getPeriodoEscolar().getCiclo().getCiclo().equals(rol.getCicloEscolar().getCiclo()) && p.getPeriodoEscolar().getMesInicio().getNumero().equals(rol.getMesInicio().getNumero())).count();
            if(coincidencia == 0){
                if(rol.getFechaFinPeriodoEscolar().before(rol.getFechaInicioPeriodoEscolar()) || rol.getFechaFinPeriodoEscolar().equals(rol.getFechaInicioPeriodoEscolar())){
                    Messages.addGlobalWarn("La fecha fin debe ser posterior a la fecha de inicio del periodo escolar.");
                }else{
                    ResultadoEJB<PeriodosEscolares> agregar = ejb.guardarPeriodoEscolar(rol.getCicloEscolar(), rol.getMesInicio(), rol.getMesFin(), rol.getAnioPeriodoEscolar());
                    if (agregar.getCorrecto()) {
                        mostrarMensajeResultadoEJB(agregar);
                        PeriodosEscolares periodoEscolar = agregar.getValor();
                        ResultadoEJB<PeriodoEscolarFechas> agregarFechas = ejb.guardarPeriodoEscolarFechas(periodoEscolar, rol.getFechaInicioPeriodoEscolar(), rol.getFechaFinPeriodoEscolar());
                        if (agregarFechas.getCorrecto()) {
                            mostrarMensajeResultadoEJB(agregarFechas);
                            listaPeriodosEscolares();
                            Ajax.update("frm");
                        }
                    }
                }
            }else{
                Messages.addGlobalWarn("El periodo escolar que desea agregar ya está registrado.");
            }
    }
    
     /**
     * Permite eliminar el ciclo escolar seleccionado
     * @param cicloEscolar
     */
    public void eliminarCicloEscolar(CiclosEscolares cicloEscolar){
        ResultadoEJB<Integer> eliminar = ejb.eliminarCicloEscolar(cicloEscolar);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaCiclosEscolares();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite eliminar el periodo escolar seleccionado
     * @param periodoEscolar
     */
    public void eliminarPeriodoEscolar(DtoPeriodoEscolarFechas periodoEscolar){
        ResultadoEJB<Integer> eliminar = ejb.eliminarPeriodoEscolar(periodoEscolar.getPeriodoEscolar());
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaPeriodosEscolares();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros relacionados al ciclo escolar seleccionado
     * @param cicloEscolar
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionCicloEscolar(@NonNull CiclosEscolares cicloEscolar){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosCicloEscolar(cicloEscolar);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existen registros realacionados al periodo escolar seleccionado
     * @param periodoEscolar
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionPeriodoEscolar(@NonNull DtoPeriodoEscolarFechas periodoEscolar){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosPeriodoEscolar(periodoEscolar.getPeriodoEscolar());
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
}
