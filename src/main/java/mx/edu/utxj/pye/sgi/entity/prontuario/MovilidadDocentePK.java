/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
public class MovilidadDocentePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "area_academica")
    private String areaAcademica;

    public MovilidadDocentePK() {
    }

    public MovilidadDocentePK(int ciclo, int periodo, String areaAcademica) {
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.areaAcademica = areaAcademica;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public String getAreaAcademica() {
        return areaAcademica;
    }

    public void setAreaAcademica(String areaAcademica) {
        this.areaAcademica = areaAcademica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) periodo;
        hash += (areaAcademica != null ? areaAcademica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovilidadDocentePK)) {
            return false;
        }
        MovilidadDocentePK other = (MovilidadDocentePK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if ((this.areaAcademica == null && other.areaAcademica != null) || (this.areaAcademica != null && !this.areaAcademica.equals(other.areaAcademica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MovilidadDocentePK[ ciclo=" + ciclo + ", periodo=" + periodo + ", areaAcademica=" + areaAcademica + " ]";
    }
    
}
