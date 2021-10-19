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
public class DtoEficienciaEstadiaDatosComplementarios implements Serializable, Comparable<DtoEficienciaEstadiaDatosComplementarios>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter Integer seguimiento;
    @Getter @Setter Integer acreditaron;
    @Getter @Setter Integer noAcreditaron;
    @Getter @Setter Double eficienciaEstadia;
    @Getter @Setter Integer validadosDireccion;
    @Getter @Setter Integer noValidadosDireccion;
    @Getter @Setter Double eficienciaValidacionDireccion;

    public DtoEficienciaEstadiaDatosComplementarios() {
    }  

    public DtoEficienciaEstadiaDatosComplementarios(AreasUniversidad programaEducativo, Integer seguimiento, Integer acreditaron, Integer noAcreditaron, Double eficienciaEstadia, Integer validadosDireccion, Integer noValidadosDireccion, Double eficienciaValidacionDireccion) {
        this.programaEducativo = programaEducativo;
        this.seguimiento = seguimiento;
        this.acreditaron = acreditaron;
        this.noAcreditaron = noAcreditaron;
        this.eficienciaEstadia = eficienciaEstadia;
        this.validadosDireccion = validadosDireccion;
        this.noValidadosDireccion = noValidadosDireccion;
        this.eficienciaValidacionDireccion = eficienciaValidacionDireccion;
    }

    @Override
    public int compareTo(DtoEficienciaEstadiaDatosComplementarios o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEficienciaEstadiaDatosComplementarios  dtoEficienciaEstadiaDatosComplementarios){
         return dtoEficienciaEstadiaDatosComplementarios.getProgramaEducativo().getNombre();
    }
}
