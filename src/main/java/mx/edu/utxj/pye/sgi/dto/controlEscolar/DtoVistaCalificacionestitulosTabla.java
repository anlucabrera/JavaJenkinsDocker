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
@EqualsAndHashCode(of = {"vistaCalificacionesMateriatitulostabla"})
public class DtoVistaCalificacionestitulosTabla implements Serializable{
    @Getter @Setter private String docente;
    @Getter @Setter private String unidad;
    @Getter @Setter private Integer tunidades;
    @Getter @Setter private Boolean ti;

    public DtoVistaCalificacionestitulosTabla(String docente, String unidad, Integer tunidades, Boolean ti) {
        this.docente = docente;
        this.unidad = unidad;
        this.tunidades = tunidades;
        this.ti = ti;
    }
}
