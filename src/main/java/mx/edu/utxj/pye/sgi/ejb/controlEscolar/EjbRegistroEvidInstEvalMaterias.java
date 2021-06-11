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
import java.util.Objects;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
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
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Integer>> getMateriasPlanesEstudio(AreasUniversidad programaEducativo){
        try{
             List<PlanEstudio> planesEstudio = em.createQuery("SELECT p FROM PlanEstudio p WHERE p.idPe=:programa", PlanEstudio.class)
                .setParameter("programa", programaEducativo.getArea())
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
     * Permite obtener la lista de periodos escolares en las tiene información registrada el programa educativo seleccionado
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEscolares(AreasUniversidad programaEducativo){
        try{
            List<Integer> listaMaterias = getMateriasPlanesEstudio(programaEducativo).getValor();
             
            List<Integer> claves = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idMateria IN :materias ORDER BY e.unidadMateria ASC", EvaluacionSugerida.class)
                .setParameter("materias",listaMaterias)
                .getResultStream()
                .map(p->p.getPeriodoInicio())
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, ejbEventoEscolar.getPeriodoActual().getPeriodo());
            }
            List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                    .setParameter("periodos", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(periodos, "Periodos escolares con información registrada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con información registrada. (EjbRegistroEvidInstEvalMaterias.getPeriodosEscolares)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de periodos escolares en las tiene información registrada el programa educativo seleccionado
     * @param programaEducativo
     * @param periodoEscolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> buscarEvaluacionSugerida(AreasUniversidad programaEducativo, PeriodosEscolares periodoEscolar){
        try{
            List<Integer> listaMaterias = getMateriasPlanesEstudio(programaEducativo).getValor();
            
            List<DtoRegistroEvidInstEvaluacionMateria> listaDtoEvaluacionSugeridas = new ArrayList<>();
            
            //construir la lista de dto's para mostrar en tabla
            listaMaterias.forEach(materia -> {
                    
            List<EvaluacionSugerida> listaEvaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idMateria.idMateria=:materias AND e.periodoInicio=:periodo ORDER BY e.unidadMateria ASC", EvaluacionSugerida.class)
                .setParameter("materias",materia)
                .setParameter("periodo",periodoEscolar.getPeriodo())
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
                    
                DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria = new DtoRegistroEvidInstEvaluacionMateria(evaluacion, planEstudioMateria, periodo);
                listaDtoEvaluacionSugeridas.add(dtoRegistroEvidInstEvaluacionMateria);
                });
            });
             
            return ResultadoEJB.crearCorrecto(listaDtoEvaluacionSugeridas.stream().sorted(DtoRegistroEvidInstEvaluacionMateria::compareTo).collect(Collectors.toList()), "Lista de evaluaciones sugeridas registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evaluacione sugeridas registradas. (EjbRegistroEvidInstEvalMaterias.buscarEvaluacionSugerida)", e, null);
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
     * Permite verificar si existe la evidencia en la lista registrada
     * @param listaEvidenciasEvidenciasInstrumentos
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEvidenciaListaEvidenciasInstrumentos(List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasEvidenciasInstrumentos, EvidenciaEvaluacion evidencia){
        try{
           List<DtoRegistroEvidInstEvaluacionMateria> listaCoincidencias = listaEvidenciasEvidenciasInstrumentos.stream().filter(p-> Objects.equals(p.getEvaluacionSugerida().getEvidencia().getEvidencia(), evidencia.getEvidencia())).collect(Collectors.toList());
           
           return ResultadoEJB.crearCorrecto(listaCoincidencias.isEmpty(), "Resultado búsqueda de evidencia en el listado de evidencias sugeridas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existe la evidencia en la lista registrada en la unidad seleccionada
     * @param unidadMateria
     * @param listaEvidenciasEvidenciasInstrumentos
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEvidenciaUnidadListaEvidenciasInstrumentos(UnidadMateria unidadMateria, List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasEvidenciasInstrumentos, EvidenciaEvaluacion evidencia){
        try{
           List<DtoRegistroEvidInstEvaluacionMateria> listaCoincidencias = listaEvidenciasEvidenciasInstrumentos.stream().filter(p-> Objects.equals(p.getEvaluacionSugerida().getUnidadMateria().getIdUnidadMateria(), unidadMateria.getIdUnidadMateria()) && p.getEvaluacionSugerida().getEvidencia().getEvidencia() == evidencia.getEvidencia()).collect(Collectors.toList());
           
           return ResultadoEJB.crearCorrecto(listaCoincidencias.isEmpty(), "Resultado búsqueda de evidencia en la unidad seleccionada en el listado de evidencias sugeridas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite agregar la evidencia a la lista registrada
     * @param listaEvidenciasEvidenciasInstrumentos
     * @param evidencia
     * @param instrumento
     * @param metaInstrumento
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> agregarEvidenciaListaEvidenciasInstrumentos(List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasEvidenciasInstrumentos, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento){
        try{
           List<UnidadMateria> listaUnidadesMateria = listaEvidenciasEvidenciasInstrumentos.stream().map(p->p.getEvaluacionSugerida().getUnidadMateria()).distinct().collect(Collectors.toList());
           Integer periodoEscolar = listaEvidenciasEvidenciasInstrumentos.stream().map(p->p.getEvaluacionSugerida().getPeriodoInicio()).distinct().findFirst().orElse(null);
           
           listaUnidadesMateria.forEach(unidad -> {
               try {
                    if(metaInstrumento!= 0){
                    EvaluacionSugerida evalSug = new EvaluacionSugerida();
                    evalSug.setUnidadMateria(unidad);
                    evalSug.setEvidencia(evidencia);
                    evalSug.setInstrumento(instrumento);
                    evalSug.setMetaInstrumento(metaInstrumento);
                    evalSug.setPeriodoInicio(periodoEscolar);
                    evalSug.setActivo(true);
                    em.persist(evalSug);
                    
                    PlanEstudioMateria planEstudioMateria = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idMateria.idMateria=:materia", PlanEstudioMateria.class)
                        .setParameter("materia",unidad.getIdMateria().getIdMateria())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                     
                    PeriodosEscolares periodoPK = em.find(PeriodosEscolares.class, evalSug.getPeriodoInicio());
                    String periodo = periodoPK.getMesInicio().getAbreviacion().concat(" - ").concat(periodoPK.getMesFin().getAbreviacion().concat(" ").concat(String.valueOf(periodoPK.getAnio())));    
                    
                    DtoRegistroEvidInstEvaluacionMateria dto = new DtoRegistroEvidInstEvaluacionMateria(evalSug, planEstudioMateria, periodo);
                    listaEvidenciasEvidenciasInstrumentos.add(dto);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbRegistroEvidInstEvalMaterias.class.getName()).log(Level.SEVERE, null, ex);
                }
               
           });
           
            return ResultadoEJB.crearCorrecto(listaEvidenciasEvidenciasInstrumentos.stream().sorted(DtoRegistroEvidInstEvaluacionMateria::compareTo).collect(Collectors.toList()), "Lista de evidencias registradas con nueva evidencia agregada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias registradas con nueva evidencia agregada. (EjbRegistroEvidInstEvalMaterias.agregarEvidenciaListaSugerida)", e, null);
        }
    }
    
    /**
     * Permite agregar la evidencia a la lista registrada en la unidad seleccionada
     * @param listaEvidenciasEvidenciasInstrumentos
     * @param unidadMateria
     * @param evidencia
     * @param metaInstrumento
     * @param instrumento
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<List<DtoRegistroEvidInstEvaluacionMateria>> agregarEvidenciaUnidadListaEvidenciasInstrumentos(List<DtoRegistroEvidInstEvaluacionMateria> listaEvidenciasEvidenciasInstrumentos, UnidadMateria unidadMateria, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento){
        try{
           Integer periodoEscolar = listaEvidenciasEvidenciasInstrumentos.stream().map(p->p.getEvaluacionSugerida().getPeriodoInicio()).distinct().findFirst().orElse(null);
           
            if (metaInstrumento != 0) {
                EvaluacionSugerida evalSug = new EvaluacionSugerida();
                evalSug.setUnidadMateria(unidadMateria);
                evalSug.setEvidencia(evidencia);
                evalSug.setInstrumento(instrumento);
                evalSug.setMetaInstrumento(metaInstrumento);
                evalSug.setPeriodoInicio(periodoEscolar);
                evalSug.setActivo(true);
                em.persist(evalSug);

                PlanEstudioMateria planEstudioMateria = em.createQuery("SELECT p FROM PlanEstudioMateria p WHERE p.idMateria.idMateria=:materia", PlanEstudioMateria.class)
                        .setParameter("materia", unidadMateria.getIdMateria().getIdMateria())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                
                PeriodosEscolares periodoPK = em.find(PeriodosEscolares.class, evalSug.getPeriodoInicio());
                String periodo = periodoPK.getMesInicio().getAbreviacion().concat(" - ").concat(periodoPK.getMesFin().getAbreviacion().concat(" ").concat(String.valueOf(periodoPK.getAnio())));    

                DtoRegistroEvidInstEvaluacionMateria dto = new DtoRegistroEvidInstEvaluacionMateria(evalSug, planEstudioMateria, periodo);
                listaEvidenciasEvidenciasInstrumentos.add(dto);
            }
             
            return ResultadoEJB.crearCorrecto(listaEvidenciasEvidenciasInstrumentos.stream().sorted(DtoRegistroEvidInstEvaluacionMateria::compareTo).collect(Collectors.toList()), "Lista de evidencias registradas con nueva evidencia agregada a la unidad seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias registradas con nueva evidencia agregada a la unidad seleccionada. (EjbRegistroEvidInstEvalMaterias.agregarEvidenciaListaSugerida)", e, null);
        }
    }
    
    /* Método para decarga de plantilla */
    
    public String getPlantillaEvidInstMateria(PlanEstudio plan, AreasUniversidad programa) throws Throwable {
        String rutaPlantilla = ejbCarga.crearDirectorioPlantillaAlineacionMaterias();
        String rutaPlantillaC = ejbCarga.crearDirectorioPlantillaAlineacionMateriasCompleto(String.valueOf(plan.getAnio()), programa.getSiglas());
        String plantilla = rutaPlantilla.concat(EVIDINSTMAT_PLANTILLA);
        String plantillaC = rutaPlantillaC.concat(EVIDINSTMAT_ACTUALIZADO);
        Map beans = new HashMap();
        beans.put("grados", getGrados(plan).getValor());
        beans.put("materiasGrados", getMateriasGradosPlanEstudio(plan).getValor());
        beans.put("unidadesMateria", getUnidadesMateriasPlanEstudio(getMateriasGradosPlanEstudio(plan).getValor()).getValor());
        beans.put("categoriasEvaluacion",getCategoriasNivel(programa).getValor());
        beans.put("evidenciasCategoria", getEvidenciasCategorias().getValor());
        beans.put("instrumentosEvaluacion", ejbAsignacionIndicadoresCriterios.getInstrumentosEvaluacion().getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(plantilla, beans, plantillaC);

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
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvidenciaEvaluacion>> getEvidenciasCategorias(){
        try {
            
            List<EvidenciaEvaluacion> listaEvidenciasCategoria = em.createQuery("SELECT e FROM EvidenciaEvaluacion e WHERE e.activo=:valor ORDER BY e.criterio.criterio, e.descripcion ASC", EvidenciaEvaluacion.class)
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEvidenciasCategoria, "Lista de evidencias de evaluación.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias de evaluación. (EjbRegistroEvidInstEvalMaterias.getEvidenciasCategorias)", e, null);
        }
    }
    
}
