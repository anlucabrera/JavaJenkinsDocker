/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DTOAsesoriasTutoriasCuatrimestrales;
import mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriaTutoriaCuatrimestral;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCuatrimestrales;
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
public class ControladorAsesoriasTutoriasCuatrimestrales implements Serializable{
    private static final long serialVersionUID = 1280210182688329086L;
    @Getter @Setter DtoAsesoriaTutoriaCuatrimestral dto;
    
    @EJB    EjbAsesoriasTutoriasCuatrimestrales     ejb;
    @EJB    EjbFiscalizacion                        ejbFiscalizacion;
    @EJB    EjbModulos                              ejbModulos;
    
    @Inject     ControladorEmpleado                 controladorEmpleado;
    @Inject     ControladorModulosRegistro          controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoAsesoriaTutoriaCuatrimestral();
        dto.setAreaPOA(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
            dto.setListaDatosAsesoriasTutorias(ejb.getDatosAsesoriasTutorias());
            Faces.setSessionAttribute("datosAsesoriasTutorias", dto.getListaDatosAsesoriasTutorias());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
    }
    
    public void cargarListaPorEvento(){
        dto.setListaDtoAsesoriasTutoriasCuatrimestrales(ejb.getListaAsesoriaTutoriaCuatrimestralPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getAreaPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo()));
        dto.getListaDtoAsesoriasTutoriasCuatrimestrales().stream().forEach((datc) -> {
            datc.getAsesoriaTutoriaCuatrimestral().setRegistros(ejbModulos.buscaRegistroPorClave(datc.getAsesoriaTutoriaCuatrimestral().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegistroTipo(),dto.getEventoActual(),dto.getAreaPOA()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegistroTipo(),dto.getAreaPOA());
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    /************************************************ Modales de Edición y Alta de Asesorias y Tutorias Cuatrimestrales ******************************************************/
    
    public void forzarAperturaEdicionAsesoriaTutoriaMensual(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionAsesoriaTutoria').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionAsesoriaTutoriaMensual(){
        Ajax.update("frmEdicionAsesoriaTutoria");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionAsesoriaTutoriaMensual();
    }
    
    /************************************************** Alta de Asesorías o Tutorías Cuatrimestrales Mediante Formulario **************************************************/
    public void nuevoRegistroAsesoriaTutoria(){
        if(dto.getAreaPOA()!= null){
            dto.setNuevoRegistro(Boolean.TRUE);
            DTOAsesoriasTutoriasCuatrimestrales dtoAT = new DTOAsesoriasTutoriasCuatrimestrales();
            dtoAT.setAsesoriaTutoriaCuatrimestral(new AsesoriasTutoriasCuatrimestrales());
            dtoAT.setPeriodosEscolares(ejbModulos.getPeriodoEscolarActivo());
            dtoAT.getAsesoriaTutoriaCuatrimestral().setPeriodoEscolar(ejbModulos.getPeriodoEscolarActivo().getPeriodo());
            dtoAT.getAsesoriaTutoriaCuatrimestral().setArea(dto.getAreaPOA().getArea());

            dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.getPeriodoEscolarActivo());
            
            dto.setDtoAsesoriaTutoriaCuatrimestralR(dtoAT);
            dto.setMensaje("");
            actualizaInterfazEdicionAsesoriaTutoriaMensual();
        }else{
            Messages.addGlobalWarn("Debes seleccionar un área para poder dar de alta un asesoría o tutoría");
        }
    }
    
    public void guardarAsesoriaTutoria(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral){
        if(!ejb.buscaAsesoriaTutoriaCuatrimestralParaGuardado(asesoriaTutoriaCuatrimestral.getAsesoriaTutoriaCuatrimestral()).isEmpty()){
            dto.setMensaje("Los datos que ha ingresado corresponde a una asesoría o tutoría ya existente, favor de verificar su información");
        }else{
            ejb.guardaAsesoriaTutoriaCuatrimestral(asesoriaTutoriaCuatrimestral, dto.getRegistroTipo(), dto.getEje(), dto.getAreaPOA().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            DTOAsesoriasTutoriasCuatrimestrales dtoAT = new DTOAsesoriasTutoriasCuatrimestrales();
            dtoAT.setAsesoriaTutoriaCuatrimestral(new AsesoriasTutoriasCuatrimestrales());

            dtoAT.setPeriodosEscolares(ejbModulos.getPeriodoEscolarActivo());
            dtoAT.getAsesoriaTutoriaCuatrimestral().setArea(dto.getAreaPOA().getArea());
            dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.getPeriodoEscolarActivo());
            
            dto.setDtoAsesoriaTutoriaCuatrimestralR(dtoAT);
            cargarListaPorEvento();
        }
        Ajax.update("mensaje");
    }
    
    /******************************************************************** Eliminación **************************************************************************************/
    public void eliminarRegistro(DTOAsesoriasTutoriasCuatrimestrales registro) {
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getAsesoriaTutoriaCuatrimestral().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getAsesoriaTutoriaCuatrimestral().getRegistro()));
            ejbModulos.eliminarRegistro(registro.getAsesoriaTutoriaCuatrimestral().getRegistro());
            initFiltros();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
            Messages.addGlobalError("<b>¡No se pudo eliminar el registro seleccionado!</b> ");
        }
    }
    
    /************************************************** Edición de Asesorías o Tutorías Cuatrimestrales Mediante Formulario **************************************************/
    public void abrirEdicionAsesoriasTutoriasCuatrimestral(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral) {
        dto.setNuevoRegistro(Boolean.FALSE);
        dto.setDtoAsesoriaTutoriaCuatrimestralR(asesoriaTutoriaCuatrimestral);
        dto.setPeriodoEscolarAsesoriaTutoria(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getPeriodoEscolar()));
        dto.setMensaje("");
        actualizaInterfazEdicionAsesoriaTutoriaMensual();
    }
    
    public void editaAsesoriaTutoriaCuatrimestral(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoria){
        if(!ejb.buscaAsesoriaTutoriaCuatrimestralParaEdicion(asesoriaTutoria.getAsesoriaTutoriaCuatrimestral()).isEmpty()){
            dto.setMensaje("No se ha podido actualizar debido a que el sistema ha detectado un registro con las mismas caracteristicas, favor de intentar nuevamente");
            Ajax.update("mensaje");
            actualizaInterfazEdicionAsesoriaTutoriaMensual();
        }else{
            dto.setMensaje(ejb.editaAsesoriaTutoriaCuatrimestralPeriodoEscolar(asesoriaTutoria));
            Ajax.update("mensaje");
            actualizaInterfazEdicionAsesoriaTutoriaMensual();
        }
        cargarListaPorEvento();
    }
    
    public void accionAsesoriaTutoria(DTOAsesoriasTutoriasCuatrimestrales asesoriaTutoria){
        if(dto.getNuevoRegistro()){
            guardarAsesoriaTutoria(asesoriaTutoria);
        }else{
            editaAsesoriaTutoriaCuatrimestral(asesoriaTutoria);
        }
    }
    
    /******************************************************* Evidencias ************************************************************/
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void cargarEvidenciasPorRegistro(){
        try {
            dto.setListaEvidencias(ejbModulos.getListaEvidenciasPorRegistro(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistro()));
            Ajax.update("frmEvidencias");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOAsesoriasTutoriasCuatrimestrales dtoAsesoriaTutoriaC) throws Throwable{
        return ejbModulos.getListaEvidenciasPorRegistro(dtoAsesoriaTutoriaC.getAsesoriaTutoriaCuatrimestral().getRegistro());
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbModulos.eliminarEvidenciaEnRegistro(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistros(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void seleccionarRegistro(DTOAsesoriasTutoriasCuatrimestrales registro){
        dto.setDtoAsesoriaTutoriaCuatrimestralR(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        try {
            Map.Entry<Boolean, Integer> res = ejbModulos.registrarEvidenciasARegistro(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistros(), dto.getArchivos());
            if(res.getKey()){
                cargarListaPorEvento();
                Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
            }else{
                Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /************************************************************ Alineación ***************************************************************/
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro
                )event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
            
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void abrirAlineacionPOA(DTOAsesoriasTutoriasCuatrimestrales registro){
        try {
            dto.setDtoAsesoriaTutoriaCuatrimestralR(registro);
            dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(registro.getAsesoriaTutoriaCuatrimestral().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
            Ajax.oncomplete("skin();");
            Ajax.oncomplete("PF('modalAlineacion').show();");
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejbModulos.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getDtoAsesoriaTutoriaCuatrimestralR());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion() {
        try {
            Boolean eliminado = ejbModulos.eliminarAlineacion(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistro());
            if (eliminado) {
                Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
                dto.getDtoAsesoriaTutoriaCuatrimestralR().setActividadAlineada(null);
                dto.setAlineacionActividad(ejbModulos.getActividadAlineadaGeneral(dto.getDtoAsesoriaTutoriaCuatrimestralR().getAsesoriaTutoriaCuatrimestral().getRegistro()));
                actualizarEjes();
                cargarAlineacionXActividad();
                Ajax.update("frmAlineacion");
            } else {
                Messages.addGlobalError("La alineación no pudo eliminarse.");
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCuatrimestrales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
