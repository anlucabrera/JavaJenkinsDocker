package mx.edu.utxj.pye.sgi.dto;


import lombok.*;

@EqualsAndHashCode(of = {"matricula"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class DtoAlumnos {
    @Getter @Setter private String matricula;
    @Getter @Setter private Integer cveStatus;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private Short grado;

}
