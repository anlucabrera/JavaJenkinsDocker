package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCapturaCalificacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.enums.converter.CasoCriticoEstadoConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateless(name = "EjbCapturaCalificaciones")
public class EjbCapturaCalificaciones {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbPacker ejbPacker;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }

    public Long leerDiasRangoParaCapturarUnidad(){
        return (long)ep.leerPropiedadEntera("controlEscolarDiasRangoParaCapturarUnidad").orElse(7);
    }

    public BigDecimal leerCalificacionMínimaAprobatoria(){
        return new BigDecimal(ep.leerPropiedadDecimal("controlEscolarCalificacionMinimaAprobatoria").orElse(8));
    }

    /**
     * Permite validar si el usuario logueado es un docente
     * @param clave Número de nómina del usuario logueado
     * @return Regresa la instancia del personal si es que cumple con ser docente o codigo de error de lo contrario
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como un docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbCapturaCalificaciones.validarDocente)", e, null);
        }
    }

    /**
     * Permite comprobar si el docente tiene el menos una carga académica para poder determinar si puede consultar o registrar calificaciones
     * @param docente DTO del docente logueado
     * @return Regresa TRUE/FALSE según la comprobación o código de error en caso de no poder realizar la comprobación
     */
    public ResultadoEJB<Boolean> verificarCargasExistentes(PersonalActivo docente){
        try{
            //
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica ca where ca.docente=:docente", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setMaxResults(1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            return ResultadoEJB.crearCorrecto(cargaAcademica != null, "Se comprobó si el docente tiene al menos una carga académica");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }

    /**
     * Permite vertificar si hay un evento de captura de calificaciones activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_CALIFICACIONES, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones por el docente (EjbCapturaCalificaciones.verificarEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite vertificar si hay un evento de captura de calificaciones extemporanea activo para el docente
     * @param docente Instacia del docente validado
     * @return Regresa el evento escolar detectado para el docente o código de error de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEventoExtemporaneo(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CAPTURA_CALIFICACIONES_EXTEMPORANEA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para captura de calificaciones extemporánea por el docente (EjbCapturaCalificaciones.verificarEvento).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite identificar si existen configuraciones registradas por el docente con fechas que corresponden a la fecha actual
     * @param docente Docente logueado
     * @return Lista de configuraciones o código de error.
     */
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getUnidadesEnEvaluacion(PersonalActivo docente){
        try{
//            System.out.println("EjbCapturaCalificaciones.getUnidadesEnEvaluacion");
            /*em.createQuery("select c from UnidadMateriaConfiguracion  c where current_date between c.fechaInicio and  c.fechaFin and c.carga.docente=:docente", UnidadMateriaConfiguracion.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .distinct()
                    .map(unidadMateriaConfiguracion -> {
                        CargaAcademica carga = unidadMateriaConfiguracion.getCarga();
                        ResultadoEJB<DtoCargaAcademica> resCarga = ejbPacker.packCargaAcademica(carga);
                        if (!resCarga.getCorrecto()) return null;
                        return ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, resCarga.getValor());
                    })
                    .forEach(System.out::println);*/
            List<DtoUnidadConfiguracion> configuraciones = em.createQuery("select c from UnidadMateriaConfiguracion  c where current_date between c.fechaInicio and  c.fechaFin and c.carga.docente=:docente", UnidadMateriaConfiguracion.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .distinct()
                    .map(unidadMateriaConfiguracion -> {
                        CargaAcademica carga = unidadMateriaConfiguracion.getCarga();
                        ResultadoEJB<DtoCargaAcademica> resCarga = ejbPacker.packCargaAcademica(carga);
                        if (!resCarga.getCorrecto()) return null;
                        return ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, resCarga.getValor());
                    })
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
//            List<@NonNull DtoUnidadConfiguracion> configuraciones = Collections.EMPTY_LIST;
//            System.out.println("EjbCapturaCalificaciones.getUnidadesEnEvaluacion4");
            if(configuraciones.isEmpty()) return ResultadoEJB.crearErroneo(2, null,"No se encontraron configuraciones de unidades abiertas para captura.");
//            System.out.println("EjbCapturaCalificaciones.getUnidadesEnEvaluacion2");
            return ResultadoEJB.crearCorrecto(configuraciones, "Configuraciones abiertas para captura encontradas.");
        }catch (Exception e){
//            System.out.println("EjbCapturaCalificaciones.getUnidadesEnEvaluacion3");
            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener las unidades con evaluación activa por docente según la fecha actual (EjbCapturaCalificaciones.getUnidadesEnEvaluacion).", e, null);
        }
    }

    /**
     * Permite obtener una lista de periodos escolares ordenados en forma descendente por fecha en los que el docente ha tenido asignaciones académicas
     * @param docente Docente del cual se van a obtener sus periodos con asignaciones
     * @return Lista de periodos o código de error de lo contrario
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCaptura(PersonalActivo docente){
        try{
            //consultar los periodos en los que el docente tenga aisgnaciones registradas y ordenarlo en forma descentente
            List<PeriodosEscolares> periodos = em.createQuery("select ca from CargaAcademica  ca where ca.docente=:docente", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .map(cargaAcademica -> cargaAcademica.getCveGrupo())
                    .map(grupo -> grupo.getPeriodo())
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            if(periodos.isEmpty()){
                //en caso de no tener ningun periodo arrojar  un código de error
                return  ResultadoEJB.crearErroneo(2, periodos,"No se identificaron periodos escolares con carga académica para el docente.");
            }else{
//                System.out.println("periodos = " + periodos);
                return ResultadoEJB.crearCorrecto(periodos, "Periodos descendentes con asignación académica del docente");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que el docente ha tenido captura de calificaciones (EjbCapturaCalificaciones.getPeriodosConCaptura).", e, null);
        }
    }

    /**
     * Permite obtener la lista de cargas académicas que un docente tiene en el periodo escolar seleccionado por el usuario
     * @param docente Docente logueado en sistema
     * @param periodo Periodo seleccionado en pantalla
     * @return Regresa lista de cargas cadémicas o código de error de lo contrario
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcadémicasPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
        try {
            //obtener la lista de cargas académicas del docente
            List<DtoCargaAcademica> cargas = em.createQuery("select ca from CargaAcademica  ca where ca.docente=:docente", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> ejbAsignacionAcademica.pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            if(cargas.isEmpty()) return  ResultadoEJB.crearErroneo(2, cargas, "Usted no tiene carga académica en el periodo seleccionado");
            else return ResultadoEJB.crearCorrecto(cargas, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista cargas cadémicas por docente y periodo (EjbCapturaCalificaciones.getCargasAcadémicasPorPeriodo).", e, null);
        }
    }

    /**
     * Permite obtener un mapa de unidad y sus configuraciones correspondientes a la materia, grupo, periodo y docente de la carga académica especificada
     * @param dtoCargaAcademica  Carga de la que se desea conocer sus configuraciones
     * @return
     */
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguraciones(DtoCargaAcademica dtoCargaAcademica){
        try {
            List<DtoUnidadConfiguracion> configuraciones = em.createQuery("select umc from UnidadMateriaConfiguracion umc where umc.carga=:carga", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica())
                    .getResultStream()
                    .map(unidadMateriaConfiguracion -> ejbPacker.packUnidadConfiguracion(unidadMateriaConfiguracion, dtoCargaAcademica))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(configuraciones.isEmpty()) return  ResultadoEJB.crearErroneo(2, configuraciones, "No se encontraron configuraciones en la carga académica seleccionada.");
            else return ResultadoEJB.crearCorrecto(configuraciones, "Cargas académicas por docente y periodo");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbCapturaCalificaciones.getConfiguraciones).", e, null);
        }
    }

    /**
     * Permite guardar la calificación capturada de un estudiante
     * @param captura Empaquetado con la calificacion capturada
     * @return Regresa la instancia de entity de calificacion guardada si la calificación fuer guardada correctamente FALSE de lo contrario
     */
    public  ResultadoEJB<Calificacion> guardarCapturaCalificacion(DtoCapturaCalificacion.Captura captura){
        try{
            //guardar calificacion capturada
            Calificacion calificacionBD = em.find(Calificacion.class, captura.getCalificacion().getCalificacion());
            if(calificacionBD == null) return ResultadoEJB.crearErroneo(2, "La clave de la calificación no se encuentra en la base de datos, para guardar una calificación ya debe estar persistida.", Calificacion.class);
            calificacionBD.setValor(captura.getCalificacion().getValor());
            em.merge(calificacionBD);
            return ResultadoEJB.crearCorrecto(calificacionBD, "Captura de calificación guardada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la captura de calificación(EjbCapturaCalificaciones.packCapturaCalificacion).", e, Calificacion.class);
        }
    }

    /**
     * Permite guardar varias calificaciones capturadas e informar la catidad de calificaciones guardadas correctamente
     * @param calificaciones Lista de empaquetados de calificaciones capturadas
     * @return Regresa el conteo de calificaciones guardadas correctamente
     */
    public  ResultadoEJB<Integer> guardarCapturaCalificaciones(List<DtoCapturaCalificacion> calificaciones){
        try{
            //TODO:guardar calificaciones capturadas
            return ResultadoEJB.crearCorrecto(null, "N calificaciones guardadas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la captura de calificación(EjbCapturaCalificaciones.packCapturaCalificacion).", e, Integer.class);
        }
    }

    /**
     * Permite calcular el promedio exacto que obtuvo un estudiante inscrito con respecto a la relación grupo-asignatura-unidad
     * @param dtoCapturaCalificacion DTO que contiene los valores de calificaciones del estudiante en una unidad de materia
     * @return Valor del promedio por unidad del estudianto o código de error en caso no poder calcularlo
     */
    public ResultadoEJB<BigDecimal> promediarUnidad(DtoCapturaCalificacion dtoCapturaCalificacion){
        try{
//            System.out.println("EjbCapturaCalificaciones.promediarUnidad");
            if(dtoCapturaCalificacion == null) return ResultadoEJB.crearErroneo(2, "El DTO de captura de calificación es nulo", BigDecimal.class);
            if(dtoCapturaCalificacion.getCapturas() == null) return  ResultadoEJB.crearErroneo(3, "La lista de capturas de calificaciones es nula.", BigDecimal.class);

            List<Criterio> criterios = dtoCapturaCalificacion.getCapturas()
                    .stream()
                    .map(captura -> captura.getDetalle().getCriterio())
                    .distinct()
                    .sorted(Comparator.comparingInt(Criterio::getCriterio))
                    .collect(Collectors.toList());

            BigDecimal suma = criterios
                    .stream()
                    .map(criterio -> promediarCriterio(dtoCapturaCalificacion.getCapturas(), criterio))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            System.out.println("promedioUnidad = " + suma);
            return ResultadoEJB.crearCorrecto(suma, "Promedio calculado");
        }catch (Exception e){
//            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo promediar la unidad (EjbCapturaCalificaciones.promediarUnidad).", e, BigDecimal.class);
        }
    }

    /**
     * Permite calcular el promedio exacto que obtuvo un estudiante  inscrito con respecto a la relación grupo-asignatura-unidad-criterio, sumando los valores de los indicadores configurados por el docente en el criterio especificado
     * @param capturas Lista de capturas de calificaciones de todos los criterios/indicadores correspondientes a la unidad de materia
     * @param criterio Criterio del cual se requiere el valor del promedio
     * @return Valor del promedio por criterio del estudiante o código de error en caso de no poder calcularlo
     */
    public ResultadoEJB<BigDecimal> promediarCriterio(List<DtoCapturaCalificacion.Captura> capturas, Criterio criterio){
        try{
//            System.out.println("EjbCapturaCalificaciones.promediarCriterio");
//            System.out.println("criterio.getTipo() = " + criterio.getTipo());
            BigDecimal suma = capturas
                    .stream()
                    .filter(captura -> Objects.equals(captura.getDetalle().getCriterio(), criterio))
                    .map(captura -> calificarCaptura(captura))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//            System.out.println("suma = " + suma);
            BigDecimal porcentajeRecomendado = new BigDecimal(criterio.getPorcentajeRecomendado());
//            System.out.println("porcentajeRecomendado = " + porcentajeRecomendado);
            BigDecimal valor = porcentajeRecomendado.divide(new BigDecimal(100)).multiply(suma);
//            System.out.println("calificacionCriterio = " + valor);
            return ResultadoEJB.crearCorrecto(valor, "Promedio por criterio calculado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, BigDecimal.class);
        }
    }

    /**
     * Calcula el valor de una calificación ingresada en vista por el docente en escala del 1 al 10 para obtener el valor correspondiente según el porcentaje del indicador/critero configurado
     * @param captura DTO de la captura de calificación
     * @return Regresa el valor de la calificación o error de lo contrario
     */
    public ResultadoEJB<BigDecimal> calificarCaptura(DtoCapturaCalificacion.Captura captura){
        try{
//            System.out.println("EjbCapturaCalificaciones.calificarCaptura");
            BigDecimal porcentajeDetalle = new BigDecimal(captura.getDetalle().getDetalle().getPorcentaje());
//            System.out.println("porcentajeDetalle = " + porcentajeDetalle);
            BigDecimal valorCalificacion = new BigDecimal(captura.getCalificacion().getValor());
//            System.out.println("valorCalificacion = " + valorCalificacion);
            BigDecimal valor = porcentajeDetalle.divide(new BigDecimal(100)).multiply(valorCalificacion); //porcentajeDetalle / 100d * valorCalificacion;
//            System.out.println("valor = " + valor);
            return ResultadoEJB.crearCorrecto(valor, "Valor de captura de calificación por detalle, criterio y estudiante, calculado.");
        }catch (Exception e){
//            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo calificar la captura (EjbCapturaCalificaciones.calificarCaptura).", BigDecimal.class);
        }
    }

    /**
     * Obtiene la lista de casos críticos sin cerrar de un estudiante con respecto a la relación periodo-carga
     * @param dtoInscripcion DTO que contiene la referencia del periodo
     * @param dtoCargaAcademica  DTO que contiene la referencia de la carga
     * @return Regresa la lista de casos críticos con al menos un elemento o código de error en caso de error o no encontrar casos
     */
    public ResultadoEJB<List<CasoCritico>> identificarCasoCritico(DtoInscripcion dtoInscripcion, DtoCargaAcademica dtoCargaAcademica){
        try{
            List<CasoCritico> casoCriticos = em.createQuery("select cc from CasoCritico cc where cc.idEstudiante=:estudiante and cc.carga=:carga and cc.estado<>'Cerrado'", CasoCritico.class)
                    .getResultStream()
                    .filter(casoCritico -> CasoCriticoEstadoConverter.of(casoCritico.getEstado()) != null)
                    .filter(casoCritico -> CasoCriticoEstadoConverter.of(casoCritico.getEstado()).getNivel() > 0d)
                    .collect(Collectors.toList());
            if(casoCriticos.isEmpty()) return ResultadoEJB.crearErroneo(2, null,"No se encontraron casos críticos para el estudiante en la asignatura correspondiente.");
            return ResultadoEJB.crearCorrecto(casoCriticos, "Casos criticos");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, null);
        }
    }

    /**
     * Permite verificar si la captura de calificaciones de un unidad ya tiene todos los valores por cada criterio-indicador
     * @param dtoCapturaCalificacion Empaquetado de la captura de calificaciones
     * @return Regresa TRUE si la captura está completa. <br/>
     * Código 1: Error imprevisto.
     * Código 2: Valores de calificaciones nulos, es decir, sin captura.
     */
    public ResultadoEJB<Boolean> verificarCapturaCompleta(DtoCapturaCalificacion dtoCapturaCalificacion){
        try{
            Long nulos = dtoCapturaCalificacion.getCapturas()
                    .stream()
                    .map(DtoCapturaCalificacion.Captura::getCalificacion)
                    .map(Calificacion::getValor)
                    .filter(Objects::isNull)
                    .count();
            if(nulos == 0l) return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La capura de calificaciones de la unidad está completa.");
            else return  ResultadoEJB.crearErroneo(2, "La captura de calificaciones de la unidad aún está incompleta.", Boolean.TYPE);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si la captura de calificaciones por unidad está completa (EjbCapturaCalificaciones.verificarCapturaCompleta).", e, Boolean.TYPE);
        }
    }
}
