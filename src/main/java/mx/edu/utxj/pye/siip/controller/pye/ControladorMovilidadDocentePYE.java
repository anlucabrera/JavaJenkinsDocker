/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.ca.DtoMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.ca.DtoRegistrosMovilidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbMovilidadDocente;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "movilidadDocentePYE")
@ViewScoped
public class ControladorMovilidadDocentePYE implements Serializable{

    private static final long serialVersionUID = 3563443106557324245L;
    
    @Getter @Setter DtoMovilidadDocente dto;
    @Getter @Setter DtoRegistrosMovilidad dtoreg;
   
    @EJB EjbMovilidadDocente ejb;
    @EJB EjbRegistroMovilidad ejbreg;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    @Inject ControladorRegistrosMovilidadPYE  controladorRegistrosMovilidad;
    
     @EJB Facade f;
    
    @PostConstruct
    public void init(){
        dto = new DtoMovilidadDocente();        
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorRegistrosMovilidadPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
     
     /* Se agregó para Evidencias por Participante  */
    public void subirEvidencias(){
       Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getRegistroMovilidadDocente().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            controladorRegistrosMovilidad.cargarListaPorEvento();
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
     public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getRegistroMovilidadDocente().getRegistro()));
        Ajax.update("frmEvidenciasDoc");
    }
     
    public void consultarEvidencias(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getRegistroMovilidadDocente().getRegistro()));
        Ajax.update("frmEvidenciasDoc");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOMovilidadDocente registro){
         return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getRegistroMovilidadDocente().getRegistro());
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getRegistroMovilidadDocente().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidenciasDoc");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaDoc').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOMovilidadDocente registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    
     public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
     
     /* Se agregó para Alineación a Poa Participantes*/
    
    public Boolean verificaAlineacion(Integer registro) throws Throwable{
        return ejbModulos.verificaActividadAlineadaGeneral(registro);
    }
    
    public void actualizarEjes(Short ejercicio){
        dto.setEjes(ejbFiscalizacion.getEjes(ejercicio, dto.getAreaPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(DTOMovilidadDocente registro){
        try {
            dto.setRegistro(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getRegistroMovilidadDocente().getRegistro()));
            actualizarEjes(dto.getRegistro().getRegistroMovilidadDocente().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorMovilidadDocentePYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getRegistroMovilidadDocente().getRegistro());
        if(alineado){
            controladorRegistrosMovilidad.cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistro().getRegistroMovilidadDocente().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
                dto.getRegistro().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getRegistroMovilidadDocente().getRegistro()));
                actualizarEjes(dto.getRegistro().getRegistroMovilidadDocente().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorMovilidadDocentePYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
}
