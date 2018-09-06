/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbAsesoriasTutoriasCiclosPeriodos {
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaAsesoriasTutorias(String rutaArchivo) throws Throwable;
    
    public void guardaAsesoriasTutorias(List<DTOAsesoriasTutoriasCicloPeriodos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public AsesoriasTutoriasCicloPeriodos getRegistroAsesoriaTutoriaCicloPeriodo(AsesoriasTutoriasCicloPeriodos asesoriaTutoriaCicloPeriodo);
    
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
    public List<DTOAsesoriasTutoriasCicloPeriodos> getListaRegistrosPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo);
    
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
     * Elimina un registro especificado.
     * @param registro Registro a eliminar.
     * @return Devuelve TRUE si se eliminó el registro o FALSE de lo contrario.
     */
    public Boolean eliminarRegistro(DTOAsesoriasTutoriasCicloPeriodos registro);
}
