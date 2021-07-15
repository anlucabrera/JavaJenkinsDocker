/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
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
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRegistroEvidInstEvaluacionMateria;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroEvidInstEvalMaterias")
public class EjbRegistroEvidInstEvalMaterias {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbAsignacionIndicadoresCriterios ejbAsignacionIndicadoresCriterios;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbCarga ejbCarga;
    @Getter @Setter Integer integradoras = 0;
    private EntityManager em;

    public static final String EVIDINSTMAT_PLANTILLA = "registroEvidenciasInstrumentos.xlsx";
    public static final String EVIDINSTMAT_ACTUALIZADO = "registroEvidenciasInstrumentos_actualizado.xlsx";
    public static final String ACTUALIZADO_REGEVIDINSTMAT = "registrosAlineacionMateriasPlan.xlsx";
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite crear el filtro para validar si el usuario autenticado es un director de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDirector(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un director.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El director no se pudo validar. (EjbRegistroEvidInstEvalMaterias.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite crear el filtro para validar si el usuario autenticado es un encarcado de dirección de área académica
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarEncargadoDireccion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
            filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como un encargado de dirección.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El encargado de dirección de área académica no se pudo validar. (EjbRegistroEvidInstEvalMaterias.validarDirector)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos de los que es responsable el director o directora
     * @param director
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativos(PersonalActivo director){
        try{
             List<AreasUniversidad> programasEducativos = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior=:areaSuperior AND a.categoria.categoria=:categoria AND a.vigente=:valor ORDER by a.nivelEducativo DESC, a.nombre ASC", AreasUniversidad.class)
                .setParameter("areaSuperior", director.getAreaOperativa().getArea())
                .setParameter("categoria", (int)9)
                .setParameter("valor", "1")
                .getResultStream()
                .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(programasEducativos, "Programas educativos vigentes del área superior correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos vigentes del área superior correspondiente. (EjbRegistroEvidInstEvalMaterias.getProgramasEducativos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de materias que integran el plan de estudio del programa educativo seleccionado
     * @param planEstudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Integer>> getMateriasPlanesEstudio(PlanEstudio planEstudio){
        try{
             List<PlanEstudio> planesEstudio = em.createQuery("SELECT p FROM PlanEstudio p WHERE p.idPlanEstudio=:plan", PlanEstudio.class)
                .setParameter("plan", planEstudio.getIdPlanEstudio())
                .getResultStream()
                .collect(Collectors.toList());
             
             List<Integer> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan IN :planes ORDER BY p.claveMateria ASC", PlanEstudioMateria.class)
                .setParameter("planes", planesEstudio)
                .getResultStream()
                .map(p->p.getIdMateria().getIdMateria())
                .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaMaterias, "Lista de materias que integran el plan de estudio del programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias que integran el plan de estudio del programa educativo seleccionado. (EjbRegistroEvidInstEvalMaterias.getMateriasPlanesEstudio)", e, null);
        }
    }
    
      /**
     * Permite activar o desactivar evaluación sugerida dependiendo la situación del plan de estudio
     * @param planEstudio
     */
    public void activarDesactivarEvaluacionSugerida(PlanEstudio planEstudio){
        try{
            List<Materia> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan", PlanEstudioMateria.class)
                .setParameter("plan", planEstudio.getIdPlanEstudio())
                .getResultStream()
                .map(p->p.getIdMateria())
                .collect(Collectors.toList());
            
            //construir la lista de dto's para mostrar en tabla
            listaMaterias.forEach(materia -> {
                List<EvaluacionSugerida> listaEvaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idMateria.idMateria=:materia ORDER BY e.unidadMateria ASC", EvaluacionSugerida.class)
                        .setParameter("materia", materia.getIdMateria())
                        .getResultStream()
                        .collect(Collectors.toList());

                listaEvaluacionesSugeridas.forEach(evaluacion -> {
                    if (planEstudio.getEstatus()) {
                        evaluacion.setActivo(true);
                        em.merge(evaluacion);
                    } else {
                        evaluacion.setActivo(false);
                        em.merge(evaluacion);
                    }
                    em.flush();
                });
            });
        }catch (Exception e){
             Logger.getLogger(EjbRegistroEvidInstEvalMaterias.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    /**
     * Permite obtener la lista de planes de estudio vigentes del programa educativo seleccionado
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PlanEstudio>> getPlanesEstudio(AreasUniversidad programaEducativo){
        try{
            List<PlanEstudio> listaPlanesEstudio = em.createQuery("SELECT p FROM PlanEstudio p WHERE p.idPe=:programa AND p.estatus=:valor ORDER BY p.anio DESC", PlanEstudio.class)
                    .setParameter("programa", programaEducativo.getArea())
                    .setParameter("valor", true)
                    .getResultStream()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaPlanesEstudio, "Planes de estudio vigentes del programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de planes de estudio viegntes del programa educativo seleccionado. (EjbRegistroEvidInstEvalMaterias.getPlanesEstudio)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación registrados del programa educativo y plan de estudio seleccionado
     * @param programaEducativo
     * @param planEstudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> buscarEvaluacionSugerida(AreasUniversidad programaEducativo, PlanEstudio planEstudio){
        try{
            List<Materia> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan AND p.idPlan.idPe=:programa", PlanEstudioMateria.class)
                .setParameter("programa", programaEducativo.getArea())
                .setParameter("plan",planEstudio.getIdPlanEstudio())
                .getResultStream()
                .map(p->p.getIdMateria())
                .distinct()
                .collect(Collectors.toList());
            
            List<DtoRegistroEvidInstEvaluacionMateria> listaDtoEvaluacionSugeridas = new ArrayList<>();
            
            //construir la lista de dto's para mostrar en tabla
            listaMaterias.forEach(materia -> {
                    
            List<EvaluacionSugerida> listaEvaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idMateria.idMateria=:materia ORDER BY e.unidadMateria ASC", EvaluacionSugerida.class)
                .setParameter("materia",materia.getIdMateria())
                .getResultStream()
                .collect(Collectors.toList());
                
            listaEvaluacionesSugeridas.forEach(evaluacion -> {
                    PlanEstudioMateria planEstudioMateria = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idMateria.idMateria=:materia", PlanEstudioMateria.class)
                    .setParameter("materia", evaluacion.getUnidadMateria().getIdMateria().getIdMateria())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);  
                    
                PeriodosEscolares periodoPK = em.find(PeriodosEscolares.class, evaluacion.getPeriodoInicio());
                String periodo = periodoPK.getMesInicio().getAbreviacion().concat(" - ").concat(periodoPK.getMesFin().getAbreviacion().concat(" ").concat(String.valueOf(periodoPK.getAnio())));    
                
                DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria = new DtoRegistroEvidInstEvaluacionMateria(evaluacion, planEstudioMateria, periodo, programaEducativo);
                listaDtoEvaluacionSugeridas.add(dtoRegistroEvidInstEvaluacionMateria);
                });
            });
             
            return ResultadoEJB.crearCorrecto(listaDtoEvaluacionSugeridas.stream().sorted(DtoRegistroEvidInstEvaluacionMateria::compareTo).collect(Collectors.toList()), "Lista de evaluaciones sugeridas registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evaluacione sugeridas registradas. (EjbRegistroEvidInstEvalMaterias.buscarEvaluacionSugerida)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación registrados del programa educativo y plan de estudio seleccionado
     * @param programaEducativo
     * @param planEstudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> buscarEvaluacionSugeridaGrado(AreasUniversidad programaEducativo, PlanEstudio planEstudio, Integer grado){
        try{
            List<Materia> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan AND p.idPlan.idPe=:programa AND p.grado=:grado", PlanEstudioMateria.class)
                .setParameter("programa", programaEducativo.getArea())
                .setParameter("plan",planEstudio.getIdPlanEstudio())
                .setParameter("grado",grado)
                .getResultStream()
                .map(p->p.getIdMateria())
                .distinct()
                .collect(Collectors.toList());
            
            List<DtoRegistroEvidInstEvaluacionMateria> listaDtoEvaluacionSugeridas = new ArrayList<>();
            
            //construir la lista de dto's para mostrar en tabla
            listaMaterias.forEach(materia -> {
                    
            List<EvaluacionSugerida> listaEvaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idMateria.idMateria=:materia ORDER BY e.unidadMateria ASC", EvaluacionSugerida.class)
                .setParameter("materia",materia.getIdMateria())
                .getResultStream()
                .collect(Collectors.toList());
                
            listaEvaluacionesSugeridas.forEach(evaluacion -> {
                    PlanEstudioMateria planEstudioMateria = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idMateria.idMateria=:materia", PlanEstudioMateria.class)
                    .setParameter("materia", evaluacion.getUnidadMateria().getIdMateria().getIdMateria())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);  
                    
                PeriodosEscolares periodoPK = em.find(PeriodosEscolares.class, evaluacion.getPeriodoInicio());
                String periodo = periodoPK.getMesInicio().getAbreviacion().concat(" - ").concat(periodoPK.getMesFin().getAbreviacion().concat(" ").concat(String.valueOf(periodoPK.getAnio())));    
                    
                DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria = new DtoRegistroEvidInstEvaluacionMateria(evaluacion, planEstudioMateria, periodo, programaEducativo);
                listaDtoEvaluacionSugeridas.add(dtoRegistroEvidInstEvaluacionMateria);
                });
            });
             
            return ResultadoEJB.crearCorrecto(listaDtoEvaluacionSugeridas.stream().sorted(DtoRegistroEvidInstEvaluacionMateria::compareTo).collect(Collectors.toList()), "Lista de evaluaciones sugeridas registradas del grado seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evaluacione sugeridas registradas del grado seleccionado. (EjbRegistroEvidInstEvalMaterias.buscarEvaluacionSugeridaGrado)", e, null);
        }
    }
    
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @param programaEducativo Materia de la que se buscará la lista de evaluación sugerida por unidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Criterio>> getCategoriasNivel(AreasUniversidad programaEducativo){
        try {
            
            List<Criterio> listaCategoriasNivel = em.createQuery("SELECT c FROM Criterio c WHERE c.nivel=:nivel ORDER BY c.criterio ASC", Criterio.class)
                    .setParameter("nivel", programaEducativo.getNivelEducativo().getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaCategoriasNivel, "Lista de categorías de evaluación del nivel educativo que corresponde.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de categorías de evaluación del nivel educativo que corresponde. (EjbRegistroEvidInstEvalMaterias.getCategoriasNivel)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de grados del plan de estudios seleccionado
     * @param planEstudio Plan de estudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Integer>> getGradosProgramaEducativo(PlanEstudio planEstudio){
        try {
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            
            List<Integer> listaGrados = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan AND p.grado NOT IN :grados ORDER BY p.grado ASC", PlanEstudioMateria.class)
                    .setParameter("plan", planEstudio.getIdPlanEstudio())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .map(p->p.getGrado())
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaGrados, "Lista de grados del plan de estudios seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grados del plan de estudios seleccionado. (EjbRegistroEvidInstEvalMaterias.getGradosProgramaEducativo)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de materias del grado seleccionado
     * @param planEstudio Plan de estudio
     * @param grado Grado seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Materia>> getMateriasGrado(PlanEstudio planEstudio, Integer grado){
        try {
            
            List<Materia> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan AND p.grado=:grado ORDER BY p.claveMateria ASC", PlanEstudioMateria.class)
                    .setParameter("plan", planEstudio.getIdPlanEstudio())
                    .setParameter("grado", grado)
                    .getResultStream()
                    .map(p->p.getIdMateria())
                    .filter(p->p.getEstatus())
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaMaterias, "Lista de materias del grado seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias del grado seleccionado. (EjbRegistroEvidInstEvalMaterias.getMateriasGrado)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de unidades qie integran la materia seleccionada
     * @param materia Materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateria>> getUnidadesMateria(Materia materia){
        try {
            
            List<UnidadMateria> listaUnidades = em.createQuery("SELECT u FROM UnidadMateria u WHERE u.idMateria.idMateria=:materia ORDER BY u.noUnidad ASC", UnidadMateria.class)
                    .setParameter("materia", materia.getIdMateria())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaUnidades, "Lista de lista de unidades qie integran la materia seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades qie integran la materia seleccionada. (EjbRegistroEvidInstEvalMaterias.getUnidadesMateria)", e, null);
        }
    }
    
    /**
     * Permite eliminar el registro de evidencia e instrumento de evaluación seleccionado
     * @param dtoRegistroEvidInstEvaluacionMateria
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEvidenciaUnidadListaEvidenciasInstrumentos(DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria){
       try{
            EvaluacionSugerida evalSug = em.find(EvaluacionSugerida.class, dtoRegistroEvidInstEvaluacionMateria.getEvaluacionSugerida().getEvaluacionSugerida());
           
            Integer delete = em.createQuery("DELETE FROM EvaluacionSugerida e WHERE e.evaluacionSugerida=:evaluacion", EvaluacionSugerida.class)
                .setParameter("evaluacion", evalSug.getEvaluacionSugerida())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Registro de evidencia e instrumento de evaluación eliminado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el registro de evidencia e instrumento de evaluación. (EjbRegistroEvidInstEvalMaterias.eliminarEvidenciaUnidadListaEvidenciasInstrumentos)", e, null);
        }
    }
    
      /**
     * Permite verificar si existe la evidencia de evaluación en las unidades seleccionadas
     * @param listaUnidades
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Integer> buscarEvidenciaListaEvidenciasInstrumentos(List<UnidadMateria> listaUnidades, EvidenciaEvaluacion evidencia){
        try{
            List<Boolean> listaValidacion = new ArrayList<>();
            
            listaUnidades.forEach(unidad -> {            
            
                EvaluacionSugerida evaluacionReg = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idUnidadMateria=:unidad and e.evidencia.evidencia=:evidencia", EvaluacionSugerida.class)
                        .setParameter("unidad", unidad.getIdUnidadMateria())
                        .setParameter("evidencia", evidencia.getEvidencia())
                        .getResultStream()
                        .findFirst().orElse(null);
                
                listaValidacion.add(evaluacionReg!=null);
            
            });
            
            Integer verdaderos = listaValidacion.stream().filter(p->p.equals(true)).collect(Collectors.toList()).size();           
           
           return ResultadoEJB.crearCorrecto(verdaderos, "Evidencia de evaluación encontrada en las unidades de la materia seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener registro de la evidencia de evaluación en las unidades de la materia seleccionada. (EjbRegistroEvidInstEvalMaterias.buscarEvidenciaListaEvidenciasInstrumentos)", e, null);
        }
    }
    
    /**
     * Permite verificar si existe la evidencia de evaluación en la unidad seleccionadas
     * @param unidadMateria
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEvidenciaUnidadListaEvidenciasInstrumentos(UnidadMateria unidadMateria, EvidenciaEvaluacion evidencia){
        try{
            EvaluacionSugerida evaluacionReg = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idUnidadMateria=:unidad and e.evidencia.evidencia=:evidencia", EvaluacionSugerida.class)
                    .setParameter("unidad", unidadMateria.getIdUnidadMateria())
                    .setParameter("evidencia", evidencia.getEvidencia())
                    .getResultStream()
                    .findFirst().orElse(null);
           
           return ResultadoEJB.crearCorrecto(evaluacionReg!=null, "Evidencia de evaluación encontrada en la unidad seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener registro de la evidencia de evaluación en la unidad seleccionada. (EjbRegistroEvidInstEvalMaterias.buscarEvidenciaListaEvidenciasInstrumentos)", e, null);
        }
    }
    
    /**
     * Permite agregar la evidencia a la lista registrada
     * @param listaUnidadesMateria
     * @param evidencia
     * @param instrumento
     * @param metaInstrumento
     * @param periodoEscolar
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<List<EvaluacionSugerida>> agregarEvidenciaListaEvidenciasInstrumentos(List<UnidadMateria> listaUnidadesMateria, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento, PeriodosEscolares periodoEscolar){
        try{
           List<EvaluacionSugerida> listaEvaluacionesSugeridas = new ArrayList<>();
           
           listaUnidadesMateria.forEach(unidad -> {
               try {
                    if(metaInstrumento!= 0){
                    EvaluacionSugerida evalSug = new EvaluacionSugerida();
                    evalSug.setUnidadMateria(unidad);
                    evalSug.setEvidencia(evidencia);
                    evalSug.setInstrumento(instrumento);
                    evalSug.setMetaInstrumento(metaInstrumento);
                    evalSug.setPeriodoInicio(periodoEscolar.getPeriodo());
                    evalSug.setActivo(true);
                    em.persist(evalSug);
                    
                    listaEvaluacionesSugeridas.add(evalSug);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbRegistroEvidInstEvalMaterias.class.getName()).log(Level.SEVERE, null, ex);
                }
               
           });
           
            return ResultadoEJB.crearCorrecto(listaEvaluacionesSugeridas, "Lista de evidencias e instrumentos registrados en las unidades de la materia seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias e instrumentos registrados en las unidades de la materia seleccionada. (EjbRegistroEvidInstEvalMaterias.agregarEvidenciaListaEvidenciasInstrumentos)", e, null);
        }
    }
    
    /**
     * Permite agregar la evidencia a la lista registrada en la unidad seleccionada
     * @param unidadMateria
     * @param evidencia
     * @param metaInstrumento
     * @param instrumento
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<EvaluacionSugerida> agregarEvidenciaUnidadListaEvidenciasInstrumentos(UnidadMateria unidadMateria, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento, PeriodosEscolares periodoEscolar){
        try{
            
            EvaluacionSugerida evalSug = new EvaluacionSugerida();
            if (metaInstrumento != 0) {
                evalSug.setUnidadMateria(unidadMateria);
                evalSug.setEvidencia(evidencia);
                evalSug.setInstrumento(instrumento);
                evalSug.setMetaInstrumento(metaInstrumento);
                evalSug.setPeriodoInicio(periodoEscolar.getPeriodo());
                evalSug.setActivo(true);
                em.persist(evalSug);
            }
             
            return ResultadoEJB.crearCorrecto(evalSug, "Evidencia e instrumento de evaluación registrada en la unidad seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la evidencia e instrumento de evaluación en la unidad seleccionada. (EjbRegistroEvidInstEvalMaterias.agregarEvidenciaUnidadListaEvidenciasInstrumentos)", e, null);
        }
    }
    
   /**
     * Permite generar la información que contendrá la plantilla 
     * @param plan Plan de estudio
     * @param programa Programa educativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getPlantillaEvidInstMateria(PlanEstudio plan, AreasUniversidad programa) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantillaAlineacionMaterias();
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaAlineacionMateriasCompleto(String.valueOf(plan.getAnio()), programa.getSiglas());
        String plantilla = rutaPlantilla.concat(EVIDINSTMAT_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EVIDINSTMAT_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("planEstudio", plan);
        beans.put("grados", getGrados(plan).getValor());
        beans.put("materiasGrados", getMateriasGradosPlanEstudio(plan).getValor());
        beans.put("unidadesMateria", getUnidadesMateriasPlanEstudio(getMateriasGradosPlanEstudio(plan).getValor()).getValor());
        beans.put("categoriasEvaluacion",getCategoriasNivel(programa).getValor());
        beans.put("evidenciasCategoria", getEvidenciasCategorias(programa).getValor());
        beans.put("instrumentosEvaluacion", ejbAsignacionIndicadoresCriterios.getInstrumentosEvaluacion().getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

        return plantillaC;
    }
    
     /**
     * Permite generar reporte de registros del plan de estudio seleccionado
     * @param plan Plan de estudio
     * @param programa Programa educativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getRegistrosPlan(PlanEstudio plan, AreasUniversidad programa) throws Throwable {
        String rutaPlantilla = "C:\\archivos\\alineacionMaterias\\reportes\\registrosEvidInstPlan.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReporteAlineacionMaterias(String.valueOf(plan.getAnio()), programa.getSiglas());

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_REGEVIDINSTMAT);
        
        Map beans = new HashMap();
        beans.put("planEstudio", plan);
        beans.put("regEvidInstPlan", buscarEvaluacionSugerida(programa, plan).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
    /**
     * Permite obtener la lista de grados
     * @param planEstudio Plan de estudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Integer>> getGrados(PlanEstudio planEstudio){
        try {
            
            List<Integer> listaGrados = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan ORDER BY p.grado ASC", PlanEstudioMateria.class)
                    .setParameter("plan", planEstudio.getIdPlanEstudio())
                    .getResultStream()
                    .map(p->p.getGrado())
                    .distinct()
                    .filter(a -> a != 6 && a != 11)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaGrados, "Lista de grados.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de grados. (EjbRegistroEvidInstEvalMaterias.getGrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de materias del plan de estudio seleccionado
     * @param planEstudio Plan de estudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PlanEstudioMateria>> getMateriasGradosPlanEstudio(PlanEstudio planEstudio){
        try {
            
            List<PlanEstudioMateria> listaMaterias = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idPlan.idPlanEstudio=:plan ORDER BY p.grado, p.idMateria.nombre ASC", PlanEstudioMateria.class)
                    .setParameter("plan", planEstudio.getIdPlanEstudio())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaMaterias, "Lista de materias del plan de estudio seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias del plan de estudio seleccionado. (EjbRegistroEvidInstEvalMaterias.getMateriasGradosPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de unidades que integran las materias del plan de estudio seleccionado
     * @param listaPlanEstudioMateria Lista de materias qie integran el plan de estudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateria>> getUnidadesMateriasPlanEstudio(List<PlanEstudioMateria> listaPlanEstudioMateria){
        try {
            
            List<Materia>  listaMaterias = listaPlanEstudioMateria.stream().map(p->p.getIdMateria()).collect(Collectors.toList());
            
            List<UnidadMateria> listaUnidadesMaterias = em.createQuery("SELECT u FROM UnidadMateria u WHERE u.idMateria IN :materias ORDER BY u.idMateria.idMateria,u.noUnidad,u.nombre ASC", UnidadMateria.class)
                    .setParameter("materias", listaMaterias)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaUnidadesMaterias, "Lista de lista de unidades que integran las materias del plan de estudio seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades que integran las materias del plan de estudio seleccionado. (EjbRegistroEvidInstEvalMaterias.getUnidadesMateriasPlanEstudio)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias de evaluación activas en la base de datos
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvidenciaEvaluacion>> getEvidenciasCategorias(AreasUniversidad programaEducativo){
        try {
            
            List<EvidenciaEvaluacion> listaEvidenciasCategoria = em.createQuery("SELECT e FROM EvidenciaEvaluacion e WHERE e.criterio.nivel=:nivel AND e.activo=:valor ORDER BY e.criterio.criterio, e.descripcion, e.descripcion ASC", EvidenciaEvaluacion.class)
                    .setParameter("nivel", programaEducativo.getNivelEducativo().getNivel())
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEvidenciasCategoria, "Lista de evidencias de evaluación.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias de evaluación. (EjbRegistroEvidInstEvalMaterias.getEvidenciasCategorias)", e, null);
        }
    }
    
    /**
     * Permite eliminar los registros del plan de estudio activo seleccionado
     * @param planEstudio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarRegistrosPlanEstudio(PlanEstudio planEstudio){
       try{
            List<PlanEstudioMateria> listaPlanMaterias = getMateriasGradosPlanEstudio(planEstudio).getValor();
            
            List<UnidadMateria> listaUnidadesMaterias = getUnidadesMateriasPlanEstudio(listaPlanMaterias).getValor();
           
            Integer delete = em.createQuery("DELETE FROM EvaluacionSugerida e WHERE e.unidadMateria IN :lista", EvaluacionSugerida.class)
                .setParameter("lista", listaUnidadesMaterias)
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se han eliminado correctamente los registro del plan de estudio seleccionado.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se eliminaron los registros del plan de estudio seleccionado. (EjbRegistroEvidInstEvalMaterias.eliminarRegistroPlanEstudio)", e, null);
        }
    }
    
}
