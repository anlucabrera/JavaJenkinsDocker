package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2PK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.mail.FetchProfile;
import javax.persistence.EntityManager;
import java.rmi.server.ExportException;
import java.util.Date;
import java.util.List;

@Stateless (name = "EjbNuevaEvaluacionTutor")
public class EjbNuevaEvaluacionTutor {

    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public void init(){em = f.getEntityManager();em2 = f2.getEntityManager();}

    public ResultadoEJB<Evaluaciones> getEvaluacionTutorActiva (){
        try{
            Evaluaciones evaluacion = new Evaluaciones();
            evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", "Tutor")
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"La evaluacion no se encontro");}
            else {return  ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion encontrada");}

        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluación activa(EjbNuevaEvaluacionTutor.getEvaluacionTutorActiva)", e, null);
        }
    }

    /**
     * Obtiene las posibles respuestas
     *
     * @return
     */
    public List<SelectItem> getRespuestasPosibles(){
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("5", "Muy satisfecho", "Muy satisfecho"));
        l.add(new SelectItem("4", "Satisfecho", "Satisfecho"));
        l.add(new SelectItem("3", "Regularmente satisfecho", "Regularmente satisfecho"));
        l.add(new SelectItem("2", "Poco satisfecho", "Poco satisfecho"));
        l.add(new SelectItem("1", "No satisfecho", "No satisfecho"));
        //l.add(new SelectItem("0", "No aplica", "No aplica")); Sólo es para la pregunta 6
        return l;
    }

    /**
     * Obtiene las preguntas
     * @return
     */
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,1.0f, "Me enteré de los servicios y programas de apoyo al estudiante a través de información que me proporcionó mi tutor (becas, cursos, programas de intercambio estudiantil, programas culturales y deportivos y demás trámites escolares).", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,2.0f, "Las sesiones que desarrollé con mi tutor evidenció que hubo una planeación de las actividades y no una improvisación.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,3.0f, "El tutor llego a la impartición de la tutoría grupal puntualmente.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,4.0f, "Me sentí cómodo en las sesiones de tutoría por el ambiente de respeto y atención que me dio mi tutor.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,5.0f, "El tutor me orientó a recibir asesorías académicas cuando lo necesité.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,6.0f, "El tutor me orientó a instancias de apoyo psicopedagógicas, becas, enfermería, cuando tuve algún problema (si no se tuvo problemas puede contestarse como No Aplica).", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,7.0f, "Fue fácil localizar a mi tutor en las sesiones de Tutoría Individual.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,8.0f, "En general, el desempeño de mi tutor fue.", ""));
        l.add(a1);
        return l;
    }
    /**
     * Obtiene los resultados de la avaluacion al tutor activa  por clave del alumno, si no existen aun resultados los crea
     * @param evaluacion Evaluación activa
     * @param estudiante Matricula del Estudiante evaluador
     * @return  Resultado del proceso
     *
     */
    public ResultadoEJB<EvaluacionTutoresResultados2> getResultadosEvaluacionTutorEstudiante(Evaluaciones evaluacion, dtoEstudiantesEvalauciones estudiante) {

        try {
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3, "La evaluación no debe ser nula", EvaluacionTutoresResultados2.class);}
            if(estudiante == null){return ResultadoEJB.crearErroneo(4, "El estudiante no debe ser nulo", EvaluacionTutoresResultados2.class);}
            //Busca si existe registro en la tabla de resultados
            EvaluacionTutoresResultados2 resultados =em.createQuery("SELECT e FROM EvaluacionTutoresResultados2 e WHERE e.evaluaciones.evaluacion=:evaluacion AND e.evaluacionTutoresResultados2PK.evaluador=:evaluador", EvaluacionTutoresResultados2.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("evaluador", Integer.parseInt(estudiante.getMatricula()))
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            // System.out.println("Resultados de consultas--> Eva Ttor--"+resultados);
            if(resultados!=null){return ResultadoEJB.crearCorrecto(resultados, "Se cargaron los resultados.");}
            //Si no encuentra regustro de resultados del estudiante, lo crea.
            else if (resultados== null){
                //Si no encuentra resultados se deben crear
                EvaluacionTutoresResultados2 newResultados= new EvaluacionTutoresResultados2();
                EvaluacionTutoresResultados2PK pk = new EvaluacionTutoresResultados2PK();
                pk.setEvaluacion(evaluacion.getEvaluacion());
                pk.setEvaluado(estudiante.getTutor().getClave());
                pk.setEvaluador(Integer.parseInt(estudiante.getMatricula()));
                newResultados.setEvaluacionTutoresResultados2PK(pk);
                em.persist(newResultados);
                return ResultadoEJB.crearCorrecto(newResultados,"Se crearon los resultados");
            }
            else{return ResultadoEJB.crearCorrecto(resultados, "Se encontraron resultados");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados de la evaluacion a tutor del estudiante(EjbNuevaEvaluacionTutor.getResultadosEvaluacionTutorEstudiante)", e, null);
        }
    }
    public ResultadoEJB<EvaluacionTutoresResultados2>  cargarResultadosEstudianteClave(String id, String valor, EvaluacionTutoresResultados2 resultados, Operacion operacion) {
        try {
            switch(operacion){
                case PERSISTIR:
                    //Crear los resultados cargar los valores
                    f.setEntityClass(EvaluacionTutoresResultados2.class);
                    em.merge(resultados);
                    return ResultadoEJB.crearCorrecto(resultados, "Evaluacion Tutor Resultados, correcta");
                case REFRESCAR:
                    //Actualizar los resultados de las preguntas
                    switch(id){
                        case "p1": resultados.setR1(Short.parseShort(valor)); break;
                        case "p2": resultados.setR2(Short.parseShort(valor));break;
                        case "p3": resultados.setR3(Short.parseShort(valor));break;
                        case "p4": resultados.setR4(Short.parseShort(valor)); break;
                        case "p5": resultados.setR5(Short.parseShort(valor)); break;
                        case "p6": resultados.setR6(Short.parseShort(valor)); break;
                        case "p7": resultados.setR7(Short.parseShort(valor)); break;
                        case "p8": resultados.setR8(Short.parseShort(valor)); break;
                    }
                    return ResultadoEJB.crearCorrecto(resultados, "Respuestas actualizadas");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", EvaluacionTutoresResultados2.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar los resultados (EjbNuevaEvaluacionTutor.cargarResultadosEstudianteClave)", e, null);
        }
    }
    public ResultadoEJB<EvaluacionTutoresResultados2> updateCompleto(EvaluacionTutoresResultados2 resultados2, Boolean finalizado){
        try{
            if(resultados2 ==null){return ResultadoEJB.crearErroneo(2,new EvaluacionTutoresResultados2(),"Los resultados no deben ser nulos");}
            if(finalizado==null){return ResultadoEJB.crearErroneo(3,new EvaluacionTutoresResultados2(),"El estado de la evaluacion no debe ser nulo");}
            EvaluacionTutoresResultados2 ev = new EvaluacionTutoresResultados2();
            ev = resultados2;
            ev.setCompleto(finalizado);
            em.merge(ev);
            em.flush();
            return  ResultadoEJB.crearCorrecto(ev,"Actizado como completo");

        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "No se pudo actualizar(EjbNuevaEvaluacionTutor.updateCompleto)", e, null);}
    }
    }
