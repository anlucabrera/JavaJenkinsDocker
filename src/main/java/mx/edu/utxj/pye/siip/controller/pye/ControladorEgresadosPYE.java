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
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelOcupacionEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DtoEgresados;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
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
public class ControladorEgresadosPYE implements Serializable{
    private static final long serialVersionUID = 689975610878160616L;
    
    @Getter @Setter DtoEgresados dto;
    
    @EJB    EjbEgresados        ejb;
    @EJB    EjbModulos          ejbModulos;
    @EJB    EjbFiscalizacion    ejbFiscalizacion;
    @EJB    EjbCatalogos        ejbCatalogos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoEgresados();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filtros() {
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
        buscaEgresados();
    }
    
    public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 4 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria())
                .stream()
                .filter(area -> (short) 5 == area.getArea())
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
    
    public void buscaEgresados() {
        if (dto.getMesConsulta() != null && !dto.getMesesConsulta().isEmpty()) {
            dto.setListaActividadEgresado(ejb.getFiltroActividadEgresadoEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getListaActividadEgresado().stream().forEach((lae) -> {
                lae.getActividadEgresadoGeneracion().setRegistros(ejbModulos.buscaRegistroPorClave(lae.getActividadEgresadoGeneracion().getRegistro()));
            });

            dto.setListaActividadEconomicaEgresado(ejb.getFiltroActividadEconomicaEgresadoEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getListaActividadEconomicaEgresado().stream().forEach((laee) -> {
                laee.getActividadEconomicaEgresadoGeneracion().setRegistros(ejbModulos.buscaRegistroPorClave(laee.getActividadEconomicaEgresadoGeneracion().getRegistro()));
            });

            dto.setListaNivelOcupacionEgresado(ejb.getFiltroActividadNivelOcupacionEgresadoEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getListaNivelOcupacionEgresado().stream().forEach((lnoe) -> {
                lnoe.getNivelOcupacionEgresadosGeneracion().setRegistros(ejbModulos.buscaRegistroPorClave(lnoe.getNivelOcupacionEgresadosGeneracion().getRegistro()));
            });

            dto.setListaNivelIngresoEgresado(ejb.getFiltroNivelIngresoEgresadoEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getListaNivelIngresoEgresado().stream().forEach((lnie) -> {
                lnie.getNivelIngresosEgresadosGeneracion().setRegistros(ejbModulos.buscaRegistroPorClave(lnie.getNivelIngresosEgresadosGeneracion().getRegistro()));
            });
        } else {
            dto.setListaActividadEgresado(Collections.EMPTY_LIST);
            dto.setListaActividadEconomicaEgresado(Collections.EMPTY_LIST);
            dto.setListaNivelOcupacionEgresado(Collections.EMPTY_LIST);
            dto.setListaNivelIngresoEgresado(Collections.EMPTY_LIST);
        }
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaEgresados();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroAEE(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistro()));
            Ajax.update("frmEvidenciasAEE");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroNOE(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistro()));
            Ajax.update("frmEvidenciasNOE");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroNIE(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistro()));
            Ajax.update("frmEvidenciasNIE");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoAEE(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaAEE').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoNOE(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaNOE').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoNIE(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaNIE').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOActividadEgresadoGeneracion dtoAE){
        dto.setRegistroActividadEgresado(dtoAE);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }

    public void seleccionarRegistroAEE(DTOActividadEconomicaEgresadoG dtoAEE){
        dto.setRegistroActividadEconomicaEgresado(dtoAEE);
        cargarEvidenciasPorRegistroAEE();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoAEE();
    }
    
    public void seleccionarRegistroNOE(DTONivelOcupacionEgresadosG dtoNOE){
        dto.setRegistroNivelOcupacionEgresado(dtoNOE);
        cargarEvidenciasPorRegistroNOE();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoNOE();
    }
    
    public void seleccionarRegistroNIE(DTONivelIngresoEgresadosG dtoNIE){
        dto.setRegistroNivelngresoEgresado(dtoNIE);
        cargarEvidenciasPorRegistroNIE();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoNIE();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaEgresados();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasAEE(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaEgresados();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasNOE(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaEgresados();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasNIE(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaEgresados();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaAEE(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroAEE();
            Ajax.update("frmEvidenciasAEE");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaNOE(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroNOE();
            Ajax.update("frmEvidenciasNOE");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaNIE(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroNIE();
            Ajax.update("frmEvidenciasNIE");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    /****************************** Alineación de actividades con registros ***********************************************/
    
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
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
    
    public void abrirAlineacionPOA(DTOActividadEgresadoGeneracion dtoAEG){
        try {
            dto.setRegistroActividadEgresado(dtoAEG);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistro()));
            actualizarEjes(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOAAEE(DTOActividadEconomicaEgresadoG dtoAEE){
        try {
            dto.setRegistroActividadEconomicaEgresado(dtoAEE);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistro()));
            actualizarEjes(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionAEE");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionAEE').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOANOE(DTONivelOcupacionEgresadosG dtoNoe){
        try {
            dto.setRegistroNivelOcupacionEgresado(dtoNoe);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistro()));
            actualizarEjes(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionNOE");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionNOE').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOANIE(DTONivelIngresoEgresadosG dtoNIE){
        try {
            dto.setRegistroNivelngresoEgresado(dtoNIE);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistro()));
            actualizarEjes(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionNIE");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionNIE').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistro());
        if(alineado){
            buscaEgresados();
            abrirAlineacionPOA(dto.getRegistroActividadEgresado());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroAEE(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistro());
        if(alineado){
            buscaEgresados();
            abrirAlineacionPOAAEE(dto.getRegistroActividadEconomicaEgresado());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroNOE(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistro());
        if(alineado){
            buscaEgresados();
            abrirAlineacionPOANOE(dto.getRegistroNivelOcupacionEgresado());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroNIE(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistro());
        if(alineado){
            buscaEgresados();
            abrirAlineacionPOANIE(dto.getRegistroNivelngresoEgresado());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroActividadEgresado().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistro()));
                actualizarEjes(dto.getRegistroActividadEgresado().getActividadEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionAEE(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroActividadEconomicaEgresado().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistro()));
                actualizarEjes(dto.getRegistroActividadEconomicaEgresado().getActividadEconomicaEgresadoGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionAEE");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionNOE(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroNivelOcupacionEgresado().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistro()));
                actualizarEjes(dto.getRegistroNivelOcupacionEgresado().getNivelOcupacionEgresadosGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionNOE");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionNIE(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getRegistroNivelngresoEgresado().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistro()));
                actualizarEjes(dto.getRegistroNivelngresoEgresado().getNivelIngresosEgresadosGeneracion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacionNIE");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorEgresadosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
}
