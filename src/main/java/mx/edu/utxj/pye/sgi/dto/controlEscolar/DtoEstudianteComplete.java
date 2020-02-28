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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;


@RequiredArgsConstructor @ToString
public class DtoEstudianteComplete implements Serializable{
    @Getter @Setter @NonNull Estudiante estudiantes;        
    @Getter @Setter @NonNull String datosComplete;
    @Getter @Setter @NonNull String periodoEscolar;

    public DtoEstudianteComplete(Estudiante estudiantes, String datosComplete) {
        this.estudiantes = estudiantes;
        this.datosComplete = datosComplete;
    }
    
}