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
public interface EjbReportesCAExcel {
    
    public String getReporteActividadesVarias() throws Throwable;
    
    public String getReporteAsesoriasTutorias() throws Throwable;
    
    public String getReporteServiciosEnfermeria() throws Throwable;
    
    public String getReporteCompletoCuerposAcademicos()  throws Throwable;
    
    public String getReporteCompletoProductosAcademicos() throws Throwable;
    
}
