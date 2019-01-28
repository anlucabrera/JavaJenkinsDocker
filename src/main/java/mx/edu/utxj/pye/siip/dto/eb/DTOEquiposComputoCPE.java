/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOEquiposComputoCPE implements Serializable{
    private static final long serialVersionUID = 2009116103397310212L;
    @Getter @Setter @NonNull private EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private Integer total;
    @Getter @Setter private Integer escritorio;
    @Getter @Setter private Integer portatiles;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOEquiposComputoCPE(EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar, String cicloEscolar, String periodoEscolar, Integer total, Integer escritorio, Integer portatiles) {
        this.equiposComputoCicloPeriodoEscolar = equiposComputoCicloPeriodoEscolar;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.total = total;
        this.escritorio = escritorio;
        this.portatiles = portatiles;
    }
    
}
