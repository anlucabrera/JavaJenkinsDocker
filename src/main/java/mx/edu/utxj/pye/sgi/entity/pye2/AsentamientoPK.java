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
public class AsentamientoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "asentamiento")
    private int asentamiento;

    public AsentamientoPK() {
    }

    public AsentamientoPK(int estado, int municipio, int asentamiento) {
        this.estado = estado;
        this.municipio = municipio;
        this.asentamiento = asentamiento;
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

    public int getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(int asentamiento) {
        this.asentamiento = asentamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) estado;
        hash += (int) municipio;
        hash += (int) asentamiento;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsentamientoPK)) {
            return false;
        }
        AsentamientoPK other = (AsentamientoPK) object;
        if (this.estado != other.estado) {
            return false;
        }
        if (this.municipio != other.municipio) {
            return false;
        }
        if (this.asentamiento != other.asentamiento) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.AsentamientoPK[ estado=" + estado + ", municipio=" + municipio + ", asentamiento=" + asentamiento + " ]";
    }
    
}
