package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class EjbAdministracionEvaluacionEstadia {

    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbEvaluacionEstadia ejbEE;

    public List<ViewEstudianteAsesorAcademico> obtenerEstudiantesAsesorAcademico(){
        List<ViewEstudianteAsesorAcademico> eaa = new ArrayList<>();
        eaa = f2.getEntityManager().createQuery("select v from ViewEstudianteAsesorAcademico as v", ViewEstudianteAsesorAcademico.class).getResultList();
        return eaa;
    }

    public EvaluacionEstadiaResultados obtenerResultadosEvaluacionPorAlumno(String evaluador){
        Integer evaluacion = ejbEE.getEvaluacionActiva().getEvaluacion();
        List<EvaluacionEstadiaResultados> eer = f.getEntityManager().createQuery("select e from EvaluacionEstadiaResultados as e " +
                        "where e.evaluador = :evaluador and e.evaluacionEstadiaResultadosPK.evaluacion = :evaluacion",EvaluacionEstadiaResultados.class)
                .setParameter("evaluador", evaluador)
                .setParameter("evaluacion", evaluacion)
                .getResultStream().collect(Collectors.toList());
        if(!eer.isEmpty()){
            return eer.get(0);
        }else {
            return new EvaluacionEstadiaResultados();
        }
    }

    public List<ViewEstudianteAsesorAcademico> obtenerEstudiantesPorDirector(String cveDirector){
        List<ViewEstudianteAsesorAcademico> eaa = f2.getEntityManager().createQuery("select v from ViewEstudianteAsesorAcademico as v where v.cveDirector = :cveDirector",ViewEstudianteAsesorAcademico.class)
                .setParameter("cveDirector", cveDirector)
                .getResultStream().collect(Collectors.toList());
        if(eaa.isEmpty()){
            return new ArrayList<>();
        }else {
            return eaa;
        }
    }

    public List<ViewEstudianteAsesorAcademico> obtenerEstudiantesPorTutor(Integer cveMaestro){
        List<ViewEstudianteAsesorAcademico> eaa = f2.getEntityManager()
                .createQuery("select v from ViewEstudianteAsesorAcademico as v where v.cveMaestro = :cveMaestro",ViewEstudianteAsesorAcademico.class)
                .setParameter("cveMaestro", cveMaestro).getResultStream().collect(Collectors.toList());
        if(eaa.isEmpty()){
            return new ArrayList<>();
        }else {
            return eaa;
        }
    }
}
