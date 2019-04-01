package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.omnifaces.util.Faces;

@Named(value = "fichaAdm")
@ViewScoped
public class FichaAdmision implements Serializable {

    private static final long serialVersionUID = -7745875703360648941L;

    @Getter @Setter private String estado;
    @Getter @Setter private String estadoExtr;
    @Getter @Setter private Integer index = 0;
    @Getter @Setter private Integer pais;
    @Getter @Setter private Integer estado_iems;
    @Getter @Setter private Integer municipio_iems;
    @Getter @Setter private Integer localidad_iems;
    @Getter @Setter private Short areaAcademicaPO;
    @Getter @Setter private Short areaAcademicaSO;
    @Getter @Setter private String curp;
    @Getter @Setter private List<SelectItem> listaMunicipios, listaEstados,listaEstadosDomicilioRadica,listaMunicipiosRadica,listaEstadoProcedencia,listaMunicipiosProdecencia, listaLocalidad, listaPaises,
            listaGenero,listaTipoSangre, listaTipoDiscapacidad,listaAsentamientos,listaAsentamientoP,
            listaIems,listaAreasPO,listaAreasSO,listaPEP,listaPES,listaEstadosTutor,listaMunicipioTutor,listaAsentamientosTutor,listaEstadoIEMS,listaMunicipioIEMS,listaLocalidadIEMS;
    @Getter @Setter private List<Escolaridad> listaEscolaridad;
    @Getter @Setter private List<Ocupacion> listaOcupacion;
    @Getter @Setter private List<EspecialidadCentro> listaEspecialidades;
    @Getter @Setter private List<Turno> listaTurno;
    @Getter @Setter private List<String> selectAM;
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private Persona persona;
    @Getter @Setter private DatosMedicos datosMedicos = new DatosMedicos();
    @Getter @Setter private MedioComunicacion medioComunicacion;
    @Getter @Setter private Aspirante aspirante;
    @Getter @Setter private Domicilio domicilio;
    @Getter @Setter private TutorFamiliar tutorFamiliar;
    @Getter @Setter private DatosFamiliares datosFamiliares;
    @Getter @Setter private DatosAcademicos datosAcademicos;
    @Getter @Setter private Part file, fileR;
    @Getter @Setter private String doc;
    @Getter @Setter private Boolean dm = true,com = true,df = true,da = true,evif = true, estatusFicha = null;
    

    @EJB EJBSelectItems eJBSelectItems;
    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbSelectItemCE ejbItemCE;

    @PostConstruct
    public void init(){
        persona = new Persona();
        datosMedicos = new DatosMedicos();
        medioComunicacion = new MedioComunicacion();
        aspirante = new Aspirante();
        domicilio = new Domicilio();
        tutorFamiliar = new TutorFamiliar();
        datosFamiliares = new DatosFamiliares();
        datosAcademicos = new DatosAcademicos();
        listaGenero = ejbFichaAdmision.listaGeneros();
        listaTipoSangre = ejbItemCE.itemTipoSangre();
        listaTipoDiscapacidad = ejbItemCE.itemDiscapcidad();
        listaEstadosDomicilioRadica = eJBSelectItems.itemEstados();
        listaEstadoProcedencia = eJBSelectItems.itemEstados();
        listaEstadosTutor = eJBSelectItems.itemEstados();
        listaEstadoIEMS = eJBSelectItems.itemEstados();
        procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
        listaEscolaridad = ejbItemCE.itemEscolaridad();
        listaOcupacion = ejbItemCE.itemOcupacion();
        listaEspecialidades = ejbItemCE.itemEspecialidadCentro();
        listaAreasPO = ejbItemCE.itemAreaAcademica();
        listaAreasSO = ejbItemCE.itemAreaAcademica();
        listaTurno = ejbItemCE.itemTurno();
        Faces.setSessionAttribute("listaOcp", listaOcupacion);
        Faces.setSessionAttribute("listaEsc", listaEscolaridad);
        Faces.setSessionAttribute("listaEspecialidad", listaEspecialidades);
        Faces.setSessionAttribute("listTurno",listaTurno);
    }

    public void desencriptarCURP() throws IOException {
        if(file != null){
           persona = ejbFichaAdmision.leerCurp(file);
            if (persona.getNombre() != null) {
                if(persona.getIdpersona() == null){
                    if(persona.getEstado() == null){
                        listaPaises = eJBSelectItems.itemPaises();
                        clearInformacion();
                        estado = "false";
                        estadoExtr = "true";
                    }else if(persona.getEstado() <= 32){
                        pais = eJBSelectItems.itemCvePais(persona.getEstado());
                        listaMunicipios = eJBSelectItems.itemMunicipiosByClave(persona.getEstado());
                        listaEstados = eJBSelectItems.itemEstados();
                        listaPaises = eJBSelectItems.itemPaisMexico();
                        clearInformacion();
                        estado = "true";
                        estadoExtr = "false";
                    }
                }else{
                    if(persona.getEstado() > 32){
                        pais = eJBSelectItems.itemCvePais(persona.getEstado());
                        listaPaises = eJBSelectItems.itemPaises();
                        listaEstados = eJBSelectItems.itemEstadoByClave(pais);
                        verificarRegistros(persona);
                        estado = "false";
                        estadoExtr = "true";
                    }else if(persona.getEstado() <= 32){
                        pais = eJBSelectItems.itemCvePais(persona.getEstado());
                        listaMunicipios = eJBSelectItems.itemMunicipiosByClave(persona.getEstado());
                        listaEstados = eJBSelectItems.itemEstados();
                        listaPaises = eJBSelectItems.itemPaisMexico();
                        listaLocalidad = eJBSelectItems.itemLocalidadesByClave(persona.getEstado(),persona.getMunicipio());
                        verificarRegistros(persona);
                        estado = "true";
                        estadoExtr = "false";
                    }
                }
            }else{
                Messages.addGlobalError("El documento es incorrecto !");
            }
        }else{
            Messages.addGlobalError("Es necesario seleccionar un archivo !");
        }
    }

    public void verificarExistenciaRegistro(){
        persona = ejbFichaAdmision.buscaPersonaByCurp(curp.toUpperCase());
        if(persona != null){
            pais = eJBSelectItems.itemCvePais(persona.getEstado());
            listaMunicipios = eJBSelectItems.itemMunicipiosByClave(persona.getEstado());
            listaEstados = eJBSelectItems.itemEstados();
            listaPaises = eJBSelectItems.itemPaisMexico();
            listaLocalidad = eJBSelectItems.itemLocalidadesByClave(persona.getEstado(),persona.getMunicipio());
            curp = null;
            verificarRegistros(persona);
            Messages.addGlobalInfo("Registro localizado con exito!");
        }else{
            curp = null;
            init();
            dm = true;com = true;df = true;da = true;evif = true;
            Messages.addGlobalWarn("No Existe Registro con esa CURP, favor de verificarlo!");
        }

    }

    public void selectEstado(){
        listaEstados = eJBSelectItems.itemEstadoByClave(this.pais);
    }
    
    public void selectMunicipio(){
        listaMunicipiosRadica = eJBSelectItems.itemMunicipiosByClave(this.domicilio.getIdEstado());
    }

    public void selectMunicipioProcedencia(){
        listaMunicipiosProdecencia = eJBSelectItems.itemMunicipiosByClave(this.domicilio.getEstadoProcedencia());
    }

    public void selectMunicipioTutor(){
        listaMunicipioTutor = eJBSelectItems.itemMunicipiosByClave(this.tutorFamiliar.getEstado());
    }

    public void selectMunicipioIems(){
        listaMunicipioIEMS = eJBSelectItems.itemMunicipiosByClave(this.estado_iems);
    }
    
    public void selectLocalidad(){
        listaLocalidad = eJBSelectItems.itemLocalidadesByClave(this.persona.getEstado(),this.persona.getMunicipio());
    }

    public void selectLocalidadIems(){
        listaLocalidadIEMS = eJBSelectItems.itemLocalidadesByClave(this.estado_iems,this.municipio_iems);
    }

    public void selectAsentamiento(){
        listaAsentamientos = eJBSelectItems.itemAsentamientoByClave(domicilio.getIdEstado(), domicilio.getIdMunicipio());
    }

    public void selectAsentamientoProcedencia(){
        listaAsentamientoP = eJBSelectItems.itemAsentamientoByClave(domicilio.getEstadoProcedencia(), domicilio.getMunicipioProcedencia());
    }
    
    public void selectAsentamientoTutor(){
        listaAsentamientosTutor = eJBSelectItems.itemAsentamientoByClave(tutorFamiliar.getEstado(), tutorFamiliar.getMunicipio());
    }

    public void GuardaDatosPesonales(){

        if(ejbFichaAdmision.buscaPersonaByCurp(persona.getCurp()) != null){
            persona = ejbFichaAdmision.actualizaPersona(persona);
            dm = false;
            index = 1;
        } else {
            ejbFichaAdmision.GuardaPersona(persona);
            index = 0;
            dm = false;
            index = 1;
        }
        
    }

    public void guardaDatosMedicos(){
        if(datosMedicos.getCvePersona() == null){
            selectAM.stream()
                    .forEach(s -> {
                        switch (s) {
                            case "Dia":
                                datosMedicos.setFDiabetes(true);
                                break;
                            case "Hip":
                                datosMedicos.setFHipertenso(true);
                                break;
                            case "Car":
                                datosMedicos.setFCardiaco(true);
                                break;
                            case "Can":
                                datosMedicos.setFCancer(true);
                                break;
                        }
                    });
            datosMedicos.setCvePersona(persona.getIdpersona());
            ejbFichaAdmision.guardaDatosMedicos(datosMedicos);
            com = false;
            index =2;
        }else{
            selectAM.stream()
                    .forEach(s -> {
                        switch (s) {
                            case "Dia":
                                datosMedicos.setFDiabetes(true);
                                break;
                            case "Hip":
                                datosMedicos.setFHipertenso(true);
                                break;
                            case "Car":
                                datosMedicos.setFCardiaco(true);
                                break;
                            case "Can":
                                datosMedicos.setFCancer(true);
                                break;
                        }
                    });
            ejbFichaAdmision.actualizaDatosMedicos(datosMedicos);
            com = false;
            index =2;
        }
    }

    public void usoMismaDireccion(){
        domicilio.setCalleProcedencia(domicilio.getCalle());
        domicilio.setNumeroProcedencia(domicilio.getNumero());
        domicilio.setEstadoProcedencia(domicilio.getIdEstado());
        domicilio.setMunicipioProcedencia(domicilio.getIdMunicipio());
        domicilio.setAsentamientoProcedencia(domicilio.getIdAsentamiento());
        selectMunicipioProcedencia();
        selectAsentamientoProcedencia();
    }

    public void guardaComunicacionDomicilio(){
        if(domicilio.getAspirante() == null){
            medioComunicacion.setPersona(persona.getIdpersona());
            ejbFichaAdmision.guardaComunicacion(medioComunicacion);
            aspirante.setIdPersona(persona);
            aspirante.setIdProcesoInscripcion(procesosInscripcion);
            aspirante.setEstatus(false);
            aspirante.setFechaRegistro(new Date());
            ejbFichaAdmision.guardaAspirante(aspirante);
            domicilio.setAspirante(aspirante.getIdAspirante());
            ejbFichaAdmision.guardaDomicilo(domicilio);
            df = false;
            index = 3;
        }else{
            ejbFichaAdmision.actualizaCamunicacion(medioComunicacion);
            ejbFichaAdmision.actualizaDomicilio(domicilio);
            df = false;
            index = 3;
        }
    }

    public void guardaDatosFamiliares(){
        if(datosFamiliares.getAspirante() == null){
            ejbFichaAdmision.guardaTutorFamiliar(tutorFamiliar);
            if(tutorFamiliar.getIdTutorFamiliar() > 0){
                datosFamiliares.setAspirante(aspirante.getIdAspirante());
                datosFamiliares.setTutor(tutorFamiliar);
                ejbFichaAdmision.guardaDatosFamiliares(datosFamiliares);
            }
            da = false;
            index = 4;
        }else{
            ejbFichaAdmision.actualizaTutorFamiliar(tutorFamiliar);
            ejbFichaAdmision.actualizaDatosFamiliares(datosFamiliares);
            da = false;
            index = 4;
        }
        
    }
    
    public void selectIems(){
        listaIems = ejbItemCE.itemIems(this.estado_iems, this.municipio_iems, this.localidad_iems);
    }

    public void selectPEPrincipal(){
        listaPEP = ejbItemCE.itemProgramEducativoPorArea(this.areaAcademicaPO);
    }

    public void selectPEOpcional(){
        listaPES = ejbItemCE.itemProgramEducativoPorArea(this.areaAcademicaSO);       
    }

    public void guardaDatosAcademicos(){
        if(datosAcademicos.getAspirante() == null){
            if(datosAcademicos.getPrimeraOpcion().equals(datosAcademicos.getSegundaOpcion())){
                datosAcademicos.setSegundaOpcion(null);
                Messages.addGlobalWarn("La carrera principal y opcional deben de ser diferentes!");
                index = 4;
            }else{
                ejbFichaAdmision.verificarFolio(procesosInscripcion);
                aspirante.setFolioAspirante(ejbFichaAdmision.verificarFolio(procesosInscripcion));
                aspirante.setFechaRegistro(new Date());
                ejbFichaAdmision.actualizaAspirante(aspirante);
                datosAcademicos.setAspirante(aspirante.getIdAspirante());
                ejbFichaAdmision.guardaDatosAcademicos(datosAcademicos);
                evif = false;
                index = 5;
            }
            
        }else{
            if(datosAcademicos.getPrimeraOpcion().equals(datosAcademicos.getSegundaOpcion())){
                datosAcademicos.setSegundaOpcion(null);
                Messages.addGlobalWarn("La carrera principal y opcional deben de ser diferentes!");
                index = 4;
            }else{
                ejbFichaAdmision.actualizaDatosAcademicos(datosAcademicos);
                evif = false;
                index = 5;
            } 
        }
        
    }

    public void verificarRegistros(Persona persona){
        if (persona.getDatosMedicos() != null) {
            selectAM = new ArrayList<>();
            datosMedicos = persona.getDatosMedicos();
            if(datosMedicos.getFDiabetes() == true){
                selectAM.add("Dia");
            }
            if(datosMedicos.getFHipertenso() == true){
                selectAM.add("Hip");
            }
            if(datosMedicos.getFCardiaco() == true){
                selectAM.add("Car");
            }
            if(datosMedicos.getFCancer() == true){
                selectAM.add("Can");
            }
            dm = false;
            com = false;
        }
        if(persona.getMedioComunicacion() != null){
            medioComunicacion = persona.getMedioComunicacion();
            aspirante = ejbFichaAdmision.buscaAspiranteByClave(persona.getIdpersona());
            domicilio = aspirante.getDomicilio();
            selectMunicipio();
            selectAsentamiento();
            selectMunicipioProcedencia();
            selectAsentamientoProcedencia();
            com = false;
            df = false;
        }
        if(aspirante.getDatosFamiliares() != null) {
            datosFamiliares = aspirante.getDatosFamiliares();
            tutorFamiliar = datosFamiliares.getTutor();
            selectMunicipioTutor();
            selectAsentamientoTutor();
            df = false;
            da = false;

        }
        if(aspirante.getDatosAcademicos() != null){
            datosAcademicos = aspirante.getDatosAcademicos();
            Iems iems = new Iems();
            ProgramasEducativos p1 = new ProgramasEducativos();
            ProgramasEducativos p2 = new ProgramasEducativos();
            iems = ejbFichaAdmision.buscaIemsByClave(datosAcademicos.getInstitucionAcademica());
            estado_iems = iems.getLocalidad().getLocalidadPK().getClaveEstado();
            municipio_iems = iems.getLocalidad().getLocalidadPK().getClaveMunicipio();
            localidad_iems = iems.getLocalidad().getLocalidadPK().getClaveLocalidad();
            p1 = ejbFichaAdmision.buscaPEByClave(datosAcademicos.getPrimeraOpcion());
            p2 = ejbFichaAdmision.buscaPEByClave(datosAcademicos.getSegundaOpcion());
            areaAcademicaPO = p1.getArea().getArea();
            areaAcademicaSO = p2.getArea().getArea();
            selectMunicipioIems();
            selectLocalidadIems();
            selectIems();
            selectPEPrincipal();
            selectPEOpcional();
            da = false;
            evif = false;
        }
    }

    public  void clearInformacion(){
        datosMedicos = new DatosMedicos();
        selectAM = new ArrayList<>();
        medioComunicacion = new MedioComunicacion();
        domicilio = new Domicilio();
        datosFamiliares = new DatosFamiliares();
        tutorFamiliar = new TutorFamiliar();
        datosAcademicos = new DatosAcademicos();
        dm = true;
        com = true;
        df = true;
        da = true;
        evif = true;
    }
    
    public void estatusRegistroFicha(){
        persona = ejbFichaAdmision.buscaPersonaByCurp(curp);
        if(persona != null){
            aspirante = ejbFichaAdmision.buscaAspiranteByClave(persona.getIdpersona());
            if(aspirante.getDatosAcademicos() != null){
                estatusFicha = aspirante.getEstatus();
            }else{
                estatusFicha = false;
            }
        }else{
            Messages.addGlobalWarn("No existe registro o si esta incompleto, favor de verificarlo!");
        }
        curp = null;
    }
    
    public void downloadFichaAdmin() throws IOException, DocumentException{
        ejbFichaAdmision.generaFichaAdmin(persona,datosAcademicos,domicilio,aspirante,medioComunicacion);
    }
    
    public void nuevoRegistro(){
        clearInformacion();
        persona = new Persona();
    }
    private static final Logger LOG = Logger.getLogger(FichaAdmision.class.getName());
}
