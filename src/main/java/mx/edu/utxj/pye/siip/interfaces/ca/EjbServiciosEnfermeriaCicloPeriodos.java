/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo;
import mx.edu.utxj.pye.siip.dto.ca.DTOServiciosEnfemeriaCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbServiciosEnfermeriaCicloPeriodos {
    public List<DTOServiciosEnfemeriaCicloPeriodos> getListaServiciosEnfermeriaCicloPeriodos(String rutaArchivo) throws Throwable;
    public void guardaServiciosEnfermeriaCicloPeriodos(List<DTOServiciosEnfemeriaCicloPeriodos> listaServiciosEnfermeriaCicloPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    public ServiciosEnfermeriaCicloPeriodos getServicioEnfermeriaCicloPeriodo(ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos);
    
    /***************************** Catálogos para la actualización de plantillas *******************************************/
    
    /**
     * Devuelve una lista de tipo ServiciosEnfermeriaTipo, para la actualización de las plantillas correspondientes
     * @return Lista de entidades de ServiciosEnfermeriaTipo 
     * @throws java.lang.Throwable 
     */
    public List<ServiciosEnfermeriaTipo> getServiciosEnfermeriaTipos() throws Throwable;
    
    /**
     * Método que se ocupa para el filtrado de ServiciosEnfermeriaCicloPeriodos por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de DTOServiciosEnfermeriaCicloPeriodos que serán ocupados para consulta o eliminación
     */
    public List<DTOServiciosEnfemeriaCicloPeriodos> getFiltroServiciosEnfermeriaEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<ServiciosEnfermeriaCicloPeriodos> getReporteGeneralEjercicioServiciosEnfermeria(Short ejercicio);
    
    public ServiciosEnfermeriaCicloPeriodos editaServicioEnfermeria(ServiciosEnfermeriaCicloPeriodos servicioEnfermeria);
    
    public Boolean buscaServicioEnfermeriaExistente(ServiciosEnfermeriaCicloPeriodos servicioEnfermeria);
}
