/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;


/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "actividadesFormacionIntegral")
@EqualsAndHashCode
public class DTOActFormacionIntegral implements Serializable{

    private static final long serialVersionUID = 3423077670913132687L;
    @Getter @Setter @NonNull private ActividadesFormacionIntegral actividadesFormacionIntegral; //se declara como llave primaria para interacturar con sus evidencias
    @Getter @Setter private PeriodosEscolares periodosEscolares;
    @Getter @Setter private ActividadesPoa actividadAlineada;

}
