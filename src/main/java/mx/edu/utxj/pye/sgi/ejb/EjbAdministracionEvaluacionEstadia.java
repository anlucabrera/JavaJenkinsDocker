package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Integer evaluacion = ejbEE.evaluacionEstadiaPeridoActual().getEvaluacion();
        List<EvaluacionEstadiaResultados> eer = f.getEntityManager().createQuery("select e from EvaluacionEstadiaResultados as e " +
                        "where e.evaluacionEstadiaResultadosPK.evaluador = :evaluador and e.evaluacionEstadiaResultadosPK.evaluacion = :evaluacion",EvaluacionEstadiaResultados.class)
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

    public List<ListaDatosAvanceEncuestaServicio> obtenerListaDatosAvanceEvaluacionEstadia(){
        List<ListaDatosAvanceEncuestaServicio> ldaes = new ArrayList<>();
        List<ListadoGraficaEncuestaServicios> lges = new ArrayList<>();
        List<ViewEstudianteAsesorAcademico> ae = obtenerEstudiantesAsesorAcademico();
        ae.forEach(x -> {
            try {
                EvaluacionEstadiaResultados listaCompleta = obtenerResultadosEvaluacionPorAlumno(x.getMatricula());
                Comparador<EvaluacionEstadiaResultados> comparador = new ComparadorEvaluacionEstadia();
                if (listaCompleta != null) {
                    if(comparador.isCompleto(listaCompleta)){
                        lges.add(new ListadoGraficaEncuestaServicios(x.getAbreviatura(), Long.parseLong(x.getMatricula())));
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbAdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Map<String, Long> gropingByCareer = lges.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
        gropingByCareer.forEach((k, v) -> {
            Map<String, Long> groupingByCareer = ae.stream().collect(Collectors.groupingBy(ViewEstudianteAsesorAcademico::getAbreviatura, Collectors.counting()));
            groupingByCareer.forEach((k1, v1) -> {
                if(k.equals(k1)){
                    ldaes.add(new ListaDatosAvanceEncuestaServicio(k, Math.toIntExact(v1), Math.toIntExact(v), Math.toIntExact(v1 - v), (v.doubleValue() * 100) / v1.doubleValue()));
                }
            });
        });
        return ldaes;
    }
}
