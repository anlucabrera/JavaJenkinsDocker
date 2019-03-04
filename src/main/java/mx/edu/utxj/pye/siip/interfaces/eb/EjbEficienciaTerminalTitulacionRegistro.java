/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.dto.eb.DTOEficienciaTerminalTitulacionRegistro;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEficienciaTerminalTitulacionRegistro {
    
    public List<DTOEficienciaTerminalTitulacionRegistro> getListaEficienciaTerminalTitulacionRegistros(String rutaArchivo) throws Throwable;
    
    public void guardaEficienciaTerminalTitulacionRegistros(List<DTOEficienciaTerminalTitulacionRegistro> listaEficienciaTerminalTitulacionRegistro, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) throws Throwable;
    
    public EficienciaTerminalTitulacionRegistro getEficienciaTerminalTitulacionRegistro(EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro);
    
    /**
     * Método que se ocupa para el filtrado de Eficiencia Terminal por Ejercicio, Mes y área el cual es mostrado para consulta y eliminación
     * @param ejercicio Ejercicio actual que deberá venir de la tabla eventos_registro
     * @param mes   Mes actual que deberá venir de la tabla eventos_registro
     * @param area  Área superior de cada usuario logueado
     * @return Regresa una lista de registros de DTOEficienciaTerminalTitulacionRegistro que serán ocupados para consulta o eliminación
     */
    public List<DTOEficienciaTerminalTitulacionRegistro> getFiltroEficienciaTerminalEjercicioMesArea(Short ejercicio, String mes, Short area);
}
