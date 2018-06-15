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
 * @author Planeación
 */
@Embeddable
public class PuestosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_puesto")
    private int cvePuesto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;

    public PuestosPK() {
    }

    public PuestosPK(int cvePuesto, int cveUniversidad) {
        this.cvePuesto = cvePuesto;
        this.cveUniversidad = cveUniversidad;
    }

    public int getCvePuesto() {
        return cvePuesto;
    }

    public void setCvePuesto(int cvePuesto) {
        this.cvePuesto = cvePuesto;
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
        hash += (int) cvePuesto;
        hash += (int) cveUniversidad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuestosPK)) {
            return false;
        }
        PuestosPK other = (PuestosPK) object;
        if (this.cvePuesto != other.cvePuesto) {
            return false;
        }
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.PuestosPK[ cvePuesto=" + cvePuesto + ", cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
