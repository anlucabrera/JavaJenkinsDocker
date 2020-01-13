/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTODesercion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionPeriodos;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoDesercion;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDesercionPeriodos {
    
   /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
   */
   public ListaDesercionPeriodos getListaDesercionPeriodos(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaDesercionPeriodos lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaDesercionPeriodos(ListaDesercionPeriodos listaDesercionPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
     /**
     * Obtiene la clave del registro que corresponda con la clave de deserción académica.
     * @param dpe Clave de deserción.
     * @return clave del registro.
     */
   public Integer getRegistroDesercionPeriodosEspecifico(String dpe);
   
    /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param desercionPeriodosEscolares entity a registrar.
     * @return entity.
     */
   public DesercionPeriodosEscolares getRegistroDesercionPeriodosEscolares(DesercionPeriodosEscolares desercionPeriodosEscolares); 
   
   /**
     * Método que regrese datos de la tabla de Deserción Periodo Escolares dependiendo de la Clave de Deserción.
     * @param desercionPeriodosEscolares entity a registrar.
     * @return entity.
     */
   public DesercionPeriodosEscolares getRegistroDesercionClave(DesercionPeriodosEscolares desercionPeriodosEscolares);
   
   /**
     * Metodo que regresa una lista de una entity dependiendo de los criterios de busqueda
     * @param mes parametro que indica el mes o meses pertenecientes al ejercicio fiscal
     * @param ejercicio ejercicio fiscal seleccionado
     * @return 
     */
    public List<DesercionPeriodosEscolares> getListaDesercionDTO(String mes, Short ejercicio);
    
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga participantes, esta lista será ocupada para eliminar los participantes
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroReprobacionDesercionAcademica(String clave) throws Throwable;
    
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param clave   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasDesercionRep(String clave) throws Throwable;
    
    
    /**
     * Se llena el DTODesercion para mostrarse en la interfaz grafica
     * utiliza dos parametros para poder hacer el filtrado 
     * el ejercicio activo o en los que se encuentren registros y el mes
     * el mes depende el ejercicio fiscal
     * @param mes
     * @param ejercicio
     * @return 
     */
    public List<DTODesercion> getListaDtoDesercion(String mes, Short ejercicio);
    
    /**
     * Se llena la lista ListaDtoDesercion para mostrarse en la interfaz grafica
     * utiliza dos parametros para poder hacer el filtrado 
     * el ejercicio activo o en los que se encuentren registros y el mes
     * el mes depende el ejercicio fiscal
     * @param mes
     * @param ejercicio
     * @return 
     */
    public List<ListaDtoDesercion> getListaRegistrosDesercionDto(String mes, Short ejercicio);
    
     /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<ListaDtoDesercion> getRegistroDesercion();
}
