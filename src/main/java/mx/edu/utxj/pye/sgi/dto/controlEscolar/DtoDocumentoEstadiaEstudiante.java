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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;

/**
 * DTO Para documentos de estadía del estudiante
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoDocumentoEstadiaEstudiante implements Serializable{
    @Getter @Setter @NonNull DocumentoProceso documentoProceso;
    @Getter @Setter DocumentoSeguimientoEstadia documentoSeguimientoEstadia;
    @Getter @Setter String generacion;

    public DtoDocumentoEstadiaEstudiante(DocumentoProceso documentoProceso, DocumentoSeguimientoEstadia documentoSeguimientoEstadia, String generacion) {
        this.documentoProceso = documentoProceso;
        this.documentoSeguimientoEstadia = documentoSeguimientoEstadia;
        this.generacion = generacion;
    }
}
