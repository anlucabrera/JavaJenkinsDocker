/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
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
public class DtoValidacionesBaja implements Serializable{
    @Getter @Setter @NonNull String areaValidacionBaja;
    @Getter @Setter String fechaValidacionBaja;
    @Getter @Setter @NonNull String validacionBaja;
    @Getter @Setter String fechaValidacionPsic;
    @Getter @Setter @NonNull String validacionPsic;

    public DtoValidacionesBaja(String areaValidacionBaja, String fechaValidacionBaja, String validacionBaja, String fechaValidacionPsic, String validacionPsic) {
        this.areaValidacionBaja = areaValidacionBaja;
        this.fechaValidacionBaja = fechaValidacionBaja;
        this.validacionBaja = validacionBaja;
        this.fechaValidacionPsic =  fechaValidacionPsic;
        this.validacionPsic = validacionPsic;
    }
    
    
}
