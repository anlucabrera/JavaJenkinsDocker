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
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"vistaCalificacionesMateria"})
public class DtoVistaCalificaciones implements Serializable{
    @Getter @Setter private Materia materia;
    @Getter @Setter private List<BigDecimal> promedios;
    @Getter @Setter private BigDecimal tareaI;
    @Getter @Setter private BigDecimal promedioFinalO;
    @Getter @Setter private BigDecimal nivelacion;
    @Getter @Setter private BigDecimal promedioFinalN;

    public DtoVistaCalificaciones(Materia materia, List<BigDecimal> promedios, BigDecimal tareaI, BigDecimal promedioFinalO, BigDecimal nivelacion, BigDecimal promedioFinalN) {
        this.materia = materia;
        this.promedios = promedios;
        this.tareaI = tareaI;
        this.promedioFinalO = promedioFinalO;
        this.nivelacion = nivelacion;
        this.promedioFinalN = promedioFinalN;
    }
    
}
