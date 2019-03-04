/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
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
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmm");
    public static List<String> extensiones = Arrays.asList("pdf","jpg","jpeg","png");
    /**
     * Variable creada para la creación del directorio para los Módulos de Registro
     */
    public static final String[] EJES = {/*0*/"estadistica_basica", 
                                         /*1*/"calidad_academica", 
                                         /*2*/"vinculacion", 
                                         /*3*/"gestion_institucional",
                                         /*4*/"talento_humano"};
    
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
    
    /**
     * Genera una ruta relativa por año, para subida de datos atraves de técnica servlet y alojar en una carpeta denominada archivos ubicada sobre la carpeta raíz     * 
     * @param categoria Categoría de la subida, para subida de datos atraves de técnica servlet y alojar en una carpeta denominada archivos ubicada sobre la carpeta raíz
     * @return Ruta resultante
     */
    public static String genRutaRelativa(String categoria){
        return categoria.concat(File.separator).concat(Year.now().toString()).concat(File.separator);
    }
    
    /**
     * Genera una ruta relativa por area, mes y nombre de registro,
     * @param ejercicioFiscal Ejercicio fiscal del evento del registro
     * @param area Siglas del area que registra en minúscula
     * @param mes Nombre del mes completo en minúscula en el que se registra
     * @param registro Nombre del registro sin acentos, sin espacios y en minúsculas
     * @return Ruta relativa resultante
     */
    public static String genRutaRelativa(String ejercicioFiscal, String area, String mes, String registro){
        //anio,area,mes,registro
        return ejercicioFiscal.trim().concat(File.separator)
                .concat(area.toLowerCase().trim()).concat(File.separator)
                .concat(mes.toLowerCase().trim()).concat(File.separator)
                .concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(registro.toLowerCase()))).concat(File.separator);
    }

    /**
     * Genera una copia del archivo, si la ubicación de la copia ya existe, lo sobre escribe.
     * @param original Ruta del archivo original
     * @param copia Ruta de la copia del archivo.
     * @return Path del archivo destino.
     */
    public static Path copiarArchivo(File original, File copia){
        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.copiarArchivo() original: " + original);
        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.copiarArchivo() copia: " + copia);
        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.copiarArchivo() existe original: " + (original.exists()));
        if(original.exists()){
            try {
                return Files.copy(original.toPath(), copia.toPath());
            } catch (IOException ex) {
                Logger.getLogger(ServicioArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        return null;
    }

    public static String almacenarEvidenciaRegistroGeneral(AreasUniversidad area, Registros registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{
        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
        }
        String ruta = ServicioArchivos.genRutaRelativa(
                String.valueOf(registro.getEventoRegistro().getEjercicioFiscal().getAnio()),
                area.getSiglas(),
                registro.getEventoRegistro().getMes(),
                registro.getTipo().getNombre());

        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(StringUtils.prettyURL(nombreArchivo.toLowerCase()))));
        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);

        Integer cont = 1;
        while (Files.exists(Paths.get(rutaAbsoluta))) {
            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(StringUtils.prettyURL(nombreArchivo.toLowerCase()))));
            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
            cont++;
        }
        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
        archivo.write(rutaArchivo);
        return rutaAbsoluta;
    }

    /**
     * Almacena la evidencia de los Registros de SII
     * @param area
     * @param registro
     * @param archivo
     * @param eventosRegistros
     * @param tipo
     * @return
     * @throws IOException
     * @throws EvidenciaRegistroExtensionNoValidaException
     */
    public static String almacenarEvidenciaRegistroSII(AreasUniversidad area, Integer registro, Part archivo, EventosRegistros eventosRegistros, RegistrosTipo tipo) throws IOException, EvidenciaRegistroExtensionNoValidaException{
        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
        }

        String ruta = ServicioArchivos.genRutaRelativa(
                String.valueOf(eventosRegistros.getEjercicioFiscal().getAnio()), //ejercicio fiscal
                area.getSiglas(),
                eventosRegistros.getMes(),
                tipo.getNombre());

        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(StringUtils.prettyURL(nombreArchivo.toLowerCase()))));
        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);

        //si el archivo se agrega un contador extra
        Integer cont = 1;
        while(Files.exists(Paths.get(rutaAbsoluta))){
            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
            StringUtils.prettyURL(nombreArchivo.toLowerCase());
            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
            cont++;
        }

        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));

        archivo.write(rutaArchivo);
        return rutaAbsoluta;
    }

    public static void escribeLog(List<String> datosInvalidos) {
        FileWriter fichero = null;
        try {

            fichero = new FileWriter("C:\\archivos\\modulos_registro\\logs\\calidad_academica\\actividades_varias.txt");

            // Escribimos linea a linea en el fichero159
            for (String linea : datosInvalidos) {
                fichero.write(linea + "\n");
            }

            fichero.close();

        } catch (IOException ex) {
            System.out.println("Mensaje de la excepción: " + ex.getMessage());
        }
    }

}
