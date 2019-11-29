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
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;

/**
 *
 * @author Taatisz :)
 */
@Local
public interface EJBAdimEstudianteBase {
    /**
     * Devuelve un dto de estudiante segun su matricula y su periodo
     * El dto contiene todoo lo necesario para la evaluaciones
     * @param matricula
     * @param periodo periodo de la evaluacion
     * @return
     */
    public ResultadoEJB<dtoEstudiantesEvalauciones>  getClaveEstudiante (String matricula, Integer periodo);
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
     * 1.Obtiene un listado de todos los alumnos activos del periodo actual en Control escolar por periodo
     * 2.Lleno la informacion en un dtoEstudiantes Evaluciones
     * 3.Busco los registros de los estudiantes por matricula y el periodo de la evalaucion activa en la base de pye2(matriculas periodos escolares) y se agregan al dto
     * 4. Busco las claves de los estudiantes por el numero de registros en la tabla EstudianteClave y se le agrego al dto
     * @param periodo periodo en el que que se solicitan los datos
     * @return resultado del proceso/ lista del dto de estudiantes de sauitt
     *
     */

    public ResultadoEJB<List<dtoEstudiantesEvalauciones>> getEstudiantesCE (PeriodosEscolares periodo);

    
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

    public ResultadoEJB<Personal> getPersonalbyClave(int clave);


}
