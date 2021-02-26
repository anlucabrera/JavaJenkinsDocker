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

/**
 * DTO Para mostrar el listado del personal por área con o sin asignación de roles de estadía
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoRolEstadia implements Serializable{
    @Getter @Setter @NonNull Personal Personal;
    @Getter @Setter String rolCoordinador;
    @Getter @Setter String rolAsesor;

    public DtoRolEstadia(Personal Personal, String rolCoordinador, String rolAsesor) {
        this.Personal = Personal;
        this.rolCoordinador = rolCoordinador;
        this.rolAsesor = rolAsesor;
    }

    
}
