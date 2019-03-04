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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "ingresosPropiosCaptados")
@EqualsAndHashCode
public class DTOIngPropios implements Serializable{

    private static final long serialVersionUID = -6128710920932285236L;
    @Getter @Setter @NonNull private IngresosPropiosCaptados ingresosPropiosCaptados; //se declara como llave primaria para interacturar con sus evidencias
    @Getter @Setter private PeriodosEscolares periodosEscolares;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
}
