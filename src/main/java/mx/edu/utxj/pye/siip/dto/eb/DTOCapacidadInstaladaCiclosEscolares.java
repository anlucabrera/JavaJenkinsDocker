package mx.edu.utxj.pye.siip.dto.eb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOCapacidadInstaladaCiclosEscolares implements Serializable{
    private static final long serialVersionUID = 6042531187306561570L;
    @Getter @Setter @NonNull private CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private Integer totalEspaciosDocentes;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOCapacidadInstaladaCiclosEscolares(CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares, String cicloEscolar, Integer totalEspaciosDocentes) {
        this.capacidadInstaladaCiclosEscolares = capacidadInstaladaCiclosEscolares;
        this.cicloEscolar = cicloEscolar;
        this.totalEspaciosDocentes = totalEspaciosDocentes;
    }
    
}
