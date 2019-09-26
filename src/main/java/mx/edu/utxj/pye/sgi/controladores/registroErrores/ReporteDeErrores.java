package mx.edu.utxj.pye.sgi.controladores.registroErrores;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ActionEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.entity.prontuario.Reporteerrores;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ReporteDeErrores implements Serializable{

    private static final long serialVersionUID = -6775211516127859011L;

    //Variables de objetos Entitys
    @Getter    @Setter    private Reporteerrores reporteerrores;
    @Getter    @Setter    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
    @Getter    @Setter    private List<Reporteerrores> nuevaListaReporteerroreses = new ArrayList<>();

    @EJB    private EjbUtilidadesCH cH;

    @PostConstruct
    public void init() {        
        reporteerrores = new Reporteerrores();
        nuevaListaReporteerroreses.clear();
        obtenerlistaErroes();
    }
    
    public void obtenerlistaErroes(){
        nuevaListaReporteerroreses=cH.mostrarListaReporteerroreses();
    }

    public void obtenerError(ActionEvent event) {
        try {
            reporteerrores.setClase((String) event.getComponent().getAttributes().get("tipoError"));
            reporteerrores.setTipo((String) event.getComponent().getAttributes().get("tipoExeption"));
            reporteerrores.setFecha(df.parse((String) event.getComponent().getAttributes().get("fechaError")));
            reporteerrores.setRuta((String) event.getComponent().getAttributes().get("rutaError"));
            reporteerrores.setMensaje((String) event.getComponent().getAttributes().get("mensajeError"));  
            reporteerrores.setEstatus("registrado");
            cH.agregarReporteerroreses(reporteerrores);
            Messages.addGlobalInfo("El error se ha reportado exitosamente ");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ReporteDeErrores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarErroes(RowEditEvent event){
        Reporteerrores r = (Reporteerrores) event.getObject();
        cH.actualizarReporteerroreses(r);
        obtenerlistaErroes();
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

    public void delDistincion(Reporteerrores r){
        cH.eliminarReporteerroreses(r);
        obtenerlistaErroes();
    }
            
    public void metodoBase() {

    }
}
