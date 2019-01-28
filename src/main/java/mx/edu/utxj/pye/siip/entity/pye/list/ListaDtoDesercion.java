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
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;

/**
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"desercionPeriodosEscolares"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaDtoDesercion {
    private static final long serialVersionUID = 0L;
    @Getter @Setter private Boolean activo;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private String generacion;
    @Getter @Setter private BajasCausa bajasCausa;
    @Getter @Setter private BajasTipo bajasTipo;
    @Getter @Setter private DesercionPeriodosEscolares desercionPeriodosEscolares;
    
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
