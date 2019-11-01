/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoActividadesVarias;
import mx.edu.utxj.pye.siip.dto.ca.DTOActividadVaria;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbActividadesVarias;
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
public class ControladorActividadesVariasPYE implements Serializable {

    private static final long serialVersionUID = -5901698000368066017L;

    /*Implementacion del patron SET*/
    @Getter @Setter DtoActividadesVarias dto;

    @EJB    EjbActividadesVarias    ejbActividadesVarias;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;
    @EJB    EjbCatalogos            ejbCatalogos;

    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;

    @PostConstruct
    public void init() {
        dto = new DtoActividadesVarias();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setListaAreasConRegistroMensualGeneral(ejbActividadesVarias.getAreasConRegistroMensualGeneral(Boolean.FALSE, ejbModulos.getEventoRegistro().getMes()));
        filtros();
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
        buscaActividadesVarias();
    }
    
    public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa());
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria()));
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
    
    public void buscaActividadesVarias() {
        if (dto.getMesConsulta() != null && !dto.getMesesConsulta().isEmpty()) {
            dto.setLstActividadesVarias(ejbActividadesVarias.getFiltroActividadesVariasEjercicioMesArea(dto.getAnioConsulta(), dto.getMesConsulta(), dto.getAreaUniversidadPOA().getArea()));
            dto.getLstActividadesVarias().stream().forEach((av) -> {
                av.setRegistros(ejbModulos.buscaRegistroPorClave(av.getRegistro()));
            });
        } else {
            dto.setLstActividadesVarias(Collections.EMPTY_LIST);
        }
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro, ActividadesVariasRegistro actividadVariaRegistro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaActividadesVarias();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorActividadesVariasPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getRegistro().getActividadVaria().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorActividadesVariasPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(ActividadesVariasRegistro actVar){
        DTOActividadVaria dtoav = new DTOActividadVaria();
        dtoav.setActividadVaria(actVar);
        dto.setRegistro(dtoav);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getRegistro().getActividadVaria().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaActividadesVarias();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorActividadesVariasPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getRegistro().getActividadVaria().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
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

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(), dto.getRegistro().getActividadVaria().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(), dto.getRegistro().getActividadVaria().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
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
    
    public void abrirAlineacionPOA(ActividadesVariasRegistro avr){
        try {
            DTOActividadVaria dtoav = new DTOActividadVaria();
            dtoav.setActividadVaria(avr);
            dto.setRegistro(dtoav);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getActividadVaria().getRegistro()));
            actualizarEjes(dto.getRegistro().getActividadVaria().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorActividadesVariasPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getActividadVaria().getRegistro());
        if(alineado){
            buscaActividadesVarias();
            abrirAlineacionPOA(dto.getRegistro().getActividadVaria());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getRegistro().getActividadVaria().getRegistro());
        if(eliminado){ 
            try {
                Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
                dto.getRegistro().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getRegistro().getActividadVaria().getRegistro()));
                actualizarEjes(dto.getRegistro().getActividadVaria().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } catch (Throwable ex) {
                Logger.getLogger(ControladorActividadesVariasPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }

    public void forzarAperturaEdicionActividadVaria(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionActividadVaria').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionActividadVaria(){
        Ajax.update("frmEdicionActividadVaria");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionActividadVaria(); 
    }
    
    public void abrirEdicionActividadVaria(ActividadesVariasRegistro actividadVariaRegistroEditada) {
        dto.setNuevoRegistro(Boolean.FALSE);
        DTOActividadVaria dtoAvre = new DTOActividadVaria();
        dtoAvre.setActividadVaria(actividadVariaRegistroEditada);
        dto.setRegistro(dtoAvre);
        dto.setMensaje("");
        actualizaInterfazEdicionActividadVaria();
    }
    
    public void editaActividadVaria(ActividadesVariasRegistro actividadVaria){
        DTOActividadVaria dtoAvre = new DTOActividadVaria();
        if(ejbActividadesVarias.buscaActividadVariaExistente(dto.getRegistro().getActividadVaria())){
            dto.setMensaje("El nombre asignado ya esta siendo utilizado por otra actividad, favor de especificar nombre de la actividad");
        }else{
            dtoAvre.setActividadVaria(ejbActividadesVarias.editaActividadesVarias(actividadVaria));
            dto.setRegistro(dtoAvre);
            dto.setMensaje("El registro ha sido actualizado");
            buscaActividadesVarias();
            actualizaInterfazEdicionActividadVaria();
        }
        Ajax.update("mensaje");
    }
    
    public void validaFechaInicio(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && dto.getRegistro().getActividadVaria().getFechaFin() != null) {
            dto.getRegistro().getActividadVaria().setFechaInicio((Date) event.getNewValue());
            if (dto.getRegistro().getActividadVaria().getFechaInicio().before(dto.getRegistro().getActividadVaria().getFechaFin())) {
//            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");          
            } else {
                if (dto.getRegistro().getActividadVaria().getFechaFin().before(dto.getRegistro().getActividadVaria().getFechaInicio())) {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() false");
                    dto.getRegistro().getActividadVaria().setFechaFin(null);
                    dto.getRegistro().getActividadVaria().setFechaFin(dto.getRegistro().getActividadVaria().getFechaInicio());
//                Ajax.update("iptFechaTermino");
                } else {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");               
                }
            }
        }
    }
    
    public void validaFechaFin(ValueChangeEvent event) {
       if ((Date) event.getNewValue() != null && dto.getRegistro().getActividadVaria().getFechaInicio() != null) {
            dto.getRegistro().getActividadVaria().setFechaFin((Date) event.getNewValue());
            if (dto.getRegistro().getActividadVaria().getFechaFin().after(dto.getRegistro().getActividadVaria().getFechaInicio())) {
//            System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
            } else {
                if (dto.getRegistro().getActividadVaria().getFechaInicio().after(dto.getRegistro().getActividadVaria().getFechaFin())) {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() false");
                    dto.getRegistro().getActividadVaria().setFechaInicio(null);
                    dto.getRegistro().getActividadVaria().setFechaInicio(dto.getRegistro().getActividadVaria().getFechaFin());
//                Ajax.update("iptFechaInicio");
                } else {
//                System.out.println("mx.edu.utxj.pye.siip.controller.eb.Ejemplo.main() true");
                }
            }
        }
    }
    
    public void nuevoRegistro(){
        if (dto.getAreaUniversidadPOA() != null) {
            dto.setNuevoRegistro(Boolean.TRUE);
            DTOActividadVaria dtoAvre = new DTOActividadVaria();
            dtoAvre.setActividadVaria(new ActividadesVariasRegistro());
            dto.setRegistro(dtoAvre);
            dto.setMensaje("");
            actualizaInterfazEdicionActividadVaria();
        }else{
            Messages.addGlobalWarn("Debes seleccionar un área para poder dar de alta una nueva actividad");
        }
    }
    
    public void guardarActividadVaria(ActividadesVariasRegistro actividadVaria) {
        ejbActividadesVarias.guardaActividadVaria(actividadVaria, dto.getRegistroTipoAV(), dto.getEjesRegistro(), dto.getAreaUniversidadPOA().getArea(), controladorModulosRegistro.getEventosRegistros());
        dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
        buscaActividadesVarias();
        DTOActividadVaria dtoAvre = new DTOActividadVaria();
        dtoAvre.setActividadVaria(new ActividadesVariasRegistro());
        dto.setRegistro(dtoAvre);
        actualizaInterfazEdicionActividadVaria();
        Ajax.update("mensaje");
    }

    public void accionActividadVaria(ActividadesVariasRegistro actividadVaria){
        if(dto.getNuevoRegistro()){
            guardarActividadVaria(actividadVaria);
        }else{
            editaActividadVaria(actividadVaria);
        }
    }
}
