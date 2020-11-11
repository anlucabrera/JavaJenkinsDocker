package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import javax.faces.model.SelectItem;
import java.util.List;

public class CuestionarioComplementarioInfPerRolPesonalActivo extends  AbstractRol {

    @Getter @NonNull private PersonalActivo personalActivo;
    @Getter @NonNull private Personal personal;
    @Getter @NonNull private AreasUniversidad area;

    @Getter @NonNull private Evaluaciones cuestionarioActivo;

    @Getter @NonNull private CuestionarioComplementarioInformacionPersonal resultados;
    @Getter @NonNull private List<Apartado> preguntas;
    @Getter @NonNull private List<SelectItem> siNo;
    @Getter @NonNull private  List<TipoSangre> tipoSangreList;
    @Getter @NonNull private  List<TipoDiscapacidad> tipoDiscapacidads;
    @Getter @NonNull private  List<LenguaIndigena> lenguaIndigenas;
    @Getter @NonNull private  boolean finalizado, cargado;

    public CuestionarioComplementarioInfPerRolPesonalActivo(Filter<PersonalActivo> filtro, PersonalActivo personalActivo, AreasUniversidad area) {
        super(filtro);
        this.personalActivo = personalActivo;
        this.area = area;
    }

    public void setPersonalActivo(PersonalActivo personalActivo) { this.personalActivo = personalActivo; }

    public void setArea(AreasUniversidad area) { this.area = area; }

    public void setCuestionarioActivo(Evaluaciones cuestionarioActivo) { this.cuestionarioActivo = cuestionarioActivo; }

    public void setResultados(CuestionarioComplementarioInformacionPersonal resultados) { this.resultados = resultados; }

    public void setPreguntas(List<Apartado> preguntas) { this.preguntas = preguntas; }

    public void setSiNo(List<SelectItem> siNo) { this.siNo = siNo; }

    public void setTipoSangreList(List<TipoSangre> tipoSangreList) { this.tipoSangreList = tipoSangreList; }

    public void setTipoDiscapacidads(List<TipoDiscapacidad> tipoDiscapacidads) { this.tipoDiscapacidads = tipoDiscapacidads; }

    public void setLenguaIndigenas(List<LenguaIndigena> lenguaIndigenas) { this.lenguaIndigenas = lenguaIndigenas; }

    public void setFinalizado(boolean finalizado) { this.finalizado = finalizado; }

    public void setCargado(boolean cargado) { this.cargado = cargado; }

    public void setPersonal(Personal personal) { this.personal = personal; }
}
