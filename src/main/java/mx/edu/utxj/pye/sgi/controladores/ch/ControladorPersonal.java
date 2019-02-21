package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
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
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
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
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPersonal implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;
////////////////////////////////////////////////////////////////////////////////CV
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Datos Personales
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado;
////////////////////////////////////////////////////////////////////////////////Funciones
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Subordinados
    @Getter    @Setter    private List<Personal> listaPersonal = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Catalogos
    @Getter    @Setter    private List<String> estatus = new ArrayList<>();
    @Getter    @Setter    private List<Grados> listaGrados = new ArrayList<>();
    @Getter    @Setter    private List<Actividades> listaActividades = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> listaAreasUniversidads = new ArrayList<>(), listareasSuperiores = new ArrayList<>();
    @Getter    @Setter    private List<PersonalCategorias> listaPersonalCategorias = new ArrayList<>(), listaPersonalCategorias360 = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Variables de apoyo
    @Getter    @Setter    private Short actividad = 0, categoriaOP = 0, categoriaOF = 0, categoria360 = 0, grado = 0;
    @Getter    @Setter    private Integer contactoDestino, total = 0, tv1 = 0, tv2 = 0, tv3 = 0, tv4 = 0, tv5 = 0, tv6 = 0, tv7 = 0, tv8 = 0, tv9 = 0;
    @Getter    @Setter    private String clase = "", nombreTabla;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        nuevOBJPersonalSubordinado = new Personal();
        mostrarSubordinados();
        generarListasAreas();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mostrarSubordinados() {
        try {
            listaPersonal.clear();
            listaPersonal = ejbPersonal.mostrarListaPersonalsPorEstatus(1);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarListasAreas() {
        try {
            listaGrados.clear();
            listaActividades.clear();
            listareasSuperiores.clear();
            listaAreasUniversidads.clear();
            listaPersonalCategorias.clear();
            listaPersonalCategorias360.clear();

            listaAreasUniversidads = ejbAreasLogeo.mostrarAreasUniversidad();
            listaAreasUniversidads.forEach((t) -> {
                if (t.getCategoria().getCategoria() <= Short.parseShort("8")) {
                    listareasSuperiores.add(t);
                }
            });

            Collections.sort(listareasSuperiores, (x, y) -> x.getNombre().compareTo(y.getNombre()));
            listaGrados = ejbEducacion.mostrarListaGrados();
            listaActividades = ejbPersonal.mostrarListaActividades();
            listaPersonalCategorias = ejbUtilidadesCH.mostrarListaPersonalCategorias();

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

            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevOBJPersonalSubordinado = ejbPersonal.mostrarPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(contactoDestino);

            informacionCV();
            mostrarFuncioneSubordinado();

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

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }

    public void mostrarFuncioneSubordinado() {
        try {
            listaFuncioneSubordinado = new ArrayList<>();
            listaFuncioneSubordinado.clear();
            if (nuevoOBJListaPersonal.getCategoriaOperativa() == 30
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 32
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 41
                    || (nuevoOBJListaPersonal.getCategoriaOperativa() == 18 && (nuevoOBJListaPersonal.getAreaSuperior() >= 23 && nuevoOBJListaPersonal.getAreaSuperior() <= 29))
                    || (nuevoOBJListaPersonal.getCategoriaOperativa() == 34 && (nuevoOBJListaPersonal.getAreaOperativa() >= 23 && nuevoOBJListaPersonal.getAreaOperativa() <= 29))) {
                listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
            } else {
                listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevOBJPersonalSubordinado.getCategoriaEspecifica().getCategoriaEspecifica());
            }
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
            utilidadesCH.agregaBitacora(controladorEmpleado.getEmpleadoLogeado(), String.valueOf(event.getObject().hashCode()), nombreTabla, "Update");
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
            listaIdiomas.clear();
            listaLenguas.clear();
            listaCongresos.clear();
            listaLibrosPubs.clear();
            listaArticulosp.clear();
            listaMemoriaspub.clear();
            listaInnovaciones.clear();
            listaDistinciones.clear();
            listaInvestigacion.clear();
            listaDesarrolloSoftwar.clear();
            listaFormacionAcademica.clear();
            listaExperienciasLaborales.clear();
            listaCapacitacionespersonal.clear();
            listaHabilidadesInformaticas.clear();
            listaDesarrollosTecnologicos.clear();

            tv1 = 0;            tv2 = 0;            tv3 = 0;            tv4 = 0;            tv5 = 0;
            tv6 = 0;            tv7 = 0;            tv8 = 0;            tv9 = 0;            total = 0;

            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaLenguas = ejbHabilidades.mostrarLenguas(contactoDestino);
            listaDistinciones = ejbPremios.mostrarDistinciones(contactoDestino);
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(contactoDestino);
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(contactoDestino);
            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(contactoDestino);
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(contactoDestino);
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(contactoDestino);
            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(contactoDestino);
            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(contactoDestino);
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(contactoDestino);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(contactoDestino);
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(contactoDestino);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(contactoDestino);

            listaIdiomas.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv4 = tv4 + 1;
                }
            });
            listaLibrosPubs.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv7 = tv7 + 1;
                }
            });
            listaArticulosp.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv8 = tv8 + 1;
                }
            });
            listaMemoriaspub.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv9 = tv9 + 1;
                }
            });
            listaDistinciones.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv6 = tv6 + 1;
                }
            });
            listaFormacionAcademica.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv1 = tv1 + 1;
                }
            });
            listaExperienciasLaborales.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv2 = tv2 + 1;
                }
            });
            listaCapacitacionespersonal.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv3 = tv3 + 1;
                }
            });
            listaDesarrollosTecnologicos.forEach((t) -> {
                if (t.getEstatus().equals("Denegado")) {
                    tv5 = tv5 + 1;
                }
            });

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

            ejbPersonal.actualizarPersonal(nuevOBJPersonalSubordinado);
            utilidadesCH.agregaBitacora(controladorEmpleado.getEmpleadoLogeado(), nuevOBJPersonalSubordinado.getClave().toString(), "Personal", "Update");
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarPerfilSubordinado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listaArchivos() {
        try {
            List<String> rutasEvidencias = new ArrayList<>();
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
