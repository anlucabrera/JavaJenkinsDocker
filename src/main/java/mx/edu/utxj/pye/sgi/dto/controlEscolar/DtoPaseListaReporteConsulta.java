package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asistenciasacademicas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaDoc"})
public class DtoPaseListaReporteConsulta implements Serializable {

    @Getter    @Setter    private Listaalumnosca listaalumnosca;
    @Getter    @Setter    private List<Asistenciasacademicas> asistenciasacademicases;
    @Getter    @Setter    private Integer totalPasesLista;
    @Getter    @Setter    private Double porAsis;
    @Getter    @Setter    private Boolean casoCritico;

    public DtoPaseListaReporteConsulta(Listaalumnosca listaalumnosca, List<Asistenciasacademicas> asistenciasacademicases, Integer totalPasesLista, Double porAsis, Boolean casoCritico) {
        this.listaalumnosca = listaalumnosca;
        this.asistenciasacademicases = asistenciasacademicases;
        this.totalPasesLista = totalPasesLista;
        this.porAsis = porAsis;
        this.casoCritico = casoCritico;
    }
}
