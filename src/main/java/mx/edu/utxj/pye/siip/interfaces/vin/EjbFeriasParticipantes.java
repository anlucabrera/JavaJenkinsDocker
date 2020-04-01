/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.FeriasParticipantes;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantes;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaFeriasParticipantesDTO;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbFeriasParticipantes {
   
     /**
     * Obtiene la lista de registros leídos del archivo de Excel.
     * @param rutaArchivo ruta en la que se guarda el archivo.
     * @return Lista de registros que se desean subir.
     * @throws java.lang.Throwable
     */
    public ListaFeriasParticipantes getListaFeriasParticipantes(String rutaArchivo) throws Throwable;
   
     /**
     * Método que guarda registros del archivo de Excel en la tabla de la base de datos.
     * @param listaFeriasParticipantes lista de registros que se guardaran.
     * @param registrosTipo tipo de registro.
     * @param ejesRegistro eje al que pertenece el registro.
     * @param area area que está registrando.
     * @param eventosRegistros evento de registro vigente.
     */
    public void guardaFeriasParticipantes(ListaFeriasParticipantes listaFeriasParticipantes, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros);
   
     /**
     * Método que verifica si existe otro registro en la tabla de la base de datos con la misma información.
     * @param feriasParticipantes.
     * @return entity.
     */
    public FeriasParticipantes getRegistroFeriasParticipantes(FeriasParticipantes feriasParticipantes);
    
     /**
     * Obtiene la lista de registros filtrado por ejercicio fiscal y mes seleccionado.
     * @param ejercicio Ejercicio fiscal.
     * @param mes Mes del ejercicio fiscal.
     * @return Lista de registros.
     */
    public List<ListaFeriasParticipantesDTO> getRegistrosFParticipantes( String mes, Short ejercicio);
    
      /**
     * Obtiene la lista de registros actuales del registro
     * @return Lista de registros
     */
    public List<ListaFeriasParticipantesDTO> getRegistroReportePartFerProf(Short ejercicio);
}

