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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;

/**
 * DTO Vista para seguimiento de estadía del estudiante
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoSeguimientoEstadiaEstudiante implements Serializable{
    @Getter @Setter @NonNull SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter @NonNull String fechaRegistro;
    @Getter @Setter List<DtoDocumentoEstadiaEstudiante> listaDocumentosEstadia;

    public DtoSeguimientoEstadiaEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, DtoDatosEstudiante dtoEstudiante, String fechaRegistro, List<DtoDocumentoEstadiaEstudiante> listaDocumentosEstadia) {
        this.seguimientoEstadiaEstudiante = seguimientoEstadiaEstudiante;
        this.dtoEstudiante = dtoEstudiante;
        this.fechaRegistro = fechaRegistro;
        this.listaDocumentosEstadia = listaDocumentosEstadia;
    }
    
}
