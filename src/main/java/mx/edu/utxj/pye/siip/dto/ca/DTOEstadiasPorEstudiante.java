/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EstadiasPorEstudiante;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOEstadiasPorEstudiante implements Serializable{
    private static final long serialVersionUID = -8793044439690038246L;
    @Getter @Setter @NonNull private EstadiasPorEstudiante estadiasPorEstudiante;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOEstadiasPorEstudiante(EstadiasPorEstudiante estadiasPorEstudiante, String cicloEscolar, String periodoEscolar, String generacion, AreasUniversidad areasUniversidad) {
        this.estadiasPorEstudiante = estadiasPorEstudiante;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.generacion = generacion;
        this.areasUniversidad = areasUniversidad;
    }
    
}
