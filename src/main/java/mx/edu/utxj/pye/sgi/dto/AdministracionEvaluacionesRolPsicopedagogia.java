package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesAreas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class AdministracionEvaluacionesRolPsicopedagogia extends AbstractRol {


    /**
     * Representa la referencia al personal de psicopedagogia
     */
    @Getter @NonNull private PersonalActivo personalPsicopedagogia;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;

    @Getter @NonNull private PeriodosEscolares periodoActivo, periodoSeleccionado;

    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    @Getter @NonNull private PeriodoEscolarFechas periodoActivoFechas;

    @Getter @NonNull private  List<DtoAdministracionEvaluaciones> evaluaciones;

    @Getter @NonNull private Evaluaciones nuevaEvaluacion, evaluacionSeleccionada;

    @Getter @NonNull private AperturaVisualizacionEncuestas apertura, aperturaEdit;

    @Getter @NonNull private  List<EvaluacionesAreas> listaTiposEv;

    @Getter @NonNull private EvaluacionesAreas tipoEvalucionSeleecionada, tipoEvEdit;

    @Getter @NonNull private Boolean evalucionEx;

    public AdministracionEvaluacionesRolPsicopedagogia(Filter<PersonalActivo> filtro, PersonalActivo personalPsicopedagogia, AreasUniversidad programa) {
        super(filtro);
        this.personalPsicopedagogia = personalPsicopedagogia;
        this.programa = programa;
    }


    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) { this.personalPsicopedagogia = personalPsicopedagogia; }

    public void setPrograma(AreasUniversidad programa) { this.programa = programa; }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) { this.periodoActivo = periodoActivo; }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) { this.periodoSeleccionado = periodoSeleccionado; }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) { this.periodosEscolares = periodosEscolares; }

    public void setNuevaEvaluacion(Evaluaciones nuevaEvaluacion) { this.nuevaEvaluacion = nuevaEvaluacion; }

    public void setApertura(AperturaVisualizacionEncuestas apertura) { this.apertura = apertura; }

    public void setEvaluaciones(List<DtoAdministracionEvaluaciones> evaluaciones) { this.evaluaciones = evaluaciones; }

    public void setEvaluacionSeleccionada(Evaluaciones evaluacionSeleccionada) { this.evaluacionSeleccionada = evaluacionSeleccionada; }

    public void setListaTiposEv(List<EvaluacionesAreas> listaTiposEv) { this.listaTiposEv = listaTiposEv; }

    public void setTipoEvalucionSeleecionada(EvaluacionesAreas tipoEvalucionSeleecionada) { this.tipoEvalucionSeleecionada = tipoEvalucionSeleecionada; }

    public void setEvalucionEx(Boolean evalucionEx) { this.evalucionEx = evalucionEx; }

    public void setPeriodoActivoFechas(PeriodoEscolarFechas periodoActivoFechas) { this.periodoActivoFechas = periodoActivoFechas; }

    public void setAperturaEdit(AperturaVisualizacionEncuestas aperturaEdit) { this.aperturaEdit = aperturaEdit; }

    public void setTipoEvEdit(EvaluacionesAreas tipoEvEdit) { this.tipoEvEdit = tipoEvEdit; }
}
