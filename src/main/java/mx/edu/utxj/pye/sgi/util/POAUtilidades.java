package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;

@Named
@ViewScoped
public class POAUtilidades implements Serializable {

    @Getter    @Setter    private Short ef = 0;
    @Getter    @Setter    private Integer mes=0;
    
    @EJB    EjbCarga carga;
    @EJB    private EjbUtilidadesCH ejbDatosUsuarioLogeado;

    public Short obtenerejercicioFiscal(String tipo, Integer resta) {
        try {
            List<Eventos> nuevaListaEventos = new ArrayList<>();
            nuevaListaEventos = ejbDatosUsuarioLogeado.mostrarEventoses();
            if (nuevaListaEventos.isEmpty()) {
                return 0;
            }
            nuevaListaEventos.forEach((t) -> {
                if (t.getNombre().equals(tipo)) {
                    ef = Short.parseShort(String.valueOf(t.getFechaInicio().getYear() - resta));
                }
            });
            return ef;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(POAUtilidades.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public Integer obtenerMes(String tipo) {
        try {
            List<Eventos> nuevaListaEventos = new ArrayList<>();
            nuevaListaEventos = ejbDatosUsuarioLogeado.mostrarEventoses();
            if (nuevaListaEventos.isEmpty()) {
                return 0;
            }
            nuevaListaEventos.forEach((t) -> {
                if (t.getNombre().equals(tipo)) {
                    mes = t.getFechaInicio().getMonth();
                }
            });

            return mes;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public String obtenerMesNombre(Integer mes) {
        try {
            String nombre="";
            switch (mes) {
                case 0:                    nombre= "Enero";                    break;
                case 1:                    nombre= "Febrero";                    break;
                case 2:                    nombre= "Marzo";                    break;
                case 3:                    nombre= "Abril";                    break;
                case 4:                    nombre= "Mayo";                    break;
                case 5:                    nombre= "Junio";                    break;
                case 6:                    nombre= "Julio";                    break;
                case 7:                    nombre= "Agosto";                    break;
                case 8:                    nombre= "Septiembre";                    break;
                case 9:                    nombre= "Octubre";                    break;
                case 10:                    nombre= "Noviembre";                    break;
                case 11:                    nombre= "Diciembre";                    break;
            }
            return nombre;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
}
