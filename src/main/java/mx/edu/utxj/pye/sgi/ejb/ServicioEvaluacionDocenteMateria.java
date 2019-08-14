/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.model.SelectItem;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.TipoCuestionario;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultadosPK;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author Carlos Alfredo Vargas Galindo
 */
@Stateful
public class ServicioEvaluacionDocenteMateria implements EJBEvaluacionDocenteMateria {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    Facade f;
    @EJB
    Facade2 f2;

    @Override
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("5", "E", "Excelente"));
        l.add(new SelectItem("4", "MB", "Muy Bueno"));
        l.add(new SelectItem("3", "B", "Bueno"));
        l.add(new SelectItem("2", "R", "Regular"));
        l.add(new SelectItem("1", "D", "Deficiente"));
        return l;
    }

    @Override
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();

        Apartado a1 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Asiste normalmente a clases y si falta lo justifica?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 2.0f, "¿Cumple el horario de inicio y finalización de la clase?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 3.0f, "¿Cumple con su labor de asesorías?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 4.0f, "¿Cumple con el temario de la asignatura?", ""));
        Apartado a2 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 5.0f, "¿Explica al inicio del ciclo los objetivos, contenidos, metodología, evaluación y bibliografía de la materia?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 6.0f, "¿Desarrolla en clase los contenidos del programa de la materia?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 7.0f, "¿Desarrolla las clases del saber de acuerdo a la Programación Académica?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 8.0f, "¿Desarrolla las clases del saber hacer  de acuerdo a la Programación Académica?", ""));
        Apartado a3 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 9.0f, "¿Explica con claridad los conceptos de cada tema?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 10.0f, "¿Las clases están bien preparadas, organizadas y estructuradas?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 11.0f, "¿Se preocupa de los problemas de aprendizaje de sus estudiantes?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 12.0f, "¿Te motiva para que participes crítica y activamente en el desarrollo de la clase?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 13.0f, "¿La comunicación profesor-estudiante es fluida y espontánea, creando un clima de confianza?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 14.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 15.0f, "¿Es amable y respetuoso con los estudiantes?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Es accesible y está dispuesto a ayudarlos?", ""));
        Apartado a4 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "¿Desarrolla con los estudiantes las pruebas evaluativas realizadas para su mejor comprensión?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 18.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 19.0f, "¿Entrega las notas en el plazo establecido?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 20.0f, "¿Evalúa el  conocimiento del saber a través de un examen escrito u otra  herramienta?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 21.0f, "¿Evalúa la parte del saber hacer a través de la práctica, ejercicio práctico  u otra herramienta?", ""));
        Apartado a5 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 22.0f, "¿Los recursos didácticos utilizados por el docente, te ayudaron a comprender mejor la asignatura?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 23.0f, "¿El docente te motivó al uso de la biblioteca y aulas virtuales para reforzar tus conocimientos?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 24.0f, "¿Te incentiva a participar en actividades complementarias como: seminarios lecturas, charlas y congresos?", ""));
        Apartado a6 = new Apartado(6F, "INVESTIGACIÓN", new SerializableArrayList<>());
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 25.0f, "¿Promueve la investigación?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 26.0f, "¿Involucra a los estudiantes en sus proyectos de investigación con su práctica docente?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 27.0f, "¿Te motiva a participar en Congresos científico estudiantiles?", ""));
        Apartado a7 = new Apartado(7F, "PROYECCION SOCIAL", new SerializableArrayList<>());
        a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 28.0f, "¿Te incentiva a Participar en los eventos de extensión Universitaria?", ""));
        a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 29.0f, "¿Te motiva a participar en las charlas, platicas y sesiones de valores?", ""));
        a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 30.0f, "¿Involucra a los estudiantes para conservar el orden y limpieza del aula?", ""));
        a7.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 31.0f, "¿Informa a los estudiantes de todas las actividades de Proyección Social Y Extensión Universitaria?", ""));
        Apartado a8 = new Apartado(8F, "OBSERVACIONES Y/O COMENTARIOS", new SerializableArrayList<>());
        a8.getPreguntas().add(new Abierta(TipoCuestionario.EDOCENTE, 1.0f, 32.0f, "Comentarios hacia el tutor", 20));
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        l.add(a5);
        l.add(a6);
        l.add(a7);
        l.add(a8);
        return l;
    }

    @Override
    public ResultadoEJB<Evaluaciones> getEvDocenteActiva() {
        try{
            Evaluaciones evaluacion = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc",Evaluaciones.class)
                    .setParameter("tipo","Docente materia")
                    .setParameter("fecha",new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"No encontro la evaluacion");}
            else{return ResultadoEJB.crearCorrecto(evaluacion,"Se encontro la evaluación");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EJBEvaluacionDocenteMteria, getEvDocenteActiva)", e, null);
        }
    }

    @Override
    public ResultadoEJB<Evaluaciones> getUltimaEvDocenteActiva() {
        try{
            //TODO:Obtiene la ultima evalaucion de docente materia activa
            Evaluaciones evaluacion = f.getEntityManager().createQuery("select e from Evaluaciones e where e.tipo=:tipo order by e.evaluacion desc ",Evaluaciones.class)
                    .setParameter("tipo", "Docente materia")
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"No se encontro ninguna evaluacion");}
            else{return ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion encontrada"); }
        }
        catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la última evaluacion activa(EJBEvaluacionDocenteMteria, getUltimaEvDocenteActiva)", e, null);
        }

    }

    @Override
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) {
        f.setEntityClass(PeriodosEscolares.class);
        return (PeriodosEscolares) f.find(evaluacion.getPeriodo());
    }

    @Override
    public ResultadoEJB<PeriodosEscolares> getPeriodoEvaluacion(Evaluaciones evaluacion) {
        try{
            //TODO:Busca el periodo segun el periodo de la evaluacion
            PeriodosEscolares periodoEv = f.getEntityManager().createQuery("select p from PeriodosEscolares p where p.periodo=:periodo",PeriodosEscolares.class)
                    .setParameter("periodo",evaluacion.getPeriodo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;
            if(periodoEv==null){return ResultadoEJB.crearErroneo(2,periodoEv,"No se encontro el periodo");}
            else {return ResultadoEJB.crearCorrecto(periodoEv,"Se encontrol el periodo de la evaluación");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo de la evalaucion(EJBEvaluacionDocenteMteria, getUltimaEvDocenteActiva)", e, null);
        }
    }

    @Override
    public List<VistaEvaluacionDocenteMateriaPye> getDocenteMAteria(String matricula, Integer periodo) {
        TypedQuery<VistaEvaluacionDocenteMateriaPye> q = f2.getEntityManager()
                .createQuery("SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.matricula =:matricula AND v.periodo = :periodo", VistaEvaluacionDocenteMateriaPye.class);
        q.setParameter("matricula", matricula);
        q.setParameter("periodo", periodo);
        List<VistaEvaluacionDocenteMateriaPye> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return new ArrayList<>();
        } else {
            return l;
        }
    }

    @Override
    public ResultadoEJB<List<VistaEvaluacionDocenteMateriaPye>> getDocenteMateriabyMatricula(String matricula) {
        try {
            //TODO: La consulta en la vista por matricula
            List<VistaEvaluacionDocenteMateriaPye> materiasEstudiante = f2.getEntityManager().createQuery("select v from VistaEvaluacionDocenteMateriaPye v where v.matricula=:matricula",VistaEvaluacionDocenteMateriaPye.class)
                    .setParameter("matricula",matricula)
                    .getResultList();
            if(materiasEstudiante.isEmpty() || materiasEstudiante ==null){return ResultadoEJB.crearErroneo(2,materiasEstudiante,"Lista de materias vacía");}
            else{return ResultadoEJB.crearCorrecto(materiasEstudiante,"Se encontraron las materis del estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por estudiante (EJBEvaluacionDocenteMteria, getDocenteMateriabuMatricula)", e, null);
        }
    }

    @Override
    public EvaluacionDocentesMateriaResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionDocenteMateriaPye docenteMateriaPye) {
        TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.evaluacion=:evaluacion AND e.evaluacionDocentesMateriaResultadosPK.evaluador=:evaluador AND e.evaluacionDocentesMateriaResultadosPK.evaluado=:evaluado", EvaluacionDocentesMateriaResultados.class);
        q.setParameter("evaluacion", evaluacion.getEvaluacion());
        q.setParameter("evaluador", Integer.parseInt(docenteMateriaPye.getMatricula()));
        q.setParameter("evaluador", Integer.parseInt(docenteMateriaPye.getNumeroNomina()));
        List<EvaluacionDocentesMateriaResultados> l = q.getResultList();
        if (!l.isEmpty()) {
            return l.get(0);
        } else {
            f.setEntityClass(EvaluacionDocentesMateriaResultados.class);
            EvaluacionDocentesMateriaResultados resultados = new EvaluacionDocentesMateriaResultados(evaluacion.getEvaluacion(), Integer.parseInt(docenteMateriaPye.getMatricula()), docenteMateriaPye.getCveMateria(), Integer.parseInt(docenteMateriaPye.getNumeroNomina()));
            f.create(resultados);
            return resultados;
        }
    }

    @Override
    public Evaluaciones evaluacionActiva() {
        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("buscar_evaluacion_docentes_materia_activa", Evaluaciones.class);
        List<Evaluaciones> l = spq.getResultList();
        if (l == null || l.isEmpty()) {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionDocenteMateria.evaluacionActiva() la evaluacion es nula");
            return new Evaluaciones();
        } else {
//            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionDocenteMateria.evaluacionActiva() la evaluacion activa es : " + l.get(0).getEvaluacion());

            return l.get(0);
        }
    }

    @Override
    public Evaluaciones ultimaEvaluacionDocenteMaterias() {
        TypedQuery<Evaluaciones> q = f.getEntityManager().createQuery("SELECT e FROM Evaluaciones e where e.tipo=:tipo ORDER BY e.evaluacion DESC", Evaluaciones.class);
        List<Evaluaciones> l = q.setParameter("tipo", "Docente materia").getResultList();
        return l.get(0);
    }

    @Override
    public void actualizarRespuestaPorPregunta(EvaluacionDocentesMateriaResultados resultado, Float pregunta, String respuesta) {
        switch (pregunta.toString()) {
            case "1.0":
                resultado.setR1(Short.parseShort(respuesta));
                break;
            case "2.0":
                resultado.setR2(Short.parseShort(respuesta));
                break;
            case "3.0":
                resultado.setR3(Short.parseShort(respuesta));
                break;
            case "4.0":
                resultado.setR4(Short.parseShort(respuesta));
                break;
            case "5.0":
                resultado.setR5(Short.parseShort(respuesta));
                break;
            case "6.0":
                resultado.setR6(Short.parseShort(respuesta));
                break;
            case "7.0":
                resultado.setR7(Short.parseShort(respuesta));
                break;
            case "8.0":
                resultado.setR8(Short.parseShort(respuesta));
                break;
            case "9.0":
                resultado.setR9(Short.parseShort(respuesta));
                break;
            case "10.0":
                resultado.setR10(Short.parseShort(respuesta));
                break;
            case "11.0":
                resultado.setR11(Short.parseShort(respuesta));
                break;
            case "12.0":
                resultado.setR12(Short.parseShort(respuesta));
                break;
            case "13.0":
                resultado.setR13(Short.parseShort(respuesta));
                break;
            case "14.0":
                resultado.setR14(Short.parseShort(respuesta));
                break;
            case "15.0":
                resultado.setR15(Short.parseShort(respuesta));
                break;
            case "16.0":
                resultado.setR16(Short.parseShort(respuesta));
                break;
            case "17.0":
                resultado.setR17(Short.parseShort(respuesta));
                break;
            case "18.0":
                resultado.setR18(Short.parseShort(respuesta));
                break;
            case "19.0":
                resultado.setR19(Short.parseShort(respuesta));
                break;
            case "20.0":
                resultado.setR20(Short.parseShort(respuesta));
                break;
            case "21.0":
                resultado.setR21(Short.parseShort(respuesta));
                break;
            case "22.0":
                resultado.setR22(Short.parseShort(respuesta));
                break;
            case "23.0":
                resultado.setR23(Short.parseShort(respuesta));
                break;
            case "24.0":
                resultado.setR24(Short.parseShort(respuesta));
                break;
            case "25.0":
                resultado.setR25(Short.parseShort(respuesta));
                break;
            case "26.0":
                resultado.setR26(Short.parseShort(respuesta));
                break;
            case "27.0":
                resultado.setR27(Short.parseShort(respuesta));
                break;
            case "28.0":
                resultado.setR28(Short.parseShort(respuesta));
                break;
            case "29.0":
                resultado.setR29(Short.parseShort(respuesta));
                break;
            case "30.0":
                resultado.setR30(Short.parseShort(respuesta));
                break;
            case "31.0":
                resultado.setR31(Short.parseShort(respuesta));
                break;
            case "32.0":
                resultado.setR32(respuesta);
                break;

        }
    }

    @Override
    public String obtenerRespuestaPorPregunta(EvaluacionDocentesMateriaResultados resultado, Float pregunta) {
        String res = null;
        switch (pregunta.toString()) {
            case "1.0":
                res = resultado.getR1() != null ? resultado.getR1().toString() : null;
                break;
            case "2.0":
                res = resultado.getR2() != null ? resultado.getR2().toString() : null;
                break;
            case "3.0":
                res = resultado.getR3() != null ? resultado.getR3().toString() : null;
                break;
            case "4.0":
                res = resultado.getR4() != null ? resultado.getR4().toString() : null;
                break;
            case "5.0":
                res = resultado.getR5() != null ? resultado.getR5().toString() : null;
                break;
            case "6.0":
                res = resultado.getR6() != null ? resultado.getR6().toString() : null;
                break;
            case "7.0":
                res = resultado.getR7() != null ? resultado.getR7().toString() : null;
                break;
            case "8.0":
                res = resultado.getR8() != null ? resultado.getR8().toString() : null;
                break;
            case "9.0":
                res = resultado.getR9() != null ? resultado.getR9().toString() : null;
                break;
            case "10.0":
                res = resultado.getR10() != null ? resultado.getR10().toString() : null;
                break;
            case "11.0":
                res = resultado.getR11() != null ? resultado.getR11().toString() : null;
                break;
            case "12.0":
                res = resultado.getR12() != null ? resultado.getR12().toString() : null;
                break;
            case "13.0":
                res = resultado.getR13() != null ? resultado.getR13().toString() : null;
                break;
            case "14.0":
                res = resultado.getR14() != null ? resultado.getR14().toString() : null;
                break;
            case "15.0":
                res = resultado.getR15() != null ? resultado.getR15().toString() : null;
                break;
            case "16.0":
                res = resultado.getR16() != null ? resultado.getR16().toString() : null;
                break;
            case "17.0":
                res = resultado.getR17() != null ? resultado.getR17().toString() : null;
                break;
            case "18.0":
                res = resultado.getR18() != null ? resultado.getR18().toString() : null;
                break;
            case "19.0":
                res = resultado.getR19() != null ? resultado.getR19().toString() : null;
                break;
            case "20.0":
                res = resultado.getR20() != null ? resultado.getR20().toString() : null;
                break;
            case "21.0":
                res = resultado.getR21() != null ? resultado.getR21().toString() : null;
                break;
            case "22.0":
                res = resultado.getR22() != null ? resultado.getR22().toString() : null;
                break;
            case "23.0":
                res = resultado.getR23() != null ? resultado.getR23().toString() : null;
                break;
            case "24.0":
                res = resultado.getR24() != null ? resultado.getR24().toString() : null;
                break;
            case "25.0":
                res = resultado.getR25() != null ? resultado.getR25().toString() : null;
                break;
            case "26.0":
                res = resultado.getR26() != null ? resultado.getR26().toString() : null;
                break;
            case "27.0":
                res = resultado.getR27() != null ? resultado.getR27().toString() : null;
                break;
            case "28.0":
                res = resultado.getR28() != null ? resultado.getR28().toString() : null;
                break;
            case "29.0":
                res = resultado.getR29() != null ? resultado.getR29().toString() : null;
                break;
            case "30.0":
                res = resultado.getR30() != null ? resultado.getR30().toString() : null;
                break;
            case "31.0":
                res = resultado.getR31() != null ? resultado.getR31().toString() : null;
                break;
            case "32.0":
                res = resultado.getR32() != null ? resultado.getR32() : null;
                break;
        }
        return res;
    }

    @Override
    public void cargarResultadosAlmacenados(Evaluaciones evaluacion, VistaEvaluacionDocenteMateriaPye datosEvaluador, List<VistaEvaluacionDocenteMateriaPye> docentesEvaluados) {
        try {
//            System.out.println("entra al try");
            List<String> claves = new SerializableArrayList<>();
            List<String> materias = new SerializableArrayList<>();
            docentesEvaluados.forEach((lr) -> {
                claves.add(lr.getNumeroNomina());
                materias.add(lr.getCveMateria());
            });
            TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.evaluacionDocentesMateriaResultadosPK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultadosPK.evaluador=:evaluador and e.evaluacionDocentesMateriaResultadosPK.cveMateria in :cveMateria ", EvaluacionDocentesMateriaResultados.class);
            q.setParameter("evaluacion", evaluacion.getEvaluacion());
            q.setParameter("cveMateria", materias);
            q.setParameter("evaluador", Integer.parseInt(datosEvaluador.getMatricula()));
//            System.out.println("datos de la consulta : ------> : "+q +" datos que contiene la consulta "+ q.getResultList());
            List<EvaluacionDocentesMateriaResultados> l = q.getResultList();

            evaluacion.setEvaluacionDocentesMateriaResultadosList(new SerializableArrayList<>());
            docentesEvaluados.forEach((docente) -> {
                EvaluacionDocentesMateriaResultadosPK pk = new EvaluacionDocentesMateriaResultadosPK(evaluacion.getEvaluacion(), Integer.parseInt(datosEvaluador.getMatricula()), docente.getCveMateria(), Integer.parseInt(docente.getNumeroNomina()));
                EvaluacionDocentesMateriaResultados der = new EvaluacionDocentesMateriaResultados(pk);

                if (l.contains(der)) {
                    der = l.get(l.indexOf(der));
                } else {
                    f.setEntityClass(der.getClass());
                    f.create(der);
                    f.getEntityManager().flush();
                    f.getEntityManager().refresh(der);
                }
                evaluacion.getEvaluacionDocentesMateriaResultadosList().add(der);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioEvaluacionDocenteMateria.cargarResultadosAlmacenados() no se cargaron los resultados");
        }
    }

    @Override
    public List<EvaluacionDocentesMateriaResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluaciones, VistaEvaluacionDocenteMateriaPye docentes) {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_resultados_evaluacion_docentes_materia", EvaluacionDocentesMateriaResultados.class).
                registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", evaluaciones.getEvaluacion()).
                registerStoredProcedureParameter("par_evaluador", Integer.class, ParameterMode.IN).setParameter("par_evaluador", Integer.parseInt(docentes.getMatricula()));

        List<EvaluacionDocentesMateriaResultados> salida;
        salida = q.getResultList();
//                System.out.println("lista de evaluados : " + salida);
//                for(int i = salida.size()-1; i>=0; i--){
//                    System.out.println("evaluado : ---> : " + salida.get(i));
//                }
        return salida;
    }

    @Override
    public void comprobarResultado(EvaluacionDocentesMateriaResultados resultado) {
        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (31);
        for (Integer i = 1; i <= 31; i++) {
            if (obtenerRespuestaPorPregunta(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta(resultado, (float) i));
            }
        }
        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);
    }

    @Override
    public VistaEvaluacionDocenteMateriaPye getcveMateria(String matricula, String materia) {
        TypedQuery<VistaEvaluacionDocenteMateriaPye> q = f2.getEntityManager().createQuery("SELECT v FROM VistaEvaluacionDocenteMateriaPye v WHERE v.matricula = :matricula and v.cveMateria = :cveMateria", VistaEvaluacionDocenteMateriaPye.class);
        q.setParameter("matricula", matricula);
        q.setParameter("cveMateria", materia);
        List<VistaEvaluacionDocenteMateriaPye> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public VistaEvaluacionDocenteMateriaPye getDatosDocente(String matricula, String materia, Integer periodo) {
//        System.out.println("impresion de los parametros : matricula : " + matricula + " materia : " + materia + " periodo : " + periodo);
        TypedQuery<VistaEvaluacionDocenteMateriaPye> q = f2.getEntityManager().createQuery("select v from VistaEvaluacionDocenteMateriaPye v where v.matricula = :matricula and v.cveMateria = :cveMateria and v.periodo = :periodo ORDER BY v.periodo DESC", VistaEvaluacionDocenteMateriaPye.class);
        q.setParameter("matricula", matricula);
        q.setParameter("cveMateria", materia);
        q.setParameter("periodo", periodo);
//        System.out.println("resultado de la busqueda : " + q.getSingleResult());
        List<VistaEvaluacionDocenteMateriaPye> l = q.getResultList();
        if (l.isEmpty() || l == null) {
            return null;
        } else {
            return l.get(0);
        }
    }

    @Override
    public List<EvaluacionDocentesMateriaResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluaciones, Integer matricula) {
        StoredProcedureQuery q = f.getEntityManager().createStoredProcedureQuery("obtener_lista_resultados_evaluacion_docentes_materia", EvaluacionDocentesMateriaResultados.class).
                registerStoredProcedureParameter("par_evaluacion", Integer.class, ParameterMode.IN).setParameter("par_evaluacion", evaluaciones.getEvaluacion()).
                registerStoredProcedureParameter("par_evaluador", Integer.class, ParameterMode.IN).setParameter("par_evaluador", matricula);

        List<EvaluacionDocentesMateriaResultados> salida;
        salida = q.getResultList();
        if (salida.isEmpty() || salida == null) {
            return null;
        } else {
            return salida;
        }
    }

    @Override
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> getListResultadosDocenteMateriabyMatricula(Evaluaciones evaluacion, int matricula) {
        try{

            List<EvaluacionDocentesMateriaResultados> listResultados = new ArrayList<>();
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,listResultados,"La evaluación no debe ser nula.");}
            if(matricula == 0){return ResultadoEJB.crearErroneo(3,listResultados,"La matricula no debe ser nula");}
            //TODO: Busca si existen resultados de la evaluación por la matrcicula del estudiante
            listResultados = f.getEntityManager().createQuery("select e from EvaluacionDocentesMateriaResultados e where e.evaluaciones.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultadosPK.evaluador=:evaluador", EvaluacionDocentesMateriaResultados.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("evaluador",matricula)
                    .getResultList()
                ;
            if (listResultados.isEmpty() || listResultados ==null){return ResultadoEJB.crearErroneo(4,listResultados,"No se han econtrado registro del estudiante");}
            else {return ResultadoEJB.crearCorrecto(listResultados,"Se han encontrado registro");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de resultados por estudiante (EJBEvaluacionDocenteMteria, getListaReusltadosDocenteMateriabyMatricula)", e, null);
        }
    }

    @Override
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> getListResultadosDocenteMateriaCompletosbyMatricula(Evaluaciones evaluacion, int matricula) {
        try{
            //TODO: Obtiene la los resultados completos por evaluacion y por la matricula
            List<EvaluacionDocentesMateriaResultados> listaResultados= f.getEntityManager().createQuery("select e from EvaluacionDocentesMateriaResultados e where e.evaluacionDocentesMateriaResultadosPK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultadosPK.evaluador=:matricula and e.completo=true",EvaluacionDocentesMateriaResultados.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("matricula",matricula)
                    .getResultList()
                    ;
            if(listaResultados.isEmpty() || listaResultados==null){return ResultadoEJB.crearErroneo(2,listaResultados,"No se encontraron resultados");}
            else{ return ResultadoEJB.crearCorrecto(listaResultados,"Lista de resultados encontrada");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de resultados por estudiante (EJBEvaluacionDocenteMteria, getListaReusltadosDocenteMateriaCompletobyMatricula)", e, null);
        }
    }
}
