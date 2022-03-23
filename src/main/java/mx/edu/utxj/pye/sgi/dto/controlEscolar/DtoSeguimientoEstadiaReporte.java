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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 * DTO Vista para seguimiento de estadía del asesor académico, coordinador, director y vinculación
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoSeguimientoEstadiaReporte implements Serializable, Comparable<DtoSeguimientoEstadiaReporte>{
    @Getter @Setter @NonNull SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter @NonNull Personal asesorEstadia;
    @Getter @Setter @NonNull AreasUniversidad areaSuperior;
    @Getter @Setter @NonNull Personal director;
    @Getter @Setter String nombreEmpresa;
    @Getter @Setter String direccionEmpresa;
    @Getter @Setter String proyecto;
    @Getter @Setter Double semanasEstadia;
    @Getter @Setter String validacionDireccion;
    @Getter @Setter String validacionCartaPresentacion;
    @Getter @Setter String comentariosCartaPresentacion;
    @Getter @Setter String validacionCartaAceptacion;
    @Getter @Setter String comentariosCartaAceptacion;
    @Getter @Setter String validacionLiberacionEmpresarial;
    @Getter @Setter String comentariosLiberacionEmpresarial;
    @Getter @Setter String validacionEvaluacioEmpresarial;
    @Getter @Setter String comentariosEvaluacioEmpresarial;
    @Getter @Setter String validacionCartaAcreditacion;
    @Getter @Setter String comentariosCartaAcreditacion;
    @Getter @Setter String validacionEstadia;


    public DtoSeguimientoEstadiaReporte() {
        
    }

    public DtoSeguimientoEstadiaReporte(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, DtoDatosEstudiante dtoEstudiante, Personal asesorEstadia, AreasUniversidad areaSuperior, Personal director, String nombreEmpresa, String direccionEmpresa, String proyecto, Double semanasEstadia, String validacionDireccion, String validacionCartaPresentacion, String comentariosCartaPresentacion, String validacionCartaAceptacion, String comentariosCartaAceptacion, String validacionLiberacionEmpresarial, String comentariosLiberacionEmpresarial, String validacionEvaluacioEmpresarial, String comentariosEvaluacioEmpresarial, String validacionCartaAcreditacion, String comentariosCartaAcreditacion, String validacionEstadia) {
        this.seguimientoEstadiaEstudiante = seguimientoEstadiaEstudiante;
        this.dtoEstudiante = dtoEstudiante;
        this.asesorEstadia = asesorEstadia;
        this.areaSuperior = areaSuperior;
        this.director = director;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionEmpresa = direccionEmpresa;
        this.proyecto = proyecto;
        this.semanasEstadia = semanasEstadia;
        this.validacionDireccion = validacionDireccion;
        this.validacionCartaPresentacion = validacionCartaPresentacion;
        this.comentariosCartaPresentacion = comentariosCartaPresentacion;
        this.validacionCartaAceptacion = validacionCartaAceptacion;
        this.comentariosCartaAceptacion = comentariosCartaAceptacion;
        this.validacionLiberacionEmpresarial = validacionLiberacionEmpresarial;
        this.comentariosLiberacionEmpresarial = comentariosLiberacionEmpresarial;
        this.validacionEvaluacioEmpresarial = validacionEvaluacioEmpresarial;
        this.comentariosEvaluacioEmpresarial = comentariosEvaluacioEmpresarial;
        this.validacionCartaAcreditacion = validacionCartaAcreditacion;
        this.comentariosCartaAcreditacion = comentariosCartaAcreditacion;
        this.validacionEstadia = validacionEstadia;
    }

    @Override
    public int compareTo(DtoSeguimientoEstadiaReporte o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoSeguimientoEstadiaReporte dtoSeguimientoEstadia){
         return dtoSeguimientoEstadia.getDtoEstudiante().getProgramaEducativo().getNombre().concat(" ")
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getGrupo().getLiteral().toString().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }

}
