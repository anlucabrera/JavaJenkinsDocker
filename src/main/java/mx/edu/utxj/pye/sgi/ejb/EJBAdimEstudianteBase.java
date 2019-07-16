/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativos;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;

/**
 *
 * @author Taatisz :)
 */
@Local
public interface EJBAdimEstudianteBase {
    /*Busca al estudiante por matricula en la base de pye2
    /@return obtejo de tipo Matricula Periodos Escolares
    */
    public MatriculaPeriodosEscolares getEstudianteSauiiut (String matricula, Integer periodo);
    // Busca al estudiante por matricula en la base de Control escolar
    public Estudiante getEstudianteControlEscolar (String matricula);
    // Devuelve la clave del estudiante segun sea un estudiante registrado en SAUIIT o en Control Escolar
    public EstudiantesClaves getClaveEstudiante (String matricula, Integer periodo);
   
    /**
    * Buca a un estudiante por su clave  en la tabla de Matricula Periodos Escolares (Que son los que se encuentran registrados en sauitt por periodo)
    * @param estudiante Clave del estudiante 
    * @param periodo periodo 
    * @return Resultado del proceso
    */
    public ResultadoEJB<MatriculaPeriodosEscolares> getEstudianteSauittClave(EstudiantesClaves estudiante, PeriodosEscolares periodo);
    
    /**
     * Busca a un estudiante po la clave del estudiante en la base de Control Escolar
     * @param estudiante estudiante a buscar
     * @return  Resultado del proceso de la busqueda
    */
    public ResultadoEJB<Estudiante> getEstudianteCEClave(EstudiantesClaves estudiante);

    /**
     * Obtiene al estudiante por matricula de la vista de alumnos encuestas (que son los activos en Sauiit)
     * @param matricula
     * @return
     */
    public ResultadoEJB<dtoEstudiantesEvalauciones> getEstudianteActivobyMatriculaSauiit(String matricula);


    /**
     * 1.Obtiene un listado de todos los alumnos activos del periodo actual en sauiit(Vista Alumnos Encuestas) 
     * 2.Lleno la informacion en un dtoEstudiantes Evaluciones
     * 3.Busco los registros de los estudiantes por matricula y el periodo de la evalaucion activa en la base de pye2(matriculas periodos escolares) y se agregan al dto
     * 4. Busco las claves de los estudiantes por el numero de registros en la tabla EstudianteClave y se le agrego al dto
     * @param periodo periodo en el que que se solicitan los datos
     * @return resultado del proceso/ lista del dto de estudiantes de sauitt
     * 
     */
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiit(PeriodosEscolares periodo);
    
    
    /**
     * 1. Obtiene una lista de estudiantes buscada por matricula en la base de CE, los datos que se tengan los agrega el dto
     * 2. Obtiene una lista de claves de estudiante buscada por id de estudiante y la agrega al dto
     * Falta agregar la busqueda del tutor, director 
     * @return  resultado de proceso/ lista tipo dto de estudiantes de ce
     */
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesCE(PeriodosEscolares periodo);
    
    /**
     * 2.Obtiene la lista general de estudiantes tanto de sauiit y CE
     * @param periodoEvaluacion periodo de la evaluacion a tutor Activa
     * @return Resultado del proceso / listado tipo dto con todos los estudiantes
     */
    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesSauiityCE( PeriodosEscolares periodoEvaluacion);
    
    //Este metodo es adicional para convetir la clave del tutor que obtiene la Vista de Alumnunos Encuestas a personal
    public ResultadoEJB<Personal> getClaveNominabyClavePersona( int clavePersona);

    /**
     * Obtiene la lista de programas educativos activis
     * @return  Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativos>> getPEActivos ();


}
