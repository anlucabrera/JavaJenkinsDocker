package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEncuestaServicios;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

@Stateful
public class ServicioEncuestaServicios implements EjbEncuestaServicios {
    private static final long serialVersionUID = 8258275727749911168L;
    @EJB Facade f;
    @EJB Facade2 f2;

    @Override
    public Evaluaciones getEvaluacionActiva() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class);
        q.setParameter("tipo", "Servicios");
        q.setParameter("fecha", new Date());
        
        List<Evaluaciones> l = q.getResultList();
        if(l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }

    @Override
    public EncuestaServiciosResultados getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas) {
        try {
            EncuestaServiciosResultadosPK pk = new EncuestaServiciosResultadosPK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(EncuestaServiciosResultados.class);

            EncuestaServiciosResultados r = (EncuestaServiciosResultados) f.find(pk);

            if (r == null) {
                r = new EncuestaServiciosResultados(pk);
                f.create(r);
            }

            if (r.getR1() != null) {
                respuestas.put("p1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("p2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("p3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("p4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("p5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("p6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("p7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("p8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("p9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("p10", r.getR10().toString());
            }
            if (r.getR11() != null) {
                respuestas.put("p11", r.getR11().toString());
            }
            if (r.getR12() != null) {
                respuestas.put("p12", r.getR12().toString());
            }
            if (r.getR13() != null) {
                respuestas.put("p13", r.getR13().toString());
            }
            if (r.getR14() != null) {
                respuestas.put("p14", r.getR14().toString());
            }
            if (r.getR15() != null) {
                respuestas.put("p15", r.getR15().toString());
            }
            if (r.getR16() != null) {
                respuestas.put("p16", r.getR16().toString());
            }
            if (r.getR17() != null) {
                respuestas.put("p17", r.getR17().toString());
            }
            if (r.getR18() != null) {
                respuestas.put("p18", r.getR18().toString());
            }
            if (r.getR19() != null) {
                respuestas.put("p19", r.getR19().toString());
            }
            if (r.getR20() != null) {
                respuestas.put("p20", r.getR20().toString());
            }
            if (r.getR21() != null) {
                respuestas.put("p21", r.getR21().toString());
            }
            if (r.getR22() != null) {
                respuestas.put("p22", r.getR22().toString());
            }
            if (r.getR23() != null) {
                respuestas.put("p23", r.getR23().toString());
            }
            if (r.getR24() != null) {
                respuestas.put("p24", r.getR24().toString());
            }
            if (r.getR25() != null) {
                respuestas.put("p25", r.getR25().toString());
            }
            if (r.getR26() != null) {
                respuestas.put("p26", r.getR26().toString());
            }
            if (r.getR27() != null) {
                respuestas.put("p27", r.getR27().toString());
            }
            if (r.getR28() != null) {
                respuestas.put("p28", r.getR28().toString());
            }
            if (r.getR29() != null) {
                respuestas.put("p29", r.getR29().toString());
            }
            if (r.getR30() != null) {
                respuestas.put("p30", r.getR30().toString());
            }
            if (r.getR31() != null) {
                respuestas.put("p31", r.getR31().toString());
            }
            if (r.getR32() != null) {
                respuestas.put("p32", r.getR32().toString());
            }
            if (r.getR33() != null) {
                respuestas.put("p33", r.getR33().toString());
            }
            if (r.getR34() != null) {
                respuestas.put("p34", r.getR34().toString());
            }
            if (r.getR35() != null) {
                respuestas.put("p35", r.getR35().toString());
            }
            if (r.getR36() != null) {
                respuestas.put("p36", r.getR36().toString());
            }
            if (r.getR37() != null) {
                respuestas.put("p37", r.getR37().toString());
            }
            if (r.getR38() != null) {
                respuestas.put("p38", r.getR38().toString());
            }
            if (r.getR39() != null) {
                respuestas.put("p39", r.getR39().toString());
            }
            if (r.getR40() != null) {
                respuestas.put("p40", r.getR40().toString());
            }
            if (r.getR41() != null) {
                respuestas.put("p41", r.getR41().toString());
            }
            if (r.getR42() != null) {
                respuestas.put("p42", r.getR42().toString());
            }
            if (r.getR43() != null) {
                respuestas.put("p43", r.getR43().toString());
            }
            if (r.getR44() != null) {
                respuestas.put("p44", r.getR44().toString());
            }
            if (r.getR45() != null) {
                respuestas.put("p45", r.getR45().toString());
            }
            if (r.getR46() != null) {
                respuestas.put("p46", r.getR46().toString());
            }
            if (r.getR47() != null) {
                respuestas.put("p47", r.getR47().toString());
            }
            if (r.getR48() != null) {
                respuestas.put("p48", r.getR48().toString());
            }
            if (r.getR49() != null) {
                respuestas.put("p49", r.getR49().toString());
            }
            if (r.getR50() != null) {
                respuestas.put("p50", r.getR50().toString());
            }
            if (r.getR51() != null) {
                respuestas.put("p51", r.getR51().toString());
            }
            if (r.getR52() != null) {
                respuestas.put("p52", r.getR52().toString());
            }
            if (r.getR53() != null) {
                respuestas.put("p53", r.getR53().toString());
            }
            if (r.getR54() != null) {
                respuestas.put("p54", r.getR54().toString());
            }
            if (r.getR55() != null) {
                respuestas.put("p55", r.getR55().toString());
            }
            if (r.getR56() != null) {
                respuestas.put("p56", r.getR56().toString());
            }
            if (r.getR57() != null) {
                respuestas.put("p57", r.getR57().toString());
            }
            if (r.getR58() != null) {
                respuestas.put("p58", r.getR58().toString());
            }
            if (r.getR59() != null) {
                respuestas.put("p59", r.getR59().toString());
            }
            if (r.getR60() != null) {
                respuestas.put("p60", r.getR60().toString());
            }
            if (r.getR61() != null) {
                respuestas.put("p61", r.getR61().toString());
            }
            if (r.getR62() != null) {
                respuestas.put("p62", r.getR62().toString());
            }
            if (r.getR63() != null) {
                respuestas.put("p63", r.getR63().toString());
            }
            if (r.getR64() != null) {
                respuestas.put("p64", r.getR64().toString());
            }
            if (r.getR65() != null) {
                respuestas.put("p65", r.getR65().toString());
            }
            if (r.getR66() != null) {
                respuestas.put("p66", r.getR66().toString());
            }
            if (r.getR67() != null) {
                respuestas.put("p67", r.getR67().toString());
            }
            if (r.getR68() != null) {
                respuestas.put("p68", r.getR68().toString());
            }
            if (r.getR69() != null) {
                respuestas.put("p69", r.getR69().toString());
            }
            if (r.getR70() != null) {
                respuestas.put("p70", r.getR70().toString());
            }
            if (r.getR71() != null) {
                respuestas.put("p71", r.getR71().toString());
            }
            if (r.getR72() != null) {
                respuestas.put("p72", r.getR72().toString());
            }
            if (r.getR73() != null) {
                respuestas.put("p73", r.getR73().toString());
            }
            if (r.getR74() != null) {
                respuestas.put("p74", r.getR74().toString());
            }

            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    @Override
    public boolean actualizarResultado(EncuestaServiciosResultados resultado) {
        if(resultado != null){
            f.setEntityClass(EncuestaServiciosResultados.class);
            f.edit(resultado);
        }
        
        Comparador<EncuestaServiciosResultados> comparador = new ComparadorEncuestaServicios();        
        return comparador.isCompleto(resultado);
    }

    @Override
    public void actualizarRespuestaPorPregunta(EncuestaServiciosResultados resultado, String pregunta, String respuesta, Map<String,String> respuestas) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(Short.parseShort(respuesta)); break;
            case "p2": resultado.setR2(Short.parseShort(respuesta)); break;
            case "p3": resultado.setR3(Short.parseShort(respuesta)); break;
            case "p4": resultado.setR4(Short.parseShort(respuesta)); break;
            case "p5": resultado.setR5(Short.parseShort(respuesta)); break;
            case "p6": resultado.setR6(Short.parseShort(respuesta)); break;
            case "p7": resultado.setR7(Short.parseShort(respuesta)); break;
            case "p8": resultado.setR8(Short.parseShort(respuesta)); break;
            case "p9": resultado.setR9(Short.parseShort(respuesta)); break;
            case "p10": resultado.setR10(Short.parseShort(respuesta)); break;
            case "p11": resultado.setR11(Short.parseShort(respuesta)); break;
            case "p12": resultado.setR12(Short.parseShort(respuesta)); break;
            case "p13": resultado.setR13(Short.parseShort(respuesta)); break;
            case "p14": resultado.setR14(Short.parseShort(respuesta)); break;
            case "p15": resultado.setR15(Short.parseShort(respuesta)); break;
            case "p16": resultado.setR16(Short.parseShort(respuesta)); break;
            case "p17": resultado.setR17(Short.parseShort(respuesta)); break;
            case "p18": resultado.setR18(Short.parseShort(respuesta)); break;
            case "p19": resultado.setR19(Short.parseShort(respuesta)); break;
            case "p20": resultado.setR20(Short.parseShort(respuesta)); break;
            case "p21": resultado.setR21(Short.parseShort(respuesta)); break;
            case "p22": resultado.setR22(Short.parseShort(respuesta)); break;
            case "p23": resultado.setR23(Short.parseShort(respuesta)); break;
            case "p24": resultado.setR24(Short.parseShort(respuesta)); break;
            case "p25": resultado.setR25(Short.parseShort(respuesta)); break;
            case "p26": resultado.setR26(Short.parseShort(respuesta)); break;
            case "p27": resultado.setR27(Short.parseShort(respuesta)); break;
            case "p28": resultado.setR28(Short.parseShort(respuesta)); break;
            case "p29": resultado.setR29(Short.parseShort(respuesta)); break;
            case "p30": resultado.setR30(Short.parseShort(respuesta)); break;
            case "p31": resultado.setR31(Short.parseShort(respuesta)); break;
            case "p32": resultado.setR32(Short.parseShort(respuesta)); break;
            case "p33": resultado.setR33(Short.parseShort(respuesta)); break;
            case "p34": resultado.setR34(Short.parseShort(respuesta)); break;
            case "p35": resultado.setR35(Short.parseShort(respuesta)); break;
            case "p36": resultado.setR36(Short.parseShort(respuesta)); break;
            case "p37": resultado.setR37(Short.parseShort(respuesta)); break;
            case "p38": resultado.setR38(Short.parseShort(respuesta)); break;
            case "p39": resultado.setR39(Short.parseShort(respuesta)); break;
            case "p40": resultado.setR40(Short.parseShort(respuesta)); break;
            case "p41": resultado.setR41(Short.parseShort(respuesta)); break;
            case "p42": resultado.setR42(Short.parseShort(respuesta)); break;
            case "p43": resultado.setR43(Short.parseShort(respuesta)); break;
            case "p44": resultado.setR44(Short.parseShort(respuesta)); break;
            case "p45": resultado.setR45(Short.parseShort(respuesta)); break;
            case "p46": resultado.setR46(Short.parseShort(respuesta)); break;
            case "p47": resultado.setR47(Short.parseShort(respuesta)); break;
            case "p48": resultado.setR48(Short.parseShort(respuesta)); break;
            case "p49": resultado.setR49(Short.parseShort(respuesta)); break;
            case "p50": resultado.setR50(Short.parseShort(respuesta)); break;
            case "p51": resultado.setR51(Short.parseShort(respuesta)); break;
            case "p52": resultado.setR52(Short.parseShort(respuesta)); break;
            case "p53": resultado.setR53(Short.parseShort(respuesta)); break;
            case "p54": resultado.setR54(Short.parseShort(respuesta)); break;
            case "p55": resultado.setR55(Short.parseShort(respuesta)); break;
            case "p56": resultado.setR56(Short.parseShort(respuesta)); break;
            case "p57": resultado.setR57(Short.parseShort(respuesta)); break;
            case "p58": resultado.setR58(Short.parseShort(respuesta)); break;
            case "p59": resultado.setR59(Short.parseShort(respuesta)); break;
            case "p60": resultado.setR60(Short.parseShort(respuesta)); break;
            case "p61": resultado.setR61(Short.parseShort(respuesta)); break;
            case "p62": resultado.setR62(Short.parseShort(respuesta)); break;
            case "p63": resultado.setR63(Short.parseShort(respuesta)); break;
            case "p64": resultado.setR64(Short.parseShort(respuesta)); break;
            case "p65": resultado.setR65(Short.parseShort(respuesta)); break;
            case "p66": resultado.setR66(Short.parseShort(respuesta)); break;
            case "p67": resultado.setR67(Short.parseShort(respuesta)); break;
            case "p68": resultado.setR68(Short.parseShort(respuesta)); break;
            case "p69": resultado.setR69(Short.parseShort(respuesta)); break;
            case "p70": resultado.setR70(Short.parseShort(respuesta)); break;
            case "p71": resultado.setR71(Short.parseShort(respuesta)); break;
            case "p72": resultado.setR72(Short.parseShort(respuesta)); break;
            case "p73": resultado.setR73(Short.parseShort(respuesta)); break;
            case "p74": resultado.setR74(Short.parseShort(respuesta)); break;
        }
        
        respuestas.put(pregunta, respuesta);
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Orientación a los estudiantes en su desarrollo personal y pedagógico.");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 1f, "Al solicitar el servicio de psicopedagogía el tiempo de respuesta fue:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 2f, "El trato que me brindó el psicólogo(a) fue:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 3f, "El nivel en que se cumplieron mis expectativas y necesidades, es:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 1f, 4f, "Considero que el servicio de apoyo psicopedagógico es:", ""));
        l.add(a1);
        
        Apartado a2 = new Apartado(2f);
        a2.setContenido("Actividades que promueven y facilitan el conocimiento de las artes, tales como danza, música, teatro.");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 2f, 5f, "Las opciones que me ofrece la Universidad en cuanto a talleres artísticos son:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 2f, 6f, "El desempeño del  profesor asignado al taller artístico es:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 2f, 7f, "La infraestructura física del taller artístico y el equipamiento de éste me parece que es:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 2f, 8f, "El horario asignado a la actividad artística que practico me parece:", ""));
        l.add(a2);
        
        Apartado a3 = new Apartado(3f);
        a3.setContenido("Atención y preservación de la salud (Interna).");
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 3f, 9f, "La atención que recibo en el servicio médico la califico como:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 3f, 10f, "El horario de atención del consultorio lo califico como:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 3f, 11f, "La limpieza e higiene del servicio médico me parece:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 3f, 12f, "Cuando voy a consulta médica el material de curación o medicamentos que se me proporciona son:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 3f, 13f, "El tiempo que espero para recibir atención médica es:", ""));
        l.add(a3);
        
        Apartado a4 = new Apartado(4f);
        a4.setContenido("Atención y preservación de la salud (Externa).");
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 4f, 14f, "La oportunidad con que recibo la información sobre el trámite de inscripción al IMSS:", ""));
        a4.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 4f, 15f, "La atención que me brindó el personal de la UT, que realiza el trámite del IMSS fue:", ""));
        l.add(a4);
        
        Apartado a5 = new Apartado(5f);
        a5.setContenido("Fomento de la salud por medio de actividades deportivas.");
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 5f, 16f, "En general, el desempeño de los entrenadores deportivos  me parece:", ""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 5f, 17f, "Las instalaciones deportivas en donde se ofrecen las actividades me parecen:", ""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 5f, 18f, "El material deportivo con que cuenta el departamento de deportes lo califico como:", ""));
        a5.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 5f, 19f, "El grado en que satisfacen mis intereses los deportes que ofrece la universidad es:", ""));
        l.add(a5);
        
        Apartado a6 = new Apartado(6f);
        a6.setContenido("Actividades sistemáticas de orientación y apoyo en el desempeño personal y académico de alumno (Tutoría).");
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 20f, "¿Cómo considera la cordialidad y capacidad del tutor para lograr crear un clima de confianza para que usted pueda exponer su problemática?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 21f, "¿En los problemas académicos y personales que afectan su rendimiento que interés muestra el tutor?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 22f, "¿La capacidad que tiene el tutor para orientarlo en metodología y técnicas de estudio, la considera?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 23f, "¿La capacidad del tutor para diagnosticar las dificultades y realizar las acciones pertinentes para resolverlas, considera que esta?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 24f, "¿El dominio que tiene el tutor de métodos pedagógicos para la atención individualizada o grupal, lo considera que está?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 25f, "¿Como ha mejorado la participación en el programa de tutoría en su desempeño académico?", ""));
        a6.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 6f, 26f, "¿El programa de tutoría, lo considera que está?", ""));
        l.add(a6);
        
        Apartado a7 = new Apartado(7f);
        a7.setContenido("Actividades sistemáticas de orientación y apoyo en el desempeño personal y académico de alumno (Asesoría Académica).");
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 7f, 27f, "¿La capacidad que tiene el asesor para resolver dudas académicas, la considera?", ""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 7f, 28f, "Cuando requiero una asesoría académica, la disposición de parte del profesor es:", ""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 7f, 29f, "El tiempo que me asignan para la asesoría académica es:", ""));
        a7.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 7f, 30f, "La asesoría académica aclara mis dudas:", ""));
        l.add(a7);
        
        Apartado a8 =  new Apartado(8f);
        a8.setContenido("Servicio de alimentos");
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 8f, 31f, "La atención que recibo en la cafetería, es:", ""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 8f, 32f, "La variedad y el sabor de los alimentos que ofrece la cafetería es:", ""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 8f, 33f, "El tiempo que espero para recibir el servicio es:", ""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 8f, 34f, "La cantidad de comida que recibo por lo que pago es:", ""));
        a8.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 8f, 35f, "Los precios que se manejan en la cafetería son accesibles para mí:", ""));
        l.add(a8);
        
        Apartado a9 =  new Apartado(9f);
        a9.setContenido("Actividades que favorecen el crecimiento personal, a través de pláticas, talleres, conferencias, etc.");
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 9f, 36f, "Participar en las actividades de desarrollo humano, me ayuda a ser una persona más responsable y consciente de mis decisiones, de manera:", ""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 9f, 37f, "Las actividades de desarrollo humano me permiten un mejor entendimiento de mi conducta:", ""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 9f, 38f, "Los temas que se manejan en estas actividades, me parecen:", ""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 9f, 39f, "Las experiencias obtenidas en estas actividades las he aplicado en mi vida cotidiana:", ""));
        a9.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 9f, 40f, "El desempeño de los conductores de las actividades de desarrollo humano lo califico como:", ""));
        l.add(a9);
        
        Apartado a10 =  new Apartado(10f);
        a10.setContenido("Apoyo bibliográfico para complemento o refuerzo del aprendizaje");
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 41f, "El servicio y la actitud del personal que me atiende es:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 42f, "El número de títulos de libros  y ejemplares disponibles en la biblioteca,  satisface las necesidades de mi carrera:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 43f, "La distribución, ordenamiento y clasificación de los títulos de libros ejemplares de la biblioteca los califico como:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 44f, "El material de consulta (periódicos, revistas, enciclopedias, manuales, etc.) disponible responde a las necesidades de mi carrera:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 45f, "El material electromagnético (CD-R, Videos, DVD´s, etc.) responde a mis necesidades:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 46f, "El horario de atención de la biblioteca responde a mis necesidades de consulta:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 47f, "Los servicios tales como: préstamos de libros, fotocopiado y otros que ofrece la biblioteca satisfacen mis necesidades:", ""));
        a10.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 10f, 48f, "El número de computadoras conectadas a Internet, disponibles en la biblioteca,  es suficiente para satisfacer mis necesidades:", ""));
        l.add(a10);
        
        Apartado a11 =  new Apartado(11f);
        a11.setContenido("Instalaciones adecuadas para las diferentes actividades curriculares");
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 49f, "El número de computadoras disponibles en la UT satisface la demanda de los estudiantes:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 50f, "El software instalado en los laboratorios satisface mis necesidades:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 51f, "El servicio de impresión para los estudiantes es:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 52f, "El servicio de escáner para los estudiantes es:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 53f, "El horario del los laboratorios responde a mis necesidades:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 54f, "El número de aulas y laboratorios existentes en la universidad lo considero:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 55f, "Considero el equipo y mobiliario de las aulas y laboratorios como:", ""));
        a11.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 11f, 56f, "Los cubículos destinados a los profesores, para recibir la tutoría o la asesoría académica los considero:", ""));
        l.add(a11);
        
        Apartado a12 =  new Apartado(12f);
        a12.setContenido("Traslado de los estudiantes a la universidad");
        a12.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 12f, 57f, "Las rutas actuales son suficientes para trasladarme a la institución:", ""));
        a12.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 12f, 58f, "El transporte público cuenta con rutas accesibles a las zonas donde los estudiantes lo necesitamos:", ""));
        a12.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 12f, 59f, "Los conductores de transporte público respetan las tarifas de descuento para estudiantes:", ""));
        a12.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 12f, 60f, "El desempeño en general de los conductores es:", ""));
        l.add(a12);
        
        Apartado a13 =  new Apartado(13f);
        a13.setContenido("Espacios de expresión con los estudiantes, tales como: radio, revista, etc.");
        a13.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 13f, 61f, "Los medios de expresión de la universidad son adecuados para mí:", ""));
        a13.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 13f, 62f, "Considero que los medios de expresión son suficientes:", ""));
        a13.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 13f, 63f, "A través de estos medios realmente puedo expresar lo que deseo:", ""));
        a13.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 13f, 64f, "Los medios de expresión de los estudiantes contribuyen a mi propia identificación:", ""));
        l.add(a13);
        
        Apartado a14 =  new Apartado(14f);
        a14.setContenido("Estímulos al desempeño académico de los estudiantes");
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 65f, "Las convocatorias para becas se publican en tiempo y forma:", ""));
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 66f, "La difusión en cuanto al tipo de beca es:", ""));
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 67f, "La orientación que me han dado respecto al tipo de beca que más me conviene es:", ""));
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 68f, "Las solicitudes son fáciles de llenar:", ""));
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 69f, "El horario de atención es:", ""));
        a14.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 14f, 70f, "El trato que he recibido en los trámites de beca es :", ""));
        l.add(a14);
        
        Apartado a15 =  new Apartado(15f);
        a15.setContenido("Bolsa de trabajo");
        a15.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 15f, 71f, "Los puestos que se ofrecen en la bolsa de trabajo son acordes a la formación académica:", ""));
        a15.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 15f, 72f, "El desempeño del personal de la bolsa de trabajo lo considera:", ""));
        a15.getPreguntas().add(new Opciones(EvaluacionesTipo.SERVICIOS.getNumero() , 15f, 73f, "El servicio de la bolsa de trabajo de la universidad es:", ""));
        l.add(a15);
        
        return l;
    }

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "MB", "Muy bien"));
        l.add(new SelectItem("4", "B", "Bien"));
        l.add(new SelectItem("3", "R", "Regular"));
        l.add(new SelectItem("2", "M", "Mal"));
        l.add(new SelectItem("1", "P", "Pésimo"));
        l.add(new SelectItem("0", "NA", "No aplica"));

        return l;
    }
    
    @Override
    public AlumnosEncuestas obtenerAlumnos(String matricula) {
//        TypedQuery<ViewAlumnos> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.periodo= 47 AND a.cuatrimestre=11 AND a.alumnoPK.matricula=:matricula", Alumno.class);
        TypedQuery<AlumnosEncuestas> q = f2.getEntityManager().createQuery("SELECT a from AlumnosEncuestas a WHERE a.matricula=:matricula", AlumnosEncuestas.class);
        q.setParameter("matricula", matricula);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEncuestaServicios.obtenerAlumnos() se ejecuto la consulta");
        List<AlumnosEncuestas> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }
    
    @Override
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) {
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares)f.find(evaluacion.getPeriodo());
    }
    
    @Override
    public List<ListaEncuestaServicios> obtenerEncuestasRealizadas(){
        TypedQuery<ListaEncuestaServicios> q=f.getEntityManager().createQuery("SELECT l FROM ListaEncuestaServicios l",ListaEncuestaServicios.class);
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuesta.obtenerEncuestasRealizadas() se ejecuto la consulta de datos ");
        List<ListaEncuestaServicios> pr=q.getResultList();
        return pr;
    }

}
