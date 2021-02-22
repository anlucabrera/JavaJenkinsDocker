package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.Part;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

public class RegistroDelEstudianteRoConsultaMultiple extends AbstractRol{

    @Getter    @NonNull    private PersonalActivo director;    
    @Getter    @NonNull    private PersonalActivo docente;    
    @Getter    @NonNull    private PersonalActivo academica; 
    @Getter    @NonNull    private PersonalActivo estudiantiles; 
    @Getter    @NonNull    private PersonalActivo psicopedadogico;
    
    @Getter    @NonNull    private AreasUniversidad programa;
    @Getter    @NonNull    private PlanEstudio planEstudio;
    
    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.OPERATIVO;
    
    @Getter @Setter @NonNull private EventoEscolar eventoActivo;
    
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    @Getter @NonNull private Integer periodoActivo;
    
    @Getter @Setter private Boolean esDi;
    @Getter @Setter private Boolean esDo;
    @Getter @Setter private Boolean esEn;
    @Getter @Setter private Boolean esSa;  
    @Getter @Setter private Boolean esSe;   
    @Getter @Setter private Boolean esPs;     
    @Getter @Setter private Integer tipoUser;
    
    @Getter @Setter private String curpBusqueda,nombreR,tipoCal,mensaje,evidencia;
    @Getter @Setter private String mensajeV;
    @Getter @Setter private Integer paso,tipo,subpaso;
    @Getter @Setter private Part fileCurp,croquis;
    
    @Getter @Setter private Date fechaInpresion;
    
    @Getter @Setter private Boolean extran,finalizado,editarCalificaciones,puedeValidar,respuestaTrabaja;
    
    @Getter    @NonNull    private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    @Getter    @NonNull    private List<AreasUniversidad> programas;
    @Getter    @NonNull    private List<PlanEstudio> planesEstudios;
    @Getter    @Setter    private Grupo grupoSelec;
    @Getter    @Setter    private List<Grupo> gruposA;
    
    @Getter @NonNull private List<Estudiante> estudiantes;
    @Getter @NonNull private Estudiante estudiante;
    
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
    @Getter @Setter @NonNull private List<DtoReincorporacion.Familia> familias;
    @Getter @Setter @NonNull private DtoReincorporacion.VocacionalR rVocacional;
    
    //--------------------------------------------------------------------------  Ubicaciones
    @Getter @Setter @NonNull private DtoReincorporacion.Ubicaciones ubicacionNacimiento;

    //--------------------------------------------------------------------------  Catralogos
    @Getter @Setter @NonNull private List<Estudiante> estudiantesReincorporaciones;
    @Getter @Setter @NonNull private List<TipoSangre> tipoSangres;
    @Getter @Setter @NonNull private List<TipoDiscapacidad> tipoDiscapacidads;
    @Getter @Setter @NonNull private List<TipoAspirante> tipoAspirantes;
    @Getter @Setter @NonNull private List<TipoEstudiante> tipoEstudiantes;
    @Getter @Setter @NonNull private List<Ocupacion> ocupacions;
//    @Getter @Setter @NonNull private List<Grupo> grupos;
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
    @Getter @Setter @NonNull private List<String> dependientes;


    public RegistroDelEstudianteRoConsultaMultiple(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }
    
    public void setDirector(PersonalActivo director) {
        this.director = director;
    }
    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }
    public void setAcademica(PersonalActivo academica) {
        this.academica = academica;
    }
    public void setEstudiantiles(PersonalActivo estudiantiles) {
        this.estudiantiles = estudiantiles;
    }
    public void setPsicopedadogico(PersonalActivo psicopedadogico) {
        this.psicopedadogico = psicopedadogico;
    }
     public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
        if(periodos != null && !periodos.isEmpty()){
            this.setPeriodo(periodos.get(0));
        }
    }
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
         if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }
    
    public void setEstudiantes(List<Estudiante> des) {
        this.estudiantes = des;
        if (estudiantes != null && !des.isEmpty()) {
            this.setEstudiante(des.get(0));
        }
    }
    
    public void setEstudiante(Estudiante de) {
        this.estudiante = de;
    }
    
    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }
    
    public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        this.planesEstudios = new ArrayList<>();
        if (areaPlanEstudioMap != null) {
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
            areaPlanEstudioMap.forEach((t, u) -> {
                this.planesEstudios.addAll(u);
            });
        }
        if (areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)) {
            this.planesEstudios = areaPlanEstudioMap.get(programa);
            if (planesEstudios != null) {
                planesEstudios.get(0);
            }
        }
    }
}
