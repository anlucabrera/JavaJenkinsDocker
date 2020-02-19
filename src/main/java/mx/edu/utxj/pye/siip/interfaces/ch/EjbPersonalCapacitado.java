/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import javax.ejb.Local;
import java.util.Map;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapModalidad;
import mx.edu.utxj.pye.sgi.entity.pye2.PercapTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPersonalCapacitado;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPerCapParticipantes;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPersonalCapacitado {
   /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public List<DTOPersonalCapacitado> getListaPersonalCapacitado(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaPersonalCapacitado(List<DTOPersonalCapacitado> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
    /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param personalCapacitado Clave del personal.
     * @return entity.
     */
   public PersonalCapacitado getRegistroPersonalCapacitado(String personalCapacitado);
   
   /**
     * Obtiene la clave del registro que corresponda con la actividad.
     * @param personalCapacitado Clave del personal.
     * @return clave del registro.
     */
   public Integer getRegistroPersonalCapacitadoEspecifico(String personalCapacitado);
   
   /**
     * Obtiene la lista de tipo de capacitaciones actuales.
     * @return lista de la entity.
     */
   public List<PercapTipo> getPerCapTipoAct();
   
    /**
     * Obtiene la lista de modalidades de capacitación actuales.
     * @return lista de la entity.
     */
   public List<PercapModalidad> getPerCapModalidadAct();
   
   /**
     * Obtiene la lista de periodos con registros de acervo bibliográfico.
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
     * @return Lista de registros mensuales de Capacitación.
     */
    public List<DTOPersonalCapacitado> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
    
     /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @return Lista de registros mensuales de Participantes.
     */
    public List<DTOPerCapParticipantes> getListaRegistrosPorEventoAreaPeriodoPart(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
   
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
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroParticipantesPersonalCapacitado(String clave) throws Throwable;
  
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasPartPersonalCap(String clave) throws Throwable;
    
    /**
     * Método que actualiza el registro seleccionado 
     * @param nuevoPerCap
     * @return entity.
     * @throws java.lang.Throwable
     */
    public PersonalCapacitado actualizarPerCap(PersonalCapacitado nuevoPerCap) throws Throwable;
    
     /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<DTOPersonalCapacitado> getRegistroPerCap();
    
     /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<DTOPerCapParticipantes> getRegistroPartCap();
}
