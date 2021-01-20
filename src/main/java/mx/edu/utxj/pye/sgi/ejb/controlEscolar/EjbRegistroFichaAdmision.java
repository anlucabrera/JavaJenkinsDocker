package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.google.zxing.NotFoundException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import edu.mx.utxj.pye.seut.util.dto.Dto;
import edu.mx.utxj.pye.seut.util.preguntas.Tipo;
import jdk.nashorn.internal.ir.ReturnNode;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import nl.lcs.qrscan.core.QrPdf;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlbeans.impl.regex.REUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;

import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.transform.sax.SAXSource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.rmi.server.ExportException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ejb para el registro de fichas de admision para aspirantes
 * @author Taatisz :P
 */
@Stateless(name = "EjbRegistroFichaAdmision")
public class EjbRegistroFichaAdmision {
    @EJB Facade f;
    @EJB EjbPersonal ejbPersonal;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbEncuestaVocacional ejbEncuestaVocacional;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbAreasLogeo ejbAreasLogeo;

    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    /**
     * Verifica que el usuario autenticado sea de tipo aspirante
     *
     * @param tipoUsuarioAu
     * @return
     */
    public ResultadoEJB<Boolean> verficaAcceso(UsuarioTipo tipoUsuarioAu) {
        try {
            if (tipoUsuarioAu == null) {
                return ResultadoEJB.crearErroneo(2, new Boolean(false), "El tipo de usuario no debe ser nulo");
            }
            if (tipoUsuarioAu.getLabel().equals("ASPIRANTE")) {
                return ResultadoEJB.crearCorrecto(true, "Verificado como aspirante");
            } else {
                return ResultadoEJB.crearErroneo(3, new Boolean(false), "El usuario autenticado no es Aspirante");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbRegistroFichaAdmision.verificaEvento).", e, Boolean.class);
        }
    }
    /**
     * Verifica que el personal logueado sea de Servicios Escolares
     * @param clave Clave del trabajador logueado
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaSE(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAreaOperativa").orElse(10)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de servicios escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAdministracionEstudiantesSE.validaSE)", e, null);
        }

    }

    /**
     * Verifica que haya un evento aperturado para el registro de fichas de admision
     *
     * @return
     */
    public ResultadoEJB<EventoEscolar> verificaEvento() {
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REGISTRO_FICHAS_ADMISION);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbRegistroFichaAdmision.verificaEvento).", e, EventoEscolar.class);
        }
    }

    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where :fecha between p.fechaInicio and p.fechaFin and p.activoNi=:tipo", ProcesosInscripcion.class)
                    .setParameter("fecha", new Date())
                    .setParameter("tipo",true)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (procesosInscripcion == null) {
                return ResultadoEJB.crearErroneo(2, procesosInscripcion, "No se encontro proceso de inscripción");
            } else {
                return ResultadoEJB.crearCorrecto(procesosInscripcion, "Proceso de incripción activo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el proceso de inscripcion activo(EjbRegistroFichaAdmision.getProcesosInscripcionActivo).", e, null);
        }
    }
// ------------------------------ ------------------- OPERACIONES --------------------  ----------------------------

    /**
     * Guarda los datos de persona
     *
     * @param rr Dto PERSONA
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.PersonaR> operacionesPersonaR(DtoAspirante.PersonaR rr) {
        try {
            if (rr == null) {
                return ResultadoEJB.crearErroneo(2, rr, "El dto no debe ser nulo");
            }
            switch (rr.getOperacionGeneral()) {
                case PERSISTIR:
                    // System.out.println("Persona  " + rr.getPersona().getCurp());
                    em.persist(rr.getPersona());
                    f.setEntityClass(Persona.class);
                    f.flush();
                    rr.setOperacionGeneral(Operacion.ACTUALIZAR);
                    rr.setEcontrado(true);
                    break;
                case ACTUALIZAR:
                    em.merge(rr.getPersona());
                    f.setEntityClass(Persona.class);
                    f.flush();
                    rr.setOperacionMC(Operacion.ACTUALIZAR);
                    rr.setEcontrado(true);
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "Datos guardados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar DTOPersona (EjbRegistroFichaAdmision.operacionesPersonaR).", e, null);
        }
    }

    /**
     * Operaciones para guardar / actualizar los medios de comunicación
     * @param rr DTO persona
     * @return Resultados del proceso
     */
    public ResultadoEJB<DtoAspirante.PersonaR> operacionesMedioC(DtoAspirante.PersonaR rr) {
        try {
            if (rr.getMedioComunicacion() == null) {
                return ResultadoEJB.crearErroneo(2, rr, "El dto de medio de comunicación no debe ser nulo");
            }
            switch (rr.getOperacionMC()) {
                case PERSISTIR:
                    // System.out.println("Persona  " + rr.getPersona().getCurp());
                    rr.getMedioComunicacion().setPersona1(new Persona());
                    rr.getMedioComunicacion().setPersona1(rr.getPersona());
                    rr.getMedioComunicacion().setPersona(rr.getPersona().getIdpersona());
                    em.persist(rr.getMedioComunicacion());
                    f.setEntityClass(MedioComunicacion.class);
                    f.flush();
                    rr.setOperacionMC(Operacion.ACTUALIZAR);
                    rr.setEcontrado(true);
                    break;
                case ACTUALIZAR:
                    em.merge(rr.getMedioComunicacion());
                    f.setEntityClass(MedioComunicacion.class);
                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "Datos de medio de comunicación guardados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar DTOMediosDeComunicación (EjbRegistroFichaAdmision.operacionesMedioC).", e, null);
        }
    }

    /**
     * Operaciones para guardar/actualizar datos medicos del aspirante
     * @param datosMedicos DTO de datos medicos
     * @return Resultado del  proceso
     */
    public ResultadoEJB<DtoAspirante.MedicosR> operacionesDatosMedicos(DtoAspirante.MedicosR datosMedicos, DtoAspirante.PersonaR personaR) {
        try {
            if (datosMedicos == null || personaR.getPersona().getIdpersona()==null) {
                return ResultadoEJB.crearErroneo(2, datosMedicos, "El dto de medio de comunicación no debe ser nulo");
            }
            DatosMedicos dm = new DatosMedicos();
            dm= datosMedicos.getDatosMedicos();
            dm.setCvePersona(personaR.getPersona().getIdpersona());
            switch (datosMedicos.getOperacion()) {
                case PERSISTIR:
                    // System.out.println("Persona  " + rr.getPersona().getCurp());
                    em.persist(dm);
                    f.setEntityClass(DatosMedicos.class);
                    f.flush();
                    datosMedicos.setDatosMedicos(dm);
                    datosMedicos.setOperacion(Operacion.ACTUALIZAR);
                    break;
                case ACTUALIZAR:
                    em.merge(dm);
                    f.setEntityClass(DatosMedicos.class);
                    f.flush();
                    datosMedicos.setDatosMedicos(dm);
                    datosMedicos.setOperacion(Operacion.ACTUALIZAR);
                    break;
            }
            return ResultadoEJB.crearCorrecto(datosMedicos, "Datos de médicos guardados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar DTOMDatosMedicos (EjbRegistroFichaAdmision.operacionesDatosMedicos).", e, null);
        }
    }

    /**
     * Operaciones para guardar/actualizar aspirante
     * @param rr Dto aspirante
     * @param pr  Dto persona
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> operacionesAspiranteR(DtoAspirante.AspiranteR rr, Persona pr) {
        try {
            ResultadoEJB<ProcesosInscripcion> rejb = getProcesosInscripcionActivo();
            rr.setProcesosInscripcion(rejb.getValor());
            rr.getAspirante().setTipoAspirante(new TipoAspirante());
            rr.getAspirante().setTipoAspirante(rr.getTipo());
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getAspirante().setIdPersona(new Persona());
                    rr.getAspirante().setIdProcesoInscripcion(new ProcesosInscripcion());
                    rr.getAspirante().setIdPersona(pr);
                    rr.getAspirante().setIdProcesoInscripcion(rr.getProcesosInscripcion());
                    rr.getAspirante().setFechaRegistro(new Date());
                    rr.getAspirante().setEstatus(false);
                    em.persist(rr.getAspirante());
                    f.setEntityClass(Aspirante.class);
                    f.flush();
                    rr.setOperacion(Operacion.ACTUALIZAR);
                    break;
                case ACTUALIZAR:
                    em.merge(rr.getAspirante());
                    f.setEntityClass(Aspirante.class);
                    f.flush();
                    break;
            }
            return ResultadoEJB.crearCorrecto(rr, "DTOAspirante Guardado/Actualizado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error en operacion aspirante (EjbRegistroFichaAdmision.operacionesAspiranteR).", e, null);
        }
    }

    /**
     * Guarda /Actualiza los datos del domicilio de un aspirante
     * @param rr Dto del domicilio
     * @param a Aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.DomicilioR> operacionesDomicilioR(@NonNull DtoAspirante.DomicilioR rr, @NonNull Aspirante a) {
        try {
            if(rr.getDomicilio() ==null){return ResultadoEJB.crearErroneo(2,rr,"El domicilio no debe ser nulo");}
            if(a ==null){return ResultadoEJB.crearErroneo(3,rr,"El aspirante no debe ser nulo");}
            switch (rr.getOperacion()) {
                case PERSISTIR:
                    rr.getDomicilio().setAspirante1(new Aspirante());
                    rr.getDomicilio().setAspirante1(a);
                    rr.getDomicilio().setAspirante(a.getIdAspirante());
                    em.persist(rr.getDomicilio()); f.setEntityClass(Domicilio.class); f.flush(); rr.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(rr.getDomicilio());  f.setEntityClass(Domicilio.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(rr, "Domiclio Guardado/Actualizado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error en operaciones de domicilio (EjbRegistroFichaAdmision.operacionesDomicilioR).", e, null);
        }
    }

    /**
     * Guarda/ Actualiza al tutot familiar del aspirante
     * @param tutorFamiliar Dto tutor familiar del aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.TutorR> operacionesTutorA(@NonNull DtoAspirante.TutorR tutorFamiliar) {
        try {
            if(tutorFamiliar==null){return ResultadoEJB.crearErroneo(2,tutorFamiliar,"El tutor no debe ser nulo");}
            switch (tutorFamiliar.getOperacion()) {
                case PERSISTIR:  em.persist(tutorFamiliar.getTutorFamiliar());    f.setEntityClass(TutorFamiliar.class); f.flush(); tutorFamiliar.setOperacion(Operacion.ACTUALIZAR);break;
                case ACTUALIZAR: em.merge(tutorFamiliar.getTutorFamiliar());  f.setEntityClass(TutorFamiliar.class); f.flush();  break;
            }
            return ResultadoEJB.crearCorrecto(tutorFamiliar, "DTOTutor Guardado/Actualizado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error en operacion tutor (EjbRegistroFichaAdmision.operacionesTutorA).", e, null);
        }
    }

    /**
     * Operaciones Guardar/Actualizar
     * @param datosFam Dto datos familiares
     * @param a Aspirantes
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.FamiliaresR> operacionesFamiliares(@NonNull DtoAspirante.FamiliaresR datosFam,@NonNull Aspirante a) {
        try {
            if(datosFam==null){return ResultadoEJB.crearErroneo(2,datosFam,"Los datos familiares no deben ser nulos");}
            if(a==null){return ResultadoEJB.crearErroneo(3,datosFam,"El aspirante no debe ser nulo");}
            datosFam.getDatosFamiliares().setEscolaridadMadre(new Escolaridad());
            datosFam.getDatosFamiliares().setOcupacionMadre(new Ocupacion());
            datosFam.getDatosFamiliares().setEscolaridadPadre(new Escolaridad());
            datosFam.getDatosFamiliares().setOcupacionPadre(new Ocupacion());
            datosFam.getDatosFamiliares().setEscolaridadMadre(datosFam.getEscolaridadM());
            datosFam.getDatosFamiliares().setOcupacionMadre(datosFam.getOcupacionM());
            datosFam.getDatosFamiliares().setEscolaridadPadre(datosFam.getEscolaridadP());
            datosFam.getDatosFamiliares().setOcupacionPadre(datosFam.getOcupacionP());
            switch (datosFam.getOperacion()) {
                case PERSISTIR:
                    datosFam.getDatosFamiliares().setAspirante1(new Aspirante());
                    datosFam.getDatosFamiliares().setTutor(new TutorFamiliar());
                    datosFam.getDatosFamiliares().setAspirante1(a);
                    datosFam.getDatosFamiliares().setAspirante(a.getIdAspirante());
                    datosFam.getDatosFamiliares().setTutor(datosFam.getTutorR().getTutorFamiliar());
                    em.persist(datosFam.getDatosFamiliares()); f.setEntityClass(DatosFamiliares.class); f.flush(); datosFam.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(datosFam.getDatosFamiliares());  f.setEntityClass(DatosFamiliares.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(datosFam, "DTODatosFamiliares Guardados/Actualizados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error operaciones datos familiares (EjbRegistroFichaAdmision.operacionesFamiliares).", e, null);
        }
    }

    /**
     * Actualiza o refresca las encuesta de aspirante
     * @param id Número de pregunta
     * @param valor Valor ingresadp
     * @param resultados Dto de Encuesta
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.EncuestaR>  operacionEncuesta(@NonNull String id, @NonNull String valor, @NonNull DtoAspirante.EncuestaR resultados) {
        try {
            switch(resultados.getOperacion()){
                case ACTUALIZAR:
                    //Crear los resultados cargar los valores
                    f.setEntityClass(resultados.getEncuestaAspirante().getClass());
                    em.merge(resultados);
                    f.setEntityClass(EncuestaAspirante.class);
                    em.flush();
                    resultados.setEncuestaAspirante(resultados.getEncuestaAspirante());
                    resultados.setOperacion(Operacion.ACTUALIZAR);
                    return ResultadoEJB.crearCorrecto(resultados, "Evaluacion Tutor Resultados, correcta");
                case REFRESCAR:
                    //Actualizar los resultados de las preguntas
                    switch (id) {
                        case "p1":
                            resultados.getEncuestaAspirante().setR1Lenguaindigena(valor);
                            resultados.getEncuestaAspirante().setR2tipoLenguaIndigena(new LenguaIndigena());
                            resultados.getEncuestaAspirante().setR2tipoLenguaIndigena(null);
                            break;
                        case "p2":
                            resultados.getEncuestaAspirante().setR2tipoLenguaIndigena(new LenguaIndigena());
                            resultados.getEncuestaAspirante().setR2tipoLenguaIndigena(null);
                            if (resultados.getEncuestaAspirante().getR1Lenguaindigena().equals("SI")) {
                                LenguaIndigena li = em.find(LenguaIndigena.class, Short.parseShort(valor));
                                resultados.getEncuestaAspirante().setR2tipoLenguaIndigena(li);
                            }
                            break;
                        case "p3":resultados.getEncuestaAspirante().setR3comunidadIndigena(valor);break;
                        case "p4":resultados.getEncuestaAspirante().setR4programaBienestar(valor);break;
                        case "p5":resultados.getEncuestaAspirante().setR5ingresoMensual(Double.parseDouble(valor));break;
                        case "p6":resultados.getEncuestaAspirante().setR6dependesEconomicamnete(valor);break;
                        case "p7":resultados.getEncuestaAspirante().setR7ingresoFamiliar(Double.parseDouble(valor)); break;
                        case "p8":resultados.getEncuestaAspirante().setR8primerEstudiar(valor); break;
                        case "p9":resultados.getEncuestaAspirante().setR9nivelMaximoEstudios(valor);break;
                        case "p10":resultados.getEncuestaAspirante().setR10numeroDependientes(Short.parseShort(valor)); break;
                        case "p11":resultados.getEncuestaAspirante().setR11situacionEconomica(valor); break;
                        case "p12": resultados.getEncuestaAspirante().setR12hijoPemex(valor);break;
                        case "p13":resultados.getEncuestaAspirante().setR13utxjPrimeraOpcion(valor); break;
                        case "p14":resultados.getEncuestaAspirante().setR14examenAdmisionOU(valor);break;
                        case "p15":
                            resultados.getEncuestaAspirante().setR15medioImpacto(new MedioDifusion());
                            MedioDifusion li = em.find(MedioDifusion.class, Short.parseShort(valor));
                            resultados.getEncuestaAspirante().setR15medioImpacto(li);
                            break;
                        case "p16": resultados.getEncuestaAspirante().setR16segundaCarrera(valor);break;
                        case "p17": resultados.getEncuestaAspirante().setR17Alergia(valor); break;
                        case "p18": resultados.getEncuestaAspirante().setR18padecesEnfermedad(valor);break;
                        case "p19": resultados.getEncuestaAspirante().setR19tratamientoMedico(valor); break;

                    }
                    resultados.setEncuestaAspirante(resultados.getEncuestaAspirante());
                    resultados.setOperacion(Operacion.ACTUALIZAR);
                    return ResultadoEJB.crearCorrecto(resultados, "Respuestas actualizadas");
                default:
            }
            return ResultadoEJB.crearCorrecto(resultados,"Guardado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar los resultados (EjbRegistroFichaAdmision.operacionEncuesta)", e, null);
        }
    }
    public ResultadoEJB<DtoAspirante.EncuestaR> operacionesEncuestaR(Aspirante a,String valor, Integer numP) {
        try {
            Operacion operacion;
            List<EncuestaAspirante> es = em.createQuery("select e from EncuestaAspirante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", EncuestaAspirante.class).setParameter("idAspirante", a.getIdAspirante()).getResultList();
            EncuestaAspirante e = new EncuestaAspirante();
            if (!es.isEmpty()) {
                e = es.get(0);
                operacion=Operacion.ACTUALIZAR;
            } else {
                e.setAspirante(new Aspirante());
                e.setAspirante(a);
                e.setCveAspirante(a.getIdAspirante( ));
                operacion=Operacion.PERSISTIR;
            }
            switch (numP) {
                case 1:
                    e.setR1Lenguaindigena(valor);
                    e.setR2tipoLenguaIndigena(new LenguaIndigena());
                    e.setR2tipoLenguaIndigena(null);
                    break;
                case 2:

                    e.setR2tipoLenguaIndigena(new LenguaIndigena());
                    LenguaIndigena li = em.find(LenguaIndigena.class, Short.parseShort(valor));
                    e.setR2tipoLenguaIndigena(li);
                    break;
                case 3:                    e.setR3comunidadIndigena(valor);                    break;
                case 4:                    e.setR4programaBienestar(valor);                    break;
                case 5:                    e.setR5ingresoMensual(Double.parseDouble(valor));                    break;
                case 6:                    e.setR6dependesEconomicamnete(valor);                    break;
                case 7:                    e.setR7ingresoFamiliar(Double.parseDouble(valor));                    break;
                case 8:                    e.setR8primerEstudiar(valor);                    break;
                case 9:                    e.setR9nivelMaximoEstudios(valor);                    break;
                case 10:                    e.setR10numeroDependientes(Short.parseShort(valor));                    break;
                case 11:                    e.setR11situacionEconomica(valor);                    break;
                case 12:                    e.setR12hijoPemex(valor);                    break;
                case 13:                    e.setR13utxjPrimeraOpcion(valor);                    break;
                case 14:                    e.setR14examenAdmisionOU(valor);                    break;
                case 15:
                    e.setR15medioImpacto(new MedioDifusion());
                    MedioDifusion md = em.find(MedioDifusion.class, Short.parseShort(valor));
                    e.setR15medioImpacto(md);
                    break;
                case 16:                    e.setR16segundaCarrera(valor);                    break;
                case 17:                    e.setR17Alergia(valor);                    break;
                case 18:                    e.setR18padecesEnfermedad(valor);                    break;
                case 19:                    e.setR19tratamientoMedico(valor);                    break;
                case 20:                    e.setR20Hijos(valor);                    break;
                case 21:                    e.setR21noHijos(valor);                    break;

            }

            switch (operacion) {
                case PERSISTIR:
                    em.persist(e);
                    em.flush();
                    break;
                case ACTUALIZAR:
                    em.merge(e);
                    em.flush();
                    break;
            }
            ResultadoEJB<DtoAspirante.EncuestaR> b= getEncuesta(a);
            return ResultadoEJB.crearCorrecto(b.getValor(), "DTOPersona Encontrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    /**
     * Guarda/ actualiza los datos académicos
     * @param datosAcademicos dto de datos académicos
     * @param a Aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AcademicosR> operacionesAcademicos(@NonNull DtoAspirante.AcademicosR datosAcademicos,@NonNull Aspirante a) {
        try {
            if(datosAcademicos==null){return ResultadoEJB.crearErroneo(2,datosAcademicos,"Los datos académicos no deben ser nulos");}
            if(a==null){return ResultadoEJB.crearErroneo(3,datosAcademicos,"El aspirante no debe ser nulo");}
            datosAcademicos.getAcademicos().setSistemaPrimeraOpcion(new Sistema());
            datosAcademicos.getAcademicos().setSistemaSegundaOpcion(new Sistema());
            datosAcademicos.getAcademicos().setEspecialidadIems(new EspecialidadCentro());
            datosAcademicos.getAcademicos().setSistemaPrimeraOpcion(datosAcademicos.getSistemaPo());
            datosAcademicos.getAcademicos().setSistemaSegundaOpcion(datosAcademicos.getSistemaSo());
            datosAcademicos.getAcademicos().setEspecialidadIems(datosAcademicos.getEspecialidad());
            datosAcademicos.getAcademicos().setInstitucionAcademica(datosAcademicos.getIems().getIems());
            switch (datosAcademicos.getOperacion()) {
                case PERSISTIR:
                    datosAcademicos.getAcademicos().setAspirante1(new Aspirante());

                    datosAcademicos.getAcademicos().setAspirante(a.getIdAspirante());
                    datosAcademicos.getAcademicos().setAspirante1(a);

                    em.persist(datosAcademicos.getAcademicos()); f.setEntityClass(DatosAcademicos.class); f.flush(); datosAcademicos.setOperacion(Operacion.ACTUALIZAR); break;
                case ACTUALIZAR: em.merge(datosAcademicos.getAcademicos());  f.setEntityClass(DatosAcademicos.class); f.flush(); break;
            }
            return ResultadoEJB.crearCorrecto(datosAcademicos, "DTODatosAcademicos guardados/actualizados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al guardar/actualizar los datos académicos (EjbRegistroFichaAdmision.operacionesAcademicos).", e, null);
        }
    }

    /**
     * Genera el folio del aspirante
     * @param procesosInscripcion Proceso de inscripción activo
     * @return Resultado del proceso(Folio)
     */
    public ResultadoEJB<Integer> generarFolio(@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(2,new Integer(0),"El proceso de inscripción no debe ser nulo");}
            String folio = "";
            int folioUlizable = 0;
            String anyo2 = new SimpleDateFormat("yy").format(new Date());
            folio = anyo2.concat(String.valueOf(procesosInscripcion.getIdPeriodo())).concat("0000");

            TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(p.folioAspirante) FROM Aspirante AS p WHERE p.idProcesoInscripcion.idProcesosInscripcion =:idPE AND p.tipoAspirante.idTipoAspirante = 1")
                    .setParameter("idPE", procesosInscripcion.getIdProcesosInscripcion());
            if(v.getSingleResult() == null){
                folioUlizable = Integer.valueOf(folio);
            }else{
                folioUlizable = v.getSingleResult()+1;
            }
            return ResultadoEJB.crearCorrecto(folioUlizable,"Folio generado correctamente");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al generar el folio del aspirate (EjbRegistroFichaAdmision.generarFolio).", e, null);

        }
    }

    /**
     * Genera ficha de admision
     * @param persona
     * @param academicos
     * @param domicilio
     * @param aspirante
     * @param medioComunicacion
     * @param uso
     * @throws IOException
     * @throws DocumentException
     */
    public ResultadoEJB<Boolean> generarFicha(@NonNull Persona persona, @NonNull DatosAcademicos academicos,@NonNull Domicilio domicilio,@NonNull Aspirante aspirante,@NonNull MedioComunicacion medioComunicacion, @NonNull String uso) throws IOException, DocumentException {
        if(persona==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"La persona no debe ser nula");}
        if(academicos==null){return ResultadoEJB.crearErroneo(3,Boolean.FALSE,"Los datos académicos no deben ser nulos");}
        if(medioComunicacion==null){return  ResultadoEJB.crearErroneo(4,Boolean.FALSE,"Los datos de medio de comunicación no deben ser nulos");}
        if(domicilio==null){return ResultadoEJB.crearErroneo(5,Boolean.FALSE,"Los datos del domicilio no deben ser nulos");}
        if(aspirante==null){return ResultadoEJB.crearErroneo(6,Boolean.FALSE,"El aspirante no debe ser nulo");}
        if(uso==null){return ResultadoEJB.crearErroneo(7,Boolean.FALSE,"El uso no debe ser nulo");}
        String ruta = "C://archivos//plantillas//formato_ficha_admision.pdf";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");

        Generos generos = new Generos();
        generos = em.createQuery("select g from Generos g where g.genero=:genero",Generos.class).setParameter("genero",persona.getGenero()).getResultStream().findFirst().orElse(null);
        if(generos==null){return ResultadoEJB.crearErroneo(8,Boolean.FALSE,"Error al obtener el genero");}
        Iems iems = new Iems();
        iems = em.createQuery("select  i from Iems i where i.iems=:iems",Iems.class).setParameter("iems",academicos.getInstitucionAcademica()).getResultStream().findFirst().orElse(null);
        if(iems==null){return ResultadoEJB.crearErroneo(9,Boolean.FALSE,"Error al obtener el Iems");}
        Asentamiento asentamiento = new Asentamiento();
        asentamiento = em.createQuery("SELECT a FROM Asentamiento a WHERE a.asentamientoPK.asentamiento = :idA AND a.asentamientoPK.municipio = :idMun AND a.asentamientoPK.estado = :idEst", Asentamiento.class)
                .setParameter("idA", domicilio.getIdAsentamiento())
                .setParameter("idMun", domicilio.getIdMunicipio())
                .setParameter("idEst", domicilio.getIdEstado())
                .getResultStream().findFirst().orElse(null);
        if(asentamiento==null){return ResultadoEJB.crearErroneo(10, Boolean.FALSE,"Error al obtener el asentamiento");}

        InputStream is = new FileInputStream(ruta);
        PdfReader pdfReader = new PdfReader(is,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

        BarcodeQRCode qrcode = new BarcodeQRCode(String.valueOf(aspirante.getFolioAspirante()), 60, 60, null);
        Image image = qrcode.getImage();
        image.setAbsolutePosition(465f, 92f);
        PdfContentByte content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(image);

        BarcodeQRCode qrcode2 = new BarcodeQRCode(String.valueOf(aspirante.getFolioAspirante()), 60, 60, null);
        Image image2 = qrcode2.getImage();
        image2.setAbsolutePosition(465f, 442f);
        content.addImage(image2);

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("folio", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("fechaA", sm.format(aspirante.getFechaRegistro()));
        fields.setField("carreraA", ejbProcesoInscripcion.buscaAreaByClave(academicos.getPrimeraOpcion()).getNombre());
        fields.setField("apellidoPatA", persona.getApellidoPaterno());
        fields.setField("apellidoMatA", persona.getApellidoMaterno());
        fields.setField("nombreAlumnoA", persona.getNombre());
        fields.setField("fechaNacimientoA", sm.format(persona.getFechaNacimiento()));
        fields.setField("generoA",generos.getNombre());
        fields.setField("estadoCivilA", persona.getEstadoCivil());
        fields.setField("emailA", medioComunicacion.getEmail());
        fields.setField("iemsA",iems.getNombre());
        fields.setField("estadoIemsA", iems.getLocalidad().getMunicipio().getEstado().getNombre());
        fields.setField("municipioIemsA", iems.getLocalidad().getMunicipio().getNombre());
        fields.setField("localidadIemsA", iems.getLocalidad().getNombre());
        fields.setField("calleA", domicilio.getCalle());
        fields.setField("numeroA", domicilio.getNumero());
        fields.setField("coloniaA", asentamiento.getNombreAsentamiento());
        fields.setField("localidadA", asentamiento.getNombreAsentamiento());
        fields.setField("municipioA", asentamiento.getMunicipio1().getNombre());
        fields.setField("estadoA", asentamiento.getMunicipio1().getEstado().getNombre());
        fields.setField("movilA", medioComunicacion.getTelefonoMovil());
        fields.setField("fijoA", medioComunicacion.getTelefonoFijo());
        fields.setField("folioA", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("usuarioA", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("passA", persona.getCurp());
        fields.setField("turnoA", academicos.getSistemaPrimeraOpcion().getNombre());

        fields.setField("fechaC", sm.format(aspirante.getFechaRegistro()));
        fields.setField("carreraC",ejbProcesoInscripcion.buscaAreaByClave(academicos.getPrimeraOpcion()).getNombre());
        fields.setField("apellidoPatC", persona.getApellidoPaterno());
        fields.setField("apellidoMatC", persona.getApellidoMaterno());
        fields.setField("nombreAlumnoC", persona.getNombre());
        fields.setField("fechaNacimientoC", sm.format(persona.getFechaNacimiento()));
        fields.setField("generoC", generos.getNombre());
        fields.setField("estadoCivilC",  persona.getEstadoCivil());
        fields.setField("emailC", medioComunicacion.getEmail());
        fields.setField("iemsC", iems.getNombre());
        fields.setField("estadoIemsC", iems.getLocalidad().getMunicipio().getEstado().getNombre());
        fields.setField("municipioIemsC", iems.getLocalidad().getMunicipio().getNombre());
        fields.setField("localidadIemsC", iems.getLocalidad().getNombre());
        fields.setField("calleC", domicilio.getCalle());
        fields.setField("numeroC", domicilio.getNumero());
        fields.setField("coloniaC", asentamiento.getNombreAsentamiento());
        fields.setField("localidadC", asentamiento.getNombreAsentamiento());
        fields.setField("municipioC", asentamiento.getMunicipio1().getNombre());
        fields.setField("estadoC", asentamiento.getMunicipio1().getEstado().getNombre());
        fields.setField("movilC", medioComunicacion.getTelefonoMovil());
        fields.setField("fijoC", medioComunicacion.getTelefonoFijo());
        fields.setField("folioC", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("usuarioC", String.valueOf(aspirante.getFolioAspirante()));
        fields.setField("passC", persona.getCurp());
        fields.setField("turnoC", academicos.getSistemaPrimeraOpcion().getNombre());

        pdfStamper.close();
        pdfStamper.close();

        Object response = facesContext.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setContentType("application/pdf");
            hsr.setHeader("Content-disposition", "attachment; filename=\""+aspirante.getFolioAspirante()+".pdf\"");
            hsr.setContentLength(baos.size());
            try {
                ServletOutputStream out = hsr.getOutputStream();
                baos.writeTo(out);
                out.flush();
                out.close();
            } catch (IOException ex) {
                System.out.println("Error:  " + ex.getMessage());
            }
            facesContext.responseComplete();
        }

        if(uso.equals("Alumno")){
            // El correo gmail de envío
            String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
            String claveCorreo = "DServiciosEscolares19";
            String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Gracias por elegir a la Universidad Tecnologica de Xicotepec de Juárez como opción para continuar con tus estudios de nivel superior." +
                    "\n\n Para continuar descarga la ficha la admisión y asiste a las instalaciones de la UTXJ y entregar la documentación necesaria\n\n"
                    + "* Formato de Ficha de Admisión.\n"
                    + "* Copia de CURP (nuevo formato).\n"
                    + "* Copia de Acta de Nacimiento.\n"
                    + "* Copia de Certificado de Nivel Medio Superior o Constancia de Estudios Original reciente con tira de materias y calificaciones (aprobatorias) del 1o. al 5o. semestre; indicando el promedio general y firmado por el titular de la Institución\n"
                    + "* Referencia bancaria del pago de ficha y examen de admisión.\n\n" +
                    "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares";

            String identificador = "Registro de Ficha de Admisión 2021 UTXJ";
            String asunto = "Registro Exitoso";
           // System.out.println(medioComunicacion.getEmail());
            if(medioComunicacion.getEmail() != null){
                System.out.println("Correo "+ medioComunicacion.getEmail());
                try {
                    DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
                    EnvioCorreos.EnviarCorreoArchivos(correoEnvia, claveCorreo,identificador,asunto,persona.getMedioComunicacion().getEmail(),mensaje,source,String.valueOf(aspirante.getFolioAspirante()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return ResultadoEJB.crearCorrecto(Boolean.TRUE,"Ficha de admisión creada correctamente");
    }





    // ------------------ ---------------------------- BUSQUEDAS ESPECIFICAS------------------------------------------------------------

    /**
     * leé el QR del archivo pdf de la CURP
     * (Hecho por Juan)
     * Mod. Tatis
     * @param file
     * @return
     */
    public Persona leerCurp(Part file) {
        Persona p = new Persona();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        String rutaRelativa = "";
        try{
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.ServicioFichaAdmision");
            //Almacenamiento del archivo de la curp de la persona
            String ruta = ServicioArchivos.genRutaRelativa("curps");
            String rutaR = ServicioArchivos.carpetaRaiz.concat(ruta);
            String rutaArchivo = ruta.concat(file.getSubmittedFileName());
            ServicioArchivos.addCarpetaRelativa(rutaR);
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            rutaRelativa = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
            file.write(rutaArchivo);

            //Leer codigo QR de archivo de la Curp

            QrPdf pdf = new QrPdf(Paths.get(rutaR.concat(file.getSubmittedFileName())));
            String qrCode = pdf.getQRCode(1, true, true);
            String fecha_nacimiento = "";
            String[] parts = qrCode.split("\\|");
            if(buscaPersonaByCurp(parts[0]) != null){
                //System.out.println("0");
                p = buscaPersonaByCurp(parts[0]);
                //System.out.println("per" + p);

            }else{
                if((parts != null && (parts.length == 8 || parts.length == 9))) {
                    if(parts.length == 9){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts[2]).trim());
                        p.setApellidoMaterno(ucFirst(parts[3]).trim());
                        p.setNombre(ucFirst(parts[4]));
                        if(parts[5].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts[5].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts[6].replace("/","-");
                        p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                        String claveEstado = parts[0].substring(11, 13);
                        if(claveEstado.equals("NE")){
                            ResultadoEJB<Estado> resEstado = getExtranjero();
                            if(resEstado.getCorrecto()){
                                p.setEstado(resEstado.getValor().getIdestado());
                            }
                        }else {
                            Estado estado = em.createNamedQuery("Estado.findByClave", Estado.class)
                                    .setParameter("clave",claveEstado)
                                    .getResultList()
                                    .stream().findFirst().orElse(null);
                            p.setEstado(estado.getIdestado());
                        }
                        p.setUrlCurp(rutaRelativa);
                    }else if(parts.length == 8){
                        p.setCurp(parts[0]);
                        p.setApellidoPaterno(ucFirst(parts[2]));
                        p.setApellidoMaterno(ucFirst(parts[3]));
                        p.setNombre(ucFirst(parts[4]));
                        p.setGenero((short) 1);
                        if(parts[5].equals("HOMBRE"))
                            p.setGenero((short) 2);
                        if(parts[5].equals("MUJER"))
                            p.setGenero((short) 1);
                        fecha_nacimiento = parts[6].replace("/","-");
                        p.setFechaNacimiento(sm.parse(fecha_nacimiento));
                        p.setUrlCurp(rutaRelativa);
                    }else{
                        ServicioArchivos.eliminarArchivo(rutaRelativa);
                        p = new Persona();
                    }
                }
            }
        }catch (IOException ex){
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NotFoundException ex){
            ServicioArchivos.eliminarArchivo(rutaRelativa);
            Logger.getLogger(ServicioFichaAdmision.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;
    }

    /**
     * Busca persona por CURP
     * @param curpBusqueda
     * @return
     */
    public Persona buscaPersonaByCurp(String curpBusqueda) {
        Persona  persona = new Persona();
        TypedQuery<Persona> p = em.createNamedQuery("Persona.findByCurp",Persona.class)
                .setParameter("curp",curpBusqueda);
        List<Persona> personas = p.getResultList();

        if(personas.isEmpty()){
            persona = null;
        }else{
            persona = personas.get(0);
        }

        return  persona;
    }
    public static String ucFirst(String str){
        String strTemp = "";

        if (str == null || str.isEmpty()) {
            return str;
        } else {
            for (String palabra : str.split(" ")) {
                strTemp += palabra.substring(0, 1).toUpperCase() + palabra.substring(1, palabra.length()).toLowerCase() + " ";
                strTemp.trim();
            }
            return  strTemp;
        }
    }

    ResultadoEJB<Estado> getExtranjero (){
        try{
            Estado e = em.createQuery("select e from Estado e where e.idpais.idpais=:idPais and e.idestado=:estado",Estado.class)
                    .setParameter("idPais",247)
                    .setParameter("estado",2204)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(e!=null){return ResultadoEJB.crearCorrecto(e,"Estado extrangero encontrado");}
            else {return ResultadoEJB.crearErroneo(2,e,"Error al obtener el estado extranjero");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener datos extranjeros (EjbRegistroFichaAdmision.getExtranjero).", e, null);

        }
    }

    /**
     * Obtiene el dto de Persona (Exculsivo para Servicios escolares)
     * Busca un registro por curp, en caso de que encuentre alguno, obitiene sus datos (Medios de comunicación, datos médicos)
     * Si no encuentra registro de la persona regresa dto listo para registrarlo
     *
     * @param persona Persona que devuelve la lectura del QR
     * @return Resultaldo del proceso
     */
    public ResultadoEJB<DtoAspirante.PersonaR> getPersonaRSE(@NonNull Persona persona) {
        try {
            DtoAspirante.PersonaR rr = new DtoAspirante.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE);
            // System.out.println("1");
            if (persona.getNombre() == null) {
                return ResultadoEJB.crearErroneo(2, rr, "La curp no debe ser nula");
            }
            //System.out.println("2");
            //System.out.println("3" +persona.getEstado()+ " " + persona.getCurp());
            ResultadoEJB<Persona> resPersona = getPersonabyCurp(persona.getCurp());
            ResultadoEJB<Pais> resPaisO = getPaisOrigenByEstado(persona.getEstado());
            if (resPaisO.getCorrecto() != false) {
                rr.setPaisOr(resPaisO.getValor());
            }
            if (resPersona.getCorrecto() == true) {
                //Se buscan datos de medio de comunicacion
                ResultadoEJB<MedioComunicacion> resMc = getMedioCbyPersona(resPersona.getValor());
                if (resMc.getCorrecto() == true) {
                    rr.setMedioComunicacion(resMc.getValor());
                    rr.setOperacionMC(Operacion.ACTUALIZAR);
                } else {
                    //No se encontraron los datos se mandan nuevos
                    rr.setMedioComunicacion(new MedioComunicacion());
                    rr.setOperacionMC(Operacion.PERSISTIR);
                }
                //Existe un registro de la persona(se recuperan sus datos)
                rr.setPersona(resPersona.getValor());
                rr.setOperacionGeneral(Operacion.ACTUALIZAR);
                rr.setEcontrado(true);
                return ResultadoEJB.crearCorrecto(rr, "La persona ya se encuentra registrada");
            } else {
                //No existe registro de la persona se toma como registro nuevo
                Persona newPersona = new Persona();
                newPersona.setNombre(persona.getNombre());
                newPersona.setApellidoPaterno(persona.getApellidoPaterno());
                newPersona.setApellidoMaterno(persona.getApellidoMaterno());
                newPersona.setApellidoPaterno(persona.getApellidoPaterno());
                newPersona.setFechaNacimiento(persona.getFechaNacimiento());
                newPersona.setCurp(persona.getCurp());
                newPersona.setGenero(persona.getGenero());
                newPersona.setEstado(persona.getEstado());
                newPersona.setMunicipio(persona.getMunicipio());
                newPersona.setLocalidad(persona.getLocalidad());
                newPersona.setUrlCurp(persona.getUrlCurp());
                rr.setEcontrado(false);
                rr.setPersona(newPersona);
                rr.setPaisOr(resPaisO.getValor());
                rr.setMedioComunicacion(new MedioComunicacion());
                rr.setOperacionGeneral(Operacion.PERSISTIR);
                rr.setOperacionMC(Operacion.PERSISTIR);
                return ResultadoEJB.crearCorrecto(rr, "No existe registro anterior de la persona");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbRegistroFichaAdmision.getPersonaR).", e, null);
        }
    }  /**
     * Obtiene el dto de Persona
     * Busca un registro por curp, en caso de que encuentre alguno, obitiene sus datos (Medios de comunicación, datos médicos)
     * Si no encuentra registro de la persona regresa dto listo para registrarlo
     *
     * @param persona Persona que devuelve la lectura del QR
     * @return Resultaldo del proceso
     */
    public ResultadoEJB<DtoAspirante.PersonaR> getPersonaR(@NonNull Persona persona) {
        try {
            DtoAspirante.PersonaR rr = new DtoAspirante.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE);
            // System.out.println("1");
            if (persona.getNombre() == null) {
                return ResultadoEJB.crearErroneo(2, rr, "La curp no debe ser nula");
            }
            //System.out.println("2");
            if (persona.getEstado() > 32) {
                return ResultadoEJB.crearErroneo(5, rr, "Usted es nacido en el extranjero, favor de dirigirse al Departamento de servicios escolares");
            }
            else {
                //System.out.println("3" +persona.getEstado()+ " " + persona.getCurp());
                ResultadoEJB<Persona> resPersona = getPersonabyCurp(persona.getCurp());
                ResultadoEJB<Pais> resPaisO = getPaisOrigenByEstado(persona.getEstado());
                if (resPaisO.getCorrecto() != false) {
                    rr.setPaisOr(resPaisO.getValor());
                }
                if (resPersona.getCorrecto() == true) {
                    //Se buscan datos de medio de comunicacion
                    ResultadoEJB<MedioComunicacion> resMc = getMedioCbyPersona(resPersona.getValor());
                    if (resMc.getCorrecto() == true) {
                        rr.setMedioComunicacion(resMc.getValor());
                        rr.setOperacionMC(Operacion.ACTUALIZAR);
                    } else {
                        //No se encontraron los datos se mandan nuevos
                        rr.setMedioComunicacion(new MedioComunicacion());
                        rr.setOperacionMC(Operacion.PERSISTIR);
                    }
                    //Existe un registro de la persona(se recuperan sus datos)
                    rr.setPersona(resPersona.getValor());
                    rr.setOperacionGeneral(Operacion.ACTUALIZAR);
                    rr.setEcontrado(true);
                    return ResultadoEJB.crearCorrecto(rr, "La persona ya se encuentra registrada");
                } else {
                    //No existe registro de la persona se toma como registro nuevo
                    Persona newPersona = new Persona();
                    newPersona.setNombre(persona.getNombre());
                    newPersona.setApellidoPaterno(persona.getApellidoPaterno());
                    newPersona.setApellidoMaterno(persona.getApellidoMaterno());
                    newPersona.setApellidoPaterno(persona.getApellidoPaterno());
                    newPersona.setFechaNacimiento(persona.getFechaNacimiento());
                    newPersona.setCurp(persona.getCurp());
                    newPersona.setGenero(persona.getGenero());
                    newPersona.setEstado(persona.getEstado());
                    newPersona.setMunicipio(persona.getMunicipio());
                    newPersona.setLocalidad(persona.getLocalidad());
                    newPersona.setUrlCurp(persona.getUrlCurp());
                    rr.setEcontrado(false);
                    rr.setPersona(newPersona);
                    rr.setPaisOr(resPaisO.getValor());
                    rr.setMedioComunicacion(new MedioComunicacion());
                    rr.setOperacionGeneral(Operacion.PERSISTIR);
                    rr.setOperacionMC(Operacion.PERSISTIR);
                    return ResultadoEJB.crearCorrecto(rr, "No existe registro anterior de la persona");
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbRegistroFichaAdmision.getPersonaR).", e, null);
        }
    }
    /**
     * Busca a una persona por su CURP
     * @param curp CURP del aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Persona> getPersonabyCurp(@NonNull String curp) {
        try {
            Persona persona = new Persona();
            if (curp == null) {
                return ResultadoEJB.crearErroneo(2, persona, "El curp no debe ser nulo");
            }
            persona = em.createQuery("select p from  Persona p where  p.curp=:curp", Persona.class)
                    .setParameter("curp", curp)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (persona == null) {
                return ResultadoEJB.crearErroneo(3, persona, "No existe la persona con el CURP solicitado");
            } else {

                return ResultadoEJB.crearCorrecto(persona, "Persona encontrada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener a la persona por CURP(EjbRegistroFichaAdmision.getPersonabyCurp).", e, null);

        }
    }
    /**
     * Busca medios de comunicacion por persona
     *
     * @param persona Persona a buscar
     * @return Resultado del proceso
     */
    public ResultadoEJB<MedioComunicacion> getMedioCbyPersona(@NonNull Persona persona) {
        try {
            if (persona == null) { return ResultadoEJB.crearErroneo(2, new MedioComunicacion(), "La persona no debe ser nula");
            }
            MedioComunicacion mC = em.createQuery("select m from MedioComunicacion  m where m.persona=:persona", MedioComunicacion.class)
                    .setParameter("persona", persona.getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (mC == null) {
                return ResultadoEJB.crearErroneo(3, mC, "No se econtro medio de comunicacion por persona");
            } else {
                return ResultadoEJB.crearCorrecto(mC, "Medios de comunicación encontrados");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obeneter medio de comunicacion por persona(EjbRegistroFichaAdmision.getMedioCbyPersona).", e, null);
        }
    }

    /**
     * Busca los datos de la encuesta vocacional por persona
     * @param personaR Dto de la persona
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.EncuestaVocacionalR> getEncuestaVocacionalbyPersona(@NonNull DtoAspirante.PersonaR personaR){
        try{
            EncuestaVocacional ev= new EncuestaVocacional();
            DtoAspirante.EncuestaVocacionalR dtoVocacional = new DtoAspirante.EncuestaVocacionalR(ev,new AreasUniversidad(),Operacion.PERSISTIR,false);
            if(personaR==null){return ResultadoEJB.crearErroneo(2,dtoVocacional,"La persona no debe ser nulo");}
            ResultadoEJB<EncuestaVocacional> resEn = ejbEncuestaVocacional.getResultados(personaR.getPersona());
            ev= resEn.getValor();

            if(resEn.getCorrecto()){
                dtoVocacional.setOperacion(Operacion.ACTUALIZAR);
                dtoVocacional.setEcontrado(true);
                dtoVocacional.setEncuestaAspirante(ev);
                if(ev.getR4()!=null){
                    //Busca el programa educativo por clave
                    dtoVocacional.setCarreraSelect(ejbProcesoInscripcion.buscaAreaByClave(resEn.getValor().getR4()));
                    return ResultadoEJB.crearCorrecto(dtoVocacional,"Datos de la encuesta vocacional encontrados");
                }else {
                    dtoVocacional.setCarreraSelect(new AreasUniversidad());
                    return ResultadoEJB.crearCorrecto(dtoVocacional,"Datos de la encuesta vocacional encontrados");
                }
            }else {return ResultadoEJB.crearErroneo(3,dtoVocacional,"No se encontraron datos de la encuesta vocacional");}
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "No se pudo obtener la encuesta vocacional (EjbRegistroFichaAdmision.getEncuesraVocacionalbyPersona)", e, null); }

    }

    /**
     * Busca datos médicos por id de la persona
     * @param personaR Dto de la persona
     * @return Resultado del proceso
     */
    public  ResultadoEJB<DtoAspirante.MedicosR> getDatosMedicosbyPersona(@NonNull DtoAspirante.PersonaR personaR){
        try{
            DatosMedicos dm = new DatosMedicos();
            DtoAspirante.MedicosR dtoDm = new DtoAspirante.MedicosR(dm,new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR,false);
            if(personaR ==null){ return ResultadoEJB.crearErroneo(2,dtoDm,"El dto no debe ser nulo");}
            dm = em.createQuery("select d from DatosMedicos d where  d.persona.idpersona=:persona",DatosMedicos.class)
                    .setParameter("persona", personaR.getPersona().getIdpersona())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(dm == null){return ResultadoEJB.crearErroneo(3,dtoDm, "No se encontraron datos medicos de la persona");}
            else {
                dtoDm.setDatosMedicos(dm);
                dtoDm.setDiscapacidad(dm.getCveDiscapacidad());
                dtoDm.setSangre(dm.getCveTipoSangre());
                dtoDm.setOperacion(Operacion.ACTUALIZAR);
                dtoDm.setEcontrado(true);
                return ResultadoEJB.crearCorrecto(dtoDm,"Datos medios encontrados");
            }
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "No se pudo obeneter los datos medicos de la persona(EjbRegistroFichaAdmision.getDatosMedicosbyPersona).", e, null); }
    }

    /**
     * Busca a un aspirante por id de persona y el proceso de de inscripción activo
     * @param aspirante Dto del aspirante
     * @param persona Persona registrada
     * @param procesosInscripcion Proceso de inscripción activo
     * @return Resultados del proceso (C-> Dto Aspirante)
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> getAspirantebyPersona(@NonNull DtoAspirante.AspiranteR aspirante, @NonNull Persona persona,@NonNull ProcesosInscripcion procesosInscripcion){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,aspirante,"El aspirante no debe ser nulo");}
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(3,aspirante,"El proceso de inscripción no debe ser nulo");}
            if(persona==null){return ResultadoEJB.crearErroneo(4,aspirante,"La persona no debe ser nulo");}
            Aspirante aspirante1 = new Aspirante();
            aspirante1 = em.createQuery("select a from Aspirante a where a.idPersona.idpersona=:idPersona and a.idProcesoInscripcion.idProcesosInscripcion=:proceso",Aspirante.class)
                    .setParameter("idPersona",persona.getIdpersona())
                    .setParameter("proceso", procesosInscripcion.getIdProcesosInscripcion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(aspirante1 ==null){
                DtoAspirante.AspiranteR nuevoAspirante = new DtoAspirante.AspiranteR(new Aspirante(),new TipoAspirante(),procesosInscripcion,Operacion.PERSISTIR,false);
                return ResultadoEJB.crearCorrecto(nuevoAspirante,"No se encontro aspirante");}
            else {
                DtoAspirante.AspiranteR nuevoAspirante = new DtoAspirante.AspiranteR(new Aspirante(),new TipoAspirante(),procesosInscripcion,Operacion.ACTUALIZAR,true);
                nuevoAspirante.setAspirante(aspirante1);
                nuevoAspirante.setProcesosInscripcion(procesosInscripcion);
                nuevoAspirante.setTipo(aspirante1.getTipoAspirante());
                nuevoAspirante.setOperacion(Operacion.ACTUALIZAR);
                nuevoAspirante.setEcontrado(true);
                return ResultadoEJB.crearCorrecto(nuevoAspirante,"Se ha encontrado registro del aspirante");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al bucar al aspirante(EjbRegistroFichaAdmision.getAspirantebyPersona).", e, null);
        }
    }

    /**
     * Busca domicilio por aspirante
     * @param aspiranteR Aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.DomicilioR> getDomiciliobyAspirante (@NonNull DtoAspirante.AspiranteR aspiranteR){
        try{
            DtoAspirante.DomicilioR domicilioR = new DtoAspirante.DomicilioR(new Domicilio(),false,Operacion.PERSISTIR,false);
            if(aspiranteR.getAspirante() ==null){return  ResultadoEJB.crearErroneo(2,domicilioR,"El aspirante no debe ser nulo");}
            Domicilio domicilio = new Domicilio();
            domicilio = em.createQuery("select d from Domicilio d where d.aspirante=:aspirante",Domicilio.class)
                    .setParameter("aspirante",aspiranteR.getAspirante().getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(domicilio ==null){ return ResultadoEJB.crearErroneo(3,domicilioR,"No se encontró domicilio del aspirante"); }
            else {
                domicilioR.setDomicilio(domicilio);
                domicilioR.setEcontrado(true);
                domicilioR.setIgualD(false);
                domicilioR.setOperacion(Operacion.ACTUALIZAR);
                return ResultadoEJB.crearCorrecto(domicilioR,"Se encontró domicilio");
            }
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al bucar el domicilio(EjbRegistroFichaAdmision.getDomiciliobyAspirante).", e, null);}
    }

    /**
     * Busca datos familiares del aspirante
     * @param a Aspirante
     * @return Resultado del proceso (DtoDatosFamiaresR)
     */
    public ResultadoEJB<DtoAspirante.FamiliaresR> getFamiliaresR(@NonNull Aspirante a) {
        try {
            DtoAspirante.TutorR tr = new DtoAspirante.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.FamiliaresR dtoDatosFamiliares = new DtoAspirante.FamiliaresR(new DatosFamiliares(), tr, new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            if(a==null){return ResultadoEJB.crearErroneo(2,dtoDatosFamiliares,"El aspirante no debe ser nulo.");}
            DatosFamiliares datosFamiliares = new DatosFamiliares();
            datosFamiliares  = em.createQuery("select a from DatosFamiliares a INNER JOIN a.aspirante1 p WHERE p.idAspirante=:idAspirante", DatosFamiliares.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(datosFamiliares!=null){
                dtoDatosFamiliares.setDatosFamiliares(datosFamiliares);
                dtoDatosFamiliares.setEscolaridadM(datosFamiliares.getEscolaridadMadre());
                dtoDatosFamiliares.setEscolaridadP(datosFamiliares.getEscolaridadPadre());
                dtoDatosFamiliares.setOcupacionM(datosFamiliares.getOcupacionMadre());
                dtoDatosFamiliares.setOcupacionP(datosFamiliares.getOcupacionPadre());
                ResultadoEJB<DtoAspirante.TutorR> resTutor = getTutorR(datosFamiliares);
                if(resTutor.getCorrecto()==true){
                    dtoDatosFamiliares.setTutorR(resTutor.getValor());
                }else {dtoDatosFamiliares.setTutorR(tr);}
                dtoDatosFamiliares.setOperacion(Operacion.ACTUALIZAR);
                dtoDatosFamiliares.setEcontrado(Boolean.TRUE);
                return ResultadoEJB.crearCorrecto(dtoDatosFamiliares, "DTOPersona Encontrados");
            }
            else {return ResultadoEJB.crearErroneo(3,dtoDatosFamiliares,"No se encontraron datos familiares del aspirante");}

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    /**
     * Busca datos académicos por aspirante
     * @param a Aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AcademicosR> getAcademicos(@NonNull Aspirante a) {
        try {
            DtoAspirante.AcademicosR dtoDa = new DtoAspirante.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            if(a==null){return ResultadoEJB.crearErroneo(2,dtoDa,"El aspirante no debe ser nulo");}
            DatosAcademicos da = new DatosAcademicos();
            da = em.createQuery("select d from DatosAcademicos d INNER JOIN d.aspirante1 a WHERE a.idAspirante=:idAspirante", DatosAcademicos.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (da!=null) {
                dtoDa.setAcademicos(da);
                dtoDa.setEspecialidad(da.getEspecialidadIems());
                dtoDa.setSistemaPo(da.getSistemaPrimeraOpcion());
                dtoDa.setSistemaSo(da.getSistemaSegundaOpcion());
                dtoDa.setUniversidad1(getOpcionArea(da.getPrimeraOpcion()).getValor());
                dtoDa.setUniversidad2(getOpcionArea(da.getSegundaOpcion()).getValor());
                ResultadoEJB<Iems> rejb = getIemsSeleccionada(da.getInstitucionAcademica());
                if (rejb.getCorrecto()) {
                    Iems i = rejb.getValor();
                    dtoDa.setIems(i);
                    dtoDa.setEstado(i.getLocalidad().getMunicipio().getEstado());
                    dtoDa.setMunicipio(i.getLocalidad().getMunicipio());
                    dtoDa.setLocalidad(i.getLocalidad());
                }
                dtoDa.setOperacion(Operacion.ACTUALIZAR);
                dtoDa.setEcontrado(Boolean.TRUE);
                return ResultadoEJB.crearCorrecto(dtoDa, "DTOPersona Encontrados");
            }
            else {return ResultadoEJB.crearErroneo(3,dtoDa,"No se encontraron datos académicos");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obener los datos académicos(EjbRegistroFichaAdmision.getAcademicos).", e, null);
        }
    }

    /**
     * Busca resultados de la encuesta del estudiante , si no los encuestra, los crea
     * @param a Aspirante
     * @return
     */
    public ResultadoEJB<DtoAspirante.EncuestaR> getEncuesta(@NonNull  Aspirante a) {
        try {
            DtoAspirante.EncuestaR rr = new DtoAspirante.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE);
            EncuestaAspirante encuestaAspirante = em.createQuery("select e from EncuestaAspirante e INNER JOIN e.aspirante a WHERE a.idAspirante=:idAspirante", EncuestaAspirante.class).setParameter("idAspirante", a.getIdAspirante()).getResultStream().findFirst().orElse(null);
            if(encuestaAspirante!=null) {
                EncuestaAspirante ea = encuestaAspirante;
                rr.setEncuestaAspirante(ea);
                rr.setOperacion(Operacion.ACTUALIZAR);
                rr.setEcontrado(Boolean.TRUE);
                return ResultadoEJB.crearCorrecto(rr, "Se encontró encuesta");
            }
            else {return ResultadoEJB.crearErroneo(2,rr,"No se encontro resultado");}

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }

    /////-------------------------------------------------------------------------------------------------------------------------

    /**
     * Busca al tutor familar según el aspirante
     * @param tutor Datos familiares del aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.TutorR> getTutorR(@NonNull DatosFamiliares tutor) {
        try {
            DtoAspirante.TutorR dtoTutor = new DtoAspirante.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
            if(tutor==null){return ResultadoEJB.crearErroneo(2,dtoTutor,"El tutor no debe ser nulo");}
            TutorFamiliar tutorFam = new TutorFamiliar();
            tutorFam = em.createQuery("select t from DatosFamiliares d INNER JOIN d.tutor t WHERE d.aspirante=:aspirante", TutorFamiliar.class)
                    .setParameter("aspirante", tutor.getAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (tutorFam!=null) {
                dtoTutor.setTutorFamiliar(tutorFam);
                dtoTutor.setOperacion(Operacion.ACTUALIZAR);
                dtoTutor.setEcontrado(Boolean.TRUE);
                return ResultadoEJB.crearCorrecto(dtoTutor, "TutorR Encontrados");
            }
            else {return ResultadoEJB.crearErroneo(3,dtoTutor,"No existe registro de tutor familiar del aspirante");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar TutorR (EjbRegistroFichaAdmision.getTutorR).", e, null);
        }
    }
    /**
     * Busca tipo de sangre por clave
     * @param clave Clave del tipo de sangre
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoSangre> getTipoSangrebyClave(@NonNull Short clave){
        try{
            if(clave==null){return ResultadoEJB.crearErroneo(2,new TipoSangre(),"La clave no debe ser nula");}
            TipoSangre ts = new TipoSangre();
            ts = em.createQuery("select t from TipoSangre t where t.idTipoSangre=:tipoSangre",TipoSangre.class)
                    .setParameter("tipoSangre",clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(ts ==null){return ResultadoEJB.crearErroneo(3,ts,"No se encontro el tipo de sangre");}
            else {return ResultadoEJB.crearCorrecto(ts,"Tipo de sangre encontrado");}
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener el tipo de sangre(EjbRegistroFichaAdmision.getTipoSangrebyClave).", e, null);}
    }

    /**
     * Busca el tipo de discapicidad por clave
     * @param clave Clave del tipo de discapicidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoDiscapacidad> getTipoDiscapacidadbyClave(@NonNull Short clave){
        try{
            if(clave==null){return ResultadoEJB.crearErroneo(2,new TipoDiscapacidad(),"La clave no debe ser nula");}
            TipoDiscapacidad ts = new TipoDiscapacidad();
            ts = em.createQuery("select t from TipoDiscapacidad t where t.idTipoDiscapacidad=:tipoD",TipoDiscapacidad.class)
                    .setParameter("tipoD",clave)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(ts ==null){return ResultadoEJB.crearErroneo(3,ts,"No se encontro el tipo de sangre");}
            else {return ResultadoEJB.crearCorrecto(ts,"Tipo de sangre encontrado");}
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener el tipo de sangre(EjbRegistroFichaAdmision.getTipoSangrebyClave).", e, null);}
    }

    /**
     * Obtiene el tipo de aspirante de nuevo ingreso
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoAspirante> getTipoAspiranteNi(){
        try{
            TipoAspirante tipoAspirante = new TipoAspirante();
            tipoAspirante = em.createQuery("select t from TipoAspirante t where t.idTipoAspirante=1",TipoAspirante.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(tipoAspirante==null){return ResultadoEJB.crearErroneo(2,tipoAspirante,"No se encontro el tipo de aspirante.");}
            else {return ResultadoEJB.crearCorrecto(tipoAspirante,"Tipo de aspirante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el tipo de aspirante (EjbRegistroFichaAdmision.getTipoAspiranteNi).", e, null);
        }
    }


    ///////--------------------------------- Busquedas para los menús ---------------------------------

    /**
     * Obtiene una lista de paises
     *
     * @return Resulltado del proceso (Lista de paises)
     */
    public ResultadoEJB<List<Pais>> getPaises() {
        try {
            List<Pais> paises = new ArrayList<>();
            paises = em.createQuery("select p from Pais p ", Pais.class)
                    .getResultList()
            ;
            if (paises == null) {
                return ResultadoEJB.crearErroneo(2, paises, "No se encontraron los países");
            } else {
                return ResultadoEJB.crearCorrecto(paises, "Lista de países");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbRegistroFichaAdmision.verificaEvento).", e, null);
        }
    }

    /**
     * Obtiene una lista de estados por países
     *
     * @param pais Pais seleccionado
     * @return Resultado del proceso (Lista de estados)
     */
    public ResultadoEJB<List<Estado>> getEstadosbyPais(Pais pais) {
        try {
            List<Estado> estados = new ArrayList<>();
            if (pais == null) {
                return ResultadoEJB.crearErroneo(2, estados, "El país no debe ser nulo");
            }
            estados = em.createQuery("select e from Estado e where e.idpais.idpais=:idPais", Estado.class)
                    .setParameter("idPais", pais.getIdpais())
                    .getResultList()
            ;
            if (estados == null) {
                return ResultadoEJB.crearErroneo(3, estados, "No se encontraron estados");
            } else {
                return ResultadoEJB.crearCorrecto(estados, "Lista de estados");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de estados por país(EjbRegistroFichaAdmision.getEstadosbyPais).", e, null);
        }
    }

    /**
     * Obitiene una lista de municipios por estado
     *
     * @param claveEstado Estado seleccionado
     * @return Resultado del proceso (Lista de Municipios)
     */

    public ResultadoEJB<List<Municipio>> getMunicipiosbyClaveEstado(Integer claveEstado) {
        try {
            List<Municipio> municipios = new ArrayList<>();
            if (claveEstado == null) {
                return ResultadoEJB.crearErroneo(2, municipios, "El estado no debe ser nulo");
            }
            municipios = em.createQuery("select m from Municipio m where m.municipioPK.claveEstado=:claveEstado", Municipio.class)
                    .setParameter("claveEstado", claveEstado)
                    .getResultList()
            ;
            if (municipios == null) {
                return ResultadoEJB.crearErroneo(4, municipios, "No se encontraron municipios");
            } else {
                return ResultadoEJB.crearCorrecto(municipios, "Lista de municipios");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de municipios por estado(EjbRegistroFichaAdmision.getMunicipiosbyEstado).", e, null);
        }
    }

    /**
     * Obtiene una lista de localidades por municipio
     *
     * @param claveEstado    Clave Estado
     * @param claveMunicipio Clave Municipio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Localidad>> getLocalidadByMunicipio(Integer claveEstado, Integer claveMunicipio) {
        try {
            List<Localidad> localidades = new ArrayList<>();
            if (claveMunicipio == null) {
                return ResultadoEJB.crearErroneo(2, localidades, "El municipio no debe ser nulo");
            }
            localidades = em.createQuery("select l from Localidad l where  l.localidadPK.claveEstado=:claveEstado and l.localidadPK.claveMunicipio=:claveMunicipio", Localidad.class)
                    .setParameter("claveEstado", claveEstado)
                    .setParameter("claveMunicipio", claveMunicipio)
                    .getResultList()
            ;
            if (localidades == null) {
                return ResultadoEJB.crearErroneo(3, localidades, "No se encontraron localidades");
            } else {
                return ResultadoEJB.crearCorrecto(localidades, "Lista de localidades");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de localidades por municipio(EjbRegistroFichaAdmision.getLocalidadByMunicipio).", e, null);
        }
    }

    /**
     * Obtiene una lista de asentamientos por municipio
     *
     * @param claveEstado    clave de Estado seleccionado
     * @param claveMunicipio clave Municipio seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<List<Asentamiento>> getAsentamientosbyMunicipio(Integer claveEstado, Integer claveMunicipio) {
        try {
            List<Asentamiento> asentamientos = new ArrayList<>();
            if (claveEstado == null) {
                return ResultadoEJB.crearErroneo(2, asentamientos, "El estado no debe ser nulo");
            }
            if (claveMunicipio == null) {
                return ResultadoEJB.crearErroneo(2, asentamientos, "El municipio no debe ser nulo");
            }
            asentamientos = em.createQuery("select a from Asentamiento a where  a.asentamientoPK.estado=:claveEstado and a.asentamientoPK.municipio=:municipio", Asentamiento.class)
                    .setParameter("claveEstado", claveEstado)
                    .setParameter("municipio", claveMunicipio)
                    .getResultList()
            ;
            if (asentamientos == null) {
                return ResultadoEJB.crearErroneo(3, asentamientos, "No se encontraron asentamientos");
            } else {
                return ResultadoEJB.crearCorrecto(asentamientos, "Lista de asentamientos");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de asentamientos por municipio(EjbRegistroFichaAdmision.getAsentamientosbyMunicipio).", e, null);
        }
    }

    /***
     * Obtiene una lista de tipos de sangre
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoSangre>> getTiposSangre() {
        try {
            List<TipoSangre> tiposSangre = new ArrayList<>();
            tiposSangre = em.createQuery("select t from TipoSangre t order by t.nombre desc", TipoSangre.class)
                    .getResultList()
            ;
            if (tiposSangre == null) {
                return ResultadoEJB.crearErroneo(2, tiposSangre, "No se encontraron tipos de sangre");
            } else {
                return ResultadoEJB.crearCorrecto(tiposSangre, "Lista de tipos de sangre");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de tipos de sangre (EjbRegistroFichaAdmision.getTiposSangre).", e, null);
        }
    }

    /**
     * Obtiene una lista de tipos de discapacidad
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoDiscapacidad>> getTiposDiscapacidad() {
        try {
            List<TipoDiscapacidad> tiposDiscapicidad = new ArrayList<>();
            tiposDiscapicidad = em.createQuery("select t from TipoDiscapacidad t order by t.nombre desc", TipoDiscapacidad.class)
                    .getResultList()
            ;
            if (tiposDiscapicidad == null) {
                return ResultadoEJB.crearErroneo(2, tiposDiscapicidad, "No se encontraron tipos de discapacidad");
            } else {
                return ResultadoEJB.crearCorrecto(tiposDiscapicidad, "Lista de tipos de discapacidad");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de tipos discapacidad (EjbRegistroFichaAdmision.getTiposDiscapacidad).", e, null);

        }
    }

    /**
     * Obtiene la lista de ocupaciones
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Ocupacion>> getOcupaciones() {
        try {
            List<Ocupacion> ocupacions = new ArrayList<>();
            ocupacions = em.createQuery("select o from Ocupacion o order by o.descripcion desc ", Ocupacion.class)
                    .getResultList()
            ;
            if (ocupacions == null) {
                return ResultadoEJB.crearErroneo(2, ocupacions, "No se encontraron ocuapaciones");
            } else {
                return ResultadoEJB.crearCorrecto(ocupacions, "Lista de ocupaciones");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista ocuapaciones(EjbRegistroFichaAdmision.getOcupaciones).", e, null);
        }
    }

    /**
     * Obtiene la lista de escolaridades
     *
     * @return Resultado del proceso (Lista de escolaridades)
     */
    public ResultadoEJB<List<Escolaridad>> getEscolaridad() {
        try {
            List<Escolaridad> escolaridads = new ArrayList<>();
            escolaridads = em.createQuery("select e from Escolaridad e order by e.descripcion desc ", Escolaridad.class)
                    .getResultList()
            ;
            if (escolaridads == null) {
                return ResultadoEJB.crearErroneo(2, escolaridads, "No se encontraron escolaridades");
            } else {
                return ResultadoEJB.crearCorrecto(escolaridads, "Lista de escolaridades");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista escolaridades(EjbRegistroFichaAdmision.getEscolaridad).", e, null);
        }
    }

    /**
     * Obtiene la lista de lenguas indigenas
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<LenguaIndigena>> getLenguasIndigenas() {
        try {
            List<LenguaIndigena> lenguasIndigenas = new ArrayList<>();
            lenguasIndigenas = em.createQuery("select l from LenguaIndigena l order by l.nombre desc", LenguaIndigena.class)
                    .getResultList()
            ;
            if (lenguasIndigenas == null) {
                return ResultadoEJB.crearErroneo(2, lenguasIndigenas, "No se encontraron lenguas idigenas");
            } else {
                return ResultadoEJB.crearCorrecto(lenguasIndigenas, "Lista de lenguas indigenas");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de lenguas indigenas(EjbRegistroFichaAdmision.getLenguasIndigenas).", e, null);
        }
    }

    /**
     * Obtiene la lista de Medios de difusion
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<MedioDifusion>> getMediosDifusion() {
        try {
            List<MedioDifusion> mediosDifusion = new ArrayList<>();
            mediosDifusion = em.createQuery("select m from MedioDifusion  m order by m.descripcion desc", MedioDifusion.class)
                    .getResultList()
            ;
            if (mediosDifusion == null) {
                return ResultadoEJB.crearErroneo(2, mediosDifusion, "No se encontraron medios de disfusión");
            } else {
                return ResultadoEJB.crearCorrecto(mediosDifusion, "Lista de medios de difusión");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de medios de difusión(EjbRegistroFichaAdmision.getMediosDifusion).", e, null);
        }
    }

    /**
     * Obtiene la lista de especialidades
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EspecialidadCentro>> getEspecialidades() {
        try {
            List<EspecialidadCentro> especialidadCentros = new ArrayList<>();
            especialidadCentros = em.createQuery("select e from EspecialidadCentro e  order by e.nombre asc ", EspecialidadCentro.class)
                    .getResultList()
            ;
            if (especialidadCentros == null) {
                return ResultadoEJB.crearErroneo(2, especialidadCentros, "No se encontraron especialidades");
            } else {
                return ResultadoEJB.crearCorrecto(especialidadCentros, "Lista de especialidades");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de especialidades(EjbRegistroFichaAdmision.getEspecialidades).", e, null);
        }

    }

    /**
     * Obtiene una lista de sistemas
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Sistema>> getSistemas() {
        try {
            List<Sistema> sistemas = new ArrayList<>();
            sistemas = em.createQuery("select s from Sistema s order by s.nombre desc", Sistema.class)
                    .getResultList()
            ;
            if (sistemas == null) {
                return ResultadoEJB.crearErroneo(2, sistemas, "No se encontraron sistemas.");
            } else {
                return ResultadoEJB.crearCorrecto(sistemas, "Lista sistemas");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de sistemas(EjbRegistroFichaAdmision.getSistemas).", e, null);
        }
    }

    /**
     * Obtiene una lista de iems por municipio, estado y localidad
     *
     * @param estadoIems     Estado seleccionado
     * @param municipioIemes Municipio seleccionado
     * @param localidadIems  Loclidad seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Iems>> getIemsbyEstadoMunicipio(Estado estadoIems, Municipio municipioIemes, Localidad localidadIems) {
        try {
            List<Iems> iems = new ArrayList<>();
            if (estadoIems == null) {
                return ResultadoEJB.crearErroneo(2, iems, "El estado no debe ser nulo");
            }
            if (municipioIemes == null) {
                return ResultadoEJB.crearErroneo(3, iems, "El municipio no debe ser nulo");
            }
            if (localidadIems == null) {
                return ResultadoEJB.crearErroneo(4, iems, "La localidad no debe ser nulo");
            }
            iems = em.createQuery("select i from Iems i where i.localidad.municipio.estado.idestado=:idEstado and i.localidad.municipio.municipioPK.claveMunicipio=:idMunicipio and i.localidad.localidadPK.claveLocalidad=:idLocalidad", Iems.class)
                    .setParameter("idEstado", estadoIems.getIdestado())
                    .setParameter("idMunicipio", municipioIemes.getMunicipioPK().getClaveMunicipio())
                    .setParameter("idLocalidad", localidadIems.getLocalidadPK().getClaveLocalidad())
                    .getResultList()
            ;
            if (iems == null) {
                return ResultadoEJB.crearErroneo(5, iems, "No se encontraron iems");
            } else {
                return ResultadoEJB.crearCorrecto(iems, "Lista de iems");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de Iems(EjbRegistroFichaAdmision.getIemsbyEstadoMunicipio).", e, null);
        }
    }

    /**
     * Obtiene la lista de areas académicas
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasAcademicas() {
        try {
            List<AreasUniversidad> areasAcademicas = new ArrayList<>();
            areasAcademicas = em.createQuery("select a from AreasUniversidad  a where a.areaSuperior=:area and a.categoria.categoria=:categoria order by a.nombre desc", AreasUniversidad.class)
                    .setParameter("area", 2)
                    .setParameter("categoria", 8)
                    .getResultList()
            ;
            if (areasAcademicas == null) {
                return ResultadoEJB.crearErroneo(2, areasAcademicas, "No se encontraron areas académicas");
            } else {
                return ResultadoEJB.crearCorrecto(areasAcademicas, "Lista de areas académicas");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de areas académicas(EjbRegistroFichaAdmision.getAreasAcademicas).", e, null);
        }
    }

    /**
     * Obtiene una lista de programas educativos activos nivel TSU
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasE() {
        try {
            List<AreasUniversidad> areasUniversidads = em.createQuery("select t from AreasUniversidad t INNER JOIN t.categoria c WHERE c.categoria=:categoria AND t.vigente=:vigente AND t.nivelEducativo.nivel='TSU'", AreasUniversidad.class)
                    .setParameter("categoria", Short.parseShort("9"))
                    .setParameter("vigente", "1")
                    .getResultList();
            if (areasUniversidads == null) {
                return ResultadoEJB.crearErroneo(3, areasUniversidads, "No se encontraron programas educativos");
            } else {
                return ResultadoEJB.crearCorrecto(areasUniversidads, "Lista de programas educativos");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener la lista de programas educativos(EjbRegistroFichaAdmision.getProgramasE).", e, null);
        }
    }

    /**
     * Busca Area por clave
     * @param clave Clave del área
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getOpcionArea(@NonNull Short clave) {
        try {
            if(clave==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del área no debe ser nula");}
            AreasUniversidad areasS = ejbAreasLogeo.mostrarAreasUniversidad(clave);
            AreasUniversidad areasO = ejbAreasLogeo.mostrarAreasUniversidad(areasS.getAreaSuperior());

            return ResultadoEJB.crearCorrecto(areasO, "Area Encontrados");

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbRegistroFichaAdmision.getOpcionArea).", e, null);
        } catch (Throwable ex) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbRegistroFichaAdmision.getOpcionArea).", ex, null);
        }
    }

    /**
     * Busca Iems por clave
     * @param iems Clave de Iems
     * @return Resultado del proceso
     */
    public ResultadoEJB<Iems> getIemsSeleccionada(@NonNull Integer iems) {
        try {
            Iems iemses = em.createQuery("select t from Iems t WHERE t.iems=:iems", Iems.class)
                    .setParameter("iems", iems)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(iemses!=null){
                return ResultadoEJB.crearCorrecto(iemses,"Iems encontrada");
            }
            else {return ResultadoEJB.crearErroneo(3,new Iems(),"No se encontró el Iems");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar la Iems (EjbRegistroFichaAdmision.getIemsSeleccionada).", e, null);
        }
    }

    /**
     * Obtiene un país por clave
     *
     * @param clavePais Clave del país
     * @return Resultado del proceso
     */
    public ResultadoEJB<Pais> getPaisbyClave(Integer clavePais) {
        try {
            if (clavePais == null) {
                return ResultadoEJB.crearErroneo(2, new Pais(), "La clave del país no debe ser nulo");
            }
            Pais pais = new Pais();
            pais = em.createQuery("select p from Pais p where p.idpais=:clavePais", Pais.class)
                    .setParameter("clavePais", clavePais)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if (pais == null) {
                return ResultadoEJB.crearErroneo(3, pais, "No se encontro país");
            } else {
                return ResultadoEJB.crearCorrecto(pais, "Pais encontrado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener el país por clave(EjbRegistroFichaAdmision.getPaisbyClave).", e, null);
        }
    }

    /**
     * Obtiene lista de generos
     *
     * @return
     */
    public ResultadoEJB<List<Generos>> getGeneros() {
        try {
            List<Generos> generos = new ArrayList<>();
            generos = em.createQuery("select g from Generos  g order by g.nombre desc", Generos.class)
                    .getResultList();
            if (generos == null) {
                return ResultadoEJB.crearErroneo(2, generos, "No se econtraron generos");
            } else {
                return ResultadoEJB.crearCorrecto(generos, "Lista de generos");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obetener lista de generos(EjbRegistroFichaAdmision.getGeneros).", e, null);
        }
    }
    /**
     * Obtiene un país segun la clave del estado
     *
     * @param claveEstado Clave del estado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Pais> getPaisOrigenByEstado(Integer claveEstado) {
        try {
            if (claveEstado == null) {
                return ResultadoEJB.crearErroneo(2, new Pais(), "La clave del estado no debe ser nula");
            }
            Estado estado = em.createQuery("select e from Estado e where e.idestado=:estado", Estado.class)
                    .setParameter("estado", claveEstado)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (estado != null) {
                Pais pais = em.createQuery("select p from Pais p where p.idpais=:pais", Pais.class)
                        .setParameter("pais", estado.getIdpais().getIdpais())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if (pais != null) {
                    return ResultadoEJB.crearCorrecto(pais, "País encontrado");
                } else {
                    return ResultadoEJB.crearErroneo(3, pais, "No se encontro el país");
                }
            } else {
                return ResultadoEJB.crearErroneo(4, new Pais(), "Error al obtener el estado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el país de origen(EjbRegistroFichaAdmision.getPaisOrigenByEstado).", e, null);
        }
    }


}
