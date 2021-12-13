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
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoRegistroEvento implements Serializable, Comparable<DtoRegistroEvento>{
    @Getter @Setter @NonNull Registros registro;
    @Getter @Setter @NonNull String informacionRegistro;
    @Getter @Setter Integer periodoEscolar;
    @Getter @Setter @NonNull String informacionSubregistro;
    @Getter @Setter @NonNull Boolean seleccionado;

    public DtoRegistroEvento(Registros registro, String informacionRegistro, Integer periodoEscolar, String informacionSubregistro, Boolean seleccionado) {
        this.registro = registro;
        this.informacionRegistro = informacionRegistro;
        this.periodoEscolar = periodoEscolar;
        this.informacionSubregistro = informacionSubregistro;
        this.seleccionado = seleccionado;
    }
    
    @Override
    public int compareTo(DtoRegistroEvento o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoRegistroEvento dtoRegistroEventoEscolar){
         return dtoRegistroEventoEscolar.getRegistro().getTipo().getNombre().concat(" ")
                .concat(String.valueOf(dtoRegistroEventoEscolar.getInformacionRegistro()));
    }
    
}
