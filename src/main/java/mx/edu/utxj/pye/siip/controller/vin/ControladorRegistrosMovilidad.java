/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Named(value = "registrosMovilidad")
@ViewScoped
public class ControladorRegistrosMovilidad implements Serializable{

    private static final long serialVersionUID = -1812458131594012186L;
    
    @Getter @Setter DtoRegistrosMovilidad dto;
    @Getter @Setter DtoMovilidadDocente dtodoc;
    @Getter @Setter DtoMovilidadEstudiantil dtoest;
    
    @EJB EjbRegistroMovilidad ejb;
    @EJB EjbFiscalizacion ejbFiscalizacion;
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
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()));
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorRegistrosMovilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 18;
        consultarPermiso();
    }
    
    public void initFiltros(){
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
            Logger.getLogger(ControladorRegistrosMovilidad.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
     public void abrirAlineacionPOA(DTORegistroMovilidad registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getRegistrosMovilidad().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
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
    
    public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getRegistrosMovilidad().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
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
    
    public void cargarListaPorEvento(){
       dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getArea(), dto.getPeriodo()));
       dtodoc.setLista(ejb.getListaRegistrosPorEventoAreaPeriodoDoc(dto.getEventoSeleccionado(), dto.getArea(), dto.getPeriodo()));
       dtoest.setLista(ejb.getListaRegistrosPorEventoAreaPeriodoEst(dto.getEventoSeleccionado(), dto.getArea(), dto.getPeriodo()));
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
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getRegistrosMovilidad().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getRegistrosMovilidad().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
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
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
   
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaRegistroMovilidad());
        Faces.sendFile(f, true);
    }
     
    public void listaRegistroMovilidadPrevia(String rutaArchivo) {
        try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaRegistroMovilidad(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorRegistrosMovilidad.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaRegistroMovilidad() {
       if (dto.getLista() != null) {
            try {
                ejb.guardaRegistroMovilidad(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorRegistrosMovilidad.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void eliminarRegistro(Integer registro, String clave){
       
       List<Integer> registroParticipantesDoc = new ArrayList<>();
       List<Integer> registroParticipantesEst = new ArrayList<>();
       List<Integer> registroEvidencias = new ArrayList<>();
        try {
            registroParticipantesDoc = ejb.buscaRegistroDocentesRegMovilidad(clave);
            registroParticipantesEst = ejb.buscaRegistroEstudiantesRegMovilidad(clave);
            registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);
         
            if (registroParticipantesDoc.size() > 0) {
                List<Integer> registroEvidenciasDoc = new ArrayList<>();
                registroEvidenciasDoc = ejb.buscaRegistroEvidenciasDocente(clave);

                if (registroEvidenciasDoc.size() > 0) {

                    ejbModulos.eliminarRegistroEvidencias(registroEvidenciasDoc);

                }
                ejbModulos.eliminarRegistroParticipantes(registroParticipantesDoc);
                Ajax.update("formMuestraDatosActivos");
                Ajax.update("formMovDoc");
                Ajax.update("formMovEst");
            }
            if (registroParticipantesEst.size() > 0) {
                List<Integer> registroEvidenciasEst = new ArrayList<>();
                registroEvidenciasEst = ejb.buscaRegistroEvidenciasEstudiante(clave);

                if (registroEvidenciasEst.size() > 0) {

                    ejbModulos.eliminarRegistroEvidencias(registroEvidenciasEst);

                }
                ejbModulos.eliminarRegistroParticipantes(registroParticipantesEst);
                Ajax.update("formMuestraDatosActivos");
                Ajax.update("formMovDoc");
                Ajax.update("formMovEst");
            }
            if (registroEvidencias.size() > 0) {
                ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                Ajax.update("formMuestraDatosActivos");
                Ajax.update("formMovDoc");
                Ajax.update("formMovEst");

            }
            
            ejbModulos.eliminarRegistro(registro);
            initFiltros();
            Ajax.update("formMuestraDatosActivos");
            Ajax.update("formMovDoc");
            Ajax.update("formMovEst");
            
            
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorRegistrosMovilidad.class.getName()).log(Level.SEVERE, null, ex);
            addDetailMessage("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
        
    }
    
    public void eliminarRegistroParticipante(Integer registro) {
     
     
       List<Integer> registroEvidencias = new ArrayList<>();
        try {
            registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);
            
            if(registroEvidencias.size()>0){
                
                ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                ejbModulos.eliminarRegistro(registro);
                initFiltros();
                Ajax.update("formMovDoc");
                Ajax.update("formMovEst");
                
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                initFiltros();
                Ajax.update("formMovDoc");
                Ajax.update("formMovEst");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorMovilidadDocente.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(ControladorMovilidadEstudiante.class.getName()).log(Level.SEVERE, null, ex);
            addDetailMessage("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
          
     }
    
    public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
   
}
