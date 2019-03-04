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
public class FuenteInformacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "indicador")
    private int indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;

    public FuenteInformacionPK() {
    }

    public FuenteInformacionPK(int indicador, short area) {
        this.indicador = indicador;
        this.area = area;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) indicador;
        hash += (int) area;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuenteInformacionPK)) {
            return false;
        }
        FuenteInformacionPK other = (FuenteInformacionPK) object;
        if (this.indicador != other.indicador) {
            return false;
        }
        if (this.area != other.area) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.FuenteInformacionPK[ indicador=" + indicador + ", area=" + area + " ]";
    }
    
}
