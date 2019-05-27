package mx.edu.utxj.pye.sgi.dto;


import lombok.*;

@EqualsAndHashCode(of = {"cveAlumno"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class DtoAlumnos {
    @Getter @Setter private Integer cveAlumno;
    @Getter @Setter private Integer cveUniversidad;
    @Getter @Setter private String matricula;
    @Getter @Setter private Integer cveStatus;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private Short grado;

}
