package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

import java.util.List;
import javax.servlet.http.Part;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;

public class ReincorporacionRolServiciosEscolares extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalActivoSe;

    /**
     * Representa la referencia al evento activo de reincorporación
     */
    @Getter @Setter @NonNull private EventoEscolar eventoActivo;

    /**
     * Periodo escolar en el que se hara la reincorporación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad areaSe;
    
    @Getter @Setter private String curpBusqueda,nombreR,tipoCal,mensaje;
    @Getter @Setter private Integer paso,tipo;
    @Getter @Setter private Part fileCurp;
    
    @Getter @Setter private Boolean extran,finalizado,editarCalificaciones,puedeValidar,esEscolares;
    @Getter @Setter private Estudiante estudiante;
    
    @Getter @Setter @NonNull private DtoReincorporacion.General general;
    @Getter @Setter @NonNull private MedioComunicacion comunicacion;
    @Getter @Setter @NonNull private DtoReincorporacion.PersonaR personaD;
    @Getter @Setter @NonNull private DtoReincorporacion.AspiranteR aspirante;
    @Getter @Setter @NonNull private DtoReincorporacion.MedicosR dmedico;
    @Getter @Setter @NonNull private DtoReincorporacion.FamiliaresR dfamiliares;
    @Getter @Setter @NonNull private DtoReincorporacion.DomicilioR ddomicilios;
    @Getter @Setter @NonNull private DtoReincorporacion.AcademicosR dacademicos;    
    @Getter @Setter @NonNull private DtoReincorporacion.EncuestaR encuesta;
    @Getter @Setter @NonNull private DtoReincorporacion.TutorR tutor;
    @Getter @Setter @NonNull private DtoReincorporacion.ProcesoInscripcionRein rein;
    @Getter @Setter @NonNull private List<DtoReincorporacion.EstudianteR> estudianteR;
    @Getter @Setter @NonNull private List<DtoReincorporacion.AlineacionCalificaciones> calificacionesR;

    
    @Getter @Setter @NonNull private List<Estudiante> estudiantesReincorporaciones;
    @Getter @Setter @NonNull private List<TipoSangre> tipoSangres;
    @Getter @Setter @NonNull private List<TipoDiscapacidad> tipoDiscapacidads;
    @Getter @Setter @NonNull private List<TipoAspirante> tipoAspirantes;
    @Getter @Setter @NonNull private List<TipoEstudiante> tipoEstudiantes;
    @Getter @Setter @NonNull private List<Ocupacion> ocupacions;
    @Getter @Setter @NonNull private List<Grupo> grupos;
    @Getter @Setter @NonNull private List<Grupo> gruposRegistros;
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
    @Getter @Setter @NonNull private List<AreasUniversidad> areasAcademicasPo,areasAcademicasSo;
    @Getter @Setter @NonNull private List<AreasUniversidad> programasEducativosPo,programasEducativosSo;
    

    public ReincorporacionRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad area) {
        super(filtro);
        this.personalActivoSe = serviciosEscolares;
        this.areaSe = area;
    }

    public void setPersonalActivoSe(PersonalActivo personalActivoSe) {
        this.personalActivoSe = personalActivoSe;
    }

    
}
