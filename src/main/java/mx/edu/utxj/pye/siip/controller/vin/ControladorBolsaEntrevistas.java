/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoBolsaTrabajo;
import mx.edu.utxj.pye.siip.dto.vin.DtoBolsaEntrevistas;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOBolsaEntrevistas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaEntrevistas;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "entBolTrab")
@ViewScoped
public class ControladorBolsaEntrevistas implements Serializable{

    private static final long serialVersionUID = -4428992189523224082L;
    
    @Getter @Setter DtoBolsaEntrevistas dto;
    @Getter @Setter DtoBolsaTrabajo dtoreg;
   
    @EJB EjbBolsaEntrevistas ejb;
    @EJB EjbBolsaTrabajo ejbreg;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    @Inject ControladorBolsaTrabajo controladorBolsaTrabajo;
    
    @EJB Facade f;
    
    @Getter @Setter private BolsaTrabajoEntrevistas nuevaEntBolTrab;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoBolsaEntrevistas();        
        
        consultaAreaRegistro(); 
      
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea().getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorBolsaEntrevistas.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorBolsaEntrevistas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 23);
            if (areaRegistro == null) {
                dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                dto.setArea(areaRegistro);
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorBolsaEntrevistas.consultaAreaRegistro(): " + e.getMessage());
        }
    }

    public void listaBolsaEntrevistasPrevia(String rutaArchivo) {
         try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaBolsaEntrevistas(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorBolsaEntrevistas.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
     
    public void guardaBolsaEntrevistas() {
     if (dto.getLista() != null) {
            try {
                ejb.guardaBolsaEntrevistas(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorBolsaEntrevistas.class.getName()).log(Level.SEVERE, null, ex);
                if (dto.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
                }
            } finally {
                dto.getLista().clear();
                dto.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
          dto.getLista().clear();
        if (dto.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
            dto.setRutaArchivo(null);
        }
    }
    
     /* Se agregó para Evidencias por Participante  */
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            controladorBolsaTrabajo.cargarListaPorEvento();
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
     public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro()));
        Ajax.update("frmEvidenciasPart");
    }
     
    public void consultarEvidencias(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro()));
        Ajax.update("frmEvidenciasPart");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOBolsaEntrevistas registro){
         return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getBolsaTrabajoEntrevistas().getRegistro());
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidenciasPart");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaPart').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOBolsaEntrevistas registro){
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
    
     public void abrirAlineacionPOA(DTOBolsaEntrevistas registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getBolsaTrabajoEntrevistas().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacionPart");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacionPart').show();");
    }
   
     public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionPart");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
      public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEjes(){
         dto.setEjes(ejbFiscalizacion.getEjes(dto.getEventoActual().getEjercicioFiscal().getAnio(), dto.getAreaPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
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
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());
            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
     public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getBolsaTrabajoEntrevistas().getRegistro());
        if(alineado){
            controladorBolsaTrabajo.cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
     
    public List<DTOBolsaEntrevistas> consultarEntrevistas(String bolsaTrab){
         return ejb.getListaEntrevistaBolsaTrabajo(bolsaTrab);
    }
    
    public void seleccionarEntrevistas(String clave){
        dto.setListaEntBolTrab(ejb.getListaEntrevistaBolsaTrabajo(clave));
        Ajax.update("frmModalEntrevistas");
        
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEntrevistasDialogo();
    }
    
    public void forzarAperturaEntrevistasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEntrevistas').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
      public void editarRegistro(DTOBolsaEntrevistas registro) {
        dto.setRegistro(registro);
        nuevaEntBolTrab = dto.getRegistro().getBolsaTrabajoEntrevistas();
        Ajax.update("frmModalEdicionEnt");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionDialogo();
    }

    public void forzarAperturaEdicionDialogo() {
        if (dto.getForzarAperturaDialogo()) {
            Ajax.oncomplete("PF('modalEdicionEnt').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }

    public void guardarEdicion() {
        try {
             nuevaEntBolTrab = ejb.actualizarEntBolTrab(nuevaEntBolTrab);
             Ajax.update("formMuestraDatosActivosEnt");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorBolsaEntrevistas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
