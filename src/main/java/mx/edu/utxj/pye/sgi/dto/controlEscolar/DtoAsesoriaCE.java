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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoAsesoriaCE implements Serializable{
    private static final long serialVersionUID = 9091303156173913680L; 
    @Getter @Setter @NonNull private DtoUnidadConfiguracion dtoUnidadConfiguracion; 
    @Getter @Setter @NonNull private Asesoria asesoria;
    @Getter @Setter @NonNull private EventosRegistros eventosRegistros;
}
