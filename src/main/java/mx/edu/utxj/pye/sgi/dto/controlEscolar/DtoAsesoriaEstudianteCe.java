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
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesoriasEstudiantes;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoAsesoriaEstudianteCe implements Serializable{
    private static final long serialVersionUID = -2397013853917560050L;
    @Getter @Setter @NonNull private PersonalActivo personalActivo;
    @Getter @Setter @NonNull private AsesoriasEstudiantes asesoriaEstudiante;
    @Getter @Setter @NonNull private EventosRegistros eventosRegistros;
}
