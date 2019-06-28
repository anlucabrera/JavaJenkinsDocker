package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbUtilToolAcademicas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;
import org.omnifaces.util.Messages;

@Named(value = "procesoInscripcion")
@ViewScoped
public class ProcesoInscripcion implements Serializable{
    
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private Aspirante aspirante, aspiranteValido,selectAspirante;
    @Getter @Setter private Persona persona, personaValido;
    @Getter @Setter private Inscripcion estudiante;
    @Getter @Setter private Documentosentregadosestudiante documentosentregadosestudiante;
    @Getter @Setter private List<Aspirante> listaAspirantesTSU;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;
    @Getter @Setter private List<AreasUniversidad> listaPe;
    @Getter @Setter private List<AreasUniversidad> listaAreasUniversidad = new ArrayList<>();
    @Getter @Setter private List<Inscripcion> listaEstudiantes = new ArrayList<>();
    @Getter @Setter private List<SelectItem> listaPEInsc;
    @Getter @Setter private List<Grupo> listaGrupos;
    @Getter @Setter private Integer folioFicha,folioFichaInscripcion;
    @Getter @Setter private long totalRegistroSemanal,totalRegistroSabatino,totalRegistroSemanalValido,totalRegistroSabatinoValido;
    @Getter @Setter private String nombreCarreraPO,nombreCarreraSO, carreraInscrito,nombrePEPrimeraOpcion;
    @Getter @Setter private Boolean opcionIncripcion = null;
    @Getter @Setter private Short areaIncripcion;

    
    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @EJB EjbUtilToolAcademicas ejbToolAcademicas;
    
    @Inject LogonMB login;
    
    @PostConstruct
    public void init(){
        aspirante = new Aspirante();
        persona = new Persona();
        aspiranteValido = new Aspirante();
        personaValido = new Persona();
        selectAspirante = new Aspirante();
        documentosentregadosestudiante = new Documentosentregadosestudiante();
        procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
        listaAspirantesTSU = ejbProcesoInscripcion.listaAspirantesTSU(procesosInscripcion.getIdProcesosInscripcion());
        listaPe = ejbSelectItemCE.itemPEAll();
        listaAreasUniversidad = ejbAreasLogeo.listaProgramasEducativos();
        listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(procesosInscripcion.getIdPeriodo());
    }
    
    public void buscarFichaAdmision(){
        aspirante = ejbProcesoInscripcion.buscaAspiranteByFolio(folioFicha);
        if(aspirante != null){
            persona = aspirante.getIdPersona();
            nombrePEPrimeraOpcion = ejbProcesoInscripcion.buscaAreaByClave(aspirante.getDatosAcademicos().getPrimeraOpcion()).getNombre();
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
                        + "Activo a partir del 20 de mayo de 2019\n"
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
        nombrePEPrimeraOpcion = null;
    }
    
    public void resetInput(){
        init();
        folioFicha = null;
        nombrePEPrimeraOpcion = null;
        
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
            }else{
                estudiante = new Inscripcion();
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
        Integer noGruposPO = 0;
      
        if((estudiante.getIdEstudiante() == null) || (estudiante.getOpcionIncripcion() != opcionIncripcion)){
            //Condicional para detectar la opción de inscripción
            if(opcionIncripcion == true){
                noGruposPO = gruposElegibles(ejbProcesoInscripcion.listaGruposXPeriodoByCarrera((short)aspiranteValido.getIdProcesoInscripcion().getIdPeriodo(),aspiranteValido.getDatosAcademicos().getPrimeraOpcion(), aspiranteValido.getDatosAcademicos().getSistemaPrimeraOpcion().getIdSistema(), 1));
                //Verifica si existen grupos para realizar la inscripción
                if(noGruposPO == 0){
                    Messages.addFlashGlobalWarn("No existen espacios para realizar la inscripción para la carrera de primera opción !!");
                }else{
                    //Realiza inscripción del estudiante
                    if(estudiante.getIdEstudiante() == null){
                        estudiante = new Inscripcion();
                    }
                    estudiante.setAspirante(aspiranteValido);
                    estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
                    estudiante.setFechaAlta(new Date());
                    estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
                    estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
                    carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short)estudiante.getCarrera()).getNombre();
                }
            }else if(opcionIncripcion == false){
                noGruposPO = gruposElegibles(ejbProcesoInscripcion.listaGruposXPeriodoByCarrera((short)aspiranteValido.getIdProcesoInscripcion().getIdPeriodo(),aspiranteValido.getDatosAcademicos().getSegundaOpcion(), aspiranteValido.getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema(), 1));
                if(noGruposPO == 0){
                    Messages.addFlashGlobalWarn("No existen espacios para realizar la inscripción para la carrera de segunda opción !!");
                }else{
                    if(estudiante.getIdEstudiante() == null){
                        estudiante = new Inscripcion();
                    }
                    estudiante.setAspirante(aspiranteValido);
                    estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
                    estudiante.setFechaAlta(new Date());
                    estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
                    estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
                    carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short)estudiante.getCarrera()).getNombre();
                }
            }
        }else{
            //Actualiza la información del estudiante si cambiar la carrera
            estudiante.setAspirante(aspiranteValido);
            estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
            estudiante.setFechaAlta(new Date());
            estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
            estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
            carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short)estudiante.getCarrera()).getNombre();
        }
        
        listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(procesosInscripcion.getIdPeriodo());
    }
    
    public void clearDatos(){
        init();
        nombreCarreraPO = null;
        nombreCarreraSO = null;
        carreraInscrito = null;
        estudiante = new Inscripcion();
        folioFichaInscripcion = null;
        opcionIncripcion = null;
    }
    
    public void imprimirComprobateIns(){
        ejbProcesoInscripcion.generaComprobanteInscripcion(estudiante);
    }
    
    public void imprimirCartaCompromiso(){
        ejbProcesoInscripcion.generaCartaCompromiso(estudiante);
    }
    
    public void downloadFicha(Aspirante aspiranteD) throws IOException, DocumentException{
        ejbFichaAdmision.generaFichaAdmin(aspiranteD.getIdPersona(), aspiranteD.getDatosAcademicos(), aspiranteD.getDomicilio(), aspiranteD, aspiranteD.getIdPersona().getMedioComunicacion(),"SE");
    }
    
    public void actualizaListadoAspirantesTSU(){
        listaAspirantesTSU = new ArrayList<>();
        listaAspirantesTSU = ejbProcesoInscripcion.listaAspirantesTSU(procesosInscripcion.getIdProcesosInscripcion());
    }
    
    public void getEstudiante(Inscripcion e){
        areaIncripcion = ejbFichaAdmision.buscaPEByClave((short)e.getCarrera()).getAreaSuperior();
        estudiante = e;
        selectPE();
        selectGrupo();
    }
    
    public void selectPE(){
        listaPEInsc = ejbSelectItemCE.itemProgramEducativoPorArea(this.areaIncripcion);
    }
    
    public void selectGrupo(){
        listaGrupos = ejbToolAcademicas.listaByPeriodoCarrera((short)estudiante.getCarrera(), procesosInscripcion.getIdPeriodo());
    }
    
    public void cambiaCarrera(){
        ejbProcesoInscripcion.actualizaEstudiante(estudiante);
        listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(procesosInscripcion.getIdPeriodo());
    }
    
    public static Integer gruposElegibles(List<Grupo> grupos){
        List<Grupo> listaGrupos = new ArrayList<>();
        grupos.forEach((Grupo g) ->{
            System.out.println("tamaño de la lista"+g.getInscripcionList().size()+", capacidadMaxima"+g.getCapMaxima());
            if(g.getInscripcionList().size() != g.getCapMaxima()){
                listaGrupos.add(g);
            }
        });
        
        return listaGrupos.size();
    }
}
