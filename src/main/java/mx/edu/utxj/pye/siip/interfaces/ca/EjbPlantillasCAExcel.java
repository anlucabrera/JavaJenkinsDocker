/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPlantillasCAExcel {

    public String getPlantillaProgPertCalidad() throws Throwable;

    public String getPlantillaEgetsu() throws Throwable;

    public String getPlantillaExani() throws Throwable;

    public String getPlantillaAcervo() throws Throwable;

    public String getPlantillaActFormacionIntegral() throws Throwable;

    public String getPlantillaRegistroMovilidad() throws Throwable;

    public String getPlantillaReconomicientoProdep() throws Throwable;

    /**
     * Actualiza los catálogos de la plantilla de registro de Cuerpos
     * Académicos.
     *
     * @return
     * @throws Throwable
     */
    public String getPlantillaCuerposAcademicos() throws Throwable;

    /**
     * Actualiza los catálogos de la plantilla de registro de Productos
     * Académicos.
     *
     * @return
     * @throws Throwable
     */
    public String getPlantillaProductosAcademicos() throws Throwable;
}
