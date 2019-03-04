/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vinculacion;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistroMovilidadEstudiante;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "registroMovilidadEstudiante")
@EqualsAndHashCode
public class DTOMovilidadEstudiante implements Serializable{

    private static final long serialVersionUID = 6762137634757656129L;
    @Getter @Setter @NonNull private RegistroMovilidadEstudiante registroMovilidadEstudiante;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
