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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajo;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "bolsaTrabajo")
@EqualsAndHashCode
public class DTOBolsa implements Serializable{

    private static final long serialVersionUID = -1535557520004257840L;
    @Getter @Setter @NonNull private BolsaTrabajo bolsaTrabajo; //se declara como llave primaria para interacturar con sus eviedencias
    @Getter @Setter private PeriodosEscolares periodosEscolares;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String cicloEscolar;
}
