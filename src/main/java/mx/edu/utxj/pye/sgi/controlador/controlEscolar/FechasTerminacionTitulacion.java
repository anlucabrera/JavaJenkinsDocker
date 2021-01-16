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
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.FechasTerminacionRolTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFechasTerminacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FechaTerminacionTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class FechasTerminacionTitulacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter FechasTerminacionRolTitulacion rol;
    
    @EJB EjbFechasTerminacion ejb;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
     if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.FECHAS_TERMINACION_TITULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTitulacion(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new FechasTerminacionRolTitulacion(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Seleccione generacion.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("A continuación visualizará las fechas a registrar o registradas por documento.");
            rol.getInstrucciones().add("Seleccionar fecha que desea registrar.");
            rol.getInstrucciones().add("Dar clic en el botón Guardar, para registrar las fechas en la base de datos.");
           
            listaGeneracionesExpedientes();
            
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "fechas terminacion titulacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    
     /**
     * Permite obtener la lista de generaciones 
     */
    public void listaGeneracionesExpedientes(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesExpediente();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de niveles educativos
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesExpedientes();
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            obtenerFechasTerminacion();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
    /**
     * Permite obtener la lista de programas educativos
     */
    public void obtenerFechasTerminacion(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<FechaTerminacionTitulacion> res = ejb.obtenerFechasTerminacion(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setFechaTerminacionTitulacion(res.getValor());
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion= (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            ProgramasEducativosNiveles nivel= (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            obtenerFechasTerminacion();
            Ajax.update("frm");
        }
    }
    
   
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     */
    public void guardarFechasTerminacion(){
        ResultadoEJB<FechaTerminacionTitulacion> res = ejb.guardarFechasTerminacionTitulacion(rol.getGeneracion(), rol.getNivelEducativo(), rol.getFechaTerminacionTitulacion());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            obtenerFechasTerminacion();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     */
    public void actualizarFechasTerminacion(){
        ResultadoEJB<FechaTerminacionTitulacion> res = ejb.actualizarFechasTerminacionTitulacion(rol.getFechaTerminacionTitulacion());
        if(res.getCorrecto()){
            mostrarMensajeResultadoEJB(res);
            obtenerFechasTerminacion();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);
    }
}
