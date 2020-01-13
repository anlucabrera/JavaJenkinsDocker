/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

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
public class ZonificacionesMunicipioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;

    public ZonificacionesMunicipioPK() {
    }

    public ZonificacionesMunicipioPK(int estado, int municipio) {
        this.estado = estado;
        this.municipio = municipio;
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
        hash += (int) estado;
        hash += (int) municipio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZonificacionesMunicipioPK)) {
            return false;
        }
        ZonificacionesMunicipioPK other = (ZonificacionesMunicipioPK) object;
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
        return "mx.edu.utxj.pye.sgi.entity.finanzas.ZonificacionesMunicipioPK[ estado=" + estado + ", municipio=" + municipio + " ]";
    }
    
}
