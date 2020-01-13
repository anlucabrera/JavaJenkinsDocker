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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTODistribucionLabTallCPE implements Serializable{
    private static final long serialVersionUID = -2673127304184476473L;
    @Getter @Setter @NonNull private DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private AreasUniversidad areaResponsable;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTODistribucionLabTallCPE(DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares, String cicloEscolar, String periodoEscolar, AreasUniversidad areaResponsable) {
        this.distribucionLabtallCicloPeriodosEscolares = distribucionLabtallCicloPeriodosEscolares;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.areaResponsable = areaResponsable;
    }
    
}
