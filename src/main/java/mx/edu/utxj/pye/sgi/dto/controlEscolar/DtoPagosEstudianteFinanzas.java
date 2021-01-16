/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoPagosEstudianteFinanzas implements Serializable{
    
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

    public DtoPagosEstudianteFinanzas() {
    }
    
    public DtoPagosEstudianteFinanzas(Integer concepto, Integer cveRegistro, String descripcion, Date fechaPago, String idCataloogoConceptoPago, String matricula, Integer monto, String siglas, Short valcartanoadueudoTSU, Short valcartanoadedudoIng) {
        this.concepto = concepto;
        this.cveRegistro = cveRegistro;
        this.descripcion = descripcion;
        this.fechaPago = fechaPago;
        this.idCataloogoConceptoPago = idCataloogoConceptoPago;
        this.matricula = matricula;
        this.monto = monto;
        this.siglas = siglas;
        this.valcartanoadueudoTSU = valcartanoadueudoTSU;
        this.valcartanoadedudoIng = valcartanoadedudoIng;
    }
    
    
    
}
