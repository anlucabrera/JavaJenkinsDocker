/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum RegistroSiipEtapa {
    MOSTRAR("Mostrar"),
    CARGAR("Cargar");
    @Getter @Setter @NonNull private String label;
}
