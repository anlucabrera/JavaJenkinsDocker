/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.ch;

import java.io.File;
import java.io.Serializable;
import javax.ejb.Local;
import javax.servlet.http.Part;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCarga extends Serializable {

    public String subir(Part file, File rutaRelativa);

    public String subirFotoPersonal(Part file, File rutaRelativa);

    public String subirEvidenciaPOA(Part file, File rutaRelativa);
    
    public byte[] descargar(File rutaRelativa);

    /**
     * Método de subida de archivos en el servidor para los módulos de registro
     * de información el cual recibe los siguiente parámetros:
     *
     * @param ejercicio Parámetro que clasifica el Año actual
     * @param area Parámetro que clasifica Área que esta subiendo el archivo
     * @param eje Parámetro que clasifica Eje rector al cual pertenece la
     * información
     * @param registro Parámetro que clasifica en que carpeta se agregará el
     * archivo de excel
     * @param file Archivo de excel
     * @return Devuelve la ruta completa del archivo
     */
    public String subirExcelRegistro(String ejercicio, String area, String eje, String registro, Part file);

    /**
     * Método de subida de archivos por mes en el servidor para los módulos de
     * registro de información el cual recibe los siguiente parámetros:
     *
     * @param ejercicio Parámetro que clasifica el Año actual
     * @param area Parámetro que clasifica Área que esta subiendo el archivo
     * @param eje Parámetro que clasifica Eje rector al cual pertenece la
     * información
     * @param registro Parámetro que clasifica en que carpeta se agregará el
     * archivo de excel
     * @param mes Parámetro que clasifica por mes cada archivos subido en el
     * servidor
     * @param file Archivo de excel
     * @return Devuelve la ruta completa del archivo
     */
    public String subirExcelRegistroMensual(String ejercicio, String area, String eje, String mes, String registro, Part file);

    /**
     * Método que crea o comprueba si el directorio de las plantillas de los
     * Módulos de registro se encuentra disponible
     *
     * @param eje Es ocupado para crear o comprobar de que eje es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioPlantilla(String eje);

    /**
     * Método que crea o comprueba si el directorio de las plantillas
     * actualizadas de los Módulos de registro se encuentra disponible
     *
     * @param eje Es ocupado para crear o comprobar de que eje es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioPlantillaCompleto(String eje);
    
     /**
     * Método que crea o comprueba si el directorio de las plantillas de los Reportes
     * Módulos de registro se encuentra disponible
     *
     * @param eje Es ocupado para crear o comprobar de que eje es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReporte(String eje);

    /**
     * Método que crea o comprueba si el directorio de las plantillas de los Reportes
     * actualizadas de los Módulos de registro se encuentra disponible
     *
     * @param eje Es ocupado para crear o comprobar de que eje es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReporteCompleto(String eje);
    
     /**
     * Método de subida de archivos en el servidor para el módulo de titulación
     * el cual recibe los siguiente parámetros:
     *
     * @param file Archivo de excel
     * @param tipoDoc Parámetro que clasifica el tipo de documento del archivo
     * @param rutaRelativa Parámetro de la ruta relativa del archivo
     * @return Devuelve la ruta completa del archivo
     */
    public String subirDocExpTit(Part file, String tipoDoc, File rutaRelativa, String matricula);
    
     /**
     * Método de subida de archivos en el servidor para el módulo de titulación
     * el cual recibe los siguiente parámetros:
     *
     * @param file Archivo de excel
     * @param tipoDoc Parámetro que clasifica el tipo de documento del archivo
     * @param rutaRelativa Parámetro de la ruta relativa del archivo
     * @return Devuelve la ruta completa del archivo
     */
    public String subirTitExpTit(Part file, String tipoDoc, File rutaRelativa, String matricula);
    
    /**
     * Método que crea o comprueba si el directorio de los reportes de titulación
     * @param generacion Es ocupado para crear o comprobar de que generacion es el reporte que se está generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReporteCompletoTit(String generacion);
    
     /**
     * Método que crea o comprueba si el directorio de los reportes de deserción académica en control escolar
     * @param periodoEscolar Es ocupado para crear o comprobar de que periodo escolar es el reporte que se está generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReporteDesercion(String periodoEscolar);
    
     /**
     * Método de subida de archivos en el servidor para el módulo de titulación
     * el cual recibe los siguiente parámetros:
     *
     * @param file Archivo de excel
     * @param tipoDoc Parámetro que clasifica el tipo de documento del archivo
     * @param rutaRelativa Parámetro de la ruta relativa del archivo
     * @return Devuelve la ruta completa del archivo
     */
    public String subirDocumentoAspirante(Part file, String tipoDoc, File rutaRelativa);

    public String subirFotoFirmaEstudiante(Part file, File rutaRelativa);
    
    /**
     * Método de subida de archivos en el servidor para el módulo de seguimiento de estadía del estudiante
     * el cual recibe los siguiente parámetros:
     *
     * @param file Archivo de excel
     * @param tipoDoc Parámetro que clasifica el tipo de documento del archivo
     * @param rutaRelativa Parámetro de la ruta relativa del archivo
     * @return Devuelve la ruta completa del archivo
     */
    public String subirDocumentoEstadia(Part file, String tipoDoc, File rutaRelativa);
    
     /**
     * Método que crea o comprueba si el directorio de los reportes de seguimiento de estadía en control escolar
     * @param generacion Es ocupado para crear o comprobar de que generación es el reporte que se está generando
     * @param nivel Es ocupado para crear o comprobar de que nivel educativo es el reporte que se está generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReportesEstadia(String generacion, String nivel);
    
      /**
     * Método que crea o comprueba si el directorio de los reportes de deserción académica en control escolar
     * @param periodoEscolar Es ocupado para crear o comprobar de que periodo escolar es el reporte que se está generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioReportePlaneacion(String periodoEscolar);
    
     /**
     * Método que crea o comprueba si el directorio de las plantillas de los
     * Módulos de registro de evidencias e instrumentos de evaluación se encuentra disponible
     *
     * @param plan Es ocupado para crear o comprobar de que plan de estudio es la plantilla
     * @param programa Es ocupado para crear o comprobar de que carrera es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioPlantillaAlineacionMaterias(String plan, String programa);
    
     /**
     * Método que crea o comprueba si el directorio de las plantillas actualizadas de los
     * Módulos de registro de evidencias e instrumentos de evaluación se encuentra disponible
     *
     * @param plan Es ocupado para crear o comprobar de que plan de estudio es la plantilla
     * @param programa Es ocupado para crear o comprobar de que carrera es la plantilla
     * que se esta generando
     * @return Retorna la ruta del directorio
     */
    public String crearDirectorioPlantillaAlineacionMateriasCompleto (String plan, String programa);
}
