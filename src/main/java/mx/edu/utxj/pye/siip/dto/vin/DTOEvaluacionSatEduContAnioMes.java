/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionesServtecEducont;
import mx.edu.utxj.pye.sgi.entity.pye2.EvaluacionSatisfaccionResultados;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString(of = "evaluacionSatisfaccionResultados")
public class DTOEvaluacionSatEduContAnioMes implements Serializable{

    private static final long serialVersionUID = -7307262841242071293L;
    
    @ToString    @EqualsAndHashCode
    public static class ConsultaRegistros {
        
        @Getter @Setter @NonNull EvaluacionSatisfaccionResultados evaluacionSatisfaccionResultados;
        @Getter @Setter Integer numeroPregunta;
        @Getter @Setter String pregunta;
        @Getter @Setter Integer totalH;
        @Getter @Setter Integer totalI;
        @Getter @Setter Integer totalJ;
        @Getter @Setter Double satisBase5;
        @Getter @Setter Double satisBase10;
        @Getter @Setter Integer numSatisfaccion;
        @Getter @Setter Double satisfaccion;
        @Getter @Setter ActividadesPoa actividadAlineada;
        
        
    }
    
    @ToString    @EqualsAndHashCode
    public static class CatalogoPlantilla {
        
        @Getter @Setter @NonNull EvaluacionesServtecEducont evaluacionesServtecEducont;
        @Getter @Setter @NonNull String numeroPregunta;
        @Getter @Setter @NonNull String pregunta;

        public CatalogoPlantilla(EvaluacionesServtecEducont evaluacionesServtecEducont, String numeroPregunta, String pregunta) {
            this.evaluacionesServtecEducont = evaluacionesServtecEducont;
            this.numeroPregunta = numeroPregunta;
            this.pregunta = pregunta;
        }

    }
    
    @ToString    @EqualsAndHashCode
    public static class LecturaPlantilla {
        
        @Getter @Setter @NonNull EvaluacionesServtecEducont evaluacionesServtecEducont;
        @Getter @Setter @NonNull ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes;
        @Getter @Setter @NonNull String numeroPregunta;
        @Getter @Setter @NonNull String pregunta;
        @Getter @Setter @NonNull Integer respuestasA;
        @Getter @Setter @NonNull Integer respuestasB;
        @Getter @Setter @NonNull Integer respuestasC;
        @Getter @Setter @NonNull Integer respuestasD;
        @Getter @Setter @NonNull Integer respuestasE;
        @Getter @Setter @NonNull Integer respuestasF;
        @Getter @Setter @NonNull Integer respuestasG;

        public LecturaPlantilla() {
        }

        public LecturaPlantilla(EvaluacionesServtecEducont evaluacionesServtecEducont, ServiciosTecnologicosAnioMes serviciosTecnologicosAnioMes, String numeroPregunta, String pregunta, Integer respuestasA, Integer respuestasB, Integer respuestasC, Integer respuestasD, Integer respuestasE, Integer respuestasF, Integer respuestasG) {
            this.evaluacionesServtecEducont = evaluacionesServtecEducont;
            this.serviciosTecnologicosAnioMes = serviciosTecnologicosAnioMes;
            this.numeroPregunta = numeroPregunta;
            this.pregunta = pregunta;
            this.respuestasA = respuestasA;
            this.respuestasB = respuestasB;
            this.respuestasC = respuestasC;
            this.respuestasD = respuestasD;
            this.respuestasE = respuestasE;
            this.respuestasF = respuestasF;
            this.respuestasG = respuestasG;
        }
        
    }
    
}
