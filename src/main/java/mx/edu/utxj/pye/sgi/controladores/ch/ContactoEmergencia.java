package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ContactoEmergencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ContactoEmergencia implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private ContactoEmergencias contactoEmergencia=new ContactoEmergencias();
    @Getter    @Setter    private List<ContactoEmergencias> ces=new ArrayList<>();
//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal personal; 
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        contactoEmergencia=new ContactoEmergencias();
        ces=new ArrayList<>();
        consultarContactos();
    }

    /*
    Este método busca la información del Personal logeado, esto mediante la obtención de su clave    
     */
    public void consultarContactos() {
        try {
            ces = new ArrayList<>();
            ces.clear();
            ces = personal.mostrarContactosEmergencias(controladorEmpleado.getEmpleadoLogeado());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ContactoEmergencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Este método sirve para actualizar la información adicional del personal, comprueba si existe algún registro de información adicional en caso de no existir creara el registro
     */
    public void agregarContactoEmergencia() {
        try {
            contactoEmergencia.setClavePersonal(new Personal());
            contactoEmergencia.getClavePersonal().setClave(controladorEmpleado.getEmpleadoLogeado());
            contactoEmergencia = personal.crearContactosEmergencias(contactoEmergencia);
            contactoEmergencia = new ContactoEmergencias();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            consultarContactos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ContactoEmergencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarContactoEmergencia(RowEditEvent event) {
        try {
            ContactoEmergencias actualizarD = (ContactoEmergencias) event.getObject();
            personal.actualizarContactosEmergencias(actualizarD);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            consultarContactos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ContactoEmergencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarEmergencia(ContactoEmergencias ce) {
        try {
            personal.eliminarContactosEmergencias(ce);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            consultarContactos();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ContactoEmergencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metodoBase() {

    }
    
}
