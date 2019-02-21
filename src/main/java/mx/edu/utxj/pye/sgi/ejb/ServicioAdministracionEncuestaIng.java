/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateful
public class ServicioAdministracionEncuestaIng implements EjbAdministracionEncuestaIng {
    
    @EJB private Facade f;
    @EJB private Facade2 f2;
    @EJB private EjbSatisfaccionEgresadosIng ejbES;
    
    @Override
    public AperturaVisualizacionEncuestas getAperturaActiva() {
        Integer encuesta = ejbES.getEvaluacionActiva().getEvaluacion();
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
    
    /**
     * Metodo que ayuda a la busqueda de todos los estudiantes activos en el periodo actual
     * @return Devuelve la lista completa de los estudiantes activos
     */
    @Override
    public List<AlumnosEncuestas> obtenerAlumnosNoAccedieron() {
        Integer grado = 11;
        return f2.getEntityManager().createQuery("SELECT a from AlumnosEncuestas as a WHERE a.grado = :grado",AlumnosEncuestas.class)
                .setParameter("grado", grado).getResultStream().collect(Collectors.toList());
    }
    
    /**
     * Metodo que ayuda con la coleccion de la informacion de la encuesta realizada por un estudiante
     * @param matricula parametro que se envia para realizar la busqueda
     * @return devuelve los datos del estudiante
     */
    @Override
    public EncuestaSatisfaccionEgresadosIng obtenerResultadosEncServXMatricula(Integer matricula) {
        Integer evaluacionActiva = ejbES.getEvaluacionActiva().getEvaluacion();
        List<EncuestaSatisfaccionEgresadosIng> esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaSatisfaccionEgresadosIng as e where e.encuestaSatisfaccionEgresadosIngPK.evaluador = :matricula AND e.encuestaSatisfaccionEgresadosIngPK.evaluacion =:evaluacion",EncuestaSatisfaccionEgresadosIng.class)
                .setParameter("evaluacion", evaluacionActiva)
                .setParameter("matricula", matricula).getResultStream().collect(Collectors.toList());
        if(esr.isEmpty()){
            return new EncuestaSatisfaccionEgresadosIng();
        }else{
            return esr.get(0);
        }
    }
    
    /**
     * Metodo de busqueda de todos los estudiantes de acuerdo al director
     * @param cveDirector cadena que se envia para realizar la busqueda
     * @return devuelve la informacion de todos los estudiantes que se encuentran a su cargo
     */
    @Override
    public List<AlumnosEncuestas> obtenerResultadosXDirector(String cveDirector) {
        Integer grado = 11;
        List<AlumnosEncuestas> a = f2.getEntityManager()
                .createQuery("SELECT a FROM AlumnosEncuestas as a WHERE a.cveDirector =:cveDirector AND a.grado = :grado",AlumnosEncuestas.class)
                .setParameter("cveDirector", cveDirector).setParameter("grado", grado).getResultStream().collect(Collectors.toList());
        if(a.isEmpty()){
            return new ArrayList<>();
        }else{
            return a;
        }
    }
    
    @Override
    public List<AlumnosEncuestas> obtenerAlumnosOnceavo(){
        List<AlumnosEncuestas> aeo = f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas AS a",AlumnosEncuestas.class).getResultStream().collect(Collectors.toList());
        return aeo;
    }
    
    @Override
    public List<AlumnosEncuestas> obtenerResultadosXTutor(Integer tutor) {
        Integer grado = 11;
        List<AlumnosEncuestas> a = f2.getEntityManager()
                .createQuery("SELECT a FROM AlumnosEncuestas as a WHERE a.cveMaestro =:cveMaestro AND a.grado = :grado",AlumnosEncuestas.class)
                .setParameter("cveMaestro", tutor).setParameter("grado", grado).getResultStream().collect(Collectors.toList());
        if(a.isEmpty()){
            return new ArrayList<>();
        }else{
            return a;
        }
    }
    
}
