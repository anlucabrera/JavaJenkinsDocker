/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import com.google.zxing.common.detector.MathUtils;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionEncuestaServicios {


    @EJB
    private Facade f;
    @EJB private Facade2 f2;

    public Evaluaciones evaluacionEstadiaPeridoActual() {
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", 51)
                .setParameter("tipo", "Servicios")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }
    }

    /**
     * Metodo que ayuda a la busqueda de todos los estudiantes activos en el periodo actual
     * @return Devuelve la lista completa de los estudiantes activos
     */
    public List<AlumnosEncuestas> obtenerAlumnosNoAccedieron() {
        Short grado = 11;
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a",AlumnosEncuestas.class).getResultStream().collect(Collectors.toList());
        return ae;
    }

    /**
     * Metodo que ayuda con la coleccion de la informacion de la encuesta realizada por un estudiante
     * @param matricula parametro que se envia para realizar la busqueda
     * @return devuelve los datos del estudiante
     */
    public EncuestaServiciosResultados obtenerResultadosEncServXMatricula(Integer matricula) {
        Integer evaluacionActiva = evaluacionEstadiaPeridoActual().getEvaluacion();
        List<EncuestaServiciosResultados> esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaServiciosResultados as e where e.encuestaServiciosResultadosPK.evaluador = :matricula AND " +
                        "e.encuestaServiciosResultadosPK.evaluacion =:evaluacion",EncuestaServiciosResultados.class)
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
    public List<AlumnosEncuestas> obtenerResultadosXDirector(String cveDirector) {
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("select a from AlumnosEncuestas as a where a.cveDirector = :cveDirector",AlumnosEncuestas.class)
                .setParameter("cveDirector", cveDirector).getResultStream().collect(Collectors.toList());
        if(ae.isEmpty()){
            return new ArrayList<>();
        }else{
            return ae;
        }
    }

    public List<AlumnosEncuestas> obtenerResultadosXTutor(Integer cveMaestro) {
        //Short grado = 11;
        List<AlumnosEncuestas> ae = f2.getEntityManager().createQuery("SELECT a FROM AlumnosEncuestas a WHERE a.cveMaestro = :cveMaestro",AlumnosEncuestas.class)
                .setParameter("cveMaestro", cveMaestro).getResultStream().collect(Collectors.toList());
        if(ae.isEmpty()){
            return new ArrayList<>();
        }else{
            return ae;
        }
    }

    public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> avanceEncuestaServiciosPorGrupo(){
        List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> aespg = new ArrayList<>();
        List<DtoAlumnosEncuesta> dtoAE = new ArrayList<>();
        Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
        List<AlumnosEncuestas> ae1 = obtenerAlumnosNoAccedieron();
        ae1.forEach(x -> {
            EncuestaServiciosResultados esr = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
            if(esr != null){
                if(comparador.isCompleto(esr)){
                    dtoAE.add(new DtoAlumnosEncuesta(
                            x.getMatricula(), x.getCvePeriodo(), x.getNombre(), x.getApellidoPat(), x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getCveStatus(),
                            x.getCveMaestro(), x.getNombreTutor(), x.getApPatTutor(), x.getApMatTutor(), x.getCveDirector()
                    ));
                }
            }
        });
        Map<String, Map<String, Map<Short, Long>>> groupingByCareerByGroupAndQuarter = dtoAE
                .stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta::getSiglas,
                        Collectors.groupingBy(DtoAlumnosEncuesta::getGrupo,
                                Collectors.groupingBy(DtoAlumnosEncuesta::getGrado, Collectors.counting()))));
        groupingByCareerByGroupAndQuarter.forEach((k,v) -> {
            v.forEach((k1, v1) -> {
                v1.forEach((k2, v2) -> {
                    Map<String, Map<String, Map<Short, Long>>> groupingByCareerByGroupAndQuarterStudents = ae1
                            .stream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura,
                                    Collectors.groupingBy(AlumnosEncuestas::getIdGrupo,
                                            Collectors.groupingBy(AlumnosEncuestas::getGrado, Collectors.counting()))));
                    groupingByCareerByGroupAndQuarterStudents.forEach((k3, v3) -> {
                        v3.forEach((k4, v4) -> {
                            v4.forEach((k5, v5) -> {
                                if(k.equals(k3) && k1.equals(k4) && k2.equals(k5)){
                                    Integer totalPorCarrera = Math.toIntExact(v5);
                                    Integer totalEST = Math.toIntExact(v2);
                                    Integer faltantesPorContestar = totalPorCarrera - totalEST;
                                    Double percentage = (totalEST.doubleValue() * 100) / totalPorCarrera.doubleValue();
                                    aespg.add(new ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo(
                                            k3, k4, k5, totalPorCarrera, totalEST, faltantesPorContestar, percentage
                                    ));
                                }
                            });
                        });
                    });
                });
            });
        });
        return aespg;
    }

    public List<ListaDatosAvanceEncuestaServicio> obtenerListaDatosAvanceEncuestaServicio(){
        List<ListaDatosAvanceEncuestaServicio> ldaes = new ArrayList<>();
        List<ListadoGraficaEncuestaServicios> lges = new ArrayList<>();
        List<AlumnosEncuestas> ae = obtenerAlumnosNoAccedieron();
        ae.forEach(x -> {
            try {
                EncuestaServiciosResultados listaCompleta = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getMatricula()));
                Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();
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
            Map<String, Long> groupingByCareer = ae.stream().collect(Collectors.groupingBy(AlumnosEncuestas::getAbreviatura, Collectors.counting()));
            groupingByCareer.forEach((k1, v1) -> {
                if(k.equals(k1)){
                    ldaes.add(new ListaDatosAvanceEncuestaServicio(k, Math.toIntExact(v1), Math.toIntExact(v), Math.toIntExact(v1 - v), (v.doubleValue() * 100) / v1.doubleValue()));
                }
            });
        });
        return ldaes;
    }
}
