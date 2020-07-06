package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import com.itextpdf.text.DocumentException;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AgendaCitaInscripcionRolAspirante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCitaAspirante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCitaInscripcionAspirante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.enums.*;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Named(value = "agendaCitaInscripcionAspirante")
@ViewScoped
public class AgendaCitaInscripcionAspirante extends ViewScopedRol implements Desarrollable {
    @EJB private EjbPropiedades ep;
    @EJB private EjbCitaInscripcionAspirante ejbCita;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    @Getter private Boolean cargado = false;
    @Getter @Setter AgendaCitaInscripcionRolAspirante rol;

    @PostConstruct
    public void init(){ if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ASPIRANTE)) return;
    cargado = true;
    setVistaControlador(ControlEscolarVistaControlador.AGENDA_CITA_ASPIRANTE);
        ResultadoEJB<Boolean> resAcceso = ejbCita.verficaAcceso(logonMB.getUsuarioTipo());
        if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso
        ResultadoEJB<Boolean> resValidacion = ejbCita.verficaAcceso(logonMB.getUsuarioTipo());
        if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar
        rol = new AgendaCitaInscripcionRolAspirante();
        tieneAcceso = resAcceso.getValor();
        rol.setTieneAcceso(tieneAcceso);
        if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
        ResultadoEJB<ProcesosInscripcion> resProceso= ejbCita.getProcesosInscripcionActivo();
        if(resProceso.getCorrecto()==true){rol.setProcesosInscripcion(resProceso.getValor());}
        else {tieneAcceso=false;rol.setTieneAcceso(tieneAcceso);}
        ResultadoEJB<EventoEscolar> resEvento = ejbCita.verificaEvento();
        mostrarMensajeResultadoEJB(resAcceso);
        if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
        rol.setEventoEscolar(resEvento.getValor());
        ResultadoEJB<TramitesEscolares> resTramite= ejbCita.getTramiteInscripcionActivo(rol.getProcesosInscripcion());//Busca si existe el tramite aperturado
        if(resTramite.getCorrecto()==true){rol.setTramitesEscolar(resTramite.getValor()); }
        else { mostrarMensajeResultadoEJB(resTramite); tieneAcceso=false; return;}
        // Se busca un proceso de inscripción activo
        ResultadoEJB<ProcesosInscripcion> resProcesoI = ejbCita.getProcesosInscripcionActivo();
        if(resProcesoI.getCorrecto()==true){rol.setProcesosInscripcion(resProcesoI.getValor());}
        else {tieneAcceso=false;}
        // ----------------------------------------------------------------------------------------------------------------------------------------------------------
        if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
        if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
        rol.setNivelRol(NivelRol.OPERATIVO);
        rol.setCambiarFecha(Boolean.FALSE);
        diasNoDisponible();
        //---- Instrucciones-----
        rol.getInstrucciones().add("Ingresar el número de folio de tu ficha de admisión");
        rol.getInstrucciones().add("El folio se encuentra ubicado debajo del código QR del formato de tu ficha de admisión.");
        rol.getInstrucciones().add("Si no cuentas con tu folio, debes realizar el registro de tu ficha.");
        rol.getInstrucciones().add("Seleccione la fecha deseada para su cita de entrega de documentos y posteriormente confirme su cita.");
        rol.getInstrucciones().add("Una vez confirmada su cita, podrá descargar su comprante, mismo que será enviado a su correo electrónico previamente registrado en su ficha de admisión.");
        rol.getInstrucciones().add("Si desea cambiar la fecha de su cita, seleccione 'Reagendar cita'");
    }

    /**
     * Busca los días que no estan disponibles
     * Los dias que no estan disponibles se sacan según el numero de citas que estan registradas por dia posible
     */
    public void diasNoDisponible(){
        try{
            List<Date> diasInvalidos= new ArrayList<>();
            Date today = new Date();
            rol.setInvalidDays(new ArrayList<>());
            rol.getInvalidDays().add(0);
            long oneDay = 24 * 60 * 60 * 1000;
            Date newDate= new Date(today.getTime() - (1 *oneDay));
            rol.setMinDay(newDate);
            //Busca la fechas posibles
            ResultadoEJB<List<Date>> resDiasIn = ejbCita.getListFechasPosibles(rol.getTramitesEscolar());
            if(resDiasIn.getCorrecto()==true){
                diasInvalidos = resDiasIn.getValor();
                rol.setInvalidDates(diasInvalidos);
            }else {mostrarMensajeResultadoEJB(resDiasIn);}
        }catch (Exception e){mostrarExcepcion(e);}
    }

    /**
     * Empaqueta un registro por folio
     */
    public void getRegistro(){
        try{
            ResultadoEJB<DtoCitaAspirante> resPack= ejbCita.packAspiranteCita(rol.getFolio(),rol.getTramitesEscolar(),rol.getProcesosInscripcion());
            if(resPack.getCorrecto()==true){
                rol.setDtoCitaAspirante(resPack.getValor());
                if(rol.getDtoCitaAspirante().getTieneCita()==true){rol.setDis(Boolean.TRUE);}
                else if(rol.getDtoCitaAspirante().getTieneCita()==false){rol.setDis(Boolean.FALSE);}
            }else {mostrarMensajeResultadoEJB(resPack);}
        }catch (Exception e){ mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "agendaCitaAspirante";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
//        map.entrySet().forEach(System.out::println);
        return mostrar(request, map.containsValue(valor));
    }
    public void changeFecha(){
        try{
            ResultadoEJB<CitasAspirantes> resCita = ejbCita.getCitabyAspirante(rol.getDtoCitaAspirante().getAspirante(),rol.getTramitesEscolar());
            if(resCita.getCorrecto()==true){
                rol.getDtoCitaAspirante().setCitasAspirantes(resCita.getValor());
                rol.getDtoCitaAspirante().setOperacion(Operacion.ACTUALIZAR);
                rol.getDtoCitaAspirante().getCitasAspirantes().setStatus(EstatusCita.REAGENDADA.getLabel());
                ResultadoEJB<CitasAspirantes> resAc= ejbCita.operacionesCitaAspirante(rol.getDtoCitaAspirante());
                if(resAc.getCorrecto()==true){
                    rol.setCambiarFecha(Boolean.TRUE);
                    rol.setDis(Boolean.FALSE);
                    rol.getDtoCitaAspirante().setTieneCita(false);
                    rol.getDtoCitaAspirante().setOperacion(Operacion.PERSISTIR);
                    CitasAspirantes newCita = new CitasAspirantes();
                    CitasAspirantesPK pk = new CitasAspirantesPK();
                    pk.setIdTramite(rol.getTramitesEscolar().getIdTramite());
                    pk.setIdAspirante(rol.getDtoCitaAspirante().getAspirante().getIdAspirante());
                    newCita.setCitasAspirantesPK(pk);
                    rol.getDtoCitaAspirante().setCitasAspirantes(newCita);
                    mostrarMensajeResultadoEJB(resAc);
                }
                else {mostrarMensajeResultadoEJB(resAc);}

            }else {mostrarMensajeResultadoEJB(resCita);}

        }catch (Exception e){mostrarExcepcion(e);}
    }

    public void saveCita(){
        try{
            ResultadoEJB<Integer> resFolio= ejbCita.generarFolio(rol.getTramitesEscolar());
            if(resFolio.getCorrecto()==true){
                CitasAspirantesPK pk= new CitasAspirantesPK();
                pk.setIdAspirante(rol.getDtoCitaAspirante().getAspirante().getIdAspirante());
                pk.setIdTramite(rol.getTramitesEscolar().getIdTramite());
                CitasAspirantes cita = new CitasAspirantes();
                cita.setCitasAspirantesPK(pk);
                cita.setFechaCita(rol.getDtoCitaAspirante().getCitasAspirantes().getFechaCita());
                cita.setStatus(EstatusCita.CONFIRMADA.getLabel());
                rol.getDtoCitaAspirante().setOperacion(Operacion.PERSISTIR);
                cita.setFolioCita(resFolio.getValor());
                rol.getDtoCitaAspirante().setCitasAspirantes(cita);
                ResultadoEJB<CitasAspirantes> resCita = ejbCita.operacionesCitaAspirante(rol.getDtoCitaAspirante());
                if(resCita.getCorrecto()==true){
                    rol.getDtoCitaAspirante().setCitasAspirantes(resCita.getValor());
                    rol.setCambiarFecha(Boolean.TRUE);
                    rol.setDis(Boolean.TRUE);
                }else {mostrarMensajeResultadoEJB(resCita);}
            }else {mostrarMensajeResultadoEJB(resFolio);}

        }catch (Exception e){mostrarExcepcion(e);}
    }
    /*
    Genera la ficha de admisión
     */
    public void downloadComprobante() throws IOException, DocumentException {
        try{
            ResultadoEJB<Boolean> resFicha = ejbCita.generarComprobante(rol.getDtoCitaAspirante(),new Date(),rol.getTramitesEscolar());
            if(resFicha.getCorrecto()==true){
                mostrarMensajeResultadoEJB(resFicha);
            }
            else {mostrarMensajeResultadoEJB(resFicha);}
        }catch (Exception e){mostrarExcepcion(e);}

    }


}
