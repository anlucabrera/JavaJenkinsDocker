package mx.edu.utxj.pye.sgi.ejb.controlEscolar;


import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInformeIntegralDocente;
import mx.edu.utxj.pye.sgi.ejb.evaluaciones.EjbEvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionTutor;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless(name = "EjbInformeIntegralDocente")
public class EjbInformeIntegralDocente {

    @EJB EjbPropiedades ep;
    @EJB EjbEvaluacionParesAcademicos ejbPares;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    @EJB Facade2 f2;

    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();        em2 = f2.getEntityManager();
    }

    /**
     * Valida que el usuario autenticado tenga acceso al módulo
     * Secretario/a Académico
     * Director/a de área académica
     * Personal de FDA
     * Personal Docente
     * @param clave Clava del usuario autenticado
     * @return Resultado del proceso
     */

    public ResultadoEJB<PersonalActivo> validarAcceso(Integer clave){
        try{
            if(clave ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            else {
                PersonalActivo personalActivo = ejbPacker.packPersonalActivo(clave);
                ResultadoEJB<PersonalActivo> resSA= validarSA(personalActivo);
                if(resSA.getCorrecto()==true){
                    //System.out.println("SA" );
                    return ResultadoEJB.crearCorrecto(resSA.getValor(),"Validado como Secretario/a Académica");}
                else {
                    //System.out.println("----" );
                    ResultadoEJB<PersonalActivo> resfda = validarFDA(personalActivo);
                    if(resfda.getCorrecto()==true){
                        // System.out.println("FDA" +resfda.getValor() );
                        return ResultadoEJB.crearCorrecto(resfda.getValor(),"Validado como personal de FDA");}
                    else {
                        ResultadoEJB<PersonalActivo> resDir= validarDirAc(personalActivo);
                        if(resDir.getCorrecto()==true){
                            //System.out.println("Director " + resDir.getValor() );
                            return ResultadoEJB.crearCorrecto(resDir.getValor(),"Validado como Director/a de Planeacion");
                        }
                        else {
                            ResultadoEJB<PersonalActivo> resDocente = validarDocente(personalActivo);
                            if(resDocente.getCorrecto()==true){
                                //System.out.println("Docente" +resDocente.getValor() );
                                return ResultadoEJB.crearCorrecto(resDocente.getValor(),"Validado como Docente");
                            }
                            else {
                                ResultadoEJB<PersonalActivo> resIdiomas = validarIdiomas(personalActivo);
                                if(resIdiomas.getCorrecto()){
                                    return ResultadoEJB.crearCorrecto(resIdiomas.getValor(),"Validado como coordinador/a de Idiomas");
                                }else {return ResultadoEJB.crearErroneo(3,resIdiomas.getValor(),"No tiene acceso");}

                            }
                        }

                    }
                }

            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarAcceso)", e, null);

        }
    }



    /**
     * Valida que el usuario logueado sea Secretario Acedemico
     * @param personalActivo Clave del personal
     * @return Resultado del proceso
     *
     */

    public ResultadoEJB<PersonalActivo> validarSA (PersonalActivo personalActivo){
        try {
            //System.out.println("Sec Ac--->" );
            //PersonalActivo personalActivo = new PersonalActivo();
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getAreaOperativa().getArea()== 2 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==38 & personalActivo.getAreaSuperior().getArea()==1){
                //Es SA
                //System.out.println("Sec Ac" );
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como Secretario /a Académico");
            }
            else {
                //System.out.println("No es SA" );
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es SA");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarSA)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea director de algún area académica
     * @param personalActivo Clave del personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarDirAc (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getAreaSuperior().getArea()==2 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==18 || personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==48){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como director de algun area académica");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Director de Area");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarDirAc)", e, null);

        }
    }

    /**
     * Valida que el usuario logueado sea de FDA
     * @param personalActivo Clave del personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarFDA (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getPersonal().getAreaOperativa()==12 ){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como personal de FDA");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es de FDA");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarFDA)", e, null);

        }
    }

    /**
     * Valida que la persona logueada sea personal Docente
     * @param personalActivo
     * @return
     */
    public ResultadoEJB<PersonalActivo> validarDocente(PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getPersonal().getActividad().getActividad()==3 ){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como docente");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es Docente");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarDocente)", e, null);

        }
    }
    /**
     * Valida que el usuario logueado sea COrdinador de Idiomas
     * @param personalActivo Clave del personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarIdiomas (PersonalActivo personalActivo){
        try {
            if(personalActivo ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            if(personalActivo.getPersonal().getAreaOperativa()==23 & personalActivo.getPersonal().getCategoriaOperativa().getCategoria()==14 ){
                //Es Director de Planeación
                return ResultadoEJB.crearCorrecto(personalActivo,"Usuario autenticado como coordinador de Idiomas");
            }
            else {
                return ResultadoEJB.crearErroneo(2,personalActivo,"No es coordinador de Idiomas");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarFDA)", e, null);

        }
    }

    /**
     * Obtiene las áreas académicas
     * @return Lista de áreas academicas
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasAcademicas (){
        try{
            return ejbPares.getAreasAcademicas();
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obetener las áreas académicas. (EjbInformeIntegralDocente.getAreasAcademicas)", e, null);

        }
    }

    /**
     * Obtiene el área por clave
     * @param area Clave del área
     * @return Resultado del proceso
     */
    public ResultadoEJB<AreasUniversidad> getAreabyClave (@NonNull Short area){
        try{
            if(area ==null){return ResultadoEJB.crearErroneo(2,new AreasUniversidad(),"La clave del área no debe ser nula");}
            AreasUniversidad areasUniversidad = em.find(AreasUniversidad.class,area);
            if(areasUniversidad!=null){return ResultadoEJB.crearCorrecto(areasUniversidad,"Area");}
            else {return ResultadoEJB.crearErroneo(2,areasUniversidad,"No se encontró el área");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al el área(EjbInformeIntegralDocente.getAreabyClave)", e, null);
        }
    }




    /**
     * Obtiene la lista del personal según el área seleccionada
     * @param area Area Academica adsicrita/ o seleccionada
     *
     * @return Resultado del proceso (Lista de personal)
     */
    public ResultadoEJB<List<Personal>> getDocentesbyArea(@NonNull AreasUniversidad area){
        try{
            if(area==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"");}
            List<Personal> docentes = new ArrayList<>();
            docentes =  em.createQuery("select p from Personal  p where p.actividad.actividad=:actividad and p.areaSuperior =:area and p.status<>'B' order by   p.clave asc ", Personal.class)
                    .setParameter("actividad",3)
                    .setParameter("area",area.getArea()).getResultList()
            ;
            if(docentes!=null){return ResultadoEJB.crearCorrecto(docentes,"Lista de personal"); }
            else {return ResultadoEJB.crearErroneo(3,docentes,"No existen docentes en el área seleccionada");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el listado del personal (EjbInformeIntegralDocente.getPersonalbyTipoUsuario)", e, null);

        }
    }

    /**
     * Obtiene la lista de periodos Escolares en los que ha sido eveluado el docente seleccionado
     * @param docente Docente seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEvaluacionPersonal (@NonNull Personal docente){
        try{
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionParesAcademicos> resultados = em.createQuery("select d from EvaluacionParesAcademicos d where d.evaluacionParesAcademicosPK.evaluado =:clave order by d.evaluacionParesAcademicosPK.evaluacion desc ", EvaluacionParesAcademicos.class)
                    .setParameter("clave",docente.getClave())
                    .getResultList()
                    ;
            if(resultados.isEmpty()|| resultados ==null){ return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El docente seleccionado aún no ha sido evaluado"); }
            else {
                // Evaluaciones
                List<Evaluaciones> d =new ArrayList<>();
                resultados.stream().forEach(r->{
                    Evaluaciones des = new Evaluaciones();
                    des = em.find(Evaluaciones.class,r.getEvaluacionParesAcademicosPK().getEvaluacion());
                    d.add(des);
                });
                //Periodos Escolares
                List<PeriodosEscolares> periodosEscolares = new ArrayList<>();
                d.stream().distinct().forEach(e->{
                    PeriodosEscolares periodo = new PeriodosEscolares();
                    periodo = em.find(PeriodosEscolares.class,e.getPeriodo());
                    periodosEscolares.add(periodo);
                });

                return ResultadoEJB.crearCorrecto(periodosEscolares,"Periodos Escolares");

            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el listado del personal (EjbInformeIntegralDocente.getPeriodosEvaluacionPersonal)", e, null);

        }
    }

    /**
     * Obtiene los periodos en los que ha sido evaluado el docente
     * Se obtiene según los periodos que ha sido evaluado en la evaluación al Desempeño
     * @param docente Docente seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEvaluacionPersonalbyDesem (@NonNull Personal docente){
        try{
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<DesempenioEvaluacionResultados> resultados = em.createQuery("select d from DesempenioEvaluacionResultados d where d.desempenioEvaluacionResultadosPK.evaluado =:clave order by d.desempenioEvaluacionResultadosPK.evaluacion desc ", DesempenioEvaluacionResultados.class)
                    .setParameter("clave",docente.getClave())
                    .getResultList()
                    ;
            if(resultados.isEmpty()|| resultados ==null){ return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El docente seleccionado aún no ha sido evaluado"); }
            else {
                // Evaluaciones
                List<DesempenioEvaluaciones> d =new ArrayList<>();
                resultados.stream().forEach(r->{
                    DesempenioEvaluaciones des = new DesempenioEvaluaciones();
                    des = em.find(DesempenioEvaluaciones.class,r.getDesempenioEvaluacionResultadosPK().getEvaluacion());
                    d.add(des);
                });
                //Periodos Escolares
                List<PeriodosEscolares> periodosEscolares = new ArrayList<>();
                d.stream().distinct().forEach(e->{
                    PeriodosEscolares periodo = new PeriodosEscolares();
                    periodo = em.find(PeriodosEscolares.class,e.getPeriodo());
                    periodosEscolares.add(periodo);
                });
                return ResultadoEJB.crearCorrecto(periodosEscolares,"Periodos Escolares");

            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el listado del personal (EjbInformeIntegralDocente.getPeriodosEvaluacionPersonal)", e, null);

        }
    }

    /**
     * Obtiene la evaluacion del Desempeño  por periodo de Evaluación
     * @param periodosEscolares Periodo Escoalar seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<DesempenioEvaluaciones> getEvaluacionDesbyPeriodo(@NonNull PeriodosEscolares periodosEscolares){
        try{
            if(periodosEscolares ==null){return ResultadoEJB.crearErroneo(2,new DesempenioEvaluaciones(),"El periodo no debe ser nulo");}
            DesempenioEvaluaciones evaluacion = em.createQuery("select d from DesempenioEvaluaciones  d where d.periodo =:periodo", DesempenioEvaluaciones.class)
                    .setParameter("periodo",periodosEscolares.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,evaluacion,"No se encontró la evaluación");}
            else {return ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el listado del personal (EjbInformeIntegralDocente.getEvaluacionDesbyPeriodo)", e, null);
        }
    }

    /**
     * Obtiene la evaluación segun el tipo y periodo
     * @param periodo Periodo seleccionado
     * @param tipoEvaluacion Tipo evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getEvalaucionbyTipoPeriodo (@NonNull PeriodosEscolares periodo, @NonNull EvaluacionesTipo tipoEvaluacion){
        try{
            if(periodo==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"El periodo no debe ser nulo");}
            if(tipoEvaluacion==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"El periodo no debe ser nulo");}
            Evaluaciones evaluacion = em.createQuery("select e from Evaluaciones  e where e.tipo=:tipo and e.periodo=:periodo", Evaluaciones.class)
                    .setParameter("tipo",tipoEvaluacion.getLabel())
                    .setParameter("periodo",periodo.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"No existe la evaluación buscada en el periodo seleccionado");}
            else {return ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener la evaluacion por tipo y periodo(EjbInformeIntegralDocente.getEvalaucionbyTipoPeriodo)", e, null); }
    }
    /**
     * Obtiene la evaluación a tutor segun el periodo
     * @param periodo Periodo seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getEvalaucionTutorbyPeriodo (@NonNull PeriodosEscolares periodo){
        try{
            if(periodo==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"El periodo no debe ser nulo");}
            Evaluaciones evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.periodo=:periodo AND (e.tipo=:tipo2 or e.tipo=:tipo or e.tipo=:tipo3) ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo2", EvaluacionesTipo.TUTOR_2.getLabel())
                    .setParameter("tipo", EvaluacionesTipo.TUTOR.getLabel())
                    .setParameter("tipo3", EvaluacionesTipo.TUTOR_1.getLabel())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            //System.out.println("EjbInformeIntegralDocente.getEvalaucionTutorbyPeriodo "+ evaluacion);
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"No existe la evaluación buscada en el periodo seleccionado");}
            else {return ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener la evaluacion por tipo y periodo(EjbInformeIntegralDocente.getEvalaucionbyTipoPeriodo)", e, null); }
    }

    /**
     * Obtiene la evaluación a docente aplicada por periodo
     * @param periodo
     * @return
     */
    public ResultadoEJB<Evaluaciones> getEvalaucionDocentebyPeriodo (@NonNull PeriodosEscolares periodo){
        try{
            if(periodo==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"El periodo no debe ser nulo");}
            Evaluaciones evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.periodo=:periodo AND (e.tipo=:tipo OR e.tipo=:tipo1 OR e.tipo=:tipo2 OR e.tipo=:tipo3 OR e.tipo=:tipo4) ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo4", EvaluacionesTipo.DOCENTE_4.getLabel())
                    .setParameter("tipo", EvaluacionesTipo.DOCENTE.getLabel())
                    .setParameter("tipo1",EvaluacionesTipo.DOCENTE_1.getLabel())
                    .setParameter("tipo2", EvaluacionesTipo.DOCENTE_2.getLabel())
                    .setParameter("tipo3",EvaluacionesTipo.DOCENTE_3.getLabel())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            //System.out.println("EjbInformeIntegralDocente.getEvalaucionDocentebyPeriodo "+ evaluacion);
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(3,new Evaluaciones(),"No existe la evaluación buscada en el periodo seleccionado");}
            else {return ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion");}
        }catch (Exception e){ return ResultadoEJB.crearErroneo(1, "Error al obtener la evaluacion por tipo y periodo(EjbInformeIntegralDocente.getEvalaucionbyTipoPeriodo)", e, null); }
    }

    /**
     * Obtiene los resultados de la evaluación de Desempeño por periodo y docente seleccionado
     * @param evaluacion Evaluacion del periodo seleccionado
     * @param docente Docente Seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<DesempenioEvaluacionResultados> getResultadosDesempeniobyPeriodoDocente(@NonNull DesempenioEvaluaciones evaluacion, @NonNull Personal docente){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new DesempenioEvaluacionResultados(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new DesempenioEvaluacionResultados(),"El docente no debe ser nulo");}
            DesempenioEvaluacionResultados resultados = em.createQuery("select d from DesempenioEvaluacionResultados d where d.desempenioEvaluacionResultadosPK.evaluacion=:evaluacion and  d.desempenioEvaluacionResultadosPK.evaluado=:clave", DesempenioEvaluacionResultados.class)
                    .setParameter("clave",docente.getClave())
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            ;
            if(resultados !=null & resultados.getR1()!=null){ return ResultadoEJB.crearCorrecto(resultados,"Resultados Evaluación Deseméño"); }
            else {return ResultadoEJB.crearErroneo(3,resultados,"EL docente no ha sido evaluado");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion desempeño (EjbInformeIntegralDocente.getResultadosDesempeniobyPeriodoDocente)", e, null);

        }
    }

    /**
     * Obtiene la lista de resultados de la evaluacion entre pares académicos por periodo
     * @param evaluacion Evaluacion de Pares Académicos del periodo seleccionado
     * @param docente Docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionParesAcademicos>> getResultadosEvParesAcademicos (@NonNull Evaluaciones evaluacion, @NonNull Personal docente){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionParesAcademicos> resultados = em.createQuery("select e from EvaluacionParesAcademicos e where e.evaluacionParesAcademicosPK.evaluacion=:evaluacion and e.evaluacionParesAcademicosPK.evaluado=:docente and e.completo=true", EvaluacionParesAcademicos.class)
                    .setParameter("docente",docente.getClave())
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .getResultList()
                    ;
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El docente no cuenta con resultados en el periodo seleccionado");}
            else { return ResultadoEJB.crearCorrecto(resultados,"Lista de restados"); }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion desempeño (EjbInformeIntegralDocente.getResultadosDesempeniobyPeriodoDocente)", e, null);
        }
    }

    /**
     * Obtiene la lista de resultados de la evaluación a tutor (Tipo Tutor)
     * @param docente Docente seleccionado
     * @param evaluacion Evaluación
     * @return Resultado del proceso (Resultados)
     */
    public ResultadoEJB<List<EvaluacionTutoresResultados>> getResultadosTutor1byDocente(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionTutoresResultados> resultados = em.createQuery("select e from EvaluacionTutoresResultados e where e.evaluacionTutoresResultadosPK.evaluado=:docente and e.evaluacionTutoresResultadosPK.evaluacion=:evaluacion", EvaluacionTutoresResultados.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultList()
                    ;
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados de la evaluación a tutor (Tipo Tutor Cuestionario 1)
     * @param docente Docente seleccionado
     * @param evaluacion Evaluación
     * @return Resultado del proceso (Resultados)
     */
    public ResultadoEJB<List<EvaluacionTutoresResultados2>> getResultadosTutor2byDocente(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionTutoresResultados2> resultados = em.createQuery("select e from EvaluacionTutoresResultados2 e where e.evaluacionTutoresResultados2PK.evaluado=:docente and e.evaluacionTutoresResultados2PK.evaluacion=:evaluacion and e.completo=true", EvaluacionTutoresResultados2.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultList()
                    ;
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados de la evaluación a tutor (Tipo Tutor Cuestionario 2 )
     * @param docente Docente seleccionado
     * @param evaluacion Evaluación
     * @return Resultado del proceso (Resultados)
     */
    public ResultadoEJB<List<EvaluacionTutoresResultados3>> getResultadosTutor3byDocente(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionTutoresResultados3> resultados = em.createQuery("select e from EvaluacionTutoresResultados3 e where e.evaluacionTutoresResultados3PK.evaluado=:docente and e.evaluacionTutoresResultados3PK.evaluacion=:evaluacion and e.completo=true", EvaluacionTutoresResultados3.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultList()
                    ;
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    ////////////////////////////////////////// Consultas para obtener lo nombres de las materias /////////////////////////////////////

    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     *                   Tipo--> Docente Materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getMaterias(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionDocentesMateriaResultados2> resultados = em.createQuery("select distinct e from EvaluacionDocentesMateriaResultados2 e where e.evaluacionDocentesMateriaResultados2PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados2PK.evaluacion=:evaluacion and e.completo=true", EvaluacionDocentesMateriaResultados2.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            if (resultados == null|| resultados.isEmpty()) { return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no fue evaluado en el periodo seleccionado");}
            else {
                List<String> resu= new ArrayList<>();
                //System.out.println("EjbInformeIntegralDocente.getMaterias2 "+resultados);
                resultados.stream().forEach(r->{
                    resu.add(r.getEvaluacionDocentesMateriaResultados2PK().getCveMateria());
                });
                List<String> materias = resu.stream().distinct().collect(Collectors.toList());
                //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion --2"+ materias.size());
                return ResultadoEJB.crearCorrecto(materias,"Lista materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     *                   Tipo--> Docente Materia (Cuestionario 1)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getMaterias1(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionDocentesMateriaResultados> resultados = em.createQuery("select distinct e from EvaluacionDocentesMateriaResultados e where e.evaluacionDocentesMateriaResultadosPK.evaluado=:docente and e.evaluacionDocentesMateriaResultadosPK.evaluacion=:evaluacion and e.completo=true", EvaluacionDocentesMateriaResultados.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            if (resultados == null|| resultados.isEmpty()) { return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no fue evaluado en el periodo seleccionado");}
            else {
                List<String> resu= new ArrayList<>();
                //System.out.println("EjbInformeIntegralDocente.getMaterias1 "+resultados);
                resultados.stream().forEach(r->{
                    resu.add(r.getEvaluacionDocentesMateriaResultadosPK().getCveMateria());
                });
                List<String> materias = resu.stream().distinct().collect(Collectors.toList());
                //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion --1"+ materias.size());
                return ResultadoEJB.crearCorrecto(materias,"Lista materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     *                   Tipo--> Docente materia (Cuestionario 2 por contingencia)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getMaterias2(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionDocentesMateriaResultados3> resultados = em.createQuery("select distinct e from EvaluacionDocentesMateriaResultados3 e where e.evaluacionDocentesMateriaResultados3PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados3PK.evaluacion=:evaluacion and e.completo=true", EvaluacionDocentesMateriaResultados3.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            if (resultados == null|| resultados.isEmpty()) { return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no fue evaluado en el periodo seleccionado");}
            else {
                List<String> resu= new ArrayList<>();
                //System.out.println("EjbInformeIntegralDocente.getMaterias2 "+resultados);
                resultados.stream().forEach(r->{
                    resu.add(r.getEvaluacionDocentesMateriaResultados3PK().getCveMateria());
                });
                List<String> materias = resu.stream().distinct().collect(Collectors.toList());
                //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion --2"+ materias.size());
                return ResultadoEJB.crearCorrecto(materias,"Lista materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     *                   Tipo--> Docente materia (Cuestionario 3 por contingencia)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getMaterias3(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionDocentesMateriaResultados4> resultados = em.createQuery("select distinct e from EvaluacionDocentesMateriaResultados4 e where e.evaluacionDocentesMateriaResultados4PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados4PK.evaluacion=:evaluacion and e.completo=true", EvaluacionDocentesMateriaResultados4.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            if (resultados == null|| resultados.isEmpty()) { return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no fue evaluado en el periodo seleccionado");}
            else {
                List<String> resu= new ArrayList<>();
                //System.out.println("EjbInformeIntegralDocente.getMaterias3 "+resultados);
                resultados.stream().forEach(r->{
                    resu.add(r.getEvaluacionDocentesMateriaResultados4PK().getCveMateria());
                });
                List<String> materias = resu.stream().distinct().collect(Collectors.toList());
                //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion --3"+ materias.size());
                return ResultadoEJB.crearCorrecto(materias,"Lista materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo (Docente materrai cuestiuonario 4 por contingencia)
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getMaterias4(@NonNull Personal docente, @NonNull Evaluaciones evaluacion ){
        try{
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nulo");}
            if(docente ==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no debe ser nulo");}
            List<EvaluacionDocentesMateriaResultados5> resultados = em.createQuery("select distinct e from EvaluacionDocentesMateriaResultados5 e where e.evaluacionDocentesMateriaResultados5PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados5PK.evaluacion=:evaluacion and e.completo=true", EvaluacionDocentesMateriaResultados5.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("docente",docente.getClave())
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            if (resultados == null|| resultados.isEmpty()) { return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El docente no fue evaluado en el periodo seleccionado");}
            else {
                List<String> resu= new ArrayList<>();
                //System.out.println("EjbInformeIntegralDocente.getMaterias4 "+resultados);
                resultados.stream().forEach(r->{
                    resu.add(r.getEvaluacionDocentesMateriaResultados5PK().getCveMateria());
                });
                List<String> materias = resu.stream().distinct().collect(Collectors.toList());
                //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion --2"+ materias.size());
                return ResultadoEJB.crearCorrecto(materias,"Lista materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }

    ////////////////////////////////// Consultas para obtener los resultados de la evaluacion, por evaluacion, clave materia y docente evaluado //////////////////////


    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo, y materia
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado (Docente materia (Cuestionario 1))
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> getResultadosDocentebyMeteria1(@NonNull Personal docente, @NonNull Evaluaciones evaluacion, @NonNull String claveMateria ){
        try {
            if (evaluacion == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La evaluación no debe ser nulo"); }
            if (docente == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El docente no debe ser nulo"); }
            List<EvaluacionDocentesMateriaResultados> resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados e where e.evaluacionDocentesMateriaResultadosPK.evaluado=:docente and e.evaluacionDocentesMateriaResultadosPK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultadosPK.cveMateria=:claveMateria and e.completo=true", EvaluacionDocentesMateriaResultados.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("docente", docente.getClave())
                    .setParameter("claveMateria", claveMateria)
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            //  System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion "+ resultados.size());
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo, y materia
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado (Docente materia)
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados2>> getResultadosDocentebyMeteria(@NonNull Personal docente, @NonNull Evaluaciones evaluacion, @NonNull String claveMateria ){
        try {
            if (evaluacion == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La evaluación no debe ser nulo"); }
            if (docente == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El docente no debe ser nulo"); }
            List<EvaluacionDocentesMateriaResultados2> resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados2 e where e.evaluacionDocentesMateriaResultados2PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados2PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados2PK.cveMateria=:claveMateria and e.completo=true", EvaluacionDocentesMateriaResultados2.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("docente", docente.getClave())
                    .setParameter("claveMateria", claveMateria)
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            //  System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion "+ resultados.size());
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo, y materia
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado (Docente materia (Cuestionario 2 por contingencia))
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> getResultadosDocentebyMeteria2(@NonNull Personal docente, @NonNull Evaluaciones evaluacion, @NonNull String claveMateria ){
        try {
            if (evaluacion == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La evaluación no debe ser nulo"); }
            if (docente == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El docente no debe ser nulo"); }
            List<EvaluacionDocentesMateriaResultados3> resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados3 e where e.evaluacionDocentesMateriaResultados3PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados3PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados3PK.cveMateria=:claveMateria and e.completo=true", EvaluacionDocentesMateriaResultados3.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("docente", docente.getClave())
                    .setParameter("claveMateria", claveMateria)
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            //  System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion "+ resultados.size());
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }
    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo, y materia
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado (Docente materia (Cuestionario 3 por contingencia))
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> getResultadosDocentebyMeteria3(@NonNull Personal docente, @NonNull Evaluaciones evaluacion, @NonNull String claveMateria ){
        try {
            if (evaluacion == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La evaluación no debe ser nulo"); }
            if (docente == null) { return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El docente no debe ser nulo"); }
            List<EvaluacionDocentesMateriaResultados4> resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados4 e where e.evaluacionDocentesMateriaResultados4PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados4PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados4PK.cveMateria=:claveMateria and e.completo=true", EvaluacionDocentesMateriaResultados4.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("docente", docente.getClave())
                    .setParameter("claveMateria", claveMateria)
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            //System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion "+ resultados.size());
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }

    /**
     * Obtiene la lista de resultados Docente por evaluacio y periodo, y materia (Docente materrai cuestiuonario 4 por contingencia)
     * @param docente Docente selecionado
     * @param evaluacion Evaluacion aplicada en el periodo seleccionado
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> getResultadosDocente5byMeteria(@NonNull Personal docente, @NonNull Evaluaciones evaluacion, @NonNull String claveMateria ){
        try {
            if (evaluacion == null) {
                return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La evaluación no debe ser nulo");
            }
            if (docente == null) {
                return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "El docente no debe ser nulo");
            }
            List<EvaluacionDocentesMateriaResultados5> resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados5 e where e.evaluacionDocentesMateriaResultados5PK.evaluado=:docente and e.evaluacionDocentesMateriaResultados5PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados5PK.cveMateria=:claveMateria and e.completo=true", EvaluacionDocentesMateriaResultados5.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .setParameter("docente", docente.getClave())
                    .setParameter("claveMateria", claveMateria)
                    .getResultStream()
                    .collect(Collectors.toList());
            ;
            //  System.out.println("EjbInformeIntegralDocente.getResultadosDocentebyEvaluacion "+ resultados.size());
            if(resultados==null || resultados.isEmpty()){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No tiene resultados de la evaluación a tutor");}
            else {return ResultadoEJB.crearCorrecto(resultados,"Resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de la evaluacion Tutor 1 (EjbInformeIntegralDocente.getResultadosTutor1byDocente)", e, null);
        }
    }



    /**
     * Apartados de la evaluación a Desempeño
     * @return
     */
    public List<Apartado> getApartadosDesempeño() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "Metas", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 1f, "Cumplimiento en los objetivos y metas institucionales, favoreciendo la productividad, eficiencia y eficacia en la ejecución de sus funciones.", ""));
        Apartado a2 = new Apartado(2F, "Criterio", new SerializableArrayList<>());
        a2.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 2f, "Conocimiento y aplicación de las normas establecidas, que sustenten sus acciones y/o le permitan formular propuestas para favorecer el proceso institucional y personal de toma de decisiones.", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 3f, "Acciones tomadas y/o soluciones planteadas ante circunstancias imprevistas, privilegiadas el enfoque hacia los mejores resultados.", ""));
        Apartado a3 = new Apartado(3F, "Liderazgo y dirección", new SerializableArrayList<>());
        a3.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 4f, "Capacidad para coordinar y organizar actividades conjuntas, conduciendo acciones adecuadas en el manejo y motivación del personal; obteniendo los mejores resultados sobre el propósito dado.", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 5f, "Capacidad para conducir y controlar el equipo de trabajo, mediante la integración e identificación de actividades colectivas; privilegiando la consecución de los objetivos comunes y llevando a los miembros del grupo hacia los logros superiores.", ""));
        Apartado a4 = new Apartado(4F, "Calidad del trabajo", new SerializableArrayList<>());
        a4.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 6f, "Presentación, estructura, contenido, precisión, oportunidad y limpieza del trabajo encomendado para el desarrollo de sus propias funciones, así como las de sus compañeros y autoridades.", ""));
        Apartado a5 = new Apartado(5F, "Madurez y discreción", new SerializableArrayList<>());
        a5.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 7f, "Capacidad para el manejo y uso de la información clasificada como confidencial, a la que tiene acceso por la naturaleza de su puesto; privilegiando la eficiencia en sus funciones, en un marco de madurez y prudencia.", ""));
        Apartado a6 = new Apartado(6F, "Iniciativa", new SerializableArrayList<>());
        a6.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 8f, "Capacidad para la actuación espontánea, con una amplia disposición para aportar ideas innovadoras; que permitan mejorar su trabajo y el de sus demás compañeros dentro de la institución.", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 9f, "Actitud para desempeñar sus funciones sin necesidad de supervisión estrecha, actuando siempre de manera constructiva y profesional; aunque no cuente con una dirección o seguimiento permanentes.", ""));
        Apartado a7 = new Apartado(7F, "Colaboración y compromiso", new SerializableArrayList<>());
        a7.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 10f, "Interés, entusiasmo y disposición hacia las tareas que representan labores cotidianas, manifestando su involucramiento para ser parte de los esfuerzos institucionales; aun cuando éstas sean en otras áreas.", ""));
        a7.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 11f, "Interés, entusiasmo y disposición hacia las tareas que representan labores complementarias, manifestado su involucramiento para ser parte de los esfuerzos adicionales que realiza la institución; aun cuando éstas sean honorarios no convencionales o pertenezcan a otras áreas distintas a la propia.", ""));
        Apartado a8 = new Apartado(8F, "Buen uso de los recursos", new SerializableArrayList<>());
        a8.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 12f, "Capacidad para el conocimiento y manejo apropiado de la infraestructura, el equipamiento y los recursos materiales disponibles dentro de la institución; aun cuando estos pertenezcan a otras áreas distintas a la propia.", ""));
        a8.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 13f, "Cuidado y uso racional de dichos recursos dentro de la institución privilegiando el mayor ahorro y la adecuada conservación o mantenimiento de los mismos.", ""));
        a8.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 14f, "Capacidad en la iniciativa necesaria, para proponer el mayor aprovechamiento y mejor uso de los recursos dentro de la institución.", ""));
        Apartado a9 = new Apartado(9F, "Disciplina", new SerializableArrayList<>());
        a9.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 15f, "Actitud dentro del área de trabajo, observancia hacia las normas y disposiciones institucionales, acatamiento de órdenes e instrucciones y respeto a las líneas jerárquicas.", ""));
        Apartado a10 = new Apartado(10F, "Trabajo colaborativo y en equipo", new SerializableArrayList<>());
        a10.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 16f, "Integración y participación dentro del equipo de trabajo, mostrando compromiso en el alcance de los objetivos comunes; privilegiando el interés de la colectividad institucional.", ""));
        Apartado a11 = new Apartado(11F, "Relaciones interpersonales", new SerializableArrayList<>());
        a11.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 17f, "Capacidad de desarrollar condiciones propicias en el ambiente laboral, mediante su comportamiento con los compañeros de trabajo y los usuarios del servicio proporcionado; colaborando con la construcción de una adecuada imagen  de la institución.", ""));
        Apartado a12 = new Apartado(12F, "Aspecto e imagen personal", new SerializableArrayList<>());
        a12.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 18f, "Capacidad para el cuidado de su propia imagen y presentación personal; logrando que sea de manera adecuada y limpia, privilegiando una buena impresión visual.", ""));
        Apartado a13 = new Apartado(12F, "Capacitación", new SerializableArrayList<>());
        a13.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 19f, "Disposición, interés e iniciativa para su anticipación en los programas de desarrollo previstos, para el mejoramiento tanto de sus propias tareas y responsabilidades; así como de su crecimiento personal y evolución profesional.", ""));
        a13.getPreguntas().add(new Opciones(TipoCuestionario.DESEMPENIO, 1f, 20f, "Capacidad para compartir, ejecutar acciones y aplicar los conocimientos adquiridos en los programas de desarrollo previstos; que le permita potenciar la productividad, eficiencia y eficacia de su trabajo y el de sus propios compañeros.", ""));

        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        l.add(a5);
        l.add(a6);
        l.add(a7);
        l.add(a8);
        l.add(a9);
        l.add(a10);
        l.add(a11);
        l.add(a12);
        l.add(a13);

        return l;
    }

    /**
     * Obtiene el apartado según el tipo de Evaluación que se aplico en el periodo seleccionado
     * @param tipo Tipo Evaluación
     * @return
     */
    public ResultadoEJB<List<Apartado>> getApartadosTutorbyTipo (@NonNull EvaluacionesTipo tipo){
        try{
            if(tipo==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El tipo de evaluación no debe ser nula");}
            List<Apartado> apartados = new ArrayList<>();
            Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
            //Se le quitaron los comentarios a los apartados originales
            switch (tipo){
                case TUTOR_1:
                    //Cuestionario Tutor
                   // System.out.println("EjbInformeIntegralDocente.getApartadosTutorbyTipo 1");
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,1.0f, "Me enteré de los servicios y programas de apoyo al estudiante a través de información que me proporcionó mi tutor (becas, cursos, programas de intercambio estudiantil, programas culturales y demás trámites escolares).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,2.0f, "El trabajo que desarrollé con mi tutor evidenció que hubo una planeación de las actividades y no una mera improvisación.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,3.0f, "El tutor llegó a la impartición de la tutoría en tiempo y forma.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,4.0f, "Me sentí cómodo en las sesiones de tutoría por el ambiente de respeto y atención que me dio mi tutor.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,5.0f, "El tutor me canalizó a las instancias adecuadas cuando tuve algún problema (si no se tuvo problemas puede elegir la opción NO APLICA).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,6.0f, "Con las sesiones de tutorías he aprendido a tomar decisiones y asumir las consecuencias de las mismas.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,7.0f, "Fue fácil localizar a mi tutor en las sesiones de trabajo acordadas.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,8.0f, "En general, el desempeño de mi tutor fue satisfactorio.", ""));
                    break;
                case TUTOR_2:
                    //System.out.println("EjbInformeIntegralDocente.getApartadosTutorbyTipo 2");
                    //Cuestionario Tutor (Cuestionario 2)
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,1.0f, "Me enteré de los servicios y programas de apoyo al estudiante a través de información que me proporcionó mi Tutor (becas, cursos, programas de intercambio estudiantil, programas culturales y deportivos y demás trámites escolares).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,2.0f, "Las sesiones que desarrollé con mi Tutor evidenció que hubo una planeación de las actividades y no una improvisación.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,3.0f, "El Tutor llego a la impartición de la tutoría grupal puntualmente.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,4.0f, "Me sentí cómodo en las sesiones de tutoría por el ambiente de respeto y atención que me dio mi Tutor.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,5.0f, "El Tutor me orientó a recibir asesorías académicas cuando lo necesité. (si no fue necesario, puede contestarse como No Aplica).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,6.0f, "El Tutor me orientó o canalizó al área correspondiente para atender problemas de tipo psicopedagógico, (si no se tuvo problemas puede contestarse como No Aplica).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,7.0f, "El Tutor me proporcionó información de las becas que ofrece la Universidad.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,8.0f, "El Tutor me proporcionó información del servicio de enfermería que proporciona la Universidad.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,9.0f, "Fue fácil localizar a mi Tutor en las sesiones de Tutoría Individual.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,10.0f, "En general, el desempeño de mi Tutor fue.", ""));
                    break;
                case TUTOR:
                    //Cuestionario Tutor (Cuestionario 1)
                   // System.out.println("EjbInformeIntegralDocente.getApartadosTutorbyTipo 1-1");
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,1.0f, "Me enteré de los servicios y programas de apoyo al estudiante a través de información que me proporcionó mi tutor (becas, cursos, programas de intercambio estudiantil, programas culturales y deportivos y demás trámites escolares).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,2.0f, "Las sesiones que desarrollé con mi tutor evidenció que hubo una planeación de las actividades y no una improvisación.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,3.0f, "El tutor llego a la impartición de la tutoría grupal puntualmente.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,4.0f, "Me sentí cómodo en las sesiones de tutoría por el ambiente de respeto y atención que me dio mi tutor.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,5.0f, "El tutor me orientó a recibir asesorías académicas cuando lo necesité.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,6.0f, "El tutor me orientó a instancias de apoyo psicopedagógicas, becas, enfermería, cuando tuve algún problema (si no se tuvo problemas puede contestarse como No Aplica).", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,7.0f, "Fue fácil localizar a mi tutor en las sesiones de Tutoría Individual.", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.ETUTOR, 1.0f,8.0f, "En general, el desempeño de mi tutor fue.", ""));
                    break;
            }
            apartados.add(a1);
            return ResultadoEJB.crearCorrecto(apartados,"");
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al el apartado de la evalucion a tutor(EjbInformeIntegralDocente.getApartadosTutorbyTipo)", e, null);

        }
    }

    /**
     * Obtiene los apartados correspondientes Evaluacion Docente por tipo de CUestionario
     * @param tipo
     * @return
     */
    public ResultadoEJB<List<Apartado>> getApartadosDocentebyTipo (@NonNull EvaluacionesTipo tipo){
        try{
            if(tipo==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El tipo de evaluación no debe ser nula");}
            List<Apartado> apartados = new ArrayList<>();
            //Se le quitaron los comentarios a los apartados originales
            switch (tipo){
                case DOCENTE:
                    //Cuestionario Docente Materia
                    Apartado a1 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Asiste puntualmente a clases y si falta te informa oportunamente?", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Cumple el horario de inicio y finalización de la clase?", ""));
                    a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Ofrece asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
                    Apartado a2 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
                    a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, "¿Explica al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
                    Apartado a3 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Explica con claridad los conceptos de cada tema?", ""));
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Las clases están bien preparadas, organizadas y estructuradas?", ""));
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Te motiva para que participes objetiva y activamente en el desarrollo de la clase?", ""));
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Genera un clima de confianza en el que se pueden plantear distintas dudas?", ""));
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
                    a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Se dirige con un lenguaje que propio y respetuoso?", ""));
                    Apartado a4 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
                    a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
                    a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Evalúa el  conocimiento del saber a través de un examen escrito u otra  herramienta?", ""));
                    a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "¿Evalúa la parte del saber hacer a través de la práctica, ejercicio práctico u otra herramienta?", ""));
                    Apartado a5 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
                    a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "¿El docente te motivó al uso de la biblioteca y/o aplicaciones tecnológicas para reforzar tus conocimientos?", ""));
                    a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "¿Te incentiva a participar en actividades complementarias como: seminarios, lecturas, talleres y congresos?", ""));
                    Apartado a6 = new Apartado(6F, "PROYECCION SOCIAL", new SerializableArrayList<>());
                    a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Involucra a los estudiantes para conservar la disciplina, orden y limpieza en la Institución?", ""));
                    a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "¿Promueve el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
                    apartados.add(a1);
                    apartados.add(a2);
                    apartados.add(a3);
                    apartados.add(a4);
                    apartados.add(a5);
                    apartados.add(a6);
                    break;
                case DOCENTE_1:
                    Apartado a7 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
                    a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Asiste normalmente a clases y si falta lo justifica?", ""));
                    a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Cumple el horario de inicio y finalización de la clase?", ""));
                    a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Cumple con su labor de asesorías?", ""));
                    a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, "¿Cumple con el temario de la asignatura?", ""));
                    Apartado a8 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
                    a8.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Explica al inicio del ciclo los objetivos, contenidos, metodología, evaluación y bibliografía de la materia?", ""));
                    a8.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Desarrolla en clase los contenidos del programa de la materia?", ""));
                    a8.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Desarrolla las clases del saber de acuerdo a la Programación Académica?", ""));
                    a8.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Desarrolla las clases del saber hacer  de acuerdo a la Programación Académica?", ""));
                    Apartado a9 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Explica con claridad los conceptos de cada tema?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Las clases están bien preparadas, organizadas y estructuradas?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿Se preocupa de los problemas de aprendizaje de sus estudiantes?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Te motiva para que participes crítica y activamente en el desarrollo de la clase?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "¿La comunicación profesor-estudiante es fluida y espontánea, creando un clima de confianza?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "¿Es amable y respetuoso con los estudiantes?", ""));
                    a9.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Es accesible y está dispuesto a ayudarlos?", ""));
                    Apartado a10 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
                    a10.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "¿Desarrolla con los estudiantes las pruebas evaluativas realizadas para su mejor comprensión?", ""));
                    a10.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 18.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
                    a10.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 19.0f, "¿Entrega las notas en el plazo establecido?", ""));
                    a10.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 20.0f, "¿Evalúa el  conocimiento del saber a través de un examen escrito u otra  herramienta?", ""));
                    a10.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 21.0f, "¿Evalúa la parte del saber hacer a través de la práctica, ejercicio práctico  u otra herramienta?", ""));
                    Apartado a11 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
                    a11.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 22.0f, "¿Los recursos didácticos utilizados por el docente, te ayudaron a comprender mejor la asignatura?", ""));
                    a11.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 23.0f, "¿El docente te motivó al uso de la biblioteca y aulas virtuales para reforzar tus conocimientos?", ""));
                    a11.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 24.0f, "¿Te incentiva a participar en actividades complementarias como: seminarios lecturas, charlas y congresos?", ""));
                    Apartado a12 = new Apartado(6F, "INVESTIGACIÓN", new SerializableArrayList<>());
                    a12.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 25.0f, "¿Promueve la investigación?", ""));
                    a12.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 26.0f, "¿Involucra a los estudiantes en sus proyectos de investigación con su práctica docente?", ""));
                    a12.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 27.0f, "¿Te motiva a participar en Congresos científico estudiantiles?", ""));
                    Apartado a13 = new Apartado(7F, "PROYECCION SOCIAL", new SerializableArrayList<>());
                    a13.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 28.0f, "¿Te incentiva a Participar en los eventos de extensión Universitaria?", ""));
                    a13.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 29.0f, "¿Te motiva a participar en las charlas, platicas y sesiones de valores?", ""));
                    a13.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 30.0f, "¿Involucra a los estudiantes para conservar el orden y limpieza del aula?", ""));
                    a13.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 31.0f, "¿Informa a los estudiantes de todas las actividades de Proyección Social Y Extensión Universitaria?", ""));
                    apartados.add(a7);
                    apartados.add(a8);
                    apartados.add(a9);
                    apartados.add(a10);
                    apartados.add(a11);
                    apartados.add(a12);
                    apartados.add(a13);
                    break;
                case DOCENTE_2:
                    Apartado a14 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
                    a14.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
                    Apartado a15 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
                    a15.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Explica al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
                    Apartado a16 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Explica con claridad los conceptos de cada tema?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, "¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Se dirige con un lenguaje que propio y respetuoso?", ""));
                    a16.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
                    Apartado a17 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
                    a17.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
                    a17.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
                    a17.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta? ", ""));
                    Apartado a18 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
                    a18.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
                    a18.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
                    a18.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclarar de dudas?", ""));
                    a18.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
                    Apartado a19 = new Apartado(7F, "PROYECCION SOCIAL", new SerializableArrayList<>());
                    a19.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "Antes de la contingencia ¿Te involucró para conservar la disciplina, orden y limpieza en la Institución?", ""));
                    a19.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 18.0f, "Antes de la contingencia ¿Promovió el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
                    apartados.add(a14);
                    apartados.add(a15);
                    apartados.add(a16);
                    apartados.add(a17);
                    apartados.add(a18);
                    apartados.add(a19);
                    break;
                case DOCENTE_3:
                    Apartado a20 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
                    a20.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
                    Apartado a21 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
                    a21.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Explicó al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
                    Apartado a22 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Explicó con claridad los conceptos y contenidos de cada tema?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, " ¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Se dirige con un lenguaje propio y respetuoso?", ""));
                    a22.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
                    Apartado a23 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
                    a23.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
                    a23.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
                    a23.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta?", ""));
                    Apartado a24 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
                    a24.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
                    a24.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
                    a24.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclaración de dudas?", ""));
                    a24.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
                    a24.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "En general, el desempeño del docente fue.", ""));
                    Apartado a25 = new Apartado(7F, "PROYECCION SOCIAL", new SerializableArrayList<>());
                    a25.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 18.0f, "Antes de la contingencia ¿Te involucró para conservar la disciplina, orden y limpieza en la Institución?", ""));
                    a25.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 19.0f, "Antes de la contingencia ¿Promovió el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
                    apartados.add(a20);
                    apartados.add(a21);
                    apartados.add(a22);
                    apartados.add(a23);
                    apartados.add(a24);
                    apartados.add(a25);
                    break;
                case DOCENTE_4:
                    // System.out.println("EjbInformeIntegralDocente.getApartadosDocentebyTipo 4");
                    //Cuestionario Dccente Materia (Cuestionario 4 por contingencia)
                    Apartado a26 = new Apartado(1F, "METODOLOGÍA", new SerializableArrayList<>());
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Explicó al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Explicó con claridad los conceptos y contenidos de cada tema?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, " ¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Se dirige con un lenguaje propio y respetuoso?", ""));
                    a26.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
                    Apartado a27 = new Apartado(3F, "EVALUACIÓN", new SerializableArrayList<>());
                    a27.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
                    a27.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
                    a27.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta?", ""));
                    a27.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "¿El docente te entregó y aclaró las evaluaciones antes de registrarlas en el sistema?", ""));
                    Apartado a28 = new Apartado(4F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
                    a28.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
                    a28.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
                    a28.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclaración de dudas?", ""));
                    a28.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
                    Apartado a29 = new Apartado(5F, "PROYECCION SOCIAL", new SerializableArrayList<>());
                    a29.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 18.0f, "¿El docente asiste puntualmente a las sesiones programadas?", ""));
                    a29.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 19.0f, "Durante la ejecución de actividades en Aula Virtual ¿Evitó en todo momento improvisar, tener tiempo muerto y/o desorganización en las sesiones?", ""));
                    a29.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 20.0f, "¿El docente evita prácticas de favoritismo, corrupción y/o alguna otra práctica antiética?", ""));
                    a29.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 21.0f, "En general, el desempeño del docente fue.", ""));
                    apartados.add(a26);
                    apartados.add(a27);
                    apartados.add(a28);
                    apartados.add(a29);
                    break;
            }
            return ResultadoEJB.crearCorrecto(apartados,"");
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al el apartado de la evalucion a tutor(EjbInformeIntegralDocente.getApartadosTutorbyTipo)", e, null);

        }
    }


    /**
     *
     * @param valor
     * @param max
     * @return
     */
    public ResultadoEJB<Double> basePregunta (@NonNull Double valor, @NonNull Integer max){
        try{
            if(valor==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(max==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor máximo no debe ser nulo");}
            Double resultado = new Double(0);
            resultado=(valor/Double.valueOf(max)*100) ;
            return ResultadoEJB.crearCorrecto(resultado,"Valor en base 10");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al convertir resultado (EjbInformeIntegralDocente.basePregunta)", e, null);
        }
    }


    /**
     * Obtiene el estilo segun el promedio
     * @param promedio
     * @return
     */
    public ResultadoEJB<String> getStilobyPromedio (@NonNull Double promedio){
        try{
            if(promedio==null){return ResultadoEJB.crearErroneo(2,new String(),"El valor no debe ser nulo");}
            String estilo = new String("");
            if(promedio>=90 & promedio<=100){ estilo = "10, 181, 16, 0.8"; }
            if(promedio>=80 & promedio<=90){estilo ="149, 255, 152, 0.8"; }
            if(promedio>=70 & promedio<80){estilo ="251, 243, 3, 0.8"; }
            if(promedio>=60 & promedio< 70){estilo ="255, 151, 23, 0.8"; }
            if(promedio>=0 & promedio<=60){estilo ="255, 0, 0, 0.8"; }

            return ResultadoEJB.crearCorrecto(estilo,"Estilo");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al elegir el estilo(EjbInformeIntegralDocente.getStilobyPromedio)", e, null);
        }
    }

    /**
     * Obtiene el promedio del apartado
     * @param resultadosPreguntas
     * @return
     */
    public ResultadoEJB<Double> promedioApartado (@NonNull List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas){
        try{
            if(resultadosPreguntas==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            List<Double> promedios = new ArrayList<>();
            Double promedioApartado = new Double(0);
            resultadosPreguntas.stream().forEach(a->{
                promedios.add(a.getPromedio());
            });
            promedioApartado = promedios.stream().mapToDouble(d -> d).average().orElse(0.0);
            //System.out.println("EjbInformeIntegralDocente.promedioApartado PROMEDIO APARTADO  "+promedioApartado);
            return ResultadoEJB.crearCorrecto(promedioApartado,"Promedio del apartado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obenter el promedio del apartado (EjbInformeIntegralDocente.promedioApartado)", e, null);

        }
    }



    /**
     * Obtiene el promedio de la Evaluación
     * @param resultadosApartados Lista de resultados de los apartados
     * @return Resultado del proceso
     */
    public ResultadoEJB<Double> promedioEvaluacion(@NonNull List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados){
        try{
            if(resultadosApartados==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            List<Double> promedios = new ArrayList<>();
            Double promedioEvaluacion= new Double(0);
            resultadosApartados.stream().forEach(p->{
                p.getResultadoPreguntas().stream().forEach(r->{promedios.add(r.getPromedio());});
            });
            promedioEvaluacion = promedios.stream().mapToDouble(d -> d).average().orElse(0.0);
            //System.out.println("EjbInformeIntegralDocente.promedioApartado PROMEDIO APARTADO  "+promedioEvaluacion);
            return ResultadoEJB.crearCorrecto(promedioEvaluacion,"Promedio del apartado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obenter el promedio del apartado (EjbInformeIntegralDocente.promedioApartado)", e, null);

        }
    }
    /**
     * Obtiene el promedio de la Evaluación
     * @param resultadosMaterias Lista de resultados de las materias
     * @return Resultado del proceso
     */
    public ResultadoEJB<Double> promedioEvaluacionDocente(@NonNull List<DtoInformeIntegralDocente.ResultadosMateria> resultadosMaterias){
        try{
            if(resultadosMaterias==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            List<Double> promedios = new ArrayList<>();
            Double promedioEvaluacion= new Double(0);
            resultadosMaterias.stream().forEach(p->{
                p.getResultados().stream().forEach(r->{promedios.add(r.getPromedioApartado());});
            });
            promedioEvaluacion = promedios.stream().mapToDouble(d -> d).average().orElse(0.0);
            //System.out.println("EjbInformeIntegralDocente.promedioApartado PROMEDIO APARTADO  "+promedioEvaluacion);
            return ResultadoEJB.crearCorrecto(promedioEvaluacion,"Promedio del apartado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obenter el promedio del apartado (EjbInformeIntegralDocente.promedioApartado)", e, null);

        }
    }

    /**
     * Obtiene el valo obtenido en el apartado de la evaluacion
     * @param valorObtenido Calificacion de la evaluacion
     * @param caliMax Calificacion Maxima
     * @param porcentajeEv Porcentaje que equivale la evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Double> promedioIntegral (@NonNull Double valorObtenido, @NonNull Double caliMax, @NonNull Integer porcentajeEv){
        try{
            if(valorObtenido==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(caliMax==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(porcentajeEv==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}

            Double calificacion = (valorObtenido*100)/caliMax;
            Double valor =Double.valueOf( Math.round((calificacion*porcentajeEv)/100));
            return ResultadoEJB.crearCorrecto(valor,"Porcentaje obtenido");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obenter el porcentaje total (EjbInformeIntegralDocente.promedioIntegral)", e, null);
        }

    }

    /**
     * Obtiene el nombre de la materia por clave
     * @param claveMateria Clave de la materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> getNombreMateria (@NonNull String claveMateria){
        try{
            if(claveMateria==null){return ResultadoEJB.crearErroneo(2,new String(),"La clave de la materia no debe ser nula");}

            //Busca la clave de la materia en control Escolar
            PlanEstudioMateria materia = em.createQuery("select p from PlanEstudioMateria p inner join p.idMateria m where p.claveMateria=:clave", PlanEstudioMateria.class)
                    .setParameter("clave",claveMateria)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            //System.out.println("EjbInformeIntegralDocente.getNombreMateria MATERIA CE - "+materia );
            if(materia==null){
                //Busca en SAIIUT
                VistaEvaluacionDocenteMateriaPye vista = em2.createQuery("select  v from VistaEvaluacionDocenteMateriaPye v where v.cveMateria=:clave", VistaEvaluacionDocenteMateriaPye.class)
                        .setParameter("clave",claveMateria)
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                        ;
                //System.out.println("EjbInformeIntegralDocente.getNombreMateria SAIIUT "+ vista);
                if(vista==null){ return ResultadoEJB.crearErroneo(3,new String(),"No se encontro la materia");}
                else { return ResultadoEJB.crearCorrecto(vista.getNombreMateria(),"Nombre de la materia");}
            }else {
                return ResultadoEJB.crearCorrecto(materia.getIdMateria().getNombre(),"Materia");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obenter el nombre de la materia (EjbInformeIntegralDocente.getNombreMateria)", e, null);
        }
    }
    /***
     * Obtiene el valor de la pregunta según el número de pregunta
     * @param resultados Restados de la evaluación entre pares academicos
     * @param numeroPregunta Número de pregunta
     * @return Resultado del proceso (Valor)
     */
    public ResultadoEJB<Double> getResultadoPreguntaEvPares(@NonNull List<EvaluacionParesAcademicos> resultados, @NonNull String numeroPregunta){
        try{
            if(numeroPregunta==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            Double resultado = new Double(0);
            Double promPregunta;
            switch (numeroPregunta){
                case "1.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvPares 1 "+ resultado);
                    break;
                case "2.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "3.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "4.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "5.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "6.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "7.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "8.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "9.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "10.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "11.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "12.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "13.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "14.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "15.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "16.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "17.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "18.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR18()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "19.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR19()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "20.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR20()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "21.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR21()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
                case "22.0":
                    promPregunta  = resultados.stream().mapToDouble(d -> d.getR22()).average().orElse(0.0);
                    resultado = basePregunta(promPregunta,4).getValor();
                    break;
            }
            return ResultadoEJB.crearCorrecto(resultado,"Valor");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.obtenerResultadoPreguntaEvDes)", e, null);
        }
    }

    /**
     * Obtiene el promedio por pregunta del los resultados segun el tipo de cuestionario que se aplico
     * @param resultados Resultados Ev Tutor
     * @param resultados2 Resultados Ev Tutor 2
     * @param resultados3 Resultados Ev Tutor 3
     * @param numeroPregunta Numero de pregunta
     * @param tipoEvaluacion Tipo de cuestionario que se aplico
     * @return Resultado del proceso
     */

    public ResultadoEJB<Double> getResultadoPreguntaEvTutorbyTipo(List<EvaluacionTutoresResultados> resultados, List<EvaluacionTutoresResultados2> resultados2, List<EvaluacionTutoresResultados3> resultados3, @NonNull String numeroPregunta, @NonNull EvaluacionesTipo tipoEvaluacion,@NonNull Integer ponderacion ){
        try{
            if(numeroPregunta==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            Double resultado = new Double(0);
            Double promPregunta;
            switch (tipoEvaluacion){
                case TUTOR_1:
                    switch (numeroPregunta){
                        case "1.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }
                            else if (ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }

                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "2.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }

                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "3.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "4.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "5.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else  if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "6.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else  if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }

                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "7.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else  if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "8.0":
                            if(ponderacion==1){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,3).getValor();
                            }else if(ponderacion==2){
                                promPregunta  = resultados.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                                resultado = basePregunta(promPregunta,5).getValor();
                            }

                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");


                case TUTOR:
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "2.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "3.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "4.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "5.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "6.0":
                            promPregunta  = resultados2.stream().filter(r->r.getR6()!=0).mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "7.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "8.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;

                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

                case TUTOR_2:
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "2.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //  System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "3.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "4.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "5.0":
                            //Se deben quitar los valores 0
                            List<EvaluacionTutoresResultados3> l = resultados3.stream().filter(v->v.getR5()!=0).collect(Collectors.toList());
                            promPregunta  = l.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            //Se deben quitar los valores 0
                            List<EvaluacionTutoresResultados3> l2 = resultados3.stream().filter(v->v.getR6()!=0).collect(Collectors.toList());
                            promPregunta  = l2.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "7.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "8.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "9.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                        case "10.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 2 "+ resultado);
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

            }

            return ResultadoEJB.crearCorrecto(resultado,"Valor");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.obtenerResultadoPreguntaEvDes)", e, null);
        }
    }

    /**
     * Obtiene el promedio por pregunta segun el tipo de cuestionario que se aplico
     * @param resultados Resultados Evaluacion DOcente
     * @param resultados1 Resultados evaluacion Docente (Docente materia cuestionario 1)
     * @param  resultados2 Resultados evaluacion Doncete (Contingencia 2)
     * @param  resultados3 Resultados evaluacion Doncete (Contingencia 3)
     * @param  resultados4 Resultados evaluacion Doncete (Contingencia 4)
     * @param numeroPregunta Numero de pregunta
     * @param tipoEvaluacion Tipo de cuestionario
     * @return Resultado del proceso
     */
    public ResultadoEJB<Double> getResultadoPreguntaDocentebyTipo(List<EvaluacionDocentesMateriaResultados2> resultados,List<EvaluacionDocentesMateriaResultados> resultados1,List<EvaluacionDocentesMateriaResultados3> resultados2,List<EvaluacionDocentesMateriaResultados4> resultados3, List<EvaluacionDocentesMateriaResultados5> resultados4, @NonNull String numeroPregunta, @NonNull EvaluacionesTipo tipoEvaluacion){
        try{
            if(numeroPregunta==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            Double resultado = new Double(0);
            Double promPregunta;
            switch (tipoEvaluacion){
                case DOCENTE:
                    //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo");
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "2.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "3.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "4.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "5.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "7.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "8.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "9.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "10.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "11.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "12.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "13.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "14.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "15.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "16.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "17.0":
                            promPregunta  = resultados.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

                case DOCENTE_1:
                   // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo 1");

                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo PREGUNTA 1 " + promPregunta );

                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "2.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "3.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "4.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "5.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "7.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "8.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "9.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "10.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "11.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "12.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "13.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "14.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "15.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "16.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "17.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "18.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR18()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "19.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR19()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "20.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR20()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "21.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR21()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "22.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR22()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "23.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR23()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "24.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR24()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "25.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR25()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "26.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR26()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "27.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR27()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "28.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR28()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "29.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR29()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "30.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR30()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "31.0":
                            promPregunta  = resultados1.stream().mapToDouble(d -> d.getR31()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

                case DOCENTE_2:
                    //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo 2");
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "2.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "3.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "4.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "5.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "7.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "8.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "9.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "10.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "11.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "12.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "13.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "14.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "15.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "16.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "17.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "18.0":
                            promPregunta  = resultados2.stream().mapToDouble(d -> d.getR18()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

                case DOCENTE_3:
                    //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo 3-1 " + resultados3 +" Numero pregunta "+ numeroPregunta);
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo Pregunat 1 "+ promPregunta+" Resultado " + resultado);
                            break;
                        case "2.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "3.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "4.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "5.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "7.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "8.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "9.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "10.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "11.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "12.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "13.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            //System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaEvTutorbyTipo 1 "+ resultado);
                            break;
                        case "14.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "15.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "16.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "17.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "18.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR18()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "19.0":
                            promPregunta  = resultados3.stream().mapToDouble(d -> d.getR19()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

                case DOCENTE_4:
                    switch (numeroPregunta){
                        case "1.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR1()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "2.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR2()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "3.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR3()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "4.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR4()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "5.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR5()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "6.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR6()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "7.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR7()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "8.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR8()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "9.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR9()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "10.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR10()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "11.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR11()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "12.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR12()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "13.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR13()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "14.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR14()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "15.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR15()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "16.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR16()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "17.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR17()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "18.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR18()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "19.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR19()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "20.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR20()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                        case "21.0":
                            promPregunta  = resultados4.stream().mapToDouble(d -> d.getR21()).average().orElse(0.0);
                            resultado = basePregunta(promPregunta,5).getValor();
                            break;
                    }
                    return ResultadoEJB.crearCorrecto(resultado,"Valor");

            }
           // System.out.println("EjbInformeIntegralDocente.getResultadoPreguntaDocentebyTipo Resultado "+ resultado);
            return ResultadoEJB.crearCorrecto(resultado,"Valor");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.obtenerResultadoPreguntaEvDes)", e, null);
        }
    }


    /***
     * Obtiene el valor de la pregunta según el número de pregunta
     * @param resultados Restados de la evaluación de Desempeño
     * @param numeroPregunta Número de pregunta
     * @return Resultado del proceso (Valor)
     */
    public ResultadoEJB<Double> getResultadoPreguntaEvDes (@NonNull DesempenioEvaluacionResultados resultados, @NonNull String numeroPregunta){
        try{
            if(numeroPregunta==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new Double(0),"El valor no debe ser nulo");}
            Double resultado = new Double(0);
            switch (numeroPregunta){
                case "1.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR1()),5).getValor();
                    break;
                case "2.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR2()),5).getValor();                    break;
                case "3.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR3()),5).getValor();                    break;
                case "4.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR4()),5).getValor();                    break;
                case "5.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR5()),5).getValor();                    break;
                case "6.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR6()),5).getValor();                    break;
                case "7.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR7()),5).getValor();                    break;
                case "8.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR8()),5).getValor();                    break;
                case "9.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR9()),5).getValor();                    break;
                case "10.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR10()),5).getValor();                    break;
                case "11.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR11()),5).getValor();                    break;
                case "12.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR12()),5).getValor();                    break;
                case "13.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR13()),5).getValor();                    break;
                case "14.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR14()),5).getValor();                    break;
                case "15.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR15()),5).getValor();                    break;
                case "16.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR16()),5).getValor();                    break;
                case "17.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR17()),5).getValor();                    break;
                case "18.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR18()),5).getValor();                    break;
                case "19.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR19()),5).getValor();                    break;
                case "20.0":
                    resultado = basePregunta(Double.valueOf(resultados.getR20()),5).getValor();                    break;
            }
            return ResultadoEJB.crearCorrecto(resultado,"Valor");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.obtenerResultadoPreguntaEvDes)", e, null);
        }
    }



    /**
     * Obtiene el listado de resultados por materia
     * @param tipoEv Tipo de cuentionario aplicado
     * @param  apartados Apartados del cuestionario
     * @param evaluacion Evalucion aplicada en el periodo seleccionado
     * @param docente Docente seleciconado
     * @param resultados1 Resultados ev 2(Docente Materia)
     * @param resultados2 Resultados ev4 (Docente Materia Cuestionario 4 por contingencia)
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> packResultadosMateria (@NonNull List<Apartado> apartados, @NonNull EvaluacionesTipo tipoEv, @NonNull Evaluaciones evaluacion, @NonNull Personal docente, List<String> resultados1, List<String> resultados2,List<String> resultados3,List<String> resultados4,List<String> resultados5){
        try{
            List<DtoInformeIntegralDocente.ResultadosMateria> resultadosMateriaList = new ArrayList<>();
            if(tipoEv==null){return ResultadoEJB.crearErroneo(2,resultadosMateriaList,"El tipo de evaluación no debe ser nulo");}
            if(apartados==null){return ResultadoEJB.crearErroneo(2,resultadosMateriaList,"Los apartados no debe ser nulo");}
            switch (tipoEv){
                case DOCENTE:
                    //System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+ tipoEv+ " Reusltados" + resultados2);
                    resultados1.stream().forEach(r->{
                        // System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+r);
                        //Obtiene los resultados segun la clave de la materia
                        //Obtiene el nombre de la materia
                        DtoInformeIntegralDocente.ResultadosMateria resultadosMateria = new DtoInformeIntegralDocente.ResultadosMateria(new String(),new String(),new Double(0.0),new String(),new ArrayList<>());
                        resultadosMateria.setClaveMateria(r);
                        resultadosMateria.setNombreMateria(getNombreMateria(r).getValor());
                        ResultadoEJB<List<EvaluacionDocentesMateriaResultados2>> resultados = getResultadosDocentebyMeteria(docente,evaluacion,r);
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas = new ArrayList<>();
                            //Apartados
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            a.getPreguntas().stream().forEach(p->{
                                //Preguntas
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setPromedio(getResultadoPreguntaDocentebyTipo(resultados.getValor(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        resultadosMateria.setResultados(resultadosApartados);
                        resultadosMateria.setPromedioMateria(promedioEvaluacion(resultadosApartados).getValor());
                        resultadosMateria.setStyle(getStilobyPromedio(resultadosMateria.getPromedioMateria()).getValor());
                        resultadosMateriaList.add(resultadosMateria);
                    });
                    break;
                case DOCENTE_1:
                    //System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+ tipoEv+ " Reusltados" + resultados2);
                    resultados2.stream().forEach(r->{
                        // System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+r);
                        //Obtiene los resultados segun la clave de la materia
                        //Obtiene el nombre de la materia
                        DtoInformeIntegralDocente.ResultadosMateria resultadosMateria = new DtoInformeIntegralDocente.ResultadosMateria(new String(),new String(),new Double(0.0),new String(),new ArrayList<>());
                        resultadosMateria.setClaveMateria(r);
                        resultadosMateria.setNombreMateria(getNombreMateria(r).getValor());
                        ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> resultados = getResultadosDocentebyMeteria1(docente,evaluacion,r);
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas = new ArrayList<>();
                            //Apartados
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            a.getPreguntas().stream().forEach(p->{
                                //Preguntas
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setPromedio(getResultadoPreguntaDocentebyTipo(new ArrayList<>(),resultados.getValor(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE_1).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        resultadosMateria.setResultados(resultadosApartados);
                        resultadosMateria.setPromedioMateria(promedioEvaluacion(resultadosApartados).getValor());
                        resultadosMateria.setStyle(getStilobyPromedio(resultadosMateria.getPromedioMateria()).getValor());
                        resultadosMateriaList.add(resultadosMateria);
                    });
                    break;
                case DOCENTE_2:
                    //System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+ tipoEv+ " Reusltados" + resultados2);
                    resultados3.stream().forEach(r->{
                        // System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+r);
                        //Obtiene los resultados segun la clave de la materia
                        //Obtiene el nombre de la materia
                        DtoInformeIntegralDocente.ResultadosMateria resultadosMateria = new DtoInformeIntegralDocente.ResultadosMateria(new String(),new String(),new Double(0.0),new String(),new ArrayList<>());
                        resultadosMateria.setClaveMateria(r);
                        resultadosMateria.setNombreMateria(getNombreMateria(r).getValor());
                        ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> resultados = getResultadosDocentebyMeteria2(docente,evaluacion,r);
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas = new ArrayList<>();
                            //Apartados
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            a.getPreguntas().stream().forEach(p->{
                                //Preguntas
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setPromedio(getResultadoPreguntaDocentebyTipo(new ArrayList<>(),new ArrayList<>(),resultados.getValor(),new ArrayList<>(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE_2).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        resultadosMateria.setResultados(resultadosApartados);
                        resultadosMateria.setPromedioMateria(promedioEvaluacion(resultadosApartados).getValor());
                        resultadosMateria.setStyle(getStilobyPromedio(resultadosMateria.getPromedioMateria()).getValor());
                        resultadosMateriaList.add(resultadosMateria);
                    });
                    break;
                case DOCENTE_3:
                  //  System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+ tipoEv+ " Reusltados" + resultados4);
                    resultados4.stream().forEach(r->{
                       // System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+r);
                        //Obtiene los resultados segun la clave de la materia
                        //Obtiene el nombre de la materia
                        DtoInformeIntegralDocente.ResultadosMateria resultadosMateria = new DtoInformeIntegralDocente.ResultadosMateria(new String(),new String(),new Double(0.0),new String(),new ArrayList<>());
                        resultadosMateria.setClaveMateria(r);
                        resultadosMateria.setNombreMateria(getNombreMateria(r).getValor());
                        ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> resultados = getResultadosDocentebyMeteria3(docente,evaluacion,r);
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas = new ArrayList<>();
                            //Apartados
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            a.getPreguntas().stream().forEach(p->{
                                //Preguntas
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setPromedio(getResultadoPreguntaDocentebyTipo(new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),resultados.getValor(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE_3).getValor());
                                //System.out.println("EjbInformeIntegralDocente.packResultadosMateria -- "+getResultadoPreguntaDocentebyTipo(new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),resultados.getValor(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE_3).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        resultadosMateria.setResultados(resultadosApartados);
                        resultadosMateria.setPromedioMateria(promedioEvaluacion(resultadosApartados).getValor());
                        resultadosMateria.setStyle(getStilobyPromedio(resultadosMateria.getPromedioMateria()).getValor());
                        resultadosMateriaList.add(resultadosMateria);
                    });
                    break;
                case DOCENTE_4:
                    //System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+ tipoEv+ " Reusltados" + resultados2);
                    resultados5.stream().forEach(r->{
                        // System.out.println("EjbInformeIntegralDocente.packResultadosMateria "+r);
                        //Obtiene los resultados segun la clave de la materia
                        //Obtiene el nombre de la materia
                        DtoInformeIntegralDocente.ResultadosMateria resultadosMateria = new DtoInformeIntegralDocente.ResultadosMateria(new String(),new String(),new Double(0.0),new String(),new ArrayList<>());
                        resultadosMateria.setClaveMateria(r);
                        resultadosMateria.setNombreMateria(getNombreMateria(r).getValor());
                        ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> resultados = getResultadosDocente5byMeteria(docente,evaluacion,r);
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPreguntas = new ArrayList<>();
                            //Apartados
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            a.getPreguntas().stream().forEach(p->{
                                //Preguntas
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setPromedio(getResultadoPreguntaDocentebyTipo(new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),resultados.getValor(),p.getNumero().toString(), EvaluacionesTipo.DOCENTE_4).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        resultadosMateria.setResultados(resultadosApartados);
                        resultadosMateria.setPromedioMateria(promedioEvaluacion(resultadosApartados).getValor());
                        resultadosMateria.setStyle(getStilobyPromedio(resultadosMateria.getPromedioMateria()).getValor());
                        resultadosMateriaList.add(resultadosMateria);
                    });
                    break;

            }
            return ResultadoEJB.crearCorrecto(resultadosMateriaList,"Empaquetado por materia");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al el apartado de la evalucion a tutor(EjbInformeIntegralDocente.packResultadosMateria)", e, null);

        }
    }


    /**
     * Empaqueta la los resultados del periodo de la  Desempeño del docente seleccionado
     * @param docente Docente seleccionado
     * @param periodo Periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoInformeIntegralDocente.Desempeño> packEvDes (@NonNull Personal docente, @NonNull PeriodosEscolares periodo){
        try{
            DtoInformeIntegralDocente.Desempeño dto = new DtoInformeIntegralDocente.Desempeño(new DesempenioEvaluaciones(),new ArrayList<>(),new Double(0),new String());
            if(docente ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}
            if(periodo ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}

            //Obtiene la evaluacion por periodo
            ResultadoEJB<DesempenioEvaluaciones> resEv = getEvaluacionDesbyPeriodo(periodo);
            if(resEv.getCorrecto()){
                //Obtiene los resultados de la evalución
                ResultadoEJB<DesempenioEvaluacionResultados> resResultados = getResultadosDesempeniobyPeriodoDocente(resEv.getValor(),docente);
                if(resResultados.getCorrecto()){
                    //Obtiene el apartado
                    List<Apartado> apartados = getApartadosDesempeño();
                    List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                    apartados.stream().forEach(a->{
                        //System.out.println("EjbInformeIntegralDocente.packEvDes "+ a.getContenido());
                        DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());

                        apartado.setNombreApartado(a.getContenido());
                        List<DtoInformeIntegralDocente.ResultadoPregunta>resultadosPreguntas = new ArrayList<>();
                        a.getPreguntas().stream().forEach(p->{
                            DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                            pregunta.setNumeroPregunta(p.getId());
                            pregunta.setPregunta(p.getTitulo());
                            pregunta.setNumeroPregunta(p.getNumero().toString());
                            pregunta.setPromedio(getResultadoPreguntaEvDes(resResultados.getValor(),p.getNumero().toString()).getValor());
                            pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                            // System.out.println("EjbInformeIntegralDocente.packEvDes ID:  "+ p.getNumero() +"  ---Titulo:        " +p.getTitulo()+ "Resultado "+pregunta.getPromedio());
                            resultadosPreguntas.add(pregunta);
                        });
                        apartado.setResultadoPreguntas(resultadosPreguntas);
                        apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                        apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                        resultadosApartados.add(apartado);
                    });
                    dto.setResultados(resultadosApartados);
                    dto.setPromedio(promedioEvaluacion(resultadosApartados).getValor());
                    dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                    return ResultadoEJB.crearCorrecto(dto   ,"Evaluación Desempeño empaquetada");

                }else {return ResultadoEJB.crearErroneo(4,dto,"Error al obtener los resultados de desempeño");}
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obetener la evaluación de Desempeño");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.packEvDes)", e, null);
        }
    }

    /**
     * Empaqueta los resultados de la evaluacion entre pares academicos
     * @param docente Docente seleccionadio
     * @param periodo Periodo seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<DtoInformeIntegralDocente.Pares> packEvPares (@NonNull Personal docente, @NonNull PeriodosEscolares periodo){
        try{
            //System.out.println("EjbInformeIntegralDocente.packEvPares");
            DtoInformeIntegralDocente.Pares dto = new DtoInformeIntegralDocente.Pares(new Evaluaciones(),new ArrayList<>(),new Double(0),new String());
            if(docente ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}
            if(periodo ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}

            //Obtiene la evaluacion por periodo
            ResultadoEJB<Evaluaciones> resEv = getEvalaucionbyTipoPeriodo(periodo, EvaluacionesTipo.EVALUACION_PARES_ACADEMICOS);
            //System.out.println("EjbInformeIntegralDocente.packEvPares Evaluacion "+ resEv.getValor());
            if(resEv.getCorrecto()){
                //Obtiene los resultados de la evalución
                ResultadoEJB<List<EvaluacionParesAcademicos>> resResultados = getResultadosEvParesAcademicos(resEv.getValor(),docente);
                //  System.out.println("EjbInformeIntegralDocente.packEvPares 1");
                if(resResultados.getCorrecto()){
                    List<Apartado> apartados= new ArrayList<>();
                    //Obtiene el apartado segun el perfil del docente (PA o PTC)
                    if(docente.getCategoriaOperativa().getCategoria()==32){apartados = ejbPares.getApartadosPTC();}
                    if(docente.getCategoriaOperativa().getCategoria()==(30)){apartados = ejbPares.getApartadosPA();}
                    // System.out.println("EjbInformeIntegralDocente.packEvPares 4" + apartados);
                    List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                    apartados.stream().forEach(a->{
                        // System.out.println("EjbInformeIntegralDocente.packEvPares "+ a.getContenido());
                        DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                        apartado.setNombreApartado(a.getContenido());
                        List<DtoInformeIntegralDocente.ResultadoPregunta>resultadosPreguntas = new ArrayList<>();
                        a.getPreguntas().stream().forEach(p->{
                            DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                            pregunta.setNumeroPregunta(p.getId());
                            pregunta.setPregunta(p.getTitulo());
                            pregunta.setNumeroPregunta(p.getNumero().toString());
                            pregunta.setPromedio(getResultadoPreguntaEvPares(resResultados.getValor(),p.getNumero().toString()).getValor());
                            pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                            //System.out.println("EjbInformeIntegralDocente.packEvPares ID:  "+ p.getNumero() +"  ---Titulo:        " +p.getTitulo()+ "Resultado "+pregunta.getPromedio());
                            resultadosPreguntas.add(pregunta);
                        });
                        apartado.setResultadoPreguntas(resultadosPreguntas);
                        apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                        apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                        resultadosApartados.add(apartado);
                    });
                    dto.setResultados(resultadosApartados);
                    dto.setPromedio(promedioEvaluacion(resultadosApartados).getValor());
                    dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                    return ResultadoEJB.crearCorrecto(dto   ,"Evaluación entre pares empaquetada");

                }else {return ResultadoEJB.crearErroneo(4,dto,"Error al obtener los resultados de desempeño");}
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obetener la evaluación de entre pares");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.packEvDes)", e, null);
        }
    }
    /**
     * Empaqueta los resultados de al tutor
     * @param docente Docente seleccionadio
     * @param periodo Periodo seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<DtoInformeIntegralDocente.Tutor> packEvTutor (@NonNull Personal docente, @NonNull PeriodosEscolares periodo){
        try{
            DtoInformeIntegralDocente.Tutor dto = new DtoInformeIntegralDocente.Tutor(new Evaluaciones(),Boolean.FALSE,new ArrayList<>(),new Double(0),new String());
            if(docente ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}
            if(periodo ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}

            //Obtiene la evaluacion por periodo
            ResultadoEJB<Evaluaciones> resEv = getEvalaucionTutorbyPeriodo(periodo);
            // System.out.println("EjbInformeIntegralDocente.packEvTutor Evaluacion "+ resEv.getValor() +"Tipo "+ resEv.getValor().getTipo());
            if(resEv.getCorrecto()){
                //Obtiene los resultados de la evalución tutor
                if(resEv.getValor().getTipo().equals(EvaluacionesTipo.TUTOR_1.getLabel())){
                    ResultadoEJB<List<EvaluacionTutoresResultados>> resResultados = getResultadosTutor1byDocente(docente,resEv.getValor());
                    //System.out.println("EjbInformeIntegralDocente.packEvTutor "+ resResultados.getValor());
                    if(resResultados.getCorrecto()){
                        List<EvaluacionTutoresResultados> resultadosCompletos = new ArrayList<>();
                        //Debe tomar sólo los los resultados completos
                        resResultados.getValor().stream().forEach(r->{
                                    Comparador<EvaluacionTutoresResultados> comparador = new ComparadorEvaluacionTutor();
                                    Boolean finalizado = comparador.isCompleto(r);
                                    if(finalizado==true){
                                        resultadosCompletos.add(r);
                                    }
                                }
                        );
                        //ystem.out.println("EjbInformeIntegralDocente.packEvTutor Completoa"+ resultadosCompletos.size());
                        //Apartados
                        List<Apartado>apartados = getApartadosTutorbyTipo(EvaluacionesTipo.TUTOR_1).getValor();
                        //Valor 4 en pregunta 5 es No aplica, por lo tanto no debe considerarse para el promedio por pregunta
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                           // System.out.println("EjbInformeIntegralDocente.packEvTutor "+ a.getContenido());
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            List<DtoInformeIntegralDocente.ResultadoPregunta>resultadosPreguntas = new ArrayList<>();
                            a.getPreguntas().stream().forEach(p->{
                                Integer ponderacion=new Integer(0);
                                if(resEv.getValor().getEvaluacion()>23){ponderacion=2;}else {ponderacion=1;}
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getId());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPromedio(getResultadoPreguntaEvTutorbyTipo(resultadosCompletos,new ArrayList<>(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.TUTOR_1,ponderacion).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                //System.out.println("EjbInformeIntegralDocente.packEvTutor ID:  "+ p.getNumero() +"  ---Titulo:        " +p.getTitulo()+ "Resultado "+pregunta.getPromedio());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        dto.setEsTutor(Boolean.TRUE);
                        dto.setResultados(resultadosApartados);
                        dto.setPromedio(promedioEvaluacion(resultadosApartados).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                    }else {
                        //No fue tutor en ese periodo
                        dto.setEsTutor(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(4,dto,"El docente seleccionado no fue tutor de grupo en el periodo seleccionado");
                    }

                }
                if(resEv.getValor().getTipo().equals(EvaluacionesTipo.TUTOR.getLabel())){
                    ResultadoEJB<List<EvaluacionTutoresResultados2>> resResultados = getResultadosTutor2byDocente(docente,resEv.getValor());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvTutor Resultados -- ");
                        //La lista ya sólo son resultados completos
                        //Apartados
                        List<Apartado>apartados = getApartadosTutorbyTipo(EvaluacionesTipo.TUTOR).getValor();
                        // Valor 0 en pregunta 6 es No aplica
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            //System.out.println("EjbInformeIntegralDocente.packEvTutor "+ a.getContenido());
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            List<DtoInformeIntegralDocente.ResultadoPregunta>resultadosPreguntas = new ArrayList<>();
                            a.getPreguntas().stream().forEach(p->{
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getId());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPromedio(getResultadoPreguntaEvTutorbyTipo(new ArrayList<>(),resResultados.getValor(),new ArrayList<>(),p.getNumero().toString(), EvaluacionesTipo.TUTOR,new Integer(0)).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());
                                //System.out.println("EjbInformeIntegralDocente.packEvTutor ID:  "+ p.getNumero() +"  ---Titulo:        " +p.getTitulo()+ "Resultado "+pregunta.getPromedio());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        dto.setResultados(resultadosApartados);
                        dto.setPromedio(promedioEvaluacion(resultadosApartados).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        dto.setEsTutor(Boolean.TRUE);
                    }else {
                        // System.out.println("EjbInformeIntegralDocente.packEvTutor Resultados NO-- ");

                        //No fue tutor en ese periodo
                        dto.setEsTutor(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(4,dto,"El docente seleccionado no fue tutor de grupo en el periodo seleccionado");
                    }
                }
                if (resEv.getValor().getTipo().equals(EvaluacionesTipo.TUTOR_2.getLabel())){
                    ResultadoEJB<List<EvaluacionTutoresResultados3>> resResultados = getResultadosTutor3byDocente(docente,resEv.getValor());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvTutor 2");
                        //La lista ya sólo son resultados completos
                        //Valor 0 en pregunta 5 y 6 es No aplica
                        List<Apartado>apartados = getApartadosTutorbyTipo(EvaluacionesTipo.TUTOR_2).getValor();
                        //System.out.println("EjbInformeIntegralDocente.packEvTutor " +apartados.size());
                        List<DtoInformeIntegralDocente.ResultadoApartado> resultadosApartados = new ArrayList<>();
                        apartados.stream().forEach(a->{
                            // System.out.println("EjbInformeIntegralDocente.packEvTutor "+ a.getContenido());
                            DtoInformeIntegralDocente.ResultadoApartado apartado = new DtoInformeIntegralDocente.ResultadoApartado(new String(),new ArrayList<>(),new Double(0),new String());
                            apartado.setNombreApartado(a.getContenido());
                            List<DtoInformeIntegralDocente.ResultadoPregunta>resultadosPreguntas = new ArrayList<>();
                            a.getPreguntas().stream().forEach(p->{
                                DtoInformeIntegralDocente.ResultadoPregunta pregunta = new DtoInformeIntegralDocente.ResultadoPregunta(new String(),new String(),new Double(0),new String());
                                pregunta.setNumeroPregunta(p.getId());
                                pregunta.setPregunta(p.getTitulo());
                                pregunta.setNumeroPregunta(p.getNumero().toString());
                                pregunta.setPromedio(getResultadoPreguntaEvTutorbyTipo(new ArrayList<>(),new ArrayList<>(),resResultados.getValor(),p.getNumero().toString(), EvaluacionesTipo.TUTOR_2,new Integer(0)).getValor());
                                pregunta.setStyle(getStilobyPromedio(pregunta.getPromedio()).getValor());

                                //System.out.println("EjbInformeIntegralDocente.packEvTutor ID:  "+ p.getNumero() +"  ---Titulo:        " +p.getTitulo()+ "Resultado "+pregunta.getPromedio());
                                resultadosPreguntas.add(pregunta);
                            });
                            apartado.setResultadoPreguntas(resultadosPreguntas);
                            apartado.setPromedioApartado(promedioApartado(resultadosPreguntas).getValor());
                            apartado.setStyle(getStilobyPromedio(apartado.getPromedioApartado()).getValor());
                            resultadosApartados.add(apartado);
                        });
                        dto.setResultados(resultadosApartados);
                        dto.setPromedio(promedioEvaluacion(resultadosApartados).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        dto.setEsTutor(Boolean.TRUE);

                    }else {
                        // System.out.println("EjbInformeIntegralDocente.packEvTutor No fue tutor");
                        //No fue tutor en ese periodo
                        dto.setEsTutor(Boolean.FALSE);

                    }
                }
                //System.out.println("EjbInformeIntegralDocente.packEvTutor DTO" + dto.getEsTutor());
                return ResultadoEJB.crearCorrecto(dto,"Evaluación a Tutor empaquetada");
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obetener la evaluación de tutor");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.packEvDes)", e, null);
        }
    }
    /**
     * Empaqueta los resultados de la evaluacion al desempeño Docente
     * @param docente Docente seleccionadio
     * @param periodo Periodo seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<DtoInformeIntegralDocente.Docente> packEvDocente (@NonNull Personal docente, @NonNull PeriodosEscolares periodo){
        try{
            //System.out.println("EjbInformeIntegralDocente.packEvDocente");
            DtoInformeIntegralDocente.Docente dto = new DtoInformeIntegralDocente.Docente(new Evaluaciones(),new ArrayList<>(),new Double(0.0),new String(),Boolean.FALSE);
            if(docente ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}
            if(periodo ==null){return ResultadoEJB.crearErroneo(2,dto,"La evaluación no debe ser nulo");}

            //Obtiene la evaluacion por periodo
            ResultadoEJB<Evaluaciones> resEv = getEvalaucionDocentebyPeriodo(periodo);
            //System.out.println("EjbInformeIntegralDocente.packEvDocente Evaluacion "+ resEv.getValor());
            if(resEv.getCorrecto()){
                //Revisar el tipo de evaluacion para obtener los apartados correctos
                if(resEv.getValor().getTipo().equals(EvaluacionesTipo.DOCENTE.getLabel())){
                    //Docente Materia
                    List<Apartado> apartados = new ArrayList<>();
                    apartados = getApartadosDocentebyTipo(EvaluacionesTipo.DOCENTE).getValor();
                    ResultadoEJB<List<String>> resResultados= getMaterias(docente,resEv.getValor());
                    // System.out.println("EjbInformeIntegralDocente.packEvDocente Materias "+resResultados);
                    ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> resMaterias = packResultadosMateria(apartados, EvaluacionesTipo.DOCENTE,resEv.getValor(),docente,new ArrayList<>(),resResultados.getValor(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente 1111");
                        dto.setFueDocente(Boolean.TRUE);
                        dto.setResultados(resMaterias.getValor());
                        dto.setEvaluacion(resEv.getValor());
                        dto.setPromedio(promedioEvaluacionDocente(resMaterias.getValor()).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        return ResultadoEJB.crearCorrecto(dto,"Evaluación docente empaquetada");
                    }else {
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente no docente 2");
                        dto.setFueDocente(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(3,dto,"El docente no fue evaluado en el periodo seleccionado");}
                }
                else if (resEv.getValor().getTipo().equals(EvaluacionesTipo.DOCENTE_1.getLabel())) {
                    //Docente Materia (Cuestionario 1)
                    List<Apartado> apartados = new ArrayList<>();
                    apartados= getApartadosDocentebyTipo(EvaluacionesTipo.DOCENTE_1).getValor();
                    ResultadoEJB<List<String>> resResultados= getMaterias1(docente,resEv.getValor());
                    // System.out.println("EjbInformeIntegralDocente.packEvDocente Materias "+resResultados);
                    ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> resMaterias = packResultadosMateria(apartados, EvaluacionesTipo.DOCENTE_1,resEv.getValor(),docente,new ArrayList<>(),resResultados.getValor(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente 1111");
                        dto.setFueDocente(Boolean.TRUE);
                        dto.setResultados(resMaterias.getValor());
                        dto.setEvaluacion(resEv.getValor());
                        dto.setPromedio(promedioEvaluacionDocente(resMaterias.getValor()).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        return ResultadoEJB.crearCorrecto(dto,"Evaluación docente empaquetada");
                    }else {
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente no docente 2");
                        dto.setFueDocente(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(3,dto,"El docente no fue evaluado en el periodo seleccionado");}
                }
                else if (resEv.getValor().getTipo().equals(EvaluacionesTipo.DOCENTE_2.getLabel())) {
                    //Docente Materia (Cuestionario 2 por contingencia)
                    List<Apartado> apartados = new ArrayList<>();
                    apartados = getApartadosDocentebyTipo(EvaluacionesTipo.DOCENTE_2).getValor();
                    ResultadoEJB<List<String>> resResultados= getMaterias2(docente,resEv.getValor());
                    // System.out.println("EjbInformeIntegralDocente.packEvDocente Materias "+resResultados);
                    ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> resMaterias = packResultadosMateria(apartados, EvaluacionesTipo.DOCENTE_2,resEv.getValor(),docente,new ArrayList<>(),new ArrayList<>(),resResultados.getValor(),new ArrayList<>(),new ArrayList<>());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente 1111");
                        dto.setFueDocente(Boolean.TRUE);
                        dto.setResultados(resMaterias.getValor());
                        dto.setEvaluacion(resEv.getValor());
                        dto.setPromedio(promedioEvaluacionDocente(resMaterias.getValor()).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        return ResultadoEJB.crearCorrecto(dto,"Evaluación docente empaquetada");
                    }else {
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente no docente 2");
                        dto.setFueDocente(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(3,dto,"El docente no fue evaluado en el periodo seleccionado");}

                }
                else if (resEv.getValor().getTipo().equals(EvaluacionesTipo.DOCENTE_3.getLabel())) {
                    //Docente Materia (Cuestionario 3 por contingencia)
                    List<Apartado> apartados = new ArrayList<>();
                    apartados = getApartadosDocentebyTipo(EvaluacionesTipo.DOCENTE_3).getValor();
                    ResultadoEJB<List<String>> resResultados= getMaterias3(docente,resEv.getValor());
                   // System.out.println("EjbInformeIntegralDocente.packEvDocente Materias "+apartados);
                    ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> resMaterias = packResultadosMateria(apartados, EvaluacionesTipo.DOCENTE_3,resEv.getValor(),docente,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),resResultados.getValor(),new ArrayList<>());
                    if(resResultados.getCorrecto()){
                       // System.out.println("EjbInformeIntegralDocente.packEvDocente 1111");
                        dto.setFueDocente(Boolean.TRUE);
                        dto.setResultados(resMaterias.getValor());
                        dto.setEvaluacion(resEv.getValor());
                        dto.setPromedio(promedioEvaluacionDocente(resMaterias.getValor()).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        return ResultadoEJB.crearCorrecto(dto,"Evaluación docente empaquetada");
                    }else {
                       // System.out.println("EjbInformeIntegralDocente.packEvDocente no docente 2");
                        dto.setFueDocente(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(3,dto,"El docente no fue evaluado en el periodo seleccionado");}
                } else if(resEv.getValor().getTipo().equals(EvaluacionesTipo.DOCENTE_4.getLabel())){
                    //Docente Materia (Cuestionario 4 por contingencia)
                    List<Apartado> apartados = new ArrayList<>();
                    apartados = getApartadosDocentebyTipo(EvaluacionesTipo.DOCENTE_4).getValor();
                    ResultadoEJB<List<String>> resResultados= getMaterias4(docente,resEv.getValor());
                    // System.out.println("EjbInformeIntegralDocente.packEvDocente Materias "+resResultados);
                    ResultadoEJB<List<DtoInformeIntegralDocente.ResultadosMateria>> resMaterias = packResultadosMateria(apartados, EvaluacionesTipo.DOCENTE_4,resEv.getValor(),docente,new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),resResultados.getValor());
                    if(resResultados.getCorrecto()){
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente 1111");
                        dto.setFueDocente(Boolean.TRUE);
                        dto.setResultados(resMaterias.getValor());
                        dto.setEvaluacion(resEv.getValor());
                        dto.setPromedio(promedioEvaluacionDocente(resMaterias.getValor()).getValor());
                        dto.setStyle(getStilobyPromedio(dto.getPromedio()).getValor());
                        return ResultadoEJB.crearCorrecto(dto,"Evaluación docente empaquetada");
                    }else {
                        //System.out.println("EjbInformeIntegralDocente.packEvDocente no docente 2");
                        dto.setFueDocente(Boolean.FALSE);
                        return ResultadoEJB.crearErroneo(3,dto,"El docente no fue evaluado en el periodo seleccionado");}
                }
                else {return ResultadoEJB.crearErroneo(3,dto,"Se aplico un cuestionario que no está considerada en el informe");}

            }else {return ResultadoEJB.crearErroneo(3,dto,"No se aplico evaluación docente en el periodo seleccionado");}
        }catch (Exception e ){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar la evaluación Docente (EjbInformeIntegralDocente.packEvDes)", e, null);
        }
    }

    /**
     * General el informe integral Docente
     * @param periodo Periodo Seleccionado
     * @param docente Docente seleccionado
     * @return Resultado del proceso
     */

    public ResultadoEJB<DtoInformeIntegralDocente.InformeIntegral> packInformeIntegral (@NonNull PeriodosEscolares periodo, @NonNull Personal docente){
        try{
            DtoInformeIntegralDocente.Docente dtoDocente = new DtoInformeIntegralDocente.Docente(new Evaluaciones(),new ArrayList<>(),new Double(0.0),new String(),Boolean.FALSE);
            DtoInformeIntegralDocente.Tutor dtoTutor = new DtoInformeIntegralDocente.Tutor(new Evaluaciones(),Boolean.FALSE,new ArrayList<>(),new Double(0),new String());
            DtoInformeIntegralDocente.Desempeño dtoDes = new DtoInformeIntegralDocente.Desempeño(new DesempenioEvaluaciones(),new ArrayList<>(),new Double(0),new String());
            DtoInformeIntegralDocente.Pares dtoPares = new DtoInformeIntegralDocente.Pares(new Evaluaciones(),new ArrayList<>(),new Double(0),new String());
            List<DtoInformeIntegralDocente.EvaluacionIntegral > dtoEvIntegral= new ArrayList<>();
            DtoInformeIntegralDocente.InformeIntegral dto = new DtoInformeIntegralDocente.InformeIntegral(new Personal(),new AreasUniversidad(),new AreasUniversidad(),dtoDes,dtoPares,dtoTutor,dtoDocente,dtoEvIntegral,new Double(0.0));
            if(periodo==null){return ResultadoEJB.crearErroneo(2,dto,"El periodo no debe ser nulo");}
            if(docente==null){return ResultadoEJB.crearErroneo(2,dto,"El docente no debe ser nulo");}
            //Empaqueta la evaluacion al desempeño
            ResultadoEJB<DtoInformeIntegralDocente.Desempeño> resDes = packEvDes(docente,periodo);
            //Empaqueta la evaluacion entre pares academicos
            ResultadoEJB<DtoInformeIntegralDocente.Pares> resPares = packEvPares(docente,periodo);
            //Empaqueta la evaluacion Tutor
            ResultadoEJB<DtoInformeIntegralDocente.Tutor> resTutor = packEvTutor(docente,periodo);
            //Empaqueta la evaluacion Docente
            ResultadoEJB<DtoInformeIntegralDocente.Docente> resDocente = packEvDocente(docente,periodo);
            dto.setDocente(docente);
            dto.setAreaAcademica(getAreabyClave(docente.getAreaSuperior()).getValor());
            dto.setAreaOperativa(getAreabyClave(docente.getAreaOperativa()).getValor());
            // Evaluacion Integral
            DtoInformeIntegralDocente.EvaluacionIntegral dtoDesIntegral= new DtoInformeIntegralDocente.EvaluacionIntegral(new Double(0.0),new String());
            DtoInformeIntegralDocente.EvaluacionIntegral dtoDocenteIntegral= new DtoInformeIntegralDocente.EvaluacionIntegral(new Double(0.0),new String());
            DtoInformeIntegralDocente.EvaluacionIntegral dtoTutorIntegral= new DtoInformeIntegralDocente.EvaluacionIntegral(new Double(0.0),new String());
            DtoInformeIntegralDocente.EvaluacionIntegral dtoParesIntegral= new DtoInformeIntegralDocente.EvaluacionIntegral(new Double(0.0),new String());
            if (resDes.getCorrecto() & resPares.getCorrecto()){
                //System.out.println("EjbInformeIntegralDocente.packInformeIntegral 1");
                if(resDocente.getCorrecto()){
                    // System.out.println("EjbInformeIntegralDocente.packInformeIntegral Fue docente");
                    //Fue docente en ese periodo
                    dtoDes = resDes.getValor();
                    dtoPares= resPares.getValor();
                    dtoDocente= resDocente.getValor();
                    dto.setEvDesempeño(dtoDes);
                    dto.setEvPares(dtoPares);
                    dto.setEvDocente(dtoDocente);
                    dto.setEvTutor(resTutor.getValor());
                    dtoDesIntegral.setNombreApartado("Desempeño");
                    dtoDesIntegral.setValor(promedioIntegral(dto.getEvDesempeño().getPromedio(),100.00,40).getValor());
                    dtoParesIntegral.setNombreApartado("Pares");
                    dtoParesIntegral.setValor(promedioIntegral(dto.getEvPares().getPromedio(),100.00,20).getValor());
                    //Comprueba si fue tutor para hacer los calculos correspondientes a la evaluacion integral
                    if(dto.getEvTutor().getEsTutor()==true){
                        //El valor es de 20 y 20 en docente y tutor
                        dtoDocenteIntegral.setNombreApartado("Estudiante/Docente");
                        dtoDocenteIntegral.setValor(promedioIntegral(dto.getEvDocente().getPromedio(),100.00,20).getValor());
                        dtoTutorIntegral.setNombreApartado("Estudiante/Tutor");
                        dtoTutorIntegral.setValor(promedioIntegral(dto.getEvTutor().getPromedio(),100.00,20).getValor());
                        dtoEvIntegral.add(dtoDocenteIntegral);
                        dtoEvIntegral.add(dtoTutorIntegral);

                    }else {
                        //Se queda solo Estudiante/Docente con el 40
                        dtoDocenteIntegral.setNombreApartado("Estudiante/Docente");
                        dtoDocenteIntegral.setValor(promedioIntegral(dto.getEvDocente().getPromedio(),100.00,40).getValor());
                        dtoEvIntegral.add(dtoDocenteIntegral);
                    }
                    dtoEvIntegral.add(dtoDesIntegral);
                    dtoEvIntegral.add(dtoParesIntegral);
                    dto.setEvIntegral(dtoEvIntegral);
                    dto.setPorcetanjeObtenido(dtoEvIntegral.stream().mapToDouble(d->d.getValor()).sum());
                    return ResultadoEJB.crearCorrecto(dto,"Evaluación Integral Docente generada con éxito");
                }else {
                    // System.out.println("EjbInformeIntegralDocente.packInformeIntegral No feu docente");
                    dto.setEvTutor(resTutor.getValor());
                    dto.setEvPares(resPares.getValor());
                    dto.setEvDesempeño(resDes.getValor());
                    //No fue docente en ese periodo
                    if(dto.getEvTutor().getEsTutor()==true){
                        //La evaluación a tutor se mantiene con 40%
                        dtoTutorIntegral.setNombreApartado("Estudiante/Tutor");
                        dtoTutorIntegral.setValor(promedioIntegral(dto.getEvTutor().getPromedio(),100.00,40).getValor());
                        dtoDesIntegral.setNombreApartado("Desempeño");
                        dtoDesIntegral.setValor(promedioIntegral(dto.getEvDesempeño().getPromedio(),100.00,40).getValor());
                        dtoParesIntegral.setNombreApartado("Pares");
                        dtoParesIntegral.setValor(promedioIntegral(dto.getEvPares().getPromedio(),100.00,20).getValor());
                        dtoEvIntegral.add(dtoTutorIntegral);
                    }else {
                        //No fue tutor, se le asigna el 70% a la evaluacion del desempeño y 30% a pares
                        dtoDesIntegral.setNombreApartado("Desempeño");
                        dtoDesIntegral.setValor(promedioIntegral(dto.getEvDesempeño().getPromedio(),100.00,70).getValor());
                        dtoParesIntegral.setNombreApartado("Pares");
                        dtoParesIntegral.setValor(promedioIntegral(dto.getEvPares().getPromedio(),100.00,30).getValor());
                    }
                    dtoEvIntegral.add(dtoDesIntegral);
                    dtoEvIntegral.add(dtoParesIntegral);
                    dto.setEvIntegral(dtoEvIntegral);
                    dto.setPorcetanjeObtenido(dtoEvIntegral.stream().mapToDouble(d->d.getValor()).sum());
                    return ResultadoEJB.crearCorrecto(dto,"Evaluación Integral Docente generada con éxito");
                }
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error en empaquetado de las evaluaciones");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar el informe integral (EjbInformeIntegralDocente.packInformeIntegral)", e, null);
        }
    }

}
