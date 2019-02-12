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
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateful
public class ServicioAdministracionEncuestaServicios implements EjbAdministracionEncuestaServicios {

    @EJB private Facade f;
    @EJB private Facade2 f2;
    @EJB private EjbEncuestaServicios ejbES;
    
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
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a",AlumnosEncuestas.class).getResultStream().collect(Collectors.toList());
        return ae;
    }
    
    /**
     * Metodo que ayuda con la coleccion de la informacion de la encuesta realizada por un estudiante
     * @param matricula parametro que se envia para realizar la busqueda
     * @return devuelve los datos del estudiante
     */
    @Override
    public EncuestaServiciosResultados obtenerResultadosEncServXMatricula(Integer matricula) {
        //////////////////////////////*****Pruebas****\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        //List<DtoAlumnosEncuesta> dto = obtenerAlumnos();
        //List<DtoAlumnosEncuesta.DtoTutores> tutores = obtenerTutores();
        //System.out.println("Obtener alumnos:"+ dto);
        //////////////////////////////*****Pruebas****\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        Integer evaluacionActiva = ejbES.getEvaluacionActiva().getEvaluacion();
        List<EncuestaServiciosResultados> esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaServiciosResultados as e where e.encuestaServiciosResultadosPK.evaluador = :matricula AND e.encuestaServiciosResultadosPK.evaluacion =:evaluacion",EncuestaServiciosResultados.class)
                .setParameter("evaluacion", evaluacionActiva)
                .setParameter("matricula", matricula).getResultStream().collect(Collectors.toList());
        if(esr.isEmpty()){
            return new EncuestaServiciosResultados();
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
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a where a.cveDirector = :cveDirector",AlumnosEncuestas.class)
                .setParameter("cveDirector", cveDirector).getResultStream().collect(Collectors.toList());
        if(ae.isEmpty()){
            return new ArrayList<>();
        }else{
            return ae;
        }
    }

    
    
}
