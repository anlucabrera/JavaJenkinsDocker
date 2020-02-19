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
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacionPK;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoConvenios;
import mx.edu.utxj.pye.siip.dto.vin.DTOConvenio;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbConvenios;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "conveniosC")
@ManagedBean
@ViewScoped
public class ControladorConvenios implements Serializable {

    private static final long serialVersionUID = -8332222236186681210L;
    
    @Getter @Setter DtoConvenios dtoConvenios;
    
    @EJB    EjbConvenios        ejbConvenios;
    @EJB    EjbModulos          ejbModulos;
    @EJB    EjbFiscalizacion    ejbFiscalizacion;
    @EJB    EjbOrganismosVinculados     ejbOrganismosVinculados;

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
        dtoConvenios = new DtoConvenios();
        consultaAreaRegistro(); 
        if(dtoConvenios.getArea() == null){
            return;
        }
        filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 24);
            if (areaRegistro == null) {
                ResultadoEJB<AreasUniversidad> area = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if(area.getCorrecto()){
                    dtoConvenios.setArea(area.getValor());
                }else{
                    dtoConvenios.setArea(null);
                }
            } else {
                dtoConvenios.setArea(areaRegistro);
            }
        } catch (Exception ex) {
            dtoConvenios.setArea(null);
        }
    }
    
     public void listaConveniosPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                dtoConvenios.setRutaArchivo(rutaArchivo);
                dtoConvenios.setLstConvenios(ejbConvenios.getListaConvenios(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
            if (rutaArchivo != null) {
                ServicioArchivos.eliminarArchivo(rutaArchivo);
                dtoConvenios.setRutaArchivo(null);
            }
        }
    }

    public void guardaConvenios() {
        if (dtoConvenios.getLstConvenios() != null) {
            try {
                ejbConvenios.guardaConvenios(dtoConvenios.getLstConvenios(), dtoConvenios.getRegistroTipo(), dtoConvenios.getEjesRegistro(), dtoConvenios.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
                if (dtoConvenios.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dtoConvenios.getRutaArchivo());
                    dtoConvenios.setRutaArchivo(null);
                }
            } finally {
                dtoConvenios.getLstConvenios().clear();
                dtoConvenios.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }

    public void cancelarArchivo() {
        dtoConvenios.getLstConvenios().clear();
        if (dtoConvenios.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dtoConvenios.getRutaArchivo());
            dtoConvenios.setRutaArchivo(null);
        }
    }
    
    
    public void filtros() {
        dtoConvenios.setAniosConsulta(ejbModulos.getEjercicioRegistros(dtoConvenios.getRegistros(), dtoConvenios.getArea()));
        if (!dtoConvenios.getAniosConsulta().isEmpty()) {
            dtoConvenios.setAnioConsulta((short) dtoConvenios.getAniosConsulta().get(dtoConvenios.getAniosConsulta().size() - 1));
        }
        dtoConvenios.setMesesConsulta(ejbModulos.getMesesRegistros(dtoConvenios.getAnioConsulta(), dtoConvenios.getRegistros(), dtoConvenios.getArea()));
        if (!dtoConvenios.getMesesConsulta().isEmpty()) {
            dtoConvenios.setMesConsulta(dtoConvenios.getMesesConsulta().stream()
                    .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                    .findAny()
                    .orElse(dtoConvenios.getMesesConsulta().get(dtoConvenios.getMesesConsulta().size() - 1)));
        }
        buscaConvenios();
    }

    public void actualizarMeses(ValueChangeEvent e) {
        dtoConvenios.setAnioConsulta((short) e.getNewValue());
        dtoConvenios.setMesesConsulta(ejbModulos.getMesesRegistros(dtoConvenios.getAnioConsulta(), dtoConvenios.getRegistros(),dtoConvenios.getArea()));
        buscaConvenios();
    }
    
    public void buscaConvenios() {
        dtoConvenios.setLstConvenios(ejbConvenios.getFiltroConveniosEjercicioMesArea(dtoConvenios.getAnioConsulta(), dtoConvenios.getMesConsulta(), dtoConvenios.getArea().getArea()));
        dtoConvenios.getLstConvenios().stream().forEach((c) -> {
            c.setRegistros(ejbModulos.buscaRegistroPorClave(c.getRegistro()));
            c.setEmpresa(ejbOrganismosVinculados.getOrganismosVinculado(c.getEmpresa()));
            c.getEmpresa().setRegistros(ejbModulos.buscaRegistroPorClave(c.getEmpresa().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro, Convenios convenios) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            filtros();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dtoConvenios.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dtoConvenios.getRegistro().getConvenio().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dtoConvenios.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dtoConvenios.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(Convenios convenio){
        DTOConvenio dtoc = new DTOConvenio();
        dtoc.setConvenio(convenio);
        dtoConvenios.setRegistro(dtoc);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dtoConvenios.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dtoConvenios.getRegistro().getConvenio().getRegistros(), dtoConvenios.getArchivos());
            if(res.getKey()){
                buscaConvenios();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dtoConvenios.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dtoConvenios.getRegistro().getConvenio().getRegistros(), evidencia);
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
        dtoConvenios.setEjes(ejbFiscalizacion.getEjes(ejercicio, dtoConvenios.getArea()));
        if(!dtoConvenios.getEjes().isEmpty() && dtoConvenios.getAlineacionEje() == null){
            dtoConvenios.setAlineacionEje(dtoConvenios.getEjes().get(0));
            dtoConvenios.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoConvenios.getAlineacionEje(), dtoConvenios.getArea()));
        }
        Faces.setSessionAttribute("ejes", dtoConvenios.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dtoConvenios.getAlineacionActividad() != null){
            dtoConvenios.setAlineacionEje(dtoConvenios.getAlineacionActividad().getCuadroMandoInt().getEje());

            dtoConvenios.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoConvenios.getAlineacionEje(), dtoConvenios.getArea()));
            dtoConvenios.setAlineacionEstrategia(dtoConvenios.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dtoConvenios.getEstrategias());

            dtoConvenios.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoConvenios.getAlineacionEstrategia(), dtoConvenios.getArea()));
            dtoConvenios.setAlineacionLinea(dtoConvenios.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dtoConvenios.getLineasAccion());

            dtoConvenios.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoConvenios.getAlineacionLinea(), dtoConvenios.getArea(),dtoConvenios.getRegistro().getConvenio().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoConvenios.getActividades());
        }else{
            dtoConvenios.setAlineacionEje(null);
            dtoConvenios.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoConvenios.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoConvenios.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoConvenios.getAlineacionLinea(), dtoConvenios.getArea(),dtoConvenios.getRegistro().getConvenio().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dtoConvenios.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dtoConvenios.setAlineacionEje((EjesRegistro)event.getNewValue());
        dtoConvenios.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dtoConvenios.getAlineacionEje(), dtoConvenios.getArea()));
        dtoConvenios.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dtoConvenios.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dtoConvenios.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dtoConvenios.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dtoConvenios.getAlineacionEstrategia(), dtoConvenios.getArea()));
        dtoConvenios.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dtoConvenios.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(Convenios convenio){
        try {
            DTOConvenio dtoc = new DTOConvenio();
            dtoc.setConvenio(convenio);
            dtoConvenios.setRegistro(dtoc);
            dtoConvenios.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoConvenios.getRegistro().getConvenio().getRegistro()));
            actualizarEjes(dtoConvenios.getRegistro().getConvenio().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dtoConvenios.getAlineacionActividad(), dtoConvenios.getRegistro().getConvenio().getRegistro());
        if(alineado){
            buscaConvenios();
            abrirAlineacionPOA(dtoConvenios.getRegistro().getConvenio());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dtoConvenios.getRegistro().getConvenio().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dtoConvenios.getRegistro().setActividadAlineada(null);
            try {
                dtoConvenios.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dtoConvenios.getRegistro().getConvenio().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorConvenios.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dtoConvenios.getRegistro().getConvenio().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    /********************************************* Edición ***********************************************/
    public void forzarAperturaEdicionConvenio(){
        if(dtoConvenios.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionConvenio').show();");
            dtoConvenios.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionConvenio(){
        Ajax.update("frmEdicionConvenio");
        Ajax.oncomplete("skin();");
        dtoConvenios.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionConvenio();
    }
    
    public void abrirEdicionConvenio(Convenios convenio) {
        DTOConvenio dtoCon = new DTOConvenio();
        dtoCon.setConvenio(convenio);
        dtoConvenios.setRegistro(dtoCon);
        dtoConvenios.getRegistro().getConvenio().setEmpresa(dtoCon.getConvenio().getEmpresa());
        dtoConvenios.setMensaje("");
        actualizaInterfazEdicionConvenio();
    }
    
    public void editaConvenio(){
        if(ejbConvenios.buscaConvenioExistente(dtoConvenios.getRegistro().getConvenio())){
            dtoConvenios.setMensaje("Ya existe un convenio con las mismas caracteristicas, favor de corregir la empresa o bien la fecha de firma del convenio");
        }else{
            dtoConvenios.getRegistro().setConvenio(ejbConvenios.editaConvenio(dtoConvenios.getRegistro().getConvenio()));
            dtoConvenios.setMensaje("El convenio: " +  dtoConvenios.getRegistro().getConvenio().getEmpresa().getNombre() + " ha sido actualizado correctamente");
            actualizaInterfazEdicionConvenio();
        }
        Ajax.update("mensaje");
    }
    
    public List<OrganismosVinculados> completeOrganismosVinculados(String organismoVinculado) { 
        dtoConvenios.setListaEmpresas(ejbOrganismosVinculados.buscaCoincidenciasOrganismosVinculados(organismoVinculado));
        Faces.setSessionAttribute("organismosVinculados", dtoConvenios.getListaEmpresas());
        return dtoConvenios.getListaEmpresas();
    }
    
    public void seleccionaOrganismoVinculado(ValueChangeEvent event){
        dtoConvenios.getRegistro().getConvenio().setEmpresa((OrganismosVinculados)event.getNewValue());
    }
    
    public void validaFechaFirma(ValueChangeEvent event) {
        dtoConvenios.getRegistro().getConvenio().setFechaFirma((Date)event.getNewValue());
        if (dtoConvenios.getRegistro().getConvenio().getFechaFirma().before(dtoConvenios.getRegistro().getConvenio().getVigencia())) {
        } else {
            if (dtoConvenios.getRegistro().getConvenio().getVigencia().before(dtoConvenios.getRegistro().getConvenio().getFechaFirma())) {
                dtoConvenios.getRegistro().getConvenio().setVigencia(null);
                dtoConvenios.getRegistro().getConvenio().setVigencia(dtoConvenios.getRegistro().getConvenio().getFechaFirma());
            } else {
            }
        }
    }
    
    public void validaFechaVigencia(ValueChangeEvent event) {
        dtoConvenios.getRegistro().getConvenio().setVigencia((Date)event.getNewValue());
        if (dtoConvenios.getRegistro().getConvenio().getVigencia().after(dtoConvenios.getRegistro().getConvenio().getFechaFirma())) {
        } else {
            if (dtoConvenios.getRegistro().getConvenio().getFechaFirma().after(dtoConvenios.getRegistro().getConvenio().getVigencia())) {
                dtoConvenios.getRegistro().getConvenio().setFechaFirma(null);
                dtoConvenios.getRegistro().getConvenio().setFechaFirma(dtoConvenios.getRegistro().getConvenio().getVigencia());
            } else {
            }
        }
    }
    
}
