/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTOReprobacion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionReprobacion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoReprobacion;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoReprobacion;
import mx.edu.utxj.pye.sgi.entity.pye2.MateriasProgramaEducativo;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbDesercionReprobacion {
    
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
   */
    public ListaDesercionReprobacion getListaDesercionReprobacion(String rutaArchivo) throws Throwable;
    
      /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaDesercionReprobacion lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
    public void guardaDesercionReprobacion(ListaDesercionReprobacion listaDesercionReprobacion, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
    
    /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param desercionReprobacionMaterias entity a registrar.
     * @return entity.
     */
    public DesercionReprobacionMaterias getRegistroDesercionReprobacionMaterias(DesercionReprobacionMaterias desercionReprobacionMaterias);

    /**
     * Método que regresa una lista de una entity dependiendo de los criterios
     * de busqueda
     *
     * @param mes parametro que indica el mes o meses pertenecientes al
     * ejercicio fiscal
     * @param ejercicio ejercicio fiscal seleccionado
     * @return
     */
    public List<DesercionReprobacionMaterias> getListaReprobacionDTO(String mes, Short ejercicio);
 
    /**
     * Método que regresa una lista de una entity dependiendo de los criterios
     * de busqueda
     *
     * @param mes parametro que indica el mes o meses pertenecientes al
     * ejercicio fiscal
     * @param ejercicio ejercicio fiscal seleccionado
     * @return
     */
    public List<DTOReprobacion> getListaDtoReprobacion(String mes, Short ejercicio);
    
    /**
     * Método que regresa una lista de un DTO dependiendo de los criterios
     * de busqueda
     *
     * @param mes parametro que indica el mes o meses pertenecientes al
     * ejercicio fiscal
     * @param ejercicio ejercicio fiscal seleccionado
     * @return
     */
    public List<ListaDtoReprobacion> getListaRegistrosReprobacionDto(String mes, Short ejercicio);
    
    /**
     * Método que regresa una lista de una entity
     * de busqueda
     * @return
     */
    public List<MateriasProgramaEducativo> getMateriasProgramaEducativoAct();
    
     /**
     * Obtiene la lista de materias reprobadas por deserción.
     * @param desercion deserción académica.
     * @return Lista de materias reprobadas.
     */
    public List<DTOReprobacion> getListaMateriasReprobadas(String desercion);
}

