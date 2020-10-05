package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.itextpdf.text.DocumentException;
import com.sun.mail.imap.ResyncData;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoGrupo;
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
import mx.edu.utxj.pye.sgi.enums.Operacion;
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
import java.util.*;
import java.util.stream.Collectors;

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
    @EJB EjbRegistroFichaAdmision ejbRegistroFichaAdmision2;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbSelectItemCE;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @EJB EjbUtilToolAcademicas ejbToolAcademicas;
    @EJB private EjbPropiedades ep;
    @EJB private EjbGeneracionGrupos ejb;
    @EJB EjbFinanzasRegistroPagos ejbFinanzas;
    @EJB EjbSeguimientoCitasSE ejbSeguimientoCitasSE;

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
            getTipoSangre();
            getTramiteCitaActivo();
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
    //Obtiene la lista de tipo de sangre
    public void getTipoSangre(){
        try{
            ResultadoEJB<List<TipoSangre>> resSangre= ejbRegistroFichaAdmision2.getTiposSangre();
            if(resSangre.getCorrecto()==true){
                rol.setTipoSangreList(resSangre.getValor());
            }else {mostrarMensajeResultadoEJB(resSangre);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void getTramiteCitaActivo(){
        try{
            ResultadoEJB<TramitesEscolares> resTramite = ejbSeguimientoCitasSE.getTramitesbyEvento(procesosInscripcion);
            if(resTramite.getCorrecto()==true){rol.setTramiteCita(resTramite.getValor());
            }else {mostrarMensajeResultadoEJB(resTramite);}
        }catch (Exception e){mostrarExcepcion(e);}
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
    public void getCitabyAspirante(@NonNull Aspirante aspirante){
        try{
            ResultadoEJB<CitasAspirantes> resCita = ejbSeguimientoCitasSE.getCitabyAspirante(aspirante,rol.getTramiteCita());
            if(resCita.getCorrecto()==true) {
                rol.setCita(resCita.getValor());
                mostrarMensajeResultadoEJB(resCita);
            }else {mostrarMensajeResultadoEJB(resCita);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void buscarFichaAdmisionValida(){
        aspiranteValido = ejbProcesoInscripcion.buscaAspiranteByFolioValido(folioFichaInscripcion);
        if(aspiranteValido != null){
            ResultadoEJB<Vistapagosprimercuatrimestre> resVerficaPago= ejbFinanzas.getRegistroByCurp(aspiranteValido.getIdPersona().getCurp());
            if(resVerficaPago.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resVerficaPago);
            }
            else { mostrarMensajeResultadoEJB(resVerficaPago);}
            //Busca si el aspirante cuenta con cita previa
            getCitabyAspirante(aspiranteValido);
            estudiante = ejbProcesoInscripcion.findByIdAspirante(aspiranteValido.getIdAspirante());
            if(estudiante != null){
                opcionIncripcion = estudiante.getOpcionIncripcion();
                carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
                getPosiblesGrupos();
                ResultadoEJB<DtoGrupo> resGrupo = ejbProcesoInscripcion.packGrupo(estudiante.getGrupo());
                if(resGrupo.getCorrecto()==true){rol.setGrupoSeleccionado(resGrupo.getValor());}
                else {mostrarMensajeResultadoEJB(resGrupo);}
                ResultadoEJB<Documentosentregadosestudiante> resDoc = ejbProcesoInscripcion.getDocEstudiante(estudiante);
                if(resDoc.getCorrecto()==true){documentosentregadosestudiante = resDoc.getValor();
                    estudiante.setDocumentosentregadosestudiante(resDoc.getValor());
                    // System.out.println("Documentos estudiante" + documentosentregadosestudiante);
                    if(documentosentregadosestudiante.getCertificadoIems()==false || documentosentregadosestudiante.getActaNacimiento()==false){
                        rol.setCartaCom(false);
                    }else {rol.setCartaCom(true);}
                }
                else {mostrarMensajeResultadoEJB(resDoc);}
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
    public void getPosiblesGrupos(){
        try{
            if(aspiranteValido!=null){
                if(opcionIncripcion==true){
                    //Obtiene el pe opcion
                    rol.setPePo(ejbProcesoInscripcion.buscaAreaByClave(aspiranteValido.getDatosAcademicos().getPrimeraOpcion()));
                    rol.setPeSo(ejbProcesoInscripcion.buscaAreaByClave(aspiranteValido.getDatosAcademicos().getSegundaOpcion()));
                    //Se obtienen los posibles grupos de la primera opcion del aspirante
                    ResultadoEJB<List<DtoGrupo>> resGrupoPo= ejbProcesoInscripcion.getGruposbyOpcion(rol.getEventoIncripcion(),aspiranteValido,rol.getPePo(),aspiranteValido.getDatosAcademicos());
                    if(resGrupoPo.getCorrecto()==true){
                        //System.out.println("Genero grupo primera opcion");
                        rol.setPosiblesGrupos(resGrupoPo.getValor());
                        // System.out.println(rol.getPosiblesGrupos().size()+" Grupo-->"+ rol.getPosiblesGrupos().get(1).getGrupo().getGrado());
                    }else {mostrarMensajeResultadoEJB(resGrupoPo);}
                }else if(opcionIncripcion==false){
                    //Se obtienen los posibles grupos de la segunda opción
                    ResultadoEJB<List<DtoGrupo>> resGruposSo= ejbProcesoInscripcion.getGruposbyOpcion(rol.getEventoIncripcion(),aspiranteValido,rol.getPeSo(),aspiranteValido.getDatosAcademicos());
                    if(resGruposSo.getCorrecto()==true){
                        // System.out.println("Genero grupo segunda opcion");
                        rol.setPosiblesGrupos(resGruposSo.getValor());
                    }else {mostrarMensajeResultadoEJB(resGruposSo);}

                }else {
                    Messages.addGlobalError("Es necesario seleccionar la opción de inscripción del aspirante");
                }

            }

        }catch (Exception e){ mostrarExcepcion(e);}

    }

    public String nombrePE(Short idpe){
        return ejbProcesoInscripcion.buscaAreaByClave(idpe).getNombre();
    }
    public void updateCita(){
        try{
            ResultadoEJB<CitasAspirantes> resUpdateCita = ejbSeguimientoCitasSE.updateCita(rol.getCita());
            if(resUpdateCita.getCorrecto()==true){rol.setCita(resUpdateCita.getValor());mostrarMensajeResultadoEJB(resUpdateCita); }
            else {mostrarMensajeResultadoEJB(resUpdateCita);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void saveEstudiante(){
        try{
            if(estudiante.getIdEstudiante()==null){
                estudiante.setAspirante(aspiranteValido);
                estudiante.setPeriodo(rol.getEventoIncripcion().getPeriodo());
                estudiante.setFechaAlta(new Date());
                estudiante.setTrabajadorInscribe(login.getPersonal().getClave());
                //Guarda estudiante
                ResultadoEJB<Estudiante> resEstudiante=ejbProcesoInscripcion.saveEstudiante(estudiante,opcionIncripcion,rol.getGrupoSeleccionado(),documentosentregadosestudiante, Operacion.PERSISTIR,rol.getEventoIncripcion());
                if(resEstudiante.getCorrecto()==true){
                    estudiante = resEstudiante.getValor();
                    ResultadoEJB<Documentosentregadosestudiante> resDoc= ejbProcesoInscripcion.getDocEstudiante(estudiante);
                    if(resDoc.getCorrecto()==true){
                        documentosentregadosestudiante = resDoc.getValor();
                        estudiante.setDocumentosentregadosestudiante(resDoc.getValor());
                        // System.out.println("Docuementos iNS ->" +documentosentregadosestudiante);
                        if(documentosentregadosestudiante.getActaNacimiento()==false || documentosentregadosestudiante.getCertificadoIems()==false){
                            rol.setCartaCom(false);
                        }else {rol.setCartaCom(true);}
                    }else {mostrarMensajeResultadoEJB(resDoc);}
                    //Actualiza la cita (Atendido)
                    if(rol.getCita()!=null){updateCita();}
                    carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
                    mostrarMensajeResultadoEJB(resEstudiante);
                }else {mostrarMensajeResultadoEJB(resEstudiante);}
            }
            else {
                //Actualiza estudiante
                //Empaqueta el grupo
                estudiante.setAspirante(aspiranteValido);
                //System.out.println("Aspirante ->" +estudiante.getAspirante().getDatosAcademicos().getPromedio() + "Doc->" + documentosentregadosestudiante.getTipoSangre());
                ResultadoEJB<DtoGrupo> resGrupoPack= ejbProcesoInscripcion.packGrupo(estudiante.getGrupo());
                if(resGrupoPack.getCorrecto()==true){
                    rol.setGrupoSeleccionado(resGrupoPack.getValor());
                    ResultadoEJB<Estudiante> resEstudiante=ejbProcesoInscripcion.saveEstudiante(estudiante,opcionIncripcion,rol.getGrupoSeleccionado(),documentosentregadosestudiante, Operacion.ACTUALIZAR,rol.getEventoIncripcion());
                    if(resEstudiante.getCorrecto()==true){
                        //System.out.println("Actualizo estudiante");
                        estudiante = resEstudiante.getValor();
                        ResultadoEJB<Documentosentregadosestudiante> resDoc = ejbProcesoInscripcion.getDocEstudiante(estudiante);
                        if(resDoc.getCorrecto()==true){documentosentregadosestudiante= resDoc.getValor();
                            estudiante.setDocumentosentregadosestudiante(resDoc.getValor());
                            //System.out.println("Es" +estudiante + "doc " +documentosentregadosestudiante);
                        }
                        else {mostrarMensajeResultadoEJB(resDoc);}
                        mostrarMensajeResultadoEJB(resEstudiante);
                        //actualiza la cita
                        if(rol.getCita()!=null){updateCita();}
                    }else {mostrarMensajeResultadoEJB(resEstudiante);}
                    getPosiblesGrupos();
                }else {mostrarMensajeResultadoEJB(resGrupoPack);}
                carreraInscrito = ejbProcesoInscripcion.buscaAreaByClave((short) estudiante.getCarrera()).getNombre();
            }
            listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(rol.getEventoIncripcion().getPeriodo());
        }catch (Exception e){mostrarExcepcion(e);}
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
    public void seleccionaAspirante(@NonNull Aspirante aspirante){
        try{
            rol.setAspiranteSeleccionado(aspirante);
            //System.out.println("Aspirante seleccionado "+rol.getAspiranteSeleccionado());
            rol.setDatosAcademicosAspiranteSelect(rol.getAspiranteSeleccionado().getDatosAcademicos());
            //System.out.println("Datis ac" + rol.getDatosAcademicosAspiranteSelect());
            //Primera opcion
            rol.setPePoA(ejbFichaAdmision.buscaPEByClave(rol.getDatosAcademicosAspiranteSelect().getPrimeraOpcion()));
            //System.out.println("Po ->" +rol.getPePoA());
            rol.setAreaPoA(ejbFichaAdmision.buscaPEByClave(rol.getPePoA().getAreaSuperior()));
            //System.out.println("Area po ->" + rol.getAreaPoA());
            rol.setsPoA(rol.getDatosAcademicosAspiranteSelect().getSistemaPrimeraOpcion());
            //Segunda opcion
            rol.setPeSoA(ejbFichaAdmision.buscaPEByClave(rol.getDatosAcademicosAspiranteSelect().getSegundaOpcion()));
            //System.out.println("Pe Segunda opción " + rol.getPeSoA());
            rol.setAreaSoA(ejbFichaAdmision.buscaPEByClave(rol.getPeSoA().getAreaSuperior()));
            //System.out.println("Area so ->" + rol.getAreaSoA());
            rol.setsSoA(rol.getDatosAcademicosAspiranteSelect().getSistemaSegundaOpcion());
            //System.out.println("Sistema SO ->" + rol.getSSoA());
            //Lista de sistemas
            ResultadoEJB<List<Sistema>> resSistemas= ejbRegistroFichaAdmision2.getSistemas();
            if(resSistemas.getCorrecto()==true){rol.setSistemasAspirante(resSistemas.getValor());}
            //Areas academicas y pe (Primera y segunda opción)
            ResultadoEJB<List<AreasUniversidad>>resAreas = ejbRegistroFichaAdmision2.getAreasAcademicas();
            //System.out.println("Areas academicas " + resAreas.getValor());
            if(resAreas.getCorrecto()==true){ rol.setAreasPoA(resAreas.getValor());rol.setAreasSoA(resAreas.getValor());
                //System.out.println("Areas academicas " +rol.getAreasPoA());
                }
            else {mostrarMensajeResultadoEJB(resAreas);}
            //Lista de programas educativos (Primera opcion)
            ResultadoEJB<List<AreasUniversidad>> resPe = ejbRegistroFichaAdmision2.getProgramasE();
            if(resPe.getCorrecto()==true){
                rol.setPesPoA(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaPoA().getArea())).collect(Collectors.toList()));
            }else {mostrarMensajeResultadoEJB(resPe);}
            //System.out.println("PE por area primera opcion "+ rol.getPesPoA().size());
            //Lista de programas educativos (Segunda opcion)
            ResultadoEJB<List<AreasUniversidad>> resPeSo = ejbRegistroFichaAdmision2.getProgramasE();
            if(resPeSo.getCorrecto()==true){
                rol.setPesSoA(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaSoA().getArea())).collect(Collectors.toList()));
            }else {mostrarMensajeResultadoEJB(resPe);}
            //System.out.println("PE por area "+ rol.getPesSoA().size());

        }catch (Exception e){mostrarExcepcion(e);}
    }
    public void  getSelectEstudiante(@NonNull Estudiante estudiante){
        try{
            rol.setEstudianteSeleccionado(estudiante);
            //System.out.println("Estudiante seleccionado ->" + rol.getEstudianteSeleccionado());
            rol.setPeEstudiante(ejbFichaAdmision.buscaPEByClave(rol.getEstudianteSeleccionado().getCarrera()));
            rol.setAreaAEstudiante(ejbFichaAdmision.buscaPEByClave(rol.getPeEstudiante().getAreaSuperior()));
            rol.setSistemaSeleccionado(rol.getEstudianteSeleccionado().getGrupo().getIdSistema());
            //Lista de sistemas
            ResultadoEJB<List<Sistema>> resSistemas= ejbRegistroFichaAdmision2.getSistemas();
            if(resSistemas.getCorrecto()==true){rol.setSistemas(resSistemas.getValor());}
            else {mostrarMensajeResultadoEJB(resSistemas);}
            //Areas academicas y pe
            ResultadoEJB<List<AreasUniversidad>>resAreas = ejbRegistroFichaAdmision2.getAreasAcademicas();
            if(resAreas.getCorrecto()==true){ rol.setAreasAcademicasE(resAreas.getValor()); }
            else {mostrarMensajeResultadoEJB(resAreas);}
            //Lista de programas educativos x area inscrita
            ResultadoEJB<List<AreasUniversidad>> resPe = ejbRegistroFichaAdmision2.getProgramasE();
            if(resPe.getCorrecto()==true){
                rol.setPeE(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaAEstudiante().getArea())).collect(Collectors.toList()));
            }else {mostrarMensajeResultadoEJB(resPe);}
            //Obtiene el grupo
            ResultadoEJB<DtoGrupo> resGrupo = ejbProcesoInscripcion.packGrupo(rol.getEstudianteSeleccionado().getGrupo());
            if(resGrupo.getCorrecto()==true){rol.setGrupoSelecEs(resGrupo.getValor());}
            else {mostrarMensajeResultadoEJB(resGrupo);}
            //Lista de grupos posibles
            ResultadoEJB<List<Grupo>> resGruposbyPe = ejbProcesoInscripcion.getGruposbyPe(rol.getEventoIncripcion(),rol.getPeEstudiante(),rol.getSistemaSeleccionado());
            if(resGruposbyPe.getCorrecto()==true){
                List<DtoGrupo> grupos= new ArrayList<>();
                //Se recorren los grupos para poder empaquetarlos
                resGruposbyPe.getValor().forEach(g->{
                    //Se empaquetan los grupos
                    ResultadoEJB<DtoGrupo> resPack= ejbProcesoInscripcion.packGrupo(g);
                    if(resPack.getCorrecto()==true){
                        grupos.add(resPack.getValor());
                    }
                });
                rol.setGruposPosiblesE(grupos);
            }else {mostrarMensajeResultadoEJB(resGruposbyPe);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void clearDatos(){
        init();
        nombreCarreraPO = null;
        nombreCarreraSO = null;
        carreraInscrito = null;
        estudiante = new Estudiante();
        folioFichaInscripcion = null;
        opcionIncripcion = null;
        documentosentregadosestudiante =new Documentosentregadosestudiante();
        rol.setCita(new CitasAspirantes());
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
        try{
            //Lista de programas educativos x area inscrita
            ResultadoEJB<List<AreasUniversidad>> resPe = ejbRegistroFichaAdmision2.getProgramasE();
            if(resPe.getCorrecto()==true){
                rol.setPeE(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaAEstudiante().getArea())).collect(Collectors.toList()));
                rol.setPeEstudiante(rol.getPeE().get(1));
                //Lista de grupos posibles
                ResultadoEJB<List<Grupo>> resGruposbyPe = ejbProcesoInscripcion.getGruposbyPe(rol.getEventoIncripcion(),rol.getPeEstudiante(),rol.getSistemaSeleccionado());
                if(resGruposbyPe.getCorrecto()==true){
                    List<DtoGrupo> grupos= new ArrayList<>();
                    //Se recorren los grupos para poder empaquetarlos
                    resGruposbyPe.getValor().forEach(g->{
                        //Se empaquetan los grupos
                        ResultadoEJB<DtoGrupo> resPack= ejbProcesoInscripcion.packGrupo(g);
                        if(resPack.getCorrecto()==true){
                            grupos.add(resPack.getValor());
                        }
                    });
                    rol.setGruposPosiblesE(grupos);
                    rol.setGrupoSelecEs(grupos.get(1));
                }else {mostrarMensajeResultadoEJB(resGruposbyPe);}
            }else {mostrarMensajeResultadoEJB(resPe);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene los programas educativos por area academica seleccionada (Primera opción)
     */
    public  void selectPePrimeraOAspirantebyArea(){
        try{
            //Lista de programas educativos x area primera opción
            //System.out.println("Pe primera opción --------- Areas seleccionada"+ rol.getAreaPoA().getArea());
            ResultadoEJB<List<AreasUniversidad>> resPe = ejbRegistroFichaAdmision2.getProgramasE();
            //System.out.println("Lista de pe -> " +resPe.getValor().size());
            if(resPe.getCorrecto()==true){
                rol.setPesPoA(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaPoA().getArea())).collect(Collectors.toList()));
                //System.out.println("1" + rol.getPesPoA().size());
                rol.setPePoA(rol.getPesPoA().get(1));
                rol.setsPoA(rol.getSistemasAspirante().get(1));
                rol.getDatosAcademicosAspiranteSelect().setPrimeraOpcion(rol.getPePoA().getArea());
                rol.getDatosAcademicosAspiranteSelect().setSistemaPrimeraOpcion(rol.getSPoA());
               // System.out.println("Programas educativos res....." + rol.getPesPoA().size()+ "Datos academicos ->"+rol.getDatosAcademicosAspiranteSelect());

            }else {mostrarMensajeResultadoEJB(resPe);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    public  void selectPeSegundaOAspirantebyArea(){
        try{
            //Lista de programas educativos x area primera opción
            //System.out.println("Pe segunda opción ---------");
            ResultadoEJB<List<AreasUniversidad>> resPe = ejbRegistroFichaAdmision2.getProgramasE();
            if(resPe.getCorrecto()==true){
                rol.setPesSoA(resPe.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getAreaSoA().getArea())).collect(Collectors.toList()));
                rol.setPeSo(rol.getPesPoA().get(1));
                rol.setsSoA(rol.getSistemasAspirante().get(1));
                rol.getDatosAcademicosAspiranteSelect().setSegundaOpcion(rol.getPeSoA().getArea());
                rol.getDatosAcademicosAspiranteSelect().setSistemaSegundaOpcion(rol.getSSoA());
               // System.out.println("Programas educativos res....." + rol.getPesSoA().size());

            }else {mostrarMensajeResultadoEJB(resPe);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void selectGrupo(){
        try{
            //Lista de grupos posibles
            ResultadoEJB<List<Grupo>> resGruposbyPe = ejbProcesoInscripcion.getGruposbyPe(rol.getEventoIncripcion(),rol.getPeEstudiante(),rol.getSistemaSeleccionado());
            if(resGruposbyPe.getCorrecto()==true){
                List<DtoGrupo> grupos= new ArrayList<>();
                //Se recorren los grupos para poder empaquetarlos
                resGruposbyPe.getValor().forEach(g->{
                    //Se empaquetan los grupos
                    ResultadoEJB<DtoGrupo> resPack= ejbProcesoInscripcion.packGrupo(g);
                    if(resPack.getCorrecto()==true){
                        grupos.add(resPack.getValor());
                    }
                });
                rol.setGruposPosiblesE(grupos);
            }else {mostrarMensajeResultadoEJB(resGruposbyPe);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void cambiaCarrera(){
        try{
            rol.getEstudianteSeleccionado().setGrupo(rol.getGrupoSelecEs().getGrupo());
            rol.getEstudianteSeleccionado().setCarrera(rol.getPeEstudiante().getArea());
            ResultadoEJB<Estudiante> resAc= ejbProcesoInscripcion.saveEstudiante(rol.getEstudianteSeleccionado(),rol.getEstudianteSeleccionado().getOpcionIncripcion(),rol.getGrupoSelecEs(),rol.getEstudianteSeleccionado().getDocumentosentregadosestudiante(),Operacion.ACTUALIZAR,rol.getEventoIncripcion());
            if(resAc.getCorrecto()==true){
                rol.setEstudianteSeleccionado(resAc.getValor());
                listaEstudiantes = ejbProcesoInscripcion.listaEstudiantesXPeriodo(rol.getEventoIncripcion().getPeriodo());
                mostrarMensajeResultadoEJB(resAc);
            }else {mostrarMensajeResultadoEJB(resAc);}

        }catch (Exception e){mostrarExcepcion(e);}

    }

    public static Integer gruposElegibles(List<Grupo> grupos){
        List<Grupo> listaGrupos = new ArrayList<>();
        grupos.forEach((Grupo g) ->{
            //System.out.println("tamaño de la lista"+g.getEstudianteList().size()+", capacidadMaxima"+g.getCapMaxima());
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
        //System.out.println("Estudiante que se encontro por id Aspirtante" + estudianteInscrito);
        dtoAlumnoFinanzas.setPeriodo(estudianteInscrito.getPeriodo());
        dtoAlumnoFinanzas.setMatricula(estudianteInscrito.getMatricula());
        dtoAlumnoFinanzas.setCurp(estudiante.getAspirante().getIdPersona().getCurp());
        dtoAlumnoFinanzas.setEstudianteCE(estudianteInscrito);
        //System.out.println("dto"+ dtoAlumnoFinanzas);
        ResultadoEJB<Vistapagosprimercuatrimestre> resActualizaPago= ejbFinanzas.saveCambios(dtoAlumnoFinanzas);
        if(resActualizaPago.getCorrecto()!=true){mostrarMensajeResultadoEJB(resActualizaPago);}
        else {mostrarMensajeResultadoEJB(resActualizaPago);}
    }
}
