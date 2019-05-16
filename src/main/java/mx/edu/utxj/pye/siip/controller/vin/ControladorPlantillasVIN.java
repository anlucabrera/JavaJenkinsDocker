/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbReportesVINExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorPlantillasVIN implements Serializable {

    private static final long serialVersionUID = 2726149017850038684L;

    @EJB    EjbPlantillasVINExcel   ejbPlantillasVINExcel;
    @EJB    EjbReportesVINExcel     ejbReportesVINExcel;

    public void descargarPlantillaConvenios() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaConvenios());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaServTec() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaServiciosTecnologicos());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaOrgVin() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaOrganismosVinculados());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaEgresados() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaEgresados());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteConvenios() throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteConvenios());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteServiciosTecnologicos() throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteServiciosTecnologicos());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteEgresados() throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteGeneralEgresados());
        Faces.sendFile(f, true);
    }
    
}
