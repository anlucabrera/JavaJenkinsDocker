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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.eb.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionLabTallCPE;
import mx.edu.utxj.pye.siip.dto.eb.DtoDistribucionInstalaciones;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
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
public class ControladorDistribucionInstalacionesPYE implements Serializable{

    private static final long serialVersionUID = 2179388892040170127L;
    
    @Getter @Setter DtoDistribucionInstalaciones dto;
    
    @EJB    EjbDistribucionInstalaciones    ejb;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbCatalogos                    ejbCatalogos;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @PostConstruct
    public void init() {
        dto = new DtoDistribucionInstalaciones();
        dto.setAreaPOA(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
    }
    
    public void actualizaPeriodosEscolares(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegTipoCapIns(),dto.getRegTipoDistAu(),dto.getRegTipoDistLabTal(),dto.getEventoActual(),dto.getAreaUniversidadPOA()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegTipoCapIns(),dto.getRegTipoDistAu(),dto.getRegTipoDistLabTal(),dto.getAreaUniversidadPOA());
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorMatriculaPeriodosEscolaresPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    public void cargarListaPorEvento(){
        dto.setListaCapIns(ejb.getListaCapacidadInstaladaPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegTipoCapIns()));
        dto.getListaCapIns().stream().forEach((ci) -> {
            ci.getCapacidadInstaladaCiclosEscolares().setRegistros(ejbModulos.buscaRegistroPorClave(ci.getCapacidadInstaladaCiclosEscolares().getRegistro()));
        });
        
        dto.setListaDisAulas(ejb.getListaDistribucionAulasPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegTipoDistAu()));
        dto.getListaDisAulas().stream().forEach((disau) -> {
            disau.getDistribucionAulasCicloPeriodosEscolares().setRegistros(ejbModulos.buscaRegistroPorClave(disau.getDistribucionAulasCicloPeriodosEscolares().getRegistro()));
        });
        
        dto.setListaDisLabTall(ejb.getListaDistribucionLabTallPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegTipoDistLabTal()));
        dto.getListaDisLabTall().stream().forEach((dislt) -> {
            dislt.getDistribucionLabtallCicloPeriodosEscolares().setRegistros(ejbModulos.buscaRegistroPorClave(dislt.getDistribucionLabtallCicloPeriodosEscolares().getRegistro()));
        });
        
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        llenaAreasAcademicas();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void llenaAreasAcademicas(){
        Categorias cat = new Categorias((short) 2);
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(cat)
                .stream()
                .filter(area -> (short) 2 == area.getArea())
                .collect(Collectors.toList()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getAreaUniversidadPOA() == null) {
            dto.setAreaUniversidadPOA(null);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoDA(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaDA').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoDLT(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaDLT').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    ///////////////////////////////////// Guardado, Edición y Eliminación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void eliminarRegistro(DTOCapacidadInstaladaCiclosEscolares registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getCapacidadInstaladaCiclosEscolares().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getCapacidadInstaladaCiclosEscolares().getRegistro()));
            Boolean eliminado = ejbModulos.eliminarRegistro(registro.getCapacidadInstaladaCiclosEscolares().getRegistro());
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarRegistroDA(DTODistribucionAulasCPE registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getDistribucionAulasCicloPeriodosEscolares().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getDistribucionAulasCicloPeriodosEscolares().getRegistro()));
            Boolean eliminado = ejbModulos.eliminarRegistro(registro.getDistribucionAulasCicloPeriodosEscolares().getRegistro());
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarRegistroDLT(DTODistribucionLabTallCPE registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getDistribucionLabtallCicloPeriodosEscolares().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getDistribucionLabtallCicloPeriodosEscolares().getRegistro()));
            Boolean eliminado = ejbModulos.eliminarRegistro(registro.getDistribucionLabtallCicloPeriodosEscolares().getRegistro());
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////////////////////// Evidencias \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroDA(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistro()));
            Ajax.update("frmEvidenciasDA");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroDLT(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistro()));
            Ajax.update("frmEvidenciasDLT");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOCapacidadInstaladaCiclosEscolares registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro.getCapacidadInstaladaCiclosEscolares().getRegistro());
    }
    
    public List<EvidenciasDetalle> consultarEvidenciasDA(DTODistribucionAulasCPE registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro.getDistribucionAulasCicloPeriodosEscolares().getRegistro());
    }
    
    public List<EvidenciasDetalle> consultarEvidenciasDLT(DTODistribucionLabTallCPE registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro.getDistribucionLabtallCicloPeriodosEscolares().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaDA(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroDA();
            Ajax.update("frmEvidenciasDA");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaDLT(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroDLT();
            Ajax.update("frmEvidenciasDLT");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void seleccionarRegistro(DTOCapacidadInstaladaCiclosEscolares registro){
        dto.setRegistroCapacidadInstalada(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void seleccionarRegistroDA(DTODistribucionAulasCPE registro){
        dto.setRegistroDistribucionAulas(registro);
        cargarEvidenciasPorRegistroDA();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoDA();
    }
    
    public void seleccionarRegistroDLT(DTODistribucionLabTallCPE registro){
        dto.setRegistroDistribucionLabTall(registro);
        cargarEvidenciasPorRegistroDLT();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoDLT();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasDA(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasDLT(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////////////////////// Alineación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA()));
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
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void actualizarEjesDA(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void actualizarEjesDLT(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
            
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void abrirAlineacionPOA(DTOCapacidadInstaladaCiclosEscolares registro){
        try {
            dto.setRegistroCapacidadInstalada(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getCapacidadInstaladaCiclosEscolares().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOADA(DTODistribucionAulasCPE registro){
        try {
            dto.setRegistroDistribucionAulas(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getDistribucionAulasCicloPeriodosEscolares().getRegistro()));
            actualizarEjesDA();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionDA");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionDA').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOADLT(DTODistribucionLabTallCPE registro){
        try {
            dto.setRegistroDistribucionLabTall(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getDistribucionLabtallCicloPeriodosEscolares().getRegistro()));
            actualizarEjesDLT();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionDLT");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionDLT').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistroCapacidadInstalada());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroDA(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOADA(dto.getRegistroDistribucionAulas());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroDLT(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOADLT(dto.getRegistroDistribucionLabTall());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroCapacidadInstalada().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroCapacidadInstalada().getCapacidadInstaladaCiclosEscolares().getRegistro()));
                actualizarEjes();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarAlineacionDA() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroDistribucionAulas().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroDistribucionAulas().getDistribucionAulasCicloPeriodosEscolares().getRegistro()));
                actualizarEjesDA();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionDA");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarAlineacionDLT() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroDistribucionLabTall().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroDistribucionLabTall().getDistribucionLabtallCicloPeriodosEscolares().getRegistro()));
                actualizarEjesDLT();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionDLT");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionInstalacionesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
