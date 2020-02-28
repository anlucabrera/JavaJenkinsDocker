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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

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
    @Getter @Setter String situacionActual;
    @Getter @Setter String programaEducativoActual;
    @Getter @Setter PeriodosEscolares periodoActual;

    public DtoHistorialMovEstudiante(ProcesosInscripcion procesoInscripcion, Aspirante aspirante, Estudiante estudiante, AreasUniversidad programaEducativo, List<DtoMovimientoEstudiante> listaDatosMovimiento, String situacionActual, String programaEducativoActual, PeriodosEscolares periodoActual) {
        this.procesoInscripcion = procesoInscripcion;
        this.aspirante = aspirante;
        this.estudiante = estudiante;
        this.programaEducativo = programaEducativo;
        this.listaDatosMovimiento = listaDatosMovimiento;
        this.situacionActual = situacionActual;
        this.programaEducativoActual = programaEducativoActual;
        this.periodoActual = periodoActual;
    }

    public DtoHistorialMovEstudiante() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
