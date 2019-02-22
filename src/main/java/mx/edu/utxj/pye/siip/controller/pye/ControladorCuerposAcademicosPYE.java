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
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicasPK;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoCuerposAcademicos;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpAcadLineas;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerpoAreasAcademicas;
import mx.edu.utxj.pye.siip.dto.ca.DTOCuerposAcademicosR;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
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
public class ControladorCuerposAcademicosPYE implements Serializable{

    private static final long serialVersionUID = 6694975550961361657L;
    
    @Getter @Setter DtoCuerposAcademicos dtoCuerposAcademicos;
    
    @EJB    EjbCuerposAcademicos    ejbCuerposAcademicos;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;
    @EJB    EjbCatalogos            ejbCatalogos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dtoCuerposAcademicos = new DtoCuerposAcademicos();
        dtoCuerposAcademicos.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short)controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
        try {
            dtoCuerposAcademicos.setListaCuerpoAreasAcademicas(ejbCuerposAcademicos.getCuerpoAreasAcademicas());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarListas() {
        dtoCuerposAcademicos.setLstDtoCuerposAcademicosR(new ArrayList<>());
        dtoCuerposAcademicos.setLstDtoCuerpAcadIntegrantes(new ArrayList<>());
        dtoCuerposAcademicos.setLstCuerpAcadLineas(new ArrayList<>());
    }
    
    public void filtros(){
        llenaCategorias();
        dtoCuerposAcademicos.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dtoCuerposAcademicos.getListaCategoriasPOA());
    }
  
    public void actualizarAreas(ValueChangeEvent e) {
        dtoCuerposAcademicos.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dtoCuerposAcademicos.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dtoCuerposAcademicos.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dtoCuerposAcademicos.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dtoCuerposAcademicos.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dtoCuerposAcademicos.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscarCuerposAcademicos();
    }
    
    public void llenaCategorias() {
        dtoCuerposAcademicos.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dtoCuerposAcademicos.getListaCategoriasPOA().isEmpty() && dtoCuerposAcademicos.getCategoria() == null) {
            dtoCuerposAcademicos.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dtoCuerposAcademicos.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dtoCuerposAcademicos.getCategoria())
                .stream()
                .filter(area -> (short) 12 == area.getArea())
                .collect(Collectors.toList()));
        if (!dtoCuerposAcademicos.getListaAreasPOA().isEmpty() && dtoCuerposAcademicos.getAreaUniversidadPOA() == null) {
            dtoCuerposAcademicos.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dtoCuerposAcademicos.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoCuerposAcademicos.getRegistros(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        if (!dtoCuerposAcademicos.getAniosConsulta().isEmpty()) {
            dtoCuerposAcademicos.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dtoCuerposAcademicos.setMesesConsulta(ejbModulos.getMesesRegistros(dtoCuerposAcademicos.getAnioConsulta(), dtoCuerposAcademicos.getRegistros(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        if (!dtoCuerposAcademicos.getMesesConsulta().isEmpty()) {
            dtoCuerposAcademicos.setMesConsulta(null);
        }
    }
    
    public void buscarCuerposAcademicos() {
        try {
            if (dtoCuerposAcademicos.getMesConsulta() != null && !dtoCuerposAcademicos.getMesesConsulta().isEmpty()) {
                inicializarListas();
                dtoCuerposAcademicos.setLstDtoCuerposAcademicosR(ejbCuerposAcademicos.getFiltroCuerposAcademicosEjercicioMesArea(dtoCuerposAcademicos.getAnioConsulta(), dtoCuerposAcademicos.getAreaUniversidadPOA().getArea()));
                dtoCuerposAcademicos.setLstDtoCuerpAcadIntegrantes(ejbCuerposAcademicos.getFiltroCuerpAcadIntegrantesEjercicioMesArea(dtoCuerposAcademicos.getAnioConsulta(), dtoCuerposAcademicos.getAreaUniversidadPOA().getArea()));
                dtoCuerposAcademicos.setLstCuerpAcadLineas(ejbCuerposAcademicos.getFiltroCuerpAcadLineasEjercicioMesArea(dtoCuerposAcademicos.getAnioConsulta(), dtoCuerposAcademicos.getAreaUniversidadPOA().getArea()));

                dtoCuerposAcademicos.getLstDtoCuerposAcademicosR().stream().forEach((car) -> {
                    car.getCuerposAcademicosRegistro().setRegistros(ejbModulos.buscaRegistroPorClave(car.getCuerposAcademicosRegistro().getRegistro()));
                });

                dtoCuerposAcademicos.getLstDtoCuerpAcadIntegrantes().stream().forEach((cai) -> {
                    cai.getCuerpoAcademicoIntegrantes().setRegistros(ejbModulos.buscaRegistroPorClave(cai.getCuerpoAcademicoIntegrantes().getRegistro()));
                });

                dtoCuerposAcademicos.getLstCuerpAcadLineas().stream().forEach((cal) -> {
                    cal.setRegistros(ejbModulos.buscaRegistroPorClave(cal.getRegistro()));
                });
            } else {
                dtoCuerposAcademicos.setLstDtoCuerposAcademicosR(Collections.EMPTY_LIST);
                dtoCuerposAcademicos.setLstDtoCuerpAcadIntegrantes(Collections.EMPTY_LIST);
                dtoCuerposAcademicos.setLstCuerpAcadLineas(Collections.EMPTY_LIST);
            }
            Ajax.update("formMuestraDatosActivos");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void bajaCuerpoAcademico(CuerposAcademicosRegistro cuerpoAcademicoRegistro){
        if(cuerpoAcademicoRegistro.getEstatus() == true){
            ejbCuerposAcademicos.bajaCuerpoAcademico(cuerpoAcademicoRegistro);
            buscarCuerposAcademicos();
        }else{
            ejbCuerposAcademicos.altaCuerpoAcademico(cuerpoAcademicoRegistro);
            buscarCuerposAcademicos();
        }
    }
    
    public void bajaIntegrante(CuerpacadIntegrantes cuerpacadIntegrante){
        if(cuerpacadIntegrante.getEstatus() == true){
            ejbCuerposAcademicos.bajaCuerpacadIntegrantes(cuerpacadIntegrante);
            buscarCuerposAcademicos();
        }else{
            ejbCuerposAcademicos.altaCuerpacadIntegrantes(cuerpacadIntegrante);
            buscarCuerposAcademicos();
        }
    }
    
    public void bajaLineaInvestigacion(CuerpacadLineas cuerpacadLinea){
        if(cuerpacadLinea.getEstatus() == true){
            ejbCuerposAcademicos.bajaCuerpacadLineas(cuerpacadLinea);
            buscarCuerposAcademicos();
        }else{
            ejbCuerposAcademicos.altaCuerpacadLineas(cuerpacadLinea);
            buscarCuerposAcademicos();
        }
    }
    
    public void eliminarRegistro(Integer registro, CuerposAcademicosRegistro cuerposAcademicosRegistro) {
        List<Integer> registroIntegrantes = new ArrayList<>();
        List<Integer> registroLineas = new ArrayList<>();
        try {
            registroIntegrantes = ejbCuerposAcademicos.buscaRegistrosCuerpAcadIntegrantesByCuerpAcad(cuerposAcademicosRegistro);
            registroLineas = ejbCuerposAcademicos.buscaRegistrosCuerpAcadLineasByCuerpAcad(cuerposAcademicosRegistro);
            if (!registroIntegrantes.isEmpty()) {
                ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistrosParticipantes(registroIntegrantes));
                ejbModulos.eliminarRegistroParticipantes(registroIntegrantes);
            }
            if (!registroLineas.isEmpty()) {
                ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistrosParticipantes(registroLineas));
                ejbModulos.eliminarRegistroParticipantes(registroLineas);
            }
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscarCuerposAcademicos();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public void eliminarRegistroParticipante(Integer registro) throws Throwable{
        ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
        Boolean validar = ejbModulos.eliminarRegistro(registro);
        if(validar){
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            buscarCuerposAcademicos();
        }else{
            Messages.addGlobalInfo("El registro no pudo eliminarse.");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistroCuerpAcad(){
        try {
            dtoCuerposAcademicos.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistro()));
            Ajax.update("frmEvidenciasCuerpAcad");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroCuerpAcadIntegrantes(){
        try {
            dtoCuerposAcademicos.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistro()));
            Ajax.update("frmEvidenciasCuerpAcadIntegrantes");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarEvidenciasPorRegistroCuerpAcadLineas(){
        try {
            dtoCuerposAcademicos.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistro()));
            Ajax.update("frmEvidenciasCuerpAcadLineas");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoCuerpAcad(){
        if(dtoCuerposAcademicos.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaCuerpAcad').show();");
            dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoCuerpAcadIntegrantes(){
        if(dtoCuerposAcademicos.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaCuerpAcadIntegrantes').show();");
            dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoCuerpAcadLineas(){
        if(dtoCuerposAcademicos.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaCuerpAcadLineas').show();");
            dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistroCuerpAcad(DTOCuerposAcademicosR dtoCuerpAcad){
        dtoCuerposAcademicos.setRegistroCuerposAcademicosR(dtoCuerpAcad);
        cargarEvidenciasPorRegistroCuerpAcad();
        Ajax.oncomplete("skin();");
        dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoCuerpAcad();
    }
    
    public void seleccionarRegistroCuerpAcadIntegrantes(DTOCuerpAcadIntegrantes dtoCuerpAcadIntegrantes){
        dtoCuerposAcademicos.setRegistroCuerpAcadIntegrantes(dtoCuerpAcadIntegrantes);
        cargarEvidenciasPorRegistroCuerpAcadIntegrantes();
        Ajax.oncomplete("skin();");
        dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoCuerpAcadIntegrantes();
    }
    
    public void seleccionarRegistroCuerpAcadLineas(CuerpacadLineas cuerpacadLinea){
        DTOCuerpAcadLineas dtoCuerpAcadLinea = new DTOCuerpAcadLineas();
        dtoCuerpAcadLinea.setCuerpoAcademicoLineas(cuerpacadLinea);
        dtoCuerposAcademicos.setRegistroCuerpAcadLineas(dtoCuerpAcadLinea);
        cargarEvidenciasPorRegistroCuerpAcadLineas();
        Ajax.oncomplete("skin();");
        dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoCuerpAcadLineas();
    }
    
    public void subirEvidenciasCuerpAcad(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistros(), dtoCuerposAcademicos.getArchivos());
            if(res.getKey()){
                buscarCuerposAcademicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoCuerposAcademicos.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasCuerpAcadIntegrantes(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistros(), dtoCuerposAcademicos.getArchivos());
            if(res.getKey()){
                buscarCuerposAcademicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoCuerposAcademicos.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasCuerpAcadLineas(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistros(), dtoCuerposAcademicos.getArchivos());
            if(res.getKey()){
                buscarCuerposAcademicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoCuerposAcademicos.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidenciaCuerpAcad(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroCuerpAcad();
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaCuerpAcadIntegrante(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroCuerpAcadIntegrantes();
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaCuerpAcadLinea(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroCuerpAcadLineas();
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
        dtoCuerposAcademicos.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoCuerposAcademicos.getAreaUniversidadPOA()));
        if(!dtoCuerposAcademicos.getEjes().isEmpty() && dtoCuerposAcademicos.getAlineacionEje() == null){
            dtoCuerposAcademicos.setAlineacionEje(dtoCuerposAcademicos.getEjes().get(0));
            dtoCuerposAcademicos.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoCuerposAcademicos.getAlineacionEje(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dtoCuerposAcademicos.getEjes());
    }
    
    public void cargarAlineacionXActividad() {
        if (dtoCuerposAcademicos.getAlineacionActividad() != null) {
            dtoCuerposAcademicos.setAlineacionEje(dtoCuerposAcademicos.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoCuerposAcademicos.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoCuerposAcademicos.getAlineacionEje(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
            dtoCuerposAcademicos.setAlineacionEstrategia(dtoCuerposAcademicos.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoCuerposAcademicos.getEstrategias());

            dtoCuerposAcademicos.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoCuerposAcademicos.getAlineacionEstrategia(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
            dtoCuerposAcademicos.setAlineacionLinea(dtoCuerposAcademicos.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoCuerposAcademicos.getLineasAccion());

            dtoCuerposAcademicos.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoCuerposAcademicos.getAlineacionLinea(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
            Faces.setSessionAttribute("actividades", dtoCuerposAcademicos.getActividades());
        } else {
            dtoCuerposAcademicos.setAlineacionEje(null);
            dtoCuerposAcademicos.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoCuerposAcademicos.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoCuerposAcademicos.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoCuerposAcademicos.getAlineacionLinea(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        Faces.setSessionAttribute("actividades", dtoCuerposAcademicos.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoCuerposAcademicos.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoCuerposAcademicos.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoCuerposAcademicos.getAlineacionEje(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        dtoCuerposAcademicos.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dtoCuerposAcademicos.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoCuerposAcademicos.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoCuerposAcademicos.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoCuerposAcademicos.getAlineacionEstrategia(), dtoCuerposAcademicos.getAreaUniversidadPOA()));
        dtoCuerposAcademicos.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dtoCuerposAcademicos.getLineasAccion());
    }
    
    public void abrirAlineacionPOACuerpAcad(DTOCuerposAcademicosR dtoCuerpAcadRegistro) {
        try {
            dtoCuerposAcademicos.setRegistroCuerposAcademicosR(dtoCuerpAcadRegistro);
            dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerpAcadRegistro.getCuerposAcademicosRegistro().getRegistro()));
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcad");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionCuerpAcad').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOACuerpAcadIntegrantes(DTOCuerpAcadIntegrantes dtoCuerpAcadInt) {
        try {
            dtoCuerposAcademicos.setRegistroCuerpAcadIntegrantes(dtoCuerpAcadInt);
            dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerpAcadInt.getCuerpoAcademicoIntegrantes().getRegistro()));
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcadIntegrantes");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionCuerpAcadIntegrantes').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOACuerpAcadLineas(CuerpacadLineas cuerpacadLineas) {
        try {
            DTOCuerpAcadLineas dtoCuerpAcadLineas = new DTOCuerpAcadLineas();
            dtoCuerpAcadLineas.setCuerpoAcademicoLineas(cuerpacadLineas);
            dtoCuerposAcademicos.setRegistroCuerpAcadLineas(dtoCuerpAcadLineas);
            dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerpAcadLineas.getCuerpoAcademicoLineas().getRegistro()));
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcadLineas");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionCuerpAcadLineas').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistroCuerpAcad(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoCuerposAcademicos.getAlineacionActividad(), dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistro());
        if(alineado){
            buscarCuerposAcademicos();
            abrirAlineacionPOACuerpAcad(dtoCuerposAcademicos.getRegistroCuerposAcademicosR());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroCuerpAcadIntegrantes(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoCuerposAcademicos.getAlineacionActividad(), dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistro());
        if(alineado){
            buscarCuerposAcademicos();
            abrirAlineacionPOACuerpAcadIntegrantes(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroCuerpAcadLineas(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoCuerposAcademicos.getAlineacionActividad(), dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistro());
        if(alineado){
            buscarCuerposAcademicos();
            abrirAlineacionPOACuerpAcadLineas(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacionCuerpAcad(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoCuerposAcademicos.getRegistroCuerposAcademicosR().setActividadAlineada(null);
            try {
                dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcad");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionCuerpAcadIntegrantes(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().setActividadAlineada(null);
            try {
                dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerpAcadIntegrantes().getCuerpoAcademicoIntegrantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcadIntegrantes");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionCuerpAcadLineas(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoCuerposAcademicos.getRegistroCuerpAcadLineas().setActividadAlineada(null);
            try {
                dtoCuerposAcademicos.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorCuerposAcademicosPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoCuerposAcademicos.getRegistroCuerpAcadLineas().getCuerpoAcademicoLineas().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionCuerpAcadLineas");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
//    Áreas Académicas Asignadas a Cuerpos Académicos
    
    public void consultaCuerpoAreaAcademica(){
        dtoCuerposAcademicos.getListaCuerpoAreasAcademicas().stream().forEach((c) -> {
            c.setExiste(ejbCuerposAcademicos.verificaCuerpoAreaAcademica(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getCuerpoAcademico(), c.getAreaUniversidad()));
        });
    }
    
    public void forzarAperturaCuerpoAreasAcademicas(){
        if(dtoCuerposAcademicos.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCuerpoAreasAcademicas').show();");
            dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void abrirCuerpoAreasAcademicas(CuerposAcademicosRegistro cuerpAcad){
        DTOCuerposAcademicosR dtoCAR = new DTOCuerposAcademicosR();
        dtoCAR.setCuerposAcademicosRegistro(cuerpAcad);
        dtoCuerposAcademicos.setRegistroCuerposAcademicosR(dtoCAR);
        consultaCuerpoAreaAcademica();
        Ajax.update("frmCuerpoAreasAcademicas");
        Ajax.oncomplete("skin();");
        dtoCuerposAcademicos.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaCuerpoAreasAcademicas();
    }
    
    public void guardaCuerpoAreaAcademica(DTOCuerpoAreasAcademicas dtoCAA){
        CuerpoAreasAcademicasPK caapk = new CuerpoAreasAcademicasPK();
        caapk.setCuerpoAcademico(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro().getCuerpoAcademico());
        caapk.setAreaAcademica(dtoCAA.getAreaUniversidad().getArea());
        CuerpoAreasAcademicas caa = new CuerpoAreasAcademicas();
        caa.setCuerpoAreasAcademicasPK(caapk);
        caa.setCuerposAcademicosRegistro(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro());
        Boolean guardado = ejbCuerposAcademicos.guardarCuerpoAreaAcademica(caa);
        if(guardado){
            abrirCuerpoAreasAcademicas(dtoCuerposAcademicos.getRegistroCuerposAcademicosR().getCuerposAcademicosRegistro());
        }else  Messages.addGlobalError("El área académica no pudo asignarse.");
    }
    
}
