package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

import javax.faces.model.SelectItem;
import java.util.List;

public class EvaluacionParesAcademicosRolDocente extends AbstractRol {

    @Getter private PersonalActivo personalDocente;
    @Getter private Evaluaciones evaluacionActiva;
    @Getter private List<DtoEvaluacionParesAcademicos> evaluados;
    @Getter private DtoEvaluacionParesAcademicos dtoSeleccionado;
    @Getter private List<Apartado> preguntas;
    @Getter private List<SelectItem> respuestasPosibles;
    @Getter private int totalDocentes, totalEvaluados;
    @Getter private  double pocentaje;

    public void setPersonalDocente(PersonalActivo personalDocente) { this.personalDocente = personalDocente; }

    public void setDtoSeleccionado(DtoEvaluacionParesAcademicos dtoSeleccionado) { this.dtoSeleccionado = dtoSeleccionado; }

    public void setEvaluacionActiva(Evaluaciones evaluacionActiva) { this.evaluacionActiva = evaluacionActiva; }

    public void setEvaluados(List<DtoEvaluacionParesAcademicos> evaluados) { this.evaluados = evaluados; }

    public void setPreguntas(List<Apartado> preguntas) { this.preguntas = preguntas; }

    public void setRespuestasPosibles(List<SelectItem> respuestasPosibles) { this.respuestasPosibles = respuestasPosibles; }

    public void setTotalDocentes(int totalDocentes) { this.totalDocentes = totalDocentes; }

    public void setTotalEvaluados(int totalEvaluados) { this.totalEvaluados = totalEvaluados; }

    public void setPocentaje(double pocentaje) { this.pocentaje = pocentaje; }

    public EvaluacionParesAcademicosRolDocente(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.personalDocente = docente;
    }



}
