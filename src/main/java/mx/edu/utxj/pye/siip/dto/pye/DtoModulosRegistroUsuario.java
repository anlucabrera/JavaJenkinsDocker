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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecifico;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoModulosRegistroUsuario implements Serializable, Comparable<DtoModulosRegistroUsuario> {
    @Getter @Setter @NonNull ModulosRegistroEspecifico modulosRegistroEspecifico;
    @Getter @Setter @NonNull Personal personal;
    @Getter @Setter @NonNull AreasUniversidad areaRegistro;

    public DtoModulosRegistroUsuario() {
    }
    
    @Override
    public int compareTo(DtoModulosRegistroUsuario o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoModulosRegistroUsuario dtoModulosRegistroUsuario){
         return dtoModulosRegistroUsuario.getAreaRegistro().getNombre().concat(" ")
//                .concat(dtoModulosRegistroUsuario.getEjeRegistro()).concat(" ")
//                .concat(dtoModulosRegistroUsuario.getNombreModulo()).concat(" ")
                .concat(dtoModulosRegistroUsuario.getPersonal().getNombre());
    }
}
