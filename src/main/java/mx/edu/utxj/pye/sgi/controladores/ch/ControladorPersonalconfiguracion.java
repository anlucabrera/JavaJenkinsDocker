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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonalconfiguracion implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonals = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonalsFotosFaltantes = new ArrayList<>();
    @Getter    @Setter    private List<Personal> listPersonal = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> nuevaListaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Categorias> nuevaListaCategoriases = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> nuevaListaPersonalCategoriases = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> nuevaListaPersonalCategoriases360 = new ArrayList<>();
    @Getter    @Setter    private List<String> estatus = new ArrayList<>();

    @Getter    @Setter    private Iterator<ListaPersonal> empleadoActual;

    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    @Getter    @Setter    private AreasUniversidad nuevoOBJAreasUniversidad;
    @Getter    @Setter    private Categorias nuevoOBJCategorias;

    @Getter    @Setter    private Short claveCatagoria=0;   

     @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    EjbCarga carga;
    @PostConstruct
    public void init() {       
        estatus=new ArrayList<>();
        estatus.clear();
        estatus.add("0");
        estatus.add("1");
        nuevoOBJAreasUniversidad = new AreasUniversidad();
        nuevoOBJPersonalCategorias=new PersonalCategorias();
        generarListas();       
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListas() {
        try {
            nuevoOBJPersonalCategorias = new PersonalCategorias();
            nuevoOBJAreasUniversidad = new AreasUniversidad();
            nuevoOBJCategorias = new Categorias();

            nuevaListaPersonalsFotosFaltantes.clear();
            nuevaListaPersonalCategoriases360.clear();
            nuevaListaPersonalCategoriases.clear();
            nuevaListaAreasUniversidads.clear();
            nuevaListaCategoriases.clear();
            nuevaListaPersonals.clear();
            listPersonal.clear();

            nuevaListaPersonalCategoriases = ejbUtilidadesCH.mostrarListaPersonalCategorias();
            nuevaListaPersonalsFotosFaltantes = ejbPersonal.mostrarListaDeEmpleados();
            nuevaListaAreasUniversidads = ejbAreasLogeo.mostrarAllAreasUniversidad();
            listPersonal = ejbPersonal.mostrarListaPersonalsPorEstatus(1);
            nuevaListaPersonals = ejbPersonal.mostrarListaDeEmpleados();
            nuevaListaCategoriases = ejbAreasLogeo.mostrarCategorias();

            empleadoActual = nuevaListaPersonals.iterator();
            while (empleadoActual.hasNext()) {
                ListaPersonal next = empleadoActual.next();
                if (next.getClave() != 343) {
                    if (next.getActividad() == 1 || next.getActividad() == 3) {
                        empleadoActual.remove();
                    }
                }
            }

            nuevaListaPersonalCategoriases.forEach((t) -> {
                if (t.getTipo().equals("Específica")) {
                    nuevaListaPersonalCategoriases360.add(t);
                }
            });

            Collections.sort(nuevaListaAreasUniversidads, (x, y) -> x.getCategoria().getCategoria().compareTo(y.getCategoria().getCategoria()));
            Collections.sort(nuevaListaPersonalsFotosFaltantes, (x, y) -> Short.compare(x.getAreaOperativa(), y.getAreaOperativa()));
            Collections.sort(nuevaListaPersonals, (x, y) -> Short.compare(x.getCategoriaOperativa(), y.getCategoriaOperativa()));
            Collections.sort(nuevaListaPersonalCategoriases, (x, y) -> x.getTipo().compareTo(y.getTipo()));
            
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
            nuevoOBJPersonalCategorias = ejbUtilidadesCH.crearNuevoPersonalCategorias(nuevoOBJPersonalCategorias);
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
    
    public String nombreArea(Short clave) {
        try {
            AreasUniversidad areasUniversidad = new AreasUniversidad();
            areasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(clave);
            return areasUniversidad.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
  
    public String responsable(Integer clave) {
        try {           
            if (clave != null) {
                ListaPersonal listaPersonal = new ListaPersonal();
                listaPersonal = ejbPersonal.mostrarListaPersonal(clave);
                if (listaPersonal == null) {
                    return "";
                } else {
                    return listaPersonal.getNombre() + " -- " + listaPersonal.getCategoriaOperativaNombre();
                }
            } else {
                return "";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    public void onRowEdit(RowEditEvent event) {
        try {
            Personal p = new Personal();
            Personal p2 = new Personal();
            p = (Personal) event.getObject();
            p2 = ejbPersonal.mostrarPersonalLogeado(p.getClave());
            p2.setCategoria360(new PersonalCategorias(p.getCategoria360().getCategoria()));
            ejbPersonal.actualizarPersonal(p2);
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditModulos(RowEditEvent event) {
        try {
            AreasUniversidad au = new AreasUniversidad();
            au = (AreasUniversidad) event.getObject();

            ejbAreasLogeo.actualizarAreasUniversidad(au);
            Messages.addGlobalInfo("¡Operación exitosa!");
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorAdmin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!");
    }

}
