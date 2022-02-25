package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import mx.edu.utxj.pye.sgi.dto.*;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.funcional.Comparador;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionDocente;
import mx.edu.utxj.pye.sgi.funcional.ComparadorEvaluacionDocente2;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class EjbEvaluacionDocente2 {

    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em;
    private EntityManager em2;
    @EJB EJBAdimEstudianteBase estudianteBase;
    @EJB EjbPropiedades ep;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }
    /**
     * Obtiene la evaluacion a docente activa
     * @return Resultado del proceso (Evaluacion activa)
     */

    public ResultadoEJB<Evaluaciones> getEvDocenteActiva() {
        try{
            Evaluaciones evaluacion = new Evaluaciones();
            evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE :fecha BETWEEN e.fechaInicio AND e.fechaFin AND (e.tipo=:tipo OR e.tipo=:tipo2 OR e.tipo=:tipo3 OR e.tipo=:tipo4) ORDER BY e.evaluacion desc", Evaluaciones.class)
                    .setParameter("tipo2", EvaluacionesTipo.DOCENTE_2.getLabel())
                    .setParameter("tipo3", EvaluacionesTipo.DOCENTE_3.getLabel())
                    .setParameter("tipo4",EvaluacionesTipo.DOCENTE_4.getLabel())
                    .setParameter("tipo", "Docente materia")
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,evaluacion,"La evaluacion no se encontro");}
            else {return  ResultadoEJB.crearCorrecto(evaluacion,"Evaluacion encontrada");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EJBEvaluacionDocenteMteria, getEvDocenteActiva)", e, null);
        }
    }

    public ResultadoEJB<AperturaVisualizacionEncuestas> getApertura (Evaluaciones evaluacion){
        try{
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,new AperturaVisualizacionEncuestas(),"La evaluación no debe ser nula");}
            AperturaVisualizacionEncuestas apertura= new AperturaVisualizacionEncuestas();
            apertura = f.getEntityManager().createQuery("SELECT a FROM AperturaVisualizacionEncuestas a WHERE current_timestamp between a.fechaInicial and a.fechaFinal AND a.encuesta=:tipo ORDER BY a.apertura desc", AperturaVisualizacionEncuestas.class)
                    .setParameter("tipo", evaluacion.getTipo())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(apertura==null){return  ResultadoEJB.crearErroneo(3,new AperturaVisualizacionEncuestas(),"No hay apertura ");}
            else {return ResultadoEJB.crearCorrecto(apertura,"");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la evaluacion activa(EJBEvaluacionDocenteMteria, resAper)", e, null);

        }
    }
    /**
     * Obtiene la ultima evaluacion activa
     * @return
     */
    public ResultadoEJB<Evaluaciones> getUltimaEvDocenteActiva() {
        try{
            //Obtiene la ultima evalaucion de docente materia activa
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

    /**
     * Busca el periodo escolar segun la evaluacion
     * @param evaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodosEscolares> getPeriodoEvaluacion(Evaluaciones evaluacion) {
        try{
            //Busca el periodo segun el periodo de la evaluacion
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

    /**
     * Define el tipo de evaluación a docente que se va realizar
     * Modificar en confirguaracion propiedades en prontuario (tipoEvaluacionDocente) el tipo de evaluación que se va a realizar
     * return 1 = Evaluacion a docente
     * return 2 = Evaluación a docente (aulas virtuales)- Contingencia de salud
     *
     * @return
     */
    public ResultadoEJB<Integer> getTipoEvaluacion(){
        try{
            Integer tipoEvaluacion ;
            tipoEvaluacion=ep.leerPropiedadEntera("tipoEvaluacionDocente").orElse(0);
            return ResultadoEJB.crearCorrecto(tipoEvaluacion,"Tipo de evaluación");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Error al obtener el tipo de evaluación docente. (EJBEvaluacionDocenteMteria.getTipoEvaluacion)", e, null);
        }
    }

    /**
     * Posibles respuestas
     * @return
     */
    public List<SelectItem> getRespuestasPosibles() {
        List<SelectItem> l = new SerializableArrayList<>();
        l.add(new SelectItem("5", "Muy satisfecho", "Muy satisfecho"));
        l.add(new SelectItem("4", "Satisfecho", "Satisfecho"));
        l.add(new SelectItem("3", "Regularmente satisfecho", "Regularmente satisfecho"));
        l.add(new SelectItem("2", "Poco satisfecho", "Poco satisfecho"));
        l.add(new SelectItem("1", "No satisfecho", "No satisfecho"));
        return l;
    }

    /**
     * Apartados para evaluación a docente por materia
     * En la tabla de evaluaciones está como tipo "Docente materia"
     * Evaluación tipo 1
     * @return
     */
    public List<Apartado> getApartados() {
        List<Apartado> l = new SerializableArrayList<>();

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
        Apartado a6 = new Apartado(7F, "PROYECCION SOCIAL", new SerializableArrayList<>());
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 16.0f, "¿Involucra a los estudiantes para conservar la disciplina, orden y limpieza en la Institución?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 17.0f, "¿Promueve el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        l.add(a5);
        l.add(a6);
        return l;
    }
    /**
     * Apartados para evaluación docente modificada  por la contigencia de salud y el uso de aulas virtuales
     * Evaluación tipo 2
     * En la tabla evalauciones esta como tipo Docentes (Cuestionario 2 por contingencia)
     * @return
     */
    public List<Apartado> getApartadosContingencia() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
        Apartado a2 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2.0f, 2.0f, "¿Explica al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
        Apartado a3 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 3.0f, "¿Explica con claridad los conceptos de cada tema?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 4.0f, "¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 8.0f, "¿Se dirige con un lenguaje que propio y respetuoso?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
        Apartado a4 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta? ", ""));
        Apartado a5 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 13.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 14.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 15.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclarar de dudas?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 16.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
        Apartado a6 = new Apartado(6F, "PROYECCION SOCIAL", new SerializableArrayList<>());
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 6.0f, 17.0f, "Antes de la contingencia ¿Te involucró para conservar la disciplina, orden y limpieza en la Institución?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 6.0f, 18.0f, "Antes de la contingencia ¿Promovió el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        l.add(a5);
        l.add(a6);
        return l;

    }
    /**
     * Apartados para evaluación docente modificada  por la contigencia de salud y el uso de aulas virtuales
     * Evaluación tipo 2
     * En la tabla evalauciones esta como tipo Docentes (Cuestionario 3 por contingencia)
     * @return
     */
    public List<Apartado> getApartadosContingenciaCuestionario2() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "ASISTENCIA", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1.0f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
        Apartado a2 = new Apartado(2F, "PROGRAMA", new SerializableArrayList<>());
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2.0f, 2.0f, "¿Explicó al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
        Apartado a3 = new Apartado(3F, "METODOLOGÍA", new SerializableArrayList<>());
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 3.0f, "¿Explicó con claridad los conceptos y contenidos de cada tema?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 4.0f, " ¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 8.0f, "¿Se dirige con un lenguaje propio y respetuoso?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3.0f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
        Apartado a4 = new Apartado(4F, "EVALUACIÓN", new SerializableArrayList<>());
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4.0f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta?", ""));
        Apartado a5 = new Apartado(5F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 13.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 14.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 15.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclaración de dudas?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 16.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
        a5.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 5.0f, 17.0f, "En general, el desempeño del docente fue.", ""));
        Apartado a6 = new Apartado(6F, "PROYECCION SOCIAL", new SerializableArrayList<>());
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 6.0f, 18.0f, "Antes de la contingencia ¿Te involucró para conservar la disciplina, orden y limpieza en la Institución?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 6.0f, 19.0f, "Antes de la contingencia ¿Promovió el cuidado de los recursos naturales dentro y fuera de la Institución?", ""));
        a6.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 6.0f, 20.0f, "Comentarios hacie el docente. (Mínimo 11 caracteres)", ""));
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        l.add(a5);
        l.add(a6);
        return l;

    }
    /**
     * Apartados para evaluación docente modificada  por la contigencia de salud y el uso de aulas virtuales
     * Evaluación tipo 2
     * En la tabla evalauciones esta como tipo Docentes (Cuestionario  4 por contingencia)
     * @return
     */
    public List<Apartado> getApartadosContingenciaCuestionario3() {
        List<Apartado> l = new SerializableArrayList<>();
        Apartado a1 = new Apartado(1F, "METODOLOGÍA", new SerializableArrayList<>());
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 1.0f, "¿Te brindó asesorías para fortalecer los contenidos que se te dificultan de la materia?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 2.0f, "¿Explicó al inicio del cuatrimestre la estructura de la materia, como: los objetivos, contenidos, metodología, evaluación y bibliografía?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 3.0f, "¿Explicó con claridad los conceptos y contenidos de cada tema?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 4.0f, " ¿Las instrucciones definidas en el curso en Aula Virtual fueron claras?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 5.0f, "¿Te motivó para que participaras objetiva y activamente en el desarrollo de tus actividades?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 6.0f, "¿Generó un clima de confianza en el que se pueden plantear distintas dudas?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 7.0f, "¿Consigue en ti, motivación e interés en la materia?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 8.0f, "¿Se dirige con un lenguaje propio y respetuoso?", ""));
        a1.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 1f, 9.0f, "¿Te brindó alternativas de entrega de trabajos, en caso de presentar algún inconveniente con el Aula Virtual?", ""));
        Apartado a2 = new Apartado(2F, "EVALUACIÓN", new SerializableArrayList<>());
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2f, 10.0f, "¿Acepta revisar la calificación en caso de posibles errores de evaluación?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2f, 11.0f, "¿El docente te dio acompañamiento y retroalimentación de las actividades que generó en el aula virtual?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2f, 12.0f, "¿Evaluó el conocimiento del saber y saber hacer a través de un examen, ejercicio práctico u otra herramienta?", ""));
        a2.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 2f, 13.0f, "¿El docente te entregó y aclaró las evaluaciones antes de registrarlas en el sistema?", ""));
        Apartado a3 = new Apartado(3F, "RECURSOS EDUCACIONALES", new SerializableArrayList<>());
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3f, 14.0f, "Durante el desarrollo de las actividades en el Aula Virtual ¿El docente te proporcionó algún recurso extra o puentes de comunicación para resolver dudas?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3f, 15.0f, "Durante la ejecución de actividades en Aula Virtual ¿Resolvió tus dudas a tiempo para entregar tus actividades?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3f, 16.0f, "¿Cumplió los horarios que se presentan en el Aula Virtual para encuentros virtuales y aclaración de dudas?", ""));
        a3.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 3f, 17.0f, "¿Te motivó al uso de aplicaciones tecnológicas o diversos recursos de consulta para reforzar tus conocimientos o comprender mejor los temas?", ""));
        Apartado a4 = new Apartado(4F, "PROYECCION SOCIAL", new SerializableArrayList<>());
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4f, 18.0f, "¿El docente asiste puntualmente a las sesiones programadas?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4f, 19.0f, "Durante la ejecución de actividades en Aula Virtual ¿Evitó en todo momento improvisar, tener tiempo muerto y/o desorganización en las sesiones?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4f, 20.0f, "¿El docente evita prácticas de favoritismo, corrupción y/o alguna otra práctica antiética?", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4f, 21.0f, "En general, el desempeño del docente fue.", ""));
        a4.getPreguntas().add(new Opciones(TipoCuestionario.EDOCENTE, 4f, 22.0f, "Comentarios hacie el docente. (Mínimo 11 caracteres)", ""));
        l.add(a1);
        l.add(a2);
        l.add(a3);
        l.add(a4);
        return l;

    }
    
    public ResultadoEJB<EvaluacionDocentesMateriaResultados2> obtenerResultadoEvaluacionTipo1(dtoEstudiantesEvalauciones evaluador, dtoEstudianteMateria evaluado, Evaluaciones evaluacion, Map<String,String> respuestas){
        try {
            EvaluacionDocentesMateriaResultados2PK tdaPK = new EvaluacionDocentesMateriaResultados2PK(
                    evaluacion.getEvaluacion(), 
                    Integer.parseInt(evaluador.getMatricula()), 
                    evaluado.getClaveMateria(), 
                    evaluado.getDocenteImparte().getClave());
            f.setEntityClass(EvaluacionDocentesMateriaResultados2.class);
            
            EvaluacionDocentesMateriaResultados2 r = em.find(EvaluacionDocentesMateriaResultados2.class, tdaPK);
            if (r == null) {
                r = new EvaluacionDocentesMateriaResultados2(tdaPK);
                em.persist(r);
            }
            if (r.getR1() != null) {
                respuestas.put("r1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("r2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("r3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("r4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("r5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("r6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("r7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("r8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("r9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("r10", r.getR10().toString());
            }
            if (r.getR11() != null) {
                respuestas.put("r11", r.getR11().toString());
            }
            if (r.getR12() != null) {
                respuestas.put("r12", r.getR12().toString());
            }
            if (r.getR13() != null) {
                respuestas.put("r13", r.getR13().toString());
            }
            if (r.getR14() != null) {
                respuestas.put("r14", r.getR14().toString());
            }
            if (r.getR15() != null) {
                respuestas.put("r15", r.getR15().toString());
            }
            if (r.getR16() != null) {
                respuestas.put("r16", r.getR16().toString());
            }
            if (r.getR17() != null) {
                respuestas.put("r17", r.getR17().toString());
            }
            return ResultadoEJB.crearCorrecto(r, "Agregado correctamente");
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    public ResultadoEJB<EvaluacionDocentesMateriaResultados3> obtenerResultadoEvaluacionTipo2(dtoEstudiantesEvalauciones evaluador, dtoEstudianteMateria evaluado, Evaluaciones evaluacion, Map<String,String> respuestas){
        try {
            EvaluacionDocentesMateriaResultados3PK tdaPK = new EvaluacionDocentesMateriaResultados3PK(
                    evaluacion.getEvaluacion(), 
                    Integer.parseInt(evaluador.getMatricula()), 
                    evaluado.getClaveMateria(), 
                    evaluado.getDocenteImparte().getClave());
            f.setEntityClass(EvaluacionDocentesMateriaResultados3.class);
            
            EvaluacionDocentesMateriaResultados3 r = em.find(EvaluacionDocentesMateriaResultados3.class, tdaPK);
            if (r == null) {
                r = new EvaluacionDocentesMateriaResultados3(tdaPK);
                em.persist(r);
            }
            if (r.getR1() != null) {
                respuestas.put("r1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("r2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("r3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("r4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("r5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("r6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("r7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("r8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("r9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("r10", r.getR10().toString());
            }
            if (r.getR11() != null) {
                respuestas.put("r11", r.getR11().toString());
            }
            if (r.getR12() != null) {
                respuestas.put("r12", r.getR12().toString());
            }
            if (r.getR13() != null) {
                respuestas.put("r13", r.getR13().toString());
            }
            if (r.getR14() != null) {
                respuestas.put("r14", r.getR14().toString());
            }
            if (r.getR15() != null) {
                respuestas.put("r15", r.getR15().toString());
            }
            if (r.getR16() != null) {
                respuestas.put("r16", r.getR16().toString());
            }
            if (r.getR17() != null) {
                respuestas.put("r17", r.getR17().toString());
            }
            if (r.getR18() != null) {
                respuestas.put("r18", r.getR18().toString());
            }
            return ResultadoEJB.crearCorrecto(r, "Agregado correctamente");
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    public ResultadoEJB<EvaluacionDocentesMateriaResultados4> obtenerResultadoEvaluacionTipo3(dtoEstudiantesEvalauciones evaluador, dtoEstudianteMateria evaluado, Evaluaciones evaluacion, Map<String,String> respuestas){
        try {
            EvaluacionDocentesMateriaResultados4PK tdaPK = new EvaluacionDocentesMateriaResultados4PK(
                    evaluacion.getEvaluacion(), 
                    Integer.parseInt(evaluador.getMatricula()), 
                    evaluado.getClaveMateria(), 
                    evaluado.getDocenteImparte().getClave());
            f.setEntityClass(EvaluacionDocentesMateriaResultados4.class);
            
            EvaluacionDocentesMateriaResultados4 r = em.find(EvaluacionDocentesMateriaResultados4.class, tdaPK);
            if (r == null) {
                r = new EvaluacionDocentesMateriaResultados4(tdaPK);
                em.persist(r);
            }
            if (r.getR1() != null) {
                respuestas.put("r1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("r2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("r3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("r4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("r5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("r6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("r7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("r8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("r9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("r10", r.getR10().toString());
            }
            if (r.getR11() != null) {
                respuestas.put("r11", r.getR11().toString());
            }
            if (r.getR12() != null) {
                respuestas.put("r12", r.getR12().toString());
            }
            if (r.getR13() != null) {
                respuestas.put("r13", r.getR13().toString());
            }
            if (r.getR14() != null) {
                respuestas.put("r14", r.getR14().toString());
            }
            if (r.getR15() != null) {
                respuestas.put("r15", r.getR15().toString());
            }
            if (r.getR16() != null) {
                respuestas.put("r16", r.getR16().toString());
            }
            if (r.getR17() != null) {
                respuestas.put("r17", r.getR17().toString());
            }
            if (r.getR18() != null) {
                respuestas.put("r18", r.getR18().toString());
            }
            if (r.getR19() != null) {
                respuestas.put("r19", r.getR19().toString());
            }
            if (r.getR20() != null) {
                respuestas.put("r20", r.getR20());
            }
            return ResultadoEJB.crearCorrecto(r, "Agregado correctamente");
        } catch (NullPointerException e) {
            return null;
        }
    }
    
    public ResultadoEJB<EvaluacionDocentesMateriaResultados5> obtenerResultadoEvaluacionTipo4(dtoEstudiantesEvalauciones evaluador, dtoEstudianteMateria evaluado, Evaluaciones evaluacion, Map<String,String> respuestas){
        try {
            EvaluacionDocentesMateriaResultados5PK tdaPK = new EvaluacionDocentesMateriaResultados5PK(
                    evaluacion.getEvaluacion(), 
                    Integer.parseInt(evaluador.getMatricula()), 
                    evaluado.getClaveMateria(), 
                    evaluado.getDocenteImparte().getClave());
            f.setEntityClass(EvaluacionDocentesMateriaResultados5.class);
            
            EvaluacionDocentesMateriaResultados5 r = em.find(EvaluacionDocentesMateriaResultados5.class, tdaPK);
            if (r == null) {
                r = new EvaluacionDocentesMateriaResultados5(tdaPK);
                em.persist(r);
            }
            if (r.getR1() != null) {
                respuestas.put("r1", r.getR1().toString());
            }
            if (r.getR2() != null) {
                respuestas.put("r2", r.getR2().toString());
            }
            if (r.getR3() != null) {
                respuestas.put("r3", r.getR3().toString());
            }
            if (r.getR4() != null) {
                respuestas.put("r4", r.getR4().toString());
            }
            if (r.getR5() != null) {
                respuestas.put("r5", r.getR5().toString());
            }
            if (r.getR6() != null) {
                respuestas.put("r6", r.getR6().toString());
            }
            if (r.getR7() != null) {
                respuestas.put("r7", r.getR7().toString());
            }
            if (r.getR8() != null) {
                respuestas.put("r8", r.getR8().toString());
            }
            if (r.getR9() != null) {
                respuestas.put("r9", r.getR9().toString());
            }
            if (r.getR10() != null) {
                respuestas.put("r10", r.getR10().toString());
            }
            if (r.getR11() != null) {
                respuestas.put("r11", r.getR11().toString());
            }
            if (r.getR12() != null) {
                respuestas.put("r12", r.getR12().toString());
            }
            if (r.getR13() != null) {
                respuestas.put("r13", r.getR13().toString());
            }
            if (r.getR14() != null) {
                respuestas.put("r14", r.getR14().toString());
            }
            if (r.getR15() != null) {
                respuestas.put("r15", r.getR15().toString());
            }
            if (r.getR16() != null) {
                respuestas.put("r16", r.getR16().toString());
            }
            if (r.getR17() != null) {
                respuestas.put("r17", r.getR17().toString());
            }
            if (r.getR18() != null) {
                respuestas.put("r18", r.getR18().toString());
            }
            if (r.getR19() != null) {
                respuestas.put("r19", r.getR19().toString());
            }
            if (r.getR20() != null) {
                respuestas.put("r20", r.getR20().toString());
            }
            if (r.getR21() != null) {
                respuestas.put("r21", r.getR21().toString());
            }
            if (r.getR22() != null) {
                respuestas.put("r22", r.getR22());
            }
            return ResultadoEJB.crearCorrecto(r, "Agregado correctamente");
        } catch (NullPointerException e) {
            return null;
        }
    }
        

    /**
     * Obtiene los resultados por evaluacion y evaluador
     * @param evaluacion Evaluacion activa tipo 2 (doesempaño docente por contingencia)
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso (Lista de resultados por evaluador)
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados3>> getListaResultadosMateriabyMatricula(Evaluaciones evaluacion, int matricula){
        try{
            List<EvaluacionDocentesMateriaResultados3> resultados = new ArrayList<>();
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,resultados,"La evaluación no debe ser nula");}
            if(matricula ==0){return ResultadoEJB.crearErroneo(3,resultados,"La matricula no debe ser nula");}
            //Busca los resultados por la evaluacion y la matricula
            resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados3 e where e.evaluacionDocentesMateriaResultados3PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados3PK.evaluador =:evaluador",EvaluacionDocentesMateriaResultados3.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("evaluador",matricula)
                    .getResultList()
            ;
            if(resultados.isEmpty() || resultados ==null){
                return ResultadoEJB.crearErroneo(4,resultados,"No se encontraron resultados");
            }
            else {
                return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados (EJBEvaluacionDocente2.getListaResultadosMateriabyMatricula)", e, null);

        }
    }
    /**
     * Obtiene los resultados por evaluacion y evaluador
     * @param evaluacion Evaluacion activa tipo 3 (doesempaño docente por contingencia cuestionario 3)
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso (Lista de resultados por evaluador)
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados4>> getListaResultados4MateriabyMatricula(Evaluaciones evaluacion, int matricula){
        try{
            List<EvaluacionDocentesMateriaResultados4> resultados = new ArrayList<>();
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,resultados,"La evaluación no debe ser nula");}
            if(matricula ==0){return ResultadoEJB.crearErroneo(3,resultados,"La matricula no debe ser nula");}
            //Busca los resultados por la evaluacion y la matricula
            resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados4 e where e.evaluacionDocentesMateriaResultados4PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados4PK.evaluador =:evaluador",EvaluacionDocentesMateriaResultados4.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("evaluador",matricula)
                    .getResultList()
            ;
            if(resultados.isEmpty() || resultados ==null){
                return ResultadoEJB.crearErroneo(4,resultados,"No se encontraron resultados");
            }
            else {
                return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados (EJBEvaluacionDocente2.getListaResultadosMateriabyMatricula)", e, null);

        }
    }
    /**
     * Obtiene los resultados por evaluacion y evaluador
     * @param evaluacion Evaluacion activa tipo 4 (doesempaño docente por contingencia cuestionario 4)
     * @param matricula Matricula del estudiante
     * @return Resultado del proceso (Lista de resultados por evaluador)
     */
    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados5>> getListaResultados5MateriabyMatricula(Evaluaciones evaluacion, int matricula){
        try{
            List<EvaluacionDocentesMateriaResultados5> resultados = new ArrayList<>();
            if(evaluacion==null){return ResultadoEJB.crearErroneo(2,resultados,"La evaluación no debe ser nula");}
            if(matricula ==0){return ResultadoEJB.crearErroneo(3,resultados,"La matricula no debe ser nula");}
            //Busca los resultados por la evaluacion y la matricula
            resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados5 e where e.evaluacionDocentesMateriaResultados5PK.evaluacion=:evaluacion and e.evaluacionDocentesMateriaResultados5PK.evaluador =:evaluador",EvaluacionDocentesMateriaResultados5.class)
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .setParameter("evaluador",matricula)
                    .getResultList()
            ;
            if(resultados.isEmpty() || resultados ==null){
                return ResultadoEJB.crearErroneo(4,resultados,"No se encontraron resultados");
            }
            else {
                return ResultadoEJB.crearCorrecto(resultados,"Resultados encontrados");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados (EJBEvaluacionDocente2.getListaResultadosMateriabyMatricula)", e, null);

        }
    }


    /**
     * Identifica en que base de datos esta registrado el estudiante, y respecto a eso obtiene una lista de materias
     * @param estudiante dtoEstudiante
     * @param evaluacion Evaluacion actuva
     * @return Resultado del proceso (Lista de materias)
     */
    public ResultadoEJB<List<dtoEstudianteMateria>> getMateriasbyEstudiante (dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion){
        try{
            //System.out.println("Estudiante" + estudiante.getMatricula() +" Periodo" + evaluacion.getPeriodo());
            List<dtoEstudianteMateria> materias = new ArrayList<>();
            if(estudiante==null){return  ResultadoEJB.crearErroneo(2,materias, "El estudiante no debe ser nulo");}
            if(evaluacion ==null){return  ResultadoEJB.crearErroneo(3,materias,"La evaluacion no debe ser nula");}
            //Comprueba si es estudiante registrado en Control Escolar o en Saiiut
            if(estudiante.getEstudianteCE()!=null){
                //El estudiante esta registrado en Control escolar
                //Se obtienen las materias del estudiante
                //System.out.println("Esta en control escolar");
                ResultadoEJB<List<dtoEstudianteMateria>> materiasCE = getMateriasbyEstudianteCE(estudiante,evaluacion);
                if(materiasCE.getCorrecto()==true){
                    materias = materiasCE.getValor();
                    return ResultadoEJB.crearCorrecto(materias,"Lista de materias");
                }else {return ResultadoEJB.crearErroneo(5,materias,"Ocurrio un error al obetener la lista de materias del estudiante");}
            }
            else if(estudiante.getEstudianteSaiiut()!=null){
                //El estudiante esta registrado en Saiiut
                //Se obtienen la lista de materias desde Saiiut
                //System.out.println("Esta en saiiut");
                ResultadoEJB<List<dtoEstudianteMateria>>materiasSaiiut= getMateriasbyEstudianteSaiiut(estudiante,evaluacion);
                if(materiasSaiiut.getCorrecto()==true){
                    materias = materiasSaiiut.getValor();
                    return ResultadoEJB.crearCorrecto(materias,"Lista de materias");

                }else {return ResultadoEJB.crearErroneo(6,materias,"Error al obtener el listado de materias del estudiante");}
            }else {
                return ResultadoEJB.crearErroneo(4, materias,"No esta registrado en ninguna base");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por estudiante  (EJBEvaluacionDocenteMteria.getMateriasbyEstudiante)", e, null);
        }
    }

    /**
     * Obtiene la lista de materias del estudiante por periodo de la evaluacion, este sólo se usa cuando el estudiante esta registrados en sauiit
     * @param estudiante
     * @return
     */
    public  ResultadoEJB<List<dtoEstudianteMateria>> getMateriasbyEstudianteSaiiut(dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion){
        try{
            List<dtoEstudianteMateria> listMaterias = new ArrayList<>();
            List<VistaEvaluacionDocenteMateriaPye> materiasSaiiut = new ArrayList<>();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,listMaterias,"El estudiante no debe ser nulo");}
            if(evaluacion==null){return  ResultadoEJB.crearErroneo(3,listMaterias, "La evaluacion no debe ser nula");}
            //Se buscan las materias por estudiante en la vista
            materiasSaiiut = em2.createQuery("select v from VistaEvaluacionDocenteMateriaPye v where v.matricula=:matricula and v.periodo=:periodo", VistaEvaluacionDocenteMateriaPye.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("periodo",evaluacion.getPeriodo())
                    .getResultList();
            if(materiasSaiiut==null || materiasSaiiut.isEmpty()){return ResultadoEJB.crearErroneo(4,listMaterias,"No se encontraron materias del estudiante");}
            else {
                // Se reccorre la lista para porder generar la lista de materias
                materiasSaiiut.stream().forEach(m->{
                    dtoEstudianteMateria materia = new dtoEstudianteMateria();
                    //Se obtiene al docente que imparte la materia
                    ResultadoEJB<Personal> docente = estudianteBase.getPersonalbyClave(Integer.parseInt(m.getNumeroNomina()));
                    if(docente.getCorrecto()==true){ materia.setDocenteImparte(docente.getValor()); }
                    //Se llena el dto de la materia
                    materia.setClaveMateria(m.getCveMateria());
                    materia.setNombreMateria(m.getNombreMateria());
                    listMaterias.add(materia);
                });
                return ResultadoEJB.crearCorrecto(listMaterias,"Lista de materias");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por estudiante (EJBEvaluacionDocenteMteria, getMateriasbyEstudiante)", e, null);
        }
    }
    
    /**
     * Obtiene la lista de materias por estudiante registrado en Control Escolar
     * @param estudiante dto Estudiante
     * @param evaluacion Evaluacion activa
     * @return
     */
    public ResultadoEJB<List<dtoEstudianteMateria>> getMateriasbyEstudianteCE(dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion){
        try{
            List<dtoEstudianteMateria> listMaterias = new ArrayList<>();
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,listMaterias,"El estudiante no debe ser nulo");}
            if(evaluacion ==null){return ResultadoEJB.crearErroneo(3,listMaterias,"La evaluacion no debe ser nula");}
            //Consulta la carga academica segun el grupo que esta adscrito el estudiante y el periodo de la evaluacion
            List<CargaAcademica> cargaAcademica = new ArrayList<>();
            cargaAcademica = em.createQuery("select c from CargaAcademica c where c.cveGrupo.idGrupo=:idGrupo and c.cveGrupo.periodo=:periodo",CargaAcademica.class)
                    .setParameter("idGrupo",estudiante.getEstudianteCE().getGrupo().getIdGrupo())
                    .setParameter("periodo",evaluacion.getPeriodo())
                    .getResultList();
            if(cargaAcademica==null || cargaAcademica.isEmpty()){return ResultadoEJB.crearErroneo(4,listMaterias,"No se econtro carga academica"); }
            else {
                //Se recorre la lista de carga acedemica para llenar las materias del estudiante
                listMaterias = cargaAcademica
                        .stream()
                        .map(carga -> pack(carga))
                        .distinct()
                        .filter(ResultadoEJB::getCorrecto)
                        .map(ResultadoEJB::getValor)
                        .collect(Collectors.toList());
                return ResultadoEJB.crearCorrecto(listMaterias,"Lista de materias del estudiante");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por estudiante CE (EJBEvaluacionDocenteMteria.getMateriasbyEstudianteCE)", e, null);
        }
    }
    
    public ResultadoEJB<dtoEstudianteMateria> pack(CargaAcademica carga){
        dtoEstudianteMateria materia = new dtoEstudianteMateria();
        if(carga.equals(new CargaAcademica())) return ResultadoEJB.crearErroneo(1, "La carga no puede ser nulo", dtoEstudianteMateria.class);
        ResultadoEJB<Personal> docente = estudianteBase.getPersonalbyClave(carga.getDocente());
        if(!docente.getCorrecto()) return ResultadoEJB.crearErroneo(2, "No se encontro al docente", dtoEstudianteMateria.class);
        materia.setDocenteImparte(docente.getValor());
        materia.setClaveMateria(carga.getIdPlanMateria().getClaveMateria());
        materia.setNombreMateria(carga.getIdPlanMateria().getIdMateria().getNombre());
        return ResultadoEJB.crearCorrecto(materia, "Empaquetado");
    }

    public void actualizarRespuestaPregunta2(EvaluacionDocentesMateriaResultados2 resultado, String pregunta, String respuesta, Map<String,String> respuestas){
        switch (pregunta.trim()) {
            case "r1": 
                resultado.setR1(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r2": 
                resultado.setR2(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r3": 
                resultado.setR3(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r4": 
                resultado.setR4(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r5": 
                resultado.setR5(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r6": 
                resultado.setR6(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r7": 
                resultado.setR7(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r8": 
                resultado.setR8(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r9": 
                resultado.setR9(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r10": 
                resultado.setR10(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r11": 
                resultado.setR11(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r12": 
                resultado.setR12(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r13": 
                resultado.setR13(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r14": 
                resultado.setR14(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r15": 
                resultado.setR15(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r16": 
                resultado.setR16(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r17": 
                resultado.setR17(respuesta == null ? null : Short.parseShort(respuesta));
                break;         
        }
        respuestas.put(pregunta, respuesta);
    }
    
    public void actualizarRespuestaPregunta3(EvaluacionDocentesMateriaResultados3 resultado, String pregunta, String respuesta, Map<String,String> respuestas){
        switch (pregunta.trim()) {
            case "r1": 
                resultado.setR1(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r2": 
                resultado.setR2(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r3": 
                resultado.setR3(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r4": 
                resultado.setR4(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r5": 
                resultado.setR5(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r6": 
                resultado.setR6(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r7": 
                resultado.setR7(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r8": 
                resultado.setR8(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r9": 
                resultado.setR9(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r10": 
                resultado.setR10(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r11": 
                resultado.setR11(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r12": 
                resultado.setR12(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r13": 
                resultado.setR13(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r14": 
                resultado.setR14(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r15": 
                resultado.setR15(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r16": 
                resultado.setR16(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r17": 
                resultado.setR17(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r18": 
                resultado.setR18(respuesta == null ? null : Short.parseShort(respuesta));
                break;           
        }
        respuestas.put(pregunta, respuesta);
    }
    
    public void actualizarRespuestaPregunta4(EvaluacionDocentesMateriaResultados4 resultado, String pregunta, String respuesta, Map<String,String> respuestas){
        switch (pregunta.trim()) {
            case "r1": 
                resultado.setR1(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r2": 
                resultado.setR2(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r3": 
                resultado.setR3(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r4": 
                resultado.setR4(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r5": 
                resultado.setR5(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r6": 
                resultado.setR6(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r7": 
                resultado.setR7(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r8": 
                resultado.setR8(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r9": 
                resultado.setR9(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r10": 
                resultado.setR10(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r11": 
                resultado.setR11(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r12": 
                resultado.setR12(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r13": 
                resultado.setR13(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r14": 
                resultado.setR14(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r15": 
                resultado.setR15(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r16": 
                resultado.setR16(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r17": 
                resultado.setR17(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r18": 
                resultado.setR18(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r19": 
                resultado.setR19(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r20": 
                resultado.setR20(respuesta == null ? null : respuesta);
                break;           
        }
        respuestas.put(pregunta, respuesta);
    }
    
    public void actualizarRespuestaPregunta5(EvaluacionDocentesMateriaResultados5 resultado, String pregunta, String respuesta, Map<String,String> respuestas){
        switch (pregunta.trim()) {
            case "r1": 
                resultado.setR1(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r2": 
                resultado.setR2(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r3": 
                resultado.setR3(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r4": 
                resultado.setR4(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r5": 
                resultado.setR5(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r6": 
                resultado.setR6(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r7": 
                resultado.setR7(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r8": 
                resultado.setR8(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r9": 
                resultado.setR9(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r10": 
                resultado.setR10(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r11": 
                resultado.setR11(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r12": 
                resultado.setR12(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r13": 
                resultado.setR13(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r14": 
                resultado.setR14(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r15": 
                resultado.setR15(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r16": 
                resultado.setR16(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r17": 
                resultado.setR17(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r18": 
                resultado.setR18(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r19": 
                resultado.setR19(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r20": 
                resultado.setR20(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r21": 
                resultado.setR21(respuesta == null ? null : Short.parseShort(respuesta));
                break;
            case "r22": 
                resultado.setR22(respuesta == null ? null : respuesta);
                break;            
        }
        respuestas.put(pregunta, respuesta);
    }
    
    public String obtenerRespuestaPorPregunta(EvaluacionDocentesMateriaResultados2 resultado, Float pregunta) {
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
        }
        return res;
    }

    /**
     * Obtiene respuesta por pregunta de la evaluacion docente tipo 2
     * @param resultado
     * @param pregunta
     * @return
     */
    public String obtenerRespuestaPorPregunta2(EvaluacionDocentesMateriaResultados3 resultado, Float pregunta) {
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
        }
        return res;
    }
    /**
     * Obtiene respuesta por pregunta de la evaluacion docente (Cuestionario 3 por contingencia)
     * @param resultado
     * @param pregunta
     * @return
     */
    public String obtenerRespuestaPorPregunta3(EvaluacionDocentesMateriaResultados4 resultado, Float pregunta) {
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
                res = resultado.getR20() != null ? resultado.getR20() : null;
                break;

        }
        return res;
    }
    /**
     * Obtiene respuesta por pregunta de la evaluacion docente (Cuestionario 4 por contingencia)
     * @param resultado
     * @param pregunta
     * @return
     */
    public String obtenerRespuestaPorPregunta4(EvaluacionDocentesMateriaResultados5 resultado, Float pregunta) {
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
                res = resultado.getR22() != null ? resultado.getR22() : null;
                break;

        }
        return res;
    }

    /**
     * Comprueba si los ha teminado de evaluar al docente
     * @param resultado
     */
    public void comprobarResultado(EvaluacionDocentesMateriaResultados2 resultado) {
        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (17);
        for (Integer i = 1; i <= 17; i++) {
            if (obtenerRespuestaPorPregunta(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta(resultado, (float) i));
            }
        }
        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);
        em.merge(resultado);
    }
    /**
     * Comprueba si los ha teminado de evaluar al docente
     * Evaluación docente tipo 2
     * @param resultado
     */
    public void comprobarResultado2(EvaluacionDocentesMateriaResultados3 resultado) {
        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (17);
        for (Integer i = 1; i <= 17; i++) {
            if (obtenerRespuestaPorPregunta2(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta2(resultado, (float) i));
            }
        }
        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);
        em.merge(resultado);
    }
    /**
     * Comprueba si los ha teminado de evaluar al docente
     * Evaluación docente (Cuestionario 3 por contingencia)
     * @param resultado
     */
    public void comprobarResultado3(EvaluacionDocentesMateriaResultados4 resultado) {
        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (17);
        Comparador<EvaluacionDocentesMateriaResultados4> comparador = new ComparadorEvaluacionDocente();
        Boolean finalizado = comparador.isCompleto(resultado);
        completo =finalizado;
        for (Integer i = 1; i <= 17; i++) {
            if (obtenerRespuestaPorPregunta3(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta3(resultado, (float) i));
            }
        }
        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);
        em.merge(resultado);
    }
    /**
     * Comprueba si los ha teminado de evaluar al docente
     * Evaluación docente (Cuestionario 4 por contingencia)
     * @param resultado
     */
    public void comprobarResultado4(EvaluacionDocentesMateriaResultados5 resultado) {
        boolean completo = true;
        Double suma = 0D;
        Double cantidad = (double) (17);
        Comparador<EvaluacionDocentesMateriaResultados5> comparador = new ComparadorEvaluacionDocente2();
        Boolean finalizado = comparador.isCompleto(resultado);
        completo =finalizado;
        for (Integer i = 1; i <= 17; i++) {
            if (obtenerRespuestaPorPregunta4(resultado, (float) i) == null) {
                completo = false;
            } else {
                suma += Double.parseDouble(obtenerRespuestaPorPregunta4(resultado, (float) i));
            }
        }
        resultado.setCompleto(completo);
        resultado.setIncompleto(!completo);
        resultado.setPromedio(suma / cantidad);
        em.merge(resultado);
    }



}
