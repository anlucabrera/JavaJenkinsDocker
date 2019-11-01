package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoCedulaIdentifiacionDatosDeSalud {
    //TODO: Datos de salud del estudiante
    @Getter @Setter private Double p110; //Estatura
    @Getter @Setter private Double p111; //Peso
    @Getter @Setter private String p112; //Tipo de sangre
    @Getter @Setter private String p113; //Discapacidad
    @Getter @Setter private String p114; //Tipo de discapacidad
    @Getter @Setter private String p115; //Alergias
    @Getter @Setter private String p116; //Padecimiento de enfermedad
    @Getter @Setter private String p117; //Tratamiento medico
    @Getter @Setter private String p118; //IMSS
    @Getter @Setter private String p119; //Número de seguridad social
    // Todo: Atecendentes medicos famliares
    @Getter @Setter private  String p120;//Diabetes
    @Getter @Setter private  String p121;//Hipertensión
    @Getter @Setter private  String p122;//Problemas cardiacos
    @Getter @Setter private  String p123;//Cancer

}
