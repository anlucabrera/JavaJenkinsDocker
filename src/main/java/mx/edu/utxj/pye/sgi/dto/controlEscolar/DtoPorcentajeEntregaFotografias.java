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
    @Getter @Setter Integer totalEstudiantes;
    @Getter @Setter Integer totalEntrega;
    @Getter @Setter Integer totalPendientes;
    @Getter @Setter Double porcentajeEntrega;

    public DtoPorcentajeEntregaFotografias(AreasUniversidad programaEducativo, Integer totalEstudiantes, Integer totalEntrega, Integer totalPendientes, Double porcentajeEntrega) {
        this.programaEducativo = programaEducativo;
        this.totalEstudiantes = totalEstudiantes;
        this.totalEntrega = totalEntrega;
        this.totalPendientes = totalPendientes;
        this.porcentajeEntrega = porcentajeEntrega;
    }
    
    @Override
    public int compareTo(DtoPorcentajeEntregaFotografias o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoPorcentajeEntregaFotografias dtoPorcentajeEntregaFotografias){
         return dtoPorcentajeEntregaFotografias.getProgramaEducativo().getNombre();
    }
    
}
