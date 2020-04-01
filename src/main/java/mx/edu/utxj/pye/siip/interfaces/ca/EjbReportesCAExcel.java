/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.ca;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbReportesCAExcel {
    
    public String getReporteActividadesVarias(Short ejercicioFiscal) throws Throwable;
    
    public String getReporteActividadesVarias(Short ejercicioFiscal, Short area) throws Throwable;
    
    public String getReporteAsesoriasTutorias(Short ejercicio_fiscal) throws Throwable;
    
    public String getReporteAsesoriasTutorias(Short ejercicio_fiscal, Short area) throws Throwable;
    
    public String getReporteServiciosEnfermeria(Short ejercicio_fical) throws Throwable;
    
    public String getReporteCompletoCuerposAcademicos(Short ejercicio)  throws Throwable;
    
    public String getReporteCompletoProductosAcademicos(Short ejercicio) throws Throwable;
    
}
