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
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ContactosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.CorreosEmpresa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EmpresasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacionPK;
import mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.TelefonosEmpresa;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.vin.DtoOrganismosVinculados;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadesVinculacion;
import mx.edu.utxj.pye.siip.dto.vin.DTOOrganismoVinculado;
import mx.edu.utxj.pye.siip.dto.vin.DTOProgramasBeneficiadosVinculacion;
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
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dtoOrganismosVinculado = new DtoOrganismosVinculados();
        
        consultaAreaRegistro(); 
        
        inicializarUbicacion();
        filtros();
        try {
            dtoOrganismosVinculado.setListaProgramasEducativosBeneficiadosV(ejbOrganismosVinculados.getProgramasBeneficiadosVinculacion());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 29);
            if (areaRegistro == null) {
                dtoOrganismosVinculado.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
            } else {
                dtoOrganismosVinculado.setArea(areaRegistro);
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.siip.controller.vin.ControladorOrganismosVinculados.consultaAreaRegistro(): " + e.getMessage());
        }
    }
    
    public void inicializarCatalogos() {
        try {
            dtoOrganismosVinculado.setOrganismoTipos(ejbOrganismosVinculados.getOrganismosTipo());
            dtoOrganismosVinculado.setEmpresaTipos(ejbOrganismosVinculados.getEmpresasTipos());
            dtoOrganismosVinculado.setGiroTipos(ejbOrganismosVinculados.getGirosTipo());
            dtoOrganismosVinculado.setSectorTipos(ejbOrganismosVinculados.getSectoresTipo());
            
            Faces.setSessionAttribute("organismosTipos", dtoOrganismosVinculado.getOrganismoTipos());
            Faces.setSessionAttribute("empresasTipos", dtoOrganismosVinculado.getEmpresaTipos());
            Faces.setSessionAttribute("sectoresTipos", dtoOrganismosVinculado.getSectorTipos());
            Faces.setSessionAttribute("girosTipos", dtoOrganismosVinculado.getGiroTipos());
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializarUbicacion() {
        try {
            dtoOrganismosVinculado.setPais(new Pais(42));
            dtoOrganismosVinculado.setEstado(new Estado(21));
            dtoOrganismosVinculado.setMunicipio(new Municipio(new MunicipioPK(21, 197)));
            dtoOrganismosVinculado.setLocalidad(new Localidad(new LocalidadPK(21, 197, 1)));
            dtoOrganismosVinculado.setPaises(ejbFiscalizacion.getPaises());
            dtoOrganismosVinculado.setEstados(ejbFiscalizacion.getEstadosPorPais(dtoOrganismosVinculado.getPais()));
            dtoOrganismosVinculado.setMunicipios(ejbFiscalizacion.getMunicipiosPorEstado(dtoOrganismosVinculado.getEstado()));
            dtoOrganismosVinculado.setLocalidades(ejbFiscalizacion.getLocalidadesPorMunicipio(dtoOrganismosVinculado.getMunicipio()));
        } catch (Throwable ex) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, ex);
        }
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

            dtoOrganismosVinculado.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoOrganismosVinculado.getAlineacionLinea(), dtoOrganismosVinculado.getArea(),dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dtoOrganismosVinculado.getActividades());
        }else{
            dtoOrganismosVinculado.setAlineacionEje(null);
            dtoOrganismosVinculado.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dtoOrganismosVinculado.setAlineacionLinea((LineasAccion)event.getNewValue());
        dtoOrganismosVinculado.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dtoOrganismosVinculado.getAlineacionLinea(), dtoOrganismosVinculado.getArea(),dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
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
    
    public void forzarAperturaRepresentantesSecundario(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalRepresentantesSecundarios').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void consultaRepresentantesSecundario(){
        dtoOrganismosVinculado.setListaContactosEmpresas(ejbOrganismosVinculados.consultaContactosEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado()));
    }
    
    public void actualizaInterfazRepresentanteSecundario(){
        Ajax.update("frmRepresentantesSecundarios");
        Ajax.update("frmOtroContacto");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaRepresentantesSecundario(); 
    }
    
    public void abrirRepresentantesSecundarios(OrganismosVinculados organismosVinculados){
        DTOOrganismoVinculado registro = new DTOOrganismoVinculado();
        registro.setOrganismoVinculado(organismosVinculados);
        dtoOrganismosVinculado.setRegistro(registro);
        consultaRepresentantesSecundario();
        actualizaInterfazRepresentanteSecundario();
    }
    
    public void guardaRepresentanteSecundario() {
        if (dtoOrganismosVinculado.getContactoEmpresa().getContactoEmpresa() != null) {
            ejbOrganismosVinculados.editaContactoEmpresa(dtoOrganismosVinculado.getContactoEmpresa());
            dtoOrganismosVinculado.setContactoEmpresa(new ContactosEmpresa());
            Messages.addGlobalInfo("El contacto se editó de manera correcta");
            abrirRepresentantesSecundarios(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
        } else {
            dtoOrganismosVinculado.getContactoEmpresa().setEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            if (ejbOrganismosVinculados.guardarContactoEmpresa(dtoOrganismosVinculado.getContactoEmpresa())) {
                dtoOrganismosVinculado.setContactoEmpresa(new ContactosEmpresa());
                Messages.addGlobalInfo("El contacto se agregó de manera correcta.");
                abrirRepresentantesSecundarios(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            } else {
                Messages.addGlobalInfo("El contacto no se ha guardado en la base de datos.");
            }
        }
    }
    
    public void eliminarRepresentanteSecundario(ContactosEmpresa contactoEmpresa){
        Boolean eliminado = ejbOrganismosVinculados.eliminarContactoEmpresa(contactoEmpresa);
        if(eliminado){
            abrirRepresentantesSecundarios(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("El contacto se ha eliminado de la base de datos.");
        }else{
            Messages.addGlobalInfo("El contacto no se ha eliminado de la base de datos.");
        }
    }
    
    public void seleccionaRepresentanteSecundario(ContactosEmpresa contactoEmpresa) {
        dtoOrganismosVinculado.setContactoEmpresa(contactoEmpresa);
        abrirRepresentantesSecundarios(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
    }
    
    public void forzarAperturaOtrosCorreos(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalOtrosCorreos').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void consultaOtrosCorreos(){
        dtoOrganismosVinculado.setListaCorreosEmpresa(ejbOrganismosVinculados.consultaCorreosEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado()));
    }
    
    public void actualizaInterfazCorreo(){
        Ajax.update("frmOtrosCorreos");
        Ajax.update("frmOtroCorreo");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaOtrosCorreos(); 
    }
    
    public void abrirOtrosCorreos(OrganismosVinculados organismosVinculados){
        DTOOrganismoVinculado registro = new DTOOrganismoVinculado();
        registro.setOrganismoVinculado(organismosVinculados);
        dtoOrganismosVinculado.setRegistro(registro);
        consultaOtrosCorreos();
        actualizaInterfazCorreo();
    }
     
    public void guardaOtroCorreo() {
        if (dtoOrganismosVinculado.getCorreoEmpresa().getCorreoEmpresa()!= null) {
            ejbOrganismosVinculados.editaCorreoEmpresa(dtoOrganismosVinculado.getCorreoEmpresa());
            dtoOrganismosVinculado.setCorreoEmpresa(new CorreosEmpresa());
            Messages.addGlobalInfo("El correo electrónico se editó de manera correcta");
            abrirOtrosCorreos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            
        } else {
            dtoOrganismosVinculado.getCorreoEmpresa().setEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            if (ejbOrganismosVinculados.guardarCorreoEmpresa(dtoOrganismosVinculado.getCorreoEmpresa())) {
                dtoOrganismosVinculado.setCorreoEmpresa(new CorreosEmpresa());
                Messages.addGlobalInfo("El correo electrónico se agregó de manera correcta.");
                abrirOtrosCorreos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            } else {
                Messages.addGlobalInfo("El correo electrónico no se ha guardado en la base de datos.");
            }
        }
    }
    
    public void eliminarOtroCorreo(CorreosEmpresa correoEmpresa){
        Boolean eliminado = ejbOrganismosVinculados.eliminarCorreoEmpresa(correoEmpresa);
        if(eliminado){
            abrirOtrosCorreos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("El contacto se ha eliminado de la base de datos.");
        }else{
            Messages.addGlobalInfo("El contacto no se ha eliminado de la base de datos.");
        }
    }
    
    public void seleccionaOtroCorreo(CorreosEmpresa correoEmpresa) {
        dtoOrganismosVinculado.setCorreoEmpresa(correoEmpresa);
        abrirOtrosCorreos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
    }
    
    public void forzarAperturaOtrosTelefonos(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalOtrosTelefonos').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void consultaOtrosTelefonos(){
        dtoOrganismosVinculado.setListaTelefonosEmpresa(ejbOrganismosVinculados.consultaTelefonosEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado()));
    }
    
    public void actualizaInterfazTelefonos(){
        Ajax.update("frmOtrosTelefonos");
        Ajax.update("frmOtroTelefono");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaOtrosTelefonos(); 
    }
    
    public void abrirOtrosTelfonos(OrganismosVinculados organismosVinculados){
        DTOOrganismoVinculado registro = new DTOOrganismoVinculado();
        registro.setOrganismoVinculado(organismosVinculados);
        dtoOrganismosVinculado.setRegistro(registro);
        consultaOtrosTelefonos();
        actualizaInterfazTelefonos();
    }
     
    public void guardaOtroTelefono() {
        if (dtoOrganismosVinculado.getTelefonoEmpresa().getTelefonoEmpresa()!= null) {
            ejbOrganismosVinculados.editaTelefonoEmpresa(dtoOrganismosVinculado.getTelefonoEmpresa());
            dtoOrganismosVinculado.setTelefonoEmpresa(new TelefonosEmpresa());
            Messages.addGlobalInfo("El número télefononico se editó de manera correcta");
            abrirOtrosTelfonos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            
        } else {
            dtoOrganismosVinculado.getTelefonoEmpresa().setEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            if (ejbOrganismosVinculados.guardaTelefonoEmpresa(dtoOrganismosVinculado.getTelefonoEmpresa())) {
                dtoOrganismosVinculado.setTelefonoEmpresa(new TelefonosEmpresa());
                Messages.addGlobalInfo("El número telefonico se agregó de manera correcta.");
                abrirOtrosTelfonos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            } else {
                Messages.addGlobalInfo("El número telefonico no se ha guardado en la base de datos.");
            }
        }
    }
    
    public void eliminarOtroTelefono(TelefonosEmpresa telefonoEmpresa){
        Boolean eliminado = ejbOrganismosVinculados.eliminarTelefonoEmpresa(telefonoEmpresa);
        if(eliminado){
            abrirOtrosTelfonos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("El número telefonico se ha eliminado de la base de datos.");
        }else{
            Messages.addGlobalInfo("El número telefonico no se ha eliminado de la base de datos.");
        }
    }
    
    public void seleccionaOtroTelefono(TelefonosEmpresa telefonoEmpresa) {
        dtoOrganismosVinculado.setTelefonoEmpresa(telefonoEmpresa);
        abrirOtrosTelfonos(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
    }
    
    public void actualizarEstados(ValueChangeEvent event){
        dtoOrganismosVinculado.setPais((Pais)event.getNewValue());
        dtoOrganismosVinculado.setEstados(ejbFiscalizacion.getEstadosPorPais(dtoOrganismosVinculado.getPais()));
        if(dtoOrganismosVinculado.getPais().getIdpais()!=42){
            dtoOrganismosVinculado.setMunicipio(null);
            dtoOrganismosVinculado.setLocalidad(null);
        }
        Faces.setSessionAttribute("estados", dtoOrganismosVinculado.getEstados());
    }
    
    public void actualizarMunicipios(ValueChangeEvent event){
        dtoOrganismosVinculado.setEstado((Estado)event.getNewValue());
        dtoOrganismosVinculado.setMunicipios(ejbFiscalizacion.getMunicipiosPorEstado(dtoOrganismosVinculado.getEstado()));
        Faces.setSessionAttribute("municipios", dtoOrganismosVinculado.getMunicipios());
        dtoOrganismosVinculado.setLocalidad(null);
    }
    
    public void actualizarLocalidades(ValueChangeEvent event){
        dtoOrganismosVinculado.setMunicipio((Municipio)event.getNewValue());
        dtoOrganismosVinculado.setLocalidades(ejbFiscalizacion.getLocalidadesPorMunicipio(dtoOrganismosVinculado.getMunicipio()));
        Faces.setSessionAttribute("localidades", dtoOrganismosVinculado.getLocalidades());
    }

    public void cargarUbicacionXOrganismo(){
        if(dtoOrganismosVinculado.getLocalidad() != null && dtoOrganismosVinculado.getPais()!= null){
            dtoOrganismosVinculado.setPaises(ejbFiscalizacion.getPaises());
            dtoOrganismosVinculado.setPais(dtoOrganismosVinculado.getPais());
            Faces.setSessionAttribute("paises", dtoOrganismosVinculado.getPaises());
            
            dtoOrganismosVinculado.setEstados(ejbFiscalizacion.getEstadosPorPais(dtoOrganismosVinculado.getPais()));
            dtoOrganismosVinculado.setEstado(dtoOrganismosVinculado.getLocalidad().getMunicipio().getEstado());
            Faces.setSessionAttribute("estados", dtoOrganismosVinculado.getEstados());
            
            dtoOrganismosVinculado.setMunicipios(ejbFiscalizacion.getMunicipiosPorEstado(dtoOrganismosVinculado.getEstado()));
            dtoOrganismosVinculado.setMunicipio(dtoOrganismosVinculado.getLocalidad().getMunicipio());
            Faces.setSessionAttribute("municipios", dtoOrganismosVinculado.getMunicipios());
            
            dtoOrganismosVinculado.setLocalidades(ejbFiscalizacion.getLocalidadesPorMunicipio(dtoOrganismosVinculado.getMunicipio()));
            Faces.setSessionAttribute("localidades", dtoOrganismosVinculado.getLocalidades());
            
        }else{
            dtoOrganismosVinculado.setPais(null);
            dtoOrganismosVinculado.nulificarPais();
        }
    }
    
    public void abrirUbicacion(OrganismosVinculados organismoVinculado){
        try {
            DTOOrganismoVinculado dtoOrgVin = new DTOOrganismoVinculado();
            dtoOrgVin.setOrganismoVinculado(organismoVinculado);
            dtoOrganismosVinculado.setRegistro(dtoOrgVin);
            if(ejbOrganismosVinculados.getPaisOrganismoVinculado(organismoVinculado) == null){
                inicializarUbicacion();
            }else{
                dtoOrganismosVinculado.setPais(ejbOrganismosVinculados.getPaisOrganismoVinculado(organismoVinculado));
                dtoOrganismosVinculado.setLocalidad(ejbOrganismosVinculados.getLocalidadOrganismoVinculado(organismoVinculado));
                cargarUbicacionXOrganismo();
            }
            Ajax.update("frmUbicacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalUbicacion').show();");
        } catch (Throwable e) {
            Logger.getLogger(ControladorOrganismosVinculados.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void guardaUbicacion(){
        Boolean actualizado = ejbOrganismosVinculados.guardaUbicacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado(), dtoOrganismosVinculado.getPais(), dtoOrganismosVinculado.getEstado(), dtoOrganismosVinculado.getMunicipio(), dtoOrganismosVinculado.getLocalidad());
        if(actualizado){
            abrirUbicacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("La ubicación se guardó de manera correcta.");
        }else Messages.addGlobalError("La ubicación no se pudo guardar.");
    }
    
    public void eliminarUbicacion(){
        Boolean eliminado = ejbOrganismosVinculados.eliminaUbicacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
        if(eliminado){
            abrirUbicacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
            Messages.addGlobalInfo("La ubicación se eliminó de manera correcta.");
        }else Messages.addGlobalError("La ubicación no se pudo eliminar.");
    }
    
    public void abrirProgramasBeneficiadosVinculacion(OrganismosVinculados orgVin){
        DTOOrganismoVinculado dtov = new DTOOrganismoVinculado();
        dtov.setOrganismoVinculado(orgVin);
        dtoOrganismosVinculado.setRegistro(dtov);
        consultaProgramaBeneficiadoVinculacion();
        Ajax.update("frmProgramasBeneficiadosVinculacion");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaProgramasEducativosBeneficiados();
    }
    
    public void consultaProgramaBeneficiadoVinculacion(){
        dtoOrganismosVinculado.getListaProgramasEducativosBeneficiadosV().stream().forEach((t) -> {
            t.setExiste(ejbOrganismosVinculados.verificaProgramaBeneficiadoVinculacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getEmpresa(), t.getAreaUniversidad()));
        });
    }
    
    public void forzarAperturaProgramasEducativosBeneficiados(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalProgramasBeneficiadosVinculacion').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void guardaProgramaBeneficiadoVinculacion(DTOProgramasBeneficiadosVinculacion dtoOrgVinpbv){
        ProgramasBeneficiadosVinculacionPK pbvpk = new ProgramasBeneficiadosVinculacionPK();
        pbvpk.setEmpresa(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().getEmpresa());
        pbvpk.setProgramaEducativo(dtoOrgVinpbv.getAreaUniversidad().getArea());
        ProgramasBeneficiadosVinculacion programaBeneficiadoVinculacion = new ProgramasBeneficiadosVinculacion();
        programaBeneficiadoVinculacion.setProgramasBeneficiadosVinculacionPK(pbvpk);
        programaBeneficiadoVinculacion.setOrganismosVinculados(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
        Boolean guardado = ejbOrganismosVinculados.guardarProgramaBeneficiadoVinculacion(programaBeneficiadoVinculacion);
        if(guardado){
            abrirProgramasBeneficiadosVinculacion(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado());
        }else  Messages.addGlobalError("El programa educativo no pudo asignarse.");
    }
    
    public void forzarAperturaEdicionOrganismoVinculado(){
        if(dtoOrganismosVinculado.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionOrganismoVinculado').show();");
            dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionOrganismoVinculado(){
        Ajax.update("frmEdicionOrganismoVinculado");
        Ajax.oncomplete("skin();");
        dtoOrganismosVinculado.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionOrganismoVinculado();
    }
    
    public void abrirEdicionOrganismoVinculado(OrganismosVinculados organismoVinculado) {
        if("Si".equals(organismoVinculado.getConvenio())){
            dtoOrganismosVinculado.setTieneConvenio(Boolean.TRUE);
        }else{
            dtoOrganismosVinculado.setTieneConvenio(Boolean.FALSE);
        }
        inicializarCatalogos();
        DTOOrganismoVinculado dtoOrgVin = new DTOOrganismoVinculado();
        dtoOrgVin.setOrganismoVinculado(organismoVinculado);
        dtoOrganismosVinculado.setRegistro(dtoOrgVin);
        dtoOrganismosVinculado.setMensaje("");
        actualizaInterfazEdicionOrganismoVinculado();
    }
    
    public void editaOrganismoVinculado(){
        if(ejbOrganismosVinculados.buscaOrganismoVinculadoExistente(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado())){
            dtoOrganismosVinculado.setMensaje("El nombre de la empresa ya se encuentra asignado por otra empresa, la actualización será descartada");
        }else{
            dtoOrganismosVinculado.getRegistro().setOrganismoVinculado(ejbOrganismosVinculados.editaOrganismoVinculado(dtoOrganismosVinculado.getRegistro().getOrganismoVinculado()));
            dtoOrganismosVinculado.setMensaje("El Organismo Vinculado se ha actualizado");
            actualizaInterfazEdicionOrganismoVinculado();
        }
        Ajax.update("mensaje");
    }
    
    public void actualizarOrganismoTipo(ValueChangeEvent event){
        dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setOrgTip((OrganismosTipo)event.getNewValue());
    }
    
    public void actualizarEmpresasTipo(ValueChangeEvent event){
        dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setEmpTip((EmpresasTipo)event.getNewValue());
    }
    
    public void actualizarSectoresTipo(ValueChangeEvent event){
        dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setSector((SectoresTipo)event.getNewValue());
    }
    
    public void actualizarGirosTipo(ValueChangeEvent event){
        dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setGiro((GirosTipo)event.getNewValue());
    }
    
    public void actualizaTieneConvenio(ValueChangeEvent event){
        dtoOrganismosVinculado.setTieneConvenio((Boolean)event.getNewValue());
        if(dtoOrganismosVinculado.getTieneConvenio()){
            dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setConvenio("Si");
        }else{
            dtoOrganismosVinculado.getRegistro().getOrganismoVinculado().setConvenio("No");
        }
    }
    
}