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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCitaAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroFichaAdmisionRolAspiranteIngSaiiut;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCitaInscripcionAspirante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroFichaIngenieria;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Named(value = "registroFichaAdmisionAspiranteIngSaiiut")
@ViewScoped
public class RegistroFichaAdmisionAspiranteIngSaiiut extends ViewScopedRol implements Desarrollable {
    @EJB private EjbPropiedades ep;
    @EJB private EjbRegistroFichaAdmision ejbRegistroFicha;
    @EJB private EjbRegistroFichaIngenieria ejbRegistroIng;
    @EJB private EjbCitaInscripcionAspirante ejbCita;
    @Inject
    LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    @Getter @Setter
    RegistroFichaAdmisionRolAspiranteIngSaiiut rol;
    @Getter @Setter String valor;

    @PostConstruct
    public void init(){
        if(logonMB.getUsuarioTipo().equals(UsuarioTipo.ASPIRANTE_ING) || logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE)) {cargado =true;}
        else {cargado= false;return;}
        setVistaControlador(ControlEscolarVistaControlador.REGISTRO_FICHA_ASPIRANTE_ING_SAIIUT);
        ResultadoEJB<DtoAspiranteIng> resAcceso = ejbRegistroIng.verificarAcceso2(logonMB.getEmail(),logonMB.getUsuarioTipo());
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
        ResultadoEJB<DtoAspiranteIng> resValidacion = ejbRegistroIng.verificarAcceso2(logonMB.getEmail(),logonMB.getUsuarioTipo());
        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
        rol = new RegistroFichaAdmisionRolAspiranteIngSaiiut();
        if(resAcceso.getCorrecto()){tieneAcceso = true;rol.setAspiranteIng(resAcceso.getValor());}
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
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
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
        cargaDatos();
        bloqTabs();
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
            getUniversidades();

        }catch (Exception e){mostrarExcepcion(e);}

    }
    public void leerQR()throws IOException {
        if(rol.getFileCurp() != null){
            //Lee el QR
            Persona per = ejbRegistroFicha.leerCurp(rol.getFileCurp());
            if(per.getNombre() !=null){
                ResultadoEJB<DtoAspirante.PersonaR> resPersona =  ejbRegistroFicha.getPersonaR(per);
                if(resPersona.getCorrecto()==true){
                    rol.setPersonaD(resPersona.getValor());
                    //ResultadoEJB<DtoAspirante.General> resGen = ejbRegistroFicha.getDtoApiranteGeneral(resPersona.getValor().getPersona(),rol.getProcesosInscripcion());
                    //System.out.println("..."+ resGen.getValor());
                   // if(resGen.getCorrecto()){
                        //rol.setGeneral(resGen.getValor());
                        getRegistro();
                        mostrarMensajeResultadoEJB(resPersona);
                    //}else {mostrarMensajeResultadoEJB(resGen);}

                }else {mostrarMensajeResultadoEJB(resPersona);}
            }else {Messages.addGlobalError("El documento es incorrecto");}
        } else {
            Messages.addGlobalError("Es necesario seleccionar un archivo !");
        }
    }


    /*
    Verifica que exista un registro de la curp
     */
    public void verificaRegistro(){
        try{
            ResultadoEJB<Persona> resPersona =  ejbRegistroFicha.getPersonabyCurp(rol.getPersonaD().getPersona().getCurp());
            if(resPersona.getCorrecto()==true){
                rol.getPersonaD().setPersona(resPersona.getValor());
                if(rol.getPersonaD().getPersona().getIdpersona()==Integer.parseInt(rol.getPwdPer())){
                    ResultadoEJB<DtoAspirante.PersonaR> resPer= ejbRegistroFicha.getPersonaR(rol.getPersonaD().getPersona());
                    if(resPer.getCorrecto()==true){
                        rol.setPersonaD(resPer.getValor());
                        ResultadoEJB<DtoAspirante.PersonaR> resPersona2 =  ejbRegistroFicha.getPersonaR(resPersona.getValor());
                        if(resPersona2.getCorrecto()==true){    rol.setPersonaD(resPersona2.getValor());getRegistro();}
                        else {mostrarMensajeResultadoEJB(resPer);}
                    }else { mostrarMensajeResultadoEJB(resPer);}
                }
                else {mostrarMensajeResultadoEJB(ResultadoEJB.crearErroneo(3,new Persona(),"La contraseña ingresada es incorrecta."));init();}

            }
            else {mostrarMensajeResultadoEJB(resPersona);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    //Obtiene un registro
    public void getRegistro(){
        try{
            if(rol.getPersonaD().getEcontrado()==true){
                bloqTabs();
                rol.setTab2(false);
                rol.setTab3(false);
                rol.setStep(1);
                ResultadoEJB<List<Estado>> resEstadosO = ejbRegistroFicha.getEstadosbyPais(rol.getPersonaD().getPaisOr());
                rol.setEstadosOr(resEstadosO.getValor());
                ResultadoEJB<List<Municipio>> resMunicipiosO = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getPersonaD().getPersona().getEstado());
                rol.setMunicipiosOr(resMunicipiosO.getValor());
                rol.getPersonaD().getPersona().setMunicipio(rol.getPersonaD().getPersona().getMunicipio());
                ResultadoEJB<List<Localidad>> resLocalidades = ejbRegistroFicha.getLocalidadByMunicipio(rol.getPersonaD().getPersona().getEstado(),rol.getPersonaD().getPersona().getMunicipio());
               //Se busca datos medicos y medio de comunicación
                        ResultadoEJB<DtoAspirante.MedicosR> resDM = ejbRegistroFicha.getDatosMedicosbyPersona(rol.getPersonaD());
                        ResultadoEJB<MedioComunicacion> resMedC = ejbRegistroFicha.getMedioCbyPersona(rol.getPersonaD().getPersona());
                        if(resDM.getValor().getEcontrado() ==true & resMedC.getCorrecto()==true){
                            rol.setDmedico(resDM.getValor());
                            rol.setComunicacion(resMedC.getValor());
                            rol.getPersonaD().setMedioComunicacion(resMedC.getValor());
                            rol.setTab1(true);
                            rol.setTab4(false);
                            rol.setStep(3);
                            //Se busca Aspirante
                            ResultadoEJB<DtoAspirante.AspiranteR> resAspirante = ejbRegistroIng.getAspirantebyPersona(rol.getPersonaD().getPersona(),rol.getProcesosInscripcion());
                            if(resAspirante.getCorrecto()==true){
                                //Se verifica que haya entontrado al aspirante
                                if(resAspirante.getValor().getEcontrado()==false){saveAspirante();}
                                else {rol.setAspirante(resAspirante.getValor());}
                                //Se buscan datos de domicilio
                                ResultadoEJB<DtoAspirante.DomicilioR> resDomicilio = ejbRegistroFicha.getDomiciliobyAspirante(rol.getAspirante().getAspirante());
                                if(resDomicilio.getCorrecto()==true){
                                    //System.out.println("Municipio" + resDomicilio.getValor().getDomicilio().getIdMunicipio());
                                    rol.setDdomicilios(resDomicilio.getValor());
                                    getMunicipiosResi();
                                    getAsentamientoDom();
                                    getMunicipioProc();
                                    getAsentamientosProc();
                                    rol.setTab1(true);
                                    rol.setTab5(false);
                                    rol.setStep(4);
                                    //Busca datos familiares
                                    ResultadoEJB<DtoAspirante.FamiliaresR> resDatosFamiliares = ejbRegistroFicha.getFamiliaresR(rol.getAspirante().getAspirante());
                                    if(resDatosFamiliares.getCorrecto()==true){
                                        rol.setDfamiliares(resDatosFamiliares.getValor());
                                        rol.setTutor(resDatosFamiliares.getValor().getTutorR());
                                        //System.out.println("Tutor en busqueda->" + rol.getTutor());
                                        getMunicipiosTutor();
                                        getAsentamientoTutor();
                                        rol.setTab5(false);
                                        rol.setStep(5);
                                        ///Busca datos académicos
                                        ResultadoEJB<DtoAspirante.AcademicosR> resDAcademicos = ejbRegistroIng.getAcademicosbyAspirante(rol.getAspirante().getAspirante());
                                        ResultadoEJB<UniversidadEgresoAspirante> resUni = ejbRegistroIng.getUniversidadbyAspirante(rol.getAspirante().getAspirante());
                                        //Si el estudiante es egreseado de SAIIUT, se selecciona UTXJ como universidad de egreso
                                        if(rol.getAspiranteIng().getTipoAspirante().equals(AspiranteTipoIng.ASPPIRANTE_ING_OTRA_GENERACION)){ getUtxj(); }
                                        if(resUni.getCorrecto()){
                                            rol.setUniversidadEgresoAspirante(resUni.getValor());
                                            getUniversidadbyAspirante();}
                                        if(resDAcademicos.getCorrecto()){
                                            rol.setDacademicos(resDAcademicos.getValor());
                                            getMunicipiosIems();
                                            getLocalidadIems();
                                            getIemesByLocalidad();
                                            getPEpObyAreaA();
                                            getPEsObyAreaA();
                                            if(rol.getAspirante().getAspirante().getEstatus()==true){
                                                rol.setTab6(true);
                                                rol.setTab7(false);
                                                rol.setStep(6);
                                            }
                                            else {
                                                rol.setTab6(false);
                                                rol.setTab7(false);
                                                rol.setStep(6);
                                            }

                                            // Busca resultados de la encuesta del aspirante
                                            ResultadoEJB<DtoAspirante.EncuestaR> resEncuesta = ejbRegistroFicha.getEncuesta(rol.getAspirante().getAspirante());
                                            if(resEncuesta.getCorrecto()==true){
                                                rol.setEncuesta(resEncuesta.getValor());
                                                comprabarEncuesta(rol.getEncuesta().getEncuestaAspirante());
                                                if(rol.getEncuesta().getEcontrado()==true){
                                                    if(rol.getFinalizado()==true){
                                                        rol.setTab8(false);
                                                        rol.setStep(7);
                                                        //Busca los datos de la cita
                                                        getTramiteActivoCite();
                                                        diasNoDisponible();
                                                        getRegistroCita();
                                                        if(rol.getDtoCitaAspirante().getTieneCita().equals(Boolean.TRUE)){
                                                            rol.setTab9(false);
                                                            rol.setStep(8);
                                                        }else {
                                                            rol.setTab8(false);
                                                            rol.setStep(7);
                                                        }
                                                    }else {
                                                        rol.setTab7(false);
                                                        rol.setStep(6);
                                                    }
                                                }else {
                                                    rol.setTab7(false);
                                                    rol.setStep(6);
                                                }
                                            }else {
                                                //  mostrarMensajeResultadoEJB(resEncuesta);
                                            }

                                        }
                                        else {
                                            rol.setTab6(false);
                                            rol.setStep(5);
                                        }
                                    }else {
                                        rol.setTab5(false);
                                        rol.setStep(4);
                                    }
                                }
                                else {
                                    rol.setDdomicilios(new DtoAspirante.DomicilioR(new Domicilio(),false, Operacion.PERSISTIR,false));
                                    rol.setTab1(true);
                                    rol.setTab4(false);
                                    rol.setStep(3);
                                }
                            }else {mostrarMensajeResultadoEJB(resAspirante);}

                        }
                        else {
                            //System.out.println("No hay medio");
                            //rol.setDmedico(new DtoAspirante.MedicosR(new DatosMedicos(),new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR,false));
                            rol.setTab1(true);
                            rol.setTab2(false);
                            rol.setTab3(false);
                            rol.setStep(2);
                            // System.out.println("Paso -> " + rol.getStep() + "Tab " + rol.getTab3());
                        }
            }else {
                ResultadoEJB<List<Estado>> resEstadosO = ejbRegistroFicha.getEstadosbyPais(rol.getPersonaD().getPaisOr());
                rol.setEstadosOr(resEstadosO.getValor());
                ResultadoEJB<List<Municipio>> resMunicipiosO = ejbRegistroFicha.getMunicipiosbyClaveEstado(rol.getPersonaD().getPersona().getEstado());
                rol.setMunicipiosOr(resMunicipiosO.getValor());
                rol.getPersonaD().getPersona().setMunicipio(resMunicipiosO.getValor().get(0).getMunicipioPK().getClaveMunicipio());
                ResultadoEJB<List<Localidad>> resLocalidades = ejbRegistroFicha.getLocalidadByMunicipio(rol.getPersonaD().getPersona().getEstado(),rol.getPersonaD().getPersona().getMunicipio());
                if(resLocalidades.getCorrecto()==true){rol.setLocalidadsOr(resLocalidades.getValor());}
                rol.setDmedico(new DtoAspirante.MedicosR(new DatosMedicos(),new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR,false));
                rol.setStep(1);
                rol.setTab1(true);
                rol.setTab2(false);
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
        rol.setTab2(true);
        rol.setTab2(true);
        rol.setTab7(true);
        rol.setTab8(true);
        rol.setTab9(true);
    }

    /*
    Guarda persona
     */
    public void savePersona(){
        try{
            ResultadoEJB<DtoAspirante.PersonaR> resPersona = ejbRegistroFicha.operacionesPersonaR(rol.getPersonaD());
            if(resPersona.getCorrecto()){
                rol.setPersonaD(resPersona.getValor());
                getRegistro();
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
                getRegistro();
            }else { mostrarMensajeResultadoEJB(resMC);
            mostrarMensajeResultadoEJB(resSaveDm);
            }
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Guarda al aspirante sin folio
     */
    public void saveAspirante(){
        try{
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
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }

    /*
    Guarda los datos de domicilio
     */
    public void  saveDomicilio(){
        try{
            ResultadoEJB<DtoAspirante.DomicilioR> resDom = ejbRegistroFicha.operacionesDomicilioR(rol.getDdomicilios(),rol.getAspirante().getAspirante());
            if(resDom.getCorrecto()==true){
                rol.setDdomicilios(resDom.getValor());
                getRegistro();
                //comprobarPaso();
                mostrarMensajeResultadoEJB(resDom);
            }else {mostrarMensajeResultadoEJB(resDom);}
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
                    getRegistro();
                    //comprobarPaso();
                    mostrarMensajeResultadoEJB(resDatosF);}
            }else {mostrarMensajeResultadoEJB(resTutor);}
        }catch (Exception e){
            mostrarExcepcion(e);
        }
    }
    /*
    Guarda los datos académicos del aspirante
     */
    public void saveDatosAcademicos(){
        try{
            rol.getDacademicos().setUniversidad2(rol.getDacademicos().getUniversidad1());
            rol.getDacademicos().setSistemaSo(rol.getDacademicos().getSistemaPo());
            rol.getDacademicos().getAcademicos().setSegundaOpcion(rol.getDacademicos().getAcademicos().getPrimeraOpcion());
            ResultadoEJB<DtoAspirante.AcademicosR> resDtoA= ejbRegistroFicha.operacionesAcademicos(rol.getDacademicos(),rol.getAspirante().getAspirante());
            if(resDtoA.getCorrecto()){
                rol.setDacademicos(resDtoA.getValor());
                //Guarda la universidad de egreso
                ResultadoEJB<UniversidadEgresoAspirante> resUni = ejbRegistroIng.operacionUniversidadesE(rol.getUniversidad(),rol.getAspirante().getAspirante(), Operacion.PERSISTIR);
                if(resUni.getCorrecto()){
                    rol.setUniversidadEgresoAspirante(resUni.getValor());
                    getRegistro();
                    //comprobarPaso();
                    mostrarMensajeResultadoEJB(resDtoA);
                }else {mostrarMensajeResultadoEJB(resUni);}

            }
            else {mostrarMensajeResultadoEJB(resDtoA);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
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
                    ResultadoEJB<DtoAspirante.AspiranteR> resActuaA = ejbRegistroIng.operacionesAspiranteR(rol.getAspirante(),rol.getPersonaD().getPersona(), rol.getProcesosInscripcion());
                    if(resActuaA.getCorrecto()==true){
                        rol.setAspirante(resActuaA.getValor());
                        getRegistro();
                        //comprobarPaso();
                    }
                }else {
                    mostrarMensajeResultadoEJB(resFolio);
                }
            }
            getRegistro();
            //comprobarPaso();


        }else {
            getRegistro();
            //comprobarPaso();
        }
    }

    public void getUniversidadbyAspirante(){
        try{
            ResultadoEJB<UniversidadesUT> resUni = ejbRegistroIng.getUniversidadbyAspirante(rol.getUniversidadEgresoAspirante());
            if(resUni.getCorrecto()){
                rol.setUniversidad(resUni.getValor());
            }else {mostrarMensajeResultadoEJB(resUni);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void verDialogo(){
        if(rol.getDialogEn()){
            Ajax.oncomplete("PF('modalRes').show();");
            rol.setDialogEn(Boolean.FALSE);
        }
    }

    /*
    Genera la ficha de admisión
     */
    public void downloadFichaAdmin() throws IOException, DocumentException {
        try{
            ResultadoEJB<Boolean> resFicha = ejbRegistroIng.generarFicha(rol.getAspiranteIng().getMatricula(),rol.getPersonaD().getPersona(),rol.getDacademicos().getAcademicos(),rol.getUniversidad(),rol.getDdomicilios().getDomicilio(),rol.getAspirante().getAspirante(),rol.getPersonaD().getMedioComunicacion(),"Alumno");
            if(resFicha.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resFicha);
            }
            else {mostrarMensajeResultadoEJB(resFicha);}
        }catch (Exception e){mostrarExcepcion(e);}

    }
    ///////////////////// Registro de cita para inscripcion ///////////////

    /*
    Obtiene el tramite escolar de inscripcion ingeniría activo
     */
    public void getTramiteActivoCite(){
        try{
            ResultadoEJB<TramitesEscolares> resTramite = ejbRegistroIng.getTramiteInscripcionActivo(rol.getProcesosInscripcion());
            if(resTramite.getCorrecto()){
                rol.setTramiteCita(resTramite.getValor());
            }else {mostrarMensajeResultadoEJB(resTramite);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la cita del aspirante según el tramite activo
     */
    public void getRegistroCita (){
        try{
            ResultadoEJB<DtoCitaAspirante> resCita = ejbCita.packAspiranteCita(rol.getAspirante().getAspirante().getFolioAspirante().toString(),rol.getTramiteCita(),rol.getProcesosInscripcion());
            if(resCita.getCorrecto()){
                rol.setDtoCitaAspirante(resCita.getValor());
            }else {mostrarMensajeResultadoEJB(resCita);}
        }catch (Exception e){mostrarExcepcion(e);}
    }
    /**
     * Busca los días que no estan disponibles
     * Los dias que no estan disponibles se sacan según el numero de citas que estan registradas por dia posible
     */
    public void diasNoDisponible(){
        try{
            List<Date> diasInvalidos= new ArrayList<>();
            Date today = new Date();
            rol.setInvalidDays(new ArrayList<>());
            rol.getInvalidDays().add(0);
            long oneDay = 24 * 60 * 60 * 1000;
            Date newDate= new Date(today.getTime() - (1 *oneDay));
            rol.setMinDay(newDate);
            //Busca la fechas posibles
            ResultadoEJB<List<Date>> resDiasIn = ejbCita.getListFechasPosibles(rol.getTramiteCita());
            if(resDiasIn.getCorrecto()==true){
                diasInvalidos = resDiasIn.getValor();
                //Se desabilitan fechas(No habrá servicio)
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //Date fecha1= sdf.parse("2020-08-21");
                //Date fecha2= sdf.parse("2020-08-22");
                //diasInvalidos.add(fecha1);diasInvalidos.add(fecha2);
                rol.setInvalidDates(diasInvalidos);
            }else {mostrarMensajeResultadoEJB(resDiasIn);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /**
     * Cambia la fecha de su cita
     */
    public void changeFecha(){
        try{
            ResultadoEJB<CitasAspirantes> resCita = ejbCita.getCitabyAspirante(rol.getDtoCitaAspirante().getAspirante(),rol.getTramiteCita());
            if(resCita.getCorrecto()==true){
                rol.getDtoCitaAspirante().setCitasAspirantes(resCita.getValor());
                rol.getDtoCitaAspirante().setOperacion(Operacion.ACTUALIZAR);
                rol.getDtoCitaAspirante().getCitasAspirantes().setStatus(EstatusCita.REAGENDADA.getLabel());
                ResultadoEJB<CitasAspirantes> resAc= ejbCita.operacionesCitaAspirante(rol.getDtoCitaAspirante());
                if(resAc.getCorrecto()==true){
                    rol.setCambiarFecha(Boolean.TRUE);
                    rol.setDis(Boolean.FALSE);
                    rol.getDtoCitaAspirante().setTieneCita(false);
                    rol.getDtoCitaAspirante().setOperacion(Operacion.PERSISTIR);
                    CitasAspirantes newCita = new CitasAspirantes();
                    CitasAspirantesPK pk = new CitasAspirantesPK();
                    pk.setIdTramite(rol.getTramiteCita().getIdTramite());
                    pk.setIdAspirante(rol.getDtoCitaAspirante().getAspirante().getIdAspirante());
                    newCita.setCitasAspirantesPK(pk);
                    rol.getDtoCitaAspirante().setCitasAspirantes(newCita);
                    mostrarMensajeResultadoEJB(resAc);
                }
                else {mostrarMensajeResultadoEJB(resAc);}

            }else {mostrarMensajeResultadoEJB(resCita);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Guarda cita
     */
    public void saveCita(){
        try{
            ResultadoEJB<Integer> resFolio= ejbCita.generarFolio(rol.getTramiteCita());
            if(resFolio.getCorrecto()==true){
                CitasAspirantesPK pk= new CitasAspirantesPK();
                pk.setIdAspirante(rol.getDtoCitaAspirante().getAspirante().getIdAspirante());
                pk.setIdTramite(rol.getTramiteCita().getIdTramite());
                CitasAspirantes cita = new CitasAspirantes();
                cita.setCitasAspirantesPK(pk);
                cita.setFechaCita(rol.getDtoCitaAspirante().getCitasAspirantes().getFechaCita());
                cita.setStatus(EstatusCita.CONFIRMADA.getLabel());
                rol.getDtoCitaAspirante().setOperacion(Operacion.PERSISTIR);
                cita.setFolioCita(resFolio.getValor());
                rol.getDtoCitaAspirante().setCitasAspirantes(cita);
                ResultadoEJB<CitasAspirantes> resCita = ejbCita.operacionesCitaAspirante(rol.getDtoCitaAspirante());
                if(resCita.getCorrecto()==true){
                    rol.getDtoCitaAspirante().setCitasAspirantes(resCita.getValor());
                    rol.setCambiarFecha(Boolean.TRUE);
                    rol.setDis(Boolean.TRUE);
                    ResultadoEJB<DtoAspirante.AspiranteR> resValida= ejbRegistroIng.validaAspirante(rol.getAspirante());
                    if(resValida.getCorrecto()){mostrarMensajeResultadoEJB(resValida);}
                    mostrarMensajeResultadoEJB(resCita);
                    getRegistro();
                }else {mostrarMensajeResultadoEJB(resCita);}
            }else {mostrarMensajeResultadoEJB(resFolio);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    /*
    Genera comprobante de cita
     */
    public void downloadComprobante() throws IOException, DocumentException {
        try{
            ResultadoEJB<Boolean> resFicha = ejbRegistroIng.generarComprobante(rol.getDtoCitaAspirante(),new Date(),rol.getTramiteCita());
            if(resFicha.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resFicha);
            }
            else {mostrarMensajeResultadoEJB(resFicha);}
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
    Obtiene la lista de universidades
     */
    public void getUniversidades(){
        try{
            ResultadoEJB<List<UniversidadesUT>> resUni = ejbRegistroIng.getUniversidades();
            if(resUni.getCorrecto()){
                rol.setUniversidades(resUni.getValor());
            }else {mostrarMensajeResultadoEJB(resUni);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Obtiene la universidad Utxj
     */
    public void getUtxj(){
        try{
            ResultadoEJB<UniversidadesUT> resUtxj = ejbRegistroIng.getUtxj();
            if(resUtxj.getCorrecto()){
                rol.setUniversidad(resUtxj.getValor());
            }else {mostrarMensajeResultadoEJB(resUtxj);}
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
            //System.out.println("Area seleccionada ->" + rol.getDacademicos().getUniversidad1());
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
            ResultadoEJB<List<AreasUniversidad>> resPEsO = ejbRegistroFicha.getProgramasE();
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
        String valor = "registroFichaAspirante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }

}
