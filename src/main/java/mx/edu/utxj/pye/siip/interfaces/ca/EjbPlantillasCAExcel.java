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

    /**
    * Actualiza los catálogos de la plantilla de registro de Programas Pertinentes y de Calidad
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaProgPertCalidad() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de EGETSU
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaEgetsu() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de EXANI
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaExani() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de Acervo Bibliográfico
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaAcervo() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de Actividades de Formación Integral
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaActFormacionIntegral() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de Actividades de Formación Integral
    * @param actForIntClave
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaPartActFormacionIntegral(String actForIntClave) throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de Movilidad
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaRegistroMovilidad() throws Throwable;
    /**
    * Actualiza los catálogos de la plantilla de registro de Reconocimiento PRODEP
    * @return  Ruta del archivo de la plantilla
    * @throws Throwable
    */
    public String getPlantillaReconomicientoProdep() throws Throwable;

    /**
     * Actualiza los catálogos de la plantilla de registro de Cuerpos
     * Académicos.
     *
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable
     */
    public String getPlantillaCuerposAcademicos() throws Throwable;

    /**
     * Actualiza los catálogos de la plantilla de registro de Productos
     * Académicos.
     *
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable
     */
    public String getPlantillaProductosAcademicos() throws Throwable;
    
    /**
     * Actualiza los catálogos de la plantilla de asesorías y tutorías
     *
     * @param area
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable
     */
    public String getPlantillaAsesoriasTutorias(Short area) throws Throwable;
    
    /**
     * Obtiene la plantilla de actividades varias, no aplica actualización de catálogos
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable 
     */
    public String getPlantillaActividadesVarias() throws Throwable;
    
    /**
     * Actualiza los catálogos de la plantilla de estadías por estudiante
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable 
     */
    public String getPlantillaEstadiasPorEstudiante(Short area) throws Throwable;
    
    /**
     * Obtiene la plantilla de Servicios Enfermería
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable 
     */
    public String getPlantillaServiciosEnfermeria() throws Throwable;
    
    /**
     * Obtiene la plantilla de Registro de Becas
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable 
     */
    public String getPlantillaBecas() throws Throwable;
    
     /**
     * Obtiene la plantilla de Registro de Deserción Académica
     * @return  Ruta del archivo de la plantilla
     * @throws Throwable 
     */
    public String getPlantillaDesercion() throws Throwable;
    
    
    public String getReporteCuerposAcademicos() throws Throwable;
    
}