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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepTiposApoyo;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "reconocimientoProdepRegistros")
@EqualsAndHashCode
public class DTOReconocimientoProdep implements Serializable{

    private static final long serialVersionUID = -8572316516218981962L;
    @Getter @Setter @NonNull private ReconocimientoProdepRegistros reconocimientoProdepRegistros;
    @Getter @Setter private Personal personal;
    @Getter @Setter private CuerposAcademicosRegistro cuerposAcademicosRegistro;
    @Getter @Setter private ReconocimientoProdepTiposApoyo reconocimientoProdepTiposApoyo;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    @Getter @Setter private EventosRegistros eventoSeleccionado;
    @Getter @Setter private String tipo;
    
}
