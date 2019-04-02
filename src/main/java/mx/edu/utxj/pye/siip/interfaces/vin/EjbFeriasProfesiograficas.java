/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasProfesiograficas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasDTO;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasProfesiograficas;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbFeriasProfesiograficas {
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public ListaFeriasProfesiograficas getListaFeriasProfesiograficas(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaFeriasProfesiograficas lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaFeriasProfesiograficas(ListaFeriasProfesiograficas listaFeriasProfesiograficas, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
    /**
     * Obtiene la clave del registro que corresponda con la feria profesiográfica.
     * @param feria Clave de la feria profesiográfica.
     * @return clave del registro.
     */
   public Integer getRegistroFeriasProfesiograficasEspecifico(String feria);
   
    /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param feriasProfesiograficas.
     * @return entity.
     */
   public FeriasProfesiograficas getRegistroFeriasProfesiograficas(FeriasProfesiograficas feriasProfesiograficas);
   
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroParticipantesFeriasProf(String clave) throws Throwable;
   
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasPartFeriasProf(String clave) throws Throwable;
    
     /**
     * Obtiene la lista de registros filtrado por ejercicio fiscal y mes seleccionado.
     * @param ejercicio Ejercicio fiscal.
     * @param mes Mes del ejercicio fiscal.
     * @return Lista de registros.
     */
    public List<ListaFeriasDTO> getRegistroFeriaProf( String mes, Short ejercicio);
    
     /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<ListaFeriasDTO> getRegistroReporteFerProf();
}
