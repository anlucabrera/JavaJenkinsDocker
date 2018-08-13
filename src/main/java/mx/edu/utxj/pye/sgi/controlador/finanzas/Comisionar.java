/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.finanzas;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
import mx.edu.utxj.pye.sgi.entity.finanzas.ComisionOficios;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
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
        comision = new Comision(new Tramites());
        comision.setTipo(TramiteTipo.COMISION);
        comision.setGastoTipo(GastoTipo.ANTICIPADO);
        comision.setAlineacionArea(f.getEntityManager().find(AreasUniversidad.class, (short)logon.getPersonal().getAreaOperativa()));
        comision.setAreas(ejb.getAreasConPOA(Short.valueOf(caster.getEjercicioFiscal())));
        comision.setEjes(ejb.getEjes(Short.valueOf(caster.getEjercicioFiscal()), comision.getAlineacionArea().getArea()));
        comision.setEstados(ejb.getEstados());
        comision.setComisionado(logon.getPersonal());
        comision.setPosiblesComisionados(ejb.getPosiblesComisionados((short)logon.getPersonal().getAreaOperativa(), (short)comision.getComisionado().getAreaOperativa()));
        comision.getTramite().getComisionOficios().setGenerador(logon.getPersonal().getClave());
        comision.getTramite().getComisionOficios().setOficio(ejbDocumentosInternos.generarNumeroOficio(comision.getAlineacionArea(), ComisionOficios.class));
        comision.getTramite().getComisionOficios().setFechaGeneracion(new Date());
        comision.getTramite().getComisionOficios().setSuperior(ejb.getSuperior(logon.getPersonal()).getClave());
        comision.getTramite().getComisionOficios().getComisionAvisos().setZona((short)0);
        comision.getTramite().setClave(logon.getPersonal().getClave());
        comision.getTramite().setFolio((short)0);
        Ajax.update("area","eje");
        Faces.setSessionAttribute("ejes", comision.getEjes());
        Faces.setSessionAttribute("areas", comision.getAreas());
        Faces.setSessionAttribute("estados", comision.getEstados());
        Faces.setSessionAttribute("docentesActivos", comision.getPosiblesComisionados());
        comision.getTramite().setClave(logon.getPersonal().getClave()); //clave de la persona que da el seguimiento, puede o no ser la misma que el comisionado
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        comision.setAlineacionLinea((LineasAccion)event.getNewValue());
        comision.setActividades(ejb.getActividadesPorLineaAccion(comision.getAlineacionLinea(), comision.getAlineacionArea().getArea()));
        Faces.setSessionAttribute("actividades", comision.getActividades());
    }
    
    public void actualizarEjes(ValueChangeEvent event){
        comision.setAlineacionArea((AreasUniversidad)event.getNewValue());
        comision.setEjes(ejb.getEjes(Short.valueOf(caster.getEjercicioFiscal()), comision.getAlineacionArea().getArea()));
        if(!comision.getEjes().isEmpty()){
            comision.setAlineacionEje(comision.getEjes().get(0));
            comision.setEstrategias(ejb.getEstrategiasPorEje(comision.getAlineacionEje(), comision.getAlineacionArea().getArea()));
        }
        comision.setAlineacionEje(null);
        Faces.setSessionAttribute("ejes", comision.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        comision.setAlineacionEje((EjesRegistro)event.getNewValue());
        comision.setEstrategias(ejb.getEstrategiasPorEje(comision.getAlineacionEje(), comision.getAlineacionArea().getArea()));
        comision.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", comision.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        comision.setAlineacionEstrategia((Estrategias)event.getNewValue());
        comision.setLineasAccion(ejb.getLineasAccionPorEstrategia(comision.getAlineacionEstrategia(), comision.getAlineacionArea().getArea()));
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
    
    public void guardarComisión(){
        ejb.guardarTramite(comision.getTramite(), distancia);
    }
}