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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionTipoAspiranteEstudianteRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionTipoAspiranteEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
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
public class AdministracionTipoAspiranteEstudianteEscolares extends ViewScopedRol implements Desarrollable {
    @Getter @Setter AdministracionTipoAspiranteEstudianteRolEscolares rol;

    @EJB EjbAdministracionTipoAspiranteEstudiante ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_TIPOASPEST);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionTipoAspiranteEstudianteRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR TIPO DE ASPIRANTE Y/O TIPO DE ESTUDIANTE.");
            rol.getInstrucciones().add("Seleccionar la opción que corresponda dependiendo la pestaña en la que se encuentra AGREGAR ASPIRANTE o AGREGAR ESTUDIANTE.");
            rol.getInstrucciones().add("Ingresar el nombre en el campo correspondiente, máximo 45 caracteres.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ACTIVAR O DESACTIVAR TIPO DE ESTUDIANTE");
            rol.getInstrucciones().add("Dar clic en el primer icono (X o ✓) en la columna ACTIVO/INACTIVO de la fila que corresponda.");
            rol.getInstrucciones().add("ELIMINAR TIPO DE ASPIRANTE O TIPO DE ESTUDIANTE.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setPestaniaActiva(0);
            rol.setAgregarTipoAspirante(false);
            rol.setAgregarTipoEstudiante(false);
            listaTipoAspirante();
            listaTipoEstudiante();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración tipo aspirante y estudiante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaTipoAspirante(){
        ResultadoEJB<List<TipoAspirante>> res = ejb.getListaTipoAspirante();
        if(res.getCorrecto()){
            rol.setTiposAspirante(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void listaTipoEstudiante(){
        ResultadoEJB<List<TipoEstudiante>> res = ejb.getListaTipoEstudiante();
        if(res.getCorrecto()){
            rol.setTiposEstudiante(res.getValor());
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
        rol.setAgregarTipoAspirante(false);
        rol.setAgregarTipoEstudiante(false);
    }
        
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de aspirante y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarAspirante(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoAspirante(valor);
            if(rol.getAgregarTipoAspirante()){
                rol.setNuevoAspirante("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de estudiante y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoEstudiante(valor);
            if(rol.getAgregarTipoEstudiante()){
                rol.setNuevoEstudiante("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite guardar el nuevo tipo de aspirante
     */
    public void guardarTipoAspirante(){
        if (rol.getNuevoAspirante().equals("Ingresar nombre") || "".equals(rol.getNuevoAspirante())) {
            Messages.addGlobalWarn("Debe ingresar el nombre del tipo de aspirante.");
        } else{
            Integer coincidencia = (int) rol.getTiposAspirante().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoAspirante())).count();
            System.err.println("guardarTipoAspirante - coincidencia " + coincidencia);
            if(coincidencia == 0){
                ResultadoEJB<TipoAspirante> agregar = ejb.guardarTipoAspirante(rol.getNuevoAspirante());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoAspirante();
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de aspirante que desea agregar ya está registrado.");
            }
        }
    }
    
     /**
     * Permite eliminar el tipo de aspirante seleccionado
     * @param tipoAspirante
     */
    public void eliminarTipoAspirante(TipoAspirante tipoAspirante){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoAspirante(tipoAspirante);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoAspirante();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
     /**
     * Permite activar o desactivar el tipo de estudiante seleccionado
     * @param tipoEstudiante
     */
    public void activarDesactivarTipoEstudiante(TipoEstudiante tipoEstudiante){
        ResultadoEJB<TipoEstudiante> res = ejb.activarDesactivarTipoEstudiante(tipoEstudiante);
        if(res.getCorrecto()){
            tipoEstudiante.setActivo(res.getValor().getActivo());
            listaTipoEstudiante();
            mostrarMensajeResultadoEJB(res);
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite guardar un nuevo instrumento de evaluación
     */
    public void guardarTipoEstudiante(){
        if (rol.getNuevoEstudiante().equals("Ingresar nombre") || "".equals(rol.getNuevoEstudiante())) {
              Messages.addGlobalWarn("Debe ingresar el nombre del tipo de estudiante.");
        } else{
            Integer coincidencia = (int) rol.getTiposEstudiante().stream().filter(p -> p.getDescripcion().equals(rol.getNuevoEstudiante())).count();
            System.err.println("guardarTipoEstudiante - coincidencia " + coincidencia);
            if(coincidencia == 0){
                ResultadoEJB<TipoEstudiante> agregar = ejb.guardarTipoEstudiante(rol.getNuevoEstudiante());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoEstudiante();
                    rol.setPestaniaActiva(1);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de estudiante que desea agregar ya está registrado.");
            }
        }
    }
    
    /**
     * Permite eliminar el tipo de estudiante seleccionado
     * @param tipoEstudiante
     */
    public void eliminarTipoEstudiante(TipoEstudiante tipoEstudiante){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoEstudiante(tipoEstudiante);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoEstudiante();
            rol.setPestaniaActiva(1);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros del tipo de aspirante seleccionado
     * @param tipoAspirante
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionAspirante(@NonNull TipoAspirante tipoAspirante){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoAspirante(tipoAspirante);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
    /**
     * Método para verificar si existen registros del tipo de estudiante seleccionado
     * @param tipoEstudiante
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionEstudiante(@NonNull TipoEstudiante tipoEstudiante){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoEstudiante(tipoEstudiante);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
