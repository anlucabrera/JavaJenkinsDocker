/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author HOME
 */
@Embeddable
public class UnidadMateriaConfiguracionCriterioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private int configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio")
    private int criterio;

    public UnidadMateriaConfiguracionCriterioPK() {
    }

    public UnidadMateriaConfiguracionCriterioPK(int configuracion, int criterio) {
        this.configuracion = configuracion;
        this.criterio = criterio;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) configuracion;
        hash += (int) criterio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaConfiguracionCriterioPK)) {
            return false;
        }
        UnidadMateriaConfiguracionCriterioPK other = (UnidadMateriaConfiguracionCriterioPK) object;
        if (this.configuracion != other.configuracion) {
            return false;
        }
        if (this.criterio != other.criterio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionCriterioPK[ configuracion=" + configuracion + ", criterio=" + criterio + " ]";
    }
    
}
