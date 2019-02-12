package mx.edu.utxj.pye.sgi.util;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.controladores.ch.CvEducacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ViewScoped
public class UtilidadesCH implements Serializable {

    @EJB
    EjbCarga carga;
    @EJB
    private EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    public Integer obtenerEdad(Date fechaNa) {
        try {
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaNacimi = fechaNa.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();// convertir utill.Date a time.LocalDate
//            Date date = Date.from(fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant());// Convertir de time.LocalDate a utill.Date

            if (fechaActual.getMonthValue() >= fechaNacimi.getMonthValue()) {
                if (fechaActual.getDayOfMonth() >= fechaNacimi.getDayOfMonth()) {
                    return (fechaActual.getYear() - fechaNacimi.getYear());
                } else {
                    return ((fechaActual.getYear() - fechaNacimi.getYear()) - 1);
                }
            } else {
                return ((fechaActual.getYear() - fechaNacimi.getYear()) - 1);
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(UtilidadesCH.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public void agregaBitacora(Integer user, String noR, String nombreT, String evento) {
        try {
            Date fechaActual = new Date();
            Bitacoraacceso nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(user);
            nuevaBitacoraacceso.setNumeroRegistro(noR);
            nuevaBitacoraacceso.setTabla(nombreT);
            nuevaBitacoraacceso.setAccion(evento);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String agregarEvidencias(Part file, String cvT, String registro, String subNivel) {
        String ruta = "";

        if (file == null) {
            mensajes("es necesario seleccionar un archivo.", "W", "F");
            return null;
        }

        if (subNivel.equals("")) {
            ruta = carga.subir(file, new File(cvT.concat(File.separator).concat(registro).concat(File.separator)));
        } else {
            ruta = carga.subir(file, new File(cvT.concat(File.separator).concat(registro).concat(File.separator).concat(subNivel).concat(File.separator)));
        }
        
        if (!"Error: No se pudo leer el archivo".equals(ruta)) {
            mensajes("el archivo se a cargado.", "I", "C");
            return ruta;
        } else {
            mensajes("no fue posible cargar el archivo, Intente nuevamente.", "E", "F");
            return "";
        }
    }

    public void mensajes(String mensaje, String clase, String tipo) {
        switch (clase) {
            case "I":
                Messages.addGlobalInfo("Operación exitosa " + mensaje);
                break;
            case "W":
                Messages.addGlobalWarn("Error inesperado " + mensaje);
                break;
            case "E":
                Messages.addGlobalError("Error " + mensaje);
                break;
            case "F":
                Messages.addGlobalFatal("ERROR " + mensaje);
                break;
        }
        if (tipo.equals("F")) {
            return;
        }
    }
    
    public String convertirRuta(String ruta) {
        System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesCH.convertirRuta()" + ruta.isEmpty() + "1");
        
        //Se comprueba si la bao contiene la ruta de almacenamiento de la evidencia.
        if (ruta.isEmpty()) {
            mensajes("No fue posible cargar el archivo", "W", "C");
            return null;
        }
        
        //Se inicializa una variable de tipo File mediante la obtención de la ruta (variable enviada desde la interfaz gráfica.
        File file = new File(ruta);
        
        //Se realiza la separación de la ruta obtenida y se coloca una máscara para poder mostrar los archivos sin exponer su ubicación real.
        return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
}
