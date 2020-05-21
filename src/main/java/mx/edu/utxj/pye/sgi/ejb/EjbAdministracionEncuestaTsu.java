/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosTsu;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager(); em2 = f2.getEntityManager();
    }

    public Evaluaciones evaluacionEstadiaPeridoActual() {
        String periodoEscolar = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaSatisfaccionTSU")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", Integer.parseInt(periodoEscolar))
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
                        "r.resultadosEncuestaSatisfaccionTsuPK.evaluacion = :evaluacion", ResultadosEncuestaSatisfaccionTsu.class);
        q.setParameter("evaluacion", evaluacionActiva);
        q.setParameter("matricula", matricula);
        List<ResultadosEncuestaSatisfaccionTsu> pr=q.getResultList();
        if(pr.isEmpty()){
            return new ResultadosEncuestaSatisfaccionTsu();
        }else{
            return pr.get(0);
        }
    }


    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerAlumnosNoAccedieron() {
        String periodoEscolar = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaSatisfaccionTSU")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado2 = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado2")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuesta = new ArrayList<>();
        dtoAlumnosEncuesta = em2.createQuery("select a from Alumnos as a " +
                "where (a.cveStatus = :estatus or a.cveStatus = :estatus2) " +
                "and a.grupos.gruposPK.cvePeriodo = :periodo " +
                "and a.gradoActual = :grado2", Alumnos.class)
                .setParameter("estatus", 1)
                .setParameter("estatus2", 6)
                .setParameter("periodo", Integer.parseInt(periodoEscolar))
                .setParameter("grado2", Short.parseShort(grado2))
                .getResultStream().map(alumnos -> ejbAES.packEstudiantesEncuesta(alumnos)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaqueto con éxito.");
    }

    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> obtenerResultadosXDirector(String cveDirector) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuestaDirector = obtenerAlumnosNoAccedieron().getValor()
                .stream()
                .filter(alumno -> alumno.getDtoDirector().getCvePersona() == Integer.parseInt(cveDirector)).collect(Collectors.toList());
        if(dtoAlumnosEncuestaDirector.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaDirector;
        }
    }

    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> obtenerResultadosXTutor(Integer cveMaestro) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuestaTutor = obtenerAlumnosNoAccedieron().getValor()
                .stream()
                .filter(alumno -> alumno.getTutor().getPersonasPK().getCvePersona() == cveMaestro).collect(Collectors.toList());
        if(dtoAlumnosEncuestaTutor.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaTutor;
        }
    }

    public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> avanceEncuestaServiciosPorGrupoTutor(){
        List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> aespg = new ArrayList<>();
        List<DtoAlumnosEncuesta> dtoAE = new ArrayList<>();
        Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae1 = obtenerAlumnosNoAccedieron().getValor();
        ae1.forEach(x -> {
            ResultadosEncuestaSatisfaccionTsu est = getResultadoEncPorEvaluador(Integer.parseInt(x.getAlumnos().getMatricula()));
            if(est != null){
                if(comparador.isCompleto(est)){
                    String matricula = x.getAlumnos().getMatricula();
                    dtoAE.add(new DtoAlumnosEncuesta(
                            matricula, x.getGrupos().getGruposPK().getCvePeriodo(), x.getPersonas().getNombre(), x.getPersonas().getApellidoPat(), x.getPersonas().getApellidoMat(),
                            x.getCarrerasCgut().getAbreviatura(), x.getAlumnos().getGradoActual(), x.getGrupos().getIdGrupo(), x.getAlumnos().getCveStatus(), x.getTutor().getPersonasPK().getCvePersona(),
                            x.getTutor().getNombre(), x.getTutor().getApellidoPat(), x.getTutor().getApellidoMat(), String.valueOf(x.getDtoDirector().getCvePersona())
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
                            .stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getSiglas,
                                    Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getGrupo,
                                            Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getGrado, Collectors.counting()))));
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
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae = obtenerAlumnosNoAccedieron().getValor();
        ae.stream().filter(a -> a.getGrado().equals(Short.parseShort("6"))).forEach(x -> {
            try {
                ResultadosEncuestaSatisfaccionTsu listaCompleta = getResultadoEncPorEvaluador(Integer.parseInt(x.getAlumnos().getMatricula()));
                Comparador<ResultadosEncuestaSatisfaccionTsu> comparador = new ComparadorEncuestaSatisfaccionEgresadosTsu();
                if (listaCompleta != null) {
                    if(comparador.isCompleto(listaCompleta)){
                        lges.add(new ListadoGraficaEncuestaServicios(x.getCarrerasCgut().getAbreviatura(), Long.parseLong(x.getAlumnos().getMatricula())));
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbAdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Map<String, Long> gropingByCareer = lges.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
        gropingByCareer.forEach((k, v) -> {
            Map<String, Long> groupingByCareer = ae.stream().filter(a -> a.getGrado().equals(Short.parseShort("6")))
                    .collect(Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getSiglas, Collectors.counting()));
            groupingByCareer.forEach((k1, v1) -> {
                if(k.equals(k1)){
                    ldaes.add(new ListaDatosAvanceEncuestaServicio(k, Math.toIntExact(v1), Math.toIntExact(v), Math.toIntExact(v1 - v), (v.doubleValue() * 100) / v1.doubleValue()));
                }
            });
        });
        return ldaes;
    }

}
