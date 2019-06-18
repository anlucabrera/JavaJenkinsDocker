/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "qrnss")
public class QrNssDto implements Serializable{
    @Getter @Setter @NonNull Persona persona;
    @Getter @Setter String nss;
}
