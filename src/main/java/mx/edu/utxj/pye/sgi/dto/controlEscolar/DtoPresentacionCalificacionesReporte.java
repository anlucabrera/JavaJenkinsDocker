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
    @Getter @Setter private Short tipoEstudiante;
    @Getter @Setter private String nombre;
    @Getter @Setter private List<DtoVistaCalificaciones> materias;
    @Getter @Setter private BigDecimal promedioFinal;
    @Getter @Setter private String tipoEs;

    public DtoPresentacionCalificacionesReporte(Integer matricula, Short tipoEstudiante, String nombre, List<DtoVistaCalificaciones> materias, BigDecimal promedioFinal, String tipoEs) {
        this.matricula = matricula;
        this.tipoEstudiante = tipoEstudiante;
        this.nombre = nombre;
        this.materias = materias;
        this.promedioFinal = promedioFinal;
        this.tipoEs = tipoEs;
    }   
}
