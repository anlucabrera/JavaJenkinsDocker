/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEgetsu;


/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEgetsuResultados {
  /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
  public List<DTOEgetsu> getListaEgetsuResultados(String rutaArchivo) throws Throwable;
  
  /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
  public void guardaEgetsuResultados(List<DTOEgetsu> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
  
   /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param generacion Clave de la generación.
     * @return entity.
     */
  public EgetsuResultadosGeneraciones getRegistroEgetsuResultadosGeneraciones(Short generacion);
  
    /**
     * Obtiene la lista de registros filtrado por generación seleccionada.
     * @param generacion Generación.
     * @return Lista de registros.
     */
  public List<DTOEgetsu> filtroEgetsu(Short generacion);
}
