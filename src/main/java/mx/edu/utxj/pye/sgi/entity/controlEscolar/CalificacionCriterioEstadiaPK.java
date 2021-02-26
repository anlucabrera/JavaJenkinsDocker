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
 * @author UTXJ
 */
@Embeddable
public class CalificacionCriterioEstadiaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "seguimiento")
    private int seguimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio")
    private int criterio;

    public CalificacionCriterioEstadiaPK() {
    }

    public CalificacionCriterioEstadiaPK(int seguimiento, int criterio) {
        this.seguimiento = seguimiento;
        this.criterio = criterio;
    }

    public int getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(int seguimiento) {
        this.seguimiento = seguimiento;
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
        hash += (int) seguimiento;
        hash += (int) criterio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionCriterioEstadiaPK)) {
            return false;
        }
        CalificacionCriterioEstadiaPK other = (CalificacionCriterioEstadiaPK) object;
        if (this.seguimiento != other.seguimiento) {
            return false;
        }
        if (this.criterio != other.criterio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadiaPK[ seguimiento=" + seguimiento + ", criterio=" + criterio + " ]";
    }
    
}
