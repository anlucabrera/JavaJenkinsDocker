package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
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
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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
    @Getter    @Setter    private List<Docencias> listaDocenciases = new ArrayList<>();
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
    @Getter    @Setter    private Integer contactoDestino=0, total = 0, tv1 = 0, tv2 = 0, tv3 = 0, tv4 = 0, tv5 = 0, tv6 = 0, tv7 = 0, tv8 = 0, tv9 = 0;
    @Getter    @Setter    private String clase = "", nombreTabla,nombreArchivo="";
    @Getter    @Setter    DecimalFormat df = new DecimalFormat("#.00");
    List<String> filesListInDir = new ArrayList<>();
    List<String> carpetas = new ArrayList<>();
    List<String> archivosBD = new ArrayList<>();

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
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
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
            if (contactoDestino == 0) {
                Messages.addGlobalWarn("¡Sin datos!!");
                return;
            }
            nuevOBJPersonalSubordinado = ejbPersonal.mostrarPersonalLogeado(contactoDestino);
            if (nuevOBJPersonalSubordinado == null) {
                Messages.addGlobalWarn("¡Sin datos parala clave "+contactoDestino+"!!");
                return;
            }
            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(contactoDestino);
            informacionCV();
            mostrarFuncioneSubordinado();
            mostrarLista();
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
            listaDocenciases.clear();
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
            tv1 = 0;            tv2 = 0;            tv3 = 0;            tv4 = 0;            tv5 = 0;
            tv6 = 0;            tv7 = 0;            tv8 = 0;            tv9 = 0;            total = 0;            
            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaLenguas = ejbHabilidades.mostrarLenguas(contactoDestino);
            listaDistinciones = ejbPremios.mostrarDistinciones(contactoDestino);
            listaDocenciases = ejbPersonal.mostrarListaDocencias(contactoDestino);
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
            if (!listaFormacionAcademica.isEmpty()) {
                listaFormacionAcademica.forEach((t) -> {
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
            Boolean nulos = Boolean.FALSE;
            List<ListaEvaluaciones> listaEvaluaciones = new ArrayList<>();
            ResultadoEva nuevoResEva;
            String tipo = "";
            ListaEvaluaciones nOBRE;
            listaResultadoEva.clear();
            listaEvaluaciones.clear();

            List<PeriodosEscolares> periodos = ejbPersonalEvaluaciones.getPeriodos(nuevOBJPersonalSubordinado);
            Map<PeriodosEscolares, List<Evaluaciones360Resultados>> resultados360 = ejbPersonalEvaluaciones.getEvaluaciones360PorPeriodo(nuevOBJPersonalSubordinado, periodos);
            if (resultados360 == null) {
                nulos = Boolean.TRUE;
            }
            Map<PeriodosEscolares, DesempenioEvaluacionResultados> resultadosDesempenio = ejbPersonalEvaluaciones.getEvaluacionesDesempenioPorPeriodo(nuevOBJPersonalSubordinado, periodos);
            if (resultadosDesempenio == null) {
                nulos = Boolean.TRUE;
            }
            if (nulos == Boolean.FALSE) {
                listaEvaluaciones = ejbPersonalEvaluaciones.empaquetar(nuevOBJPersonalSubordinado, periodos, resultados360, resultadosDesempenio);

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
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorResultadosEvaluaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarEvidenciasCH() {
        try {
            nombreArchivo = "CV " + nuevoOBJListaPersonal.getClave() +" - "+ nuevoOBJListaPersonal.getNombre();            
            String rutaGeneral = "C:\\archivos\\evidenciasCapitalHumano\\" + nuevoOBJListaPersonal.getClave();
            String rutaZipGeneral = "C:\\archivos\\evidenciasCapitalHumano\\zips\\" + nombreArchivo+ ".zip";
            
            File dirP = new File(rutaGeneral);
            String zipDirName = rutaZipGeneral;
                        
            carpetas = new ArrayList<>();
            carpetas.clear();
            carpetas.add(rutaGeneral);
            for (int i = 0; i < carpetas.size(); i++) {
                File dir = new File(carpetas.get(i));
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        if (rutasEvidenciasBD.contains(file.getAbsoluteFile().toString())) {
                            filesListInDir.add(file.getAbsolutePath());
                        }
                        
                    } else if (file.isDirectory()) {
                        carpetas.add(file.getAbsolutePath());
                    }
                }
            }   
                        
            try (FileOutputStream fos = new FileOutputStream(zipDirName)) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                for (String filePath : filesListInDir) {
                    if(filePath.contains("evidenciasCapitalHumano")){
                        //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                        ZipEntry ze = new ZipEntry(filePath.substring(dirP.getAbsolutePath().length() + 1, filePath.length()));
                        zos.putNextEntry(ze);
                        //read the file and write to ZipOutputStream
                        FileInputStream fis = new FileInputStream(filePath);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                        fis.close();
                    }
                }
                zos.close();
            }

            if (!rutasEvidenciasBD.isEmpty()) {
                Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/evidenciasCapitalHumano/zips/" + nombreArchivo + ".zip" + "');");
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populateFilesList(File dir) throws IOException {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filesListInDir.add(file.getAbsolutePath());
            } else {
                populateFilesList(file);
            }
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
