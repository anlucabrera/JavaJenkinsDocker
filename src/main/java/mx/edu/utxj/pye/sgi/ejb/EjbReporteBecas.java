package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoReporteBecas;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.EstudiantesPye;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EjbReporteBecas {

    /**
     * Obtiene una lista de periodos de la tabla de becas
     * @return Resultado del proceso  (Lista de periodos escolares)
     */

    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosBecas();

    /**
     * Obtiene lista de registros de las becas asignadas por periodo
     * @param periodo periodo a consultar
     * @return Resultado del proceso (Lista de registros de becas)
     */


    public ResultadoEJB<List<BecasPeriodosEscolares>> getBecasbyPeriodo(PeriodosEscolares periodo);

    /**
     * Obtiene el tipo de beca por la clave de la beca
     * @param clave_tipoBeca
     * @return Resultado del proceso (Tipo de Beca)
     */
    public ResultadoEJB<BecaTipos> getTipoBecabyClave(Integer clave_tipoBeca);

    /**
     * Obtiene el estudiantes que se encuentran en sauiit por su matricula y su periodo
     * @param periodo Periodo seleccionado
     * @param matricula
     * @return Resultado del proceso (Lista de estudiantes en sauiit)
     */
    public ResultadoEJB<VistaAlumnosPye> getEstudiantesSauiitbyPeriodo(String matricula);

    /**
     * Obtiene el estudiantes registrados en CE por el perido seleccionado
     * @param periodo Periodo Seleccionado
     * @param  matricula
     * @return Resultado del proceso (Estudiantes registraados en CE)
     */
    public ResultadoEJB<EstudiantesPye> getEstudiantesCEbyPeriodo(String matricula);

    /**
     * Obtiene el area de la universidad por las siglas del area
     * @param siglas siglas del area
     * @return Resultado del proceso (AreaUniversidad)
     */

    public ResultadoEJB<AreasUniversidad> getAreabySiglas(String siglas);

    /**
     * Obtiene el area de la universidad por clave
     * @param clave_area clave de la universidad
     * @return Resultado del Proceso (Area de la univesidad)
     */

    public ResultadoEJB<AreasUniversidad> getAreabyClave(Integer clave_area);

    /**
     * Obtiene al encargado del area por area
     * @param area area a buscar
     * @return Resultado del proceso - Personal encargado del area
     */

    public ResultadoEJB<Personal> getDirectorArea(AreasUniversidad area);

    /**
     * Obtiene el reporte de las becas asiganadas seguin el periodo seleccionado
     * @param periodo periodo seleccionado
     * @return
     */
/*
    public ResultadoEJB<dtoReporteBecas> getEstudiante(PeriodosEscolares periodo, String matricula);



*/
}
