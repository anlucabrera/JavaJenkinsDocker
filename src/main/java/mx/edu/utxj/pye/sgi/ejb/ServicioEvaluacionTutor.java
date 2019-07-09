package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultadosPK;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioEvaluacionTutor implements EjbEvaluacionTutor {
    private static final long serialVersionUID = 8615480818156924255L;
    
    @EJB Facade f;
    @EJB Facade2 f2;

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
//        l.add(new SelectItem("4", "No aplica", "No aplica"));Solo para pregunta 5
        l.add(new SelectItem("3", "Siempre", "Siempre"));
        l.add(new SelectItem("2", "Regularmente", "Regularmente"));
        l.add(new SelectItem("1", "Nunca", "Nunca"));
        
        return l;
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1F, "", new ArrayList<>());
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
    public List<VistaEvaluacionesTutores> getListaEstudiantes(Integer periodo, String matricula) {
        TypedQuery<VistaEvaluacionesTutores> q = f2.getEntityManager().createQuery("SELECT v from VistaEvaluacionesTutores v WHERE v.pk.matricula=:matricula AND v.pk.periodo=:periodo", VistaEvaluacionesTutores.class);
        q.setParameter("periodo", periodo);
        q.setParameter("matricula", matricula);
        
        List<VistaEvaluacionesTutores> l = q.getResultList();
        return l;
    }

    @Override
    public List<EvaluacionTutoresResultados> getListaTutores(VistaEvaluacionesTutores estudianteEvaluador) {
        Evaluaciones evaluacion = evaluacionActiva();
        
        TypedQuery<EvaluacionTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionTutoresResultados r WHERE r.evaluacionesTutoresResultadosPK.evaluacion=:evaluacion AND r.evaluacionesTutoresResultadosPK.evaluador=:evaluador AND r.evaluacionesTutoresResultadosPK.evaluado=:evaluado", EvaluacionTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(estudianteEvaluador.getPk().getMatricula()));
        q.setParameter("evaluado", Integer.parseInt(estudianteEvaluador.getPk().getNumeroNomina()));
        return q.getResultList();
    }

    @Override
    public Evaluaciones evaluacionActiva() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Tutor");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

    @Override
    public void cargarResultadosAlmacenados(Evaluaciones evaluacion, VistaEvaluacionesTutores evaluador, List<EvaluacionTutoresResultados> evaluados) {
        //verificar si existe la fila de resultados
        f.setEntityClass(EvaluacionTutoresResultados.class);
        EvaluacionTutoresResultadosPK evaluacionesTutoresResultadosPK = new EvaluacionTutoresResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(evaluador.getPk().getMatricula()), Integer.parseInt(evaluador.getPk().getNumeroNomina()));
        EvaluacionTutoresResultados resultado = (EvaluacionTutoresResultados) f.find(evaluacionesTutoresResultadosPK);
        
        //si no exite crearla y cargarla a la evaluacion con una lista de resultados limpia 
        if(resultado == null){
            resultado = new EvaluacionTutoresResultados(evaluacionesTutoresResultadosPK);
            f.create(resultado);
            f.getEntityManager().detach(evaluacion);
            evaluacion.setEvaluacionTutoresResultadosList(new ArrayList<>());
            evaluacion.getEvaluacionTutoresResultadosList().add(resultado);
        }
    }

    @Override
    public String obtenerRespuestaPorPregunta(EvaluacionTutoresResultados resultado, Float pregunta) {
        String res = null;
        switch (pregunta.toString()) {
            case "1.0":
                res = resultado.getR1() != null ? resultado.getR1().toString() : null;
                break;
            case "2.0":
                res = resultado.getR2() != null ? resultado.getR2().toString() : null;
                break;
            case "3.0":
                res = resultado.getR3() != null ? resultado.getR3().toString() : null;
                break;
            case "4.0":
                res = resultado.getR4() != null ? resultado.getR4().toString() : null;
                break;
            case "5.0":
                res = resultado.getR5() != null ? resultado.getR5().toString() : null;
                break;
            case "6.0":
                res = resultado.getR6() != null ? resultado.getR6().toString() : null;
                break;
            case "7.0":
                res = resultado.getR7() != null ? resultado.getR7().toString() : null;
                break;
            case "8.0":
                res = resultado.getR8() != null ? resultado.getR8().toString() : null;
                break;
            case "9.0":
                res = resultado.getR9() != null ? resultado.getR9() : null;
                break;
        }
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionDesempenio.obtenerRespuestaPorPregunta(): " + res);
        return res;
    }

    @Override
    public List<EvaluacionTutoresResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluacion, VistaEvaluacionesTutores evaluador) {
        TypedQuery<EvaluacionTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionTutoresResultados r WHERE r.evaluacionesTutoresResultadosPK.evaluacion=:evaluacion AND r.evaluacionesTutoresResultadosPK.evaluador=:evaluador", EvaluacionTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(evaluador.getPk().getMatricula()));
        return q.getResultList();
    }

    @Override
    public void actualizarRespuestaPorPregunta(EvaluacionTutoresResultados resultado, Float pregunta, String respuesta) {
        switch (pregunta.toString()) {
            case "1.0":
                resultado.setR1(Short.parseShort(respuesta));
                break;
            case "2.0":
                resultado.setR2(Short.parseShort(respuesta));
                break;
            case "3.0":
                resultado.setR3(Short.parseShort(respuesta));
                break;
            case "4.0":
                resultado.setR4(Short.parseShort(respuesta));
                break;
            case "5.0":
                resultado.setR5(Short.parseShort(respuesta));
                break;
            case "6.0":
                resultado.setR6(Short.parseShort(respuesta));
                break;
            case "7.0":
                resultado.setR7(Short.parseShort(respuesta));
                break;
            case "8.0":
                resultado.setR8(Short.parseShort(respuesta));
                break;
            case "9.0":
                resultado.setR9(respuesta.trim());
                break;
        }
    }
}
