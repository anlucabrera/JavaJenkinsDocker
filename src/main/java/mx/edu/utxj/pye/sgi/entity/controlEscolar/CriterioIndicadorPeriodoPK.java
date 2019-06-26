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
public class CriterioIndicadorPeriodoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio")
    private int criterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "indicador")
    private int indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;

    public CriterioIndicadorPeriodoPK() {
    }

    public CriterioIndicadorPeriodoPK(int criterio, int indicador, int periodo) {
        this.criterio = criterio;
        this.indicador = indicador;
        this.periodo = periodo;
    }

    public int getCriterio() {
        return criterio;
    }

    public void setCriterio(int criterio) {
        this.criterio = criterio;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) criterio;
        hash += (int) indicador;
        hash += (int) periodo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioIndicadorPeriodoPK)) {
            return false;
        }
        CriterioIndicadorPeriodoPK other = (CriterioIndicadorPeriodoPK) object;
        if (this.criterio != other.criterio) {
            return false;
        }
        if (this.indicador != other.indicador) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioIndicadorPeriodoPK[ criterio=" + criterio + ", indicador=" + indicador + ", periodo=" + periodo + " ]";
    }
    
}
