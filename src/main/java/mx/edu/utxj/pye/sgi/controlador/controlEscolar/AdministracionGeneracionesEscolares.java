/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionGeneracionesRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionGeneraciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionCiclosPeriodosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosescolaresGeneraciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AdministracionGeneracionesEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AdministracionGeneracionesRolEscolares rol;

    @EJB EjbAdministracionGeneraciones ejb;
    @EJB EjbAdministracionCiclosPeriodosEscolares ejbAdministracionCiclosPeriodosEscolares;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_GENERACIONES);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionGeneracionesRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR GENERACIÓN Y/O RELACIONAR CICLOS ESCOLARES CON GENERACIONES.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda dependiendo la pestaña en la que se encuentra AGREGAR GENERACIÓN o RELACIONAR CICLO A GENERACIÓN.");
            rol.getInstrucciones().add("GENERACIÓN: Seleccionar año de inicio y fin de la generación.");
            rol.getInstrucciones().add("RELACIONAR CICLO A GENERACIÓN: Seleccionar ciclo escolar y generación, los ciclos escolares que se deben relacionar son los comprendidos durante la generación seleccionada.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ELIMINAR GENERACIÓN o RELACIÓN CICLO Y GENERACIÓN.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarGeneracion(false);
            rol.setAgregarCicloGeneracion(false);
            listaGeneraciones();
            listaCiclosGeneraciones();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración generaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaGeneraciones(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneraciones();
        if(res.getCorrecto()){
            rol.setGeneraciones(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
   
    public void listaCiclosGeneraciones(){
        ResultadoEJB<List<CiclosescolaresGeneraciones>> res = ejb.getCicloGeneraciones();
        if(res.getCorrecto()){
            rol.setCicloGeneraciones(res.getValor());
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
        rol.setAgregarGeneracion(false);
        rol.setAgregarCicloGeneracion(false);
    }
    
     /**
     * Permite que cambiar el valor del año de inicio de la generación
     * @param e Evento del cambio de valor
     */
    public void cambiarAnioInicio(ValueChangeEvent e){
        if(e.getNewValue() instanceof Short){
            Short anio = (Short)e.getNewValue();
            rol.setAnioInicio(anio);
            rol.setAnioFin(ejb.getAnioFin(rol.getAnioInicio()).getValor());
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que cambiar el valor del ciclo escolar seleccionado
     * @param e Evento del cambio de valor
     */
    public void cambiarCicloEscolar(ValueChangeEvent e){
        if(e.getNewValue() instanceof CiclosEscolares){
            CiclosEscolares ciclo = (CiclosEscolares)e.getNewValue();
            rol.setCicloEscolar(ciclo);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que cambiar el valor de la generación seleccionada
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion = (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
     
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar una generación y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarGeneracion(valor);
            if(rol.getAgregarGeneracion()){
               rol.setAnios(ejb.getAniosInicio().getValor());
               rol.setAnioInicio(rol.getAnios().get(0));
               rol.setAnioFin(ejb.getAnioFin(rol.getAnioInicio()).getValor());
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar o relacionar un ciclo a una generación y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarCicloGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarCicloGeneracion(valor);
            if(rol.getAgregarCicloGeneracion()){
                rol.setCiclosEscolares(ejbAdministracionCiclosPeriodosEscolares.getCiclosEscolares().getValor());
                rol.setCicloEscolar(rol.getCiclosEscolares().get(0));
                rol.setGeneracion(rol.getGeneraciones().get(0));
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    
     /**
     * Permite guardar la generación
     */
    public void guardarGeneracion(){
        Integer coincidencia = (int) rol.getGeneraciones().stream().filter(p -> p.getInicio().equals(rol.getAnioInicio())).count();
            if(coincidencia == 0){
                ResultadoEJB<Generaciones> agregar = ejb.guardarGeneracion(rol.getAnioInicio(), rol.getAnioFin());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaGeneraciones();
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("La generación que desea agregar ya está registrada.");
            }
    }
    
    
     /**
     * Permite guardar la relación del ciclo escolar con la generación
     */
    public void guardarCicloGeneracion(){
        Integer coincidencia = (int) rol.getCicloGeneraciones().stream().filter(p -> p.getCiclo().getCiclo().equals(rol.getCicloEscolar().getCiclo()) && p.getGeneracion().getGeneracion().equals(rol.getGeneracion().getGeneracion())).count();
        if (coincidencia == 0) {
            ResultadoEJB<CiclosescolaresGeneraciones> agregar = ejb.guardarCicloGeneracion(rol.getCicloEscolar(), rol.getGeneracion());
            if (agregar.getCorrecto()) {
                mostrarMensajeResultadoEJB(agregar);
                listaCiclosGeneraciones();
                Ajax.update("frm");
            }
        } else {
            Messages.addGlobalWarn("El ciclo y la generación que desea agregar ya están relacionados.");
        }
    }
    
     /**
     * Permite eliminar la generación seleccionada
     * @param generacion
     */
    public void eliminarGeneracion(Generaciones generacion){
        ResultadoEJB<Integer> eliminar = ejb.eliminarGeneracion(generacion);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaGeneraciones();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite eliminar la relación del ciclo y la generación seleccionada
     * @param ciclosescolaresGeneraciones
     */
    public void eliminarCicloGeneracion(CiclosescolaresGeneraciones ciclosescolaresGeneraciones){
        ResultadoEJB<Integer> eliminar = ejb.eliminarCicloGeneracion(ciclosescolaresGeneraciones);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaCiclosGeneraciones();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros relacionados a la generación seleccionada
     * @param generacion
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionGeneracion(@NonNull Generaciones generacion){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosGeneraciones(generacion);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
