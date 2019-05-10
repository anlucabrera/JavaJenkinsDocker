/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTOEquiposComputoInternetCPE;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDistribucionEquipamiento {
    public List<DTOEquiposComputoCPE> getListaEquiposComputoCPE(String rutaArchivo) throws Throwable;
    public List<DTOEquiposComputoInternetCPE> getListaEquiposComputoInternetCPE(String rutaArchivo) throws Throwable;
    public void guardaEquipoComputoCPE(List<DTOEquiposComputoCPE> listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaEquipoComputoInternetCPE(List<DTOEquiposComputoInternetCPE> listaDistribucionEquipamiento, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public EquiposComputoCicloPeriodoEscolar getEquiposComputCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar);
    public EquiposComputoInternetCicloPeriodoEscolar getEquiposComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar);
    
    /**
     * Obtiene la lista de periodos con registros de la distribución de equipos de cómputo.
     * @param registrosTipoEqC
     * @param registrosTipoEqCI
     * @param eventoRegistro
     * @param area
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipoEqC, RegistrosTipo registrosTipoEqCI, EventosRegistros eventoRegistro, AreasUniversidad area);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTOEquiposComputoCPE> getListaEquiposComputoPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);
    
    public List<EquiposComputoCicloPeriodoEscolar> reporteEquiposComputoCicloPeriodoEscolares();
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTOEquiposComputoInternetCPE> getListaEquiposComputoInternetPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);

    public List<EquiposComputoInternetCicloPeriodoEscolar> reporteEquiposComputoInternetCicloPeriodoEscolares();
    
    /**
     * Comprueba si el periodo del evento actual se encuentra en el periodo mas reciente, en caso de no encontrarlo obtiene el periodo correspondiente.
     * @param periodos Lista de periodos obtenidos con registros.
     * @param eventos Eventos del periodo mas reciente.
     * @param eventoActual Evento de registro actual.
     * @param registrosTipoEC
     * @param registrosTipoECI
     * @param area
     * @return Lista de periodos actualizados y eventos que incluyen evento actual del periodo correspondiente.
     * @throws mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException Se lanza en casi que se requiera un periodo que no existe aún en la base de datos.
     */
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipoEC, RegistrosTipo registrosTipoECI, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException;    
    
    public Boolean buscaEquipoComputoExistente(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar);
    
    public Boolean buscaEquipoComputoInternetExistente(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar);
    
    public EquiposComputoCicloPeriodoEscolar editaEquipoComputoCicloPeriodoEscolar(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar);
    
    public EquiposComputoInternetCicloPeriodoEscolar editaEquipoComputoInternetCicloPeriodoEscolar(EquiposComputoInternetCicloPeriodoEscolar equiposComputoInternetCicloPeriodoEscolar);
    
}
