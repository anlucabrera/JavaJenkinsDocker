package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonal implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<AreasUniversidad> listaAreasUniversidads = new ArrayList<>(),listareasSuperiores = new ArrayList<>();
    @Getter    @Setter    private List<InformacionAdicionalPersonal> nuevaListaInformacionAdicionalPersonalSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>(), nuevaListaFuncionesGenerales = new ArrayList<>(), estatus = new ArrayList<>(),rutasEvidencias=new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Personal> listaPersonal = new ArrayList<>(), listaPersonalSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonaSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> listaPersonalCategorias = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> listaPersonalCategorias360 = new ArrayList<>();
    @Getter    @Setter    private List<Grados> listaGrados = new ArrayList<>();
    @Getter    @Setter    private List<Actividades> listaActividades = new ArrayList<>();
    @Getter    @Setter    private List<Short> listaClaveAS = new ArrayList<>();

    @Getter    @Setter    private Short actividad=0,categoriaOP=0,categoriaOF=0,categoria360 = 0,grado=0;
    @Getter    @Setter    private String clase = "";
    @Getter    @Setter    private Funciones nuevoOBJFunciones;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private Integer usuario,contactoDestino, total = 0, tv1 = 0, tv2 = 0, tv3 = 0, tv4 = 0, tv5 = 0, tv6 = 0, tv7 = 0, tv8 = 0, tv9 = 0;
    @Getter    @Setter    private FormacionAcademica oBJFormacionAcademica,selectecOBJFormacionAcademica;
    @Getter    @Setter    private ExperienciasLaborales oBJLaborales,selectecOBJLaborales;   
    @Getter    @Setter    private Capacitacionespersonal oBJCapacitacionespersonal,selectecOBJCapacitacionespersonal;
    @Getter    @Setter    private Idiomas oBJIdiomas,selectecOBJIdiomas;
    @Getter    @Setter    private DesarrollosTecnologicos oBJDesarrollosTecnologicoses,selectecOBJDesarrollosTecnologicoses;
    @Getter    @Setter    private Distinciones oBJDistincioneses,selectecOBJDistincioneses;
    @Getter    @Setter    private LibrosPub oBJLibrosPubs,selectecOBJLibrosPubs;
    @Getter    @Setter    private Articulosp oBJoBJArticulosps,selectecOBJoBJArticulosps;
    @Getter    @Setter    private Memoriaspub oBJMemoriaspubs,selectecOBJMemoriaspubs;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal, nuevoOBJListaPersonalFiltroAreas;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado;
    @Getter    @Setter    private String ruta1 = "",ruta2 = "",ruta3 = "",ruta4 = "",ruta5 = "";
    
    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    
    @Inject    ControladorEmpleado controladorEmpleado;
     
    @PostConstruct
    public void init() {
        System.out.println("ControladorPersonal Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        nuevOBJPersonalSubordinado = new Personal();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
        mostrarContactosParaNotificacion();
        generarListasAreas();
        System.out.println("ControladorPersonal Fin: " + System.currentTimeMillis());
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void generarListasAreas() {
        try {
            listaGrados.clear();
            listaClaveAS.clear();
            listaActividades.clear();
            listareasSuperiores.clear();
            listaAreasUniversidads.clear();
            listaPersonalCategorias.clear();
            listaPersonalCategorias360.clear();
            
            
            listaAreasUniversidads = ejbAreasLogeo.mostrarAreasUniversidad();
            listaAreasUniversidads.forEach((t) -> {
                if (t.getCategoria().getCategoria() == Short.parseShort("10") || t.getCategoria().getCategoria() == Short.parseShort("9")) {
                } else {
                    listareasSuperiores.add(t);
                }
            });

            Collections.sort(listareasSuperiores, (x, y) -> x.getNombre().compareTo(y.getNombre()));
            listaGrados = ejbDatosUsuarioLogeado.mostrarListaGrados();
            listaActividades = ejbDatosUsuarioLogeado.mostrarListaActividades();
            listaPersonalCategorias = ejbDatosUsuarioLogeado.mostrarListaPersonalCategorias();
            
            listaPersonalCategorias.forEach((t) -> {
                if (t.getTipo().equals("Específica")) {
                    listaPersonalCategorias360.add(t);
                }
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilSubordinado() {
        try {
            nuevaListaListaPersonaSubordinado.clear();
            nuevaListaInformacionAdicionalPersonalSubordinado.clear();
            listaPersonalSubordinado.clear();

            nuevoOBJInformacionAdicionalPersonal = ejbDatosUsuarioLogeado.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevOBJPersonalSubordinado = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(contactoDestino);

            informacionCV();
            mostrarFuncioneSubordinado();
            convertirRutaDatosPersonales();
            
            actividad = nuevOBJPersonalSubordinado.getActividad().getActividad();
            categoriaOP = nuevOBJPersonalSubordinado.getCategoriaOperativa().getCategoria();
            categoriaOF = nuevOBJPersonalSubordinado.getCategoriaOficial().getCategoria();
            categoria360 = nuevOBJPersonalSubordinado.getCategoria360().getCategoria();
            grado = nuevOBJPersonalSubordinado.getGrado().getGrado();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarContactosParaNotificacion() {
        try {
            listaPersonal.clear();
            listaPersonal = ejbSelectec.mostrarListaDeEmpleadosTotalActivos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }

    public void mostrarFuncioneSubordinado() {
        try {
            nuevaListaFuncionesGenerales.clear();
            nuevaListaFuncionesEspecificas.clear();
            switch (nuevoOBJListaPersonal.getCategoriaOperativa()) {
                case 30:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 32:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 34:
                    if (nuevoOBJListaPersonal.getAreaOperativa() >= 24 && nuevoOBJListaPersonal.getAreaOperativa() <= 56) {
                        listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 18:
                    if (nuevoOBJListaPersonal.getAreaOperativa() >= 24 && nuevoOBJListaPersonal.getAreaOperativa() <= 56) {
                        listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 41:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                default:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
            }
            if (listaFuncioneSubordinado.isEmpty()) {
            } else {
                for (int i = 0; i <= listaFuncioneSubordinado.size() - 1; i++) {
                    nuevoOBJFunciones = new Funciones();
                    nuevoOBJFunciones = listaFuncioneSubordinado.get(i);
                    if ("GENERAL".equals(nuevoOBJFunciones.getTipo())) {
                        nuevaListaFuncionesGenerales.add(nuevoOBJFunciones.getNombre());
                    } else {
                        nuevaListaFuncionesEspecificas.add(nuevoOBJFunciones.getNombre());
                    }
                }
            }
            listaFuncioneSubordinado.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            clase = String.valueOf(event.getObject().getClass());
            switch (clase) {
                case "class mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica":
                    nombreTabla = "Formacion Academica";
                    ejbEducacion.actualizarFormacionAcademica((FormacionAcademica) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales":
                    nombreTabla = "Experiencias Laborales";
                    ejbEducacion.actualizarExperienciasLaborales((ExperienciasLaborales) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal":
                    nombreTabla = "Capacitaciones personal";
                    ejbEducacion.actualizarCapacitacionespersonal((Capacitacionespersonal) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.Idiomas":
                    nombreTabla = "Idiomas";
                    ejbHabilidades.actualizarIdiomas((Idiomas) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos":
                    nombreTabla = "Desarrollos Tecnologicos";
                    ejbTecnologia.actualizarDesarrollosTecnologicos((DesarrollosTecnologicos) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.Distinciones":
                    nombreTabla = "Distinciones";
                    ejbPremios.actualizarDistinciones((Distinciones) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.LibrosPub":
                    nombreTabla = "Libros Publicados";
                    ejbProduccionProfecional.actualizarLibrosPub((LibrosPub) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.Articulosp":
                    nombreTabla = "Articulos Publicados";
                    ejbProduccionProfecional.actualizarArticulosp((Articulosp) event.getObject());
                    break;
                case "class mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub":
                    nombreTabla = "Memorias Publicados";
                    ejbProduccionProfecional.actualizarMemoriaspub((Memoriaspub) event.getObject());
                    break;
            }
            numeroRegistro = String.valueOf(event.getObject().hashCode());
            accion = "Update";
            agregaBitacora();
            clase = "";
            Messages.addGlobalInfo("¡Operación exitosa!!");
            informacionCV();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void informacionCV() {
        try {
            listaFormacionAcademica.clear();
            listaExperienciasLaborales.clear();
            listaCapacitacionespersonal.clear();
            listaIdiomas.clear();
            listaHabilidadesInformaticas.clear();
            listaLenguas.clear();
            listaDesarrolloSoftwar.clear();
            listaDesarrollosTecnologicos.clear();
            listaInnovaciones.clear();
            listaDistinciones.clear();
            listaLibrosPubs.clear();
            listaArticulosp.clear();
            listaMemoriaspub.clear();
            listaInvestigacion.clear();
            listaCongresos.clear();

            total = 0;
            tv1 = 0;
            tv2 = 0;
            tv3 = 0;
            tv4 = 0;
            tv5 = 0;
            tv6 = 0;
            tv7 = 0;
            tv8 = 0;
            tv9 = 0;

            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(contactoDestino);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(contactoDestino);
            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(contactoDestino);
            listaLenguas = ejbHabilidades.mostrarLenguas(contactoDestino);
            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(contactoDestino);
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(contactoDestino);
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(contactoDestino);
            listaDistinciones = ejbPremios.mostrarDistinciones(contactoDestino);
            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(contactoDestino);
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(contactoDestino);
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(contactoDestino);
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(contactoDestino);
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(contactoDestino);

            for (int i = 0; i <= listaFormacionAcademica.size() - 1; i++) {
                oBJFormacionAcademica = listaFormacionAcademica.get(i);
                if (oBJFormacionAcademica.getEstatus().equals("Denegado")) {
                    tv1 = tv1 + 1;
                }
            }

            for (int i = 0; i <= listaExperienciasLaborales.size() - 1; i++) {
                oBJLaborales = listaExperienciasLaborales.get(i);
                if (oBJLaborales.getEstatus().equals("Denegado")) {
                    tv2 = tv2 + 1;
                }
            }

            for (int i = 0; i <= listaCapacitacionespersonal.size() - 1; i++) {
                oBJCapacitacionespersonal = listaCapacitacionespersonal.get(i);
                if (oBJCapacitacionespersonal.getEstatus().equals("Denegado")) {
                    tv3 = tv3 + 1;
                }
            }

            for (int i = 0; i <= listaIdiomas.size() - 1; i++) {
                oBJIdiomas = listaIdiomas.get(i);
                if (oBJIdiomas.getEstatus().equals("Denegado")) {
                    tv4 = tv4 + 1;
                }
            }

            for (int i = 0; i <= listaDesarrollosTecnologicos.size() - 1; i++) {
                oBJDesarrollosTecnologicoses = listaDesarrollosTecnologicos.get(i);
                if (oBJDesarrollosTecnologicoses.getEstatus().equals("Denegado")) {
                    tv5 = tv5 + 1;
                }
            }

            for (int i = 0; i <= listaDistinciones.size() - 1; i++) {
                oBJDistincioneses = listaDistinciones.get(i);
                if (oBJDistincioneses.getEstatus().equals("Denegado")) {
                    tv6 = tv6 + 1;
                }
            }

            for (int i = 0; i <= listaLibrosPubs.size() - 1; i++) {
                oBJLibrosPubs = listaLibrosPubs.get(i);
                if (oBJLibrosPubs.getEstatus().equals("Denegado")) {
                    tv7 = tv7 + 1;
                }
            }

            for (int i = 0; i <= listaArticulosp.size() - 1; i++) {
                oBJoBJArticulosps = listaArticulosp.get(i);
                if (oBJoBJArticulosps.getEstatus().equals("Denegado")) {
                    tv8 = tv8 + 1;
                }
            }

            for (int i = 0; i <= listaMemoriaspub.size() - 1; i++) {
                oBJMemoriaspubs = listaMemoriaspub.get(i);
                if (oBJMemoriaspubs.getEstatus().equals("Denegado")) {
                    tv9 = tv9 + 1;
                }
            }
            total = tv1 + tv2 + tv3 + tv4 + tv5 + tv6 + tv7 + tv8 + tv9;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarEmpleado() {
        try {
            nuevOBJPersonalSubordinado.setActividad(new Actividades());
            nuevOBJPersonalSubordinado.setCategoriaOficial(new PersonalCategorias());
            nuevOBJPersonalSubordinado.setCategoriaOperativa(new PersonalCategorias());
            nuevOBJPersonalSubordinado.setCategoria360(new PersonalCategorias());
            nuevOBJPersonalSubordinado.setGrado(new Grados());

            nuevOBJPersonalSubordinado.getActividad().setActividad(actividad);
            nuevOBJPersonalSubordinado.getCategoriaOperativa().setCategoria(categoriaOP);
            nuevOBJPersonalSubordinado.getCategoriaOficial().setCategoria(categoriaOF);
            nuevOBJPersonalSubordinado.getCategoria360().setCategoria(categoria360);
            nuevOBJPersonalSubordinado.getGrado().setGrado(grado);

            ejbDatosUsuarioLogeado.actualizarPersonal(nuevOBJPersonalSubordinado);
            nombreTabla = "Personal";
            numeroRegistro = nuevOBJPersonalSubordinado.getClave().toString();
            accion = "Update";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarPerfilSubordinado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String convertirRuta(String ruta) {
        if (selectecOBJFormacionAcademica != null) {
            File file = new File(selectecOBJFormacionAcademica.getEvidenciaTitulo());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRuta2(String ruta) {
        if (selectecOBJFormacionAcademica != null) {
            File file = new File(selectecOBJFormacionAcademica.getEvidenciaCedula());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaEL(String ruta) {
        if (selectecOBJLaborales != null) {
            File file = new File(selectecOBJLaborales.getEvidenciaNombremiento());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaAP(String ruta) {
        if (selectecOBJCapacitacionespersonal != null) {
            File file = new File(selectecOBJCapacitacionespersonal.getEvidenciaCapacitacion());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaId(String ruta) {
        if (selectecOBJIdiomas != null) {
            File file = new File(selectecOBJIdiomas.getEvidenciaDoc());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaDT(String ruta) {
        if (selectecOBJDesarrollosTecnologicoses != null) {
            File file = new File(selectecOBJDesarrollosTecnologicoses.getDocumentoRespaldo());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaPr(String ruta) {
        if (selectecOBJDistincioneses != null) {
            File file = new File(selectecOBJDistincioneses.getEvidenciaDistincion());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaLP(String ruta) {
        if (selectecOBJLibrosPubs != null) {
            File file = new File(selectecOBJLibrosPubs.getEvidencia());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaAr(String ruta) {
        if (selectecOBJoBJArticulosps != null) {
            File file = new File(selectecOBJoBJArticulosps.getEvidencia());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaMP(String ruta) {
        if (selectecOBJMemoriaspubs != null) {
            File file = new File(selectecOBJMemoriaspubs.getEvidencia());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void convertirRutaDatosPersonales() {
        if (nuevoOBJInformacionAdicionalPersonal != null) {
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null) {
                File file = new File(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                ruta1 = "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
            } else {
                ruta1 = "";
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp() != null) {
                File file = new File(nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp());
                ruta2 = "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
            } else {
                ruta2 = "";
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio() != null) {
                File file = new File(nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio());
                ruta3 = "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
            } else {
                ruta3 = "";
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null) {
                File file = new File(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                ruta4 = "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
            } else {
                ruta4 = "";
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc() != null) {
                File file = new File(nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc());
                ruta5 = "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
            } else {
                ruta5 = "";
            }
        } else {
            ruta1 = "";
            ruta2 = "";
            ruta3 = "";
            ruta4 = "";
            ruta5 = "";
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
        }
    }
    
    public void agregaBitacora() {
        try {
            Date fechaActual = new Date();
            nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(usuario);
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void listaArchivos() {
        try {
            rutasEvidencias.clear();
            File evidencias = new File("C:" + File.separator + "archivos" + File.separator + "evidenciasCapitalHumano" + File.separator + contactoDestino);
            if (evidencias.exists()) {
                File[] evidenciaList = evidencias.listFiles();
                for (int i = 0; i <= evidenciaList.length - 1; i++) {
                    rutasEvidencias.add(evidenciaList[i].getPath());
                }
            }

//            Files.walk(Paths.get("C:\\archivos\\evidenciasCapitalHumano\\" + contactoDestino)).forEach(ruta -> {
//                if (Files.isRegularFile(ruta)) {
//                    rutasEvidencias.add(ruta.toString());
//                }
//            });
            ZipWritter.generar(rutasEvidencias, contactoDestino);
            Ajax.oncomplete("PF('dlgEvidencias').show();");
            Ajax.oncomplete("PF('dlgEvidencias').show();");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
