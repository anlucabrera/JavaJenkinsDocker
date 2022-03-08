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
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoriaPK;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausaCategoria;
import mx.edu.utxj.pye.sgi.entity.prontuario.DesercionPorEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionBajasEscolares")
public class EjbAdministracionBajasEscolares {
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
     * Permite obtener la lista de tipos de baja registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasTipo>> getListaTiposBaja(){
        try{
            
            List<BajasTipo> listaTiposBaja = em.createQuery("SELECT b FROM BajasTipo b ORDER BY b.tipoBaja ASC",  BajasTipo.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTiposBaja, "Lista de tipos de baja registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de baja registradas. (EjbAdministracionBajasEscolares.getListaTiposBaja)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de causas de baja registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasCausa>> getListaCausasBaja(){
        try{
            
            List<BajasCausa> listaCausasBaja = em.createQuery("SELECT b FROM BajasCausa b ORDER BY b.cveCausa ASC",  BajasCausa.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaCausasBaja, "Lista de causas de baja registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de causas de baja registradas. (EjbAdministracionBajasEscolares.getListaCausasBaja)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de causas de baja por categoría registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasCausaCategoria>> getListaCausasBajaCategoria(){
        try{
            
            List<BajasCausaCategoria> listaCausasCategoria = em.createQuery("SELECT b FROM BajasCausaCategoria b ORDER BY b.bajasCausaCategoriaPK.categoria, b.bajasCausa.causa ASC",  BajasCausaCategoria.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaCausasCategoria, "Lista de causas de baja por categoría registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de causas de baja por categoría registradas. (EjbAdministracionBajasEscolares.getListaCausasBajaCategoria)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de causas de baja sin categoría
     * @param causasCategoria
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BajasCausa>> getListaCausasSinCategoria(List<BajasCausaCategoria> causasCategoria){
        try{
            List<Integer> causasConCategoria = causasCategoria.stream().map(p->p.getBajasCausaCategoriaPK().getCausa()).collect(Collectors.toList());
            
            List<BajasCausa> listaCausasSinCategoria = em.createQuery("SELECT b FROM BajasCausa b WHERE b.cveCausa NOT IN :lista ORDER BY b.causa ASC",  BajasCausa.class)
                    .setParameter("lista", causasConCategoria)
                    .getResultStream()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(listaCausasSinCategoria, "Lista de causas de baja sin categoría.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de causas de baja sin categoría. (EjbAdministracionBajasEscolares.getListaCausasSinCategoria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de categorías registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getListaCategorias(){
        try{
            
            List<String> listaCategorias = em.createQuery("SELECT b FROM BajasCausaCategoria b",  BajasCausaCategoria.class)
                    .getResultStream()
                    .map(p->p.getBajasCausaCategoriaPK().getCategoria())
                    .distinct()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(listaCategorias, "Lista de categorías registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de categorías registradas. (EjbAdministracionBajasEscolares.getListaCategorias)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo tipo de baja
     * @param tipoBaja
     * @return Resultado del proceso
     */
    public ResultadoEJB<BajasTipo> guardarTipoBaja(String tipoBaja){
        try{
            Integer ultimaClave = 0;
            ultimaClave = em.createQuery("SELECT MAX(b.tipoBaja) FROM BajasTipo b", Integer.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(0);
            
            int consecutivo = ultimaClave + 1;
            
            BajasTipo bajaTipo = new BajasTipo();
            bajaTipo.setTipoBaja(consecutivo);
            bajaTipo.setDescripcion(tipoBaja);
            em.persist(bajaTipo);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(bajaTipo, "Se registró correctamente un nuevo tipo de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de baja. (EjbAdministracionBajasEscolares.guardarTipoBaja)", e, null);
        }
    }
    
     /**
     * Permite guardar una nueva causa de baja
     * @param causaBaja
     * @return Resultado del proceso
     */
    public ResultadoEJB<BajasCausa> guardarCausaBaja(String causaBaja){
        try{
            Integer ultimaClave = 0;
            ultimaClave = em.createQuery("SELECT MAX(b.cveCausa) FROM BajasCausa b", Integer.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(0);
            
            int consecutivo = ultimaClave + 1;
            
            BajasCausa bajaCausa = new BajasCausa();
            bajaCausa.setCveCausa(consecutivo);
            bajaCausa.setCausa(causaBaja);
            bajaCausa.setActiva(true);
            em.persist(bajaCausa);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(bajaCausa, "Se registró correctamente una nueva causa de baja.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente una nueva causa de baja. (EjbAdministracionBajasEscolares.guardarCausaBaja)", e, null);
        }
    }
    
    /**
     * Permite guardar la relación de una baja con una categoría
     * @param causaBaja
     * @param categoria
     * @return Resultado del proceso
     */
    public ResultadoEJB<BajasCausaCategoria> guardarCausaCategoria(BajasCausa causaBaja, String categoria){
        try{
            BajasCausaCategoriaPK causaCategoriaPK = new BajasCausaCategoriaPK(causaBaja.getCveCausa(), categoria);
            
            BajasCausaCategoria bajaCausaCategoria = new BajasCausaCategoria();
            bajaCausaCategoria.setBajasCausaCategoriaPK(causaCategoriaPK);
            bajaCausaCategoria.setBajasCausa(causaBaja);
            em.persist(bajaCausaCategoria);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(bajaCausaCategoria, "Se registró correctamente la relación de una baja con una categoría.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la relación de una baja con una categoría. (EjbAdministracionBajasEscolares.guardarCausaCategoria)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar la causa de baja seleccionada
     * @param causaBaja
     * @return Resultado del proceso
     */
    public ResultadoEJB<BajasCausa> activarDesactivarCausaBaja(BajasCausa causaBaja){
        try{
           
            Boolean valor;
            
            if(causaBaja.getActiva()){
                valor = false;
            }else{
                valor = true;
            }
            
            causaBaja.setActiva(valor);
            em.merge(causaBaja);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(causaBaja, "Se ha cambiado correctamente el status de la causa de baja seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status de la causa de baja seleccionada. (EjbAdministracionBajasEscolares.activarDesactivarCausaBaja)", e, null);
        }
    }
    
     /**
     * Permite eliminar el tipo de baja seleccionada
     * @param tipoBaja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoBaja(BajasTipo tipoBaja){
        try{
            
            Integer delete = em.createQuery("DELETE FROM BajasTipo b WHERE b.tipoBaja=:tipo", BajasTipo.class)
                .setParameter("tipo", tipoBaja.getTipoBaja())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de baja seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de baja seleccionada. (EjbAdministracionBajasEscolares.eliminarTipoBaja)", e, null);
        }
    }
    
     /**
     * Permite eliminar la causa de baja seleccionada
     * @param causaBaja
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarCausaBaja(BajasCausa causaBaja){
        try{
            
            Integer delete = em.createQuery("DELETE FROM BajasCausa b WHERE b.cveCausa=:causa", BajasCausa.class)
                .setParameter("causa", causaBaja.getCveCausa())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la causa de baja seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la causa de baja seleccionada. (EjbAdministracionBajasEscolares.eliminarCausaBaja)", e, null);
        }
    }
    
     /**
     * Permite eliminar la relación de una baja y la categoría seleccionada
     * @param causaCategoria
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarCausaCategoria(BajasCausaCategoria causaCategoria){
        try{
            
            Integer delete = em.createQuery("DELETE FROM BajasCausaCategoria b WHERE b.bajasCausaCategoriaPK=:categoria", BajasCausaCategoria.class)
                .setParameter("categoria", causaCategoria.getBajasCausaCategoriaPK())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la relación de una baja y la categoría seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la relación de una baja y la categoría seleccionada. (EjbAdministracionBajasEscolares.eliminarCausaCategoria)", e, null);
        }
    }
    
    /**
     * Permite verificar si existen registros del tipo de baja seleccionada
     * @param tipoBaja
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoBaja(BajasTipo tipoBaja){
        try{
            Long registrosBajaProntuario = em.createQuery("SELECT d FROM DesercionPorEstudiante d WHERE d.tipoBaja.tipoBaja=:tipo", DesercionPorEstudiante.class)
                    .setParameter("tipo", tipoBaja.getTipoBaja())
                    .getResultStream()
                    .count();
            
            Long registrosBajaCE = em.createQuery("SELECT b FROM Baja b WHERE b.tipoBaja=:tipo", Baja.class)
                    .setParameter("tipo", tipoBaja.getTipoBaja())
                    .getResultStream()
                    .count();
            
            Boolean valor;
            if(registrosBajaProntuario>0 || registrosBajaCE>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de baja seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de baja seleccionada.", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existen registros de la causa de baja seleccionada
     * @param causaBaja
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosCausaBaja(BajasCausa causaBaja){
        try{
            Long registrosBajaProntuario = em.createQuery("SELECT d FROM DesercionPorEstudiante d WHERE d.causaBaja.cveCausa=:causa", DesercionPorEstudiante.class)
                    .setParameter("causa", causaBaja.getCveCausa())
                    .getResultStream()
                    .count();
            
            Long registrosBajaCE = em.createQuery("SELECT b FROM Baja b WHERE b.causaBaja=:causa", Baja.class)
                    .setParameter("causa", causaBaja.getCveCausa())
                    .getResultStream()
                    .count();
            
            Long registrosBajaCategoria = em.createQuery("SELECT b FROM BajasCausaCategoria b WHERE b.bajasCausaCategoriaPK.causa=:causa", BajasCausaCategoria.class)
                    .setParameter("causa", causaBaja.getCveCausa())
                    .getResultStream()
                    .count();
            
            Boolean valor;
            if(registrosBajaProntuario>0 || registrosBajaCE>0 || registrosBajaCategoria>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros de la causa de baja seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros de la causa de baja seleccionada.", e, Boolean.TYPE);
        }
    }
    
}
