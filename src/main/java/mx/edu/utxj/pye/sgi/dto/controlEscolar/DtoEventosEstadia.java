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

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEventosEstadia implements Serializable{
    @Getter @Setter @NonNull String actividad;
    @Getter @Setter @NonNull String usuario;
    @Getter @Setter @NonNull Date fechaInicio;
    @Getter @Setter @NonNull Date fechaFin;
}
