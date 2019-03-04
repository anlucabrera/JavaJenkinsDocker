package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class ZipWritter {

    @Getter
    @NonNull
    private final String salida;
    @Getter
    @NonNull
    private String root;
    @Getter
    private final ArrayList<File> contenidos = new ArrayList<>();
    @Getter
    private final ArrayList<String> nombres = new ArrayList<>();
    @Getter
    private ZipParameters parameters;
    @Getter
    private ZipFile zipFile;

    byte[] buffer = new byte[1024];

    /**
     * 
     * @param salida Debe ser la ruta de salida del archivo zip a crear con la extensión .zip
     * @param root Ruta raiz interna del archivo zip, puede ser /
     */
    public ZipWritter(String salida, String root) {
        this.salida = salida.trim();
        this.root = root.trim();
        if (this.root.equals("")) {
            this.root = "zip";
        }
    }

    public void addArchivo(String ruta) {
//        String url = "http://sgot.com.mx:8080/sgot-images/".concat(ruta);

        contenidos.add(new File(ruta));
    }

    public void generarZip() {
        try {
            parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setRootFolderInZip(root.concat("/"));
//            ("jvv.aldesa.sgot.util.ZipWritter.generarZip() salida: " + salida);
            CargaArchivosCH.eliminarArchivo(salida);
            zipFile = new ZipFile(salida);

            int i = 0;
            for (File f : contenidos) {
//                ("jvv.aldesa.sgot.util.ZipWritter.generarZip() f: " + f.toString());
                if (f.exists()) {
                    if (f.isFile()) {
                        zipFile.addFile(f, parameters);
                    } else if (f.isDirectory()) {
//                        zipFile.addFolder(f, parameters);
                        final String nombre = contenidos.get(i).toString();
                        try (Stream<Path> paths = Files.walk(Paths.get(f.toString()))) {
                            paths.filter(Files::isRegularFile).forEach((Path path) -> {
//                                ("jvv.aldesa.sgot.util.ZipWritter.generarZip() path: " + path);
                                parameters.setRootFolderInZip(root.concat("/").concat(nombre).concat("/"));
                                try {
                                    zipFile.addFile(path.toFile(), parameters);
                                } catch (ZipException ex) {
                                    Logger.getLogger(ZipWritter.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                        } catch (IOException ex) {
                            Logger.getLogger(ZipWritter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }                
                i++;
            }
        } catch (ZipException ex) {
            Logger.getLogger(ZipWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String salida = "G:\\Jonny\\archivos\\zip.zip";
        ZipWritter zw = new ZipWritter(salida, "zip");
        zw.addArchivo("G:\\Jonny\\archivos\\f09.xlsx");
        zw.addArchivo("G:\\Jonny\\archivos\\gimsa\\fotos\\P00067");
        zw.addArchivo("G:\\Jonny\\archivos\\eca\\fotos\\P07170");
        zw.addArchivo("G:\\Jonny\\archivos\\gimsa\\fotos\\P00068");
        zw.generarZip();
    }

    public static File generar(List<String> sustituciones, Integer clave) {
        ZipWritter zw = new ZipWritter("C:\\archivos\\evidenciasCapitalHumano\\zips\\"+clave+".zip", clave.toString());
        sustituciones.forEach((t) -> {
            File f = new File(t);
            zw.addArchivo(t);            
        });
        zw.generarZip();

        return zw.getZipFile().getFile();
    }
}
