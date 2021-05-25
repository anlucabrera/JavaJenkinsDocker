package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoReincorporacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ReincorporacionCalificacionesRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;



@Named
@ViewScoped
public class ReincorporacionCalificacionesServiciosEscolares extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter ReincorporacionCalificacionesRolServiciosEscolares rol;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    
    @EJB EjbReincorporacion ejb;
    @EJB EjbValidacionRol evr;
    @EJB EjbFichaAdmision efa;
    @EJB EjbPropiedades ep;
    
    @Inject LogonMB logonMB;

    



@PostConstruct
    public void init(){
        try {
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.REINCORPORACION);
            ResultadoEJB<Filter<PersonalActivo>> resAccesoEs = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
                                  
            if (!resAccesoEs.getCorrecto()){mostrarMensajeResultadoEJB(resAccesoEs);return;}
            
            Filter<PersonalActivo> filtroEs = resAccesoEs.getValor();//se obtiene el filtro resultado de la validación        
            
            PersonalActivo activoEs = filtroEs.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            
            rol = new ReincorporacionCalificacionesRolServiciosEscolares(filtroEs, activoEs, activoEs.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(activoEs);
            rol.setPersonalActivoSe(activoEs);
            
                        
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!tieneAcceso){mostrarMensajeNoAcceso();return;}
            rol.setNivelRol(NivelRol.OPERATIVO);
            
            rol.setCalificacionesR(new ArrayList<>());
            rol.setEstudiantesReincorporaciones(new ArrayList<>());
            rol.setTipoCal("Regulatoria");
            rol.setEstudiante(new Estudiante());
        
            ResultadoEJB<List<Estudiante>> resAcceso = ejb.getEstudiantesReincorporaciones(); 
            if (!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}   
            rol.setEstudiantesReincorporaciones(resAcceso.getValor());
        
            if (logonMB.getPer() != 0) {
                estudianteSeleccionado();
            }
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

// Validaciones Acceso
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "reincorporaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    public void estudianteSeleccionado(){    
        try {
            if (logonMB.getPer()!= 0) {                
                rol.getEstudiantesReincorporaciones().forEach((t) -> {
                    if (Objects.equals(logonMB.getPer(), t.getIdEstudiante())) {
                        rol.setEstudiante(t);
                    }
                });
            }
            buscarAlineacionCalificaciones();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void asignaEstudianteconsulta() {
        logonMB.setPer(rol.getEstudiante().getIdEstudiante());
        Faces.redirect("controlEscolar/se/registroCalificacionesReincorporaciones.xhtml");
    }
    
    public void buscarAlineacionCalificaciones(){          
        ResultadoEJB<List<DtoReincorporacion.AlineacionCalificaciones>> resAcceso = ejb.getAlineacionCalificaciones(rol.getEstudiante().getAspirante(),Boolean.TRUE,rol.getEstudiante().getMatricula()); 
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}
        rol.setCalificacionesR(resAcceso.getValor());        
    }
    
// Registro y Actualizacion informacion 
    
    public void guardaCalificaciones(ValueChangeEvent event) {
        String idC=event.getComponent().getId();
        String[] parts=idC.split("-");
        ResultadoEJB<CalificacionPromedio> rejb =ejb.registrarCalificacionesPorPromedio(Integer.parseInt(parts[2]),Integer.parseInt(parts[1]),Double.parseDouble(event.getNewValue().toString()),rol.getTipoCal());
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        buscarAlineacionCalificaciones();
    }
    
    public void guardaCalificacionesValidadas() {
        if (!rol.getCalificacionesR().isEmpty()) {
            rol.getCalificacionesR().forEach((t) -> {
                if (!t.getCalificacionesReincorporacions().isEmpty()) {
                    t.getCalificacionesReincorporacions().forEach((ca) -> {
                        ResultadoEJB<CalificacionPromedio> rejb = ejb.registrarCalificacionesPorPromedio(ca.getCalificacionPromedio().getCalificacionPromedioPK().getIdEstudiante(), ca.getCalificacionPromedio().getCalificacionPromedioPK().getCarga(), ca.getCalificacionPromedio().getValor(), "Oficial");
                    });
                }
            });
        } 
    }

    public void imprimirValores() {

    }
}
