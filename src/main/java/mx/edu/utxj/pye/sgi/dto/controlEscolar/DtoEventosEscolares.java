/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEventosEscolares implements Serializable{
    @Getter @Setter @NonNull Integer numero;
    @Getter @Setter @NonNull String actividad;
    @Getter @Setter @NonNull Personal personal;
    @Getter @Setter @NonNull Date fechaInicio;
    @Getter @Setter @NonNull Date fechaFin;
}
