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
}
