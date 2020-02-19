/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"vistaCalificacionesReporte"})
public class DtoPresentacionCalificacionesReporte implements Serializable{
    
    @Getter @Setter private Integer matricula;
    @Getter @Setter private String nombre;
    @Getter @Setter private List<DtoVistaCalificaciones> materias;
    @Getter @Setter private BigDecimal promedioFinal;

    public DtoPresentacionCalificacionesReporte(Integer matricula, String nombre, List<DtoVistaCalificaciones> materias, BigDecimal promedioFinal) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.materias = materias;
        this.promedioFinal = promedioFinal;
    }
}
