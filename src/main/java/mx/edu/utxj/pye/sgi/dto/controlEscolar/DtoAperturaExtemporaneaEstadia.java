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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoEstadia;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAperturaExtemporaneaEstadia implements Serializable{
    @Getter @Setter @NonNull AperturaExtemporaneaEventoEstadia aperturaExtemporanea;
    @Getter @Setter @NonNull Personal personalSolicita;
    @Getter @Setter Personal personalValido;

    public DtoAperturaExtemporaneaEstadia() {
    }
    
}
