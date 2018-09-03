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
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosMovilidad;


/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTORegistroMovilidad implements Serializable{

    private static final long serialVersionUID = -5019382998968081784L;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolarCursado;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private RegistrosMovilidad registrosMovilidad;
}
