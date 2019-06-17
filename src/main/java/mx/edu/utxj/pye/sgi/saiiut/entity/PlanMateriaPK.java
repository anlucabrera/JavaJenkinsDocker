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
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class PlanMateriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_plan")
    private int cvePlan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_carrera")
    private int cveCarrera;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_division")
    private int cveDivision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_unidad_academica")
    private int cveUnidadAcademica;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_turno")
    private int cveTurno;

    public PlanMateriaPK() {
    }

    public PlanMateriaPK(int cvePlan, int cveCarrera, int cveDivision, int cveUnidadAcademica, int cveUniversidad, String cveMateria, int cveTurno) {
        this.cvePlan = cvePlan;
        this.cveCarrera = cveCarrera;
        this.cveDivision = cveDivision;
        this.cveUnidadAcademica = cveUnidadAcademica;
        this.cveUniversidad = cveUniversidad;
        this.cveMateria = cveMateria;
        this.cveTurno = cveTurno;
    }

    public int getCvePlan() {
        return cvePlan;
    }

    public void setCvePlan(int cvePlan) {
        this.cvePlan = cvePlan;
    }

    public int getCveCarrera() {
        return cveCarrera;
    }

    public void setCveCarrera(int cveCarrera) {
        this.cveCarrera = cveCarrera;
    }

    public int getCveDivision() {
        return cveDivision;
    }

    public void setCveDivision(int cveDivision) {
        this.cveDivision = cveDivision;
    }

    public int getCveUnidadAcademica() {
        return cveUnidadAcademica;
    }

    public void setCveUnidadAcademica(int cveUnidadAcademica) {
        this.cveUnidadAcademica = cveUnidadAcademica;
    }

    public int getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
    }

    public int getCveTurno() {
        return cveTurno;
    }

    public void setCveTurno(int cveTurno) {
        this.cveTurno = cveTurno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cvePlan;
        hash += (int) cveCarrera;
        hash += (int) cveDivision;
        hash += (int) cveUnidadAcademica;
        hash += (int) cveUniversidad;
        hash += (cveMateria != null ? cveMateria.hashCode() : 0);
        hash += (int) cveTurno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanMateriaPK)) {
            return false;
        }
        PlanMateriaPK other = (PlanMateriaPK) object;
        if (this.cvePlan != other.cvePlan) {
            return false;
        }
        if (this.cveCarrera != other.cveCarrera) {
            return false;
        }
        if (this.cveDivision != other.cveDivision) {
            return false;
        }
        if (this.cveUnidadAcademica != other.cveUnidadAcademica) {
            return false;
        }
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        if ((this.cveMateria == null && other.cveMateria != null) || (this.cveMateria != null && !this.cveMateria.equals(other.cveMateria))) {
            return false;
        }
        if (this.cveTurno != other.cveTurno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.PlanMateriaPK[ cvePlan=" + cvePlan + ", cveCarrera=" + cveCarrera + ", cveDivision=" + cveDivision + ", cveUnidadAcademica=" + cveUnidadAcademica + ", cveUniversidad=" + cveUniversidad + ", cveMateria=" + cveMateria + ", cveTurno=" + cveTurno + " ]";
    }
    
}
