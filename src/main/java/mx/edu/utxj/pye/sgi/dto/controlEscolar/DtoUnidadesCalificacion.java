package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ToString @EqualsAndHashCode
public class DtoUnidadesCalificacion implements Serializable {
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private List<DtoEstudiante> dtoEstudiantes;
    @Getter @Setter @NonNull private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    @Getter @Setter @NonNull private Map<DtoUnidadesCalificacionPK, DtoCapturaCalificacion> calificacionMap;
    @Getter @Setter @NonNull private Map<DtoEstudiante, TareaIntegradoraPromedio> tareaIntegradoraPromedioMap;

    public DtoUnidadesCalificacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull List<DtoEstudiante> dtoEstudiantes, @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoCargaAcademica = dtoCargaAcademica;
        this.dtoEstudiantes = dtoEstudiantes;
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        this.calificacionMap = new HashMap<>();
        this.tareaIntegradoraPromedioMap = new HashMap<>();
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoUnidadesCalificacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull DtoEstudiante dtoEstudiante;
        @Getter @Setter @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion;
    }

    public void agregarCapturaCalificacion(DtoEstudiante dtoEstudiante, DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCapturaCalificacion dtoCapturaCalificacion) throws Exception{
        if(!Objects.equals(dtoCargaAcademica.getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La carga académica no coincide con la materia especificada.");
        if(!Objects.equals(dtoUnidadConfiguracion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La unidad no coincide con la materia especificada.");
        if(!Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La línea de captura de calificaciones no coincide con la materia especificada.");
        if(!Objects.equals(dtoEstudiante.getPersona(), dtoCapturaCalificacion.getDtoEstudiante().getPersona())) throw  new Exception("Los datos del estudiante no coinciden con la línea de captura de calificación.");

        DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionPK(dtoCargaAcademica, dtoEstudiante, dtoUnidadConfiguracion);
        calificacionMap.put(pk, dtoCapturaCalificacion);
    }

    public BigDecimal getPromedioUnidad(@NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion, @NonNull DtoEstudiante dtoEstudiante){
        DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionPK(dtoCargaAcademica, dtoEstudiante, dtoUnidadConfiguracion);
        if(calificacionMap.containsKey(pk)) return calificacionMap.get(pk).getPromedio();
        else return BigDecimal.ZERO;
    }
}
