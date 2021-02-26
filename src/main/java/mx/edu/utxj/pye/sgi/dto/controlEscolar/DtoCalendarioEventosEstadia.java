/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoCalendarioEventosEstadia implements Serializable{
    @Getter @Setter @NonNull EventoEstadia eventoEstadia;
    @Getter @Setter @NonNull String situacion;

    public DtoCalendarioEventosEstadia() {
    }
    
}
