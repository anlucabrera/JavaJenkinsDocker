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
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoEvaluacionSatEduCont;
import mx.edu.utxj.pye.siip.dto.vin.DTOEvaluacionSatEduContAnioMes;
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
public class ControladorEvaluacionSatEduContAnioMes extends ViewScopedRol implements Serializable{

    private static final long serialVersionUID = -2703539860493689359L;
    
    @Getter @Setter DtoEvaluacionSatEduCont dtoEvaluacionSatEduCont;

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
        dtoEvaluacionSatEduCont = new DtoEvaluacionSatEduCont();
        consultaAreaRegistro();
        if(dtoEvaluacionSatEduCont.getArea() == null){
            return;
        }
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEvaluacionSatEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 95);
            if (areaRegistro == null) {
                ResultadoEJB<AreasUniversidad> area = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if(area.getCorrecto()){
                    dtoEvaluacionSatEduCont.setArea(area.getValor());
                }else{
                    dtoEvaluacionSatEduCont.setArea(null);
                }
            } else {
                dtoEvaluacionSatEduCont.setArea(areaRegistro);
            }
        } catch (Exception ex) {
            dtoEvaluacionSatEduCont.setArea(null);
        }
    }
    
    public void inicializarCatalogos(){
        try {
            dtoEvaluacionSatEduCont.setServicios(ejbSatisfaccionServTecEduContAnioMes.getListaServicios().getValor());
            Faces.setSessionAttribute("serviciosTecnologicos", dtoEvaluacionSatEduCont.getServicios());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEvaluacionSatEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void listaEvaluacionSatEduContAnioMesPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                dtoEvaluacionSatEduCont.setRutaArchivo(rutaArchivo);
                dtoEvaluacionSatEduCont.setLstEvaluacionSatEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getListaEvaluacionSatEduContAnioMes(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
            if (dtoEvaluacionSatEduCont.getRutaArchivo() != null) {
                ServicioArchivos.eliminarArchivo(dtoEvaluacionSatEduCont.getRutaArchivo());
                dtoEvaluacionSatEduCont.setRutaArchivo(null);
            }
        }
    }
    
    public void guardaEvaluacionSatEduCont() {
        if (dtoEvaluacionSatEduCont.getLstEvaluacionSatEducontAnioMes()!= null) {
            try {
                ResultadoEJB<List<EvaluacionSatisfaccionResultados>> resGuardar = ejbSatisfaccionServTecEduContAnioMes.guardaEvaluacionSatEduCont(dtoEvaluacionSatEduCont.getLstEvaluacionSatEducontAnioMes(), dtoEvaluacionSatEduCont.getRegistroTipoST(), dtoEvaluacionSatEduCont.getEjesRegistro(), dtoEvaluacionSatEduCont.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
                if(resGuardar.getCorrecto()){
                    Messages.addGlobalInfo("El registro de evaluación de satisfacción de servicio tecnológico y educación continua se ha guardado correctamente.");
                }else{
                    Messages.addGlobalError("No se pudo guardar el registro de evaluación de satisfacción de servicio tecnológico y educación continua.");
                }
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                Logger.getLogger(ControladorEvaluacionSatEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
                if (dtoEvaluacionSatEduCont.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dtoEvaluacionSatEduCont.getRutaArchivo());
                    dtoEvaluacionSatEduCont.setRutaArchivo(null);
                }
            } finally {
                dtoEvaluacionSatEduCont.getLstEvaluacionSatEducontAnioMes().clear();
                dtoEvaluacionSatEduCont.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }

    public void cancelarArchivo() {
        dtoEvaluacionSatEduCont.getLstEvaluacionSatEducontAnioMes().clear();
        if (dtoEvaluacionSatEduCont.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dtoEvaluacionSatEduCont.getRutaArchivo());
            dtoEvaluacionSatEduCont.setRutaArchivo(null);
        }
    }
    
    public void filtros() {
        dtoEvaluacionSatEduCont.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoEvaluacionSatEduCont.getRegistros(),dtoEvaluacionSatEduCont.getArea()));
        if(!dtoEvaluacionSatEduCont.getAniosConsulta().isEmpty()){
            dtoEvaluacionSatEduCont.setAnioConsulta((short) dtoEvaluacionSatEduCont.getAniosConsulta().get(dtoEvaluacionSatEduCont.getAniosConsulta().size()-1));
        }
        dtoEvaluacionSatEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoEvaluacionSatEduCont.getAnioConsulta(),dtoEvaluacionSatEduCont.getRegistros(),dtoEvaluacionSatEduCont.getArea()));
        if(!dtoEvaluacionSatEduCont.getMesesConsulta().isEmpty()){
            dtoEvaluacionSatEduCont.setMesConsulta(dtoEvaluacionSatEduCont.getMesesConsulta().stream()
                .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                .findAny()
                .orElse(dtoEvaluacionSatEduCont.getMesesConsulta().get(dtoEvaluacionSatEduCont.getMesesConsulta().size()-1)));
        }
        buscaEvaluacionSatEduCont();
    }
    
    public void actualizarMeses(ValueChangeEvent e) {
        dtoEvaluacionSatEduCont.setAnioConsulta((short) e.getNewValue());
        dtoEvaluacionSatEduCont.setMesesConsulta(ejbModulos.getMesesRegistros(dtoEvaluacionSatEduCont.getAnioConsulta(), dtoEvaluacionSatEduCont.getRegistros(),dtoEvaluacionSatEduCont.getArea()));
        buscaEvaluacionSatEduCont();
    }
    
    public void buscaEvaluacionSatEduCont() {
        if (dtoEvaluacionSatEduCont.getMesConsulta() != null && !dtoEvaluacionSatEduCont.getMesesConsulta().isEmpty()) {
            dtoEvaluacionSatEduCont.setLstDtoEvaluacionSatEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getFiltroEvaluacionSatEduContEjercicioMesArea(dtoEvaluacionSatEduCont.getAnioConsulta(), dtoEvaluacionSatEduCont.getMesConsulta(), dtoEvaluacionSatEduCont.getArea().getArea()).getValor());
            
            dtoEvaluacionSatEduCont.getLstDtoEvaluacionSatEducontAnioMes().stream().forEach((st) -> {
                st.getEvaluacionSatisfaccionResultados().setRegistros(ejbModulos.buscaRegistroPorClave(st.getEvaluacionSatisfaccionResultados().getRegistro()));
            });

        } else {
            dtoEvaluacionSatEduCont.setLstDtoEvaluacionSatEducontAnioMes(Collections.EMPTY_LIST);
        }
        Faces.setSessionAttribute("satisfaccionServTecEduCont", dtoEvaluacionSatEduCont.getLstDtoEvaluacionSatEducontAnioMes());
        Ajax.update("formMuestraDatosActivos");
//        ecServiciosTecnologicosAnioMeses = ejbServiciosTecnologicosAnioMes.getFiltroServiciosTecnologicos(Short.valueOf(ejercicio), mes);
    }
    
    public void eliminarRegistro(Integer registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            Messages.addGlobalInfo("Se ha eliminado el registro correctamente.");
            buscaEvaluacionSatEduCont();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorEvaluacionSatEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ControladorEvaluacionSatEduContAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoEvaluacionSatEduCont.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoEvaluacionSatEduCont.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOEvaluacionSatEduContAnioMes.ConsultaRegistros dTOEvaluacionSatEduContAnioMes){
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
                buscaEvaluacionSatEduCont();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoEvaluacionSatEduCont.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
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
        dtoEvaluacionSatEduCont.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoEvaluacionSatEduCont.getArea()));
        if(!dtoEvaluacionSatEduCont.getEjes().isEmpty() && dtoEvaluacionSatEduCont.getAlineacionEje() == null){
            dtoEvaluacionSatEduCont.setAlineacionEje(dtoEvaluacionSatEduCont.getEjes().get(0));
            dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getArea()));
        }
        Faces.setSessionAttribute("ejes", dtoEvaluacionSatEduCont.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoEvaluacionSatEduCont.getAlineacionActividad() != null){
            dtoEvaluacionSatEduCont.setAlineacionEje(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getArea()));
            dtoEvaluacionSatEduCont.setAlineacionEstrategia(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoEvaluacionSatEduCont.getEstrategias());

            dtoEvaluacionSatEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoEvaluacionSatEduCont.getAlineacionEstrategia(), dtoEvaluacionSatEduCont.getArea()));
            dtoEvaluacionSatEduCont.setAlineacionLinea(dtoEvaluacionSatEduCont.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoEvaluacionSatEduCont.getLineasAccion());

            dtoEvaluacionSatEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoEvaluacionSatEduCont.getAlineacionLinea(), dtoEvaluacionSatEduCont.getArea(),dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoEvaluacionSatEduCont.getActividades());
        }else{
            dtoEvaluacionSatEduCont.setAlineacionEje(null);
            dtoEvaluacionSatEduCont.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoEvaluacionSatEduCont.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoEvaluacionSatEduCont.getAlineacionLinea(), dtoEvaluacionSatEduCont.getArea(),dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoEvaluacionSatEduCont.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoEvaluacionSatEduCont.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoEvaluacionSatEduCont.getAlineacionEje(), dtoEvaluacionSatEduCont.getArea()));
        dtoEvaluacionSatEduCont.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dtoEvaluacionSatEduCont.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoEvaluacionSatEduCont.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoEvaluacionSatEduCont.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoEvaluacionSatEduCont.getAlineacionEstrategia(), dtoEvaluacionSatEduCont.getArea()));
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
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoEvaluacionSatEduCont.getAlineacionActividad(), dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistro());
        if(alineado){
            buscaEvaluacionSatEduCont();
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
                Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoEvaluacionSatEduCont.getRegistro().getEvaluacionSatisfaccionResultados().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
   
     public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DTOEvaluacionSatEduContAnioMes.ConsultaRegistros registroNew = (DTOEvaluacionSatEduContAnioMes.ConsultaRegistros) dataTable.getRowData();
        ejbSatisfaccionServTecEduContAnioMes.actualizarEvaluacionSatEduContAnioMes(registroNew.getEvaluacionSatisfaccionResultados());
//        dtoEvaluacionSatEduCont.setLstDtoEvaluacionSatEducontAnioMes(ejbSatisfaccionServTecEduContAnioMes.getFiltroEvaluacionSatEduContEjercicioMesArea(dtoEvaluacionSatEduCont.getAnioConsulta(), dtoEvaluacionSatEduCont.getMesConsulta(), dtoEvaluacionSatEduCont.getArea().getArea()).getValor());
//        Faces.setSessionAttribute("satisfaccionServTecEduCont", dtoEvaluacionSatEduCont.getLstDtoEvaluacionSatEducontAnioMes());
//        Ajax.update("formMuestraDatosActivos");
    }
}
