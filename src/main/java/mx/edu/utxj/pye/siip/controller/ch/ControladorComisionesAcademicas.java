/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ch;

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
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComisionesAcademicas;
import mx.edu.utxj.pye.siip.dto.ch.DtoComisionesAcademicas;
import mx.edu.utxj.pye.siip.dto.ch.DtoParticipantesComAcad;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComAcadParticipantes;
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
@Named(value = "comisionesAcad")
@ViewScoped
public class ControladorComisionesAcademicas implements Serializable{

    private static final long serialVersionUID = 7452372662288721017L;
    //Variables para almacenar el registro
    @Getter @Setter DtoComisionesAcademicas dto;
    @Getter @Setter DtoParticipantesComAcad dtopart;
    
    @EJB EJBSelectItems ejbItems;
    @EJB EjbComisionesAcademicas ejbComisionesAcademicas;
    @EJB EjbComAcadParticipantes ejbComAcadParticipantes;
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
    @Inject ControladorComAcadParticipantes controladorComAcadParticipantes;
    
    @PostConstruct
    public void init(){
        dto = new DtoComisionesAcademicas();  
        dtopart = new  DtoParticipantesComAcad();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short)48);
        dto.setEje(new EjesRegistro());
        dto.getEje().setEje(2);
        
        consultaAreaRegistro();
        
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 48));   
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        dtopart.setRegistroTipo(new RegistrosTipo());
        dtopart.getRegistroTipo().setRegistroTipo((short)49);
        dtopart.setEje(new EjesRegistro());
        dtopart.getEje().setEje(2);
        
        consultaAreaRegistroParticipantes();
        
        dtopart.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dtopart.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
       
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
            dtopart.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initFiltros();
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 1;
        consultarPermiso();

    }
    
    public void consultaAreaRegistro() {
        AreasUniversidad areaRegistro = new AreasUniversidad();
        areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 1);
        if (areaRegistro == null) {
            dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).getArea());
        } else {
            dto.setArea(areaRegistro.getArea());
        }
    }
    
    public void consultaAreaRegistroParticipantes() {
        AreasUniversidad areaRegistro = new AreasUniversidad();
        areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 1);
        if (areaRegistro == null) {
            dtopart.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).getArea());
        } else {
            dtopart.setArea(areaRegistro.getArea());
        }
    }
    
    public void initFiltros(){
         
        if (dto.getSelectItemEjercicioFiscal() == null) {
            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 48).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 48, dto.getEjercicioFiscal()));
            filtroComisionesAcademicas(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
        }
        
        
    }
    
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCHExcel.getPlantillaComisionesAcademicas());
        Faces.sendFile(f, true);
    }
    
     /*
     * se inicializan los filtrados
     */
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 48, ejercicioFiscal));
//        filtroComisionesAcademicas(dto.getSelectItemMes().get(-1).getLabel(),ejercicioFiscal);
          filtroComisionesAcademicas(dto.getSelectItemMes().get(0).getLabel(),ejercicioFiscal);
    }

    public void filtroComisionesAcademicas(String mes, Short ejercicio) {
        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);
        dto.setLista(ejbComisionesAcademicas.getRegistroDTOComAcad(mes, ejercicio));
        dtopart.setLista(ejbComisionesAcademicas.getRegistroDTOPartComAcad(mes, ejercicio));
        if (dto.getLista().isEmpty() || dto.getLista()== null) {
            Messages.addGlobalWarn("No hay información registrada en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
      
    }
     
    public void listaComisionesAcademicasPrevia(String rutaArchivo) {
         try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejbComisionesAcademicas.getListaComisionesAcademicas(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
   
    public void guardaComisionesAcademicas() {
       if (dto.getLista() != null) {
            try {
                ejbComisionesAcademicas.guardaComisionesAcademicas(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
    
    public void abrirAlineacionPOA(DTOComisionesAcademicas registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getComisionesAcademicas().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
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
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getComisionesAcademicas().getRegistro());
        if (alineado) {
            filtroComisionesAcademicas(dto.getMes(), dto.getEjercicioFiscal());
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        } else {
            Messages.addGlobalError("El registro no pudo alinearse.");
        }
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
   
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getComisionesAcademicas().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOComisionesAcademicas registro){
         return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getComisionesAcademicas().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getComisionesAcademicas().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getComisionesAcademicas().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getComisionesAcademicas().getRegistro(), evidencia);
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
    
    public void seleccionarRegistro(DTOComisionesAcademicas registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getComisionesAcademicas().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if (res.getKey()) {
            filtroComisionesAcademicas(dto.getMes(), dto.getEjercicioFiscal());
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        } else {
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(), String.valueOf(dto.getArchivos().size())));
        }
    }
     
  
    public void eliminarRegistro(Integer registro, String clave){
        
           List<Integer> registroParticipantes = new ArrayList<>();
           List<Integer> registroEvidencias = new ArrayList<>();
           try {
               registroParticipantes = ejbComisionesAcademicas.buscaRegistroParticipantesComAcad(clave);
               registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);

               if (registroParticipantes.size() > 0) {
                   List<Integer> registroEvidenciasPart = new ArrayList<>();
                   registroEvidenciasPart = ejbComisionesAcademicas.buscaRegistroEvidenciasPartComAcad(clave);

                   if (registroEvidenciasPart.size() > 0) {

                       ejbModulos.eliminarRegistroEvidencias(registroEvidenciasPart);

                   }
                   ejbModulos.eliminarRegistroParticipantes(registroParticipantes);
                   Ajax.update("formMuestraDatosActivos");
                   Ajax.update("formMuestraDatosActivosPCA");
               }

               if (registroEvidencias.size() > 0) {

                   ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                   Ajax.update("formMuestraDatosActivos");
               }

               ejbModulos.eliminarRegistro(registro);
               init();
               Ajax.update("formMuestraDatosActivos");
           } catch (Throwable ex) {
               Logger.getLogger(ControladorComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
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
                Ajax.update("formMuestraDatosActivosPCA");
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                initFiltros();
                Ajax.update("formMuestraDatosActivosPCA");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorComisionesAcademicas.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
          
     }
    
}
