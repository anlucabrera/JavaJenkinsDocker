package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;

import java.io.Serializable;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "becas")
public class dtoReporteBecas implements Serializable {

    //TODO: Información  de la beca por estudiante
    @Getter @Setter String  matricula;
    @Getter @Setter String nombreC;
    @Getter @Setter String siglas;
    @Getter @Setter int grado;
    @Getter @Setter String grupo;
    @Getter @Setter String sexo;
    @Getter @Setter BecaTipos tipoBeca;
    @Getter @Setter String status;
    
    //TODO:Información del director
    @Getter @Setter AreasUniversidad carrera;
    @Getter @Setter Personal director;




}
