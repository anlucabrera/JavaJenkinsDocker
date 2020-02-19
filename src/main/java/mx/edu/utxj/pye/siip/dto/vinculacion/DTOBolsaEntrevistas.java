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
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "bolsaTrabajoEntrevistas")
@EqualsAndHashCode
public class DTOBolsaEntrevistas implements Serializable{

    private static final long serialVersionUID = 2799823249847985278L;
    @Getter @Setter @NonNull private BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas; //se declara como llave primaria para interacturar con sus eviedencias
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private Generaciones generaciones;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String programaEducativo;
    @Getter @Setter private String generacion;
}
