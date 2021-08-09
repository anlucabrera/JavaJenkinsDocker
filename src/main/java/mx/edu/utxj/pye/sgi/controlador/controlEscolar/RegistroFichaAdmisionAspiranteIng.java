package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspiranteIng;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroFichaAdmisionRolAspiranteIng;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaIngenieria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named(value = "registroFichaAdmisionAspiranteIng")
@ViewScoped
public class RegistroFichaAdmisionAspiranteIng extends ViewScopedRol implements Desarrollable {
    @EJB private EjbPropiedades ep;
    @EJB private EjbRegistroFichaIngenieria ejbRegistroIng;
    @EJB private EjbRegistroFichaAdmision ejbRegistroFicha;
    @EJB private EjbFichaAdmision efa;
    @Inject
    LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    @Getter @Setter
    RegistroFichaAdmisionRolAspiranteIng rol;
    @Getter @Setter String valor;
    @Getter @Setter Boolean finalizoVocacional;

    @PostConstruct
    public void init(){
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
        cargado = true;
        setVistaControlador(ControlEscolarVistaControlador.REGISTRO_FICHA_ASPIRANTE_ING);
        ResultadoEJB<DtoAspiranteIng> resAcceso = ejbRegistroIng.verificarAcceso(logonMB.getEmail());
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
        ResultadoEJB<DtoAspiranteIng> resValidacion = ejbRegistroIng.verificarAcceso(logonMB.getEmail());
        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
        rol = new RegistroFichaAdmisionRolAspiranteIng();
        if(resAcceso.getCorrecto()){tieneAcceso=true;}else {tieneAcceso=false;}
        rol.setEsTipo(resAcceso.getValor());
        rol.setTieneAcceso(tieneAcceso);
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        ResultadoEJB<EventoEscolar> resEvento = ejbRegistroIng.verficarEventoRegistro();
        mostrarMensajeResultadoEJB(resAcceso);
        if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        rol.setEventoEscolar(resEvento.getValor());
        // Se busca un proceso de inscripción activo
        ResultadoEJB<ProcesosInscripcion> resProcesoI = ejbRegistroIng.getProcesosInscripcionActivo();
        if(resProcesoI.getCorrecto()==true){rol.setProcesosInscripcion(resProcesoI.getValor());}
        else {tieneAcceso=false;}
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        //if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);
        rol.setComunicacion(new MedioComunicacion());
        rol.setPersonaD(new DtoAspirante.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setAspirante(new DtoAspirante.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDmedico(new DtoAspirante.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setTutor(new DtoAspirante.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDfamiliares(new DtoAspirante.FamiliaresR(new DatosFamiliares(), rol.getTutor(), new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDdomicilios(new DtoAspirante.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE));
        rol.setDacademicos(new DtoAspirante.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE));
        rol.setEncuesta(new DtoAspirante.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE));

        rol.setDialogEn(Boolean.FALSE);
        rol.setEsTipo(resAcceso.getValor());
        cargaDatos();
        getPersona();
        getRegistro();
        getAspirante();
        ResultadoEJB<EventoEscolar> resEventoValidacion= ejbRegistroIng.getUltimoEventoValidacionFicha(rol.getEventoEscolar());
        if(resEventoValidacion.getCorrecto()){rol.setEventoValidacion(resEventoValidacion.getValor());}
        else {mostrarMensajeResultadoEJB(resEventoValidacion);}
        ResultadoEJB<EventoEscolar> resEventoInscripcion = ejbRegistroIng.getIncripcionIng(rol.getEventoEscolar());
        if(resEventoInscripcion.getCorrecto()){rol.setEventoInscripcion(resEventoInscripcion.getValor());}
        else {mostrarMensajeResultadoEJB(resEventoInscripcion);}
    }

    public void getPersona(){
        try{
            ResultadoEJB<DtoAspirante.PersonaR> resPer = ejbRegistroFicha.getPersonaR(rol.getEsTipo().getEstudianteCE().getAspirante().getIdPersona());
            if(resPer.getCorrecto()){
                rol.setPersonaD(resPer.getValor());
                // getRegistro();
            }else mostrarMensajeResultadoEJB(resPer);
        }catch (Exception e){mostrarExcepcion(e);}
    }


    public void cargaDatos(){
        try{
            ResultadoEJB<List<TipoSangre>> resTipoSangre = ejbRegistroFicha.getTiposSangre(); //Tipos de sangre
            if(resTipoSangre.getCorrecto()==true){rol.setTipoSangres(resTipoSangre.getValor());}
            else {mostrarMensajeResultadoEJB(resTipoSangre);}
            ResultadoEJB<List<TipoDiscapacidad>> resDiscapacidades = ejbRegistroFicha.getTiposDiscapacidad();//Tipos de discapacidad
            if(resDiscapacidades.getCorrecto()==true){rol.setTipoDiscapacidads(resDiscapacidades.getValor());}
            else {mostrarMensajeResultadoEJB(resDiscapacidades);}
            ResultadoEJB<List<Ocupacion>> resOcupaciones= ejbRegistroFicha.getOcupaciones();//Ocupaciones
            if(resOcupaciones.getCorrecto()==true){rol.setOcupacions(resOcupaciones.getValor());}
            else {mostrarMensajeResultadoEJB(resOcupaciones);}
            ResultadoEJB<List<Escolaridad>> resEscolaridades = ejbRegistroFicha.getEscolaridad();//Escolaridades
            if(resEscolaridades.getCorrecto()==true){rol.setEscolaridads(resEscolaridades.getValor());}
            else {mostrarMensajeResultadoEJB(resEscolaridades);}
            ResultadoEJB<List<LenguaIndigena>> resLI = ejbRegistroFicha.getLenguasIndigenas();// Lenguas indigenas
            if(resLI.getCorrecto()==true){rol.setLenguaIndigenas(resLI.getValor());}
            else {mostrarMensajeResultadoEJB(resLI);}
            ResultadoEJB<List<MedioDifusion>> resMedDif = ejbRegistroFicha.getMediosDifusion(); // Medios de difusion
            if(resMedDif.getCorrecto()==true){rol.setMedioDifusions(resMedDif.getValor());}
            else {mostrarMensajeResultadoEJB(resMedDif);}
            ResultadoEJB<List<EspecialidadCentro>> resEspecialidad = ejbRegistroFicha.getEspecialidades(); //Especialidad Centro
            if(resEspecialidad.getCorrecto()==true){rol.setEspecialidadCentros(resEspecialidad.getValor());}
            else {mostrarMensajeResultadoEJB(resEspecialidad);}
            ResultadoEJB<List<Sistema>> resSistema = ejbRegistroFicha.getSistemas(); //Sistemas
            if(resSistema.getCorrecto()==true){rol.setSistemas(resSistema.getValor());}
            else {mostrarMensajeResultadoEJB(resSistema);}
            getPaises();
            getEstadosMexico();
            getAreasAcadémicas();
            getGeneros();
        }catch (Exception e){mostrarExcepcion(e);}

    }






    //Obtiene un registro
    public void getRegistro(){
        try{
            bloqTabs();
            rol.setTab1(false);
            rol.setStep(0);
            if(rol.getPersonaD().getEcontrado()==true){
                ResultadoEJB<List<Estado>> resEstadosO = ejbRegistroFicha.getEstadosbyPais(rol.getPersonaD().getPaisOr());
                rol.setEstadosOr(resEstadosO.getValor());
                ResultadoEJB<List<Municipio>> resMunicipiosO = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getPersonaD().getPersona().getEstado());
                rol.setMunicipiosOr(resMunicipiosO.getValor());
                rol.getPersonaD().getPersona().setMunicipio(rol.getPersonaD().getPersona().getMunicipio());
                ResultadoEJB<List<Localidad>> resLocalidades = ejbRegistroFicha.getLocalidadByMunicipio(rol.getPersonaD().getPersona().getEstado(),rol.getPersonaD().getPersona().getMunicipio());
                if(resLocalidades.getCorrecto()==true){rol.setLocalidadsOr(resLocalidades.getValor());}
                //Se busca datos medicos y medio de comunicación
                ResultadoEJB<DtoAspirante.MedicosR> resDM = ejbRegistroFicha.getDatosMedicosbyPersona(rol.getPersonaD());
                ResultadoEJB<MedioComunicacion> resMedC = ejbRegistroFicha.getMedioCbyPersona(rol.getPersonaD().getPersona());
                if(resDM.getValor().getEcontrado() ==true & resMedC.getCorrecto()==true){
                    rol.setDmedico(resDM.getValor());
                    rol.getPersonaD().setMedioComunicacion(resMedC.getValor());
                }
            }
            else {
                ResultadoEJB<List<Estado>> resEstadosO = ejbRegistroFicha.getEstadosbyPais(rol.getPersonaD().getPaisOr());
                rol.setEstadosOr(resEstadosO.getValor());
                ResultadoEJB<List<Municipio>> resMunicipiosO = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getPersonaD().getPersona().getEstado());
                rol.setMunicipiosOr(resMunicipiosO.getValor());
                rol.getPersonaD().getPersona().setMunicipio(resMunicipiosO.getValor().get(0).getMunicipioPK().getClaveMunicipio());
                ResultadoEJB<List<Localidad>> resLocalidades = ejbRegistroFicha.getLocalidadByMunicipio(rol.getPersonaD().getPersona().getEstado(),rol.getPersonaD().getPersona().getMunicipio());
                if(resLocalidades.getCorrecto()==true){rol.setLocalidadsOr(resLocalidades.getValor());}
                rol.setDmedico(new DtoAspirante.MedicosR(new DatosMedicos(),new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR,false));

            }

        }catch (Exception e){mostrarExcepcion(e);}
    }

    //Usar domicilio residencia como procedencia
    public void usarDireccionResidencia() {
        if (rol.getDdomicilios().getIgualD().equals(Boolean.TRUE)) {
            rol.setMunicipiosPo(rol.getMunicipiosDo());
            rol.setAsentamientosPo(rol.getAsentamientosDo());
            rol.getDdomicilios().getDomicilio().setCalleProcedencia(rol.getDdomicilios().getDomicilio().getCalle());
            rol.getDdomicilios().getDomicilio().setNumeroProcedencia(rol.getDdomicilios().getDomicilio().getNumero());
            rol.getDdomicilios().getDomicilio().setEstadoProcedencia(rol.getDdomicilios().getDomicilio().getIdEstado());
            rol.getDdomicilios().getDomicilio().setMunicipioProcedencia(rol.getDdomicilios().getDomicilio().getIdMunicipio());
            rol.getDdomicilios().getDomicilio().setAsentamientoProcedencia(rol.getDdomicilios().getDomicilio().getIdAsentamiento());
        }
    }
    public void comprabarEncuesta(@NonNull EncuestaAspirante resultados){
        if(rol.getEncuesta().getEcontrado()){
            Integer tt=0;
            if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR2tipoLenguaIndigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR3comunidadIndigena()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR4programaBienestar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR5ingresoMensual()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR6dependesEconomicamnete()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR7ingresoFamiliar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR8primerEstudiar()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR9nivelMaximoEstudios()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR10numeroDependientes()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR11situacionEconomica()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR12hijoPemex()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR13utxjPrimeraOpcion()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR14examenAdmisionOU()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR15medioImpacto()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR16segundaCarrera()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR17Alergia()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR18padecesEnfermedad()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR19tratamientoMedico()!= null){tt=tt+1;}
            if(rol.getEncuesta().getEncuestaAspirante().getR20Hijos()!=null){
                if(rol.getEncuesta().getEncuestaAspirante().getR20Hijos().equals("No")){ tt=tt+1;
                }else if(rol.getEncuesta().getEncuestaAspirante().getR20Hijos().equals("Si")){
                    if(rol.getEncuesta().getEncuestaAspirante().getR21noHijos()!=null){tt=tt+1;}
                }
            }
            if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("Si") && tt==20){
                rol.setFinalizado(Boolean.TRUE);
            }else if(rol.getEncuesta().getEncuestaAspirante().getR1Lenguaindigena().equals("No") && tt==19){
                rol.setFinalizado(Boolean.TRUE);
            }else{
                rol.setFinalizado(Boolean.FALSE);
            }
        }
    }

    public void getPersonaByCurp(){
        try{
            if(rol.getPersonaD().getPersona().getCurp()==null){return;}
            ResultadoEJB<Persona> getPersona = ejbRegistroFicha.getPersonabyCurp(rol.getPersonaD().getPersona().getCurp());
            if(getPersona.getCorrecto()==true){
                rol.getPersonaD().setPersona(getPersona.getValor());
            }else {mostrarMensajeResultadoEJB(getPersona);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    public void bloqTabs(){
        rol.setTab1(true);
        rol.setTab2(true);
        rol.setTab3(true);
        rol.setTab4(true);
        rol.setTab5(true);
        rol.setTab6(true);
        rol.setTab7(true);
    }

    /*
    Guarda persona
     */
    public void savePersona(){
        try{
            ResultadoEJB<DtoAspirante.PersonaR> resPersona = ejbRegistroFicha.operacionesPersonaR(rol.getPersonaD());
            if(resPersona.getCorrecto()){
                rol.setPersonaD(resPersona.getValor());
                rol.setTab2(false);
                rol.setStep(1);
                // comprobarPaso();
                mostrarMensajeResultadoEJB(resPersona);
            }
            else {mostrarMensajeResultadoEJB(resPersona);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Guarda datos medicos y medios de comunicación
     */
    public void  saveDatosMedicosyComunicacion(){
        try {
            ResultadoEJB<DtoAspirante.PersonaR> resMC = ejbRegistroFicha.operacionesMedioC(rol.getPersonaD());
            //Datos medicos
            ResultadoEJB<DtoAspirante.MedicosR> resSaveDm = ejbRegistroFicha.operacionesDatosMedicos(rol.getDmedico(),rol.getPersonaD());
            if(resMC.getCorrecto()==true & resSaveDm.getCorrecto()==true){
                //saveAspirante();
                rol.getPersonaD().setMedioComunicacion(resMC.getValor().getMedioComunicacion());
                rol.setDmedico(resSaveDm.getValor());
                mostrarMensajeResultadoEJB(resMC);
                mostrarMensajeResultadoEJB(resSaveDm);
                rol.setTab3(false);
                rol.setStep(2);
                getAspiranteAnterior();
                getAspirante();
                getDomicilio();
            }else { mostrarMensajeResultadoEJB(resMC);
                mostrarMensajeResultadoEJB(resSaveDm);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene el registro del aspirante anterior (Nuevo Ingreso)
     */
    public void getAspiranteAnterior(){
        try{
            ResultadoEJB<DtoAspirante.AspiranteR> resAspiranteA= ejbRegistroIng.getAspiranteAnterior(rol.getPersonaD().getPersona());
            if(resAspiranteA.getCorrecto()){
                rol.setAspiranteAnterior(resAspiranteA.getValor());
            }else {mostrarMensajeResultadoEJB(resAspiranteA);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene aspirante sin folio
     */
    public void getAspirante(){
        try{
            ResultadoEJB<DtoAspirante.AspiranteR> resAspiranteNuevo = ejbRegistroIng.getAspirantebyPersona(rol.getPersonaD().getPersona(),rol.getProcesosInscripcion());
            if(resAspiranteNuevo.getCorrecto()){
                if(resAspiranteNuevo.getValor().getEcontrado().equals(false)){
                    getTipoAspirante();
                    Aspirante aspiranteA = new Aspirante();
                    aspiranteA.setIdPersona(rol.getPersonaD().getPersona());
                    aspiranteA.setIdProcesoInscripcion(rol.getProcesosInscripcion());
                    aspiranteA.setTipoAspirante(rol.getAspirante().getTipo());
                    aspiranteA.setEstatus(false);
                    aspiranteA.setFechaRegistro(new Date());
                    DtoAspirante.AspiranteR dtoAspirante = new DtoAspirante.AspiranteR(aspiranteA,rol.getAspirante().getTipo(),rol.getProcesosInscripcion(), Operacion.PERSISTIR,false);
                    rol.setAspirante(dtoAspirante);
                    ResultadoEJB<DtoAspirante.AspiranteR> resAspirante = ejbRegistroFicha.operacionesAspiranteR(rol.getAspirante(),rol.getPersonaD().getPersona());
                    if(resAspirante.getCorrecto()==true){
                        rol.setAspirante(resAspirante.getValor());
                        mostrarMensajeResultadoEJB(resAspirante);
                    }else {mostrarMensajeResultadoEJB(resAspirante);}
                }else {
                    rol.setAspirante(resAspiranteNuevo.getValor());
                }
            }else {mostrarMensajeResultadoEJB(resAspiranteNuevo);}

        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    public void getDomicilio (){
        try{
            ResultadoEJB<DtoAspirante.DomicilioR> resDomicilio = ejbRegistroFicha.getDomiciliobyAspirante(rol.getAspirante().getAspirante());
            if(resDomicilio.getCorrecto()){
                rol.setDdomicilios(resDomicilio.getValor());
                getMunicipiosResi();
                getAsentamientoDom();
                getMunicipioProc();
                getAsentamientosProc();
            }
            else {
                //Busca los datos del domicilio de su registro anterior
                ResultadoEJB<DtoAspirante.DomicilioR> resDomicilioA = ejbRegistroFicha.getDomiciliobyAspirante(rol.getAspiranteAnterior().getAspirante());
                if(resDomicilioA.getCorrecto() & resDomicilioA.getValor().getEcontrado().equals(true)){
                    //Crea los datos del domicilio
                    rol.setDdomicilios(resDomicilioA.getValor());
                    rol.getDdomicilios().setOperacion(Operacion.PERSISTIR);
                    rol.getDdomicilios().setIgualD(false);
                    rol.getDdomicilios().setEcontrado(false);
                    ResultadoEJB<DtoAspirante.DomicilioR> resDom = ejbRegistroFicha.operacionesDomicilioR(rol.getDdomicilios(),rol.getAspirante().getAspirante());
                    if(resDom.getCorrecto()){
                        rol.setDdomicilios(resDom.getValor());
                        getMunicipiosResi();
                        getAsentamientoDom();
                        getMunicipioProc();
                        getAsentamientosProc();
                    }else {mostrarMensajeResultadoEJB(resDom);}
                }
            }


        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Guarda los datos de domicilio
     */
    public void  saveDomicilio(){
        try{
            ResultadoEJB<DtoAspirante.DomicilioR> resDom = ejbRegistroFicha.operacionesDomicilioR(rol.getDdomicilios(),rol.getAspirante().getAspirante());
            if(resDom.getCorrecto()==true){
                rol.setDdomicilios(resDom.getValor());
                rol.setTab4(false);
                rol.setStep(3);
                getDatosFamiliares();
                mostrarMensajeResultadoEJB(resDom);
            }else {mostrarMensajeResultadoEJB(resDom);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Obtiene los datos familiares
     */
    public void getDatosFamiliares (){
        try{
            ResultadoEJB<DtoAspirante.FamiliaresR> resDatosFamiliares = ejbRegistroFicha.getFamiliaresR(rol.getAspirante().getAspirante());
            if(resDatosFamiliares.getCorrecto()){
                rol.setDfamiliares(resDatosFamiliares.getValor());
                rol.setTutor(resDatosFamiliares.getValor().getTutorR());
                getMunicipiosTutor();
                getAsentamientoTutor();
            }else {
                //Busca los datos familiares  del registro anterior
                ResultadoEJB<DtoAspirante.FamiliaresR> resDatosFamiliaresA = ejbRegistroFicha.getFamiliaresR(rol.getAspiranteAnterior().getAspirante());
                if(resDatosFamiliaresA.getCorrecto()){
                    rol.setDfamiliares(resDatosFamiliaresA.getValor());
                    rol.getDfamiliares().setOperacion(Operacion.PERSISTIR);
                    rol.getDfamiliares().setEcontrado(false);
                    ResultadoEJB<DtoAspirante.FamiliaresR> resFam = ejbRegistroFicha.operacionesFamiliares(rol.getDfamiliares(), rol.getAspirante().getAspirante());
                    if(resFam.getCorrecto()){
                        rol.setDfamiliares(resFam.getValor());
                        rol.setTutor(resDatosFamiliares.getValor().getTutorR());
                        getMunicipiosTutor();
                        getAsentamientoTutor();
                    }else {mostrarMensajeResultadoEJB(resFam);}
                }else {mostrarMensajeResultadoEJB(resDatosFamiliaresA);}
            }

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Guarda tutor familiar y datos familiares
     */
    public void  saveDatosFamiliares(){
        try{
            //Se guarda primero al tutor familiar
            ResultadoEJB<DtoAspirante.TutorR> resTutor = ejbRegistroFicha.operacionesTutorA(rol.getTutor());
            if(resTutor.getCorrecto()==true){
                rol.setTutor(resTutor.getValor());
                ResultadoEJB<DtoAspirante.FamiliaresR> resDatosF = ejbRegistroFicha.operacionesFamiliares(rol.getDfamiliares(),rol.getAspirante().getAspirante());
                if(resDatosF.getCorrecto()==true){
                    rol.setDfamiliares(resDatosF.getValor());
                    rol.setTab5(false);
                    rol.setStep(4);
                    getDatosAcademicos();
                    mostrarMensajeResultadoEJB(resDatosF);}
            }else {mostrarMensajeResultadoEJB(resTutor);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /**
     * Obtiene los datos academicos del aspirante
     */

    public void getDatosAcademicos (){
        try{
            ResultadoEJB<DtoAspirante.AcademicosR> resDAcademicos = ejbRegistroIng.getAcademicos(rol.getAspirante().getAspirante(),rol.getEsTipo().getEstudianteCE());
            if(resDAcademicos.getCorrecto()){
                rol.setDacademicos(resDAcademicos.getValor());
                rol.getDacademicos().setOperacion(Operacion.ACTUALIZAR);
                getMunicipiosIems();
                getLocalidadIems();
                getIemesByLocalidad();
                getPEpObyAreaA();
                getPEsObyAreaA();
                getUniversidades();
                getUniversidadEgreso();
                getUniversidadEgresoAspirante();
            }else {
                //Busca los datos académicos del registro anterior
                ResultadoEJB<DtoAspirante.AcademicosR> resDAcademicosA = ejbRegistroIng.getAcademicos(rol.getAspiranteAnterior().getAspirante(),rol.getEsTipo().getEstudianteCE());
                if(resDAcademicosA.getCorrecto()){
                    rol.setDacademicos(resDAcademicosA.getValor());
                    rol.getDacademicos().setOperacion(Operacion.PERSISTIR);
                    ResultadoEJB<DtoAspirante.AcademicosR> resDtoA= ejbRegistroFicha.operacionesAcademicos(rol.getDacademicos(),rol.getAspirante().getAspirante());
                    if(resDtoA.getCorrecto()){
                        rol.setDacademicos(resDtoA.getValor());
                        getMunicipiosIems();
                        getLocalidadIems();
                        getIemesByLocalidad();
                        getPEpObyAreaA();
                        getPEsObyAreaA();
                        getUniversidades();
                        getUniversidadEgreso();
                        getUniversidadEgresoAspirante();
                    }else {mostrarMensajeResultadoEJB(resDtoA);}

                }else {mostrarMensajeResultadoEJB(resDAcademicosA);}
            }

        }catch (Exception e){mostrarExcepcion(e);}
    }


    /**
     * Obtiene la universidad de egreso del aspirante
     */
    public void getUniversidadEgresoAspirante (){
        try{
            ResultadoEJB<UniversidadEgresoAspirante> resUE = ejbRegistroIng.getUniversidadbyAspirante(rol.getAspirante().getAspirante());
            if(resUE.getCorrecto()){
                //Existe registro
                rol.setUniversidadEgresoAspirante(resUE.getValor());
            }else {
                //No existe registro (Lo agrega)
                ResultadoEJB<UniversidadEgresoAspirante> resOperU = ejbRegistroIng.operacionUniversidadesE(rol.getUniversidadEgreso(),rol.getAspirante().getAspirante(), Operacion.PERSISTIR);
                if(resOperU.getCorrecto()){
                    rol.setUniversidadEgresoAspirante(resOperU.getValor());
                }else {mostrarMensajeResultadoEJB(resOperU);}
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Guarda los datos académicos del aspirante
     */
    public void saveDatosAcademicos(){
        try{
            ResultadoEJB<DtoAspirante.AcademicosR> resDtoA= ejbRegistroFicha.operacionesAcademicos(rol.getDacademicos(),rol.getAspirante().getAspirante());
            if(resDtoA.getCorrecto()){
                rol.setDacademicos(resDtoA.getValor());
                rol.setTab6(false);
                rol.setStep(5);
                getEncuesta();
                mostrarMensajeResultadoEJB(resDtoA);
            }
            else {mostrarMensajeResultadoEJB(resDtoA);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la encuesta del aspirante
     */
    public void  getEncuesta (){
        try{
            ResultadoEJB<DtoAspirante.EncuestaR> resEncuesta = ejbRegistroFicha.getEncuesta(rol.getAspirante().getAspirante());
            if(resEncuesta.getCorrecto()){
                rol.setEncuesta(resEncuesta.getValor());
            }else {rol.setEncuesta(resEncuesta.getValor());}
        }catch (Exception e){mostrarExcepcion(e);}
    }



    /**
     * Guarda encuesta
     * @param event
     */
    public void saveEncuesta(ValueChangeEvent event) {
        Integer numeroP=0;
        String valor="";
        //System.out.println("valor"+event.getComponent().getId());
        switch (event.getComponent().getId()) {
            case "p1": numeroP=1; valor=event.getNewValue().toString();  break;
            case "li": numeroP=2;
                LenguaIndigena li=(LenguaIndigena)event.getNewValue();
                if(li.getIdLenguaIndigena()!=null){
                    valor=li.getIdLenguaIndigena().toString();
                }
                break;
            case "p3": numeroP=3; valor=event.getNewValue().toString(); break;
            case "p4": numeroP=4; valor=event.getNewValue().toString(); break;
            case "p5": numeroP=5; valor=event.getNewValue().toString(); break;
            case "p6": numeroP=6; valor=event.getNewValue().toString(); break;
            case "p7": numeroP=7; valor=event.getNewValue().toString(); break;
            case "p8": numeroP=8; valor=event.getNewValue().toString(); break;
            case "p9": numeroP=9; valor=event.getNewValue().toString(); break;
            case "p10": numeroP=10; valor=event.getNewValue().toString();  break;
            case "p11": numeroP=11; valor=event.getNewValue().toString();  break;
            case "p12": numeroP=12; valor=event.getNewValue().toString();  break;
            case "p13": numeroP=13; valor=event.getNewValue().toString();  break;
            case "p14": numeroP=14; valor=event.getNewValue().toString();  break;
            case "md": numeroP=15;
                MedioDifusion md=(MedioDifusion)event.getNewValue();
                if(md.getIdMedioDifusion()!=null){
                    valor=md.getIdMedioDifusion().toString();
                }
                break;
            case "p16": numeroP=16; valor=event.getNewValue().toString();  break;
            case "p17": numeroP=17; valor=event.getNewValue().toString();  break;
            case "p18": numeroP=18; valor=event.getNewValue().toString();  break;
            case "p19": numeroP=19; valor=event.getNewValue().toString();  break;
            case "p20": numeroP=20; valor=event.getNewValue().toString();  break;
            case "p21": numeroP=21; valor=event.getNewValue().toString();  break;


        }
        ResultadoEJB<DtoAspirante.EncuestaR> rejb =ejbRegistroFicha.operacionesEncuestaR(rol.getAspirante().getAspirante(),valor,numeroP);
        if(!rejb.getCorrecto()){ mostrarMensajeResultadoEJB(rejb);return;}
        rol.setEncuesta(rejb.getValor());
        comprabarEncuesta(rol.getEncuesta().getEncuestaAspirante());
        if(rol.getFinalizado()==true){
            if(rol.getAspirante().getAspirante().getFolioAspirante()==null){
                rol.setFolioAspirante(new Integer(0));
                ResultadoEJB<Integer> resFolio = ejbRegistroIng.generarFolio(rol.getProcesosInscripcion());
                if(resFolio.getCorrecto()==true){
                    rol.setFolioAspirante(resFolio.getValor());
                    rol.getAspirante().getAspirante().setFolioAspirante(rol.getFolioAspirante());
                    rol.getAspirante().getAspirante().setEstatus(false);
                    rol.getAspirante().setOperacion(Operacion.ACTUALIZAR);
                    ResultadoEJB<DtoAspirante.AspiranteR> resActuaA = ejbRegistroIng.operacionesAspiranteR(rol.getAspirante(),rol.getPersonaD().getPersona(),rol.getProcesosInscripcion());
                    if(resActuaA.getCorrecto()==true){
                        rol.setAspirante(resActuaA.getValor());
                        rol.setTab7(false);
                        rol.setStep(6);
                        //getRegistro();
                        //comprobarPaso();
                    }
                }else {
                    mostrarMensajeResultadoEJB(resFolio);
                }
            }
            rol.setTab7(false);
            rol.setStep(6);
            //getRegistro();
            //comprobarPaso();


        }else {
            rol.setEncuesta(rejb.getValor());
            //getRegistro();
            //comprobarPaso();
        }
    }

    /*
    Genera la ficha de admisión
     */
    public void downloadFichaAdmin() throws IOException, DocumentException {
        try{
            ResultadoEJB<Boolean> resFicha = ejbRegistroIng.generarFicha(String.valueOf(rol.getEsTipo().getEstudianteCE().getMatricula()),rol.getPersonaD().getPersona(),rol.getDacademicos().getAcademicos(),rol.getUniversidadEgreso(),rol.getDdomicilios().getDomicilio(),rol.getAspirante().getAspirante(),rol.getPersonaD().getMedioComunicacion(),"Alumno");
            if(resFicha.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resFicha);
            }
            else {mostrarMensajeResultadoEJB(resFicha);}
        }catch (Exception e){mostrarExcepcion(e);}

    }

    /*
    Valida ficha de admisión
     */
    public void validarFicha (){
        try{
            ResultadoEJB<DtoAspirante.AspiranteR> res = ejbRegistroIng.validaAspirante(rol.getAspirante());
            if(res.getCorrecto()){
                mostrarMensajeResultadoEJB(res);
            }else {mostrarMensajeResultadoEJB(res);}


        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
   Obtiene el tipo de aspirante de nuevo ingreso
    */
    public void getTipoAspirante(){
        try{
            ResultadoEJB<TipoAspirante> resTipoA = ejbRegistroIng.getTipoAspiranteIng();
            if(resTipoA.getCorrecto()==true){rol.getAspirante().setTipo(resTipoA.getValor());}
            else {mostrarMensajeResultadoEJB(resTipoA);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Carga los paises (Nacionalidad)
     */
    public void getPaises(){
        try{
            ResultadoEJB<List<Pais>> resPaises = ejbRegistroFicha.getPaises();
            if(resPaises.getCorrecto()==true){
                rol.setPaisesN(resPaises.getValor());
            }else {mostrarMensajeResultadoEJB(resPaises);}
        }catch (Exception e){ mostrarExcepcion(e); }
    }
    /*
    Carga lista de estados segun el país que se selecciono
     */
    public void getEstadosByPais(Pais paisSeleccionado){
        try{
            ResultadoEJB<List<Estado>> resEstados = ejbRegistroFicha.getEstadosbyPais(paisSeleccionado);
            if(resEstados.getCorrecto()==true){
                //Estados segun el pais (Nacimiento)
                rol.setEstadosOr(resEstados.getValor());
            }else {mostrarMensajeResultadoEJB(resEstados);}
        }catch (Exception e){mostrarExcepcion(e);}
    }


    /*
        Obtiene lista de estados de mexico
     */
    public void getEstadosMexico(){
        try{
            //Obtiene el pais(Mexico)
            ResultadoEJB<Pais> resPais = ejbRegistroFicha.getPaisbyClave(42);
            if(resPais.getCorrecto()==true){
                //Obtiene la lista de estados de México
                ResultadoEJB<List<Estado>> resEstados = ejbRegistroFicha.getEstadosbyPais(resPais.getValor());
                if(resEstados.getCorrecto()==true){
                    rol.setEstadosOr(resEstados.getValor());//Estados Nacimiento
                    rol.setEstadosDo(resEstados.getValor());//Estados Domicilio
                    rol.setEstadosPo(resEstados.getValor());//Estados Procedencia
                    rol.setEstadosTt(resEstados.getValor()); //Estado Tutor
                    rol.setEstadosIe(resEstados.getValor()); //Estados Iems
                }else {mostrarMensajeResultadoEJB(resEstados);}
            }else {mostrarMensajeResultadoEJB(resPais);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene lista de municipios segun el estado de naciemiento
    */
    public void getMunicipioNac(){
        try {
            ResultadoEJB<List<Municipio>> resMunicipio = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getPersonaD().getPersona().getEstado());
            if(resMunicipio.getCorrecto()==true){
                rol.setMunicipiosOr(resMunicipio.getValor());
            }else {mostrarMensajeResultadoEJB(resMunicipio);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene los municipios segun el estado de Domicilio de residencia seleccionado
     */
    public void getMunicipiosResi(){
        try{
            ResultadoEJB<List<Municipio>> resMunicipio = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getDdomicilios().getDomicilio().getIdEstado());
            if(resMunicipio.getCorrecto()==true){rol.setMunicipiosDo(resMunicipio.getValor());}
            else {mostrarMensajeResultadoEJB(resMunicipio);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la lista de municipios segun el estado de Procedencia seleccionado
     */
    public void getMunicipioProc (){
        try{
            ResultadoEJB<List<Municipio>> resMunicicpio = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getDdomicilios().getDomicilio().getEstadoProcedencia());
            if(resMunicicpio.getCorrecto()==true){rol.setMunicipiosPo(resMunicicpio.getValor());}
            else {mostrarMensajeResultadoEJB(resMunicicpio);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la lista de municipios segun el estado de Tutor seleccionado
     */
    public void  getMunicipiosTutor(){
        try{
            ResultadoEJB<List<Municipio>> resMunicipios = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getDfamiliares().getTutorR().getTutorFamiliar().getEstado());
            if(resMunicipios.getCorrecto()==true){
                rol.setMunicipiosTt(resMunicipios.getValor());
            }else {mostrarMensajeResultadoEJB(resMunicipios);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene lista de municipios segun el estado del Iems seleccionado
     */
    public void getMunicipiosIems(){
        try{
            ResultadoEJB<List<Municipio>> resMunicipio = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getDacademicos().getEstado().getIdestado());
            if(resMunicipio.getCorrecto()==true){rol.setMunicipiosIe(resMunicipio.getValor());}
            else {mostrarMensajeResultadoEJB(resMunicipio);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la lista de localidades segun
     */
    public void getLocalidadNaci(){
        try{

            ResultadoEJB<List<Localidad>> resLocalidad = ejbRegistroFicha.getLocalidadByMunicipio(rol.getPersonaD().getPersona().getEstado(),rol.getPersonaD().getPersona().getMunicipio());
            if(resLocalidad.getCorrecto()==true){rol.setLocalidadsOr(resLocalidad.getValor());}
            else {mostrarMensajeResultadoEJB(resLocalidad);}
        }catch (Exception e){ mostrarExcepcion(e);}
    }
    /*
    Obtiene la listades segun el municipio seleccionado para Iems
     */
    public void getLocalidadIems(){
        try{
            ResultadoEJB<List<Localidad>> resLocaldad = ejbRegistroFicha.getLocalidadByMunicipio(rol.getDacademicos().getEstado().getIdestado(),rol.getDacademicos().getMunicipio().getMunicipioPK().getClaveMunicipio());
            if(resLocaldad.getCorrecto()==true){rol.setLocalidadsIe(resLocaldad.getValor());}
            else {mostrarMensajeResultadoEJB(resLocaldad);}
        }catch (Exception e){}
    }

    /**
     * Obtiene la lista de universidades
     */
    public void getUniversidades (){
        try{
            ResultadoEJB<List<UniversidadesUT>> resUniversidades = ejbRegistroIng.getUniversidades();
            if (resUniversidades.getCorrecto()){
                rol.setUniversidades(resUniversidades.getValor());
            }else {mostrarMensajeResultadoEJB(resUniversidades);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /**
     * Obtiene la universidad de egreso (UTXJ)
     */
    public void getUniversidadEgreso (){
        try{
            ResultadoEJB<UniversidadesUT> resUtxj = ejbRegistroIng.getUtxj();
            if(resUtxj.getCorrecto()){
                rol.setUniversidadEgreso(resUtxj.getValor());
            }else {mostrarMensajeResultadoEJB(resUtxj);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
        Obtiene la lista de asentamientos segun el estado y muncipio seleccionado del domicilio
     */
    public void getAsentamientoDom(){
        try{
            ResultadoEJB<List<Asentamiento>> resAsentamiento = ejbRegistroFicha.getAsentamientosbyMunicipio(rol.getDdomicilios().getDomicilio().getIdEstado(),rol.getDdomicilios().getDomicilio().getIdMunicipio());
            if(resAsentamiento.getCorrecto()==true){rol.setAsentamientosDo(resAsentamiento.getValor()); }
            else {mostrarMensajeResultadoEJB(resAsentamiento);}
        }catch (Exception e){}

    }
    /*
        Obtiene la lista de asentamientos segun el estado y municipio de Domicilio de residencia seleccionado
     */
    public void  getAsentamientosProc(){
        try{
            ResultadoEJB<List<Asentamiento>> resAsentamiento= ejbRegistroFicha.getAsentamientosbyMunicipio(rol.getDdomicilios().getDomicilio().getEstadoProcedencia(),rol.getDdomicilios().getDomicilio().getMunicipioProcedencia());
            if(resAsentamiento.getCorrecto()==true){rol.setAsentamientosPo(resAsentamiento.getValor());}
            else {mostrarMensajeResultadoEJB(resAsentamiento);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la lista de asentamientos de tutor
     */
    public void getAsentamientoTutor(){
        try{
            ResultadoEJB<List<Asentamiento>> resAentamiento= ejbRegistroFicha.getAsentamientosbyMunicipio(rol.getTutor().getTutorFamiliar().getEstado(),rol.getTutor().getTutorFamiliar().getMunicipio());
            if(resAentamiento.getCorrecto()==true){rol.setAsentamientosTt(resAentamiento.getValor());
            }
            else {mostrarMensajeResultadoEJB(resAentamiento);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    obtiene una lista de Iems segub el estado , municipio y localidad seleccionados
     */
    public  void getIemesByLocalidad(){
        try{
            ResultadoEJB<List<Iems>> resIems = ejbRegistroFicha.getIemsbyEstadoMunicipio(rol.getDacademicos().getEstado(),rol.getDacademicos().getMunicipio(),rol.getDacademicos().getLocalidad());
            if(resIems.getCorrecto() ==true){rol.setIemses(resIems.getValor());}
            else {mostrarMensajeResultadoEJB(resIems);}
        }catch (Exception e){ mostrarExcepcion(e);}
    }
    /*
      Obtiene una lista de areas acadeicas
     */
    public  void  getAreasAcadémicas(){
        try{
            ResultadoEJB<List<AreasUniversidad>> resAreasA = ejbRegistroFicha.getAreasAcademicas();
            if(resAreasA.getCorrecto() ==true){rol.setAreasAcademicas(resAreasA.getValor());}
            else {mostrarMensajeResultadoEJB(resAreasA);}
        }catch (Exception e){}
    }
    /*
    Obtiene una lista de programas educativos segun el area academica de la primera opcion
     */
    public void  getPEpObyAreaA(){
        try{
            ResultadoEJB<List<AreasUniversidad>> resPEpO = ejbRegistroIng.getPEIng();
            if(resPEpO.getCorrecto()==true){
                rol.setProgramasEducativosPo(resPEpO.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getDacademicos().getUniversidad1().getArea())).collect(Collectors.toList()));
            }
            else {mostrarMensajeResultadoEJB(resPEpO);}
        }catch (Exception e){ mostrarExcepcion(e);}
    }
    /*
    Obtiene una lista de programas educativos segun el area academica so seleccionada
     */
    public void  getPEsObyAreaA(){
        try{
            ResultadoEJB<List<AreasUniversidad>> resPEsO = ejbRegistroIng.getPEIng();
            if(resPEsO.getCorrecto()==true){rol.setProgramasEducativosSo(resPEsO.getValor());
                rol.setProgramasEducativosSo(resPEsO.getValor().stream().filter(t -> Objects.equals(t.getAreaSuperior(), rol.getDacademicos().getUniversidad2().getArea())).collect(Collectors.toList()));
            }
            else {mostrarMensajeResultadoEJB(resPEsO);}
        }catch (Exception e){}
    }

    public void  getGeneros (){
        try {
            ResultadoEJB<List<Generos>> resGeneros = ejbRegistroFicha.getGeneros();
            if(resGeneros.getCorrecto()==true){rol.setGeneros(resGeneros.getValor());}
            else {mostrarMensajeResultadoEJB(resGeneros);}
        }catch (Exception e){}
    }



    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registroFichaIng";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

}
