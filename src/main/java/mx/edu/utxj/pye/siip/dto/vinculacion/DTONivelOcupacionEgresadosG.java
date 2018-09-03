/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vinculacion;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionEgresadosGeneracion;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTONivelOcupacionEgresadosG implements Serializable{
    private static final long serialVersionUID = -3459678588227406139L;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter private NivelOcupacionEgresadosGeneracion nivelOcupacionEgresadosGeneracion;
    @Getter @Setter private Integer total;
}
