/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoTutoriaIndividualCE implements Serializable{
    private static final long serialVersionUID = 584724976727345935L;
    @Getter @Setter @NonNull private TutoriasIndividuales tutoriaIndividual;
    @Getter @Setter @NonNull private DtoEstudiante estudiante;
    @Getter @Setter private DtoCasoCritico casoCritico;

    public DtoTutoriaIndividualCE(TutoriasIndividuales tutoriaIndividual, DtoEstudiante estudiante, DtoCasoCritico casoCritico) {
        this.tutoriaIndividual = tutoriaIndividual;
        this.estudiante = estudiante;
        this.casoCritico = casoCritico;
    }
    
}
