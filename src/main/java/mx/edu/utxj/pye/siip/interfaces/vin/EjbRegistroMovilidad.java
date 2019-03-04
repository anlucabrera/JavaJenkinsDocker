/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasMovilidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTORegistroMovilidad;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadDocente;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOMovilidadEstudiante;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbRegistroMovilidad {
    
   public List<DTORegistroMovilidad> getListaRegistroMovilidad(String rutaArchivo) throws Throwable;
   
   
   public void guardaRegistroMovilidad(List<DTORegistroMovilidad> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
   
   /*
   * Valida que la clave del registro exista en la tabla, antes de insertar un registro en Movilidad Docente y Estudiantil
   */
   public Integer getRegistroMovilidadEspecifico(String registroMovilidad); 
   
    /*
   * Valida que los datos no se dupliquen en registro de movilidad
   */
   public RegistrosMovilidad getRegistrosMovilidad(RegistrosMovilidad registrosMovilidad);
   
    /*
   * Obtiene la lista de Programas de Movilidad que se encuentran guardadados en la base de datos, para poder descargarse en la plantilla del registro
   */
 
   public List<ProgramasMovilidad> getProgramasMovilidadAct();
   
    /**
     * Obtiene la lista de periodos con registros de tutorías y asesorías.
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro();
    
    /**
     * Obtiene la lista de eventos de registros correspondientes a un periodo escolar.
     * @param periodo Periodo escolar que filtra eventos de registro.
     * @return Lista de eventos de registro.
     */
    public List<EventosRegistros> getEventosPorPeriodo(PeriodosEscolares periodo);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @return Lista de registros mensuales.
     */
    public List<DTORegistroMovilidad> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
    
    
    public List<DTOMovilidadDocente> getListaRegistrosPorEventoAreaPeriodoDoc(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
    
    
     public List<DTOMovilidadEstudiante> getListaRegistrosPorEventoAreaPeriodoEst(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
   
    /**
     * Comprueba si el periodo del evento actual se encuentra en el periodo mas reciente, en caso de no encontrarlo obtiene el periodo correspondiente
     * @param periodos Lista de periodos obtenidos con registros
     * @param eventos Eventos del periodo mas reciente
     * @param eventoActual Evento de registro actual
     * @return Lista de periodos actualizados y eventos que inclyen evento actual del periodo correspondiente
     * @throws mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException Se lanza en casi que se requiera un periodo que no existe aún en la base de datos
     */
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual) throws PeriodoEscolarNecesarioNoRegistradoException;
    
    /**
     * Elimina un registro especificado de la tabla registro_movilidad_docente.
     * @param clave Registro a eliminar.
     * @return Devuelve TRUE si se eliminó el registro o FALSE de lo contrario.
     */
    public List<Integer> buscaRegistroDocentesRegMovilidad(String clave) throws Throwable;
     /**
     * Elimina un registro especificado de la tabla registro_movilidad_estudiante.
     * @param clave Registro a eliminar.
     * @return Devuelve TRUE si se eliminó el registro o FALSE de lo contrario.
     */
    public List<Integer> buscaRegistroEstudiantesRegMovilidad(String clave) throws Throwable;
   
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasDocente(String clave) throws Throwable;
     
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasEstudiante(String clave) throws Throwable;
}
