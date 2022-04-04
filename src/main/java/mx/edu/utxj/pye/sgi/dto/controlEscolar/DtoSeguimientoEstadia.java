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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 * DTO Vista para seguimiento de estadía del asesor académico, coordinador, director y vinculación
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoSeguimientoEstadia implements Serializable, Comparable<DtoSeguimientoEstadia>{
    @Getter @Setter @NonNull SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter @NonNull Personal asesorEstadia;
    @Getter @Setter @NonNull AreasUniversidad areaSuperior;
    @Getter @Setter @NonNull Personal director;
    @Getter @Setter OrganismosVinculados empresa;
    @Getter @Setter AsesorEmpresarialEstadia asesorEmpresarialEstadia;
    @Getter @Setter Double semanasEstadia;


    public DtoSeguimientoEstadia() {
        
    }

    public DtoSeguimientoEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, DtoDatosEstudiante dtoEstudiante, Personal asesorEstadia, AreasUniversidad areaSuperior, Personal director, OrganismosVinculados empresa, AsesorEmpresarialEstadia asesorEmpresarialEstadia, Double semanasEstadia) {
        this.seguimientoEstadiaEstudiante = seguimientoEstadiaEstudiante;
        this.dtoEstudiante = dtoEstudiante;
        this.asesorEstadia = asesorEstadia;
        this.areaSuperior = areaSuperior;
        this.director = director;
        this.empresa = empresa;
        this.asesorEmpresarialEstadia = asesorEmpresarialEstadia;
        this.semanasEstadia = semanasEstadia;
    }

    @Override
    public int compareTo(DtoSeguimientoEstadia o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoSeguimientoEstadia dtoSeguimientoEstadia){
         return dtoSeguimientoEstadia.getDtoEstudiante().getProgramaEducativo().getNombre().concat(" ")
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getGrupo().getLiteral().toString().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoSeguimientoEstadia.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }

}
