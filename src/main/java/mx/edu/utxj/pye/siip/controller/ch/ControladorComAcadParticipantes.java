/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComAcadParticipantes;
import mx.edu.utxj.pye.siip.dto.ch.DtoComisionesAcademicas;
import mx.edu.utxj.pye.siip.dto.ch.DtoParticipantesComAcad;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComAcadParticipantes;
import mx.edu.utxj.pye.siip.interfaces.ch.EjbComisionesAcademicas;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEvidenciasAlineacion;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named(value = "partComAcad")
@ViewScoped
public class ControladorComAcadParticipantes implements Serializable{

    private static final long serialVersionUID = 1001739931387675404L;
    //Variables para almacenar el registro
    @Getter @Setter DtoParticipantesComAcad dto;
    @Getter @Setter DtoComisionesAcademicas dtoreg;
    
    @EJB EJBSelectItems ejbItems;
    @EJB EjbComAcadParticipantes ejb;
    @EJB EjbComisionesAcademicas ejbreg;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbEvidenciasAlineacion ejbEvidenciasAlineacion;
    @EJB EjbModulos ejbModulos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorComisionesAcademicas controladorComisionesAcademicas;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init(){
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dtoreg = new DtoComisionesAcademicas();  
        dto = new  DtoParticipantesComAcad();
        dto.setRegistroTipo(new RegistrosTipo());
        dto.getRegistroTipo().setRegistroTipo((short)49);
        dto.setEje(new EjesRegistro());
        dto.getEje().setEje(2);
        
        consultaAreaRegistro(); 
        
        dto.setAreaPOA(ejbFiscalizacion.getAreaConPOA(dto.getArea()));
        dto.setClavesAreasSubordinadas(ejbFiscalizacion.getAreasSubordinadasSinPOA(dto.getAreaPOA()).stream().map(a -> a.getArea()).collect(Collectors.toList()));
        
        controladorComisionesAcademicas.init();
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorComAcadParticipantes.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorComAcadParticipantes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consultaAreaRegistro() {
        try {
            AreasUniversidad areaRegistro = new AreasUniversidad();
            areaRegistro = controladorModulosRegistro.consultaAreaRegistro((short) 1);
            if (areaRegistro == null) {
                dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).getArea());
            } else {
                dto.setArea(areaRegistro.getArea());
            }
        } catch (Exception ex) {
            System.out.println("ControladorComAcadParticipantes.consultaAreaRegistro: " + ex.getMessage());
        }
    }
  
    public void listaComAcadParticipantesPrevia(String rutaArchivo) {
      try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaComAcadParticipantes(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorComAcadParticipantes.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaComAcadParticipantes() {
     if (dto.getLista() != null) {
            try {
                ejb.guardaComAcadParticipantes(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorComAcadParticipantes.class.getName()).log(Level.SEVERE, null, ex);
                if (dto.getRutaArchivo() != null) {
                    ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
                }
            } finally {
                dto.getLista().clear();
                dto.setRutaArchivo(null);
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        dto.getLista().clear();
        if (dto.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
            dto.setRutaArchivo(null);
        }
    }
   
      /* Se agregó para Evidencias por Participante  */
    public void subirEvidencias(){
        Map.Entry<Boolean, Integer> res = ejbEvidenciasAlineacion.registrarEvidenciasARegistro(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro(), dto.getArchivos(), dto.getEventoActual(), dto.getRegistroTipo());
        if (res.getKey()) {
            Messages.addGlobalInfo("Las evidencias se registraron correctamente.");
        } else {
            Messages.addGlobalError(String.format("Se registraron %s de %s evidencias, verifique e intente agregar las evidencias faltantes.", res.getValue().toString(), String.valueOf(dto.getArchivos().size())));
        }
    }
     public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro()));
        Ajax.update("frmEvidenciasPart");
    }
     
    public void consultarEvidencias(){
        dto.setListaEvidencias(ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro()));
        Ajax.update("frmEvidenciasPart");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOComAcadParticipantes registro){
         return ejbEvidenciasAlineacion.getListaEvidenciasPorRegistro(registro.getComisionesAcademicasParticipantes().getRegistro());
    }
    
    public void eliminarEvidencia(EvidenciasDetalle evidencia){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarEvidenciaEnRegistro(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro(), evidencia);
        if(eliminado){ 
            Messages.addGlobalInfo("El archivo se eliminó de forma correcta.");
            cargarEvidenciasPorRegistro();
            Ajax.update("frmEvidenciasPart");
        }else Messages.addGlobalError("El archivo no pudo eliminarse.");
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidenciaPart').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOComAcadParticipantes registro){
//       System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.seleccionarRegistro(): " + registro);
        dto.setRegistro(registro);
        cargarEvidenciasPorRegistro();
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEvidenciasDialogo();
    }
     
    
     public void descargarEvidencia(EvidenciasDetalle evidencia) throws IOException{
        File f = new File(evidencia.getRuta());
        Faces.sendFile(f, false);
    }
    
     /* Se agregó para Alineación a Poa Participantes*/
    
     public void abrirAlineacionPOA(DTOComAcadParticipantes registro){
        dto.setRegistro(registro);        
        dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(registro.getComisionesAcademicasParticipantes().getRegistro()));
        actualizarEjes();
        cargarAlineacionXActividad();
        Ajax.update("frmAlineacionPart");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacionPart').show();");
    }
   
     public void eliminarAlineacion(){
        Boolean eliminado = ejbEvidenciasAlineacion.eliminarAlineacion(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro());
        if(eliminado){ 
            Messages.addGlobalInfo("La alineación se eliminó de forma correcta.");
            dto.getRegistro().setActividadAlineada(null);
            dto.setAlineacionActividad(ejbEvidenciasAlineacion.getActividadAlineada(dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro()));
            actualizarEjes();
            cargarAlineacionXActividad();
            Ajax.update("frmAlineacionPart");
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarAlineacion() alineacion: " + dto.getRegistro().getActividadAlineada());
        }else Messages.addGlobalError("La alineación no pudo eliminarse.");
    }
    
      public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEjes(){
         dto.setEjes(ejbFiscalizacion.getEjes(dto.getEventoActual().getEjercicioFiscal().getAnio(), dto.getAreaPOA()));
        if(!dto.getEjes().isEmpty() && dto.getAlineacionEje() == null){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        }
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
        dto.nulificarEstrategia();
//        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
        dto.nulificarLinea();
//        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    } 
    
    public void cargarAlineacionXActividad(){
        if(dto.getAlineacionActividad() != null){
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() actividad: " + dto.getAlineacionActividad());
            dto.setAlineacionEje(dto.getAlineacionActividad().getCuadroMandoInt().getEje());

            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA()));
            dto.setAlineacionEstrategia(dto.getAlineacionActividad().getCuadroMandoInt().getEstrategia());
            Faces.setSessionAttribute("estrategias", dto.getEstrategias());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() estrategias: " + dto.getEstrategias());

            dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA()));
            dto.setAlineacionLinea(dto.getAlineacionActividad().getCuadroMandoInt().getLineaAccion());
            Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() lineas: " + dto.getLineasAccion());

            dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA(),dto.getEventoActual().getEjercicioFiscal().getAnio()));
            Faces.setSessionAttribute("actividades", dto.getActividades());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.cargarAlineacionXActividad() actividades: " + dto.getActividades());
//            dto.setAlineacionActividad(dto.getAlineacionActividad());
        }else{
            dto.setAlineacionEje(null);
            dto.nulificarEje();
        }
    }
    
     public void alinearRegistro(){
       Boolean alineado = ejbEvidenciasAlineacion.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro().getComisionesAcademicasParticipantes().getRegistro());
        if (alineado) {
            controladorComisionesAcademicas.initFiltros();
            abrirAlineacionPOA(dto.getRegistro());
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        } else {
            Messages.addGlobalError("El registro no pudo alinearse.");
        }
    }
    
}
