/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionEncuestaTsu {


    @EJB
    private Facade f;
    @EJB private Facade2 f2;
    @EJB private EjbAdministracionEncuestaServicios ejbAES;


    public Evaluaciones evaluacionEstadiaPeridoActual() {
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", 51)
                .setParameter("tipo", "Satisfacción de egresados de TSU")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }
    }

    public ResultadosEncuestaSatisfaccionTsu getResultadoEncPorEvaluador(Integer matricula){
        Integer evaluacionActiva = evaluacionEstadiaPeridoActual().getEvaluacion();
        TypedQuery<ResultadosEncuestaSatisfaccionTsu> q = f.getEntityManager()
                .createQuery("SELECT r FROM ResultadosEncuestaSatisfaccionTsu r " +
                        "WHERE r.resultadosEncuestaSatisfaccionTsuPK.evaluador= :matricula and " +
                        "r.resultadosEncuestaSatisfaccionTsuPK.evaluacion = :evaluacion",ResultadosEncuestaSatisfaccionTsu.class);
        q.setParameter("evaluacion", evaluacionActiva);
        q.setParameter("matricula", matricula);
        List<ResultadosEncuestaSatisfaccionTsu> pr=q.getResultList();
        if(pr.isEmpty()){
            return new ResultadosEncuestaSatisfaccionTsu();
        }else{
            return pr.get(0);
        }
    }

    public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> avanceEncuestaServiciosPorGrupoTutor(){
        List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> aespg = new ArrayList<>();
        List<DtoAlumnosEncuesta> dtoAE = new ArrayList<>();
        Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
        List<AlumnosEncuestas> ae1 = ejbAES.obtenerAlumnosNoAccedieron();
        ae1.forEach(x -> {
            ResultadosEncuestaSatisfaccionTsu est = getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
            if(est != null){
                if(comparador.isCompleto(est)){
                    String matricula = x.getMatricula();
                    dtoAE.add(new DtoAlumnosEncuesta(
                            matricula, x.getCvePeriodo(), x.getNombre(), x.getApellidoPat(), x.getApellidoMat(), x.getAbreviatura(), x.getGrado(), x.getIdGrupo(), x.getCveStatus(), x.getCveMaestro(),
                            x.getNombreTutor(), x.getApPatTutor(), x.getApMatTutor(), x.getCveDirector()
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

    public List<ListaDatosAvanceEncuestaServicio> obtenerListaDatosAvanceEncuestaSatisfaccion(){
        List<ListaDatosAvanceEncuestaServicio> ldaes = new ArrayList<>();
        List<ListadoGraficaEncuestaServicios> lges = new ArrayList<>();
        List<AlumnosEncuestas> ae = ejbAES.obtenerAlumnosNoAccedieron();
        ae.forEach(x -> {
            try {
                ResultadosEncuestaSatisfaccionTsu listaCompleta = getResultadoEncPorEvaluador(Integer.parseInt(x.getMatricula()));
                Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
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
