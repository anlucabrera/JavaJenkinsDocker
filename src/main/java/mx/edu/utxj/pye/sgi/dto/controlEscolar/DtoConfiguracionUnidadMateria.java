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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;

@RequiredArgsConstructor @ToString
public class DtoConfiguracionUnidadMateria implements Serializable{
    @Getter @Setter @NonNull UnidadMateria unidadMateria;
    @Getter @Setter @NonNull UnidadMateriaConfiguracion unidadMateriaConfiguracion;

}
