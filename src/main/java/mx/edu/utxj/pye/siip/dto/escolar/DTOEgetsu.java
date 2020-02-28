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
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "egetsuResultadosGeneraciones")
@EqualsAndHashCode
public class DTOEgetsu implements Serializable{
    
    private static final long serialVersionUID = 2701990608371591903L;
    @Getter @Setter private @NonNull EgetsuResultadosGeneraciones egetsuResultadosGeneraciones;
    @Getter @Setter private Generaciones generaciones;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private String generacion;
}
