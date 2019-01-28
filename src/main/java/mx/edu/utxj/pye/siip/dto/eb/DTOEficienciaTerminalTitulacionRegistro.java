/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOEficienciaTerminalTitulacionRegistro implements Serializable{

    private static final long serialVersionUID = 303424085436584777L;
    
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private String generacion;
    @Getter @Setter private String periodoInicio;
    @Getter @Setter private String periodoFin;
    @Getter @Setter @NonNull private EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOEficienciaTerminalTitulacionRegistro(AreasUniversidad areasUniversidad, String generacion, String periodoInicio, String periodoFin, EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro) {
        this.areasUniversidad = areasUniversidad;
        this.generacion = generacion;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.eficienciaTerminalTitulacionRegistro = eficienciaTerminalTitulacionRegistro;
    }
    
}
