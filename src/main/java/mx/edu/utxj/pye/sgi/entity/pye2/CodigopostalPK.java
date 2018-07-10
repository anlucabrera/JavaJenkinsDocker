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
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class CodigopostalPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo_postal")
    private String codigoPostal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "consecutivo_cp")
    private int consecutivoCp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;

    public CodigopostalPK() {
    }

    public CodigopostalPK(String codigoPostal, int consecutivoCp, int estado, int municipio) {
        this.codigoPostal = codigoPostal;
        this.consecutivoCp = consecutivoCp;
        this.estado = estado;
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getConsecutivoCp() {
        return consecutivoCp;
    }

    public void setConsecutivoCp(int consecutivoCp) {
        this.consecutivoCp = consecutivoCp;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoPostal != null ? codigoPostal.hashCode() : 0);
        hash += (int) consecutivoCp;
        hash += (int) estado;
        hash += (int) municipio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CodigopostalPK)) {
            return false;
        }
        CodigopostalPK other = (CodigopostalPK) object;
        if ((this.codigoPostal == null && other.codigoPostal != null) || (this.codigoPostal != null && !this.codigoPostal.equals(other.codigoPostal))) {
            return false;
        }
        if (this.consecutivoCp != other.consecutivoCp) {
            return false;
        }
        if (this.estado != other.estado) {
            return false;
        }
        if (this.municipio != other.municipio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CodigopostalPK[ codigoPostal=" + codigoPostal + ", consecutivoCp=" + consecutivoCp + ", estado=" + estado + ", municipio=" + municipio + " ]";
    }
    
}
