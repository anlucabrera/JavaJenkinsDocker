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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.PersonalCapacitado;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "personalCapacitado")
@EqualsAndHashCode
public class DTOPersonalCapacitado implements Serializable{

    private static final long serialVersionUID = -2314920772290427205L;
    @Getter @Setter  @NonNull private PersonalCapacitado personalCapacitado; //se declara como llave primaria para interacturar con sus evidencias
    @Getter @Setter private PeriodosEscolares periodosEscolares;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String cicloEscolar;
    
}
