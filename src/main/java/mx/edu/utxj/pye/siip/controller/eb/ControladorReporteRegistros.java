/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbReportesExcel;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "reporteRegistros")
@ViewScoped
public class ControladorReporteRegistros implements Serializable{

    private static final long serialVersionUID = -5804088615265553166L;
    
    @EJB EjbReportesExcel ejb;
    @EJB EjbModulos ejbModulos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    
//    @Getter @Setter private Short claveArea;
//    @Getter @Setter private AreasUniversidad area;
    
    @PostConstruct
    public void init(){
        
//        TODO: Detectar que tipos de registros se consultarán para determinar el área de registro
//        claveArea = controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
        
    }
     
    public void descargarReporteIngPropios(Integer ejercicioFiscal) throws IOException, Throwable{
        Short ejercicioShort = ejercicioFiscal.shortValue();
        File f = new File(ejb.getReporteIngPropios(ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReportePresupuestos(Integer ejercicioFiscal) throws IOException, Throwable{
        Short ejercicioShort = ejercicioFiscal.shortValue();
        File f = new File(ejb.getReportePresupuestos(ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteDifusion(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejb.getReporteDifusion(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteVisitas(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejb.getReporteVisitas(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteBecas(Short ejercicioFiscal) throws IOException, Throwable{
        File f = new File(ejb.getReporteBecas(ejercicioFiscal));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteAFI(Short claveArea, Integer ejercicio) throws IOException, Throwable{
        Short ejercicioShort = ejercicio.shortValue();
        File f = new File(ejb.getReporteAFI(claveArea,ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteDesercion(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejb.getReporteDesercion(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReportePerCap(Integer ejercicio) throws IOException, Throwable{
        Short ejercicioShort = ejercicio.shortValue();
        File f = new File(ejb.getReportePerCap(ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteFerProf(Short ejercicio) throws IOException, Throwable{
        File f = new File(ejb.getReporteFerProf(ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteMov(Short claveArea, Short ejercicio) throws IOException, Throwable{
        File f = new File(ejb.getReporteMov(claveArea,ejercicio));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteBolTrab(Integer ejercicio) throws IOException, Throwable{
        Short ejercicioShort = ejercicio.shortValue();
        File f = new File(ejb.getReporteBolTrab(ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteAcervo(Integer ejercicio) throws IOException, Throwable{
        Short ejercicioShort = ejercicio.shortValue();
        File f = new File(ejb.getReporteAcervo(ejercicioShort));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteRecProdep(Short ejercicio, Short area) throws IOException, Throwable{
        File f = new File(ejb.getReporteRecProdep(ejercicio, area));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteComAcad() throws IOException, Throwable{
        File f = new File(ejb.getReporteComAcad());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteProgEst() throws IOException, Throwable{
        File f = new File(ejb.getReporteProgEst());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteEgetsu() throws IOException, Throwable{
        File f = new File(ejb.getReporteEgetsu());
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteExani() throws IOException, Throwable{
        File f = new File(ejb.getReporteExani());
        Faces.sendFile(f, true);
    }
}
