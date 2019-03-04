/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.caphum;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicas;
import mx.edu.utxj.pye.sgi.entity.pye2.ComisionesAcademicasTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "comisionesAcademicas")
@EqualsAndHashCode
public class DTOComisionesAcademicas implements Serializable{

    private static final long serialVersionUID = -2261179161525106960L;
    @Getter @Setter @NonNull private ComisionesAcademicas comisionesAcademicas; //se declara como llave primaria para interacturar con sus evidencias
    @Getter @Setter private ComisionesAcademicasTipos comisionesAcademicasTipos;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private EventosRegistros eventoSeleccionado;
    @Getter @Setter private String tipoComision;
    
}
