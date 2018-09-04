package mx.edu.utxj.pye.siip.controller.ca;

import java.io.Serializable;
import java.util.Date;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoAsesoriasTutorias;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbAsesoriasTutoriasCiclosPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
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
    @EJB EjbModulos ejbModulos;
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoAsesoriasTutorias();        
        dto.setArea((short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        dto.setEventoActual(ejbModulos.getEventoRegistro());
        initFiltros();
    }
    
    public void initFiltros(){
        dto.setPeriodos(ejb.getPeriodosConregistro());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
        cargarListaPorEvento();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setPeriodo((PeriodosEscolares)e.getNewValue());
        dto.setEventosPorPeriodo(ejb.getEventosPorPeriodo(dto.getPeriodo()));
    }
    
    public void cargarListaPorEvento(){
        dto.setLista(ejb.getListaRegistrosPorEvento(dto.getEventoSeleccionado()));
    }
    
    public void listaAsesoriasTutoriasPrevia(String rutaArchivo) {
        try {
            if(rutaArchivo != null){
                dto.setRutaArchivo(rutaArchivo);
                dto.setLista(ejb.getListaAsesoriasTutorias(rutaArchivo));
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
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
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
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
