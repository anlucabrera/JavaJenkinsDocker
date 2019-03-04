/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ch;

import javax.ejb.Local;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPlantillasCHExcel {
    
    public String getPlantillaComisionesAcademicas() throws Throwable;
    
    public String getPlantillaProgramasEstimulos() throws Throwable;
    
    public String getPlantillaPersonalCapacitado() throws Throwable;
    
}
