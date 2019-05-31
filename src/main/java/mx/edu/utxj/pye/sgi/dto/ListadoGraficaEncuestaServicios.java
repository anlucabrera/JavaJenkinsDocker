/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Planeación
 */
@EqualsAndHashCode(of = {"siglas"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListadoGraficaEncuestaServicios {
    @Getter @Setter @NotNull private String siglas;
    @Getter @Setter private Long totalPorCarrera;

    @EqualsAndHashCode(of = {"siglas"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class graficaEncuestaServiciosPorTutor{
        @Getter @Setter @NotNull private String siglas;
        @Getter @Setter private String cveGrupo;
        @Getter @Setter private Long totalPorCarrera;
    }
}
