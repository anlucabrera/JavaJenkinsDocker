package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@ToString @EqualsAndHashCode
public class DtoUnidadesCalificacion implements Serializable {
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private List<DtoEstudiante> dtoEstudiantes;
    @Getter @Setter @NonNull private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    @Getter @Setter @NonNull private Map<DtoUnidadesCalificacionPK, DtoCapturaCalificacion> calificacionMap;
    @Getter @Setter @NonNull private Map<DtoEstudiante, TareaIntegradoraPromedio> tareaIntegradoraPromedioMap;
    @Getter @Setter @NonNull private Map<DtoNivelacionPK, DtoCalificacionNivelacion> nivelacionMap;
    @Getter @Setter @NonNull private Boolean activaPorFecha;
    @Getter @Setter @NonNull private Boolean activaPorAperturaExtemporanea;

    public DtoUnidadesCalificacion(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull List<DtoEstudiante> dtoEstudiantes, @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones, @NonNull Boolean activaPorFecha, @NonNull Boolean activaPorAperturaExtemporanea) {
        this.dtoCargaAcademica = dtoCargaAcademica;
        this.dtoEstudiantes = dtoEstudiantes;
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        this.calificacionMap = new HashMap<>();
        this.tareaIntegradoraPromedioMap = new HashMap<>();
        this.nivelacionMap = new HashMap<>();
        this.activaPorFecha = activaPorFecha;
        this.activaPorAperturaExtemporanea = activaPorAperturaExtemporanea;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoUnidadesCalificacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull DtoEstudiante dtoEstudiante;
        @Getter @Setter @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoNivelacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull DtoEstudiante dtoEstudiante;
    }

    public void agregarCapturaCalificacion(DtoEstudiante dtoEstudiante, DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCapturaCalificacion dtoCapturaCalificacion) throws Exception{
        if(!Objects.equals(dtoCargaAcademica.getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La carga académica no coincide con la materia especificada.");
        if(!Objects.equals(dtoUnidadConfiguracion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La unidad no coincide con la materia especificada.");
        if(!Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getMateria(), dtoCargaAcademica.getMateria())) throw new Exception("La línea de captura de calificaciones no coincide con la materia especificada.");
        if(!Objects.equals(dtoEstudiante.getPersona(), dtoCapturaCalificacion.getDtoEstudiante().getPersona())) throw  new Exception("Los datos del estudiante no coinciden con la línea de captura de calificación.");

        DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionPK(dtoCargaAcademica, dtoEstudiante, dtoUnidadConfiguracion);
//        System.out.println("dtoCapturaCalificacion = " + dtoEstudiante.getInscripciones().stream().map(DtoInscripcion::getInscripcion).map(Estudiante::getMatricula).collect(Collectors.toList()));
//        if(dtoEstudiante.getAspirante().getIdAspirante() == 917)
//            System.out.println("dtoCapturaCalificacion.getPromedio() = " + dtoCapturaCalificacion.getPromedio());
        calificacionMap.put(pk, dtoCapturaCalificacion);
    }

    public BigDecimal getPromedioUnidad(DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoEstudiante dtoEstudiante){
        try{
            DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionPK(dtoCargaAcademica, dtoEstudiante, dtoUnidadConfiguracion);
            if(calificacionMap.containsKey(pk)) {
//            if(dtoEstudiante.getInscripcionActiva().getInscripcion().getMatricula() == 190575)
//                System.out.println("calificacionMap = " + calificacionMap.get(pk).getPromedio());
                return calificacionMap.get(pk).getPromedio();
            }
            else return BigDecimal.ZERO;
        }catch (NullPointerException e){
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
