/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import javax.ejb.Local;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPlantillasVINExcel {
    
    public String getPlantillaConvenios() throws Throwable;
    
    public String getPlantillaOrganismosVinculados() throws Throwable;
}