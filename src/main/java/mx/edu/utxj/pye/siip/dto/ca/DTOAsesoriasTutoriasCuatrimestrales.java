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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCuatrimestrales;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DTOAsesoriasTutoriasCuatrimestrales implements Serializable{
    private static final long serialVersionUID = 2706457493140886907L;
    @Getter @Setter @NonNull private AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral;
    @Getter @Setter private PeriodosEscolares periodosEscolares;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOAsesoriasTutoriasCuatrimestrales(AsesoriasTutoriasCuatrimestrales asesoriaTutoriaCuatrimestral, PeriodosEscolares periodosEscolares, AreasUniversidad areasUniversidad) {
        this.asesoriaTutoriaCuatrimestral = asesoriaTutoriaCuatrimestral;
        this.periodosEscolares = periodosEscolares;
        this.areasUniversidad = areasUniversidad;
    }
    
}
