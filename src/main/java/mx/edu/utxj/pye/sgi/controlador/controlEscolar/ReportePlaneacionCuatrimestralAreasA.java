package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbAsignacionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbReporteConfiguracionesCA;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Reporteplaneacioncuatrimestralareaacademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ReportePlaneacionCuatrimestralAreasA implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();
   @Getter    @Setter private List<AreasUniversidad> aus = new ArrayList<>();  
    @Getter    @Setter private List<Reporteplaneacioncuatrimestralareaacademica> rs = new ArrayList<>();    
    
    @EJB    EjbAreasLogeo eal;
    @EJB    EjbAsignacionIndicadoresCriterios eaic;    
    @EJB    EjbReporteConfiguracionesCA ercca;

    @PostConstruct
    public void init() {
        au = new AreasUniversidad();
        mostrarListaAreas();
    }

    public void mostrarListaAreas() {
        try {
            aus = new ArrayList<>();
            aus.clear();
            eal.mostrarAreasUniversidad().forEach((t) -> {
                if (t.getCategoria().getCategoria() == 8) {
                    aus.add(t);
                }
            });
            au=aus.get(0);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void mostrarLista() {
        try {
            rs = new ArrayList<>();
            rs.clear();
            rs=eaic.buscarReporteAreaAcademica(au.getArea());            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarReporte() {
        try {
//            List<CargaAcademica> academicas=new ArrayList<>();
//            academicas=ercca.
            
//            rs=eaic.buscarReporteAreaAcademica(au.getArea());            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ReportePlaneacionCuatrimestralAreasA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void extra() {
        
    }
    
    public static class ReporteConfiguracion {

        @Getter        @Setter        private String programa;
        @Getter        @Setter        private String grupo;
        @Getter        @Setter        private String materia;
        @Getter        @Setter        private String unidad;
        @Getter        @Setter        private String ser;
        @Getter        @Setter        private String sab;
        @Getter        @Setter        private String sah;        
        @Getter        @Setter        private Integer inSer;
        @Getter        @Setter        private String inSab;
        @Getter        @Setter        private String inSah;

        public ReporteConfiguracion(String programa, String grupo, String materia, String unidad, String ser, String sab, String sah, Integer inSer, String inSab, String inSah) {
            this.programa = programa;
            this.grupo = grupo;
            this.materia = materia;
            this.unidad = unidad;
            this.ser = ser;
            this.sab = sab;
            this.sah = sah;
            this.inSer = inSer;
            this.inSab = inSab;
            this.inSah = inSah;
        }
    }    
}
