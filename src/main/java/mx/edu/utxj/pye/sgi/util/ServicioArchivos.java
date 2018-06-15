/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Jonny
 */
public class ServicioArchivos implements Serializable{
    private static final long serialVersionUID = 3938477944120494089L;
    public static String carpetaRaiz;
    private static final String carpetaW = "C:\\archivos\\";
    private static final String carpetaL = "/home/admin/archivos/";
    public static final String[] CATEGORIAS = {"cargas","fotos"};
    
    static{
        carpetaRaiz = carpetaW;
        if (File.separatorChar == '/') {
            carpetaRaiz = carpetaL;
        }
        File file = new File(carpetaRaiz);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
    public static String subir(String usuario, String categoria, UploadedFile archivo) throws IOException {
            //Se crea la ruta relativa para la carpeta contenedora del archivo en carga.
            String rutaRelativa = genCarpetaRelativa(usuario, categoria);//ServicioEvidencia.genCarpetaRelativa(carpetaRaiz, poa, area, mes, categoria);
            addCarpetaRelativa(rutaRelativa);
            String nombreArchivo = archivo.getFileName().trim().toLowerCase();
            InputStream is = archivo.getInputstream();
            String ext = FilenameUtils.getExtension(nombreArchivo);// .getSubmittedFileName());
            Path file = Files.createTempFile(Paths.get(rutaRelativa), nombreArchivo.split("\\.")[0], "." + ext);
            System.out.println("jvv.aldesa.sgot.util.ServicioArchivos.subir(): " + nombreArchivo + "-->" + file.toString());
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            is.close();
            
            return file.toString();
    }
    
    public static Path subirFoto(String usuario, String categoria, String folio, UploadedFile archivo) throws IOException {
            //Se crea la ruta relativa para la carpeta contenedora del archivo en carga.
            String rutaRelativa = genCarpetaRelativa(usuario, categoria, folio);//ServicioEvidencia.genCarpetaRelativa(carpetaRaiz, poa, area, mes, categoria);
            addCarpetaRelativa(rutaRelativa);
            String nombreArchivo = archivo.getFileName().trim().toLowerCase();
            InputStream is = archivo.getInputstream();
            String ext = FilenameUtils.getExtension(nombreArchivo);// .getSubmittedFileName());
            Path file = Files.createTempFile(Paths.get(rutaRelativa), nombreArchivo.split("\\.")[0], "." + ext);
            System.out.println("jvv.aldesa.sgot.util.ServicioArchivos.subirFoto(): " + nombreArchivo + "-->" + file.toString());
            Files.copy(is, file, StandardCopyOption.REPLACE_EXISTING);
            is.close();
            
            return file;
    }
    
    public static void eliminarArchivo(String ruta){
        try {
            Path path = Paths.get(ruta);
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            Logger.getLogger(ServicioArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
           
    
    /**
     * Genera la carpeta relativa, la cual se compone de la carpeta raíz y los
     * parámetros de los archivos. Se utiliza para definir la ruta de la carpeta
     * contenedora del archivo que se va a cargar o descargar.
     *
     * @param usuario Usuario que realiza el almacenamiento del archivo.
     * @param categoria Categoría de la evidencia.
     * @return Regresa la cadena que compone a la ruta relativa.
     */
    public static String genCarpetaRelativa(String usuario, String categoria) {
        return carpetaRaiz + usuario.trim().toLowerCase() + File.separator + categoria + File.separator;
    }
    public static String genCarpetaRelativa(String usuario, String categoria,String folio) {
        return carpetaRaiz + usuario.trim().toLowerCase() + File.separator + categoria + File.separator + folio + File.separator;
    }
    /**
     * Se encarga de verificar si la carpeta relativa existe, en caso contrario
     * la crea. <br/>
     * Se utiliza para el aseguramiento de la existencia de la carpeta
     * contenedora cuando se carga un archivo.
     *
     * @param carpetaRelativa Ruta de la carpeta relativa, generalmente se
     * obtiene mediante el método String genCarpetaRelativa
     */
    public static void addCarpetaRelativa(String carpetaRelativa) {
        File file = new File(carpetaRelativa);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
