/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import java.util.Map;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.pye.DTOActFormacionIntegral;
import mx.edu.utxj.pye.siip.dto.pye.DTOParticipantesActFormInt;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbActFormacionIntegral {
    
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public List<DTOActFormacionIntegral> getListaActFormacionIntegral(String rutaArchivo) throws Throwable;
   
   /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaActFormacionIntegral(List<DTOActFormacionIntegral> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
   /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param actividadFormacionIntegral Clave de la actividad.
     * @return entity.
     */
   public ActividadesFormacionIntegral getRegistroActividadesFormacionIntegral(String actividadFormacionIntegral);
   
    /**
     * Obtiene la clave del registro que corresponda con la actividad.
     * @param actividadFormacionIntegral Clave de la actividad.
     * @return clave del registro.
     */
   public Integer getRegistroActFormacionIntegralEspecifico(String actividadFormacionIntegral);
   
   /**
     * Obtiene la lista de tipo de actividades actuales.
     * @return lista de la entity.
     */
   public List<ActividadesTipos> getActividadesTiposAct();
    
   /**
     * Obtiene la lista de tipo de eventos actuales.
     * @return lista de la entity.
     */
   public List<EventosTipos> getEventosTiposAct();
   
    /**
     * Obtiene la lista de periodos con registros de actividades de formación integral.
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
    public List<DTOActFormacionIntegral> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @return Lista de registros mensuales.
     */
    public List<DTOParticipantesActFormInt> getListaRegistrosPorEventoAreaPeriodoPart(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
   
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
    public List<Integer> buscaRegistroParticipantesActFormInt(String clave) throws Throwable;
  
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasPartActFormInt(String clave) throws Throwable;
    
     /**
     * Método que devuelve la lista de Áreas que tienen registros en el mes seleccionado
     * @param mes Nombre del mes para realizar la búsqueda en el registro de Actividades de Formación Integral
     * @return Devuelve una lista de entities de AreasUniversidad que tienen registros en el mes correspondiente
     */
    public List<AreasUniversidad> getAFIAreasConRegistroMensualGeneral(String mes);
    
     /**
     * Método que actualiza el registro seleccionado 
     * @param nuevaActFormInt
     * @return entity.
     * @throws java.lang.Throwable
     */
    public ActividadesFormacionIntegral actualizarActFormInt(ActividadesFormacionIntegral nuevaActFormInt) throws Throwable;
    
     /**
     * Obtiene la lista de registros actuales del registro
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @return Lista de registros
     */
    public List<DTOActFormacionIntegral> getListaRegistrosAFI(Short claveArea);
    
    /**
     * Obtiene la lista de registros actuales del registro
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @return Lista de registros
     */
    public List<DTOParticipantesActFormInt> getListaRegistrosPAFI(Short claveArea);
}
