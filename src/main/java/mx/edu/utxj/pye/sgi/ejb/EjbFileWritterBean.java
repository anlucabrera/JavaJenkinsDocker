package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.apache.commons.io.FilenameUtils;

import javax.ejb.Stateless;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

@Stateless(name = "EjbFileWritterBean")
@MultipartConfig
public class EjbFileWritterBean {
    public ResultadoEJB<File> almacenarArchivo(String categoria, String nombreArchivo, Part file) {
        try {
            String rutaRelativa = ServicioArchivos.genRutaRelativa(categoria); //carpeta contendora relativa a la carpeta configurada como multipartconfig en web.xml
            String extension = FilenameUtils.getExtension(file.getSubmittedFileName()); //
            String rutaArchivoRelativa = rutaRelativa.concat(nombreArchivo).concat(".").concat(extension);
            String rutaArchivoAbsoluta = ServicioArchivos.carpetaRaiz.concat(rutaArchivoRelativa);
            ServicioArchivos.addCarpetaRelativa(ServicioArchivos.carpetaRaiz.concat(rutaRelativa));
            ServicioArchivos.eliminarArchivo(rutaArchivoAbsoluta);
            file.write(rutaArchivoRelativa);
            File f = new File(rutaArchivoAbsoluta);

            if(f.exists()){
                return ResultadoEJB.crearCorrecto(f, "La evidencia se registró correctamente."); //new ResultadoEJB<>(f,"",null,0);
            }else{
                return ResultadoEJB.crearErroneo(1,"El archivo no pudo almacenarse.",File.class); //new ResultadoEJB<>(f,"El archivo no pudo almacenarse.",null,1);
            }
        } catch (IOException e) {
            return ResultadoEJB.crearErroneo(2,"El archivo no pudo almacenarse.", e, File.class); //new ResultadoEJB<>(null,"El archivo no pudo almacenarse: " + e.getCause()!=null?e.getCause().getMessage():e.getMessage(),e,2);
        }
    }

}
