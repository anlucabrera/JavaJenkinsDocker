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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionCatalogosAdmision")
public class EjbAdministracionCatalogosAdmision {
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
     * Permite obtener la lista de tipo de discapacidad registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoDiscapacidad>> getListaTipoDiscapacidad(){
        try{
            
            List<TipoDiscapacidad> listaTipoDiscapacidad = em.createQuery("SELECT t FROM TipoDiscapacidad t ORDER BY t.idTipoDiscapacidad ASC",  TipoDiscapacidad.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTipoDiscapacidad, "Lista de tipo de discapacidad registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de discapacidad registrados. (EjbAdministracionCatalogosAdmision.getListaTipoDiscapacidad)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de lenguas indígenas registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<LenguaIndigena>> getListaLenguaIndigena(){
        try{
            
            List<LenguaIndigena> listaTipoLenguaIndigena = em.createQuery("SELECT l FROM LenguaIndigena l ORDER BY l.idLenguaIndigena ASC",  LenguaIndigena.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTipoLenguaIndigena, "Lista de tipo de lenguas indígenas registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de lenguas indígenas registradas. (EjbAdministracionCatalogosAdmision.getListaLenguaIndigena)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipo de sangre registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<TipoSangre>> getListaTipoSangre(){
        try{
            
            List<TipoSangre> listaTipoSangre = em.createQuery("SELECT t FROM TipoSangre t ORDER BY t.idTipoSangre ASC",  TipoSangre.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTipoSangre, "Lista de tipo de sangre registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de sangre registrados. (EjbAdministracionCatalogosAdmision.getListaTipoSangre)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de medios de difusión registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<MedioDifusion>> getListaMediosDifusion(){
        try{
            
            List<MedioDifusion> listaMediosDifusion = em.createQuery("SELECT m FROM MedioDifusion m ORDER BY m.idMedioDifusion ASC",  MedioDifusion.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaMediosDifusion, "Lista de tipo de medios de difusión registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipo de medios de difusión registrados. (EjbAdministracionCatalogosAdmision.getListaMediosDifusion)", e, null);
        }
    }
        
    /**
     * Permite guardar un nuevo tipo de discapacidad
     * @param nuevaDiscapacidadNombre
     * @param nuevaDiscapacidadDescripcion
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoDiscapacidad> guardarTipoDiscapacidad(String nuevaDiscapacidadNombre, String nuevaDiscapacidadDescripcion){
        try{
            short ultimaClave = 0;
            ultimaClave = em.createQuery("SELECT MAX(t.idTipoDiscapacidad) FROM TipoDiscapacidad t", Short.class)
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            
            int consecutivo = ultimaClave + 1;
            
            TipoDiscapacidad tipoDiscapacidad = new TipoDiscapacidad();
            tipoDiscapacidad.setIdTipoDiscapacidad((short)consecutivo);
            tipoDiscapacidad.setNombre(nuevaDiscapacidadNombre);
            tipoDiscapacidad.setDescripcion(nuevaDiscapacidadDescripcion);
            tipoDiscapacidad.setEstatus(true);
            em.persist(tipoDiscapacidad);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(tipoDiscapacidad, "Se registró correctamente un nuevo tipo de discapacidad.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de discapacidad. (EjbAdministracionCatalogosAdmision.guardarTipoDiscapacidad)", e, null);
        }
    }
    
    /**
     * Permite guardar una nueva lengua indígena
     * @param nuevaLenguaIndigena
     * @return Resultado del proceso
     */
    public ResultadoEJB<LenguaIndigena> guardarLenguaIndigena(String nuevaLenguaIndigena){
        try{
            
            LenguaIndigena lenguaIndigena = new LenguaIndigena();
            lenguaIndigena.setNombre(nuevaLenguaIndigena);
            em.persist(lenguaIndigena);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(lenguaIndigena, "Se registró correctamente un nueva lengua indígena.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nueva lengua indígena. (EjbAdministracionCatalogosAdmision.guardarLenguaIndigena)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo tipo de sangre
     * @param nuevoTipoSangre
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoSangre> guardarTipoSangre(String nuevoTipoSangre){
        try{
            short ultimaClave = 0;
            ultimaClave = em.createQuery("SELECT MAX(t.idTipoSangre) FROM TipoSangre t", Short.class)
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            
            int consecutivo = ultimaClave + 1;
            TipoSangre tipoSangre = new TipoSangre();
            tipoSangre.setIdTipoSangre((short)consecutivo);
            tipoSangre.setNombre(nuevoTipoSangre);
            em.persist(tipoSangre);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(tipoSangre, "Se registró correctamente un nuevo tipo de sangre.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de sangre. (EjbAdministracionCatalogosAdmision.guardarTipoSangre)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo medio de difusión
     * @param nuevoMedioDifusion
     * @return Resultado del proceso
     */
    public ResultadoEJB<MedioDifusion> guardarMedioDifusion(String nuevoMedioDifusion){
        try{
            
            MedioDifusion medioDifusion = new MedioDifusion();
            medioDifusion.setDescripcion(nuevoMedioDifusion);
            medioDifusion.setEstatus(true);
            em.persist(medioDifusion);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(medioDifusion, "Se registró correctamente un nuevo medio de difusión.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo medio de difusión. (EjbAdministracionCatalogosAdmision.guardarMedioDifusion)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar el tipo de discapacidad seleccionado
     * @param tipoDiscapacidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<TipoDiscapacidad> activarDesactivarTipoDiscapacidad(TipoDiscapacidad tipoDiscapacidad){
        try{
           
            Boolean valor;
            
            if(tipoDiscapacidad.getEstatus()){
                valor = false;
            }else{
                valor = true;
            }
            
            tipoDiscapacidad.setEstatus(valor);
            em.merge(tipoDiscapacidad);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(tipoDiscapacidad, "Se ha cambiado correctamente el status del tipo de discapacidad seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del tipo de discapacidad seleccionado. (EjbAdministracionCatalogosAdmision.activarDesactivarTipoDiscapacidad)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar el medio de difusión seleccionado
     * @param medioDifusion
     * @return Resultado del proceso
     */
    public ResultadoEJB<MedioDifusion> activarDesactivarMedioDifusion(MedioDifusion medioDifusion){
        try{
           
            Boolean valor;
            
            if(medioDifusion.getEstatus()){
                valor = false;
            }else{
                valor = true;
            }
            
            medioDifusion.setEstatus(valor);
            em.merge(medioDifusion);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(medioDifusion, "Se ha cambiado correctamente el status del medio de difusión seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del medio de difusión seleccionado. (EjbAdministracionCatalogosAdmision.activarDesactivarMedioDifusion)", e, null);
        }
    }
    
    /**
     * Permite eliminar el tipo de discapacidad seleccionado
     * @param tipoDiscapacidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoDiscapacidad(TipoDiscapacidad tipoDiscapacidad){
        try{
            
            Integer delete = em.createQuery("DELETE FROM TipoDiscapacidad t WHERE t.idTipoDiscapacidad=:discapacidad", TipoDiscapacidad.class)
                .setParameter("discapacidad", tipoDiscapacidad.getIdTipoDiscapacidad())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de discapacidad seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de discapacidad seleccionado. (EjbAdministracionCatalogosAdmision.eliminarTipoDiscapacidad)", e, null);
        }
    }
    
    /**
     * Permite eliminar la lengua indígena seleccionada
     * @param lenguaIndigena
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarLenguaIndigena(LenguaIndigena lenguaIndigena){
        try{
            
            Integer delete = em.createQuery("DELETE FROM LenguaIndigena l WHERE l.idLenguaIndigena=:lengua", LenguaIndigena.class)
                .setParameter("lengua", lenguaIndigena.getIdLenguaIndigena())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la lengua indígena seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la lengua indígena seleccionada. (EjbAdministracionCatalogosAdmision.eliminarLenguaIndigena)", e, null);
        }
    }
    
    /**
     * Permite eliminar el tipo de sangre seleccionado
     * @param tipoSangre
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoSangre(TipoSangre tipoSangre){
        try{
            
            Integer delete = em.createQuery("DELETE FROM TipoSangre t WHERE t.idTipoSangre=:sangre", TipoSangre.class)
                .setParameter("sangre", tipoSangre.getIdTipoSangre())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de sangre seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de sangre seleccionado. (EjbAdministracionCatalogosAdmision.eliminarTipoSangre)", e, null);
        }
    }
    
     /**
     * Permite eliminar el medio de difusión seleccionado
     * @param medioDifusion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarMedioDifusion(MedioDifusion medioDifusion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM MedioDifusion m WHERE m.idMedioDifusion=:medio", MedioDifusion.class)
                .setParameter("medio", medioDifusion.getIdMedioDifusion())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el medio de difusión seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el medio de difusión seleccionado. (EjbAdministracionCatalogosAdmision.eliminarMedioDifusion)", e, null);
        }
    }
    
    /**
     * Permite verificar si existen registros del tipo de discapacidad seleccionada
     * @param tipoDiscapacidad
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoDiscapacidad(TipoDiscapacidad tipoDiscapacidad){
        try{
            Long registrosDatosMedicos = em.createQuery("SELECT m FROM DatosMedicos m WHERE m.cveDiscapacidad.idTipoDiscapacidad=:discapacidad", DatosMedicos.class)
                    .setParameter("discapacidad", tipoDiscapacidad.getIdTipoDiscapacidad())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosDatosMedicos>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de discapacidad seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de discapacidad seleccionada.", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existen registros de la lengua indígena seleccionada
     * @param lenguaIndigena
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosLenguaIndigena(LenguaIndigena lenguaIndigena){
        try{
            Long registrosEncuestaAspirante = em.createQuery("SELECT e FROM EncuestaAspirante e WHERE e.r2tipoLenguaIndigena.idLenguaIndigena=:lengua", EncuestaAspirante.class)
                    .setParameter("lengua", lenguaIndigena.getIdLenguaIndigena())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosEncuestaAspirante>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros de la lengua indígena seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de la lengua indígena seleccionada.", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existen registros del tipo de sangre seleccionada
     * @param tipoSangre
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoSangre(TipoSangre tipoSangre){
        try{
            Long registrosDatosMedicos = em.createQuery("SELECT m FROM DatosMedicos m WHERE m.cveTipoSangre.idTipoSangre=:sangre", DatosMedicos.class)
                    .setParameter("sangre", tipoSangre.getIdTipoSangre())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosDatosMedicos>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de sangre seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de sangre seleccionada.", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existen registros del medio de difusión seleccionado
     * @param medioDifusion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosMedioDifusion(MedioDifusion medioDifusion){
        try{
            Long registrosEncuestaAspirante = em.createQuery("SELECT e FROM EncuestaAspirante e WHERE e.r15medioImpacto.idMedioDifusion=:medio", EncuestaAspirante.class)
                    .setParameter("medio", medioDifusion.getIdMedioDifusion())
                    .getResultStream()
                    .count();
                  
            Boolean valor;
            if(registrosEncuestaAspirante>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del medio de difusión seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación del medio de difusión seleccionadoa.", e, Boolean.TYPE);
        }
    }
    
    
}
