/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ListaDatosAvanceEncuestaServicio;
import mx.edu.utxj.pye.sgi.dto.ListadoGraficaEncuestaServicios;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbAdministracionEncuestaIng {

    @EJB private Facade f;
    @EJB private Facade2 f2;
    @EJB private EjbSatisfaccionEgresadosIng ejbES;
    @EJB private EjbAdministracionEncuestaServicios ejbADM;
    private EntityManager em, em2;
    @Getter
    @Setter
    List<DtoAlumnosEncuesta.DtoDirectores> dtoDirectores = new ArrayList<>();

    @PostConstruct
    public  void init(){
        em = f.getEntityManager(); em2 = f2.getEntityManager();
    }


    public AperturaVisualizacionEncuestas getAperturaActiva() {
        Integer encuesta = ejbES.getEvaluacionActiva().getEvaluacion();
        List<AperturaVisualizacionEncuestas> aVE = f.getEntityManager()
                .createQuery("SELECT a FROM AperturaVisualizacionEncuestas a "
                        + "WHERE a.encuesta =:encuesta AND :fecha "
                        + "BETWEEN a.fechaInicial AND a.fechaFinal ORDER BY a.encuesta DESC", AperturaVisualizacionEncuestas.class)
                .setParameter("encuesta", encuesta)
                .setParameter("fecha", new Date()).getResultStream()
                .collect(Collectors.toList());
        if(aVE.isEmpty()){
            return new AperturaVisualizacionEncuestas();
        }else{
            return aVE.get(0);
        }
    }

    public Evaluaciones evaluacionPeriodoActual() {
        String periodoEncuesta = Objects.requireNonNull(em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaSatisfaccionING")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", Integer.parseInt(periodoEncuesta))
                .setParameter("tipo", "Satisfacción de egresados de ingenieria")
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
        Integer grado = 11;
        return f2.getEntityManager().createQuery("SELECT a from AlumnosEncuestas as a WHERE a.grado = :grado", AlumnosEncuestas.class)
                .setParameter("grado", grado).getResultStream().collect(Collectors.toList());
    }

    /**
     * Metodo que ayuda con la coleccion de la informacion de la encuesta realizada por un estudiante
     * @param matricula parametro que se envia para realizar la busqueda
     * @return devuelve los datos del estudiante
     */
    public EncuestaSatisfaccionEgresadosIng obtenerResultadosEncServXMatricula(Integer matricula) {
        Integer evaluacionActiva = evaluacionPeriodoActual().getEvaluacion();
        List<EncuestaSatisfaccionEgresadosIng> esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaSatisfaccionEgresadosIng as e where e.encuestaSatisfaccionEgresadosIngPK.evaluador = :matricula AND e.encuestaSatisfaccionEgresadosIngPK.evaluacion =:evaluacion", EncuestaSatisfaccionEgresadosIng.class)
                .setParameter("evaluacion", evaluacionActiva)
                .setParameter("matricula", matricula).getResultStream().collect(Collectors.toList());
        if(esr.isEmpty()){
            return new EncuestaSatisfaccionEgresadosIng();
        }else{
            return esr.get(0);
        }
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerAlumnosOnceavo(){
        String periodoEscolar = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "periodoEncuestaSatisfaccionING")
                .getResultStream().findFirst().orElse(null)).getValor();
        String grado4 = Objects.requireNonNull(f.getEntityManager().createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "grado4")
                .getResultStream().findFirst().orElse(null)).getValor();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuesta;
        dtoAlumnosEncuesta = em2.createQuery("select a from Alumnos as a " +
                "where (a.cveStatus = :estatus or a.cveStatus = :estatus2) " +
                "and a.grupos.gruposPK.cvePeriodo = :periodo " +
                "and (a.gradoActual = :grado4)", Alumnos.class)
                .setParameter("estatus", 1)
                .setParameter("estatus2", 6)
                .setParameter("periodo", Integer.parseInt(periodoEscolar))
                .setParameter("grado4", Short.parseShort(grado4))
                .getResultStream().map(alumnos -> ejbADM.packEstudiantesEncuesta(alumnos)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaqueto con éxito.");
    }
    /**
     * Metodo de busqueda de todos los estudiantes de acuerdo al director
     * @param cveDirector cadena que se envia para realizar la busqueda
     * @return devuelve la informacion de todos los estudiantes que se encuentran a su cargo
     */
    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> obtenerResultadosXDirector(String cveDirector) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnos = obtenerAlumnosOnceavo().getValor()
                .stream().filter(director -> director.getDtoDirector().getCvePersona() == Integer.parseInt(cveDirector))
                .collect(Collectors.toList());
        if(dtoAlumnos.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnos;
        }
    }

    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> obtenerResultadosXTutor(Integer tutor) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuestaTutor = obtenerAlumnosOnceavo().getValor()
                .stream()
                .filter(alumno -> alumno.getGrupos().getCveMaestro().equals(tutor)).collect(Collectors.toList());
        if(dtoAlumnosEncuestaTutor.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaTutor;
        }
    }

    public List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> avanceEncuestaSatIngPorGrupo(){
        List<ListaDatosAvanceEncuestaServicio.AvanceEncuestaServiciosPorGrupo> aespg = new ArrayList<>();
        List<DtoAlumnosEncuesta> dtoAE = new ArrayList<>();
        Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae1 = obtenerAlumnosOnceavo().getValor();
        ae1.forEach(x -> {
            EncuestaSatisfaccionEgresadosIng esr = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
            if(esr != null){
                if(comparador.isCompleto(esr)){
                    dtoAE.add(new DtoAlumnosEncuesta(
                            x.getAlumnos().getMatricula(), x.getGrupos().getGruposPK().getCvePeriodo(), x.getPersonas().getNombre(), x.getPersonas().getApellidoPat(), x.getPersonas().getApellidoMat(),
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

    public List<ListaDatosAvanceEncuestaServicio> obtenerListaDatosAvanceEncuestaSatIng(){
        List<ListaDatosAvanceEncuestaServicio> ldaes = new ArrayList<>();
        List<ListadoGraficaEncuestaServicios> lges = new ArrayList<>();
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> ae = obtenerAlumnosOnceavo().getValor();
        ae.forEach(x -> {
            try {
                EncuestaSatisfaccionEgresadosIng listaCompleta = obtenerResultadosEncServXMatricula(Integer.parseInt(x.getAlumnos().getMatricula()));
                Comparador<EncuestaSatisfaccionEgresadosIng> comparador = new ComparadorEncuestaSatisfaccionEgresadosIng();
                if (listaCompleta != null) {
                    if(comparador.isCompleto(listaCompleta)){
                        lges.add(new ListadoGraficaEncuestaServicios(x.getSiglas(), Long.parseLong(x.getAlumnos().getMatricula())));
                    }
                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbAdministracionEncuestaServicios.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Map<String, Long> gropingByCareer = lges.stream().collect(Collectors.groupingBy(ListadoGraficaEncuestaServicios::getSiglas, Collectors.counting()));
        gropingByCareer.forEach((k, v) -> {
            Map<String, Long> groupingByCareer = ae.stream().collect(Collectors.groupingBy(DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral::getSiglas, Collectors.counting()));
            groupingByCareer.forEach((k1, v1) -> {
                if(k.equals(k1)){
                    ldaes.add(new ListaDatosAvanceEncuestaServicio(k, Math.toIntExact(v1), Math.toIntExact(v), Math.toIntExact(v1 - v), (v.doubleValue() * 100) / v1.doubleValue()));
                }
            });
        });
        return ldaes;
    }
}
