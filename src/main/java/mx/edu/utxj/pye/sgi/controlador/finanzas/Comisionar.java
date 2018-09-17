/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.finanzas;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.Comision;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbDocumentosInternos;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.GastoTipo;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "comisionar")
@ViewScoped
public class Comisionar implements Serializable{
    @Getter @Setter private String origen = "Universidad Tecnológica de Xicotepec de Juárez",destino = "Coordinación General de Universidades Tecnológicas y Politécnicas",ruta;
    
    @Getter @Setter private Comision comision;
//    @Getter @Setter private Tramites tramite;
    
    @Getter private Double distancia;
        
    @Getter private final GastoTipo gastoTipoAnticipado = GastoTipo.ANTICIPADO, gastoTipoDevengado = GastoTipo.DEVENGADO;
    @Getter private final TramiteTipo tramiteTipoComision = TramiteTipo.COMISION, tramiteTipoProductoServicio = TramiteTipo.PRODUCTO_SERVICIO;
    
    @Inject LogonMB logon;
    @Inject Caster caster;
    @EJB EjbFiscalizacion ejb;
    @EJB EjbDocumentosInternos ejbDocumentosInternos;
    @EJB Facade f;
    
    @PostConstruct
    public void init(){
        comision = ejb.inicializarComision(logon.getPersonal());
        comision.setAreaPOA(ejb.getAreaConPOA(logon.getPersonal().getAreaOperativa()));
        comision.setClavesAreasSubordinadas(ejb.getAreasSubordinadasSinPOA(comision.getAreaPOA()));
        Ajax.update("area","eje");
        Faces.setSessionAttribute("ejes", comision.getEjes());
        Faces.setSessionAttribute("areas", comision.getAreas());
        Faces.setSessionAttribute("estados", comision.getEstados());
        Faces.setSessionAttribute("municipios", comision.getMunicipios());
        Faces.setSessionAttribute("docentesActivos", comision.getPosiblesComisionados());
        
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        comision.setAlineacionLinea((LineasAccion)event.getNewValue());
        comision.setActividades(ejb.getActividadesPorLineaAccion(comision.getAlineacionLinea(), comision.getAreaPOA()));
        Faces.setSessionAttribute("actividades", comision.getActividades());
    }
    
    public void actualizarEjes(ValueChangeEvent event){
        comision.setAlineacionArea((AreasUniversidad)event.getNewValue());
        comision.setClavesAreasSubordinadas(ejb.getAreasSubordinadasSinPOA(comision.getAlineacionArea()));
        comision.setEjes(ejb.getEjes(Short.valueOf(caster.getEjercicioFiscal()), comision.getAreaPOA()));
        if(!comision.getEjes().isEmpty()){
            comision.setAlineacionEje(comision.getEjes().get(0));
            comision.setEstrategias(ejb.getEstrategiasPorEje(comision.getAlineacionEje(), comision.getAreaPOA()));
            comision.setPosiblesComisionados(ejb.getPosiblesComisionados(logon.getPersonal().getAreaOperativa(), comision.getAlineacionArea().getArea()));
        }
        comision.setAlineacionEje(null);
        Faces.setSessionAttribute("ejes", comision.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        comision.setAlineacionEje((EjesRegistro)event.getNewValue());
        comision.setEstrategias(ejb.getEstrategiasPorEje(comision.getAlineacionEje(), comision.getAreaPOA()));
        comision.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", comision.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        comision.setAlineacionEstrategia((Estrategias)event.getNewValue());
        comision.setLineasAccion(ejb.getLineasAccionPorEstrategia(comision.getAlineacionEstrategia(), comision.getAreaPOA()));
        comision.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", comision.getLineasAccion());
    }
    
    public void actualizarMunicipios(ValueChangeEvent event){
        comision.setEstado((Estado)event.getNewValue());
        comision.setMunicipios(ejb.getMunicipiosPorEstado(comision.getEstado()));
        Faces.setSessionAttribute("municipios", comision.getMunicipios());
    }
    
    public void actualizarDestino(){
        destino = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("destino");
    }

    public void actualizarDistancia(){
        distancia = Double.parseDouble(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("distancia"));
    }

    public void actualizarOrigen(){
        origen = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("origen");
    }
    
    public void guardarComisión() throws IOException{
        try{
            ejb.guardarTramite(comision.getTramite(), distancia);
            Faces.redirect("finanzas/tramites.xhtml");
//            return "tramites?faces-redirect=true";
        }catch(EJBException ex){
            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Comisionar.guardarComisión() No se pudo guardar el trámite.");
            LOG.log(Level.SEVERE, null, ex);
        }
//        return null;
    }
    
    public String guardarComisión2(){
        try{
            ejb.guardarTramite(comision.getTramite(), distancia);
//            Faces.redirect("tramites.xhtml");
            return "tramites?faces-redirect=true";
        }catch(EJBException ex){
            System.out.println("mx.edu.utxj.pye.sgi.controlador.finanzas.Comisionar.guardarComisión() No se pudo guardar el trámite.");
            LOG.log(Level.SEVERE, null, ex);
        }
        return null;
    }
    private static final Logger LOG = Logger.getLogger(Comisionar.class.getName());
}