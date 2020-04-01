/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.pa;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOPresupuestos;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPresupuestos {
     /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
   public List<DTOPresupuestos> getListaPresupuestos(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaPresupuestos(List<DTOPresupuestos> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
   /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param presupuestos
     * @return entity.
     */
   public Presupuestos getRegistroPresupuestos(Presupuestos presupuestos);
   
   /**
     * Obtiene la lista de tipo de capitulos actuales.
     * @return lista de la entity.
     */
   public List<CapitulosTipos> getCapitulosTiposAct();
   
    /**
     * Obtiene la lista de registros filtrado por ejercicio fiscal y mes seleccionado.
     * @param ejercicio Ejercicio fiscal.
     * @param mes Mes del ejercicio fiscal.
     * @return Lista de registros.
     */
   public List<DTOPresupuestos> getRegistroDTOPresupuestos(String mes, Short ejercicio);
   
    /**
     * Método que actualiza el registro seleccionado 
     * @param nuevoPresupuesto
     * @return entity.
     * @throws java.lang.Throwable
     */
    public Presupuestos actualizarPresupuesto(Presupuestos nuevoPresupuesto) throws Throwable;
    
   /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
   public List<DTOPresupuestos> getRegistroPresupuestos(Short ejercicioFiscal);
}
