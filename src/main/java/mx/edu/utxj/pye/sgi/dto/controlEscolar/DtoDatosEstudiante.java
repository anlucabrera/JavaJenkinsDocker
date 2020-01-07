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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;


@RequiredArgsConstructor @ToString
public class DtoDatosEstudiante implements Serializable{
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull Aspirante aspirante;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    
    public DtoDatosEstudiante(Estudiante estudiante, AreasUniversidad programaEducativo, PeriodosEscolares periodoEscolar) {
        this.estudiante = estudiante;
        this.programaEducativo = programaEducativo;
        this.periodoEscolar = periodoEscolar;
    }
    
   
}
