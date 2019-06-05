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
import javax.validation.constraints.Size;
/**
 *
 * @author UTXJ
 */
@Embeddable
public class AspirantesPK implements Serializable{
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_aspirante")
    private Integer cveAspirante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "folio_ceneval")
    private String folioCeneval;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_proceso")
    private int cveProceso;

    public AspirantesPK() {
    }

    public AspirantesPK(Integer cveAspirante, String folioCeneval, int cveUniversidad, int cveProceso) {
        this.cveAspirante = cveAspirante;
        this.folioCeneval = folioCeneval;
        this.cveUniversidad = cveUniversidad;
        this.cveProceso = cveProceso;
    }

    public Integer getCveAspirante() {
        return cveAspirante;
    }

    public void setCveAspirante(Integer cveAspirante) {
        this.cveAspirante = cveAspirante;
    }

    public String getFolioCeneval() {
        return folioCeneval;
    }

    public void setFolioCeneval(String folioCeneval) {
        this.folioCeneval = folioCeneval;
    }

    public int getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public int getCveProceso() {
        return cveProceso;
    }

    public void setCveProceso(int cveProceso) {
        this.cveProceso = cveProceso;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.cveAspirante);
        hash = 73 * hash + Objects.hashCode(this.folioCeneval);
        hash = 73 * hash + this.cveUniversidad;
        hash = 73 * hash + this.cveProceso;
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
        final AspirantesPK other = (AspirantesPK) obj;
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        if (this.cveProceso != other.cveProceso) {
            return false;
        }
        if (!Objects.equals(this.folioCeneval, other.folioCeneval)) {
            return false;
        }
        if (!Objects.equals(this.cveAspirante, other.cveAspirante)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AspirantesPK{" + "cveAspirante=" + cveAspirante + ", folioCeneval=" + folioCeneval + ", cveUniversidad=" + cveUniversidad + ", cveProceso=" + cveProceso + '}';
    }
}
