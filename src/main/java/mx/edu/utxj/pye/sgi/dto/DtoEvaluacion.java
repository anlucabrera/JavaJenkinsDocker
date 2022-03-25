package mx.edu.utxj.pye.sgi.dto;


import lombok.*;

@EqualsAndHashCode(of = {"evaluacion"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class DtoEvaluacion {
    @Getter @Setter private Integer evaluacion;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private String tipo;

}
