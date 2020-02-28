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
public class AlumnosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_alumno")
    private int cveAlumno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;

    public AlumnosPK() {
    }

    public AlumnosPK(int cveAlumno, int cveUniversidad) {
        this.cveAlumno = cveAlumno;
        this.cveUniversidad = cveUniversidad;
    }

    public int getCveAlumno() {
        return cveAlumno;
    }

    public void setCveAlumno(int cveAlumno) {
        this.cveAlumno = cveAlumno;
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
        hash += (int) cveAlumno;
        hash += (int) cveUniversidad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlumnosPK)) {
            return false;
        }
        AlumnosPK other = (AlumnosPK) object;
        if (this.cveAlumno != other.cveAlumno) {
            return false;
        }
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosPK[ cveAlumno=" + cveAlumno + ", cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
