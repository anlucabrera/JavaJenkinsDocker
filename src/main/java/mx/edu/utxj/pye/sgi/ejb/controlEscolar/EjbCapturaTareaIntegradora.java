package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.CalificacionesCuatrimestre;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Stateless(name = "EjbCapturaTareaIntegradoraEJB")
public class EjbCapturaTareaIntegradora {
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbConverter ejbConverter;
    @EJB EjbPacker ejbPacker;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite vertificar si hay un evento de captura de calificaciones activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_TAREA_INTEGRADORA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones por el docente (EjbCapturaTareaIntegradora.verificarEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite vertificar si hay un evento de captura de calificaciones extemporanea activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEventoExtemporaneo(PersonalActivo docente){
        try{
//            System.out.println("EjbCapturaTareaIntegradora.verificarEventoExtemporaneo");
//            System.out.println("docente = " + docente);
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_TAREA_INTEGRADORA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones extemporánea por el docente (EjbCapturaTareaIntegradora.verificarEventoExtemporaneo).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite el calcular el promedio que obtiene un estudiante en una materia
     * @param dtoUnidadesCalificacion Empaquetado de las calificaciones de una carga académica
     * @param dtoCargaAcademica Empaquetado de la carga académica de la cual se obtienen las calificaciones
     * @param dtoEstudiante Empaquetado del estudiante del que se desea obtener su promedio
     * @return Regresa el valor del promedio del estudiante o código de error en caso de no poder calcularlo
     */
    public ResultadoEJB<BigDecimal> promediarAsignatura(@NonNull DtoUnidadesCalificacion dtoUnidadesCalificacion, @NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        try{
            //
            BigDecimal suma = dtoUnidadesCalificacion.getCalificacionMap().entrySet().stream()
                    .map(entrada -> entrada.getValue())
                    .filter(dtoCapturaCalificacion -> Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica(), dtoCargaAcademica.getCargaAcademica()))
                    .filter(dtoCapturaCalificacion -> Objects.equals(dtoCapturaCalificacion.getDtoEstudiante().getPersona(), dtoEstudiante.getPersona()))
                    .map(dtoCapturaCalificacion -> {
                        BigDecimal porcentaje = new BigDecimal(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().getPorcentaje());
                        BigDecimal promedio = dtoCapturaCalificacion.getPromedio();
                        return promedio.multiply(porcentaje).divide(new BigDecimal(100));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = verificarTareaIntegradora(dtoCargaAcademica);
            if(tareaIntegradoraResultadoEJB.getCorrecto()){
                ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
                if(!dtoEstudianteToDtoInscripcionPorCargaAcademica.getCorrecto()) return ResultadoEJB.crearErroneo(2, "Se detectó configuración de tarea integradora, pero no se encontró inscripción del estudiante en el periodo de la carga. ".concat(dtoEstudianteToDtoInscripcionPorCargaAcademica.getMensaje()), BigDecimal.class);
                TareaIntegradora tareaIntegradora = tareaIntegradoraResultadoEJB.getValor();
                TareaIntegradoraPromedioPK pk = new TareaIntegradoraPromedioPK(tareaIntegradora.getIdTareaIntegradora(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
                TareaIntegradoraPromedio tareaIntegradoraPromedio = em.createQuery("select tip from TareaIntegradoraPromedio tip where tip.tareaIntegradoraPromedioPK=:pk", TareaIntegradoraPromedio.class)
                        .setParameter("pk", pk)
                        .getResultStream()
                        .findFirst().orElse(null);

                if(tareaIntegradoraPromedio != null) {
                    BigDecimal calificacion = new BigDecimal(tareaIntegradoraPromedio.getValor());
                    BigDecimal porcentaje = new BigDecimal(tareaIntegradora.getPorcentaje());
                    suma = suma.add(calificacion.multiply(porcentaje).divide(new BigDecimal(100)));
                }
            }

            if(!tareaIntegradoraResultadoEJB.getCorrecto() && tareaIntegradoraResultadoEJB.getResultado() != 2) return ResultadoEJB.crearErroneo(2, tareaIntegradoraResultadoEJB.getMensaje(), BigDecimal.class);
            ResultadoEJB<Boolean> actualizarPromedioAsignatura = actualizarPromedioAsignatura(dtoCargaAcademica, dtoEstudiante, suma);
            if(!actualizarPromedioAsignatura.getCorrecto()) System.out.println("actualizarPromedioAsignatura = " + actualizarPromedioAsignatura);
            return ResultadoEJB.crearCorrecto(suma, "Promedio por materia");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo calcular el promedio de la asignatura (EjbCapturaTareaIntegradora.promediarAsignatura).", e, BigDecimal.class);
        }
    }
    
    /**
     * Permite el calcular el promedio que obtiene un estudiante en una materia
     * @param dtoUnidadesCalificacion Empaquetado de las calificaciones de una carga académica
     * @param dtoCargaAcademica Empaquetado de la carga académica de la cual se obtienen las calificaciones
     * @param dtoEstudiante Empaquetado del estudiante del que se desea obtener su promedio
     * @return Regresa el valor del promedio del estudiante o código de error en caso de no poder calcularlo
     */
    public ResultadoEJB<BigDecimal> promediarAsignaturaAlineacion(@NonNull DtoUnidadesCalificacionAlineacion dtoUnidadesCalificacion, @NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante){
        try{
            //
            BigDecimal suma = dtoUnidadesCalificacion.getCalificacionMap().entrySet().stream()
                    .map(entrada -> entrada.getValue())
                    .filter(dtoCapturaCalificacion -> Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica(), dtoCargaAcademica.getCargaAcademica()))
                    .filter(dtoCapturaCalificacion -> Objects.equals(dtoCapturaCalificacion.getDtoEstudiante().getPersona(), dtoEstudiante.getPersona()))
                    .map(dtoCapturaCalificacion -> {
                        BigDecimal porcentaje = new BigDecimal(dtoCapturaCalificacion.getDtoUnidadConfiguracion().getUnidadMateriaConfiguracion().getPorcentaje());
                        BigDecimal promedio = dtoCapturaCalificacion.getPromedio();
                        return promedio.multiply(porcentaje).divide(new BigDecimal(100));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            ResultadoEJB<TareaIntegradora> tareaIntegradoraResultadoEJB = verificarTareaIntegradora(dtoCargaAcademica);
            if(tareaIntegradoraResultadoEJB.getCorrecto()){
                ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
                if(!dtoEstudianteToDtoInscripcionPorCargaAcademica.getCorrecto()) return ResultadoEJB.crearErroneo(2, "Se detectó configuración de tarea integradora, pero no se encontró inscripción del estudiante en el periodo de la carga. ".concat(dtoEstudianteToDtoInscripcionPorCargaAcademica.getMensaje()), BigDecimal.class);
                TareaIntegradora tareaIntegradora = tareaIntegradoraResultadoEJB.getValor();
                TareaIntegradoraPromedioPK pk = new TareaIntegradoraPromedioPK(tareaIntegradora.getIdTareaIntegradora(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
                TareaIntegradoraPromedio tareaIntegradoraPromedio = em.createQuery("select tip from TareaIntegradoraPromedio tip where tip.tareaIntegradoraPromedioPK=:pk", TareaIntegradoraPromedio.class)
                        .setParameter("pk", pk)
                        .getResultStream()
                        .findFirst().orElse(null);

                if(tareaIntegradoraPromedio != null) {
                    BigDecimal calificacion = new BigDecimal(tareaIntegradoraPromedio.getValor());
                    BigDecimal porcentaje = new BigDecimal(tareaIntegradora.getPorcentaje());
                    suma = suma.add(calificacion.multiply(porcentaje).divide(new BigDecimal(100)));
                }
            }

            if(!tareaIntegradoraResultadoEJB.getCorrecto() && tareaIntegradoraResultadoEJB.getResultado() != 2) return ResultadoEJB.crearErroneo(2, tareaIntegradoraResultadoEJB.getMensaje(), BigDecimal.class);
            ResultadoEJB<Boolean> actualizarPromedioAsignatura = actualizarPromedioAsignatura(dtoCargaAcademica, dtoEstudiante, suma);
            if(!actualizarPromedioAsignatura.getCorrecto()) System.out.println("actualizarPromedioAsignatura = " + actualizarPromedioAsignatura);
            return ResultadoEJB.crearCorrecto(suma, "Promedio por materia");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo calcular el promedio de la asignatura (EjbCapturaTareaIntegradora.promediarAsignatura).", e, BigDecimal.class);
        }
    }

    public ResultadoEJB<Boolean> actualizarPromedioAsignatura(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante, BigDecimal promedio){
        try{
//            System.out.println("EjbCapturaTareaIntegradora.actualizarPromedioAsignatura");
//            System.out.println("dtoCargaAcademica = " + dtoCargaAcademica + ", dtoEstudiante = " + dtoEstudiante + ", promedio = " + promedio);
            ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
            if(!dtoEstudianteToDtoInscripcionPorCargaAcademica.getCorrecto()) return ResultadoEJB.crearErroneo(2, dtoEstudianteToDtoInscripcionPorCargaAcademica.getMensaje(), Boolean.TYPE);
            CalificacionPromedioPK pk = new CalificacionPromedioPK(dtoCargaAcademica.getCargaAcademica().getCarga(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
//            System.out.println("pk = " + pk);
            CalificacionPromedio calificacionPromedio = em.find(CalificacionPromedio.class, pk);
//            System.out.println("calificacionPromedio = " + calificacionPromedio);
            if(calificacionPromedio == null) {
                CargaAcademica cargaAcademica = em.find(CargaAcademica.class, pk.getCarga());
//                System.out.println("cargaAcademica = " + cargaAcademica);
                Estudiante estudiante = em.find(Estudiante.class, pk.getIdEstudiante());
//                System.out.println("estudiante = " + estudiante);
                calificacionPromedio = new CalificacionPromedio(pk);
                calificacionPromedio.setCargaAcademica(cargaAcademica);
                calificacionPromedio.setEstudiante(estudiante);
                calificacionPromedio.setValor(promedio.doubleValue());
                calificacionPromedio.setFechaActualizacion(new Date());
                calificacionPromedio.setTipo("Oficial");
//                em.persist(calificacionPromedio);
                f.create(calificacionPromedio);
            }else{
                calificacionPromedio.setValor(promedio.doubleValue());
//                em.merge(calificacionPromedio);
                f.edit(calificacionPromedio);
            }

            em.flush();
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Promedio actualizado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar actualizar el promedio de una asignatura (EjbCapturaTareaIntegradora.actualizarPromedioAsignatura)", e, Boolean.class);
        }

    }

    /**
     * Permite verificar si una carga académica tiene tarea integradora
     * @param dtoCargaAcademica EMpaquetado de la carga académica de la cual se requiere verificar si tiene tarea integradora
     * @return Devueleve la tarea integradora, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<TareaIntegradora> verificarTareaIntegradora(@NonNull DtoCargaAcademica dtoCargaAcademica){
        try{
            TareaIntegradora tareaIntegradora = em.createQuery("select ti from TareaIntegradora ti where ti.carga=:carga", TareaIntegradora.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .getResultStream()
                    .findFirst().orElse(null);
            if(tareaIntegradora == null) return ResultadoEJB.crearErroneo(2, "La carga académica no tiene tarea integradora asignada.", TareaIntegradora.class);
            return ResultadoEJB.crearCorrecto(tareaIntegradora, "La carga académica tiene tarea integradora");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si la carga académica tiene tarea integradora (EjbCapturaTareaIntegradora.verificarTareaIntegradora).", e, TareaIntegradora.class);
        }
    }

    /**
     * Permite generar un mapa de estudiante y calificacion como contendor de calificaciones para la tarea integradora de la carga académica.
     * @param dtoEstudiantes
     * @param tareaIntegradora
     * @return
     */
    public ResultadoEJB<Map<DtoEstudiante, TareaIntegradoraPromedio>> generarContenedorCalificaciones(@NonNull  List<DtoEstudiante> dtoEstudiantes, @NonNull TareaIntegradora tareaIntegradora, DtoCargaAcademica dtoCargaAcademica){
        try{
            Map<DtoEstudiante, TareaIntegradoraPromedio> map = new HashMap<>();
            List<TareaIntegradoraPromedio> tareaIntegradoraPromedios = em.createQuery("select tip from TareaIntegradoraPromedio tip where tip.tareaIntegradora=:tareaIntegradora", TareaIntegradoraPromedio.class)
                    .setParameter("tareaIntegradora", tareaIntegradora)
                    .getResultList();
//            System.out.println("tareaIntegradoraPromedios = " + tareaIntegradoraPromedios);

            dtoEstudiantes.forEach(dtoEstudiante -> {
//                TareaIntegradora tareaIntegradoraBD = em.find(TareaIntegradora.class, tareaIntegradora.getIdTareaIntegradora());
//                ResultadoEJB<DtoCargaAcademica> packCargaAcademica = ejbPacker.packCargaAcademica(tareaIntegradoraBD.getCarga());
                ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
                TareaIntegradoraPromedioPK pk = new TareaIntegradoraPromedioPK(tareaIntegradora.getIdTareaIntegradora(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
//                System.out.println("pk = " + pk);
                TareaIntegradoraPromedio tareaIntegradoraPromedio = new TareaIntegradoraPromedio(pk);
//                System.out.println("tareaIntegradoraPromedio = " + tareaIntegradoraPromedio);

//                System.out.println("!tareaIntegradoraPromedios.contains(tareaIntegradoraPromedio) = " + !tareaIntegradoraPromedios.contains(tareaIntegradoraPromedio));
                if(!tareaIntegradoraPromedios.contains(tareaIntegradoraPromedio)){
                    em.persist(tareaIntegradoraPromedio);
                    em.flush();
                }else tareaIntegradoraPromedio = tareaIntegradoraPromedios.get(tareaIntegradoraPromedios.indexOf(tareaIntegradoraPromedio));

                map.put(dtoEstudiante, tareaIntegradoraPromedio);
            });

            return ResultadoEJB.crearCorrecto(map, "Contenedor de calificaciones para tarea integradora");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo construir el contenedor de calificaciones para tarea integradora (EjbCapturaTareaIntegradora.generarContenedorCalificaciones).", e, null);
        }
    }

    /**
     * Permite guardar la calificación de una tarea integradora
     * @param tareaIntegradora Tarea integradora configurada por el docente
     * @param dtoUnidadesCalificacion Empaquetado de las calificaciones por materia
     * @param dtoEstudiante Empaquetado del estudiante del cual se guarda su calificación
     * @return Devuelve la entity de la calificación o código de error en caso de no poder guardar
     */
    public ResultadoEJB<TareaIntegradoraPromedio> guardarCalificacion(@NonNull TareaIntegradora tareaIntegradora, @NonNull DtoUnidadesCalificacion dtoUnidadesCalificacion, @NonNull DtoEstudiante dtoEstudiante){
        try{
            @NonNull Map<DtoEstudiante, TareaIntegradoraPromedio> tareaIntegradoraPromedioMap = dtoUnidadesCalificacion.getTareaIntegradoraPromedioMap();
            TareaIntegradoraPromedio tareaIntegradoraPromedio = tareaIntegradoraPromedioMap.get(dtoEstudiante);

            if(tareaIntegradoraPromedio == null) return ResultadoEJB.crearErroneo(2, "El estudiante indicado no pertenece al grupo de la carga académica seleccionada", TareaIntegradoraPromedio.class);

            if(em.contains(tareaIntegradoraPromedio)) em.persist(tareaIntegradoraPromedio);
            else em.merge(tareaIntegradoraPromedio);
            em.flush();

            return ResultadoEJB.crearCorrecto(tareaIntegradoraPromedio, "Calificación guardada correctamente.");
        }catch (Exception e){
            System.err.println("EjbCapturaTareaIntegradora.guardarCalificacion");
            System.err.println("Error: tareaIntegradora = [" + tareaIntegradora + "], dtoUnidadesCalificacion = [" + dtoUnidadesCalificacion + "], dtoEstudiante = [" + dtoEstudiante + "]");
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la calificación de la tarea integradora (EjbCapturaTareaIntegradora.generarContenedorCalificaciones)", e, TareaIntegradoraPromedio.class);
        }
    }

    /**
     * Permite guardar la calificación de nivelación y recuperar el entity ya sincronizado con el contexto de persistencia
     * @param dtoUnidadesCalificacion EMpaquetado del contenedor de calidicaciones
     * @param dtoEstudiante EMpaquetado del estudiado al que se le guarda su calificación
     * @return Regresa el entity sincronizado, código de error 2 si la calificación del estudiante especificado no se encuentra en el contendor o código 1 para error desconocido
     */
    public ResultadoEJB<CalificacionNivelacion> guardarNivelacion(@NonNull DtoUnidadesCalificacion dtoUnidadesCalificacion, @NonNull DtoEstudiante dtoEstudiante){
        try{
            @NonNull DtoCargaAcademica dtoCargaAcademica = dtoUnidadesCalificacion.getDtoCargaAcademica();
            DtoUnidadesCalificacion.DtoNivelacionPK pk = new DtoUnidadesCalificacion.DtoNivelacionPK(dtoCargaAcademica, dtoEstudiante);
            if(dtoUnidadesCalificacion.getNivelacionMap().containsKey(pk)){
                @NonNull DtoCalificacionNivelacion dtoCalificacionNivelacion = dtoUnidadesCalificacion.getNivelacionMap().get(pk);
                @NonNull CalificacionNivelacion calificacionNivelacion = dtoCalificacionNivelacion.getCalificacionNivelacion();
                @NonNull Indicador indicador = dtoCalificacionNivelacion.getIndicador();
//                System.out.println("indicador = " + indicador);
                ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoUnidadesCalificacion.getDtoCargaAcademica());
                CalificacionNivelacionPK calificacionNivelacionPK = new CalificacionNivelacionPK(dtoCargaAcademica.getCargaAcademica().getCarga(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
                CalificacionNivelacion calificacionNivelacionBD = em.find(CalificacionNivelacion.class, calificacionNivelacionPK);
                if(calificacionNivelacionBD != null) {
                    Double valor = calificacionNivelacion.getValor();
                    calificacionNivelacion = calificacionNivelacionBD;
                    calificacionNivelacion.setValor(valor);
                }
                calificacionNivelacion.setIndicador(indicador);
                calificacionNivelacion.setCargaAcademica(dtoCargaAcademica.getCargaAcademica());
                calificacionNivelacion.setEstudiante(dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion());
//                System.out.println("calificacionNivelacion = " + calificacionNivelacion.getIndicador());
                if(em.contains(calificacionNivelacion)){
//                    System.out.println(1);
                    em.merge(calificacionNivelacion);
                }else {
//                    System.out.println(2);
                    em.persist(calificacionNivelacion);
                }
                em.flush();
//                System.out.println("calificacionNivelacionPK = " + calificacionNivelacion.getCalificacionNivelacionPK());
//                System.out.println("calificacionNivelacionEM = " + calificacionNivelacion.getIndicador());
                return ResultadoEJB.crearCorrecto(calificacionNivelacion, "Calificación de nivelación guardada correctamente.");
            }else return ResultadoEJB.crearErroneo(2, "No se encontró la calificación de nivelación a guardar del estudiante indicado en el contenedor.", CalificacionNivelacion.class);
        }catch (Exception e){
            System.out.println("EjbCapturaTareaIntegradora.guardarNivelacion");
            System.out.println("dtoUnidadesCalificacion = [" + dtoUnidadesCalificacion + "], dtoEstudiante = [" + dtoEstudiante + "]");
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "EjbCapturaTareaIntegradora.guardarNivelacion).", e, CalificacionNivelacion.class);
        }
    }

    /**
     * Permite obtener la lista de indicadores de evaluación p
     * @return Lista de indicadores o código de error.
     */
    public ResultadoEJB<List<Indicador>> consultarIndicadores(){
        try{
            List<Indicador> indicadores = em.createQuery("select i from Indicador i where i.estatus=true order by i.nombre", Indicador.class).getResultList();
            return ResultadoEJB.crearCorrecto(indicadores, "Lista de indicadores de evaluación.");
        }catch (Exception e){
            System.out.println("EjbCapturaTareaIntegradora.consultarIndicadores");
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores de evaluación (EjbCapturaTareaIntegradora.generarContenedorCalificaciones)", e, null);
        }
    }
    
     /**
     * Permite comprobar las fechas de apertura del apartado de resultados de tarea integradora
     * @param dtoCargaAcademica carga académica
     * @return Regresa TRUE/FALSE según la comprobación o código de error en caso de no poder realizar la comprobación
     */
    public ResultadoEJB<Boolean> fechasAperturaTI(DtoCargaAcademica dtoCargaAcademica){
        try{
            TareaIntegradora tareaIntegradora = em.createQuery("select t from TareaIntegradora t where t.carga.carga =:carga", TareaIntegradora.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            
            Calendar fechaMinima = Calendar.getInstance();
            fechaMinima.setTime(tareaIntegradora.getFechaEntrega());
            fechaMinima.add(Calendar.DAY_OF_YEAR, -8);
            Date fechaInicio=fechaMinima.getTime();
            
            Calendar fechaMaxima = Calendar.getInstance();
            fechaMaxima.setTime(tareaIntegradora.getFechaEntrega());
            fechaMaxima.add(Calendar.DAY_OF_YEAR, 8);
            Date fechaFin=fechaMaxima.getTime();
            
            Date fechaActual = new Date();
            Boolean permiso = Boolean.FALSE;
            if(fechaActual.after(fechaInicio)  && fechaActual.before(fechaFin)){
                permiso = Boolean.TRUE;
            }
            
            return ResultadoEJB.crearCorrecto(permiso, "Se comprobaron las fechas de apertura de tarea integradora");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
      /**
     * Permite comprobar si existe apertura extemporánea de nivelación final de un estudiante
     * @param dtoCargaAcademica carga académica
     * @param dtoEstudiante estudiante
     * @return Regresa TRUE/FALSE según la comprobación o código de error en caso de no poder realizar la comprobación
     */
    public ResultadoEJB<Boolean> existeAperIndNivelacion(DtoCargaAcademica dtoCargaAcademica, DtoEstudiante dtoEstudiante){
        try{
            String tipoEval ="Nivelación Final";
            PermisosCapturaExtemporaneaEstudiante permiso = em.createQuery("select p from PermisosCapturaExtemporaneaEstudiante p inner join p.idPlanMateria pm inner join p.idGrupo g inner join p.estudiante e where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and e.idEstudiante =:estudiante and p.tipoEvaluacion=:tipo and p.validada=:valor", PermisosCapturaExtemporaneaEstudiante.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("tipo", tipoEval)
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            return ResultadoEJB.crearCorrecto(permiso != null, "Se comprobó que el estudiante tiene apertura extemporánea de nivelación final");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
      /**
     * Permite comprobar si existe apertura extemporánea de tarea integradora grupal
     * @param dtoCargaAcademica carga académica
     * @return Regresa TRUE/FALSE según la comprobación o código de error en caso de no poder realizar la comprobación
     */
    public ResultadoEJB<Boolean> existeAperGrupalTI(DtoCargaAcademica dtoCargaAcademica){
        try{
            String tipoEval ="Tarea Integradora";
            PermisosCapturaExtemporaneaGrupal permiso = em.createQuery("select p from PermisosCapturaExtemporaneaGrupal p inner join p.idPlanMateria pm inner join p.idGrupo g where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.tipoEvaluacion=:tipo and p.validada=:valor", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("tipo", tipoEval)
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            return ResultadoEJB.crearCorrecto(permiso != null, "Se comprobó que el grupo tiene apertura extemporánea de tarea integradora");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
      /**
     * Permite comprobar si existe apertura extemporánea de tarea integradora de un estudiante
     * @param dtoCargaAcademica carga académica
     * @param dtoEstudiante estudiante
     * @return Regresa TRUE/FALSE según la comprobación o código de error en caso de no poder realizar la comprobación
     */
    public ResultadoEJB<Boolean> existeAperIndTI(DtoCargaAcademica dtoCargaAcademica, DtoEstudiante dtoEstudiante){
        try{
            String tipoEval ="Tarea Integradora";
            PermisosCapturaExtemporaneaEstudiante permiso = em.createQuery("select p from PermisosCapturaExtemporaneaEstudiante p inner join p.idPlanMateria pm inner join p.idGrupo g inner join p.estudiante e where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and e.idEstudiante =:estudiante and p.tipoEvaluacion=:tipo and p.validada=:valor", PermisosCapturaExtemporaneaEstudiante.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("tipo", tipoEval)
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            return ResultadoEJB.crearCorrecto(permiso != null, "Se comprobó que el estudiante tiene apertura extemporánea de tarea integradora");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
}
