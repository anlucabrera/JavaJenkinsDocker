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
public class ComunicacionesPK implements Serializable{
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
    @Column(name = "consecutivo_comunicacion")
    private int consecutivoComunicacion;

    public ComunicacionesPK() {
    }

    public ComunicacionesPK(Integer cvePersona, int cveUniversidad, int consecutivoComunicacion) {
        this.cvePersona = cvePersona;
        this.cveUniversidad = cveUniversidad;
        this.consecutivoComunicacion = consecutivoComunicacion;
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

    public int getConsecutivoComunicacion() {
        return consecutivoComunicacion;
    }

    public void setConsecutivoComunicacion(int consecutivoComunicacion) {
        this.consecutivoComunicacion = consecutivoComunicacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.cvePersona);
        hash = 67 * hash + this.cveUniversidad;
        hash = 67 * hash + this.consecutivoComunicacion;
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
        final ComunicacionesPK other = (ComunicacionesPK) obj;
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        if (this.consecutivoComunicacion != other.consecutivoComunicacion) {
            return false;
        }
        if (!Objects.equals(this.cvePersona, other.cvePersona)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComunicacionesPK{" + "cvePersona=" + cvePersona + ", cveUniversidad=" + cveUniversidad + ", consecutivoComunicacion=" + consecutivoComunicacion + '}';
    }
    
}
