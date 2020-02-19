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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;



/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAsignadosIndicadoresCriterios implements Serializable{
    @Getter @Setter @NonNull UnidadMateriaConfiguracionDetalle configuracionDetalle;
    @Getter @Setter @NonNull UnidadMateriaConfiguracion configuracion;
    @Getter @Setter @NonNull UnidadMateria unidadMateria;
    @Getter @Setter @NonNull Criterio criterio;
    @Getter @Setter @NonNull Indicador indicador;

}
