/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPlantillasEBExcel {
    
    /**
     * Actualiza los catálogos de la plantilla de Matricula Inicial
     * @return  Devuelve la ruta de la plantilla actualizada
     * @throws Throwable 
     */
    public String getPlantillaMatriculaPeriodosEscolares() throws Throwable;
    
    /**
     * Actualiza los catálogos de la plantilla de eficiencia terminal
     * @return  Devuelve la ruta de la plantilla actualizada
     * @throws Throwable 
     */
    public String getPlantillaEficienciaTerminal() throws Throwable;
    
    /**
     * Actualiza los catálogos de la plantilla de distribución de equipamiento
     * @return Devuelve la ruta de la plantilla actualizada
     * @throws Throwable 
     */
    public String getPlantillaDistribucionEquipamiento() throws Throwable;
    
    /**
     * Actualiza los catálogos de la plantilla de distribución de instalaciones
     * @return  Devuelve la ruta de la plantilla actualizada
     * @throws Throwable 
     */
    public String getPlantillaDistribucionInstalaciones() throws Throwable;

}
