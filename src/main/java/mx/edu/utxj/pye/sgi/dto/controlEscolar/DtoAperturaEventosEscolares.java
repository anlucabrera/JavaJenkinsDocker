/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolarDetalle;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAperturaEventosEscolares implements Serializable{
    @Getter @Setter @NonNull EventoEscolarDetalle eventoEscolarDetalle;
    @Getter @Setter @NonNull String tipoApertura;
    @Getter @Setter @NonNull String areaPersonal;
    @Getter @Setter @NonNull String situacion;
}
