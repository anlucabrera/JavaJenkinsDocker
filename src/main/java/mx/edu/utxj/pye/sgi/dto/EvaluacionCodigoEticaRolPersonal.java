package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados2;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

import javax.faces.model.SelectItem;
import java.util.List;

public class EvaluacionCodigoEticaRolPersonal extends AbstractRol {
    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter     @NonNull    private     PersonalActivo personal;
    @Getter     @NonNull    private     Evaluaciones evaluacionActiva;
    @Getter     @NonNull    private     List<Apartado> apartados;
    @Getter     @NonNull    private     List<SelectItem> siNo,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12;
    @Getter     @NonNull    private     EvaluacionConocimientoCodigoEticaResultados2 resultados;
    @Getter     @NonNull    private     Integer totalCorrectas;
    @Getter     @NonNull    private     Double promedio;




    public EvaluacionCodigoEticaRolPersonal(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setEvaluacionActiva(Evaluaciones evaluacionActiva) { this.evaluacionActiva = evaluacionActiva; }
    public void setApartados(List<Apartado> apartados) { this.apartados = apartados; }
    public void setSiNo(List<SelectItem> siNo) { this.siNo = siNo; }
    public void setP3(List<SelectItem> p3) { this.p3 = p3; }
    public void setP4(List<SelectItem> p4) { this.p4 = p4; }
    public void setP5(List<SelectItem> p5) { this.p5 = p5; }
    public void setP6(List<SelectItem> p6) { this.p6 = p6; }
    public void setP7(List<SelectItem> p7) { this.p7 = p7; }
    public void setP8(List<SelectItem> p8) { this.p8 = p8; }
    public void setP9(List<SelectItem> p9) { this.p9 = p9; }
    public void setP10(List<SelectItem> p10) { this.p10 = p10; }
    public void setP11(List<SelectItem> p11) { this.p11 = p11; }
    public void setP12(List<SelectItem> p12) { this.p12 = p12; }
    public void setResultados(EvaluacionConocimientoCodigoEticaResultados2 resultados) { this.resultados = resultados; }
    public void setTotalCorrectas(Integer totalCorrectas) { this.totalCorrectas = totalCorrectas; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
}
