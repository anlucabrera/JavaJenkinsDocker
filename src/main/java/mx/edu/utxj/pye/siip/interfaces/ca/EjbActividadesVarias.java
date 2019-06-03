/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbActividadesVarias {

    public List<ActividadesVariasRegistro> getListaActividadesVarias(String rutaArchivo) throws Throwable;
    
    public void guardaActividadesVarias(List<ActividadesVariasRegistro> listaActividadesVarias, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public ActividadesVariasRegistro buscaActividadVariaRegistroEspecifico(ActividadesVariasRegistro avr);
    /**
     * Método que se ocupa para el filtrado de ActividadesVariasRegistro por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de ActividadesVariasRegistro que serán ocupados para consulta o eliminación
     */
    public List<ActividadesVariasRegistro> getFiltroActividadesVariasEjercicioMesArea(Short ejercicio, String mes, Short area);
    
    public List<ActividadesVariasRegistro> getFiltroActividadesVariasEjercicioMes(Short ejercicio, String mes);
    
    public List<AreasUniversidad> getAreasConRegistroMensualGeneral(Boolean tipoConsulta, String mes);
    
    /**
     * Actualiza en base de datos los campos de la tabla de registros de ActividadesVarias
     * @param actividadVariaEditada
     * @return 
     */
    public ActividadesVariasRegistro editaActividadesVarias(ActividadesVariasRegistro actividadVariaEditada);
    
    public Boolean buscaActividadVariaExistente(ActividadesVariasRegistro actividadVaria);
    
    public List<ActividadesVariasRegistro> reporteActividadesVariasPorEjercicio();
    
    public void guardaActividadVaria(ActividadesVariasRegistro actividadVaria, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
}
