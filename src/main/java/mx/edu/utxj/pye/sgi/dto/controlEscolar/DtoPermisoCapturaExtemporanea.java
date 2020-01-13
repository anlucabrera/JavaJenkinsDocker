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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaGrupal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoPermisoCapturaExtemporanea implements Serializable{
    @Getter @Setter @NonNull PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Personal personal;
}
