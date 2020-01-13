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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbPlantillasEBExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbReportesEBExcel;
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
    
    @EJB    EjbPlantillasEBExcel    ejbPlantillasEBExcel;
    @EJB    EjbReportesEBExcel      ejbReportesEBExcel;
    
    public void descargarPlantillaMatriculaPeriodoEscolar()throws IOException, Throwable{
        File f = new File(ejbPlantillasEBExcel.getPlantillaMatriculaPeriodosEscolares());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaEficienciaTerminal() throws IOException, Throwable{
        File f = new File(ejbPlantillasEBExcel.getPlantillaEficienciaTerminal());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaDistribucionEquipamiento() throws IOException, Throwable{
        File f = new File(ejbPlantillasEBExcel.getPlantillaDistribucionEquipamiento());
        Faces.sendFile(f, true);
    }
    
    public void descargarPlantillaDistribucionInstalaciones() throws IOException, Throwable{
        File f = new File(ejbPlantillasEBExcel.getPlantillaDistribucionInstalaciones());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteDistribucionEquipamiento() throws IOException, Throwable{
        File f = new File(ejbReportesEBExcel.getReporteDistribucionEquipamiento());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteEjercicioMatriculaPeriodoEscolar() throws IOException, Throwable{
        File f = new File(ejbReportesEBExcel.getReporteMatriculaPorEjercicio());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteCuatrimestralDistribucionInstalaciones(PeriodosEscolares periodoEscolar, AreasUniversidad areaUniversidad) throws IOException, Throwable{
        File f = new File(ejbReportesEBExcel.getReportePorPeriodoEscolarDistribucionInstalaciones(periodoEscolar, areaUniversidad));
        Faces.sendFile(f, true);
    }
}
