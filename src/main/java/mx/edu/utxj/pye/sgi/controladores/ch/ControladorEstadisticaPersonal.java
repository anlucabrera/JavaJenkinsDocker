package mx.edu.utxj.pye.sgi.controladores.ch;

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
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorEstadisticaPersonal implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaVistaListaPersonal = new ArrayList<>();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbSelectec;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        mostrarPlantillaPersonal();
    }

    public void mostrarPlantillaPersonal() {
        try {
            nuevaVistaListaPersonal = ejbSelectec.mostrarListaDeEmpleados();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEstadisticaPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
