/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOServiciosEnfemeriaCicloPeriodos implements Serializable{
    private static final long serialVersionUID = 5399676328172918959L;
    @Getter @Setter @NonNull private ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOServiciosEnfemeriaCicloPeriodos(ServiciosEnfermeriaCicloPeriodos serviciosEnfermeriaCicloPeriodos, String cicloEscolar, String periodoEscolar) {
        this.serviciosEnfermeriaCicloPeriodos = serviciosEnfermeriaCicloPeriodos;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
    }
    
}
