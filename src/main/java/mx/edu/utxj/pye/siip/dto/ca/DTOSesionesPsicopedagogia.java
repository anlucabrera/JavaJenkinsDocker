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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DTOSesionesPsicopedagogia implements Serializable{
    private static final long serialVersionUID = 1788800084249713519L;
    @Getter @Setter @NonNull private SesionIndividualMensualPsicopedogia sesionIndividualMensualPsicopedogia;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOSesionesPsicopedagogia(SesionIndividualMensualPsicopedogia sesionIndividualMensualPsicopedogia, AreasUniversidad areasUniversidad) {
        this.sesionIndividualMensualPsicopedogia = sesionIndividualMensualPsicopedogia;
        this.areasUniversidad = areasUniversidad;
    }
    
}
