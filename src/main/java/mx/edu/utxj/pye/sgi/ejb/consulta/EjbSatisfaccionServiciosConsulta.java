package mx.edu.utxj.pye.sgi.ejb.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.DtoAreaAcademica;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.*;
import mx.edu.utxj.pye.sgi.ejb.EjbEncuestaServicios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.*;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.SatisfaccionServiciosApartado;
import mx.edu.utxj.pye.sgi.enums.converter.SatisfaccionServiciosApartadoConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.Serializador;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
//        System.out.println("EjbSatisfaccionServiciosConsulta.init");
    }

    public synchronized ResultadoEJB<DtoSatisfaccionServiciosEncuesta> calcularConcentrado(@NonNull Evaluaciones evaluacion, DtoSatisfaccionServiciosCuestionario cuestionario, ServiciosConsultaDto dto2){
        try{
            final Integer serviciosConsultaSatisfaccionServiciosSerializacion = ep.leerPropiedadEntera("serviciosConsultaSatisfaccionServiciosSerializacion").orElse(0);

//            System.out.println();
//            System.out.println("EjbSatisfaccionServiciosConsulta.calcularConcentrado");
//            System.out.println("evaluacion = " + evaluacion);
            if(dto2.hayContenedor(evaluacion)) {
//                System.out.println("Contenedor ya disponible");
                return ResultadoEJB.crearCorrecto(dto2.getContenedores().get(evaluacion), "Contenedor ya estaba disponible");
            }
//            System.out.println("EjbSatisfaccionServiciosConsulta.calcularConcentrado");
//            System.out.println("evaluacion = " + evaluacion);
            Evaluaciones evaluacionBD = em.find(Evaluaciones.class, evaluacion.getEvaluacion());
//            System.out.println("evaluacionBD = " + evaluacionBD);

            if(serviciosConsultaSatisfaccionServiciosSerializacion != 0){
                ResultadoEJB<DtoSatisfaccionServiciosEncuesta> deserializarDtoSatisfaccionServiciosEncuesta = Serializador.deserializarDtoSatisfaccionServiciosEncuesta(evaluacion.getTipo(), evaluacion.getEvaluacion());
                if(deserializarDtoSatisfaccionServiciosEncuesta.getCorrecto()){
//                System.out.println("deserializarDtoSatisfaccionServiciosEncuesta 0 = " + deserializarDtoSatisfaccionServiciosEncuesta);
                    return ResultadoEJB.crearCorrecto(deserializarDtoSatisfaccionServiciosEncuesta.getValor(), "Concentrado localizado como serializado");
                }
            }

            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, evaluacion.getPeriodo());
//            System.out.println("periodo = " + periodo);
            List<EncuestaServiciosResultados> encuestaServiciosResultados;
            ResultadoEJB<List<EncuestaServiciosResultados>> leerEncuestaServiciosResultadosSerializadas = leerEncuestaServiciosResultadosSerializadas(evaluacionBD.getEvaluacion());
            if(leerEncuestaServiciosResultadosSerializadas.getCorrecto()) {
                encuestaServiciosResultados = leerEncuestaServiciosResultadosSerializadas.getValor();
//                System.out.println("leerEncuestaServiciosResultadosSerializadas 0 = " + leerEncuestaServiciosResultadosSerializadas);
            }
            else {
//                System.out.println("leerEncuestaServiciosResultadosSerializadas 1 = " + leerEncuestaServiciosResultadosSerializadas);
                encuestaServiciosResultados = em.createQuery("select r from EncuestaServiciosResultados r inner join r.evaluaciones e where r.evaluaciones=:evaluacion ", EncuestaServiciosResultados.class)
                        .setParameter("evaluacion", evaluacionBD)
                        .getResultList();

                if(serviciosConsultaSatisfaccionServiciosSerializacion != 0)
                    encuestaServiciosResultados.parallelStream().forEach(encuestaServiciosResultados1 -> Serializador.serializarEncuestaServiciosResultados(encuestaServiciosResultados1));
            }
//            System.out.println("encuestaServiciosResultados = " + encuestaServiciosResultados.size());

            ResultadoEJB<List<DtoSatisfaccionServiciosEstudiante>> leerDtoSatisfaccionServiciosEstudianteSerializadas = leerDtoSatisfaccionServiciosEstudianteSerializadas(encuestaServiciosResultados, periodo.getPeriodo());
            if(leerDtoSatisfaccionServiciosEstudianteSerializadas.getCorrecto()){
                DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta = new DtoSatisfaccionServiciosEncuesta(evaluacionBD, periodo, periodo.getCiclo(), cuestionario);
//                System.out.println("dtoSatisfaccionServiciosEncuesta 0 = " + dtoSatisfaccionServiciosEncuesta);
                dtoSatisfaccionServiciosEncuesta.setSatisfaccionServiciosEstudiantes(leerDtoSatisfaccionServiciosEstudianteSerializadas.getValor());
//                System.out.println("1 = " + 1);
                calcularFrecuencias(dtoSatisfaccionServiciosEncuesta, dto2.getRespuestas(), dtoSatisfaccionServiciosEncuesta.getSatisfaccionServiciosEstudiantes());
//                System.out.println("2 = " + 2);
                if(serviciosConsultaSatisfaccionServiciosSerializacion != 0) Serializador.serializarDtoSatisfaccionServiciosEncuesta(dtoSatisfaccionServiciosEncuesta);
//                System.out.println("3 = " + 3);
                return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEncuesta, "Concentrado de encuestas de satisfacción de servicios realizado.");
            }

            List<MatriculaPeriodosEscolares> matriculaPeriodosEscolaresList;
            ResultadoEJB<List<MatriculaPeriodosEscolares>> leerMatriculasSerializadas = leerMatriculasSerializadas(encuestaServiciosResultados, periodo.getPeriodo());
            if(leerMatriculasSerializadas.getCorrecto()) matriculaPeriodosEscolaresList = leerMatriculasSerializadas.getValor();
            else {
                matriculaPeriodosEscolaresList = em.createQuery("select m from MatriculaPeriodosEscolares m where m.periodo=:periodo", MatriculaPeriodosEscolares.class)
                        .setParameter("periodo", evaluacion.getPeriodo())
                        .getResultList();

                if(serviciosConsultaSatisfaccionServiciosSerializacion != 0)
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
//            System.out.println("dtoSatisfaccionServiciosEstudiantes = " + dtoSatisfaccionServiciosEstudiantes.size());
            if(serviciosConsultaSatisfaccionServiciosSerializacion != 0)
                dtoSatisfaccionServiciosEstudiantes.parallelStream().forEach(dtoSatisfaccionServiciosEstudiante -> Serializador.serializarDtoSatisfaccionServiciosEstudiante(dtoSatisfaccionServiciosEstudiante));

            DtoSatisfaccionServiciosEncuesta dtoSatisfaccionServiciosEncuesta = new DtoSatisfaccionServiciosEncuesta(evaluacionBD, periodo, periodo.getCiclo(), cuestionario);
//            System.out.println("dtoSatisfaccionServiciosEncuesta = " + dtoSatisfaccionServiciosEncuesta);
            dtoSatisfaccionServiciosEncuesta.setSatisfaccionServiciosEstudiantes(dtoSatisfaccionServiciosEstudiantes);
            calcularFrecuencias(dtoSatisfaccionServiciosEncuesta, dto2.getRespuestas(), dtoSatisfaccionServiciosEstudiantes);

            if(serviciosConsultaSatisfaccionServiciosSerializacion != 0)
                Serializador.serializarDtoSatisfaccionServiciosEncuesta(dtoSatisfaccionServiciosEncuesta);
            return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEncuesta, "Concentrado de encuestas de satisfacción de servicios realizado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar crear el concentraro de encuesta de satisfacción de servicios (EjbSatisfaccionServiciosConsulta.calcularConcentrado).", e, DtoSatisfaccionServiciosEncuesta.class);
        }
    }

    public ResultadoEJB<List<DtoAreaAcademica>> getProgramasEvaluacion(Evaluaciones evaluacion, DtoSatisfaccionServiciosEncuesta encuesta){
        try{
            final Integer serviciosConsultaSatisfaccionServiciosSerializacion = ep.leerPropiedadEntera("serviciosConsultaSatisfaccionServiciosSerializacion").orElse(0);

            if(serviciosConsultaSatisfaccionServiciosSerializacion != 0){
                ResultadoEJB<List<DtoAreaAcademica>> leerDtoAreaAcademicaSerializadas = leerDtoAreaAcademicaSerializadas(evaluacion.getPeriodo());
                if(leerDtoAreaAcademicaSerializadas.getCorrecto()) return ResultadoEJB.crearCorrecto(leerDtoAreaAcademicaSerializadas.getValor(), "Objetos ya serializados");
            }

            List<AreasUniversidad> areas = em.createQuery("select a from AreasUniversidad a inner join a.categoria c where c.categoria=:categoria order by a.nombre", AreasUniversidad.class).setParameter("categoria", (short) 8).getResultList();
            List<AreasUniversidad> programas = encuesta.getSatisfaccionServiciosEstudiantes()
                    .stream()
                    .map(DtoSatisfaccionServiciosEstudiante::getDtoEstudiantePeriodo)
                    .map(DtoEstudiantePeriodo::getPrograma)
                    .distinct()
                    .sorted(DtoSatisfaccionServiciosEncuesta.areasUniversidadComparator)
                    .collect(Collectors.toList());
//            programas.stream().map(AreasUniversidad::getNombre).forEach(System.out::println);
            //em.createQuery("select a from AreasUniversidad a inner join a.categoria c inner join a.nivelEducativo n where c.categoria=:categoria and a.vigente = '1' order by n.nivel desc, a.vigente desc, a.nombre asc", AreasUniversidad.class).setParameter("categoria", (short) 9).getResultList();
            List<DtoAreaAcademica> dtoAreaAcademicas = new Vector<>();
            areas.parallelStream().forEachOrdered(area -> {
                DtoAreaAcademica dtoAreaAcademica = new DtoAreaAcademica(area, evaluacion.getPeriodo());
                List<AreasUniversidad> programasPorArea = programas.stream().filter(programa -> Objects.equals(programa.getAreaSuperior(), area.getArea())).collect(Collectors.toList());
                dtoAreaAcademica.setProgramas(programasPorArea);
                if(serviciosConsultaSatisfaccionServiciosSerializacion != 0) Serializador.serializarDtoAreaAcademica(dtoAreaAcademica);
                dtoAreaAcademicas.add(dtoAreaAcademica);
            });
            return ResultadoEJB.crearCorrecto(dtoAreaAcademicas.stream().sorted(Comparator.comparing(dtoAreaAcademica -> dtoAreaAcademica.getAreaAcademica().getArea())).collect(Collectors.toList()),"Areas académicas empaquetadas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar crear el concentraro de encuesta de satisfacción de servicios (EjbSatisfaccionServiciosConsulta.getProgramasEvaluacion).", e , null);
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
            List<Evaluaciones> evaluaciones = em.createQuery("select e from Evaluaciones e where e.tipo=:tipo and not (current_date between e.fechaInicio and e.fechaFin) order by e.periodo desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.SERVICIOS.getLabel())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(evaluaciones, "Lista de evaluaciones");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, null);
        }
    }

    public ResultadoEJB<List<MatriculaPeriodosEscolares>> leerMatriculasSerializadas(List<EncuestaServiciosResultados> encuestaServiciosResultados, Integer periodo){
        final Integer serviciosConsultaSatisfaccionServiciosSerializacion = ep.leerPropiedadEntera("serviciosConsultaSatisfaccionServiciosSerializacion").orElse(0);

        if(serviciosConsultaSatisfaccionServiciosSerializacion == 0)
            return ResultadoEJB.crearErroneo(-1, null, "La serialización está desactivada");

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

    public ResultadoEJB<List<EncuestaServiciosResultados>> leerEncuestaServiciosResultadosSerializadas(Integer periodo){
        final Integer serviciosConsultaSatisfaccionServiciosSerializacion = ep.leerPropiedadEntera("serviciosConsultaSatisfaccionServiciosSerializacion").orElse(0);

        if(serviciosConsultaSatisfaccionServiciosSerializacion == 0)
            return ResultadoEJB.crearErroneo(-1, null, "La serialización está desactivada");


        try{
            ResultadoEJB<List<EncuestaServiciosResultados>> deserializarMasivo = Serializador.deserializarMasivo(EncuestaServiciosResultados.class.getName(), periodo, EncuestaServiciosResultados.class);
            if(deserializarMasivo.getCorrecto()) return ResultadoEJB.crearCorrecto(deserializarMasivo.getValor(), deserializarMasivo.getMensaje());
            else return ResultadoEJB.crearErroneo(2, null, deserializarMasivo.getMensaje());
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar deserializar los objetos", e, null);
        }
    }

    public ResultadoEJB<List<DtoAreaAcademica>> leerDtoAreaAcademicaSerializadas(Integer periodoDeConsulta){
        final Integer serviciosConsultaSatisfaccionServiciosSerializacion = ep.leerPropiedadEntera("serviciosConsultaSatisfaccionServiciosSerializacion").orElse(0);

        if(serviciosConsultaSatisfaccionServiciosSerializacion == 0)
            return ResultadoEJB.crearErroneo(-1, null, "La serialización está desactivada");

        try{
            ResultadoEJB<List<DtoAreaAcademica>> deserializarMasivo = Serializador.deserializarMasivo(DtoAreaAcademica.class.getName(), periodoDeConsulta, DtoAreaAcademica.class);
            if(deserializarMasivo.getCorrecto()) return ResultadoEJB.crearCorrecto(deserializarMasivo.getValor(), deserializarMasivo.getMensaje());
            else return ResultadoEJB.crearErroneo(2, null, deserializarMasivo.getMensaje());
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar deserializar los objetos", e, null);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.Conteo> calcularFrecuenciaPrograma(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad programa, Pregunta pregunta, BigDecimal respuesta){
        try{
            DtoSatisfaccionServiciosEncuesta.Conteo conteoPrograma = new DtoSatisfaccionServiciosEncuesta.Conteo(new DtoSatisfaccionServiciosEncuesta.Dato(programa, pregunta, respuesta), 0l);
//            if(pregunta.getNumero() < 2 && programa.getArea() == 49){
//                System.out.println("EjbSatisfaccionServiciosConsulta.calcularFrecuenciaPrograma");
//                System.out.println("encuesta = " + encuesta + ", programa = " + programa.getNombre() + ", pregunta = " + pregunta + ", respuesta = " + respuesta);
//                encuesta.getSatisfaccionServiciosEstudiantes()
//                        .stream()
//                        .filter(dtoSatisfaccionServiciosEstudiante -> dtoSatisfaccionServiciosEstudiante.getDtoEstudiantePeriodo().getPrograma().getArea().intValue() == programa.getArea().intValue())
//                        .map(DtoSatisfaccionServiciosEstudiante::getDtoEstudiantePeriodo)
//                        .map(DtoEstudiantePeriodo::getPrograma)
//                        .forEach(System.out::println);
//            }
            long frecuencia = encuesta.getSatisfaccionServiciosEstudiantes()
                    .stream()
                    .filter(dtoSatisfaccionServiciosEstudiante -> Objects.equals(dtoSatisfaccionServiciosEstudiante.getDtoEstudiantePeriodo().getPrograma().getArea(), programa.getArea()))
                    .map(dtoSatisfaccionServiciosEstudiante -> dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().entrySet())
                    .flatMap(entries -> entries.stream())
                    .filter(preguntaBigDecimalEntry -> Objects.equals(preguntaBigDecimalEntry.getKey(), pregunta))
                    .map(preguntaBigDecimalEntry -> preguntaBigDecimalEntry.getValue())
                    .filter(bigDecimal -> bigDecimal.equals(respuesta))
                    .count();
            conteoPrograma.setFrecuencia(frecuencia);
            return ResultadoEJB.crearCorrecto(conteoPrograma, "Frecuencia calculada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar calcular la frecuencia por conteo de programa", e, DtoSatisfaccionServiciosEncuesta.Conteo.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.ConteoInstitucional> calcularFrecuenciaInstitucional(DtoSatisfaccionServiciosEncuesta encuesta, Pregunta pregunta, BigDecimal respuesta){
        try{
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

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucional> calcularFrecuenciaPorFila(DtoSatisfaccionServiciosEncuesta encuesta, Pregunta pregunta, @NonNull List<BigDecimal> respuestas){
        try{
            if(encuesta.getPreguntaFilaInstitucionalMap().containsKey(pregunta)){
                return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaInstitucionalMap().get(pregunta), "Calculo ya realizado");
            } else encuesta.getPreguntaFilaInstitucionalMap().put(pregunta, new DtoSatisfaccionServiciosEncuesta.FilaInstitucional(pregunta));
            respuestas.parallelStream().forEach(respuesta -> {
                ResultadoEJB<DtoSatisfaccionServiciosEncuesta.ConteoInstitucional> calcularFrecuenciaInstitucional = calcularFrecuenciaInstitucional(encuesta, pregunta, respuesta);
                if(calcularFrecuenciaInstitucional.getCorrecto()){
                    DtoSatisfaccionServiciosEncuesta.@NonNull ConteoInstitucional conteoInstitucional = calcularFrecuenciaInstitucional.getValor();
                    encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().put(respuesta, conteoInstitucional);
                }else {
                    DtoSatisfaccionServiciosEncuesta.@NonNull ConteoInstitucional conteoInstitucional = new DtoSatisfaccionServiciosEncuesta.ConteoInstitucional(new DtoSatisfaccionServiciosEncuesta.DatoInstitucional(pregunta, respuesta), 0l);
                    encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().put(respuesta, conteoInstitucional);
                }
            });
            long totalG = encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucional::getFrecuencia)
                    .sum();
            long totalH = encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucional::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucional::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucional::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setTotalG(totalG);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setTotalH(totalH);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setTotalJ(totalJ);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setTotalK(totalK);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setTotalBase10(totalBase10);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getPreguntaFilaInstitucionalMap().get(pregunta).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaInstitucionalMap().get(pregunta), "Calculo realizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaPorFila"), e, DtoSatisfaccionServiciosEncuesta.FilaInstitucional.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaPrograma> calcularFrecuenciaPorFila(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad programa, Pregunta pregunta, @NonNull List<BigDecimal> respuestas){
        try{
            DtoSatisfaccionServiciosEncuesta.FilaProgramaPK pk = new DtoSatisfaccionServiciosEncuesta.FilaProgramaPK(programa, pregunta);
            if(encuesta.getPreguntaFilaProgramaMap().containsKey(pk)){
                return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaProgramaMap().get(pk), "Calculo ya realizado");
            } else encuesta.getPreguntaFilaProgramaMap().put(pk, new DtoSatisfaccionServiciosEncuesta.FilaPrograma(pk));
            respuestas.parallelStream().forEach(respuesta -> {
                ResultadoEJB<DtoSatisfaccionServiciosEncuesta.Conteo> calcularFrecuenciaPrograma = calcularFrecuenciaPrograma(encuesta, programa, pregunta, respuesta);
                if(calcularFrecuenciaPrograma.getCorrecto()){
                    DtoSatisfaccionServiciosEncuesta.@NonNull Conteo conteoPrograma = calcularFrecuenciaPrograma.getValor();
                    encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().put(respuesta, conteoPrograma);
                }else {
                    DtoSatisfaccionServiciosEncuesta.@NonNull Conteo conteoprograma = new DtoSatisfaccionServiciosEncuesta.Conteo(new DtoSatisfaccionServiciosEncuesta.Dato(programa, pregunta, respuesta), 0l);
                    encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().put(respuesta, conteoprograma);
                }
            });
            long totalG = encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            long totalH = encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getPreguntaFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setTotalG(totalG);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setTotalH(totalH);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setTotalJ(totalJ);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setTotalK(totalK);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setTotalBase10(totalBase10);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getPreguntaFilaProgramaMap().get(pk).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaProgramaMap().get(pk), "Calculo realizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaPorFila"), e, DtoSatisfaccionServiciosEncuesta.FilaPrograma.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado> calcularFrecuenciaPorApartado(DtoSatisfaccionServiciosEncuesta encuesta, Apartado apartado, @NonNull List<BigDecimal> respuestas){
        try{
            //
            if(encuesta.getApartadoFilaInstitucionalMap().containsKey(apartado)){
                return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaInstitucionalMap().get(apartado), "Calculo ya realizado");
            }else encuesta.getApartadoFilaInstitucionalMap().put(apartado, new DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado(apartado));

            List<DtoSatisfaccionServiciosEncuesta.FilaInstitucional> filasInstitucionales = encuesta.getPreguntaFilaInstitucionalMap().entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getKey().getApartado(), apartado.getId()))
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filasInstitucionales
                        .stream()
                        .map(DtoSatisfaccionServiciosEncuesta.FilaInstitucional::getDatoInstitucionalMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteoInstitucional -> conteoInstitucional.getFrecuencia())
                        .sum();
                encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalApartado(new DtoSatisfaccionServiciosEncuesta.DatoInstitucionalApartado(apartado, respuesta), suma));
            });

            long totalG = encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalApartado::getFrecuencia)
                    .sum();

            long totalH = encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalApartado::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalApartado::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getApartadoFilaInstitucionalMap().get(apartado).getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalApartado::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setTotalG(totalG);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setTotalH(totalH);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setTotalJ(totalJ);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setTotalK(totalK);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setTotalBase10(totalBase10);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getApartadoFilaInstitucionalMap().get(apartado).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaInstitucionalMap().get(apartado), "Calculo realizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaPorApartado"), e, DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> calcularFrecuenciaPorApartado(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad programa, Apartado apartado, @NonNull List<BigDecimal> respuestas){
        try{
            DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK pk = new DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK(programa, apartado);
//            System.out.println("pk = " + pk);
            if(encuesta.getApartadoFilaProgramaMap().containsKey(pk)){
//                System.out.println("pk ya almacenada = " + pk);
                return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaProgramaMap().get(pk), "Calculo ya realizado");
            }else encuesta.getApartadoFilaProgramaMap().put(pk, new DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado(pk));

            List<DtoSatisfaccionServiciosEncuesta.FilaPrograma> filasInstitucionales = encuesta.getPreguntaFilaProgramaMap().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().getPrograma().getArea().intValue() == pk.getPrograma().getArea().intValue())
                    .filter(entry -> Objects.equals(entry.getKey().getPregunta().getApartado(), apartado.getId()))
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filasInstitucionales
                        .stream()
                        .map(DtoSatisfaccionServiciosEncuesta.FilaPrograma::getDatoMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteo -> conteo.getFrecuencia())
                        .sum();
                encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.ConteoApartado(new DtoSatisfaccionServiciosEncuesta.DatoApartado(programa, apartado, respuesta), suma));
            });

            long totalG = encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();

            long totalH = encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getApartadoFilaProgramaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getApartadoFilaProgramaMap().get(pk).setTotalG(totalG);
            encuesta.getApartadoFilaProgramaMap().get(pk).setTotalH(totalH);
            encuesta.getApartadoFilaProgramaMap().get(pk).setTotalJ(totalJ);
            encuesta.getApartadoFilaProgramaMap().get(pk).setTotalK(totalK);
            encuesta.getApartadoFilaProgramaMap().get(pk).setTotalBase10(totalBase10);
            encuesta.getApartadoFilaProgramaMap().get(pk).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getApartadoFilaProgramaMap().get(pk).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaProgramaMap().get(pk), "Calculo realizado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaPorApartado"), e, DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalGeneral> calcularFrecuenciaGeneral(DtoSatisfaccionServiciosEncuesta encuesta, @NonNull List<BigDecimal> respuestas){
        try{
            //
            if(encuesta.getFilaInstitucionalGeneral() != null) return ResultadoEJB.crearCorrecto(encuesta.getFilaInstitucionalGeneral(), "Calculo ya realizado");
            else encuesta.setFilaInstitucionalGeneral(new DtoSatisfaccionServiciosEncuesta.FilaInstitucionalGeneral());

            List<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado> filas = new Vector<>(encuesta.getApartadoFilaInstitucionalMap().values());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filas
                        .stream()
                        .map(DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado::getDatoInstitucionalMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteoInstitucional -> conteoInstitucional.getFrecuencia())
                        .sum();
                encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalGeneral(respuesta, suma));
            });

            long totalG = encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalGeneral::getFrecuencia)
                    .sum();

            long totalH = encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalGeneral::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalGeneral::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getFilaInstitucionalGeneral().getDatoInstitucionalMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoInstitucionalGeneral::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getFilaInstitucionalGeneral().setTotalG(totalG);
            encuesta.getFilaInstitucionalGeneral().setTotalH(totalH);
            encuesta.getFilaInstitucionalGeneral().setTotalJ(totalJ);
            encuesta.getFilaInstitucionalGeneral().setTotalK(totalK);
            encuesta.getFilaInstitucionalGeneral().setTotalBase10(totalBase10);
            encuesta.getFilaInstitucionalGeneral().setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getFilaInstitucionalGeneral().setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getFilaInstitucionalGeneral(), "Calculo realizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaGeneral"), e, DtoSatisfaccionServiciosEncuesta.FilaInstitucionalGeneral.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaGeneral> calcularFrecuenciaGeneral(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad programa, @NonNull List<BigDecimal> respuestas){
        try{
            if(encuesta.getFilaProgramaGeneralMap().containsKey(programa)) return ResultadoEJB.crearCorrecto(encuesta.getFilaProgramaGeneralMap().get(programa), "Calculo ya realizado");
            else encuesta.getFilaProgramaGeneralMap().put(programa, new DtoSatisfaccionServiciosEncuesta.FilaProgramaGeneral(programa));

            List<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> filas = new Vector<>(encuesta.getApartadoFilaProgramaMap().values());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filas
                        .stream()
                        .filter(filaProgramaApartado -> filaProgramaApartado.getPk().getPrograma().getArea().intValue() == programa.getArea().intValue())
                        .map(DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado::getDatoMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteo -> conteo.getFrecuencia())
                        .sum();
                encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.ConteoGeneral(new DtoSatisfaccionServiciosEncuesta.DatoGeneral(programa, respuesta), suma));
            });

            long totalG = encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoGeneral::getFrecuencia)
                    .sum();

            long totalH = encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoGeneral::getFrecuencia)
                    .sum();
            long totalJ = encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoGeneral::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getFilaProgramaGeneralMap().get(programa).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoGeneral::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getFilaProgramaGeneralMap().get(programa).setTotalG(totalG);
            encuesta.getFilaProgramaGeneralMap().get(programa).setTotalH(totalH);
            encuesta.getFilaProgramaGeneralMap().get(programa).setTotalJ(totalJ);
            encuesta.getFilaProgramaGeneralMap().get(programa).setTotalK(totalK);
            encuesta.getFilaProgramaGeneralMap().get(programa).setTotalBase10(totalBase10);
            encuesta.getFilaProgramaGeneralMap().get(programa).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getFilaProgramaGeneralMap().get(programa).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getFilaProgramaGeneralMap().get(programa), "Calculo realizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("calcularFrecuenciaGeneral"), e, DtoSatisfaccionServiciosEncuesta.FilaProgramaGeneral.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico> calcularSerie(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad area, SatisfaccionServiciosApartado satisfaccionServiciosApartado){
        try {
//            System.out.println("EjbSatisfaccionServiciosConsulta.calcularSerie");
//            System.out.println("encuesta = " + encuesta + ", area = " + area.getArea() + ", satisfaccionServiciosApartado = " + satisfaccionServiciosApartado);

            List<DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistorico> dtoSatisfaccionHistoricos = em.createQuery("select s from SatisfaccionHistorico s inner join s.areasUniversidad a inner join s.ciclosEscolares c where a.area=:area order by s.satisfaccionHistoricoPK.ciclo, s.satisfaccionHistoricoPK.apartado", SatisfaccionHistorico.class)
                    .setParameter("area", area.getArea())
                    .getResultStream()
                    .map(satisfaccionHistorico -> new DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistorico(satisfaccionHistorico.getCiclosEscolares(), satisfaccionHistorico.getAreasUniversidad(), satisfaccionServiciosApartado, satisfaccionHistorico))
                    .collect(Collectors.toList());
            if(dtoSatisfaccionHistoricos.isEmpty()) return ResultadoEJB.crearErroneo(2, "No se detectaron datos históricos en la base de datos.", DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico.class);

            DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico graficaSerieAreaHistorico = new DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico(new DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistoricoPK(area, satisfaccionServiciosApartado), dtoSatisfaccionHistoricos);
            return ResultadoEJB.crearCorrecto(graficaSerieAreaHistorico, "Historico calculado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Error desconocido: ".concat(DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico.class.getName().concat(".calcularSerie")), e, DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico.class);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ResultadoEJB<SatisfaccionHistorico> actualizarHistoricoInstitucional(Apartado apartado, DtoSatisfaccionServiciosEncuesta encuesta, DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado filaInstitucionalApartado, AreasUniversidad areaInstitucional){
        try{
            Double apartadoClave = (double) apartado.getId();
            encuesta.getApartadoFilaInstitucionalMap().put(apartado, filaInstitucionalApartado);
            SatisfaccionHistoricoPK pk = new SatisfaccionHistoricoPK(encuesta.getCiclosEscolares().getCiclo(), areaInstitucional.getArea(), apartadoClave);
            SatisfaccionHistorico satisfaccionHistoricoInstitucionalActual = em.find(SatisfaccionHistorico.class, pk);
            boolean nuevo = satisfaccionHistoricoInstitucionalActual == null;
            if(satisfaccionHistoricoInstitucionalActual == null) satisfaccionHistoricoInstitucionalActual = new SatisfaccionHistorico(pk);

            satisfaccionHistoricoInstitucionalActual.setSatisfaccionNivel(filaInstitucionalApartado.getTotalK().doubleValue());
            satisfaccionHistoricoInstitucionalActual.setAreasUniversidad(areaInstitucional);
            satisfaccionHistoricoInstitucionalActual.setCiclosEscolares(encuesta.getCiclosEscolares());
            satisfaccionHistoricoInstitucionalActual.setSatisfaccionPorcentaje(filaInstitucionalApartado.getPorcentajeSatisfechos().doubleValue());
//            System.out.println("nuevo = " + nuevo + ", satisfaccionHistoricoInstitucionalActual = " + satisfaccionHistoricoInstitucionalActual);
            if(nuevo) em.persist(satisfaccionHistoricoInstitucionalActual);
            else em.merge(satisfaccionHistoricoInstitucionalActual);

            return ResultadoEJB.crearCorrecto(satisfaccionHistoricoInstitucionalActual, "Resultado institucional histórico sincronizado con el contexto JPA");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, SatisfaccionHistorico.class);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public ResultadoEJB<SatisfaccionHistorico> actualizarHistoricoArea(Apartado apartado, DtoSatisfaccionServiciosEncuesta encuesta, DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado filaInstitucionalApartado, AreasUniversidad areaInstitucional){
        try{
            Double apartadoClave = (double) apartado.getId();
//            encuesta.getApartadoFilaAreaMap().put(apartado, filaInstitucionalApartado);
            SatisfaccionHistoricoPK pk = new SatisfaccionHistoricoPK(encuesta.getCiclosEscolares().getCiclo(), areaInstitucional.getArea(), apartadoClave);
            SatisfaccionHistorico satisfaccionHistoricoAreaActual = em.find(SatisfaccionHistorico.class, pk);
            boolean nuevo = satisfaccionHistoricoAreaActual == null;
            if(satisfaccionHistoricoAreaActual == null) satisfaccionHistoricoAreaActual = new SatisfaccionHistorico(pk);

            satisfaccionHistoricoAreaActual.setSatisfaccionNivel(filaInstitucionalApartado.getTotalK().doubleValue());
            satisfaccionHistoricoAreaActual.setAreasUniversidad(areaInstitucional);
            satisfaccionHistoricoAreaActual.setCiclosEscolares(encuesta.getCiclosEscolares());
            satisfaccionHistoricoAreaActual.setSatisfaccionPorcentaje(filaInstitucionalApartado.getPorcentajeSatisfechos().doubleValue());
//            System.out.println("nuevo = " + nuevo + ", satisfaccionHistoricoInstitucionalActual = " + satisfaccionHistoricoInstitucionalActual);
            if(nuevo) em.persist(satisfaccionHistoricoAreaActual);
            else em.merge(satisfaccionHistoricoAreaActual);

            return ResultadoEJB.crearCorrecto(satisfaccionHistoricoAreaActual, "Resultado institucional histórico sincronizado con el contexto JPA");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, SatisfaccionHistorico.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> fusionarApartadoPorAreaSuperior(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad area, Apartado apartado, @NonNull List<BigDecimal> respuestas){
        try {
//            System.out.println("EjbSatisfaccionServiciosConsulta.fusionarApartadoPorAreaSuperior");
//            System.out.println("encuesta.getCiclosEscolares().getFin().getYear() = " + encuesta.getCiclosEscolares().getFin().getYear());
//            System.out.println("area = " + area.getSiglas());
//            System.out.println("apartado = " + apartado.getContenido());
//            System.out.println("encuesta.getApartadoFilaProgramaMap().size() = " + encuesta.getApartadoFilaProgramaMap().size());
            DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK pk = new DtoSatisfaccionServiciosEncuesta.FilaProgramaApartadoPK(area, apartado);
            if(encuesta.getApartadoFilaAreaMap().containsKey(pk)){
                return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaAreaMap().get(pk), "Calculo ya realizado");
            }else encuesta.getApartadoFilaAreaMap().put(pk, new DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado(pk));

            List<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> filaProgramas = encuesta.getApartadoFilaProgramaMap().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().getPrograma().getAreaSuperior().shortValue() == area.getArea().shortValue())
                    .filter(entry -> entry.getKey().getApartado().getId().doubleValue() == apartado.getId().doubleValue())
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList());
//            System.out.println("filaProgramas = " + filaProgramas.size());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filaProgramas
                        .stream()
                        .map(DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado::getDatoMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteo -> conteo.getFrecuencia())
                        .sum();
                encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.ConteoApartado(new DtoSatisfaccionServiciosEncuesta.DatoApartado(area, apartado, respuesta), suma));
            });

            long totalG = encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();

            long totalH = encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();

            if(totalH == 0l) {
                encuesta.getApartadoFilaAreaMap().get(pk).setTotalG(totalG);
                encuesta.getApartadoFilaAreaMap().get(pk).setTotalH(totalH);
                encuesta.getApartadoFilaAreaMap().get(pk).setTotalJ(0l);
                encuesta.getApartadoFilaAreaMap().get(pk).setTotalK(BigDecimal.ZERO);
                encuesta.getApartadoFilaAreaMap().get(pk).setTotalBase10(BigDecimal.ZERO);
                encuesta.getApartadoFilaAreaMap().get(pk).setPorcentajeSatisfechos(BigDecimal.ZERO);
                encuesta.getApartadoFilaAreaMap().get(pk).setPorcentajeNoSatisfechos(BigDecimal.ZERO);
                return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaAreaMap().get(pk), "Calculo sin participantes");
            }

            long totalJ = encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getApartadoFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.ConteoApartado::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getApartadoFilaAreaMap().get(pk).setTotalG(totalG);
            encuesta.getApartadoFilaAreaMap().get(pk).setTotalH(totalH);
            encuesta.getApartadoFilaAreaMap().get(pk).setTotalJ(totalJ);
            encuesta.getApartadoFilaAreaMap().get(pk).setTotalK(totalK);
            encuesta.getApartadoFilaAreaMap().get(pk).setTotalBase10(totalBase10);
            encuesta.getApartadoFilaAreaMap().get(pk).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getApartadoFilaAreaMap().get(pk).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getApartadoFilaAreaMap().get(pk), "Calculo realizado");
        }catch (Exception e){
            System.out.println("area = " + area.getSiglas() + ", apartado = " + apartado.getContenido() + ", respuestas = " + respuestas);
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("fusionarApartadoPorAreaSuperior"), e, DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaPrograma> fusionarPreguntaPorAreaSuperior(DtoSatisfaccionServiciosEncuesta encuesta, AreasUniversidad area, Pregunta pregunta, @NonNull List<BigDecimal> respuestas){
        try {
//            System.out.println("EjbSatisfaccionServiciosConsulta.fusionarApartadoPorAreaSuperior");
//            System.out.println("encuesta.getCiclosEscolares().getFin().getYear() = " + encuesta.getCiclosEscolares().getFin().getYear());
//            System.out.println("area = " + area.getSiglas());
//            System.out.println("pregunta = " + pregunta.getTitulo());
//            System.out.println("encuesta.getApartadoFilaProgramaMap().size() = " + encuesta.getApartadoFilaProgramaMap().size());
            DtoSatisfaccionServiciosEncuesta.FilaProgramaPK pk = new DtoSatisfaccionServiciosEncuesta.FilaProgramaPK(area, pregunta);
            if(encuesta.getPreguntaFilaAreaMap().containsKey(pk)){
                return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaAreaMap().get(pk), "Calculo ya realizado");
            }else encuesta.getPreguntaFilaAreaMap().put(pk, new DtoSatisfaccionServiciosEncuesta.FilaPrograma(pk));

            List<DtoSatisfaccionServiciosEncuesta.FilaPrograma> filaProgramas = encuesta.getPreguntaFilaProgramaMap().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().getPrograma().getAreaSuperior().shortValue() == area.getArea().shortValue())
                    .filter(entry -> entry.getKey().getPregunta().getNumero().doubleValue() == pregunta.getNumero().doubleValue())
                    .map(entry -> entry.getValue())
                    .collect(Collectors.toList());
//            System.out.println("filaProgramas = " + filaProgramas.size());

            respuestas.parallelStream().forEach(respuesta -> {
                long suma = filaProgramas
                        .stream()
                        .map(DtoSatisfaccionServiciosEncuesta.FilaPrograma::getDatoMap)
                        .map(map -> map.get(respuesta))
                        .mapToLong(conteo -> conteo.getFrecuencia())
                        .sum();
                encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().put(respuesta, new DtoSatisfaccionServiciosEncuesta.Conteo(new DtoSatisfaccionServiciosEncuesta.Dato(area, pregunta, respuesta), suma));
            });

            long totalG = encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();

            long totalH = encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(0)) != 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();

            if(totalH == 0l) {
                encuesta.getPreguntaFilaAreaMap().get(pk).setTotalG(totalG);
                encuesta.getPreguntaFilaAreaMap().get(pk).setTotalH(totalH);
                encuesta.getPreguntaFilaAreaMap().get(pk).setTotalJ(0l);
                encuesta.getPreguntaFilaAreaMap().get(pk).setTotalK(BigDecimal.ZERO);
                encuesta.getPreguntaFilaAreaMap().get(pk).setTotalBase10(BigDecimal.ZERO);
                encuesta.getPreguntaFilaAreaMap().get(pk).setPorcentajeSatisfechos(BigDecimal.ZERO);
                encuesta.getPreguntaFilaAreaMap().get(pk).setPorcentajeNoSatisfechos(BigDecimal.ZERO);
                return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaAreaMap().get(pk), "Calculo sin participantes");
            }

            long totalJ = encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .mapToLong(value -> value.getFrecuencia() * value.getPk().getRespuesta().longValue())
                    .sum();
            BigDecimal totalK = (new BigDecimal(totalJ)).divide((new BigDecimal(totalH)), 8, RoundingMode.HALF_UP);
            BigDecimal totalBase10 = totalK.multiply(new BigDecimal(2));
            long totalSatisfechos = encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteo -> conteo.getPk().getRespuesta().compareTo(new BigDecimal(3)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            long totalInsatisfechos = encuesta.getPreguntaFilaAreaMap().get(pk).getDatoMap().values()
                    .stream()
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(4)) < 0)
                    .filter(conteoInstitucional -> conteoInstitucional.getPk().getRespuesta().compareTo(new BigDecimal(0)) > 0)
                    .mapToLong(DtoSatisfaccionServiciosEncuesta.Conteo::getFrecuencia)
                    .sum();
            BigDecimal porcentajeSatisfechos = (new BigDecimal(totalSatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            BigDecimal porcentajeInsatisfechos = (new BigDecimal(totalInsatisfechos)).divide(new BigDecimal(totalH), 8, RoundingMode.HALF_UP);
            encuesta.getPreguntaFilaAreaMap().get(pk).setTotalG(totalG);
            encuesta.getPreguntaFilaAreaMap().get(pk).setTotalH(totalH);
            encuesta.getPreguntaFilaAreaMap().get(pk).setTotalJ(totalJ);
            encuesta.getPreguntaFilaAreaMap().get(pk).setTotalK(totalK);
            encuesta.getPreguntaFilaAreaMap().get(pk).setTotalBase10(totalBase10);
            encuesta.getPreguntaFilaAreaMap().get(pk).setPorcentajeSatisfechos(porcentajeSatisfechos);
            encuesta.getPreguntaFilaAreaMap().get(pk).setPorcentajeNoSatisfechos(porcentajeInsatisfechos);
            return ResultadoEJB.crearCorrecto(encuesta.getPreguntaFilaAreaMap().get(pk), "Calculo realizado");
        }catch (Exception e){
            System.out.println("area = " + area.getSiglas() + ", pregunta = " + pregunta.getTitulo() + ", respuestas = " + respuestas);
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error desconocido".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat("fusionarPreguntaPorAreaSuperior"), e, DtoSatisfaccionServiciosEncuesta.FilaPrograma.class);
        }
    }

    public ResultadoEJB<DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico> leerValorInstitucionalHistorico(DtoSatisfaccionServiciosEncuesta encuesta){
        try{
            Map<Integer, Double> satisfaccionServiciosInstitucional = ep.leerPropiedadDecimalMapa("satisfaccionServiciosInstitucionalPorcentaje");
            DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico graficaSerieInstitucionalHistorico = new DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico();
            List<DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistoricoInstitucional> lista = new Vector<>();
            satisfaccionServiciosInstitucional.entrySet().forEach(entrada -> {
                DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistoricoInstitucional dtoSatisfaccionHistorico = new DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistoricoInstitucional(entrada.getKey(), 0d, entrada.getValue());
                lista.add(dtoSatisfaccionHistorico);
            });

            graficaSerieInstitucionalHistorico.setDtoSatisfaccionHistoricos(lista.stream().sorted(DtoSatisfaccionServiciosEncuesta.dtoSatisfaccionHistoricoInstitucionalComparator).collect(Collectors.toList()));
            encuesta.setGraficaSerieInstitucionalHistorico(graficaSerieInstitucionalHistorico);
            return ResultadoEJB.crearCorrecto(graficaSerieInstitucionalHistorico, "Datos tomados de la tabla de propiedades de configuración.");
        }catch (Exception e){
            System.out.println("EjbSatisfaccionServiciosConsulta.leerValorInstitucionalHistorico");
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error. ".concat(DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico.class.getName()).concat(". leerValorInstitucionalHistorico"), e, DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico.class);
        }
    }

    public ResultadoEJB<Boolean> calcularFrecuencias(DtoSatisfaccionServiciosEncuesta encuesta, @NonNull List<BigDecimal> respuestas, List<@NonNull DtoSatisfaccionServiciosEstudiante> dtoSatisfaccionServiciosEstudiantes){
        try{
            short areaInstitucionalClave = (short)ep.leerPropiedadEntera("areaInstitucional").orElse(72);
            AreasUniversidad areaInstitucional = em.find(AreasUniversidad.class, areaInstitucionalClave);

            List<@NonNull AreasUniversidad> programas = dtoSatisfaccionServiciosEstudiantes
                    .parallelStream()
                    .map(DtoSatisfaccionServiciosEstudiante::getDtoEstudiantePeriodo)
                    .map(DtoEstudiantePeriodo::getPrograma)
                    .distinct()
                    .sorted(DtoSatisfaccionServiciosEncuesta.areasUniversidadComparator)
                    .collect(Collectors.toList());
            encuesta.setProgramas(programas);

            ResultadoEJB<List<DtoAreaAcademica>> getProgramasEvaluacion = getProgramasEvaluacion(encuesta.getEvaluacion(), encuesta);
            List<DtoAreaAcademica> dtoAreasAcademicas = getProgramasEvaluacion.getCorrecto() ? getProgramasEvaluacion.getValor() : Collections.EMPTY_LIST;

            encuesta.getCuestionario().preguntas.forEach(pregunta -> {
                ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucional> calcularFrecuenciaPorFila = calcularFrecuenciaPorFila(encuesta, pregunta, respuestas);
                if(calcularFrecuenciaPorFila.getCorrecto()){
                    encuesta.getPreguntaFilaInstitucionalMap().put(pregunta, calcularFrecuenciaPorFila.getValor());
                }else {
                    System.out.println("pregunta sin calculo = " + pregunta);
                }
            });

            encuesta.getCuestionario().getApartados().forEach(apartado -> {
                ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalApartado> calcularFrecuenciaPorApartado = calcularFrecuenciaPorApartado(encuesta, apartado, respuestas);
                if(calcularFrecuenciaPorApartado.getCorrecto()) {
                    ResultadoEJB<SatisfaccionHistorico> actualizarHistoricoInstitucional = actualizarHistoricoInstitucional(apartado, encuesta, calcularFrecuenciaPorApartado.getValor(), areaInstitucional);
                    if(!actualizarHistoricoInstitucional.getCorrecto())
                        System.out.println("actualizarHistoricoInstitucional erroneo = " + actualizarHistoricoInstitucional);
                }
                else System.out.println("apartado sin calculo = " + apartado);
            });

            ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaInstitucionalGeneral> calcularFrecuenciaGeneral = calcularFrecuenciaGeneral(encuesta, respuestas);
            if(calcularFrecuenciaGeneral.getCorrecto()) encuesta.setFilaInstitucionalGeneral(calcularFrecuenciaGeneral.getValor());
            else System.out.println("calcularFrecuenciaGeneral sin calculo = " + calcularFrecuenciaGeneral);

//            System.out.println("Empezando recolección de datos por programa");
            programas.forEach(programa -> {
//                System.out.println("programa = " + programa);
                encuesta.getCuestionario().preguntas.parallelStream().forEach(pregunta -> {
                    DtoSatisfaccionServiciosEncuesta.FilaProgramaPK pk = new DtoSatisfaccionServiciosEncuesta.FilaProgramaPK(programa, pregunta);
                    ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaPrograma> calcularFrecuenciaPorFila = calcularFrecuenciaPorFila(encuesta, programa, pregunta, respuestas);
                    if(calcularFrecuenciaPorFila.getCorrecto()){
                        encuesta.getPreguntaFilaProgramaMap().put(pk, calcularFrecuenciaPorFila.getValor());
                    }else {
                        System.out.println("programa- pregunta sin calculo = " + pk);
                    }
                });

                encuesta.getCuestionario().getApartados().parallelStream().forEach(apartado -> {
                    ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> calcularFrecuenciaPorApartado = calcularFrecuenciaPorApartado(encuesta, programa, apartado, respuestas);
                    if(calcularFrecuenciaPorApartado.getCorrecto()) encuesta.getApartadoFilaProgramaMap().put(calcularFrecuenciaPorApartado.getValor().getPk(), calcularFrecuenciaPorApartado.getValor());
                    else {
                        System.out.println("programa - apartado sin calculo = " + apartado);
                    }
                });

                ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaGeneral> calcularFrecuenciaProgramaGeneral = calcularFrecuenciaGeneral(encuesta, programa, respuestas);
                if(calcularFrecuenciaProgramaGeneral.getCorrecto()) encuesta.getFilaProgramaGeneralMap().put(programa, calcularFrecuenciaProgramaGeneral.getValor());
                else System.out.println("calcularFrecuenciaProgramaGeneral sin calculo = " + calcularFrecuenciaProgramaGeneral);
            });

            //fusionar calculos de programas en areas
            encuesta.getCuestionario().getApartados().forEach(apartado -> {
                dtoAreasAcademicas.forEach(dtoAreaAcademica -> {
                    ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaProgramaApartado> fusionarApartadoPorAreaSuperior = fusionarApartadoPorAreaSuperior(encuesta, dtoAreaAcademica.getAreaAcademica(), apartado, respuestas);
                    if(fusionarApartadoPorAreaSuperior.getCorrecto()) {
                        ResultadoEJB<SatisfaccionHistorico> actualizarHistoricoArea = actualizarHistoricoArea(apartado, encuesta, fusionarApartadoPorAreaSuperior.getValor(), dtoAreaAcademica.getAreaAcademica());
                        if(!actualizarHistoricoArea.getCorrecto())
                            System.out.println("actualizarHistoricoArea sin calculo = " + actualizarHistoricoArea);
                    }
                    else System.out.println("fusionarApartadoPorAreaSuperior sin calculo = " + fusionarApartadoPorAreaSuperior);
                });
            });

            List<SatisfaccionHistorico> resultList = em.createQuery("select s from SatisfaccionHistorico s inner join s.areasUniversidad a inner join s.ciclosEscolares c order by s.satisfaccionHistoricoPK.ciclo, s.satisfaccionHistoricoPK.apartado", SatisfaccionHistorico.class).getResultList();
            resultList.forEach(satisfaccionHistorico -> {
                DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistoricoPK pk = new DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistoricoPK(satisfaccionHistorico.getAreasUniversidad(), SatisfaccionServiciosApartadoConverter.of(satisfaccionHistorico.getSatisfaccionHistoricoPK().getApartado()));

                if(!encuesta.getGraficaSerieAreaHistoricoMap().containsKey(pk)) encuesta.getGraficaSerieAreaHistoricoMap().put(pk, new DtoSatisfaccionServiciosEncuesta.GraficaSerieAreaHistorico(pk, new Vector<>()));
                encuesta.getGraficaSerieAreaHistoricoMap().get(pk).getDtoSatisfaccionHistoricos().add(new DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistorico(satisfaccionHistorico.getCiclosEscolares(), pk.getArea(), pk.getSatisfaccionServiciosApartado(), satisfaccionHistorico));
            });

            List<@NonNull CiclosEscolares> ciclosEscolares = encuesta.getGraficaSerieAreaHistoricoMap().values()
                    .stream()
                    .map(graficaSerieAreaHistorico -> graficaSerieAreaHistorico.getDtoSatisfaccionHistoricos())
                    .flatMap(dtoSatisfaccionHistoricos -> dtoSatisfaccionHistoricos.stream())
                    .map(DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistorico::getCiclo)
                    .distinct()
                    .sorted(Comparator.comparing(CiclosEscolares::getInicio))
                    .collect(Collectors.toList());

            List<SatisfaccionHistorico> satisfaccionHistoricosInstitucionales = em.createQuery("select s from SatisfaccionHistorico s where s.satisfaccionHistoricoPK.area = :area", SatisfaccionHistorico.class).setParameter("area", areaInstitucionalClave).getResultList();

            Arrays.stream(SatisfaccionServiciosApartado.values()).forEach(satisfaccionServiciosApartado -> {
                encuesta.getGraficaSerieInstitucionalHistoricoApartadoMap().put(satisfaccionServiciosApartado, new DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistoricoApartado(satisfaccionServiciosApartado, new Vector<>()));
                ciclosEscolares.forEach(ciclosEscolar -> {
                    SatisfaccionHistoricoPK pk = new SatisfaccionHistoricoPK(ciclosEscolar.getCiclo(), areaInstitucionalClave, satisfaccionServiciosApartado.getApartado());
                    SatisfaccionHistorico satisfaccionHistorico = new SatisfaccionHistorico(pk);
                    if(satisfaccionHistoricosInstitucionales.contains(satisfaccionHistorico)){
                        satisfaccionHistorico = satisfaccionHistoricosInstitucionales.get(satisfaccionHistoricosInstitucionales.indexOf(satisfaccionHistorico));
                    }else{
                        satisfaccionHistorico.setSatisfaccionNivel(0d);
                        satisfaccionHistorico.setAreasUniversidad(areaInstitucional);
                        satisfaccionHistorico.setCiclosEscolares(ciclosEscolar);
                        satisfaccionHistorico.setSatisfaccionPorcentaje(0d);
                        em.persist(satisfaccionHistorico);
                    }

                    DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistoricoInstitucionalApartado dtoSatisfaccionHistoricoInstitucionalApartado = new DtoSatisfaccionServiciosEncuesta.DtoSatisfaccionHistoricoInstitucionalApartado(ciclosEscolar, satisfaccionServiciosApartado, satisfaccionHistorico.getSatisfaccionNivel(), satisfaccionHistorico.getSatisfaccionPorcentaje());
                    encuesta.getGraficaSerieInstitucionalHistoricoApartadoMap().get(satisfaccionServiciosApartado).getDtoSatisfaccionHistoricos().add(dtoSatisfaccionHistoricoInstitucionalApartado);
                });
            });

            //fusionar calculos de preguntas en areas
            encuesta.getCuestionario().getPreguntas().forEach(pregunta -> {
                dtoAreasAcademicas.forEach(dtoAreaAcademica -> {
                    ResultadoEJB<DtoSatisfaccionServiciosEncuesta.FilaPrograma> fusionarPreguntaPorAreaSuperior = fusionarPreguntaPorAreaSuperior(encuesta, dtoAreaAcademica.getAreaAcademica(), pregunta, respuestas);
                    if(fusionarPreguntaPorAreaSuperior.getCorrecto()) {/*System.out.println("fusionarPreguntaPorAreaSuperior = " + fusionarPreguntaPorAreaSuperior);*/}
                    else System.out.println("fusionarPreguntaPorAreaSuperior sin calculo = " + fusionarPreguntaPorAreaSuperior);
                });
            });

            ResultadoEJB<DtoSatisfaccionServiciosEncuesta.GraficaSerieInstitucionalHistorico> leerValorInstitucionalHistorico = leerValorInstitucionalHistorico(encuesta);
            if(!leerValorInstitucionalHistorico.getCorrecto())
                System.out.println("leerValorInstitucionalHistorico sin calculo = " + leerValorInstitucionalHistorico);

            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Calculos realizados");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error. ".concat(EjbSatisfaccionServiciosConsulta.class.getName()).concat(".calcularFrecuencias"), e, Boolean.TYPE);
        }
    }
}
