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
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.SatisfaccionServtecEducontAnioMes;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoSatisfaccionServTecEduCont;
import mx.edu.utxj.pye.siip.dto.vin.DTOSatisfaccionServTecEduContAnioMes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbSatisfaccionServTecEduContAnioMes;
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
public class ControladorSatisfaccionServTecEduContAnioMesPYE implements Serializable{

    private static final long serialVersionUID = 4748011023304316735L;

    @Getter @Setter DtoSatisfaccionServTecEduCont dtoSatisfaccionServTecEduCont;

    @EJB    EjbSatisfaccionServTecEduContAnioMes ejbSatisfaccionServTecEduContAnioMes;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbCatalogos                    ejbCatalogos;
    
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
        dtoSatisfaccionServTecEduCont = new DtoSatisfaccionServTecEduCont();
        
        ResultadoEJB<AreasUniversidad> resArea = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
            if(!resArea.getCorrecto()){
                return;
            }
        dtoSatisfaccionServTecEduCont.setArea(resArea.getValor());
        
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public void inicializarCatalogos(){
        try {
            dtoSatisfaccionServTecEduCont.setServicios(ejbSatisfaccionServTecEduContAnioMes.getListaServicios().getValor());
            Faces.setSessionAttribute("serviciosTecnologicos", dtoSatisfaccionServTecEduCont.getServicios());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filtros() {
        llenaCategorias();
        dtoSatisfaccionServTecEduCont.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dtoSatisfaccionServTecEduCont.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dtoSatisfaccionServTecEduCont.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dtoSatisfaccionServTecEduCont.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dtoSatisfaccionServTecEduCont.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dtoSatisfaccionServTecEduCont.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dtoSatisfaccionServTecEduCont.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dtoSatisfaccionServTecEduCont.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaSatisfaccionSerTecEduCont();
    }
    
   public void llenaCategorias() {
        dtoSatisfaccionServTecEduCont.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dtoSatisfaccionServTecEduCont.getListaCategoriasPOA().isEmpty() && dtoSatisfaccionServTecEduCont.getCategoria() == null) {
            dtoSatisfaccionServTecEduCont.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dtoSatisfaccionServTecEduCont.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dtoSatisfaccionServTecEduCont.getCategoria())
                .stream()
                .filter(area -> (short) 17 == area.getArea())
                .collect(Collectors.toList()));
        if (!dtoSatisfaccionServTecEduCont.getListaAreasPOA().isEmpty() && dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA() == null) {
            dtoSatisfaccionServTecEduCont.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dtoSatisfaccionServTecEduCont.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoSatisfaccionServTecEduCont.getRegistros(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        if (!dtoSatisfaccionServTecEduCont.getAniosConsulta().isEmpty()) {
            dtoSatisfaccionServTecEduCont.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dtoSatisfaccionServTecEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoSatisfaccionServTecEduCont.getAnioConsulta(), dtoSatisfaccionServTecEduCont.getRegistros(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        if (!dtoSatisfaccionServTecEduCont.getMesesConsulta().isEmpty()) {
            dtoSatisfaccionServTecEduCont.setMesConsulta(null);
        }
    }
    
    public void buscaSatisfaccionSerTecEduCont() {
        if (dtoSatisfaccionServTecEduCont.getMesConsulta() != null && !dtoSatisfaccionServTecEduCont.getMesesConsulta().isEmpty()) {
            dtoSatisfaccionServTecEduCont.setLstDtoSatisfaccionServtecEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getFiltroSatisfaccionSerTecEduContEjercicioMesArea(dtoSatisfaccionServTecEduCont.getAnioConsulta(), dtoSatisfaccionServTecEduCont.getMesConsulta(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA().getArea()).getValor());
    
            dtoSatisfaccionServTecEduCont.getLstDtoSatisfaccionServtecEducontAnioMes().stream().forEach((st) -> {
                st.getSatisfaccionServtecEducontAnioMes().setRegistros(ejbModulos.buscaRegistroPorClave(st.getSatisfaccionServtecEducontAnioMes().getRegistro()));
            });
        } else {
            dtoSatisfaccionServTecEduCont.setLstDtoSatisfaccionServtecEducontAnioMes(Collections.EMPTY_LIST);
        }
        Faces.setSessionAttribute("serviciosTecnologicos", dtoSatisfaccionServTecEduCont.getLstDtoSatisfaccionServtecEducontAnioMes());
        Ajax.update("formMuestraDatosActivos");
    }

    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            Messages.addGlobalInfo("Se ha eliminado el registro correctamente.");
            buscaSatisfaccionSerTecEduCont();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dtoSatisfaccionServTecEduCont.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoSatisfaccionServTecEduCont.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoSatisfaccionServTecEduCont.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOSatisfaccionServTecEduContAnioMes  dTOSatisfaccionServTecEduContAnioMes){
        dtoSatisfaccionServTecEduCont.setRegistro(dTOSatisfaccionServTecEduContAnioMes);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dtoSatisfaccionServTecEduCont.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros(), dtoSatisfaccionServTecEduCont.getArchivos());
            if(res.getKey()){
                buscaSatisfaccionSerTecEduCont();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoSatisfaccionServTecEduCont.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros(), evidencia);
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
        dtoSatisfaccionServTecEduCont.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        if(!dtoSatisfaccionServTecEduCont.getEjes().isEmpty() && dtoSatisfaccionServTecEduCont.getAlineacionEje() == null){
            dtoSatisfaccionServTecEduCont.setAlineacionEje(dtoSatisfaccionServTecEduCont.getEjes().get(0));
            dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dtoSatisfaccionServTecEduCont.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoSatisfaccionServTecEduCont.getAlineacionActividad() != null){
            dtoSatisfaccionServTecEduCont.setAlineacionEje(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
            dtoSatisfaccionServTecEduCont.setAlineacionEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoSatisfaccionServTecEduCont.getEstrategias());

            dtoSatisfaccionServTecEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionEstrategia(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
            dtoSatisfaccionServTecEduCont.setAlineacionLinea(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoSatisfaccionServTecEduCont.getLineasAccion());

            dtoSatisfaccionServTecEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoSatisfaccionServTecEduCont.getAlineacionLinea(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA(),dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoSatisfaccionServTecEduCont.getActividades());
        }else{
            dtoSatisfaccionServTecEduCont.setAlineacionEje(null);
            dtoSatisfaccionServTecEduCont.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoSatisfaccionServTecEduCont.getAlineacionLinea(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA(),dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoSatisfaccionServTecEduCont.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        dtoSatisfaccionServTecEduCont.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dtoSatisfaccionServTecEduCont.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionEstrategia(), dtoSatisfaccionServTecEduCont.getAreaUniversidadPOA()));
        dtoSatisfaccionServTecEduCont.nulificarLinea();
//        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dtoSatisfaccionServTecEduCont.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes) {
        try {
            DTOSatisfaccionServTecEduContAnioMes registro = new DTOSatisfaccionServTecEduContAnioMes();
            registro.setSatisfaccionServtecEducontAnioMes(satisfaccionServtecEducontAnioMes);
            dtoSatisfaccionServTecEduCont.setRegistro(registro);
            dtoSatisfaccionServTecEduCont.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getSatisfaccionServtecEducontAnioMes().getRegistro()));
            actualizarEjes(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoSatisfaccionServTecEduCont.getAlineacionActividad(), dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistro());
        if(alineado){
            buscaSatisfaccionSerTecEduCont();
            abrirAlineacionPOA(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoSatisfaccionServTecEduCont.getRegistro().setActividadAlineada(null);
            try {
                dtoSatisfaccionServTecEduCont.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
   public void forzarAperturaEdicionServicioTecnologico(){
        if(dtoSatisfaccionServTecEduCont.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionServicioTecnologico').show();");
            dtoSatisfaccionServTecEduCont.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionSatisfaccionSerTecEduCont(){
        Ajax.update("frmEdicionServicioTecnologico");
        Ajax.oncomplete("skin();");
        dtoSatisfaccionServTecEduCont.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionServicioTecnologico();
    }
    
    public void abrirEdicionSatisfaccionServTecEduCont(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes) {
        inicializarCatalogos();
        DTOSatisfaccionServTecEduContAnioMes dtosstecam = new DTOSatisfaccionServTecEduContAnioMes();
        dtosstecam.setSatisfaccionServtecEducontAnioMes(satisfaccionServtecEducontAnioMes);
        dtoSatisfaccionServTecEduCont.setRegistro(dtosstecam);
        actualizaInterfazEdicionSatisfaccionSerTecEduCont();
    }
    
    public void editaSatisfaccionServTecEduCont(){
        dtoSatisfaccionServTecEduCont.getRegistro().setSatisfaccionServtecEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.actualizarSatisfaccionSerTecEduContAnioMes(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes()).getValor());
        actualizaInterfazEdicionSatisfaccionSerTecEduCont();
    }
}
