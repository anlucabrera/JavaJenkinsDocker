package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import javax.servlet.http.Part;
import java.util.Date;
import java.util.List;

public class RegistroFichaAdmisionRolAspiranteIngSaiiut {

    @Getter @NonNull Boolean tieneAcceso = false;

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    @Getter @Setter @NonNull private DtoAspiranteIng aspiranteIng;
    /**
     * Representa al evento escolar de registro de fichas
     */
    @Getter         @NonNull   private EventoEscolar eventoEscolar;
    @Getter         @NonNull    private ProcesosInscripcion procesosInscripcion;
    /////////////////////////////////////////////////////
    @Getter         @Setter     private     String curpBusqueda,pwdPer;
    @Getter         @Setter     private     Integer paso, folioAspirante;
    @Getter         @Setter     private     Part fileCurp;
    @Getter         @Setter     private     Boolean extran,finalizado,dialogEn;
    @Getter         @Setter     private AreasUniversidad areaApO, areaAsO;
    @Getter         @Setter     private     String valor, resultadoEn;

    /////////////////////Pasos //////////////////
    @Getter         @Setter     private     Integer step;
    @Getter         @Setter     private     Boolean tab1,tab2,tab3,tab4,tab5,tab6,tab7, tab8,tab9;
    // /// Datos del Apirante /////////////////
    @Getter @Setter @NonNull private DtoAspirante.General general;
    @Getter @Setter @NonNull private MedioComunicacion comunicacion;
    @Getter @Setter @NonNull private DtoAspirante.PersonaR personaD;
    @Getter @Setter @NonNull private DtoAspirante.AspiranteR aspirante;
    @Getter @Setter @NonNull private DtoAspirante.MedicosR dmedico;
    @Getter @Setter @NonNull private DtoAspirante.FamiliaresR dfamiliares;
    @Getter @Setter @NonNull private DtoAspirante.DomicilioR ddomicilios;
    @Getter @Setter @NonNull private DtoAspirante.AcademicosR dacademicos;
    @Getter @Setter @NonNull private DtoAspirante.EncuestaR encuesta;
    @Getter @Setter @NonNull private DtoAspirante.TutorR tutor;
    @Getter @Setter @NonNull private UniversidadEgresoAspirante universidadEgresoAspirante;
    // Datos para el registro de la cita

    @Getter @Setter private TramitesEscolares tramiteCita;
    @Getter @Setter private Boolean cambiarFecha,dis;
    @Getter @Setter private Date minDay;
    @Getter @Setter private List<Date> invalidDates;
    @Getter @Setter private List<Integer> invalidDays;
    @Getter @Setter private String folio;
    @Getter @Setter private DtoCitaAspirante dtoCitaAspirante;

    //Datos necesario para registro
    @Getter @Setter @NonNull private List<TipoSangre> tipoSangres;
    @Getter @Setter @NonNull private List<TipoDiscapacidad> tipoDiscapacidads;
    @Getter @Setter @NonNull private List<Ocupacion> ocupacions;
    @Getter @Setter @NonNull private List<Escolaridad> escolaridads;
    @Getter @Setter @NonNull private List<LenguaIndigena> lenguaIndigenas;
    @Getter @Setter @NonNull private List<MedioDifusion> medioDifusions;
    @Getter @Setter @NonNull private List<EspecialidadCentro> especialidadCentros;
    @Getter @Setter @NonNull private List<Sistema> sistemas;
    @Getter @Setter @NonNull private List<Pais> paisesN;
    @Getter @Setter @NonNull private List<Estado> estadosOr,estadosDo,estadosPo,estadosTt,estadosIe;
    @Getter @Setter @NonNull private List<Municipio> municipiosOr,municipiosDo,municipiosPo,municipiosTt,municipiosIe;
    @Getter @Setter @NonNull private List<Localidad> localidadsOr,localidads,localidadsIe;
    @Getter @Setter @NonNull private List<Asentamiento> asentamientosDo,asentamientosPo,asentamientosTt;
    @Getter @Setter @NonNull private List<Iems> iemses;
    @Getter @Setter @NonNull private List<AreasUniversidad> areasAcademicas;
    @Getter @Setter @NonNull private List<AreasUniversidad> programasEducativosPo,programasEducativosSo,carrerasEv;
    @Getter @Setter @NonNull private  List<Generos> generos;
    @Getter @Setter @NonNull private  List<UniversidadesUT> universidades;
    @Getter @Setter @NonNull private UniversidadesUT universidad;

    public void setEventoEscolar(EventoEscolar eventoEscolar) {
        this.eventoEscolar = eventoEscolar;
    }

    public void setTieneAcceso(Boolean tieneAcceso) { this.tieneAcceso = tieneAcceso; }

    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) {
        this.procesosInscripcion = procesosInscripcion;
    }
}
