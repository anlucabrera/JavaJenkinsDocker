/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.pye.list.ListaDtoVisitasIndustriales;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaVisitasIndustriales;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbVisitasIndustriales {
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public ListaVisitasIndustriales getListaVisitasIndustriales(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaVisitasIndustriales lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaVisitasIndustriales(ListaVisitasIndustriales listaVisitasIndustriales, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param visitasIndustriales.
     * @return entity.
     */
   public VisitasIndustriales getRegistroVisitasIndustriales(VisitasIndustriales visitasIndustriales); 
   
    /**
     * Se llena la lista de visitas industriales que se muestran en la interfaz del cliente
     * para llenarla se toman como dato dos parametros importantes el ejercicio fiscal y el mes que contenga ese ejercicio fiscal
     * @param mes parametro de filtrado
     * @param ejercicio parametro de filtrado
     * @return 
     */
    public List<VisitasIndustriales> getVisitasIndustrialesRegistrosPorEjercicioMesArea(String mes,Short ejercicio);
    /**
     * Metodo que llana el DTO conformado por dos entidades, el cual se llena a base de 
     * la misma forma que los anteriores, solo que a diferencia de encontrar los datos de dos entities 
     * con los parametros, este llena los datos del DTO mediante dos entities
     * @param mes parametro que indica el mes o meses pertenecientes al ejercicio fiscal
     * @param ejercicio ejercicio fiscal seleccionado
     * @return 
     */
    public List<ListaDtoVisitasIndustriales> getListaVisitasIndutrialesDTO(String mes, Short ejercicio);
    
}
