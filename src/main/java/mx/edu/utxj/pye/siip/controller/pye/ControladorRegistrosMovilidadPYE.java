/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
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
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoRegistrosMovilidad;
import mx.edu.utxj.pye.siip.dto.ca.DtoMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.ca.DtoMovilidadEstudiantil;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTORegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbRegistroMovilidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named(value = "registrosMovilidadPYE")
@ViewScoped
public class ControladorRegistrosMovilidadPYE implements Serializable{

    private static final long serialVersionUID = -1812458131594012186L;
    
    @Getter @Setter DtoRegistrosMovilidad dto;
    @Getter @Setter DtoMovilidadDocente dtodoc;
    @Getter @Setter DtoMovilidadEstudiantil dtoest;
    
    @EJB EjbRegistroMovilidad ejb;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbCatalogos ejbCatalogos;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPlantillasCAExcel ejbPlantillasCAExcel;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @EJB Facade f;
    
    //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoRegistrosMovilidad();  
        dtodoc = new DtoMovilidadDocente();
        dtoest = new DtoMovilidadEstudiantil();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorRegistrosMovilidadPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 69;
        consultarPermiso();
    }
    
    public void initFiltros(){
        llenaCategorias();
        llenaAreas();
        llenaPeriodosMeses();
        cargarListaPorEvento();
        Faces.setSessionAttribute("categorias", dto.getListaCategoriasPOA());
    }
  
     public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa());
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(dto.getListaCategoriasPOA().get(0));
        }
    }
    
    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getAreaPOA()== null) {
            dto.setAreaPOA(dto.getListaAreasPOA().get(0));
        }
    }
    
    public void llenaPeriodosMeses() {
        dto.setPeriodos(ejb.getPeriodosConregistro());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual());
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorRegistrosMovilidadPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarAreas(ValueChangeEvent e){
        dto.setCategoria((Categorias)e.getNewValue());
        llenaAreas();
        llenaPeriodosMeses();
        cargarListaPorEvento();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
     
    public void actualizarPeriodosMeses(ValueChangeEvent e){
        dto.setAreaPOA((AreasUniversidad)e.getNewValue());
        llenaPeriodosMeses();
        cargarListaPorEvento();
    }
    
   
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    
    
    public void cargarListaPorEvento(){
       dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo()));
       dtodoc.setLista(ejb.getListaRegistrosPorEventoAreaPeriodoDoc(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo()));
       dtoest.setLista(ejb.getListaRegistrosPorEventoAreaPeriodoEst(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo()));
        Ajax.update("formMuestraDatosActivos");
        Ajax.update("formMovDoc");
        Ajax.update("formMovEst");
    }
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getRegistrosMovilidad().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTORegistroMovilidad registro){
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getRegistrosMovilidad().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
  
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getRegistrosMovilidad().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            cargarListaPorEvento();
            Ajax.update("frmEvidencias");
            Ajax.update("formMuestraDatosActivos");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
      
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTORegistroMovilidad registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getRegistrosMovilidad().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            cargarListaPorEvento();
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
  
    public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
    
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
    
    public void abrirAlineacionPOA(DTORegistroMovilidad registro){
        try {
            dto.setRegistro(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getRegistrosMovilidad().getRegistro()));
            actualizarEjes(dto.getRegistro().getRegistrosMovilidad().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDesercionPeriodosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getRegistrosMovilidad().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistro().getRegistrosMovilidad().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
                dto.getRegistro().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getRegistrosMovilidad().getRegistro()));
                actualizarEjes(dto.getRegistro().getRegistrosMovilidad().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorDesercionPeriodosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
   
}
