/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;

/**
 *
 * @author UTXJ
 */
public class DtoDocumentosCartaRespCursoIMMSEstudiante {
    @Getter @Setter @NonNull DocumentoProceso documentoProceso;
    @Getter @Setter DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion;
    @Getter @Setter String generacion;

    public DtoDocumentosCartaRespCursoIMMSEstudiante(DocumentoProceso documentoProceso, DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion, String generacion) {
        this.documentoProceso = documentoProceso;
        this.documentoSeguimientoVinculacion = documentoSeguimientoVinculacion;
        this.generacion = generacion;
    }
    
}
