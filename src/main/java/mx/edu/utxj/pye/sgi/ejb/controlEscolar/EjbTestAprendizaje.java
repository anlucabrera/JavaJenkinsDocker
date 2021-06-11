/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizajePK;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorTestDiagnosticoAprendizaje;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Planeacion
 */
@Stateless
public class EjbTestAprendizaje {

    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em, em2;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<Boolean> tieneAccesoTest(){
        VariablesProntuario v = em.createQuery("select v from VariablesProntuario as v where v.nombre = :nombre", VariablesProntuario.class)
                .setParameter("nombre", "denegarAccesoTest")
                .getResultStream()
                .findFirst().get();
        if(v.getValor().equals("1")) return ResultadoEJB.crearErroneo(2, "No hay acceso al Test de Diagnóstico de Aprendizaje", Boolean.class);
        return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El estudiante tiene acceso al Test");
    }
    
    public ResultadoEJB<Evaluaciones> obtenerTestActivo(){
        Evaluaciones e = em.createQuery("select e from Evaluaciones as e WHERE e.tipo = :tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin", Evaluaciones.class)
                .setParameter("tipo", "Test de Diagnósitco de Aprendizaje")
                .setParameter("fecha", new Date())
                .getResultStream()
                .findFirst().orElse(new Evaluaciones());
        if(e.equals(new Evaluaciones())) return ResultadoEJB.crearErroneo(2, "No se encontró evaluación activa", Evaluaciones.class);
        return ResultadoEJB.crearCorrecto(e, "Evaluación activa");
    }
    
    public ResultadoEJB<Estudiante> validarEstudiante(Integer matricula, Integer periodo){
        Estudiante e = em.createQuery("select e "
                + "from Estudiante as e "
                + "where e.matricula = :matricula "
                + "and e.periodo = :periodo "
                + "and (e.grupo.grado <> :grado1 or e.grupo.grado <> :grado2)", Estudiante.class)
                .setParameter("matricula", matricula)
                .setParameter("periodo", periodo)
                .setParameter("grado1", 11)
                .setParameter("grado2", 6)
                .getResultStream().findFirst().orElse(new Estudiante());
        if(e.equals(new Estudiante())) return ResultadoEJB.crearErroneo(2, "El estudiante que ingreso no se encuentra o está en periodo de estadía", Estudiante.class);
        return ResultadoEJB.crearCorrecto(e, "Estudiante activo");
                
    }
    
    public ResultadoEJB<Alumnos> validarAlumnos(String matricula, Integer periodo){
        Alumnos a = f2.getEntityManager().createQuery("SELECT a FROM Alumnos as a "
                + "where a.matricula = :matricula "
                + "and a.grupos.gruposPK.cvePeriodo = :periodo "
                + "and (a.gradoActual <> :grado1 or a.gradoActual <> :grado2)", Alumnos.class)
                .setParameter("matricula", matricula)
                .setParameter("periodo", periodo)
                .setParameter("grado1", Short.parseShort("11"))
                .setParameter("grado2", Short.parseShort("6"))
                .getResultStream().findFirst().orElse(new Alumnos());
        if(a.equals(new Alumnos())) return ResultadoEJB.crearErroneo(1, "El estudiante que ingrso no se encuentra o está en periodo de estadia", Alumnos.class);
        return ResultadoEJB.crearCorrecto(a, "Estudiante activo");
    }
    
    public ResultadoEJB<Boolean> verificarTestCompleto(Evaluaciones evaluacion, Integer matricula){
        TestDiagnosticoAprendizaje tda = em.createQuery("select t from TestDiagnosticoAprendizaje as t where t.testDiagnosticoAprendizajePK.evaluacion = :evaluacion and t.testDiagnosticoAprendizajePK.evaluador = :evaluador", TestDiagnosticoAprendizaje.class)
                .setParameter("evaluacion", evaluacion.getEvaluacion())
                .setParameter("evaluador", matricula)
                .getResultStream().findFirst().orElse(new TestDiagnosticoAprendizaje());
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        if(comparador.isCompleto(tda)){
            return ResultadoEJB.crearCorrecto(Boolean.TRUE,"La evaluacion esta completa");
        }else {
            return ResultadoEJB.crearErroneo(1, Boolean.FALSE,"La encuesta no ha sido finalzada");
        }
    }
    
    public ResultadoEJB<TestDiagnosticoAprendizaje> obtenerResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas){
        try {
            TestDiagnosticoAprendizajePK tdaPK = new TestDiagnosticoAprendizajePK(evaluacion.getEvaluacion(), evaluador);
            f.setEntityClass(TestDiagnosticoAprendizaje.class);
            
            TestDiagnosticoAprendizaje r = em.find(TestDiagnosticoAprendizaje.class, tdaPK);
            if (r == null) {
                r = new TestDiagnosticoAprendizaje(tdaPK);
                r.setFechaAplicacion(new Date());
                em.persist(r);
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
            return ResultadoEJB.crearCorrecto(r, "Agregado correctamente");
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    public ResultadoEJB<Boolean> actualizarResultado(TestDiagnosticoAprendizaje test){
        if(test != null){
            em.merge(test);
            //System.out.println("Resultado actualizado");
        }
        
        Comparador<TestDiagnosticoAprendizaje> comparador = new ComparadorTestDiagnosticoAprendizaje();
        return ResultadoEJB.crearCorrecto(comparador.isCompleto(test), "Test completo");
    }
    
    public void actualizarRespuestaPregunta(TestDiagnosticoAprendizaje resultado, String pregunta, String respuesta, Map<String,String> respuestas){
        switch (pregunta.trim()) {
            case "p1": resultado.setR1(Short.parseShort(respuesta)); break;
            case "p5": resultado.setR5(Short.parseShort(respuesta)); break;
            case "p9": resultado.setR9(Short.parseShort(respuesta)); break;
            case "p10": resultado.setR10(Short.parseShort(respuesta)); break;
            case "p11": resultado.setR11(Short.parseShort(respuesta)); break;
            case "p16": resultado.setR16(Short.parseShort(respuesta)); break;
            case "p17": resultado.setR17(Short.parseShort(respuesta)); break;
            case "p22": resultado.setR22(Short.parseShort(respuesta)); break;
            case "p26": resultado.setR26(Short.parseShort(respuesta)); break;
            case "p27": resultado.setR27(Short.parseShort(respuesta)); break;
            case "p32": resultado.setR32(Short.parseShort(respuesta)); break;
            case "p36": resultado.setR36(Short.parseShort(respuesta)); break;
            case "p2": resultado.setR2(Short.parseShort(respuesta)); break;
            case "p3": resultado.setR3(Short.parseShort(respuesta)); break;
            case "p12": resultado.setR12(Short.parseShort(respuesta)); break;
            case "p13": resultado.setR13(Short.parseShort(respuesta)); break;
            case "p15": resultado.setR15(Short.parseShort(respuesta)); break;
            case "p19": resultado.setR19(Short.parseShort(respuesta)); break;
            case "p20": resultado.setR20(Short.parseShort(respuesta)); break;
            case "p23": resultado.setR23(Short.parseShort(respuesta)); break;
            case "p24": resultado.setR24(Short.parseShort(respuesta)); break;
            case "p28": resultado.setR28(Short.parseShort(respuesta)); break;
            case "p29": resultado.setR29(Short.parseShort(respuesta)); break;
            case "p33": resultado.setR33(Short.parseShort(respuesta)); break;
            case "p4": resultado.setR4(Short.parseShort(respuesta)); break;
            case "p6": resultado.setR6(Short.parseShort(respuesta)); break;
            case "p7": resultado.setR7(Short.parseShort(respuesta)); break;
            case "p8": resultado.setR8(Short.parseShort(respuesta)); break;
            case "p14": resultado.setR14(Short.parseShort(respuesta)); break;
            case "p18": resultado.setR18(Short.parseShort(respuesta)); break;
            case "p21": resultado.setR21(Short.parseShort(respuesta)); break;
            case "p25": resultado.setR25(Short.parseShort(respuesta)); break;
            case "p30": resultado.setR30(Short.parseShort(respuesta)); break;
            case "p31": resultado.setR31(Short.parseShort(respuesta)); break;
            case "p34": resultado.setR34(Short.parseShort(respuesta)); break;
            case "p35": resultado.setR35(Short.parseShort(respuesta)); break;
            
        }
        respuestas.put(pregunta, respuesta);
    }
    
    public ResultadoEJB<List<Apartado>> obtenerApartados(){
        List<Apartado> l = new ArrayList<>();
        
        Apartado a1 = new Apartado(1f);
        a1.setContenido("Visual");
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 1f, "Puedo recordar algo mejor si lo escribo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 5f, "Puedo visualizar imágenes en mi cabeza:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 9f, "Tomo muchas notas de lo que leo y escucho:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 10f, "Me ayuda MIRAR  a la persona que está hablando. Me mantiene enfocado:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 11f, "Se me hace difícil entender lo que una persona está diciendo si hay ruidos alrededor:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 16f, "Es más fácil para mi hacer un trabajo en un lugar tranquilo:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 17f, "Me resulta fácil entender mapas, tablas y gráficos:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 22f, "Cuando estoy concentrado leyendo o escribiendo, la radio me molesta:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 26f, "Cuando estoy en un examen, puedo \"ver\" la página en el libro de textos y la respuesta:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 27f, "No puedo recordar una broma lo suficiente para contarla luego:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 32f, "Cuando estoy tratando de recordar algo nuevo (número de teléfono), me ayuda formarme una imagen mental:", ""));
        a1.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 1f, 36f, "Cuando tengo una gran idea, debo escribirla inmediatamente, o la olvido con facilidad:", ""));
        l.add(a1);
        
        Apartado a2 = new Apartado(2f);
        a2.setContenido("Auditivo");
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 2f, "Al leer, oigo las palabras en mi cabeza o leo en voz alta:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 3f, "Necesito hablar las cosas para entenderlas mejor:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 12f, "Prefiero que alguien me diga cómo tengo  que hacer las cosas que leer las instrucciones:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 13f, "Prefiero escuchar una conferencia o una grabación a leer un libro:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 15f, "Puedo seguir fácilmente  a una persona que está hablando, aunque mi cabeza esté hacia abajo:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 19f, "Recuerdo mejor lo que la gente dice que su aspecto:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 20f, "Recuerdo mejor si estudio en voz alta con alguien:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 23f, "Me resulta difícil crear imágenes en mi cabeza:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 24f, "Me resulta útil decir en voz alta las tareas que tengo que hacer:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 28f, "Al aprender algo nuevo, prefiero escuchar la información, luego leer y luego hacerlo:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 29f, "Me gusta completar una tarea antes de comenzar otra:", ""));
        a2.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 2f, 33f, "Para obtener una nota extra, pefiero grabar un informe a escribirlo:", ""));
        l.add(a2);
        
        Apartado a3 = new Apartado(3f);
        a3.setContenido("Kinestésico");
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 4f, "No me gusta leer o escuchar instrucciones, prefiero simplemente comenzar a hacer las cosas:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 6f, "Puedo estudiar mejor si escucho música:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 7f, "Necesito recreos frecuentes cuando estudio:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 8f, "Pienso mejor cuando tnego la libertad de moverme, estar sentado detrás de un escritorio no es para mi:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 14f, "Cuando no puedo pensar en una palabra específica, uso mis manos y llamo al objeto \"coso\":", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 18f, "Cuando comienzo un artículo o un libro, prefiero espiar la última página:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 21f, "Tomo notas, pero nunca vuelvo a releerlas:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 25f, "Mi cuaderno y mi escritorio pueden verse un desastre, pero sé exactamente dónde está cada cosa:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 30f, "Uso mis dedos para contar y muevo los labios cuando leo:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 31f, "No me gusta releer mi trabajo:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 34f, "Fantaseo en clase:", ""));
        a3.getPreguntas().add(new Opciones(EvaluacionesTipo.TEST_DIAGNOSTICO_APRENDIZAJE.getNumero() , 3f, 35f, "Para obtener una calificación extra, prefiero crear un proyecto a escribir un informe:", ""));
        l.add(a3);
        
        return ResultadoEJB.crearCorrecto(l, "Apartados agregados correctamente");
    }
    
    public ResultadoEJB<List<SelectItem>> obtenerRespuestasPosibles(){
        List<SelectItem> l = new ArrayList<>();
        l.add(new SelectItem("5", "CS", "Casi siempre = 5"));
        l.add(new SelectItem("4", "F", "Frecuentemente = 4"));
        l.add(new SelectItem("3", "AV", "A veces = 3"));
        l.add(new SelectItem("2", "RV", "Rara vez = 2"));
        l.add(new SelectItem("1", "CN", "Casi nunca = 1"));
        
        return ResultadoEJB.crearCorrecto(l, "Items creados correctamente");
    }
    
    public ResultadoEJB<Personas> obtenerPersona(Integer cvePersona){
        Personas p = f2.getEntityManager().createQuery("select p from Personas as p where p.personasPK.cvePersona = :cvePersona", Personas.class)
                .setParameter("cvePersona", cvePersona)
                .getResultStream().findFirst().get();
        return ResultadoEJB.crearCorrecto(p, "Persona");
    }
    
    public ResultadoEJB<CarrerasCgut> obtenerPE(Integer carrea){
        CarrerasCgut c = f2.getEntityManager().createQuery("select c from CarrerasCgut as c where c.cveCarrera = :carrera", CarrerasCgut.class)
                .setParameter("carrera", carrea)
                .getResultStream().findFirst().get();
        return ResultadoEJB.crearCorrecto(c, "Carrera");
    }
    
}
