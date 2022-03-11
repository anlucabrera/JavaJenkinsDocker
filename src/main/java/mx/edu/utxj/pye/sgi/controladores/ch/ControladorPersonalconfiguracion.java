package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.omnifaces.cdi.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidades;
import mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidadesPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Habilidades;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

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
    @Getter    @Setter    private List<Habilidades> habilidadeses = new ArrayList<>();
    @Getter    @Setter    private List<PeriodosEscolares> periodosEscolareses = new ArrayList<>();
    @Getter    @Setter    private List<CategoriasHabilidades> categoriasHabilidadeses = new ArrayList<>();
    @Getter    @Setter    private List<String> estatus = new ArrayList<>();

    @Getter    @Setter    private Iterator<ListaPersonal> empleadoActual;

    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias,personalCategoriasSeleccionada;
    @Getter    @Setter    private AreasUniversidad nuevoOBJAreasUniversidad;
    @Getter    @Setter    private Categorias nuevoOBJCategorias;
    @Getter    @Setter    private Habilidades habilidades;
    @Getter    @Setter    private PeriodosEscolares escolares= new PeriodosEscolares();

    @Getter    @Setter    private Short claveCatagoria = 0,cvlCat360=0;
    @Getter    @Setter    private Integer periodoEv = 0,cvlHab=0,pestaniaActica=0;
    @Getter    @Setter    private Boolean nuevaHabiliad=Boolean.TRUE;
    

    @Getter    @Setter    private Part file;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    
    @Inject    UtilidadesCH utilidadesCH;
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        estatus = new ArrayList<>();
        estatus.clear();
        estatus.add("0");
        estatus.add("1");
        nuevoOBJAreasUniversidad = new AreasUniversidad();
        nuevoOBJPersonalCategorias = new PersonalCategorias();
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
            periodosEscolareses.clear();
            habilidadeses.clear();
            
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
                if (t.getTipo().equals("Específica") && t.getActivo() ) {
                    nuevaListaPersonalCategoriases360.add(t);
                }
            });

            periodosEscolareses = ejbUtilidadesCH.mostrarPeriodosEscolaresEvaluaciones360();
            habilidadeses= ejbUtilidadesCH.mostrarListaHabilidades();
//            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalconfiguracion.generarListas()"+habilidadeses.size());
            escolares=periodosEscolareses.get(0);
            periodoEv=escolares.getPeriodo();
            Collections.sort(nuevaListaAreasUniversidads, (x, y) -> x.getCategoria().getCategoria().compareTo(y.getCategoria().getCategoria()));
            Collections.sort(nuevaListaPersonalsFotosFaltantes, (x, y) -> Short.compare(x.getAreaOperativa(), y.getAreaOperativa()));
            Collections.sort(nuevaListaPersonals, (x, y) -> Short.compare(x.getCategoriaOperativa(), y.getCategoriaOperativa()));
            Collections.sort(nuevaListaPersonalCategoriases, (x, y) -> x.getTipo().compareTo(y.getTipo()));

            consultaCategoriasHabilidades(0);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void numeroProcesoAsiganado(ValueChangeEvent event) {
        pestaniaActica=4;
        periodoEv = Integer.parseInt(event.getNewValue().toString());
        consultaCategoriasHabilidades(0);
    }
    
    public void numeroCategoria(ValueChangeEvent event) {
        try {
            pestaniaActica = 4;
            cvlCat360 = Short.parseShort(event.getNewValue().toString());
            personalCategoriasSeleccionada = new PersonalCategorias();
            if (cvlCat360 != 0) {
                consultaCategoriasHabilidades(1);
            } else {
                consultaCategoriasHabilidades(0);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonalconfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void numeroHanilidad(ValueChangeEvent event) {
        pestaniaActica = 4;
        cvlHab = Integer.parseInt(event.getNewValue().toString());
        if (cvlHab == 0) {
            nuevaHabiliad = Boolean.TRUE;
        } else {
            nuevaHabiliad = Boolean.FALSE;
        }
    }

    public void consultaCategoriasHabilidades(Integer tipo) {
        try {
            pestaniaActica = 4;
            categoriasHabilidadeses = new ArrayList<>();
            if (tipo == 0) {
                categoriasHabilidadeses = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
            } else {
                categoriasHabilidadeses = new ArrayList<>();
                List<CategoriasHabilidades> catHabs = new ArrayList<>();
                catHabs = ejbUtilidadesCH.mostrarCategoriasHabilidades(periodoEv);
                categoriasHabilidadeses = catHabs.stream().filter(t -> Objects.equals(t.getPersonalCategorias().getCategoria(), cvlCat360)).collect(Collectors.toList());
                personalCategoriasSeleccionada = categoriasHabilidadeses.get(0).getPersonalCategorias();
            }
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
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        utilidadesCH.agregarFoto(file, new File("personal".concat(File.separator)));
        //Finalmente se procede a reiniciar las variables utilizadas en el método
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
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void imprimirValores() {
    }
}