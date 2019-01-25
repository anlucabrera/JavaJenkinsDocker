package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.io.Serializable;

@RequiredArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "personal")
public class PersonalActivo implements Serializable {
    @Getter @Setter @NonNull private Personal personal;
    @Getter @Setter private AreasUniversidad areaOperativa;
    @Getter @Setter private AreasUniversidad areaSuperior;
    @Getter @Setter private AreasUniversidad areaOficial;
    @Getter @Setter private AreasUniversidad areaPOA;
}
