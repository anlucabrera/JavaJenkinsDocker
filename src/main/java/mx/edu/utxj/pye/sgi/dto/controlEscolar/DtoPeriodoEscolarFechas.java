/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoPeriodoEscolarFechas implements Serializable{
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter PeriodoEscolarFechas fechasPeriodoEscolar;

    public DtoPeriodoEscolarFechas(PeriodosEscolares periodoEscolar, PeriodoEscolarFechas fechasPeriodoEscolar) {
        this.periodoEscolar = periodoEscolar;
        this.fechasPeriodoEscolar = fechasPeriodoEscolar;
    }
    
    
}
