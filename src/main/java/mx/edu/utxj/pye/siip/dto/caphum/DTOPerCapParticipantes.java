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
import mx.edu.utxj.pye.sgi.entity.pye2.ParticipantesPersonalCapacitado;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "participantesPersonalCapacitado")
@EqualsAndHashCode
public class DTOPerCapParticipantes implements Serializable{

    private static final long serialVersionUID = 172771671814468739L;
    @Getter @Setter @NonNull private ParticipantesPersonalCapacitado participantesPersonalCapacitado; //se declara como llave primaria para interacturar con sus evidencias
    @Getter @Setter private Personal personal;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    
}
