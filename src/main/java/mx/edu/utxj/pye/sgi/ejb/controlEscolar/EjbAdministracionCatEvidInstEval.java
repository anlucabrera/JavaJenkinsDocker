/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionSugerida;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvidenciaEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.InstrumentoEvaluacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionCatEvidInstEval")
public class EjbAdministracionCatEvidInstEval {
    
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /**
     * Permite obtener la lista de niveles educativos de las categorías de evaluación registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesEducativos(){
        try{
            List<ProgramasEducativosNiveles> listaNivelesEducativos = new ArrayList<>();
            
            List<String> listaNivelesCriterio = em.createQuery("SELECT c FROM Criterio c ORDER BY c.criterio ASC",  Criterio.class)
                    .getResultStream()
                    .map(p->p.getNivel())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaNivelesCriterio.forEach(nivelCriterio -> {
                ProgramasEducativosNiveles nivelEducativo = em.find(ProgramasEducativosNiveles.class, nivelCriterio);
                listaNivelesEducativos.add(nivelEducativo);
            });
           
            return ResultadoEJB.crearCorrecto(listaNivelesEducativos, "Lista de niveles educativos de las categorías de evaluación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos de las categorías de evaluación. (EjbAdministracionCatEvidInstEval.getNivelesEducativos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias de evaluación por categoría del nivel educativo seleccionado
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvidenciaEvaluacion>> getCategoriasEvidenciaNivel(ProgramasEducativosNiveles nivelEducativo){
        try{
            
            List<EvidenciaEvaluacion> listaEvidenciasCategoriaNivel = em.createQuery("SELECT e FROM EvidenciaEvaluacion e WHERE e.criterio.nivel=:nivel ORDER BY e.criterio.criterio, e.descripcion ASC",  EvidenciaEvaluacion.class)
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
          
           
            return ResultadoEJB.crearCorrecto(listaEvidenciasCategoriaNivel, "Lista de evidencias de evaluación por categoría del nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias de evaluación por categoría del nivel educativo seleccionado. (EjbAdministracionCatEvidInstEval.getCategoriasEvidenciaNivel)", e, null);
        }
    }
    
     /**
     * Permite activar o desactivar un la evidencia de la categoría de evaluación seleccionada
     * @param evidenciaEvaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvidenciaEvaluacion> activarDesactivarEvidenciaCategoria(EvidenciaEvaluacion evidenciaEvaluacion){
        try{
           
            Boolean valor;
            
            if(evidenciaEvaluacion.getActivo()){
                valor = false;
            }else{
                valor = true;
            }
            
            evidenciaEvaluacion.setActivo(valor);
            em.merge(evidenciaEvaluacion);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(evidenciaEvaluacion, "Se ha cambiado correctamente el status de la evidencia de la categoría seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status de la evidencia de la categoría seleccionada. (EjbAdministracionCatEvidInstEval.activarDesactivarEvidenciaCategoria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de categorías de evaluación del nivel educativo seleccionado
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Criterio>> getCategoriasNivel(ProgramasEducativosNiveles nivelEducativo){
        try{
            
            List<Criterio> listaCategoriasNivel = em.createQuery("SELECT c FROM Criterio c WHERE c.nivel=:nivel ORDER BY c.criterio",  Criterio.class)
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
          
           
            return ResultadoEJB.crearCorrecto(listaCategoriasNivel, "Lista de categorías de evaluación del nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de categorías de evaluación del nivel educativo seleccionado. (EjbAdministracionCatEvidInstEval.getCategoriasNivel)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias de evaluación disponibles para agregar a la categoría seleccionada
     * @param nivelEducativo
     * @param listaCategoriasEvidencias
     * @param categoria
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getEvidenciasDisponiblesAgregar(ProgramasEducativosNiveles nivelEducativo, List<EvidenciaEvaluacion> listaCategoriasEvidencias, Criterio categoria){
        try{
            List<String> evidenciasCategoriaRegistradas = listaCategoriasEvidencias.stream().filter(p->p.getCriterio().equals(categoria)).map(p->p.getDescripcion()).collect(Collectors.toList());
            
            List<String> listaEvidenciasDisponibles = em.createQuery("SELECT e FROM EvidenciaEvaluacion e WHERE e.descripcion NOT IN :lista ORDER BY e.descripcion",  EvidenciaEvaluacion.class)
                    .setParameter("lista", evidenciasCategoriaRegistradas)
                    .getResultStream()
                    .map(p->p.getDescripcion())
                    .distinct()
                    .collect(Collectors.toList());
          
           
            return ResultadoEJB.crearCorrecto(listaEvidenciasDisponibles, "Lista de evidencias de evaluación disponibles para agregar a la categoría seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias de evaluación disponibles para agregar a la categoría seleccionada. (EjbAdministracionCatEvidInstEval.getEvidenciasDisponiblesAgregar)", e, null);
        }
    }
    
    /**
     * Permite guardar la evidencia a la categoría educativa seleccionada
     * @param categoria
     * @param evidencia
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvidenciaEvaluacion> guardarEvidenciaCategoria(Criterio categoria, String evidencia){
        try{
            
            EvidenciaEvaluacion evidenciaEvaluacion = new EvidenciaEvaluacion();
            evidenciaEvaluacion.setDescripcion(evidencia);
            evidenciaEvaluacion.setCriterio(categoria);
            evidenciaEvaluacion.setActivo(true);
            em.persist(evidenciaEvaluacion);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(evidenciaEvaluacion, "Se registró correctamente la evidencia a la categoría educativa seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la evidencia a la categoría educativa seleccionada. (EjbAdministracionCatEvidInstEval.guardarEvidenciaCategoria)", e, null);
        }
    }
    
    /**
     * Permite eliminar la evidencia de la categoría de evaluación seleccionada
     * @param evidenciaEvaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEvidenciaCategoria(EvidenciaEvaluacion evidenciaEvaluacion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM EvidenciaEvaluacion e WHERE e.evidencia=:evidencia", EvidenciaEvaluacion.class)
                .setParameter("evidencia", evidenciaEvaluacion.getEvidencia())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el evento escolar del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el evento escolar del periodo seleccionado. (EjbAdministracionCatEvidInstEval.eliminarEvidenciaCategoria)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de instrumentos de evaluación registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<InstrumentoEvaluacion>> getInstrumentosEvaluacion(){
        try{
            
            List<InstrumentoEvaluacion> listaInstrumentosEvaluacion = em.createQuery("SELECT i FROM InstrumentoEvaluacion i ORDER BY i.descripcion",  InstrumentoEvaluacion.class)
                    .getResultStream()
                    .collect(Collectors.toList());
          
           
            return ResultadoEJB.crearCorrecto(listaInstrumentosEvaluacion, "Lista de evidencias de instrumentos de evaluación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de instrumentos de evaluación registrados. (EjbAdministracionCatEvidInstEval.getInstrumentosEvaluacion)", e, null);
        }
    }
    
    /**
     * Permite validar o desactivar un instrumento de evaluación
     * @param instrumentoEvaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<InstrumentoEvaluacion> activarDesactivarInstrumentoEvaluacion(InstrumentoEvaluacion instrumentoEvaluacion){
        try{
           
            Boolean valor;
            
            if(instrumentoEvaluacion.getActivo()){
                valor = false;
            }else{
                valor = true;
            }
            
            instrumentoEvaluacion.setActivo(valor);
            em.merge(instrumentoEvaluacion);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(instrumentoEvaluacion, "Se ha cambiado correctamente el status del instrumento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del instrumento seleccionado. (EjbAdministracionCatEvidInstEval.activarDesactivarInstrumentoEvaluacion)", e, null);
        }
    }
    
    /**
     * Permite guardar el instrumento de evaluación
     * @param instrumento
     * @return Resultado del proceso
     */
    public ResultadoEJB<InstrumentoEvaluacion> guardarInstrumentoEvaluacion(String instrumento){
        try{
            
            InstrumentoEvaluacion instrumentoEvaluacion = new InstrumentoEvaluacion();
            instrumentoEvaluacion.setDescripcion(instrumento);
            instrumentoEvaluacion.setActivo(true);
            em.persist(instrumentoEvaluacion);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(instrumentoEvaluacion, "Se registró correctamente el instrumento de evaluación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el instrumento de evaluación. (EjbAdministracionCatEvidInstEval.guardarInstrumentoEvaluacion)", e, null);
        }
    }
    
    /**
     * Permite eliminar el instrumento de evaluación seleccionado
     * @param instrumentoEvaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarInstrumentoEvaluacion(InstrumentoEvaluacion instrumentoEvaluacion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM InstrumentoEvaluacion i WHERE i.instrumento=:instrumento", InstrumentoEvaluacion.class)
                .setParameter("instrumento", instrumentoEvaluacion.getInstrumento())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el instrumento de evaluación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el instrumento de evaluación seleccionado. (EjbAdministracionCatEvidInstEval.eliminarInstrumentoEvaluacion)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe registro de la evidencia de evaluación seleccionada
     * @param evidenciaEvaluacion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarPlaneacionEvidenciaEvaluacion(EvidenciaEvaluacion evidenciaEvaluacion){
        try{
            Long configuracionesEvidencia = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.evidencia.evidencia=:evidencia", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                    .setParameter("evidencia", evidenciaEvaluacion.getEvidencia())
                    .getResultStream()
                    .count();
            
            Long evaluacionSugeridaEvidencia = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.evidencia.evidencia=:evidencia", EvaluacionSugerida.class)
                    .setParameter("evidencia", evidenciaEvaluacion.getEvidencia())
                    .getResultStream()
                    .count();
                            
            Boolean valor;
            if(configuracionesEvidencia>0 || evaluacionSugeridaEvidencia>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registro de la evidencia de evaluación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registro de la evidencia de evaluación seleccionada.", e, Boolean.TYPE);
        }
    }
    
     /**
     * Permite verificar si existe registro del instrumento de evaluación seleccionado
     * @param instrumentoEvaluacion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarPlaneacionInstrumentoEvaluacion(InstrumentoEvaluacion instrumentoEvaluacion){
        try{
            Long configuracionesInstrumento = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.instrumento.instrumento=:instrumento", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                    .setParameter("instrumento", instrumentoEvaluacion.getInstrumento())
                    .getResultStream()
                    .count();
            
            Long evaluacionSugeridaInstrumento = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.instrumento.instrumento=:instrumento", EvaluacionSugerida.class)
                    .setParameter("instrumento", instrumentoEvaluacion.getInstrumento())
                    .getResultStream()
                    .count();
                            
            Boolean valor;
            if(configuracionesInstrumento>0 || evaluacionSugeridaInstrumento>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registro del instrumento de evaluación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registro del instrumento de evaluación seleccionado.", e, Boolean.TYPE);
        }
    }
    
}
