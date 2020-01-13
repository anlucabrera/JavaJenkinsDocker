/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoFormatoBaja implements Serializable{
    @Getter @Setter @NonNull DtoRegistroBajaEstudiante registroBajaEstudiante;
    @Getter @Setter @NonNull List<DtoMateriaReprobada> materiasReprobadas;
    @Getter @Setter @NonNull List<DtoDocumentosEstudiante> documentosEstudiante;
    @Getter @Setter @NonNull Personal directorCarrera;
    @Getter @Setter @NonNull Personal tutorGrupo;
    
}
