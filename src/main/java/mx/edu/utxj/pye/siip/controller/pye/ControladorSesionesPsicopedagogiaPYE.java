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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia;
import mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DTOSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.dto.ca.DtoSesionPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbSesionesPsicopedagogia;
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
public class ControladorSesionesPsicopedagogiaPYE implements Serializable{
    
//    TODO: Pendiente de verificar el modulo completo de administrador
    private static final long serialVersionUID = -1607308138502126918L;
    
    @Getter @Setter DtoSesionPsicopedagogia dto;
    
    @EJB    EjbSesionesPsicopedagogia   ejbSesionesPsicopedagogia;
    @EJB    EjbFiscalizacion            ejbFiscalizacion;
    @EJB    EjbModulos                  ejbModulos;
    @EJB    EjbCatalogos                ejbCatalogos;
    @EJB    EjbAreasLogeo               ejbAreasLogeo;
    
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
            dto = new DtoSesionPsicopedagogia();

            ResultadoEJB<AreasUniversidad> resArea = ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
            if (!resArea.getCorrecto()) {
                return;
            }
            dto.setAreaUniversidadAdministrador(resArea.getValor());

            dto.setLstAreasConflicto(ejbSesionesPsicopedagogia.getListaAreasDeConflicto());
            dto.setLstOtrosTiposSesionesPsicopedagogia(ejbSesionesPsicopedagogia.getListaOtrosTiposSesionesPsicopedagogia());
            dto.setLstProgramasEducativos(ejbCatalogos.getProgramasEducativos());

            Faces.setSessionAttribute("areasConflicto", dto.getLstAreasConflicto());
            Faces.setSessionAttribute("otroTipoSesion", dto.getLstOtrosTiposSesionesPsicopedagogia());
            Faces.setSessionAttribute("programasEducativos", dto.getLstProgramasEducativos());
            filtros();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void filtros(){
        llenaCategorias();
        dto.nulificarCategoria();
        Faces.setSessionAttribute("categorias", dto.getListaCategoriasPOA());
    }
    public void actualizarAreas(ValueChangeEvent e) {
        dto.setCategoria((Categorias) e.getNewValue());
        llenaAreas();
        dto.nulificarAreaPOA();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void actualizarAnios(ValueChangeEvent e){
        dto.setArea((AreasUniversidad)e.getNewValue());
        llenaAnios();
        dto.nulificarAnioConsulta();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setAnioConsulta((short) e.getNewValue());
        llenaMeses();
        buscaSesionesIndividuales();
    }
    
    public void llenaCategorias() {
        dto.setListaCategoriasPOA(ejbCatalogos.getCategoriaAreasConPoa()
                .stream()
                .filter(categoria -> (short) 7 == categoria.getCategoria())
                .collect(Collectors.toList()));
        if (!dto.getListaCategoriasPOA().isEmpty() && dto.getCategoria() == null) {
            dto.setCategoria(null);
        }
    }

    public void llenaAreas() {
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadPorCategoriaConPoa(dto.getCategoria())
                .stream()
                .filter(area -> (short) 18 == area.getArea())
                .collect(Collectors.toList()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getArea() == null) {
            dto.setArea(null);
        }
    }
    
    public void llenaAnios() {
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistros(dto.getRegistros(), dto.getArea()));
        if (!dto.getAniosConsulta().isEmpty()) {
            dto.setAnioConsulta(null);
        }
    }
    
    public void llenaMeses() {
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(), dto.getArea()));
        if (!dto.getMesesConsulta().isEmpty()) {
            dto.setMesConsulta(null);
        }
    }
    
    public void buscaSesionesIndividuales() {
        if (dto.getMesConsulta() != null && !dto.getMesesConsulta().isEmpty()) {
            dto.setLstSesionesPsicopedagogia(ejbSesionesPsicopedagogia.getFiltroSesionesIndividualesPorAreaEjercicioMesArea(dto.getArea(), dto.getAnioConsulta(), dto.getMesConsulta()));
            dto.getLstSesionesPsicopedagogia().stream().forEach((sp) -> {
                sp.getSesionIndividualMensualPsicopedogia().setRegistros(ejbModulos.buscaRegistroPorClave(sp.getSesionIndividualMensualPsicopedogia().getRegistro()));
            });
        } else {
            dto.setLstSesionesPsicopedagogia(Collections.EMPTY_LIST);
        }
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void eliminarRegistro(Integer registro){
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro, ejbModulos.getListaEvidenciasPorRegistro(registro));
            ejbModulos.eliminarRegistro(registro);
            buscaSesionesIndividuales();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    /*********************************** Registro de evidencias ****************************************/
    
    public List<EvidenciasDetalle> consultarEvidencias(Integer registro) throws Throwable {
        return ejbModulos.getListaEvidenciasPorRegistro(registro);
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOSesionesPsicopedagogia dtoSP){
        dto.setDtoSesionPsicopedagogia(dtoSP);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                buscaSesionesIndividuales();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros(), evidencia);
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
    
    /**************************** Nuevo registro y Edición de registro *********************************/
    
    public void forzarAperturaSesionPsicopedagogia(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalRegistroSesionPsicopedagogia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazSesionPsicopedagogia(){
        Ajax.update("frmRegistroSesionPsicopedagogia");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaSesionPsicopedagogia(); 
    }
    
    public void abrirNuevoRegistroSesionPsicopedagogia(){
        if (dto.getArea()!= null) {
            dto.setNuevoRegistro(Boolean.TRUE);
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            dto.setMensaje("");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            Messages.addGlobalWarn("Debes seleccionar un área para poder dar de alta un nuevo registro");
        }
    }
    
    public void abrirEdicionSesionPsicopedagogia(DTOSesionesPsicopedagogia sesionPsicopedagogia) {
        try {
            dto.setNuevoRegistro(Boolean.FALSE);
            dto.setDtoSesionPsicopedagogia(sesionPsicopedagogia);
            accionProgramaEducativo(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getOtroTipoSesion());
            if (dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getOtroTipoSesion().getOtroTipoSesionPsicopedagogia() == 2) {
                dto.setLstProgramasEducativos(ejbAreasLogeo.mostrarAllAreasUniversidad());
                Faces.setSessionAttribute("programasEducativos", dto.getLstProgramasEducativos());
            }
            if (dto.getHabilitaProgramaEducativo()) {
                dto.setProgramaEducativo(dto.getDtoSesionPsicopedagogia().getProgramaEducativo());
                Faces.setSessionAttribute("programasEducativos", dto.getLstProgramasEducativos());
            } else {
                dto.setProgramaEducativo(null);
            }
            dto.setMensaje("");
            actualizaInterfazSesionPsicopedagogia();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarSesionPsipedagogia(SesionIndividualMensualPsicopedogia sesionPsicopedagogia) {
        sesionPsicopedagogia.setMes(controladorModulosRegistro.getEventosRegistros().getMes());
        sesionPsicopedagogia.setProgramaEducativo(dto.getProgramaEducativo().getArea());
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia,controladorModulosRegistro.getEventosRegistros().getEjercicioFiscal().getAnio()).isEmpty()) {
            dto.setMensaje("No se ha podido guardar, los datos que ha ingresado corresponden con un registro previamente registrado");
            actualizaInterfazSesionPsicopedagogia();
        } else {
            ejbSesionesPsicopedagogia.guardaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia, dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void guardarSesionPsipedagogiaSPE(SesionIndividualMensualPsicopedogia sesionPsicopedagogia) {
        sesionPsicopedagogia.setMes(controladorModulosRegistro.getEventosRegistros().getMes());
        sesionPsicopedagogia.setProgramaEducativo(null);
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaSPE(sesionPsicopedagogia,controladorModulosRegistro.getEventosRegistros().getEjercicioFiscal().getAnio()).isEmpty()) {
            dto.setMensaje("No se ha podido guardar, los datos que ha ingresado corresponden con un registro previamente registrado");
            actualizaInterfazSesionPsicopedagogia();
        } else {
            ejbSesionesPsicopedagogia.guardaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia, dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }

    public void editaSesionPsicopedagogia(SesionIndividualMensualPsicopedogia sesionPsicopedagogia){
        DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
        sesionPsicopedagogia.setProgramaEducativo(dto.getProgramaEducativo().getArea());
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaParaEdicion(sesionPsicopedagogia,controladorModulosRegistro.getEventosRegistros().getEjercicioFiscal().getAnio()).isEmpty()) {
            dto.setMensaje("No se ha podido actualizar, los datos que ha ingresado corresponden con un registro previo");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            dto.setMensaje(ejbSesionesPsicopedagogia.editaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia));
            dtoSP.setSesionIndividualMensualPsicopedogia(sesionPsicopedagogia);
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void editaSesionPsicopedagogiaSPE(SesionIndividualMensualPsicopedogia sesionPsicopedagogia){
        DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaSPEParaEdicion(sesionPsicopedagogia,controladorModulosRegistro.getEventosRegistros().getEjercicioFiscal().getAnio()).isEmpty()) {
            dto.setMensaje("No se ha podido actualizar, los datos que ha ingresado corresponden con un registro previo");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            if(dto.getProgramaEducativo() == null){
                sesionPsicopedagogia.setProgramaEducativo(null);
            }
            dto.setMensaje(ejbSesionesPsicopedagogia.editaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia));
            dtoSP.setSesionIndividualMensualPsicopedogia(sesionPsicopedagogia);
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void accionSesionPsicopedagogia(SesionIndividualMensualPsicopedogia sesionIndividualPsicopedagogia){
        if(dto.getNuevoRegistro()){
            if(dto.getHabilitaProgramaEducativo()){
                guardarSesionPsipedagogia(sesionIndividualPsicopedagogia);
            }else{
                guardarSesionPsipedagogiaSPE(sesionIndividualPsicopedagogia);
            }
        }else{
            if(dto.getHabilitaProgramaEducativo()){
                editaSesionPsicopedagogia(sesionIndividualPsicopedagogia);
            }else{
                editaSesionPsicopedagogiaSPE(sesionIndividualPsicopedagogia);
            }
            
        }
    }
    
    public void habilitaProgramaEducativo(ValueChangeEvent e){
        accionProgramaEducativo((OtrosTiposSesionesPsicopedagogia)e.getNewValue());
    }
    
    public void accionProgramaEducativo(OtrosTiposSesionesPsicopedagogia otroTipoSesionPsicopedagogia) {
        try {
            if ((otroTipoSesionPsicopedagogia.getOtroTipoSesionPsicopedagogia() == 1) || (otroTipoSesionPsicopedagogia.getOtroTipoSesionPsicopedagogia() == 3)) {
                dto.setHabilitaProgramaEducativo(Boolean.FALSE);
            } else {
                dto.setHabilitaProgramaEducativo(Boolean.TRUE);
                if (otroTipoSesionPsicopedagogia.getOtroTipoSesionPsicopedagogia() == 2) {
                    dto.setLstProgramasEducativos(ejbAreasLogeo.mostrarAllAreasUniversidad());
                    Faces.setSessionAttribute("programasEducativos", dto.getLstProgramasEducativos());
                }
            }
            dto.setMensaje("");
            Ajax.update("mensaje");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizaProgramaEducativoChange(ValueChangeEvent e){
        dto.setProgramaEducativo((AreasUniversidad)e.getNewValue());
    }
    
    /****************************** Alineación de actividades con registros ***********************************************/
    public Boolean verificaAlineacion(Integer registro) throws Throwable{
        return ejbModulos.verificaActividadAlineadaGeneral(registro);
    }
    
    public void actualizarEjes(Short ejercicio){
        dto.setEjes(ejbFiscalizacion.getEjes(ejercicio, dto.getArea()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getArea()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getArea(),dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getArea(),dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getArea()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }
    
    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getArea()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void abrirAlineacionPOA(DTOSesionesPsicopedagogia sesionesP){
        try {
            dto.setDtoSesionPsicopedagogia(sesionesP);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistro()));
            actualizarEjes(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistro());
        if(alineado){
            buscaSesionesIndividuales();
            abrirAlineacionPOA(dto.getDtoSesionPsicopedagogia());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getDtoSesionPsicopedagogia().setActividadAlineada(null);
            try {
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistro()));
            } catch (Throwable ex) {
                Logger.getLogger(ControladorSesionesPsicopedagogiaPYE.class.getName()).log(Level.SEVERE, null, ex);
            }
            actualizarEjes(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio());
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
}
