/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "DtoProcesosIntegracion")
@EqualsAndHashCode
public class DtoProcesosIntegracion implements Serializable{
    private static final long serialVersionUID = 1304458214610294698L;
    @Getter @Setter private ProcesosGeneraciones procesosGeneraciones;
    @Getter @Setter private Generaciones generacion;
    @Getter @Setter private PeriodosEscolares periodoIntegracion;
    @Getter @Setter private String nivel;
    
}
