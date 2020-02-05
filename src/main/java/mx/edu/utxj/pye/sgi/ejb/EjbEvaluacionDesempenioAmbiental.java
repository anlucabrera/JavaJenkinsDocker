package mx.edu.utxj.pye.sgi.ejb;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionAmbiental;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionCodigoEtica;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.*;

@Stateless
public class EjbEvaluacionDesempenioAmbiental {

    @EJB Facade f;
    @EJB Facade2 f2;
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }


    public ResultadoEJB<Filter<PersonalActivo>> validarPersonal(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(ep.leerPropiedadEntera("clavePersona").orElse(409)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como la persona especificada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbValidacionRol.validarDocente)", e, null);
        }
    }

    public ResultadoEJB<Alumnos> verificarEstudiante(String matricula){
        try{
            //verificar apertura del evento
            Integer periodo = Integer.parseInt(Objects.requireNonNull(em.createQuery("select v from VariablesProntuario v where v.nombre = :nombre", VariablesProntuario.class)
                    .setParameter("nombre", "periodoEncuestaSatisfaccionING").getResultStream().findFirst().orElse(null)).getValor());
            Alumnos alumnos = f2.getEntityManager().createQuery("select a from Alumnos as a where a.matricula = :matricula and a.cveStatus = :estatus " +
                    "and a.grupos.gruposPK.cvePeriodo = :periodo", Alumnos.class)
                    .setParameter("estatus", 1)
                    .setParameter("matricula", matricula)
                    .setParameter("periodo", periodo)
                    .getResultStream().findFirst().orElse(null);
            if(alumnos == null){
                return ResultadoEJB.crearErroneo(2,alumnos, "No existe evento aperturado del tipo solicitado.");
            }else{
                return ResultadoEJB.crearCorrecto(alumnos, "Alumno encontrado con éxito.");
            }

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para generacion de grupos (EjbGeneracionGrupos.).", e, Alumnos.class);
        }
    }

    public ResultadoEJB<Personas> obtenerEstudiante(Integer idEstudiante){
        try{
            //verificar apertura del evento
            Personas alumnos = f2.getEntityManager().createQuery("select p from Personas as p where p.personasPK.cvePersona = :cvePersona", Personas.class)
                    .setParameter("cvePersona", idEstudiante)
                    .getResultStream().findFirst().orElse(new Personas());
            return ResultadoEJB.crearCorrecto(alumnos, "Evento aperturado.");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para generacion de grupos (EjbGeneracionGrupos.).", e, Personas.class);
        }
    }

    /**
     * Permite verificar si hay un periodo abierto para reincoporaciones
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<Evaluaciones> verificarEvaluacion(){
        try{
            //verificar apertura del evento
            Evaluaciones eventoEscolar = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", "Evaluación del Desempeño Ambiental de la UTXJ")
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2,eventoEscolar, "No existe evento aperturado del tipo solicitado.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para generacion de grupos (EjbGeneracionGrupos.).", e, Evaluaciones.class);
        }
    }

    public ResultadoEJB<List<Apartado>> apartados(){
        List<Apartado> a = new ArrayList<>();
        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 1f, "Consumo de energía eléctrica", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 2f, "Consumo de agua", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 3f, "Vertidos de agua residual", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 4f, "Generación de basura", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 5f, "Derrames químicos", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 6f, "Emisiones a la atmósfera", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 7f, "Ruido", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 8f, "Plagas sin control", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 9f, "Mal uso del estacionamiento", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_AMBIENTAL.getNumero() , 1f, 10f, "Comentarios o recomendaciones", ""));
        a.add(a1);
        return ResultadoEJB.crearCorrecto(a, "Se genero las preguntas correctamente");
    }

    public ResultadoEJB<EvaluacionDesempenioAmbientalUtxj> getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas) {
        try {
            EvaluacionDesempenioAmbientalUtxjPK pk = new EvaluacionDesempenioAmbientalUtxjPK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(EvaluacionDesempenioAmbientalUtxj.class);

            EvaluacionDesempenioAmbientalUtxj r = (EvaluacionDesempenioAmbientalUtxj) f.find(pk);

            if (r == null) {
                r = new EvaluacionDesempenioAmbientalUtxj(pk);
                r.setFechaElaboracion(new Date());
                em.persist(r);
            }

            if (r.getR1() != null) {
                respuestas.put("p1", r.getR1());
            }
            if (r.getR11() != null) {
                respuestas.put("p11", r.getR11());
            }
            if (r.getR2() != null) {
                respuestas.put("p2", r.getR2());
            }
            if (r.getR21() != null) {
                respuestas.put("p21", r.getR21());
            }
            if (r.getR3() != null) {
                respuestas.put("p3", r.getR3());
            }
            if (r.getR31() != null) {
                respuestas.put("p31", r.getR31());
            }
            if (r.getR4() != null) {
                respuestas.put("p4", r.getR4());
            }
            if (r.getR41() != null) {
                respuestas.put("p41", r.getR41());
            }
            if (r.getR5() != null) {
                respuestas.put("p5", r.getR5());
            }
            if (r.getR51() != null) {
                respuestas.put("p51", r.getR51());
            }
            if (r.getR6() != null) {
                respuestas.put("p6", r.getR6());
            }
            if (r.getR61() != null) {
                respuestas.put("p61", r.getR61());
            }
            if (r.getR7() != null) {
                respuestas.put("p7", r.getR7());
            }
            if (r.getR71() != null) {
                respuestas.put("p71", r.getR71());
            }
            if (r.getR8() != null) {
                respuestas.put("p8", r.getR8());
            }
            if (r.getR81() != null) {
                respuestas.put("p81", r.getR81());
            }
            if (r.getR9() != null) {
                respuestas.put("p9", r.getR9());
            }
            if (r.getR91() != null) {
                respuestas.put("p91", r.getR91());
            }
            if (r.getR10() != null) {
                respuestas.put("p10", r.getR10());
            }
            if (r.getR111() != null) {
                respuestas.put("p111", r.getR111());
            }
            return ResultadoEJB.crearCorrecto(r, "");
        } catch (NullPointerException ne) {
            return null;
        }
    }

    public boolean actualizarResultado(EvaluacionDesempenioAmbientalUtxj resultado) {
        if(resultado != null){
            f.setEntityClass(EvaluacionDesempenioAmbientalUtxj.class);
            f.edit(resultado);
        }

        Comparador<EvaluacionDesempenioAmbientalUtxj> comparador = new ComparadorEvaluacionAmbiental();
        return comparador.isCompleto(resultado);
    }

    public void actualizarRespuestaPorPregunta(EvaluacionDesempenioAmbientalUtxj resultado, String pregunta, String respuesta, Map<String,String> respuestas) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(respuesta); break;
            case "p11": resultado.setR11(respuesta); break;
            case "p2": resultado.setR2(respuesta); break;
            case "p21": resultado.setR21(respuesta); break;
            case "p3": resultado.setR3(respuesta); break;
            case "p31": resultado.setR31(respuesta); break;
            case "p4": resultado.setR4(respuesta); break;
            case "p41": resultado.setR41(respuesta); break;
            case "p5": resultado.setR5(respuesta); break;
            case "p51": resultado.setR51(respuesta); break;
            case "p6": resultado.setR6(respuesta); break;
            case "p61": resultado.setR61(respuesta); break;
            case "p7": resultado.setR7(respuesta); break;
            case "p71": resultado.setR71(respuesta); break;
            case "p8": resultado.setR8(respuesta); break;
            case "p81": resultado.setR81(respuesta); break;
            case "p9": resultado.setR9(respuesta); break;
            case "p91": resultado.setR91(respuesta); break;
            case "p10": resultado.setR10(respuesta); break;
            case "p111": resultado.setR111(respuesta); break;
        }

        respuestas.put(pregunta, respuesta);
    }
}
