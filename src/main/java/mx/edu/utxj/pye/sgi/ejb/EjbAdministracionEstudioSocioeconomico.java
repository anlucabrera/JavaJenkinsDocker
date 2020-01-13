package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EjbAdministracionEstudioSocioeconomico {

    @EJB Facade f; @EJB Facade2 f2; @EJB EjbEvaluacionEstudioSocioeconomico ejbES;

    public AperturaVisualizacionEncuestas getAperturaActiva() {
        Integer encuesta = ejbES.evaluacionActiva().getEvaluacion();
        List<AperturaVisualizacionEncuestas> aVE = f.getEntityManager()
                .createQuery("SELECT a FROM AperturaVisualizacionEncuestas a "
                        + "WHERE a.encuesta =:encuesta AND :fecha "
                        + "BETWEEN a.fechaInicial AND a.fechaFinal ORDER BY a.encuesta DESC",AperturaVisualizacionEncuestas.class)
                .setParameter("encuesta", encuesta)
                .setParameter("fecha", new Date()).getResultStream()
                .collect(Collectors.toList());
        if(aVE.isEmpty()){
            return new AperturaVisualizacionEncuestas();
        }else{
            return aVE.get(0);
        }
    }

    public List<AlumnosEncuestas> alumnosEncuestas(){
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a",AlumnosEncuestas.class).getResultStream().collect(Collectors.toList());
        return ae;
    }

    public EvaluacionesEstudioSocioeconomicoResultados evaluacionesEstudioSocioeconomicoResultadoXMatricula(Integer matricula){
        Integer evaluacionActiva = ejbES.evaluacionActiva().getEvaluacion();
        List<EvaluacionesEstudioSocioeconomicoResultados> eesr = f.getEntityManager()
                .createQuery("select e from EvaluacionesEstudioSocioeconomicoResultados as e " +
                        "where e.evaluacionesEstudioSocioeconomicoResultadosPK.evaluador = :matricula " +
                        "and e.evaluacionesEstudioSocioeconomicoResultadosPK.evaluacion = :evaluacion",EvaluacionesEstudioSocioeconomicoResultados.class)
                .setParameter("matricula", matricula)
                .setParameter("evaluacion", evaluacionActiva)
                .getResultStream().collect(Collectors.toList());
        if(eesr.isEmpty()){
            return new EvaluacionesEstudioSocioeconomicoResultados();
        }else {
            return eesr.get(0);
        }
    }

    public List<AlumnosEncuestas> alumnosEncuestasPorDirector(String cveDirector){
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a where a.cveDirector = :cveDirector",AlumnosEncuestas.class)
                .setParameter("cveDirector", cveDirector).getResultStream().collect(Collectors.toList());
        if(ae.isEmpty()){
            return new ArrayList<>();
        }else{
            return ae;
        }
    }

    public List<AlumnosEncuestas> alumnosEncuestasPorTutor(Integer cveMaestro){
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a where a.cveMaestro = :cveMaestro",AlumnosEncuestas.class)
                .setParameter("cveMaestro", cveMaestro).getResultStream().collect(Collectors.toList());
        if(ae.isEmpty()){
            return new ArrayList<>();
        }else{
            return ae;
        }
    }


}
