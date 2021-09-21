/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionesServtecEducont;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.SatisfaccionServtecEducontAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTOEvaluacionSatEduContAnioMes;
import mx.edu.utxj.pye.siip.dto.vin.DTOSatisfaccionServTecEduContAnioMes;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbSatisfaccionServTecEduContAnioMes {
    public List<SatisfaccionServtecEducontAnioMes> getListaSatisfaccionServTecEduContAnioMes(String rutaArchivo) throws  Throwable;
    public  ResultadoEJB<List<SatisfaccionServtecEducontAnioMes>> guardaSatisfaccionServTecEduCont(List<SatisfaccionServtecEducontAnioMes> listaSatisfaccionServTecEduContAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public  ResultadoEJB<ServiciosTecnologicosAnioMes> getRegistroServicioTecnologicoEspecifico(String servicio);
    
    public  ResultadoEJB<SatisfaccionServtecEducontAnioMes> getRegistroSatisfaccionServTecEduContEspecifico(String servicio);
    
    /**
     * Método que se ocupa para el filtrado de Servicios Tecnológicos por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Servicios Tecnológicos que serán ocupados para consulta o eliminación
     */
    public ResultadoEJB<List<DTOSatisfaccionServTecEduContAnioMes>> getFiltroSatisfaccionSerTecEduContEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public ResultadoEJB<List<DTOSatisfaccionServTecEduContAnioMes>> getReporteGeneralSatisfaccionSerTecEduCont(Short ejercicio);
    
    /**
     * Método que realiza una consulta a la base de datos de todos los tipos de servicios tecnológicos que se encuentran en la base de datos, el cual será ocupado para el llenado de la plantilla de servicios tecnológicos
     * @return  Devuelve una lista de entidades de ServiciosTipos
     * @throws Throwable 
     */
    public ResultadoEJB<List<ServiciosTecnologicosAnioMes>> getListaServicios() throws Throwable;
    
    public ResultadoEJB<SatisfaccionServtecEducontAnioMes> actualizarSatisfaccionSerTecEduContAnioMes(SatisfaccionServtecEducontAnioMes satisfaccionServtecEducontAnioMes);
    
    public List<DTOEvaluacionSatEduContAnioMes.LecturaPlantilla> getListaEvaluacionSatEduContAnioMes(String rutaArchivo) throws  Throwable;
    
    public  ResultadoEJB<List<EvaluacionSatisfaccionResultados>> guardaEvaluacionSatEduCont(List<DTOEvaluacionSatEduContAnioMes.LecturaPlantilla> listaEvaluacionSatisfaccionResultadoses, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
   
    public  ResultadoEJB<EvaluacionSatisfaccionResultados> getRegistroEvaluacionSatEduContEspecifico(String servicio, String numeroPregunta);
    
    /**
     * Método que se ocupa para el filtrado de Servicios Tecnológicos por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Servicios Tecnológicos que serán ocupados para consulta o eliminación
     */
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros>> getFiltroEvaluacionSatEduContEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.ConsultaRegistros>> getReporteGeneralEvaluacionSatEduCont(Short ejercicio);
    
    public ResultadoEJB<EvaluacionSatisfaccionResultados> actualizarEvaluacionSatEduContAnioMes(EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados);
    
    public ResultadoEJB<List<DTOEvaluacionSatEduContAnioMes.CatalogoPlantilla>> getListaPreguntasEvaluacion() throws Throwable;
    
    public ResultadoEJB<EvaluacionesServtecEducont> getEvaluacionActiva();
}
