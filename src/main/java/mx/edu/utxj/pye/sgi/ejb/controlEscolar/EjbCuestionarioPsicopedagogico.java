package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.CedulaIdentificacionRolPsicopedagogia;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.enums.Operacion;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorCuestionarioPsicopedagogicoEstudiante;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless (name = "EjbCuestionarioPsicopedagogico")
public class EjbCuestionarioPsicopedagogico {

    @EJB Facade f;
    @EJB EjbEstudianteBean ejbEstudianteBean;
    private EntityManager em;
    @PostConstruct public void init(){em = f.getEntityManager();}

    /**
     * Valida que el usuario logueado sea un estudiante activo
     * @param matricula
     * @return
     */
    public ResultadoEJB<Estudiante> validaEstudiante(Integer matricula){
        try{
            Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula and (e.tipoEstudiante.idTipoEstudiante=:tipo or e.tipoEstudiante.idTipoEstudiante=:tipo2 or e.tipoEstudiante.idTipoEstudiante=:tipo3) order by  e.periodo desc", Estudiante.class).setParameter("matricula", matricula)
                    .setParameter("tipo",1)
                    .setParameter("tipo2",5)
                    .setParameter("tipo3",6)
                    .getResultStream().findFirst().orElse(new Estudiante());
            //System.out.println("EjbCuestionarioPsicopedagogico.validaEstudiante "+ e);
            return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante docente no se pudo validar. (EjbConsultaCalificacion.validadEstudiante)", e, null);
        }
    }

    /**
     * Valida que haya una encuesta psicopedagogica activa
     * @return Resultado del Preceso
     */
    public ResultadoEJB<Evaluaciones> getcuestionarioActiva(){
        try {
            // System.out.println("Entro a cuestionario activo");
            Evaluaciones encuestaActiva = new Evaluaciones();
            encuestaActiva = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
            .setParameter("tipo","Cuestionario Psicopedagógico")
            .setParameter("fecha",new Date())
            .getResultStream()
            .findFirst()
            .orElse(null)
            ;

            //System.out.println("Evaluacion" + encuestaActiva);
            if(encuestaActiva !=null){return ResultadoEJB.crearCorrecto(encuestaActiva,"Se encontró una encuesta activa");}
            else {return ResultadoEJB.crearErroneo(3,encuestaActiva,"No hay encuesta psicopedagógica activa");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbCuestionarioPsicopedagogico.validaEstudiante)", e, null);
        }
    }

    /**
     * Busca  resultados por estudiante, si no existen los crea --Esto es para los estudiantes
     * @param estudiante
     * @return Resultado del proceso (Resultados del cuestionario)
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> getResultadosEstudiante(Estudiante estudiante, Evaluaciones evaluacion){
        try{
            CuestionarioPsicopedagogicoResultados resultados = new CuestionarioPsicopedagogicoResultados();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,resultados,"El estudiante no debe ser nulo");}
            if(evaluacion==null){return ResultadoEJB.crearErroneo(3,resultados,"La evaluación no debe ser nula");}
            // Busca el primer registro del estudiante

            Estudiante e = em.createQuery("select e from Estudiante as e where e.matricula = :matricula and (e.tipoEstudiante.idTipoEstudiante=:tipo or e.tipoEstudiante.idTipoEstudiante=:tipo2 or e.tipoEstudiante.idTipoEstudiante=:tipo3) order by  e.periodo asc", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("tipo",1)
                    .setParameter("tipo2",5)
                    .setParameter("tipo3",6)
                    .getResultStream().findFirst().orElse(new Estudiante());

            //System.out.println("EjbCuestionarioPsicopedagogico.validaEstudiante "+ e);
            // Busca resultados por el id del estudiante
            resultados = em.createQuery("select c from CuestionarioPsicopedagogicoResultados c where c.cuestionarioPsicopedagogicoResultadosPK.idEstudiante=:idEstudiante order by c.cuestionarioPsicopedagogicoResultadosPK.evaluacion ",CuestionarioPsicopedagogicoResultados.class)
                    .setParameter("idEstudiante",e.getIdEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            //System.out.println("Resultados" + resultados);
            //Comprueba si hay resultados, si no los crea
            if(resultados==null){
                CuestionarioPsicopedagogicoResultados resultados2 = new CuestionarioPsicopedagogicoResultados();
                CuestionarioPsicopedagogicoResultadosPK pk = new CuestionarioPsicopedagogicoResultadosPK();
                pk.setEvaluacion(evaluacion.getEvaluacion());
                pk.setIdEstudiante(e.getIdEstudiante());
                resultados2.setCuestionarioPsicopedagogicoResultadosPK(pk);
                em.persist(resultados2);
                em.flush();
                return ResultadoEJB.crearCorrecto(resultados2,"Se han creado los resultados");
            }else {return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo generar los resultados. (EjbCuestionarioPsicopedagogico.getResultados)", e, null);
        }
    }

    /**
     * Busca resultados por estudiante y matricula (No se filtra por evaluacion, porque se debe vizualizar los datos del cuestionario por historico a pesar de que el cuestionario ya esté cerrado)
     *
     * @param estudiante Estudiante a buscar
     * @return
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> getResultadosCuestionarioPersonal(Estudiante estudiante, Personal examinador){
        try{
            CuestionarioPsicopedagogicoResultados resultados = new CuestionarioPsicopedagogicoResultados();
            if(estudiante ==null){return  ResultadoEJB.crearErroneo(2,resultados,"El estudiante no debe ser nulo");}
            Estudiante estudiante1 = em.createQuery("select e from Estudiante e where e.matricula=:matricula and e.grupo.grado=:grado",Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("grado",1)
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(estudiante1!=null){
                //Busca resultados
                resultados = em.createQuery("select c from CuestionarioPsicopedagogicoResultados c where c.cuestionarioPsicopedagogicoResultadosPK.idEstudiante=:idEstudiante order by c.cuestionarioPsicopedagogicoResultadosPK.evaluacion",CuestionarioPsicopedagogicoResultados.class)
                        .setParameter("idEstudiante",estudiante1.getIdEstudiante())
                        .getResultStream()
                        .findFirst()
                        .orElse(null)
                ;
                //Si no se encuentran resultados el estudiante, se da aviso al personal examinador que el estudiante no ha respondido el cuestionario
                if(resultados==null){return ResultadoEJB.crearErroneo(5,resultados,"El estudiante con matricula "+estudiante.getMatricula() +" no ha respondido el cuestionario");}
                //Se revisa que los resultados ya hayan sido terminados
                else if(resultados.getCompleto()==true){
                    //Se agrega la clave del persona
                    resultados.setClave(examinador.getClave());
                    resultados.setReviso(false);
                    em.merge(resultados);
                    return ResultadoEJB.crearCorrecto(resultados,"Se ha agregado a personal examinador");
                }else {return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");}
            }
            else {return ResultadoEJB.crearErroneo(3,resultados,"No encontro registro del estudiante en 1 er cuatrimestre");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados(Personal). (EjbCuestionarioPsicopedagogico.getResultadosCuestionarioPersonal)", e, null);
        }
    }

    /**
     * Verifica que los resultados del cuestionario del estudiante que se obtuvieron ya cuenten ya hayan sido examinados por un el personal de psicopedadogia
     * En caso de que no, se envia a los resultados la clave del personal logeado como personal de psicopedagogia
     * @param resultados Resultados obtenidos del estudiante
     * @param examinador Personal de Psicopedagogia, encargado de exminar los resultados del cuestionario
     * @return Resultado del proceso (Resultados actualizados)
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> crearResultadosPersonal(CuestionarioPsicopedagogicoResultados resultados, Personal examinador){
        try{
            if(resultados ==null){return ResultadoEJB.crearErroneo(2,resultados,"Los resultados no deben ser nulos");}
            if(examinador ==null){return ResultadoEJB.crearErroneo(3,resultados,"El examinador no debe ser nulo");}
            //TODO: Comprueba que tenga un examinador
            if(resultados.getClave()==null){
                //TODO: En caso de que sea nulo, le agrega la clave del personal examinador
                resultados.setClave(examinador.getClave());
                resultados.setReviso(false);
                em.merge(resultados);
                return ResultadoEJB.crearCorrecto(resultados,"Se ha agregado al personal examinador");
            }
            else {return ResultadoEJB.crearCorrecto(resultados,"Los resultados ya cuentan con personal examinador");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se agregar personal examinador a los resultados(Personal). (EjbCuestionarioPsicopedagogico.getResultadosCuestionarioPersonal)", e, null);
        }
    }

    /**
     * Identifica mensajes de alerta, en este caso sólo para identificar si el estudiante ha repondido (Completado el cuestionario psicopedagogico)
     * Identifica si los resultados encontrados han sido examinados por el personal de psicopedagogía
     * @param rol
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAlerta>> identificaMensajes(CedulaIdentificacionRolPsicopedagogia rol){
        try {
            if(rol.getEstudiante()!=null){
                List<DtoAlerta> alertas = new ArrayList<>();
                //Buscar los resultados por estudiante
                if(rol.getResultados().getEstudiante()==null ){
                    //Se envia mensaje de alerta que el estudiante no ha respondido el cuestionario
                    alertas.add(new DtoAlerta("El estudiante " + rol.getEstudiante().getAspirante().getIdPersona().getNombre()+" "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()+" no ha contestado el cuestionario", AlertaTipo.ERROR));
                }else {
                    //Comprueba que el estudiante ya lo haya terminado el cuestionario
                    if(rol.getResultados().getCompleto()==true){
                        //Lo termino
                        alertas.add(new DtoAlerta("El estudiante " + rol.getEstudiante().getAspirante().getIdPersona().getNombre()+" "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()+" ha completado el cuestionario", AlertaTipo.CORRECTO));
                    }
                    else {
                        //No lo ha terminado
                        alertas.add(new DtoAlerta("El estudiante " + rol.getEstudiante().getAspirante().getIdPersona().getNombre()+" "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno() + " "+rol.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()+" no ha completado el cuestionario", AlertaTipo.INFORMATIVO));
                    }
                    //Comprueba que ya cuente con un personal examinador
                    if(rol.getResultados().getClave()==null){
                        //Se envia alerta de que el cuestionario no cuenta con personal examinador
                        alertas.add(new DtoAlerta("Los resultados del cuestionario no han sido examinados.", AlertaTipo.INFORMATIVO));
                    }else {
                        //Revisa si el personal examinador  ya termino de revisar las respuestas
                        if(rol.getResultados().getReviso()==true){
                            //Envia mesaje de aviso que ya se ha terminado de revisar el cuestuinario
                            alertas.add(new DtoAlerta("Los resultados del cuestionario han sido revisados", AlertaTipo.CORRECTO));
                        }else {
                            //Mensaje de alerta que no se ha terminado de revisar el cuestionario
                            alertas.add(new DtoAlerta("Los resultados del cuestionario no se han terminado de examinar.", AlertaTipo.INFORMATIVO));
                        }
                    }

                }
                return ResultadoEJB.crearCorrecto(alertas, "Lista de mensajes");
            }
            return ResultadoEJB.crearCorrecto(Collections.EMPTY_LIST, "Sin mensajes");

        } catch (Exception e) {return ResultadoEJB.crearErroneo(1, "No se pudieron identificar los mensajes(EjbCredencializacion.identificaMensajes)", e, null);}
    }

    /**
     * Actualiza y persiste (Segun la operacion) los resultados del Cuestionario del estudiante
     * @param id Numero de pregunta
     * @param valor Respuesta a guardar
     * @param resultado Resultados
     * @param operacion Operacion a realizar (Refrescar o persistir)
     * @return
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> cargaResultadosCuestionarioPsicopedagogico(String id, String valor, CuestionarioPsicopedagogicoResultados resultado, Operacion operacion){
        try{
            switch(operacion){
                case PERSISTIR:
                    if(resultado!=null){
                        em.merge(resultado);
                        return ResultadoEJB.crearCorrecto(resultado,"Resultados Actualizados");
                    }else { return  ResultadoEJB.crearErroneo(2,resultado,"Los resultados no deben ser nulos");}
                case REFRESCAR:
                    switch (id.trim()) {
                        case "r1": resultado.setR1(valor); break;
                        case "r2": resultado.setR2(valor); break;
                        case "r3": resultado.setR3(valor); break;
                        case "r4": resultado.setR4(valor); break;
                        case "r5": resultado.setR5(valor); break;
                        case "r6": resultado.setR6(valor); break;
                        case "r7": resultado.setR7(valor); break;
                        case "r8": resultado.setR8(valor); break;
                        case "r9": resultado.setR9(valor); break;
                        case "r10": resultado.setR10(valor); break;
                        case "r11": resultado.setR11(valor); break;
                        case "r12": resultado.setR12(valor); break;
                        case "r13": resultado.setR13(valor); break;
                        case "r14": resultado.setR14(valor); break;
                        case "r15": resultado.setR15(valor); break;
                        case "r16": resultado.setR16(valor); if(!resultado.getR16().equals("Finados")){resultado.setR17(null); }break ;
                        case "r17": resultado.setR17(valor); break;
                        case "r18": resultado.setR18(valor); break;
                        case "r19": resultado.setR19(valor); if(resultado.getR19().equals("No")){resultado.setR20(null);}break ;
                        case "r20": resultado.setR20(valor); break;
                        case "r21": resultado.setR21(valor); if(resultado.getR21().equals("No")){resultado.setR59(null);}break ;
                        case "r22": resultado.setR22(valor); break;
                        case "r23": resultado.setR23(valor); break;
                        case "r24": resultado.setR24(valor); break;
                        case "r25": resultado.setR25(valor); if(resultado.getR25().equals("No")){
                            resultado.setR26(null);
                            resultado.setR27(null);
                            resultado.setR28(null);
                            resultado.setR29(null);
                            resultado.setR30(null);
                            resultado.setR31(null);
                            resultado.setR32(null);
                            resultado.setR33(null);
                            resultado.setR34(null);
                            resultado.setR35(null);
                            resultado.setR36(null);
                            resultado.setR37(null);
                            resultado.setR38(null);
                            resultado.setR39(null);
                            resultado.setR40(null);
                            resultado.setR41(null);
                            resultado.setR42(null);
                        }break ;
                        case "r26": resultado.setR26(valor); break;
                        case "r27": resultado.setR27(valor); break;
                        case "r28": resultado.setR28(valor); break;
                        case "r29": resultado.setR29(valor); break;
                        case "r30": resultado.setR30(valor); break;
                        case "r31": resultado.setR31(valor); break;
                        case "r32": resultado.setR32(valor); break;
                        case "r33": resultado.setR33(valor); break;
                        case "r34": resultado.setR34(valor); break;
                        case "r35": resultado.setR35(valor); break;
                        case "r36": resultado.setR36(valor); break;
                        case "r37": resultado.setR37(valor); break;
                        case "r38": resultado.setR38(valor); break;
                        case "r39": resultado.setR39(valor); break;
                        case "r40": resultado.setR40(valor); break;
                        case "r41": resultado.setR41(valor); break;
                        case "r42": resultado.setR42(valor); break;
                        case "r43": resultado.setR43(valor); if(resultado.getR43().equals("No")){resultado.setR44(null);}break ;
                        case "r44": resultado.setR44(valor); break;
                        case "r54": resultado.setR54(valor);if(resultado.getR54().equals("No")){resultado.setR55(null);}break ;
                        case "r55": resultado.setR55(valor); break;
                        case "r56": resultado.setR56(valor); break;
                        case "r57": resultado.setR57(valor); if(resultado.getR57().equals("No")){resultado.setR58(null);}break ;
                        case "r58": resultado.setR58(valor); break;
                        case "r59": resultado.setR59(valor); break;
                    }return ResultadoEJB.crearCorrecto(resultado,"Se actualizaron las respuestas por pregunta");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", CuestionarioPsicopedagogicoResultados.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar o persistir las resupuestas (EjbCredencializacion.cargaResultadosCuestionarioPsicopedagogico)", e, null);

        }
    }
    /**
     * Actualiza y persiste (Segun la operacion) los resultados del Cuestionario del estudiante
     * @param id Numero de pregunta
     * @param valor Respuesta a guardar
     * @param resultado Resultados
     * @param operacion Operacion a realizar (Refrescar o persistir)
     * @return
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> cargaResultadosCuestionarioPsicopedagogicoPersonal(String id, String valor, CuestionarioPsicopedagogicoResultados resultado, Operacion operacion){
        try{
            switch(operacion){
                case PERSISTIR:
                    if(resultado!=null){
                        em.merge(resultado);
                        return ResultadoEJB.crearCorrecto(resultado,"Resultados Actualizados");
                    }else { return  ResultadoEJB.crearErroneo(2,resultado,"Los resultados no deben ser nulos");}
                case REFRESCAR:
                    switch (id.trim()) {
                        case "r45": resultado.setR45(valor); break;
                        case "r46": resultado.setR46(valor); break;
                        case "r47": resultado.setR47(valor); break;
                        case "r48": resultado.setR48(valor); break;
                        case "r49": resultado.setR49(valor); break;
                        case "r50": resultado.setR50(valor); break;
                        case "r51": resultado.setR51(valor); break;
                        case "r52": resultado.setR52(valor); break;
                        case "r53": resultado.setR53(valor); break;
                    }return ResultadoEJB.crearCorrecto(resultado,"Se actualizaron las respuestas por pregunta");
                default:
                    return ResultadoEJB.crearErroneo(2, "No se pudo actualizar", CuestionarioPsicopedagogicoResultados.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar o persistir las resupuestas del Personal(EjbCredencializacion.cargaResultadosCuestionarioPsicopedagogicoPersonal)", e, null);

        }
    }

    /**
     * Actualiza los resultados del cuestionario del estudiante como completos
     * @param resultados Resultados del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> actualizarCompleto(CuestionarioPsicopedagogicoResultados resultados){
        try {
            if(resultados==null){return ResultadoEJB.crearErroneo(2,resultados,"Los resultados no deben ser nulos");}
            resultados.setCompleto(true);
            em.merge(resultados);
            return ResultadoEJB.crearCorrecto(resultados,"Los resultados del cuestionario del estudiante se han actualizado como completo.");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actulizar cuestionario completo (EjbCredencializacion.actualizarCompleto)", e, null);
        }
    }

    /**
     * Actualiza los resultados del cuestionario del estudiante a revisados por un personal examinador
     * @param resultados Resultados del cuestionario del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<CuestionarioPsicopedagogicoResultados> actualizaRevisado(CuestionarioPsicopedagogicoResultados resultados) {
        try {
            if(resultados==null){return ResultadoEJB.crearErroneo(2,resultados,"Los resultados no deben ser nulos");}
            //resultados.setReviso(true);
            em.merge(resultados);
            return ResultadoEJB.crearCorrecto(resultados,"Los resultados del cuestionario del estudiante se han actualizado como revisados por el examinador");

        }catch (Exception e){

        }
        return null;
    }

    //---------------------------------------------SECCIÓN DE APARTADOS Y POSIBLES RESPUESTAS--------------------------------------//
    /**
     * Genera los apartados con sus preguntas del cuestionario psicopedagógico
     * Se modificaron las pregunas (Apartado 2) Se queda como respaldo 20/09/2021
     * @return Apartados
     */
    public List<Apartado> getApartados(){
        List<Apartado> l = new ArrayList<>();

        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 1f, "¿Te gusta cómo eres?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 2f, "¿Por qué?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 3f, "¿Cuáles son tus principales cualidades?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 4f, "¿Cuáles son tus principales defectos?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 5f, "¿Qué valores aprecias más de la gente?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 6f, "¿Qué es lo que más te disgusta de la gente?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 7f, "Señala tres situaciones o aspectos que te provocan temor",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 8f, "¿Actualmente tienes novio(a)?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 9f, "¿Tienes planes de matrimonio a corto plazo?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 10f, "¿Qué planes tienes para tu futuro personal?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 11f, "¿Qué planes tienes para tu futuro académico?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 12f, "¿Qué planes tienes para tu futuro profesional?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 13f, "¿Qué actividades o pasatiempos disfrutas realizar?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 14f, "¿A qué te dedicas en tu tiempo libre?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 15f, "Si pudieras cambiar algo de tu vida ¿qué sería?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 16f, "Estado civil de tus padres:",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 17f, "¿Quién?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 18f, "En estos momentos de mi vida ¿cuáles son mis principales preocupaciones?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 19f, "¿En este momento existe algún problemas en tu Familia?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 20f, "¿De qué tipo?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 21f, "¿Te gustaría hablar sobre estos problemas con alguna persona del área de Psicopedagogía que te pudiera orientar?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 22f, "¿Qué materias se te dificultan más?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 23f, "¿Has reprobado alguna materia o presentado examen extraordinario?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 24f, "¿Qué materias?",""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 25f, "¿Utilizas alguna técnica de estudio?",""));

        l.add(a1);
        Apartado a2 = new Apartado(2f);
        a2.setContenido("Técnicas de estudio");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 26f, "Buscas información adicional a la que te proporcionaban tus profesores",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 27f, "Relacionas los nuevos conocimientos con tus experiencias previas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 28f, "Organizas distintos materiales para integrar tus trabajos",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 29f, "Solicitas que los maestros te aclararan tus dudas antes de realizar algún trabajo",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 30f, "Realizas ejercicios para reafirmar tu conocimiento",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 31f, "Te planteas preguntas a partir de la información que analizabas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 32f, "Discutes tus puntos de vista con maestros y compañeros",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 33f, "Elaboras esquemas, cuadros sinópticos, mapas conceptuales u otras estrategias para sintetizar la información",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 34f, "Analizas la eficacia de tus estrategias de estudio",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 35f, "Buscas nuevas formas de estudiar cuando alguna no te daba el resultado que esperabas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 36f, "Tomar apuntes en clase",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 37f, "Colaboras con tus compañeros de equipo para cumplir tareas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 38f, "Diriges a tu equipo para el logro de las actividades académicas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 39f, "Tomas decisiones en busca de mejores resultados en tus actividades académicas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 40f, "Negocias acuerdos con tus compañeros de grupo",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 41f, "Utilizas equipo de cómputo para la elaboración de tus tareas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 42f, "Consultas textos en inglés para apoyarte en la realización de tus tareas",""));
        l.add(a2);
        Apartado a3 = new Apartado(3f);
        a3.setContenido("Ultimo");
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 3f, 43f, "¿Cuentas en casa con algunos libros que apoyan tus estudios?",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 3f, 44f, "¿Cuáles? ",""));
        l.add(a3);
        Apartado a4 = new Apartado(4f);
        a4.setContenido("Conclusiones del la eximinadora o examinador");
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 45f, "Con base en la información proporcionada ¿se considera al estudiante como elemento de uno o más grupos altamente vulnerables?",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 46f, "Marque los grupos en los que considera al estudiante como altamente vulnerable",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 47f, "Observaciones generales por parte de la examinadora o examinador",""));
        l.add(a4);
        return l;
    }

    /**
     * Actualización de apartados del cuestionario psicopedagogico
     * Se cometo el número con el que se guardan en la bd por la actualización
     * @return
     */
    public List<Apartado> getApartados2(){
        List<Apartado> l = new ArrayList<>();

        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 1f, "¿Te gusta cómo eres?",""));//1
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 2f, "¿Por qué?",""));//2
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 3f, "¿Cuáles son tus principales cualidades?",""));//3
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 4f, "¿Cuáles son tus principales defectos?",""));//4
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 5f, "¿Qué valores aprecias más de la gente?",""));//5
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 6f, "¿Qué es lo que más te disgusta de la gente?",""));//6
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 7f, "Señala tres situaciones o aspectos que te provocan temor",""));//7
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 8f, "¿Actualmente tienes novio(a)?",""));//8
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 9f, "¿Tienes planes de matrimonio a corto plazo?",""));//9
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 10f, "¿Qué planes tienes para tu futuro personal?",""));//10
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 11f, "¿Qué planes tienes para tu futuro académico?",""));//11
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 12f, "¿Qué planes tienes para tu futuro profesional?",""));//12
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 13f, "¿Qué actividades o pasatiempos disfrutas realizar?",""));//13
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 14f, "¿A qué te dedicas en tu tiempo libre?",""));//14
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 15f, "¿Estás en tratamiento médico?",""));//54
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 16f, "¿Por qué?",""));//55
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 17f, "Si pudieras cambiar algo de tu vida ¿qué sería?",""));//15
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 18f, "Tus padres se encuentran:",""));//16
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 19f, "¿Quién?",""));//17
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 20f, "En estos momentos de mi vida ¿cuáles son mis principales preocupaciones?",""));//18
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 21f, "¿Con qué frecuencia consumes alcohol o fumas tabaco?",""));//56
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 22f, "¿Has consumido alguna otra sustancia adictiva?",""));//57
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 23f, "¿Cuál?",""));//58
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 24f, "¿En este momento existe algún problemas en tu Familia?",""));//19
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 25f, "¿De qué tipo?",""));//20
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 26f, "¿Te gustaría hablar con la psicóloga para recibir orientación, sobre alguna situación que te esté causando problemas?",""));//21
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 26f, "¿Por qué?",""));//59
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 27f, "¿Qué materias se te dificultan más?",""));//22
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 28f, "¿Has reprobado alguna materia o presentado examen extraordinario?",""));//23
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 29f, "¿Qué materias?",""));//24
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 1f, 30f, "¿Utilizas alguna técnica de estudio?",""));//25
        //25 a la 53 corrido
        l.add(a1);
        Apartado a2 = new Apartado(2f);
        a2.setContenido("Técnicas de estudio");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 31f, "Buscas información adicional a la que te proporcionaban tus profesores",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 32f, "Relacionas los nuevos conocimientos con tus experiencias previas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 33f, "Organizas distintos materiales para integrar tus trabajos",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 34f, "Solicitas que los maestros te aclararan tus dudas antes de realizar algún trabajo",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 35f, "Realizas ejercicios para reafirmar tu conocimiento",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 36f, "Te planteas preguntas a partir de la información que analizabas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 36f, "Discutes tus puntos de vista con maestros y compañeros",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 37f, "Elaboras esquemas, cuadros sinópticos, mapas conceptuales u otras estrategias para sintetizar la información",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 38f, "Analizas la eficacia de tus estrategias de estudio",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 39f, "Buscas nuevas formas de estudiar cuando alguna no te daba el resultado que esperabas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 40f, "Tomar apuntes en clase",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 41f, "Colaboras con tus compañeros de equipo para cumplir tareas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 42f, "Diriges a tu equipo para el logro de las actividades académicas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 43f, "Tomas decisiones en busca de mejores resultados en tus actividades académicas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 44f, "Negocias acuerdos con tus compañeros de grupo",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 45f, "Utilizas equipo de cómputo para la elaboración de tus tareas",""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 2f, 46f, "Consultas textos en inglés para apoyarte en la realización de tus tareas",""));
        l.add(a2);
        Apartado a3 = new Apartado(3f);
        a3.setContenido("Ultimo");
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 3f, 47f, "¿Cuentas en casa con algunos libros que apoyan tus estudios?",""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 3f, 48f, "¿Cuáles? ",""));
        l.add(a3);
        Apartado a4 = new Apartado(4f);
        a4.setContenido("Conclusiones del la eximinadora o examinador");
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 49f, "Con base en la información proporcionada ¿se considera al estudiante como elemento de uno o más grupos altamente vulnerables?",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 50f, "Marque los grupos en los que considera al estudiante como altamente vulnerable",""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.CUESTIONARIO_PSICOPEDAGOGICO.getNumero() , 4f, 51f, "Observaciones generales por parte de la examinadora o examinador",""));
        l.add(a4);
        return l;
    }

    /**
     * Respuesta posibles SI/NO
     * @return
     */
    public List<SelectItem> getSiNo(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Si","Si","Si"));
        l.add(new SelectItem("No","No","No"));
        return  l;

    }

    /**
     * Posibles respouestas pregunta #16 Estado civil de los padres
     * @return
     */
    public List<SelectItem> getEstadoCivilPadres(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Juntos","Juntos","Juntos"));
        l.add(new SelectItem("Separados","Separados","Separados"));
        l.add(new SelectItem("Finados","Finados","Finados"));
        return  l;
    }

    /**
     * Posibles repuestas de pregunts #17 -- Sólo en caso de haber seleccionado "Finados" en pregunta #16
     * @return
     */
    public List<SelectItem> getFamFinado(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Madre","Madre","Madre"));
        l.add(new SelectItem("Padre","Padre","Padre"));
        l.add(new SelectItem("Ambos","Ambos","Ambos"));
        return  l;
    }

    /**
     * Posibles respuestas para tipo de problema familiar pregunta #20, se habilita solo en caso de haber reponsido "Si" en pregunta #19
     * @return
     */
    public List<SelectItem> getTipoProblemaFam(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Económico","Económico","Económico"));
        l.add(new SelectItem("Salud","Salud","Salud"));
        l.add(new SelectItem("Conyugales","Conyugales","Conyugales"));
        l.add(new SelectItem("Falta de comunicación","Falta de comunicación","Falta de comunicación"));
        l.add(new SelectItem("Adicciones","Adicciones","Adicciones"));
        l.add(new SelectItem("Otro","Otro","Otro"));
        return  l;
    }

    /**
     * Posibles Respuestas para Apartado 2 "Tecnicas de estudio"
     * @return
     */
    public List<SelectItem> getTecnicasEstudio(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Siempre o casi siempre","Siempre o casi siempre","Siempre o casi siempre"));
        l.add(new SelectItem("Con bastante frecuencia","Con bastante frecuencia","Con bastante frecuencia"));
        l.add(new SelectItem("Con poca frecuencia","Con poca frecuencia","Con poca frecuencia"));
        l.add(new SelectItem("Casi nunca o nunca","Casi nunca o nunca","Casi nunca o nunca"));
        return  l;
    }

    /**
     * Posibles respuestas para pregunta #46
     * @return
     */
    public List<SelectItem> getGruposVunerabilidad(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("Económico","Económico","Económico"));
        l.add(new SelectItem("Vocacional","Vocacional","Vocacional"));
        l.add(new SelectItem("Familiar","Familiar","Familiar"));
        l.add(new SelectItem("De salud","De salud","De Salud"));
        l.add(new SelectItem("Afectivo emocional","Afectivo emocional","Afectivo emocional"));
        l.add(new SelectItem("Autoestima","Autoestima","Autoestima"));
        l.add(new SelectItem("Aprovechamiento académico","Aprovechamiento académico","Aprovechamiento académico"));
        return  l;
    }






}
