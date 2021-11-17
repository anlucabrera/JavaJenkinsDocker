package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InformeIntegralDocenteRolMultiple {

    @Getter @NonNull private PersonalActivo personalActivo;
    @Getter @NonNull private AreasUniversidad area;
    @Getter @Setter @NonNull    protected NivelRol nivelRol = NivelRol.CONSULTA;
    @Getter @Setter private     List<String> instrucciones = new ArrayList<>();

    @Getter @NonNull private    List<AreasUniversidad> areasAcademicas;
    @Getter @NonNull private    List<Personal> docentes;
    @Getter @NonNull private    List<PeriodosEscolares> periodosEscolares;

    @Getter @NonNull private AreasUniversidad areaSeleccionada;
    @Getter @NonNull private Personal docente;
    @Getter @NonNull private    Date fechaImpresion;
    @Getter @NonNull private    Boolean tutor,fueDocente;
    @Getter @NonNull private    Boolean esDocente,esDirector,esSA,esFDA;
    @Getter @NonNull private PeriodosEscolares periodoSeleccionado;
    @Getter @NonNull private DtoInformeIntegralDocente dtoInformeIntegralDocente;
    @Getter @NonNull private    DtoInformeIntegralDocente.Desempeño evDesempeño;
    @Getter @NonNull private    DtoInformeIntegralDocente.Pares evPares;
    @Getter @NonNull private    DtoInformeIntegralDocente.Tutor evTutor;
    @Getter @NonNull private    DtoInformeIntegralDocente.Docente evDocente;
    @Getter @NonNull private    DtoInformeIntegralDocente.InformeIntegral informeIntegral;
    @Getter @NonNull private    List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPares;
    @Getter @NonNull private    List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosTutor;
    @Getter @NonNull private    Map<String, List<DtoInformeIntegralDocente.EvaluacionIntegral>> mappedList;


    public void setPersonalActivo(PersonalActivo personalActivo) { this.personalActivo = personalActivo; }
    public void setArea(AreasUniversidad area) { this.area = area; }
    public void setInstrucciones(List<String> instrucciones) { this.instrucciones = instrucciones; }
    public void setAreaSeleccionada(AreasUniversidad areaSeleccionada) { this.areaSeleccionada = areaSeleccionada; }
    public void setAreasAcademicas(List<AreasUniversidad> areasAcademicas) { this.areasAcademicas = areasAcademicas; }
    public void setDocente(Personal docente) { this.docente = docente; }
    public void setDocentes(List<Personal> docentes) { this.docentes = docentes; }
    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) { this.periodosEscolares = periodosEscolares; }
    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) { this.periodoSeleccionado = periodoSeleccionado; }
    public void setEvDesempeño(DtoInformeIntegralDocente.Desempeño evDesempeño) { this.evDesempeño = evDesempeño; }
    public void setDtoInformeIntegralDocente(DtoInformeIntegralDocente dtoInformeIntegralDocente) { this.dtoInformeIntegralDocente = dtoInformeIntegralDocente; }
    public void setEvPares(DtoInformeIntegralDocente.Pares evPares) { this.evPares = evPares; }
    public void setEvTutor(DtoInformeIntegralDocente.Tutor evTutor) { this.evTutor = evTutor; }
    public void setTutor(Boolean tutor) { this.tutor = tutor; }
    public void setEvDocente(DtoInformeIntegralDocente.Docente evDocente) { this.evDocente = evDocente; }
    public void setInformeIntegral(DtoInformeIntegralDocente.InformeIntegral informeIntegral) { this.informeIntegral = informeIntegral; }
    public void setFechaImpresion(Date fechaImpresion) { this.fechaImpresion = fechaImpresion; }
    public void setResultadosPares(List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosPares) { this.resultadosPares = resultadosPares; }
    public void setResultadosTutor(List<DtoInformeIntegralDocente.ResultadoPregunta> resultadosTutor) { this.resultadosTutor = resultadosTutor; }
    public void setEsDocente(Boolean esDocente) { this.esDocente = esDocente; }
    public void setEsDirector(Boolean esDirector) { this.esDirector = esDirector; }
    public void setEsSA(Boolean esSA) { this.esSA = esSA; }
    public void setEsFDA(Boolean esFDA) { this.esFDA = esFDA; }
    public void setMappedList(Map<String, List<DtoInformeIntegralDocente.EvaluacionIntegral>> mappedList) { this.mappedList = mappedList; }
    public void setFueDocente(Boolean fueDocente) { this.fueDocente = fueDocente; }
}
