/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.servgen;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.DistribucionLabtallCicloPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTODistribucionLabTallCPE implements Serializable{
    private static final long serialVersionUID = -2673127304184476473L;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private AreasUniversidad areaResponsable;
    @Getter @Setter private DistribucionLabtallCicloPeriodosEscolares distribucionLabtallCicloPeriodosEscolares;
}
