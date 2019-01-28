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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoDifusionIems;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaDifusionIemsDTO;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbDifusionIems;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named(value = "difIems")
@ManagedBean
@ViewScoped
public class ControladorDifusionIems implements Serializable{

    private static final long serialVersionUID = 0L;
   
    @Getter @Setter DtoDifusionIems dto;
    @EJB EJBSelectItems ejbItems; 
    @EJB EjbDifusionIems ejbDifusionIems; 
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    @EJB EjbPlantillasVINExcel ejbPlantillasVINExcel;
    
    //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        dto = new DtoDifusionIems();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short)29);
        dto.setEjesRegistro(new EjesRegistro());
        dto.getEjesRegistro().setEje(4);
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 29));
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()));
        if (dto.getSelectItemEjercicioFiscal() == null) {
            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 29).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 29, dto.getEjercicioFiscal()));
            filtroIems(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
        }     
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorFeriasParticipantes.class.getName()).log(Level.SEVERE, null, ex);
        }
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 25;
        consultarPermiso();
        
    }
    
     public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasVINExcel.getPlantillaDifusionIEMS());
        Faces.sendFile(f, true);
    }
     /*
     * se inizializan los filtrados
     */
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 29, ejercicioFiscal));
        filtroIems(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
    }

    public void filtroIems(String mes, Short ejercicio) {

        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);
        dto.setListadoDIemsDTO(ejbDifusionIems.getRegistroDifusionIemsDTO(mes, ejercicio));

        if (dto.getListadoDIemsDTO().isEmpty() || dto.getListadoDIemsDTO() == null) {
            Messages.addGlobalWarn("no se encontraron actividades registradas en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
    }
    public void eliminarRegistro(Integer registro){
        
       List<Integer> registroEvidencias = new ArrayList<>();
        try {
            registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);
            
           
            if(registroEvidencias.size()>0){
            
                ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formMuestraDatosActivos");
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formMuestraDatosActivos");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorDifusionIems.class.getName()).log(Level.SEVERE, null, ex);
            addDetailMessage("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    
    public void listaDifusionIemsPrevia(String rutaArchivo) {
       try {
           dto.setListaDifusionIems(ejbDifusionIems.getListaDifusionIems(rutaArchivo));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDifusionIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void guardaDifusionIems() {
        try {
            ejbDifusionIems.guardaDifusionIems(dto.getListaDifusionIems(),dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDifusionIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        dto.setListaDifusionIems(null);
    }
    
    public void abrirAlineacionPOA(ListaDifusionIemsDTO registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getDifusion().getRegistro()));
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
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getDifusion().getRegistro());
        if(alineado){
            filtroIems(dto.getMes(), dto.getEjercicioFiscal());
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
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getDifusion().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(ListaDifusionIemsDTO registro){
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getDifusion().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getDifusion().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getDifusion().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getDifusion().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
     public void seleccionarRegistro(ListaDifusionIemsDTO registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getDifusion().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            filtroIems(dto.getMes(), dto.getEjercicioFiscal());
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
   
}
