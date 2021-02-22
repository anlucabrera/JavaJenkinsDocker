package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
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
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

public class RegistroDelEstudianteRolEstudiante{

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private Estudiante estudianteConsulta;
    /**
     * Representa la referencia al rol que hace uso del modulo
     */
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
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
    
    @Getter @Setter private String curpBusqueda,nombreR,tipoCal,mensaje,evidencia;
    @Getter @Setter private Integer paso,tipo,subpaso;
    @Getter @Setter private Part fileCurp,croquis;
    
    @Getter @Setter private Boolean extran,finalizado,editarCalificaciones,puedeValidar,respuestaTrabaja;
    
    //--------------------------------------------------------------------------  Datos primera face de desarrollo
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
    
    //--------------------------------------------------------------------------  Datos Segunta face de desarrollo
    @Getter @Setter @NonNull private DtoReincorporacion.RegistroEstudiante registroEstudiante;
    @Getter @Setter @NonNull private DtoReincorporacion.EstudianteR registrDelEstudianteR;    
    @Getter @Setter @NonNull private DtoReincorporacion.RcontactoEmergencia rcontactoEmergencia;
    @Getter @Setter @NonNull private List<DtoReincorporacion.RcontactoEmergencia> emergencias;
    @Getter @Setter @NonNull private DtoReincorporacion.AcademicosCR academicosCR;
    @Getter @Setter @NonNull private DtoReincorporacion.RegDatosLaborales regDatosLaborales;
    @Getter @Setter @NonNull private DtoReincorporacion.SosioeconomicosR sosioeconomicosR;
    @Getter @Setter @NonNull private DtoReincorporacion.Familia familiaR;
    @Getter @Setter @NonNull private DtoReincorporacion.VocacionalR encuestaVocacional;
    @Getter @Setter @NonNull private List<DtoReincorporacion.Familia> familias;
    
    //--------------------------------------------------------------------------  Ubicaciones
    @Getter @Setter @NonNull private DtoReincorporacion.Ubicaciones ubicacionNacimiento;

    //--------------------------------------------------------------------------  Catralogos
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
    @Getter @Setter @NonNull private List<Estado> estadosPa,estadosMa,estadosFa,estadosCe,estadosDl;
    @Getter @Setter @NonNull private List<Municipio> municipiosOr,municipiosDo,municipiosPo,municipiosTt,municipiosIe;    
    @Getter @Setter @NonNull private List<Municipio> municipiosPa,municipiosMa,municipiosFa,municipiosCe,municipiosDl;
    @Getter @Setter @NonNull private List<Localidad> localidadsOr,localidads,localidadsIe;
    @Getter @Setter @NonNull private List<Asentamiento> asentamientosDo,asentamientosPo,asentamientosTt;
    @Getter @Setter @NonNull private List<Asentamiento> asentamientosPa,asentamientosMa,asentamientosFa,asentamientosCe,asentamientosDl;
    @Getter @Setter @NonNull private List<Iems> iemses;
    @Getter @Setter @NonNull private List<AreasUniversidad> areasAcademicasPo,areasAcademicasSo;
    @Getter @Setter @NonNull private List<AreasUniversidad> programasEducativosPo,programasEducativosSo;
    
    
    @Getter @Setter @NonNull private List<String> lateralidades;
    @Getter @Setter @NonNull private List<String> parentescos;
    @Getter @Setter @NonNull private List<String> prioridades;
    @Getter @Setter @NonNull private List<String> recursos;
    @Getter @Setter @NonNull private List<String> viviendas;
    @Getter @Setter @NonNull private List<String> transportes;

    public Boolean tieneAccesoEs(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        setEstudiante(estudiante);
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudianteConsulta = estudiante;
        this.curpBusqueda=estudiante.getAspirante().getIdPersona().getCurp();
    }

    
}
