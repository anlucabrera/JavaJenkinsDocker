package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAspiranteIng;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCitaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;
import mx.edu.utxj.pye.sgi.util.EnvioCorreos;

import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "EjbRegistroFichaIngenieria")
public class EjbRegistroFichaIngenieria {
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbRegistroFichaAdmision ejbRegistroFichaAdmision;

    private EntityManager em;
    private  EntityManager emS;

    @PostConstruct
    public void init() {
        em = f.getEntityManager(); emS = f2.getEntityManager();
    }

    /**
     * Verifica el acceso al modulo
     * Valida si el estudiante esta registrado en CE
     * @param matricula
     * @return
     */
    public ResultadoEJB<DtoAspiranteIng> verificarAcceso(@NonNull String matricula){
        try{
            DtoAspiranteIng dto = new DtoAspiranteIng();
            if(matricula==null){return ResultadoEJB.crearErroneo(2,new DtoAspiranteIng(),"La matricula no debe ser nula");}
            //Verifica estudiante en CE
            ResultadoEJB<Estudiante> resEstudianteCE = verificaEstudianteCE(matricula);
            if(resEstudianteCE.getCorrecto()){
                //Es estudiante registrado en CE
                dto.setEstudianteCE(resEstudianteCE.getValor());
                dto.setTipoAspirante(AspiranteTipoIng.ASPIRANTE_ING);
                return ResultadoEJB.crearCorrecto(dto,"Estudiante CE");
            }else {return ResultadoEJB.crearErroneo(2,dto,"Estudiante no validado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el estudiante (EjbResgitroFichaIngenieria.verificarAcceso).", e, null);

        }
    }

    /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }

    /**
     * Verifica acceso de estudiante de SAIIUT o de Otra UT
     * @param matricula
     * @return
     */
    public ResultadoEJB<DtoAspiranteIng> verificarAcceso2(String matricula, @NonNull UsuarioTipo tipo){
        try{
            DtoAspiranteIng dto = new DtoAspiranteIng();
            if(tipo.equals(UsuarioTipo.ESTUDIANTE)){
                //validación SAIIUT
                ResultadoEJB<Alumnos> resSAIIUT = getEstudianteSaiiut(matricula);
                if(resSAIIUT.getCorrecto()){
                    dto.setMatricula(resSAIIUT.getValor().getMatricula());
                    dto.setEstudianteSAIIUT(resSAIIUT.getValor());
                    dto.setTipoAspirante(AspiranteTipoIng.ASPPIRANTE_ING_OTRA_GENERACION);
                    return ResultadoEJB.crearCorrecto(dto,"");
                }else {return ResultadoEJB.crearErroneo(3,dto,"Error al verificar estudiante en SAIIUT");}
            }else if(tipo.equals(UsuarioTipo.ASPIRANTEING)){
                dto.setMatricula("Por definir");
                dto.setTipoAspirante(AspiranteTipoIng.ASPPIRANTE_ING_OTRA_UT);
                return ResultadoEJB.crearCorrecto(dto,"Aspirante de otra UT");
            }else {return ResultadoEJB.crearErroneo(2,dto,"El tipo de usuario no debe ser nulo");}


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el estudiante (EjbResgitroFichaIngenieria.verificarAcceso).", e, null);

        }
    }

    /**
     * Verifica que el estudiante esté registrado en SAIIUT
     * Estudiante con estatus 6 (Egresado no titulado)
     * Ultimo grado 6
     * @param matricula matricula del estudiante logueado
     * @return
     */
    public ResultadoEJB<Alumnos> getEstudianteSaiiut(@NonNull String matricula){
        try{
            if(matricula==null){return ResultadoEJB.crearErroneo(2,new Alumnos(),"La matricula no debe ser nula");}
            Alumnos alumno = new Alumnos();
            List<Alumnos> listAlum= new ArrayList<>();
            alumno = emS.createQuery("select a from Alumnos a where a.matricula=:matricula order by a.gradoActual desc", Alumnos.class)
                    .setParameter("matricula",matricula)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(alumno==null){
                return ResultadoEJB.crearErroneo(4,new Alumnos(),"No se encontró al estudiante");}
            else if(alumno.getGradoActual()==11){
                return ResultadoEJB.crearErroneo(3,new Alumnos(),"El estudiante es egresado de Lic / Ing");}
            else if(alumno.getGradoActual()==6){
                return ResultadoEJB.crearCorrecto(alumno,"Estudiante egresado TSU");}
            else {
                return ResultadoEJB.crearErroneo(5,alumno,"El estudiante no es egresado de TSU");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el estudiante en SAIIUT (EjbResgitroFichaIngenieria.getEstudianteSaiiut)", e, null);

        }
    }

    /**
     * Verifica que el estudiante loggeado sea un estudiante egresado (Control Escolar)
     * @param matricula matricula del estudiante
     * @return Resultado del proceso
     */

    public ResultadoEJB<Estudiante> verificaEstudianteCE(@NonNull String matricula){
        try{
            //System.out.println("EjbRegistroFichaIngenieria.verificaEstudianteCE" + matricula);
            if(matricula ==null){return ResultadoEJB.crearErroneo(2,new Estudiante(),"La matricula no debe ser nula");}
            Short tipo =1;
            Integer grado =6;
            Estudiante e = em.createQuery("select e from Estudiante  e where e.matricula=:matricula order by e.periodo desc ", Estudiante.class)
                    .setParameter("matricula",Integer.parseInt( matricula))
                    .getResultStream()
                    .findFirst()
                    .orElse(new Estudiante())
                    ;


            //System.out.println("EjbRegistroFichaIngenieria.verificaEstudianteCE" + e);
            if(e !=null){
                if(e.getTipoEstudiante().getIdTipoEstudiante().equals(tipo) & e.getGrupo().getGrado()==grado){
                    return ResultadoEJB.crearCorrecto(e,"Estudiante ");
                }else {return ResultadoEJB.crearErroneo(4,e,"Estudiante no egresado");}
            }
            else {return ResultadoEJB.crearErroneo(3,e,"No se encontró estudiante CE");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el estudiante (EjbResgitroFichaIngenieria.verificaEstudianteCE).", e, Estudiante.class);

        }
    }

    /**
     * Verifica el evento de reguistro de fichas de admisión para nivel ingeniería
     * @return
     */

    public ResultadoEJB<EventoEscolar> verficarEventoRegistro(){
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REGISTRO_FICHAS_ADMISION_ING);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision de ingeniería (EjbResgitroFichaIngenieria.verificaEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Obtiene el proceso de inscripción de Ingeniería activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where :fecha between p.fechaInicio and p.fechaFin and p.activoIng=:tipo", ProcesosInscripcion.class)
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
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el proceso de inscripcion activo(EjbResgitroFichaIngenieria.getProcesosInscripcionActivo).", e, null);
        }
    }

    /**
     * Obtiene el evento
     * @return
     */
    public ResultadoEJB<EventoEscolar> getEventoValidacionFicha (){
        try{
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.VALIDACION_FICHA_INGENIERIA);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento de valiadación de ficha(EjbResgitroFichaIngenieria.getEventoValidacionFicha).", e, null);
        }
    }

    public ResultadoEJB<EventoEscolar> getUltimoEventoValidacionFicha (@NonNull EventoEscolar eventoRegistro){
        try{
            //verificar apertura del evento
            EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar e where e.tipo=:tipo and e.periodo=:periodo", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.VALIDACION_FICHA_INGENIERIA.getLabel())
                    .setParameter("periodo",eventoRegistro.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2,eventoEscolar, "No existe evento aperturado del tipo solicitado.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento escolar (EjbEventoEscolar.verificarEventoAperturado).", e, EventoEscolar.class);
        }
    }
    public ResultadoEJB<EventoEscolar> getIncripcionIng(EventoEscolar periodoEvento){
        try{
            //verificar apertura del evento
            EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar e where e.tipo=:tipo and e.periodo=:periodo", EventoEscolar.class)
                    .setParameter("tipo", EventoEscolarTipo.INSCRIPCION_INGENIERIA.getLabel())
                    .setParameter("periodo",periodoEvento.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2,eventoEscolar, "No existe evento aperturado del tipo solicitado.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento escolar (EjbEventoEscolar.verificarEventoAperturado).", e, EventoEscolar.class);
        }
    }
    /**
     * Busca a un aspirante de ingeniería por id de persona y el proceso de de inscripción activo
     * @param persona Persona registrada
     * @param procesosInscripcion Proceso de inscripción activo
     * @return Resultados del proceso (C-> Dto Aspirante)
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> getAspirantebyPersona(@NonNull Persona persona, @NonNull ProcesosInscripcion procesosInscripcion){
        try{
            DtoAspirante.AspiranteR ar = new DtoAspirante.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
            if(procesosInscripcion==null){return ResultadoEJB.crearErroneo(3,ar,"El proceso de inscripción no debe ser nulo");}
            if(persona==null){return ResultadoEJB.crearErroneo(4,ar,"La persona no debe ser nulo");}
            Aspirante aspirante1 = new Aspirante();
            aspirante1 = em.createQuery("select a from Aspirante a where a.idPersona.idpersona=:idPersona and a.idProcesoInscripcion.idProcesosInscripcion=:proceso and a.tipoAspirante.idTipoAspirante=:tipo", Aspirante.class)
                    .setParameter("idPersona",persona.getIdpersona())
                    .setParameter("proceso", procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("tipo",3)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(aspirante1 ==null){
                DtoAspirante.AspiranteR nuevoAspirante = new DtoAspirante.AspiranteR(new Aspirante(),new TipoAspirante(),procesosInscripcion, Operacion.PERSISTIR,false);
                return ResultadoEJB.crearCorrecto(nuevoAspirante,"No se encontro aspirante");}
            else {
                DtoAspirante.AspiranteR nuevoAspirante = new DtoAspirante.AspiranteR(new Aspirante(),new TipoAspirante(),procesosInscripcion, Operacion.ACTUALIZAR,true);
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
     * Obtiene el registro del aspirante anterior (Nuevo ingreso)
     * @param persona Persona
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> getAspiranteAnterior (@NonNull Persona persona){
        try{
            DtoAspirante.AspiranteR ar = new DtoAspirante.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
            if(persona ==null){return ResultadoEJB.crearErroneo(2,ar,"La persona no debe ser nulo" );}
            Aspirante aspirante1 = new Aspirante();
            aspirante1 = em.createQuery("select a from Aspirante a where a.idPersona.idpersona=:idPersona and a.tipoAspirante.idTipoAspirante=:tipo", Aspirante.class)
                    .setParameter("idPersona",persona.getIdpersona())
                    .setParameter("tipo", 1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(aspirante1!=null){
                ar.setAspirante(aspirante1);
                ar.setProcesosInscripcion(aspirante1.getIdProcesoInscripcion());
                ar.setTipo(aspirante1.getTipoAspirante());
                ar.setOperacion(Operacion.ACTUALIZAR);
                ar.setEcontrado(true);
                return ResultadoEJB.crearCorrecto(ar,"Registro anterior");
            }else {return ResultadoEJB.crearErroneo(3,ar,"No se encontró registro anterior de la persona");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al bucar al aspirante anterior (EjbRegistroFichaAdmision.getAspiranteAnterior).", e, null);
        }
    }
/*
    public ResultadoEJB<DtoAspirante.General> getDtoApiranteGeneral(Persona persona) {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(1)");
            Boolean encontrado=Boolean.FALSE;
            DtoAspirante.PersonaR pr = new DtoAspirante.PersonaR(new Persona(), new MedioComunicacion(), new Pais(), Operacion.PERSISTIR, Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.AspiranteR ar = new DtoAspirante.AspiranteR(new Aspirante(), new TipoAspirante(), new ProcesosInscripcion(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.MedicosR mr = new DtoAspirante.MedicosR(new DatosMedicos(), new TipoSangre(), new TipoDiscapacidad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.TutorR tr = new DtoAspirante.TutorR(new TutorFamiliar(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.FamiliaresR fr = new DtoAspirante.FamiliaresR(new DatosFamiliares(), tr, new Ocupacion(), new Ocupacion(), new Escolaridad(), new Escolaridad(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.DomicilioR dr = new DtoAspirante.DomicilioR(new Domicilio(), Boolean.FALSE, Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.AcademicosR ac = new DtoAspirante.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.EncuestaR er = new DtoAspirante.EncuestaR(new EncuestaAspirante(), new LenguaIndigena(), new MedioDifusion(), Operacion.PERSISTIR, Boolean.FALSE);
            DtoAspirante.EncuestaVocacionalR ev= new DtoAspirante.EncuestaVocacionalR(new EncuestaVocacional(), new AreasUniversidad(), Operacion.PERSISTIR,Boolean.FALSE);
            DtoAspirante.General rr=new DtoAspirante.General(pr, ar, mr, fr, tr, dr, ac, er,ev, encontrado);
             System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(2)");
            if(persona.getCurp()==null){return ResultadoEJB.crearErroneo(2,rr,"El CURP no debe ser nulo");}
            // Obtiene persona por curp
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(2.2)"+ persona);
            ResultadoEJB<DtoAspirante.PersonaR> resPR = ejbRegistroFichaAdmision.getPersonaR(persona);
            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(2.3)");

            if (resPR.getCorrecto()) {
                System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(3)");
                pr = resPR.getValor();
                if (pr.getEcontrado()) {
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(4)");
                    ResultadoEJB<DtoAspirante.AspiranteR> resAR = getAspirantebyPersona(persona);
                    if (resAR.getCorrecto()) {
                        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(5)");
                        ar = resAR.getValor();
                        if (ar.getEcontrado()) {
                            ResultadoEJB<DtoAspirante.EncuestaVocacionalR> resEn = ejbRegistroFichaAdmision.getEncuestaVocacionalbyPersona(persona);
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(6)");
                            ResultadoEJB<DtoAspirante.FamiliaresR> resFR = ejbRegistroFichaAdmision.getFamiliaresR(ar.getAspirante());
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(6.5)");
                            ResultadoEJB<DtoAspirante.AcademicosR> resAC = ejbRegistroFichaAdmision.getAcademicos(ar.getAspirante());
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(7)");
                            ResultadoEJB<DtoAspirante.DomicilioR> resDR = ejbRegistroFichaAdmision.getDomiciliobyAspirante(ar.getAspirante());
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(8)");
                            ResultadoEJB<DtoAspirante.EncuestaR> resER = ejbRegistroFichaAdmision.getEncuesta(ar.getAspirante());
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(9)");
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(10)");
                            if(resEn.getCorrecto()){ev= resEn.getValor();}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener los datos de la encuesta vocacional");}
                            if (resFR.getCorrecto()) {fr = resFR.getValor();if(fr.getEcontrado()){tr=fr.getTutorR();}}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener los datos familiares");}
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(11)");
                            if (resAC.getCorrecto()) {ac = resAC.getValor();}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener los datos académicos");}
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(12)");
                            if (resDR.getCorrecto()) {dr = resDR.getValor();}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener los datos del domicilio");}
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(13)");
                            if (resER.getCorrecto()) {er = resER.getValor();}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener la encuesta del aspirante");}
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(14)");

                        }
                    }
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReincorporacion.getDtoReincorporacion(17)");
                    ResultadoEJB<DtoAspirante.MedicosR> resMR = ejbRegistroFichaAdmision.getDatosMedicosbyPersona(pr);

                    if (resMR.getCorrecto()) {mr = resMR.getValor();}else {return ResultadoEJB.crearErroneo(3,rr,"Error al obtener los datos médicos");}
                    encontrado=Boolean.TRUE;
                }
            }else {return ResultadoEJB.crearErroneo(4,rr,"Error al obtener los datos de la persona");}
            return ResultadoEJB.crearCorrecto(rr,"Dto Aspirante");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar DTOPersona (EjbReincorporacion.getPersonaR).", e, null);
        }
    }
    */


    /**
     * Obtiene la lista de ingenirías y licencituras vigentes
     * @return
     */
    public ResultadoEJB<List<AreasUniversidad>> getPE (){
        try{
            List<AreasUniversidad> pe = new ArrayList<>();
            pe = em.createQuery("select a from AreasUniversidad a where a.vigente=:vigente and (a.nivelEducativo.nivel=:nivel or a.nivelEducativo.nivel=:nivel2)", AreasUniversidad.class)
                    .setParameter("vigente","1")
                    .setParameter("nivel","5A")
                    .setParameter("nivel2","5B")
                    .getResultList()
            ;
            if(pe.isEmpty() || pe==null){return ResultadoEJB.crearErroneo(2,pe,"No existen pe Ing o Lic");}
            else {return ResultadoEJB.crearCorrecto(pe,"Lista de ingenirías y licencituras");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los PE de la Ing y Lic (EjbRegistroFichaIngenieria.getPE).", e, null);

        }
    }
    /**
     * Obtien la lista de programas educativos nivel 5A y 5B
     * @return Resultado del proveso
     */
    public ResultadoEJB<List<AreasUniversidad>> getPEIng(){
        try{
            List<AreasUniversidad> pe = new ArrayList<>();
            pe = em.createQuery("select a from AreasUniversidad a where a.vigente=:vigente and (a.nivelEducativo.nivel=:nivel or a.nivelEducativo.nivel=:nivel2)", AreasUniversidad.class)
                    .setParameter("vigente","1")
                    .setParameter("nivel","5A")
                    .setParameter("nivel2","5B")
                    .getResultList()
            ;
            System.out.println("EjbRegistroFichaIngenieria.getPEIngbyArea Areas 5A y 5B" + pe);
            if(pe.isEmpty()||pe ==null ){return ResultadoEJB.crearErroneo(3,pe,"No exiten ingenirías");}
            else {return ResultadoEJB.crearCorrecto(pe,"Programas educativos nivel ingeniería");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los PE de la Ing (EjbRegistroFichaIngenieria.getPEIngbyArea).", e, null);
        }
    }

    /**
     * Obtien la lista de programas educativos según su programa educativo de egreso
     * @param peEgreso Programa eductivo egreso
     * @return Resultado del proveso
     */
    public ResultadoEJB<List<AreasUniversidad>> getPEIngbyArea(@NonNull AreasUniversidad peEgreso){
        try{
            if(peEgreso ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El programa educativo de egreso no debe ser nulo");}
            List<AreasUniversidad> pe = new ArrayList<>();
            pe = em.createQuery("select a from AreasUniversidad a where a.areaSuperior =:areaSup and a.vigente=:vigente and (a.nivelEducativo.nivel=:nivel or a.nivelEducativo.nivel=:nivel2)", AreasUniversidad.class)
                    .setParameter("areaSup",peEgreso.getAreaSuperior())
                    .setParameter("vigente","1")
                    .setParameter("nivel","5A")
                    .setParameter("nivel2","5B")
                    .getResultList()
            ;
            if(pe.isEmpty()||pe ==null ){return ResultadoEJB.crearErroneo(3,pe,"No exiten ingenirías");}
            else {return ResultadoEJB.crearCorrecto(pe,"Programas educativos nivel ingeniería");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los PE de la Ing (EjbRegistroFichaIngenieria.getPEIngbyArea).", e, null);
        }
    }
    public ResultadoEJB<AreasUniversidad> getAreabyClave(@NonNull Short clave){
        try{
            if(clave==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del área no debe ser nulo");}
            AreasUniversidad a = new AreasUniversidad();
            a= em.createQuery("select a from AreasUniversidad  a where a.area=:clave", AreasUniversidad.class)
                    .setParameter("clave", clave)
                    .getResultStream()
                    .findFirst().orElse(null)
            ;
            if(a==null){return ResultadoEJB.crearErroneo(3,new AreasUniversidad(),"No se encontró el área");}
            else{return ResultadoEJB.crearCorrecto(a,"área");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el área po clave(EjbRegistroFichaIngenieria.getAreabyClave).", e, null);

        }
    }
    /**
     * Obtiene el tipo de aspirante de ingeniería
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoAspirante> getTipoAspiranteIng(){
        try{
            TipoAspirante tipoAspirante = new TipoAspirante();
            tipoAspirante = em.createQuery("select t from TipoAspirante t where t.idTipoAspirante=3", TipoAspirante.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(tipoAspirante==null){return ResultadoEJB.crearErroneo(2,tipoAspirante,"No se encontro el tipo de aspirante.");}
            else {return ResultadoEJB.crearCorrecto(tipoAspirante,"Tipo de aspirante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el tipo de aspirante (EjbRegistroFichaAdmision.getTipoAspiranteNi).", e, null);
        }
    }

    /**
     * Obtiene los datos academicos del registro anterior del aspirante
     * @param a Aspirante anterior (Nuevo ingreso)
     * @param  estudiante Estudiante logueado
     * @return Resultado del proceso
     *
     */
    public ResultadoEJB<DtoAspirante.AcademicosR> getAcademicos(@NonNull Aspirante a, @NonNull Estudiante estudiante) {
        try {
            DtoAspirante.AcademicosR dtoDa = new DtoAspirante.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            if(a==null){return ResultadoEJB.crearErroneo(2,dtoDa,"El aspirante no debe ser nulo");}
            DatosAcademicos da = new DatosAcademicos();
            da = em.createQuery("select d from DatosAcademicos d where d.aspirante=:idAspirante", DatosAcademicos.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (da!=null) {
                dtoDa.setAcademicos(da);
                dtoDa.setEspecialidad(da.getEspecialidadIems());
                dtoDa.setSistemaPo(da.getSistemaPrimeraOpcion());
                dtoDa.setSistemaSo(da.getSistemaPrimeraOpcion());
                //Obtiene el programa educativo se seguimiento (Lic/Ing)
                ResultadoEJB<AreasUniversidad> resPe = getCarreraSeguimiento(estudiante.getCarrera());
                if(resPe.getCorrecto()){
                    dtoDa.getAcademicos().setPrimeraOpcion(resPe.getValor().getArea());
                    dtoDa.getAcademicos().setSegundaOpcion(resPe.getValor().getArea());
                    dtoDa.setUniversidad1(getOpcionAreaSup(resPe.getValor()).getValor());
                    dtoDa.setUniversidad2(getOpcionAreaSup(resPe.getValor()).getValor());
                    ResultadoEJB<Iems> rejb = ejbRegistroFichaAdmision.getIemsSeleccionada(da.getInstitucionAcademica());
                    if (rejb.getCorrecto()) {
                        Iems i = rejb.getValor();
                        dtoDa.setIems(i);
                        dtoDa.setEstado(i.getLocalidad().getMunicipio().getEstado());
                        dtoDa.setMunicipio(i.getLocalidad().getMunicipio());
                        dtoDa.setLocalidad(i.getLocalidad());
                    }
                    dtoDa.setOperacion(Operacion.ACTUALIZAR);
                    dtoDa.setEcontrado(Boolean.FALSE);
                    return ResultadoEJB.crearCorrecto(dtoDa, "DTOPersona Encontrados");
                }else {
                    return ResultadoEJB.crearErroneo(6,dtoDa,"Error al obtener el PE de seguimiento");}

            }
            else {
                return ResultadoEJB.crearErroneo(3,dtoDa,"No se encontraron datos académicos");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obener los datos académicos(EjbRegistroFichaAdmision.getAcademicos).", e, null);
        }
    }

    /**
     * Obtiene los datos academicos del registro anterior del aspirante
     * @param a Aspirante
     * @return Resultado del proceso
     *
     */
    public ResultadoEJB<DtoAspirante.AcademicosR> getAcademicosbyAspirante(@NonNull Aspirante a) {
        try {
            DtoAspirante.AcademicosR dtoDa = new DtoAspirante.AcademicosR(new DatosAcademicos(), new AreasUniversidad(), new AreasUniversidad(), new Sistema(), new Sistema(), new Estado(), new Municipio(), new Localidad(), new Iems(), new EspecialidadCentro(), Operacion.PERSISTIR, Boolean.FALSE);
            if(a==null){return ResultadoEJB.crearErroneo(2,dtoDa,"El aspirante no debe ser nulo");}
            DatosAcademicos da = new DatosAcademicos();
            da = em.createQuery("select d from DatosAcademicos d where d.aspirante=:idAspirante", DatosAcademicos.class)
                    .setParameter("idAspirante", a.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (da!=null) {
                dtoDa.setAcademicos(da);
                dtoDa.setEspecialidad(da.getEspecialidadIems());
                dtoDa.setSistemaPo(da.getSistemaPrimeraOpcion());
                dtoDa.setSistemaSo(da.getSistemaPrimeraOpcion());
                //Obtiene el programa educativo se seguimiento (Lic/Ing)
                dtoDa.setUniversidad1(getAreaSupbyPE(a.getDatosAcademicos().getPrimeraOpcion()).getValor());
                dtoDa.setUniversidad2(getAreaSupbyPE(a.getDatosAcademicos().getSegundaOpcion()).getValor());
                ResultadoEJB<Iems> rejb = ejbRegistroFichaAdmision.getIemsSeleccionada(da.getInstitucionAcademica());
                if (rejb.getCorrecto()) {
                    Iems i = rejb.getValor();
                    dtoDa.setIems(i);
                    dtoDa.setEstado(i.getLocalidad().getMunicipio().getEstado());
                    dtoDa.setMunicipio(i.getLocalidad().getMunicipio());
                    dtoDa.setLocalidad(i.getLocalidad());
                }
                dtoDa.setOperacion(Operacion.ACTUALIZAR);
                dtoDa.setEcontrado(Boolean.FALSE);
                return ResultadoEJB.crearCorrecto(dtoDa, "DTOPersona Encontrados");
            }
            else {
                return ResultadoEJB.crearErroneo(3,dtoDa,"No se encontraron datos académicos");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Error al obener los datos académicos(EjbRegistroFichaAdmision.getAcademicos).", e, null);
        }
    }
    /**
     * Obtiene la carrera de seguimiento  (Licencitura /Ingeniería) según su programa educativo de egreso
     * @param peEgreso Programa educativp de egreso
     * @return Resultado del proceso
     */

    public ResultadoEJB<AreasUniversidad> getCarreraSeguimiento (@NonNull Short peEgreso){
        try{
            if(peEgreso==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(), "El programa educativo de egreso no debe ser nulo");}
            Short claveArea= 0;
            switch (peEgreso){
                case 30:
                    claveArea =69;break;
                case 33:
                    claveArea = 37;break;
                case 34:
                    claveArea = 38; break;
                case 35:
                    claveArea = 59; break;
                case 36:
                    claveArea = 77; break;
                case 39:
                    claveArea =42; break;
                case 40:
                    claveArea= 42; break;
                case 43:
                    claveArea= 44; break;
                case 49:
                    claveArea= 50; break;
                case 64:
                    claveArea =76; break;
                case 65:
                    claveArea= 75; break;
            }
            AreasUniversidad pe = new AreasUniversidad();
            pe = em.createQuery("select a from AreasUniversidad  a where  a.area=:area", AreasUniversidad.class)
                    .setParameter("area",claveArea)
                    .getResultStream().findFirst().orElse(null)
            ;
            if(pe!=null){ return ResultadoEJB.crearCorrecto(pe,"Licenciatura /Ing");}
            else {return ResultadoEJB.crearErroneo(3,new AreasUniversidad(), "No se encontró carrera de seguimiento");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la Licenciatura / Ingeniería de seguimiento (EjbRegistroFichaAdmision.getCarreraSeguimiento).", e, null);

        }
    }
    /**
     * Busca Area Academica por programa educativo
     * @param pe Programa educativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getOpcionAreaSup(@NonNull AreasUniversidad pe) {
        try {
            if(pe==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del área no debe ser nula");}
            AreasUniversidad a = em.createQuery("select a from AreasUniversidad  a where a.area=:area", AreasUniversidad.class)
                    .setParameter("area",pe.getAreaSuperior())
                    .getResultStream()
                    .findFirst()
                    .orElse(new AreasUniversidad());
            if(a!=null){return ResultadoEJB.crearCorrecto(a,"Area encontrada");}
            else {return ResultadoEJB.crearErroneo(4,a,"Error");}

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbRegistroFichaAdmision.getOpcionArea).", e, null);
        }
    }

    /**
     * Obtiene la área académica según la clave del programa educativo
     * @param clavePE Clave del programa educativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreaSupbyPE(@NonNull Short clavePE){
        try{
            if(clavePE==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del programa educativo no debe ser nula");}
            AreasUniversidad a = new AreasUniversidad();
            a= em.createQuery("select a from AreasUniversidad  a where a.area=:clave", AreasUniversidad.class)
                    .setParameter("clave",clavePE)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(a!=null){
                AreasUniversidad areAc = new AreasUniversidad();
                areAc = em.createQuery("select a from AreasUniversidad a where a.area=:area", AreasUniversidad.class)
                        .setParameter("area", a.getAreaSuperior())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                ;
                if(areAc!=null){return ResultadoEJB.crearCorrecto(areAc,"Área académica");}
                else {return ResultadoEJB.crearErroneo(4,new AreasUniversidad(),"No se encontró el área académica");}
            }
            else {return ResultadoEJB.crearErroneo(3,new AreasUniversidad(),"No se encotró el programa educativo");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo recuperar el Area (EjbRegistroFichaAdmision.getAreaSupbyPE).", e, null);
        }
    }




    /**
     * Genera el folio del aspirante de ingeniería
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

            TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(p.folioAspirante) FROM Aspirante AS p WHERE p.idProcesoInscripcion.idProcesosInscripcion =:idPE and (p.tipoAspirante.idTipoAspirante=:tipo or p.tipoAspirante.idTipoAspirante=:tipo2) ")
                    .setParameter("idPE", procesosInscripcion.getIdProcesosInscripcion())
                    .setParameter("tipo", 1)
                    .setParameter("tipo2",3)
                    ;
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
     * Operaciones para guardar/actualizar aspirante
     * @param rr Dto aspirante
     * @param pr  Dto persona
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> operacionesAspiranteR(DtoAspirante.AspiranteR rr, Persona pr, @NonNull ProcesosInscripcion procesosInscripcion) {
        try {
            rr.setProcesosInscripcion(procesosInscripcion);
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
     * Valida la ficha del aspirante
     * @param aspirante Aspirante de ingeniería
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAspirante.AspiranteR> validaAspirante(@NonNull DtoAspirante.AspiranteR aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2, aspirante,"El aspirante no debe ser nulo");}
            aspirante.getAspirante().setEstatus(Boolean.TRUE);
            aspirante.getAspirante().setFechaValidacion(new Date());
            em.merge(aspirante.getAspirante());
            em.flush();
            aspirante.setOperacion(Operacion.ACTUALIZAR);
            aspirante.setEcontrado(true);
            return ResultadoEJB.crearCorrecto(aspirante,"¡Se ha validado tu ficha con éxito!");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error en operacion validar aspirante (EjbRegistroFichaAdmision.validaAspirante).", e, null);
        }
    }


    /**
     * Obtiene la lista de universidades Técnologicas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UniversidadesUT>> getUniversidades (){
        try{
            List<UniversidadesUT> universidades = em.createQuery("select u from UniversidadesUT u where u.activo=:activo order by u.nombre asc ", UniversidadesUT.class)
                    .setParameter("activo",1)
                    .getResultList()
                    ;
            if(universidades.isEmpty() || universidades ==null){ return ResultadoEJB.crearErroneo(2,universidades,"Lista de universidades vacías"); }
            else {return ResultadoEJB.crearCorrecto(universidades,"Lista de universidades");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de universidades (EjbRegistroFichaAdmision.getUniversidades).", e, null);

        }
    }

    /**
     * Obtiene la universidad de egreso del aspirante
     * @param aspirante
     * @return
     */
    public ResultadoEJB<UniversidadesUT> getUniversidadbyAspirante(@NonNull UniversidadEgresoAspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new UniversidadesUT(),"La universidad de egreso del aspirante no deben ser nulos");}
            UniversidadesUT universidadesUT = new UniversidadesUT();
            universidadesUT = em.createQuery("select  u from UniversidadesUT u where u.cveUniversidad=:clave", UniversidadesUT.class)
                    .setParameter("clave",aspirante.getUniversidadEgresoAspirantePK().getUniversidadEgreso())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(universidadesUT!=null){return ResultadoEJB.crearCorrecto(universidadesUT,"Universidad de egreso");}
            else {return ResultadoEJB.crearErroneo(3,universidadesUT,"No se encontro la universidad");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la universidad de egreso(EjbRegistroFichaAdmision.getUniversidadbyAspirante).", e, null);

        }
    }

    /**
     * Obtiene la universidad de egreso (UTXJ)
     */
    public ResultadoEJB<UniversidadesUT> getUtxj (){
        try{
            UniversidadesUT utxj = new UniversidadesUT();
            utxj = em.createQuery("select u from UniversidadesUT u where u.cveUniversidad=:clave", UniversidadesUT.class)
                    .setParameter("clave",50)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(utxj!= null){return ResultadoEJB.crearCorrecto(utxj,"");}
            else {return ResultadoEJB.crearErroneo(2,utxj,"Error al obtener la universidad");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la universidad UTXJ (EjbRegistroFichaAdmision.getUtxj).", e, null);
        }
    }

    /**
     * Obtiene la universidad de egreso del aspirante de ingeniería
     * @param aspirante Aspirante de ingeniería
     * @return Resultado del proceso
     */
    public ResultadoEJB<UniversidadEgresoAspirante> getUniversidadbyAspirante (@NonNull Aspirante aspirante){
        try{
            if(aspirante==null){return ResultadoEJB.crearErroneo(2,new UniversidadEgresoAspirante(),"El aspirante no debe ser nulo");}
            UniversidadEgresoAspirante ue = new UniversidadEgresoAspirante();
            ue = em.createQuery("select u from UniversidadEgresoAspirante u where u.universidadEgresoAspirantePK.aspirante=:aspirante", UniversidadEgresoAspirante.class)
                    .setParameter("aspirante", aspirante.getIdAspirante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(ue ==null){return ResultadoEJB.crearErroneo(3,new UniversidadEgresoAspirante(),"No se encontró registro");}
            else {return ResultadoEJB.crearCorrecto(ue,"Universidad de egreso");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la universidad de egreso del aspirante(EjbRegistroFichaIngenieria.getUniversidadbyAspirante)", e, null);

        }
    }

    /**
     * Actualiza / guarda la universidad de egreso del aspirante
     * @param universidad Universidad de egreso
     * @param aspirante Apirante de ingeniería
     * @param operacion Operación (Persistir o Actualizar)
     * @return Resultado del proceso
     */
    public ResultadoEJB<UniversidadEgresoAspirante> operacionUniversidadesE (@NonNull UniversidadesUT universidad , @NonNull Aspirante aspirante, @NonNull Operacion operacion){
        try{
            if(universidad==null){return ResultadoEJB.crearErroneo(2,new UniversidadEgresoAspirante(),"La universidad egreso no debe ser nula");}
            if(aspirante==null){return ResultadoEJB.crearErroneo(3,new UniversidadEgresoAspirante(),"El aspirante no debe ser nulo");}
            if(operacion==null){return ResultadoEJB.crearErroneo(4,new UniversidadEgresoAspirante(),"La operación no debe ser nula");}
            UniversidadEgresoAspirante newUniversidadEgresoAspirante = new UniversidadEgresoAspirante();
            UniversidadEgresoAspirantePK pk = new UniversidadEgresoAspirantePK();
            switch (operacion){
                case PERSISTIR:
                    pk.setAspirante(aspirante.getIdAspirante());
                    pk.setUniversidadEgreso(universidad.getCveUniversidad());
                    newUniversidadEgresoAspirante.setUniversidadEgresoAspirantePK(pk);
                    em.persist(newUniversidadEgresoAspirante);
                    em.flush();
                    break;
                case ACTUALIZAR:
                    ResultadoEJB<UniversidadEgresoAspirante> resUni = getUniversidadbyAspirante(aspirante);
                    if(resUni.getCorrecto()){
                        newUniversidadEgresoAspirante = resUni.getValor();
                        newUniversidadEgresoAspirante.getUniversidadEgresoAspirantePK().setUniversidadEgreso(universidad.getCveUniversidad());
                        em.merge(newUniversidadEgresoAspirante);
                    }
                    break;
            }
            return ResultadoEJB.crearCorrecto(newUniversidadEgresoAspirante,"Universidad egreso guardada/Actualizada");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al guardar /actualizar la universidad de egreso del aspirante(EjbRegistroFichaIngenieria.operacionUniversidadesE)", e, null);
        }
    }

    /**
     * Obtiene el tramite escolar tipo Inscripcion Ingeniería activo según el periodo del proceso de Inscripción activo
     * @return Resultado del proceso (Tramite escolar)
     */
    public ResultadoEJB<TramitesEscolares> getTramiteInscripcionActivo(@NonNull ProcesosInscripcion procesosInscripcionActivo){
        try{
            if(procesosInscripcionActivo==null){return ResultadoEJB.crearErroneo(2,new TramitesEscolares(),"El proceso de inscripción no debe ser nulo"); }
            TramitesEscolares tramitesEscolares = new TramitesEscolares();
            //Verifica tramite aperturado
            tramitesEscolares = em.createQuery("select t from TramitesEscolares t where t.tipoTramite=:tipoT and t.periodo=:periodo and T.tipoPersona=:tipoPer", TramitesEscolares.class)
                    .setParameter("tipoT", TramiteEscolar.INSCRIPCION_ING.getLabel())
                    .setParameter("periodo",procesosInscripcionActivo.getIdPeriodo())
                    .setParameter("tipoPer", TipoPersonaTramite.ASPIRANTE_ING.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(tramitesEscolares == null){
                return ResultadoEJB.crearErroneo(2,tramitesEscolares, "No existe tramite escolar activo.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(tramitesEscolares, "Tramite aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el tramite activo(EjbCitaInscripcionAspirante.getTramiteInscripcionActivo).", e, TramitesEscolares.class);
        }
    }
    /**
     * Genera ficha de admision
     * @param persona
     * @param domicilio
     * @param aspirante
     * @param medioComunicacion
     * @param uso
     * @throws IOException
     * @throws DocumentException
     */
    public ResultadoEJB<Boolean> generarFicha(@NonNull String matricula, @NonNull Persona persona, @NonNull DatosAcademicos academicos, @NonNull UniversidadesUT universidadesUT, @NonNull Domicilio domicilio, @NonNull Aspirante aspirante, @NonNull MedioComunicacion medioComunicacion, @NonNull String uso) throws IOException, DocumentException {
        if(persona==null){return ResultadoEJB.crearErroneo(2,Boolean.FALSE,"La persona no debe ser nula");}
        if(academicos==null){return ResultadoEJB.crearErroneo(3,Boolean.FALSE,"Los datos académicos no deben ser nulos");}
        if(medioComunicacion==null){return  ResultadoEJB.crearErroneo(4,Boolean.FALSE,"Los datos de medio de comunicación no deben ser nulos");}
        if(domicilio==null){return ResultadoEJB.crearErroneo(5,Boolean.FALSE,"Los datos del domicilio no deben ser nulos");}
        if(aspirante==null){return ResultadoEJB.crearErroneo(6,Boolean.FALSE,"El aspirante no debe ser nulo");}
        if(uso==null){return ResultadoEJB.crearErroneo(7,Boolean.FALSE,"El uso no debe ser nulo");}
        String ruta = "C://archivos//plantillas//preregistro_ing.pdf";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");

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

        BarcodeQRCode qrcode = new BarcodeQRCode(String.valueOf(aspirante.getFolioAspirante()), 120, 120, null);
        Image image = qrcode.getImage();
        image.setAbsolutePosition(425f, 69f);
        PdfContentByte content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(image);

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("txtCarrera", getAreabyClave(academicos.getPrimeraOpcion()).getValor().getNombre());
        fields.setField("txtTurno",academicos.getSistemaPrimeraOpcion().getNombre());
        fields.setField("txtUniversidad",universidadesUT.getNombre());
        fields.setField("txtNombreC",persona.getApellidoPaterno().concat(" ").concat(persona.getApellidoMaterno()).concat(" ").concat(persona.getNombre()));
        fields.setField("txtFechaNac",sm.format(persona.getFechaNacimiento()));
        fields.setField("txtEmail",medioComunicacion.getEmail());
        fields.setField("txtDireccion",domicilio.getCalle().concat(" #").concat(domicilio.getNumero()).concat(", ").concat(asentamiento.getNombreAsentamiento().concat(" CP -").concat(asentamiento.getCodigoPostal()).concat(", ").concat(asentamiento.getMunicipio1().getNombre())).concat(", ").concat(asentamiento.getMunicipio1().getEstado().getNombre()));
        fields.setField("txtMovil",medioComunicacion.getTelefonoMovil());
        fields.setField("txtFijo",medioComunicacion.getTelefonoFijo());
        fields.setField("txtMatricula",matricula);
        fields.setField("txtFecha",sm.format(aspirante.getFechaRegistro()));
        fields.setField("txtFolio",String.valueOf(aspirante.getFolioAspirante()));
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
            String claveCorreo = "DeptoEscolares21";
            String mensaje = "Estimado(a) "+persona.getNombre()+"\n\n Gracias por elegir a la Universidad Tecnologica de Xicotepec de Juárez como opción para continuar con tus estudios de nivel superior." +
                    "\n\n Si eres egresado de generación reciente (2019 -2021), valida tu ficha en la plataforma SII, realiza el pago de colegiatura y de pago de seguro facultativo, asiste a las instalaciones de la universidad y entrega los comprobantes de pago, para que" +
                    "se realice la inscripción autómatica." +
                    ".\n\n"
                    + "Si eres egresado de generaciones anteriores, o egresado de otra UT, asiste a las instalaciones de la universidad el día que agendaste tu cita y sigue las instrucciones que indican en tu comprobante.\n" +
                    "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares";

            String identificador = "Pre registro Ingenierías y Licenciaturas 2021 UTXJ";
            String asunto = "Pre registo Exitoso";
            // System.out.println(medioComunicacion.getEmail());
            if(medioComunicacion.getEmail() != null){
                // System.out.println("Correo "+ medioComunicacion.getEmail());
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

    /**
     * Genera el comprobante del registro de la cita
     * @param dto Dto de la cita del aspirante
     * @param fecha Fecha de impresión
     * @param tramitesEscolares Tramite de la cita
     * @return Resultado del proceso
     * @throws IOException
     * @throws DocumentException
     */
    public ResultadoEJB<Boolean> generarComprobante (@NonNull DtoCitaAspirante dto, @NonNull Date fecha, @NonNull TramitesEscolares tramitesEscolares)throws IOException, DocumentException {
        if(dto.getAspirante().getIdPersona()==null){return ResultadoEJB.crearErroneo(2,false,"La persona no debe ser nula");}
        if(dto.getCitasAspirantes()== null){return ResultadoEJB.crearErroneo(3,false,"La cita no debe sernula");}
        if(fecha ==null){return ResultadoEJB.crearErroneo(4,false,"La fecha no debe ser nula");}
        String ruta = "C://archivos//plantillas//formato_cita_ing.pdf";

        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
        InputStream is = new FileInputStream(ruta);
        PdfReader pdfReader = new PdfReader(is,null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);

        BarcodeQRCode qrcode = new BarcodeQRCode(String.valueOf(dto.getCitasAspirantes().getFolioCita()), 100, 80, null);
        Image image = qrcode.getImage();
        image.setAbsolutePosition(465f, 610f);
        PdfContentByte content = pdfStamper.getOverContent(pdfReader.getNumberOfPages());
        content.addImage(image);

        AcroFields fields = pdfStamper.getAcroFields();
        fields.setField("txtFolio", String.valueOf(dto.getCitasAspirantes().getFolioCita()));
        fields.setField("txtNombre", String.valueOf(dto.getAspirante().getIdPersona().getNombre().concat(" ").concat(dto.getAspirante().getIdPersona().getApellidoPaterno().concat(" ").concat(dto.getAspirante().getIdPersona().getApellidoMaterno()))));
        fields.setField("txtCurp", dto.getAspirante().getIdPersona().getCurp());
        fields.setField("txtTipoTramite", tramitesEscolares.getTipoTramite());
        fields.setField("txtFechaCita", sm.format(dto.getCitasAspirantes().getFechaCita()));
        fields.setField("txtFecha", sm.format(fecha));
        fields.setField("txtEstatus", dto.getCitasAspirantes().getStatus());
        pdfStamper.close();
        pdfStamper.close();

        Object response = facesContext.getExternalContext().getResponse();
        if (response instanceof HttpServletResponse) {
            HttpServletResponse hsr = (HttpServletResponse) response;
            hsr.setContentType("application/pdf");
            hsr.setHeader("Content-disposition", "attachment; filename=\""+dto.getCitasAspirantes().getFolioCita()+"_"+dto.getAspirante().getIdPersona().getCurp()+".pdf\"");
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
        if(dto.getAspirante().getIdPersona().getMedioComunicacion().getEmail()!=null){
            String  mail= dto.getAspirante().getIdPersona().getMedioComunicacion().getEmail();
            // El correo gmail de envío
            String correoEnvia = "servicios.escolares@utxicotepec.edu.mx";
            String claveCorreo = "DServiciosEscolares21";
            String mensaje = "Estimado(a)"+ dto.getAspirante().getIdPersona().getNombre() + ", haz realizado tu registro de tu cita con éxito"+
                    "\n\n Para continuar descarga el comprobante de tu cita y leé con atención las indicaciones.\n\n"
                    + "Recuerda que al asistir a tu cita debes tener todas las medidas sanitarias, por lo cual el uso de cobre bocas es obligatorio.\n"
                    + "ATENTAMENTE \n" +
                    "Departamento de Servicios Escolares";

            String identificador = "Agenda tu cita UTXJ 2021";
            String asunto = "Registro de cita éxitoso";
            if(mail != null){
                try {
                    DataSource source = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
                    EnvioCorreos.EnviarCorreoArchivos(correoEnvia, claveCorreo,identificador,asunto,mail,mensaje,source,String.valueOf(dto.getCitasAspirantes().getFolioCita()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return ResultadoEJB.crearCorrecto(true,"Se genero comprobante de la cita");

    }





}
