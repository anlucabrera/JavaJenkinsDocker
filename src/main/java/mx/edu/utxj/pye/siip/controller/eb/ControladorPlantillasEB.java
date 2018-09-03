/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbPlantillasEBExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorPlantillasEB implements Serializable{
    
    private static final long serialVersionUID = 4672526767052124642L;
    
    @EJB
    EjbPlantillasEBExcel ejbPlantillasEBExcel;
    
    public void descargarPlantillaMatriculaPeriodoEscolar()throws IOException, Throwable{
        File f = new File(ejbPlantillasEBExcel.getPlantillaMatriculaPeriodosEscolares());
        Faces.sendFile(f, true);
    }
    
}
