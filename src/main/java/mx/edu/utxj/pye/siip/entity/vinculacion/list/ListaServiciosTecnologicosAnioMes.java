/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.vinculacion.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOServiciosTecnologicosParticipantes;

/**
 *
 * @author UTXJ
 */
public class ListaServiciosTecnologicosAnioMes implements Serializable{

    private static final long serialVersionUID = 6012489508579587606L;
    @Getter @Setter private List<ServiciosTecnologicosAnioMes> serviciosTecnologicosAnioMes;
    @Getter @Setter private List<DTOServiciosTecnologicosParticipantes> dtoServiciosTecnologicosParticipantes;
}
