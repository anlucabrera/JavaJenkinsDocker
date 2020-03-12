/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaEstudiante;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAperturaExtPorEstudiante implements Serializable{
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante;
    @Getter @Setter String informacionApertura;
    @Getter @Setter Boolean validada;

    public DtoAperturaExtPorEstudiante() {
    }

    public DtoAperturaExtPorEstudiante(Estudiante estudiante, PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante, String informacionApertura, Boolean validada) {
        this.estudiante = estudiante;
        this.permisosCapturaExtemporaneaEstudiante = permisosCapturaExtemporaneaEstudiante;
        this.informacionApertura = informacionApertura;
        this.validada = validada;
    }

   
}
