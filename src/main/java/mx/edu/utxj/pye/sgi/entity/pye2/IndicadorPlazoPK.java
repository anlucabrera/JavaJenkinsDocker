/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class IndicadorPlazoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "indicador")
    private int indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejercicio_fiscal")
    private short ejercicioFiscal;

    public IndicadorPlazoPK() {
    }

    public IndicadorPlazoPK(int indicador, short ejercicioFiscal) {
        this.indicador = indicador;
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public short getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) indicador;
        hash += (int) ejercicioFiscal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorPlazoPK)) {
            return false;
        }
        IndicadorPlazoPK other = (IndicadorPlazoPK) object;
        if (this.indicador != other.indicador) {
            return false;
        }
        if (this.ejercicioFiscal != other.ejercicioFiscal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.IndicadorPlazoPK[ indicador=" + indicador + ", ejercicioFiscal=" + ejercicioFiscal + " ]";
    }
    
}
