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
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.siip.dto.escolar.DTOAsesoriasTutoriasCicloPeriodos;
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
                                         /*3*/"proceso_administrativo", 
                                         /*4*/"capital_humano"};
    
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
     * @param categoria Categoría de la subida
     * @return 
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
    
    public static String almacenarEvidenciaRegistro(AreasUniversidad area, DTOAsesoriasTutoriasCicloPeriodos registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
        }
        
        String ruta = ServicioArchivos.genRutaRelativa(
                String.valueOf(registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
                area.getSiglas(),
                registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getEventoRegistro().getMes(),
                registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getTipo().getNombre());
        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getAsesoriasTutoriasCicloPeriodos().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
        
        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
        
        //si el archivo se agrega un contador extra
        Integer cont = 1;
        while(Files.exists(Paths.get(rutaAbsoluta))){
            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
            cont++;
        }
        
        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
                
        archivo.write(rutaArchivo);
        return rutaAbsoluta;
    }
    
//    /**
//     * Almacena la evidencia de los registros de becas por periodo
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroBP(AreasUniversidad area, ListaBecasDto registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getBecasPeriodosEscolares().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getBecasPeriodosEscolares().getRegistros().getEventoRegistro().getMes(),
//                registro.getBecasPeriodosEscolares().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getBecasPeriodosEscolares().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de los registros de la difusion de las iems
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroDI(AreasUniversidad area, ListaDifusionIemsDTO registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getDifusion().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getDifusion().getRegistros().getEventoRegistro().getMes(),
//                registro.getDifusion().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getDifusion().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de los registros de matricula
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroMPE(AreasUniversidad area, ListaDtoMatricula registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getMatricula().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getMatricula().getRegistros().getEventoRegistro().getMes(),
//                registro.getMatricula().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getMatricula().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    
//    /**
//     * Almacena la evidencia de visitas industriales ligadas a la tabla de  visitas industriales
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroVINDUSTRIALES(AreasUniversidad area, ListaDtoVisitasIndustriales registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getVisitasIndustriales().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getVisitasIndustriales().getRegistros().getEventoRegistro().getMes(),
//                registro.getVisitasIndustriales().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getVisitasIndustriales().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de ligada a los registros de la tabla de desercion por periodos
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroDesPer(AreasUniversidad area, ListaDtoDesercion registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getDesercionPeriodosEscolares().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getDesercionPeriodosEscolares().getRegistros().getEventoRegistro().getMes(),
//                registro.getDesercionPeriodosEscolares().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getDesercionPeriodosEscolares().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de deseercion
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroDesRep(AreasUniversidad area, ListaDtoReprobacion registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getDesercionReprobacionMaterias().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getDesercionReprobacionMaterias().getRegistros().getEventoRegistro().getMes(),
//                registro.getDesercionReprobacionMaterias().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getDesercionReprobacionMaterias().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de las ferias profesiograficas
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroFProfes(AreasUniversidad area, ListaFeriasDTO registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getFeriasProfesiograficas().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getFeriasProfesiograficas().getRegistros().getEventoRegistro().getMes(),
//                registro.getFeriasProfesiograficas().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getFeriasProfesiograficas().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
//    /**
//     * Almacena la evidencia de los participantes en las ferias profesiograficas
//     * @param area
//     * @param registro
//     * @param archivo
//     * @return
//     * @throws IOException
//     * @throws EvidenciaRegistroExtensionNoValidaException 
//     */
//    public static String almacenarEvidenciaRegistroFPart(AreasUniversidad area, ListaFeriasParticipantesDTO registro, Part archivo) throws IOException, EvidenciaRegistroExtensionNoValidaException{        
//        if(!extensiones.contains(FilenameUtils.getExtension(archivo.getSubmittedFileName()).toLowerCase())){
//            throw new EvidenciaRegistroExtensionNoValidaException(archivo.getSubmittedFileName());
//        }
//        
//        String ruta = ServicioArchivos.genRutaRelativa(
//                String.valueOf(registro.getFeriasParticipantes().getRegistros().getEventoRegistro().getEjercicioFiscal().getAnio()), //ejercicio fiscal
//                area.getSiglas(),
//                registro.getFeriasParticipantes().getRegistros().getEventoRegistro().getMes(),
//                registro.getFeriasParticipantes().getRegistros().getTipo().getNombre());
//        System.out.println("mx.edu.utxj.pye.sgi.util.ServicioArchivos.almacenarEvidenciaRegistro(" + registro.getFeriasParticipantes().getRegistros().getTipo().getNombre() + ") ruta: " + ruta);
//        
//        String nombreArchivo = sdf.format(new Date()).concat("_").concat(archivo.getSubmittedFileName());
//        String rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//        String rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//        
//        //si el archivo se agrega un contador extra
//        Integer cont = 1;
//        while(Files.exists(Paths.get(rutaAbsoluta))){
//            nombreArchivo = sdf.format(new Date()).concat("_").concat(cont.toString()).concat("_").concat(archivo.getSubmittedFileName());
//            rutaArchivo = ruta.concat(StringUtils.quitarEspacios(StringUtils.quitarAcentos(nombreArchivo)));
//            rutaAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivo);
//            cont++;
//        }
//        
//        ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(ruta));
//                
//        archivo.write(rutaArchivo);
//        return rutaAbsoluta;
//    }
}
