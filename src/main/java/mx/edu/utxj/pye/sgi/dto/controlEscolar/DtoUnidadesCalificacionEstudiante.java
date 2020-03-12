package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class DtoUnidadesCalificacionEstudiante implements Serializable {
    @Getter @Setter @NonNull private Estudiante estudiante;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademicas;
    @Getter @Setter @NonNull private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones = new ArrayList<>();
    @Getter @Setter @NonNull private Map<DtoUnidadesCalificacionPK, DtoCapturaCalificacionEstudiante> calificacionMap = new HashMap<>();
    @Getter @Setter @NonNull private Map<DtoCargaAcademica, TareaIntegradoraPromedio> tareaIntegradoraPromedioMap = new HashMap<>();
    @Getter @Setter @NonNull private Map<DtoNivelacionPK, DtoCalificacionNivelacion> nivelacionMap = new HashMap<>();

    public DtoUnidadesCalificacionEstudiante(@NonNull Estudiante estudiante, @NonNull DtoCargaAcademica dtoCargaAcademicas, @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.estudiante = estudiante;
        this.dtoCargaAcademicas = dtoCargaAcademicas;
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        this.calificacionMap = new HashMap<>();
        this.tareaIntegradoraPromedioMap = new HashMap<>();
        this.nivelacionMap = new HashMap<>();
    }

    public static class DtoUnidadesCalificacionPK{
        @Getter @Setter @NonNull DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion;

        public DtoUnidadesCalificacionPK(@NonNull DtoCargaAcademica dtoCargaAcademica, @NonNull Estudiante estudiante, @NonNull DtoUnidadConfiguracion dtoUnidadConfiguracion) {
            this.dtoCargaAcademica = dtoCargaAcademica;
            this.estudiante = estudiante;
            this.dtoUnidadConfiguracion = dtoUnidadConfiguracion;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DtoUnidadesCalificacionPK)) return false;
            DtoUnidadesCalificacionPK that = (DtoUnidadesCalificacionPK) o;
            return getDtoCargaAcademica().equals(that.getDtoCargaAcademica()) &&
                    getEstudiante().equals(that.getEstudiante()) &&
                    getDtoUnidadConfiguracion().equals(that.getDtoUnidadConfiguracion());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getDtoCargaAcademica(), getEstudiante(), getDtoUnidadConfiguracion());
        }

        @Override
        public String toString() {
            return "DtoUnidadesCalificacionPK{" +
                    "dtoCargaAcademica=" + dtoCargaAcademica +
                    ", estudiante=" + estudiante +
                    ", dtoUnidadConfiguracion=" + dtoUnidadConfiguracion +
                    '}';
        }
    }

    public static class DtoNivelacionPK{
        @Getter @Setter
        DtoCargaAcademica dtoCargaAcademica;
        @Getter @Setter
        Estudiante estudiante;

        public DtoNivelacionPK(DtoCargaAcademica dtoCargaAcademica, Estudiante estudiante) {
            this.dtoCargaAcademica = dtoCargaAcademica;
            this.estudiante = estudiante;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DtoNivelacionPK)) return false;
            DtoNivelacionPK that = (DtoNivelacionPK) o;
            return getDtoCargaAcademica().equals(that.getDtoCargaAcademica()) &&
                    getEstudiante().equals(that.getEstudiante());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getDtoCargaAcademica(), getEstudiante());
        }

        @Override
        public String toString() {
            return "DtoNivelacionPK{" +
                    "dtoCargaAcademica=" + dtoCargaAcademica +
                    ", estudiante=" + estudiante +
                    '}';
        }
    }

    public void agregarCapturaCalificacion(DtoCargaAcademica dtoCargaAcademica, DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCapturaCalificacionEstudiante dtoCapturaCalificacion) throws Exception{
        if(!Objects.equals(dtoCargaAcademica.getCargaAcademica().getCarga(), dtoUnidadConfiguracion.getDtoCargaAcademica().getCargaAcademica().getCarga())) throw new Exception("La carga académica no coincide con la materia especificada.");
        if(!Objects.equals(dtoCargaAcademica.getCargaAcademica().getCarga(), dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica().getCarga())) throw new Exception("La unidad no coincide con la materia especificada.");
        if(!Objects.equals(dtoCapturaCalificacion.getDtoCargaAcademica().getCargaAcademica().getCarga(), dtoUnidadConfiguracion.getDtoCargaAcademica().getCargaAcademica().getCarga())) throw new Exception("La línea de captura de calificaciones no coincide con la materia especificada.");

        DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK(dtoCargaAcademica, estudiante, dtoUnidadConfiguracion);
        calificacionMap.put(pk, dtoCapturaCalificacion);
    }

    public BigDecimal getPromedioUnidad(DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCargaAcademica dtoCargaAcademica){
        if (dtoUnidadConfiguracion == null)return BigDecimal.ZERO;
        if (dtoCargaAcademica == null)return BigDecimal.ZERO;
        if(dtoUnidadConfiguracion.getDtoCargaAcademica().getCargaAcademica().getCarga().equals(dtoCargaAcademica.getCargaAcademica().getCarga())){
            DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK pk = new DtoUnidadesCalificacionEstudiante.DtoUnidadesCalificacionPK(dtoCargaAcademica, estudiante, dtoUnidadConfiguracion);
            if(calificacionMap.containsKey(pk)) {
                return calificacionMap.get(pk).getPromedio();
            }
            else return BigDecimal.ZERO;
        }else return BigDecimal.ZERO;

    }

    public String getEscala(DtoUnidadConfiguracion dtoUnidadConfiguracion, DtoCargaAcademica dtoCargaAcademica){
        if (dtoUnidadConfiguracion == null)return "";
        if (dtoCargaAcademica == null)return "";
        if(dtoUnidadConfiguracion.getDtoCargaAcademica().getCargaAcademica().getCarga().equals(dtoCargaAcademica.getCargaAcademica().getCarga())){
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
        }else return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DtoUnidadesCalificacionEstudiante)) return false;
        DtoUnidadesCalificacionEstudiante that = (DtoUnidadesCalificacionEstudiante) o;
        return getEstudiante().equals(that.getEstudiante()) &&
                getDtoCargaAcademicas().equals(that.getDtoCargaAcademicas()) &&
                getDtoUnidadConfiguraciones().equals(that.getDtoUnidadConfiguraciones()) &&
                getCalificacionMap().equals(that.getCalificacionMap()) &&
                getTareaIntegradoraPromedioMap().equals(that.getTareaIntegradoraPromedioMap()) &&
                getNivelacionMap().equals(that.getNivelacionMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEstudiante(), getDtoCargaAcademicas(), getDtoUnidadConfiguraciones(), getCalificacionMap(), getTareaIntegradoraPromedioMap(), getNivelacionMap());
    }

    @Override
    public String toString() {
        return "DtoUnidadesCalificacionEstudiante{" +
                "estudiante=" + estudiante +
                ", dtoCargaAcademicas=" + dtoCargaAcademicas +
                ", dtoUnidadConfiguraciones=" + dtoUnidadConfiguraciones +
                ", calificacionMap=" + calificacionMap +
                ", tareaIntegradoraPromedioMap=" + tareaIntegradoraPromedioMap +
                ", nivelacionMap=" + nivelacionMap +
                '}';
    }
}
