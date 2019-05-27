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
public interface EjbReportesVINExcel {
    
    public String getReporteConvenios() throws Throwable;
    
    public String getReporteServiciosTecnologicos() throws Throwable;
    
    public String getReporteGeneralEgresados() throws Throwable;
    
    public String getReporteOrganismosVinculados() throws Throwable;
    
}
