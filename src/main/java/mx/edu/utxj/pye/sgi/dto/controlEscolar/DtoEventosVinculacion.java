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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEventosVinculacion implements Serializable{
    @Getter @Setter @NonNull EventoVinculacion  eventoVinculacion;
    @Getter @Setter @NonNull Generaciones generacion;
    @Getter @Setter @NonNull ProgramasEducativosNiveles nivel;
}
