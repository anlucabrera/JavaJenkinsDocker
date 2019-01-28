/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "cuerposAcademicosRegistro")
@EqualsAndHashCode
public class DTOCuerposAcademicosR implements Serializable{
    private static final long serialVersionUID = 2309165007972946655L;
    @Getter @Setter @NonNull private CuerposAcademicosRegistro cuerposAcademicosRegistro;
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOCuerposAcademicosR(CuerposAcademicosRegistro cuerposAcademicosRegistro, AreasUniversidad area) {
        this.cuerposAcademicosRegistro = cuerposAcademicosRegistro;
        this.area = area;
    }
}
