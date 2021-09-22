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
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoEvaluacionSatEduCont;
import mx.edu.utxj.pye.siip.dto.vin.DTOEvaluacionSatEduContAnioMes;
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
public class ControladorEvaluacionSatEduContAnioMesPYE implements Serializable{

    private static final long serialVersionUID = -2036958677797548009L;
    
    @Getter @Setter DtoEvaluacionSatEduCont dtoEvaluacionSatEduCont;

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
        dtoEvaluacionSatEduCont = new DtoEvaluacionSatEduCont();
        
        ResultadoEJB<AreasUniversidad> resArea = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
            if(!resArea.getCorrecto()){
                return;
            }
        dtoEvaluacionSatEduCont.setArea(resArea.getValor());
        
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public void inicializarCatalogos(){
        try {
            dtoEvaluacionSatEduCont.setServicios(ejbSatisfaccionServTecEduContAnioMes.getListaServicios().getValor());
            Faces.setSessionAttribute("serviciosTecnologicos", dtoEvaluacionSatEduCont.getServicios());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filtros() {
        llenaCategorias();
        dtoEvaluacionSatEduCont.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dtoEvaluacionSatEduCont.getListaCategoriasPOA());
    }
    
    public void actualizarAreas(ValueChangeEvent e) {
        dtoEvaluacionSatEduCont.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dtoEvaluacionSatEduCont.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dtoEvaluacionSatEduCont.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dtoEvaluacionSatEduCont.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dtoEvaluacionSatEduCont.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dtoEvaluacionSatEduCont.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaSatisfaccionSerTecEduCont();
    }
    
   public void llenaCategorias() {
        dtoEvaluacionSatEduCont.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 6 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dtoEvaluacionSatEduCont.getListaCategoriasPOA().isEmpty() && dtoEvaluacionSatEduCont.getCategoria() == null) {
            dtoEvaluacionSatEduCont.setCategoria(null);
        }
    }
    
    public void llenaAreas() {
        dtoEvaluacionSatEduCont.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dtoEvaluacionSatEduCont.getCategoria())
                .stream()
                .filter(area -> (short) 17 == area.getArea())
                .collect(Collectors.toList()));
        if (!dtoEvaluacionSatEduCont.getListaAreasPOA().isEmpty() && dtoEvaluacionSatEduCont.getAreaUniversidadPOA() == null) {
            dtoEvaluacionSatEduCont.setAreaUniversidadPOA(null);
        }
    }
    
    public void llenaAnios() {
        dtoEvaluacionSatEduCont.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoEvaluacionSatEduCont.getRegistros(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        if (!dtoEvaluacionSatEduCont.getAniosConsulta().isEmpty()) {
            dtoEvaluacionSatEduCont.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dtoEvaluacionSatEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoEvaluacionSatEduCont.getAnioConsulta(), dtoEvaluacionSatEduCont.getRegistros(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        if (!dtoEvaluacionSatEduCont.getMesesConsulta().isEmpty()) {
            dtoEvaluacionSatEduCont.setMesConsulta(null);
        }
    }
    
    public void buscaSatisfaccionSerTecEduCont() {
        if (dtoEvaluacionSatEduCont.getMesConsulta() != null && !dtoEvaluacionSatEduCont.getMesesConsulta().isEmpty()) {
            dtoEvaluacionSatEduCont.setLstDtoEvaluacionSatEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getFiltroEvaluacionSatEduContEjercicioMesArea(dtoEvaluacionSatEduCont.getAnioConsulta(), dtoEvaluacionSatEduCont.getMesConsulta(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA().getArea()).getValor());
    
            dtoEvaluacionSatEduCont.getLstDtoEvaluacionSatEducontAnioMes().stream().forEach((st) -> {
                st.getEvaluacionSatisfaccionResultados().setRegistros(ejbModulos.buscaRegistroPorClave(st.getEvaluacionSatisfaccionResultados().getRegistro()));
            });
        } else {
            dtoEvaluacionSatEduCont.setLstDtoEvaluacionSatEducontAnioMes(Collections.EMPTY_LIST);
        }
        Faces.setSessionAttribute("serviciosTecnologicos", dtoEvaluacionSatEduCont.getLstDtoEvaluacionSatEducontAnioMes());
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
            dtoEvaluacionSatEduCont.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoEvaluacionSatEduCont.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoEvaluacionSatEduCont.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOEvaluacionSatEduContAnioMes.ConsultaRegistros  dTOEvaluacionSatEduContAnioMes){
        dtoEvaluacionSatEduCont.setRegistro(dTOEvaluacionSatEduContAnioMes);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dtoEvaluacionSatEduCont.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros(), dtoEvaluacionSatEduCont.getArchivos());
            if(res.getKey()){
                buscaSatisfaccionSerTecEduCont();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoEvaluacionSatEduCont.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros(), evidencia);
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
        dtoEvaluacionSatEduCont.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        if(!dtoEvaluacionSatEduCont.getEjes().isEmpty() && dtoEvaluacionSatEduCont.getAlineacionEje() == null){
            dtoEvaluacionSatEduCont.setAlineacionEje(dtoEvaluacionSatEduCont.getEjes().get(0));
            dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dtoEvaluacionSatEduCont.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoEvaluacionSatEduCont.getAlineacionActividad() != null){
            dtoEvaluacionSatEduCont.setAlineacionEje(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
            dtoEvaluacionSatEduCont.setAlineacionEstrategia(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoEvaluacionSatEduCont.getEstrategias());

            dtoEvaluacionSatEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoEvaluacionSatEduCont.getAlineacionEstrategia(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
            dtoEvaluacionSatEduCont.setAlineacionLinea(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoEvaluacionSatEduCont.getLineasAccion());

            dtoEvaluacionSatEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoEvaluacionSatEduCont.getAlineacionLinea(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA(),dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoEvaluacionSatEduCont.getActividades());
        }else{
            dtoEvaluacionSatEduCont.setAlineacionEje(null);
            dtoEvaluacionSatEduCont.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoEvaluacionSatEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoEvaluacionSatEduCont.getAlineacionLinea(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA(),dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoEvaluacionSatEduCont.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        dtoEvaluacionSatEduCont.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dtoEvaluacionSatEduCont.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoEvaluacionSatEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoEvaluacionSatEduCont.getAlineacionEstrategia(), dtoEvaluacionSatEduCont.getAreaUniversidadPOA()));
        dtoEvaluacionSatEduCont.nulificarLinea();
//        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dtoEvaluacionSatEduCont.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados) {
        try {
            DTOEvaluacionSatEduContAnioMes.ConsultaRegistros registro = new DTOEvaluacionSatEduContAnioMes.ConsultaRegistros();
            registro.setEvaluacionSatisfaccionResultados(evaluacionSatisfaccionResultados);
            dtoEvaluacionSatEduCont.setRegistro(registro);
            dtoEvaluacionSatEduCont.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getEvaluacionSatisfaccionResultados().getRegistro()));
            actualizarEjes(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoEvaluacionSatEduCont.getAlineacionActividad(), dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistro());
        if(alineado){
            buscaSatisfaccionSerTecEduCont();
            abrirAlineacionPOA(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistro());   
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoEvaluacionSatEduCont.getRegistro().setActividadAlineada(null);
            try {
                dtoEvaluacionSatEduCont.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorServiciosTecnologicosAnioMesPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
   public void forzarAperturaEdicionServicioTecnologico(){
        if(dtoEvaluacionSatEduCont.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionServicioTecnologico').show();");
            dtoEvaluacionSatEduCont.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionSatisfaccionSerTecEduCont(){
        Ajax.update("frmEdicionServicioTecnologico");
        Ajax.oncomplete("skin();");
        dtoEvaluacionSatEduCont.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionServicioTecnologico();
    }
    
//    public void abrirEdicionSatisfaccionServTecEduCont(EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados) {
//        inicializarCatalogos();
//        DTOEvaluacionSatEduContAnioMes dtosstecam = new DTOEvaluacionSatEduContAnioMes();
//        dtosstecam.setSatisfaccionServtecEducontAnioMes(evaluacionSatisfaccionResultados);
//        dtoEvaluacionSatEduCont.setRegistro(dtosstecam);
//        actualizaInterfazEdicionSatisfaccionSerTecEduCont();
//    }
//    
//    public void editaSatisfaccionServTecEduCont(){
//        dtoEvaluacionSatEduCont.getRegistro().setSatisfaccionServtecEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.actualizarSatisfaccionSerTecEduContAnioMes(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados()).getValor());
//        actualizaInterfazEdicionSatisfaccionSerTecEduCont();
//    }
    
}
