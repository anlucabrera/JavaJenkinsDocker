package mx.edu.utxj.pye.sgi.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex;
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
    @Getter    @Setter    private String tpRactividad = "In";
    @Getter    @Setter    private String tpRrecursoJu = "In";
    @Getter    @Setter    private Integer mes=0,diasExtra=0;
    @Getter    @Setter    private LocalDateTime fechaActualHora = LocalDateTime.now();
    @Getter    @Setter    private Eventos ea= new Eventos();
    @Getter    @Setter    private Permisosevaluacionpoaex permisoRegAct= new Permisosevaluacionpoaex();
    @Getter    @Setter    private Permisosevaluacionpoaex permisoRegRec= new Permisosevaluacionpoaex();
    @Getter    @Setter    private Procesopoa procesopoa = new Procesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaArea = new Comentariosprocesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaApye = new Comentariosprocesopoa();
    @Getter    @Setter    private Comentariosprocesopoa cpoaAFin = new Comentariosprocesopoa();
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");    

    @EJB    EjbCarga ejbUtilidadesCH;
    @EJB    EjbCatalogosPoa ecp;    
    @EJB    EjbRegistroActividades era;
    @EJB    EjbAreasLogeo eal;
    @EJB    EjbAdministrador administrador;
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
    
    public Short obtenerejercicioFiscalActivo(String tipo, Integer resta) {
        try {
            
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
            String nombre = "";
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
                case 12:                    nombre= "Sin mes de evaluacion";                    break;
            }
            return nombre;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public Integer obtenerMesNumero(String mes) {
        try {
            Integer numeroM = 0;
            switch (mes) {
                case "Enero":                    numeroM = 0;                    break;
                case "Febrero":                    numeroM = 1;                    break;
                case "Marzo":                    numeroM = 2;                    break;
                case "Abril":                    numeroM = 3;                    break;
                case "Mayo":                    numeroM = 4;                    break;
                case "Junio":                    numeroM = 5;                    break;
                case "Julio":                    numeroM = 6;                    break;
                case "Agosto":                    numeroM = 7;                    break;
                case "Septiembre":                    numeroM = 8;                    break;
                case "Octubre":                    numeroM = 9;                    break;
                case "Noviembre":                    numeroM = 10;                    break;
                case "Diciembre":                    numeroM = 11;                    break;
                default:                    numeroM = 12;                    break;
            }
            return numeroM;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
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

    public Double totalProgramado(ActividadesPoa actividadesPoas, Integer numeroMes) {
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

    public Double totalAlcanzado(ActividadesPoa actividadesPoas, Integer numeroMes) {
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
        try {
            String asunto = ""; 
            procesopoa = euch.mostrarEtapaPOAArea(areaDestino.getArea());
            EjerciciosFiscales ef =obtenerAnioRegistro(procesopoa.getEjercicioFiscalEtapa1());
            
            Boolean refi = false;
            Boolean pye = false;

            cpoaArea = new Comentariosprocesopoa();
            cpoaApye = new Comentariosprocesopoa();
            cpoaAFin = new Comentariosprocesopoa();

            cpoaArea.setArea(areaDestino.getArea());
            cpoaApye.setArea(Short.parseShort("6"));
            cpoaAFin.setArea(Short.parseShort("7"));

            cpoaArea.setStatus(Boolean.FALSE);
            cpoaApye.setStatus(Boolean.FALSE);
            cpoaAFin.setStatus(Boolean.FALSE);

            cpoaArea.setComentarios(observaciones);
            cpoaApye.setComentarios(observaciones);
            cpoaAFin.setComentarios(observaciones);

            cpoaArea.setFecha(new Date());
            cpoaApye.setFecha(new Date());
            cpoaAFin.setFecha(new Date());
            switch (tipo) {
                case "A":
                    switch (rol) {
                        case "Us":
                            asunto = "Confirmación de finalización de Registro de actividades";
                            cpoaApye.setComentarios("El área " + areaDestino.getNombre() + " concluyó satisfactoriamente la etapa de Registro de Actividades.");
                            cpoaArea.setComentarios("Ha enviado a revisión su Registro de Actividades, espere las observaciones o la apertura de la etapa de Asignación de Recurso");
                            procesopoa.setRegistroAFinalizado(Boolean.TRUE);
                            refi = false;
                            break;
                        case "Ad":
                            if (aceptado) {
                                asunto = "Confirmación de Validación de Registro de actividades";
                                cpoaArea.setComentarios("Su área concluyó satisfactoriamente la etapa de Registro de Actividades, ya puede continuar con la etapa de Presupuestación");
                                cpoaApye.setComentarios("Ha aceptado el Registro de Actividades del área " + areaDestino.getNombre());
                                cpoaAFin.setComentarios("El área " + areaDestino.getNombre() + " ha iniciado con la etapa de Asignación de Recurso");
                                procesopoa.setValidacionRegistroA(Boolean.TRUE);
                                refi = true;
                                actualizarAgregarPermisosExtPOA("Registro");
                            } else {
                                asunto = "Denegacion de Registro de actividades";
                                cpoaArea.setComentarios("Se han echo las siguintes observaciones a su Registro de actividades" + observaciones);
                                cpoaApye.setComentarios("Ha denegado el Registro de Actividades del área " + areaDestino.getNombre() + " con las siguintes observaciones -- "+ observaciones
                                        + "--. Por lo cual se le ha re-aperturado la etapa de Registro de actividades");
                                procesopoa.setRegistroAFinalizado(Boolean.FALSE);
                                refi = false;
                                actualizarAgregarPermisosExtPOA("Registro");
                                actualizarAgregarPermisosExtPOA("Recurso");
                            }
                            break;
                    }
                    pye = true;
                    break;
                case "R":
                    switch (rol) {
                        case "Us":
                            asunto = "Confirmación de finalización de Asignación de Recurso y Registro de Justificaciones";
                            cpoaAFin.setComentarios("El área " + areaDestino.getNombre() + " concluyó satisfactoriamente la etapa de Presupuestación.");
                            cpoaArea.setComentarios("Ha enviado a revisión su Asignación de Recurso y Registro de Justificaciones, espere las observaciones o la confirmación de finalización del proceso POA fase 1(Programación, presupuestación, Justificación)");
                            procesopoa.setAsiganacionRFinalizado(Boolean.TRUE);
                            procesopoa.setRegistroJustificacionFinalizado(Boolean.TRUE);
                            pye = false;
                            break;
                        case "Ad":
                            if (aceptado) {
                                asunto = "Confirmación de finalización del proceso POA";
                                cpoaArea.setComentarios("Se le informa que su área concluyó satisfactoriamente la etapa de la programacion de actividades para del ejercicio " + ef.getAnio() + " Gracias por su colaboración.");
                                cpoaAFin.setComentarios("El área." + areaDestino.getNombre() + " ha concluido satisfactoriamente el proceso POA fase 1(Programación, presupuestación, Justificación)");
                                cpoaApye.setComentarios("El área." + areaDestino.getNombre() + " ha concluido satisfactoriamente el proceso POA fase 1(Programación, presupuestación, Justificación)");
                                procesopoa.setValidacionRFFinalizado(Boolean.TRUE);
                                procesopoa.setValidacionJustificacion(Boolean.TRUE);
                                pye = true;
                            } else {
                                asunto = "Denegacion de Asignación de Recurso";
                                cpoaArea.setComentarios("Se han echo las siguintes observaciones a su Asignación de Recurso y Registro de Justificaciones: -- "
                                        + observaciones);
                                cpoaAFin.setComentarios("Ha denegado la signación de Recurso y Registro de Justificaciones del área." + areaDestino.getNombre() + " con las siguintes observaciones: --"
                                        + observaciones
                                        + " -- Por lo cual se le ha re-aperturado la etapa de Asignación de Recurso");
                                procesopoa.setAsiganacionRFinalizado(Boolean.FALSE);
                                procesopoa.setRegistroJustificacionFinalizado(Boolean.FALSE);
                                actualizarAgregarPermisosExtPOA("Recurso");
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
                            cpoaArea.setComentarios("Por medio del presente le informo que se le ha realizado un reajuste al presupuesto de su área, para más información solicitarla al correo de recursos.financieros@utxicotepec.edu.mx");
                            cpoaAFin.setComentarios("Por medio del presente le informo que ha realizado un reajuste al presupuesto del área de " + areaDestino.getNombre());
                            break;
                        case "El":
                            cpoaArea.setComentarios("Por medio del presente le informo que se le ha retirado el presupuesto de su área, para más información solicitarla al correo de recursos.financieros@utxicotepec.edu.mx");
                            cpoaAFin.setComentarios("Por medio del presente le informo que ha le ha retirado el presupuesto al área de " + areaDestino.getNombre());
                            cpoaApye.setComentarios("Por medio del presente se le informa que el área." + areaDestino.getNombre() + " ya NO cuenta con presupuesto asignado para realizar la presupuestación correspondiente");
                            pye = true;
                            break;
                    }
                    refi = true;
            }
          
            cpoaArea.setProceso(asunto);
            cpoaApye.setProceso(asunto);
            cpoaAFin.setProceso(asunto);

            cpoaArea.setEjercicioFiscal(new EjerciciosFiscales());
            cpoaApye.setEjercicioFiscal(new EjerciciosFiscales());
            cpoaAFin.setEjercicioFiscal(new EjerciciosFiscales());
            
            cpoaArea.setEjercicioFiscal(ef);
            cpoaApye.setEjercicioFiscal(ef);
            cpoaAFin.setEjercicioFiscal(ef);
            
            cpoaArea = era.agregarComentariosprocesopoa(cpoaArea);
            if (refi) {
                cpoaAFin = era.agregarComentariosprocesopoa(cpoaAFin);
            }
            if (pye) {
                cpoaApye = era.agregarComentariosprocesopoa(cpoaApye);
            }
            actualizarProcesopoa();
            recargarPag();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
       

        
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
    
    public void actualizarAgregarPermisosExtPOA(String tpR) {
        try {
//            System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(A)"+tpR);
            List<Calendarioevaluacionpoa> calendarioevaluacionpoas = euch.mostrarCalendarioevaluacionpoas().stream().filter(t -> (t.getEjercicioFiscal() == procesopoa.getEjercicioFiscalEtapa1()) && (t.getEstapa().equals("Registro")) && (t.getMesEvaluacion().equals(tpR))).collect(Collectors.toList());
            List<Permisosevaluacionpoaex> permisosevaluacionpoaexs = procesopoa.getPermisosevaluacionpoaexList().stream().filter(t -> (t.getEvaluacionPOA().getEjercicioFiscal() == procesopoa.getEjercicioFiscalEtapa1()) && (t.getEvaluacionPOA().getEstapa().equals("Registro")) && (t.getEvaluacionPOA().getMesEvaluacion().equals(tpR))).collect(Collectors.toList());

//            System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(1)"+calendarioevaluacionpoas.size());
//            System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(2)"+permisosevaluacionpoaexs.size());
            
            Calendarioevaluacionpoa calendarioRegistro = new Calendarioevaluacionpoa();
            Permisosevaluacionpoaex permisosevaluacionpoaex = new Permisosevaluacionpoaex();

            if (!calendarioevaluacionpoas.isEmpty()) {
                calendarioRegistro = calendarioevaluacionpoas.get(0);
            }
//            System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(B)"+calendarioRegistro.getFechaInicio());
            permisosevaluacionpoaex.setFechaApertura(new Date());
            permisosevaluacionpoaex.setFechaCierre(calendarioRegistro.getFechaFin());
            permisosevaluacionpoaex.setEvaluacionPOA(new Calendarioevaluacionpoa());
            permisosevaluacionpoaex.setProcesoPOA(new Procesopoa());
            permisosevaluacionpoaex.setEvaluacionPOA(calendarioRegistro);
            permisosevaluacionpoaex.setProcesoPOA(procesopoa);
            
//            System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(C)"+permisosevaluacionpoaex);
            
            LocalDateTime fechafCalendario = uch.castearDaLDT(permisosevaluacionpoaex.getFechaCierre());
            Integer dias = (int) ((uch.castearLDTaD(fechafCalendario).getTime() - uch.castearLDTaD(fechaActualHora).getTime()) / 86400000);
            if (dias <= 4) {
                switch (fechaActualHora.getDayOfWeek()) {
                    case MONDAY:    fechaActualHora = fechaActualHora.plusDays(4);                          break;
                    case TUESDAY:   fechaActualHora = fechaActualHora.plusDays(6);                            break;
                    case WEDNESDAY: fechaActualHora = fechaActualHora.plusDays(6);                            break;
                    case THURSDAY:  fechaActualHora = fechaActualHora.plusDays(6);                            break;
                    case FRIDAY:    fechaActualHora = fechaActualHora.plusDays(6);                            break;
                    case SATURDAY:  fechaActualHora = fechaActualHora.plusDays(5);                            break;
                    case SUNDAY:    fechaActualHora = fechaActualHora.plusDays(4);                            break;
                }
                permisosevaluacionpoaex.setFechaCierre(uch.castearLDTaD(fechaActualHora));
                
                if (!permisosevaluacionpoaexs.isEmpty()) {
//                    System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(D)");
                    administrador.eliminarPermisosevaluacionpoaex(permisosevaluacionpoaexs.get(0));
                }
//                System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(E)");
                permisosevaluacionpoaex=administrador.crearPermisosevaluacionpoaex(permisosevaluacionpoaex);
//                System.out.println("mx.edu.utxj.pye.sgi.util.UtilidadesPOA.actualizarAgregarPermisosExtPOA(F)"+permisosevaluacionpoaex);

            }
//            ce.areaPoa();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EjerciciosFiscales obtenerAnioRegistro(Short idC) {
        return ecp.mostrarEjercicioFiscaleses(idC);
    }
    
    public EjerciciosFiscales obtenerAnioRegistroActivo(Short anio) {
        return ecp.mostrarEjercicioFiscalAnio(anio);
    }


    public void recargarPag() {
        Faces.refresh();
    }

    public Calendarioevaluacionpoa buscarCalendarioPOA(Procesopoa p,Integer celEva,Short ef) {
        try {
            Calendarioevaluacionpoa calendarioevaluacionpoa = new Calendarioevaluacionpoa();
            List<Calendarioevaluacionpoa> calendarioPoaActivo = new ArrayList<>();
            calendarioPoaActivo.clear();
            calendarioPoaActivo = euch.mostrarCalendarioevaluacionpoas();
            List<Calendarioevaluacionpoa> calendarioevaluacionpoas = new ArrayList<>();
            calendarioevaluacionpoas.clear();
            calendarioPoaActivo.forEach((t) -> {
                if ((new Date().before(t.getFechaFin()) && new Date().after(t.getFechaInicio())) && t.getEstapa().equals("Evaluación")) {
                    calendarioevaluacionpoas.add(t);
                }
            });
            List<Permisosevaluacionpoaex> ps = euch.mostrarPermisosEvaluacionExtemporaneaPOA(new Date(), p);
            ps.forEach((t) -> {
                if (t.getEvaluacionPOA().getEvaluacionPOA() >= 3) {
                    Calendarioevaluacionpoa c = new Calendarioevaluacionpoa();
                    c = t.getEvaluacionPOA();
                    c.setFechaInicio(t.getFechaApertura());
                    c.setFechaFin(t.getFechaCierre());
                    calendarioevaluacionpoas.add(c);
                }
            });
            List<Calendarioevaluacionpoa> c = new ArrayList<>();
            List<Calendarioevaluacionpoa> c2 = new ArrayList<>();
            c = calendarioevaluacionpoas.stream().filter(t -> Objects.equals(t.getEvaluacionPOA(), celEva)).collect(Collectors.toList());
            c2 = calendarioevaluacionpoas.stream().filter(t -> t.getEstapa().equals("Evaluación")).collect(Collectors.toList());
            if (!c.isEmpty()) {
                calendarioevaluacionpoa = c.get(0);
            } else if (!c2.isEmpty()) {
                calendarioevaluacionpoa = c2.get(0);
            } else {
                calendarioevaluacionpoa = new Calendarioevaluacionpoa(20, new Date(), new Date(), "No hay mes activo","Evaluación", false,ef);
                calendarioevaluacionpoas.add(calendarioevaluacionpoa);
            }
            return calendarioevaluacionpoa;
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(UtilidadesPOA.class.getName()).log(Level.SEVERE, null, ex);
            return new Calendarioevaluacionpoa(20, new Date(), new Date(), "No hay mes activo","Evaluación", false,ef);
        }
    }

    public Boolean buscarCalendarioPOA(String evento, Procesopoa p) {
        try {
            Boolean b =Boolean.FALSE;
            Calendarioevaluacionpoa calendarioevaluacionpoa = new Calendarioevaluacionpoa();
            List<Calendarioevaluacionpoa> calendarioPoaActivo = new ArrayList<>();
            calendarioPoaActivo.clear();
            calendarioPoaActivo = euch.mostrarCalendarioevaluacionpoas();
            List<Calendarioevaluacionpoa> calendarioevaluacionpoas = new ArrayList<>();
            calendarioevaluacionpoas.clear();
            calendarioevaluacionpoas=calendarioPoaActivo.stream().filter(t -> Objects.equals(t.getMesEvaluacion(), evento)).collect(Collectors.toList());
            List<Permisosevaluacionpoaex> ps = euch.mostrarPermisosEvaluacionExtemporaneaPOA(new Date(), p).stream().filter(t -> t.getEvaluacionPOA().getMesEvaluacion().equals(evento)).collect(Collectors.toList());
            
            if(ps.isEmpty()){                
                calendarioevaluacionpoa=calendarioevaluacionpoas.get(calendarioevaluacionpoas.size()-1);
            } else {
                Permisosevaluacionpoaex p1=new Permisosevaluacionpoaex();
                p1=ps.get(0);
                calendarioevaluacionpoa = calendarioevaluacionpoas.get(calendarioevaluacionpoas.size()-1);
                calendarioevaluacionpoa.setFechaInicio(p1.getFechaApertura());
                calendarioevaluacionpoa.setFechaFin(p1.getFechaCierre());
            }
            
            if(periodoActivo(calendarioevaluacionpoa.getFechaInicio(),calendarioevaluacionpoa.getFechaFin())){
                return Boolean.TRUE;
            }else{
                return Boolean.FALSE;
            }
                                
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(UtilidadesPOA.class.getName()).log(Level.SEVERE, null, ex);
            return Boolean.FALSE;
        }
    }
    
    public Boolean periodoActivo(Date inicio, Date fin){
        if ((new Date().before(fin) && new Date().after(inicio))) {
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }
}
