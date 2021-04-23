package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;

public class EvaluacionParesAcademicosRolFortalecimiento extends AbstractRol {

    @Getter private PersonalActivo personalFortalecimiento;
    @Getter private Evaluaciones evaluacion, evaluacionActiva;
    @Getter private List<Evaluaciones> evaluaciones;
    @Getter private List<DtoEvaluacionParesAcademicos> combinaciones;
    @Getter private  List<AreasUniversidad> areas ;
    @Getter private AreasUniversidad areaSeleccionada,areaSelect;
    @Getter private DtoEvaluacionParesAcademicos dtoSeleccionado, nuevaCombinacion;
    @Getter private List<PersonalActivo> docentesPares, docentesArea;
    @Getter private PersonalActivo docenteEvaluador, docenteEvaluado, docenteEvaluadoUpdate;
    @Getter private Boolean editar;

    public void setPersonalFortalecimiento(PersonalActivo personalFortalecimiento) { this.personalFortalecimiento = personalFortalecimiento; }

    public void setEvaluacion(Evaluaciones evaluacion) { this.evaluacion = evaluacion; }

    public void setCombinaciones(List<DtoEvaluacionParesAcademicos> combinaciones) { this.combinaciones = combinaciones; }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) { this.evaluaciones = evaluaciones; }

    public void setAreas(List<AreasUniversidad> areas) { this.areas = areas; }

    public void setAreaSelect(AreasUniversidad areaSelect) { this.areaSelect = areaSelect; }

    public void setDtoSeleccionado(DtoEvaluacionParesAcademicos dtoSeleccionado) { this.dtoSeleccionado = dtoSeleccionado; }

    public void setDocentesPares(List<PersonalActivo> docentesPares) { this.docentesPares = docentesPares; }

    public void setDocentesArea(List<PersonalActivo> docentesArea) { this.docentesArea = docentesArea; }

    public void setDocenteEvaluador(PersonalActivo docenteEvaluador) { this.docenteEvaluador = docenteEvaluador; }

    public void setDocenteEvaluado(PersonalActivo docenteEvaluado) { this.docenteEvaluado = docenteEvaluado; }

    public void setDocenteEvaluadoUpdate(PersonalActivo docenteEvaluadoUpdate) { this.docenteEvaluadoUpdate = docenteEvaluadoUpdate; }

    public void setNuevaCombinacion(DtoEvaluacionParesAcademicos nuevaCombinacion) { this.nuevaCombinacion = nuevaCombinacion; }

    public void setAreaSeleccionada(AreasUniversidad areaSeleccionada) { this.areaSeleccionada = areaSeleccionada; }

    public void setEvaluacionActiva(Evaluaciones evaluacionActiva) { this.evaluacionActiva = evaluacionActiva; }

    public void setEditar(Boolean editar) { this.editar = editar; }

    public EvaluacionParesAcademicosRolFortalecimiento(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.personalFortalecimiento = filtro.getEntity();
    }



}
