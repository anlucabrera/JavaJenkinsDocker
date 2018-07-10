/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.ch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateful;
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
public class ServicioCarga implements EjbCarga {

    private static final long serialVersionUID = -28440987935768377L;

    @Getter
    @Setter
    private String aleatorio = "", nombreArchivo;
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

}
