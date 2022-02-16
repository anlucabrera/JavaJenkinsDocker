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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosescolaresGeneraciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.DesercionHistorico;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistrosPeriodos;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministracionCiclosPeriodosEscolares")
public class EjbAdministracionCiclosPeriodosEscolares {
    
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
     * Permite obtener los ciclos escolares registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CiclosEscolares>> getCiclosEscolares(){
        try{
            
            List<CiclosEscolares> ciclosEscolares = em.createQuery("SELECT c FROM CiclosEscolares c ORDER BY c.ciclo DESC",  CiclosEscolares.class)
                    .getResultList();
          
            return ResultadoEJB.crearCorrecto(ciclosEscolares, "Ciclos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los ciclos escolares registrados. (EjbAdministracionCiclosPeriodosEscolares.getCiclosEscolares)", e, null);
        }
    }
    
    /**
     * Permite obtener los periodos escolares registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPeriodoEscolarFechas>> getPeriodosEscolares(){
        try{
            
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT p FROM PeriodosEscolares p ORDER BY p.periodo DESC",  PeriodosEscolares.class)
                    .getResultList();
            
            List<DtoPeriodoEscolarFechas> listaDtoPeriodosEscolaresFechas = new ArrayList<>();
            
            periodosEscolares.forEach(periodo -> {
                PeriodoEscolarFechas fechasPeriodoEscolar = em.createQuery("SELECT p FROM PeriodoEscolarFechas p WHERE p.periodosEscolares.periodo=:periodo",  PeriodoEscolarFechas.class)
                        .setParameter("periodo", periodo.getPeriodo())
                        .getResultStream().findFirst().orElse(new PeriodoEscolarFechas());
                
                DtoPeriodoEscolarFechas dtoPeriodoEscolarFechas = new DtoPeriodoEscolarFechas(periodo, fechasPeriodoEscolar);
                listaDtoPeriodosEscolaresFechas.add(dtoPeriodoEscolarFechas);
            });
          
            return ResultadoEJB.crearCorrecto(listaDtoPeriodosEscolaresFechas, "Periodos escolares registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los periodos escolares registrados. (EjbAdministracionCiclosPeriodosEscolares.getPeriodosEscolares)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de años de inicio disponibles para seleccionar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Date>> getAniosInicio(){
        try{
            List<Date> anios = new ArrayList<>();
            Date anioActual = new Date();
            anios.add(anioActual);
            
            for (int i = 1; i < 6; i += 1) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR, i);
                Date anioIncremental = c.getTime();
                anios.add(anioIncremental);
            }
            
            return ResultadoEJB.crearCorrecto(anios, "Anio actual propuesto para anio inicio del ciclo escolar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el anio actual propuesto para anio inicio del ciclo escolar. (EjbAdministracionCiclosPeriodosEscolares.getAnioInicio)", e, null);
        }
    }
    
    /**
     * Permite calcular el año fin del ciclo escolar según el año de inicio
     * @param anioInicio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Date> getAnioFin(Date anioInicio){
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(anioInicio);
            c.add(Calendar.YEAR, 1);
            Date anioFin = c.getTime();
         
            return ResultadoEJB.crearCorrecto(anioFin, "Anio fin propuesto para anio fin del ciclo escolar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el anio fin propuesto para anio fin del ciclo escolar. (EjbAdministracionCiclosPeriodosEscolares.getAnioFin)", e, null);
        }
    }
    
    /**
     * Permite obtener los meses de inicio disponibles para agregar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Meses>> getMesesInicio(){
        try{
            
            List<Meses> mesesInicio = em.createQuery("SELECT p FROM PeriodosEscolares p ORDER BY p.mesInicio.numero ASC",  PeriodosEscolares.class)
                    .getResultStream()
                    .map(p->p.getMesInicio())
                    .distinct()
                    .collect(Collectors.toList());
          
            return ResultadoEJB.crearCorrecto(mesesInicio, "Meses de inicio disponibles.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los meses de inicio disponibles. (EjbAdministracionCiclosPeriodosEscolares.getMesesInicio)", e, null);
        }
    }
    
    /**
     * Permite obtener el mes fin dependiendo de mes de inicio seleccionado
     * @param mesInicio
     * @return Resultado del proceso
     */
    public ResultadoEJB<Meses> getMesFin(Meses mesInicio){
        try{
            
           Meses mesFin = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE p.mesInicio.numero=:mes",  PeriodosEscolares.class)
                    .setParameter("mes", mesInicio.getNumero())
                    .getResultStream()
                    .map(p->p.getMesFin())
                    .distinct()
                    .findFirst().get();
          
            return ResultadoEJB.crearCorrecto(mesFin, "Mes fin del mes de inicio seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mes fin del mes de inicio seleccionado. (EjbAdministracionCiclosPeriodosEscolares.getMesFin)", e, null);
        }
    }
    
    /**
     * Permite obtener el anio actual
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> getAnioPeriodoEscolar(){
        try{
            
            Date fechaActual = new Date();
            SimpleDateFormat formatoAnio = new SimpleDateFormat("yyyy");
            String anioActual = formatoAnio.format(fechaActual);
            Integer anioPeriodoEscolar = Integer.parseInt(anioActual);
            
          
            return ResultadoEJB.crearCorrecto(anioPeriodoEscolar, "Anio actual.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el anio actual. (EjbAdministracionCiclosPeriodosEscolares.getAnioPeriodoEscolar)", e, null);
        }
    }
    
    /**
     * Permite guardar un ciclo escolar
     * @param anioInicio
     * @param anioFin
     * @return Resultado del proceso
     */
    public ResultadoEJB<CiclosEscolares> guardarCicloEscolar(Date anioInicio, Date anioFin){
        try{
            CiclosEscolares cicloEscolar = new CiclosEscolares();

            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();

            calendar1.setTime(anioInicio);
            calendar2.setTime(anioFin);
            int dateYear1 = calendar1.get(Calendar.YEAR);
            int dateYear2 = calendar2.get(Calendar.YEAR);
            
            StoredProcedureQuery query = em.createStoredProcedureQuery("prontuario.insertarCicloEscolar");
            query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
            query.setParameter(1, dateYear1);
            query.setParameter(2, dateYear2);
            query.execute();
            
            return ResultadoEJB.crearCorrecto(cicloEscolar, "Se registró correctamente el ciclo escolar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el ciclo escolar. (EjbAdministracionCiclosPeriodosEscolares.guardarCiclosEscolares)", e, null);
        }
    }
    
    /**
     * Permite guardar un periodo escolar
     * @param cicloEscolar
     * @param mesInicio
     * @param mesFin
     * @param anio
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodosEscolares> guardarPeriodoEscolar(CiclosEscolares cicloEscolar, Meses mesInicio, Meses mesFin, Integer anio){
        try{
            PeriodosEscolares periodoEscolar = new PeriodosEscolares();
            periodoEscolar.setCiclo(cicloEscolar);
            periodoEscolar.setMesInicio(mesInicio);
            periodoEscolar.setMesFin(mesFin);
            periodoEscolar.setAnio(anio);
            periodoEscolar.setTipo("Cuatrimestral");
            em.persist(periodoEscolar);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(periodoEscolar, "Se registró correctamente el periodo escolar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el periodo escolar. (EjbAdministracionCiclosPeriodosEscolares.guardarPeriodoEscolar)", e, null);
        }
    }
    
     /**
     * Permite guardar fechas del periodo escolar
     * @param periodoEscolar
     * @param fechaInicio
     * @param fechaFin
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodoEscolarFechas> guardarPeriodoEscolarFechas(PeriodosEscolares periodoEscolar, Date fechaInicio, Date fechaFin){
        try{
            
            PeriodoEscolarFechas periodoEscolarFechas = new PeriodoEscolarFechas();
            periodoEscolarFechas.setPeriodosEscolares(periodoEscolar);
            periodoEscolarFechas.setPeriodo(periodoEscolar.getPeriodo());
            periodoEscolarFechas.setInicio(fechaInicio);
            periodoEscolarFechas.setFin(fechaFin);
            em.persist(periodoEscolarFechas);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(periodoEscolarFechas, "Se registraron correctamente las fechas del periodo escolar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudieron registrar correctamente las fechas del periodo escolar. (EjbAdministracionCiclosPeriodosEscolares.guardarPeriodoEscolarFechas)", e, null);
        }
    }
     
     /**
     * Permite actualizar las fechas de inicio y fin de un periodo escolar registrado previamente
     * @param periodoEscolarFechas
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodoEscolarFechas> actualizarFechasPeriodoEscolar(PeriodoEscolarFechas periodoEscolarFechas){
        try{
            PeriodoEscolarFechas periodoFechasBD  = em.find(PeriodoEscolarFechas.class, periodoEscolarFechas.getPeriodo());
                periodoFechasBD.setInicio(periodoEscolarFechas.getInicio());
                periodoFechasBD.setFin(periodoEscolarFechas.getFin());
                em.merge(periodoFechasBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(periodoFechasBD, "Se actualizó correctamente las fechas de inicio y fin del periodo escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudieron actualizar las fechas de inicio y fin del periodo escolar seleccionado. (EjbAdministracionCiclosPeriodosEscolares.actualizarFechasPeriodoEscolar)", e, null);
        }
    }
    
    /**
     * Permite eliminar el ciclo escolar seleccionado
     * @param cicloEscolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarCicloEscolar(CiclosEscolares cicloEscolar){
        try{
            
            Integer delete = em.createQuery("DELETE FROM CiclosEscolares c WHERE c.ciclo=:ciclo", CiclosEscolares.class)
                .setParameter("ciclo", cicloEscolar.getCiclo())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el ciclo escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el ciclo escolar seleccionado. (EjbAdministracionCiclosPeriodosEscolares.eliminarCicloEscolar)", e, null);
        }
    }
    
     /**
     * Permite eliminar el periodo escolar seleccionado
     * @param periodoEscolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarPeriodoEscolar(PeriodosEscolares periodoEscolar){
        try{
            
            Integer delete = em.createQuery("DELETE FROM PeriodosEscolares p WHERE p.periodo=:periodo", PeriodosEscolares.class)
                .setParameter("periodo", periodoEscolar.getPeriodo())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el periodo escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el periodo escolar seleccionado. (EjbAdministracionCiclosPeriodosEscolares.eliminarPeriodoEscolar)", e, null);
        }
    }
    
    /**
     * Permite verificar si existen registros del ciclo escolar seleccionado
     * @param ciclosEscolares
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosCicloEscolar(CiclosEscolares ciclosEscolares){
        try{
            Long periodosCiclo = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE p.ciclo.ciclo=:ciclo", PeriodosEscolares.class)
                    .setParameter("ciclo", ciclosEscolares.getCiclo())
                    .getResultStream()
                    .count();
            
            Long generacionesCiclo = em.createQuery("SELECT c FROM CiclosescolaresGeneraciones c WHERE c.ciclo.ciclo=:ciclo", CiclosescolaresGeneraciones.class)
                    .setParameter("ciclo", ciclosEscolares.getCiclo())
                    .getResultStream()
                    .count();
            
            Long desercionProntuario = em.createQuery("SELECT d FROM DesercionHistorico d WHERE d.ciclo.ciclo=:ciclo", DesercionHistorico.class)
                    .setParameter("ciclo", ciclosEscolares.getCiclo())
                    .getResultStream()
                    .count();
                            
            Boolean valor;
            if(periodosCiclo>0 || generacionesCiclo>0 || desercionProntuario>0)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registro del ciclo escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registro del ciclo escolar seleccionado.", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existen registros del periodo escolar seleccionado
     * @param periodoEscolar
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarRegistrosPeriodoEscolar(PeriodosEscolares periodoEscolar){
        try{
            Long eventosEscolaresPeriodos = em.createQuery("SELECT e FROM EventoEscolar e WHERE e.periodo=:periodo ", EventoEscolar.class)
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .count();
            
            Long eventosRegistrosPeriodos = em.createQuery("SELECT e FROM EventosRegistrosPeriodos e WHERE e.eventosRegistrosPeriodosPK.periodoEscolar=:periodo", EventosRegistrosPeriodos.class)
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .count();
                           
            Boolean valor;
            if(eventosEscolaresPeriodos>0 || eventosRegistrosPeriodos>0 || periodoEscolar.getPeriodo()<52)
            {
                valor = Boolean.TRUE;
            }else{
                valor = Boolean.FALSE;
            }
            
           return ResultadoEJB.crearCorrecto(valor, "Resultados verificación de registro del periodo escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvieron resultados de verificación de registro del periodo escolar seleccionado.", e, Boolean.TYPE);
        }
    }
    
}
