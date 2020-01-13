/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
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
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPersonalCapacitado;
import mx.edu.utxj.pye.siip.dto.ch.DtoPersonalCapacitado;
import mx.edu.utxj.pye.siip.dto.ch.DtoParticipantesPerCap;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPersonalCapacitado;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPartPerCap;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbPlantillasCHExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "personalCap")
@ViewScoped
public class ControladorPersonalCapacitado implements Serializable{

    private static final long serialVersionUID = -3107456074086473233L;
    
    @Getter @Setter DtoPersonalCapacitado dto;
    @Getter @Setter DtoParticipantesPerCap dtopart;
    
    @EJB EjbPersonalCapacitado ejb;
    @EJB EjbPartPerCap ejbPart;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @EJB EjbPlantillasCHExcel ejbPlantillasCHExcel;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
   
    @EJB Facade f;
     
    //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistro;
    
    @Getter @Setter private PersonalCapacitado nuevoPerCap;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoPersonalCapacitado();  
        dtopart = new  DtoParticipantesPerCap();
        
        consultaAreaRegistro();
      
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea().getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 2;
        consultarPermiso();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 2);
            if (areaRegistro == null) {
                dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                dto.setArea(areaRegistro);
            }
        } catch (Exception ex) {
            System.out.println("ControladorPersonalCapacitado.consultaAreaRegistro: " + ex.getMessage());
        }
    }
   
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCHExcel.getPlantillaPersonalCapacitado());
        Faces.sendFile(f, true);
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
            Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    public void abrirAlineacionPOA(DTOPersonalCapacitado registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getPersonalCapacitado().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
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
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
//        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    } 
    
    public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getPersonalCapacitado().getRegistro());
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(), dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void cargarListaPorEvento(){
       dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo()));
       dtopart.setLista(ejb.getListaRegistrosPorEventoAreaPeriodoPart(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo()));
    }
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getPersonalCapacitado().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOPersonalCapacitado registro){
         return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getPersonalCapacitado().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getPersonalCapacitado().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getPersonalCapacitado().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getPersonalCapacitado().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
      
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOPersonalCapacitado registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getPersonalCapacitado().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
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
    
  
    public void listaPersonalCapacitadoPrevia(String rutaArchivo) {
         try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaPersonalCapacitado(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
   
    public void guardaPersonalCapacitado() {
      if (dto.getLista() != null) {
            try {
                ejb.guardaPersonalCapacitado(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getAreaPOA().getArea(), controladorModulosRegistro.getEventosRegistros());
                Ajax.update("formMuestraDatosActivos");
                Ajax.update("formMuestraDatosActivosPPC");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
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
        
           List<Integer> registroParticipantes = new ArrayList<>();
           List<Integer> registroEvidencias = new ArrayList<>();
           try {
               registroParticipantes = ejb.buscaRegistroParticipantesPersonalCapacitado(clave);
               registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);

               if (registroParticipantes.size() > 0) {
                   List<Integer> registroEvidenciasPart = new ArrayList<>();
                   registroEvidenciasPart = ejb.buscaRegistroEvidenciasPartPersonalCap(clave);

                   if (registroEvidenciasPart.size() > 0) {

                       ejbModulos.eliminarRegistroEvidencias(registroEvidenciasPart);

                   }
                   ejbModulos.eliminarRegistroParticipantes(registroParticipantes);
                   initFiltros();
                   Ajax.update("formMuestraDatosActivos");
                   Ajax.update("formMuestraDatosActivosPPC");
               }

               if (registroEvidencias.size() > 0) {

                   ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                   initFiltros();
                   Ajax.update("formMuestraDatosActivos");
               }

               ejbModulos.eliminarRegistro(registro);
               initFiltros();
               Ajax.update("formMuestraDatosActivos");

           } catch (Throwable ex) {
               Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
               Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
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
                Ajax.update("formMuestraDatosActivosPPC");
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                initFiltros();
                Ajax.update("formMuestraDatosActivosPPC");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorPerCapParticipantes.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
          
     }
     
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
    
    public void editarRegistro(DTOPersonalCapacitado registro) {
        dto.setRegistro(registro);
        nuevoPerCap = dto.getRegistro().getPersonalCapacitado();
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionDialogo();
    }

    public void forzarAperturaEdicionDialogo() {
        if (dto.getForzarAperturaDialogo()) {
            Ajax.oncomplete("PF('modalEdicion').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }

    public void guardarEdicion() {
        try {
            nuevoPerCap = ejb.actualizarPerCap(nuevoPerCap);
            Ajax.update("formMuestraDatosActivos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPersonalCapacitado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
