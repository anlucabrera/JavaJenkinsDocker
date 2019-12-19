package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ToString
@EqualsAndHashCode
public class DtoUnidadesCalificacionEstudiante implements Serializable {
    @Getter @Setter @NonNull private Estudiante estudiante;
    @Getter @Setter @NonNull private List<DtoCargaAcademica> dtoCargaAcademicas;
    @Getter @Setter @NonNull private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    @Getter @Setter @NonNull private Map<DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK, DtoCapturaCalificacionEstudiante> calificacionMap;
    @Getter @Setter @NonNull private Map<DtoCargaAcademica, TareaIntegradoraPromedio> tareaIntegradoraPromedioMap;
    @Getter @Setter @NonNull private Map<DtoUnidadesCalificacionEstudiante.DtoNivelacionPK, DtoCalificacionNivelacion> nivelacionMap;

    public DtoUnidadesCalificacionEstudiante(@NonNull Estudiante estudiante, @NonNull List<DtoCargaAcademica> dtoCargaAcademicas, @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.estudiante = estudiante;
        this.dtoCargaAcademicas = dtoCargaAcademicas;
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        this.calificacionMap = new HashMap<>();
        this.tareaIntegradoraPromedioMap = new HashMap<>();
        this.nivelacionMap = new HashMap<>();
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoUnidadesCalificacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoNivelacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull Estudiante estudiante;
    }

    public void agregarCapturaCalificacion(DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCapturaCalificacionEstudiante dtoCapturaCalificacion) throws Exception{
        if(!Objects.equals(dtoCargaAcademica.getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La carga académica no coincide con la materia especificada.");
        if(!Objects.equals(dtoUnidadConfiguracion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La unidad no coincide con la materia especificada.");
        if(!Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La línea de captura de calificaciones no coincide con la materia especificada.");
        if(!Objects.equals(estudiante.getAspirante().getIdPersona(), dtoCapturaCalificacion.getEstudiante().getAspirante().getIdPersona())) throw  new Exception("Los datos del estudiante no coinciden con la línea de captura de calificación.");

        DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK(dtoCargaAcademica, estudiante, dtoUnidadConfiguracion);
        calificacionMap.put(pk, dtoCapturaCalificacion);
    }

    public BigDecimal getPromedioUnidad(@NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion, @NonNull DtoCargaAcademica dtoCargaAcademica){
        DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK(dtoCargaAcademica, estudiante, dtoUnidadConfiguracion);
        if(calificacionMap.containsKey(pk)) {
            return calificacionMap.get(pk).getPromedio();
        }
        else return BigDecimal.ZERO;
    }

    public String getEscala(@NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion, @NonNull DtoCargaAcademica dtoCargaAcademica){
        BigDecimal promedioUnidad = getPromedioUnidad(dtoUnidadConfiguracion, dtoCargaAcademica);
        String escala = "";
        if(promedioUnidad.compareTo(BigDecimal.valueOf(0)) == 0 || promedioUnidad.compareTo(BigDecimal.valueOf(8)) < 0){
            escala = "NA";
        }
        if(promedioUnidad.compareTo(BigDecimal.valueOf(10)) == 0){
            escala = "AU";
            if(dtoUnidadConfiguracion.getUnidadMateria().getIntegradora() == Boolean.TRUE){
                escala =  "CA";
            }
        }
        if((promedioUnidad.compareTo(BigDecimal.valueOf(9)) >= 0) && (promedioUnidad.compareTo(BigDecimal.valueOf(10)) < 0)){
            escala = "DE";
            if(dtoUnidadConfiguracion.getUnidadMateria().getIntegradora() == Boolean.TRUE){
                escala = "CD";
            }
        }
        if(promedioUnidad.compareTo(BigDecimal.valueOf(8)) >= 0 && promedioUnidad.compareTo(BigDecimal.valueOf(9)) < 0){
            escala = "SA";
            if(dtoUnidadConfiguracion.getUnidadMateria().getIntegradora() == Boolean.TRUE){
                escala = "CO";
            }
        }
        return escala;
    }
}
