/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

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
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.view.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.controller.pye.ControladorFeriasParticipantesPYE;
import mx.edu.utxj.pye.siip.dto.vin.DtoFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasDTO;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbFeriasProfesiograficas;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named(value = "feriasPro")
@ManagedBean
@ViewScoped
public class ControladorFeriasProfesiograficas implements Serializable{

    private static final long serialVersionUID = 0L;
    @Getter @Setter DtoFeriasProfesiograficas dto;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EJBSelectItems ejbItems;
    @Inject ControladorFeriasParticipantes cfp;
    @EJB EjbPlantillasVINExcel ejbPlantillasVINExcel;
    //Variables para almacenar el registro
    @EJB EjbFeriasProfesiograficas ejbFeriasProfesiograficas;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
     //Variables para verificar permiso del usuario para visualizar apartado
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistroVIN, claveRegistroPYE;
        
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoFeriasProfesiograficas();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short) 34);
        dto.setEjesRegistro(new EjesRegistro());
        dto.getEjesRegistro().setEje(4);
        
        consultaAreaRegistro(); 
        if(dto.getArea() == null){
            return;
        }
        
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 34));
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea().getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        if (dto.getSelectItemEjercicioFiscal() == null) {
//            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 34).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 34, dto.getEjercicioFiscal()));
            filtroFeriaProf(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
        }
        cfp.init();
        
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistroVIN = 27;
        claveRegistroPYE = 64;
        consultarPermiso();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 27);
            if (areaRegistro == null) {
                ResultadoEJB<AreasUniversidad> area = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if(area.getCorrecto()){
                    dto.setArea(area.getValor());
                }else{
                    dto.setArea(null);
                }
            } else {
                dto.setArea(areaRegistro);
            }
        } catch (Exception ex) {
            dto.setArea(null);
        }
    }
    
    public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasVINExcel.getPlantillaFeriasProfesiograficas());
        Faces.sendFile(f, true);
    }
    /*
     * se inizializan los filtrados
     */
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 34, ejercicioFiscal));
        filtroFeriaProf(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
        cfp.seleccionarMes(ejercicioFiscal);
        cfp.filtroFeriaPart(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
    }

    public void filtroFeriaProf(String mes, Short ejercicio) {

        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);
        dto.setListaFeriasPDTO(ejbFeriasProfesiograficas.getRegistroFeriaProf(mes, ejercicio));
        if (dto.getListaFeriasPDTO() == null) {
            Messages.addGlobalWarn("No se han registrado Ferias Profesiográficas en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
        cfp.filtroFeriaPart(mes, ejercicio);
    }
    
    public void eliminarRegistro(Integer registro, String clave){
        
           List<Integer> registroParticipantes = new ArrayList<>();
           List<Integer> registroEvidencias = new ArrayList<>();
           try {
               registroParticipantes = ejbFeriasProfesiograficas.buscaRegistroParticipantesFeriasProf(clave);
               registroEvidencias = ejbEvidenciasAlineacion.buscaRegistroEvidenciasRegistro(registro);

               if (registroParticipantes.size() > 0) {
                   List<Integer> registroEvidenciasPart = new ArrayList<>();
                   registroEvidenciasPart = ejbFeriasProfesiograficas.buscaRegistroEvidenciasPartFeriasProf(clave);

                   if (registroEvidenciasPart.size() > 0) {

                       ejbModulos.eliminarRegistroEvidencias(registroEvidenciasPart);

                   }
                   ejbModulos.eliminarRegistroParticipantes(registroParticipantes);
                   init();
                   Ajax.update("formtab");
               }

               if (registroEvidencias.size() > 0) {

                   ejbModulos.eliminarRegistroEvidencias(registroEvidencias);
                   init();
                   Ajax.update("formtab");
               }

               ejbModulos.eliminarRegistro(registro);
               init();
               Ajax.update("formtab");

           } catch (Throwable ex) {
               Logger.getLogger(ControladorFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
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
                init();
                Ajax.update("formtab");
            }
            if(registroEvidencias.isEmpty()){
            
                ejbModulos.eliminarRegistro(registro);
                init();
                Ajax.update("formtab");
            }
            
        } catch (Throwable ex) {
            Logger.getLogger(ControladorFeriasParticipantes.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
     }
    
     public void abrirAlineacionPOA(ListaFeriasDTO registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getFeriasProfesiograficas().getRegistro()));
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
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
     public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getFeriasProfesiograficas().getRegistro());
        if(alineado){
            filtroFeriaProf(dto.getMes(), dto.getEjercicioFiscal());
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
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getFeriasProfesiograficas().getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(ListaFeriasDTO registro){
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getFeriasProfesiograficas().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getFeriasProfesiograficas().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getFeriasProfesiograficas().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getFeriasProfesiograficas().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
     public void seleccionarRegistro(ListaFeriasDTO registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getFeriasProfesiograficas().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            filtroFeriaProf(dto.getMes(), dto.getEjercicioFiscal());
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
    public void listaFeriasProfesiograficasPrevia(String rutaArchivo) {
        try {
            dto.setListaFeriasProfesiograficas(ejbFeriasProfesiograficas.getListaFeriasProfesiograficas(rutaArchivo));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardaFeriasProfesiograficas() {
        try {
            ejbFeriasProfesiograficas.guardaFeriasProfesiograficas(dto.getListaFeriasProfesiograficas(), dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFeriasProfesiograficas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        dto.getListaFeriasProfesiograficas().getFerias().clear();
    }
    
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistroEjesDistintos(clavePersonal, claveRegistroVIN, claveRegistroPYE);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
}
