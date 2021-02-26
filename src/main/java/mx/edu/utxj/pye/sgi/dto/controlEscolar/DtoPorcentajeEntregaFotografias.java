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
public class DtoPorcentajeEntregaFotografias implements Serializable, Comparable<DtoPorcentajeEntregaFotografias>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Integer totalEstudiantes;
    @Getter @Setter @NonNull Integer totalEntrega;
    @Getter @Setter @NonNull Integer totalPendientes;
    @Getter @Setter @NonNull Double porcentajeEntrega;
    
    @Override
    public int compareTo(DtoPorcentajeEntregaFotografias o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoPorcentajeEntregaFotografias dtoPorcentajeEntregaFotografias){
         return dtoPorcentajeEntregaFotografias.getProgramaEducativo().getNombre();
    }
    
}
