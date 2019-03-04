/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvidenciasAlineacion {
   
     /**
     * Método que devuelve la lista de registros encontrados en caso de que el registro principal contenga evidencias, esta lista será ocupada para eliminar las evidencias
     * @param registro   Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los registros encontrados
     * @throws Throwable 
     */
    public List<Integer> buscaRegistroEvidenciasRegistro(Integer registro) throws Throwable;
    
    
    /**
     * Obtiene la lista de evidencias del registro correspondiente.
     * @param registro Registro a obtener sus evidencias
     * @return Lista de evidencias detalle
     */
    public List<EvidenciasDetalle> getListaEvidenciasPorRegistro(Integer registro);
    
    /**
     * Registra múltiples archivos como evidencias del registro especificado.
     * @param registro Registro al que se van a agreegar las evidencias.
     * @param archivos Lista de archivos de evidencias.
     * @param eventosRegistros Evento Registro en el que se está registrando la información.
     * @param registrosTipo Información tipo de registro que se está registrando.
     * @return Regresa una entrada de mapa con la clave tipo boleana indicando si todos las evidencias se almacenaron y como valor la cantidad de evidencias registradas.
     */
    public Map.Entry<Boolean, Integer> registrarEvidenciasARegistro(Integer registro, List<Part> archivos, EventosRegistros eventosRegistros, RegistrosTipo registrosTipo);
    
    /**
     * Elimina una evidencia de un registro, si la evidencia solo está asignada al registro especificado la elimina de la base de datos y del disco duro, de lo contrario solo la 
     * desliga del registro.
     * @param registro Registro al que está ligada la evidencia.
     * @param evidenciasDetalle Evidencia a desligar o eliminar
     * @return Regresa TRUE si la evidencia se eliminó, FALSE de lo contrario o NULL si solo se desligó.
     */
    public Boolean eliminarEvidenciaEnRegistro(Integer registro, EvidenciasDetalle evidenciasDetalle);
    
    /**
     * Obtiene la referencia a la actividad que está alineada con el registro.
     * @param registro Registro del cual se desea conocer su actividad alineada.
     * @return Devuelve la referencia a la actividad alineada o null si es que no ha sido alineada.
     */
    public ActividadesPoa getActividadAlineada(Integer registro);
    
    /**
     * Alinea el registro con la actividad para que compartan evidencias.
     * @param actividad Actividad con la que se desea alinear.
     * @param registro Registro que se desea alinear.
     * @return Devuelve TRUE si la alineación se completó o FALSE de lo contrario.
     */
    public Boolean alinearRegistroActividad(ActividadesPoa actividad, Integer registro);
    
     /**
     * Elimina la alineación de un registro con una actividad, sin eliminar la evidencia ni el registro
     * @param registro Registro que esta ligado con l actividad.
     * @return Regresa TRUE si la evidencia se eliminó, FALSE de lo contrario o NULL si solo se desligó.
     */
    public Boolean eliminarAlineacion(Integer registro);
    
}
