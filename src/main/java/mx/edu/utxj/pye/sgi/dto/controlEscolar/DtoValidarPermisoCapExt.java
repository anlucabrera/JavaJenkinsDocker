/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoValidarPermisoCapExt implements Serializable {
    @Getter @Setter @NonNull Integer clavePermiso;
    @Getter @Setter @NonNull String periodoEscolar;
    @Getter @Setter @NonNull String tipoApertura;
    @Getter @Setter @NonNull String tipoSolicitud;
    @Getter @Setter String estudiante;
    @Getter @Setter @NonNull Grupo grupo;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull PlanEstudioMateria planMateria;
    @Getter @Setter UnidadMateria unidadMateria;
    @Getter @Setter @NonNull String unidad;
    @Getter @Setter @NonNull Personal docente;
    @Getter @Setter @NonNull String tipoEvaluacion;
    @Getter @Setter @NonNull String rangoFechas;
    @Getter @Setter @NonNull String justificacion;
    @Getter @Setter Personal personaValido;
    @Getter @Setter Date fechaValidacion;
    @Getter @Setter Boolean validado;

    public DtoValidarPermisoCapExt() {
    }

    
    
    public DtoValidarPermisoCapExt(Integer clavePermiso, String periodoEscolar, String tipoApertura, String tipoSolicitud, String estudiante, Grupo grupo, AreasUniversidad programaEducativo, PlanEstudioMateria planMateria, UnidadMateria unidadMateria, String unidad, Personal docente, String tipoEvaluacion, String rangoFechas, String justificacion, Personal personaValido, Date fechaValidacion, Boolean validado) {
        this.clavePermiso = clavePermiso;
        this.periodoEscolar = periodoEscolar;
        this.tipoApertura = tipoApertura;
        this.tipoSolicitud = tipoSolicitud;
        this.estudiante = estudiante;
        this.grupo = grupo;
        this.programaEducativo = programaEducativo;
        this.planMateria = planMateria;
        this.unidadMateria = unidadMateria;
        this.unidad = unidad;
        this.docente = docente;
        this.tipoEvaluacion = tipoEvaluacion;
        this.rangoFechas = rangoFechas;
        this.justificacion = justificacion;
        this.personaValido = personaValido;
        this.fechaValidacion = fechaValidacion;
        this.validado = validado;
    }
}
