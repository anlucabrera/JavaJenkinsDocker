/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComAcadParticipantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOComisionesAcademicas;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbComisionesAcademicas {
   /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public List<DTOComisionesAcademicas> getListaComisionesAcademicas(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaComisionesAcademicas(List<DTOComisionesAcademicas> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   /**
     * Obtiene la clave del registro que corresponda con la actividad.
     * @param comisionAcademica Clave de la comisión académica.
     * @return clave del registro.
     */
   public Integer getRegistroComisionesAcademicasEspecifico(String comisionAcademica);
   
   /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param comisionesAcademicas
     * @return entity.
     */
   public ComisionesAcademicas getRegistroComisionesAcademicas(ComisionesAcademicas comisionesAcademicas); 
   
   /**
     * Obtiene la lista de tipo de comisiones académicas actuales.
     * @return lista de la entity.
     */
   public List<ComisionesAcademicasTipos> getComisionesAcademicasTiposAct();
   
    /**
     * Obtiene la lista de registros de comisiones académicas filtrado por ejercicio y mes seleccionado.
     * @param ejercicio Ejercicio fiscal.
     * @param mes Mes del ejercicio fiscal.
     * @return Lista de registros.
     */
   public List<DTOComisionesAcademicas> getRegistroDTOComAcad(String mes, Short ejercicio);
   
   /**
     * Obtiene la lista de registros de participantes de comisiones académicas filtrado por ejercicio y mes seleccionado.
     * @param ejercicio Ejercicio fiscal.
     * @param mes Mes del ejercicio fiscal.
     * @return Lista de registros.
     */
   public List<DTOComAcadParticipantes> getRegistroDTOPartComAcad(String mes, Short ejercicio);
   
    /**
     * Obtiene la lista de registros de participantes correspondientes a la clave de la comisión académica.
     * @param clave Clave de comisión académica.
     * @return Lista de registros.
     */
   public List<Integer> buscaRegistroParticipantesComAcad(String clave) throws Throwable;
   
    /**
     * Método que devuelve la lista de registros encontrados en caso de que el
     * registro principal contenga evidencias, esta lista será ocupada para
     * eliminar las evidencias
     *
     * @param clave Entity que permite la búsqueda de los participantes
     * @return Devuelve una lista de enteros que contiene las claves de los
     * registros encontrados
     * @throws Throwable
     */
    public List<Integer> buscaRegistroEvidenciasPartComAcad(String clave) throws Throwable;

}
