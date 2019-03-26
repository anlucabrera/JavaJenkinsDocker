/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.IemsServedu;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbIems {
    
    /**
     * Obtiene la lista de iems.
     * @return lista de la entity.
     */
    public List<Iems> getIems();
    
    /**
     * Obtiene la lista de iems por fechas.
     * @return lista de la entity.
     */
    public List<Iems> getIemsByFechas();
    
    /**
     * Cambiar el status actual del IEMS en la base de datos
     * @param iemsID
     * @throws java.lang.Throwable
     *
     */   
    public void cambiarStatusIEMS(Integer iemsID) throws Throwable;
    
     /**
     * Obtiene la lista de registros filtrado por estado y municipio seleccionado.
     * @param estado Clave del estado.
     * @param municipio Clave del municipio.
     * @return Lista de registros.
     */
    public List<Iems> filtroIems(Integer estado, Integer municipio);
    
     /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
    public ListaIemsPrevia getListaIemsPrevia (String rutaArchivo) throws Throwable;
    
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaIemsPrevia lista de registros que se guardaran.
     * @throws java.lang.Throwable
     */
    public void guardaIems(ListaIemsPrevia listaIemsPrevia) throws Throwable;
    
     /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param iems.
     * @return entity.
     */
    public Iems getRegistroIems(Iems iems);
    
    /**
     * Obtiene la lista de los iems actuales.
     * @return lista de la entity.
     */
    public List<Iems> getIemsVigentes();
    
    /**
     * Obtiene la clave del registro que corresponda con el iems.
     * @param iems Clave del Iems.
     * @return clave del registro.
     */
    public Integer getRegistroIemsEspecifico(Integer iems);
    
    /**
     * Obtiene la lista de tipos de servicio educativo actuales.
     * @return lista de la entity.
     */
    public List<IemsServedu> getServEducativosAct();
     
    /**
     * Obtiene el status del registro correspondiente.
     * @param iemsID Registro a obtener su status
     * @return Status del registro
    */
    public List<Iems> getListaStatusPorRegistro(Integer iemsID);
    
     /**
     * Método que actualiza el IEMS seleccionado 
     * @param nuevoIems
     * @return entity.
     * @throws java.lang.Throwable
     */
    public Iems actualizarIems(Iems nuevoIems) throws Throwable;    
}
