/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Planeacion
 */
@Embeddable
public class PeriodosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_periodo")
    private int cvePeriodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;

    public PeriodosPK() {
    }

    public PeriodosPK(int cvePeriodo, int cveUniversidad) {
        this.cvePeriodo = cvePeriodo;
        this.cveUniversidad = cveUniversidad;
    }

    public int getCvePeriodo() {
        return cvePeriodo;
    }

    public void setCvePeriodo(int cvePeriodo) {
        this.cvePeriodo = cvePeriodo;
    }

    public int getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cvePeriodo;
        hash += (int) cveUniversidad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodosPK)) {
            return false;
        }
        PeriodosPK other = (PeriodosPK) object;
        if (this.cvePeriodo != other.cvePeriodo) {
            return false;
        }
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.PeriodosPK[ cvePeriodo=" + cvePeriodo + ", cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
