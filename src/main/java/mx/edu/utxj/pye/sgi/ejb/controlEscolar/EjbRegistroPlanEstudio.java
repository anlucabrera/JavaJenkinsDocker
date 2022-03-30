/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAlineacionAcedemica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaMetasPropuestas;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaRegistro;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaUnidades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudioMateriaCompetencias;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgreso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacionPlanMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacionPlanMateriaPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacional;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacionalPlanMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacionalPlanMateriaPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroPlanEstudio")
public class EjbRegistroPlanEstudio {

    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbCarga ejbCarga;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    private EntityManager em;
    
    List<PlanEstudio> programas;
    List<AreasUniversidad> areasUniversidads;
    List<DtoAlineacionAcedemica.PlanesEstudioDto> planesEstudioDto;
//    DtoAlineacionAcedemica.CatalosAlineacion catalosAlineacion;

    List<PlanEstudioMateria> pemsDTO;
    List<CriterioDesempenio> cdsDTO;
    List<AtributoEgreso> aesDTO;
    List<IndicadorAlineacion> iasDTO;
    List<ObjetivoEducacional> oesDTO;

    
    
    ObjetivoEducacional educacional;
    CriterioDesempenio desempenio;
    AtributoEgreso egreso;
    IndicadorAlineacion indicadorAlineacion;
    AreasUniversidad areaAlineacion;

    public static final String ALINEACION_CATALOGOS_PLANTILLA = "alineacionEducativaCatalogos.xlsx";
    public static final String ALINEACION_CATALOGOS_ACTUALIZADO = "alineacionEducativaCatalogos.xlsx";
    public static final String ALINEACION_PLANTILLA = "alineacionEducativa.xlsx";
    public static final String ALINEACION_ACTUALIZADO = "alineacionEducativa.xlsx";
    public static final String DESEMPENIOS_PLANTILLA = "nivelesDesempenio.xlsx";
    public static final String DESEMPENIOS_ACTUALIZADO = "nivelesDesempenio.xlsx";
    
    
    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }

    // Se valida el acceso del personal autorizado, así como se obtienen los catálogos necesarios para poder crear un plan de estudios
    /**
     * Permite validar el usuario autenticado si es como director de área
     * académica
     *
     * @param clave Número de Nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave) {
        try {
            return ejbAsignacionAcademica.validarDirector(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }

    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDirector(Integer clave) {
        try {
            return ejbAsignacionAcademica.validarEncargadoDireccion(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }

    /**
     * Permite obtener el listado de áreas de conocimiento activas
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreaConocimiento>> getAreasConocimiento() {
        try {
            final List<AreaConocimiento> areasConocimiento = em.createQuery("SELECT ac FROM AreaConocimiento ac WHERE ac.estatus = 1", AreaConocimiento.class)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(areasConocimiento, "Lista de áreas de conocimiento activas");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de áreas de conocimiento", e, null);
        }
    }
    
    /**
     * Permite obtener el listado de competencias del plan de estudios
     * seleccionado
     *
     * @param plan clave del plan de estudios activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Competencia>> getCompetenciasPlan(PlanEstudio plan) {
        try {
            final List<Competencia> competenciasPlan = em.createQuery("SELECT c FROM Competencia c INNER JOIN c.planEstudios plan WHERE plan.idPlanEstudio=:plan", Competencia.class)
                    .setParameter("plan", plan.getIdPlanEstudio())
                    .getResultList();
            competenciasPlan.add(new Competencia(0, "Nueva Competencia", "Generica"));

            return ResultadoEJB.crearCorrecto(competenciasPlan, "Lista de competenciad activas por plan de estudio");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de competenciade del plan de estudio", e, null);
        }
    }

    public ResultadoEJB<List<MetasPropuestas>> getMateriasMetas(PlanEstudio planEst) {
        try {
            final List<MetasPropuestas> metasplane = em.createQuery("SELECT m FROM MetasPropuestas m WHERE m.idPlanMateria.idPlan.idPlanEstudio = :idPlanEstudio", MetasPropuestas.class)
                    .setParameter("idPlanEstudio", planEst.getIdPlanEstudio())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(metasplane, "Lista de Metas propuestas activas por plan de estudio");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de Metas Propuestas del plan de estudio", e, null);
        }
    }
    
    public ResultadoEJB<List<MetasPropuestas>> getMateriasMetasIdiomas(PlanEstudio planEst) {
        try {
            final List<MetasPropuestas> idomas = em.createQuery("SELECT m FROM MetasPropuestas m INNER JOIN m.idPlanMateria pem INNER JOIN pem.idMateria a  INNER JOIN a.idAreaConocimiento c WHERE m.idPlanMateria.idPlan.idPlanEstudio = :idPlanEstudio AND (c.idAreaConocimiento=:tsu OR c.idAreaConocimiento=:ling)", MetasPropuestas.class)
                    .setParameter("idPlanEstudio", planEst.getIdPlanEstudio())
                    .setParameter("tsu", 3)
                    .setParameter("ling", 8)
                    .getResultList();
            List<MetasPropuestas> metasplane = idomas.stream().filter(t -> !t.getIdPlanMateria().getIdMateria().getNombre().contains("Oral")).collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(metasplane, "Lista de Metas propuestas activas por plan de estudio");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de Metas Propuestas del plan de estudio", e, null);
        }
    }


    public ResultadoEJB<List<DtoPlanEstudioMateriaCompetencias>> obtenerDtoPEMC(PlanEstudio plan) {
        try {
            final List<DtoPlanEstudioMateriaCompetencias> l = new ArrayList<>();
            final List<PlanEstudioMateria> pems = em.createQuery("SELECT pem FROM PlanEstudioMateria pem INNER JOIN pem.idPlan plan WHERE plan.idPlanEstudio=:plan", PlanEstudioMateria.class)
                    .setParameter("plan", plan.getIdPlanEstudio())
                    .getResultList();

            pems.forEach((p) -> {
                p.getCompetenciaList().forEach((c) -> {
                    DtoPlanEstudioMateriaCompetencias dpemc = new DtoPlanEstudioMateriaCompetencias(c, c, p, plan);
                    l.add(dpemc);
                });
            });

            return ResultadoEJB.crearCorrecto(l, "Lista de competenciad activas por plan de estudio");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de competenciade del plan de estudio", e, null);
        }
    }

    /**
     * Mapea los programas educativos con sus planes de estudio
     *
     * @param director Área operativa del director
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> getProgramasEducativos(PersonalActivo director) {
        try {
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);

            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("areaPoa", director.getAreaPOA().getArea())
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();

            Map<AreasUniversidad, List<PlanEstudio>> programasPlanMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarPlanesEstudio(programa)));

            return ResultadoEJB.crearCorrecto(programasPlanMap, "Listado de Programas Educativos");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de programas educativos. (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> getProgramasEducativosDocenteCargasAcademicas(PersonalActivo docente,PeriodosEscolares pe) {
        try {

            List<Short> planes = em.createQuery("select p from CargaAcademica ca INNER JOIN ca.idPlanMateria pem INNER JOIN pem.idPlan p where ca.docente=:docente  AND ca.evento.periodo=:periodo GROUP BY p.idPe", PlanEstudio.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", pe.getPeriodo())
                    .getResultStream()
                    .map(a -> a.getIdPe())
                    .collect(Collectors.toList());
            Map<AreasUniversidad, List<PlanEstudio>> programasPlanMap;
            List<AreasUniversidad> programas = new ArrayList<>(); 
            if (!planes.isEmpty()) {
                programas = em.createQuery("select a from AreasUniversidad a where a.area IN :areas order by a.area desc", AreasUniversidad.class)
                        .setParameter("areas", planes)
                        .getResultStream()
                        .distinct()
                        .collect(Collectors.toList());
            }

            programasPlanMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarPlanesEstudio(programa)));

            return ResultadoEJB.crearCorrecto(programasPlanMap, "Listado de Programas Educativos");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de programas educativos. (EjbRegistroPlanEstudio)", e, null);
        }
    }

    public ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> getProgramasEducativostotal() {
        try {
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);

            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();

            Map<AreasUniversidad, List<PlanEstudio>> programasPlanMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarPlanesEstudio(programa)));

            return ResultadoEJB.crearCorrecto(programasPlanMap, "Listado de Programas Educativos");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de programas educativos. (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> getProgramasEducativosTutorados(Integer cvTutor, Integer periodo) {
        try {
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);

            List<Short> gs = em.createQuery("SELECT g FROM Grupo g WHERE g.tutor=:tutor AND g.periodo = :periodo", Grupo.class)
                    .setParameter("tutor", cvTutor)
                    .setParameter("periodo", periodo)
                    .getResultStream()
                    .map(a -> a.getIdPe())
                    .collect(Collectors.toList());

            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.categoria.categoria=:categoria and a.vigente = '1' AND a.area IN :petutorados order by a.nombre", AreasUniversidad.class)
                    .setParameter("categoria", programaEducativoCategoria)
                    .setParameter("petutorados", gs)
                    .getResultList();

            Map<AreasUniversidad, List<PlanEstudio>> programasPlanMap = programas.stream()
                    .collect(Collectors.toMap(programa -> programa, programa -> generarPlanesEstudio(programa)));

            return ResultadoEJB.crearCorrecto(programasPlanMap, "Listado de Programas Educativos");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de programas educativos. (EjbRegistroPlanEstudio)", e, null);
        }
    }

    /**
     * Permite listar las materias registradas
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getListadoMaterias(PlanEstudio pe) {
        try {
            final List<Materia> materias = em.createQuery("SELECT m FROM PlanEstudioMateria pem INNER JOIN pem.idMateria m INNER JOIN pem.idPlan p WHERE p.idPlanEstudio=:idPlanEstudio", Materia.class)
                    .setParameter("idPlanEstudio", pe.getIdPlanEstudio())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(materias, "Listado de materias registradas");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista materias (EjbRegistroPlanEstudio)", e, null);
        }
    }

    // Se inicia con el CRUD de un plan de estudios 
    /**
     * Permite registrar un nuevo plan de estudios y poder registrar sus
     * materias
     *
     * @param areasUniversidad área a la cual pertenece el nuevo plan de
     * estudios
     * @param planEstudio plan de estudios a registrar, actualizar o eliminar
     * @param operacion operación a realizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<PlanEstudio> registrarPlanEstudio(AreasUniversidad areasUniversidad, PlanEstudio planEstudio, Operacion operacion) {
        try {
            f.setEntityClass(PlanEstudio.class);
            switch (operacion) {
                case PERSISTIR:
                    em.persist(planEstudio);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(planEstudio, "Se registró correctamente el Plan Estudio");
                case ACTUALIZAR:
                    em.merge(planEstudio);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(planEstudio, "Se actualizo correctamente el Plan Estudio");
                case ELIMINAR:
                    if (planEstudio.getPlanEstudioMateriaList().isEmpty()) {
                        f.remove(planEstudio);
                        f.flush();
                        return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente el Plan Estudio");
                    } else {
                        return ResultadoEJB.crearCorrecto(null, "No fue posible la eliminación del Plan de estudio, ya que cuanta con materias cargadas");
                    }
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", PlanEstudio.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el Plan Estudio (EjbRegistroPlanEstudio)", e, null);
        }
    }

    // Se inicia con el CRUD de materias con sus Unidades
    /**
     * Permite registrar un nuevo plan de estudios y poder registrar sus
     * materias
     *
     * @param materia materia a registrar, actualizar o eliminar
     * @param unidadesdMateria unidades pertenecientes a la materia registrada
     * @param operacion operación a realizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Materia> registrarMateria(DtoMateriaRegistro dmr, Operacion operacion) {
        try {
            f.setEntityClass(Materia.class);
            switch (operacion) {
                case PERSISTIR:
                    dmr.getMateria().setIdAreaConocimiento(dmr.getAreaConocimiento());
                    em.persist(dmr.getMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmr.getMateria(), "Se registró correctamente La Matertia");
                case ACTUALIZAR:
                    em.merge(dmr.getMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmr.getMateria(), "Se actualizo correctamente La Matertia");
                case ELIMINAR:
                    if (dmr.getMateria().getPlanEstudioMateriaList().isEmpty() && dmr.getMateria().getPlanEstudioMateriaList().isEmpty()) {
                        f.remove(dmr.getMateria());
                        f.flush();
                        return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente La Matertia");
                    } else {
                        return ResultadoEJB.crearCorrecto(null, "No fue posible la eliminación de la materia, ya que cuenta con registros de unidades y/o con una asignación a un plan de estudio");
                    }
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", Materia.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar La Matertia (EjbRegistroPlanEstudio)", e, null);
        }
    }

    /**
     * Permite registrar un nuevo plan de estudios y poder registrar sus
     * materias
     *
     * @param unidadMateria materia a registrar, actualizar o eliminar
     * @param operacion operación a realizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<UnidadMateria> registrarUnidadMateria(DtoMateriaUnidades dmu, Operacion operacion) {
        try {
            f.setEntityClass(UnidadMateria.class);
            switch (operacion) {
                case PERSISTIR:
                    dmu.getUnidadMateria().setIdMateria(dmu.getMateria());
                    em.persist(dmu.getUnidadMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmu.getUnidadMateria(), "Se registró correctamente La Unidad Materia");
                case ACTUALIZAR:
                    em.merge(dmu.getUnidadMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmu.getUnidadMateria(), "Se actualizo correctamente La Unidad Materia");
                case ELIMINAR:
//                    System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.registrarUnidadMateria(1)");
                    if (dmu.getUnidadMateria().getUnidadMateriaConfiguracionList().isEmpty()) {
//                        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.registrarUnidadMateria(2 true)");
                        f.remove(dmu.getUnidadMateria());
                        f.flush();
                        return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente La Unidad Materia");
                    } else {
//                        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.registrarUnidadMateria(2 false)");
                        return ResultadoEJB.crearCorrecto(null, "No fue posible la eliminación de la unidad, ya que cuenta con registros en el módulo de Configuración unidad materia");
                    }
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", UnidadMateria.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar La Unidad Materia (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public ResultadoEJB<MetasPropuestas> registrarMetaMateria(DtoMateriaMetasPropuestas dmu, Operacion operacion) {
        try {
            f.setEntityClass(MetasPropuestas.class);
            switch (operacion) {
                case PERSISTIR:
                    dmu.getMetasPropuestas().setIdPlanMateria(new PlanEstudioMateria());
                    dmu.getMetasPropuestas().setIdPlanMateria(dmu.getMateria());
                    em.persist(dmu.getMetasPropuestas());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmu.getMetasPropuestas(), "Se registró correctamente La Unidad Materia");
                case ACTUALIZAR:
                    em.merge(dmu.getMetasPropuestas());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dmu.getMetasPropuestas(), "Se actualizo correctamente La Unidad Materia");
                case ELIMINAR:
                    f.remove(dmu.getMetasPropuestas());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente La Unidad Materia");
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", MetasPropuestas.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar La Unidad Materia (EjbRegistroPlanEstudio)", e, null);
        }
    }

    /**
     * Permite registrar materias al plan de estudios seleccionado
     *
     * @param materia materia a registrar
     * @param unidadMateria unidades que contendra la materia de estudios
     * seleccionado
     * @param operacion operación a registrar
     * @return Resultado del proceso
     */
    public ResultadoEJB<PlanEstudioMateria> registrarPlanEstudioMateria(Materia materia, PlanEstudio planEstudio, PlanEstudioMateria estudioMateria, Operacion operacion) {
        try {
            f.setEntityClass(PlanEstudioMateria.class);
            switch (operacion) {
                case PERSISTIR:
                    estudioMateria.setIdMateria(materia);
                    estudioMateria.setIdPlan(planEstudio);
                    em.persist(estudioMateria);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(estudioMateria, "Se registró correctamente la materia");
                case ACTUALIZAR:
                    em.merge(estudioMateria);
                    f.flush();
                    return ResultadoEJB.crearCorrecto(estudioMateria, "Se actualizo correctamente la materia");
                case ELIMINAR:
                    if (estudioMateria.getCargaAcademicaList().isEmpty()) {
                        f.remove(estudioMateria);
                        f.flush();
                        return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente la materia");
                    } else {
                        return ResultadoEJB.crearCorrecto(null, "No fue posible la eliminación del Plan estudio Materia, ya que cuenta con registros en el módulo de Carga académica");
                    }

                default:
                    return ResultadoEJB.crearErroneo(1, null, "Operación no autorizada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo registar la materia con el plan de estudios (EjbRegistroPlanEstudio)", e, null);
        }
    }

    public ResultadoEJB<Competencia> registrarPlanEstudioMateriaCompetencias(DtoPlanEstudioMateriaCompetencias dpemc, Operacion operacion) {
        try {
            switch (operacion) {
                case PERSISTIR:
                    f.setEntityClass(Competencia.class);
                    dpemc.getCompetenciaNewR().setPlanEstudios(dpemc.getPlanEstudio());
                    dpemc.getPlanEstudioMateria().getCompetenciaList().add(dpemc.getCompetenciaNewR());
                    em.persist(dpemc.getCompetenciaNewR());
                    dpemc.getCompetenciaNewR().getPlanEstudioMateriaList().add(dpemc.getPlanEstudioMateria());
                    em.merge(dpemc.getPlanEstudioMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dpemc.getCompetencia(), "Se registró correctamente la competencia");
                case ACTUALIZAR:
                    dpemc.getCompetencia().setPlanEstudios(dpemc.getPlanEstudio());
                    dpemc.getPlanEstudioMateria().getCompetenciaList().add(dpemc.getCompetencia());
                    dpemc.getCompetencia().getPlanEstudioMateriaList().add(dpemc.getPlanEstudioMateria());
                    em.merge(dpemc.getCompetencia());
                    em.merge(dpemc.getPlanEstudioMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(dpemc.getCompetencia(), "Se actualizo correctamente la materia");
                case ELIMINAR:
                    f.setEntityClass(Competencia.class);
                    f.remove(dpemc.getPlanEstudioMateria());
                    f.flush();
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente la materia");
                default:
                    return ResultadoEJB.crearErroneo(1, null, "Operación no autorizada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo registar la materia con el plan de estudios (EjbRegistroPlanEstudio)", e, null);
        }
    }

    public List<PlanEstudio> generarPlanesEstudio(AreasUniversidad programas) {
        return em.createQuery("SELECT pe FROM PlanEstudio pe WHERE pe.idPe = :idPe AND pe.estatus=:estatus", PlanEstudio.class)
                .setParameter("idPe", programas.getArea())
                .setParameter("estatus", Boolean.TRUE)
                .getResultList();
    }

    /**
     * Permite obtener los planes de estudios vigentes de acuerdo al programa
     * educativo
     *
     * @param ProgramaEducativo clave del programa educativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<PlanEstudio> getPlanEstudios(Integer ProgramaEducativo) {
        try {
            //TODO: listar los planes de estudios vigente del programa educativo
            return ResultadoEJB.crearCorrecto(null, "Listado de plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de planes de estudio. (EjbRegistroPlanEstudio)", e, null);
        }
    }

    /**
     * Permite listar las materias registradas para el plan de estudio
     * seleccionado
     *
     * @param planEstudio Clave de plan de estudios seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getListadoMateriasPlanEstudio(Integer planEstudio) {
        try {
            //TODO:Consulta de materias de acuerdo al plan de estudios seleccionado
            return ResultadoEJB.crearCorrecto(null, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista materias para este plan de estudios (EjbRegistroPlanEstudio)", e, null);
        }
    }

    /**
     * Permite mapear el listado de materias con sus unidades registradas de
     * acuerdo al plan de estudios seleccionado
     *
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Materia, List<UnidadMateria>>> getListaUnidadesMateria() {
        try {
            List<Materia> ms = em.createQuery("select ma from Materia  ma", Materia.class).getResultList();

            Map<Materia, List<UnidadMateria>> UnidadesMa = ms.stream()
                    .collect(Collectors.toMap(materia -> materia, materia -> generarunidadesMaterias(materia)));

            return ResultadoEJB.crearCorrecto(UnidadesMa, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades(EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public List<UnidadMateria> generarunidadesMaterias(Materia materia) {
        List<UnidadMateria> p = em.createQuery("SELECT um FROM UnidadMateria um INNER JOIN um.idMateria ma WHERE ma.idMateria = :idMateria", UnidadMateria.class)
                .setParameter("idMateria", materia.getIdMateria())
                .getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
    
    public ResultadoEJB<Map<Materia, List<MetasPropuestas>>> getListaMetasMateria() {
        try {
            List<Materia> ms = em.createQuery("select ma from Materia  ma", Materia.class).getResultList();

            Map<Materia, List<MetasPropuestas>> UnidadesMa = ms.stream()
                    .collect(Collectors.toMap(materia -> materia, materia -> generarMetasMaterias(materia)));

            return ResultadoEJB.crearCorrecto(UnidadesMa, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades(EjbRegistroPlanEstudio)", e, null);
        }
    }

    public List<MetasPropuestas> generarMetasMaterias(Materia materia) {
        List<MetasPropuestas> p = em.createQuery("SELECT um FROM MetasPropuestas um INNER JOIN um.idPlanMateria ma INNER JOIN ma.idMateria mat WHERE mat.idMateria = :idMateria", MetasPropuestas.class)
                .setParameter("idMateria", materia.getIdMateria())
                .getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public ResultadoEJB<List<PlanEstudioMateria>> getListaPlanEstudioMaterias(PersonalActivo director) {
        try {
            final List<PlanEstudio> pe = new ArrayList<>();
            final List<PlanEstudioMateria> pems = new ArrayList<>();
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);

            List<AreasUniversidad> programas = em.createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
                    .setParameter("areaPoa", director.getAreaPOA().getArea())
                    .setParameter("categoria", programaEducativoCategoria)
                    .getResultList();

//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.getListaPlanEstudioMaterias(programas)"+programas.size());
            programas.forEach((t) -> {
                pe.addAll(generarPlanesEstudio(t));
            });
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.getListaPlanEstudioMaterias(pe)"+pe.size());

            pe.forEach((t) -> {
                pems.addAll(generarPlanEstuidoMaterias(t));
            });
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.getListaPlanEstudioMaterias(pems)"+pems.size());

            return ResultadoEJB.crearCorrecto(pems, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de Planes de estudio(EjbRegistroPlanEstudio)", e, null);
        }
    }

    public List<PlanEstudioMateria> generarPlanEstuidoMaterias(PlanEstudio estudio) {
        return em.createQuery("SELECT pem FROM PlanEstudioMateria pem INNER JOIN pem.idPlan plan WHERE plan.idPlanEstudio = :idPlanEstudio", PlanEstudioMateria.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
    }

    public List<Competencia> generarCompetenciasPorPlanEstuidoMaterias(PlanEstudioMateria pem) {
        PlanEstudioMateria estudioMateria = new PlanEstudioMateria();
        estudioMateria = em.createQuery("SELECT pem FROM PlanEstudioMateria pem WHERE pem.idPlanMateria = :idPlanMateria", PlanEstudioMateria.class)
                .setParameter("idPlanMateria", pem.getIdPlanMateria())
                .getResultList().get(0);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.generarCompetenciasPorPlanEstuidoMaterias(1)"+estudioMateria);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroPlanEstudio.generarCompetenciasPorPlanEstuidoMaterias(1)"+estudioMateria.getCompetenciaList().isEmpty());
        if (estudioMateria.getCompetenciaList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return estudioMateria.getCompetenciaList();
        }
    }

    public ResultadoEJB<List<Grupo>> getListaGrupoPlanEstudio(PlanEstudio planEstudio, PeriodosEscolares escolares) {
        try {
            List<Grupo> gs = em.createQuery("select g from Grupo g INNER JOIN g.plan p where p.idPlanEstudio=:idPlanEstudio AND g.periodo=:periodo", Grupo.class)
                    .setParameter("idPlanEstudio", planEstudio.getIdPlanEstudio())
                    .setParameter("periodo", escolares.getPeriodo())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(gs, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de Planes de estudio(EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public ResultadoEJB<List<Grupo>> getListaGrupoPorTutor(PersonalActivo tutor, PeriodosEscolares escolares) {
        try {
            List<Grupo> gs = em.createQuery("select g from Grupo g where g.tutor=:tutor AND g.periodo=:periodo", Grupo.class)
                    .setParameter("tutor", tutor.getPersonal().getClave())
                    .setParameter("periodo", escolares.getPeriodo())
                    .getResultList();

            return ResultadoEJB.crearCorrecto(gs, "Listado de grupos por tutor");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de Planes de estudio(EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    public ResultadoEJB<List<Grupo>> getListaGrupoPorTutorYPe(PersonalActivo tutor, PeriodosEscolares escolares, Short pe) {
        try {
            List<Grupo> gs = em.createQuery("select g from Grupo g where g.tutor=:tutor AND g.periodo=:periodo AND g.idPe=:idpe", Grupo.class)
                    .setParameter("tutor", tutor.getPersonal().getClave())
                    .setParameter("periodo", escolares.getPeriodo())
                    .setParameter("idpe", pe)
                    .getResultList();

            return ResultadoEJB.crearCorrecto(gs, "Listado de grupos por tutor");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de Planes de estudio(EjbRegistroPlanEstudio)", e, null);
        }
    }

    public ResultadoEJB<List<Listaalumnosca>> getListaAlumnosPorGrupo(Grupo grupo) {
        try {
            List<Listaalumnosca> listaalumnoscas = new ArrayList<>();
            List<Estudiante> cas = em.createQuery("select c from Estudiante c INNER JOIN c.grupo g INNER JOIN c.tipoEstudiante t where g.idGrupo=:idGrupo AND t.idTipoEstudiante=:idTipoEstudiante", Estudiante.class)
                    .setParameter("idGrupo", grupo.getIdGrupo())
                    .setParameter("idTipoEstudiante", Short.parseShort("1"))
                    .getResultList();
            if (!cas.isEmpty()) {
                List<Integer> matriculas=new ArrayList<>();
                cas.forEach((t) -> {
                    matriculas.add(t.getMatricula());
                });
                listaalumnoscas = em.createQuery("select a from Listaalumnosca a WHERE a.matricula in :matriculas GROUP BY a.matricula ORDER BY a.esApePat,a.esApeMat,a.esNombre", Listaalumnosca.class)
                        .setParameter("matriculas", matriculas)
                        .getResultList();
            }
            return ResultadoEJB.crearCorrecto(listaalumnoscas, "Listado de Alumnos registrados para este grupo");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de Planes de estudio(EjbRegistroPlanEstudio)", e, null);
        }
    }

    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes() {
        try {
            final List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();

            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
        }
    }

    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosRegistros(PersonalActivo docente) {
        try {
            List<Integer> claves = em.createQuery("SELECT g FROM Grupo g WHERE g.tutor =:tutor", Grupo.class)
                    .setParameter("tutor", docente.getPersonal().getClave())
                    .getResultStream()
                    .map(a -> a.getPeriodo())
                    .collect(Collectors.toList());
            List<PeriodosEscolares> periodos = new ArrayList<>();
            
//            List<Integer> claves=new ArrayList<>();
//            claves.add(52);
                    
            if (!claves.isEmpty()) {
                periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                        .setParameter("periodos", claves)
                        .getResultStream()
                        .distinct()
                        .collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
        }
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosRegistrosPE(PlanEstudio progEdu) {
        try {
            List<Integer> claves = em.createQuery("SELECT g FROM Grupo g INNER JOIN g.plan p WHERE p.idPlanEstudio =:idPlanEstudio GROUP BY g.periodo", Grupo.class)
                    .setParameter("idPlanEstudio", progEdu.getIdPlanEstudio())
                    .getResultStream()
                    .map(a -> a.getPeriodo())
                    .collect(Collectors.toList());
            List<PeriodosEscolares> periodos = new ArrayList<>();
            
//            List<Integer> claves=new ArrayList<>();
//            claves.add(52);
                    
            if (!claves.isEmpty()) {
                periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                        .setParameter("periodos", claves)
                        .getResultStream()
                        .distinct()
                        .collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
        }
    }
    
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
    
    public List<DtoAlineacionAcedemica.Presentacion> generarDtoAlineacionAcedemica(PlanEstudio estudio,String tipo) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<PlanEstudioMateria> pems = em.createQuery("SELECT pem FROM PlanEstudioMateria pem INNER JOIN pem.idPlan plan WHERE plan.idPlanEstudio = :idPlanEstudio", PlanEstudioMateria.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        
        if (!pems.isEmpty()) {
            pems.forEach((t) -> {
                switch (tipo) {
                    case "Ob":
                        if (!t.getObjetivoEducacionalPlanMateriaList().isEmpty()) {
                            t.getObjetivoEducacionalPlanMateriaList().forEach((ob) -> {
                                ObjetivoEducacional oe = ob.getObjetivoEducacional1();
                                daas.add(new DtoAlineacionAcedemica.Presentacion(oe.getObjetivoEducacional(), oe.getClave(), oe.getDescripcion(), ob.getNivelAportacion(), 0D, oe.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "Ae":
                        if (!t.getAtributoEgresoList().isEmpty()) {
                            t.getAtributoEgresoList().forEach((ob) -> {
                                daas.add(new DtoAlineacionAcedemica.Presentacion(ob.getAtributoEgreso(), ob.getClave(), ob.getDescripcion(), "", 0D, ob.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "Cr":
                        if (!t.getCriterioDesempenioList().isEmpty()) {
                            t.getCriterioDesempenioList().forEach((ob) -> {
                                daas.add(new DtoAlineacionAcedemica.Presentacion(ob.getCriteriDesempenio(), ob.getClave(), ob.getDescripcion(), "", 0D, ob.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "In":
                        if (!t.getIndicadorAlineacionPlanMateriaList().isEmpty()) {
                            t.getIndicadorAlineacionPlanMateriaList().forEach((ob) -> {
                                IndicadorAlineacion oe = ob.getIndicadorAlineacion();
                                daas.add(new DtoAlineacionAcedemica.Presentacion(oe.getIndicadorPem(), oe.getClave(), oe.getDescripcion(), "", ob.getMetaIndicador(), oe.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                }
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<DtoAlineacionAcedemica.Presentacion> generarDtoAlineacionAcedemicaIdiomas(PlanEstudio estudio,String tipo) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<PlanEstudioMateria> idomas = em.createQuery("SELECT pem FROM PlanEstudioMateria pem INNER JOIN pem.idPlan plan INNER JOIN pem.idMateria m INNER JOIN m.idAreaConocimiento c WHERE plan.idPlanEstudio = :idPlanEstudio AND (c.idAreaConocimiento=:tsu OR c.idAreaConocimiento=:ling)", PlanEstudioMateria.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .setParameter("tsu", 3)
                .setParameter("ling", 8)
                .getResultList();
        List<PlanEstudioMateria> pems =idomas.stream().filter(t -> !t.getIdMateria().getNombre().contains("Oral")).collect(Collectors.toList());
        
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        
        if (!pems.isEmpty()) {
            pems.forEach((t) -> {
                switch (tipo) {
                    case "Ob":
                        if (!t.getObjetivoEducacionalPlanMateriaList().isEmpty()) {
                            t.getObjetivoEducacionalPlanMateriaList().forEach((ob) -> {
                                ObjetivoEducacional oe = ob.getObjetivoEducacional1();
                                daas.add(new DtoAlineacionAcedemica.Presentacion(oe.getObjetivoEducacional(), oe.getClave(), oe.getDescripcion(), ob.getNivelAportacion(), 0D, oe.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "Ae":
                        if (!t.getAtributoEgresoList().isEmpty()) {
                            t.getAtributoEgresoList().forEach((ob) -> {
                                daas.add(new DtoAlineacionAcedemica.Presentacion(ob.getAtributoEgreso(), ob.getClave(), ob.getDescripcion(), "", 0D, ob.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "Cr":
                        if (!t.getCriterioDesempenioList().isEmpty()) {
                            t.getCriterioDesempenioList().forEach((ob) -> {
                                daas.add(new DtoAlineacionAcedemica.Presentacion(ob.getCriteriDesempenio(), ob.getClave(), ob.getDescripcion(), "", 0D, ob.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                    case "In":
                        if (!t.getIndicadorAlineacionPlanMateriaList().isEmpty()) {
                            t.getIndicadorAlineacionPlanMateriaList().forEach((ob) -> {
                                IndicadorAlineacion oe = ob.getIndicadorAlineacion();
                                daas.add(new DtoAlineacionAcedemica.Presentacion(oe.getIndicadorPem(), oe.getClave(), oe.getDescripcion(), "", ob.getMetaIndicador(), oe.getPlanEstudio(), t,auv,tipo));
                            });
                        }
                        break;
                }
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<DtoAlineacionAcedemica.Presentacion> generarCatalogoObjetivosEducacionales(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<ObjetivoEducacional> oes = em.createQuery("SELECT pem FROM ObjetivoEducacional pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", ObjetivoEducacional.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                daas.add(new DtoAlineacionAcedemica.Presentacion(t.getObjetivoEducacional(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),auv,"Ob"));
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<DtoAlineacionAcedemica.PlanesEstudioMateriaCatalogo> generarCatalogogeneralPEM(Map<AreasUniversidad, List<PlanEstudio>> map) {
        final List<DtoAlineacionAcedemica.PlanesEstudioMateriaCatalogo> daas = new ArrayList<>();
        programas = new ArrayList<>();
        map.forEach((t, u) -> {
            programas.addAll(u);
        });
        if (!programas.isEmpty()) {
            programas.forEach((t) -> {
                List<PlanEstudioMateria> pems = em.createQuery("SELECT pem FROM PlanEstudioMateria pem INNER JOIN pem.idPlan plan WHERE plan.idPlanEstudio = :idPlanEstudio", PlanEstudioMateria.class)
                        .setParameter("idPlanEstudio", t.getIdPlanEstudio())
                        .getResultList();
                pems.forEach((ob) -> {
                    String fil=ob.getIdPlan().getIdPlanEstudio()+"-"+ob.getGrado();
                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioMateriaCatalogo(t, ob,fil));
                });
            });
        }
        return daas;
    }
    
    public String generaClave(String nivel, String tipoR) {
        String cvN="";
        if (nivel.equals("TSU")) {
            cvN = tipoR+"0-T";
        } else if (nivel.equals("5B")) {
            cvN = tipoR+"0-L";
        } else {
            cvN = tipoR+"0-I";
        }
        return cvN;
    }
    
    public List<DtoAlineacionAcedemica.PlanesEstudioObjtivo> generarCatalogogeneralOb(Map<AreasUniversidad, List<PlanEstudio>> map) {
        final List<DtoAlineacionAcedemica.PlanesEstudioObjtivo> daas = new ArrayList<>();
        map.forEach((t, u) -> {
//            System.out.println("Area " + t.getNombre() + " PEs " + u.size());
            if (!u.isEmpty()) {
                PlanEstudio estudio = u.get(0);
                ObjetivoEducacional oe = new ObjetivoEducacional();
                oe.setObjetivoEducacional(0);
                oe.setClave(generaClave(t.getNivelEducativo().getNivel(), "OE"));
                oe.setDescripcion("No aplica");
                daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, oe));
//                try {
//                    areaAlineacion = new AreasUniversidad();
//                    areaAlineacion = ejbAreasLogeo.mostrarAreasUniversidad(estudio.getIdPe());
//                } catch (Throwable ex) {
//                    Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                List<ProgramasEducativosContinuidad> pec = ejbAreasLogeo.listaProgramasEducativosContinuidadAlineaciones(estudio.getIdPe());
                List<ObjetivoEducacional> oes = em.createQuery("SELECT pem FROM ObjetivoEducacional pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", ObjetivoEducacional.class)
                        .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                        .getResultList();
//                if (pec.isEmpty()) {
//                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, oe, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
                oes.forEach((ob) -> {
                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, ob));
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, ob, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
                });
//                } else {
//                    pec.forEach((ct) -> {
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, oe, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
//                        oes.forEach((ob) -> {
//                            daas.add(new DtoAlineacionAcedemica.PlanesEstudioObjtivo(estudio, ob, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
//                        });
//                    });
//                }
            }
        });

//        Collections.sort(daas, (x, y) -> x.getContinuidad().compareTo(y.getContinuidad()));
        return daas;
    }

    public List<DtoAlineacionAcedemica.PlanesEstudioCriterio> generarCatalogogeneralCr(Map<AreasUniversidad, List<PlanEstudio>> map) {
        final List<DtoAlineacionAcedemica.PlanesEstudioCriterio> daas = new ArrayList<>();
        map.forEach((t, u) -> {
//            System.out.println("Area " + t.getNombre() + " PEs " + u.size());
            if (!u.isEmpty()) {
                PlanEstudio estudio = u.get(0);
                CriterioDesempenio oe = new CriterioDesempenio();
                oe.setCriteriDesempenio(0);
                oe.setClave(generaClave(t.getNivelEducativo().getNivel(), "CD"));
                oe.setDescripcion("No aplica");
                daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, oe));
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, oe));
//                try {
//                    areaAlineacion = new AreasUniversidad();
//                    areaAlineacion = ejbAreasLogeo.mostrarAreasUniversidad(estudio.getIdPe());
//                } catch (Throwable ex) {
//                    Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                List<ProgramasEducativosContinuidad> pec = ejbAreasLogeo.listaProgramasEducativosContinuidadAlineaciones(estudio.getIdPe());
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, oe, estudio.getIdPlanEstudio() + "-0", areaAlineacion.getNivelEducativo().getNombre()));
                List<CriterioDesempenio> cds = em.createQuery("SELECT pem FROM CriterioDesempenio pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", CriterioDesempenio.class)
                        .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                        .getResultList();
//                if (pec.isEmpty()) {
//                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, oe, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
                cds.forEach((ob) -> {
                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, ob));
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, ob, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
                });
//                } else {
//                    pec.forEach((ct) -> {
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, oe, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
//                        cds.forEach((ob) -> {
//                            daas.add(new DtoAlineacionAcedemica.PlanesEstudioCriterio(estudio, ob, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
//                        });
//                    });
//                }
            }
        });
//        Collections.sort(daas, (x, y) -> x.getContinuidad().compareTo(y.getContinuidad()));
        return daas;
    }

    public List<DtoAlineacionAcedemica.PlanesEstudioAtributo> generarCatalogogeneralAt(Map<AreasUniversidad, List<PlanEstudio>> map) {
        final List<DtoAlineacionAcedemica.PlanesEstudioAtributo> daas = new ArrayList<>();
        map.forEach((t, u) -> {
//            System.out.println("Area " + t.getNombre() + " PEs " + u.size());
            if (!u.isEmpty()) {
                PlanEstudio estudio = u.get(0);
                AtributoEgreso oe = new AtributoEgreso();
                oe.setAtributoEgreso(0);
                oe.setClave(generaClave(t.getNivelEducativo().getNivel(), "AE"));
                oe.setDescripcion("No aplica");
                daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, oe));
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, oe));
//                try {
//                    areaAlineacion = new AreasUniversidad();
//                    areaAlineacion = ejbAreasLogeo.mostrarAreasUniversidad(estudio.getIdPe());
//                } catch (Throwable ex) {
//                    Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                List<ProgramasEducativosContinuidad> pec = ejbAreasLogeo.listaProgramasEducativosContinuidadAlineaciones(estudio.getIdPe());
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, oe, estudio.getIdPlanEstudio() + "-0", areaAlineacion.getNivelEducativo().getNombre()));
                List<AtributoEgreso> aes = em.createQuery("SELECT pem FROM AtributoEgreso pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", AtributoEgreso.class)
                        .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                        .getResultList();
//                if (pec.isEmpty()) {
//                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, oe, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
                aes.forEach((ob) -> {
                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, ob));
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, ob, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
                });
//                } else {
//                    pec.forEach((ct) -> {
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, oe, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
//                        aes.forEach((ob) -> {
//                            daas.add(new DtoAlineacionAcedemica.PlanesEstudioAtributo(estudio, ob, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
//                        });
//                    });
//                }
            }
        });
//        Collections.sort(daas, (x, y) -> x.getContinuidad().compareTo(y.getContinuidad()));
        return daas;
    }

    public List<DtoAlineacionAcedemica.PlanesEstudioIndicador> generarCatalogogeneralIn(Map<AreasUniversidad, List<PlanEstudio>> map) {
        final List<DtoAlineacionAcedemica.PlanesEstudioIndicador> daas = new ArrayList<>();
        map.forEach((t, u) -> {
//            System.out.println("Area " + t.getNombre() + " PEs " + u.size());
            if (!u.isEmpty()) {
                PlanEstudio estudio = u.get(0);
                IndicadorAlineacion oe = new IndicadorAlineacion();
                oe.setIndicadorPem(0);
                oe.setClave(generaClave(t.getNivelEducativo().getNivel(), "I"));
                oe.setDescripcion("No aplica");
                daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, oe));
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, oe));
//                try {
//                    areaAlineacion = new AreasUniversidad();
//                    areaAlineacion = ejbAreasLogeo.mostrarAreasUniversidad(estudio.getIdPe());
//                } catch (Throwable ex) {
//                    Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                List<ProgramasEducativosContinuidad> pec = ejbAreasLogeo.listaProgramasEducativosContinuidadAlineaciones(estudio.getIdPe());
//                daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, oe, estudio.getIdPlanEstudio() + "-0", areaAlineacion.getNivelEducativo().getNombre()));
                List<IndicadorAlineacion> ias = em.createQuery("SELECT pem FROM IndicadorAlineacion pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", IndicadorAlineacion.class)
                        .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                        .getResultList();
//                if (pec.isEmpty()) {
//                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, oe, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
                ias.forEach((ob) -> {
                    daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, ob));
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, ob, estudio.getIdPe() + "-0", areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
                });
//                } else {
//                    pec.forEach((ct) -> {
//                        daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, oe, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+oe.getClave()));
//                        ias.forEach((ob) -> {
//                            daas.add(new DtoAlineacionAcedemica.PlanesEstudioIndicador(estudio, ob, ct.getProgramaTSU().getArea() + "-" + ct.getProgramaContinuidad(), areaAlineacion.getNivelEducativo().getNombre(),estudio.getIdPlanEstudio()+"-"+ob.getClave()));
//                        });
//                    });
//                }
            }
        });
//        Collections.sort(daas, (x, y) -> x.getContinuidad().compareTo(y.getContinuidad()));
        return daas;
    }

    public List<DtoAlineacionAcedemica.Presentacion> generarIndicadoresAlineacion(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<IndicadorAlineacion> oes = em.createQuery("SELECT pem FROM IndicadorAlineacion pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", IndicadorAlineacion.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                if (!t.getIndicadorAlineacionPlanMateriaList().isEmpty()) {
                    t.getIndicadorAlineacionPlanMateriaList().forEach((o) -> {
                        daas.add(new DtoAlineacionAcedemica.Presentacion(t.getIndicadorPem(), t.getClave(), t.getDescripcion(), "", o.getMetaIndicador(), t.getPlanEstudio(), o.getPlanEstudioMateria(),auv,"In"));
                    });
                }else {
                    daas.add(new DtoAlineacionAcedemica.Presentacion(t.getIndicadorPem(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),new AreasUniversidad(),"In"));
                }
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
   public List<DtoAlineacionAcedemica.Presentacion> generarCatalogoIndicadoresAlineacion(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<IndicadorAlineacion> oes = em.createQuery("SELECT pem FROM IndicadorAlineacion pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", IndicadorAlineacion.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
               .getResultList();
       AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
       if (!oes.isEmpty()) {
           oes.forEach((t) -> {
               daas.add(new DtoAlineacionAcedemica.Presentacion(t.getIndicadorPem(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),auv,"In"));
           });
           return daas;
       } else {
           return new ArrayList<>();
       }
    }
   
    public List<DtoAlineacionAcedemica.Presentacion> generarCriteriosDesempenio(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<CriterioDesempenio> oes = em.createQuery("SELECT pem FROM CriterioDesempenio pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", CriterioDesempenio.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                if (!t.getPlanEstudioMateriaList().isEmpty()) {
                    t.getPlanEstudioMateriaList().forEach((o) -> {
                        daas.add(new DtoAlineacionAcedemica.Presentacion(t.getCriteriDesempenio(), t.getClave(), t.getDescripcion(),"", 0D, t.getPlanEstudio(), o,auv,"Cr"));
                    });
                } else {
                    daas.add(new DtoAlineacionAcedemica.Presentacion(t.getCriteriDesempenio(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),new AreasUniversidad(),"Cr"));
                }
        });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }

    public List<DtoAlineacionAcedemica.Presentacion> generarCatalogoCriteriosDesempenio(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<CriterioDesempenio> oes = em.createQuery("SELECT pem FROM CriterioDesempenio pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", CriterioDesempenio.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                daas.add(new DtoAlineacionAcedemica.Presentacion(t.getCriteriDesempenio(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),auv,"Cr"));
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<DtoAlineacionAcedemica.Presentacion> generarAtributosEgreso(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<AtributoEgreso> oes = em.createQuery("SELECT pem FROM AtributoEgreso pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", AtributoEgreso.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                if (!t.getPlanEstudioMateriaList().isEmpty()) {
                    t.getPlanEstudioMateriaList().forEach((o) -> {
                        daas.add(new DtoAlineacionAcedemica.Presentacion(t.getAtributoEgreso(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), o,auv,"Ae"));
                    });
                } else {
                    daas.add(new DtoAlineacionAcedemica.Presentacion(t.getAtributoEgreso(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),new AreasUniversidad(),"Ae"));
                }
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public List<DtoAlineacionAcedemica.Presentacion> generarCatalogoAtributosEgreso(PlanEstudio estudio) {
        List<DtoAlineacionAcedemica.Presentacion> daas = new ArrayList<>();
        List<AtributoEgreso> oes = em.createQuery("SELECT pem FROM AtributoEgreso pem INNER JOIN pem.planEstudio plan WHERE plan.idPlanEstudio = :idPlanEstudio", AtributoEgreso.class)
                .setParameter("idPlanEstudio", estudio.getIdPlanEstudio())
                .getResultList();
        AreasUniversidad auv = em.createQuery("SELECT au FROM AreasUniversidad au WHERE au.area = :area", AreasUniversidad.class)
                .setParameter("area", estudio.getIdPe())
                .getSingleResult();
        if (!oes.isEmpty()) {
            oes.forEach((t) -> {
                daas.add(new DtoAlineacionAcedemica.Presentacion(t.getAtributoEgreso(), t.getClave(), t.getDescripcion(), "", 0D, t.getPlanEstudio(), new PlanEstudioMateria(),auv,"Ae"));
            });
            return daas;
        } else {
            return new ArrayList<>();
        }
    }
    
    public void accionesAlineacion(List<PlanEstudioMateria> pem, DtoAlineacionAcedemica.Presentacion daa,String tipo,Operacion operacion) {
        switch(operacion){
            case PERSISTIR: 
                switch(tipo){
                    case "Ob":
                        f.setEntityClass(ObjetivoEducacional.class);
                        educacional= new ObjetivoEducacional();
                        if(daa.getIde()==0){
                            educacional.setPlanEstudio(new PlanEstudio());
                            educacional.setPlanEstudio(daa.getPlanEstudio());
                            educacional.setClave(daa.getClave());
                            educacional.setDescripcion(daa.getDescripcion());
                            em.persist(educacional);
                        }else{
                            educacional=em.find(ObjetivoEducacional.class, daa.getIde());
                        }
                        f.flush();
                        if (!pem.isEmpty()) {
                            pem.forEach((t) -> {
                                f.setEntityClass(ObjetivoEducacionalPlanMateria.class);
                                ObjetivoEducacionalPlanMateriaPK materiaPK = new ObjetivoEducacionalPlanMateriaPK();
                                materiaPK.setIdPlanMateria(t.getIdPlanMateria());
                                materiaPK.setObjetivoEducacional(educacional.getObjetivoEducacional());
                                ObjetivoEducacionalPlanMateria oepm = new ObjetivoEducacionalPlanMateria();
                                oepm.setNivelAportacion(daa.getNivelA());
                                oepm.setObjetivoEducacional1(new ObjetivoEducacional());
                                oepm.setObjetivoEducacional1(educacional);
                                oepm.setPlanEstudioMateria(new PlanEstudioMateria());
                                oepm.setPlanEstudioMateria(t);
                                oepm.setObjetivoEducacionalPlanMateriaPK(new ObjetivoEducacionalPlanMateriaPK());
                                oepm.setObjetivoEducacionalPlanMateriaPK(materiaPK);
                                em.persist(oepm);
                                f.flush();
                            });
                        }
                        break;
                    case "Ae":
                        f.setEntityClass(AtributoEgreso.class);
                        egreso= new AtributoEgreso();
                        if(daa.getIde()==0){
                            egreso.setPlanEstudio(new PlanEstudio());
                            egreso.setPlanEstudio(daa.getPlanEstudio());
                            egreso.setClave(daa.getClave());
                            egreso.setDescripcion(daa.getDescripcion());
                            em.persist(egreso);
                        }else{
                            egreso=em.find(AtributoEgreso.class, daa.getIde());
                        }
                        f.flush();
                        if (!pem.isEmpty()) {
                            pem.forEach((t) -> {
                                egreso.getPlanEstudioMateriaList().add(t);
                                em.merge(egreso);
                                f.flush();
                            });
                        }
                        break;
                    case "In":
                        f.setEntityClass(IndicadorAlineacion.class);
                        indicadorAlineacion= new IndicadorAlineacion();
                        if(daa.getIde()==0){
                            indicadorAlineacion.setPlanEstudio(new PlanEstudio());
                            indicadorAlineacion.setPlanEstudio(daa.getPlanEstudio());
                            indicadorAlineacion.setClave(daa.getClave());
                            indicadorAlineacion.setDescripcion(daa.getDescripcion());
                            em.persist(indicadorAlineacion);
                        }else{
                            indicadorAlineacion=em.find(IndicadorAlineacion.class, daa.getIde());
                        }
                        f.flush();

                        if (!pem.isEmpty()) {
                            pem.forEach((t) -> {
                                f.setEntityClass(IndicadorAlineacion.class);
                                IndicadorAlineacionPlanMateriaPK materiaPK = new IndicadorAlineacionPlanMateriaPK();
                                materiaPK.setIdPlanMateria(t.getIdPlanMateria());
                                materiaPK.setIndicador(indicadorAlineacion.getIndicadorPem());
                                IndicadorAlineacionPlanMateria oepm = new IndicadorAlineacionPlanMateria();
                                oepm.setMetaIndicador(daa.getMeta());
                                oepm.setIndicadorAlineacion(new IndicadorAlineacion());
                                oepm.setIndicadorAlineacion(indicadorAlineacion);
                                oepm.setPlanEstudioMateria(new PlanEstudioMateria());
                                oepm.setPlanEstudioMateria(t);
                                oepm.setIndicadorAlineacionPlanMateriaPK(new IndicadorAlineacionPlanMateriaPK());
                                oepm.setIndicadorAlineacionPlanMateriaPK(materiaPK);
                                em.persist(oepm);
                                f.flush();
                            });
                        }
                        break;
                    case "Cr":
                        f.setEntityClass(CriterioDesempenio.class);
                        desempenio= new CriterioDesempenio();
                        if(daa.getIde()==0){
                            desempenio.setPlanEstudio(new PlanEstudio());
                            desempenio.setPlanEstudio(daa.getPlanEstudio());
                            desempenio.setClave(daa.getClave());
                            desempenio.setDescripcion(daa.getDescripcion());
                            em.persist(desempenio);
                        }else{
                            desempenio=em.find(CriterioDesempenio.class, daa.getIde());
                        }
                        f.flush();
                        if (!pem.isEmpty()) {
                            pem.forEach((t) -> {
                                desempenio.getPlanEstudioMateriaList().add(t);
                                em.merge(desempenio);
                                f.flush();
                            });
                        }
                        break;
                } 
                break;
            case ACTUALIZAR: 
                switch(tipo){
                    case "Ob":
                        f.setEntityClass(ObjetivoEducacional.class);
                        educacional = new ObjetivoEducacional();
                        educacional = em.find(ObjetivoEducacional.class, daa.getIde());
                        educacional.setDescripcion(daa.getDescripcion());
                        em.merge(educacional);

                        f.flush();

                        f.setEntityClass(ObjetivoEducacionalPlanMateria.class);
                      
                        ObjetivoEducacionalPlanMateriaPK materiaPK = new ObjetivoEducacionalPlanMateriaPK();
                        materiaPK.setIdPlanMateria(daa.getPlanEstudioMateria().getIdPlanMateria());
                        materiaPK.setObjetivoEducacional(educacional.getObjetivoEducacional());
                        ObjetivoEducacionalPlanMateria oepm = new ObjetivoEducacionalPlanMateria();
                        
                        oepm=em.find(ObjetivoEducacionalPlanMateria.class, materiaPK);
                        oepm.setNivelAportacion(daa.getNivelA());
                        
                        em.merge(oepm);
                        f.flush();

                        break;
                    case "Ae":
                        f.setEntityClass(AtributoEgreso.class);
                        egreso = new AtributoEgreso();
                        egreso = em.find(AtributoEgreso.class, daa.getIde());
                        egreso.setDescripcion(daa.getDescripcion());
                        em.merge(egreso);
                        f.flush();
                        break;
                    case "In":
                        f.setEntityClass(IndicadorAlineacion.class);
                        indicadorAlineacion = new IndicadorAlineacion();
                        indicadorAlineacion = em.find(IndicadorAlineacion.class, daa.getIde());
                        indicadorAlineacion.setDescripcion(daa.getDescripcion());
                        em.persist(indicadorAlineacion);
                        f.flush();
                        f.setEntityClass(IndicadorAlineacion.class);
                        IndicadorAlineacionPlanMateriaPK iapmpk = new IndicadorAlineacionPlanMateriaPK();
                        iapmpk.setIdPlanMateria(daa.getPlanEstudioMateria().getIdPlanMateria());
                        iapmpk.setIndicador(indicadorAlineacion.getIndicadorPem());

                        IndicadorAlineacionPlanMateria iapm = new IndicadorAlineacionPlanMateria();
                        iapm = em.find(IndicadorAlineacionPlanMateria.class, iapmpk);
                        iapm.setMetaIndicador(daa.getMeta());
                        em.merge(iapm);
                        f.flush();
                        break;
                    case "Cr":
                        f.setEntityClass(CriterioDesempenio.class);
                        desempenio = new CriterioDesempenio();
                        desempenio = em.find(CriterioDesempenio.class, daa.getIde());
                        desempenio.setDescripcion(daa.getDescripcion());
                        em.persist(desempenio);
                        f.flush();
                        break;
                }
                break;
            
            case ELIMINAR: break;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    public String getPlantillaAlineacionAcademica(Map<AreasUniversidad, List<PlanEstudio>> mapaAreaPe) throws Throwable {
        areasUniversidads = new ArrayList<>();
        programas = new ArrayList<>();
        mapaAreaPe.forEach((t, u) -> {
            areasUniversidads.add(t);
            programas.addAll(u);
        });
        AreasUniversidad au = areasUniversidads.get(0);
        AreasUniversidad auPeSu = ejbAreasLogeo.mostrarAreasUniversidad(au.getAreaSuperior());
        planesEstudioDto = new ArrayList<>();
        programas.forEach((t) -> {
            try {
                AreasUniversidad auPe = ejbAreasLogeo.mostrarAreasUniversidad(t.getIdPe());
                planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t,auPe));
//                if (auPe.getProgramasEducativosContinuidadList().isEmpty()) {
//                    ProgramasEducativosContinuidad continuidad = new ProgramasEducativosContinuidad();
//                    continuidad.setActivo(Boolean.FALSE);
//                    continuidad.setContinuidad(0);
//                    continuidad.setProgramaContinuidad(Short.valueOf("0"));
//                    continuidad.setProgramaTSU(auPe);
//                    planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, continuidad));
//                } else {
//                    auPe.getProgramasEducativosContinuidadList().forEach((c) -> {
//                        planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, c));
//                    });
//                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        String rutaPlantilla = ejbCarga.crearDirectorioPlantillaAlineacionMaterias();
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaAlineacionEducativaCompleto(auPeSu.getSiglas());
        String plantilla = rutaPlantilla.concat(ALINEACION_CATALOGOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ALINEACION_CATALOGOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("alineacionAcedemica", planesEstudioDto);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
    public String getPlantillaAlineacionAcademicaFinal(Map<AreasUniversidad, List<PlanEstudio>> mapaAreaPe) throws Throwable {
        areasUniversidads = new ArrayList<>();
        programas = new ArrayList<>();
        mapaAreaPe.forEach((t, u) -> {
            areasUniversidads.add(t);
            programas.addAll(u);
        });
        AreasUniversidad au = areasUniversidads.get(0);
        AreasUniversidad auPeSu = ejbAreasLogeo.mostrarAreasUniversidad(au.getAreaSuperior());
        planesEstudioDto = new ArrayList<>();
        programas.forEach((t) -> {
            try {
                AreasUniversidad auPe = ejbAreasLogeo.mostrarAreasUniversidad(t.getIdPe());
                planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t,auPe));
//                if (auPe.getProgramasEducativosContinuidadList().isEmpty()) {
//                    ProgramasEducativosContinuidad continuidad = new ProgramasEducativosContinuidad();
//                    continuidad.setActivo(Boolean.FALSE);
//                    continuidad.setContinuidad(0);
//                    continuidad.setProgramaContinuidad(Short.valueOf("0"));
//                    continuidad.setProgramaTSU(auPe);
//                    planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, continuidad));
//                } else {
//                    auPe.getProgramasEducativosContinuidadList().forEach((c) -> {
//                        planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, c));
//                    });
//                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        String rutaPlantilla = ejbCarga.crearDirectorioPlantillaAlineacionMaterias();
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaAlineacionEducativaCompleto(auPeSu.getSiglas());
        String plantilla = rutaPlantilla.concat(ALINEACION_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(ALINEACION_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("alineacionAcedemica", planesEstudioDto);
        beans.put("materias", generarCatalogogeneralPEM(mapaAreaPe));
        beans.put("objetivos", generarCatalogogeneralOb(mapaAreaPe));
        beans.put("indiAL", generarCatalogogeneralIn(mapaAreaPe));
        beans.put("criterio",generarCatalogogeneralCr(mapaAreaPe));
        beans.put("atributoAl", generarCatalogogeneralAt(mapaAreaPe));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
    public String getPlantillaNivelesDesempenioFinal(Map<AreasUniversidad, List<PlanEstudio>> mapaAreaPe) throws Throwable {
        areasUniversidads = new ArrayList<>();
        programas = new ArrayList<>();
        mapaAreaPe.forEach((t, u) -> {
            areasUniversidads.add(t);
            programas.addAll(u);
        });
        AreasUniversidad au = areasUniversidads.get(0);
        AreasUniversidad auPeSu = ejbAreasLogeo.mostrarAreasUniversidad(au.getAreaSuperior());
        planesEstudioDto = new ArrayList<>();
        programas.forEach((t) -> {
            try {
                AreasUniversidad auPe = ejbAreasLogeo.mostrarAreasUniversidad(t.getIdPe());
                planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t,auPe));
//                if (auPe.getProgramasEducativosContinuidadList().isEmpty()) {
//                    ProgramasEducativosContinuidad continuidad = new ProgramasEducativosContinuidad();
//                    continuidad.setActivo(Boolean.FALSE);
//                    continuidad.setContinuidad(0);
//                    continuidad.setProgramaContinuidad(Short.valueOf("0"));
//                    continuidad.setProgramaTSU(auPe);
//                    planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, continuidad));
//                } else {
//                    auPe.getProgramasEducativosContinuidadList().forEach((c) -> {
//                        planesEstudioDto.add(new DtoAlineacionAcedemica.PlanesEstudioDto(t, auPe, c));
//                    });
//                }
            } catch (Throwable ex) {
                Logger.getLogger(EjbRegistroPlanEstudio.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        String rutaPlantilla = ejbCarga.crearDirectorioPlantillaAlineacionMaterias();
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaAlineacionEducativaCompleto(auPeSu.getSiglas());
        String plantilla = rutaPlantilla.concat(DESEMPENIOS_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(DESEMPENIOS_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("alineacionAcedemica", planesEstudioDto);
        beans.put("materias", generarCatalogogeneralPEM(mapaAreaPe));
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
}

