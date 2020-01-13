/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import java.util.List;
import mx.edu.utxj.pye.siip.dto.eb.DTOArchivoRepositorio;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbRepositorio {

    public List<DTOArchivoRepositorio> getListaArchivosPorAreaEjeRegistroEjercicioMes(Short ejercicio, String siglas, List<String> ejes, String mes, List<String> tiposRegistros);
}
