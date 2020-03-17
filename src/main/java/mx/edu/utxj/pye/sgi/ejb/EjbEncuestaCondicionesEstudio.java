package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudioPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCondicionesEstudio;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Stateless
public class EjbEncuestaCondicionesEstudio {
    @EJB Facade f;
    @EJB Facade2 f2;
    EntityManager em;
    EntityManager em2;
    Comparador<EncuestaCondicionesEstudio> comparador = new ComparadorCondicionesEstudio();

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }

    public Evaluaciones getEvaluacionActiva() {
        return em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                .setParameter("tipo", "Encuesta de Condiciones de Estudio")
                .setParameter("fecha", new Date())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public EncuestaCondicionesEstudio getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas) {
        try {
            EncuestaCondicionesEstudioPK pk = new EncuestaCondicionesEstudioPK(evaluacion.getEvaluacion(), evaluador);

            EncuestaCondicionesEstudio r = (EncuestaCondicionesEstudio) em.find(EncuestaCondicionesEstudio.class, pk);

            if (r == null) {
                r = new EncuestaCondicionesEstudio(pk);
                r.setEvaluaciones(evaluacion);
//                f.create(r);
                em.persist(r);
                em.flush();
            }

            if (r.getR1() != null) respuestas.put("p1", r.getR1().toString());

            if (r.getR2() != null) respuestas.put("p2", r.getR2().toString());

            if (r.getR3() != null) respuestas.put("p3", r.getR3().toString());

            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    public boolean actualizarResultado(EncuestaCondicionesEstudio resultado) {
        if(resultado != null) em.merge(resultado);

        return comparador.isCompleto(resultado);
    }

    public void actualizarRespuestaPorPregunta(EncuestaCondicionesEstudio resultado, String pregunta, String respuesta, Map<String,String> respuestas) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(respuesta); break;
            case "p2": resultado.setR2(respuesta); break;
            case "p3": resultado.setR3(respuesta); break;
        }

        respuestas.put(pregunta, respuesta);
    }

}
