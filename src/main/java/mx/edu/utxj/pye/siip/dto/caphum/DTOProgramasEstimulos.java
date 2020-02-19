/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.caphum;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulosTipos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "programasEstimulos")
@EqualsAndHashCode
public class DTOProgramasEstimulos implements Serializable{

    private static final long serialVersionUID = 1679413082638910682L;
    @Getter @Setter @NonNull private ProgramasEstimulos programasEstimulos;
    @Getter @Setter private Personal personal;
    @Getter @Setter private ProgramasEstimulosTipos programasEstimulosTipos;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private EventosRegistros eventoSeleccionado;
    @Getter @Setter private String puesto;
    @Getter @Setter private String area;
}
