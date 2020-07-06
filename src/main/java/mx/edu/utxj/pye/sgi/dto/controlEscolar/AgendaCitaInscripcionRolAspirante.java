package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TramitesEscolares;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AgendaCitaInscripcionRolAspirante {
    @Getter @NonNull Boolean tieneAcceso = false;
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    /*
    Representa el trameite de inscripción activo y el proceso de inscripcion activo
     */
    @Getter @NonNull private EventoEscolar eventoEscolar;
    @Getter @NonNull private TramitesEscolares tramitesEscolar;
    @Getter @NonNull private ProcesosInscripcion procesosInscripcion;

    /*

     */
    @Getter @Setter private Boolean cambiarFecha,dis;
    @Getter @Setter private Date minDay;
    @Getter @Setter private List<Date> invalidDates;
    @Getter @Setter private List<Integer> invalidDays;
    @Getter @Setter private String folio;
    @Getter @Setter private DtoCitaAspirante dtoCitaAspirante;
    @Getter @Setter private  List<String> instrucciones = new ArrayList<>();

    public void setTramitesEscolar(TramitesEscolares tramitesEscolar) {
        this.tramitesEscolar = tramitesEscolar;
    }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }

    public void setEventoEscolar(EventoEscolar eventoEscolar) {
        this.eventoEscolar = eventoEscolar;
    }

    public void setTieneAcceso(Boolean tieneAcceso) {
        this.tieneAcceso = tieneAcceso;
    }
}
