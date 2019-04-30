/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultadosPK;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewEstudianteAsesorAcademico;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbEvaluacionEstadia {
    
    @Getter @Setter private Integer periodo;
    @EJB private Facade f;
    @EJB private Facade2 f2;
    
    public Evaluaciones getEvaluacionActiva(){
        List<Evaluaciones> e = f.getEntityManager()
                .createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc",
                        Evaluaciones.class)
                .setParameter("tipo", "Evaluación Estadía")
                .setParameter("fecha", new Date())
                .getResultStream().collect(Collectors.toList());
        if(e.isEmpty()){
            return new Evaluaciones();
        }else{
            return e.get(0);
        }
    }
    
    public Alumnos obtenerAlumnos(String matricula) {
        Short grado = 11;
        TypedQuery<Periodos> periodoAct = f2.getEntityManager().createQuery("SELECT p FROM Periodos AS p",Periodos.class);
        List<Periodos> periodos = periodoAct.getResultList();
        periodos.stream().forEach(x -> {
            Boolean activo = true;
            Boolean acv = x.getActivo().equals(true);
            if (activo.equals(acv)) {
                periodo = x.getPeriodosPK().getCvePeriodo();
                System.out.println("Periodo:"+ periodo);
            }
        });
//        TypedQuery<ViewAlumnos> q = f.getEntityManager().createQuery("SELECT a from Alumno a WHERE a.alumnoPK.periodo= 47 AND a.cuatrimestre=11 AND a.alumnoPK.matricula=:matricula", Alumno.class);
        TypedQuery<Alumnos> q = f2.getEntityManager()
                .createQuery("SELECT a from Alumnos a " 
                        + "WHERE a.matricula=:matricula AND "
                        + "a.cveStatus = :estatus AND "
                        + "a.grupos.gruposPK.cvePeriodo = :periodo AND "
                        + "a.gradoActual = :grado", Alumnos.class);
        q.setParameter("estatus", 1);
        q.setParameter("periodo", periodo);
        q.setParameter("grado", grado);
        q.setParameter("matricula", matricula);
        //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEncuestaServicios.obtenerAlumnos() se ejecuto la consulta");
        List<Alumnos> l = q.getResultList();
        if(!l.isEmpty()){
            return l.get(0);
        }else{
            return null;
        }
    }
    
    public EvaluacionEstadiaResultados getResultado(Evaluaciones evaluacion, String evaluador, Integer evaluado, Map<String,String> respuestas) {
        try {
            EvaluacionEstadiaResultadosPK pk = new EvaluacionEstadiaResultadosPK(evaluacion.getEvaluacion(),evaluador, evaluado);
            f.setEntityClass(EvaluacionEstadiaResultados.class);

            EvaluacionEstadiaResultados r = (EvaluacionEstadiaResultados) f.find(pk);

            if (r == null) {
                r = new EvaluacionEstadiaResultados(pk);
                f.create(r);
            }

            if (r.getR1() != null) {
                respuestas.put("p1", r.getR1());
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
            return r;
        } catch (NullPointerException ne) {
            return null;
        }
    }
    
    public ViewEstudianteAsesorAcademico viewEstudianteAsesorAcademico(String matricula){
        List<ViewEstudianteAsesorAcademico> veaa = f2.getEntityManager()
                .createQuery("select v from ViewEstudianteAsesorAcademico  as v where v.matricula = :matricula",ViewEstudianteAsesorAcademico.class)
                .setParameter("matricula", matricula).getResultList();
        if(!veaa.isEmpty()){
            return veaa.get(0);
        }else{
            return new ViewEstudianteAsesorAcademico();
        }
    }
    
    public boolean actualizarResultado(EvaluacionEstadiaResultados resultado) {
        if(resultado != null){
            f.setEntityClass(EvaluacionEstadiaResultados.class);
            f.edit(resultado);
        }
        
        Comparador<EvaluacionEstadiaResultados> comparador = new ComparadorEvaluacionEstadia();        
        return comparador.isCompleto(resultado);
    }
    
    public void actualizarRespuestaPorPregunta(EvaluacionEstadiaResultados resultado, String pregunta, String respuesta, Map<String,String> respuestas) {
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(respuesta); break;
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
        }
        
        respuestas.put(pregunta, respuesta);
    }
    
    
    public List<Apartado> getApartados() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 1f, "Tu proyecto de estadía de Ingeniería se orientó a:", ""));
        l.add(a1);
        return l;
    }
    
    public List<Apartado> getApartados1() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Durante la estadía el asesor de la Universidad:");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 2f, "Te visitó periódicamente en la empresa donde realizaste tu estadía:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 3f, "Te asesoró durante el desarrollo del proyecto:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 4f, "Te proporcionó la asesoría de manera clara y precisa.:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 5f, "Te recibió para asesoría en las fechas y horarios establecidos:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 6f, "Se coordinó con el asesor de la empresa para apoyarte en la realización del proyecto:", ""));
        l.add(a1);
        
        Apartado a2 = new Apartado(2f);
        a2.setContenido("Durante la estadía el asesor de la empresa (o en su caso asesor de proyecto de la UT):");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 2f, 7f, "Estableció el objetivo del proyecto y de cada etapa del proceso:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 2f, 8f, "Mantuvo un seguimiento constante al proyecto:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 2f, 9f, "Asesoró en la realización del proyecto:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 2f, 10f, "Te facilitó las herramientas y equipo necesario para el desarrollo de tu proyecto.:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 2f, 11f, "Mostró conocimientos y experiencia en las áreas relacionadas con tu carrera:", ""));
        l.add(a2);
        return l;
    }
    
    public List<Apartado> getApartados2() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("¿Recibiste alguno de los siguientes apoyos por parte de la Universidad, durante la estadía de TSU?");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 12f, "Orientación en la definición de tu proyecto:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 13f, "Acceso a salas de cómputo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 14f, "Acceso a salas de idiomas:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 15f, "Acceso a servicios de consulta de información:", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<Apartado> getApartados3() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Durante la Estadía de Ingeniería ¿Recibiste alguno de los siguientes apoyos por parte de la empresa (o en su caso de la Universidad, para proyectos donde no participaron empresas)?");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 16f, "Económico:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 17f, "Transporte:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 18f, "Comedor:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 19f, "Capacitación:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 20f, "Impresión y/o fotocopiado:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 21f, "Internet:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 22f, "Área de trabajo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 23f, "Herramientas y equipo de trabajo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 24f, "Información sobre políticas de la empresa:", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<Apartado> getApartados4() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("¿Cómo consideras que fue el apoyo que recibiste en cada una de las etapas de la estadía de Ingeniería?:");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 25f, "Análisis del problema a resolver:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 26f, "Proyecto o plan de trabajo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 27f, "Implementación de estrategias y procedimientos para resolver el problema:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 28f, "Implementación de actividades del proyecto:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 29f, "Impresión y/o fotocopiado:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 30f, "Medición y evaluación de resultados:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 31f, "Elaboración de reportes parciales de avances:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 32f, "Elaboración de reporte final:", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<Apartado> getApartados5() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Durante la Estadía de Ingeniería ¿Tuviste la oportunidad de practicar o desarrollar algunas de las siguientes habilidades?:");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 33f, "Manejo de equipamiento, procedimientos:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 34f, "Trabajo en equipo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 35f, "Toma de decisiones:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 36f, "Manejo del idioma inglés:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 37f, "Manejo de la computadora:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 38f, "Control de procesos:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 39f, "Liderazgo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 40f, "Expresión oral y escrita en español:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 41f, "Solución de Problemas:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 42f, "Capacidad de negociación:", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<Apartado> getApartados6() {
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("¿Cómo calificarías la estadía de Ingeniería que realizaste?:");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 43f, "Aplicación de conocimientos adquiridos durante la carrera:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 44f, "Relación con tu carrera:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 45f, "Relación con el sector productivo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 46f, "Solución del problema planteado:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 47f, "Retroalimentación del progreso del proyecto:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.EVALUACION_ESTADIA.getNumero() , 1f, 48f, "Experiencia adquirida para desempeñarte en el sector productivo:", ""));
        l.add(a1);
        
        return l;
    }
    
    public List<SelectItem> getRespuestasPosibles1(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("4", "S", "Siempre"));
        l.add(new SelectItem("3", "CS", "Casi Siempre"));
        l.add(new SelectItem("2", "CN", "Casi Nunca"));
        l.add(new SelectItem("1", "N", "Nunca"));
        return l;
    }
    
    public List<SelectItem> getRespuestasPosibles2(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("4", "S", "Siempre"));
        l.add(new SelectItem("3", "CS", "Casi Siempre"));
        l.add(new SelectItem("2", "CN", "Casi Nunca"));
        l.add(new SelectItem("1", "N", "Nunca"));
        l.add(new SelectItem("0", "NR", "Nunca lo Requerí"));
        return l;
    }
    
    public List<SelectItem> getRespuestasPosibles3(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "E", "Excelente"));
        l.add(new SelectItem("4", "MB", "Muy Bueno"));
        l.add(new SelectItem("3", "R", "Regular"));
        l.add(new SelectItem("2", "D", "Deficiente"));
        l.add(new SelectItem("1", "M", "Malo"));
        return l;
    }
    
    public List<SelectItem> getRespuestasPosibles4(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "E", "Excelente"));
        l.add(new SelectItem("4", "MB", "Muy Bueno"));
        l.add(new SelectItem("3", "R", "Regular"));
        l.add(new SelectItem("2", "M", "Mala"));
        l.add(new SelectItem("1", "P", "Pésima"));
        return l;
    }
    
    public List<SelectItem> getRespuestasPosibles5(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("La solución de un problema dentro de la empresa asignada", "La solución de un problema dentro de la empresa asignada", "La solución de un problema dentro de la empresa asignada"));
        l.add(new SelectItem("El desarrollo de proyecto innovador dentro de la empresa asignada", "El desarrollo de proyecto innovador dentro de la empresa asignada", "El desarrollo de proyecto innovador dentro de la empresa asignada"));
        l.add(new SelectItem("El desarrollo de un proyecto dentro de la universidad", "El desarrollo de un proyecto dentro de la universidad", "El desarrollo de un proyecto dentro de la universidad"));
        l.add(new SelectItem("La realización de un proyecto de desarrollo comunitario, sin la participación de empresas.", "La realización de un proyecto de desarrollo comunitario, sin la participación de empresas.", "La realización de un proyecto de desarrollo comunitario, sin la participación de empresas."));
        return l;
    }
}
