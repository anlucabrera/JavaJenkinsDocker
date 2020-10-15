package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Comentariosprocesopoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class UtilidadesPOA implements Serializable {

    @Getter    @Setter    private Short ef = 0;
    @Getter    @Setter    private Integer mes=0,diasExtra=0;
    @Getter    @Setter    private LocalDateTime fechaActualHora = LocalDateTime.now();
    @Getter    @Setter    private Eventos ea= new Eventos();
    @Getter    @Setter    private Procesopoa procesopoa = new Procesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaArea = new Comentariosprocesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaApye = new Comentariosprocesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaAFin = new Comentariosprocesopoa();
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");    

    @EJB    EjbCarga ejbUtilidadesCH;
    @EJB    EjbCatalogosPoa ecp;    
    @EJB    EjbRegistroActividades era;
    @EJB    EjbAreasLogeo eal;
    @EJB    private EjbUtilidadesCH euch;
    @Inject ControladorEmpleado ce;
    @Inject UtilidadesCorreosElectronicos correosElectronicos;
    @Inject    UtilidadesCH uch;

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

    public String obtenerAreaNombre(Short clave) {
        try {
            AreasUniversidad au = new AreasUniversidad();
            au = eal.mostrarAreasUniversidad(clave);
            return au.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String obtenerSemaforo(Double porcentaje) {
        String semaforo = "";
        if (porcentaje >= 0D && porcentaje <= 89.99) {
            semaforo = "semaforoRojo";
        } else if (porcentaje >= 90D && porcentaje <= 94.99) {
            semaforo = "semaforoAmarillo";
        } else if (porcentaje >= 95D && porcentaje <= 115.99) {
            semaforo = "semaforoVerde";
        } else if (porcentaje >= 116D) {
            semaforo = "semaforoRojo";
        }
        return semaforo;
    }
    
    public Double obtenerTotalPorcejate(Double porcentaje) {
        if (porcentaje >= 116 && porcentaje <= 165) {
                porcentaje = 89.99;
            } else if (porcentaje >= 166 && porcentaje <= 215) {
                porcentaje = 84.99;
            } else if (porcentaje >= 216 && porcentaje <= 265) {
                porcentaje = 79.99;
            } else if (porcentaje >= 266 && porcentaje <= 315) {
                porcentaje = 74.99;
            } else if (porcentaje >= 316) {
                porcentaje = 69.99;
            }
        return porcentaje;
    }
    
    public Double obtenerTotalPorcejateGeneral(Double alcansadas, Double planeadas) {
        Double porcentaje = 0D;
        if (planeadas.equals(alcansadas)) {
            porcentaje = 100.0;
        } else {
            porcentaje = (alcansadas / planeadas) * 100;
        }
        return porcentaje;
    }

    public Double totalProgramado(ActividadesPoa actividadesPoas,Integer numeroMes) {
        Double totalPCorte = 0D;
        switch (numeroMes) {
            case 11:                totalPCorte = totalPCorte + actividadesPoas.getNPDiciembre();
            case 10:                totalPCorte = totalPCorte + actividadesPoas.getNPNoviembre();
            case 9:                totalPCorte = totalPCorte + actividadesPoas.getNPOctubre();
            case 8:                totalPCorte = totalPCorte + actividadesPoas.getNPSeptiembre();
            case 7:                totalPCorte = totalPCorte + actividadesPoas.getNPAgosto();
            case 6:                totalPCorte = totalPCorte + actividadesPoas.getNPJulio();
            case 5:                totalPCorte = totalPCorte + actividadesPoas.getNPJunio();
            case 4:                totalPCorte = totalPCorte + actividadesPoas.getNPMayo();
            case 3:                totalPCorte = totalPCorte + actividadesPoas.getNPAbril();
            case 2:                totalPCorte = totalPCorte + actividadesPoas.getNPMarzo();
            case 1:                totalPCorte = totalPCorte + actividadesPoas.getNPFebrero();
            case 0:                totalPCorte = totalPCorte + actividadesPoas.getNPEnero();                break;
        }        
        return totalPCorte;
    }

    public Double totalAlcanzado(ActividadesPoa actividadesPoas,Integer numeroMes) {
        Double totalACorte = 0D;
        switch (numeroMes) {
            case 11:                totalACorte = totalACorte + actividadesPoas.getNADiciembre();
            case 10:                totalACorte = totalACorte + actividadesPoas.getNANoviembre();
            case 9:                totalACorte = totalACorte + actividadesPoas.getNAOctubre();
            case 8:                totalACorte = totalACorte + actividadesPoas.getNASeptiembre();
            case 7:                totalACorte = totalACorte + actividadesPoas.getNAAgosto();
            case 6:                totalACorte = totalACorte + actividadesPoas.getNAJulio();
            case 5:                totalACorte = totalACorte + actividadesPoas.getNAJunio();
            case 4:                totalACorte = totalACorte + actividadesPoas.getNAMayo();
            case 3:                totalACorte = totalACorte + actividadesPoas.getNAAbril();
            case 2:                totalACorte = totalACorte + actividadesPoas.getNAMarzo();
            case 1:                totalACorte = totalACorte + actividadesPoas.getNAFebrero();
            case 0:                totalACorte = totalACorte + actividadesPoas.getNAEnero();                break;
        }
        return totalACorte;
    }
    
    public void enviarCorreo(String tipo, String rol, Boolean aceptado, String observaciones, AreasUniversidad areaDestino) {
        diasExtra=0;  
        String asunto="";
        Boolean refi = false;
        Boolean pye = false;
        try {
            procesopoa = euch.mostrarEtapaPOAArea(areaDestino.getArea());
            if (aceptado && rol.equals("Ad")) {
                switch (tipo) {
                    case "A":                        ea = euch.mostrarEventosRegistro("POA", "Recurso").get(0);                        break;
                    case "R":                        ea = euch.mostrarEventosRegistro("POA", "Justificacion").get(0);                        break;
                }
                LocalDateTime fechaf = uch.castearDaLDT(ea.getFechaFin());
                LocalDateTime fechai = uch.castearDaLDT(ea.getFechaInicio());
                if ((fechaActualHora.isAfter(fechai) || fechaActualHora.equals(fechai))) {
                    EventosAreas e = new EventosAreas();
                    EventosAreasPK epk = new EventosAreasPK();
                    epk = new EventosAreasPK(ea.getEvento(), procesopoa.getArea());                    
                    e.setEventosAreasPK(new EventosAreasPK());
                    e.setEventosAreasPK(epk);                  
                    Integer dias = (int) ((uch.castearLDTaD(fechaf).getTime() - uch.castearLDTaD(fechaActualHora).getTime()) / 86400000);
                    if(dias<0){
                        diasExtra=Math.abs((int) ((uch.castearLDTaD(fechaActualHora).getTime()- uch.castearLDTaD(fechaf).getTime()) / 86400000));
                    }
                    if (dias <= 4) {
                        switch (fechaActualHora.getDayOfWeek()) {
                            case MONDAY:                                diasExtra =diasExtra+ 4;                                break;
                            case TUESDAY:                                diasExtra = diasExtra+6;                                break;
                            case WEDNESDAY:                                diasExtra = diasExtra+6;                                break;
                            case THURSDAY:                                diasExtra =diasExtra+ 6;                                break;
                            case FRIDAY:                                diasExtra = diasExtra+6;                                break;
                            case SATURDAY:                                diasExtra = diasExtra+5;                                break;
                            case SUNDAY:                                diasExtra =diasExtra+ 4;                                break;
                        }
                    }                    
                    e.setDiasExtra(diasExtra);
                    euch.agregarEventosesAreases(e);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        cpoaArea=new Comentariosprocesopoa();
        cpoaApye=new Comentariosprocesopoa();
        cpoaAFin=new Comentariosprocesopoa();
        
                /*
        *titulo = titulo del correo 
        *asunto = asunto del correo 
        *cpoaArea.setComentarios(mensaje que se envia al área en proceso de POA
        *cpoaAFin.setComentarios(mensaje que se envia a la Sub direccion de Recursos Financieros
        *cpoaApye.setComentarios(mensaje que se envia a la Direccion de Planeación y evaluación
        *procesopoa.set........(valor) = Actualizacion del estatus en el proceso POA
        *tipo = A: Registro de actividades
        *       R: Asignación de Recurso
        *       J: Registro de Justificaciones 
        *rol=   Us: Área en Proceso de POA
        *       Ad: Administrador
         */
                
        cpoaArea.setArea(areaDestino.getArea());
        cpoaApye.setArea(Short.parseShort("6"));
        cpoaAFin.setArea(Short.parseShort("7"));
        
        cpoaArea.setStatus(Boolean.FALSE);
        cpoaApye.setStatus(Boolean.FALSE);
        cpoaAFin.setStatus(Boolean.FALSE);     
        
        cpoaArea.setComentarios(observaciones);
        cpoaApye.setComentarios(observaciones);
        cpoaAFin.setComentarios(observaciones);
        switch (tipo) {
            case "A":
                switch (rol) {
                    case "Us":
                        asunto = "Confirmación de finalización de Registro de actividades";
                       cpoaApye.setComentarios("Por medio del presente le informo que el área " + areaDestino.getNombre() + " concluyó satisfactoriamente la etapa de Registro de Actividades. \n"
                                + "Quedo en espera de sus observaciones, al correo: " + areaDestino.getCorreoInstitucional());
                        cpoaArea.setComentarios("Por medio del presente se le informa que se ha enviado a revisión su Registro de Actividades. \n"
                                + "Espere las observaciones o la apertura de la etapa de Asignación de Recurso");
                        procesopoa.setRegistroAFinalizado(true);
                        refi = false;
                        break;
                    case "Ad":
                        if (aceptado) {                            
                            asunto = "Confirmación de Validación de Registro de actividades";
                            cpoaArea.setComentarios("Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Registro de Actividades. \n"
                                    + "Puede continuar con la etapa de Presupuestación");
                            cpoaApye.setComentarios("Por medio del presente se le informa que ha aceptado el Registro de Actividades del área." + areaDestino.getNombre() + "\n"
                                    + "Por lo cual se le ha aperturado la etapa de Asignación de Recurso");
                            cpoaAFin.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ha iniciado con la etapa de Asignación de Recurso");
                            procesopoa.setValidacionRegistroA(true);
                            refi = true;
                        } else {
                            asunto = "Denegacion de Registro de actividades";
                            cpoaArea.setComentarios("Por medio del presente le informo que su Registro de actividades cuenta con las siguintes observaciones \n"
                                    + observaciones
                                    + "\n Enviar comentarios al correo de planeacion.evaluacion@utxicotepec.edu.mx");
                            cpoaApye.setComentarios("Por medio del presente se le informa que ha denegado el Registro de Actividades del área." + areaDestino.getNombre() + " con las siguintes observaciones \n"
                                    + observaciones
                                    + "Por lo cual se le ha re-aperturado la etapa de Registro de actividades");
                            procesopoa.setRegistroAFinalizado(false);
                            refi = false;
                        }
                        break;
                }
                pye = true;
                break;
            case "R":
                switch (rol) {
                    case "Us":
                        asunto = "Confirmación de finalización de Asignación de Recurso";
                        cpoaAFin.setComentarios("Por medio del presente le informo que el área " + areaDestino.getNombre() + " concluyó satisfactoriamente la etapa de Presupuestación. \n"
                                + "Quedo en espera de sus observaciones, al correo: " + areaDestino.getCorreoInstitucional());
                        cpoaArea.setComentarios("Por medio del presente se le informa que se ha enviado a revisión su Asignación de Recurso. \n"
                                + "Espere las observaciones o la apertura de la etapa de Registro de Justificaciones");
                        procesopoa.setAsiganacionRFinalizado(true);
                        pye = false;
                        break;
                    case "Ad":
                        if (aceptado) {
                            asunto = "Confirmación de Validación de Asignación de Recurso";
                            cpoaArea.setComentarios("Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Presupuestación. \n"
                                    + "Puede continuar con la etapa de Justificacione");
                            cpoaAFin.setComentarios("Por medio del presente se le informa que ha aceptado la Asignación de Recurso del área." + areaDestino.getNombre() + "\n"
                                    + "Por lo cual se le ha aperturado la etapa de Registro de Justificaciones");
                            cpoaApye.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ha iniciado con la etapa de Registro de Justificaciones");
                            procesopoa.setValidacionRFFinalizado(true);
                            pye = true;
                        } else {
                            asunto = "Denegacion de Asignación de Recurso";
                            cpoaArea.setComentarios("Por medio del presente le informo que su Asignación de Recurso cuenta con las siguintes observaciones \n"
                                    + observaciones
                                    + "\n Enviar comentarios al correo de recursos.financieros@utxicotepec.edu.mx");
                            cpoaAFin.setComentarios("Por medio del presente se le informa que ha denegado la Asignación de Recurso del área." + areaDestino.getNombre() + " con las siguintes observaciones \n"
                                    + observaciones
                                    + "Por lo cual se le ha re-aperturado la etapa de Asignación de Recurso");
                            procesopoa.setAsiganacionRFinalizado(false);
                            pye = false;
                        }
                        break;
                }
                refi = true;
                break;
            case "J":
                switch (rol) {
                    case "Us":
                        asunto = "Confirmación de Finalización de Registro de Justificaciones";
                        cpoaAFin.setComentarios("Por medio del presente le informo que el área " + areaDestino.getNombre() + " concluyó satisfactoriamente la etapa de Registro de Justificaciones. \n"
                                + "Quedo en espera de sus observaciones, al correo: " + areaDestino.getCorreoInstitucional());
                        cpoaArea.setComentarios("Por medio del presente se le informa que se ha enviado a revisión su Registro de Justificaciones. \n"
                                + "Espere las observaciones o la confirmación de finalización del proceso POA fase 1(Programación, presupuestación, Justificación)");
                        procesopoa.setRegistroJustificacionFinalizado(true);
                        pye = false;
                        break;
                    case "Ad":
                        if (aceptado) {
                            asunto = "Confirmación de finalización del proceso POA";
                            cpoaArea.setComentarios("Por medio del presente le informo que su área concluyó satisfactoriamente la etapa de Registro de Justificaciones. \n"
                                    + "Felicidades ha finalizado la primera fase del Plan Anual de Trabajo. \n"
                                    + "Quedamos a la espera de inicio del periodo de evaluación en el mes de enero. \n"
                                    + "Gracias por su colaboración.");
                            cpoaAFin.setComentarios("Por medio del presente se le informa que ha aceptado el Registro de Justificaciones del área." + areaDestino.getNombre() + "\n"
                                    + "Por lo cual ha concluido satisfactoriamente el proceso POA fase 1(Programación, presupuestación, Justificación)");
                            cpoaApye.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ha concluido satisfactoriamente el proceso POA fase 1(Programación, presupuestación, Justificación)");
                            procesopoa.setValidacionJustificacion(true);
                            pye = true;
                        } else {
                            asunto = "Denegacion de Registro de Justificaciones";
                            cpoaArea.setComentarios("Por medio del presente le informo que su Registro de Justificaciones cuenta con las siguintes observaciones \n"
                                    + observaciones
                                    + "\n Enviar comentarios al correo de recursos.financieros@utxicotepec.edu.mx");
                            cpoaAFin.setComentarios("Por medio del presente se le informa que ha denegado el Registro de Justificaciones del área." + areaDestino.getNombre() + " con las siguintes observaciones \n"
                                    + observaciones
                                    + "Por lo cual se le ha re-aperturado la etapa de Registro de Justificaciones");
                            procesopoa.setValidacionJustificacion(true);
                            pye = false;
                        }
                        break;
                }
                refi = true;
                break;
            case "P":
                asunto = "Presupuesto";
                switch (rol) {
                    case "As":
                        cpoaArea.setComentarios("Por medio del presente le informo que su área ya cuenta con presupuesto asignado para realizar la presupuestación correspondiente la cual estará habilitada a partir de que le llegue el correo de 'Confirmación de Validación de Registro de actividades' por parte de la Dirección de Planeación y Evaluación");
                        cpoaAFin.setComentarios("Por medio del presente le informo que ha asigando presupuesto al área de " + areaDestino.getNombre() + "el cual estara desponible para realizar la presupuestación correspondiente que estará habilitada a partir de que le llegue el correo de 'Confirmación de Validación de Registro de actividades' por parte de la Dirección de Planeación y Evaluación");
                        cpoaApye.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ya cuenta con presupuesto asignado para realizar la presupuestación correspondiente");

                        pye = true;
                        break;
                    case "Ac":
                        cpoaArea.setComentarios("Por medio del presente le informo que se le ha realizado un reajuste al presupuesto de su área, para más información. \n Enviar comentarios y solicitar más al correo de recursos.financieros@utxicotepec.edu.mx");
                        cpoaAFin.setComentarios("Por medio del presente le informo que ha realizado un reajuste al presupuesto del área de " + areaDestino.getNombre());
                        break;
                    case "El":
                        cpoaArea.setComentarios("Por medio del presente le informo que se le ha retirado el presupuesto de su área, para más información. \n Enviar comentarios al correo de recursos.financieros@utxicotepec.edu.mx");
                        cpoaAFin.setComentarios("Por medio del presente le informo que ha le ha retirado el presupuesto al área de " + areaDestino.getNombre());
                        cpoaApye.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ya NO cuenta con presupuesto asignado para realizar la presupuestación correspondiente");
                        pye = true;
                        break;
                }
                refi = true;
                break;
        }

        cpoaArea.setProceso(asunto);
        cpoaApye.setProceso(asunto);
        cpoaAFin.setProceso(asunto);

        cpoaArea.setEjercicioFiscal(new EjerciciosFiscales(procesopoa.getEjercicioFiscalEtapa1()));
        cpoaApye.setEjercicioFiscal(new EjerciciosFiscales(procesopoa.getEjercicioFiscalEtapa1()));
        cpoaAFin.setEjercicioFiscal(new EjerciciosFiscales(procesopoa.getEjercicioFiscalEtapa1()));
        
        cpoaArea=era.agregarComentariosprocesopoa(cpoaArea);
        if (refi) {
            cpoaAFin=era.agregarComentariosprocesopoa(cpoaAFin);
        }
        if (pye) {
            cpoaApye=era.agregarComentariosprocesopoa(cpoaApye);
        }
        actualizarProcesopoa();
        recargarPag();
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

    public EjerciciosFiscales obtenerAnioRegistro(Short idC) {
        return ecp.mostrarEjercicioFiscaleses(idC);
    }

    public void recargarPag() {
        Faces.refresh();
    }

}
