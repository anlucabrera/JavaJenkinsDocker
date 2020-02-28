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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoTutoriaGrupalCE implements Serializable{
    private static final long serialVersionUID = 6206319260022505789L;
    @Getter @Setter @NonNull    private     TutoriasGrupales        tutoriaGrupal;
    @Getter @Setter @NonNull    private     DtoEstudianteComplete   dtoEstudianteComplete;
}
