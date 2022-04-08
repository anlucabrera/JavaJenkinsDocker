package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.Document; 
import com.itextpdf.text.pdf.PdfContentByte; 
import com.itextpdf.text.pdf.PdfImportedPage; 
import com.itextpdf.text.pdf.PdfReader; 
import com.itextpdf.text.pdf.PdfWriter; 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ch.CurriculumRIPPPA;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.PdfUtilidades;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorComisionDictaminadora implements Serializable {

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
    @Getter    @Setter    private List<Capacitacionespersonal> capAceptadas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<String> rutasEvidenciasBD = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Datos Personales
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Personal nuevOBJPersonalSubordinado;
    
    @Getter    @Setter    private List<CurriculumRIPPPA.CurriculumRIPPPAReportes> cripppar; 
    @Getter    @Setter    private List<CurriculumRIPPPA.CurriculumRIPPPAReportes> crippparFilter; 
    @Getter    @Setter    private CurriculumRIPPPA.Personales personales;
    @Getter    @Setter    private CurriculumRIPPPA.Escolaridad escolaridad;
    @Getter    @Setter    private CurriculumRIPPPA.Experiencia experiencia;  
    @Getter    @Setter    private CurriculumRIPPPA.Capacitacion capacitacion; 
    
    @Getter    @Setter    private CurriculumRIPPPA.ResumenCV resumenCV;     
    @Getter    @Setter    private CurriculumRIPPPA.DatosPersonal datosPersonal; 
////////////////////////////////////////////////////////////////////////////////Subordinados
    @Getter    @Setter    private List<Personal> listaPersonal = new ArrayList<>();
////////////////////////////////////////////////////////////////////////////////Variables de apoyo
    @Getter    @Setter    private Integer contactoDestino=0;
    @Getter    @Setter    private String nombreArchivo="";
    @Getter    @Setter    private Boolean concentrado=Boolean.TRUE;
    @Getter    @Setter    DecimalFormat df = new DecimalFormat("#.00");
////////////////////////////////////////////////////////////////////////////////EJB's
    @EJB    private EjbPersonal ejbPersonal;
    @EJB    private EjbEducacion ejbEducacion;
    @EJB    private EjbHabilidades ejbHabilidades;
    @EJB    private EjbPremios ejbPremios;
    @EJB    private EjbTecnologia ejbTecnologia;
    @EJB    private EjbProduccionProfecional ejbProduccionProfecional;
     
    @Inject LogonMB logonMB;
    @Inject PdfUtilidades pu;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        nuevOBJPersonalSubordinado = new Personal();
        inicializador();
        mostrarSubordinados();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mostrarSubordinados() {
        try {
            concentrado= Boolean.TRUE;
            listaPersonal.clear();
            List<Personal> personal = ejbPersonal.mostrarListaPersonalsPorEstatus(1);
            personal.forEach((t) -> {
                switch (t.getCategoriaOficial().getCategoria()) {
                    case 30:
                    case 32:
                    case 41:
                        listaPersonal.add(t);
                        break;
                }
            });
            Collections.sort(listaPersonal, (x, y) -> x.getCategoriaOficial().getCategoria().compareTo(y.getCategoriaOficial().getCategoria()));
            cripppar = new ArrayList<>();
            cripppar.clear();

            listaPersonal.forEach((t) -> {
                contactoDestino = t.getClave();
                mostrarPerfilSubordinado();
                Integer tCv = resumenCV.getListaIdiomas().size() + resumenCV.getListaLenguas().size() + resumenCV.getListaCongresos().size() + resumenCV.getListaLibrosPubs().size() + resumenCV.getListaArticulosp().size() + resumenCV.getListaDocenciases().size() + resumenCV.getListaMemoriaspub().size() + resumenCV.getListaInnovaciones().size() + resumenCV.getListaDistinciones().size() + resumenCV.getListaInvestigacion().size() + resumenCV.getListaDesarrolloSoftwar().size() + resumenCV.getListaFormacionAcademica().size() + resumenCV.getListaExperienciasLaborales().size() + resumenCV.getListaCapacitacionespersonal().size() + resumenCV.getListaHabilidadesInformaticas().size() + resumenCV.getListaDesarrollosTecnologicos().size();
               
                CurriculumRIPPPA.CurriculumRIPPPAReportes a = new CurriculumRIPPPA.CurriculumRIPPPAReportes(t, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "semaforoRojo");
                if (tCv != 0) {
                    a.setCv(Boolean.TRUE);
                }
                if (personales.getCuentaA() && personales.getCuentaC()) {
                    a.setPersonales(Boolean.TRUE);
                }

                switch (t.getCategoriaOficial().getCategoria()) {
                    case 32:
                        if (escolaridad.getCuentaCp() && escolaridad.getCuentaTp()) {
                            a.setEscolaridad(Boolean.TRUE);
                            a.setSemaforo("semaforoAmarillo");
                        }
                        if (experiencia.getCuenta41() && experiencia.getCuenta42() && experiencia.getCuenta43() && experiencia.getCuenta44() && experiencia.getCuenta45() && experiencia.getCuenta46() && experiencia.getCuenta47()) {
                            a.setExperiencia(Boolean.TRUE);
                        }
                        a.setCapacitacion(capacitacion.getCuentaEv());
                        if (a.getCapacitacion() && a.getEscolaridad() && a.getExperiencia() && a.getPersonales() && a.getCv()) {
                            a.setSemaforo("semaforoVerde");
                        }
                        break;
                    case 30:
                        if ((escolaridad.getCuentaCp() && escolaridad.getCuentaTp()) || (escolaridad.getCuentaCi() && escolaridad.getCuentaTi())) {
                            a.setEscolaridad(Boolean.TRUE);
                            a.setSemaforo("semaforoAmarillo");
                        }
                        if (experiencia.getCuenta41() && experiencia.getCuenta42() && experiencia.getCuenta47() && experiencia.getCuenta48()) {
                            a.setExperiencia(Boolean.TRUE);
                        }
                        a.setCapacitacion(capacitacion.getCuentaEv());
                        if (a.getCapacitacion() && a.getEscolaridad() && a.getExperiencia() && a.getPersonales() && a.getCv()) {
                            a.setSemaforo("semaforoVerde");
                        }
                        break;
                    case 41:
                        if ((escolaridad.getCuentaCt() && escolaridad.getCuentaTt()) || (escolaridad.getCuentaCp() && escolaridad.getCuentaTp()) || (escolaridad.getCuentaCi() && escolaridad.getCuentaTi())) {
                            a.setEscolaridad(Boolean.TRUE);
                            a.setSemaforo("semaforoAmarillo");
                        }
                        if (experiencia.getCuenta41() &&  experiencia.getCuenta47() && experiencia.getCuenta49()) {
                            a.setExperiencia(Boolean.TRUE);
                        }
                        if (a.getEscolaridad() && a.getExperiencia() && a.getPersonales() && a.getCv()) {
                            a.setSemaforo("semaforoVerde");
                        }
                        break;
                }

                if (experiencia.getCuenta41() && experiencia.getCuenta42() && experiencia.getCuenta43() && experiencia.getCuenta44() && experiencia.getCuenta45() && experiencia.getCuenta46() && experiencia.getCuenta47() && experiencia.getCuenta48() && experiencia.getCuenta49()) {
                    a.setExperiencia(Boolean.TRUE);
                }
                if (a.getCapacitacion() && a.getEscolaridad() && a.getExperiencia() && a.getPersonales() && a.getCv()) {
                    a.setSemaforo("semaforoVerde");
                }
                cripppar.add(a);
            });
            contactoDestino = 0;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void inicializador() {
        try {
            personales = new CurriculumRIPPPA.Personales("", Boolean.FALSE, "", Boolean.FALSE);
            escolaridad = new CurriculumRIPPPA.Escolaridad("", Boolean.FALSE, "", Boolean.FALSE, "", Boolean.FALSE, "", Boolean.FALSE, "", Boolean.FALSE, "", Boolean.FALSE, "", Boolean.FALSE);
            experiencia = new CurriculumRIPPPA.Experiencia(new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE, new ArrayList<>(), Boolean.FALSE);
            capacitacion = new CurriculumRIPPPA.Capacitacion(new ArrayList<>(), Boolean.FALSE);            
            crippparFilter= new ArrayList<>();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarDatosRIPPPA(Personal p) {
        contactoDestino = p.getClave();
        mostrarPerfilSubordinado();
        concentrado = Boolean.FALSE;
    }

    public void regresarConcnetrado() {
        concentrado = Boolean.TRUE;
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
            datosPersonal=new CurriculumRIPPPA.DatosPersonal(nuevOBJPersonalSubordinado, nuevoOBJListaPersonal, nuevoOBJInformacionAdicionalPersonal);
            inicializador();
            informacionCV();
//            unirPDFs();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void informacionCV() {
        try {            
            capAceptadas.clear();
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
            
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\1.0.pdf");
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\resumenCv"+nuevoOBJListaPersonal.getClave()+"_"+nuevoOBJListaPersonal.getNombre()+".pdf");
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\2.0.pdf");
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null) {
                prepararPdfParaUion(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                personales.setCuentaA(Boolean.TRUE);
                personales.setRutaA(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null) {
                prepararPdfParaUion(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                personales.setCuentaC(Boolean.TRUE);
                personales.setRutaC(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
            }
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\3.0.pdf");
            if (!listaFormacionAcademica.isEmpty()) {
                listaFormacionAcademica.forEach((t) -> {
                    switch (t.getNivelEscolaridad().getGrado()) {
                        case 5:                        case 6:                            
                            if (t.getEvidenciaTitulo() != null) {
                                prepararPdfParaUion(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTm(Boolean.TRUE);
                                escolaridad.setRutaTm(t.getEvidenciaTitulo());
                            }
                            break;
                        case 7:                        case 8:                            
                            if (t.getEvidenciaCedula() != null) {
                                prepararPdfParaUion(t.getEvidenciaCedula());
                                escolaridad.setCuentaCt(Boolean.TRUE);
                                escolaridad.setRutaCt(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
                                prepararPdfParaUion(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTt(Boolean.TRUE);
                                escolaridad.setRutaTt(t.getEvidenciaTitulo());
                            }
                            break;
                        case 9:                        case 10:                            
                            if (t.getEvidenciaCedula() != null) {
                                prepararPdfParaUion(t.getEvidenciaCedula());
                                escolaridad.setCuentaCi(Boolean.TRUE);
                                escolaridad.setRutaCi(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
                                prepararPdfParaUion(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTi(Boolean.TRUE);
                                escolaridad.setRutaTi(t.getEvidenciaTitulo());
                            }
                            break;
                        case 13:                        case 14:                        case 15:                        case 16:
                            if (t.getEvidenciaCedula() != null) {
                                prepararPdfParaUion(t.getEvidenciaCedula());
                                escolaridad.setCuentaCp(Boolean.TRUE);
                                escolaridad.setRutaCp(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
                                prepararPdfParaUion(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTp(Boolean.TRUE);
                                escolaridad.setRutaTp(t.getEvidenciaTitulo());
                            }
                            break;
                    }                    
                });
            }
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\4.0.pdf");
            if (!listaExperienciasLaborales.isEmpty()) {
                listaExperienciasLaborales.forEach((t) -> {
                    if (!t.getClaveRipppa().equals("No Aplica")) {
//                        prepararPdfParaUion(t.getEvidenciaNombremiento());
                        switch (t.getClaveRipppa()) {
                            case "41":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta41(Boolean.TRUE);
                                    experiencia.getRuta41().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"41", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "42":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta42(Boolean.TRUE);
                                    experiencia.getRuta42().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"42", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "43":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta43(Boolean.TRUE);
                                    experiencia.getRuta43().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"43", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "44":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta44(Boolean.TRUE);
                                    experiencia.getRuta44().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"44", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "45":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta45(Boolean.TRUE);
                                    experiencia.getRuta45().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"45", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "46":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta46(Boolean.TRUE);
                                    experiencia.getRuta46().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"46", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "47":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta47(Boolean.TRUE);
                                    experiencia.getRuta47().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"47", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "48":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta48(Boolean.TRUE);
                                    experiencia.getRuta48().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"48", t.getEvidenciaNombremiento()));
                                }
                                break;
                            case "49":
                                if (t.getEvidenciaNombremiento() != null) {
                                    prepararPdfParaUion(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta49(Boolean.TRUE);
                                    experiencia.getRuta49().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getEmpleo().toString(),"E",t.getEmpleo().toString()+"-"+"E"+"-"+"49", t.getEvidenciaNombremiento()));
                                }
                                break;
                        }
                    }
                });
            }
            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\5.0.pdf");
            if (!listaCapacitacionespersonal.isEmpty()) {
                listaCapacitacionespersonal.forEach((t) -> {
                    if (t.getEvidenciaCapacitacion() != null) {
                        if (!t.getRipppa().equals("No Aplica")) {
                            if (t.getRipppa().equals("48")) {
                                experiencia.setCuenta48(Boolean.TRUE);
                                experiencia.getRuta48().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getCursoClave().toString(),"C",t.getCursoClave().toString()+"-"+"C"+"-"+"48", t.getEvidenciaCapacitacion()));
                            } else {
                                capacitacion.setCuentaEv(Boolean.TRUE);
                                capacitacion.getRutasEv().add(new CurriculumRIPPPA.RegistrosRIPPPA(t.getCursoClave().toString(),"C",t.getCursoClave().toString()+"-"+"C"+"-"+"51", t.getEvidenciaCapacitacion()));
                                prepararPdfParaUion(t.getEvidenciaCapacitacion());
                            }
                        }
                    }
                });
            }            
            
            resumenCV = new CurriculumRIPPPA.ResumenCV(listaIdiomas, listaLenguas, listaCongresos, listaLibrosPubs, listaArticulosp, listaDocenciases, listaMemoriaspub, listaInnovaciones, listaDistinciones, listaInvestigacion, listaDesarrolloSoftwar, listaFormacionAcademica, listaExperienciasLaborales, listaCapacitacionespersonal, listaHabilidadesInformaticas, listaDesarrollosTecnologicos, Boolean.FALSE);

            Integer tCv = resumenCV.getListaIdiomas().size() + resumenCV.getListaLenguas().size() + resumenCV.getListaCongresos().size() + resumenCV.getListaLibrosPubs().size() + resumenCV.getListaArticulosp().size() + resumenCV.getListaDocenciases().size() + resumenCV.getListaMemoriaspub().size() + resumenCV.getListaInnovaciones().size() + resumenCV.getListaDistinciones().size() + resumenCV.getListaInvestigacion().size() + resumenCV.getListaDesarrolloSoftwar().size() + resumenCV.getListaFormacionAcademica().size() + resumenCV.getListaExperienciasLaborales().size() + resumenCV.getListaCapacitacionespersonal().size() + resumenCV.getListaHabilidadesInformaticas().size() + resumenCV.getListaDesarrollosTecnologicos().size();
            if (tCv != 0) {
                resumenCV.setCompleto(Boolean.TRUE);
            } else {
                resumenCV.setCompleto(Boolean.FALSE);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void prepararPdfParaUion(String ruta) {
        File evidencia = new File(ruta);
        if (evidencia.exists()) {
            rutasEvidenciasBD.add(ruta);
        }
    }
    
    public void unirPDFs() {
        try {
            pu.generarPdf(resumenCV, datosPersonal);
            nombreArchivo="";
            nombreArchivo = "CVRIPPPA " + nuevoOBJListaPersonal.getClave() +" - "+ nuevoOBJListaPersonal.getNombre();
            List<InputStream> inputPdfList = new ArrayList<InputStream>();
            rutasEvidenciasBD.forEach((t) -> {
                FileInputStream fis;
                try {
                    fis = new FileInputStream(t);
                    inputPdfList.add(fis);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            OutputStream outputStream = new FileOutputStream("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\"+nombreArchivo+".pdf");
            Document document = new Document();
            List<PdfReader> readers = new ArrayList<PdfReader>();
            int totalPages = 0;
            //Create pdf Iterator object using inputPdfList. 
            Iterator<InputStream> pdfIterator = inputPdfList.iterator();
            // Create reader list for the input pdf files. 
            while (pdfIterator.hasNext()) {
                InputStream pdf = pdfIterator.next();
                PdfReader pdfReader = new PdfReader(pdf);
                readers.add(pdfReader);
                totalPages = totalPages + pdfReader.getNumberOfPages();
            }
            // Create writer for the outputStream 
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            //Open document. 
            document.open();
            //Contain the pdf data. 
            PdfContentByte pageContentByte = writer.getDirectContent();
            PdfImportedPage pdfImportedPage;
            int currentPdfReaderPage = 1;
            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
            // Iterate and process the reader list. 
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();
                //Create page and add content. 
                while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                    document.newPage();
                    pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                    pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                    currentPdfReaderPage++;
                }
                currentPdfReaderPage = 1;
            }
            //Close document and outputStream. 
            outputStream.flush();
            document.close();
            outputStream.close();
            System.out.println("Pdf files merged successfully.");
            
            if (!rutasEvidenciasBD.isEmpty()) {
                Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/evidenciasCapitalHumano/RIPPPA/" + nombreArchivo + ".pdf" + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizaRegistros(ValueChangeEvent event) {
        try {
            String[] datos = event.getNewValue().toString().split("-");
            List<String> textoSeparado = Arrays.asList(datos);
            if (textoSeparado.get(1).equals("E")) {
                ExperienciasLaborales el = new ExperienciasLaborales();
                el = ejbEducacion.mostrarExperienciasLabora(Integer.parseInt(textoSeparado.get(0)));
                el.setClaveRipppa(textoSeparado.get(2));
                ejbEducacion.actualizarExperienciasLaborales(el);
            } else {
                Capacitacionespersonal ca = new Capacitacionespersonal();
                ca = ejbEducacion.mostrarCapacitacionpersonal(Integer.parseInt(textoSeparado.get(0)));
                ca.setRipppa(textoSeparado.get(2));
                ejbEducacion.actualizarCapacitacionespersonal(ca);
            }
            informacionCV();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
