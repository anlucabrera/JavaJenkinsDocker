package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ProcesoInscripcionRolServiciosEscolares;
import mx.edu.utxj.pye.sgi.dto.dtoAlumnoFinanzas;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.finanzasRegistrodePagos.EjbFinanzasRegistroPagos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Vistapagosprimercuatrimestre;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named(value = "procesoInscripcion")
@ViewScoped
public class ProcesoInscripcion extends ViewScopedRol implements Desarrollable {
    
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private Aspirante aspirante, aspiranteValido,selectAspirante;
    @Getter @Setter private Persona persona, personaValido;
    @Getter @Setter private Estudiante estudiante,estudianteInscrito;
    @Getter @Setter private Documentosentregadosestudiante documentosentregadosestudiante;
    @Getter @Setter private List<Aspirante> listaAspirantesTSU;
    @Getter @Setter private List<Aspirante> listaAspirantesTSUXPE;
    @Getter @Setter private List<AreasUniversidad> listaPe;
    @Getter @Setter private List<AreasUniversidad> listaAreasUniversidad = new ArrayList<>();
    @Getter @Setter private List<Estudiante> listaEstudiantes = new ArrayList<>();
    @Getter @Setter private List<SelectItem> listaPEInsc;
    @Getter @Setter private List<Grupo> listaGrupos;
    @Getter @Setter private Integer folioFicha,folioFichaInscripcion;
    @Getter @Setter private long totalRegistroSemanal,totalRegistroSabatino,totalRegistroSemanalValido,totalRegistroSabatinoValido;
    @Getter @Setter private String nombreCarreraPO,nombreCarreraSO, carreraInscrito,nombrePEPrimeraOpcion;
    @Getter @Setter private Boolean opcionIncripcion = null;
    @Getter @Setter private Short areaIncripcion;
    @Getter @Setter ProcesoInscripcionRolServiciosEscolares rol;
    @Getter @Setter private Vistapagosprimercuatrimestre registro;
    @Getter @Setter private dtoAlumnoFinanzas dtoAlumnoFinanzas;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;

    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @EJB EjbUtilToolAcademicas ejbToolAcademicas;
    @EJB private EjbPropiedades ep;
    @EJB private EjbGeneracionGrupos ejb;
    @EJB EjbFinanzasRegistroPagos ejbFinanzas;
    
    @Inject LogonMB login;
    
    

@Getter private Boolean cargado = false;

@PostConstruct
    public void init(){
        try{
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.INSCRIPCION);
            aspirante = new Aspirante();
            persona = new Persona();
            aspiranteValido = new Aspirante();
            personaValido = new Persona();
            selectAspirante = new Aspirante();
            documentosentregadosestudiante = new Documentosentregadosestudiante();
            procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
            listaAspirantesTSU = ejbProcesoInscripcion.listaAspirantesTSU(procesosInscripcion.getIdProcesosInscripcion());
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave()); //Validar si pertenece departamento de Servicios Escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Filter<PersonalActivo>> resValidacion = ejb.validarServiciosEscolares(logonMB.getPersonal().getClave());
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo serviciosEscolares = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new ProcesoInscripcionRolServiciosEscolares(filtro, serviciosEscolares, serviciosEscolares.getAreaOperativa());
            tieneAcceso = rol.tieneAcceso(serviciosEscolares);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            rol.setServiciosEscolares(serviciosEscolares);
            ResultadoEJB<EventoEscolar> resEvRegFichas = ejbProcesoInscripcion.verificarEventoRegistroFichas(); //Evento para registro de fichas
            if(resEvRegFichas.getCorrecto()==true){rol.setEventoRegistroFichas(resEvRegFichas.getValor());}
            //else {mostrarMensajeResultadoEJB(resEvRegFichas);}
            //System.out.println("Registro de fichas "+ rol.getEventoRegistroFichas());
            ResultadoEJB<EventoEscolar> resEvento = ejbProcesoInscripcion.verificarEventoIncipcion();  //Verifica evento de ProcesoInscripcion
            if(resEvento.getCorrecto()==true){rol.setEventoIncripcion(resEvento.getValor());}
            //else {mostrarMensajeResultadoEJB(resEvento);}
            // System.out.println("Inscripcion"+ rol.getEventoRegistroFichas());
            if(comprabarEventos()==false){tieneAcceso=false;}//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setPeriodoActivo(resEvento.getValor().getPeriodo());
            ////////////
            listaPe = ejbSelectItemCE.itemPEAll();
            listaAreasUniversidad = ejbAreasLogeo.listaProgramasEducativos();
            if(rol.getEventoIncripcion()!=null){ listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(rol.getEventoIncripcion().getPeriodo());}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "inscripciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    public Boolean comprabarEventos(){
         try{
             Boolean acceso =false;
             if(rol.getEventoIncripcion()!=null){acceso =true; }
             else if(rol.getEventoRegistroFichas()!=null){acceso=true;}
             else {acceso =false;}
             return acceso;
            }catch (Exception e){
             mostrarExcepcion(e);
             return false;
         }
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
        String claveCorreo = "DServiciosEscolares19";
        String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Se le informa que su ficha de admisión ha sido validada correctamente, para continuar con el tu proceso de inscripción se le pide de favor que continúes con tu exámen institucional y ceneval.\n\n" +
                        "Los datos de acceso se les enviará vía correo electrónico. \n\n"
                        + "ATENTAMENTE \n" +
                        "Departamento de Servicios Escolares";
        String identificador = "Registro de Ficha de Admisión 2020 UTXJ";
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
            ResultadoEJB<Vistapagosprimercuatrimestre> resVerficaPago= ejbFinanzas.getRegistroByCurp(aspiranteValido.getIdPersona().getCurp());
            if(resVerficaPago.getCorrecto()==true){documentosentregadosestudiante.setPagoColegiatura(true);}
            else {
                documentosentregadosestudiante.setPagoColegiatura(false);
                mostrarMensajeResultadoEJB(resVerficaPago);}
            estudiante = ejbProcesoInscripcion.findByIdAspirante(aspiranteValido.getIdAspirante());
            if(estudiante != null){
                opcionIncripcion = estudiante.getOpcionIncripcion();
                documentosentregadosestudiante = estudiante.getDocumentosentregadosestudiante();
                carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
            }else{
                estudiante = new Estudiante();
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
                        estudiante = new Estudiante();
                    }
                    estudiante.setAspirante(aspiranteValido);
                    estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
                    estudiante.setFechaAlta(new Date());
                    estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
                    estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
                    carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
                    Messages.addGlobalInfo("Se ha inscrito al estudiante con éxito!");
                }
            }else if(opcionIncripcion == false){
                noGruposPO = gruposElegibles(ejbProcesoInscripcion.listaGruposXPeriodoByCarrera((short)aspiranteValido.getIdProcesoInscripcion().getIdPeriodo(),aspiranteValido.getDatosAcademicos().getSegundaOpcion(), aspiranteValido.getDatosAcademicos().getSistemaSegundaOpcion().getIdSistema(), 1));
                if(noGruposPO == 0){
                    Messages.addFlashGlobalWarn("No existen espacios para realizar la inscripción para la carrera de segunda opción !!");
                }else{
                    if(estudiante.getIdEstudiante() == null){
                        estudiante = new Estudiante();
                    }
                    estudiante.setAspirante(aspiranteValido);
                    estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
                    estudiante.setFechaAlta(new Date());
                    estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
                    estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
                    carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
                    Messages.addGlobalInfo("Se ha inscrito al estudiante con éxito!");
                }
            }
        }else{
            //Actualiza la información del estudiante si cambiar la carrera
            estudiante.setAspirante(aspiranteValido);
            estudiante.setPeriodo(procesosInscripcion.getIdPeriodo());
            estudiante.setFechaAlta(new Date());
            estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
            estudiante =ejbProcesoInscripcion.guardaEstudiante(estudiante,documentosentregadosestudiante,opcionIncripcion);
            carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
        }
        
        listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(procesosInscripcion.getIdPeriodo());
//        actualizaPago();
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
    
    public void getEstudiante(Estudiante e){
        areaIncripcion = ejbFichaAdmision.buscaPEByClave((short)e.getCarrera()).getAreaSuperior();
        estudiante = e;
        selectPE();
        selectGrupo();
    }
    
    public void selectPE(){
        listaPEInsc = ejbSelectItemCE.itemProgramEducativoPorArea(this.areaIncripcion);
    }
    
    public void selectGrupo(){
        listaGrupos = ejbToolAcademicas.listaByPeriodoCarrera((short) estudiante.getCarrera(), procesosInscripcion.getIdPeriodo());
    }
    
    public void cambiaCarrera(){
        ejbProcesoInscripcion.actualizaEstudiante(estudiante);
        listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(procesosInscripcion.getIdPeriodo());
    }
    
    public static Integer gruposElegibles(List<Grupo> grupos){
        List<Grupo> listaGrupos = new ArrayList<>();
        grupos.forEach((Grupo g) ->{
            System.out.println("tamaño de la lista"+g.getEstudianteList().size()+", capacidadMaxima"+g.getCapMaxima());
            if(g.getEstudianteList().size() != g.getCapMaxima()){
                listaGrupos.add(g);
            }
        });
        
        return listaGrupos.size();
    }
    //TODO: Actualiza el registro de pago en Finanazas
    public void actualizaPago(){
        estudianteInscrito = new Estudiante();
        dtoAlumnoFinanzas = new dtoAlumnoFinanzas();
        estudianteInscrito = ejbProcesoInscripcion.findByIdAspirante(aspiranteValido.getIdAspirante());
        System.out.println("Estudiante que se encontro por id Aspirtante" + estudianteInscrito);
        dtoAlumnoFinanzas.setPeriodo(estudianteInscrito.getPeriodo());
        dtoAlumnoFinanzas.setMatricula(estudianteInscrito.getMatricula());
        dtoAlumnoFinanzas.setCurp(estudiante.getAspirante().getIdPersona().getCurp());
        dtoAlumnoFinanzas.setEstudianteCE(estudianteInscrito);
        System.out.println("dto"+ dtoAlumnoFinanzas);
        ResultadoEJB<Vistapagosprimercuatrimestre> resActualizaPago= ejbFinanzas.saveCambios(dtoAlumnoFinanzas);
        if(resActualizaPago.getCorrecto()!=true){mostrarMensajeResultadoEJB(resActualizaPago);}
        else {mostrarMensajeResultadoEJB(resActualizaPago);}
    }
}
