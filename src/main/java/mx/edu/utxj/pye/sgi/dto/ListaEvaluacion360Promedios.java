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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@EqualsAndHashCode(of = {"clave"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEvaluacion360Promedios {
    @Getter @Setter @NotNull private Integer clave;
    @Getter @Setter private String nombre;
    @Getter @Setter private String areaOperativa;
    @Getter @Setter private String categoriaOperativa;
    @Getter @Setter private Double promedio;
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoListaResultadosEvaluacion360{
        @Getter @Setter private Evaluaciones360 evaluacion;
        @Getter @Setter private PeriodosEscolares periodoEscolar;
        @Getter @Setter private String evaluadores;
        @Getter @Setter private String evaluadoresNombres;
        @Getter @Setter private Integer evaluado;
        @Getter @Setter private Personal persona;
        @Getter @Setter private AreasUniversidad areaOp;
        @Getter @Setter private AreasUniversidad areaSup;
        @Getter @Setter private PersonalCategorias personalCategoria;
        @Getter @Setter private Double r1;
        @Getter @Setter private Double r2;
        @Getter @Setter private Double r3;
        @Getter @Setter private Double r4;
        @Getter @Setter private Double r5;
        @Getter @Setter private Double r6;
        @Getter @Setter private Double r7;
        @Getter @Setter private Double r8;
        @Getter @Setter private Double r9;
        @Getter @Setter private Double r10;
        @Getter @Setter private Double r11;
        @Getter @Setter private Double r12;
        @Getter @Setter private Double r13;
        @Getter @Setter private Double r14;
        @Getter @Setter private Double r15;
        @Getter @Setter private Double r16;
        @Getter @Setter private Double r17;
        @Getter @Setter private Double r18;
        @Getter @Setter private Double r19;
        @Getter @Setter private Double r20;
        @Getter @Setter private Double r21;
        @Getter @Setter private Double r22;
        @Getter @Setter private Double r23;
        @Getter @Setter private Double r24;
        @Getter @Setter private Double r25;
        @Getter @Setter private Double r26;
        @Getter @Setter private Double r27;
        @Getter @Setter private Double r28;
        @Getter @Setter private Double r29;
        @Getter @Setter private Double r30;
        @Getter @Setter private Double r31;
        @Getter @Setter private String r32;
        @Getter @Setter private String r33;
        @Getter @Setter private String completo;
        @Getter @Setter private String incompleto;
        @Getter @Setter private Double promedio;
    }
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoListaReporteEvaluacion360{
        @Getter @Setter private PeriodosEscolares periodoEscolar;
        @Getter @Setter private Evaluaciones360 evaluacion;
        @Getter @Setter private Integer evaluador;
        @Getter @Setter private Personal persona;
        @Getter @Setter private AreasUniversidad areaOp;
        @Getter @Setter private AreasUniversidad areaSup;
        @Getter @Setter private PersonalCategorias catOp;
        @Getter @Setter private String tipo;
        @Getter @Setter private String cveCategoria;
        @Getter @Setter private String nombreCategoria;
        @Getter @Setter private String completo;
        @Getter @Setter private String resultado;
        @Getter @Setter private String evaluados;
        @Getter @Setter private String evaluadosNombre;
    }
    
}
