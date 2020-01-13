/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.eb.DTOEficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.siip.dto.eb.DtoEficienciaTerminal;
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
public class ControladorEficienciaTerminalTitulacionRegistroPYE implements Serializable{

    private static final long serialVersionUID = -2757897393394368408L;
    
    @Getter @Setter DtoEficienciaTerminal dto;
    
    @Getter private Double promedioEficienciaTerminal, promedioTasaTitulacion, promedioRegistroDGP;
    
    @EJB    EjbEficienciaTerminalTitulacionRegistro     ejbEficienciaTerminalTitulacionRegistro;
    @EJB    EjbModulos                                  ejbModulos;
    @EJB    EjbFiscalizacion                            ejbFiscalizacion;
    @EJB    EjbCatalogos                                ejbCatalogos;
    
    @Inject     ControladorEmpleado             controladorEmpleado;
    @Inject     ControladorModulosRegistro      controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoEficienciaTerminal();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public void filtros(){
        llenaCategorias();
        dto.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dto.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dto.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dto.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dto.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaEficienciaTerminal();
    }
    
    public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 7 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria())
                .stream()
                .filter(area -> (short) 60 == area.getArea())
                .collect(Collectors.toList()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getAreaUniversidadPOA() == null) {
            dto.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistros(dto.getRegistros(), dto.getAreaUniversidadPOA()));
        if (!dto.getAniosConsulta().isEmpty()) {
            dto.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(), dto.getAreaUniversidadPOA()));
        if (!dto.getMesesConsulta().isEmpty()) {
            dto.setMesConsulta(null);
        }
    }
    
    public void buscaEficienciaTerminal() {
        if (dto.getMesConsulta() != null && !dto.getMesesConsulta().isEmpty()) {
            dto.setListaEficienciaTerminal(ejbEficienciaTerminalTitulacionRegistro.getFiltroEficienciaTerminalEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getListaEficienciaTerminal().stream().forEach((et) -> {
                et.getEficienciaTerminalTitulacionRegistro().setRegistros(ejbModulos.buscaRegistroPorClave(et.getEficienciaTerminalTitulacionRegistro().getRegistro()));
            });
        } else {
            dto.setListaEficienciaTerminal(Collections.EMPTY_LIST);
        }
        Ajax.update("formMuestraDatosActivos");
    }

    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaEficienciaTerminal();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
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
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
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
        dto.setEjes(ejbFiscalizacion.getEjes(ejercicio, dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
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
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistroPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dto.getRegistro().getEficienciaTerminalTitulacionRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
}
