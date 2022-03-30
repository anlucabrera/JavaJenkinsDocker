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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoCumplimientoCartaResponsivaCursoIMSS implements Serializable, Comparable<DtoCumplimientoCartaResponsivaCursoIMSS>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull DocumentoProceso documentoProceso;
    @Getter @Setter Integer estudiantesIniciaron;
    @Getter @Setter Integer estudiantesActivos;
    @Getter @Setter Integer conDocumento;
    @Getter @Setter Integer sinDocumento;
    @Getter @Setter Integer documentoValidado;
    @Getter @Setter Double porcentajeCumplimiento;
    @Getter @Setter Double porcentajeValidacion;

    public DtoCumplimientoCartaResponsivaCursoIMSS() {
    }
    
    @Override
    public int compareTo(DtoCumplimientoCartaResponsivaCursoIMSS o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoCumplimientoCartaResponsivaCursoIMSS dtoCumplimientoCartaResponsivaCursoIMSS){
         return dtoCumplimientoCartaResponsivaCursoIMSS.getProgramaEducativo().getNombre().concat(" ")
                 .concat(String.valueOf(dtoCumplimientoCartaResponsivaCursoIMSS.getDocumentoProceso().getDocumento().getDocumento()));
    }
    
}
