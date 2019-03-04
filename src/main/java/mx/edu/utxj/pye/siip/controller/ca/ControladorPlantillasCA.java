/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorPlantillasCA implements Serializable{
    
    private static final long serialVersionUID = 3793691115996192343L;
    
    @EJB
    EjbPlantillasCAExcel ejbPlantillasCAExcel;
    
    public void descargarPlantillaCuerposAcademicos() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaCuerposAcademicos());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaProductosAcademicos() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaProductosAcademicos());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaAsesoriasTutorias(Short area) throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaAsesoriasTutorias(area));
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaActVar() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaActividadesVarias());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaEstadias(Short area) throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaEstadiasPorEstudiante(area));
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaServiciosEnfermeria() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaServiciosEnfermeria());
        Faces.sendFile(f, true);
    }
    
}
