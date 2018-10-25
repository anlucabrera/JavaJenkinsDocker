/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoOrganismosVinculados;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOOrganismoVinculado;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "organismosVinculadosC")
@ManagedBean
@ViewScoped
public class ControladorOrganismosVinculados implements Serializable {

    private static final long serialVersionUID = 2262599808841468998L;
    
    @Getter @Setter DtoOrganismosVinculados dtoOrganismosVinculado;
    
    @EJB    EjbOrganismosVinculados ejbOrganismosVinculados;
    @EJB    EjbModulos              ejbModulos;
    @EJB    EjbFiscalizacion        ejbFiscalizacion;
    @EJB    EjbConvenios            ejbConvenios;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init() {
        dtoOrganismosVinculado = new DtoOrganismosVinculados();
        dtoOrganismosVinculado.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        filtros();
    }

    public void listaOrganismosVinculadosPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                dtoOrganismosVinculado.setRutaArchivo(rutaArchivo);
                dtoOrganismosVinculado.setLstOrganismosVinculados(ejbOrganismosVinculados.getListaOrganismosVinculados(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
            if (dtoOrganismosVinculado.getRutaArchivo() != null) {
                ServicioArchivos.eliminarArchivo(dtoOrganismosVinculado.getRutaArchivo());
                dtoOrganismosVinculado.setRutaArchivo(null);
            }
        }
    }
    
    public void guardaOrganismosVinculados() {
        if (dtoOrganismosVinculado.getLstOrganismosVinculados()!= null) {
            try {
                ejbOrganismosVinculados.guardaOrganismosVinculados(dtoOrganismosVinculado.getLstOrganismosVinculados(), dtoOrganismosVinculado.getRegistroTipo(), dtoOrganismosVinculado.getEjesRegistro(), dtoOrganismosVinculado.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
                if (dtoOrganismosVinculado.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dtoOrganismosVinculado.getRutaArchivo());
                    dtoOrganismosVinculado.setRutaArchivo(null);
                }
            } finally {
                dtoOrganismosVinculado.getLstOrganismosVinculados().clear();
                dtoOrganismosVinculado.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }

    }
    
    public void cancelarArchivo(){
        dtoOrganismosVinculado.getLstOrganismosVinculados().clear();
//        listaOrganismosVinculados.getOrganismosVinculadosLst().clear();
        if (dtoOrganismosVinculado.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dtoOrganismosVinculado.getRutaArchivo());
            dtoOrganismosVinculado.setRutaArchivo(null);
        }
    }
    
    public void filtros() {
        dtoOrganismosVinculado.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoOrganismosVinculado.getRegistros(),dtoOrganismosVinculado.getArea()));
        if(!dtoOrganismosVinculado.getAniosConsulta().isEmpty()){
            dtoOrganismosVinculado.setAnioConsulta((short) dtoOrganismosVinculado.getAniosConsulta().get(dtoOrganismosVinculado.getAniosConsulta().size()-1));
        }
        dtoOrganismosVinculado.setMesesConsulta(ejbModulos.getMesesRegistros(dtoOrganismosVinculado.getAnioConsulta(),dtoOrganismosVinculado.getRegistros(),dtoOrganismosVinculado.getArea()));
        if(!dtoOrganismosVinculado.getMesesConsulta().isEmpty()){
            dtoOrganismosVinculado.setMesConsulta(dtoOrganismosVinculado.getMesesConsulta().stream()
                .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                .findAny()
                .orElse(dtoOrganismosVinculado.getMesesConsulta().get(dtoOrganismosVinculado.getMesesConsulta().size()-1)));
        }
        buscaOrganismosVinculados();
        buscaActividadesVinculacion();
    }
    
    public void actualizarMeses(ValueChangeEvent e) {
        dtoOrganismosVinculado.setAnioConsulta((short) e.getNewValue());
        dtoOrganismosVinculado.setMesesConsulta(ejbModulos.getMesesRegistros(dtoOrganismosVinculado.getAnioConsulta(), dtoOrganismosVinculado.getRegistros(),dtoOrganismosVinculado.getArea()));
        buscaOrganismosVinculados();
    }

    public void buscaOrganismosVinculados() {
        dtoOrganismosVinculado.setLstOrganismosVinculados(ejbOrganismosVinculados.getFiltroOrganismoVinculadoEjercicioMesArea(dtoOrganismosVinculado.getAnioConsulta(), dtoOrganismosVinculado.getMesConsulta(), dtoOrganismosVinculado.getArea().getArea()));
        dtoOrganismosVinculado.getLstOrganismosVinculados().stream().forEach((t) -> {
            t.setRegistros(ejbModulos.buscaRegistroPorClave(t.getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void buscaActividadesVinculacion(){
        try {
            dtoOrganismosVinculado.setListaActividadesVinculacion(ejbOrganismosVinculados.getActividadesVinculacion());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarRegistro(Integer registro, OrganismosVinculados orgVin) {
        try {
            ejbOrganismosVinculados.bajaOrganismoVinculado(orgVin);
            
//            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
//            ejbModulos.eliminarRegistro(registro);
            filtros();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }

    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro() {
        try {
            dtoOrganismosVinculado.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(OrganismosVinculados orgVinc){
        DTOOrganismoVinculado dtoov = new DTOOrganismoVinculado();
        dtoov.setOrganismoVinculado(orgVinc);
        dtoOrganismosVinculado.setRegistro(dtoov);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros(), dtoOrganismosVinculado.getArchivos());
            if(res.getKey()){
                buscaOrganismosVinculados();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoOrganismosVinculado.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros(), evidencia);
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
        dtoOrganismosVinculado.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoOrganismosVinculado.getArea()));
        if(!dtoOrganismosVinculado.getEjes().isEmpty() && dtoOrganismosVinculado.getAlineacionEje() == null){
            dtoOrganismosVinculado.setAlineacionEje(dtoOrganismosVinculado.getEjes().get(0));
            dtoOrganismosVinculado.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoOrganismosVinculado.getAlineacionEje(), dtoOrganismosVinculado.getArea()));
        }
        Faces.setSessionAttribute("ejes", dtoOrganismosVinculado.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoOrganismosVinculado.getAlineacionActividad() != null){
            dtoOrganismosVinculado.setAlineacionEje(dtoOrganismosVinculado.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoOrganismosVinculado.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoOrganismosVinculado.getAlineacionEje(), dtoOrganismosVinculado.getArea()));
            dtoOrganismosVinculado.setAlineacionEstrategia(dtoOrganismosVinculado.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoOrganismosVinculado.getEstrategias());

            dtoOrganismosVinculado.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoOrganismosVinculado.getAlineacionEstrategia(), dtoOrganismosVinculado.getArea()));
            dtoOrganismosVinculado.setAlineacionLinea(dtoOrganismosVinculado.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoOrganismosVinculado.getLineasAccion());

            dtoOrganismosVinculado.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoOrganismosVinculado.getAlineacionLinea(), dtoOrganismosVinculado.getArea()));
            Faces.setSessionAttribute("actividades", dtoOrganismosVinculado.getActividades());
        }else{
            dtoOrganismosVinculado.setAlineacionEje(null);
            dtoOrganismosVinculado.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoOrganismosVinculado.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoOrganismosVinculado.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoOrganismosVinculado.getAlineacionLinea(), dtoOrganismosVinculado.getArea()));
        Faces.setSessionAttribute("actividades", dtoOrganismosVinculado.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoOrganismosVinculado.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoOrganismosVinculado.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoOrganismosVinculado.getAlineacionEje(), dtoOrganismosVinculado.getArea()));
        dtoOrganismosVinculado.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dtoOrganismosVinculado.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoOrganismosVinculado.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoOrganismosVinculado.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoOrganismosVinculado.getAlineacionEstrategia(), dtoOrganismosVinculado.getArea()));
        dtoOrganismosVinculado.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dtoOrganismosVinculado.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(OrganismosVinculados orgVin){
        try {
            DTOOrganismoVinculado registro = new DTOOrganismoVinculado();
            registro.setOrganismoVinculado(orgVin);
            dtoOrganismosVinculado.setRegistro(registro);
            dtoOrganismosVinculado.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistro()));
            actualizarEjes(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void abrirAlineacionPOAPrueba(OrganismosVinculados orgVin){
        System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorOrganismosVinculados.abrirAlineacionPOAPrueba(): " + orgVin.getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
        System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorOrganismosVinculados.abrirAlineacionPOAPrueba(): " + orgVin.getRegistro());
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoOrganismosVinculado.getAlineacionActividad(), dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistro());
        if(alineado){
            buscaOrganismosVinculados();
            abrirAlineacionPOA(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoOrganismosVinculado.getRegistro().setActividadAlineada(null);
            try {
                dtoOrganismosVinculado.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    public void abrirActividadesVinculacion(OrganismosVinculados orgVin){
        DTOOrganismoVinculado registro = new DTOOrganismoVinculado();    
        registro.setOrganismoVinculado(orgVin);
        dtoOrganismosVinculado.setRegistro(registro);
        consultaVinculacion();
        Ajax.update("frmActividadesVinculacion");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaActividadesVinculacion();
    }
    
    public void forzarAperturaActividadesVinculacion(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalActividadesVinculacion').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void consultaVinculacion() {
        dtoOrganismosVinculado.getListaActividadesVinculacion().stream().forEach((t) -> {
            t.setExiste(ejbOrganismosVinculados.verificaActividadVinculacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado(), t.getActividadVinculacion()));
        });
    }
    
    public void guardaActividadVinculacion(DTOActividadesVinculacion actividadVinculacion){
        Boolean asignado = ejbOrganismosVinculados.guardarActividadVinculacionEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado(), actividadVinculacion);
        if(asignado){
            abrirActividadesVinculacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
        }else Messages.addGlobalError("La actividad no pudo asignarse.");
    }
    
    public Boolean verificarSiTieneConvenio(Integer empresa){
        return ejbConvenios.verificaConvenio(empresa);
    }
    
}