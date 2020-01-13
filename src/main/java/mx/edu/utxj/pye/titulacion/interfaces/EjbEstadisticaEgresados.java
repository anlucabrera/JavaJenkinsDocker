/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.interfaces;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.titulacion.dto.dtoEstudianteParaReporte;
import mx.edu.utxj.pye.titulacion.dto.dtoEstadisticaEgresados;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEstadisticaEgresados {
    
    /**
     * Obtiene la lista de generaciones con registros en expedientes de titulación
     * @return Lista de entity Generaciones
     */
    public List<Generaciones> getGeneracionesConRegistro();
    
    /**
     * Obtiene la lista de niveles con registros en expedientes de titulación
     * @param generacion
     * @return Lista de String de Niveles
     */
    public List<String> getNivelesConRegistro(Generaciones generacion);
    
     /**
     * Obtiene reporte de integración de expedientes de titulación
     * @param generacion
     * @param nivel
     * @return Lista de dtoEstadisticaEgresados por programa educativo
     */
    public List<dtoEstadisticaEgresados> obtenerReporteIntegracionExp(Generaciones generacion, String nivel);
    
     /**
     * Obtiene número de estudiantes inscritos (reportado en registro de SII de matricula periodos escolares)
     * @param periodo
     * @param cuatrimestre
     * @param programa
     * @return Lista de MatriculaPeriodosEscolares 
     */
    public List<MatriculaPeriodosEscolares> obtenerEstudiantesInscritos(Integer periodo, String cuatrimestre, Short programa);
    
     /**
     * Obtiene número de estudiantes egresados
     * @param listaMatriculas
     * @param procesosGeneraciones
     * @return Lista de entity Alumnos por programa educativo
     */
    public List<Alumnos> obtenerEgresados(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones procesosGeneraciones);
    
     /**
     * Obtiene número de estudiantes egresados no titulados (acreditaron estadía)
     * @param listaMatriculas
     * @param procesosGeneraciones
     * @return Lista de entity Alumnos por programa educativo
     */
    public List<Alumnos> obtenerEgresadosAcreditaronEstadia(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones procesosGeneraciones);
    
     /**
     * Obtiene número de estudiantes con expediente integrado
     * @param listaMatriculas
     * @param proceso
     * @param programa
     * @return Lista de entity ExpedientesTitulacion por programa educativo
     */
    public List<ExpedientesTitulacion> obtenerExpedientes(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones proceso, String programa);

     /**
     * Obtiene número de estudiantes con expediente validado
     * @param listaMatriculas
     * @param proceso
     * @param programa
     * @return Lista de entity ExpedientesTitulacion por programa educativo
     */
    public List<ExpedientesTitulacion> obtenerExpedientesValidados(List<MatriculaPeriodosEscolares> listaMatriculas, ProcesosGeneraciones proceso, String programa);
    
     /**
     * Obtiene listado de alumnos por generación y nivel seleccionado con situación: académica, integración de expediente y validación de expediente.
     * @param generacion
     * @return Lista de dtoEstadisticaEgresados
     */
    public List<dtoEstudianteParaReporte> obtenerListadoGeneral(Generaciones generacion);
   
     /**
     * Convertir clave de status del estudiante en SAIIUT a descripcion
     * @param claveStatus
     * @return String con la descripción del status académico
     */
    public String convertirStatusSAIIUT(Integer claveStatus);
    
     /**
     * Obtiene descripción de la situación del expediente de titulación: no ha integrado, sin validar o validado.
     * @param matricula
     * @param programa
     * @return String con la descripción del status de expediente de titulación
     */
    public String expedienteTitulacion(String matricula, String programa);
    
     /**
     * Obtiene listado de estudiantes inscritos en el periodo y cuatrimestre correspondiente (reportado en registro de SII de matricula periodos escolares).
     * @param periodo
     * @param cuatrimestre
     * @return Lista de entity MatriculaPeriodosEscolares general
     */
    public List<MatriculaPeriodosEscolares> obtenerEstudiantesInscritosGeneral(Integer periodo, String cuatrimestre);
    
     /**
    * Genera concentrado de número total de expedientes por generación, nivel educativo y programa educativo.
    * @param generacion
     * @param nivel
    * @return Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteGeneracionNivel(Generaciones generacion, String nivel) throws Throwable;
    
      /**
    * Genera listado de estudiantes por generación, nivel educativo y programa educativo.
    * @param generacion
    * @return Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getListadoGeneracionNivel(Generaciones generacion) throws Throwable;
}
