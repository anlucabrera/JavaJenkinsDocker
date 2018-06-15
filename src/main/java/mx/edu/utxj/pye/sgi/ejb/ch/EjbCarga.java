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
public interface EjbCarga extends Serializable{
    public String subir(Part file, File rutaRelativa);
    
    public byte[] descargar(File rutaRelativa);
}
