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
public class DtoReporteEstadiaVinculacion implements Serializable, Comparable<DtoReporteEstadiaVinculacion>{
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Integer estudiantesActivos;
    @Getter @Setter Integer estudiantesEvidencia;
    @Getter @Setter Integer estudiantesSinEvidencia;
    @Getter @Setter Integer estudiantesEvidenciaValidada;
    @Getter @Setter Integer estudiantesEvidenciaNoValidada;
    @Getter @Setter Double porcentajeCumplimiento;
    @Getter @Setter Double porcentajeValidacion;

    public DtoReporteEstadiaVinculacion(AreasUniversidad programaEducativo, Integer estudiantesActivos, Integer estudiantesEvidencia, Integer estudiantesSinEvidencia, Integer estudiantesEvidenciaValidada, Integer estudiantesEvidenciaNoValidada, Double porcentajeCumplimiento, Double porcentajeValidacion) {
        this.programaEducativo = programaEducativo;
        this.estudiantesActivos = estudiantesActivos;
        this.estudiantesEvidencia = estudiantesEvidencia;
        this.estudiantesSinEvidencia = estudiantesSinEvidencia;
        this.estudiantesEvidenciaValidada = estudiantesEvidenciaValidada;
        this.estudiantesEvidenciaNoValidada = estudiantesEvidenciaNoValidada;
        this.porcentajeCumplimiento = porcentajeCumplimiento;
        this.porcentajeValidacion = porcentajeValidacion;
    }
    
    @Override
    public int compareTo(DtoReporteEstadiaVinculacion o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoReporteEstadiaVinculacion dtoReporteEstadiaVinculacion){
         return dtoReporteEstadiaVinculacion.getProgramaEducativo().getNombre();
    }
    
}
