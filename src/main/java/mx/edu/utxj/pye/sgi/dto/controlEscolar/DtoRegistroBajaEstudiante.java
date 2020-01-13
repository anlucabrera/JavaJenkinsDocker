/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
/**
 *
 * @author UTXJ
 */
import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

@RequiredArgsConstructor @ToString
public class DtoRegistroBajaEstudiante implements Serializable{
    @Getter @Setter @NonNull Baja registroBaja;
    @Getter @Setter @NonNull BajasTipo tipoBaja;
    @Getter @Setter @NonNull BajasCausa causaBaja;
    @Getter @Setter @NonNull Personal personalRegistro;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter @NonNull String cicloEscolar;
    @Getter @Setter @NonNull String fechaBaja;

    public DtoRegistroBajaEstudiante() {
    }
}
