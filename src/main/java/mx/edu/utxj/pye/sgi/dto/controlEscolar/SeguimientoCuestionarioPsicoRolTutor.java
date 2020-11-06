package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.dtoAvanceEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.AperturaVisualizacionEncuestas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class SeguimientoCuestionarioPsicoRolTutor extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalTutor;
    @Getter @NonNull AperturaVisualizacionEncuestas aperturaActiva;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;
    @Getter  @NonNull private PeriodosEscolares periodoActivo;
    @Getter @NonNull private List<DtoCuestionarioPsicopedagogicoEstudiante> listaGeneral,completos,incompletos,noIngresaron,filter;
    @Getter @NonNull private  Integer totalCompletos,totalIncomepletos,totalNoAcceso,totalFaltantes;
    @Getter @NonNull private  double porcentaje;
    @Getter @NonNull private dtoAvanceEvaluaciones dtoAcvance;
    @Getter @NonNull private  List<dtoAvanceEvaluaciones> listAvance, listAvancePE, filterPE;

    public SeguimientoCuestionarioPsicoRolTutor(Filter<PersonalActivo> filtro, PersonalActivo personalTutor, AreasUniversidad programa) {
        super(filtro);
        this.personalTutor = personalTutor;
        this.programa = programa;
    }
    public void setPersonalTutor(PersonalActivo personalTutor) { this.personalTutor = personalTutor; }

    public void setPrograma(AreasUniversidad programa) { this.programa = programa; }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) { this.periodoActivo = periodoActivo; }

    public void setListaGeneral(List<DtoCuestionarioPsicopedagogicoEstudiante> listaGeneral) { this.listaGeneral = listaGeneral; }

    public void setCompletos(List<DtoCuestionarioPsicopedagogicoEstudiante> completos) { this.completos = completos; }

    public void setIncompletos(List<DtoCuestionarioPsicopedagogicoEstudiante> incompletos) { this.incompletos = incompletos; }

    public void setNoIngresaron(List<DtoCuestionarioPsicopedagogicoEstudiante> noIngresaron) { this.noIngresaron = noIngresaron; }

    public void setAperturaActiva(AperturaVisualizacionEncuestas aperturaActiva) { this.aperturaActiva = aperturaActiva; }

    public void setTotalCompletos(Integer totalCompletos) { this.totalCompletos = totalCompletos; }

    public void setTotalIncomepletos(Integer totalIncomepletos) { this.totalIncomepletos = totalIncomepletos; }

    public void setTotalNoAcceso(Integer totalNoAcceso) { this.totalNoAcceso = totalNoAcceso; }

    public void setTotalFaltantes(Integer totalFaltantes) { this.totalFaltantes = totalFaltantes; }

    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public void setFilter(List<DtoCuestionarioPsicopedagogicoEstudiante> filter) { this.filter = filter; }

    public void setDtoAcvance(dtoAvanceEvaluaciones dtoAcvance) { this.dtoAcvance = dtoAcvance; }

    public void setListAvance(List<dtoAvanceEvaluaciones> listAvance) { this.listAvance = listAvance; }

    public void setListAvancePE(List<dtoAvanceEvaluaciones> listAvancePE) { this.listAvancePE = listAvancePE; }

    public void setFilterPE(List<dtoAvanceEvaluaciones> filterPE) { this.filterPE = filterPE; }
}
