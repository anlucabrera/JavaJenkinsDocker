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

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoIndicadoresPorCriterioConfiguracion implements Serializable{
    @Getter @Setter @NonNull Integer clave;
    @Getter @Setter @NonNull Integer configuracion;
    @Getter @Setter @NonNull Integer periodo;
    @Getter @Setter @NonNull String nombreUnidad;
    @Getter @Setter @NonNull Integer unidad;
    @Getter @Setter @NonNull Integer claveCriterio;
    @Getter @Setter @NonNull String criterio;
    @Getter @Setter @NonNull Double porcentaje;
    @Getter @Setter @NonNull Integer claveIndicador;
    @Getter @Setter @NonNull String indicador;
    @Getter @Setter @NonNull Double porcentajeIndicador;

    public DtoIndicadoresPorCriterioConfiguracion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
