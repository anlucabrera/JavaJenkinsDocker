package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.DtoAdministracionEvaluaciones;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPeriodoEventoRegistro;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesAreas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "EjbAdministracionEvaluaciones")

public class EjbAdministracionEvaluaciones {
    @EJB EjbValidacionRol ejbValidacionRol;
    @EJB EjbPeriodoEventoRegistro ejbPeriodoEventoRegistro;

    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;


    @PostConstruct
    public void init(){em = f.getEntityManager();}

    /**
     * Valida que la persona autenticada sea personal de psicopedagogía
     * @param clave Clave de trabajador autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaPersonalActivo(Integer clave){
        return ejbValidacionRol.validarPsicopedagogia(clave);
    }
    /**
     * Valida que la persona autenticada sea personal de psicopedagogía
     * @param clave Clave de trabajador autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaPersonalFortalecimiento(Integer clave){
        return ejbValidacionRol.validarFortalecimiento(clave);
    }
    /**
     * Obtiene el periodo escolar activo
     * @return
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoEscolarActivo(){
        try{
            return ejbPeriodoEventoRegistro.getPeriodoEscolarActivo();
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo escolar activo (EjbAdministracionEvaluaciones.getPeriodoEscolarActivo)", e, PeriodosEscolares.class);
        }
    }

    public ResultadoEJB<PeriodoEscolarFechas> getPeriodoFecha(@NonNull PeriodosEscolares periodo){
        try{
            if(periodo==null){return ResultadoEJB.crearErroneo(2,new PeriodoEscolarFechas(),"El periodo no debe ser nulo");}
            PeriodoEscolarFechas periodoEscolarFechas = em.createQuery("select p from PeriodoEscolarFechas  p where p.periodo=:periodo",PeriodoEscolarFechas.class)
                    .setParameter("periodo",periodo.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            return ResultadoEJB.crearCorrecto(periodoEscolarFechas,"Periodo");
        }catch ( Exception e){
            return ResultadoEJB.crearErroneo(1, "Error(EjbAdministracionEvaluaciones.getPeriodoFecha)", e, PeriodoEscolarFechas.class);

        }
    }

    /**
     * Obtiene la lista de periodos escolares
     * @return Resultado del proceso
     */
    public  ResultadoEJB<List<PeriodosEscolares>> getPeriodosEscores(){
        try{
            List<PeriodosEscolares> periodosEscolares = em.createQuery("select p from PeriodosEscolares p order by p.periodo desc",PeriodosEscolares.class)
                    .getResultList()
                    ;
            if(periodosEscolares.isEmpty() || periodosEscolares==null){return ResultadoEJB.crearErroneo(2,periodosEscolares,"No se encontraron periodos");}
            else {return ResultadoEJB.crearCorrecto(periodosEscolares,"Periodos escolares");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos(EjbAdministracionEvaluaciones.getPeriodosEscores)", e, null);

        }
    }

    /**
     * Obtiene el periodo por evaluación
     * @param evaluacion Evaluación
     * @return Resultado del proceso
     */
    public  ResultadoEJB<PeriodosEscolares> getPeriodobyEvaluacion(@NonNull Evaluaciones evaluacion){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new PeriodosEscolares(),"La evaluación no debe ser nula");}
            PeriodosEscolares periodosEscolares = em.createQuery("select p from PeriodosEscolares p where p.periodo=:periodo",PeriodosEscolares.class)
                    .setParameter("periodo",evaluacion.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(periodosEscolares==null){return ResultadoEJB.crearErroneo(3,periodosEscolares,"No se encontró periodo");}
            else {return ResultadoEJB.crearCorrecto(periodosEscolares,"Periodo de la evaluación");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo por evaluación(EjbAdministracionEvaluaciones.getPeriodobyEvaluacion)", e, null);

        }
    }

    /***
     * Verifica que si la evaluación está activa en el periodo activo
     * De lo contrario no lo deja reapeturar
     * @param evaluacion Evaluación
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> verficaActiva(@NonNull Evaluaciones evaluacion, PeriodosEscolares periodoActivo){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,false,"La evalaución no debe ser nula");}
            Evaluaciones evaluacionA = em.createQuery("select e from Evaluaciones e where e.evaluacion=:evaluacion and e.periodo=:periodo", Evaluaciones.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("periodo", periodoActivo.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(evaluacionA==null){return ResultadoEJB.crearCorrecto(false,"La evaluación no está activa en el periodo activo");}
            else {return ResultadoEJB.crearCorrecto(true,"La evaluación está activa");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si la evaluación está activa(EjbAdministracionEvaluaciones.verficaActiva)", e, null);

        }
    }


    /**
     * Obtiene la lista de evaluaciones que estan bajo el cargo de psicopedagogía
     * @return Resultado del proceso (Lista de evaluaciones)
     */
    public  ResultadoEJB<List<EvaluacionesAreas>> getEvaluacionesPsicopedagogia(@NonNull AreasUniversidad areaPsicoP){
        try{
            if(areaPsicoP==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El área no debe ser nula");}
            List<EvaluacionesAreas> evaluaciones = em.createQuery("select e from EvaluacionesAreas e where e.areaResponsable=:area",EvaluacionesAreas.class)
                    .setParameter("area",areaPsicoP.getArea())
                    .getResultList()
                    ;
            if(evaluaciones.isEmpty() || evaluaciones==null){return ResultadoEJB.crearErroneo(3,evaluaciones,"No se encontraron evauaciones a cargo del área");}
            else {return ResultadoEJB.crearCorrecto(evaluaciones,"Evaluaciones encontradas");}
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones (EjbAdministracionEvaluaciones.getEvaluacionesPsicopedagogia", e, null); }
    }

    /**
     * Busca evaluación por tipo y periodo seleccionado
     * @param periodosEscolares Periodo seleecionado
     * @param tipo Tipo de evaluación
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> evaluacionesbyPeriodoandTipo(@NonNull PeriodosEscolares periodosEscolares, @NonNull String tipo){
        try{
            if(periodosEscolares==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"");}
            if(tipo ==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"El tipo de evaluación no debe ser nulo");}
            Evaluaciones evaluaciones = em.createQuery("select e from Evaluaciones e where e.periodo=:periodo and e.tipo=:tipo order by e.periodo desc", Evaluaciones.class)
                    .setParameter("periodo",periodosEscolares.getPeriodo())
                    .setParameter("tipo",tipo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluaciones==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"No se encontro evaluación ");}
            else {return ResultadoEJB.crearCorrecto(evaluaciones,"Evaluacion");}

        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones por periodo(EjbAdministracionEvaluaciones.getEvaluacionesbyTipoPeriodo)", e, null); }

    }

    /**
     * Empaquete la evaluación
     * @param evaluacion Evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoAdministracionEvaluaciones> packEvaluacion(@NonNull Evaluaciones evaluacion,@NonNull PeriodosEscolares periodoActivo){
        try{
            DtoAdministracionEvaluaciones dto = new DtoAdministracionEvaluaciones(new Evaluaciones(), new PeriodosEscolares(), false);
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}
            if(periodoActivo==null){return ResultadoEJB.crearErroneo(2,dto,"El periodo activo no debe ser nulo");}

            dto.setEvaluacion(evaluacion);
            //Obtiene el periodo de la evaluación
            ResultadoEJB<PeriodosEscolares> resPeriodo= getPeriodobyEvaluacion(evaluacion);
            if(resPeriodo.getCorrecto()){
                dto.setPeriodo(resPeriodo.getValor());
                ResultadoEJB<Boolean> resActiva= verficaActiva(evaluacion,periodoActivo);
                if(resActiva.getCorrecto()){dto.setActiva(resActiva.getValor());
                    return ResultadoEJB.crearCorrecto(dto ,"Evaluación empaquetada"); }
                else {return ResultadoEJB.crearErroneo(4,dto,"Error al veridicar si la evaluación está activa");}
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obtener el periodo de la evaluación");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la evaluación(EjbAdministracionEvaluaciones.packEvaluacion)", e, null);
        }
    }

    /**
     * Obtiene la lista de evaluaciones segun la lista de evaluaciones a su cargo por periodo seleccionado
     * @param evaluacionesAreas  Lista de evaluación a su cargo
     * @param periodoSeleccionado Periodo seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAdministracionEvaluaciones>> getEvaluacionesbyTipoPeriodo(@NonNull List<EvaluacionesAreas> evaluacionesAreas,@NonNull PeriodosEscolares periodoSeleccionado,@NonNull PeriodosEscolares periodoActivo){
        try{
            if(evaluacionesAreas==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"Las evaluaciones no deben ser nulas");}
            if(periodoSeleccionado==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El periodo no debe ser nulo");}
            if(periodoActivo==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El periodo no debe ser nulo");}

            List<DtoAdministracionEvaluaciones> evaluaciones = new ArrayList<>();
            evaluacionesAreas.forEach(e->{
                ResultadoEJB<Evaluaciones> resEv=evaluacionesbyPeriodoandTipo(periodoSeleccionado,e.getTipoEvaluacion());
                if(resEv.getCorrecto()){
                    //Empaqueta la evaluación
                    ResultadoEJB<DtoAdministracionEvaluaciones> resPack= packEvaluacion(resEv.getValor(),periodoActivo);
                    if(resPack.getCorrecto()){
                        evaluaciones.add(resPack.getValor());
                    }else {return;}
                }else return;
            });
            return ResultadoEJB.crearCorrecto(evaluaciones,"Lista de evaluaciones");
        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones (EjbAdministracionEvaluaciones.getEvaluacionesbyTipo)", e, null); }

    }

    /**
     * Obtiene la lista de evaluaciones a cargo del área según el periodo seleccionado
     * @param area Area del personal autenticado
     * @param periodoSeleleccionado Periodo Escolar seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAdministracionEvaluaciones>> getEvaluacionesbyArea(@NonNull AreasUniversidad area,@NonNull PeriodosEscolares periodoSeleleccionado,@NonNull PeriodosEscolares periodoActivo){
        try {
            //System.out.println("Area" + area);
            if(area==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El área no debe ser nula");}
            if(periodoSeleleccionado==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El periodo no debe ser nulo");}
            //Obtiene la lista de evaluaciones a su cargo
            ResultadoEJB<List<EvaluacionesAreas>> resEvaluaciones= getEvaluacionesPsicopedagogia(area);
            if(resEvaluaciones.getCorrecto()){
                //Obtiene las evaluaciones por periodo
                ResultadoEJB<List<DtoAdministracionEvaluaciones>> resEv= getEvaluacionesbyTipoPeriodo(resEvaluaciones.getValor(),periodoSeleleccionado,periodoActivo);
                if(resEv.getCorrecto()){
                    return ResultadoEJB.crearCorrecto(resEv.getValor(),"Evaluaciones");
                }else {return ResultadoEJB.crearErroneo(5,new ArrayList<>(),"Error al obtener las evaluaciones por periodo");}
            }else {
                return ResultadoEJB.crearErroneo(4,new ArrayList<>(),"Error al obtener las evaluaciones del área");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la lista de evaluaciones a cargo (EjbAdministracionEvaluaciones.packEvaluacion)", e, null);

        }
    }

    /**
     * Actualiza/crea una evaluación según la operacion
     * @param evaluacion Evaluacion
     * @param operacion Operacion
     * @return Resultado del proceso
     */

    public ResultadoEJB<Evaluaciones> operacionesEvaluacion(@NonNull Evaluaciones evaluacion, @NonNull Operacion operacion){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"La evaluación no debe ser nula");}
            if(operacion==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"La operación no debe ser nula");}

            switch (operacion){
                case PERSISTIR:
                    f.setEntityClass(Evaluaciones.class);
                    em.persist(evaluacion);
                    em.flush();
                case ACTUALIZAR:
                    f.setEntityClass(Evaluaciones.class);
                    em.merge(evaluacion);
                    em.flush();
            }
            return ResultadoEJB.crearCorrecto(evaluacion,"Evaluación actualizada");


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error en operaciones de la evaluacion (EjbAdministracionEvaluaciones.operacionesEvaluacion", e, null);

        }
    }

    /**
     * Verifica que no exista evaluacion del mismo tipo en el periodo activo
     * @param tipo Tipo de la evaluacion
     * @param periodosEscolares Periodo escolar activo
     * @return Resultado del proceso
     */

    public  ResultadoEJB<Boolean> verficarEvaluacion (@NonNull String tipo, @NonNull PeriodosEscolares periodosEscolares){
        try{
            if(tipo==null){return   ResultadoEJB.crearErroneo(2,false,"El tipo no debe ser nulo");}
            if(periodosEscolares==null){ return ResultadoEJB.crearErroneo(3, false,"El periodo no debe ser nulo");}
            Evaluaciones evaluacion= em.createQuery("select e from Evaluaciones e where e.tipo=:tipo and  e.periodo=:periodo", Evaluaciones.class)
                    .setParameter("periodo",periodosEscolares.getPeriodo())
                    .setParameter("tipo",tipo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluacion==null){return ResultadoEJB.crearCorrecto(false,"No exite evaluación activa tipo "+ tipo + " en el periodo activo");}
            else {return ResultadoEJB.crearErroneo(3,true,"Ya exite evaluación una evaluación del tipo "+ tipo+ " en el perido activo");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error en al verficar si existe evaluación en el periodo(EjbAdministracionEvaluaciones.verficaActiva", e, null);

        }
    }

    /**
     * Obtiene la paertura para la visualización del seguimiento del evaluaciones por tipo
     * @param tipo Tipo de Evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaVisualizacionEncuestas> getAperturabyTipo(@NonNull String tipo){
        try{
            if(tipo==null){return ResultadoEJB.crearErroneo(2,new AperturaVisualizacionEncuestas(),"El tipo no debe ser nulo");}
            AperturaVisualizacionEncuestas aperturaVisualizacionEncuestas = em.createQuery("select a from AperturaVisualizacionEncuestas  a where a.encuesta=:tipo", AperturaVisualizacionEncuestas.class)
                    .setParameter("tipo",tipo)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(aperturaVisualizacionEncuestas==null){return ResultadoEJB.crearErroneo(4,new AperturaVisualizacionEncuestas(),"No hay apertura");}
            else {return ResultadoEJB.crearCorrecto(aperturaVisualizacionEncuestas,"Apertura visualización encontrada");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error (EjbAdministracionEvaluaciones.getAperturabyTipo", e, null);
        }
    }

    /**
     * Permite actualizar la paertura de la visualizacion del seguimiento de evaluaciones
     * @param apertura Apertura
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaVisualizacionEncuestas> updateAperturaSeguimiento(@NonNull AperturaVisualizacionEncuestas apertura){
        try{
            if(apertura==null){return ResultadoEJB.crearErroneo(2,new AperturaVisualizacionEncuestas(),"La apertura no debe ser nula");}
            f.setEntityClass(AperturaVisualizacionEncuestas.class);
            em.merge(apertura);
            em.flush();
            return ResultadoEJB.crearCorrecto(apertura,"Apertura actualizada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error (EjbAdministracionEvaluaciones.updateAperturaSeguimiento", e, null);
        }
    }



}
