/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "estudiante")
public class EstudianteDto implements Serializable{
    @Getter @Setter @NonNull private Inscripcion estudiante;
    @Getter @Setter private Persona persona;
    @Getter @Setter private Generos genero;
    @Getter @Setter private PeriodosEscolares periodoEscolar;
    @Getter @Setter private AreasUniversidad programaEducativo;
    
}
