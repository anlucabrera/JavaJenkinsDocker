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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoHistorialEstudiante implements Serializable{
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull Date fecha;
    @Getter @Setter @NonNull String periodoEscolar;
    @Getter @Setter @NonNull String programaEducativo;
    @Getter @Setter @NonNull String tipoMovimiento;
    @Getter @Setter @NonNull String informacionMovimiento;
    @Getter @Setter @NonNull String personaRealizo;
}
