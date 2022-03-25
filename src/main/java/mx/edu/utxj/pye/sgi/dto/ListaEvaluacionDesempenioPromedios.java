/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@EqualsAndHashCode(of = {"clave"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEvaluacionDesempenioPromedios {
    @Getter @Setter @NotNull private Integer clave;
    @Getter @Setter private String nombre;
    @Getter @Setter private String areaOperativa;
    @Getter @Setter private String categoriaOperativa;
    @Getter @Setter private Double promedio;
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoListaResultadosEvaluacionDesempenio{
        @Getter @Setter private DesempenioEvaluaciones evaluacion;
        @Getter @Setter private PeriodosEscolares periodoEscolar;
        @Getter @Setter private Personal evaluador;
        @Getter @Setter private AreasUniversidad evaluadorAreaOp;
        @Getter @Setter private AreasUniversidad evaluadorAreaSup;
        @Getter @Setter private PersonalCategorias evaluadorPersonalCat;
        @Getter @Setter private Personal evaluado;
        @Getter @Setter private AreasUniversidad evaluadoAreaOp;
        @Getter @Setter private AreasUniversidad evaluadoAreaSup;
        @Getter @Setter private PersonalCategorias evaluadoPersonalCat;
        @Getter @Setter private Short r1;
        @Getter @Setter private Short r2;
        @Getter @Setter private Short r3;
        @Getter @Setter private Short r4;
        @Getter @Setter private Short r5;
        @Getter @Setter private Short r6;
        @Getter @Setter private Short r7;
        @Getter @Setter private Short r8;
        @Getter @Setter private Short r9;
        @Getter @Setter private Short r10;
        @Getter @Setter private Short r11;
        @Getter @Setter private Short r12;
        @Getter @Setter private Short r13;
        @Getter @Setter private Short r14;
        @Getter @Setter private Short r15;
        @Getter @Setter private Short r16;
        @Getter @Setter private Short r17;
        @Getter @Setter private Short r18;
        @Getter @Setter private Short r19;
        @Getter @Setter private Short r20;
        @Getter @Setter private String r21;
        @Getter @Setter private String completo;
        @Getter @Setter private String incompleto;
        @Getter @Setter private Double promedio;
    }
}
