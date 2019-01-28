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
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.NivelIngresosEgresadosGeneracion;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTONivelIngresoEgresadosG implements Serializable{
    private static final long serialVersionUID = -4164986590389741092L;
    @Getter @Setter @NonNull private NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter private Integer total;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTONivelIngresoEgresadosG(NivelIngresosEgresadosGeneracion nivelIngresosEgresadosGeneracion, String generacion, AreasUniversidad programaEducativo, Integer total) {
        this.nivelIngresosEgresadosGeneracion = nivelIngresosEgresadosGeneracion;
        this.generacion = generacion;
        this.programaEducativo = programaEducativo;
        this.total = total;
    }
    
}
