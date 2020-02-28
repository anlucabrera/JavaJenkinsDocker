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
import mx.edu.utxj.pye.sgi.entity.pye2.AulasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaTiposInstalaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.siip.dto.eb.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.eb.DTODistribucionLabTallCPE;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDistribucionInstalaciones {
    public List<DTOCapacidadInstaladaCiclosEscolares> getListaCapacidadInstaladaCiclosEScolares(String rutaArchivo) throws Throwable;
    public List<DTODistribucionAulasCPE> getListaDistribucionAulasCPE(String rutaArchivo) throws Throwable;
    public List<DTODistribucionLabTallCPE> getListaDistribucionLabTall(String rutaArchivo) throws Throwable;
    public void guardaCapacidadInstalada(List<DTOCapacidadInstaladaCiclosEscolares> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaDistribucionAulas(List<DTODistribucionAulasCPE> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaDistribucionLabTall(List<DTODistribucionLabTallCPE> listaDistribucionInstalaciones, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public CapacidadInstaladaCiclosEscolares getCapacidadIntaladaCE(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares);
    public DistribucionAulasCicloPeriodosEscolares getDistribucionAulasCPE(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares);
    public DistribucionLabtallCicloPeriodosEscolares getDistribucionLabtallCicloPeriodosEscolares(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares);
    
    /***************************** Catálogos para la actualización de plantillas *******************************************/
    /**
     * Devuelve una lista completa de CapacidadInstaladaTiposInstalaciones para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de CapacidadInstaladaTiposInstalaciones
     */
    public List<CapacidadInstaladaTiposInstalaciones> getCapacidadInstaladaTiposInstalacioneses();
    
    /**
     * Devuelve una lista completa de AulasTipo para el llenado de las plantillas correspondientes
     * @return  Lista de entidades de AulasTipo
     */
    public List<AulasTipo> getAulasTipos();
    
    /**
     * Obtiene la lista de periodos con registros con la distribución de instalaciones.
     * @param registrosTipoCapInst
     * @param registrosTipoAulas
     * @param registrosTipoLabTall
     * @param eventoRegistro
     * @param area
     * @return Lista de periodos.
     */
    public List<PeriodosEscolares> getPeriodosConregistro(RegistrosTipo registrosTipoCapInst, RegistrosTipo registrosTipoAulas, RegistrosTipo registrosTipoLabTall, EventosRegistros eventoRegistro, AreasUniversidad area);

    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTOCapacidadInstaladaCiclosEscolares> getListaCapacidadInstaladaPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);
    
    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTODistribucionAulasCPE> getListaDistribucionAulasPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);

    /**
     * Obtiene la lista de registros correspondientes al evento seleccionado.
     * @param evento Evento seleccionado.
     * @param claveArea Área a la que pertenece el usuario y que servirá como filtro.
     * @param periodo Periodo del evento y que servirá como filtro
     * @param registrosTipo Permite filtrar los registros por el tipo necesario.
     * @return Lista de registros mensuales.
     */
    public List<DTODistribucionLabTallCPE> getListaDistribucionLabTallPorEventoAreaPeriodo(EventosRegistros evento, Short claveArea, PeriodosEscolares periodo, RegistrosTipo registrosTipo);
    
     /**
     * Comprueba si el periodo del evento actual se encuentra en el periodo mas reciente, en caso de no encontrarlo obtiene el periodo correspondiente.
     * @param periodos Lista de periodos obtenidos con registros.
     * @param eventos Eventos del periodo mas reciente.
     * @param eventoActual Evento de registro actual.
     * @param registrosTipoCapInst
     * @param registrosTipoAulas
     * @param registrosTipoLabTall
     * @param area
     * @return Lista de periodos actualizados y eventos que incluyen evento actual del periodo correspondiente.
     * @throws mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException Se lanza en casi que se requiera un periodo que no existe aún en la base de datos.
     */
    public Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoActual, RegistrosTipo registrosTipoCapInst, RegistrosTipo registrosTipoAulas, RegistrosTipo registrosTipoLabTall, AreasUniversidad area) throws PeriodoEscolarNecesarioNoRegistradoException;    

    public List<CapacidadInstaladaCiclosEscolares> getReporteCuatrimestralCapacidadInstaladaCicloPeriodosEscolares(PeriodosEscolares periodoEscolar, AreasUniversidad areasUniversidad);
    
    public List<DistribucionAulasCicloPeriodosEscolares> getReporteCuatrimestralDistribucionAulas(PeriodosEscolares periodoEscolar, AreasUniversidad areasUniversidad);
    
    public List<DistribucionLabtallCicloPeriodosEscolares> getReporteCuatrimestralDistribucionLaboratoriosTalleres(PeriodosEscolares periodoEscolar, AreasUniversidad areasUniversidad);
    
}
