/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.prontuario.list;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;

/**
 *
 * @author UTXJ
 */
public class ListaOrganismosVinculados implements Serializable {

    private static final long serialVersionUID = -7441627752552560966L;
    @Getter @Setter private Date fecha_reporte;
    @Getter @Setter private List<OrganismosVinculados> organismosVinculadosLst;
}