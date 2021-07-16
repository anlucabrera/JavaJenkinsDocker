/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class UniversidadEgresoAspirantePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "aspirante")
    private int aspirante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "universidad_egreso")
    private int universidadEgreso;

    public UniversidadEgresoAspirantePK() {
    }

    public UniversidadEgresoAspirantePK(int aspirante, int universidadEgreso) {
        this.aspirante = aspirante;
        this.universidadEgreso = universidadEgreso;
    }

    public int getAspirante() {
        return aspirante;
    }

    public void setAspirante(int aspirante) {
        this.aspirante = aspirante;
    }

    public int getUniversidadEgreso() {
        return universidadEgreso;
    }

    public void setUniversidadEgreso(int universidadEgreso) {
        this.universidadEgreso = universidadEgreso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) aspirante;
        hash += (int) universidadEgreso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniversidadEgresoAspirantePK)) {
            return false;
        }
        UniversidadEgresoAspirantePK other = (UniversidadEgresoAspirantePK) object;
        if (this.aspirante != other.aspirante) {
            return false;
        }
        if (this.universidadEgreso != other.universidadEgreso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UniversidadEgresoAspirantePK[ aspirante=" + aspirante + ", universidadEgreso=" + universidadEgreso + " ]";
    }
    
}
