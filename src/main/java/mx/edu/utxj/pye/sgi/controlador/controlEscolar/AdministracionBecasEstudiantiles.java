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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AdministracionBecasRolEstudiantiles;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAdministracionBecas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
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
public class AdministracionBecasEstudiantiles extends ViewScopedRol implements Desarrollable{
    @Getter @Setter AdministracionBecasRolEstudiantiles rol;

    @EJB EjbAdministracionBecas ejb;
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
            setVistaControlador(ControlEscolarVistaControlador.ADMINISTRAR_BECAS);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEstudiantiles(logon.getPersonal().getClave());//validar si es personal de servicios escolares
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new AdministracionBecasRolEstudiantiles(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(usuario);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(ejbAsignacionIndicadoresCriterios.getPeriodoActual());
//            rol.setSoloLectura(true);

            rol.getInstrucciones().add("REGISTRAR TIPO DE BECA.");
            rol.getInstrucciones().add("Seleccionar AGREGAR TIPO DE BECA.");
            rol.getInstrucciones().add("Ingresar el nombre en el campo correspondiente, máximo 45 caracteres.");
            rol.getInstrucciones().add("Dar clic en GUARDAR para registrar.");
            rol.getInstrucciones().add("ELIMINAR TIPO DE BECA.");
            rol.getInstrucciones().add("Dar clic en el icono (cesto de basura) de la columna ELIMINAR de la fila que corresponda.");

            rol.setAgregarTipoBeca(false);
            listaTipoBeca();

        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "administración becas";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void listaTipoBeca(){
        ResultadoEJB<List<BecaTipos>> res = ejb.getListaTiposBeca();
        if(res.getCorrecto()){
            rol.setTiposBeca(res.getValor());
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
   
    /**
     * Permite que al cambiar el valor del inputSwitch para agregar un tipo de beca y se habiliten los componentes correspondientes
     * @param e Evento del cambio de valor
     */
    public void cambiarAgregarTipoBeca(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setAgregarTipoBeca(valor);
            if(rol.getAgregarTipoBeca()){
                rol.setNuevoTipoBeca("Ingresar nombre");
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
   
    /**
     * Permite guardar el nuevo tipo de beca
     */
    public void guardarTipoBeca(){
        if (rol.getNuevoTipoBeca().equals("Ingresar nombre")) {
            Messages.addGlobalWarn("Debe ingresar el nombre del tipo de beca.");
        } else{
            Integer coincidencia = (int) rol.getTiposBeca().stream().filter(p -> p.getNombre().equals(rol.getNuevoTipoBeca())).count();
            if(coincidencia == 0){
                ResultadoEJB<BecaTipos> agregar = ejb.guardarTipoBeca(rol.getNuevoTipoBeca());
                if (agregar.getCorrecto()) {
                    mostrarMensajeResultadoEJB(agregar);
                    listaTipoBeca();
                    rol.setAgregarTipoBeca(false);
                    Ajax.update("frm");
                }
            }else{
                Messages.addGlobalWarn("El tipo de beca que desea agregar ya está registrada.");
            }
        }
    }
    
     /**
     * Permite eliminar el tipo de baja seleccionada
     * @param tipoBeca
     */
    public void eliminarTipoBeca(BecaTipos tipoBeca){
        ResultadoEJB<Integer> eliminar = ejb.eliminarTipoBeca(tipoBeca);
        if(eliminar.getCorrecto()){
            mostrarMensajeResultadoEJB(eliminar);
            listaTipoBeca();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(eliminar);
        
    }
    
    /**
     * Método para verificar si existen registros del tipo de beca seleccionada
     * @param tipoBeca
     * @return Verdadero o Falso, según sea el caso
     */
    public Boolean deshabilitarEliminacionTipoBeca(@NonNull BecaTipos tipoBeca){
        Boolean permiso= Boolean.FALSE;
        ResultadoEJB<Boolean> res = ejb.verificarRegistrosTipoBeca(tipoBeca);
        if(res.getCorrecto()){
            permiso=res.getValor();
        }else mostrarMensajeResultadoEJB(res);
        return permiso;
    }
    
}
