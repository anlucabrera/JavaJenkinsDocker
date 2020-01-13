/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.escolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ExaniResultadosCiclosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "exaniResultadosCiclosEscolares")
@EqualsAndHashCode
public class DTOExani implements Serializable{
    
    private static final long serialVersionUID = 2600677164316973621L;
    @Getter @Setter private @NonNull ExaniResultadosCiclosEscolares exaniResultadosCiclosEscolares;
    @Getter @Setter private CiclosEscolares ciclosEscolares;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private Integer nuevoIngreso;
    @Getter @Setter private Double porICNEalto;
    @Getter @Setter private Double porICNEmedio;
    @Getter @Setter private Double porICNEbajo;
}
