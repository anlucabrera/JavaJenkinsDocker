package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoCedulaIdentifiacionDatosDeSalud {
    //TODO: Datos de salud del estudiante
    @Getter @Setter private Double p108; //Estatura
    @Getter @Setter private Double p109; //Peso
    @Getter @Setter private String p110; //Tipo de sangre
    @Getter @Setter private String p111; //Discapacidad
    @Getter @Setter private String p112; //Tipo de discapacidad
    @Getter @Setter private String p113; //Alergias
    @Getter @Setter private String p114; //Padecimiento de enfermedad
    @Getter @Setter private String p115; //Tratamiento medico
    @Getter @Setter private String p116; //IMSS
    @Getter @Setter private String p117; //Número de seguridad social
    // Todo: Atecendentes medicos famliares
    @Getter @Setter private  String p118;//Diabetes
    @Getter @Setter private  String p119;//Hipertensión
    @Getter @Setter private  String p120;//Problemas cardiacos
    @Getter @Setter private  String p121;//Cancer

}
