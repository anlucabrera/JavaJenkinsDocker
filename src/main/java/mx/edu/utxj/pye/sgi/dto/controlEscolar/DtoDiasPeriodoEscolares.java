/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@RequiredArgsConstructor @ToString
public class DtoDiasPeriodoEscolares implements Serializable{
    @Getter @Setter @NonNull Date fechaInicio;
    @Getter @Setter @NonNull Date fechaFin;
    @Getter @Setter @NonNull Integer dias;
}
