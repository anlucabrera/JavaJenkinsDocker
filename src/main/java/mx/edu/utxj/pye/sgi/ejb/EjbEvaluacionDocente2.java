package mx.edu.utxj.pye.sgi.ejb;

import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import edu.mx.utxj.pye.seut.util.preguntas.Abierta;
import edu.mx.utxj.pye.seut.util.preguntas.Opciones;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.*;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
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

@Stateless
public class EjbEvaluacionDocente2 {

    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em;
    private EntityManager em2;
    @EJB EJBAdimEstudianteBase estudianteBase;

    @PostConstruct
    public void init(){em = f.getEntityManager();em2 = f2.getEntityManager();}
    /**
     * Obtiene la evaluacion a docente activa
     * @return Resultado del proceso (Evaluacion activa)
     */

    public ResultadoEJB<Evaluaciones> getEvDocenteActiva() {
        try{
            Evaluaciones evaluacion = new Evaluaciones();

            // Ahorita solo es de prueba, pero se debe cambiar a que busque por rango de fecha, para que tome la evaluacion actual
            evaluacion = em.createQuery("SELECT e FROM Evaluaciones e WHERE e.tipo=:tipo AND :fecha BETWEEN e.fechaInicio AND e.fechaFin ORDER BY e.evaluacion desc", Evaluaciones.class)
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
     * Apartados
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
     * Busca los resultados de la evaluacion a docente por evaluador, evaluado y evaluacion
     * @param evaluador Estudiante evaluador
     * @param evaluado Docente - materia evaluado
     * @param evaluacion Evaluacion activa
     * @return
     */

    public ResultadoEJB<EvaluacionDocentesMateriaResultados2> getResultadobyEvaluadorEvaluado(dtoEstudiantesEvalauciones evaluador, dtoEstudianteMateria evaluado, Evaluaciones evaluacion){
        try {
            EvaluacionDocentesMateriaResultados2 resultados = new EvaluacionDocentesMateriaResultados2();
            if(evaluador ==null){return  ResultadoEJB.crearErroneo(2,resultados,"El evaluador no debe ser nulo");}
            if(evaluado ==null){return ResultadoEJB.crearErroneo(3,resultados,"El evaluador no debe ser nulo");}
            if(evaluacion==null){return ResultadoEJB.crearErroneo(4,resultados,"La evaluación no debe ser nula");}
            resultados = em.createQuery("select e from EvaluacionDocentesMateriaResultados2 e where e.evaluacionDocentesMateriaResultados2PK.evaluador=:evaluador and e.evaluacionDocentesMateriaResultados2PK.evaluado=:evaluado and e.evaluacionDocentesMateriaResultados2PK.cveMateria=:cveMateria and e.evaluacionDocentesMateriaResultados2PK.evaluacion=:evaluacion",EvaluacionDocentesMateriaResultados2.class)
                    .setParameter("evaluador",Integer.parseInt(evaluador.getMatricula()))
                    .setParameter("evaluado",evaluado.getDocenteImparte().getClave())
                    .setParameter("cveMateria",evaluado.getClaveMateria())
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
            ;
            if(resultados ==null){
                //No existen resultados, entonces los crea
                EvaluacionDocentesMateriaResultados2PK pk = new EvaluacionDocentesMateriaResultados2PK();
                pk.setEvaluacion(evaluacion.getEvaluacion());
                pk.setEvaluador(Integer.parseInt(evaluador.getMatricula()));
                pk.setEvaluado(evaluado.getDocenteImparte().getClave());
                pk.setCveMateria(evaluado.getClaveMateria());
                EvaluacionDocentesMateriaResultados2 resultados1 = new EvaluacionDocentesMateriaResultados2();
                resultados1.setEvaluacionDocentesMateriaResultados2PK(pk);
                em.persist(resultados1);
                resultados = resultados1;
                return ResultadoEJB.crearCorrecto(resultados,"Se crearon los resultados");
            }else {
                //Existen resultados ...
                return ResultadoEJB.crearCorrecto(resultados,"Resultados");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener los resultados (EJBEvaluacionDocenteMteria.getResultadobyEvaluadorEvaluado)", e, null);
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
            List<dtoEstudianteMateria> materias = new ArrayList<>();
            if(estudiante==null){return  ResultadoEJB.crearErroneo(2,materias, "El estudiante no debe ser nulo");}
            if(evaluacion ==null){return  ResultadoEJB.crearErroneo(3,materias,"La evaluacion no debe ser nula");}
            //Comprueba si es estudiante registrado en Control Escolar o en Saiiut
            if(estudiante.getEstudianteCE()!=null){
                //El estudiante esta registrado en Control escolar
                //Se obtienen las materias del estudiante
                ResultadoEJB<List<dtoEstudianteMateria>> materiasCE = getMateriasbyEstudianteCE(estudiante,evaluacion);
                if(materiasCE.getCorrecto()==true){
                    materias = materiasCE.getValor();
                    return ResultadoEJB.crearCorrecto(materias,"Lista de materias");
                }else {return ResultadoEJB.crearErroneo(5,materias,"Ocurrio un error al obetener la lista de materias del estudiante");}
            }
            else if(estudiante.getEstudianteSaiiut()!=null){
                //El estudiante esta registrado en Saiiut
                //Se obtienen la lista de materias desde Saiiut
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
                    .getResultList()
            ;
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
                    .getResultList()
            ;
            if(cargaAcademica==null || cargaAcademica.isEmpty()){return ResultadoEJB.crearErroneo(4,listMaterias,"No se econtro carga academica"); }
            else {
                //Se reoorre la lista de carga acedemica para llenar las materias del estudiante
                cargaAcademica.stream().forEach(c->{
                    dtoEstudianteMateria materia = new dtoEstudianteMateria();
                    //Se busca al docente que imparte la materia
                    ResultadoEJB<Personal> docente = estudianteBase.getPersonalbyClave(c.getDocente());
                    if(docente.getCorrecto()==true){materia.setDocenteImparte(docente.getValor());}
                    materia.setClaveMateria(c.getIdPlanMateria().getClaveMateria());
                    materia.setNombreMateria(c.getIdPlanMateria().getIdMateria().getNombre());
                    listMaterias.add(materia);
                });
                return ResultadoEJB.crearCorrecto(listMaterias,"Lista de materias del estudiante");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de materias por estudiante CE (EJBEvaluacionDocenteMteria.getMateriasbyEstudianteCE)", e, null);
        }
    }

    /**
     * Actualiza respuesta por pregunta
     * @param resultados Resultados
     * @param pregunta Numero de pregunta
     * @param valor Valor ingresado
     * @return
     */
    public ResultadoEJB<EvaluacionDocentesMateriaResultados2> actualizaRespuestaPorPregunta2(EvaluacionDocentesMateriaResultados2 resultados, String pregunta, String valor){
        try{
            switch (pregunta.toString()) {
                case "r1":
                    resultados.setR1(Short.parseShort(valor));
                    break;
                case "r2":
                    resultados.setR2(Short.parseShort(valor));
                    break;
                case "r3":
                    resultados.setR3(Short.parseShort(valor));
                    break;
                case "r4":
                    resultados.setR4(Short.parseShort(valor));
                    break;
                case "r5":
                    resultados.setR5(Short.parseShort(valor));
                    break;
                case "r6":
                    resultados.setR6(Short.parseShort(valor));
                    break;
                case "r7":
                    resultados.setR7(Short.parseShort(valor));
                    break;
                case "r8":
                    resultados.setR8(Short.parseShort(valor));
                    break;
                case "r9":
                    resultados.setR9(Short.parseShort(valor));
                    break;
                case "r10":
                    resultados.setR10(Short.parseShort(valor));
                    break;
                case "r11":
                    resultados.setR11(Short.parseShort(valor));
                    break;
                case "r12":
                    resultados.setR12(Short.parseShort(valor));
                    break;
                case "r13":
                    resultados.setR13(Short.parseShort(valor));
                    break;
                case "r14":
                    resultados.setR14(Short.parseShort(valor));
                    break;
                case "r15":
                    resultados.setR15(Short.parseShort(valor));
                    break;
                case "r16":
                    resultados.setR16(Short.parseShort(valor));
                    break;
                case "r17":
                    resultados.setR17(Short.parseShort(valor));
                    break;
            }
            return ResultadoEJB.crearCorrecto(resultados,"Resultados actualizados");

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar los resultados (EJBEvaluacionDocenteMteria.actualizaRespuestaPorPregunta2)", e, null);
        }
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



}
