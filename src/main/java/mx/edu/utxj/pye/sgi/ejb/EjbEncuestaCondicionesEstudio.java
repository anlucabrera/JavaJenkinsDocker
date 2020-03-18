package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.DtoAlumnosEncuesta;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudio;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaCondicionesEstudioPK;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCondicionesEstudio;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class EjbEncuestaCondicionesEstudio {
    @EJB Facade f;
    @EJB Facade2 f2;
    EntityManager em;
    EntityManager em2;
    Comparador<EncuestaCondicionesEstudio> comparador = new ComparadorCondicionesEstudio();
    @EJB EjbAdministracionEncuestaServicios ejbES;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }

    public Evaluaciones evaluacionEstadiaPeridoActual() {
        List<Evaluaciones> e = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones as e where e.periodo = :periodo and e.tipo = :tipo", Evaluaciones.class)
                .setParameter("periodo", Integer.parseInt("53"))
                .setParameter("tipo", "Encuesta de Condiciones de Estudio")
                .getResultStream().collect(Collectors.toList());
        if (e.isEmpty()) {
            return new Evaluaciones();
        } else {
            return e.get(0);
        }
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

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral>> obtenerAlumnosNoAccedieron() {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneral> dtoAlumnosEncuesta = new ArrayList<>();
        dtoAlumnosEncuesta = em2.createQuery("select a from Alumnos as a " +
                "where (a.cveStatus = :estatus) " +
                "and a.grupos.gruposPK.cvePeriodo = :periodo " +
                "and (a.gradoActual = :grado1 or a.gradoActual = :grado2 or a.gradoActual = :grado3)", Alumnos.class)
                .setParameter("estatus", 1)
                .setParameter("periodo", 53)
                .setParameter("grado1", Short.parseShort("5"))
                .setParameter("grado2", Short.parseShort("8"))
                .setParameter("grado3", Short.parseShort("11"))
                .getResultStream().map(alumnos -> ejbES.packEstudiantesEncuesta(alumnos)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());

        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaqueto con éxito.");
    }

    public ResultadoEJB<List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar>> obtenerAlumnosNoAccedieronCE(){
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAlumnosEncuesta = new ArrayList<>();
        dtoAlumnosEncuesta = em.createQuery("select e from Estudiante as e where e.tipoEstudiante.idTipoEstudiante = :tipoE and e.periodo = :periodo", Estudiante.class)
                .setParameter("tipoE", 1)
                .setParameter("periodo", 53)
                .getResultStream()
                .map(estudiante -> ejbES.packEstudiantesEncuesta(estudiante)).filter(ResultadoEJB::getCorrecto).map(ResultadoEJB::getValor).collect(Collectors.toList());
        return ResultadoEJB.crearCorrecto(dtoAlumnosEncuesta, "Se empaquetó con éxito");
    }

    public EncuestaCondicionesEstudio obtenerResultadoEncuesta(Integer matricula) {
        Integer evaluacionActiva = evaluacionEstadiaPeridoActual().getEvaluacion();
        EncuestaCondicionesEstudio esr = f.getEntityManager()
                .createQuery("SELECT e FROM EncuestaCondicionesEstudio as e where e.encuestaCondicionesEstudioPK.evaluador = :matricula AND " +
                        "e.encuestaCondicionesEstudioPK.evaluacion =:evaluacion", EncuestaCondicionesEstudio.class)
                .setParameter("evaluacion", evaluacionActiva)
                .setParameter("matricula", matricula).getResultStream().findFirst().orElse(new EncuestaCondicionesEstudio());
        return esr;
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

    public List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> obtenerResultadosXDirectorCE(Integer cveDirector) {
        List<DtoAlumnosEncuesta.DtoAlumnosEncuestaGeneralControlEscolar> dtoAlumnosEncuestaDirector = obtenerAlumnosNoAccedieronCE().getValor()
                .stream()
                .filter(alumno -> alumno.getDtoDirector().getCvePersona() == cveDirector).collect(Collectors.toList());
        //System.out.println("Alumnos por director:"+ dtoAlumnosEncuestaDirector);
        if(dtoAlumnosEncuestaDirector.isEmpty()){
            return new ArrayList<>();
        }else{
            return dtoAlumnosEncuestaDirector;
        }
    }

}
