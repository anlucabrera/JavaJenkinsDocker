/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.eb;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbReportesEBExcel {
    
    public String getReporteDistribucionEquipamiento(Short ejercicioFiscal) throws Throwable;
    
    public String getReporteMatriculaPorEjercicio(Short ejercicio) throws Throwable;
    
    public String getReportePorPeriodoEscolarDistribucionInstalaciones(Short ejercicio, AreasUniversidad areaUniversidad) throws Throwable;
    
}
