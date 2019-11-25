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
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
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
    
    public void extra() {
        
    }
}
