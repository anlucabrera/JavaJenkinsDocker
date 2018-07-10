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
public class MunicipioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "claveEstado")
    private int claveEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveMunicipio")
    private int claveMunicipio;

    public MunicipioPK() {
    }

    public MunicipioPK(int claveEstado, int claveMunicipio) {
        this.claveEstado = claveEstado;
        this.claveMunicipio = claveMunicipio;
    }

    public int getClaveEstado() {
        return claveEstado;
    }

    public void setClaveEstado(int claveEstado) {
        this.claveEstado = claveEstado;
    }

    public int getClaveMunicipio() {
        return claveMunicipio;
    }

    public void setClaveMunicipio(int claveMunicipio) {
        this.claveMunicipio = claveMunicipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) claveEstado;
        hash += (int) claveMunicipio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipioPK)) {
            return false;
        }
        MunicipioPK other = (MunicipioPK) object;
        if (this.claveEstado != other.claveEstado) {
            return false;
        }
        if (this.claveMunicipio != other.claveMunicipio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK[ claveEstado=" + claveEstado + ", claveMunicipio=" + claveMunicipio + " ]";
    }
    
}
