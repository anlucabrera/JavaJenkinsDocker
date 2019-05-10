/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTipos;
import mx.edu.utxj.pye.siip.dto.vin.DTOServiciosTecnologicosParticipantes;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbServiciosTecnologicosAnioMes {
    public List<ServiciosTecnologicosAnioMes> getListaServiciosTecnologicosAnioMes(String rutaArchivo) throws  Throwable;
    public List<DTOServiciosTecnologicosParticipantes> getListaServiciosTecnologicosParticipantes(String rutaArchivo) throws  Throwable;
    public void guardaServiciosTecnologicosAnioMes(List<ServiciosTecnologicosAnioMes> listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public void guardaServiciosTecnologicosParticipantes(List<DTOServiciosTecnologicosParticipantes> listaServiciosTecnologicosAnioMes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public Integer getRegistroServicioTecnologicoEspecifico(String servicio);
    public ServiciosTecnologicosAnioMes getServiciosTecnologicosAnioMes(ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes);
    public ServiciosTecnologicosParticipantes getServiciosTecnologicosParticipantes(ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes);
    
    /**
     * Método que se ocupa para el filtrado de Servicios Tecnológicos por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Servicios Tecnológicos que serán ocupados para consulta o eliminación
     */
    public List<ServiciosTecnologicosAnioMes> getFiltroServiciosTecnologicosEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<ServiciosTecnologicosAnioMes> getReporteGeneralServiciosTecnologicos();
    
    /**
     * Método que se ocupa para el filtrado de Servicios Tecnológicos Participantes por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de Servicios Tecnológicos Participantes que serán ocupados para consulta o eliminación
     */
    public List<DTOServiciosTecnologicosParticipantes> getFiltroServiciosTecnologicosPartEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<ServiciosTecnologicosParticipantes> getReporteGeneralServiciosTecnologicoParticipantes();
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param servicioTecnologico   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroParticipantesServiciosTecnologicos(ServiciosTecnologicosAnioMes servicioTecnologico) throws Throwable;
    
    /**
     * Método que realiza una consulta a la base de datos de todos los tipos de servicios tecnológicos que se encuentran en la base de datos, el cual será ocupado para el llenado de la plantilla de servicios tecnológicos
     * @return  Devuelve una lista de entidades de ServiciosTipos
     * @throws Throwable 
     */
    public List<ServiciosTipos> getListaServiciosTipo() throws Throwable;
    
    public ServiciosTecnologicosAnioMes editaServicioTecnologicoAnioMes(ServiciosTecnologicosAnioMes servicioTecnologicoAnioMes);
    
    public ServiciosTecnologicosParticipantes editaServicioTecnologicoParticipante(ServiciosTecnologicosParticipantes servicioTecnologicoParticipante);
    
}