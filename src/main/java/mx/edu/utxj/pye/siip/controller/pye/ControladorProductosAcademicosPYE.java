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
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoProductoAcademico;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.ca.DTOProductosAcademicosPersonal;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProductosAcademicos;
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
public class ControladorProductosAcademicosPYE implements Serializable{    

    private static final long serialVersionUID = 7685788092620440196L;
    
    @Getter @Setter DtoProductoAcademico dtoProductoAcademico;
    
    @EJB    EjbProductosAcademicos  ejbProductosAcademicos;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;
    @EJB    EjbCatalogos            ejbCatalogos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dtoProductoAcademico = new DtoProductoAcademico();
        dtoProductoAcademico.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short)controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
    }
    
    public void filtros(){
        llenaCategorias();
        dtoProductoAcademico.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dtoProductoAcademico.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dtoProductoAcademico.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dtoProductoAcademico.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dtoProductoAcademico.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dtoProductoAcademico.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dtoProductoAcademico.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dtoProductoAcademico.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscarProductosAcademicos();
    }
    
    public void llenaCategorias() {
        dtoProductoAcademico.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dtoProductoAcademico.getListaCategoriasPOA().isEmpty() && dtoProductoAcademico.getCategoria() == null) {
            dtoProductoAcademico.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dtoProductoAcademico.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dtoProductoAcademico.getCategoria())
                .stream()
                .filter(area -> (short) 12 == area.getArea())
                .collect(Collectors.toList()));
        if (!dtoProductoAcademico.getListaAreasPOA().isEmpty() && dtoProductoAcademico.getAreaUniversidadPOA() == null) {
            dtoProductoAcademico.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dtoProductoAcademico.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoProductoAcademico.getRegistros(), dtoProductoAcademico.getAreaUniversidadPOA()));
        if (!dtoProductoAcademico.getAniosConsulta().isEmpty()) {
            dtoProductoAcademico.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dtoProductoAcademico.setMesesConsulta(ejbModulos.getMesesRegistros(dtoProductoAcademico.getAnioConsulta(), dtoProductoAcademico.getRegistros(), dtoProductoAcademico.getAreaUniversidadPOA()));
        if (!dtoProductoAcademico.getMesesConsulta().isEmpty()) {
            dtoProductoAcademico.setMesConsulta(null);
        }
    }
    
    public void buscarProductosAcademicos() {
        try {
            if (dtoProductoAcademico.getMesConsulta() != null && !dtoProductoAcademico.getMesesConsulta().isEmpty()) {
                dtoProductoAcademico.setLstDtoProductosAcademicos(ejbProductosAcademicos.getFiltroProductosAcademicosEjercicioMesArea(dtoProductoAcademico.getAnioConsulta(), dtoProductoAcademico.getMesConsulta(), dtoProductoAcademico.getAreaUniversidadPOA().getArea()));
                dtoProductoAcademico.setLstDtoProductosAcademicosPersonal(ejbProductosAcademicos.getFiltroProductosAcademicosPersonalEjercicioMesArea(dtoProductoAcademico.getAnioConsulta(), dtoProductoAcademico.getMesConsulta(), dtoProductoAcademico.getAreaUniversidadPOA().getArea()));

                dtoProductoAcademico.getLstDtoProductosAcademicos().stream().forEach((t) -> {
                    t.getProductosAcademicos().setRegistros(ejbModulos.buscaRegistroPorClave(t.getProductosAcademicos().getRegistro()));
                });
                dtoProductoAcademico.getLstDtoProductosAcademicosPersonal().stream().forEach((t) -> {
                    t.getProductoAcademicoPersonal().setRegistros(ejbModulos.buscaRegistroPorClave(t.getProductoAcademicoPersonal().getRegistro()));
                });
            } else {
                dtoProductoAcademico.setLstDtoProductosAcademicos(Collections.EMPTY_LIST);
                dtoProductoAcademico.setLstDtoProductosAcademicosPersonal(Collections.EMPTY_LIST);
            }
            Ajax.update("formMuestraDatosActivos");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarRegistro(Integer registro, ProductosAcademicos productoAcademico) {
        List<Integer> registroParticipantes = new ArrayList<>();
        try {
            registroParticipantes = ejbProductosAcademicos.buscaRegistrosPersonalProductosAcademicos(productoAcademico);
            if (!registroParticipantes.isEmpty()) {
                ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistrosParticipantes(registroParticipantes));
                ejbModulos.eliminarRegistroParticipantes(registroParticipantes);
            }
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscarProductosAcademicos();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public void eliminarRegistroParticipante(Integer registro) throws Throwable{
        ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
        Boolean validar = ejbModulos.eliminarRegistro(registro);
        if(validar){
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            buscarProductosAcademicos();
        }else{
            Messages.addGlobalInfo("El registro no pudo eliminarse.");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistroProdAcad(){
        try {
            dtoProductoAcademico.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistro()));
            Ajax.update("frmEvidenciasProductosAcademicos");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroProdAcadPersonal(){
        try {
            dtoProductoAcademico.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistro()));
            Ajax.update("frmEvidenciasProductosAcademicosPersonal");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoProductosAcademicos(){
        if(dtoProductoAcademico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaProductosAcademicos').show();");
            dtoProductoAcademico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoProductosAcademicosPersonal(){
        if(dtoProductoAcademico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciasProductosAcademicosPersonal').show();");
            dtoProductoAcademico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistroProdAcad(DTOProductosAcademicos dtoProdAcad){
        dtoProductoAcademico.setRegistroProductoAcademico(dtoProdAcad);
        cargarEvidenciasPorRegistroProdAcad();
        Ajax.oncomplete("skin();");
        dtoProductoAcademico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoProductosAcademicos();
    }
    
    public void seleccionarRegistroProdAcadPersonal(DTOProductosAcademicosPersonal dtoProdAcadPer){
        dtoProductoAcademico.setRegistroProductosAcademicosPersonal(dtoProdAcadPer);
        cargarEvidenciasPorRegistroProdAcadPersonal();
        Ajax.oncomplete("skin();");
        dtoProductoAcademico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoProductosAcademicosPersonal();
    }
    
    public void subirEvidenciasProdAcad(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistros(), dtoProductoAcademico.getArchivos());
            if(res.getKey()){
                buscarProductosAcademicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoProductoAcademico.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasProdAcadPersonal(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistros(), dtoProductoAcademico.getArchivos());
            if(res.getKey()){
                buscarProductosAcademicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoProductoAcademico.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidenciaProdAcad(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroProdAcad();
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaProdAcadPersonal(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroProdAcadPersonal();
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    /****************************** Alineación de actividades con registros ***********************************************/
    
    /**
     * 
     * @param registro
     * @return
     * @throws Throwable 
     */
    public Boolean verificaAlineacion(Integer registro) throws Throwable{
        return ejbModulos.verificaActividadAlineadaGeneral(registro);
    }
    
    public void actualizarEjes(Short ejercicio){
        dtoProductoAcademico.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoProductoAcademico.getAreaUniversidadPOA()));
        if(!dtoProductoAcademico.getEjes().isEmpty() && dtoProductoAcademico.getAlineacionEje() == null){
            dtoProductoAcademico.setAlineacionEje(dtoProductoAcademico.getEjes().get(0));
            dtoProductoAcademico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoProductoAcademico.getAlineacionEje(), dtoProductoAcademico.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dtoProductoAcademico.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoProductoAcademico.getAlineacionActividad() != null){
            dtoProductoAcademico.setAlineacionEje(dtoProductoAcademico.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoProductoAcademico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoProductoAcademico.getAlineacionEje(), dtoProductoAcademico.getAreaUniversidadPOA()));
            dtoProductoAcademico.setAlineacionEstrategia(dtoProductoAcademico.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoProductoAcademico.getEstrategias());

            dtoProductoAcademico.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoProductoAcademico.getAlineacionEstrategia(), dtoProductoAcademico.getAreaUniversidadPOA()));
            dtoProductoAcademico.setAlineacionLinea(dtoProductoAcademico.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoProductoAcademico.getLineasAccion());

            dtoProductoAcademico.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoProductoAcademico.getAlineacionLinea(), dtoProductoAcademico.getAreaUniversidadPOA()));
            Faces.setSessionAttribute("actividades", dtoProductoAcademico.getActividades());
        }else{
            dtoProductoAcademico.setAlineacionEje(null);
            dtoProductoAcademico.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoProductoAcademico.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoProductoAcademico.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoProductoAcademico.getAlineacionLinea(), dtoProductoAcademico.getAreaUniversidadPOA()));
        Faces.setSessionAttribute("actividades", dtoProductoAcademico.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoProductoAcademico.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoProductoAcademico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoProductoAcademico.getAlineacionEje(), dtoProductoAcademico.getAreaUniversidadPOA()));
        dtoProductoAcademico.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dtoProductoAcademico.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoProductoAcademico.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoProductoAcademico.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoProductoAcademico.getAlineacionEstrategia(), dtoProductoAcademico.getAreaUniversidadPOA()));
        dtoProductoAcademico.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dtoProductoAcademico.getLineasAccion());
    }
    
    public void abrirAlineacionPOAProdAcad(DTOProductosAcademicos dtoProdAcad) {
        try {
            dtoProductoAcademico.setRegistroProductoAcademico(dtoProdAcad);
            dtoProductoAcademico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoProdAcad.getProductosAcademicos().getRegistro()));
            actualizarEjes(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionProdAcad");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionProdAcad').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirAlineacionPOAProdAcadPersonal(DTOProductosAcademicosPersonal dtoProdAcadPersonal) {
        try {
            dtoProductoAcademico.setRegistroProductosAcademicosPersonal(dtoProdAcadPersonal);
            dtoProductoAcademico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoProdAcadPersonal.getProductoAcademicoPersonal().getRegistro()));
            actualizarEjes(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionProdAcadPersonal");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionProdAcadPersonal').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistroProdAcad(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoProductoAcademico.getAlineacionActividad(), dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistro());
        if(alineado){
            buscarProductosAcademicos();
            abrirAlineacionPOAProdAcad(dtoProductoAcademico.getRegistroProductoAcademico());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroPersonalProdAcadPersonal(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoProductoAcademico.getAlineacionActividad(), dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistro());
        if(alineado){
            buscarProductosAcademicos();
            abrirAlineacionPOAProdAcadPersonal(dtoProductoAcademico.getRegistroProductosAcademicosPersonal());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacionProdAcad(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoProductoAcademico.getRegistroProductoAcademico().setActividadAlineada(null);
            try {
                dtoProductoAcademico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoProductoAcademico.getRegistroProductoAcademico().getProductosAcademicos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionProdAcad");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionProdAcadPersonal(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoProductoAcademico.getRegistroProductosAcademicosPersonal().setActividadAlineada(null);
            try {
                dtoProductoAcademico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorProductosAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoProductoAcademico.getRegistroProductosAcademicosPersonal().getProductoAcademicoPersonal().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionProdAcadPersonal");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
}
