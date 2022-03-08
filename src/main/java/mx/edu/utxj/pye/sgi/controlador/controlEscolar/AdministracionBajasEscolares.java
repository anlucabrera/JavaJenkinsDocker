/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionBajasRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionBajasEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoria;
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
public class AdministracionBajasEscolares extends ViewScopedRol implements Desarrollable{
@Getter @Setter AdministracionBajasRolEscolares rol;

    @EJB EjbAdministracionBajasEscolares ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_BAJAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionBajasRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR TIPO DE BAJA, CAUSA DE BAJA Y/O RELACIONAR BAJA CON UNA CATEGORÍA.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda dependiendo la pestaña en la que se encuentra AGREGAR TIPO DE BAJA, CAUSA DE BAJA, o RELACIONAR BAJA - CATEGORÍA.");
            rol.getInstrucciones().add("Ingresar el nombre en el campo correspondiente, máximo 45 caracteres.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ACTIVAR O DESACTIVAR CAUSA DE BAJA");
            rol.getInstrucciones().add("Dar clic en el icono (X o ✓) en la columna ACTIVA/INACTIVA de la fila que corresponda.");
            rol.getInstrucciones().add("ELIMINAR TIPO DE BAJA, CAUSA DE BAJA o RELACIÓN BAJA - CATEGORÍA.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarTipoBaja(false);
            rol.setAgregarCausaBaja(false);
            rol.setAgregarBajaCategoria(false);
            listaTipoBaja();
            listaCausasBaja();
            listaBajasCategoria();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración causas de baja y categorización";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaTipoBaja(){
        ResultadoEJB<List<BajasTipo>> res = ejb.getListaTiposBaja();
        if(res.getCorrecto()){
            rol.setTiposBaja(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaCausasBaja(){
        ResultadoEJB<List<BajasCausa>> res = ejb.getListaCausasBaja();
        if(res.getCorrecto()){
            rol.setCausasBaja(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaBajasCategoria(){
        ResultadoEJB<List<BajasCausaCategoria>> res = ejb.getListaCausasBajaCategoria();
        if(res.getCorrecto()){
            rol.setCategoriasCausaBaja(res.getValor());
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
        rol.setAgregarTipoBaja(false);
        rol.setAgregarCausaBaja(false);
        rol.setAgregarBajaCategoria(false);
    }
        
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de baja y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarTipoBaja(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoBaja(valor);
            if(rol.getAgregarTipoBaja()){
                rol.setNuevoTipoBaja("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar una causa de baja y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarCausaBaja(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarCausaBaja(valor);
            if(rol.getAgregarCausaBaja()){
                rol.setNuevaCausaBaja("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar la relación de una causa de baja con una categoría y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarBajaCategoria(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarBajaCategoria(valor);
            if(rol.getAgregarBajaCategoria()){
                rol.setCausasBajaDisponibles(ejb.getListaCausasSinCategoria(rol.getCategoriasCausaBaja()).getValor());
                rol.setCategorias(ejb.getListaCategorias().getValor());
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que cambiar el valor de la causa de baja seleccionada
     * @param e Evento del cambio de valor
     */
    public void cambiarCausaBaja(ValueChangeEvent e){
        if(e.getNewValue() instanceof BajasCausa){
            BajasCausa causa = (BajasCausa)e.getNewValue();
            rol.setCausaBaja(causa);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que cambiar el valor de la categoría seleccionada
     * @param e Evento del cambio de valor
     */
    public void cambiarCategoria(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            String categoria = (String)e.getNewValue();
            rol.setCategoria(categoria);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite guardar el nuevo tipo de baja
     */
    public void guardarTipoBaja(){
        if (rol.getNuevoTipoBaja().equals("Ingresar nombre")) {
            Messages.addGlobalWarn("Debe ingresar el nombre del tipo de baja.");
        } else{
            Integer coincidencia = (int) rol.getTiposBaja().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoTipoBaja())).count();
            if(coincidencia == 0){
                ResultadoEJB<BajasTipo> agregar = ejb.guardarTipoBaja(rol.getNuevoTipoBaja());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoBaja();
                    rol.setAgregarTipoBaja(false);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de baja que desea agregar ya está registrada.");
            }
        }
    }
    
     /**
     * Permite eliminar el tipo de baja seleccionada
     * @param tipoBaja
     */
    public void eliminarTipoBaja(BajasTipo tipoBaja){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoBaja(tipoBaja);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoBaja();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite guardar una nueva causa de baja
     */
    public void guardarCausaBaja(){
        if (rol.getNuevaCausaBaja().equals("Ingresar nombre")) {
              Messages.addGlobalWarn("Debe ingresar el nombre de la causa de baja.");
        } else{
            Integer coincidencia = (int) rol.getCausasBaja().stream().filter(p -> p.getCausa().equals(rol.getNuevaCausaBaja())).count();
            if(coincidencia == 0){
                ResultadoEJB<BajasCausa> agregar = ejb.guardarCausaBaja(rol.getNuevaCausaBaja());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaCausasBaja();
                    rol.setAgregarCausaBaja(false);
                    rol.setPestaniaActiva(1);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("La causa de baja que desea agregar ya está registrada.");
            }
        }
    }
    
     /**
     * Permite activar o desactivar la causa de baja seleccionada
     * @param causaBaja
     */
    public void activarDesactivarCausaBaja(BajasCausa causaBaja){
        ResultadoEJB<BajasCausa> res = ejb.activarDesactivarCausaBaja(causaBaja);
        if(res.getCorrecto()){
            causaBaja.setActiva(res.getValor().getActiva());
            listaCausasBaja();
            mostrarMensajeResultadoEJB(res);
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite eliminar la causa de baja seleccionada
     * @param causaBaja
     */
    public void eliminarCausaBaja(BajasCausa causaBaja){
        ResultadoEJB<Integer> eliminar = ejb.eliminarCausaBaja(causaBaja);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaCausasBaja();
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Permite guardar una relación de baja con categoría
     */
    public void guardarBajaCategoria(){
        ResultadoEJB<BajasCausaCategoria> agregar = ejb.guardarCausaCategoria(rol.getCausaBaja(), rol.getCategoria());
        if (agregar.getCorrecto()) {
            mostrarMensajeResultadoEJB(agregar);
            listaBajasCategoria();
            rol.setAgregarBajaCategoria(false);
            rol.setPestaniaActiva(2);
            Ajax.update("frm");
        }
    }
    
    /**
     * Permite eliminar la relación de la causa de baja con una categoría
     * @param bajaCategoria
     */
    public void eliminarBajaCategoria(BajasCausaCategoria bajaCategoria){
        ResultadoEJB<Integer> eliminar = ejb.eliminarCausaCategoria(bajaCategoria);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaBajasCategoria();
            rol.setPestaniaActiva(2);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros del tipo de baja seleccionada
     * @param tipoBaja
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionTipoBaja(@NonNull BajasTipo tipoBaja){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoBaja(tipoBaja);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existen registros de la causa de baja seleccionada
     * @param causaBaja
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionCausaBaja(@NonNull BajasCausa causaBaja){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosCausaBaja(causaBaja);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }

}
