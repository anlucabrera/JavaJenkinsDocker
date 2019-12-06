/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.pye;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.util.ServicioCURP;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.eb.DtoMatriculaPeriodoEscolar;
import mx.edu.utxj.pye.siip.dto.eb.DTOMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
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
public class ControladorMatriculaPeriodosEscolaresPYE implements Serializable{

    private static final long serialVersionUID = -6081012468934845725L;
    
    @Getter @Setter DtoMatriculaPeriodoEscolar dto;
    
    @EJB    EjbMatriculaPeriodosEscolares   ejb;
    @EJB    EjbModulos                      ejbModulos;
    @EJB    EjbFiscalizacion                ejbFiscalizacion;
    @EJB    EjbCatalogos                    ejbCatalogos;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;

    @PostConstruct
    public void init(){
        dto = new DtoMatriculaPeriodoEscolar();
        dto.setAreaPOA(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setPeriodoEscolarActivo(ejbModulos.getPeriodoEscolarActivo());
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
            dto.setProgramasEducativos(ejbCatalogos.getProgramasEducativos());
            Faces.setSessionAttribute("programasEducativos", dto.getProgramasEducativos());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorMatriculaPeriodosEscolaresPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
    }
    
    public void actualizaPeriodosEscolares(ValueChangeEvent e){
        dto.setAreaUniversidadPOA((AreasUniversidad)e.getNewValue());
        dto.setPeriodos(ejb.getPeriodosConregistro(dto.getRegistroTipo(),dto.getEventoActual()));
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual(), dto.getRegistroTipo());
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorMatriculaPeriodosEscolaresPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    public void cargarListaPorEvento(){
        dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getAreaUniversidadPOA().getArea(), dto.getPeriodo(), dto.getRegistroTipo()));
        dto.getLista().stream().forEach((mpe) -> {
            mpe.getMatricula().setRegistros(ejbModulos.buscaRegistroPorClave(mpe.getMatricula().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void initFiltros(){
        llenaAreasAcademicas();
        Faces.setSessionAttribute("areas", dto.getListaAreasPOA());
    }
    
    public void llenaAreasAcademicas(){
        dto.setListaAreasPOA(ejbCatalogos.getAreasUniversidadDepartamentosConPoa()
                .stream()
                .filter(area -> (short) 10 == area.getArea())
                .collect(Collectors.toList()));
        if (!dto.getListaAreasPOA().isEmpty() && dto.getAreaUniversidadPOA() == null) {
            dto.setAreaUniversidadPOA(null);
        }
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    ///////////////////////////////////// Guardado, Edición y Eliminación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void eliminarRegistro(DTOMatriculaPeriodosEscolares registro){
        try {
            ejbModulos.eliminarEvidenciasEnRegistroGeneral(registro.getMatricula().getRegistro(), ejbModulos.getListaEvidenciasPorRegistro(registro.getMatricula().getRegistro()));
            Boolean eliminado = ejb.eliminarRegistro(registro);
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        } catch (Throwable ex) {
            Logger.getLogger(ControladorMatriculaPeriodosEscolaresPYE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    ///////////////////////////////////// Evidencias \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejb.getListaEvidenciasPorRegistro(dto.getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOMatriculaPeriodosEscolares registro){
        return ejb.getListaEvidenciasPorRegistro(registro);
    }
    
    public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejb.eliminarEvidenciaEnRegistro(dto.getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidencias");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void seleccionarRegistro(DTOMatriculaPeriodosEscolares registro){
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
    
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejb.registrarEvidenciasARegistro(dto.getRegistro(), dto.getArchivos());
        if(res.getKey()){ 
            cargarListaPorEvento();
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        }else{ 
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(),String.valueOf(dto.getArchivos().size())));
        }
    }
    
    ///////////////////////////////////// Alineación \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        dto.nulificarEstrategia();
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
        dto.nulificarLinea();
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejbModulos.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistro().getMatricula().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaUniversidadPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaUniversidadPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaUniversidadPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
    public void abrirAlineacionPOA(DTOMatriculaPeriodosEscolares registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejb.getActividadAlineada(registro));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejb.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro());
        if(alineado){
            cargarListaPorEvento();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void eliminarAlineacion(){
        Boolean eliminado = ejb.eliminarAlineacion(dto.getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La elineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejb.getActividadAlineada(dto.getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacion");
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
    /********************************************* Edición ***********************************************/
    public void forzarAperturaEdicionMatriculaPeriodoEscolar(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicionMatriculaPeriodoEscolar').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazEdicionMatriculaPeriodoEscolar(){
        Ajax.update("frmEdicionMatriculaPeriodoEscolar");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionMatriculaPeriodoEscolar();
    }
    
    public void abrirEdicionMatriculaPeriodoEscolar(MatriculaPeriodosEscolares matPerEsc) {
        DTOMatriculaPeriodosEscolares dtoMaPerEsc = new DTOMatriculaPeriodosEscolares();
        dtoMaPerEsc.setMatricula(matPerEsc);
        dto.setRegistro(dtoMaPerEsc);
        dto.setMensaje("");
        dto.setPeriodoEscolarMatriculaPeriodoEscolar(ejbModulos.buscaPeriodoEscolarEspecifico(dto.getRegistro().getMatricula().getPeriodo()));
        dto.setProgramaEducativoMatriculaPeriodoEscolar(ejbModulos.buscaProgramaEducativoEspecifico(dto.getRegistro().getMatricula().getProgramaEducativo()));
        actualizaInterfazEdicionMatriculaPeriodoEscolar();
    }
    
    public void editaMatriculaPeriodoEscolar(){
        if (ServicioCURP.validaCurp(dto.getRegistro().getMatricula().getCurp())) {
            if (ejb.buscaMatriculaPeriodoEscolarExistente(dto.getRegistro().getMatricula())) {
                dto.setMensaje("La matricula ya ha sido registrada anteriormente");
            } else {
                dto.getRegistro().setMatricula(ejb.editaMatriculaPeriodoEscolar(dto.getRegistro().getMatricula()));
                dto.setMensaje("El registro ha sido actualizado");
                actualizaInterfazEdicionMatriculaPeriodoEscolar();
            }
        } else {
            dto.setMensaje("La CURP que ha ingresado no es válida");
        }
        Ajax.update("mensaje");
    }
    
    public void actualizarProgramaEducativo(ValueChangeEvent event){
        dto.setProgramaEducativoMatriculaPeriodoEscolar(((AreasUniversidad)event.getNewValue()));
        dto.getRegistro().getMatricula().setProgramaEducativo(dto.getProgramaEducativoMatriculaPeriodoEscolar().getArea());
    }
    
}
