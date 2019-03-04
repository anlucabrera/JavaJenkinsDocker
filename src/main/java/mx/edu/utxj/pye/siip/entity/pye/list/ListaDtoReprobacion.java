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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.MateriasProgramaEducativo;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;

/**
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"desercionReprobacionMaterias"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaDtoReprobacion {
    private static final long serialVersionUID = -5162564017256453585L;
    @Getter @Setter private Boolean activo;
    @Getter @Setter private String matricula;
    @Getter @Setter private MateriasProgramaEducativo materias;
    @Getter @Setter private Personal personal;
    @Getter @Setter private DesercionReprobacionMaterias desercionReprobacionMaterias;
    
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
