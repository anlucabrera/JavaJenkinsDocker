/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.escolar;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EficienciaTerminalTitulacionRegistro;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOEficienciaTerminalTitulacionRegistro implements Serializable{

    private static final long serialVersionUID = 303424085436584777L;
    
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private String generacion;
    @Getter @Setter private String periodoInicio;
    @Getter @Setter private String periodoFin;
    @Getter @Setter private EficienciaTerminalTitulacionRegistro eficienciaTerminalTitulacionRegistro;
    
}
