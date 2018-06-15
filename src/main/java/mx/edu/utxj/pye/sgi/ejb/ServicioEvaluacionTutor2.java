
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;


@Stateful
public class ServicioEvaluacionTutor2 implements EjbEvaluacionTutor2 {
    @EJB Facade f;
    @EJB Facade2 f2;
    
    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
//        l.add(new SelectItem("4", "No aplica", "No aplica"));Solo para pregunta 5
        l.add(new SelectItem("3", "Siempre", "Siempre"));
        l.add(new SelectItem("2", "Regularmente", "Regularmente"));
        l.add(new SelectItem("1", "Nunca", "Nunca"));
        
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
    public VistaEvaluacionesTutores getEstudianteTutor(Integer periodo, String matricula) {
        TypedQuery<VistaEvaluacionesTutores> q = f2.getEntityManager().createQuery("SELECT v from VistaEvaluacionesTutores v WHERE v.pk.matricula=:matricula AND v.pk.periodo=:periodo", VistaEvaluacionesTutores.class);
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
    public EvaluacionesTutoresResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionesTutores estudiante) {
        TypedQuery<EvaluacionesTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionesTutoresResultados r WHERE r.pk.evaluacion=:evaluacion AND r.pk.evaluador=:evaluador", EvaluacionesTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(estudiante.getPk().getMatricula()));
        List<EvaluacionesTutoresResultados> l = q.getResultList();
        
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            f.setEntityClass(EvaluacionesTutoresResultados.class);
            EvaluacionesTutoresResultados resultados = new EvaluacionesTutoresResultados(evaluacion.getEvaluacion(), Integer.parseInt(estudiante.getPk().getMatricula()), Integer.parseInt(estudiante.getPk().getNumeroNomina()));
            f.create(resultados);
            return resultados;
        }
    }
    @Override
    public EvaluacionesTutoresResultados getSoloResultados(Evaluaciones evaluacion, Integer estudiante) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getSoloResultados() matricula :_ " + estudiante);
        TypedQuery<EvaluacionesTutoresResultados> q = f.getEntityManager().createQuery("SELECT r FROM EvaluacionesTutoresResultados r WHERE r.pk.evaluacion=:evaluacion AND r.pk.evaluador=:evaluador", EvaluacionesTutoresResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", estudiante);
        List<EvaluacionesTutoresResultados> l = q.getResultList();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getSoloResultados() lista de estudiante ejb : " + l);
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void guardar(EvaluacionesTutoresResultados resultados) {
        f.setEntityClass(EvaluacionesTutoresResultados.class);
        f.edit(resultados);
    }

    @Override
    public void actualizar(String id, String valor, EvaluacionesTutoresResultados resultados) {
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
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) {
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares)f.find(evaluacion.getPeriodo());
    }

    @Override
    public List<VistaEvaluacionesTutores> getListaTutores() {
        TypedQuery<Listaperiodosescolares> periodo = f.getEntityManager().createQuery("SELECT p from Listaperiodosescolares p ORDER BY p.periodo DESC", Listaperiodosescolares.class);
        if (periodo.getResultList().isEmpty() || periodo.getResultList() == null) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getListaTutores() no se encontro ninguna lista de periodos");
        } else {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionTutor2.getListaTutores() el periodo es  --> : " + periodo);
        }
        TypedQuery<VistaEvaluacionesTutores> q = f2.getEntityManager().createQuery("SELECT v from VistaEvaluacionesTutores v WHERE v.pk.periodo = :periodo", VistaEvaluacionesTutores.class);
        q.setParameter("periodo", periodo.getSingleResult().getPeriodo());
        if(q.getResultList().isEmpty() || q.getResultList() == null){
            return null;
        }else{
            return q.getResultList();
        }
    }

    
}
