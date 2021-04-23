package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.*;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

@Stateless (name = " EjbEvaluacionParesAcademicos")
public class EjbEvaluacionParesAcademicos {

    @EJB
    EjbPersonalBean ejbPersonalBean;
    @EJB
    EjbPropiedades ep;
    @EJB
    Facade f;
    @EJB
    EjbValidacionRol ejbValidacionRol;
    private EntityManager em;
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Valida que la persona autenticada sea personal de psicopedagogía
     * @param clave Clave de trabajador autenticado
     * @return Resultado del proceso
     * Validación de personal que crea las combinaciones
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaPersonalFortalecimiento(Integer clave){
        return ejbValidacionRol.validarFortalecimiento(clave);
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
     * Valida que el usuario logueado tenga acceso . Tienen acceso al reporte:
     * - Secretaría Académica
     * - Director
     * - FDA
     * @param clave Clave de personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarAcceso(Integer clave){
        try{
            if(clave ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            else {
                PersonalActivo personalActivo = ejbPersonalBean.pack(clave);

                ResultadoEJB<PersonalActivo> resSA = validarSA(personalActivo);
                if(resSA.getCorrecto()==true){
                    //System.out.println("Rector" );
                    return ResultadoEJB.crearCorrecto(resSA.getValor(),"Validado como SA");}
                else {
                    //System.out.println("----" );
                    ResultadoEJB<PersonalActivo> resDir = validarDirAc(personalActivo);
                    if(resDir.getCorrecto()==true){
                        //System.out.println("Academica" +resSA.getValor() );
                        return ResultadoEJB.crearCorrecto(resDir.getValor(),"Validado como Director");}
                    else {
                        ResultadoEJB<Filter<PersonalActivo>> resFDA= validaPersonalFortalecimiento(personalActivo.getPersonal().getClave());
                        if(resFDA.getCorrecto()==true){
                            // System.out.println("Director de planeación" +resDirPlaneacion.getValor() );
                            return ResultadoEJB.crearCorrecto(resFDA.getValor().getEntity(),"Validado como Director/a de Planeacion");
                        }
                        else {
                            return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"");
                        }

                    }
                }

            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarAcceso)", e, null);

        }
    }
    /**
     * Valida que el usuario logueado tenga acceso . Tienen acceso al reporte:
     * - Secretaría Académica
     * - FDA
     * @param clave Clave de personal logueado
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> validarAccesoFDASA(Integer clave){
        try{
            if(clave ==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave no debe ser nula");}
            else {
                PersonalActivo personalActivo = ejbPersonalBean.pack(clave);

                ResultadoEJB<PersonalActivo> resSA = validarSA(personalActivo);
                if(resSA.getCorrecto()==true){
                    //System.out.println("Rector" );
                    return ResultadoEJB.crearCorrecto(resSA.getValor(),"Validado como SA");}
                else {
                    ResultadoEJB<Filter<PersonalActivo>> resFDA= validaPersonalFortalecimiento(personalActivo.getPersonal().getClave());
                        if(resFDA.getCorrecto()==true){
                            // System.out.println("Director de planeación" +resDirPlaneacion.getValor() );
                            return ResultadoEJB.crearCorrecto(resFDA.getValor().getEntity(),"Validado como Director/a de Planeacion");
                        }
                        else {
                            return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"");
                        }


                }

            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFichaAdmisionReporte.validarAcceso)", e, null);

        }
    }


    /**
     * Obtiene la evaluación activa de tipo Evaluación entre pares académicos
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getEvaluacionActiva(@NonNull Evaluaciones evaluacionSelect){
        try{
           // System.out.println("Entro a evaluacion actova");
            //Evaluaciones evaluacion =  new Evaluaciones();
            Evaluaciones evaluacion = em.createQuery("select e from Evaluaciones e where e.evaluacion=:eva and current_timestamp between e.fechaInicio and e.fechaFin", Evaluaciones.class)
                    .setParameter("eva", evaluacionSelect.getEvaluacion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
           // System.out.println("...."+ evaluacion);
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3,evaluacion,"No existe evaluación activa");}
            else{return ResultadoEJB.crearCorrecto(evaluacion,"Evaluación activa");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EjbEvaluacionParesAcademicos.getEvaluacionActiva)", e, null);
       }
    }
    /**
     * Obtiene la evaluación activa de tipo Evaluación entre pares académicos
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getEvaluacionActivaDoc(){
        try{
            // System.out.println("Entro a evaluacion actova");
            //Evaluaciones evaluacion =  new Evaluaciones();
            Evaluaciones evaluacion = em.createQuery("select e from Evaluaciones e where e.tipo=:tipo and current_timestamp between e.fechaInicio and e.fechaFin", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.EVALUACION_PARES_ACADEMICOS.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
           // System.out.println("...."+ evaluacion);
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3,evaluacion,"No existe evaluación activa");}
            else{return ResultadoEJB.crearCorrecto(evaluacion,"Evaluación activa");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EjbEvaluacionParesAcademicos.getEvaluacionActiva)", e, null);
        }
    }

    /**
     * Obtiene la última evalución activa
     * @return
     */
    public ResultadoEJB<Evaluaciones> getUltimaEvaluacionActiva(){
        try{
            Evaluaciones ev= new Evaluaciones();
            ev = em.createQuery("select e from Evaluaciones  e where e.tipo=:tipo order by e.periodo desc", Evaluaciones.class)
                .setParameter("tipo", EvaluacionesTipo.EVALUACION_PARES_ACADEMICOS.getLabel())
                .getResultStream()
                .findFirst()
                .orElse(null);
            ;
            if(ev==null){return ResultadoEJB.crearErroneo(2,new Evaluaciones(),"No se encontró evalución");}
            else {return ResultadoEJB.crearCorrecto(ev,"Evaluación");}
        }catch (Exception e){
                return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion por periodo(EjbEvaluacionParesAcademicos.getEvaluacionbyPeriodo)", e, null);

        }
    }

    /**
     * Obtiene la lista de evaluaciones
     * Tipo Evaluacion entre pares académicos
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Evaluaciones>> getEvaluaciones(){
        try{
            List<Evaluaciones> evaluaciones = new ArrayList<>();
            evaluaciones = em.createQuery("select e from Evaluaciones  e where e.tipo=:tipo order by e.periodo desc", Evaluaciones.class)
                .setParameter("tipo", EvaluacionesTipo.EVALUACION_PARES_ACADEMICOS.getLabel())
                .getResultList()
            ;
            if(evaluaciones.isEmpty() || evaluaciones==null){return ResultadoEJB.crearErroneo(2,evaluaciones,"No se encontraron evaluaciones");}
            else{return ResultadoEJB.crearCorrecto(evaluaciones,"Lista de evaluaciones");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evaluaciones(EjbEvaluacionParesAcademicos.getEvaluaciones)", e, null);

        }
    }

    ///////////////////////////////////////////METODOS PARA LAS COMBINACIONES DE LA EVALUACION////////////////////////////////////////////////////////////////////////

    /**
     * Obtiene la lista de periodos en las que se ha aplicado la evaluación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEvaluacion(){
        try{
            final List<PeriodosEscolares> periodos = em.createQuery("select ee from Evaluaciones ee where ee.tipo = :tipo order by ee.periodo desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.EVALUACION_PARES_ACADEMICOS.getLabel())
                    .getResultStream()
                    .map(eventoEscolar -> em.find(PeriodosEscolares.class, eventoEscolar.getPeriodo()))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(periodos, "Lista de periodos de la evaluación");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener lista de periodos (EjbEvaluacionParesAcademicos.getPeriodosEvaluacion)", e, null);
        }
    }

    /**
     * Obtiene la lista del personal docente PA  y PTC
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PersonalActivo>> getPersonalDocente(){
        try{
            List<PersonalActivo> doPersonalList1 = em.createQuery("select p from Personal p where p.status<>:status and (p.categoriaOperativa.categoria=:cat1 or p.categoriaOperativa.categoria=:cat2) order by p.nombre asc", Personal.class)
                    .setParameter("status",'B')
                    .setParameter("cat1",30)
                    .setParameter("cat2",32)
                    .getResultStream()
                    .map(p->ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
            ;
            if(doPersonalList1.isEmpty() || doPersonalList1==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"");}
            else {return ResultadoEJB.crearCorrecto(doPersonalList1,"Lista de ");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de docentes(EjbEvaluacionParesAcademicos.getPeriodosEvaluacion)", e, null);
        }
    }


    /**
     * Obtiene la lista de áreas academicas
     * @return
     */
    public ResultadoEJB<List<AreasUniversidad>> getAreasAcademicas(){
        try{
            List<AreasUniversidad> areasAcademicas = new ArrayList<>();
            areasAcademicas = em.createQuery("select a from AreasUniversidad  a where a.areaSuperior=:area and a.categoria.categoria=:categoria and a.vigente=:vigente order by  a.nombre desc", AreasUniversidad.class)
                    .setParameter("area", 2)
                    .setParameter("categoria", 8)
                    .setParameter("vigente","1")
                    .getResultList()
            ;
            //Obtiene área de Ideomas
            AreasUniversidad idiomas = em.createQuery("select a from AreasUniversidad a where a.area=:area", AreasUniversidad.class)
                    .setParameter("area",23)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(areasAcademicas.isEmpty() || areasAcademicas ==null || idiomas==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Areas académicas inexistentes");}
            else {
                areasAcademicas.add(idiomas);
                return ResultadoEJB.crearCorrecto(areasAcademicas,"Lista de áreas académicas"); }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de docentes por area(EjbEvaluacionParesAcademicos.getPersonalDocenteb|yArea)", e, null);
        }
    }

    /**
     * Obtiene lista de docentes PA y PTC por área
     * @param area Area seleccioanda
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PersonalActivo>> getPersonalDocentebyArea(@NonNull AreasUniversidad area){
        try{
            //
            // System.out.println("Entro a getPersonalDocentebyArea-->"+ area.getSiglas());
            List<PersonalActivo> personalList = new ArrayList<>();
            if(area==null){return ResultadoEJB.crearErroneo(2,personalList,"El área no debe ser nula");}
            List<PersonalActivo> doPersonalList1 = em.createQuery("select p from Personal p where p.areaSuperior=:area and p.status<>:status and (p.categoriaOperativa.categoria=:cat1 or p.categoriaOperativa.categoria=:cat2) order by p.nombre asc", Personal.class)
                    .setParameter("status",'B')
                    .setParameter("area",area.getArea())
                    .setParameter("cat1",30)
                    .setParameter("cat2",32)
                    .getResultStream()
                    .map(p->ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
                    ;

            /*List<PersonalActivo> docentes = em.createQuery("select p from Personal p where p.categoriaOperativa.categoria=:cat1 or p.categoriaOperativa.categoria=:cat2 and p.areaSuperior=:area", Personal.class)
                    .setParameter("cat1", 30)
                    .setParameter("cat2",32)
                    .setParameter("area",area.getArea())
                    .getResultStream()
                    .map(p -> ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
                    */
           // docentes.stream().filter(p->p.getPersonal().getStatus().equals('R')& p.getPersonal().getStatus().equals('A'));
           // System.out.println("Lista -->"+ doPersonalList1);
            if(doPersonalList1.isEmpty() || doPersonalList1==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No existen docentes en el área seleccionada");}
            else {return ResultadoEJB.crearCorrecto(doPersonalList1,"Lista de docentes por área");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de docentes por area(EjbEvaluacionParesAcademicos.getPersonalDocentebyArea)", e, null);
        }
    }
    public ResultadoEJB<List<EvaluacionParesAcademicos>> getCombinaciones(@NonNull Evaluaciones evaluacion){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nula");}
            List<EvaluacionParesAcademicos> combinaciones= em.createQuery("select e from EvaluacionParesAcademicos e where e.evaluacionParesAcademicosPK.evaluacion=:evaluacion and e.combinacionValidada=:val", EvaluacionParesAcademicos.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("val",true)
                    .getResultList()
                    ;
            if(combinaciones.isEmpty() || combinaciones==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No se encontraron combinaciones de la evaluación seleccionada");}
            else {return ResultadoEJB.crearCorrecto(combinaciones,"Combinaciones");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de combinaciones por evaluacion(EjbEvaluacionParesAcademicos.getCombinaciones)", e, null);
        }
    }

    /**
     * Empaqueta
     * @param evaluador
     * @param evaluado
     * @param  evaluacion
     * @return
     */
    public ResultadoEJB<DtoEvaluacionParesAcademicos> packDto(@NonNull Evaluaciones evaluacion, @NonNull PersonalActivo evaluador, @NonNull PersonalActivo evaluado){
        try {
            //System.out.println("Evaluaciooon --"+evaluacion.getEvaluacion());
            if (evaluador == null) {
                return ResultadoEJB.crearErroneo(2, new DtoEvaluacionParesAcademicos(), "El evaluador no debe ser nulo");
            }
            if (evaluado == null) {
                return ResultadoEJB.crearErroneo(2, new DtoEvaluacionParesAcademicos(), "El evaluado no debe ser nulo");
            }
            DtoEvaluacionParesAcademicos dto = new DtoEvaluacionParesAcademicos();
            dto.setEvaluador(evaluador);
            dto.setEvaluado(evaluado);
            dto.setCategoriaEvaluador(evaluador.getPersonal().getCategoriaOperativa());
            dto.setCategoriaEvaluado(evaluado.getPersonal().getCategoriaOperativa());

            //Busca si existe la combinacion
            ResultadoEJB<EvaluacionParesAcademicos> resResultados = getCombinacionEvaluadorEvaluado(evaluacion,evaluador,evaluado);
            //System.out.println("Combinación ........."+ resResultados.getValor());
            if(resResultados.getCorrecto()){
                dto.setCombinacion(resResultados.getValor());
                if(resResultados.getValor().getR1()!=null || resResultados.getValor().getCombinacionValidada()==true){
                   // System.out.println("Se puede editar "+ resResultados.getValor().getCombinacionValidada());
                    dto.setEditable(Boolean.TRUE);}else {dto.setEditable(Boolean.FALSE);}
                return ResultadoEJB.crearCorrecto(dto,"Empaquetado");
            }else {return ResultadoEJB.crearErroneo(3,dto,"Error al obenter los resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar (EjbEvaluacionParesAcademicos.packDto)", e, null);

        }
    }

    /**
     * Ordena aleatoriamente la lista de docentes a evaluados
     * Tomando en cuenta la condiciones:
     * Cada docente debe evaluar min 3 o max 5 docentes de la misma categoría del evaluador (PTC o PA)
     * @param personalFiltrado Lista de docentes activos a evaluar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PersonalActivo>> shuffleEvaluados(@NonNull List<PersonalActivo> personalFiltrado){
        try{
            List<PersonalActivo> evaluados = new ArrayList<>();
           // System.out.println("Personal filtrado que recibe -->"+ personalFiltrado.size());
            // Mezcla aleatoriamente los docentes a evaluar
            Collections.shuffle(personalFiltrado);
            //System.out.println("Lista r!"+ personalFiltrado);
            //Genera 5 números aleatorios
            Integer num1 =(int)(Math.random()*personalFiltrado.size());
           // System.out.println("Número 1 "+ num1);
            Integer num2 =(int)(Math.random()*personalFiltrado.size());
           // System.out.println("Número 2 "+num2);
            Integer num3 =(int)(Math.random()*personalFiltrado.size());
           // System.out.println("Número 3 "+num3);
            Integer num4 =(int)(Math.random()*personalFiltrado.size());
           // System.out.println("Número 2 "+num4);
            Integer num5 =(int)(Math.random()*personalFiltrado.size());
            //System.out.println("Número 3 "+num5);
            if(personalFiltrado.size()>5){
                //solo toma 5 docentes para evaluar
               // System.out.println("Evaluado ->" + personalFiltrado.get(0));
                //System.out.println("Lista en 5 --<" +evaluados.size());
                evaluados.add(personalFiltrado.get(num1));
                evaluados.add(personalFiltrado.get(num2));
                evaluados.add(personalFiltrado.get(num3));
                evaluados.add(personalFiltrado.get(num4));
                evaluados.add(personalFiltrado.get(num5));
                return ResultadoEJB.crearCorrecto(evaluados,"Lista de evaluados");
            }else if(personalFiltrado.size()==4){
                //toma 3 los docentes
                evaluados.add(personalFiltrado.get(num1));
                evaluados.add(personalFiltrado.get(num2));
                evaluados.add(personalFiltrado.get(num3));
               // System.out.println("Lista en 3 --<" +evaluados.size());
                return ResultadoEJB.crearCorrecto(evaluados,"Lista de evaluados");
            }
            else if (personalFiltrado.isEmpty()|| personalFiltrado==null){
                //System.out.println("No hay docentes de la misma categoria en su area");
                return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"No existen docentes de la misma categoría de su área");
            }else {
                //deja como evaluados los que tiene, son menos de 3
                evaluados = personalFiltrado;
                return ResultadoEJB.crearCorrecto(evaluados,"Lista de evaluados");

            }
        }catch ( Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al generar aleatorio(EjbEvaluacionParesAcademicos.suffleListaEmpaqueta)", e, null);

        }
    }

    /**
     * Empaqueta la lista de evaluados con su evaluador
     * @param evaluador Docente evaluador
     * @param evaluados Lista de docentes a evaluar
     * @return
     */
    public ResultadoEJB<List<DtoEvaluacionParesAcademicos>> empaquetarEvaluadosEvaluador(@NonNull Evaluaciones evaluacion, @NonNull PersonalActivo evaluador, @NonNull List<PersonalActivo> evaluados){
        try{
            //System.out.println("Entro a empaquetar" + evaluados.size());
            if(evaluador==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El evaluador no debe ser nulo");}
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluacion no debe ser nula");}
            if(evaluados==null || evaluados.isEmpty()){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El evaluados no deben ser nulos");}
            List<DtoEvaluacionParesAcademicos> listaEmpaquetada = new ArrayList<>();
            evaluados.stream().forEach(e->{
                //System.out.println("1-->");
                ResultadoEJB<DtoEvaluacionParesAcademicos> pack = packDto(evaluacion,evaluador,e);
                //System.out.println("Empaquetado->"+ pack.getValor() );
                if(pack.getCorrecto()){
                   // System.out.println("---");
                    listaEmpaquetada.add(pack.getValor());
                }else {
                    System.out.println("Error al empaquetar-- empaquetarEvaluadosEvaluador ");
                }
            });
            return ResultadoEJB.crearCorrecto(listaEmpaquetada,"Lista empaquetada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar(EjbEvaluacionParesAcademicos.empaquetarEvaluadosEvaluador)", e, null);

        }
    }

    /**
     * Busca los resultados de la evaluación ppor evaluacion, evaluador y evaluado
     * Si no existen resultados los crea
     * @param evaluacion Evaluaión seleccionada
     * @param evaluador Docente evaluador
     * @param evaluado Evaluado
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvaluacionParesAcademicos> getCombinacionEvaluadorEvaluado(@NonNull Evaluaciones evaluacion, @NonNull PersonalActivo evaluador, @NonNull PersonalActivo evaluado){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"La evaluación no debe ser nula");}
            if(evaluador==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"El evaluador no debe ser nulo");}
            if(evaluado==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"El evaluado no debe ser nulo");}
            EvaluacionParesAcademicos ev = new EvaluacionParesAcademicos();
            ev= em.createQuery("select e from EvaluacionParesAcademicos e where e.evaluacionParesAcademicosPK.evaluacion=:evaluacion and e.evaluacionParesAcademicosPK.evaluador=:evaluador and e.evaluacionParesAcademicosPK.evaluado=:evaluado", EvaluacionParesAcademicos.class)
            .setParameter("evaluacion",evaluacion.getEvaluacion())
            .setParameter("evaluador", evaluador.getPersonal().getClave())
            .setParameter("evaluado",evaluado.getPersonal().getClave())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
            //System.out.println("Resultado de consulta->"+ev);
            if(ev==null){
                EvaluacionParesAcademicos resEvaluacionParesAcademicos = new EvaluacionParesAcademicos();
                EvaluacionParesAcademicosPK pk= new EvaluacionParesAcademicosPK();
                pk.setEvaluacion(evaluacion.getEvaluacion());
                pk.setEvaluador(evaluador.getPersonal().getClave());
                pk.setEvaluado(evaluado.getPersonal().getClave());
                resEvaluacionParesAcademicos.setEvaluacionParesAcademicosPK(pk);
                resEvaluacionParesAcademicos.setCompleto(false);
                resEvaluacionParesAcademicos.setCombinacionValidada(false);
                em.persist(resEvaluacionParesAcademicos);
                em.flush();
                return ResultadoEJB.crearCorrecto(resEvaluacionParesAcademicos,"Se ha creado la combinación con éxito");
            }else {
                return ResultadoEJB.crearCorrecto(ev,"Ya existe una combinación, seleccione otro evaluado");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener la combinación(EjbEvaluacionParesAcademicos.getCombinacionEvaluadorEvaluado)", e, null);
        }
    }

    /**
     * Obtiene al personal activo por clave
     * @param clave Clave
     * @return Resultado del proceso
     */
    public ResultadoEJB<PersonalActivo> getPersonalbyClave(@NonNull Integer clave){
        try{
           // System.out.println("Clave que recibe "+clave);
            if(clave==null){return ResultadoEJB.crearErroneo(2,new PersonalActivo(),"La clave del docente no debe ser nulo");}
           return ResultadoEJB.crearCorrecto(ejbPersonalBean.pack(clave),"");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener al personal (EjbEvaluacionParesAcademicos.getPersonalbyClave)", e, null);
        }
    }

    /**
     * Empaqueta la lista de combinaciones encontradas
     * @param combinaciones Combinaciones
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEvaluacionParesAcademicos>> empaquetaCombinacion(@NonNull List<EvaluacionParesAcademicos> combinaciones){
        try{
            if (combinaciones.isEmpty()|| combinaciones==null){return ResultadoEJB.crearErroneo(2, new ArrayList<>(), "La lista de combinaciones no debe ser nula");}
            List<DtoEvaluacionParesAcademicos> combinacionesEmpaquetadas = new ArrayList<>();
            //Recorre la lista de combinaciones
            combinaciones.stream().forEach(c->{
                DtoEvaluacionParesAcademicos dto = new DtoEvaluacionParesAcademicos();
                //Busca al evaluador y al evaluado por clave
                ResultadoEJB<PersonalActivo> resEvaluador = getPersonalbyClave(c.getEvaluacionParesAcademicosPK().getEvaluador());
                ResultadoEJB<PersonalActivo> resEvaluado = getPersonalbyClave(c.getEvaluacionParesAcademicosPK().getEvaluado());
               // System.out.println("Evaluador ->"+resEvaluador.getValor()+ " Evaluado->"+ resEvaluado.getValor());
                if(resEvaluador.getCorrecto() & resEvaluado.getCorrecto()){
                    dto.setEvaluador(resEvaluador.getValor());
                    dto.setEvaluado(resEvaluado.getValor());
                    dto.setCombinacion(c);
                    dto.setCategoriaEvaluador(resEvaluador.getValor().getPersonal().getCategoriaOperativa());
                    dto.setCategoriaEvaluado(resEvaluado.getValor().getPersonal().getCategoriaOperativa());
                    if(c.getR1()!=null){dto.setEditable(Boolean.FALSE);}else {dto.setEditable(Boolean.TRUE);}
                    combinacionesEmpaquetadas.add(dto);
                }else {
                    System.out.println("Error al obtener el personal");
                }
            });
            return ResultadoEJB.crearCorrecto(combinacionesEmpaquetadas,"Lista de combinaciones empaquetadas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al empaquetar las combinaciones(EjbEvaluacionParesAcademicos.empaquetaCombinacion)", e, null);

        }
    }

    public ResultadoEJB<List<EvaluacionParesAcademicos>> getResultadosbyEvaluador(@NonNull Evaluaciones evaluacion, @NonNull PersonalActivo evaluador){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluación no debe ser nula");}
            if(evaluador==null){return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"El evaluador no debe ser nulo");}
            List<EvaluacionParesAcademicos> resusultados= new ArrayList<>();
            resusultados = em.createQuery("select e from EvaluacionParesAcademicos  e where e.evaluacionParesAcademicosPK.evaluacion=:ev and e.evaluacionParesAcademicosPK.evaluador=:evadualor order by e.evaluacionParesAcademicosPK.evaluado asc ", EvaluacionParesAcademicos.class)
            .setParameter("ev",evaluacion.getEvaluacion())
            .setParameter("evadualor",evaluador.getPersonal().getClave())
            .getResultList()
            ;
            if(resusultados.isEmpty() || resusultados==null){return ResultadoEJB.crearErroneo(4,resusultados,"No existen combinaciones del doceente");}
            else {return ResultadoEJB.crearCorrecto(resusultados,"Combinaciones encontradas");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resutados por evaluador(EjbEvaluacionParesAcademicos.getResultadosbyEvaluador)", e, null);

        }
    }
    public ResultadoEJB<List<DtoEvaluacionParesAcademicos>> generaCombinacionesbyArea(@NonNull AreasUniversidad area, @NonNull Evaluaciones evaluacion){
        try{
            if(area==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"El área no debe ser nula");}
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new ArrayList<>(),"La evaluacion no debe ser nula");}
            List<DtoEvaluacionParesAcademicos> combinaciones = new ArrayList<>();
            ResultadoEJB<List<PersonalActivo>> resDocentes = getPersonalDocentebyArea(area);
                //System.out.println("docentes "+resDocentes.getValor());

                if (resDocentes.getCorrecto()) {
                    //Recorre la lista de personal y obtiene cuantos docentes hay en su area de su categoria
                    resDocentes.getValor().stream().forEach(p->{
                        //COMBROBAR SI EXISTEN COMBINACIONES CREADAS
                        ResultadoEJB<List<EvaluacionParesAcademicos>> resResultados= getResultadosbyEvaluador(evaluacion,p);
                        if(resResultados.getCorrecto()){
                            ResultadoEJB<List<DtoEvaluacionParesAcademicos>> resEm = empaquetaCombinacion(resResultados.getValor());
                            if(resEm.getCorrecto()){combinaciones.addAll(resEm.getValor());}
                            else { System.out.println("Error al empaquetar combinaciones existentes"); }
                        }else {
                            //filtrar a los docentes de su categoria
                            List<PersonalActivo> filtrado = resDocentes.getValor().stream().filter(d->d.getPersonal().getClave()!=p.getPersonal().getClave())
                                    .filter(c->c.getPersonal().getCategoriaOperativa().getCategoria().equals(p.getPersonal().getCategoriaOperativa().getCategoria()))
                                    .collect(Collectors.toList());
                            ;
                            //Genera la lista de docentes que debe evaluar
                            ResultadoEJB<List<PersonalActivo>> resEvaluados= shuffleEvaluados(filtrado);
                            //System.out.println("Lista random -->"+resEvaluados.getValor().size());
                            if(resEvaluados.getCorrecto()){
                                //Empaqueta al evaluador junto con sus evaluados
                                ResultadoEJB<List<DtoEvaluacionParesAcademicos>> pack = empaquetarEvaluadosEvaluador(evaluacion,p,resEvaluados.getValor());
                                //System.out.println("Lista empaquetada"+pack.getValor());
                                if(pack.getCorrecto()){
                                    combinaciones.addAll(pack.getValor());
                                }else {
                                    System.out.println("Error al generar la listas empaquetadas");
                                }
                            }else {
                                System.out.println("Error al obetener la lista de avaluados");
                            }
                        }

                    });
                    return ResultadoEJB.crearCorrecto(combinaciones,"Combinaciones generadas");
                }else {
                    System.out.println("Error al obenter la lista de docentes por área");
                return ResultadoEJB.crearErroneo(3,new ArrayList<>(),"Errror al obtener la lista de docentes por area");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al generar combinaciones por area(EjbEvaluacionParesAcademicos.generaCombinacionesbyArea)", e, null);

        }
    }

    /**
     * Permite identificar a una lista de posibles docentes de la misma categoria y área para seleccionar al evaluador
     * @param evaluador  Docente evaluador
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(PersonalActivo evaluador){
        try{
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<PersonalActivo> docentes = em.createQuery("select p from Personal p where p.areaSuperior=:area and p.status<>:status and p.categoriaOperativa.categoria=:cat order by p.nombre asc", Personal.class)
                    .setParameter("status",'B')
                    .setParameter("area",evaluador.getAreaSuperior().getArea())
                    .setParameter("cat",evaluador.getPersonal().getCategoriaOperativa().getCategoria())
                    .getResultStream()
                    .map(p->ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
            ;
            List<PersonalActivo> filtrado = docentes.stream().filter(d->d.getPersonal().getClave()!=evaluador.getPersonal().getClave())
                    .collect(Collectors.toList());
            ;
            //System.out.println("Lista2...." + filtrado.size());
            return ResultadoEJB.crearCorrecto(filtrado, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbAsignacionAcademica.buscarDocente)", e, null);
        }
    }

    /**
     *
     * @param combinacion
     * @return
     */
     public ResultadoEJB<EvaluacionParesAcademicos> operacionCombinacion(@NonNull Evaluaciones ev, DtoEvaluacionParesAcademicos combinacion, PersonalActivo evaluado, Operacion operacion){
        try{
            if(combinacion==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"La combinacion no debe ser nula");}
            switch (operacion){
                case ACTUALIZAR:
                    ResultadoEJB<EvaluacionParesAcademicos> resGet = getCombinacionEvaluadorEvaluado(ev,combinacion.getEvaluador(),combinacion.getEvaluado());
                    if(resGet.getCorrecto()){
                        em.remove(resGet.getValor());
                        em.flush();
                        return getCombinacionEvaluadorEvaluado(ev,combinacion.getEvaluador(),evaluado);
                       /* EvaluacionParesAcademicos com = new EvaluacionParesAcademicos();
                        EvaluacionParesAcademicosPK pk = new EvaluacionParesAcademicosPK();
                        pk.setEvaluacion(ev.getEvaluacion());
                        pk.setEvaluador(combinacion.getEvaluador().getPersonal().getClave());
                        pk.setEvaluado(evaluado.getPersonal().getClave());
                        com.setEvaluacionParesAcademicosPK(pk);
                        com.setCombinacionValidada(false);
                        com.setCompleto(false);
                        em.persist(com);
                        em.flush();

                       return ResultadoEJB.crearCorrecto(com,"Cobinación actualizada");
                       */
                    }else {return ResultadoEJB.crearErroneo(3,new EvaluacionParesAcademicos(), "Error");}


                case ELIMINAR:
                    ResultadoEJB<EvaluacionParesAcademicos> resDel = getCombinacionEvaluadorEvaluado(ev,combinacion.getEvaluador(),combinacion.getEvaluado() );
                    if(resDel.getCorrecto()){
                        em.remove(resDel.getValor());
                        em.flush();
                        return ResultadoEJB.crearCorrecto(resDel.getValor(),"Combinación eliminada correctamente");
                    }

                case PERSISTIR:
                    //Comprueba que la combinacion no exista
                    ResultadoEJB<EvaluacionParesAcademicos> resSave = getCombinacionEvaluadorEvaluado(ev,combinacion.getEvaluador(),combinacion.getEvaluado() );
                    if(resSave.getCorrecto()){

                    }

                    EvaluacionParesAcademicos eva = new EvaluacionParesAcademicos();
                    EvaluacionParesAcademicosPK pka = new EvaluacionParesAcademicosPK();
                    pka.setEvaluacion(ev.getEvaluacion());
                    pka.setEvaluador(combinacion.getEvaluador().getPersonal().getClave());
                    pka.setEvaluado(combinacion.getEvaluado().getPersonal().getClave());
                    eva.setEvaluacionParesAcademicosPK(pka);
                    eva.setCompleto(false);
                    eva.setCombinacionValidada(false);
                    em.persist(eva);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(eva,"Combinación creada correctamente");

                default:
                    return ResultadoEJB.crearErroneo(3,combinacion.getCombinacion(),"Error al "+operacion.getLabel()+ "la combinación");

            }


        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la combinacion. (EjbAsignacionAcademica.buscarDocente)", e, null);
        }
     }

    /**
     * Valida una combinación
     * @param ev Evaluacion
     * @param combinacion Dto de la combinación
     * @return Resultado del proceso
     */
     public ResultadoEJB<EvaluacionParesAcademicos> validarCombinaciones (@NonNull Evaluaciones ev, @NonNull DtoEvaluacionParesAcademicos combinacion){
         try{
             if(combinacion==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"La combinación  no debe ser nula");}
             ResultadoEJB<EvaluacionParesAcademicos> resGet = getCombinacionEvaluadorEvaluado(ev,combinacion.getEvaluador(),combinacion.getEvaluado());
             if(resGet.getCorrecto()){
                resGet.getValor().setCombinacionValidada(true);
                em.merge(resGet.getValor());
                em.flush();
                return ResultadoEJB.crearCorrecto(resGet.getValor(),"Se ha validado la combinación con éxito");
             }else {return ResultadoEJB.crearErroneo(3,resGet.getValor(),"No se encontró combinación");}
         }catch ( Exception e){
             return ResultadoEJB.crearErroneo(1, "No se pudo validar la combinacion. (EjbAsignacionAcademica.validarCombinaciones)", e, null);

         }

     }
     ////////////////////////////////////////////////////// --CUESTIONARIO ---//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Valida que la persona autenticada sea personal docente
     * @param clave Clave de trabajador autenticado
     * @return Resultado del proceso
     * Validación de personal que crea las combinaciones
     */
    public ResultadoEJB<Filter<PersonalActivo>> validaPersonalDocente(Integer clave){
        return ejbValidacionRol.validarDocente(clave);
    }
    /**
     * Apartados para evaluación para docentes PTC
     * @return
     */
    public List<Apartado> getApartadosPTC() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,1f, "Realiza las actividades que se le requieran en tiempo y forma a las que fue comisionado.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,2f, "Entrega reportes, evidencias, evaluaciones y/u otros aspectos que se requieran en tiempo y forma.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,3f, "Demuestra su compromiso al llegar con puntualidad a las reuniones de trabajo.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,4f, "Participa activamente en los proyectos de investigación con publicaciones científicas.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,5f, "Realiza movilidades docentes que enriquezcan los proyectos de investigación", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,6f, "Propone actividades y acciones innovadoras, retadoras, colectivas y adecuadas al beneficio del logro de los objetivos y propósitos planteados por la institución.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,7f, "Atiende oportunamente de manera eficaz y apegado a las normas y lineamientos institucionales los diferentes problemas e imprevistos que se presentan en los distintos escenarios.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,8f, "Comparte y apoya a sus compañeros en los conocimientos que posee y/o adquirió en los programas de desarrollo previstos.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,9f, "Ofrece diferentes criterios que permiten mejorar su trabajo y el de sus compañeros.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,10f, "Expresa su opinión y punto de vista de manera atenta y respetuosa a todos los miembros de la institución.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,11f, "Integra a los miembros de la comunidad en la toma de decisiones, resolución de conflictos y en la elaboración de las propuestas y/o actividades que le soliciten.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,12f, "Logra coordinar y organizar actividades conjuntas con los miembros de la comunidad con los que le toque trabajar.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,13f, "Genera un buen ambiente laboral con los miembros del grupo. ", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,14f, "Cumple acuerdos establecidos, así como el reglamento interno de la institución en la aplicación de sus funciones.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,15f, "Hace uso adecuado de la información que tiene a su cargo respetando el contrato de confidencialidad y la protección de datos del usuario.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,16f, "Reconoce que la información a la que tiene acceso es clasificada y confidencial.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,17f, "Actúa de manera autónoma, responsable constructiva, profesional y ética sin la necesidad de supervisión y/o seguimiento.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,18f, "Presenta una estructura, contenido, precisión y limpieza en el trabajo encomendado para el desarrollo de sus funciones.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,19f, "Realiza las actividades de gestión que le corresponden para lograr el adecuado funcionamiento del equipo. ", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,20f, "Evita el retraso de las acciones a desempeñar en las funciones que le corresponden.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,21f, "Apoya a sus compañeros en las tareas que les corresponden para el logro de los objetivos en común.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,22f, "Distribuyen adecuadamente las actividades a realizar con el fin de hacer más eficiente los procesos, fortalecer los perfiles y evitar cargas administrativas.", ""));

        l.add(a1);
        return l;
    }
    /**
     * Apartados para evaluación para docentes PA
     * @return
     */
    public List<Apartado> getApartadosPA() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,1f, "Realiza las actividades que se le requieran en tiempo y forma a las que fue comisionado.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,2f, "Entrega reportes, evidencias, evaluaciones y/u otros aspectos que se requieran en tiempo y forma.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,3f, "Demuestra su compromiso al llegar con puntualidad a las reuniones de trabajo.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,4f, "Participa activamente en los proyectos de investigación con publicaciones científicas.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,5f, "Realiza movilidades docentes que enriquezcan los proyectos de investigación", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,6f, "Propone actividades y acciones innovadoras, retadoras, colectivas y adecuadas al beneficio del logro de los objetivos y propósitos planteados por la institución.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,7f, "Atiende oportunamente de manera eficaz y apegado a las normas y lineamientos institucionales los diferentes problemas e imprevistos que se presentan en los distintos escenarios.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,8f, "Comparte y apoya a sus compañeros en los conocimientos que posee y/o adquirió en los programas de desarrollo previstos.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,9f, "Ofrece diferentes criterios que permiten mejorar su trabajo y el de sus compañeros.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,10f, "Expresa su opinión y punto de vista de manera atenta y respetuosa a todos los miembros de la institución.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,11f, "Integra a los miembros de la comunidad en la toma de decisiones, resolución de conflictos y en la elaboración de las propuestas y/o actividades que le soliciten.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,12f, "Logra coordinar y organizar actividades conjuntas con los miembros de la comunidad con los que le toque trabajar.", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.PARES_ACADEMICOS, 1f,13f, "Genera un buen ambiente laboral con los miembros del grupo. ", ""));
        l.add(a1);
        return l;
    }
    /**
     * Posibles respuestas
     * @return
     */
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("4", "De acuerdo", "De acuerdo"));
        l.add(new SelectItem("3", "Parcialmente de acuerdo", "Parcialmente de acuerdo"));
        l.add(new SelectItem("2", "Parcialmente en desacuerdo", "Parcialmente en desacuerdo"));
        l.add(new SelectItem("1", "En desacuerdo", "En desacuerdo"));
        return l;
    }
    /**
     * Actualiza o persiste según la operacion los de los resultados
     * Evaluacion Tutor
     * @param id
     * @param valor
     * @param resultados
     * @param operacion
     * @return
     */
    public ResultadoEJB<EvaluacionParesAcademicos> cargarResultadosDocentes(String id, String valor, EvaluacionParesAcademicos resultados, Operacion operacion) {
        try {
            switch(operacion){
                case PERSISTIR:
                    //Crear los resultados cargar los valores
                    f.setEntityClass(EvaluacionParesAcademicos.class);
                    em.merge(resultados);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(resultados, "Evaluacion entre pares académicos, correcta");
                case REFRESCAR:
                    //Actualizar los resultados de las preguntas
                    switch(id){
                        case "r1": resultados.setR1(Short.parseShort(valor)); break;
                        case "r2": resultados.setR2(Short.parseShort(valor));break;
                        case "r3": resultados.setR3(Short.parseShort(valor));break;
                        case "r4": resultados.setR4(Short.parseShort(valor)); break;
                        case "r5": resultados.setR5(Short.parseShort(valor)); break;
                        case "r6": resultados.setR6(Short.parseShort(valor)); break;
                        case "r7": resultados.setR7(Short.parseShort(valor)); break;
                        case "r8": resultados.setR8(Short.parseShort(valor)); break;
                        case "r9": resultados.setR9(Short.parseShort(valor)); break;
                        case "r10": resultados.setR10(Short.parseShort(valor)); break;
                        case "r11": resultados.setR11(Short.parseShort(valor)); break;
                        case "r12": resultados.setR12(Short.parseShort(valor)); break;
                        case "r13": resultados.setR13(Short.parseShort(valor)); break;
                        case "r14": resultados.setR14(Short.parseShort(valor)); break;
                        case "r15": resultados.setR15(Short.parseShort(valor)); break;
                        case "r16": resultados.setR16(Short.parseShort(valor)); break;
                        case "r17": resultados.setR17(Short.parseShort(valor)); break;
                        case "r18": resultados.setR18(Short.parseShort(valor)); break;
                        case "r19": resultados.setR19(Short.parseShort(valor)); break;
                        case "r20": resultados.setR20(Short.parseShort(valor)); break;
                        case "r21": resultados.setR21(Short.parseShort(valor)); break;
                        case "r22": resultados.setR22(Short.parseShort(valor)); break;


                    }
                    return ResultadoEJB.crearCorrecto(resultados, "Respuestas actualizadas");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", EvaluacionParesAcademicos.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo cargar los resultados (EjbNuevaEvaluacionTutor.cargarResultadosEstudianteClave)", e, null);
        }
    }
    /**
     * Actualiza como completo los resultados
     * @param resultados2 resultados de la evaluacion
     * @param finalizado
     * @return Resultados del proceso
     */
    public ResultadoEJB<EvaluacionParesAcademicos> updateCompleto(EvaluacionParesAcademicos resultados2, Boolean finalizado){
        try{
            if(resultados2 ==null){return ResultadoEJB.crearErroneo(2,new EvaluacionParesAcademicos(),"Los resultados no deben ser nulos");}
            if(finalizado==null){return ResultadoEJB.crearErroneo(3,new EvaluacionParesAcademicos(),"El estado de la evaluacion no debe ser nulo");}
            EvaluacionParesAcademicos ev = new EvaluacionParesAcademicos();
            ev = resultados2;
            ev.setCompleto(finalizado);
            em.merge(ev);
            em.flush();
            return  ResultadoEJB.crearCorrecto(ev,"Actizado como completo");

        }catch (Exception e){return ResultadoEJB.crearErroneo(1, "No se pudo actualizar(EjbNuevaEvaluacionTutor.updateCompleto)", e, null);}
    }

    public String obtenerRespuestaPorPregunta(EvaluacionParesAcademicos resultado, Integer pregunta) {
        String res = null;
        switch (pregunta.toString()) {
            case "1": res = resultado.getR1() != null ? resultado.getR1().toString() : null;break;
            case "2 ": res = resultado.getR2() != null ? resultado.getR2().toString() : null;break;
            case "3": res = resultado.getR3() != null ? resultado.getR3().toString() : null;break;
            case "4": res = resultado.getR4() != null ? resultado.getR4().toString() : null;break;
            case "5": res = resultado.getR5() != null ? resultado.getR5().toString() : null;break;
            case "6": res = resultado.getR6() != null ? resultado.getR6().toString() : null;break;
            case "7": res = resultado.getR7() != null ? resultado.getR7().toString() : null;break;
            case "8": res = resultado.getR8() != null ? resultado.getR8().toString() : null;break;
            case "9": res = resultado.getR9() != null ? resultado.getR9().toString() : null;break;
            case "10": res = resultado.getR10() != null ? resultado.getR10().toString() : null;break;
            case "11":res = resultado.getR11() != null ? resultado.getR11().toString() : null;break;
            case "12": res = resultado.getR12() != null ? resultado.getR12().toString() : null;break;
            case "13": res = resultado.getR13() != null ? resultado.getR13().toString() : null;break;
            case "14": res = resultado.getR14() != null ? resultado.getR14().toString() : null;break;
            case "15": res = resultado.getR15() != null ? resultado.getR15().toString() : null;break;
            case "16": res = resultado.getR16() != null ? resultado.getR16().toString() : null;break;
            case "17": res = resultado.getR17() != null ? resultado.getR17().toString() : null;break;
            case "18": res = resultado.getR18() != null ? resultado.getR18().toString() : null;break;
            case "19": res = resultado.getR19() != null ? resultado.getR19().toString() : null;break;
            case "20": res = resultado.getR20() != null ? resultado.getR20().toString() : null;break;
            case "21": res = resultado.getR21() != null ? resultado.getR21().toString() : null;break;
            case "22": res = resultado.getR22() != null ? resultado.getR22().toString() : null;break;
        }
        return res;
    }
    /**
     * Comprueba si los ha teminado de evaluar al docente
     * @param resultado
     */
    public double calculapromedioPA(EvaluacionParesAcademicos resultado) {

        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (13);
        Double promedio =0.0;
        for (Integer i = 1; i <= 13; i++) {
            if (obtenerRespuestaPorPregunta(resultado, i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta(resultado, i));
            }
        }
        resultado.setCompleto(completo);
        return promedio=(suma / cantidad);
        //em.merge(resultado);
    }
    /**
     * Comprueba si los ha teminado de evaluar al docente
     * @param resultado
     */
    public double calculapromedioPTC(EvaluacionParesAcademicos resultado) {

        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (22);
        Double promedio =0.0;
        for (Integer i = 1; i <= 22; i++) {
            if (obtenerRespuestaPorPregunta(resultado, i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta(resultado, i));
            }
        }
        resultado.setCompleto(completo);
        return promedio=(suma / cantidad);
        //em.merge(resultado);
    }

    ///////////////////////////////////////Seguimiento de evaluaciones /////////
    /**
     * Busca una apertura de visualizacion del cuestionario activo
     * @return Resultado del proceso
     */

    public ResultadoEJB<AperturaVisualizacionEncuestas> getAperturaActiva(){
        try{
            AperturaVisualizacionEncuestas visualizacionEncuestas = em.createQuery("select a from AperturaVisualizacionEncuestas  a where a.encuesta=:tipo and :fecha between a.fechaInicial AND a.fechaFinal order by a.apertura desc ", AperturaVisualizacionEncuestas.class)
                    .setParameter("fecha", new Date())
                    .setParameter("tipo","Evaluación entre pares académicos")
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(visualizacionEncuestas !=null){return ResultadoEJB.crearCorrecto(visualizacionEncuestas,"Apertura de visualización del cuestionario activa");}
            else {return ResultadoEJB.crearErroneo(3,visualizacionEncuestas,"No exite apertura de visualización del cuestionario activa");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el último cuestionario aperturado. (EjbSeguimientoCuestionarioPsicopedagico.getAperturaActiva", e, null);
        }
    }


}