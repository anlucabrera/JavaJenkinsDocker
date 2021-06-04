/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.ch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateful;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Utils;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author UTXJ
 */
@Stateful
@MultipartConfig()
public class ServicioCarga implements EjbCarga {

    private static final long serialVersionUID = -28440987935768377L;

    @Getter
    @Setter
    private String aleatorio = "", nombreArchivo, extension;
    @Getter
    @Setter
    private Boolean caracteresEspeciales, valida;
    @Getter
    @Setter
    String[] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    @Override
    public String subir(Part file, File rutaRelativa) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/evidenciasCapitalHumano/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivoInico " + nombreArchivo);
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir()" + caracteresEspeciales);
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivo " + nombreArchivo);
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivoFinal " + nombreArchivo);
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
//                    System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() numero: " + numero);
                    aleatorio = aleatorio + abecedario[numero];
                }
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() aleatorio: " + aleatorio);
                String name = carpeta.toString().concat(File.separator).concat(aleatorio).concat(nombreArchivo);
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() name: " + name);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }

    @Override
    public String subirFotoPersonal(Part file, File rutaRelativa) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();

            String name = carpeta.toString().concat(File.separator).concat(nombreArchivo);
            FileOutputStream fos = new FileOutputStream(name);
            FileCopyUtils.copy(content, fos);
            aleatorio = "";
            return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }
    
    @Override
    public String subirEvidenciaPOA(Part file, File rutaRelativa) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivoInico " + nombreArchivo);
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir()" + caracteresEspeciales);
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivo " + nombreArchivo);
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
//            System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() nombreArchivoFinal " + nombreArchivo);
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
//                    System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() numero: " + numero);
                    aleatorio = aleatorio + abecedario[numero];
                }
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() aleatorio: " + aleatorio);
                String name = carpeta.toString().concat(File.separator).concat(aleatorio).concat(nombreArchivo);
//                System.out.println("mx.edu.utxj.pye.sgi.ch.servicio.ServicioCarga.subir() name: " + name);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }


    public static void addCarpetaRelativa(String carpetaRelativa) {
        File file = new File(carpetaRelativa);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public byte[] descargar(File rutaRelativa) {
        try {
            File file = new File("C:/archivos/evidenciasCapitalHumano/".concat(rutaRelativa.toString()));
            return Files.readAllBytes(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /************************ Métodos creados para la administración de archivos de los módulos de registro ***************************************************************************************/     
//    Valores que se ocuparán para generar una clave aleatoria al nombre del archivo
    String[] abecedarioMinusculas = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
//    Variables creadas para la subida de archivos de los Modulos de registro
    public static final String modulosRegistro = "modulos_registro";
    public static final String formatosTitulacion = "formatosTitulacion";
    public static final String plantillas = "plantillas";
    public static final String reportes = "reportes";
    public static final String completo = "completo";
    private static final String carpetaW = "C:\\archivos\\";
    private static final String carpetaL = "/home/admin/archivos/";
    public static String carpetaRaiz;
    public static final String formatosEscolares = "formatosEscolares";
    public static final String seguimientoEstadia = "seguimientoEstadia";
    public static final String reportesPlaneacion = "reportesPlaneacion";
    public static final String alineacionMaterias = "alineacionMaterias";

//    Método que se encarga de crear la carpeta raíz en caso de que no exista para poder almacenar el archivo
    static {
        carpetaRaiz = carpetaW;
        if (File.separatorChar == '/') {
            carpetaRaiz = carpetaL;
        }
        File file = new File(carpetaRaiz);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
    
//    Método que se encarga de generar la ruta del archivo concatenando los valores enviados desde el Ejb, la cual será ocupada para devolver posteriormenta la ruta del archivo
    public static String genCarpetaRelativa(String modulosRegistro, String ejercicio, String usuario, String eje, String registro) {
        return carpetaRaiz + modulosRegistro + File.separator + ejercicio + File.separator + usuario + File.separator + eje + File.separator + registro + File.separator;
    }
    
    public static String genCarpetaRelativa(String modulosRegistro, String ejercicio, String usuario, String eje, String mes, String registro) {
        return carpetaRaiz + modulosRegistro + File.separator + ejercicio + File.separator + usuario + File.separator + eje + File.separator + mes + File.separator + registro + File.separator;
    }
    
    public static String genCarpetaRelativa(String modulosRegistro, String plantillas, String eje){
        return carpetaRaiz + modulosRegistro + File.separator + plantillas + File.separator + eje + File.separator;
    }
    public static String genCarpetaRelativa(String modulosRegistro, String plantillas, String eje, String completo){
        return carpetaRaiz + modulosRegistro + File.separator + plantillas + File.separator + eje + File.separator + completo + File.separator;
    }
    
    public static String genCarpetaRelativaReporte(String modulosRegistro, String reportes, String eje){
        return carpetaRaiz + modulosRegistro + File.separator + reportes + File.separator + eje + File.separator;
    }
    
    public static String genCarpetaRelativaReporte(String modulosRegistro, String reportes, String eje, String completo){
        return carpetaRaiz + modulosRegistro + File.separator + reportes + File.separator + eje + File.separator + completo + File.separator;
    }
    
     public static String genCarpetaRelativa(String alineacionMaterias){
        return carpetaRaiz + alineacionMaterias + File.separator ;
    }
        
    @Override
    public String subirExcelRegistro(String ejercicio, String area, String eje, String registro, Part file) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            String rutaRelativa = genCarpetaRelativa(modulosRegistro, ejercicio, area, eje, registro);
            addCarpetaRelativa(rutaRelativa);
            String nombreArchivo1 = file.getSubmittedFileName().trim().toLowerCase();
            nombreArchivo1 = prettyURL(nombreArchivo1);

            for (int i = 1; i <= 10; i++) {
                int numero = (int) Math.round(Math.random() * 35);
                aleatorio = aleatorio + abecedarioMinusculas[numero];
            }

            LocalDate localDateOf = LocalDate.now();
            String name = rutaRelativa.concat(String.valueOf(localDateOf)).concat("_").concat(aleatorio).concat("_").concat(nombreArchivo1);
            FileOutputStream fos = new FileOutputStream(name);
            FileCopyUtils.copy(content, fos);
            aleatorio = "";

            return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }
    
    @Override
    public String subirExcelRegistroMensual(String ejercicio, String area, String eje, String mes, String registro, Part file) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            String rutaRelativa = genCarpetaRelativa(modulosRegistro, ejercicio, area, eje, mes, registro);
            addCarpetaRelativa(rutaRelativa.toLowerCase());
            String nombreArchivo2 = file.getSubmittedFileName().trim().toLowerCase();
            nombreArchivo2 = prettyURL(nombreArchivo2);

            for (int i = 1; i <= 10; i++) {
                int numero = (int) Math.round(Math.random() * 35);
                aleatorio = aleatorio + abecedarioMinusculas[numero];
            }

            LocalDate localDateOf = LocalDate.now();
            String name = rutaRelativa.concat(String.valueOf(localDateOf)).concat("_").concat(aleatorio).concat("_").concat(nombreArchivo2).toLowerCase();
            FileOutputStream fos = new FileOutputStream(name);
            FileCopyUtils.copy(content, fos);
            aleatorio = "";

            return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }

//    Método que se encarga de validar el nombre del archivo y en caso de contener caracteres especiales son remplazados por un guión medio
    public static String prettyURL(String string) {
        if (string == null) {
            return null;
        }

        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                /*.replaceAll("[^\\p{Alnum}]+", ".")*/
                .replaceAll("[^A-Za-z0-9-_.()]+", "-");
    }
    
    @Override
    public String crearDirectorioPlantilla(String eje) {
        String rutaRelativa = genCarpetaRelativa(modulosRegistro, plantillas, eje);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String crearDirectorioPlantillaCompleto(String eje) {
        String rutaRelativa = genCarpetaRelativa(modulosRegistro, plantillas, eje, completo);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String crearDirectorioReporte(String eje) {
        String rutaRelativa = genCarpetaRelativaReporte(modulosRegistro, reportes, eje);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String crearDirectorioReporteCompleto(String eje) {
        String rutaRelativa = genCarpetaRelativaReporte(modulosRegistro, reportes, eje, completo);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String subirDocExpTit(Part file, String tipoDoc, File rutaRelativa, String matricula) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(): " + rutaRelativa);
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/expedientesTitulacion/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
            //extrae la extension y el nombre de archivo por separado
            String tipo = file.getContentType();
                if ("image/jpg".equals(tipo) || "image/jpeg".equals(tipo)) {
                    extension = ".jpg";
                }
                else if("image/png".equals(tipo)){
                    extension = ".png";
                }
                if("application/pdf".equals(tipo)){
                    extension = ".pdf";
                }
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
                    aleatorio = aleatorio + abecedario[numero];
                }
                String name = carpeta.toString().concat(File.separator).concat(tipoDoc).concat("_").concat(matricula).concat(extension);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(fin)");
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }
    
    @Override
    public String subirTitExpTit(Part file, String tipoDoc, File rutaRelativa, String matricula) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(): " + rutaRelativa);
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/expedientesTitulacion/titulos/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
            //extrae la extension y el nombre de archivo por separado
            String tipo = file.getContentType();
                if ("image/jpg".equals(tipo) || "image/jpeg".equals(tipo)) {
                    extension = ".jpg";
                }
                else if("image/png".equals(tipo)){
                    extension = ".png";
                }
                if("application/pdf".equals(tipo)){
                    extension = ".pdf";
                }
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
                    aleatorio = aleatorio + abecedario[numero];
                }
                String name = carpeta.toString().concat(File.separator).concat(tipoDoc).concat("_").concat(matricula).concat(extension);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(fin)");
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }

    @Override
    public String crearDirectorioReporteCompletoTit(String generacion) {
        String rutaRelativa = genCarpetaRelativa(formatosTitulacion, reportes, generacion);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String crearDirectorioReporteDesercion(String periodoEscolar) {
        String rutaRelativa = genCarpetaRelativa(formatosEscolares, reportes, periodoEscolar);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String subirDocumentoAspirante(Part file, String tipoDoc, File rutaRelativa) {
        //        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(): " + rutaRelativa);
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("D:/archivos/aspirantes/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
            //extrae la extension y el nombre de archivo por separado
            String tipo = file.getContentType();
                if ("image/jpg".equals(tipo) || "image/jpeg".equals(tipo)) {
                    extension = ".jpg";
                }
                else if("image/png".equals(tipo)){
                    extension = ".png";
                }
                if("application/pdf".equals(tipo)){
                    extension = ".pdf";
                }
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
                    aleatorio = aleatorio + abecedario[numero];
                }
                String name = carpeta.toString().concat(File.separator).concat(tipoDoc).concat(extension);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(fin)");
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }

    @Override
    public String subirFotoFirmaEstudiante(Part file, File rutaRelativa) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/control_escolar/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();

            String name = carpeta.toString().concat(File.separator).concat(nombreArchivo);
            FileOutputStream fos = new FileOutputStream(name);
            FileCopyUtils.copy(content, fos);
            aleatorio = "";
            return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }

     @Override
    public String subirDocumentoEstadia(Part file, String tipoDoc, File rutaRelativa) {
        //        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(): " + rutaRelativa);
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            File carpeta = new File("C:/archivos/seguimientoEstadia/".concat(rutaRelativa.toString()));
            addCarpetaRelativa(carpeta.toString());
            nombreArchivo = file.getSubmittedFileName();
            //extrae la extension y el nombre de archivo por separado
            String tipo = file.getContentType();
                if ("image/jpg".equals(tipo) || "image/jpeg".equals(tipo)) {
                    extension = ".jpg";
                }
                else if("image/png".equals(tipo)){
                    extension = ".png";
                }
                if("application/pdf".equals(tipo)){
                    extension = ".pdf";
                }
            Pattern p = Pattern.compile("[^A-Za-z0-9-_.() ]+");
            Matcher m = p.matcher(nombreArchivo);
            StringBuffer sb = new StringBuffer();
            valida = m.find();
            while (valida) {
                m.appendReplacement(sb, "");
                valida = m.find();
            }
            m.appendTail(sb);
            nombreArchivo = sb.toString();
                for (int i = 1; i <= 10; i++) {
                    int numero = (int) Math.round(Math.random() * 35);
                    aleatorio = aleatorio + abecedario[numero];
                }
                String name = carpeta.toString().concat(File.separator).concat(tipoDoc).concat(extension);
                FileOutputStream fos = new FileOutputStream(name);
                FileCopyUtils.copy(content, fos);
                aleatorio = ""; 
//                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServicioCarga.subirDocExpTit(fin)");
                return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }
    
    @Override
    public String crearDirectorioReportesEstadia(String generacion, String nivel) {
        String rutaRelativa = genCarpetaRelativa(seguimientoEstadia, reportes, generacion, nivel);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }

    @Override
    public String crearDirectorioReportePlaneacion(String periodoEscolar) {
        String rutaRelativa = genCarpetaRelativa(reportesPlaneacion, reportes, periodoEscolar);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }
    
    @Override
    public String crearDirectorioPlantillaAlineacionMaterias() {
        String rutaRelativa = genCarpetaRelativa(alineacionMaterias);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }
    
    @Override
    public String crearDirectorioPlantillaAlineacionMateriasCompleto(String plan, String programa) {
        String rutaRelativa = genCarpetaRelativa(alineacionMaterias, plantillas, programa, plan, completo);
        addCarpetaRelativa(rutaRelativa);
        return rutaRelativa;
    }
    
    @Override
    public String subirPlantillaAlineacionMaterias(String plan, String programa, Part file) {
        try {
            byte[] content = Utils.toByteArray(file.getInputStream());
            String rutaRelativa = genCarpetaRelativa(alineacionMaterias, programa, plan);
            addCarpetaRelativa(rutaRelativa);
            String nombreArchivo1 = file.getSubmittedFileName().trim().toLowerCase();
            nombreArchivo1 = prettyURL(nombreArchivo1);

            for (int i = 1; i <= 10; i++) {
                int numero = (int) Math.round(Math.random() * 35);
                aleatorio = aleatorio + abecedarioMinusculas[numero];
            }

            LocalDate localDateOf = LocalDate.now();
            String name = rutaRelativa.concat(String.valueOf(localDateOf)).concat("_").concat(aleatorio).concat("_").concat(nombreArchivo1);
            FileOutputStream fos = new FileOutputStream(name);
            FileCopyUtils.copy(content, fos);
            aleatorio = "";

            return name;
        } catch (IOException ex) {
            Logger.getLogger(ServicioCarga.class.getName()).log(Level.SEVERE, null, ex);
            return "Error: No se pudo leer el archivo";
        }
    }
}
