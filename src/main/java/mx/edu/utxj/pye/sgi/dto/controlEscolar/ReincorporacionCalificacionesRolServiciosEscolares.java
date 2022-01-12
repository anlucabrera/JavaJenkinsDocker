package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.pye2.UniversidadesUT;

public class ReincorporacionCalificacionesRolServiciosEscolares extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalActivoSe;

    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad areaSe;
    
    @Getter @Setter private String tipoCal;
    @Getter @Setter private String idPlanEstudio;
    @Getter @Setter private Integer cvestudiante;
    @Getter @Setter private Integer pestaniaActiva;
    @Getter @Setter private Integer universidadActiva;
    @Getter @Setter private Integer planConsulta;
    @Getter @Setter private Boolean tipoRep;
    @Getter @Setter private Boolean filtaBaja;
    
    
    @Getter @Setter private String mesIni;
    @Getter @Setter private String mesFin;
    
    @Getter @Setter private Integer anioIni;
    @Getter @Setter private Integer anioFin;
    
    @Getter @Setter @NonNull private List<String> meses;
    @Getter @Setter @NonNull private List<Integer> anios;
    
    
    @Getter @Setter private Estudiante estudiante;
    @Getter @Setter private CalificacionesHistorialTsuOtrosPe historialTsu;
    @Getter @Setter @NonNull private DtoReincorporacion.HistorialTsu tsu;
    
    
    @Getter @Setter private List<CalificacionesHistorialTsuOtrosPe> historialCalificacionesTsus;
    @Getter @Setter private List<CalificacionesHistorialTsu> historialCalificacionesSaiiuts;
    
    @Getter @Setter @NonNull private List<Estudiante> estudiantesReincorporaciones;
    @Getter @Setter @NonNull private List<DtoReincorporacion.CalificacionesR> calificacionesTSU;
    @Getter @Setter @NonNull private List<DtoReincorporacion.CalificacionesR> calificacionesLIN;
    
    @Getter @Setter @NonNull private List<DtoReincorporacion.AlineacionCalificaciones> calificacionesR;  
    @Getter @Setter @NonNull private List<UniversidadesUT> universidades;  
    @Getter @Setter @NonNull private List<DtoReincorporacion.PlanesDeEstudioConsulta> planesDeEstudioConsultas;  
    @Getter @Setter @NonNull private List<DtoReincorporacion.ReporteReincorporaciones> reincorporacioneses;  
    @Getter @Setter @NonNull private List<DtoReincorporacion.ReporteReincorporaciones> reincorporacionesEstudiantes;  
    @Getter @Setter @NonNull private List<DtoReincorporacion.HistoricoReincorporaciones> historicoReincorporacioneses;
    @Getter @Setter @NonNull private List<PlanEstudio> planEstudios;  

    public ReincorporacionCalificacionesRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad area) {
        super(filtro);
        this.personalActivoSe = serviciosEscolares;
        this.areaSe = area;
    }

    public void setPersonalActivoSe(PersonalActivo personalActivoSe) {
        this.personalActivoSe = personalActivoSe;
    }

    
}
