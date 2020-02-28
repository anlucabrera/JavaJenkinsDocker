/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Planeación
 */
@EqualsAndHashCode(of = {"nombreArea"})  @ToString
public class dtoCorreosAreas {
    @Getter @Setter private String nombreArea;
    @Getter @Setter private String correoInstitucional;
    @Getter @Setter private String correoPerRespondableA;

    public dtoCorreosAreas() {
    }

    public dtoCorreosAreas(String nombreArea, String correoInstitucional, String correoPerRespondableA) {
        this.nombreArea = nombreArea;
        this.correoInstitucional = correoInstitucional;
        this.correoPerRespondableA = correoPerRespondableA;
    }

    @Override
    public String toString() {
        return "dtoCorreosAreas{" +
                "nombreArea='" + nombreArea + '\'' +
                ", correoInstitucional='" + correoInstitucional + '\'' +
                ", correoPerRespondableA='" + correoPerRespondableA + '\'' +
                '}';
    }
}
