/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.eb.DTOEficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoEficienciaTerminal;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorEficienciaTerminalTitulacionRegistro implements Serializable{

    private static final long serialVersionUID = -2757897393394368408L;
    
    @Getter @Setter DtoEficienciaTerminal dto;
    
    @Getter private Double promedioEficienciaTerminal, promedioTasaTitulacion, promedioRegistroDGP;
    
    @EJB    EjbEficienciaTerminalTitulacionRegistro     ejbEficienciaTerminalTitulacionRegistro;
    @EJB    EjbModulos                                  ejbModulos;
    @EJB    EjbFiscalizacion                            ejbFiscalizacion;
    
    @Inject     ControladorEmpleado             controladorEmpleado;
    @Inject     ControladorModulosRegistro      controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoEficienciaTerminal();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
    }
    
    public Double calculaEficienciaTerminal(Integer egresados, Integer nuevoIngreso){
        if(egresados > 0 || nuevoIngreso > 0){
            promedioEficienciaTerminal = ((double)egresados / (double)nuevoIngreso) * (double)100;
        }else{
            promedioEficienciaTerminal = 0.0;
        }
        promedioEficienciaTerminal = Math.round(promedioEficienciaTerminal * 100) / (double)100;
        return promedioEficienciaTerminal;
    }
    public Double calculaTasaTitulacion(Integer titulados, Integer egresados){
        if(titulados > 0 || egresados > 0){
            promedioTasaTitulacion = ((double)titulados / (double)egresados) * (double)100;
        }else{
            promedioTasaTitulacion = 0.0;
        }
        promedioTasaTitulacion = Math.round(promedioTasaTitulacion * 100) / (double)100;
        return promedioTasaTitulacion;
    }
    public Double calculaRegistroDGP(Integer registradosDGP, Integer titulados){
        if(registradosDGP > 0 || titulados > 0){
            promedioRegistroDGP = ((double)registradosDGP / (double)titulados) * (double)100;
        }else{
            promedioRegistroDGP = 0.0;
        }
        promedioRegistroDGP = Math.round(promedioRegistroDGP * 100) / (double)100;
        return promedioRegistroDGP;
    }
    
    public void listaEficienciaTerminalTitulacionRegistroPrevia(String rutaArchivo){
        try {
            if (rutaArchivo != null) {
                dto.setRutaArchivo(rutaArchivo);
                dto.setListaEficienciaTerminal(ejbEficienciaTerminalTitulacionRegistro.getListaEficienciaTerminalTitulacionRegistros(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                dto.setRutaArchivo(null);
            }
        }
    }
    
    public void guardaEficienciaTerminalTitulacion(){
        if (dto.getListaEficienciaTerminal() != null) {
            try {
                ejbEficienciaTerminalTitulacionRegistro.guardaEficienciaTerminalTitulacionRegistros(dto.getListaEficienciaTerminal(), dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
                if (dto.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
                    dto.setRutaArchivo(null);
                }
            } finally {
                dto.getListaEficienciaTerminal().clear();
                dto.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        dto.getListaEficienciaTerminal().clear();
        if (dto.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
            dto.setRutaArchivo(null);
        }
    }
    
    public void filtros(){
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistros(dto.getRegistros(), dto.getArea()));
        if (!dto.getAniosConsulta().isEmpty()) {
            dto.setAnioConsulta((short) dto.getAniosConsulta().get(dto.getAniosConsulta().size() - 1));
        }
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(), dto.getArea()));
        if (!dto.getMesesConsulta().isEmpty()) {
            dto.setMesConsulta(dto.getMesesConsulta().stream()
                    .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                    .findAny()
                    .orElse(dto.getMesesConsulta().get(dto.getMesesConsulta().size() - 1)));
        }
        buscaEficienciaTerminal();
    }
    
    public void actualizarMeses(ValueChangeEvent e) {
        dto.setAnioConsulta((short) e.getNewValue());
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(),dto.getArea()));
        buscaEficienciaTerminal();
    }
    
    public void buscaEficienciaTerminal() {
        dto.setListaEficienciaTerminal(ejbEficienciaTerminalTitulacionRegistro.getFiltroEficienciaTerminalEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getArea().getArea()));
        dto.getListaEficienciaTerminal().stream().forEach((et) -> {
            et.getEficienciaTerminalTitulacionRegistro().setRegistros(ejbModulos.buscaRegistroPorClave(et.getEficienciaTerminalTitulacionRegistro().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            filtros();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
            addDetailMessage("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOEficienciaTerminalTitulacionRegistro dtoEficienciaTerminal){
        dto.setRegistro(dtoEficienciaTerminal);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaEficienciaTerminal();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    /****************************** Alineación de actividades con registros ***********************************************/
    
    /**
     * 
     * @param registro
     * @return
     * @throws Throwable 
     */ 
    public Boolean verificaAlineacion(Integer registro) throws Throwable{
        return ejbModulos.verificaActividadAlineadaGeneral(registro);
    }
    
    public void actualizarEjes(Short ejercicio){
        dto.setEjes(ejbFiscalizacion.getEjes(ejercicio, dto.getArea()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getArea()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getArea()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getArea()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getArea()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(DTOEficienciaTerminalTitulacionRegistro eficienciaTermina){
        try {
            dto.setRegistro(eficienciaTermina);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistro()));
            actualizarEjes(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistro());
        if(alineado){
            buscaEficienciaTerminal();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            try {
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
}
