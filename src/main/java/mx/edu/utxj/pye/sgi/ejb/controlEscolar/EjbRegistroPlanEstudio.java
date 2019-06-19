/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroPlanEstudio")
public class EjbRegistroPlanEstudio {
    
    @EJB EjbAsignacionAcademica ejbAsignacionAcademica;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    
    /**
     * Permite validar el usuario autenticado si es como director de área académica
     * @param clave Número de Nomina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try {
            return ejbAsignacionAcademica.validarDirector(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroPlanEstudio.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener el listado de áreas de conocimiento activas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreaConocimiento>> getAreasConocimiento(){
        try {
            final List<AreaConocimiento> areasConocimiento = f.getEntityManager().createQuery("SELECT ac FROM AreaConocimiento ac WHERE ac.estatus = 1", AreaConocimiento.class)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(areasConocimiento, "Lista de áreas de conocimiento activas");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de áreas de conocimiento", e, null);
        }
    }
    
    /**
     * Permite obtener el listado de competencias del plan de estudios seleccionado
     * @param plan clave del plan de estudios activo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Competencia>> getCompetenciasPlan(PlanEstudio plan){
        try {
            final List<Competencia> competenciasPlan = f.getEntityManager().createQuery("SELECT c FROM Competencia c WHERE c.planEstudios = :plan", Competencia.class)
                    .setParameter("plan", plan.getIdPlanEstudio())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(competenciasPlan, "Lista de competenciad activas por plan de estudio");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Imposible obtener el listado de competenciade del plan de estudio", e, null);
        }
    }

            
            
    /**
     * Mapea los programas educativos con sus planes de estudio
     * @param director Área operativa del director
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<AreasUniversidad, List<PlanEstudio>>> getProgramasEducativos(PersonalActivo director){
        try {
            Integer programaEducativoCategoria = ep.leerPropiedadEntera("programaEducativoCategoria").orElse(9);
            
            List<AreasUniversidad> programas = f.getEntityManager().createQuery("select a from AreasUniversidad  a where a.areaSuperior=:areaPoa and a.categoria.categoria=:categoria and a.vigente = '1' order by a.nombre", AreasUniversidad.class)
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
    
    public List<PlanEstudio> generarPlanesEstudio(AreasUniversidad programas){
        return f.getEntityManager().createQuery("SELECT pe FROM PlanEstudio pe WHERE pe.idPe = :programa", PlanEstudio.class)
                .setParameter("programa", programas.getArea())
                .getResultList();
    }
    
    /**
     * Permite obtener los planes de estudios vigentes de acuerdo al programa educativo
     * @param ProgramaEducativo clave del programa educativo
     * @return  Resultado del proceso
     */
    public ResultadoEJB<PlanEstudio> getPlanEstudios(Integer ProgramaEducativo){
        try {
            //TODO: listar los planes de estudios vigente del programa educativo
            return ResultadoEJB.crearCorrecto(null, "Listado de plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No fue posible obtener el listado de planes de estudio. (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite registrar un nuevo plan de estudios y poder registrar sus materias
     * @param areasUniversidad área a la cual pertenece el nuevo plan de estudios
     * @param planEstudio plan de estudios a registrar, actualizar o eliminar
     * @param operacion operación a realizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<PlanEstudio> registrarPlanEstudio(AreasUniversidad areasUniversidad,PlanEstudio planEstudio, Operacion operacion){
        try {
            switch (operacion){
                case PERSISTIR:
                    //TODO: registrar plan de estudios
                    return ResultadoEJB.crearCorrecto(planEstudio, "Se registró correctamente el Plan Estudio");
                case ACTUALIZAR:
                    //TODO: actualizar plan de estudios
                    return ResultadoEJB.crearCorrecto(planEstudio, "Se actualizo correctamente el Plan Estudio");
                case ELIMINAR:
                    //TODO: eliminar plan de estudios
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente el Plan Estudio");
                default:
                    return ResultadoEJB.crearErroneo(2, "Operación no autorizada.", PlanEstudio.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el Plan Estudio (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite registrar materias al plan de estudios seleccionado
     * @param planEstudio plan de estudios seleccionado
     * @param materia materia a registrar
     * @param operacion operación a registrar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> registrarMateriaPlanEstudio(PlanEstudio planEstudio, Materia materia, Operacion operacion){
        try {
            switch (operacion){
                case PERSISTIR:
                    //TODO: registrar plan de estudios
                    return ResultadoEJB.crearCorrecto(null, "Se registró correctamente la materia");
                case ACTUALIZAR:
                    //TODO: actualizar plan de estudios
                    return ResultadoEJB.crearCorrecto(null, "Se actualizo correctamente la materia");
                case ELIMINAR:
                    //TODO: eliminar plan de estudios
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente la materia");
                default:
                    return ResultadoEJB.crearErroneo(1, null, "Operación no autorizada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo registar la materia con el plan de estudios (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite listar las materias registradas para el plan de estudio seleccionado
     * @param planEstudio Clave de plan de estudios seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getListadoMateriasPlanEstudio(Integer planEstudio){
        try {
            //TODO:Consulta de materias de acuerdo al plan de estudios seleccionado
            return ResultadoEJB.crearCorrecto(null, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista materias para este plan de estudios (EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite asignar unidades a las materias del plan de estudios seleccionado
     * @param unidadMateria unidad de la materia a registrar
     * @param operacion operación a realizar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Materia, List<UnidadMateria>>> registrarUnidadMateria(UnidadMateria unidadMateria, Operacion operacion){
        try {
            switch(operacion){
                case PERSISTIR:
                    //TODO: registrar Unidad Materia
                    return ResultadoEJB.crearCorrecto(null, "Se registró la unidad para la materia corectamente");
                case ACTUALIZAR:
                    //TODO: actualizar Unidad Materia
                    return ResultadoEJB.crearCorrecto(null, "Se actualizo correctamente la unidad para la materia");
                case ELIMINAR:
                    //TODO: eliminar unidad materia
                    return ResultadoEJB.crearCorrecto(null, "Se elimino correctamente unidad para la materia");
                default:
                    return ResultadoEJB.crearErroneo(1, null, "Operación no autorizada");
            }
        } catch (Exception e) {
             return ResultadoEJB.crearErroneo(1, "No se pudo registrar la unidad(EjbRegistroPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite mapear el listado de materias con sus unidades registradas de acuerdo al plan de estudios seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Materia,List<UnidadMateria>>> getListaUnidadesMateria(){
        try {
            //TODO: Listado de unidades por materia
            return ResultadoEJB.crearCorrecto(null, "Listado de materias registrados para este Plan de estudios");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades(EjbRegistroPlanEstudio)", e, null);
        }
    }
}
