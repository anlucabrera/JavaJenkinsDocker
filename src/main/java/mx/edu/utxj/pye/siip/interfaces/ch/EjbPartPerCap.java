/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import javax.ejb.Local;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.caphum.DTOPerCapParticipantes;
/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPartPerCap {
    /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */ 
   public List<DTOPerCapParticipantes> getListaPartPerCap(String rutaArchivo) throws Throwable;
   
    /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param lista lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
   public void guardaPartPerCap(List<DTOPerCapParticipantes> lista, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
    /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param participantesPersonalCapacitado
     * @return entity.
     */
   public ParticipantesPersonalCapacitado getRegistroParticipantesPersonalCapacitado(ParticipantesPersonalCapacitado participantesPersonalCapacitado);
}
