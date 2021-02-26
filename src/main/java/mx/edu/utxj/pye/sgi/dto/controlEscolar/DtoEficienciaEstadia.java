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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEficienciaEstadia implements Serializable, Comparable<DtoEficienciaEstadia>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter Integer seguimiento;
    @Getter @Setter Integer acreditaron;
    @Getter @Setter Integer noAcreditaron;
    @Getter @Setter Double eficienciaEstadia;

    public DtoEficienciaEstadia() {
    }  

    public DtoEficienciaEstadia(AreasUniversidad programaEducativo, Integer seguimiento, Integer acreditaron, Integer noAcreditaron, Double eficienciaEstadia) {
        this.programaEducativo = programaEducativo;
        this.seguimiento = seguimiento;
        this.acreditaron = acreditaron;
        this.noAcreditaron = noAcreditaron;
        this.eficienciaEstadia = eficienciaEstadia;
    }
    
    @Override
    public int compareTo(DtoEficienciaEstadia o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEficienciaEstadia dtoEficienciaEstadia){
         return dtoEficienciaEstadia.getProgramaEducativo().getNombre();
    }
}
