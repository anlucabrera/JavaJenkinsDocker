/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
public class IndicadoresCarrouselPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "indicador")
    private int indicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave")
    private String clave;

    public IndicadoresCarrouselPK() {
    }

    public IndicadoresCarrouselPK(int indicador, String clave) {
        this.indicador = indicador;
        this.clave = clave;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) indicador;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadoresCarrouselPK)) {
            return false;
        }
        IndicadoresCarrouselPK other = (IndicadoresCarrouselPK) object;
        if (this.indicador != other.indicador) {
            return false;
        }
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.IndicadoresCarrouselPK[ indicador=" + indicador + ", clave=" + clave + " ]";
    }
    
}
