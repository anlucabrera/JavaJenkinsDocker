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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionJustificacionesAperturaRolEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionJustificacionesApertura;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroBajas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class AdministracionJustificacionesAperturaEscolares extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AdministracionJustificacionesAperturaRolEscolares rol;

    @EJB EjbAdministracionJustificacionesApertura ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_JUSTIFICACIONES_APERTURASEXTEMPORANEAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbRegistroBajas.validarServiciosEscolares(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionJustificacionesAperturaRolEscolares(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR JUSTIFICACIÓN DE APERTURA EXTEMPORÁNEA.");
            rol.getInstrucciones().add("Seleccionar la opción AGREGAR JUSTIFICACIÓN.");
            rol.getInstrucciones().add("Ingresar la descripción en el campo correspondiente, máximo 500 caracteres.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ACTIVAR O DESACTIVAR JUSTIFICACIÓN.");
            rol.getInstrucciones().add("Dar clic en el icono (X o ✓) en la columna ACTIVA/INACTIVA de la fila que corresponda.");
            rol.getInstrucciones().add("ELIMINAR JUSTIFICACIÓN.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setAgregarJustificacion(false);
            listaJustificaciones();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración justificaciones aperturas extemporáneas";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaJustificaciones(){
        ResultadoEJB<List<JustificacionPermisosExtemporaneos>> res = ejb.getJustificacionPermisosExtemporaneos();
        if(res.getCorrecto()){
            rol.setJustificaciones(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
   
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar una justificación y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarJustificacion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarJustificacion(valor);
            if(rol.getAgregarJustificacion()){
               rol.setDescripcion("Ingresar descripción");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    
     /**
     * Permite guardar la justificación
     */
    public void guardarJustificacion(){
        if (rol.getDescripcion().equals("Ingresar descripción")) {
            Messages.addGlobalWarn("Debe ingresar la descripción de la justificación que desea registrar.");
        } else {
            Integer coincidencia = (int) rol.getJustificaciones().stream().filter(p -> p.getDescripcion().equals(rol.getDescripcion())).count();
            if (coincidencia == 0) {
                ResultadoEJB<JustificacionPermisosExtemporaneos> agregar = ejb.guardarJustificacion(rol.getDescripcion());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaJustificaciones();
                    rol.setAgregarJustificacion(false);
                    Ajax.update("frm");
                }
            } else {
                Messages.addGlobalWarn("La justificación que desea agregar ya está registrada.");
            }
        }
    }
    
     /**
     * Permite activar o desactivar la justificación seleccionada
     * @param justificacion
     */
    public void activarDesactivarJustificacion(JustificacionPermisosExtemporaneos justificacion){
        ResultadoEJB<JustificacionPermisosExtemporaneos> res = ejb.activarDesactivarJustificacion(justificacion);
        if(res.getCorrecto()){
            justificacion.setActivo(res.getValor().getActivo());
            listaJustificaciones();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite eliminar la justificación seleccionada
     * @param justificacion
     */
    public void eliminarJustificacion(JustificacionPermisosExtemporaneos justificacion){
        ResultadoEJB<Integer> eliminar = ejb.eliminarJustificacion(justificacion);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaJustificaciones();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros relacionados a la justificación seleccionada
     * @param justificacion
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionJustificacion(@NonNull JustificacionPermisosExtemporaneos justificacion){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosJustificaciones(justificacion);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}

