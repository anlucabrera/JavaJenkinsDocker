/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SegurosFacultativosEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoSeguroFacultativo implements Serializable, Comparable<DtoSeguroFacultativo>{
    @Getter             @Setter             @NonNull                private             SegurosFacultativosEstudiante               seguroFactultativo;
    @Getter             @Setter             @NonNull                private             DtoEstudiante                               estudiante;
    @Getter             @Setter             private                 PersonalActivo      personalActivo;
    @Getter             @Setter             private                 Boolean             validacionSeguro;
    @Getter             @Setter             private                 AreasUniversidad    programaEducativo;

    public DtoSeguroFacultativo(SegurosFacultativosEstudiante seguroFactultativo, DtoEstudiante estudiante, PersonalActivo personalActivo) {
        this.seguroFactultativo = seguroFactultativo;
        this.estudiante = estudiante;
        this.personalActivo = personalActivo;
    }

    public DtoSeguroFacultativo(SegurosFacultativosEstudiante seguroFactultativo, DtoEstudiante estudiante, AreasUniversidad programaEducativo) {
        this.seguroFactultativo = seguroFactultativo;
        this.estudiante = estudiante;
        this.programaEducativo = programaEducativo;
    }

    public DtoSeguroFacultativo(SegurosFacultativosEstudiante seguroFactultativo, DtoEstudiante estudiante, PersonalActivo personalActivo, AreasUniversidad programaEducativo) {
        this.seguroFactultativo = seguroFactultativo;
        this.estudiante = estudiante;
        this.personalActivo = personalActivo;
        this.programaEducativo = programaEducativo;
    }
    
    @Override
    public int compareTo(DtoSeguroFacultativo o) {
        return toLabel(this).compareTo(toLabel(o));
    }
    
    public static String toLabel(DtoSeguroFacultativo dtoSeguroFacultativo){
        return Caster.convertirFormatoFecha(dtoSeguroFacultativo.getSeguroFactultativo().getFechaRegistro())
                .concat(" - ")
                .concat(dtoSeguroFacultativo.getSeguroFactultativo().getEstatusRegistro())
                .concat(" - ")
                .concat(dtoSeguroFacultativo.getEstudiante().getPersona().getNombre().concat(" ").concat(dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoPaterno().concat(" ").concat(dtoSeguroFacultativo.getEstudiante().getPersona().getApellidoMaterno())))
                ;
    }
    
}
