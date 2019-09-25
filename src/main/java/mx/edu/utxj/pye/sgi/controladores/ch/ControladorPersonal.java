package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import mx.edu.utxj.pye.sgi.dto.ListaEvaluaciones;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
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
    @Getter    @Setter    private List<String> rutasEvidenciasBD = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Datos Personales
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado;
////////////////////////////////////////////////////////////////////////////////Funciones
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Evaluaciones
    @Getter    @Setter    private List<ResultadoEva> listaResultadoEva = new ArrayList<>();
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
    @Getter    @Setter    DecimalFormat df = new DecimalFormat("#.00");

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    @EJB    EjbPersonalEvaluaciones ejbPersonalEvaluaciones;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;    
    @Inject    ControladorResultadosEvaluaciones cre;
    
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

            listaAreasUniversidads = ejbAreasLogeo.mostrarAreasUniversidadActivas();
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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(Inicio)");
            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevOBJPersonalSubordinado = ejbPersonal.mostrarPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(contactoDestino);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(1)");
            informacionCV();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(2)");
            mostrarFuncioneSubordinado();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(3)");
            mostrarLista();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(4)");
            actividad = nuevOBJPersonalSubordinado.getActividad().getActividad();
            categoriaOP = nuevOBJPersonalSubordinado.getCategoriaOperativa().getCategoria();
            categoriaOF = nuevOBJPersonalSubordinado.getCategoriaOficial().getCategoria();
            categoria360 = nuevOBJPersonalSubordinado.getCategoria360().getCategoria();
            grado = nuevOBJPersonalSubordinado.getGrado().getGrado();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.mostrarPerfilSubordinado(Fin)");
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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(1)");
            listaIdiomas.clear();
            listaLenguas.clear();
            listaCongresos.clear();
            listaLibrosPubs.clear();
            listaArticulosp.clear();
            listaMemoriaspub.clear();
            listaInnovaciones.clear();
            listaDistinciones.clear();
            rutasEvidenciasBD.clear();
            listaInvestigacion.clear();
            listaDesarrolloSoftwar.clear();
            listaFormacionAcademica.clear();
            listaExperienciasLaborales.clear();
            listaCapacitacionespersonal.clear();
            listaHabilidadesInformaticas.clear();
            listaDesarrollosTecnologicos.clear();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(2)");
            tv1 = 0;            tv2 = 0;            tv3 = 0;            tv4 = 0;            tv5 = 0;
            tv6 = 0;            tv7 = 0;            tv8 = 0;            tv9 = 0;            total = 0;
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(3)");
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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(4)");
            if (!listaIdiomas.isEmpty()) {
                listaIdiomas.forEach((t) -> {
                    if (t.getEvidenciaDoc() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaDoc());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv4 = tv4 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(A)");
            if (!listaLibrosPubs.isEmpty()) {
                listaLibrosPubs.forEach((t) -> {
                    if (t.getEvidencia() != null){
                        rutasEvidenciasBD.add(t.getEvidencia());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv7 = tv7 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(B)");
            if (!listaArticulosp.isEmpty()) {
                listaArticulosp.forEach((t) -> {
                    if (t.getEvidencia() != null){
                        rutasEvidenciasBD.add(t.getEvidencia());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv8 = tv8 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(C)");
            if (!listaMemoriaspub.isEmpty()) {
                listaMemoriaspub.forEach((t) -> {
                    if (t.getEvidencia() != null){
                        rutasEvidenciasBD.add(t.getEvidencia());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv9 = tv9 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(D)");
            if (!listaDistinciones.isEmpty()) {
                listaDistinciones.forEach((t) -> {
                    if (t.getEvidenciaDistincion() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaDistincion());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv6 = tv6 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(E)");
            if (!listaFormacionAcademica.isEmpty()) {
                listaFormacionAcademica.forEach((t) -> {
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(t.getEvidenciaCedula())"+t.getEvidenciaCedula());
                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(t.getEvidenciaTitulo())"+t.getEvidenciaTitulo());
                    if (t.getEvidenciaCedula() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaCedula());
                    }
                    if (t.getEvidenciaTitulo() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaTitulo());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv1 = tv1 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(F)");
            if (!listaExperienciasLaborales.isEmpty()) {
                listaExperienciasLaborales.forEach((t) -> {
                    if (t.getEvidenciaNombremiento() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv2 = tv2 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(G)");
            if (!listaCapacitacionespersonal.isEmpty()) {
                listaCapacitacionespersonal.forEach((t) -> {
                    if (t.getEvidenciaCapacitacion() != null){
                        rutasEvidenciasBD.add(t.getEvidenciaCapacitacion());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv3 = tv3 + 1;
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(H)");
            if (!listaDesarrollosTecnologicos.isEmpty()) {
                listaDesarrollosTecnologicos.forEach((t) -> {
                    if (t.getDocumentoRespaldo() != null){
                        rutasEvidenciasBD.add(t.getDocumentoRespaldo());
                    }
                    if (t.getEstatus().equals("Denegado")) {
                        tv5 = tv5 + 1;
                    }
                });
            }

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonal.informacionCV(I)");
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

    public void mostrarLista() {
        try {
            List<ListaEvaluaciones> listaEvaluaciones = new ArrayList<>();
            ResultadoEva nuevoResEva;
            String tipo="";
            ListaEvaluaciones nOBRE;
            listaResultadoEva.clear();
            listaEvaluaciones.clear();
            
            Personal personal = ejbPersonalEvaluaciones.getPersonal(contactoDestino);
            List<PeriodosEscolares> periodos = ejbPersonalEvaluaciones.getPeriodos(personal);
            Map<PeriodosEscolares, List<Evaluaciones360Resultados>> resultados360 = ejbPersonalEvaluaciones.getEvaluaciones360PorPeriodo(personal, periodos);
            Map<PeriodosEscolares, DesempenioEvaluacionResultados> resultadosDesempenio = ejbPersonalEvaluaciones.getEvaluacionesDesempenioPorPeriodo(personal, periodos);
            listaEvaluaciones = ejbPersonalEvaluaciones.empaquetar(personal, periodos, resultados360, resultadosDesempenio);

            for (int i = 0; i <= listaEvaluaciones.size() - 1; i++) {
                nOBRE = listaEvaluaciones.get(i);

                if (!nOBRE.getPromedio360().isNaN()) {
                    if (nOBRE.getPromedio360() != null) {
                        if (nOBRE.getPromedio360() == 0.0) {
                            tipo = "proceso";
                        }
                        if (nOBRE.getPromedio360() > 0.0 && nOBRE.getPromedio360() <= 0.9) {
                            tipo = "danger";
                        }
                        if (nOBRE.getPromedio360() >= 1.0 && nOBRE.getPromedio360() <= 1.9) {
                            tipo = "warning";
                        }
                        if (nOBRE.getPromedio360() >= 2.0 && nOBRE.getPromedio360() <= 2.9) {
                            tipo = "info";
                        }
                        if (nOBRE.getPromedio360() >= 3.0 && nOBRE.getPromedio360() <= 4.0) {
                            tipo = "success";
                        }
                        nuevoResEva = new ResultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), Double.parseDouble(df.format(nOBRE.getPromedio360())), "Evaluación 360°", tipo);
                        listaResultadoEva.add(nuevoResEva);
                    }
                } else {
                    nuevoResEva = new ResultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), 0.00, "Evaluación 360°", "proceso");
                    listaResultadoEva.add(nuevoResEva);
                }

                if (!nOBRE.getPromedioDesepenio().isNaN()) {
                    if (nOBRE.getPromedioDesepenio() != null) {
                        if (nOBRE.getPromedioDesepenio() == 0.0) {
                            tipo = "proceso";
                        }
                        if (nOBRE.getPromedioDesepenio() > 0.0 && nOBRE.getPromedioDesepenio() <= 1.24) {
                            tipo = "danger";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 1.25 && nOBRE.getPromedioDesepenio() <= 2.49) {
                            tipo = "warning";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 2.50 && nOBRE.getPromedioDesepenio() <= 3.74) {
                            tipo = "info";
                        }
                        if (nOBRE.getPromedioDesepenio() >= 3.75 && nOBRE.getPromedioDesepenio() <= 5.0) {
                            tipo = "success";
                        }
                        nuevoResEva = new ResultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), Double.parseDouble(df.format(nOBRE.getPromedioDesepenio())), "Evaluación Desempeño", tipo);
                        listaResultadoEva.add(nuevoResEva);
                    }
                } else {
                    nuevoResEva = new ResultadoEva(nOBRE.getPeriodoEscolar().getAnio(), nOBRE.getPeriodoEscolar().getPeriodo(), nOBRE.getPeriodoEscolar().getMesFin().getMes(), nOBRE.getPeriodoEscolar().getMesInicio().getMes(), 0.00, "Evaluación Desempeño", "proceso");
                    listaResultadoEva.add(nuevoResEva);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorResultadosEvaluaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void descargarEvidenciasCH() {
        try {
            String nombreArchivo = "";
            if (nuevoOBJInformacionAdicionalPersonal != null) {
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null){
                    rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                }
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp() != null){
                    rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp());
                }
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio() != null){
                    rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio());
                }
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null){
                    rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                }
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc() != null){
                    rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc());
                }
            }                        
            nombreArchivo = nuevoOBJListaPersonal.getClave() + " CV " + nuevoOBJListaPersonal.getNombre();

            if (!rutasEvidenciasBD.isEmpty()) {
                File zip = ZipWritter.generarZipPoa(rutasEvidenciasBD, nombreArchivo, Integer.parseInt(String.valueOf(nuevoOBJInformacionAdicionalPersonal.getClave())));
                Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/evidenciasCapitalHumano/zips/" + nombreArchivo + ".zip" + "');");
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public static class ResultadoEva {

        @Getter        @Setter        private int anio;
        @Getter        @Setter        private int periodo;
        @Getter        @Setter        private String fin;
        @Getter        @Setter        private String inicio;
        @Getter        @Setter        private Double promedio;
        @Getter        @Setter        private String tipoEvaluacion;
        @Getter        @Setter        private String tipoClas;

        private ResultadoEva(int _anio, int _periodo, String _fin, String _inicio, Double _promedio, String _tipoEvaluacion, String _tipoClas) {
            anio = _anio;
            periodo = _periodo;

            fin = _fin;
            inicio = _inicio;
            promedio = _promedio;
            tipoEvaluacion = _tipoEvaluacion;
            tipoClas = _tipoClas;
        }

    }
}
