package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@AllArgsConstructor @ToString @EqualsAndHashCode
public class DtoMateria implements Serializable {
    @Getter @Setter @NonNull private Materia materia;
    @Getter @Setter private DtoCargaAcademica dtoCargaAcademica;
    @Getter private Integer horasPorSemana = 0;
    @Getter @Setter private Boolean activa = true;

    public void setHorasPorSemana(Integer horasPorSemana) {
        this.horasPorSemana = horasPorSemana;
        System.out.println("this.horasPorSemana = " + this.horasPorSemana);
        System.out.println("(dtoCargaAcademica != null) = " + (dtoCargaAcademica != null));
        if(dtoCargaAcademica != null){
            dtoCargaAcademica.getCargaAcademica().setHorasSemana(horasPorSemana);
            System.out.println("dtoCargaAcademica.getCargaAcademica().getHorasSemana() = " + dtoCargaAcademica.getCargaAcademica().getHorasSemana());
        }
    }
}
