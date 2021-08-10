/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;


/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEventosTitulacion implements Serializable{
    
    @ToString    @EqualsAndHashCode
    public static class GeneracionesControlEscolar {
        @Getter @Setter @NonNull EventoTitulacion eventoTitulacion;
        @Getter @Setter @NonNull Generaciones generacion;
        @Getter @Setter @NonNull ProgramasEducativosNiveles nivel;

        public GeneracionesControlEscolar(EventoTitulacion eventoTitulacion, Generaciones generacion, ProgramasEducativosNiveles nivel) {
            this.eventoTitulacion = eventoTitulacion;
            this.generacion = generacion;
            this.nivel = nivel;
        }

    }
    
    @ToString    @EqualsAndHashCode
    public static class GeneracionesSAIIUT {
        @Getter @Setter @NonNull ProcesosGeneraciones eventoTitulacion;
        @Getter @Setter @NonNull Generaciones generacion;
        @Getter @Setter @NonNull PeriodosEscolares periodo;
        @Getter @Setter @NonNull Date fechaInicio;
        @Getter @Setter @NonNull Date fechaFin;

        public GeneracionesSAIIUT(ProcesosGeneraciones eventoTitulacion, Generaciones generacion, PeriodosEscolares periodo, Date fechaInicio, Date fechaFin) {
            this.eventoTitulacion = eventoTitulacion;
            this.generacion = generacion;
            this.periodo = periodo;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
        }
    }
    
}
