/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadEconomicaEgresadoGeneracion;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOActividadEconomicaEgresadoG implements Serializable{
    private static final long serialVersionUID = 631730979592878172L;
    @Getter @Setter @NonNull private ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOActividadEconomicaEgresadoG(ActividadEconomicaEgresadoGeneracion actividadEconomicaEgresadoGeneracion, String generacion, AreasUniversidad programaEducativo) {
        this.actividadEconomicaEgresadoGeneracion = actividadEconomicaEgresadoGeneracion;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
    }
    
}
