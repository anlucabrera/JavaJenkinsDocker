/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.escolar.list;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;

/**
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"becas"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaBecasDto {
    @Getter @Setter private Boolean activo;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoAsignacion;
    @Getter @Setter private BecaTipos becaTipos;
    @Getter @Setter private BecasPeriodosEscolares becasPeriodosEscolares;
    
    
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
