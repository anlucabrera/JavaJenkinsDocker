/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import mx.edu.utxj.pye.siip.controller.eb.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
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
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoInternetCPE;
import mx.edu.utxj.pye.siip.dto.eb.DtoDistribucionEquipamiento;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionEquipamiento;
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
public class ControladorDistribucionEquipamientoPYE implements Serializable{

    private static final long serialVersionUID = -4519318290264813424L;
    
    @Getter @Setter DtoDistribucionEquipamiento dto;
    
    @EJB    EjbDistribucionEquipamiento     ejb;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbCatalogos                    ejbCatalogos;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoDistribucionEquipamiento();
        dto.setAreaPOA(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
    }
    
    public void actualizaPeriodosEscolares(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegistroTipoEqCom(),dto.getRegistroTipoEqComInt(),dto.getEventoActual(),dto.getAreaUniversidadPOA()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegistroTipoEqCom(),dto.getRegistroTipoEqComInt(),dto.getAreaUniversidadPOA());
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
        dto.setListaEquiposComputo(ejb.getListaEquiposComputoPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipoEqCom()));
        dto.getListaEquiposComputo().stream().forEach((eccpe) -> {
            eccpe.getEquiposComputoCicloPeriodoEscolar().setRegistros(ejbModulos.buscaRegistroPorClave(eccpe.getEquiposComputoCicloPeriodoEscolar().getRegistro()));
        });
        
        dto.setListaEquiposComputoInternet(ejb.getListaEquiposComputoInternetPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipoEqComInt()));
        dto.getListaEquiposComputoInternet().stream().forEach((ecicpe) -> {
            ecicpe.getEquiposComputoInternetCicloPeriodoEscolar().setRegistros(ejbModulos.buscaRegistroPorClave(ecicpe.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        llenaAreasAcademicas();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void llenaAreasAcademicas(){
        Categorias cat = new Categorias((short) 4);
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(cat)
                .stream()
                .filter(area -> (short) 4 == area.getArea())
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
    
    public void forzarAperturaEvidenciasDialogoECI(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaECI').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    ///////////////////////////////////// Guardado, Edición y Eliminación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void eliminarRegistro(DTOEquiposComputoCPE registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getEquiposComputoCicloPeriodoEscolar().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getEquiposComputoCicloPeriodoEscolar().getRegistro()));
            Boolean eliminado = ejbModulos.eliminarRegistro(registro.getEquiposComputoCicloPeriodoEscolar().getRegistro());
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarRegistroECI(DTOEquiposComputoInternetCPE registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro()));
            Boolean eliminado = ejbModulos.eliminarRegistro(registro.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro());
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////////////////////// Evidencias \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroECI(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistro()));
            Ajax.update("frmEvidenciasECI");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOEquiposComputoCPE registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro.getEquiposComputoCicloPeriodoEscolar().getRegistro());
    }
    
    public List<EvidenciasDetalle> consultarEvidenciasECI(DTOEquiposComputoInternetCPE registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaECI(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroECI();
            Ajax.update("frmEvidenciasECI");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void seleccionarRegistro(DTOEquiposComputoCPE registro){
        dto.setRegistroEquiposComputoCPE(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void seleccionarRegistroECI(DTOEquiposComputoInternetCPE registro){
        dto.setRegistroEquiposInternetCPE(registro);
        cargarEvidenciasPorRegistroECI();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoECI();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasECI(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
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
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void actualizarEjesECI(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
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
    
    public void abrirAlineacionPOA(DTOEquiposComputoCPE registro){
        try {
            dto.setRegistroEquiposComputoCPE(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getEquiposComputoCicloPeriodoEscolar().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOAECI(DTOEquiposComputoInternetCPE registro){
        try {
            dto.setRegistroEquiposInternetCPE(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getEquiposComputoInternetCicloPeriodoEscolar().getRegistro()));
            actualizarEjesECI();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionECI");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionECI').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistroEquiposComputoCPE());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroECI(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOAECI(dto.getRegistroEquiposInternetCPE());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroEquiposComputoCPE().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getRegistro()));
                actualizarEjes();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarAlineacionECI() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroEquiposInternetCPE().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getRegistro()));
                actualizarEjesECI();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionECI");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDistribucionEquipamientoPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    
    /************************************* Edición - Equipo de computo **************************************/
    
    public void forzarAperturaEdicionEquipoComputo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionEquipoComputo').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionEquipoComputo(){
        Ajax.update("frmEdicionEquipoComputo");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionEquipoComputo();
    }
    
    public void abrirEdicionEquipoComputo(EquiposComputoCicloPeriodoEscolar eqCom) {
        DTOEquiposComputoCPE dtoEC = new DTOEquiposComputoCPE();
        dtoEC.setEquiposComputoCicloPeriodoEscolar(eqCom);
        
        dto.setRegistroEquiposComputoCPE(dtoEC);
        dto.setCicloEscolar(ejbModulos.buscaCicloEscolarEspecifico(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getCicloEscolar()));
        dto.setPeriodoEscolar(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar().getPeriodoEscolar()));
        
        dto.setCicloInicio(dateToCalendar(dto.getCicloEscolar().getInicio()).get(Calendar.YEAR));
        dto.setCicloFin(dateToCalendar(dto.getCicloEscolar().getFin()).get(Calendar.YEAR));
        dto.setMensaje("");
        
        actualizaInterfazEdicionEquipoComputo();
    }
    
    public void editaEquipoComputo(){
        if(ejb.buscaEquipoComputoExistente(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar())){
            dto.setMensaje("La distribución de equipo de cómputo que ha ingresado ya ha sido agregada anteriormente");
        }else{
            dto.getRegistroEquiposComputoCPE().setEquiposComputoCicloPeriodoEscolar(ejb.editaEquipoComputoCicloPeriodoEscolar(dto.getRegistroEquiposComputoCPE().getEquiposComputoCicloPeriodoEscolar()));
            dto.setMensaje("La distribución de equipo de cómputo ha sido actualizado correctamente");
            actualizaInterfazEdicionEquipoComputo();
        }
        Ajax.update("mensaje");
    }
    
    /************************************* Edición - Equipo de computo con internet **************************************/
    
    public void forzarAperturaEdicionEquipoComputoInternet(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionEquipoComputoInternet').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionEquipoComputoInternet(){
        Ajax.update("frmEdicionEquipoComputoInternet");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionEquipoComputoInternet();
    }
    
    public void abrirEdicionEquipoComputoInternet(EquiposComputoInternetCicloPeriodoEscolar eqComInternet) {
        DTOEquiposComputoInternetCPE dtoECInternet = new DTOEquiposComputoInternetCPE();
        dtoECInternet.setEquiposComputoInternetCicloPeriodoEscolar(eqComInternet);
        
        dto.setRegistroEquiposInternetCPE(dtoECInternet);
        dto.setCicloEscolar(ejbModulos.buscaCicloEscolarEspecifico(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getCicloEscolar()));
        dto.setPeriodoEscolar(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar().getPeriodoEscolar()));
        
        dto.setCicloInicio(dateToCalendar(dto.getCicloEscolar().getInicio()).get(Calendar.YEAR));
        dto.setCicloFin(dateToCalendar(dto.getCicloEscolar().getFin()).get(Calendar.YEAR));
        dto.setMensaje("");
        
        actualizaInterfazEdicionEquipoComputoInternet();
    }
    
    public void editaEquipoComputoInternet(){
        if(ejb.buscaEquipoComputoInternetExistente(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar())){
            dto.setMensaje("La distribución de equipo de cómputo con internet que ha ingresado ya ha sido agregada anteriormente");
        }else{
            dto.getRegistroEquiposInternetCPE().setEquiposComputoInternetCicloPeriodoEscolar(ejb.editaEquipoComputoInternetCicloPeriodoEscolar(dto.getRegistroEquiposInternetCPE().getEquiposComputoInternetCicloPeriodoEscolar()));
            dto.setMensaje("La distribución de equipo de cómputo con internet ha sido actualizada correctamente");
            actualizaInterfazEdicionEquipoComputoInternet();
        }
        Ajax.update("mensaje");
    }
    
}
