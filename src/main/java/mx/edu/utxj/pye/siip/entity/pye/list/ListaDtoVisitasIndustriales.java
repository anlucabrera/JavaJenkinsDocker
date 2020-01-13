/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.pye.list;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.VisitasIndustriales;

/**
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"desercionReprobacionMaterias"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaDtoVisitasIndustriales {
    @Getter @Setter private Boolean activo;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private VisitasIndustriales visitasIndustriales;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    
}
