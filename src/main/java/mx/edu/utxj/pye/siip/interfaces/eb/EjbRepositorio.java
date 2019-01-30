/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbRepositorio {

    public List<String> getListaArchivosPorAreaEjeRegistroEjercicioMes(String ruta_principal);
}
