/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.Becas;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionBecas")
public class EjbAdministracionBecas {
    
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
     * Permite validar si el usuario es jefe del departamento de servicios estudiantiles
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarServiciosEstudiantiles(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            
            if(p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getAreaOperativa() == 11 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como jefe de servicios estudiantiles.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbAdministracionBecas.validarServiciosEstudiantiles)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de tipos de baja registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<BecaTipos>> getListaTiposBeca(){
        try{
            
            List<BecaTipos> listaTiposBeca = em.createQuery("SELECT b FROM BecaTipos b ORDER BY b.becaTipo ASC",  BecaTipos.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(listaTiposBeca, "Lista de tipos de beca registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de beca registradas. (EjbAdministracionBecas.getListaTiposBaja)", e, null);
        }
    }
    
    /**
     * Permite guardar un nuevo tipo de beca
     * @param tipoBeca
     * @return Resultado del proceso
     */
    public ResultadoEJB<BecaTipos> guardarTipoBeca(String tipoBeca){
        try{
            BecaTipos becaTipo = new BecaTipos();
            becaTipo.setNombre(tipoBeca);
            becaTipo.setActiva(true);
            em.persist(becaTipo);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(becaTipo, "Se registró correctamente un nuevo tipo de beca.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente un nuevo tipo de beca. (EjbAdministracionBecas.guardarTipoBeca)", e, null);
        }
    }
    
    /**
     * Permite activar o desactivar el tipo de beca seleccionada
     * @param tipoBeca
     * @return Resultado del proceso
     */
    public ResultadoEJB<BecaTipos> activarDesactivarTipoBeca(BecaTipos tipoBeca){
        try{
           
            Boolean valor;
            
            if(tipoBeca.getActiva()){
                valor = false;
            }else{
                valor = true;
            }
            
            tipoBeca.setActiva(valor);
            em.merge(tipoBeca);
            f.flush();
            
            
            return ResultadoEJB.crearCorrecto(tipoBeca, "Se ha cambiado correctamente el status del tipo de beca seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar el status del tipo de beca seleccionada. (EjbAdministracionBajasEscolares.activarDesactivarTipoBeca)", e, null);
        }
    }
    
     /**
     * Permite eliminar el tipo de beca seleccionada
     * @param tipoBeca
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarTipoBeca(BecaTipos tipoBeca){
        try{
            
            Integer delete = em.createQuery("DELETE FROM BecaTipos b WHERE b.becaTipo=:tipo", BecaTipos.class)
                .setParameter("tipo", tipoBeca.getBecaTipo())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el tipo de beca seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el tipo de beca seleccionada. (EjbAdministracionBecas.eliminarTipoBeca)", e, null);
        }
    }
   
    /**
     * Permite verificar si existen registros del tipo de beca seleccionada
     * @param tipoBeca
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosTipoBeca(BecaTipos tipoBeca){
        try{
            Long registrosBecaProntuario = em.createQuery("SELECT b FROM Becas b WHERE b.becaTipo.becaTipo=:tipo", Becas.class)
                    .setParameter("tipo", tipoBeca.getBecaTipo())
                    .getResultStream()
                    .count();
            
            Long registrosBecaPYE2= em.createQuery("SELECT b FROM BecasPeriodosEscolares b WHERE b.beca=:tipo", BecasPeriodosEscolares.class)
                    .setParameter("tipo", tipoBeca.getBecaTipo())
                    .getResultStream()
                    .count();
            
            Boolean valor;
            if(registrosBecaProntuario>0 || registrosBecaPYE2>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros del tipo de beca seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros del tipo de beca seleccionada.", e, Boolean.TYPE);
        }
    }
    
}
