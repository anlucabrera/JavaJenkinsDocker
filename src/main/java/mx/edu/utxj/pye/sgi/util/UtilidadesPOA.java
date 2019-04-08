package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;

@Named
@ViewScoped
public class UtilidadesPOA implements Serializable {

    @Getter    @Setter    private Short ef = 0;
    @Getter    @Setter    private Integer mes=0;
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();
    
    @EJB    EjbCarga carga;
    @EJB    private EjbUtilidadesCH euch;
    @Inject ControladorEmpleado ce;
    @Inject UtilidadesCorreosElectronicos correosElectronicos;

    public Short obtenerejercicioFiscal(String tipo, Integer resta) {
        try {
            List<Eventos> nuevaListaEventos = new ArrayList<>();
            nuevaListaEventos = euch.mostrarEventoses();
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
            Logger.getLogger(UtilidadesPOA.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public Integer obtenerMes(String tipo) {
        try {
            List<Eventos> nuevaListaEventos = new ArrayList<>();
            nuevaListaEventos = euch.mostrarEventoses();
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
   
    public void enviarCorreo(Integer tipo, String cdestino) {
        String pwd="fp6inrls3";
        Integer tipoDcorreo=1;
        String correoRemitente = "", contrasenia = "", correoDestino = "", titulo = "", asunto = "", mensaje = "";
        procesopoa=ce.getProcesopoa();
        correoRemitente = ce.getNuevaAreasUniversidad().getCorreoInstitucional();
        contrasenia = pwd;
        titulo = "Informe de avance del Plan de Trabajo Anual";
        switch (tipo) {
            case 1:
                System.out.println("Finalización de Registro de Actividades");
                correoDestino = "zabdi_end@hotmail.com";// correo de planeacion
                asunto = "Confirmación de finalización de Registro de actividades";
                mensaje = "Por medio del presente le informo que el área "+ ce.getNuevaAreasUniversidad().getNombre() +" concluyó satisfactoriamente la etapa de Registro de Actividades. \n"
                        + "Quedo en espera de sus observaciones, al correo: "+ce.getNuevaAreasUniversidad().getCorreoInstitucional();
                procesopoa.setRegistroAFinalizado(true);
                break;
            case 2:
                System.out.println("Validación de Registro de Actividades");
                correoDestino = cdestino;
                asunto = "Confirmación de Validación de Registro de actividades";
                mensaje = "Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Registro de Actividades. \n"
                        + "Puede continuar con la etapa de Presupuestación";
                procesopoa.setValidacionRegistroA(true);
                break;
            case 3:
                System.out.println("Finalización de Asignación de Recurso");
                correoDestino = "zabdi_end@hotmail.com";//correo de recursos financiaeros
                asunto = "Confirmación de finalización de Asignación de Recurso";
                mensaje = "Por medio del presente le informo que el área "+ ce.getNuevaAreasUniversidad().getNombre() +" concluyó satisfactoriamente la etapa de Presupuestación. \n"
                        + "Quedo en espera de sus observaciones, al correo: "+ce.getNuevaAreasUniversidad().getCorreoInstitucional();
                procesopoa.setAsiganacionRFinalizado(true);
                break;
            case 4:
                System.out.println("Validación de Asignación de Recursos");
                correoDestino = cdestino;
                asunto = "Confirmación de Validación de Asignación de Recurso";
                mensaje = "Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Presupuestación. \n"
                        + "Puede continuar con la etapa de Justificacione";
                procesopoa.setValidacionRFFinalizado(true);
                break;
            case 5:
                System.out.println("Finalización de Registro de Justificaciones");
                correoDestino = "zabdi_end@hotmail.com";//correo de recursos financiaeros
                asunto = "Confirmación de Finalización de Registro de Justificaciones";
                mensaje = "Por medio del presente le informo que el área "+ ce.getNuevaAreasUniversidad().getNombre() +" concluyó satisfactoriamente la etapa de Registro de Justificaciones. \n"
                        + "Quedo en espera de sus observaciones, al correo: "+ce.getNuevaAreasUniversidad().getCorreoInstitucional();
                procesopoa.setRegistroJustificacionFinalizado(true);
                break;
            case 6:
                System.out.println("Validación de Registro de Justificaciones");
                correoDestino = cdestino;
                asunto = "Confirmación de finalización del proceso POA";
                mensaje = "Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Registro de Justificaciones. \n"
                        + "Felicidades ha finalizado la primera fase del Plan Anual de Trabajo. \n"
                        + "Quedamos a la espera de inicio del periodo de evaluación en el mes de enero. \n"
                        + "Gracias por su colaboración.";
                procesopoa.setValidacionJustificacion(true);
                tipoDcorreo=2;
                break;
        }
        System.out.println("correoRemitente: " + correoRemitente);
        System.out.println("contrasenia: " + contrasenia);
        System.out.println("correoDestino: " + correoDestino);
        System.out.println("titulo: " + titulo);
        System.out.println("asunto: " + asunto);
        System.out.println("mensaje: " + mensaje);
        correosElectronicos.enviarConfirmacionCorreoElectronico(correoRemitente, contrasenia, correoDestino, titulo, asunto, mensaje,tipoDcorreo);
        actualizarProcesopoa();
    }
  
    public void actualizarProcesopoa() {
        try {
            euch.actualizarEtapaPOA(procesopoa);
            ce.areaPoa();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
