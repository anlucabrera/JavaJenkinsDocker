package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonalconfiguracion implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonals = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonalsFotosFaltantes = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> nuevaListaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Categorias> nuevaListaCategoriases = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> nuevaListaPersonalCategoriases = new ArrayList<>();

    @Getter    @Setter    private Iterator<ListaPersonal> empleadoActual;

    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    @Getter    @Setter    private AreasUniversidad nuevoOBJAreasUniversidad;
    @Getter    @Setter    private Categorias nuevoOBJCategorias;

    @Getter    @Setter    private Short claveCatagoria=0;   

     @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    EjbCarga carga;
    @PostConstruct
    public void init() {
        System.out.println("ControladorPersonalconfiguracion Inicio: " + System.currentTimeMillis());
        nuevoOBJAreasUniversidad = new AreasUniversidad();
        nuevoOBJPersonalCategorias=new PersonalCategorias();
        generarListas();
        System.out.println("ControladorPersonalconfiguracion Fin: " + System.currentTimeMillis());
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListas() {
        try {
            nuevoOBJPersonalCategorias = new PersonalCategorias();
            nuevoOBJAreasUniversidad = new AreasUniversidad();
            nuevoOBJCategorias = new Categorias();

            nuevaListaPersonalsFotosFaltantes.clear();
            nuevaListaPersonalCategoriases.clear();
            nuevaListaAreasUniversidads.clear();
            nuevaListaCategoriases.clear();
            nuevaListaPersonals.clear();

            nuevaListaPersonals = ejbSelectec.mostrarListaDeEmpleados();
            nuevaListaPersonalsFotosFaltantes = ejbSelectec.mostrarListaDeEmpleados();
            nuevaListaPersonalCategoriases = ejbDatosUsuarioLogeado.mostrarListaPersonalCategorias();
            nuevaListaCategoriases = ejbAreasLogeo.mostrarCategorias();
            nuevaListaAreasUniversidads = ejbAreasLogeo.mostrarAreasUniversidad();

            empleadoActual = nuevaListaPersonals.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getActividad() == 1 || next.getActividad() == 3) {
                    empleadoActual.remove();
                }
            }
            
            Collections.sort(nuevaListaAreasUniversidads, (x, y) -> x.getCategoria().getCategoria().compareTo(y.getCategoria().getCategoria()));
            Collections.sort(nuevaListaPersonalCategoriases, (x, y) -> x.getTipo().compareTo(y.getTipo()));
            Collections.sort(nuevaListaPersonals, (x, y) -> Short.compare(x.getCategoriaOperativa(),y.getCategoriaOperativa()));
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearNuevasAreaUnivercidad() {
        try {
            nuevoOBJAreasUniversidad.setCategoria(new Categorias(claveCatagoria));
            nuevoOBJAreasUniversidad = ejbAreasLogeo.agregarAreasUniversidad(nuevoOBJAreasUniversidad);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarAreaUnivercidad() {
        try {            
            nuevoOBJAreasUniversidad = ejbAreasLogeo.actualizarAreasUniversidad(nuevoOBJAreasUniversidad);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearNuevasPersonalCategorias() {
        try {
            nuevoOBJPersonalCategorias = ejbDatosUsuarioLogeado.crearNuevoPersonalCategorias(nuevoOBJPersonalCategorias);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaDistincion() {
        if (file != null) {
            ruta = carga.subirFotoPersonal(file, new File("personal".concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                Messages.addGlobalInfo("Foto agregada!!");
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
        file = null;
    }
}
