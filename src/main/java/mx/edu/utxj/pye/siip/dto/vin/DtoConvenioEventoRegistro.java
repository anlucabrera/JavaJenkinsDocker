/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.Convenios;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoConvenioEventoRegistro{
    @Getter @Setter @NonNull  private Convenios     convenio;
    @Getter @Setter @NonNull  private Integer       eventoRegistro;
    @Getter @Setter @NonNull  private String        descripcionEventoRegistro;
}
