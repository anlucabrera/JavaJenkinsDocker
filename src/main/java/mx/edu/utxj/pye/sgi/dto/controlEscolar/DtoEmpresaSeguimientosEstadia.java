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
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEmpresaSeguimientosEstadia implements Serializable, Comparable<DtoEmpresaSeguimientosEstadia>{
    
    @Getter @Setter @NonNull OrganismosVinculados empresa;
    @Getter @Setter @NonNull Integer seguimientosAsignados;
    
    
    @Override
    public int compareTo(DtoEmpresaSeguimientosEstadia o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEmpresaSeguimientosEstadia dtoEmpresaSeguimientosEstadia){
         return dtoEmpresaSeguimientosEstadia.getEmpresa().getNombre();
    }
}
