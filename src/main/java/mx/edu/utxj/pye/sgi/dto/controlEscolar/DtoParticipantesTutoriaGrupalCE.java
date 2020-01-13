/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoParticipantesTutoriaGrupalCE implements Serializable{
    private static final long serialVersionUID = 5259327184908220249L;
    @Getter @Setter @NonNull private DtoDatosEstudiante             estudiante;
    @Getter @Setter @NonNull private ParticipantesTutoriaGrupal     participanteTutoriaGrupal;
    @Getter @Setter @NonNull private Boolean                        participacion;
    @Getter @Setter private List<DtoCasoCritico>                    listaCasosCriticos;

    public DtoParticipantesTutoriaGrupalCE(DtoDatosEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public DtoParticipantesTutoriaGrupalCE(DtoDatosEstudiante estudiante, Boolean participacion) {
        this.estudiante = estudiante;
        this.participacion = participacion;
    }
    
    
}
