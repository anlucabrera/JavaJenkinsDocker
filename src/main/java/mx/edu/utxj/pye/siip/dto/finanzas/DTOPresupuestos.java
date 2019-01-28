/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.finanzas;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.Presupuestos;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "presupuestos")
@EqualsAndHashCode
public class DTOPresupuestos implements  Serializable{

    private static final long serialVersionUID = 2744706726321936943L;
    @Getter @Setter private @NonNull Presupuestos presupuestos;
    @Getter @Setter private CapitulosTipos capitulosTipos;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private EventosRegistros eventoSeleccionado;
    @Getter @Setter private String capitulo;
}
