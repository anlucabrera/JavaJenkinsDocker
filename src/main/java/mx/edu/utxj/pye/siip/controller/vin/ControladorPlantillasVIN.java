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
    
    public void descargarPlantillaSatisfaccionServTecEduCont() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaSatisfaccionServTecEduCont());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaEvaluacionSatisfaccionServTecEduCont() throws IOException, Throwable {
        File f = new File(ejbPlantillasVINExcel.getPlantillaEvaluacionSatisfaccionServTecEduCont());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteConvenios(Short ejercicioFiscal) throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteConvenios(ejercicioFiscal));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteServiciosTecnologicos(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteServiciosTecnologicos(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteEgresados(Short ejercicio, Integer claveTrabajador) throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteGeneralEgresados(ejercicio,claveTrabajador));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteOrganismosVinculados(Short ejercicio) throws Throwable{
        File f = new File(ejbReportesVINExcel.getReporteOrganismosVinculados(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteSatisfaccionServTecEduCont(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteSatisfaccionServTecEduCont(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteEvaluacionSatisfaccionEduCont(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejbReportesVINExcel.getReporteEvaluacionSatisfaccionEduCont(ejercicio));
        Faces.sendFile(f, true);
    }
    
}
