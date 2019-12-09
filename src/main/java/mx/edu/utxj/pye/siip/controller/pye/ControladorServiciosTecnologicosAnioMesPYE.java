/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTipos;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoServiciosTecnologicos;
import mx.edu.utxj.pye.siip.dto.vin.DTOServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTOServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
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
public class ControladorServiciosTecnologicosAnioMesPYE implements Serializable{

    private static final long serialVersionUID = 4748011023304316735L;

    @Getter @Setter DtoServiciosTecnologicos dtoServicioTecnologico;

    @EJB    EjbServiciosTecnologicosAnioMes ejbServiciosTecnologicosAnioMes;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbCatalogos                    ejbCatalogos;
    @EJB    EjbOrganismosVinculados         ejbOrganismosVinculados;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dtoServicioTecnologico = new DtoServiciosTecnologicos();
        dtoServicioTecnologico.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarCatalogos(){
        try {
            dtoServicioTecnologico.setServicioTipos(ejbServiciosTecnologicosAnioMes.getListaServiciosTipo());
            Faces.setSessionAttribute("serviciosTipos", dtoServicioTecnologico.getServicioTipos());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarCatalogosParticipantes(){
        dtoServicioTecnologico.setGeneraciones(ejbCatalogos.getGeneracionesAct());
        dtoServicioTecnologico.setProgramasEducativos(ejbCatalogos.getProgramasEducativosGeneral());
        dtoServicioTecnologico.setEstados(ejbFiscalizacion.getEstados());
        Faces.setSessionAttribute("generaciones", dtoServicioTecnologico.getGeneraciones());
        Faces.setSessionAttribute("programasEducativos", dtoServicioTecnologico.getProgramasEducativos());
        Faces.setSessionAttribute("estados", dtoServicioTecnologico.getEstados());
    }
    
    public void filtros() {
        llenaCategorias();
        dtoServicioTecnologico.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dtoServicioTecnologico.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dtoServicioTecnologico.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dtoServicioTecnologico.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dtoServicioTecnologico.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dtoServicioTecnologico.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dtoServicioTecnologico.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dtoServicioTecnologico.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaServiciosTecnologicos();
    }
    
    public void llenaCategorias() {
        dtoServicioTecnologico.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dtoServicioTecnologico.getListaCategoriasPOA().isEmpty() && dtoServicioTecnologico.getCategoria() == null) {
            dtoServicioTecnologico.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dtoServicioTecnologico.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dtoServicioTecnologico.getCategoria())
                .stream()
                .filter(area -> (short) 17 == area.getArea())
                .collect(Collectors.toList()));
        if (!dtoServicioTecnologico.getListaAreasPOA().isEmpty() && dtoServicioTecnologico.getAreaUniversidadPOA() == null) {
            dtoServicioTecnologico.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dtoServicioTecnologico.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoServicioTecnologico.getRegistros(), dtoServicioTecnologico.getAreaUniversidadPOA()));
        if (!dtoServicioTecnologico.getAniosConsulta().isEmpty()) {
            dtoServicioTecnologico.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dtoServicioTecnologico.setMesesConsulta(ejbModulos.getMesesRegistros(dtoServicioTecnologico.getAnioConsulta(), dtoServicioTecnologico.getRegistros(), dtoServicioTecnologico.getAreaUniversidadPOA()));
        if (!dtoServicioTecnologico.getMesesConsulta().isEmpty()) {
            dtoServicioTecnologico.setMesConsulta(null);
        }
    }
    
    public void buscaServiciosTecnologicos() {
        if (dtoServicioTecnologico.getMesConsulta() != null && !dtoServicioTecnologico.getMesesConsulta().isEmpty()) {
            dtoServicioTecnologico.setLstServiciosTecnologicosAnioMes(ejbServiciosTecnologicosAnioMes.getFiltroServiciosTecnologicosEjercicioMesArea(dtoServicioTecnologico.getAnioConsulta(), dtoServicioTecnologico.getMesConsulta(), dtoServicioTecnologico.getAreaUniversidadPOA().getArea()));
            dtoServicioTecnologico.setLstDtoServiciosTecnologicosParticipantes(ejbServiciosTecnologicosAnioMes.getFiltroServiciosTecnologicosPartEjercicioMesArea(dtoServicioTecnologico.getAnioConsulta(), dtoServicioTecnologico.getMesConsulta(), dtoServicioTecnologico.getAreaUniversidadPOA().getArea()));

            dtoServicioTecnologico.getLstServiciosTecnologicosAnioMes().stream().forEach((st) -> {
                st.setRegistros(ejbModulos.buscaRegistroPorClave(st.getRegistro()));
            });

            dtoServicioTecnologico.getLstDtoServiciosTecnologicosParticipantes().stream().forEach((stp) -> {
                stp.getServiciosTecnologicosParticipantes().setRegistros(ejbModulos.buscaRegistroPorClave(stp.getServiciosTecnologicosParticipantes().getRegistro()));
            });
        } else {
            dtoServicioTecnologico.setLstServiciosTecnologicosAnioMes(Collections.EMPTY_LIST);
            dtoServicioTecnologico.setLstDtoServiciosTecnologicosParticipantes(Collections.EMPTY_LIST);
        }
        Faces.setSessionAttribute("serviciosTecnologicos", dtoServicioTecnologico.getLstServiciosTecnologicosAnioMes());
        Ajax.update("formMuestraDatosActivos");
    }

    public void eliminarRegistro(Integer registro, ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes) {
        List<Integer> registroParticipantes = new ArrayList<>();
        try {
            registroParticipantes = ejbServiciosTecnologicosAnioMes.buscaRegistroParticipantesServiciosTecnologicos(serviciosTecnologicosAnioMes);
            if (!registroParticipantes.isEmpty()){
                ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistrosParticipantes(registroParticipantes));
                ejbModulos.eliminarRegistroParticipantes(registroParticipantes);
            }
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaServiciosTecnologicos();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public void eliminarRegistroParticipante(Integer registro) throws Throwable {
        ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
        Boolean validar = ejbModulos.eliminarRegistro(registro);
        if (validar) {
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            buscaServiciosTecnologicos();
        } else {
            Messages.addGlobalInfo("El registro no pudo eliminarse.");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dtoServicioTecnologico.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void cargarEvidenciasPorRegistroParticipantes(){
        try {
            dtoServicioTecnologico.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistro()));
            Ajax.update("frmEvidenciasParticipantes");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoServicioTecnologico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void forzarAperturaEvidenciasDialogoParticipantes(){
        if(dtoServicioTecnologico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaParticipantes').show();");
            dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(ServiciosTecnologicosAnioMes servicioTecnologico){
        DTOServiciosTecnologicosAnioMes dtoServiciosTecnologicosAnioMes = new DTOServiciosTecnologicosAnioMes();
        dtoServiciosTecnologicosAnioMes.setServiciosTecnologicosAnioMes(servicioTecnologico);
        dtoServicioTecnologico.setRegistro(dtoServiciosTecnologicosAnioMes);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void seleccionarRegistroParticipantes(DTOServiciosTecnologicosParticipantes dtoServiciosTecnologicosParticipantes){
        dtoServicioTecnologico.setRegistroParticipantes(dtoServiciosTecnologicosParticipantes);
        cargarEvidenciasPorRegistroParticipantes();
        Ajax.oncomplete("skin();");
        dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogoParticipantes();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistros(), dtoServicioTecnologico.getArchivos());
            if(res.getKey()){
                buscaServiciosTecnologicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoServicioTecnologico.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirEvidenciasParticipantes(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros(), dtoServicioTecnologico.getArchivos());
            if(res.getKey()){
                buscaServiciosTecnologicos();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoServicioTecnologico.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void eliminarEvidenciaParticipantes(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros(), evidencia);
        if(eliminado){
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistroParticipantes();
            Ajax.update("frmEvidenciasParticipantes");
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
        dtoServicioTecnologico.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoServicioTecnologico.getAreaUniversidadPOA()));
        if(!dtoServicioTecnologico.getEjes().isEmpty() && dtoServicioTecnologico.getAlineacionEje() == null){
            dtoServicioTecnologico.setAlineacionEje(dtoServicioTecnologico.getEjes().get(0));
            dtoServicioTecnologico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoServicioTecnologico.getAlineacionEje(), dtoServicioTecnologico.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dtoServicioTecnologico.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoServicioTecnologico.getAlineacionActividad() != null){
            dtoServicioTecnologico.setAlineacionEje(dtoServicioTecnologico.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoServicioTecnologico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoServicioTecnologico.getAlineacionEje(), dtoServicioTecnologico.getAreaUniversidadPOA()));
            dtoServicioTecnologico.setAlineacionEstrategia(dtoServicioTecnologico.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoServicioTecnologico.getEstrategias());

            dtoServicioTecnologico.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoServicioTecnologico.getAlineacionEstrategia(), dtoServicioTecnologico.getAreaUniversidadPOA()));
            dtoServicioTecnologico.setAlineacionLinea(dtoServicioTecnologico.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoServicioTecnologico.getLineasAccion());

            dtoServicioTecnologico.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoServicioTecnologico.getAlineacionLinea(), dtoServicioTecnologico.getAreaUniversidadPOA(),dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoServicioTecnologico.getActividades());
        }else{
            dtoServicioTecnologico.setAlineacionEje(null);
            dtoServicioTecnologico.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoServicioTecnologico.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoServicioTecnologico.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoServicioTecnologico.getAlineacionLinea(), dtoServicioTecnologico.getAreaUniversidadPOA(),dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoServicioTecnologico.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoServicioTecnologico.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoServicioTecnologico.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoServicioTecnologico.getAlineacionEje(), dtoServicioTecnologico.getAreaUniversidadPOA()));
        dtoServicioTecnologico.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dtoServicioTecnologico.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoServicioTecnologico.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoServicioTecnologico.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoServicioTecnologico.getAlineacionEstrategia(), dtoServicioTecnologico.getAreaUniversidadPOA()));
        dtoServicioTecnologico.nulificarLinea();
//        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dtoServicioTecnologico.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes) {
        try {
            DTOServiciosTecnologicosAnioMes registro = new DTOServiciosTecnologicosAnioMes();
            registro.setServiciosTecnologicosAnioMes(serviciosTecnologicosAnioMes);
            dtoServicioTecnologico.setRegistro(registro);
            dtoServicioTecnologico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getServiciosTecnologicosAnioMes().getRegistro()));
            actualizarEjes(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOAParticipantes(DTOServiciosTecnologicosParticipantes dtoServiciosTecnologicosParticipantes) {
        try {
            dtoServicioTecnologico.setRegistroParticipantes(dtoServiciosTecnologicosParticipantes);
            dtoServicioTecnologico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoServiciosTecnologicosParticipantes.getServiciosTecnologicosParticipantes().getRegistro()));
            actualizarEjes(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionParticipantes");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacionParticipantes').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoServicioTecnologico.getAlineacionActividad(), dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistro());
        if(alineado){
            buscaServiciosTecnologicos();
            abrirAlineacionPOA(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void alinearRegistroParticipantes(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoServicioTecnologico.getAlineacionActividad(), dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistro());
        if(alineado){
            buscaServiciosTecnologicos();
            abrirAlineacionPOAParticipantes(dtoServicioTecnologico.getRegistroParticipantes());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoServicioTecnologico.getRegistro().setActividadAlineada(null);
            try {
                dtoServicioTecnologico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void eliminarAlineacionParticipante(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoServicioTecnologico.getRegistroParticipantes().setActividadAlineada(null);
            try {
                dtoServicioTecnologico.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionParticipantes");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void forzarAperturaEdicionServicioTecnologico(){
        if(dtoServicioTecnologico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionServicioTecnologico').show();");
            dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionServicioTecnologico(){
        Ajax.update("frmEdicionServicioTecnologico");
        Ajax.oncomplete("skin();");
        dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionServicioTecnologico();
    }
    
    public void abrirEdicionServicioTecnologico(ServiciosTecnologicosAnioMes servicioTecnologico) {
        if("Por oferta".equals(dtoServicioTecnologico.getServicioDemandado().toString())){
            dtoServicioTecnologico.setServicioDemandado(Boolean.TRUE);
        }else{
            dtoServicioTecnologico.setServicioDemandado(Boolean.FALSE);
        }
        inicializarCatalogos();
        DTOServiciosTecnologicosAnioMes dtoSerTec = new DTOServiciosTecnologicosAnioMes();
        dtoSerTec.setServiciosTecnologicosAnioMes(servicioTecnologico);
        dtoServicioTecnologico.setRegistro(dtoSerTec);
        actualizaInterfazEdicionServicioTecnologico();
    }
    
    public void editaServicioTecnologico(){
        dtoServicioTecnologico.getRegistro().setServiciosTecnologicosAnioMes(ejbServiciosTecnologicosAnioMes.editaServicioTecnologicoAnioMes(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes()));
        actualizaInterfazEdicionServicioTecnologico();
    }
    
    public void actualizarServicioTipo(ValueChangeEvent event){
        dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setServicioTipo((ServiciosTipos)event.getNewValue());
    }
    
    public void validaFechaInicio(ValueChangeEvent event) {
        dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaInicio((Date)event.getNewValue());
        if (dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaInicio().before(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaTermino())) {
        } else {
            if (dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaTermino().before(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaInicio())) {
                dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaTermino(null);
                dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaTermino(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaInicio());
            } else {
            }
        }
    }
    
    public void validaFechaFin(ValueChangeEvent event) {
        dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaTermino((Date)event.getNewValue());
        if (dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaTermino().after(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaInicio())) {
        } else {
            if (dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaInicio().after(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaTermino())) {
                dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaInicio(null);
                dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setFechaInicio(dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().getFechaTermino());
            } else {
            }
        }
    }
    
    public void actualizaServicioDemandado(ValueChangeEvent event){
        dtoServicioTecnologico.setServicioDemandado((Boolean)event.getNewValue());
        if(dtoServicioTecnologico.getServicioDemandado()){
            dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setServicioDemandado("Por oferta");
        }else{
            dtoServicioTecnologico.getRegistro().getServiciosTecnologicosAnioMes().setServicioDemandado("Por demanda");
        }
    }
    
    /************************************************************************************************************************/
    
    public void forzarAperturaEdicionServicioTecnologicoParticipante(){
        if(dtoServicioTecnologico.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionServicioTecnologicoParticipante').show();");
            dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionServicioTecnologicoParticipante(){
        Ajax.update("frmEdicionServicioTecnologicoParticipante");
        Ajax.oncomplete("skin();");
        dtoServicioTecnologico.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionServicioTecnologicoParticipante();
    }
    
    public void abrirEdicionServicioTecnologicoParticipante(ServiciosTecnologicosParticipantes servicioTecnologicoParticipante) {
        inicializarCatalogosParticipantes();
        DTOServiciosTecnologicosParticipantes dtoSerTecPart = new DTOServiciosTecnologicosParticipantes();
        dtoSerTecPart.setServiciosTecnologicosParticipantes(servicioTecnologicoParticipante);
        dtoServicioTecnologico.setRegistroParticipantes(dtoSerTecPart);
        
        dtoServicioTecnologico.setMunicipios(ejbFiscalizacion.getMunicipiosPorEstado(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getMunicipio().getEstado()));
        Faces.setSessionAttribute("municipios", dtoServicioTecnologico.getMunicipios());
        
        actualizaInterfazEdicionServicioTecnologicoParticipante();
    }

    public void editaServicioTecnologicoParticipante(){
        if(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getEmpresa() == null){
            dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().setEmpresa(null);
        }
        dtoServicioTecnologico.getRegistroParticipantes().setServiciosTecnologicosParticipantes(ejbServiciosTecnologicosAnioMes.editaServicioTecnologicoParticipante(dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes()));
        actualizaInterfazEdicionServicioTecnologicoParticipante();
    }
    
    public List<OrganismosVinculados> completeOrganismosVinculados(String organismoVinculado) { 
        dtoServicioTecnologico.setListaEmpresas(ejbOrganismosVinculados.buscaCoincidenciasOrganismosVinculados(organismoVinculado));
        Faces.setSessionAttribute("organismosVinculados", dtoServicioTecnologico.getListaEmpresas());
        return dtoServicioTecnologico.getListaEmpresas();
    }
    
    public void seleccionaOrganismoVinculado(ValueChangeEvent event){
        dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().setEmpresa((OrganismosVinculados)event.getNewValue());
    }
    
    public void actualizarServicioTecnologicoAsignado(ValueChangeEvent event){
        dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().setServicioTecnologico((ServiciosTecnologicosAnioMes)event.getNewValue());
    }
    
    public void actualizarMunicipiosSTP(ValueChangeEvent event){
        dtoServicioTecnologico.getRegistroParticipantes().getServiciosTecnologicosParticipantes().getMunicipio().setEstado((Estado)event.getNewValue());
        dtoServicioTecnologico.setMunicipios(ejbFiscalizacion.getMunicipiosPorEstado((Estado)event.getNewValue()));
        Faces.setSessionAttribute("municipios", dtoServicioTecnologico.getMunicipios());
    }
}
