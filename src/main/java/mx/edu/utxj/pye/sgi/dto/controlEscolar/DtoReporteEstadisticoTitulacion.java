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
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoReporteEstadisticoTitulacion implements Serializable{
    @Getter @Setter @NonNull Generaciones generacion;
    @Getter @Setter @NonNull ProgramasEducativosNiveles nivelEducativo;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Integer estudiantesIniciaron;
    @Getter @Setter @NonNull Integer estudiantesActivos;
    @Getter @Setter @NonNull Integer estudiantesAcreditados;
    @Getter @Setter @NonNull Integer estudiantesExpediente;
    @Getter @Setter @NonNull Integer estudiantesExpedienteValidado;
}
