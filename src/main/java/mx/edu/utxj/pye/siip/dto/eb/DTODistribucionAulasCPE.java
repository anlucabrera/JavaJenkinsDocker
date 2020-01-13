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
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionAulasCicloPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTODistribucionAulasCPE implements Serializable{
    private static final long serialVersionUID = -6189813966194276510L;
    @Getter @Setter @NonNull private DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTODistribucionAulasCPE(DistribucionAulasCicloPeriodosEscolares distribucionAulasCicloPeriodosEscolares, String cicloEscolar, String periodoEscolar) {
        this.distribucionAulasCicloPeriodosEscolares = distribucionAulasCicloPeriodosEscolares;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
    }
    
}
