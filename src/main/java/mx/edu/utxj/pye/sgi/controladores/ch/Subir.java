/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author UTXJ
 */
@Named(value = "subir")
@SessionScoped
public class Subir implements Serializable {

    private static final long serialVersionUID = -749807042912528766L;

    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    @EJB    EjbCarga carga;

    public void upload() {
        ruta = carga.subir(file, new File("203".concat(File.separator).concat("files").concat(File.separator)));
//        System.out.println("mx.edu.utxj.pye.subir.controlador.Subir.upload() res:" + ruta);

        content = new DefaultStreamedContent(new ByteArrayInputStream(carga.descargar(new File("203".concat(File.separator).concat("files").concat(File.separator).concat(file.getSubmittedFileName())))), "application/pdf");
    }
}
