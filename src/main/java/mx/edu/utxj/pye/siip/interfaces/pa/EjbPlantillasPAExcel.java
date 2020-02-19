/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.pa;

import javax.ejb.Local;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPlantillasPAExcel {
    
    /**
    * Actualiza los catálogos de la plantilla de registro de Presupuestos
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaPresupuestos() throws Throwable;
    
    /**
    * Actualiza los catálogos de la plantilla de registro de Ingresos Propios
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaIngPropios() throws Throwable;
    
}
