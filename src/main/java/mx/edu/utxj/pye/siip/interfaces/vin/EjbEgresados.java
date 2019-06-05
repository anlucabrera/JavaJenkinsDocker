/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEconomicaEgresadoG;
import mx.edu.utxj.pye.siip.dto.vin.DTOActividadEgresadoGeneracion;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelIngresoEgresadosG;
import mx.edu.utxj.pye.siip.dto.vin.DTONivelOcupacionEgresadosG;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEgresados {
    public List<DTOActividadEgresadoGeneracion> getListaActividadEgresadoGeneracion(String rutaArchivo) throws Throwable;
    public List<DTOActividadEconomicaEgresadoG> getListaActividadEconomicaEgresadoGeneracion(String rutaArchivo) throws Throwable;
    public List<DTONivelOcupacionEgresadosG> getListaNivelOcupacionEgresadoGeneracion(String rutaArchivo) throws Throwable;
    public List<DTONivelIngresoEgresadosG> getListaNivelIngresoEgresadoGeneracion(String rutaArchivo) throws Throwable;
    public void guardaActividadEgresadoGeneracion(List<DTOActividadEgresadoGeneracion> listaActividadEgresadoGeneracion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaActividadEcnomicaEgresadoG(List<DTOActividadEconomicaEgresadoG> listaActividadEconomicaEgresadoG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaNivelOcupacionEgresadoG(List<DTONivelOcupacionEgresadosG> listaNivelOcupacionEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaNivelIngresoEgresadoG(List<DTONivelIngresoEgresadosG> listaNivelIngresoEgresadosG, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public ActividadEgresadoGeneracion getActividadEgresadoGeneracion(ActividadEgresadoGeneracion actividadEgresadoGeneracion);
    public ActividadEconomicaEgresadoGeneracion getActividadEconomicaEgresadoGeneracion(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion);
    public NivelOcupacionEgresadosGeneracion getNivelOcupacionEgresadosGeneracion(NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion);
    public NivelIngresosEgresadosGeneracion getNivelIngresosEgresadosGeneracion(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion);
    
    /***************************** Catálogos para la actualización de plantillas *******************************************/
    
    /**
     * Devuelve una lista de tipo ActividadEgresadoTipos, para la actualización de las plantillas correspondientes
     * @return Lista de entidades de ActividadEgresadoTipos 
     * @throws java.lang.Throwable 
     */
    public List<ActividadEgresadoTipos> getActividadEgresadoTipos() throws Throwable;
    
    /**
     * Devuelve una lista de tipo NivelOcupacionTipos, para la actualización de las plantillas correspondientes
     * @return Lista de entidades de NivelOcupacionTipos 
     * @throws java.lang.Throwable 
     */
    public List<NivelOcupacionTipos> getNivelOcupacionTipos() throws Throwable;
    
    /**
     * Devuelve una lista de tipo NivelIngresosTipos, para la actualización de las plantillas correspondientes
     * @return Lista de entidades de NivelIngresosTipos 
     * @throws java.lang.Throwable 
     */
    public List<NivelIngresosTipos> getNivelIngresosTipos() throws Throwable;
    
    /***************************** Filtrado de información por ejercicio mes y área *******************************************/
    
    /**
     * Métodos que se ocupan para el filtrado de los diferentes tipos de registro de egresados por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de DTOServiciosEnfermeriaCicloPeriodos que serán ocupados para consulta o eliminación
     */
    public List<DTOActividadEgresadoGeneracion> getFiltroActividadEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<DTOActividadEconomicaEgresadoG> getFiltroActividadEconomicaEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<DTONivelOcupacionEgresadosG> getFiltroActividadNivelOcupacionEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<DTONivelIngresoEgresadosG> getFiltroNivelIngresoEgresadoEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<ActividadEgresadoGeneracion> getReporteActividadEgresadoPorEjercicio();
    
    public List<ActividadEconomicaEgresadoGeneracion> getReporteActividadEconomicaEgresadoPorEjercicio();
    
    public List<NivelOcupacionEgresadosGeneracion> getReporteNivelOcupacionEgresadoPorEjercicio();
    
    public List<NivelIngresosEgresadosGeneracion> getReporteNivelIngresosEgresadoPorEjercicio();
    
}
