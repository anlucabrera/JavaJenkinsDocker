/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosescolaresGeneraciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.facade.Facade;
/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionGeneraciones")
public class EjbAdministracionGeneraciones {
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
     * Permite obtener las generaciones registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneraciones(){
        try{
            
            List<Generaciones> generaciones = em.createQuery("SELECT g FROM Generaciones g ORDER BY g.generacion DESC",  Generaciones.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(generaciones, "Generaciones registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener las generaciones registradas. (EjbAdministracionGeneraciones.getGeneraciones)", e, null);
        }
    }
    
    /**
     * Permite obtener los ciclos y generaciones relacionados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CiclosescolaresGeneraciones>> getCicloGeneraciones(){
        try{
            
            List<CiclosescolaresGeneraciones> cicloGeneraciones = em.createQuery("SELECT c FROM CiclosescolaresGeneraciones c ORDER BY c.ciclo.ciclo, c.generacion.generacion DESC",  CiclosescolaresGeneraciones.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(cicloGeneraciones, "Ciclos y generaciones relacionados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudirton obtener los ciclos y generaciones relacionados. (EjbAdministracionGeneraciones.getCicloGeneraciones)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de años de inicio disponibles para seleccionar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Short>> getAniosInicio(){
        try{
            List<Short> anios = new ArrayList<>();
            
            Date fechaActual = new Date();
            SimpleDateFormat formatoAnio = new SimpleDateFormat("yyyy");
            String anioActualString = formatoAnio.format(fechaActual);
            Short anioActualShort = Short.parseShort(anioActualString);
            anios.add(anioActualShort);
            
            for (int i = 1; i < 6; i += 1) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR, i);
                Date anioIncremental = c.getTime();
                String anioIncrementalString = formatoAnio.format(anioIncremental);
                Short anioIncrementalShort = Short.parseShort(anioIncrementalString);
                anios.add(anioIncrementalShort);
            }
            
            return ResultadoEJB.crearCorrecto(anios, "Anios inicio propuestos para anio inicio de la generaición.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los anios propuestos para anio inicio de la generación. (EjbAdministracionGeneraciones.getAniosInicio)", e, null);
        }
    }
    
    /**
     * Permite calcular el año fin de la generación según el año de inicio
     * @param anioInicio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Short> getAnioFin(Short anioInicio){
        try{
            int suma = anioInicio + 2;
            Short anioFin = (short) suma;
         
            return ResultadoEJB.crearCorrecto(anioFin, "Anio propuesto para anio fin de la generación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el anio propuesto para anio fin de la generación. (EjbAdministracionGeneraciones.getAnioFin)", e, null);
        }
    }
    
    /**
     * Permite guardar una generación
     * @param anioInicio
     * @param anioFin
     * @return Resultado del proceso
     */
    public ResultadoEJB<Generaciones> guardarGeneracion(Short anioInicio, Short anioFin){
        try{
            Generaciones generacion = new Generaciones();
            generacion.setInicio(anioInicio);
            generacion.setFin(anioFin);
            em.persist(generacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(generacion, "Se registró correctamente la generación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la generación. (EjbAdministracionGeneraciones.guardarGeneracion)", e, null);
        }
    }
    
    /**
     * Permite guardar la relación de un ciclo con una generación
     * @param cicloEscolar
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<CiclosescolaresGeneraciones> guardarCicloGeneracion(CiclosEscolares cicloEscolar, Generaciones generacion){
        try{
            CiclosescolaresGeneraciones  ciclosescolaresGeneraciones = new CiclosescolaresGeneraciones();
            ciclosescolaresGeneraciones.setCiclo(cicloEscolar);
            ciclosescolaresGeneraciones.setGeneracion(generacion);
            em.persist(ciclosescolaresGeneraciones);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(ciclosescolaresGeneraciones, "Se registró correctamente la relación del ciclo y generación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente la relación del ciclo y generación. (EjbAdministracionGeneraciones.guardarCicloGeneracion)", e, null);
        }
    }
    
    /**
     * Permite eliminar la generación seleccionada
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarGeneracion(Generaciones generacion){
        try{
            
            Integer delete = em.createQuery("DELETE FROM Generaciones g WHERE g.generacion=:generacion", Generaciones.class)
                .setParameter("generacion", generacion.getGeneracion())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la generación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la generación seleccionada. (EjbAdministracionGeneraciones.eliminarGeneracion)", e, null);
        }
    }
    
     /**
     * Permite eliminar la relación del ciclo y generación
     * @param ciclosescolaresGeneraciones
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarCicloGeneracion(CiclosescolaresGeneraciones ciclosescolaresGeneraciones){
        try{
            
            Integer delete = em.createQuery("DELETE FROM CiclosescolaresGeneraciones c WHERE c.ceg=:cicloGeneracion", CiclosescolaresGeneraciones.class)
                .setParameter("cicloGeneracion", ciclosescolaresGeneraciones.getCeg())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la relación del ciclo y generación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la relación del ciclo y generación. (EjbAdministracionGeneraciones.eliminarCicloGeneracion)", e, null);
        }
    }
    
    /**
     * Permite verificar si existen registros de la generación seleccionada
     * @param generacion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosGeneraciones(Generaciones generacion){
        try{
            Long gruposGeneracion = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion", Grupo.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .count();
            
            Long ciclosGeneracion = em.createQuery("SELECT c FROM CiclosescolaresGeneraciones c WHERE c.generacion.generacion=:generacion", CiclosescolaresGeneraciones.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .count();
            
            Long expTitulacionGeneracion = em.createQuery("SELECT p FROM ProcesosGeneraciones p WHERE p.procesosGeneracionesPK.generacion=:generacion", ProcesosGeneraciones.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .count();
                            
            Boolean valor;
            if(gruposGeneracion>0 || ciclosGeneracion>0 || expTitulacionGeneracion>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registros de la generación seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registros de la generación seleccionada.", e, Boolean.TYPE);
        }
    }
    
}
