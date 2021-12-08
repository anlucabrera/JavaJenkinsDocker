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
import mx.edu.utxj.pye.sgi.entity.pye2.EvidenciasDetalle;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoRegistroEvidencias implements Serializable, Comparable<DtoRegistroEvidencias>{
    @Getter @Setter @NonNull EvidenciasDetalle evidenciasDetalle;
    @Getter @Setter @NonNull String ejercicio;
    @Getter @Setter @NonNull String mes;
    @Getter @Setter @NonNull DtoTipoInformacionRegistro dtoTipoInformacionRegistro;
    @Getter @Setter @NonNull String nombreEvidencia;

    public DtoRegistroEvidencias() {
    }
    
    @Override
    public int compareTo(DtoRegistroEvidencias o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoRegistroEvidencias dtoRegistroEvidencias){
         return dtoRegistroEvidencias.getDtoTipoInformacionRegistro().getTipoRegistro().concat(" ")
                .concat(String.valueOf(dtoRegistroEvidencias.getEvidenciasDetalle().getEvidencia().getEvidencia()));
    }
}
