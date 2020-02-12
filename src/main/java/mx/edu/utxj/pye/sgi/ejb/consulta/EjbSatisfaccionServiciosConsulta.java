package mx.edu.utxj.pye.sgi.ejb.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.*;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.Serializador;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.awt.geom.Area;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

@Stateless
public class EjbSatisfaccionServiciosConsulta {
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEncuestaServicios ejbEncuestaServicios;
    @EJB EjbPacker ejbPacker;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado(@NonNull Evaluaciones evaluacion, DtoSatisfaccionServiciosCuestionario cuestionario){
        try{
            System.out.println("EjbSatisfaccionServiciosConsulta.calcularConcentrado");
//            System.out.println("evaluacion = " + evaluacion);
            Evaluaciones evaluacionBD = em.find(Evaluaciones.class, evaluacion.getEvaluacion());
            System.out.println("evaluacionBD = " + evaluacionBD);
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, evaluacion.getPeriodo());
            System.out.println("periodo = " + periodo);
            List<EncuestaServiciosResultados> encuestaServiciosResultados = em.createQuery("select r from EncuestaServiciosResultados r inner join r.evaluaciones e where r.evaluaciones=:evaluacion ", EncuestaServiciosResultados.class)
                    .setParameter("evaluacion", evaluacionBD)
                    .getResultList();
//            System.out.println("encuestaServiciosResultados = " + encuestaServiciosResultados.size());

            ResultadoEJB<List<DtoSatisfaccionServiciosEstudiante>> leerDtoSatisfaccionServiciosEstudianteSerializadas = leerDtoSatisfaccionServiciosEstudianteSerializadas(encuestaServiciosResultados, periodo.getPeriodo());
            if(leerDtoSatisfaccionServiciosEstudianteSerializadas.getCorrecto()){
                DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta = new DtoSatisfaccionServiciosEncuesta(evaluacionBD, periodo, periodo.getCiclo(), cuestionario);
                System.out.println("dtoSatisfaccionServiciosEncuesta 0 = " + dtoSatisfaccionServiciosEncuesta);
                dtoSatisfaccionServiciosEncuesta.setSatisfaccionServiciosEstudiantes(leerDtoSatisfaccionServiciosEstudianteSerializadas.getValor());
                return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEncuesta, "Concentrado de encuestas de satisfacción de servicios realizado.");
            }

            List<MatriculaPeriodosEscolares> matriculaPeriodosEscolaresList;
            ResultadoEJB<List<MatriculaPeriodosEscolares>> leerMatriculasSerializadas = leerMatriculasSerializadas(encuestaServiciosResultados, periodo.getPeriodo());
            if(leerMatriculasSerializadas.getCorrecto()) matriculaPeriodosEscolaresList = leerMatriculasSerializadas.getValor();
            else {
                matriculaPeriodosEscolaresList = em.createQuery("select m from MatriculaPeriodosEscolares m where m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                        .setParameter("periodo", evaluacion.getPeriodo())
                        .getResultList();
                matriculaPeriodosEscolaresList.parallelStream().forEach(matriculaPeriodosEscolares -> Serializador.serializarMatriculaPeriodoEscolar(matriculaPeriodosEscolares));
            }
//            System.out.println("matriculaPeriodosEscolaresList = " + matriculaPeriodosEscolaresList.size());
            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad a inner join a.categoria c where c.categoria=:categoria", AreasUniversidad.class)
                    .setParameter("categoria", (short) 9)
                    .getResultList();
//            System.out.println("programas.size() = " + programas.size());

            List<@NonNull DtoSatisfaccionServiciosEstudiante> dtoSatisfaccionServiciosEstudiantes = encuestaServiciosResultados
                    .parallelStream()
                    .map(resultados -> ejbPacker.packDtoSatisfaccionServiciosEstudiante(resultados, evaluacion, cuestionario, matriculaPeriodosEscolaresList, programas))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            System.out.println("dtoSatisfaccionServiciosEstudiantes = " + dtoSatisfaccionServiciosEstudiantes.size());
            dtoSatisfaccionServiciosEstudiantes.parallelStream().forEach(dtoSatisfaccionServiciosEstudiante -> Serializador.serializarDtoSatisfaccionServiciosEstudiante(dtoSatisfaccionServiciosEstudiante));

            DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta = new DtoSatisfaccionServiciosEncuesta(evaluacionBD, periodo, periodo.getCiclo(), cuestionario);
            System.out.println("dtoSatisfaccionServiciosEncuesta = " + dtoSatisfaccionServiciosEncuesta);
            dtoSatisfaccionServiciosEncuesta.setSatisfaccionServiciosEstudiantes(dtoSatisfaccionServiciosEstudiantes);
            return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEncuesta, "Concentrado de encuestas de satisfacción de servicios realizado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar crear el concentraro de encuesta de satisfacción de servicios (EjbSatisfaccionServiciosConsulta.calcularConcentrado).", e, DtoSatisfaccionServiciosEncuesta.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosCuestionario> generarCuestionario(){
        try{
            List<Apartado> apartados = ejbEncuestaServicios.getApartados();
            DtoSatisfaccionServiciosCuestionario cuestionario = new DtoSatisfaccionServiciosCuestionario(apartados);
            cuestionario.getApartados().forEach(apartado -> cuestionario.preguntas.addAll(apartado.getPreguntas()));
            cuestionario.ordenar();
            return ResultadoEJB.crearCorrecto(cuestionario, "Cuestionario generado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al generar el cuestionario para satisfacción de servicios (EjbSatisfaccionServiciosConsulta.generarCuestionario).", e, DtoSatisfaccionServiciosCuestionario.class);
        }
    }

    public ResultadoEJB<List<Evaluaciones>> buscarEvaluaciones(){
        try{
            //
            List<Evaluaciones> evaluaciones = em.createQuery("select e from Evaluaciones e where e.tipo=:tipo order by e.periodo desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.SERVICIOS.getLabel())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(evaluaciones, "Lista de evaluaciones");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, null);
        }
    }

    public ResultadoEJB<List<MatriculaPeriodosEscolares>> leerMatriculasSerializadas(List<EncuestaServiciosResultados> encuestaServiciosResultados, Integer periodo){
        try{
            List<MatriculaPeriodosEscolares> lista = new Vector<>();
            encuestaServiciosResultados.parallelStream().forEach(encuestaServiciosResultado -> {
                ResultadoEJB<MatriculaPeriodosEscolares> deserializarMatriculaPeriodoEscolar = Serializador.deserializarMatriculaPeriodoEscolar(encuestaServiciosResultado.getEncuestaServiciosResultadosPK().getEvaluador(), periodo);
                if(deserializarMatriculaPeriodoEscolar.getCorrecto()) lista.add(deserializarMatriculaPeriodoEscolar.getValor());
            });
            if(lista.isEmpty()) return ResultadoEJB.crearErroneo(2, null, "No se encontró ningún objeto serializado.");
            return ResultadoEJB.crearCorrecto(lista, "Objetos serializados encontrados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar deserializar los objetos", e, null);
        }
    }

    public ResultadoEJB<List<DtoSatisfaccionServiciosEstudiante>> leerDtoSatisfaccionServiciosEstudianteSerializadas(List<EncuestaServiciosResultados> encuestaServiciosResultados, Integer periodo){
        try{
            List<DtoSatisfaccionServiciosEstudiante> lista = new Vector<>();
            encuestaServiciosResultados.parallelStream().forEach(encuestaServiciosResultado -> {
                ResultadoEJB<DtoSatisfaccionServiciosEstudiante> deserializarDtoSatisfaccionServiciosEstudiante = Serializador.deserializarDtoSatisfaccionServiciosEstudiante(encuestaServiciosResultado.getEncuestaServiciosResultadosPK().getEvaluador(), periodo);
                if(deserializarDtoSatisfaccionServiciosEstudiante.getCorrecto()) lista.add(deserializarDtoSatisfaccionServiciosEstudiante.getValor());
            });
            if(lista.isEmpty()) return ResultadoEJB.crearErroneo(2, null, "No se encontró ningún objeto serializado.");
            return ResultadoEJB.crearCorrecto(lista, "Objetos serializados encontrados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar deserializar los objetos", e, null);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.ConteoInstitucional> calcularFrecuenciaInstitucional(DtoSatisfaccionServiciosEncuesta encuesta, Pregunta pregunta, BigDecimal respuesta){
        try{
            //
            DtoSatisfaccionServiciosEncuesta.ConteoInstitucional conteoInstitucional = new DtoSatisfaccionServiciosEncuesta.ConteoInstitucional(new DtoSatisfaccionServiciosEncuesta.DatoInstitucional(pregunta, respuesta), 0l);
            long frecuencia = encuesta.getSatisfaccionServiciosEstudiantes()
                    .stream()
                    .map(dtoSatisfaccionServiciosEstudiante -> dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().entrySet())
                    .flatMap(entries -> entries.stream())
                    .filter(preguntaBigDecimalEntry -> Objects.equals(preguntaBigDecimalEntry.getKey(), pregunta))
                    .map(preguntaBigDecimalEntry -> preguntaBigDecimalEntry.getValue())
                    .filter(bigDecimal -> bigDecimal.equals(respuesta))
                    .count();
            conteoInstitucional.setFrecuencia(frecuencia);
            return ResultadoEJB.crearCorrecto(conteoInstitucional, "Frecuencia calculada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar calcular la frecuencia por conteo institucional", e, DtoSatisfaccionServiciosEncuesta.ConteoInstitucional.class);
        }
    }
}
