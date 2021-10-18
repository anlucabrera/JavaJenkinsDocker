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
public interface EjbReportesExcel {
    
    /**
    * Genera reporte de registro de Ingresos Propios
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteIngPropios(Short ejercicioFiscal) throws Throwable;
    
    /**
    * Genera reporte de registro de Presupuestos
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReportePresupuestos(Short ejercicioFiscal) throws Throwable;
    
    /**
    * Genera reporte de registro de Difusión IEMS
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteDifusion(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Visitas Industriales
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteVisitas(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Becas
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteBecas(Short ejercicioFiscal) throws Throwable;
    
    /**
    * Genera reporte de registro de Actividades de Formación Integral - Participantes
    * @param claveArea
     * @param ejercicio
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteAFI(Short claveArea, Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Deserción Académica - Reprobación
     * @param ejercicio
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteDesercion(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Personal Capacitado - Participantes
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReportePerCap(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Ferias Profesionales - Participantes
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteFerProf(Short ejercicio) throws Throwable;
   
    /**
    * Genera reporte de registro de Movilidad Académica y Docente
    * @param claveArea
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteMov(Short claveArea, Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Bolsa de Trabajo - Entrevistas
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteBolTrab(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Acervo Bibliográfico
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteAcervo(Short ejercicio) throws Throwable;
    
    /**
    * Genera reporte de registro de Reconocimiento PRODEP
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteRecProdep(Short ejercicio, Short area) throws Throwable;
    
    /**
    * Genera reporte de registro de Comisiones Académicas y Participantes
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteComAcad() throws Throwable;
    
    /**
    * Genera reporte de registro de Programas de Estímulos
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteProgEst() throws Throwable;
    
    /**
    * Genera reporte de registro de Resultados de Egetsu
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteEgetsu() throws Throwable;
    
    /**
    * Genera reporte de registro de Resultados de Exani
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteExani() throws Throwable;
    
}
