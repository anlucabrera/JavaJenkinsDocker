/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoTipoInformacionRegistro implements Serializable{
    @Getter @Setter @NonNull String tipoRegistro;
    @Getter @Setter @NonNull String informacionRegistro;
    @Getter @Setter Integer periodoEscolar;

    public DtoTipoInformacionRegistro() {
    }
            
    
}
