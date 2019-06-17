/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;

import mx.edu.utxj.pye.sgi.saiiut.entity.PlanesEstudio;
import mx.edu.utxj.pye.sgi.saiiut.entity.Materias1;
import mx.edu.utxj.pye.sgi.saiiut.entity.CalificacionesAlumno;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "calificacionesSaiiut")
public class CalificacionesSaiiutDto implements Serializable{
    @Getter @Setter @NonNull private CalificacionesAlumno calificacionesAlumno;
    @Getter @Setter private Alumnos alumno;
    @Getter @Setter private PlanesEstudio planesEstudio;
    @Getter @Setter private Materias1 materia1;
    
}
