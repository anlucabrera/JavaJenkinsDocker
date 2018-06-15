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
public class PersonasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_persona")
    private int cvePersona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;

    public PersonasPK() {
    }

    public PersonasPK(int cvePersona, int cveUniversidad) {
        this.cvePersona = cvePersona;
        this.cveUniversidad = cveUniversidad;
    }

    public int getCvePersona() {
        return cvePersona;
    }

    public void setCvePersona(int cvePersona) {
        this.cvePersona = cvePersona;
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
        hash += (int) cvePersona;
        hash += (int) cveUniversidad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonasPK)) {
            return false;
        }
        PersonasPK other = (PersonasPK) object;
        if (this.cvePersona != other.cvePersona) {
            return false;
        }
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.ejb.saiiut.entity.PersonasPK[ cvePersona=" + cvePersona + ", cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
