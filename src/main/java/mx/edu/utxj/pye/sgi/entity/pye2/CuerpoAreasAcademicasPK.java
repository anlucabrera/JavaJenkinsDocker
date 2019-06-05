/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
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
public class CuerpoAreasAcademicasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area_academica")
    private short areaAcademica;

    public CuerpoAreasAcademicasPK() {
    }

    public CuerpoAreasAcademicasPK(String cuerpoAcademico, short areaAcademica) {
        this.cuerpoAcademico = cuerpoAcademico;
        this.areaAcademica = areaAcademica;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public short getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(short areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuerpoAcademico != null ? cuerpoAcademico.hashCode() : 0);
        hash += (int) areaAcademica;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpoAreasAcademicasPK)) {
            return false;
        }
        CuerpoAreasAcademicasPK other = (CuerpoAreasAcademicasPK) object;
        if ((this.cuerpoAcademico == null && other.cuerpoAcademico != null) || (this.cuerpoAcademico != null && !this.cuerpoAcademico.equals(other.cuerpoAcademico))) {
            return false;
        }
        if (this.areaAcademica != other.areaAcademica) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicasPK[ cuerpoAcademico=" + cuerpoAcademico + ", areaAcademica=" + areaAcademica + " ]";
    }

}
