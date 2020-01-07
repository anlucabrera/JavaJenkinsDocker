/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaRegistro;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoMateriaUnidades;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanEstudioMateriaCompetencias;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroPlanEstudio")
public class EjbRegistroPlanEstudio {

    @EJB
    EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB
    EjbPropiedades ep;
    @EJB
    Facade f;
    private EntityManager em;

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
        return em.createQuery("SELECT pe FROM PlanEstudio pe WHERE pe.idPe = :idPe", PlanEstudio.class)
                .setParameter("idPe", programas.getArea())
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
//            List<Integer> claves = em.createQuery("SELECT g FROM Grupo g WHERE g.tutor =:tutor", Grupo.class)
//                    .setParameter("tutor", docente.getPersonal().getClave())
//                    .getResultStream()
//                    .map(a -> a.getPeriodo())
//                    .collect(Collectors.toList());
            List<PeriodosEscolares> periodos = new ArrayList<>();
            
            List<Integer> claves=new ArrayList<>();
            claves.add(52);
                    
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

}
