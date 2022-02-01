/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionTipoAspiranteEstudiante")
public class EjbAdministracionTipoAspiranteEstudiante {
    
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
     * Permite obtener la lista de tipo de aspirante registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoAspirante>> getListaTipoAspirante(){
        try{
            
            List<TipoAspirante> listaTipoAspirante = em.createQuery("SELECT t FROM TipoAspirante t ORDER BY t.idTipoAspirante ASC",  TipoAspirante.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTipoAspirante, "Lista de tipo de aspirante registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de aspirante registrados. (EjbAdministracionTipoAspiranteEstudiante.getListaTipoAspirante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipo de estudiante registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoEstudiante>> getListaTipoEstudiante(){
        try{
            
            List<TipoEstudiante> listaTipoEstudiante = em.createQuery("SELECT t FROM TipoEstudiante t ORDER BY t.idTipoEstudiante ASC",  TipoEstudiante.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTipoEstudiante, "Lista de tipo de estudiante registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de estudiante registrados. (EjbAdministracionTipoAspiranteEstudiante.getListaTipoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo tipo de aspirante
     * @param nuevoAspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoAspirante> guardarTipoAspirante(String nuevoAspirante){
        try{
            
            TipoAspirante tipoAspirante = new TipoAspirante();
            tipoAspirante.setDescripcion(nuevoAspirante);
            em.persist(tipoAspirante);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(tipoAspirante, "Se registró correctamente un nuevo tipo de aspirante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de aspirante. (EjbAdministracionTipoAspiranteEstudiante.guardarTipoAspirante)", e, null);
        }
    }
    
    /**
     * Permite eliminar el tipo de aspirante seleccionado
     * @param tipoAspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoAspirante(TipoAspirante tipoAspirante){
        try{
            
            Integer delete = em.createQuery("DELETE FROM TipoAspirante t WHERE t.idTipoAspirante=:aspirante", TipoAspirante.class)
                .setParameter("aspirante", tipoAspirante.getIdTipoAspirante())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de aspirante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de aspirante seleccionado. (EjbAdministracionTipoAspiranteEstudiante.eliminarTipoAspirante)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar el tipo de estudiante seleccionado
     * @param tipoEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoEstudiante> activarDesactivarTipoEstudiante(TipoEstudiante tipoEstudiante){
        try{
           
            Boolean valor;
            
            if(tipoEstudiante.getActivo()){
                valor = false;
            }else{
                valor = true;
            }
            
            tipoEstudiante.setActivo(valor);
            em.merge(tipoEstudiante);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(tipoEstudiante, "Se ha cambiado correctamente el status del tipo de estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del tipo de estudiante seleccionado. (EjbAdministracionTipoAspiranteEstudiante.activarDesactivarTipoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo tipo de estudiante
     * @param nuevoEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoEstudiante> guardarTipoEstudiante(String nuevoEstudiante){
        try{
            
            TipoEstudiante tipoEstudiante = new TipoEstudiante();
            tipoEstudiante.setDescripcion(nuevoEstudiante);
            tipoEstudiante.setActivo(true);
            em.persist(tipoEstudiante);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(tipoEstudiante, "Se registró correctamente un nuevo tipo de estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de estudiante. (EjbAdministracionTipoAspiranteEstudiante.guardarTipoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite eliminar el tipo de estudiante seleccionado
     * @param tipoEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoEstudiante(TipoEstudiante tipoEstudiante){
        try{
            
            Integer delete = em.createQuery("DELETE FROM TipoEstudiante t WHERE t.idTipoEstudiante=:estudiante", TipoEstudiante.class)
                .setParameter("estudiante", tipoEstudiante.getIdTipoEstudiante())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de estudiante seleccionado. (EjbAdministracionTipoAspiranteEstudiante.eliminarTipoEstudiante)", e, null);
        }
    }
    
     /**
     * Permite verificar si existen registros del tipo de aspirante seleccionado
     * @param tipoAspirante
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoAspirante(TipoAspirante tipoAspirante){
        try{
            Long registrosAspirante = em.createQuery("SELECT a FROM Aspirante a WHERE a.tipoAspirante.idTipoAspirante=:aspirante", Aspirante.class)
                    .setParameter("aspirante", tipoAspirante.getIdTipoAspirante())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosAspirante>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de aspirante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de aspirante seleccionado.", e, Boolean.TYPE);
        }
    }
    
     /**
     * Permite verificar si existen registros del tipo de estudiante seleccionado
     * @param tipoEstudiante
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoEstudiante(TipoEstudiante tipoEstudiante){
        try{
            Long registrosEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.tipoEstudiante.idTipoEstudiante=:estudiante", Estudiante.class)
                    .setParameter("estudiante", tipoEstudiante.getIdTipoEstudiante())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosEstudiante>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de estudiante seleccionado.", e, Boolean.TYPE);
        }
    }
    
}
