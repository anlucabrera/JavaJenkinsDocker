/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Collections;
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
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNotificacionesAreas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroNotificacionRolGeneral;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroNotificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCe;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCeImagenes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesEnlaces;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.NotificacionesTipo;
import mx.edu.utxj.pye.sgi.enums.RolNotificacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroNotificacionesGeneral extends ViewScopedRol implements Desarrollable{
    private static final long serialVersionUID = -3552096699095522780L;
    @Getter     @Setter                         private                     RegistroNotificacionRolGeneral              rol;         
    @Getter     Boolean                         tieneAcceso = false;
    
    @EJB        EjbRegistroNotificaciones       ejb;
    @EJB        EjbValidacionRol                ejbValidacionRol;
    @EJB        EjbPropiedades                  ep;
    
    @Inject     LogonMB                         logonMB;
    @Getter     private                         Boolean                     cargado = false;
    
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro de notificaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    @PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_NOTIFICACIONES);
            
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbValidacionRol.validarTrabajador(logonMB.getPersonal().getClave());
            if(!resAcceso.getCorrecto()) {mostrarMensajeResultadoEJB(resAcceso);return;}
            rol = new RegistroNotificacionRolGeneral(resAcceso.getValor());
            tieneAcceso = rol.tieneAcceso(rol.getPersonal());
            if(verificarInvocacionMenu()) return;
            if(!validarIdentificacion()) return;
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            cargaAreasParaAsignarNotificacion();
            
            inicializarNotificacionCe();
            inicializarNotificacionCeEnlace();
            inicializarNotificacionCeImagen();
            
            inicializarListaNotificacionesCe();
            inicializarListaNotificacionesEnlaces();
            inicializarListaNotificacionesImagenes();
        } catch (Exception e) {mostrarExcepcion(e);}
    }
    
    public void actualizar(){
        repetirUltimoMensaje();
    }
    
    public List<NotificacionesTipo> getListaNotificacionesTipos(){
        return NotificacionesTipo.ListaGenericos();
    }
    
    public List<RolNotificacion> getListaNotificacionRol(){
        return RolNotificacion.ListaNotificacion();
    }
    
    public void cargaAreasParaAsignarNotificacion(){
        ResultadoEJB<List<DtoNotificacionesAreas>> resDtoAreasParaNotificacion = ejb.consultarListadoAreasParaNotificacion();
        if(resDtoAreasParaNotificacion.getCorrecto()){
            rol.setListaDtoNotificacionesAreas(resDtoAreasParaNotificacion.getValor());
        }else{
            mostrarMensajeResultadoEJB(resDtoAreasParaNotificacion);
        }
    }
    
    public void inicializarNotificacionCe(){
        rol.setNotificacionCe(new NotificacionesCe());
        rol.setFechaInicio(new Date());
        rol.setFechaFin(new Date());
        rol.getNotificacionCe().setPersonaRegistro(rol.getPersonal().getPersonal().getClave());
    }
    
    public void inicializarNotificacionCeEnlace(){
        rol.setNotificacionEnlace(new NotificacionesEnlaces());
    }
    
    public void inicializarNotificacionCeImagen(){
        rol.setNotificacionCeImagen(new NotificacionesCeImagenes());
    }
    
    public void inicializarListaNotificacionesCe(){
        rol.setListaNotificacionesCe(new ArrayList<>());
        rol.setListaNotificacionesCe(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaNotificacionesEnlaces(){
        rol.setListaNotificacionesEnlaces(new ArrayList<>());
        rol.setListaNotificacionesEnlaces(Collections.EMPTY_LIST);
    }
    
    public void inicializarListaNotificacionesImagenes(){
        rol.setListaNotificacionesCeImagenes(new ArrayList<>());
        rol.setListaNotificacionesCeImagenes(Collections.EMPTY_LIST);
    }
    
    public void inicializarGeneral() {
        inicializarNotificacionCe();
        inicializarNotificacionCeEnlace();
        inicializarNotificacionCeImagen();

        inicializarListaNotificacionesCe();
        inicializarListaNotificacionesEnlaces();
        inicializarListaNotificacionesImagenes();
    }
    
    public void valifaFechaInicio(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getFechaFin() != null || rol.getFechaFin() != null) {
            rol.setFechaInicio((Date) event.getNewValue());
            if (rol.getFechaInicio().before(rol.getFechaFin())) {
            } else {
                if (rol.getFechaFin().before(rol.getFechaInicio())) {
                    rol.setFechaFin(null);
                    rol.setFechaFin(rol.getFechaInicio());
                } else {}
            }
        }
    }
    
    public void validaFechaFin(ValueChangeEvent event){
        if ((Date) event.getNewValue() != null && rol.getFechaInicio() != null || rol.getFechaInicio() != null) {
            rol.setFechaFin((Date) event.getNewValue());
            if (rol.getFechaFin().after(rol.getFechaInicio())) {
            } else {
                if (rol.getFechaInicio().after(rol.getFechaFin())) {
                    rol.setFechaInicio(null);
                    rol.setFechaInicio(rol.getFechaFin());
                } else {}
            }
        }
    }
    
    public void guardarNotificacion(){
        rol.getNotificacionCe().setFechaInicioDuracion(rol.getFechaInicio());
        rol.getNotificacionCe().setFechaFinDuracion(rol.getFechaFin());
        rol.getNotificacionCe().setGeneral(Boolean.FALSE);
        rol.getNotificacionCe().setAlcance(String.join(",", rol.getAlcance()));
        ResultadoEJB<NotificacionesCe> resNotificacion = ejb.guardaNotificacion(rol.getNotificacionCe());
        if(resNotificacion.getCorrecto()){
            mostrarMensajeResultadoEJB(resNotificacion);
            inicializarGeneral();
        }else{
            mostrarMensajeResultadoEJB(resNotificacion);
        }
    }
    
}
