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
 * @author UTXJ
 */
@Embeddable
public class CiudadesAsentamientosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_estado")
    private int cveEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_ciudad")
    private int cveCiudad;

    public CiudadesAsentamientosPK() {
    }

    public CiudadesAsentamientosPK(int cveEstado, int cveCiudad) {
        this.cveEstado = cveEstado;
        this.cveCiudad = cveCiudad;
    }

    public int getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(int cveEstado) {
        this.cveEstado = cveEstado;
    }

    public int getCveCiudad() {
        return cveCiudad;
    }

    public void setCveCiudad(int cveCiudad) {
        this.cveCiudad = cveCiudad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveEstado;
        hash += (int) cveCiudad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CiudadesAsentamientosPK)) {
            return false;
        }
        CiudadesAsentamientosPK other = (CiudadesAsentamientosPK) object;
        if (this.cveEstado != other.cveEstado) {
            return false;
        }
        if (this.cveCiudad != other.cveCiudad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.CiudadesAsentamientosPK[ cveEstado=" + cveEstado + ", cveCiudad=" + cveCiudad + " ]";
    }
    
}
