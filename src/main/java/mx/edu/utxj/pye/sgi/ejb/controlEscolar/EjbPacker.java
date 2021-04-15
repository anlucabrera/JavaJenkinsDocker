package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import edu.mx.utxj.pye.seut.util.util.Cuestionario;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoAreaUniversidadCategoria;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoEstudiantePeriodo;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosCuestionario;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.*;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.*;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.converter.CasoCriticoEstadoConverter;
import mx.edu.utxj.pye.sgi.enums.converter.CasoCriticoTipoConverter;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.ejb.EjbLogin;
import mx.edu.utxj.pye.sgi.util.DateUtils;
import mx.edu.utxj.pye.sgi.util.NumberUtils;
import mx.edu.utxj.pye.sgi.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.awt.dnd.DragGestureEvent;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Stateless(name = "EjbPacker")
public class EjbPacker {
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbCapturaCalificaciones ejbCapturaCalificaciones;
    @EJB EjbCasoCritico ejbCasoCritico;
    @EJB EjbValidacionComentarios ejbValidacionComentarios;
    @EJB EjbConverter ejbConverter;
    @EJB EjbPropiedades ep;
    @EJB EjbLogin ejbLogin;
    private EntityManager em;

     @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    public PersonalActivo packPersonalActivo(Personal personal){
        if(personal == null) return null;
//        if(personal.getStatus().equals('B')) return null;
        PersonalActivo activo = new PersonalActivo(personal);
        activo.setAreaOficial(em.find(AreasUniversidad.class, personal.getAreaOficial()));
        activo.setAreaOperativa(em.find(AreasUniversidad.class, personal.getAreaOperativa()));
        activo.setAreaPOA(ejbFiscalizacion.getAreaConPOA(personal.getAreaOperativa()));
        activo.setAreaSuperior(em.find(AreasUniversidad.class, personal.getAreaSuperior()));
        List<Grupo> grupos = em.createQuery("select g from Grupo g  where g.tutor=:tutor order by g.periodo desc", Grupo.class)
                .setParameter("tutor", personal.getClave())
                .getResultStream()
//                .map(cargaAcademica -> packCargaAcademica(cargaAcademica))
//                .filter(ResultadoEJB::getCorrecto)
//                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        activo.setGruposTutorados(grupos);
        List<CargaAcademica> cargas = em.createQuery("select c from CargaAcademica c where c.docente=:docente order by c.evento.periodo desc", CargaAcademica.class)
                .setParameter("docente", personal.getClave())
                .getResultStream()
//                .map(cargaAcademica -> packCargaAcademica(cargaAcademica))
//                .filter(ResultadoEJB::getCorrecto)
//                .map(ResultadoEJB::getValor)
                .collect(Collectors.toList());
        activo.setCargaAcademicas(cargas);
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
            e.printStackTrace();
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
            UnidadMateriaConfiguracion unidadMateriaConfiguracionBD = em.find(UnidadMateriaConfiguracion.class, unidadMateriaConfiguracion.getConfiguracion());
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
            List<Criterio> criterios = detalles.stream().map(DtoUnidadConfiguracion.Detalle::getCriterio).distinct().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
            criterios.forEach(criterio -> {
                detalleListMap.put(criterio, new ArrayList<>());
            });
            detalles.forEach(detalle -> {
                if(detalleListMap.containsKey(detalle.getCriterio())) detalleListMap.get(detalle.getCriterio()).add(detalle);
            });
            Boolean activaPorFecha = DateUtils.isBetweenWithRange(new Date(), unidadMateriaConfiguracionBD.getFechaInicio(), unidadMateriaConfiguracionBD.getFechaFin(), ejbCapturaCalificaciones.leerDiasRangoParaCapturarUnidad());
            PermisosCapturaExtemporaneaGrupal permiso = em.createQuery("select p from PermisosCapturaExtemporaneaGrupal p inner join p.idPlanMateria pm inner join p.idGrupo g where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.idUnidadMateria=:unidad and p.validada=:valor", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("unidad", unidadMateria)
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
//            System.out.println("EjbPacker.packUnidadConfiguracion");
//            System.out.println("unidadMateria = " + unidadMateria);
//            System.out.println("permiso = " + permiso);
            Boolean activaPorPermiso = permiso != null;

            DtoUnidadConfiguracion dto = new DtoUnidadConfiguracion(unidadMateria, unidadMateriaConfiguracionBD, detalleListMap, dtoCargaAcademica, activaPorFecha, activaPorPermiso);
            return ResultadoEJB.crearCorrecto(dto, "Configuración de unidad empaquetada");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la unidad de configuración (EjbPacker.packUnidadConfiguracion).", e, DtoUnidadConfiguracion.class);
        }
    }
    
    /**
     * Permite empaquetar la configuración de una unidad
     * @param unidadMateriaConfiguracion Entity de la configuración de la unidad
     * @return Regresa empaquetado de la configuración de la unidad o código de error de lo contrario
     */
    public ResultadoEJB<DtoUnidadConfiguracionAlineacion> packUnidadConfiguracionAlineacion(UnidadMateriaConfiguracion unidadMateriaConfiguracion, DtoCargaAcademica dtoCargaAcademica){
        try{
            UnidadMateriaConfiguracion unidadMateriaConfiguracionBD = em.find(UnidadMateriaConfiguracion.class, unidadMateriaConfiguracion.getConfiguracion());
            UnidadMateria unidadMateria = unidadMateriaConfiguracionBD.getIdUnidadMateria();
            final Map<Criterio,List<DtoUnidadConfiguracionAlineacion.Detalle>> detalleListMap = new HashMap<>();
            List<DtoUnidadConfiguracionAlineacion.Detalle> detalles = em.createQuery("select d from UnidadMateriaConfiguracionEvidenciaInstrumento d where d.configuracion.configuracion=:configuracion and d.configuracion.carga.docente=:docente", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                    .setParameter("configuracion", unidadMateriaConfiguracionBD.getConfiguracion())
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .getResultStream()
                    .map(detalle -> packDtoUnidadConfiguracionDetalleAlineacion(detalle))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            List<Criterio> criterios = detalles.stream().map(DtoUnidadConfiguracionAlineacion.Detalle::getCriterio).distinct().sorted(Comparator.comparingInt(Criterio::getCriterio)).collect(Collectors.toList());
            criterios.forEach(criterio -> {
                detalleListMap.put(criterio, new ArrayList<>());
            });
            detalles.forEach(detalle -> {
                if(detalleListMap.containsKey(detalle.getCriterio())) detalleListMap.get(detalle.getCriterio()).add(detalle);
            });
            Boolean activaPorFecha = DateUtils.isBetweenWithRange(new Date(), unidadMateriaConfiguracionBD.getFechaInicio(), unidadMateriaConfiguracionBD.getFechaFin(), ejbCapturaCalificaciones.leerDiasRangoParaCapturarUnidad());
            PermisosCapturaExtemporaneaGrupal permiso = em.createQuery("select p from PermisosCapturaExtemporaneaGrupal p inner join p.idPlanMateria pm inner join p.idGrupo g where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.idUnidadMateria=:unidad and p.validada=:valor", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("unidad", unidadMateria)
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
//            System.out.println("EjbPacker.packUnidadConfiguracion");
//            System.out.println("unidadMateria = " + unidadMateria);
//            System.out.println("permiso = " + permiso);
            Boolean activaPorPermiso = permiso != null;

            DtoUnidadConfiguracionAlineacion dto = new DtoUnidadConfiguracionAlineacion(unidadMateria, unidadMateriaConfiguracionBD, detalleListMap, dtoCargaAcademica, activaPorFecha, activaPorPermiso);
            return ResultadoEJB.crearCorrecto(dto, "Configuración de unidad empaquetada");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la unidad de configuración (EjbPacker.packUnidadConfiguracion).", e, DtoUnidadConfiguracionAlineacion.class);
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
     * Permite empaquetar el detalle de la configuración de unidad incluyendo su criterio y su indicador
     * @param unidadMateriaConfiguracionDetalle Instancia del entity del de talle de configuración del cual se vana leer su indicador y su criterio de evaluación
     * @return Regresa el empaquetado o código de error
     */
    public ResultadoEJB<DtoUnidadConfiguracionAlineacion.Detalle> packDtoUnidadConfiguracionDetalleAlineacion(UnidadMateriaConfiguracionEvidenciaInstrumento unidadMateriaConfiguracionDetalle){
        try{
            UnidadMateriaConfiguracionEvidenciaInstrumento unidadMateriaConfiguracionDetalleBD = em.find(UnidadMateriaConfiguracionEvidenciaInstrumento.class, unidadMateriaConfiguracionDetalle.getConfiguracionEvidenciaInstrumento());
            DtoUnidadConfiguracionAlineacion.Detalle detalle = new DtoUnidadConfiguracionAlineacion.Detalle(unidadMateriaConfiguracionDetalleBD, unidadMateriaConfiguracionDetalleBD.getEvidencia().getCriterio(), unidadMateriaConfiguracionDetalleBD.getEvidencia(), unidadMateriaConfiguracionDetalleBD.getInstrumento());
//            System.out.println("detalle = " + detalle);
            return ResultadoEJB.crearCorrecto(detalle, "Detalle de la configuración de unidad");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el detalle de la configuración de unidad (EjbPacker.packDtoUnidadConfiguracionDetalleAlineacion)", DtoUnidadConfiguracionAlineacion.Detalle.class);
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
            List<DtoInscripcion> dtoInscripciones = em.createQuery("select e from Estudiante e where e.matricula=:matricula order by e.periodo desc", Estudiante.class)
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
            if(dtoInscripcionActiva == null) return ResultadoEJB.crearErroneo(2, "El estudiante no tiene inscripción activa.", DtoEstudiante.class);
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
            ResultadoEJB<List<DtoEstudiante>> res = packDtoEstudiantesGrupo(dtoCargaAcademica);
            if(!res.getCorrecto()) {
                return ResultadoEJB.crearErroneo(2, res.getMensaje(), DtoGrupoEstudiante.class);
            }

            List<DtoCapturaCalificacion> dtoCapturaCalificaciones = res.getValor().stream()
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
     * Permite empaquetar un grupo de estudiantes a partir del empaquetado de unca carga academica y los ordena por apellido paterno, materno y nombre
     * @param dtoCargaAcademica Carga académica de la cual se quiere empaquetar el grupo de estudiantes
     * @return Grupo de estudiantes o código de error de lo contrario
     */
    public ResultadoEJB<DtoGrupoEstudianteAlineacion> packGrupoEstudianteAlineacion(DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracionAlineacion){
        try{
            ResultadoEJB<List<DtoEstudiante>> res = packDtoEstudiantesGrupo(dtoCargaAcademica);
            if(!res.getCorrecto()) {
                return ResultadoEJB.crearErroneo(2, res.getMensaje(), DtoGrupoEstudianteAlineacion.class);
            }

            List<DtoCapturaCalificacionAlineacion> dtoCapturaCalificaciones = res.getValor().stream()
                    .map(dtoEstudiante -> packCapturaCalificacionAlineacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracionAlineacion))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            DtoGrupoEstudianteAlineacion dtoGrupoEstudianteAlineacion = new DtoGrupoEstudianteAlineacion(dtoCargaAcademica, dtoCapturaCalificaciones);
            return ResultadoEJB.crearCorrecto(dtoGrupoEstudianteAlineacion, "Grupo empaquetado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el grupo con estudiantes a partir de una carga academica(EjbPacker.packGrupoEstudianteAlineacion).", e, DtoGrupoEstudianteAlineacion.class);
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
                    .map(detalle -> packDtoCapturaCalificacionCaptura(detalle, dtoEstudiante, dtoCargaAcademica.getPeriodo().getPeriodo()))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            
            PermisosCapturaExtemporaneaEstudiante permiso = em.createQuery("select p from PermisosCapturaExtemporaneaEstudiante p inner join p.idPlanMateria pm inner join p.idGrupo g inner join p.estudiante e where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.idUnidadMateria=:unidad and e.idEstudiante =:estudiante and p.validada=:valor", PermisosCapturaExtemporaneaEstudiante.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("unidad", dtoUnidadConfiguracion.getUnidadMateria())
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            
            Boolean permisoExtInd = permiso != null;
            
            DtoCapturaCalificacion dtoCapturaCalificacion = new DtoCapturaCalificacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion, permisoExtInd, capturas);
            ResultadoEJB<BigDecimal> resPromedio = ejbCapturaCalificaciones.promediarUnidad(dtoCapturaCalificacion);
            if(resPromedio.getCorrecto()) {
                dtoCapturaCalificacion.setPromedio(resPromedio.getValor());
                ResultadoEJB<Boolean> validarPromedioAprobatorio = ejbCapturaCalificaciones.validarPromedioAprobatorio(dtoCapturaCalificacion.getPromedio());
                if(validarPromedioAprobatorio.getCorrecto()){
                    dtoCapturaCalificacion.setEstaAprobado(validarPromedioAprobatorio.getValor());

                    ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
                    UnidadMateriaComentario unidadMateriaComentario = em.createQuery("select c from UnidadMateriaComentario c where c.estudiante=:estudiante and c.unidadMateriaConfiguracion=:configuracion", UnidadMateriaComentario.class)
                            .setParameter("estudiante", dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion())
                            .setParameter("configuracion", dtoUnidadConfiguracion.getUnidadMateriaConfiguracion())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    dtoCapturaCalificacion.setTieneComentarioReprobatorio(unidadMateriaComentario != null);
                    if(dtoCapturaCalificacion.getTieneComentarioReprobatorio()) dtoCapturaCalificacion.setComentarioReprobatorio(unidadMateriaComentario);

                    ResultadoEJB<Boolean> eliminarComentarioReprobatorio = ejbValidacionComentarios.eliminarComentarioReprobatorio(dtoCapturaCalificacion);//intenta eliminar el comentario si es que no es necesario
//                    System.out.println("eliminarComentarioReprobatorio = " + eliminarComentarioReprobatorio);
                }else validarPromedioAprobatorio.getException().printStackTrace();
            }else resPromedio.getException().printStackTrace();

            /*//identificar caso crítico abierto mas reciente registrado por el usuario
            ResultadoEJB<DtoCasoCritico> generarNuevo = ejbCasoCritico.generarNuevo(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion, CasoCriticoTipo.ASISTENCIA_IRREGURLAR);
            if(generarNuevo.getCorrecto() && !Objects.equals(CasoCriticoEstado.SIN_REGISTRO, generarNuevo.getValor().getEstado())) {
                dtoCapturaCalificacion.setDtoCasoCritico(generarNuevo.getValor());
                dtoCapturaCalificacion.setTieneCasoCritico(true);
            }

            //identificar casos críticos  generados por sistema
            CasoCriticoTipo.ListaSistema().forEach(casoCriticoTipo -> {
                ResultadoEJB<DtoCasoCritico> generarNuevo1 = ejbCasoCritico.generarNuevo(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion, casoCriticoTipo);
                if(generarNuevo1.getCorrecto() && !Objects.equals(CasoCriticoEstado.SIN_REGISTRO, generarNuevo1.getValor().getEstado())){
                    dtoCapturaCalificacion.getCasosCriticosSistema().put(casoCriticoTipo, generarNuevo1.getValor());
                    dtoCapturaCalificacion.setTieneCasoCriticoSistema(true);
                }
            });*/
            ResultadoEJB<Boolean> cargarCasosCriticos = ejbCasoCritico.cargarCasosCriticos(dtoCapturaCalificacion);
            if(!cargarCasosCriticos.getCorrecto()) cargarCasosCriticos.getException().printStackTrace();

            return ResultadoEJB.crearCorrecto(dtoCapturaCalificacion, "Captura de calificación empaquetada");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la captura de calificaciones a partir del empaquetado de estudiante y carga academica(EjbPacker.packCapturaCalificacion).", e, DtoCapturaCalificacion.class);
        }
    }
    
    /**
     * Permite empaquetar una captura de calificacion a partir de un empaquetado de estudiante y carga académica para preparar herramientas de captura de calificacion por un estudiante de un grupo en una materia
     * @param dtoEstudiante Empaquetado del estudiante a quien se le va a capturar calificaciones
     * @param dtoCargaAcademica Empaquetado de la carga académica a la que se le va a capturar la calificación.
     * @return Regresa el empaquetada para la captura de calificaciones  o código de error de lo contrario.
     */
    public ResultadoEJB<DtoCapturaCalificacionAlineacion> packCapturaCalificacionAlineacion(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion){
        try{
            //empaquetar captura de calificacion
            List<DtoCapturaCalificacionAlineacion.Captura> capturas = dtoUnidadConfiguracion.getUnidadMateriaConfiguracionDetalles()
                    .values()
                    .stream()
                    .flatMap(detalles -> detalles.stream())
                    .map(detalle -> packDtoCapturaCalificacionCapturaAlineacion(detalle, dtoEstudiante, dtoCargaAcademica.getPeriodo().getPeriodo()))
                    .filter(ResultadoEJB::getCorrecto)
                    .map(ResultadoEJB::getValor)
                    .collect(Collectors.toList());
            
            PermisosCapturaExtemporaneaEstudiante permiso = em.createQuery("select p from PermisosCapturaExtemporaneaEstudiante p inner join p.idPlanMateria pm inner join p.idGrupo g inner join p.estudiante e where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.idUnidadMateria=:unidad and e.idEstudiante =:estudiante and p.validada=:valor", PermisosCapturaExtemporaneaEstudiante.class)
                    .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                    .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                    .setParameter("unidad", dtoUnidadConfiguracion.getUnidadMateria())
                    .setParameter("estudiante", dtoEstudiante.getInscripcionActiva().getInscripcion().getIdEstudiante())
                    .setParameter("valor", (int)1)
                    .getResultStream()
                    .findAny()
                    .orElse(null);
            
            Boolean permisoExtInd = permiso != null;
            
            DtoCapturaCalificacionAlineacion dtoCapturaCalificacion = new DtoCapturaCalificacionAlineacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion, permisoExtInd, capturas);
            ResultadoEJB<BigDecimal> resPromedio = ejbCapturaCalificaciones.promediarUnidadAlineacion(dtoCapturaCalificacion);
            if(resPromedio.getCorrecto()) {
                dtoCapturaCalificacion.setPromedio(resPromedio.getValor());
                ResultadoEJB<Boolean> validarPromedioAprobatorio = ejbCapturaCalificaciones.validarPromedioAprobatorio(dtoCapturaCalificacion.getPromedio());
                if(validarPromedioAprobatorio.getCorrecto()){
                    dtoCapturaCalificacion.setEstaAprobado(validarPromedioAprobatorio.getValor());

                    ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
                    UnidadMateriaComentario unidadMateriaComentario = em.createQuery("select c from UnidadMateriaComentario c where c.estudiante=:estudiante and c.unidadMateriaConfiguracion=:configuracion", UnidadMateriaComentario.class)
                            .setParameter("estudiante", dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion())
                            .setParameter("configuracion", dtoUnidadConfiguracion.getUnidadMateriaConfiguracion())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    dtoCapturaCalificacion.setTieneComentarioReprobatorio(unidadMateriaComentario != null);
                    if(dtoCapturaCalificacion.getTieneComentarioReprobatorio()) dtoCapturaCalificacion.setComentarioReprobatorio(unidadMateriaComentario);

                    ResultadoEJB<Boolean> eliminarComentarioReprobatorio = ejbValidacionComentarios.eliminarComentarioReprobatorioAlineacion(dtoCapturaCalificacion);//intenta eliminar el comentario si es que no es necesario
//                    System.out.println("eliminarComentarioReprobatorio = " + eliminarComentarioReprobatorio);
                }else validarPromedioAprobatorio.getException().printStackTrace();
            }else resPromedio.getException().printStackTrace();

            ResultadoEJB<Boolean> cargarCasosCriticos = ejbCasoCritico.cargarCasosCriticosAlineacion(dtoCapturaCalificacion);
            if(!cargarCasosCriticos.getCorrecto()) cargarCasosCriticos.getException().printStackTrace();

            return ResultadoEJB.crearCorrecto(dtoCapturaCalificacion, "Captura de calificación empaquetada");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la captura de calificaciones a partir del empaquetado de estudiante y carga academica(EjbPacker.packCapturaCalificacion).", e, DtoCapturaCalificacionAlineacion.class);
        }
    }

    /**
     * Permite empaquetar una linea de captura de calificación que incluye el detalle de configuración de unidad y la referencia de la inscripción activa del estudiante
     * @param detalle Detalle de la configuración de unidad
     * @param dtoEstudiante Empaqueta del estudiante que contiene la lista de inscripciones de la cual se obtendría la inscripcón activa
     * @return
     */
    public ResultadoEJB<DtoCapturaCalificacion.Captura> packDtoCapturaCalificacionCaptura(DtoUnidadConfiguracion.Detalle detalle, DtoEstudiante dtoEstudiante, Integer periodo){
        try{
//            System.out.println("detalle = [" + detalle + "], dtoEstudiante = [" + dtoEstudiante + "]");
            /*Estudiante inscripcionActiva = dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(DtoInscripcion::getActivo)
                    .map(DtoInscripcion::getInscripcion)
                    .max(Comparator.comparing(Estudiante::getPeriodo))
                    .orElse(null);*/
//            Estudiante inscripcionActiva = dtoEstudiante.getInscripcionActiva().getInscripcion();
//            System.out.println("inscripcionActiva = " + inscripcionActiva);
//            if(inscripcionActiva == null) return ResultadoEJB.crearErroneo(2, "No se tiene una inscripción activa del estudiante a un grupo", DtoCapturaCalificacion.Captura.class);
            DtoInscripcion inscripcion = dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getPeriodo() == periodo)
                    .findFirst()
                    .orElse(null);
            if(inscripcion == null) return ResultadoEJB.crearErroneo(2, "No se tiene una inscripción del estudiante a un grupo en el periodo del evento escolar especificado.", DtoCapturaCalificacion.Captura.class);
            Calificacion calificacion = em.createQuery("select c from Calificacion  c where c.idEstudiante=:estudiante and c.configuracionDetalle=:detalle", Calificacion.class)
                    .setParameter("estudiante", inscripcion.getInscripcion())
                    .setParameter("detalle", detalle.getDetalle())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if(calificacion == null){//si no existe calificación se crea con valor nulo
                calificacion = new Calificacion();
                calificacion.setConfiguracionDetalle(detalle.getDetalle());
                calificacion.setIdEstudiante(inscripcion.getInscripcion());
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
     * Permite empaquetar una linea de captura de calificación que incluye el detalle de configuración de unidad y la referencia de la inscripción activa del estudiante
     * @param detalle Detalle de la configuración de unidad
     * @param dtoEstudiante Empaqueta del estudiante que contiene la lista de inscripciones de la cual se obtendría la inscripcón activa
     * @return
     */
    public ResultadoEJB<DtoCapturaCalificacionAlineacion.Captura> packDtoCapturaCalificacionCapturaAlineacion(DtoUnidadConfiguracionAlineacion.Detalle detalle, DtoEstudiante dtoEstudiante, Integer periodo){
        try{
            DtoInscripcion inscripcion = dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(dtoInscripcion -> dtoInscripcion.getInscripcion().getPeriodo() == periodo)
                    .findFirst()
                    .orElse(null);
            if(inscripcion == null) return ResultadoEJB.crearErroneo(2, "No se tiene una inscripción del estudiante a un grupo en el periodo del evento escolar especificado.", DtoCapturaCalificacionAlineacion.Captura.class);
            CalificacionEvidenciaInstrumento calificacion = em.createQuery("select c from CalificacionEvidenciaInstrumento  c where c.idEstudiante=:estudiante and c.configuracionEvidencia=:detalle", CalificacionEvidenciaInstrumento.class)
                    .setParameter("estudiante", inscripcion.getInscripcion())
                    .setParameter("detalle", detalle.getDetalle())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if(calificacion == null){//si no existe calificación se crea con valor nulo
                calificacion = new CalificacionEvidenciaInstrumento();
                calificacion.setConfiguracionEvidencia(detalle.getDetalle());
                calificacion.setIdEstudiante(inscripcion.getInscripcion());
                calificacion.setValor(null);
                em.persist(calificacion);
            }

            DtoCapturaCalificacionAlineacion.Captura captura = new DtoCapturaCalificacionAlineacion.Captura(detalle, calificacion);
//            System.out.println("captura = " + captura);
            return ResultadoEJB.crearCorrecto(captura, "Captura de calificación");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la captura de calificación (EjbPacker.packDtoCapturaCalificacionCaptura).", DtoCapturaCalificacionAlineacion.Captura.class);
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

    /**
     * Permite empaquetar un caso critico
     * @param casoCritico Instancia del caso critico proveniente de la base de datos
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa el empaquetado del caso critico o código de error en caso de no poner generarlo
     */
    public ResultadoEJB<DtoCasoCritico> packCasoCritico(CasoCritico casoCritico, DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion){
        try{
//            System.out.println("EjbPacker.packCasoCritico");
//            System.out.println("casoCritico = [" + casoCritico + "], dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            CasoCriticoEstado estado = CasoCriticoEstadoConverter.of(casoCritico.getEstado());
            CasoCriticoTipo tipo = CasoCriticoTipoConverter.of(casoCritico.getTipo());
//            System.out.println("tipo = " + tipo);
            DtoCasoCritico dtoCasoCritico = new DtoCasoCritico(casoCritico, tipo, estado, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
            return ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, DtoCasoCritico.class);
        }
    }
    
    /**
     * Permite empaquetar un caso critico
     * @param casoCritico Instancia del caso critico proveniente de la base de datos
     * @param dtoEstudiante Empaquetado de la inscripción activa del estudiante
     * @param dtoCargaAcademica Empaquetado de la carga académica del docente en la materia y grupo
     * @param dtoUnidadConfiguracion Empaquetado de la configuración de unidad que se muestra en pantalla
     * @return Regresa el empaquetado del caso critico o código de error en caso de no poner generarlo
     */
    public ResultadoEJB<DtoCasoCriticoAlineacion> packCasoCriticoAlineacion(CasoCritico casoCritico, DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion){
        try{
//            System.out.println("EjbPacker.packCasoCritico");
//            System.out.println("casoCritico = [" + casoCritico + "], dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            CasoCriticoEstado estado = CasoCriticoEstadoConverter.of(casoCritico.getEstado());
            CasoCriticoTipo tipo = CasoCriticoTipoConverter.of(casoCritico.getTipo());
//            System.out.println("tipo = " + tipo);
            DtoCasoCriticoAlineacion dtoCasoCritico = new DtoCasoCriticoAlineacion(casoCritico, tipo, estado, dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
            return ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, DtoCasoCriticoAlineacion.class);
        }
    }
    
    public ResultadoEJB<DtoCasoCritico> packCasoCriticoEstudiante(CasoCritico casoCritico, DtoEstudiante dtoEstudiante){
        try{
//            System.out.println("EjbPacker.packCasoCritico");
//            System.out.println("casoCritico = [" + casoCritico + "], dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            CasoCriticoEstado estado = CasoCriticoEstadoConverter.of(casoCritico.getEstado());
            CasoCriticoTipo tipo = CasoCriticoTipoConverter.of(casoCritico.getTipo());
//            System.out.println("tipo = " + tipo);
            DtoCasoCritico dtoCasoCritico = new DtoCasoCritico(casoCritico, tipo, estado, dtoEstudiante);
            return ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, DtoCasoCritico.class);
        }
    }
    
    public ResultadoEJB<DtoCasoCriticoAlineacion> packCasoCriticoEstudianteAlineacion(CasoCritico casoCritico, DtoEstudiante dtoEstudiante){
        try{
//            System.out.println("EjbPacker.packCasoCritico");
//            System.out.println("casoCritico = [" + casoCritico + "], dtoEstudiante = [" + dtoEstudiante + "], dtoCargaAcademica = [" + dtoCargaAcademica + "], dtoUnidadConfiguracion = [" + dtoUnidadConfiguracion + "]");
            CasoCriticoEstado estado = CasoCriticoEstadoConverter.of(casoCritico.getEstado());
            CasoCriticoTipo tipo = CasoCriticoTipoConverter.of(casoCritico.getTipo());
//            System.out.println("tipo = " + tipo);
            DtoCasoCriticoAlineacion dtoCasoCritico = new DtoCasoCriticoAlineacion(casoCritico, tipo, estado, dtoEstudiante);
            return ResultadoEJB.crearCorrecto(dtoCasoCritico, "Caso crítico empaquetado");
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "", e, DtoCasoCriticoAlineacion.class);
        }
    }

    /**
     * Permite empaquetar un mapeo de calificiones para estudiantes de una misma carga académica.
     * @param dtoCargaAcademica Empaquetado de la carga académica de la que se requiere reunir las calificaciones
     * @param dtoUnidadConfiguraciones Lista de empaquetados de las configuraciones de las unidades que conforman la materia de la carga académica
     * @return
     */
    public ResultadoEJB<DtoUnidadesCalificacion> packDtoUnidadesCalificacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones, EventoEscolar eventoEscolar){
        try{
            ResultadoEJB<List<DtoEstudiante>> res = packDtoEstudiantesHistoricoGrupo(dtoCargaAcademica);
            if(res.getCorrecto()) {
                List<DtoEstudiante> dtoEstudiantes = res.getValor();
                String tipoEval ="Nivelación Final";
                Boolean activaPorFecha = eventoEscolar == null?false:DateUtils.isBetweenWithRange(new Date(), eventoEscolar.getInicio(), eventoEscolar.getFin(), ejbCapturaCalificaciones.leerDiasRangoParaCapturarUnidad());
                PermisosCapturaExtemporaneaGrupal permiso = em.createQuery("select p from PermisosCapturaExtemporaneaGrupal p inner join p.idPlanMateria pm inner join p.idGrupo g where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.tipoEvaluacion=:tipo and p.validada=:valor", PermisosCapturaExtemporaneaGrupal.class)
                        .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                        .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                        .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                        .setParameter("tipo", tipoEval)
                        .setParameter("valor", (int)1)
                        .getResultStream()
                        .findAny()
                        .orElse(null);
                Boolean activaPorPermiso = permiso != null;

                DtoUnidadesCalificacion dtoUnidadesCalificacion = new DtoUnidadesCalificacion(dtoCargaAcademica, dtoEstudiantes, dtoUnidadConfiguraciones, activaPorFecha, activaPorPermiso);
                dtoEstudiantes.forEach(dtoEstudiante -> {
//                    if(dtoEstudiante.getAspirante().getIdAspirante() == 917)
//                        System.out.println("dtoEstudiante.getPersona() = " + dtoEstudiante.getPersona());
                    dtoUnidadConfiguraciones.forEach(dtoUnidadConfiguracion -> {
                        ResultadoEJB<DtoCapturaCalificacion> dtoCapturaCalificacionResultadoEJB = packCapturaCalificacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
                        if(dtoCapturaCalificacionResultadoEJB.getCorrecto()){
                            try {
//                                if(dtoEstudiante.getAspirante().getIdAspirante() == 917)
//                                    System.out.println("dtoCapturaCalificacionResultadoEJB.getValor() = " + dtoCapturaCalificacionResultadoEJB.getValor().getDtoCargaAcademica().getMateria());
                                dtoUnidadesCalificacion.agregarCapturaCalificacion(dtoEstudiante, dtoUnidadConfiguracion, dtoCapturaCalificacionResultadoEJB.getValor());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else
                            System.out.println("dtoCapturaCalificacionResultadoEJB = " + dtoCapturaCalificacionResultadoEJB);
                    });
                });
                return ResultadoEJB.crearCorrecto(dtoUnidadesCalificacion, "Empaqueta de calificaciones por unidad de un grupo.");
            }else return ResultadoEJB.crearErroneo(2, res.getMensaje(), DtoUnidadesCalificacion.class);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetas las calificaciones de todas la unidades de una materia(EjbPacker.packCapturaCalificacion).", e, DtoUnidadesCalificacion.class);
        }
    }
    
     /**
     * Permite empaquetar un mapeo de calificiones para estudiantes de una misma carga académica.
     * @param dtoCargaAcademica Empaquetado de la carga académica de la que se requiere reunir las calificaciones
     * @param dtoUnidadConfiguraciones Lista de empaquetados de las configuraciones de las unidades que conforman la materia de la carga académica
     * @return
     */
    public ResultadoEJB<DtoUnidadesCalificacionAlineacion> packDtoUnidadesCalificacionAlineacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull List<DtoUnidadConfiguracionAlineacion> dtoUnidadConfiguraciones, EventoEscolar eventoEscolar){
        try{
            ResultadoEJB<List<DtoEstudiante>> res = packDtoEstudiantesHistoricoGrupo(dtoCargaAcademica);
            if(res.getCorrecto()) {
                List<DtoEstudiante> dtoEstudiantes = res.getValor();
                String tipoEval ="Nivelación Final";
                Boolean activaPorFecha = eventoEscolar == null?false:DateUtils.isBetweenWithRange(new Date(), eventoEscolar.getInicio(), eventoEscolar.getFin(), ejbCapturaCalificaciones.leerDiasRangoParaCapturarUnidad());
                PermisosCapturaExtemporaneaGrupal permiso = em.createQuery("select p from PermisosCapturaExtemporaneaGrupal p inner join p.idPlanMateria pm inner join p.idGrupo g where current_date between  p.fechaInicio and p.fechaFin and g.idGrupo=:grupo and p.docente=:docente and pm.idMateria.idMateria=:materia and p.tipoEvaluacion=:tipo and p.validada=:valor", PermisosCapturaExtemporaneaGrupal.class)
                        .setParameter("docente", dtoCargaAcademica.getDocente().getPersonal().getClave())
                        .setParameter("grupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                        .setParameter("materia", dtoCargaAcademica.getMateria().getIdMateria())
                        .setParameter("tipo", tipoEval)
                        .setParameter("valor", (int)1)
                        .getResultStream()
                        .findAny()
                        .orElse(null);
                Boolean activaPorPermiso = permiso != null;

                DtoUnidadesCalificacionAlineacion dtoUnidadesCalificacion = new DtoUnidadesCalificacionAlineacion(dtoCargaAcademica, dtoEstudiantes, dtoUnidadConfiguraciones, activaPorFecha, activaPorPermiso);
                dtoEstudiantes.forEach(dtoEstudiante -> {
//                    if(dtoEstudiante.getAspirante().getIdAspirante() == 917)
//                        System.out.println("dtoEstudiante.getPersona() = " + dtoEstudiante.getPersona());
                    dtoUnidadConfiguraciones.forEach(dtoUnidadConfiguracion -> {
                        ResultadoEJB<DtoCapturaCalificacionAlineacion> dtoCapturaCalificacionResultadoEJB = packCapturaCalificacionAlineacion(dtoEstudiante, dtoCargaAcademica, dtoUnidadConfiguracion);
                        if(dtoCapturaCalificacionResultadoEJB.getCorrecto()){
                            try {
//                                if(dtoEstudiante.getAspirante().getIdAspirante() == 917)
//                                    System.out.println("dtoCapturaCalificacionResultadoEJB.getValor() = " + dtoCapturaCalificacionResultadoEJB.getValor().getDtoCargaAcademica().getMateria());
                                dtoUnidadesCalificacion.agregarCapturaCalificacion(dtoEstudiante, dtoUnidadConfiguracion, dtoCapturaCalificacionResultadoEJB.getValor());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else
                            System.out.println("dtoCapturaCalificacionResultadoEJB = " + dtoCapturaCalificacionResultadoEJB);
                    });
                });
                return ResultadoEJB.crearCorrecto(dtoUnidadesCalificacion, "Empaqueta de calificaciones por unidad de un grupo.");
            }else return ResultadoEJB.crearErroneo(2, res.getMensaje(), DtoUnidadesCalificacionAlineacion.class);
        }catch (Exception e){
            e.printStackTrace();
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetas las calificaciones de todas la unidades de una materia(EjbPacker.packCapturaCalificacion).", e, DtoUnidadesCalificacionAlineacion.class);
        }
    }

    /**
     * Permite empaquetar la lista de estudiantes de un grupo, segun su carga académica
     * @param dtoCargaAcademica Carga académica que determina grupo, docente y materia que se imparte
     * @return Lista de empaquetados de estudiantes pertenecientes al grupo de la carga académica o código de error de lo contrario
     */
    public ResultadoEJB<List<DtoEstudiante>> packDtoEstudiantesGrupo(DtoCargaAcademica dtoCargaAcademica){
        try{
            Grupo grupo = em.find(Grupo.class, dtoCargaAcademica.getGrupo().getIdGrupo());
            List<DtoEstudiante> dtoEstudiantes = em.createQuery("select e from Estudiante e where e.grupo=:grupo and e.tipoEstudiante.idTipoEstudiante=1 order by e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
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
                        if (paterno != null) res = res.concat(paterno);
                        if (materno != null) res = res.concat(materno);
                        if (nombre != null) res = res.concat(nombre);
                        return res;
                    }))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(dtoEstudiantes, "Lista de estudiantes de un grupo ordenados por apellido y nombre");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar las lista de estudiantes de un grupo ordenados por apellido y nombre(EjbPacker.packCapturaCalificacion).", e, null);
        }
    }
    
     /**
     * Permite empaquetar la lista de estudiantes de un grupo, segun su carga académica
     * @param dtoCargaAcademica Carga académica que determina grupo, docente y materia que se imparte
     * @return Lista de empaquetados de estudiantes pertenecientes al grupo de la carga académica o código de error de lo contrario
     */
    public ResultadoEJB<List<DtoEstudiante>> packDtoEstudiantesHistoricoGrupo(DtoCargaAcademica dtoCargaAcademica){
        try{
            Grupo grupo = em.find(Grupo.class, dtoCargaAcademica.getGrupo().getIdGrupo());
            List<DtoEstudiante> dtoEstudiantes = em.createQuery("select e from Estudiante e where e.grupo=:grupo order by e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
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
                        if (paterno != null) res = res.concat(paterno);
                        if (materno != null) res = res.concat(materno);
                        if (nombre != null) res = res.concat(nombre);
                        return res;
                    }))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(dtoEstudiantes, "Lista de estudiantes de un grupo ordenados por apellido y nombre");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar las lista de estudiantes de un grupo ordenados por apellido y nombre(EjbPacker.packCapturaCalificacion).", e, null);
        }
    }

    /**
     * Permite empaquetar la calificación de nivelación con entities sincronizadas si es que exoste el registro en la base de datos o transientes de lo contrario
     * @param dtoCargaAcademica Empaquetado de la carga académica de la nivelación deseada
     * @param dtoEstudiante Empaquetado del estudiante de la nivelación deseada
     * @param indicador Indicador de la nivelación deseada
     * @return Regresa el empaquetado o código de error si ocurre error.
     */
    public ResultadoEJB<DtoCalificacionNivelacion> packDtoCalificacionNivelacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull DtoEstudiante dtoEstudiante, @NonNull Indicador indicador){
        try{
            ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica = ejbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica(dtoEstudiante, dtoCargaAcademica);
            CalificacionNivelacionPK pk = new CalificacionNivelacionPK(dtoCargaAcademica.getCargaAcademica().getCarga(), dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion().getIdEstudiante());
            CalificacionNivelacion calificacionNivelacion = em.find(CalificacionNivelacion.class, pk);
            if(calificacionNivelacion == null){
                calificacionNivelacion = new CalificacionNivelacion(pk);
                calificacionNivelacion.setEstudiante(dtoEstudianteToDtoInscripcionPorCargaAcademica.getValor().getInscripcion());
                calificacionNivelacion.setCargaAcademica(dtoCargaAcademica.getCargaAcademica());
                calificacionNivelacion.setIndicador(indicador);
                calificacionNivelacion.setValor(0d);
            }

//            if(calificacionNivelacion.getCargaAcademica().getCarga() == 161 && calificacionNivelacion.getEstudiante().getIdEstudiante() == 194)
//                System.out.println("calificacionNivelacion.getIndicador() = " + calificacionNivelacion.getIndicador());

            DtoCalificacionNivelacion dtoCalificacionNivelacion = new DtoCalificacionNivelacion(calificacionNivelacion, indicador);
            dtoCalificacionNivelacion.setIndicador(calificacionNivelacion.getIndicador());
//            System.out.println("dtoCalificacionNivelacion.getIndicador() = " + dtoCalificacionNivelacion.getIndicador());
            return ResultadoEJB.crearCorrecto(dtoCalificacionNivelacion, "Empaquetado de nivelación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la calificación de nivelación (EjbPacker.packDtoCalificacionNivelacion).", e, DtoCalificacionNivelacion.class);
        }
    }

    /**
     * Permite empaquetar un área institucional y su categoría
     * @param area Clave del área requerida
     * @return Regresa el empaquetado, código 1 para error desconocido, código 2  para indicar una clave incorrecta o código 3 para indicar que el área no tiene categoría
     */
    public ResultadoEJB<DtoAreaUniversidadCategoria> packDtoAreaUniversidadCategoria(@NonNull Short area){
        try{
            //
            AreasUniversidad areasUniversidad = em.find(AreasUniversidad.class, area);
            if(areasUniversidad == null) return ResultadoEJB.crearErroneo(2, "No se encontró un área institucional con la clave especificada.", DtoAreaUniversidadCategoria.class);
            Categorias categoria = areasUniversidad.getCategoria();
            if(categoria == null) return ResultadoEJB.crearErroneo(3, "No se encontró la categoría del área requerida.", DtoAreaUniversidadCategoria.class);
            DtoAreaUniversidadCategoria dtoAreaUniversidadCategoria = new DtoAreaUniversidadCategoria(areasUniversidad, categoria);
            return ResultadoEJB.crearCorrecto(dtoAreaUniversidadCategoria, "Area de la universidad y su categoría empaquetados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar empaquetar un área intitucional con su clave.", e, DtoAreaUniversidadCategoria.class);
        }
    }

    /**
     * Empaqueta una asociación del estudiante con sus periodo escolar, en caso de ser un estudinate del nuevo sistema se inlcuye un empaquetado del estudiante
     * @param matricula Valor de la matrícula
     * @param periodo Valor del periodo a asociar
     * @return Regresa el empaquetado, código 1 para error desconocido, código 2 para indicar que la matricula no está registrada en el epriodo, código 3 para indicar que el periodo no es válido
     */
    public ResultadoEJB<DtoEstudiantePeriodo> packDtoEstudiantePeriodo(String matricula, Integer periodo){
        try{
            PeriodosEscolares periodosEscolares = em.find(PeriodosEscolares.class, periodo);
            if(periodosEscolares == null) return ResultadoEJB.crearErroneo(3, "El periodo no es válido", DtoEstudiantePeriodo.class);
            CiclosEscolares ciclosEscolares = periodosEscolares.getCiclo();

            MatriculaPeriodosEscolares matriculaPeriodosEscolares = em.createQuery("select m from MatriculaPeriodosEscolares m where m.periodo=:periodo and m.matricula=:matricula", MatriculaPeriodosEscolares.class)
                    .setParameter("matricula", StringUtils.trim(matricula))
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(matriculaPeriodosEscolares == null) return ResultadoEJB.crearErroneo(2, "La matrícula no está asociada al period en la BD", DtoEstudiantePeriodo.class);

            AreasUniversidad programa = em.find(AreasUniversidad.class, matriculaPeriodosEscolares.getProgramaEducativo());

            DtoEstudiantePeriodo dtoEstudiantePeriodo = new DtoEstudiantePeriodo(matriculaPeriodosEscolares, programa, periodosEscolares, ciclosEscolares, UsuarioTipo.ESTUDIANTE);
            /*ResultadoEJB<DtoEstudiante> packEstudiante = packEstudiante(NumberUtils.stringToInt(matricula));
            if(packEstudiante.getCorrecto()) {
                dtoEstudiantePeriodo.setDtoEstudiante(packEstudiante.getValor());
                dtoEstudiantePeriodo.setUsuarioTipo(UsuarioTipo.ESTUDIANTE19);
            }*/

            return ResultadoEJB.crearCorrecto(dtoEstudiantePeriodo, "Asociación de estudiante con periodo empaquetada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el estudiante con su periodo (EjbPacker.packDtoAreaUniversidadCategoria)", e, DtoEstudiantePeriodo.class);
        }
    }

    /**
     * Empaqueta una asociación del estudiante con sus periodo escolar, en caso de ser un estudinate del nuevo sistema se inlcuye un empaquetado del estudiante
     * @param matricula Valor de la matrícula
     * @param periodo Valor del periodo a asociar
     * @return Regresa el empaquetado, código 1 para error desconocido, código 2 para indicar que la matricula no está registrada en el epriodo, código 3 para indicar que el periodo no es válido
     */
    public ResultadoEJB<DtoEstudiantePeriodo> packDtoEstudiantePeriodoEnDemanda(String matricula, Integer periodo, List<MatriculaPeriodosEscolares> matriculasPeriodosEscolares, List<AreasUniversidad> programas){
        try{
            PeriodosEscolares periodosEscolares = em.find(PeriodosEscolares.class, periodo);
            if(periodosEscolares == null) return ResultadoEJB.crearErroneo(3, "El periodo no es válido", DtoEstudiantePeriodo.class);
            CiclosEscolares ciclosEscolares = periodosEscolares.getCiclo();

            MatriculaPeriodosEscolares matriculaPeriodosEscolares = matriculasPeriodosEscolares.stream().filter(m -> Objects.equals(m.getMatricula(), matricula)).filter(m -> Objects.equals(m.getPeriodo(), periodo)).findFirst().orElse(null);

            /*MatriculaPeriodosEscolares matriculaPeriodosEscolares = em.createQuery("select m from MatriculaPeriodosEscolares m where m.periodo=:periodo and m.matricula=:matricula", MatriculaPeriodosEscolares.class)
                    .setParameter("matricula", StringUtils.trim(matricula))
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);*/
            if(matriculaPeriodosEscolares == null) {
                List<Integer> periodos = ciclosEscolares.getPeriodosEscolaresList().stream().map(PeriodosEscolares::getPeriodo).collect(Collectors.toList());
                matriculaPeriodosEscolares = em.createQuery("select m from MatriculaPeriodosEscolares  m where m.matricula = :matricula and m.periodo in :periodos order by m.periodo desc", MatriculaPeriodosEscolares.class)
                        .setParameter("matricula", matricula)
                        .setParameter("periodos", periodos)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                if(matriculaPeriodosEscolares == null) return ResultadoEJB.crearErroneo(2, "La matrícula no está asociada al period en la BD", DtoEstudiantePeriodo.class);
                System.out.println("matriculaPeriodosEscolares = " + matriculaPeriodosEscolares);
            }

            final short programaEducativo = matriculaPeriodosEscolares.getProgramaEducativo();
            AreasUniversidad programa = programas.stream().filter(p -> Objects.equals(p.getArea(), programaEducativo)).findFirst().orElse(null); //em.find(AreasUniversidad.class, matriculaPeriodosEscolares.getProgramaEducativo());

            DtoEstudiantePeriodo dtoEstudiantePeriodo = new DtoEstudiantePeriodo(matriculaPeriodosEscolares, programa, periodosEscolares, ciclosEscolares, UsuarioTipo.ESTUDIANTE);
            /*ResultadoEJB<DtoEstudiante> packEstudiante = packEstudiante(NumberUtils.stringToInt(matricula));
            if(packEstudiante.getCorrecto()) {
                dtoEstudiantePeriodo.setDtoEstudiante(packEstudiante.getValor());
                dtoEstudiantePeriodo.setUsuarioTipo(UsuarioTipo.ESTUDIANTE19);
            }*/

            return ResultadoEJB.crearCorrecto(dtoEstudiantePeriodo, "Asociación de estudiante con periodo empaquetada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el estudiante con su periodo (EjbPacker.packDtoAreaUniversidadCategoria)", e, DtoEstudiantePeriodo.class);
        }
    }

    /**
     * Empaqueta los resultados obtenidos de la encuesta de satisfacción de servicios incluyendo el empaquetado del estudiante/periodo, el registro de los resultados de la encuesta y un mapa de preguntas y sus valores de respuestas
     * de un estudiante
     * @param encuestaServiciosResultados Registro de respuestas que dió el estudiante en su cuestionario
     * @param evaluacion Instancia de la evaluación correspondiente de la que se desea obtener las
     * @param cuestionario
     * @return
     */
    public ResultadoEJB<DtoSatisfaccionServiciosEstudiante> packDtoSatisfaccionServiciosEstudiante(EncuestaServiciosResultados encuestaServiciosResultados, Evaluaciones evaluacion, DtoSatisfaccionServiciosCuestionario cuestionario, List<MatriculaPeriodosEscolares> matriculaPeriodosEscolaresList, List<AreasUniversidad> programas){
        try{
//            System.out.println("EjbPacker.packDtoSatisfaccionServiciosEstudiante");
//            System.out.println("evaluacion.getPeriodo() = " + evaluacion.getPeriodo());
            ResultadoEJB<DtoEstudiantePeriodo> packDtoEstudiantePeriodo = packDtoEstudiantePeriodoEnDemanda(String.valueOf(encuestaServiciosResultados.getEncuestaServiciosResultadosPK().getEvaluador()), evaluacion.getPeriodo(), matriculaPeriodosEscolaresList, programas);
            if(!packDtoEstudiantePeriodo.getCorrecto()) {
//                System.out.println("EjbPacker.packDtoSatisfaccionServiciosEstudiante");
//                System.out.println("encuestaServiciosResultados = " + encuestaServiciosResultados + ", evaluacion = " + evaluacion);
                System.out.println("String.valueOf(encuestaServiciosResultados.getEncuestaServiciosResultadosPK().getEvaluador()) = " + String.valueOf(encuestaServiciosResultados.getEncuestaServiciosResultadosPK().getEvaluador()));
                System.out.println("packDtoEstudiantePeriodo.getMensaje() = " + packDtoEstudiantePeriodo.getMensaje());
                return ResultadoEJB.crearErroneo(2, "No se pudo empaquetar DtoSatisfaccionServiciosEstudiante. Mensaje: ".concat(packDtoEstudiantePeriodo.getMensaje()), DtoSatisfaccionServiciosEstudiante.class);
            }else{
//                System.out.println("packDtoEstudiantePeriodo.getValor().getMatriculaPeriodosEscolares().getMatricula() = " + packDtoEstudiantePeriodo.getValor().getMatriculaPeriodosEscolares().getMatricula());
            }

            DtoSatisfaccionServiciosEstudiante dtoSatisfaccionServiciosEstudiante = new DtoSatisfaccionServiciosEstudiante(packDtoEstudiantePeriodo.getValor(), encuestaServiciosResultados);
            cuestionario.getPreguntas().forEach(pregunta -> {
                Integer posicion = cuestionario.getPosiciones().get(pregunta.getNumero());
//                System.out.println("posicion = " + posicion);
                switch (posicion){
                    case 0: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR1())); break;
                    case 1: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR2())); break;
                    case 2: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR3())); break;
                    case 3: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR4())); break;
                    case 4: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR5())); break;
                    case 5: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR6())); break;
                    case 6: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR7())); break;
                    case 7: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR8())); break;
                    case 8: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR9())); break;
                    case 9: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR10())); break;
                    case 10: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR11())); break;
                    case 11: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR12())); break;
                    case 12: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR13())); break;
                    case 13: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR14())); break;
                    case 14: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR15())); break;
                    case 15: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR16())); break;
                    case 16: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR17())); break;
                    case 17: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR18())); break;
                    case 18: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR19())); break;
                    case 19: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR20())); break;
                    case 20: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR21())); break;
                    case 21: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR22())); break;
                    case 22: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR23())); break;
                    case 23: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR24())); break;
                    case 24: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR25())); break;
                    case 25: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR26())); break;
                    case 26: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR27())); break;
                    case 27: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR28())); break;
                    case 28: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR29())); break;
                    case 29: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR30())); break;
                    case 30: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR31())); break;
                    case 31: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR32())); break;
                    case 32: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR33())); break;
                    case 33: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR34())); break;
                    case 34: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR35())); break;
                    case 35: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR36())); break;
                    case 36: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR37())); break;
                    case 37: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR38())); break;
                    case 38: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR39())); break;
                    case 39: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR40())); break;
                    case 40: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR41())); break;
                    case 41: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR42())); break;
                    case 42: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR43())); break;
                    case 43: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR44())); break;
                    case 44: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR45())); break;
                    case 45: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR46())); break;
                    case 46: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR47())); break;
                    case 47: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR48())); break;
                    case 48: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR49())); break;
                    case 49: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR50())); break;
                    case 50: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR51())); break;
                    case 51: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR52())); break;
                    case 52: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR53())); break;
                    case 53: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR54())); break;
                    case 54: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR55())); break;
                    case 55: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR56())); break;
                    case 56: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR57())); break;
                    case 57: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR58())); break;
                    case 58: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR59())); break;
                    case 59: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR60())); break;
                    case 60: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR61())); break;
                    case 61: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR62())); break;
                    case 62: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR63())); break;
                    case 63: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR64())); break;
                    case 64: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR65())); break;
                    case 65: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR66())); break;
                    case 66: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR67())); break;
                    case 67: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR68())); break;
                    case 68: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR69())); break;
                    case 69: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR70())); break;
                    case 70: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR71())); break;
                    case 71: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR72())); break;
                    case 72: dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap().put(pregunta, new BigDecimal(encuestaServiciosResultados.getR73())); break;
                }
            });

//            System.out.println("dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap() = " + dtoSatisfaccionServiciosEstudiante.getPreguntaValorMap());
            return ResultadoEJB.crearCorrecto(dtoSatisfaccionServiciosEstudiante, "dtoSatisfaccionServiciosEstudiante empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al empaquetar DtoSatisfaccionServiciosEstudiante (EjbPacker.packDtoSatisfaccionServiciosEstudiante)", e, DtoSatisfaccionServiciosEstudiante.class);
        }
    }
}
