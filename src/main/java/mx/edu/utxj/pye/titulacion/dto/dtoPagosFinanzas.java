/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoPagosFinanzas")
@EqualsAndHashCode
public class dtoPagosFinanzas implements Serializable{

    private static final long serialVersionUID = 5748066900424598601L;
    @Getter @Setter private Integer concepto;
    @Getter @Setter @NonNull private Integer cveRegistro;
    @Getter @Setter private String descripcion;
    @Getter @Setter private Date fechaPago;
    @Getter @Setter private String idCataloogoConceptoPago;
    @Getter @Setter private String matricula;
    @Getter @Setter private Integer monto;
    @Getter @Setter private String siglas;
    @Getter @Setter private Short valcartanoadueudoTSU; 
    @Getter @Setter private Short valcartanoadedudoIng;
}
