package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;
import org.omnifaces.util.Messages;

@Named(value = "procesoInscripcion")
@ViewScoped
public class ProcesoInscripcion implements Serializable{
    
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private Aspirante aspirante, aspiranteValido;
    @Getter @Setter private Persona persona, personaValido;
    @Getter @Setter private Estudiante estudiante = new Estudiante();
    @Getter @Setter private Documentosentregadosestudiante documentosentregadosestudiante;
    @Getter @Setter private List<Aspirante> listaAspirantesTSU;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;
    @Getter @Setter private List<AreasUniversidad> listaPe;
    @Getter @Setter private List<AreasUniversidad> listaAreasUniversidad = new ArrayList<>();
    @Getter @Setter private Integer folioFicha,folioFichaInscripcion;
    @Getter @Setter private long totalRegistroSemanal,totalRegistroSabatino,totalRegistroSemanalValido,totalRegistroSabatinoValido;
    @Getter @Setter private String nombreCarreraPO,nombreCarreraSO, carreraInscrito;
    @Getter @Setter private Boolean opcionIncripcion = null;

    
    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    
    @PostConstruct
    public void init(){
        aspirante = new Aspirante();
        persona = new Persona();
        aspiranteValido = new Aspirante();
        personaValido = new Persona();
        documentosentregadosestudiante = new Documentosentregadosestudiante();
        procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
        listaAspirantesTSU = ejbProcesoInscripcion.listaAspirantesTSU(procesosInscripcion.getIdProcesosInscripcion());
        listaPe = ejbSelectItemCE.itemPEAll();
        listaAreasUniversidad = ejbAreasLogeo.listaProgramasEducativos();
    }
    
    public void buscarFichaAdmision(){
        aspirante = ejbProcesoInscripcion.buscaAspiranteByFolio(folioFicha);
        if(aspirante != null){
            persona = aspirante.getIdPersona();
            nombreCarreraPO = ejbProcesoInscripcion.buscaAreaByClave(aspirante.getDatosAcademicos().getPrimeraOpcion()).getNombre();
            nombreCarreraSO = ejbProcesoInscripcion.buscaAreaByClave(aspirante.getDatosAcademicos().getSegundaOpcion()).getNombre();
            Messages.addGlobalInfo("Registro encontrado exitosamente de "+persona.getNombre()+" !");
        }else{
            Messages.addGlobalError("No se encuentra registro de ficha de admisión con este folio !");
            folioFicha = null;
        }
    }
    
    public void validarFichaAdmision(){
        ejbFichaAdmision.actualizaAspirante(aspirante);
        String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
        String claveCorreo = "Serv.Escolares";
        String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Se le informa que su ficha de admisión ha sido validada correctamente, para continuar con el tu proceso de inscripción se le pide de favor que continúes con tu exámen institucional y ceneval.\n\n" +
                        "Datos de acceso a exámen Institucional: \n\n"
                        + "Url: http://escolar.utxj.edu.mx/utxj \n"
                        + "Usuario: "+aspirante.getFolioAspirante()+" \n" +
                          "Password: "+persona.getCurp()+"\n\n "+
                        "ATENTAMENTE \n" +
                        "Departamento de Servicios Escolares";
        String identificador = "Registro de Ficha de Admisión 2019 UTXJ";
        String asunto = "Validación Ficha de Admisión";
        if(aspirante.getIdPersona().getMedioComunicacion().getEmail() != null){
           EnvioCorreos.EnviarCorreoTxt(correoEnvia, claveCorreo, identificador,asunto,aspirante.getIdPersona().getMedioComunicacion().getEmail(),mensaje); 
        }
        init();
        folioFicha = null;
    }
    
    public void resetInput(){
        init();
        folioFicha = null;
    }
    
    public Long carlcularTotales(Short clavePe){
        listaAspirantesTSUXPE = ejbProcesoInscripcion.lisAspirantesByPE(clavePe, procesosInscripcion.getIdProcesosInscripcion());
        
        totalRegistroSemanal = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal"))
                .count();
        
        totalRegistroSabatino = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino"))
                .count();
        
        totalRegistroSemanalValido = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Semanal") && x.getEstatus() == true)
                .count();
        
        totalRegistroSabatinoValido = listaAspirantesTSUXPE.stream()
                .filter(x -> x.getDatosAcademicos().getSistemaPrimeraOpcion().getNombre().equals("Sabatino") && x.getEstatus() == true)
                .count();
        
        return totalRegistroSemanal;
    }
    
    public void buscarFichaAdmisionValida(){
        aspiranteValido = ejbProcesoInscripcion.buscaAspiranteByFolioValido(folioFichaInscripcion);
        if(aspiranteValido != null){
            estudiante = ejbProcesoInscripcion.findByIdAspirante(aspiranteValido.getIdAspirante());
            if(estudiante != null){
                opcionIncripcion = estudiante.getOpcionIncripcion();
                documentosentregadosestudiante = estudiante.getDocumentosentregadosestudiante();
                carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short)estudiante.getCarrera()).getNombre();
            }
            
            personaValido = aspiranteValido.getIdPersona();
            nombreCarreraPO = ejbProcesoInscripcion.buscaAreaByClave(aspiranteValido.getDatosAcademicos().getPrimeraOpcion()).getNombre();
            nombreCarreraSO = ejbProcesoInscripcion.buscaAreaByClave(aspiranteValido.getDatosAcademicos().getSegundaOpcion()).getNombre();
            Messages.addGlobalInfo("Registro encontrado exitosamente de "+personaValido.getNombre()+" !");
        }else{
            Messages.addGlobalError("No se encuentra registro con este folio , verificar si ya fue validado!");
            folioFichaInscripcion = null;
        }
    }
    
    public String nombrePE(Short idpe){
        return ejbProcesoInscripcion.buscaAreaByClave(idpe).getNombre();
    }
    
    public void guardarEstudiante(){
        estudiante = new Estudiante();
        estudiante.setAspirante(aspiranteValido);
        estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
        estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
        carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short)estudiante.getCarrera()).getNombre();
    }
    
    public void clearDatos(){
        init();
        nombreCarreraPO = null;
        nombreCarreraSO = null;
        carreraInscrito = null;
        estudiante = new Estudiante();
        folioFichaInscripcion = null;
        opcionIncripcion = null;
    }
    
    public void imprimirComprobateIns(){
        ejbProcesoInscripcion.generaComprobanteInscripcion(estudiante);
    }
}
