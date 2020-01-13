/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoDesercionReprobacion;
import mx.edu.utxj.pye.siip.dto.escolar.DTOReprobacion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoReprobacion;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionReprobacion;
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
@Named(value = "reprobacionPer")
@ManagedBean
@ViewScoped
public class ControladorDesercionReprobacion implements Serializable {

    private static final long serialVersionUID = -7311681428495274355L;
 /*Se agrega el dto que contiene las propiedades originales del controlador y el selectItem que maneja los filtros*/
    @Getter @Setter DtoDesercionReprobacion dto;
    @EJB EJBSelectItems ejbItems;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    @EJB EjbDesercionReprobacion ejbDesercionReprobacion;

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
        dto = new DtoDesercionReprobacion();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short) 26);
        dto.setEjesRegistro(new EjesRegistro());
        dto.getEjesRegistro().setEje(3);
        
        consultaAreaRegistro(); 
        
        /*INIT FILTRADO*/
        dto.setSelectItemEjercicioFiscal(ejbItems.itemEjercicioFiscalPorRegistro((short) 26));
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        if (dto.getSelectItemEjercicioFiscal() == null) {
//            Messages.addGlobalInfo("No existen registros");
        } else {
            dto.setEjercicioFiscal((short) ejbItems.itemEjercicioFiscalPorRegistro((short) 26).get(0).getValue());
            dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 26, dto.getEjercicioFiscal()));

            filtroReprobacion(dto.getSelectItemMes().get(0).getLabel(), dto.getEjercicioFiscal());
        }
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorDesercionReprobacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorDesercionReprobacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        /*FIN DEL INIT FILTRADO*/

    }

    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 13);
            if (areaRegistro == null) {
                areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 41);
                if (areaRegistro == null) {
                    dto.setArea((ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())).getArea());
                } else {
                    dto.setArea(areaRegistro.getArea());
                }
            } else {
                dto.setArea(areaRegistro.getArea());
            }
        } catch (Exception ex) {
            System.out.println("ControladorDesercionReprobacion.consultaAreaRegistro: " + ex.getMessage());
        }
    }
    

    /*INICIO DEL FILTRADO Y ELIMINACION*/
    public void seleccionarMes(Short ejercicioFiscal) {
        dto.setSelectItemMes(ejbItems.itemMesesPorRegistro((short) 26, ejercicioFiscal));
        filtroReprobacion(dto.getSelectItemMes().get(0).getLabel(), ejercicioFiscal);
    }

    public void filtroReprobacion(String mes, Short ejercicio) {
        dto.setMes(mes);
        dto.setEjercicioFiscal(ejercicio);
        dto.setListaDtoReprobacion(ejbDesercionReprobacion.getListaRegistrosReprobacionDto(mes, ejercicio));
        if (dto.getListaDtoReprobacion() == null) {
            Messages.addGlobalWarn("No hay registros de Deserción por Reprobación en el mes " + mes + " y el ejercicio fiscal " + ejercicio);
        }
    }
    
    public void abrirAlineacionPOA(ListaDtoReprobacion registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getDesercionReprobacionMaterias().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion1");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion1').show();");
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
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
     public void alinearRegistro(){
        Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getDesercionReprobacionMaterias().getRegistro());
        if(alineado){
            filtroReprobacion(dto.getMes(), dto.getEjercicioFiscal());
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getDesercionReprobacionMaterias().getRegistro()));
        Ajax.update("frmEvidencias1");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(ListaDtoReprobacion registro){
        return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getDesercionReprobacionMaterias().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getDesercionReprobacionMaterias().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getDesercionReprobacionMaterias().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion1");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getDesercionReprobacionMaterias().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias1");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
     public void seleccionarRegistro(ListaDtoReprobacion registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
       Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getDesercionReprobacionMaterias().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if(res.getKey()){ 
            filtroReprobacion(dto.getMes(), dto.getEjercicioFiscal());
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia1').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }


    /*FIN DEL FILTRADO Y ELIMINACION*/
    public void listaDesercionReprobacionPrevia(String rutaArchivo) {
        try {
            dto.setListaDesercionReprobacion(ejbDesercionReprobacion.getListaDesercionReprobacion(rutaArchivo));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDesercionReprobacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardaDesercionReprobacion() {
        try {
            ejbDesercionReprobacion.guardaDesercionReprobacion(dto.getListaDesercionReprobacion(), dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDesercionReprobacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cancelarArchivo() {
        dto.getListaDesercionReprobacion().getReprobacion().clear();

    }
    
    public List<DTOReprobacion> consultarMatRep(String desercion){
         return ejbDesercionReprobacion.getListaMateriasReprobadas(desercion);
    }
    
    public void seleccionarMatRep(String clave){
        dto.setListaMatRep(ejbDesercionReprobacion.getListaMateriasReprobadas(clave));
        Ajax.update("frmModalMaterias");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaMatRepDialogo();
    }
    
    public void forzarAperturaMatRepDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalMaterias').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }

}
