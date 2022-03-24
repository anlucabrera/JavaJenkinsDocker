package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.Document; 
import com.itextpdf.text.pdf.PdfContentByte; 
import com.itextpdf.text.pdf.PdfImportedPage; 
import com.itextpdf.text.pdf.PdfReader; 
import com.itextpdf.text.pdf.PdfWriter; 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
    
    @Getter    @Setter    private CurriculumRIPPPA.CurriculumRIPPPAReportes cripppar; 
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
        mostrarSubordinados();
        inicializador();
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void mostrarSubordinados() {
        try {
            listaPersonal.clear();
            listaPersonal = ejbPersonal.mostrarListaPersonalsPorEstatus(1);
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
            cripppar = new CurriculumRIPPPA.CurriculumRIPPPAReportes(personales, escolaridad, experiencia, capacitacion);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
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
            datosPersonal=new CurriculumRIPPPA.DatosPersonal(nuevOBJPersonalSubordinado, nuevoOBJListaPersonal, nuevoOBJInformacionAdicionalPersonal);
            inicializador();
            informacionCV();
            unirPDFs();
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
//            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\2.0.pdf");
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null) {
//                rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                personales.setCuentaA(Boolean.TRUE);
                personales.setRutaA(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
            }
            if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null) {
//                rutasEvidenciasBD.add(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                personales.setCuentaC(Boolean.TRUE);
                personales.setRutaC(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
            }
//            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\3.0.pdf");
            if (!listaFormacionAcademica.isEmpty()) {
                listaFormacionAcademica.forEach((t) -> {
                    switch (t.getNivelEscolaridad().getGrado()) {
                        case 5:                        case 6:                            
                            if (t.getEvidenciaTitulo() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTm(Boolean.TRUE);
                                escolaridad.setRutaTm(t.getEvidenciaTitulo());
                            }
                            break;
                        case 7:                        case 8:                            
                            if (t.getEvidenciaCedula() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaCedula());
                                escolaridad.setCuentaCt(Boolean.TRUE);
                                escolaridad.setRutaCt(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTt(Boolean.TRUE);
                                escolaridad.setRutaTt(t.getEvidenciaTitulo());
                            }
                            break;
                        case 9:                        case 10:                            
                            if (t.getEvidenciaCedula() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaCedula());
                                escolaridad.setCuentaCi(Boolean.TRUE);
                                escolaridad.setRutaCi(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTi(Boolean.TRUE);
                                escolaridad.setRutaTi(t.getEvidenciaTitulo());
                            }
                            break;
                        case 13:                        case 14:                        case 15:                        case 16:
                            if (t.getEvidenciaCedula() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaCedula());
                                escolaridad.setCuentaCp(Boolean.TRUE);
                                escolaridad.setRutaCp(t.getEvidenciaCedula());
                            }
                            if (t.getEvidenciaTitulo() != null) {
//                                rutasEvidenciasBD.add(t.getEvidenciaTitulo());
                                escolaridad.setCuentaTp(Boolean.TRUE);
                                escolaridad.setRutaTp(t.getEvidenciaTitulo());
                            }
                            break;
                    }                    
                });
            }
//            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\4.0.pdf");
            if (!listaExperienciasLaborales.isEmpty()) {
                listaExperienciasLaborales.forEach((t) -> {
                    if (!t.getClaveRipppa().equals("No Aplica")) {
//                        rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                        switch (t.getClaveRipppa()) {
                            case "41":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta41(Boolean.TRUE);
                                    experiencia.getRuta41().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "42":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta42(Boolean.TRUE);
                                    experiencia.getRuta42().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "43":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta43(Boolean.TRUE);
                                    experiencia.getRuta43().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "44":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta44(Boolean.TRUE);
                                    experiencia.getRuta44().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "45":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta45(Boolean.TRUE);
                                    experiencia.getRuta45().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "46":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta46(Boolean.TRUE);
                                    experiencia.getRuta46().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "47":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta47(Boolean.TRUE);
                                    experiencia.getRuta47().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "48":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta48(Boolean.TRUE);
                                    experiencia.getRuta48().add(t.getEvidenciaNombremiento());
                                }
                                break;
                            case "49":
                                if (t.getEvidenciaNombremiento() != null) {
//                                    rutasEvidenciasBD.add(t.getEvidenciaNombremiento());
                                    experiencia.setCuenta49(Boolean.TRUE);
                                    experiencia.getRuta49().add(t.getEvidenciaNombremiento());
                                }
                                break;
                        }
                    }
                });
            }
//            rutasEvidenciasBD.add("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\5.0.pdf");
            if (!listaCapacitacionespersonal.isEmpty()) {
                listaCapacitacionespersonal.forEach((t) -> {
                    if (t.getEvidenciaCapacitacion() != null) {
                        if (!t.getRipppa().equals("No Aplica")) {
                            if (t.getRipppa().equals("48")) {
                                experiencia.setCuenta48(Boolean.TRUE);
                                experiencia.getRuta48().add(t.getEvidenciaCapacitacion());
                            } else {
                                capAceptadas.add(t);
                                capacitacion.setCuentaEv(Boolean.TRUE);
                                capacitacion.getRutasEv().add(t.getEvidenciaCapacitacion());
//                                rutasEvidenciasBD.add(t.getEvidenciaCapacitacion());
                            }
                        }
                    }
                });
            }
            resumenCV= new CurriculumRIPPPA.ResumenCV(listaIdiomas, listaLenguas, listaCongresos, listaLibrosPubs, listaArticulosp, listaDocenciases, listaMemoriaspub, listaInnovaciones, listaDistinciones, listaInvestigacion, listaDesarrolloSoftwar, listaFormacionAcademica, listaExperienciasLaborales, listaCapacitacionespersonal, listaHabilidadesInformaticas, listaDesarrollosTecnologicos);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void unirPDFs() {
        try {
            
            pu.generarPdf(resumenCV, datosPersonal);
//            nombreArchivo="";
//            nombreArchivo = "CVRIPPPA " + nuevoOBJListaPersonal.getClave() +" - "+ nuevoOBJListaPersonal.getNombre();
//            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorComisionDictaminadora.unirPDFs()"+rutasEvidenciasBD);
//            List<InputStream> inputPdfList = new ArrayList<InputStream>();
//            rutasEvidenciasBD.forEach((t) -> {
//                FileInputStream fis;
//                try {
//                    fis = new FileInputStream(t);
//                    inputPdfList.add(fis);
//                } catch (FileNotFoundException ex) {
//                    Logger.getLogger(ControladorComisionDictaminadora.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            OutputStream outputStream = new FileOutputStream("C:\\archivos\\evidenciasCapitalHumano\\RIPPPA\\"+nombreArchivo+".pdf");
//
//            Document document = new Document();
//            List<PdfReader> readers = new ArrayList<PdfReader>();
//            int totalPages = 0;
//
//            //Create pdf Iterator object using inputPdfList. 
//            Iterator<InputStream> pdfIterator = inputPdfList.iterator();
//
//            // Create reader list for the input pdf files. 
//            while (pdfIterator.hasNext()) {
//                InputStream pdf = pdfIterator.next();
//                PdfReader pdfReader = new PdfReader(pdf);
//                readers.add(pdfReader);
//                totalPages = totalPages + pdfReader.getNumberOfPages();
//            }
//
//            // Create writer for the outputStream 
//            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
//
//            //Open document. 
//            document.open();
//
//            //Contain the pdf data. 
//            PdfContentByte pageContentByte = writer.getDirectContent();
//            PdfImportedPage pdfImportedPage;
//            int currentPdfReaderPage = 1;
//            Iterator<PdfReader> iteratorPDFReader = readers.iterator();
//
//            // Iterate and process the reader list. 
//            while (iteratorPDFReader.hasNext()) {
//                PdfReader pdfReader = iteratorPDFReader.next();
//                //Create page and add content. 
//                while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
//                    document.newPage();
//                    pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
//                    pageContentByte.addTemplate(pdfImportedPage, 0, 0);
//                    currentPdfReaderPage++;
//                }
//                currentPdfReaderPage = 1;
//            }
//
//            //Close document and outputStream. 
//            outputStream.flush();
//            document.close();
//            outputStream.close();
//            System.out.println("Pdf files merged successfully.");
//            
//            if (!rutasEvidenciasBD.isEmpty()) {
//                Ajax.oncomplete("descargar('" + "http://siip.utxicotepec.edu.mx/archivos/evidencias2/evidenciasCapitalHumano/RIPPPA/" + nombreArchivo + ".pdf" + "');");
//            }
//
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
