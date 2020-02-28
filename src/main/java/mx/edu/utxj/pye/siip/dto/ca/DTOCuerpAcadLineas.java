/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "cuerposAcademicosLineasInvestigacion")
@EqualsAndHashCode
public class DTOCuerpAcadLineas implements Serializable{
    private static final long serialVersionUID = 2063346014227678078L;
    @Getter @Setter @NonNull private CuerpacadLineas cuerpoAcademicoLineas;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
