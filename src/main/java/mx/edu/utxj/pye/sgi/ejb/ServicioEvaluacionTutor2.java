package mx.edu.utxj.pye.sgi.ejb;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.view.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;


@Stateful
public class ServicioEvaluacionTutor2 implements EjbEvaluacionTutor2 {
    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EJBAdimEstudianteBase ejbAdminAlumno;

    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public void init(){em = f.getEntityManager();em2 = f2.getEntityManager();}

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
        /**
         * Se eliminaron esta ponderación por petición de Psicopedagogia
         *  l.add(new SelectItem("3", "Siempre", "Siempre"));
         *  l.add(new SelectItem("2", "Regularmente", "Regularmente"));
         * l.add(new SelectItem("1", "Nunca", "Nunca"));
         */
//        l.add(new SelectItem("4", "No aplica", "No aplica"));Solo para pregunta 5
        l.add(new SelectItem("1", "Insatisfactorio", "Insatisfactorio"));
        l.add(new SelectItem("2", "Mejorable", "Mejorable"));
        l.add(new SelectItem("3", "Poco satisfactorio", "Poco satisfactorio"));
        l.add(new SelectItem("4", "Satisfactorio", "Satisfactorio"));
        l.add(new SelectItem("5", "Muy satisfactorio", "Muy satisfactorio"));
        l.add(new SelectItem("0", "No aplica", "No aplica"));

        return l;
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();

        Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,1.0f, "Me enteré de los servicios y programas de apoyo al estudiante a través de información que me proporcionó mi tutor (becas, cursos, programas de intercambio estudiantil, programas culturales y demás trámites escolares).", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,2.0f, "El trabajo que desarrollé con mi tutor evidenció que hubo una planeación de las actividades y no una mera improvisación.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,3.0f, "El tutor llegó a la impartición de la tutoría en tiempo y forma.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,4.0f, "Me sentí cómodo en las sesiones de tutoría por el ambiente de respeto y atención que me dio mi tutor.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,5.0f, "El tutor me canalizó a las instancias adecuadas cuando tuve algún problema (si no se tuvo problemas puede elegir la opción NO APLICA).", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,6.0f, "Con las sesiones de tutorías he aprendido a tomar decisiones y asumir las consecuencias de las mismas.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,7.0f, "Fue fácil localizar a mi tutor en las sesiones de trabajo acordadas.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,8.0f, "En general, el desempeño de mi tutor fue satisfactorio.", ""));
        a1.getPreguntas().add(new Abierta(TipoCuestionario.ETUTOR, 1.0f, 9.0f, "Comentarios hacia el tutor (Mínimo 20 caracteres)", 20));
        l.add(a1);

        return l;
    }


    @Override
    public ResultadoEJB<Evaluaciones> getEvaluacionActiva() {
        try{
            Evaluaciones evaluacion = new Evaluaciones();
            //TODO: Ahorita solo es de prueba, pero se debe cambiar a que busque por rango de fecha, para que tome la evaluacion actual
            evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
            .setParameter("tipo", "Tutor")
            .setParameter("fecha", new Date())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"La evaluacion no se encontro");}
            else {return  ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion encontrada");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluación activa(ServicioEvaluacionTutor2.getEvaluacionActiva)", e, null);
        }
    }

    @Override
    public VistaEvaluacionesTutores getEstudianteTutor(Integer periodo, String matricula) {
        TypedQuery<VistaEvaluacionesTutores> q = em2.createQuery("SELECT v from VistaEvaluacionesTutores v WHERE v.pk.matricula=:matricula AND v.pk.periodo=:periodo", VistaEvaluacionesTutores.class);
        q.setParameter("periodo", periodo);
        q.setParameter("matricula", matricula);

        List<VistaEvaluacionesTutores> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

    @Override
    public EvaluacionTutoresResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionesTutores estudiante) {
        TypedQuery<EvaluacionTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionesTutoresResultados r WHERE r.evaluacionesTutoresResultadosPK.evaluacion=:evaluacion AND r.evaluacionesTutoresResultadosPK.evaluador=:evaluador", EvaluacionTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(estudiante.getPk().getMatricula()));
        List<EvaluacionTutoresResultados> l = q.getResultList();

        if(!l.isEmpty()){
            return l.get(0);
        }else{
            f.setEntityClass(EvaluacionTutoresResultados.class);
            EvaluacionTutoresResultados resultados = new EvaluacionTutoresResultados(evaluacion.getEvaluacion(), Integer.parseInt(estudiante.getPk().getMatricula()), Integer.parseInt(estudiante.getPk().getNumeroNomina()));
            f.create(resultados);
            return resultados;
        }
    }
    @Override
    public EvaluacionTutoresResultados getSoloResultados(Evaluaciones evaluacion, Integer estudiante) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getSoloResultados() matricula :_ " + estudiante);
        TypedQuery<EvaluacionTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionesTutoresResultados r WHERE r.evaluacionesTutoresResultadosPK.evaluacion=:evaluacion AND r.evaluacionesTutoresResultadosPK.evaluador=:evaluador", EvaluacionTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", estudiante);
        List<EvaluacionTutoresResultados> l = q.getResultList();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getSoloResultados() lista de estudiante ejb : " + l);
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void guardar(EvaluacionTutoresResultados resultados) {
        f.setEntityClass(EvaluacionTutoresResultados.class);
        f.edit(resultados);
    }

    @Override
    public void actualizar(String id, String valor, EvaluacionTutoresResultados resultados) {
        switch(id){
            case "p1": resultados.setR1(Short.parseShort(valor)); break;
            case "p2": resultados.setR2(Short.parseShort(valor)); break;
            case "p3": resultados.setR3(Short.parseShort(valor)); break;
            case "p4": resultados.setR4(Short.parseShort(valor)); break;
            case "p5": resultados.setR5(Short.parseShort(valor)); break;
            case "p6": resultados.setR6(Short.parseShort(valor)); break;
            case "p7": resultados.setR7(Short.parseShort(valor)); break;
            case "p8": resultados.setR8(Short.parseShort(valor)); break;
            case "p9": resultados.setR9(valor); break;
        }
    }


    @Override
    public List<VistaEvaluacionesTutores> getListaTutores() {
        TypedQuery<Listaperiodosescolares> periodo = em.createQuery("SELECT p from Listaperiodosescolares p ORDER BY p.periodo DESC", Listaperiodosescolares.class);
        if (periodo.getResultList().isEmpty() || periodo.getResultList() == null) {
           // System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getListaTutores() no se encontro ninguna lista de periodos");
        } else {
           // System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getListaTutores() el periodo es  --> : " + periodo);
        }
        TypedQuery<VistaEvaluacionesTutores> q = em2.createQuery("SELECT v from VistaEvaluacionesTutores v WHERE v.pk.periodo = :periodo", VistaEvaluacionesTutores.class);
        q.setParameter("periodo", periodo.getSingleResult().getPeriodo());
        if(q.getResultList().isEmpty() || q.getResultList() == null){
            return null;
        }else{
            return q.getResultList();
        }
    }



    @Override
    public ResultadoEJB<EvaluacionTutoresResultados> getResultadosEvaluacionTutorEstudiante(Evaluaciones evaluacion, dtoEstudiantesEvalauciones estudiante) {

        try {
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3, "La evaluación no debe ser nula", EvaluacionTutoresResultados.class);}
            if(estudiante == null){return ResultadoEJB.crearErroneo(4, "El estudiante no debe ser nulo", EvaluacionTutoresResultados.class);}
            //TODO: Busca si existe registro en la tabla de resultados
            EvaluacionTutoresResultados resultados =em.createQuery("SELECT e FROM EvaluacionTutoresResultados e WHERE e.evaluaciones.evaluacion=:evaluacion AND e.evaluacionTutoresResultadosPK.evaluador=:clave", EvaluacionTutoresResultados.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("clave", estudiante.getEstudiantesClaves().getClave())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
           // System.out.println("Resultados de consultas--> Eva Ttor--"+resultados);
            if(resultados!=null){return ResultadoEJB.crearCorrecto(resultados, "Se cargaron los resultados.");}
            //TODO:Si no encuentra regustro de resultados del estudiante, lo crea.
            else if (resultados== null){
                //Si no encuentra resultados se deben crear
                EvaluacionTutoresResultados newResultados= new EvaluacionTutoresResultados();
                EvaluacionTutoresResultadosPK pk = new EvaluacionTutoresResultadosPK();
                pk.setEvaluacion(evaluacion.getEvaluacion());
                pk.setEvaluado(estudiante.getTutor().getClave());
                pk.setEvaluador(estudiante.getEstudiantesClaves().getClave());
                newResultados.setEvaluacionTutoresResultadosPK(pk);
                em.persist(newResultados);
                return ResultadoEJB.crearCorrecto(newResultados,"Se crearon los resultados");
            }
            else{return ResultadoEJB.crearCorrecto(resultados, "Se encontraron resultados");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados de la evaluacion a tutor del estudiante", e, null);
        }
    }

    @Override
    public ResultadoEJB<EvaluacionTutoresResultados>  cargarResultadosEstudianteClave(String id, String valor, EvaluacionTutoresResultados resultados, Operacion operacion) {
        try {
            switch(operacion){
                case PERSISTIR:
                    //TODO: Crear los resultados cargar los valores
                    f.setEntityClass(EvaluacionTutoresResultados.class);
                    em.merge(resultados);
                    return ResultadoEJB.crearCorrecto(resultados, "Evaluacion Tutor Resultados, correcta");
                case REFRESCAR:
                    // TODO: Actualizar los resultados de las preguntas
                    switch(id){
                        case "p1": resultados.setR1(Short.parseShort(valor)); break;
                        case "p2": resultados.setR2(Short.parseShort(valor));break;
                        case "p3": resultados.setR3(Short.parseShort(valor));break;
                        case "p4": resultados.setR4(Short.parseShort(valor)); break;
                        case "p5": resultados.setR5(Short.parseShort(valor)); break;
                        case "p6": resultados.setR6(Short.parseShort(valor)); break;
                        case "p7": resultados.setR7(Short.parseShort(valor)); break;
                        case "p8": resultados.setR8(Short.parseShort(valor)); break;
                        case "p9": resultados.setR9(valor); break;
                    }
                    return ResultadoEJB.crearCorrecto(resultados, "Respuestas actualizadas");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", EvaluacionTutoresResultados.class);

            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar los resultados ejbEvaluacionTutor2", e, null);

        }

    }

    @Override
    public ResultadoEJB<Personal> getTutor(AlumnosEncuestas tutor) {
        try {
            if(tutor==null){return ResultadoEJB.crearErroneo(3,"El tutor no debe ser nulo", Personal.class);}
            //TODO: Busca al tutor en sauitt para sacar la clave de su nomina
            ListaUsuarioClaveNomina tutorSauitt= em2.createQuery("SELECT l FROM ListaUsuarioClaveNomina l WHERE l.cvePersona=:clave", ListaUsuarioClaveNomina.class)
                    .setParameter("clave", tutor.getCveMaestro())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(tutorSauitt!=null){
                //TODO: Busca al tutor por clave de su nomina en la base de Capital humano
                Personal tutorPersonal= em.createQuery("SELECT p FROM Personal p WHERE p.clave=:clave", Personal.class)
                        .setParameter("clave", Integer.parseInt(tutorSauitt.getNumeroNomina()))
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                if(tutorPersonal!=null){return ResultadoEJB.crearCorrecto(tutorPersonal, "Se ha encontrado ");}
                else{return ResultadoEJB.crearErroneo(4,tutorPersonal , "No se encontro al tutor en la base de Capital Humano");}
            }else{return ResultadoEJB.crearErroneo(2, "No se encontro al personal, en saiutt", Personal.class);}


        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo buscar al tutor ejbServicioTutor2 getTutor", e, Personal.class);
        }

    }

}