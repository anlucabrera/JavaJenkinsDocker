/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class DomiciliosPK implements Serializable{
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_persona")
    private Integer cvePersona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "consecutivo_domicilio")
    private int consecutivoDomicilio;

    public DomiciliosPK() {
    }

    public DomiciliosPK(Integer cvePersona, int cveUniversidad, int consecutivoDomicilio) {
        this.cvePersona = cvePersona;
        this.cveUniversidad = cveUniversidad;
        this.consecutivoDomicilio = consecutivoDomicilio;
    }

    public Integer getCvePersona() {
        return cvePersona;
    }

    public void setCvePersona(Integer cvePersona) {
        this.cvePersona = cvePersona;
    }

    public int getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public int getConsecutivoDomicilio() {
        return consecutivoDomicilio;
    }

    public void setConsecutivoDomicilio(int consecutivoDomicilio) {
        this.consecutivoDomicilio = consecutivoDomicilio;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.cvePersona);
        hash = 37 * hash + this.cveUniversidad;
        hash = 37 * hash + this.consecutivoDomicilio;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomiciliosPK other = (DomiciliosPK) obj;
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        if (this.consecutivoDomicilio != other.consecutivoDomicilio) {
            return false;
        }
        if (!Objects.equals(this.cvePersona, other.cvePersona)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DomiciliosPK{" + "cvePersona=" + cvePersona + ", cveUniversidad=" + cveUniversidad + ", consecutivoDomicilio=" + consecutivoDomicilio + '}';
    }
    
    
}
