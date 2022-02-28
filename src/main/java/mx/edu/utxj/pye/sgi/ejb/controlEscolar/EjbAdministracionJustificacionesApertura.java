/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.JustificacionPermisosExtemporaneos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaGrupal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaEstudiante;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionJustificacionesApertura")
public class EjbAdministracionJustificacionesApertura {
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
     * Permite obtener las justificaciones de aperturas extemporáneas registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<JustificacionPermisosExtemporaneos>> getJustificacionPermisosExtemporaneos(){
        try{
            
            List<JustificacionPermisosExtemporaneos> justificaciones = em.createQuery("SELECT j FROM JustificacionPermisosExtemporaneos j ORDER BY j.justificacion ASC",  JustificacionPermisosExtemporaneos.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(justificaciones, "Justificaciones de aperturas extemporáneas registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener las justificaciones de aperturas extemporáneas registradas. (EjbAdministracionJustificacionesApertura.getJustificacionPermisosExtemporaneos)", e, null);
        }
    }
    
    /**
     * Permite guardar una justificación de permiso de apertura nueva
     * @param descripcion
     * @return Resultado del proceso
     */
    public ResultadoEJB<JustificacionPermisosExtemporaneos> guardarJustificacion(String descripcion){
        try{
            JustificacionPermisosExtemporaneos justificacion = new JustificacionPermisosExtemporaneos();
            justificacion.setDescripcion(descripcion);
            justificacion.setActivo(true);
            em.persist(justificacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(justificacion, "Se registró correctamente la generación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la generación. (EjbAdministracionJustificacionesApertura.guardarJustificacion)", e, null);
        }
    }
    
    /**
     * Permite eliminar la justificación seleccionada
     * @param justificacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarJustificacion(JustificacionPermisosExtemporaneos justificacion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM JustificacionPermisosExtemporaneos j WHERE j.justificacion=:justificacion", JustificacionPermisosExtemporaneos.class)
                .setParameter("justificacion", justificacion.getJustificacion())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la justificación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la justificación seleccionada. (EjbAdministracionJustificacionesApertura.eliminarJustificacion)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar la justificación seleccionada
     * @param justificacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<JustificacionPermisosExtemporaneos> activarDesactivarJustificacion(JustificacionPermisosExtemporaneos justificacion){
        try{
           
            Boolean valor;
            
            if(justificacion.getActivo()){
                valor = false;
            }else{
                valor = true;
            }
            
            justificacion.setActivo(valor);
            em.merge(justificacion);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(justificacion, "Se ha cambiado correctamente el status de la justificación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status de la justificación seleccionada. (EjbAdministracionJustificacionesApertura.activarDesactivarJustificacion)", e, null);
        }
    }
    
     /**
     * Permite verificar si existen registros de la justificación seleccionada
     * @param justificacion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosJustificaciones(JustificacionPermisosExtemporaneos justificacion){
        try{
            Long permisosGrupales = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.justificacionPermiso.justificacion=:justificacion", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("justificacion", justificacion.getJustificacion())
                    .getResultStream()
                    .count();
            
            Long permisosPorEstudiante = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.justificacionPermiso.justificacion=:justificacion", PermisosCapturaExtemporaneaEstudiante.class)
                    .setParameter("justificacion", justificacion.getJustificacion())
                    .getResultStream()
                    .count();
            
              
            Boolean valor;
            if(permisosGrupales>0 || permisosPorEstudiante>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros de la justificación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros de la justificación seleccionada.", e, Boolean.TYPE);
        }
    }
}
