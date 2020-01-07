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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoHistorialMovEstudiante implements Serializable{
    @Getter @Setter @NonNull ProcesosInscripcion procesoInscripcion;
    @Getter @Setter @NonNull Aspirante aspirante;
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter List<DtoMovimientoEstudiante> listaDatosMovimiento;

    public DtoHistorialMovEstudiante(ProcesosInscripcion procesoInscripcion, Aspirante aspirante, Estudiante estudiante, AreasUniversidad programaEducativo, List<DtoMovimientoEstudiante> listaDatosMovimiento) {
        this.procesoInscripcion = procesoInscripcion;
        this.aspirante = aspirante;
        this.estudiante = estudiante;
        this.programaEducativo = programaEducativo;
        this.listaDatosMovimiento = listaDatosMovimiento;
    }

    public DtoHistorialMovEstudiante() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
