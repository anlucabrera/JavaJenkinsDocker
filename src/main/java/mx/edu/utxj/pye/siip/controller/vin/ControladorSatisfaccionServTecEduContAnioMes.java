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
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.SatisfaccionServtecEducontAnioMes;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoSatisfaccionServTecEduCont;
import mx.edu.utxj.pye.siip.dto.vin.DTOSatisfaccionServTecEduContAnioMes;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbSatisfaccionServTecEduContAnioMes;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorSatisfaccionServTecEduContAnioMes extends ViewScopedRol implements Serializable{

    private static final long serialVersionUID = 611666934446643100L;

    @Getter @Setter DtoSatisfaccionServTecEduCont dtoSatisfaccionServTecEduCont;

    @EJB EjbSatisfaccionServTecEduContAnioMes ejbSatisfaccionServTecEduContAnioMes;
    @EJB EjbModulos ejbModulos;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbCatalogos ejbCatalogos;
    
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
        dtoSatisfaccionServTecEduCont = new DtoSatisfaccionServTecEduCont();
        consultaAreaRegistro();
        if(dtoSatisfaccionServTecEduCont.getArea() == null){
            return;
        }
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 93);
            if (areaRegistro == null) {
                ResultadoEJB<AreasUniversidad> area = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if(area.getCorrecto()){
                    dtoSatisfaccionServTecEduCont.setArea(area.getValor());
                }else{
                    dtoSatisfaccionServTecEduCont.setArea(null);
                }
            } else {
                dtoSatisfaccionServTecEduCont.setArea(areaRegistro);
            }
        } catch (Exception ex) {
            dtoSatisfaccionServTecEduCont.setArea(null);
        }
    }
    
    public void inicializarCatalogos(){
        try {
            dtoSatisfaccionServTecEduCont.setServicios(ejbSatisfaccionServTecEduContAnioMes.getListaServicios().getValor());
            Faces.setSessionAttribute("serviciosTecnologicos", dtoSatisfaccionServTecEduCont.getServicios());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void listaSatisfaccionServTecEduContAnioMesPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                dtoSatisfaccionServTecEduCont.setRutaArchivo(rutaArchivo);
                dtoSatisfaccionServTecEduCont.setLstSatisfaccionServtecEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getListaSatisfaccionServTecEduContAnioMes(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
            if (dtoSatisfaccionServTecEduCont.getRutaArchivo() != null) {
                ServicioArchivos.eliminarArchivo(dtoSatisfaccionServTecEduCont.getRutaArchivo());
                dtoSatisfaccionServTecEduCont.setRutaArchivo(null);
            }
        }
    }
    
    public void guardaSatisfaccionServTecEduCont() {
        if (dtoSatisfaccionServTecEduCont.getLstSatisfaccionServtecEducontAnioMes()!= null) {
            try {
                ResultadoEJB<List<SatisfaccionServtecEducontAnioMes>> resGuardar = ejbSatisfaccionServTecEduContAnioMes.guardaSatisfaccionServTecEduCont(dtoSatisfaccionServTecEduCont.getLstSatisfaccionServtecEducontAnioMes(), dtoSatisfaccionServTecEduCont.getRegistroTipoST(), dtoSatisfaccionServTecEduCont.getEjesRegistro(), dtoSatisfaccionServTecEduCont.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
                if(resGuardar.getCorrecto()){
                    Messages.addGlobalInfo("El registro de satisfacción de servicio tecnológico y educación continua se ha guardado correctamente.");
                }else{
                    Messages.addGlobalError("No se pudo guardar el registro de satisfacción de servicio tecnológico y educación continua.");
                }
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
                if (dtoSatisfaccionServTecEduCont.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dtoSatisfaccionServTecEduCont.getRutaArchivo());
                    dtoSatisfaccionServTecEduCont.setRutaArchivo(null);
                }
            } finally {
                dtoSatisfaccionServTecEduCont.getLstSatisfaccionServtecEducontAnioMes().clear();
                dtoSatisfaccionServTecEduCont.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }

    public void cancelarArchivo() {
        dtoSatisfaccionServTecEduCont.getLstSatisfaccionServtecEducontAnioMes().clear();
        if (dtoSatisfaccionServTecEduCont.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dtoSatisfaccionServTecEduCont.getRutaArchivo());
            dtoSatisfaccionServTecEduCont.setRutaArchivo(null);
        }
    }
    
    public void filtros() {
        dtoSatisfaccionServTecEduCont.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoSatisfaccionServTecEduCont.getRegistros(),dtoSatisfaccionServTecEduCont.getArea()));
        if(!dtoSatisfaccionServTecEduCont.getAniosConsulta().isEmpty()){
            dtoSatisfaccionServTecEduCont.setAnioConsulta((short) dtoSatisfaccionServTecEduCont.getAniosConsulta().get(dtoSatisfaccionServTecEduCont.getAniosConsulta().size()-1));
        }
        dtoSatisfaccionServTecEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoSatisfaccionServTecEduCont.getAnioConsulta(),dtoSatisfaccionServTecEduCont.getRegistros(),dtoSatisfaccionServTecEduCont.getArea()));
        if(!dtoSatisfaccionServTecEduCont.getMesesConsulta().isEmpty()){
            dtoSatisfaccionServTecEduCont.setMesConsulta(dtoSatisfaccionServTecEduCont.getMesesConsulta().stream()
                .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                .findAny()
                .orElse(dtoSatisfaccionServTecEduCont.getMesesConsulta().get(dtoSatisfaccionServTecEduCont.getMesesConsulta().size()-1)));
        }
        buscaSatisfaccionSerTecEduCont();
    }
    
    public void actualizarMeses(ValueChangeEvent e) {
        dtoSatisfaccionServTecEduCont.setAnioConsulta((short) e.getNewValue());
        dtoSatisfaccionServTecEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoSatisfaccionServTecEduCont.getAnioConsulta(), dtoSatisfaccionServTecEduCont.getRegistros(),dtoSatisfaccionServTecEduCont.getArea()));
        buscaSatisfaccionSerTecEduCont();
    }
    
    public void buscaSatisfaccionSerTecEduCont() {
        if (dtoSatisfaccionServTecEduCont.getMesConsulta() != null && !dtoSatisfaccionServTecEduCont.getMesesConsulta().isEmpty()) {
            dtoSatisfaccionServTecEduCont.setLstDtoSatisfaccionServtecEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getFiltroSatisfaccionSerTecEduContEjercicioMesArea(dtoSatisfaccionServTecEduCont.getAnioConsulta(), dtoSatisfaccionServTecEduCont.getMesConsulta(), dtoSatisfaccionServTecEduCont.getArea().getArea()).getValor());
            
            dtoSatisfaccionServTecEduCont.getLstDtoSatisfaccionServtecEducontAnioMes().stream().forEach((st) -> {
                st.getSatisfaccionServtecEducontAnioMes().setRegistros(ejbModulos.buscaRegistroPorClave(st.getSatisfaccionServtecEducontAnioMes().getRegistro()));
            });

        } else {
            dtoSatisfaccionServTecEduCont.setLstSatisfaccionServtecEducontAnioMes(Collections.EMPTY_LIST);
        }
        Faces.setSessionAttribute("satisfaccionServTecEduCont", dtoSatisfaccionServTecEduCont.getLstSatisfaccionServtecEducontAnioMes());
        Ajax.update("formMuestraDatosActivos");
//        ecServiciosTecnologicosAnioMeses = ejbServiciosTecnologicosAnioMes.getFiltroServiciosTecnologicos(Short.valueOf(ejercicio), mes);
    }
    
    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            Messages.addGlobalInfo("Se ha eliminado el registro correctamente.");
            buscaSatisfaccionSerTecEduCont();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControladorSatisfaccionServTecEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
        dtoSatisfaccionServTecEduCont.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoSatisfaccionServTecEduCont.getArea()));
        if(!dtoSatisfaccionServTecEduCont.getEjes().isEmpty() && dtoSatisfaccionServTecEduCont.getAlineacionEje() == null){
            dtoSatisfaccionServTecEduCont.setAlineacionEje(dtoSatisfaccionServTecEduCont.getEjes().get(0));
            dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getArea()));
        }
        Faces.setSessionAttribute("ejes", dtoSatisfaccionServTecEduCont.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoSatisfaccionServTecEduCont.getAlineacionActividad() != null){
            dtoSatisfaccionServTecEduCont.setAlineacionEje(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getArea()));
            dtoSatisfaccionServTecEduCont.setAlineacionEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoSatisfaccionServTecEduCont.getEstrategias());

            dtoSatisfaccionServTecEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionEstrategia(), dtoSatisfaccionServTecEduCont.getArea()));
            dtoSatisfaccionServTecEduCont.setAlineacionLinea(dtoSatisfaccionServTecEduCont.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoSatisfaccionServTecEduCont.getLineasAccion());

            dtoSatisfaccionServTecEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoSatisfaccionServTecEduCont.getAlineacionLinea(), dtoSatisfaccionServTecEduCont.getArea(),dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoSatisfaccionServTecEduCont.getActividades());
        }else{
            dtoSatisfaccionServTecEduCont.setAlineacionEje(null);
            dtoSatisfaccionServTecEduCont.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoSatisfaccionServTecEduCont.getAlineacionLinea(), dtoSatisfaccionServTecEduCont.getArea(),dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoSatisfaccionServTecEduCont.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoSatisfaccionServTecEduCont.getAlineacionEje(), dtoSatisfaccionServTecEduCont.getArea()));
        dtoSatisfaccionServTecEduCont.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dtoSatisfaccionServTecEduCont.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoSatisfaccionServTecEduCont.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoSatisfaccionServTecEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoSatisfaccionServTecEduCont.getAlineacionEstrategia(), dtoSatisfaccionServTecEduCont.getArea()));
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
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoSatisfaccionServTecEduCont.getRegistro().getSatisfaccionServtecEducontAnioMes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DTOSatisfaccionServTecEduContAnioMes registroNew = (DTOSatisfaccionServTecEduContAnioMes) dataTable.getRowData();
        ejbSatisfaccionServTecEduContAnioMes.actualizarSatisfaccionSerTecEduContAnioMes(registroNew.getSatisfaccionServtecEducontAnioMes());
        init();
    }
    
}
