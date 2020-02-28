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
    
    public String getPlantillaServiciosTecnologicos() throws Throwable;
    
    public String getPlantillaOrganismosVinculados() throws Throwable;
    
    public String getPlantillaBolsaTrabajo() throws Throwable;
    
    public String getPlantillaEgresados() throws Throwable;
    
    public String getPlantillaVisitasIndustriales() throws Throwable;
    
    public String getPlantillaDifusionIEMS() throws Throwable;
    
    public String getPlantillaFeriasProfesiograficas() throws Throwable;
    
    public String getPlantillaIEMS() throws Throwable;
}