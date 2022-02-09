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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionCatalogosAdmisionRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionCatalogosAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
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
public class AdministracionCatalogosAdmisionEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AdministracionCatalogosAdmisionRolEscolares rol;

    @EJB EjbAdministracionCatalogosAdmision ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_CATALOGOSADMISION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionCatalogosAdmisionRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR TIPO DE DISCAPACIDAD, LENGUA INDÍGENA, TIPO DE SANGRE Y/O MEDIO DE DIFUSIÓN.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda dependiendo la pestaña en la que se encuentra AGREGAR DISCAPACIDAD, LENGUA INDÍGENA, TIPO DE SANGRE o MEDIO DE DIFUSIÓN.");
            rol.getInstrucciones().add("Ingresar el nombre en el campo correspondiente, máximo 45 caracteres.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ACTIVAR O DESACTIVAR TIPO DE DISCAPACIDAD O MEDIO DE DIFUSIÓN");
            rol.getInstrucciones().add("Dar clic en el icono (X o ✓) en la columna ACTIVO/INACTIVO de la fila que corresponda.");
            rol.getInstrucciones().add("ELIMINAR TIPO DE DISCAPACIDAD, LENGUA INDÍGENA, TIPO DE SANGRE O MEDIO DE DIFUSIÓN.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarTipoDiscapacidad(false);
            rol.setAgregarLenguaIndigena(false);
            rol.setAgregarTipoSangre(false);
            rol.setAgregarMedioDifusion(false);
            listaTipoDiscapacidad();
            listaLenguaIndigena();
            listaTipoSangre();
            listaMedioDifusion();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración catálogos admisión";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaTipoDiscapacidad(){
        ResultadoEJB<List<TipoDiscapacidad>> res = ejb.getListaTipoDiscapacidad();
        if(res.getCorrecto()){
            rol.setTiposDiscapacidad(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaLenguaIndigena(){
        ResultadoEJB<List<LenguaIndigena>> res = ejb.getListaLenguaIndigena();
        if(res.getCorrecto()){
            rol.setLenguasIndigenas(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaTipoSangre(){
        ResultadoEJB<List<TipoSangre>> res = ejb.getListaTipoSangre();
        if(res.getCorrecto()){
            rol.setTiposSangre(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaMedioDifusion(){
        ResultadoEJB<List<MedioDifusion>> res = ejb.getListaMediosDifusion();
        if(res.getCorrecto()){
            rol.setMediosDifusion(res.getValor());
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
        rol.setAgregarTipoDiscapacidad(false);
        rol.setAgregarLenguaIndigena(false);
        rol.setAgregarTipoSangre(false);
        rol.setAgregarMedioDifusion(false);
    }
        
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de discapacidad y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarDiscapacidad(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoDiscapacidad(valor);
            if(rol.getAgregarTipoDiscapacidad()){
                rol.setNuevaDiscapacidadNombre("Ingresar nombre");
                rol.setNuevaDiscapacidadDescripcion("Ingresar descripción");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar una lengua indígena y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarLengua(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarLenguaIndigena(valor);
            if(rol.getAgregarLenguaIndigena()){
                rol.setNuevaLengua("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de sangre y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarTipoSangre(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoSangre(valor);
            if(rol.getAgregarTipoSangre()){
                rol.setNuevoTipoSangre("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un medio de difusión y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarMedioDifusion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarMedioDifusion(valor);
            if(rol.getAgregarMedioDifusion()){
                rol.setNuevoMedio("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite guardar el nuevo tipo de discapacidad
     */
    public void guardarTipoDiscapacidad(){
        if (rol.getNuevaDiscapacidadNombre().equals("Ingresar nombre") && rol.getNuevaDiscapacidadDescripcion().equals("Ingresar descripción")) {
            Messages.addGlobalWarn("Debe ingresar el nombre y la descripción del tipo de discapacidad.");
        } else if(rol.getNuevaDiscapacidadNombre().equals("Ingresar nombre") && !rol.getNuevaDiscapacidadDescripcion().equals("Ingresar descripción")){
            Messages.addGlobalWarn("Debe ingresar el nombre del tipo de discapacidad.");
        }else if(!rol.getNuevaDiscapacidadNombre().equals("Ingresar nombre") && rol.getNuevaDiscapacidadDescripcion().equals("Ingresar descripción")){
            Messages.addGlobalWarn("Debe ingresar la descripción del tipo de discapacidad.");
        }else{
            Integer coincidencia = (int) rol.getTiposDiscapacidad().stream().filter(p -> p.getNombre().equals(rol.getNuevaDiscapacidadNombre())).count();
            if(coincidencia == 0){
                ResultadoEJB<TipoDiscapacidad> agregar = ejb.guardarTipoDiscapacidad(rol.getNuevaDiscapacidadNombre(), rol.getNuevaDiscapacidadDescripcion());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoDiscapacidad();
                    rol.setAgregarTipoDiscapacidad(false);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de discapacidad que desea agregar ya está registrado.");
            }
        }
    }
    
     /**
     * Permite eliminar el tipo de aspirante seleccionado
     * @param tipoDiscapacidad
     */
    public void eliminarTipoDiscapacidad(TipoDiscapacidad tipoDiscapacidad){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoDiscapacidad(tipoDiscapacidad);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoDiscapacidad();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite activar o desactivar el tipo de discapacidad seleccionada
     * @param tipoDiscapacidad
     */
    public void activarDesactivarTipoDiscapacidad(TipoDiscapacidad tipoDiscapacidad){
        ResultadoEJB<TipoDiscapacidad> res = ejb.activarDesactivarTipoDiscapacidad(tipoDiscapacidad);
        if(res.getCorrecto()){
            tipoDiscapacidad.setEstatus(res.getValor().getEstatus());
            listaTipoDiscapacidad();
            mostrarMensajeResultadoEJB(res);
            rol.setPestaniaActiva(0);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite guardar una nueva lengua indígena
     */
    public void guardarLenguaIndigena(){
        if (rol.getNuevaLengua().equals("Ingresar nombre")) {
              Messages.addGlobalWarn("Debe ingresar el nombre de la lengua indígena.");
        } else{
            Integer coincidencia = (int) rol.getLenguasIndigenas().stream().filter(p -> p.getNombre().equals(rol.getNuevaLengua())).count();
            if(coincidencia == 0){
                ResultadoEJB<LenguaIndigena> agregar = ejb.guardarLenguaIndigena(rol.getNuevaLengua());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaLenguaIndigena();
                    rol.setAgregarLenguaIndigena(false);
                    rol.setPestaniaActiva(1);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("La lengua indígena que desea agregar ya está registrada.");
            }
        }
    }
    
    /**
     * Permite eliminar la lengua indígena seleccionada
     * @param lenguaIndigena
     */
    public void eliminarLenguaIndigena(LenguaIndigena lenguaIndigena){
        ResultadoEJB<Integer> eliminar = ejb.eliminarLenguaIndigena(lenguaIndigena);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaLenguaIndigena();
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Permite guardar un nuevo tipo de sangre
     */
    public void guardarTipoSangre(){
        if (rol.getNuevoTipoSangre().equals("Ingresar nombre")) {
              Messages.addGlobalWarn("Debe ingresar el nombre del tipo de sangre.");
        } else{
            Integer coincidencia = (int) rol.getTiposSangre().stream().filter(p -> p.getNombre().equals(rol.getNuevoTipoSangre())).count();
            if(coincidencia == 0){
                ResultadoEJB<TipoSangre> agregar = ejb.guardarTipoSangre(rol.getNuevoTipoSangre());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoSangre();
                    rol.setAgregarTipoSangre(false);
                    rol.setPestaniaActiva(2);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de sangre que desea agregar ya está registrada.");
            }
        }
    }
    
    /**
     * Permite eliminar el tipo de sangre seleccionado
     * @param tipoSangre
     */
    public void eliminarTipoSangre(TipoSangre tipoSangre){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoSangre(tipoSangre);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoSangre();
            rol.setPestaniaActiva(2);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite guardar un nuevo medio de difusión
     */
    public void guardarMedioDifusion(){
        if (rol.getNuevoMedio().equals("Ingresar nombre")) {
              Messages.addGlobalWarn("Debe ingresar el nombre del medio de difusión.");
        } else{
            Integer coincidencia = (int) rol.getMediosDifusion().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoMedio())).count();
            if(coincidencia == 0){
                ResultadoEJB<MedioDifusion> agregar = ejb.guardarMedioDifusion(rol.getNuevoMedio());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaMedioDifusion();
                    rol.setAgregarMedioDifusion(false);
                    rol.setPestaniaActiva(3);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El medio de difusión que desea agregar ya está registrada.");
            }
        }
    }
    
    /**
     * Permite activar o desactivar el medio de difusión seleccionado
     * @param medioDifusion
     */
    public void activarDesactivarMedioDifusion(MedioDifusion medioDifusion){
        ResultadoEJB<MedioDifusion> res = ejb.activarDesactivarMedioDifusion(medioDifusion);
        if(res.getCorrecto()){
            medioDifusion.setEstatus(res.getValor().getEstatus());
            listaMedioDifusion();
            mostrarMensajeResultadoEJB(res);
            rol.setPestaniaActiva(3);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite eliminar el medio de difusión seleccionado
     * @param medioDifusion
     */
    public void eliminarMedioDifusion(MedioDifusion medioDifusion){
        ResultadoEJB<Integer> eliminar = ejb.eliminarMedioDifusion(medioDifusion);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaMedioDifusion();
            rol.setPestaniaActiva(3);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros del tipo de discapacidad seleccionada
     * @param tipoDiscapacidad
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionTipoDiscapacidad(@NonNull TipoDiscapacidad tipoDiscapacidad){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoDiscapacidad(tipoDiscapacidad);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existen registros de la lengua indígena seleccionada
     * @param lenguaIndigena
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionLenguaIndigena(@NonNull LenguaIndigena lenguaIndigena){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosLenguaIndigena(lenguaIndigena);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existen registros del medio de difusión seleccionado
     * @param medioDifusion
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionMedioDifusion(@NonNull MedioDifusion medioDifusion){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosMedioDifusion(medioDifusion);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    /**
     * Método para verificar si existen registros del tipo de sangre seleccionado
     * @param tipoSangre
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionTipoSangre(@NonNull TipoSangre tipoSangre){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoSangre(tipoSangre);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
