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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Evidencias;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EventoRegistroNoExistenteException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "asesoriasTutoriasCicloEscolar")
@ViewScoped
public class ControladorAsesoriasTutoriasCicloEscolar implements Serializable{

    private static final long serialVersionUID = -7714155978335195969L;
    
    @Getter @Setter DtoAsesoriasTutorias dto;
    
    @EJB EjbAsesoriasTutoriasCiclosPeriodos ejb;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
//        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.init()");
        dto = new DtoAsesoriasTutorias();        
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        dto.setAreaPOA(ejb.getAreaConPOA(dto.getArea()));
        try {
            dto.setEventoActual(ejbModulos.getEventoRegistro());
        } catch (EventoRegistroNoExistenteException ex) {
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolar.class.getName()).log(Level.SEVERE, null, ex);
        }
        initFiltros();
    }
    
    public void initFiltros(){
        dto.setPeriodos(ejb.getPeriodosConregistro());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        try {
            Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> entrada = ejb.comprobarEventoActual(dto.getPeriodos(), dto.getEventosPorPeriodo(), dto.getEventoActual());
//            System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.initFiltros() entrada: " + entrada);
            if(entrada != null){
                dto.setPeriodos(entrada.getKey());
                dto.setEventosPorPeriodo(entrada.getValue());
            }
        } catch (PeriodoEscolarNecesarioNoRegistradoException ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolar.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarListaPorEvento();
    }
    
    public void abrirAlineacionPOA(DTOAsesoriasTutoriasCicloPeriodos registro){
        dto.setRegistro(registro);
        actualizarEjes();
        Ajax.update("frmAlineacion");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalAlineacion').show();");
    }
    
    public void actualizarActividades(ValueChangeEvent event){
        dto.setAlineacionLinea((LineasAccion)event.getNewValue());
        dto.setActividades(ejbFiscalizacion.getActividadesPorLineaAccion(dto.getAlineacionLinea(), dto.getAreaPOA().getArea()));
        Faces.setSessionAttribute("actividades", dto.getActividades());
    }
    
    public void actualizarEjes(){
        dto.setEjes(ejbFiscalizacion.getEjes(dto.getRegistro().getAsesoriasTutoriasCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio(), dto.getAreaPOA().getArea()));
        dto.getEjes().forEach(e -> System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.actualizarEjes() eje: " + e));
        if(!dto.getEjes().isEmpty()){
            dto.setAlineacionEje(dto.getEjes().get(0));
            dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA().getArea()));
        }
        dto.setAlineacionEje(null);
        Faces.setSessionAttribute("ejes", dto.getEjes());
    }

    public void actualizarEstrategias(ValueChangeEvent event){
        dto.setAlineacionEje((EjesRegistro)event.getNewValue());
        dto.setEstrategias(ejbFiscalizacion.getEstrategiasPorEje(dto.getAlineacionEje(), dto.getAreaPOA().getArea()));
        dto.setAlineacionEstrategia(null);
        Faces.setSessionAttribute("estrategias", dto.getEstrategias());
    }

    public void actualizarLineasAccion(ValueChangeEvent event){
        dto.setAlineacionEstrategia((Estrategias)event.getNewValue());
        dto.setLineasAccion(ejbFiscalizacion.getLineasAccionPorEstrategia(dto.getAlineacionEstrategia(), dto.getAreaPOA().getArea()));
        dto.setAlineacionLinea(null);
        Faces.setSessionAttribute("lineasAccion", dto.getLineasAccion());
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void alinearRegistro(){
        Boolean alineado = ejb.alinearRegistroActividad(dto.getAlineacionActividad(), dto.getRegistro());
        if(alineado){ 
            Messages.addGlobalInfo("El registro se alineó de forma correcta.");
        }else Messages.addGlobalError("El registro no pudo alinearse.");
    }
    
    public void cargarListaPorEvento(){
        dto.setLista(ejb.getListaRegistrosPorEventoAreaPeriodo(dto.getEventoSeleccionado(), dto.getArea(), dto.getPeriodo()));
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void cargarEvidenciasPorRegistro(){
        dto.setListaEvidencias(ejb.getListaEvidenciasPorRegistro(dto.getRegistro()));
        Ajax.update("frmEvidencias");
    }
    
    public List<EvidenciasDetalle> consultarEvidencias(DTOAsesoriasTutoriasCicloPeriodos registro){
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
    
    public void eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro){
        Boolean eliminado = ejb.eliminarRegistro(registro);
//        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.eliminarRegistro() eliminado: " + eliminado);
        if(eliminado){ 
            Messages.addGlobalInfo("El registro se eliminó de forma correcta.");
            cargarListaPorEvento();
        }else Messages.addGlobalError("El registró no pudo eliminarse.");
    }
    
    public void forzarAperturaEvidenciasDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalCargaEvidencia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void seleccionarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro){
//        System.out.println("mx.edu.utxj.pye.siip.controller.ca.ControladorAsesoriasTutoriasCicloEscolar.seleccionarRegistro(): " + registro);
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
    
    public void listaAsesoriasTutoriasPrevia(String rutaArchivo) {
        try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaAsesoriasTutorias(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
            Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolar.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaAsesoriasTutorias(){
        if (dto.getLista() != null) {
            try {
                ejb.guardaAsesoriasTutorias(dto.getLista(), dto.getRegistroTipo(), dto.getEje(), dto.getArea(), controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage());
                Logger.getLogger(ControladorAsesoriasTutoriasCicloEscolar.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void cancelarArchivo() {
        dto.getLista().clear();
        if (dto.getRutaArchivo() != null) {
            ServicioArchivos.eliminarArchivo(dto.getRutaArchivo());
            dto.setRutaArchivo(null);
        }
    }
}
