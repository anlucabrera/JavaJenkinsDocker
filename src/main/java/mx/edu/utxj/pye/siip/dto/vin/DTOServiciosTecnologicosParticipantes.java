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
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosParticipantes;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "serviciosTecnologicosParticipantes")
@EqualsAndHashCode
public class DTOServiciosTecnologicosParticipantes implements Serializable{
    private static final long serialVersionUID = -8886379137437964410L;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull private ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes;
    @Getter @Setter private String organismoVinculadoString;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOServiciosTecnologicosParticipantes(String generacionToString, AreasUniversidad programaEducativo, ServiciosTecnologicosParticipantes serviciosTecnologicosParticipantes) {
        this.generacion = generacionToString;
        this.programaEducativo = programaEducativo;
        this.serviciosTecnologicosParticipantes = serviciosTecnologicosParticipantes;
    }
}
