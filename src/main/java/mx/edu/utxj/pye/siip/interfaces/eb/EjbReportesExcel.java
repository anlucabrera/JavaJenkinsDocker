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
    public String getReporteIngPropios() throws Throwable;
    
    /**
    * Genera reporte de registro de Presupuestos
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReportePresupuestos() throws Throwable;
    
    /**
    * Genera reporte de registro de Difusión IEMS
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteDifusion() throws Throwable;
    
    /**
    * Genera reporte de registro de Visitas Industriales
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteVisitas() throws Throwable;
    
    /**
    * Genera reporte de registro de Becas
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteBecas() throws Throwable;
    
    /**
    * Genera reporte de registro de Actividades de Formación Integral - Participantes
    * @param claveArea
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteAFI(Short claveArea) throws Throwable;
    
    /**
    * Genera reporte de registro de Deserción Académica - Reprobación
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteDesercion() throws Throwable;
    
    /**
    * Genera reporte de registro de Personal Capacitado - Participantes
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReportePerCap() throws Throwable;
    
    /**
    * Genera reporte de registro de Ferias Profesionales - Participantes
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteFerProf() throws Throwable;
   
    /**
    * Genera reporte de registro de Movilidad Académica y Docente
    * @param claveArea
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteMov(Short claveArea) throws Throwable;
    
    /**
    * Genera reporte de registro de Bolsa de Trabajo - Entrevistas
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteBolTrab() throws Throwable;
    
    /**
    * Genera reporte de registro de Acervo Bibliográfico
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteAcervo() throws Throwable;
    
    /**
    * Genera reporte de registro de Reconocimiento PRODEP
    * @return  Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteRecProdep() throws Throwable;
    
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
