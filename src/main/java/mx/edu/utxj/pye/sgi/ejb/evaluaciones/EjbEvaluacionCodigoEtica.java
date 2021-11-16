package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import com.github.adminfaces.starter.infra.model.Filter;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2PK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "EjbEvaluacionCodigoEtica" )
public class EjbEvaluacionCodigoEtica {
    @EJB
    EjbPersonalBean ejbPersonalBean;
    @EJB
    EjbPropiedades ep;
    @EJB
    Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    /**
     * Permite validar si el usuario autenticado es personal activo
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarPersonal(Integer clave) {
        try {
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            if(p.getPersonal().getStatus().equals("B")){return ResultadoEJB.crearErroneo(2,filtro,"El personal autenticado no está activo");}
            else {return ResultadoEJB.crearCorrecto(filtro,"Validado como personal activo");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbEvaluacionCodigoEtica.validarPersonal)", e, null);
        }
    }

    /**
     * Obtiene la evaluacion tipo Evaluacion conocimientos de los código ética y conducta activa
     * @return Resultado del proceso
     */
    public ResultadoEJB<Evaluaciones> getEvaluacionActiva(){
        try{
            Evaluaciones evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo", EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getLabel())
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(evaluacion!=null){return ResultadoEJB.crearCorrecto(evaluacion,"Evaluación activa");}
            else {return ResultadoEJB.crearErroneo(3,evaluacion,"No existe evaluación activa");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No hay evaluación activa(EjbEvaluacionCodigoEtica.validarPersonalActivo)", e, null);

        }
    }
    /////////////////////////////////////////////////// PREGUNTAS ///////////////////////////////////////////////////////////////////////////
    /**
     * Obtiene las preguntas del cuestionario
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Apartado>> apartados(){
        List<Apartado> a = new ArrayList<>();
        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 1f, "¿La Universidad se rige bajo los valores estipulados en el Código de Ética?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 2f, "¿La Universidad tiene establecido un Código de Conducta que regule el comportamiento ético de los Servidores Públicos?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 3f, "¿Cuál es el objetivo del Código de Ética?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 4f, "¿Qué Unidad Administrativa es competente para la interpretación del Acuerdo de la Secretaria de la Función Pública, por el que expide el Código de Ética y las Reglas de Integridad para el ejercicio de la función pública?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 5f, "¿Qué es conflicto de interés?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 6f, "¿Cuál es el principal objetivo del Comité de Ética y de Prevención de Conflictos de Interés?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 7f, "¿Cuáles son las funciones que tienen los miembros del Comité?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 8f, "¿Qué sucede si durante la elaboración de las conductas o comportamientos, la dependencia o entidad advierte que, de los principios, valores o reglas contenidos en el Código de Ética, no se desprende alguno que se considere relevante para el desarrollo del empleo, cargo, comisión o atribuciones de las y los servidores públicos?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 9f, "¿Qué es el Sistema Nacional Anticorrupción?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 10f, "¿Cuál es el principal objetivo del Sistema Nacional y Estatal Anticorrupción?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 11f, "¿Cuáles son las modalidades de la presentación de la declaración de situación patrimonial y de intereses?", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_CODIGO_ETICA_CONDUCTA.getNumero() , 1f, 12f, "Cuando NO se presente la declaración de situación patrimonial y de intereses sin causa justificada, ¿Qué procede?", ""));
        a.add(a1);
        return ResultadoEJB.crearCorrecto(a, "Se genero las preguntas correctamente");
    }

    /////////////////////////////////////////////// Posibles respuestas ////////////////////////////////////////////////////////////////
    public ResultadoEJB<List<SelectItem>> siNo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Si", "A"));
        l.add(new SelectItem("B", "No", "B"));
        l.add(new SelectItem("C", "No sabe", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }

    public ResultadoEJB<List<SelectItem>> respuestasPregunta3(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Dar a conocer a los servidores públicos de la Administración Pública Estatal, sus obligaciones de carácter ético, garantizando a la sociedad el correcto, honorable y adecuado desempeño de la función pública, con la finalidad de fortalecer las instituciones que conforman la Administración Pública Estatal.", "A"));
        l.add(new SelectItem("B", "Establecer las áreas de riesgo de cada Dependencia o Entidad.", "B"));
        l.add(new SelectItem("C", "Las Dependencias y Entidades deberán tener integrado su Comité de Ética y Prevención de Conflictos de Interés, mediante el cual elaborarán el Código de Ética y Código de Conducta.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }

    public ResultadoEJB<List<SelectItem>> respuestasPregunta4(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Las Dependencias y Entidades a través de su Comité de Ética y Prevención de Conflictos de Interés.", "A"));
        l.add(new SelectItem("B", "La Secretaría de la Función Pública a través de la Unidad de Ética o de manera individual por la Dirección de Normas y Procedimientos y la Coordinación General de Órganos de Vigilancia y Control.La Secretaría de la Función Pública a través de la Unidad de Ética o de manera individual por la Dirección de Normas y Procedimientos y la Coordinación General de Órganos de Vigilancia y Control.", "B"));
        l.add(new SelectItem("C", "La persona Titular de la Presidencia del Comité de Ética y Prevención de Conflictos de Interés.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta5(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Momento en el que un interés laboral, personal, profesional, familiar o de negocios de la persona servidora pública no afecta el desempeño imparcial y objetivo de sus funciones.", "A"));
        l.add(new SelectItem("B", "La situación en que el Servidor Público, se encuentra impedido de cumplir con el principio de imparcialidad, en el desempeño de su empleo, cargo o comisión, debido a interses personales, familiares o de negocios que interfieren en la atención o resolución de un asunto.", "B"));
        l.add(new SelectItem("C", "El servidor público se desempeña de manera objetiva en el desempeño de su empleo, cargo o comisión, respetando los principios y valores de la normativa aplicable y puede interferir en asuntos respecto a sus intereses personales, familiares o de negocios.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta6(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Fomento de la ética.", "A"));
        l.add(new SelectItem("B", "Fomento de la Integridad.", "B"));
        l.add(new SelectItem("C", "Mejora constante del clima y cultura organizacional.", "C"));
        l.add(new SelectItem("D", "Todas las anteriores.", "D"));

        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta7(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Desempeñarse en apego a los valores, principios y reglas de integridad.", "A"));
        l.add(new SelectItem("B", "Comprometerse de manera activa en el desarrollo de las actividades que se acuerden por el comité.", "B"));
        l.add(new SelectItem("C", "Divulgar la información a la que tengan acceso.", "C"));
        l.add(new SelectItem("D", "Todas las anteriores.", "D"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta8(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Podrán incorporar valores, principios o reglas, siempre y cuando sean armónicos con el Código de Ética.", "A"));
        l.add(new SelectItem("B", "No se puede agregar otra disposición.", "B"));
        l.add(new SelectItem("C", "Ninguna de las anteriores.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta9(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Coadyuva al cumplimiento de los objetivos del Comité Coordinador Estatal, así como ser la instancia de vinculación con las organizaciones sociales y académicas relacionadas con las materias del Sistema Estatal.", "A"));
        l.add(new SelectItem("B", "Responsable de establecer mecanismos de coordinación entre los integrantes del Sistema Estatal y tendrá bajo su encargo el diseño, promoción y evaluación de políticas públicas de combate a la corrupción.", "B"));
        l.add(new SelectItem("C", "Es la instancia de coordinación entre las autoridades de todos los órdenes de gobierno competentes en la prevención, detección y sanción de responsabilidades administrativas y hechos de corrupción, así como en la fiscalización y control de recursos públicos.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta10(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "La prevención, detección, investigación y sanción de faltas administrativas y hechos de corrupción, así como la fiscalización y control de recursos públicos.", "A"));
        l.add(new SelectItem("B", "La prevención, detección, investigación y sanción de faltas administrativas y hechos de corrupción.", "B"));
        l.add(new SelectItem("C", "Fiscalización y control a recursos públicos, para así lograr la sanción de faltas administrativas y hechos de corrupción.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta11(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Modificación, Conclusión y Aviso.", "A"));
        l.add(new SelectItem("B", "Inicial, Modificación, Conclusión y Aviso.", "B"));
        l.add(new SelectItem("C", "Inicial, Modificación y Conclusión.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
    public ResultadoEJB<List<SelectItem>> respuestasPregunta12(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("A", "Se iniciará inmediatamente la investigación por presunta responsabilidad por la comisión de las Faltas administrativas correspondientes.", "A"));
        l.add(new SelectItem("B", "Se requerirá por escrito al Declarante el cumplimiento de dicha obligación.", "B"));
        l.add(new SelectItem("C", "Las dos anteriores.", "C"));
        return ResultadoEJB.crearCorrecto(l,"");
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Obtiene los resultados guardados por evaluacion y evaluador
     * En caso de que no existan, los crea
     * @param personal Personal autenticado
     * @param evaluacion Evaluacion Activa
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> getResultados (@NonNull PersonalActivo personal, @NonNull Evaluaciones evaluacion){
        try{
            if(personal==null){return   ResultadoEJB.crearErroneo(2,new EvaluacionConocimientoCodigoEticaResultados2(),"El personal no debe ser nulo");}
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3,new EvaluacionConocimientoCodigoEticaResultados2(),"La evaluación no debe ser nula");}
            EvaluacionConocimientoCodigoEticaResultados2 resultados = new EvaluacionConocimientoCodigoEticaResultados2();
            resultados = em.createQuery("select  e from EvaluacionConocimientoCodigoEticaResultados2 e where e.evaluacionConocimientoCodigoEticaResultados2PK.evaluacion=:evaluacion and e.evaluacionConocimientoCodigoEticaResultados2PK.evaluador=:evaluador",EvaluacionConocimientoCodigoEticaResultados2.class)
            .setParameter("evaluador",personal.getPersonal().getClave())
            .setParameter("evaluacion",evaluacion.getEvaluacion())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;
           // System.out.println("EjbEvaluacionCodigoEtica.getResultados "+ resultados);
            if(resultados==null){
                EvaluacionConocimientoCodigoEticaResultados2 newResultados = new EvaluacionConocimientoCodigoEticaResultados2();
                EvaluacionConocimientoCodigoEticaResultados2PK pk = new EvaluacionConocimientoCodigoEticaResultados2PK();
                //Se crean los resultados
                pk.setEvaluador(personal.getPersonal().getClave());
                pk.setEvaluacion(evaluacion.getEvaluacion());
                newResultados.setEvaluacionConocimientoCodigoEticaResultados2PK(pk);
                newResultados.setCompleto(false);
                em.persist(newResultados);
                //System.out.println("EjbEvaluacionCodigoEtica.getResultados 2"+ newResultados);
                em.flush();
                return ResultadoEJB.crearCorrecto(resultados,"Resultados creados correctamente");
            }else {
                //Regresa los resultados obtenidos
                return ResultadoEJB.crearCorrecto(resultados,"Resultados obtenidos");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener los resultados de le evaluación(EjbEvaluacionCodigoEtica.getResultados)", e, null);

        }
    }

    /**
     * Actualiza / refrezca los resultados d
     * @param id ID de la pregunta
     * @param valor Valor a guardar
     * @param resultados Resultados de la evaluacion
     * @param operacion Operacion a realizar Actualizar/Refrescar
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> cargarResultados(String id, String valor,@NonNull EvaluacionConocimientoCodigoEticaResultados2 resultados, Operacion operacion){
        try{
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new EvaluacionConocimientoCodigoEticaResultados2(),"Los resultados no deben ser nulos");}
            switch (operacion){
                case REFRESCAR:
                    switch (id){
                        case "r1": resultados.setR1(valor); break;
                        case "r2": resultados.setR2(valor); break;
                        case "r3": resultados.setR3(valor); break;
                        case "r4": resultados.setR4(valor); break;
                        case "r5": resultados.setR5(valor); break;
                        case "r6": resultados.setR6(valor); break;
                        case "r7": resultados.setR7(valor); break;
                        case "r8": resultados.setR8(valor); break;
                        case "r9": resultados.setR9(valor); break;
                        case "r10": resultados.setR10(valor); break;
                        case "r11": resultados.setR11(valor); break;
                        case "r12": resultados.setR12(valor); break;
                    }
                    return ResultadoEJB.crearCorrecto(resultados,"");
                case ACTUALIZAR:
                    //Actualiza los resultos
                    f.setEntityClass(EvaluacionConocimientoCodigoEticaResultados2.class);
                    em.merge(resultados);
                    em.flush();
                    return  ResultadoEJB.crearCorrecto(resultados,"Resultados actualizados");
               default:
                   return ResultadoEJB.crearCorrecto(resultados,"Resultados actualualizados");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al cargar los resultados de le evaluación(EjbEvaluacionCodigoEtica.cargarResultados)", e, null);

        }
    }

    /**
     * Actualiza la evaluación como finalizada
     * @param resultados Resultados de le evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<EvaluacionConocimientoCodigoEticaResultados2> updateCompleto(EvaluacionConocimientoCodigoEticaResultados2 resultados){
        try {
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new EvaluacionConocimientoCodigoEticaResultados2(),"Los resultados no deben ser nulos");}
            resultados.setCompleto(true);
            em.merge(resultados);
            return ResultadoEJB.crearCorrecto(resultados,"Evaluación finalizada");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al actualizar los resultados (EjbEvaluacionCodigoEtica.updateCompleto)", e, null);

        }
    }

    /**
     * Cuenta el total de respuestas correctas
     * @param resultados Resultados
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> totalCorrectas (@NonNull  EvaluacionConocimientoCodigoEticaResultados2 resultados){
        try{
            if(resultados==null){return ResultadoEJB.crearErroneo(2,new Integer(0),"Los resultados no deben ser nulos");}
            Integer totalCorrectas = 0;
            if(resultados.getR1().equals("A")){totalCorrectas++;}
            if(resultados.getR2().equals("A")){totalCorrectas++;}
            if(resultados.getR3().equals("A")){totalCorrectas++;}
            if(resultados.getR4().equals("B")){totalCorrectas++;}
            if(resultados.getR5().equals("B")){totalCorrectas++;}
            if(resultados.getR6().equals("D")){totalCorrectas++;}
            if(resultados.getR7().equals("A")){totalCorrectas++;}
            if(resultados.getR8().equals("A")){totalCorrectas++;}
            if(resultados.getR9().equals("C")){totalCorrectas++;}
            if(resultados.getR10().equals("A")){totalCorrectas++;}
            if(resultados.getR11().equals("C")){totalCorrectas++;}
            if(resultados.getR12().equals("C")){totalCorrectas++;}
            return ResultadoEJB.crearCorrecto(totalCorrectas,"Total de respuestas correctas");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obetener las respuestas correctas  (EjbEvaluacionCodigoEtica.totalCorrectas)", e, null);
        }
    }




}

