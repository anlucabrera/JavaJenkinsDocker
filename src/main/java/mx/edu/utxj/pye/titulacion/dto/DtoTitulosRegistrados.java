/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.titulacion.TituloExpediente;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoTitulosRegistrados")
@EqualsAndHashCode
public class DtoTitulosRegistrados implements Serializable{
    private static final long serialVersionUID = -5157591394852082866L;
    @Getter @Setter private DtoExpedienteMatricula dtoExpMatricula;
    @Getter @Setter private TituloExpediente tituloExpediente;
    
}
