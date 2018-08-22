/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEvaluacionDocentesResultados;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAdministracionEncuestas {
    /**
     * obtiene el periodo actual 
     * @return 
     */
    public PeriodosEscolares getPeriodoActual();
    /**
     * verifica que el usuario que acceso a el sistema sea o no sea tutor, esto se determina si
     * el usuario esta asignado a algun grupo
     * @param maestro <- se obtiene la clave del usuario
     * @param periodo <- se obtiene el periodo actual 
     * @return 
     */
    public List<Grupos> esTutor(Integer maestro, Integer periodo);   
    /**
     * Obtiene la lista de alumnos por tutor(docente)
     * @param grupo<- dictamina a de que grupo son los alumnos del focente
     * @return 
     */
    public List<Alumnos> getAlumnosPeriodoActual();
    
    public List<Alumnos> getListaDeAlumnosPorDocente (Integer grupo);
    /**
     * busca a los alumnos mediante la matricula
     * @param matricula
     * @return 
     */   
    public List<VistaAlumnosPye>findAllByMatricula(String matricula);
    /**
     * obtiene toda la lista de estudiantes que no esten finados
     * @return 
     */
    public List<VistaAlumnosPye> getEstudiantesGeneral();
    /**
     * verifica que el usuario logueado sea director de carrera
     * @param areaSup
     * @param actividad
     * @param catOp
     * @param clave
     * @return 
     */
    public List<Personal> esDirectorDeCarrera(Short areaSup, Integer actividad, Integer catOp, Integer clave);
    /**
     * obtiene las areas que tiene a cargo el director de carrera
     * @param identificador 
     * @param estatus
     * @return 
     */
    public List<AreasUniversidad> obtenerAreasDirector(Short identificador, String estatus);
    
    /**
     * 
     * @param areaSeleccionada
     * @return 
     */
    public List<AreasUniversidad> obtenerAreasPorDirectorReporteSA(Short areaSeleccionada);
    /**
     * 
     * @param siglas
     * @param evaluacion
     * @return 
     */
    
    public List<ListaEvaluacionDocentesResultados> resultadosEvaluacionGlobalDirector(String siglas, Integer evaluacion);
    /**
     * obtiene la evaluacion segun sea el periodo seleccionado
     * @param periodo
     * @return 
     */
    public Evaluaciones360 getEvaluacion360Administracion(Integer periodo);
    /**
     * obtiene evaluacion segun sea el periodo seleccionado
     * @param periodo
     * @return 
     */
    public DesempenioEvaluaciones getEvaluacionDesempenioAdministracion(Integer periodo); 
    /**
     * Encuentra los resultados de la evaluación de una persona(se implementa par buscar los resultados de un subordinado)
     * @param evaluacion
     * @param evaluado
     * @return 
     */
    public List<Evaluaciones360Resultados> getEvaluaciones360ResultadosSubordinados(Evaluaciones360 evaluacion, Integer evaluado);
    /**
     * Encuentra los resultados de la evaluacion de una persona (se implementa para buscar los resultados de un subordinado)
     * @param evaluacion
     * @param evaluador
     * @return 
     */
    public List<DesempenioEvaluacionResultados> getEvaluacionesDesempenioSubordinados(Integer evaluacion, Integer evaluador);
    /**
     * Ontiene una evaluacion segun el periodo y el tipo
     * @param periodo
     * @param tipo
     * @return 
     */
    public Evaluaciones getEvaluaciones(Integer periodo, String tipo);
    /**
     * Entcuentra los resultados de la evaluacion al tutor segun sea la clave 
     * de trabajador del tutor seleccionado y la clave de la evaluacion.
     * @param evaluacion
     * @param evaluado
     * @return 
     */
    public List<EvaluacionesTutoresResultados> getEvaluacionesTutoresResultados(Evaluaciones evaluacion, Integer evaluado);
    /**
     * obtiene una evaluacion segun sea el periodo que se elija.
     * @param periodo
     * @return 
     */
    public EvaluacionDocentesMaterias getEvaluacionDoncete(Integer periodo);
    /**
     * obtiene los resultados de los docentes evaluados segun sea la evaluación seleccionada
     * @param evaluacion
     * @param evaluado
     * @return 
     */
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesResultadosPromedioGeneral(EvaluacionDocentesMaterias evaluacion, Integer evaluado);
    /**
     * Regresa el listado de las materias que imparte un subordinado si es docente
     * @param subordinado la clave del trabajador
     * @return si no es docente regresa null
     */
    public List<VistaEvaluacionDocenteMateriaPye> getMateriasPorDocente(Integer subordinado,Integer periodo);
    /**
     * Retorna la lista de evaluaciones del subordinado por materia
     * @param evaluacion
     * @param evaluado
     * @param materia
     * @return en caso de no ser encontrado regresa null
     */
    public List<EvaluacionDocentesMateriaResultados> getEvaluacionDocentesResultadosPromedioMateria(EvaluacionDocentesMaterias evaluacion, Integer evaluado, String materia);

    /**
     * obtiene la lista del personal docente que se encuentre adscrito a el area seleccionada
     * @param area <- area seleccionada en la intrefaz grafica y por la cual se busca al personal docente
     * @return 
     */
    public List<ListaPersonal> getListadoDocentesPorArea(Short area);
    
    /**
     * obtiene todos los resultados de la evaluación seleccionada
     * @param evaluacion 
     * @return 
     */
    public List<EvaluacionDocentesMateriaResultados> getGlobalEvaluacionDocentes(EvaluacionDocentesMaterias evaluacion);
    /**
     * obtiene la lista del personal que tiene experiencia docente
     * @return 
     */
    public List<ListaPersonal> personalGeneral ();
    /**
     * Obtiene la lista de personal completa
     * @return 
     */
    public List<ListaPersonal> personalGeneralCompleta ();
    /**
     * crea una nueva evaluacion de 360°
     * @param evaluacion
     * @return 
     */
    public Evaluaciones360 nuevaEvaluacion360( Evaluaciones360 evaluacion);
    /**{
     * Crea una nueva evaluación de desempeño
     * @param evaluacion
     * @return 
     */
    public DesempenioEvaluaciones nuevaEvaluacionDesempenio(DesempenioEvaluaciones evaluacion);
    /**
     * obtiene la lista general de evaluaciones de 360°
     * @return 
     */
    public List<Evaluaciones360> getListaEvaluaciones360();
    /**
     * obntiene los resultados de una evaluacion a 360° correspondiente a los parametros
     * evaluacion  y perdiodo
     * @param evaluacion
     * @param periodo
     * @return 
     */
    public List<Evaluaciones360Resultados> getListaResultadosEvaluacion360(Integer evaluacion, Integer periodo);
    /**
     * ontiene
     * @return 
     */
    public List<DesempenioEvaluaciones> getListaEvaluacionesDesempenio();
    
    public List<DesempenioEvaluacionResultados> getListaResultadosEvaluacionDesempenio(Integer evaluacion, Integer periodo);
    
    
    public List<Alumno> getEstudiantesSEScolaes();
    
    public List<Alumno> getEstudiantePorMatricula(Integer matricula);
    
    public AreasUniversidad getProgramaPorClave(Short clave);
}
