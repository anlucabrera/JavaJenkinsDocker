package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonalconfiguracion implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaPersonals = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> nuevaListaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Categorias> nuevaListaCategoriases = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> nuevaListaPersonalCategoriases = new ArrayList<>();
    
    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    @Getter    @Setter    private AreasUniversidad nuevoOBJAreasUniversidad;
    @Getter    @Setter    private Categorias nuevoOBJCategorias;
    
    @Getter    @Setter    private Short claveCatagoria=0;    
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
 
    @PostConstruct
    public void init() {
        nuevoOBJAreasUniversidad = new AreasUniversidad();
        nuevoOBJPersonalCategorias=new PersonalCategorias();
        generarListas();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListas() {
        try {
            nuevoOBJAreasUniversidad = new AreasUniversidad();
            nuevoOBJPersonalCategorias = new PersonalCategorias();
            nuevoOBJCategorias = new Categorias();

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalconfiguracion.generarListas()");
            nuevaListaAreasUniversidads.clear();
            nuevaListaCategoriases.clear();
            nuevaListaPersonals.clear();
            nuevaListaPersonalCategoriases.clear();

            nuevaListaPersonals = ejbSelectec.mostrarListaDeEmpleados();
            nuevaListaPersonalCategoriases = ejbDatosUsuarioLogeado.mostrarListaPersonalCategorias();
            nuevaListaCategoriases = ejbAreasLogeo.mostrarCategorias();
            nuevaListaAreasUniversidads = ejbAreasLogeo.mostrarAreasUniversidad();

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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalconfiguracion.crearNuevasAreaUnivercidad(claveCatagoria)"+claveCatagoria);
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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalconfiguracion.crearNuevasPersonalCategorias(nuevoOBJPersonalCategorias)"+nuevoOBJPersonalCategorias);
            nuevoOBJPersonalCategorias = ejbDatosUsuarioLogeado.crearNuevoPersonalCategorias(nuevoOBJPersonalCategorias);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            generarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
