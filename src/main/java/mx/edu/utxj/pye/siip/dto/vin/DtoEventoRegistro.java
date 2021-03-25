/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoEventoRegistro implements Serializable{
    private static final long serialVersionUID = -7646528504574596977L;
    @Getter @Setter @NonNull    private     Integer     eventoRegistro;
    @Getter @Setter @NonNull    private     String      descripcionEventoRegistro;
}
