package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UniversidadEgresoAspirante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.List;

public class ValidacionFichaRolAspiranteIngenieria {
    @Getter @NonNull Boolean tieneAcceso = false;
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    @Getter         @NonNull   private EventoEscolar eventoValidacion,eventoInscripcion;
    @Getter @NonNull private ProcesosInscripcion procesosInscripcion;
    // /// Datos del Apirante /////////////////
    @Getter @Setter @NonNull private DtoAspirante.General general;
    @Getter @Setter @NonNull private MedioComunicacion comunicacion;
    @Getter @Setter @NonNull private DtoAspirante.PersonaR personaD;
    @Getter @Setter @NonNull private DtoAspirante.AspiranteR aspirante,aspiranteAnterior;
    @Getter @Setter @NonNull private DtoAspirante.MedicosR dmedico;
    @Getter @Setter @NonNull private DtoAspirante.FamiliaresR dfamiliares;
    @Getter @Setter @NonNull private DtoAspirante.DomicilioR ddomicilios;
    @Getter @Setter @NonNull private DtoAspirante.AcademicosR dacademicos;
    @Getter @Setter @NonNull private DtoAspirante.EncuestaR encuesta;
    @Getter @Setter @NonNull private DtoAspirante.TutorR tutor;
    @Getter @Setter @NonNull private DtoAspiranteIng esTipo;
    @Getter @Setter @NonNull private UniversidadesUT universidadEgreso;
    @Getter @Setter @NonNull private UniversidadEgresoAspirante universidadEgresoAspirante;
    ///////////////////////
    @Getter @Setter @NonNull private List<AreasUniversidad> programasEducativosPo;

    public void setEventoValidacion(EventoEscolar eventoValidacion) { this.eventoValidacion = eventoValidacion; }

    public void setEventoInscripcion(EventoEscolar eventoInscripcion) { this.eventoInscripcion = eventoInscripcion; }

    public void setTieneAcceso(Boolean tieneAcceso) { this.tieneAcceso = tieneAcceso; }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) { this.procesosInscripcion = procesosInscripcion; }
}
