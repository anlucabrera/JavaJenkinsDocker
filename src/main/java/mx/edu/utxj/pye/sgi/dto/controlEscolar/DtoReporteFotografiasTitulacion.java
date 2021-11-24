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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoReporteFotografiasTitulacion implements Serializable, Comparable<DtoReporteFotografiasTitulacion>{
    @Getter @Setter @NonNull Generaciones generacion;
    @Getter @Setter @NonNull ProgramasEducativosNiveles nivelEducativo;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter ExpedienteTitulacion expedienteTitulacion;
    @Getter @Setter MedioComunicacion medioComunicacion;
    @Getter @Setter String situacionExpediente;
    @Getter @Setter String situacionFotografia;
    @Getter @Setter String fechaFotografia;
    @Getter @Setter String rutaFotografia;

    public DtoReporteFotografiasTitulacion(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad programaEducativo, Estudiante estudiante, ExpedienteTitulacion expedienteTitulacion, MedioComunicacion medioComunicacion, String situacionExpediente, String situacionFotografia, String fechaFotografia, String rutaFotografia) {
        this.generacion = generacion;
        this.nivelEducativo = nivelEducativo;
        this.programaEducativo = programaEducativo;
        this.estudiante = estudiante;
        this.expedienteTitulacion = expedienteTitulacion;
        this.medioComunicacion = medioComunicacion;
        this.situacionExpediente = situacionExpediente;
        this.situacionFotografia = situacionFotografia;
        this.fechaFotografia = fechaFotografia;
        this.rutaFotografia = rutaFotografia;
    }

    @Override
    public int compareTo(DtoReporteFotografiasTitulacion o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoReporteFotografiasTitulacion  dtoReporteFotografiasTitulacion){
         return dtoReporteFotografiasTitulacion.getProgramaEducativo().getNombre().concat("")
                 .concat(dtoReporteFotografiasTitulacion.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoReporteFotografiasTitulacion.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoReporteFotografiasTitulacion.getEstudiante().getAspirante().getIdPersona().getNombre());
    }
}
