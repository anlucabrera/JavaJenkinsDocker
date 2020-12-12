package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

import java.util.List;

public class ReporteEvaluacionesRolDocente extends  AbstractRol  {
    /**
     * Representa la referencia hacia el personal docente
     */
    @Getter @NonNull private PersonalActivo docente;

    @Getter @NonNull private List<DtoReporteEvaluaciones> listReporte;

    @Getter @NonNull private StreamedContent file;

    @Getter @NonNull private DtoReporteEvaluaciones dto;
    @Getter @NonNull  private Integer idEvaluado, idPeriodo,idEvaluacion;

    public ReporteEvaluacionesRolDocente(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setListReporte(List<DtoReporteEvaluaciones> listReporte) { this.listReporte = listReporte; }

    public void setDocente(PersonalActivo docente) { this.docente = docente; }

    public void setFile(StreamedContent file) { this.file = file; }

    public void setDto(DtoReporteEvaluaciones dto) { this.dto = dto; }

    public void setIdEvaluado(Integer idEvaluado) { this.idEvaluado = idEvaluado; }

    public void setIdPeriodo(Integer idPeriodo) { this.idPeriodo = idPeriodo; }

    public void setIdEvaluacion(Integer idEvaluacion) { this.idEvaluacion = idEvaluacion; }
}
