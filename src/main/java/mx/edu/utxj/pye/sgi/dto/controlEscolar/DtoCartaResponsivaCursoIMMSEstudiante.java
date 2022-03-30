/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoCartaResponsivaCursoIMMSEstudiante implements Serializable, Comparable<DtoCartaResponsivaCursoIMMSEstudiante>{
    @Getter @Setter @NonNull SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante;
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter List<DtoDocumentosCartaRespCursoIMMSEstudiante> listaDocumentosVinculacion;

    public DtoCartaResponsivaCursoIMMSEstudiante(SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante, DtoDatosEstudiante dtoEstudiante, List<DtoDocumentosCartaRespCursoIMMSEstudiante> listaDocumentosVinculacion) {
        this.seguimientoVinculacionEstudiante = seguimientoVinculacionEstudiante;
        this.dtoEstudiante = dtoEstudiante;
        this.listaDocumentosVinculacion = listaDocumentosVinculacion;
    }
    
    @Override
    public int compareTo(DtoCartaResponsivaCursoIMMSEstudiante o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante){
         return dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getProgramaEducativo().getNombre().concat(" ")
                 .concat(dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getEstudiante().getGrupo().getLiteral().toString().concat(" "))
                 .concat(dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoCartaResponsivaCursoIMMSEstudiante.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }
}
