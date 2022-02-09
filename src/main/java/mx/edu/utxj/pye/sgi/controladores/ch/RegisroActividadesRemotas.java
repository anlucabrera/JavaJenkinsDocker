package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesremotas;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class RegisroActividadesRemotas implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;
    
    @Getter    @Setter    private List<Actividadesremotas> actividadesremotas = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal personal = new ListaPersonal();
    @Getter    @Setter    private Actividadesremotas actividadesremota = new Actividadesremotas();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
    @Getter    @Setter    private Part file;

//@EJB
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        fechaActual = LocalDate.now();        
        actividadesremota = new Actividadesremotas();
        actividadesremota.setFecha(utilidadesCH.castearLDaD(fechaActual));        
        personal = controladorEmpleado.getNuevoOBJListaPersonal();
        mostrarLista();
    }
// ------------------------------------------------------------- Generales -------------------------------------------------------------

    public void mostrarLista() {
        try {
            actividadesremota = new Actividadesremotas();
            actividadesremota.setFecha(utilidadesCH.castearLDaD(fechaActual));        
            fechaActual =LocalDate.now();
            actividadesremotas = new ArrayList<>();           
            actividadesremotas = ejbNotificacionesIncidencias.mostrarActividadesremotas(personal.getClave());
            
            Collections.sort(actividadesremotas, (x, y) -> Integer.compare(utilidadesCH.castearDaLD(y.getFecha()).getDayOfYear(), utilidadesCH.castearDaLD(x.getFecha()).getDayOfYear()));
                
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause());
            Logger.getLogger(RegisroActividadesRemotas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// ------------------------------------------------------------- incidencias -------------------------------------------------------------
    public void crearIncidencia() {
        try {
            actividadesremota.setClavePersonal(new Personal());
            actividadesremota.getClavePersonal().setClave(personal.getClave());
            ejbNotificacionesIncidencias.agregarActividadesremotas(actividadesremota);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(RegisroActividadesRemotas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarIncidencia(Actividadesremotas incidencias) {
        try {
//            utilidadesCH.agregaBitacora(personal.getClave(), incidencias.getIncidenciaID().toString(), "Incidencias", "Delate");
            ejbNotificacionesIncidencias.eliminarActividadesremotas(incidencias);
            mostrarLista();
            Ajax.update("@form");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(RegisroActividadesRemotas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Actividadesremotas incidencias = (Actividadesremotas) event.getObject();
            ejbNotificacionesIncidencias.actualizarActividadesremotas(incidencias);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// ------------------------------------------------------------- Incacpacidades -------------------------------------------------------------


    public void metodoBase() {

    }
}