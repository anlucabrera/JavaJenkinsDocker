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
public class MunicipiosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_estado")
    private int cveEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_municipio")
    private int cveMunicipio;

    public MunicipiosPK() {
    }

    public MunicipiosPK(int cveEstado, int cveMunicipio) {
        this.cveEstado = cveEstado;
        this.cveMunicipio = cveMunicipio;
    }

    public int getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(int cveEstado) {
        this.cveEstado = cveEstado;
    }

    public int getCveMunicipio() {
        return cveMunicipio;
    }

    public void setCveMunicipio(int cveMunicipio) {
        this.cveMunicipio = cveMunicipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveEstado;
        hash += (int) cveMunicipio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MunicipiosPK)) {
            return false;
        }
        MunicipiosPK other = (MunicipiosPK) object;
        if (this.cveEstado != other.cveEstado) {
            return false;
        }
        if (this.cveMunicipio != other.cveMunicipio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.MunicipiosPK[ cveEstado=" + cveEstado + ", cveMunicipio=" + cveMunicipio + " ]";
    }
    
}
