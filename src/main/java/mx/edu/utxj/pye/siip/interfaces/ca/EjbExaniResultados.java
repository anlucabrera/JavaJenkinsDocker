/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.ExaniResultadosCiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.escolar.DTOExani;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbExaniResultados {
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
    public List<DTOExani> getListaExaniResultadosCiclosEscolares(String rutaArchivo) throws Throwable;
   
     /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
    public void guardaExaniResultadosCiclosEscolares(List<DTOExani> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
     /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param cicloEscolar Clave del ciclo escolar.
     * @param programaEducativo Clave del programa educativo.
     * @return entity.
     */
    public ExaniResultadosCiclosEscolares getRegistroExaniResultadosCiclosEscolares(Integer cicloEscolar, Short programaEducativo);
  
    /**
     * Obtiene la lista de registros filtrado por ciclo escolar seleccionado.
     * @param cicloEsc Ciclo escolar.
     * @return Lista de registros.
     */
    public List<DTOExani> filtroExani(Integer cicloEsc);
    
   /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<DTOExani> getRegistroReporteExani();
    
}
