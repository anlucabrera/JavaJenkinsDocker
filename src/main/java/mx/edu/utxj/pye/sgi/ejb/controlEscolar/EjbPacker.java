package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.DateUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Stateless(name = "EjbPacker")
public class EjbPacker {
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbPropiedades ep;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public PersonalActivo packPersonalActivo(Personal personal){
        if(personal == null) return null;
        if(personal.getStatus().equals('B')) return null;
        PersonalActivo activo = new PersonalActivo(personal);
        activo.setAreaOficial(em.find(AreasUniversidad.class, personal.getAreaOficial()));
        activo.setAreaOperativa(em.find(AreasUniversidad.class, personal.getAreaOperativa()));
        activo.setAreaPOA(ejbFiscalizacion.getAreaConPOA(personal.getAreaOperativa()));
        activo.setAreaSuperior(em.find(AreasUniversidad.class, personal.getAreaSuperior()));
        return activo;
    }

    public PersonalActivo packPersonalActivo(Integer id){
        if(id != null && id >0){
            Personal personal = em.find(Personal.class, id);
            return packPersonalActivo(personal);
        }

        return null;
    }

    /**
     * Empaqueta una carga académica en su DTO Wrapper
     * @param cargaAcademica Carga académica que se va a empaquetar
     * @return Carga académica empaquetada
     */
    public ResultadoEJB<DtoCargaAcademica> packCargaAcademica(CargaAcademica cargaAcademica){
//        System.out.println("cargaAcademica = " + cargaAcademica);
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una carga académica nula.", DtoCargaAcademica.class);
            if(cargaAcademica.getCarga() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una carga académica con clave nula.", DtoCargaAcademica.class);

            CargaAcademica cargaAcademicaBD = em.find(CargaAcademica.class, cargaAcademica.getCarga());
            if(cargaAcademicaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar una carga académica no registrada previamente en base de datos.", DtoCargaAcademica.class);

            Grupo grupo = em.find(Grupo.class, cargaAcademicaBD.getCveGrupo().getIdGrupo());
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, grupo.getPeriodo());
            PlanEstudioMateria planEstudioMateria = cargaAcademicaBD.getIdPlanMateria();
            PlanEstudio planEstudio = planEstudioMateria.getIdPlan();
            Materia materia = em.find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = packPersonalActivo(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudio.getIdPe());
            DtoCargaAcademica dto = new DtoCargaAcademica(cargaAcademicaBD, periodo, docente, grupo, materia, programa, planEstudio, planEstudioMateria);

            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
        }
    }

    /**
     * Permite engrapar una carga académica mediante los valores de su llave primaria.
     * @param grupo Valor de la clave del grupo.
     * @param materia Valor de la clave de la materia.
     * @param docente Valor de la clave del docente.
     * @return DTO wrapper de la carga académica o null si no existe.
     */
    public ResultadoEJB<DtoCargaAcademica> packCargaAcademica(Integer grupo, Integer materia, Integer docente){
        try{
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica  ca where ca.cveGrupo.idGrupo=:grupo and ca.idPlanMateria.idMateria.idMateria=:materia and ca.docente=:docente", CargaAcademica.class)
                    .setParameter("grupo", grupo)
                    .setParameter("materia", materia)
                    .setParameter("docente", docente)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
//            CargaAcademicaPK pk = new CargaAcademicaPK(grupo, materia, docente);
            return packCargaAcademica(cargaAcademica); //pack(em.find(CargaAcademica.class, pk));
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica con claves (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
        }
    }

    /**
     * Permite engrapar en un DTO una materia a través de la clave del grupo y la instancia de la materia, agregando la asignación de materia en el grupo si es que ya existe.
     * @param grupo Clave del grupo del cual se busca la asignación de la materia.
     * @param materia Instancia del entity de la materia.
     * @return DTO wrapper de la materia con carga académica en el grupo correspondiente si es que existe.
     */
    public  ResultadoEJB<DtoMateria> packMateria(Grupo grupo, Materia materia, AreasUniversidad programa){
//        System.out.println("grupo = [" + grupo + "], materia = [" + materia + "], programa = [" + programa + "]");
        try{
            ResultadoEJB<PlanEstudioMateria> resPlanEstudioMateria = ejbAsignacionAcademica.getPlanEstudioMateria(programa, materia, grupo.getGrado());
//            System.out.println("resPlanEstudioMateria = " + resPlanEstudioMateria);
            if(!resPlanEstudioMateria.getCorrecto()) return ResultadoEJB.crearCorrecto(new DtoMateria(materia, null, 0), resPlanEstudioMateria.getMensaje());
            CargaAcademica cargaAcademica = em.createQuery("select ca from CargaAcademica ca where ca.idPlanMateria=:planMateria and ca.cveGrupo=:grupo", CargaAcademica.class)
                    .setParameter("planMateria", resPlanEstudioMateria.getValor())
                    .setParameter("grupo", grupo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
//            System.out.println("cargaAcademica = " + cargaAcademica);
            if(cargaAcademica != null){
                ResultadoEJB<DtoCargaAcademica> resCarga = packCargaAcademica(cargaAcademica);
//                System.out.println("resCarga = " + resCarga);
                DtoMateria dto = new DtoMateria(materia, resCarga.getValor(), cargaAcademica.getHorasSemana());
//                System.out.println("dto correcto = " + dto);
//                System.out.println("dto.getDtoCargaAcademica() = " + dto.getDtoCargaAcademica());
                return ResultadoEJB.crearCorrecto(dto, "Materia empaquetada");
            }else{
                DtoMateria dto = new DtoMateria(materia, null, 0);
//                System.out.println("dto error = " + dto);
                return ResultadoEJB.crearCorrecto(dto, "Se empaquetó la materia pero no ha sido asignada.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la materia (EjbAsignacionAcademica.packMateria).", e, DtoMateria.class);
        }
    }

    /**
     * Permite empaquetar la configuración de una unidad
     * @param unidadMateriaConfiguracion Entity de la configuración de la unidad
     * @return Regresa empaquetado de la configuración de la unidad o código de error de lo contrario
     */
    public ResultadoEJB<DtoUnidadConfiguracion> packUnidadConfiguracion(UnidadMateriaConfiguracion unidadMateriaConfiguracion, DtoCargaAcademica dtoCargaAcademica){
        try{
//            System.out.println("EjbPacker.packUnidadConfiguracion");
//            System.out.println("unidadMateriaConfiguracion = " + unidadMateriaConfiguracion);
            //empaquetar configuración de unidad
            UnidadMateriaConfiguracion unidadMateriaConfiguracionBD = em.find(UnidadMateriaConfiguracion.class, unidadMateriaConfiguracion.getConfiguracion());
//            System.out.println("unidadMateriaConfiguracionBD = " + unidadMateriaConfiguracionBD);
            UnidadMateria unidadMateria = unidadMateriaConfiguracionBD.getIdUnidadMateria();
            final Map<Criterio,List<DtoUnidadConfiguracion.Detalle>> detalleListMap = new HashMap<>();
            List<DtoUnidadConfiguracion.Detalle> detalles = em.createQuery("select d from UnidadMateriaConfiguracionDetalle d where d.configuracion.configuracion=:configuracion and d.configuracion.carga.docente=:docente", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("configuracion", unidadMateriaConfiguracionBD.getConfiguracion())
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .getResultStream()
                    .map(detalle -> packDtoUnidadConfiguracionDetalle(detalle))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
//            detalles.forEach(System.out::println);
            List<Criterio> criterios = detalles.stream().map(DtoUnidadConfiguracion.Detalle::getCriterio).distinct().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
            criterios.forEach(criterio -> {
//                System.out.println("criterio = " + criterio);
                detalleListMap.put(criterio, new ArrayList<>());
            });
            detalles.forEach(detalle -> {
                if(detalleListMap.containsKey(detalle.getCriterio())) detalleListMap.get(detalle.getCriterio()).add(detalle);
            });
            Boolean activaPorFecha = DateUtils.isBetween(new Date(), unidadMateriaConfiguracionBD.getFechaInicio(), unidadMateriaConfiguracionBD.getFechaFin());

            DtoUnidadConfiguracion dto = new DtoUnidadConfiguracion(unidadMateria, unidadMateriaConfiguracionBD, detalleListMap, dtoCargaAcademica, activaPorFecha);
            return ResultadoEJB.crearCorrecto(dto, "Configuración de unidad empaquetada");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la unidad de configuración (EjbPacker.packUnidadConfiguracion).", e, DtoUnidadConfiguracion.class);
        }
    }

    /**
     * Permite empaquetar el detalle de la configuración de unidad incluyendo su criterio y su indicador
     * @param unidadMateriaConfiguracionDetalle Instancia del entity del de talle de configuración del cual se vana leer su indicador y su criterio de evaluación
     * @return Regresa el empaquetado o código de error
     */
    public ResultadoEJB<DtoUnidadConfiguracion.Detalle> packDtoUnidadConfiguracionDetalle(UnidadMateriaConfiguracionDetalle unidadMateriaConfiguracionDetalle){
        try{
            UnidadMateriaConfiguracionDetalle unidadMateriaConfiguracionDetalleBD = em.find(UnidadMateriaConfiguracionDetalle.class, unidadMateriaConfiguracionDetalle.getConfiguracionDetalle());
            DtoUnidadConfiguracion.Detalle detalle = new DtoUnidadConfiguracion.Detalle(unidadMateriaConfiguracionDetalleBD, unidadMateriaConfiguracionDetalleBD.getCriterio(), unidadMateriaConfiguracionDetalleBD.getIndicador());
//            System.out.println("detalle = " + detalle);
            return ResultadoEJB.crearCorrecto(detalle, "Detalle de la configuración de unidad");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el detalle de la configuración de unidad (EjbPacker.packDtoUnidadConfiguracionDetalle)", DtoUnidadConfiguracion.Detalle.class);
        }
    }

    /**
     * Permite empaquetar un estudiante segun su matricula
     * @param matricula Matricula del estudiante
     * @return Estudiante empaquetado o código de error de lo contrario
     */
    public ResultadoEJB<DtoEstudiante> packEstudiante(Integer matricula){
        try{
            //empaquetar estudiante
            List<DtoInscripcion> dtoInscripciones = em.createQuery("select e from Estudiante e where e.matricula=:matricula order by e.periodo", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream()
                    .map(estudiante -> packInscripcion(estudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            if(dtoInscripciones.isEmpty()) return ResultadoEJB.crearErroneo(2, "No se pudo identificar ninguna inscripción de estudiante con la matrícula proporcionada.", DtoEstudiante.class);
            Aspirante aspirante = dtoInscripciones.get(0).getInscripcion().getAspirante();
            Persona persona = aspirante.getIdPersona();
            DtoInscripcion dtoInscripcionActiva = dtoInscripciones.stream()
                    .filter(DtoInscripcion::getActivo).max(Comparator.comparingInt(value -> value.getPeriodo().getPeriodo()))
                    .orElse(null);
            DtoEstudiante dtoEstudiante = new DtoEstudiante(persona, aspirante, dtoInscripciones, dtoInscripcionActiva);
//            System.out.println("dtoEstudiante = " + dtoEstudiante);
            return ResultadoEJB.crearCorrecto(dtoEstudiante, "Estudiante empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el estudiante a partir de su mattricula (EjbPacker.packEstudiante).", e, DtoEstudiante.class);
        }
    }

    /**
     * Permite empaquetar un estudiante segun su matricula
     * @param estudiante Entity de la inscripción del estudiante
     * @return Estudiante empaquetado o código de error de lo contrario
     */
    public ResultadoEJB<DtoEstudiante> packEstudiante(Estudiante estudiante){
        if(estudiante == null) return ResultadoEJB.crearErroneo(1, "El estudiante es nulo y no se puede empaquetar", DtoEstudiante.class);
        return  packEstudiante(estudiante.getMatricula());
    }

    /**
     * Permite empaquetar la inscripión a un grupo y periodo de un estudiante
     * @param estudiante Instancia de la inscipción a empaquetar
     * @return Regresa el empaquetado de la inscripción o código de error
     */
    public ResultadoEJB<DtoInscripcion> packInscripcion(Estudiante estudiante){
        try{
            //empaquetar inscripción del estudiante
            if(estudiante == null) return  ResultadoEJB.crearErroneo(2, "No se pudo identificar al estudiante con la matricula y grupo proporcionados.", DtoInscripcion.class);

            Grupo grupo = estudiante.getGrupo();
            ResultadoEJB<PeriodosEscolares> resPeriodoActivo = getPeriodoActivo();
            if(!resPeriodoActivo.getCorrecto()) return ResultadoEJB.crearErroneo(3, resPeriodoActivo.getMensaje(), DtoInscripcion.class);

            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            Generaciones generacion = em.find(Generaciones.class, grupo.getGeneracion());
            DtoInscripcion dto = new DtoInscripcion(estudiante, grupo, periodo, generacion, periodo.equals(resPeriodoActivo.getValor()));
            return ResultadoEJB.crearCorrecto(dto, "Estudiante empaquetado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la inscripción del estudiante a partir de su mattricula (EjbPacker.packInscripcion).", e, DtoInscripcion.class);
        }
    }

    /**
     * Permite empaquetar un grupo de estudiantes a partir del empaquetado de unca carga academica y los ordena por apellido paterno, materno y nombre
     * @param dtoCargaAcademica Carga académica de la cual se quiere empaquetar el grupo de estudiantes
     * @return Grupo de estudiantes o código de error de lo contrario
     */
    public ResultadoEJB<DtoGrupoEstudiante> packGrupoEstudiante(DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
            //empaquetar grupo estudiante
//            System.out.println("EjbPacker.packGrupoEstudiante");
            Grupo grupo = em.find(Grupo.class, dtoCargaAcademica.getGrupo().getIdGrupo());
//            System.out.println("grupo = " + grupo);
//            System.out.println("grupo.getInscripcionList() = " + grupo.getInscripcionList());
            /*List<Estudiante> estudiantes = em.createQuery("select e from Estudiante e where e.grupo=:grupo order by e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
                    .setParameter("grupo", grupo)
                    .getResultList();*/
//            System.out.println("estudiantes = " + estudiantes);
            List<DtoCapturaCalificacion> dtoCapturaCalificaciones = em.createQuery("select e from Estudiante e where e.grupo=:grupo order by e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
                    .setParameter("grupo", grupo)
                    .getResultStream()
                    .distinct()
                    .map(estudiante -> packEstudiante(estudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .distinct()
                    .sorted(Comparator.comparing(dtoEstudiante -> {
                        String res = "";
                        String paterno = dtoEstudiante.getAspirante().getIdPersona().getApellidoPaterno();
                        String materno = dtoEstudiante.getAspirante().getIdPersona().getApellidoMaterno();
                        String nombre = dtoEstudiante.getAspirante().getIdPersona().getNombre();
                        if(paterno!=null) res = res.concat(paterno);
                        if(materno!=null) res = res.concat(materno);
                        if(nombre!=null) res = res.concat(nombre);
                        return res;
                    }))
                    .map(dtoEstudiante -> packCapturaCalificacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            DtoGrupoEstudiante dtoGrupoEstudiante = new DtoGrupoEstudiante(dtoCargaAcademica, dtoCapturaCalificaciones);
            return ResultadoEJB.crearCorrecto(dtoGrupoEstudiante, "Grupo empaquetado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el grupo con estudiantes a partir de una carga academica(EjbPacker.packEstudiante).", e, DtoGrupoEstudiante.class);
        }
    }

    /**
     * Permite empaquetar una captura de calificacion a partir de un empaquetado de estudiante y carga académica para preparar herramientas de captura de calificacion por un estudiante de un grupo en una materia
     * @param dtoEstudiante Empaquetado del estudiante a quien se le va a capturar calificaciones
     * @param dtoCargaAcademica Empaquetado de la carga académica a la que se le va a capturar la calificación.
     * @return Regresa el empaquetada para la captura de calificaciones  o código de error de lo contrario.
     */
    public ResultadoEJB<DtoCapturaCalificacion> packCapturaCalificacion(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
            //empaquetar captura de calificacion
            List<DtoCapturaCalificacion.Captura> capturas = dtoUnidadConfiguracion.getUnidadMateriaConfiguracionDetalles()
                    .values()
                    .stream()
                    .flatMap(detalles -> detalles.stream())
                    .map(detalle -> packDtoCapturaCalificacionCaptura(detalle, dtoEstudiante))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            DtoCapturaCalificacion dtoCapturaCalificacion = new DtoCapturaCalificacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion, capturas);
            ResultadoEJB<BigDecimal> resPromedio = ejbCapturaCalificaciones.promediarUnidad(dtoCapturaCalificacion);
            if(resPromedio.getCorrecto()) dtoCapturaCalificacion.setPromedio(resPromedio.getValor());

            return ResultadoEJB.crearCorrecto(dtoCapturaCalificacion, "Captura de calificación empaquetada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la captura de calificaciones a partir del empaquetado de estudiante y carga academica(EjbPacker.packCapturaCalificacion).", e, DtoCapturaCalificacion.class);
        }
    }

    /**
     * Permite empaquetar una linea de captura de calificación que incluye el detalle de configuración de unidad y la referencia de la inscripción activa del estudiante
     * @param detalle Detalle de la configuración de unidad
     * @param dtoEstudiante Empaqueta del estudiante que contiene la lista de inscripciones de la cual se obtendría la inscripcón activa
     * @return
     */
    public ResultadoEJB<DtoCapturaCalificacion.Captura> packDtoCapturaCalificacionCaptura(DtoUnidadConfiguracion.Detalle detalle, DtoEstudiante dtoEstudiante){
        try{
//            System.out.println("detalle = [" + detalle + "], dtoEstudiante = [" + dtoEstudiante + "]");
            Estudiante inscripcionActiva = dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(DtoInscripcion::getActivo)
                    .map(DtoInscripcion::getInscripcion)
                    .max(Comparator.comparing(Estudiante::getPeriodo))
                    .orElse(null);
//            System.out.println("inscripcionActiva = " + inscripcionActiva);
            if(inscripcionActiva == null) return ResultadoEJB.crearErroneo(2, "No se tiene una inscripción activa del estudiante a un grupo", DtoCapturaCalificacion.Captura.class);
            Calificacion calificacion = em.createQuery("select c from Calificacion  c where c.idEstudiante=:estudiante and c.configuracionDetalle=:detalle", Calificacion.class)
                    .setParameter("estudiante", inscripcionActiva)
                    .setParameter("detalle", detalle.getDetalle())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if(calificacion == null){//si no existe calificación se crea con valor nulo
                calificacion = new Calificacion();
                calificacion.setConfiguracionDetalle(detalle.getDetalle());
                calificacion.setIdEstudiante(inscripcionActiva);
                calificacion.setValor(null);
                em.persist(calificacion);
            }

            DtoCapturaCalificacion.Captura captura = new DtoCapturaCalificacion.Captura(detalle, calificacion);
//            System.out.println("captura = " + captura);
            return ResultadoEJB.crearCorrecto(captura, "Captura de calificación");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la captura de calificación (EjbPacker.packDtoCapturaCalificacionCaptura).", DtoCapturaCalificacion.Captura.class);
        }
    }

    /**
     * Permite obtener el periodo activo intentando leer la propiedad de configuración periodoActivo, en caso de no encontrarla se intenta obtener el periodo activo por sus fechas de inicio y fin declaradas en la tabla periodo_escolar_fechas
     * @return Regresa valor del periodo o código de error de lo contrario
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoActivo(){
        try{
            Integer periodoActivo = ep.leerPropiedadEntera("periodoActivo").orElse(-1);
            if(periodoActivo == -1 ) {
                PeriodoEscolarFechas periodoEscolarFechas = em.createQuery("select f from PeriodoEscolarFechas f where current_date between f.inicio and f.fin", PeriodoEscolarFechas.class)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if(periodoEscolarFechas ==null) return ResultadoEJB.crearErroneo(2, "No se pudo leer la propiedad periodoActivo ni se pudo detectar el periodo activo por fechas en la tabla periodo_escolar_fechas.", PeriodosEscolares.class);
                else return ResultadoEJB.crearCorrecto(periodoEscolarFechas.getPeriodosEscolares(), "Periodo escolar calculado por fechas");
            }else {
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, periodoActivo);
                if(periodo == null) return ResultadoEJB.crearErroneo(3, "El valor almacenado en la propiedad de configuración periodoActivo no corresponde a un periodo escolar existente.", PeriodosEscolares.class);
                else return ResultadoEJB.crearCorrecto(periodo, "Periodo escolar calculado a partir de la propiedad de configuración periodoActivo.");
            }
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo activo (EjbPacker.getPeriodoActivo).", e, PeriodosEscolares.class);
        }
    }
}
