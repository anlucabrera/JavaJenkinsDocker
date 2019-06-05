/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
public class CargaAcademicaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_grupo")
    private int cveGrupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_materia")
    private int cveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;

    public CargaAcademicaPK() {
    }

    public CargaAcademicaPK(int cveGrupo, int cveMateria, int docente) {
        this.cveGrupo = cveGrupo;
        this.cveMateria = cveMateria;
        this.docente = docente;
    }

    public int getCveGrupo() {
        return cveGrupo;
    }

    public void setCveGrupo(int cveGrupo) {
        this.cveGrupo = cveGrupo;
    }

    public int getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(int cveMateria) {
        this.cveMateria = cveMateria;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveGrupo;
        hash += (int) cveMateria;
        hash += (int) docente;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaAcademicaPK)) {
            return false;
        }
        CargaAcademicaPK other = (CargaAcademicaPK) object;
        if (this.cveGrupo != other.cveGrupo) {
            return false;
        }
        if (this.cveMateria != other.cveMateria) {
            return false;
        }
        if (this.docente != other.docente) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademicaPK[ cveGrupo=" + cveGrupo + ", cveMateria=" + cveMateria + ", docente=" + docente + " ]";
    }
    
}
