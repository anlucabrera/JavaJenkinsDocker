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
public class MovilidadAcademicaPK implements Serializable {

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
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;

    public MovilidadAcademicaPK() {
    }

    public MovilidadAcademicaPK(int ciclo, int periodo, String siglas) {
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.siglas = siglas;
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

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) periodo;
        hash += (siglas != null ? siglas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovilidadAcademicaPK)) {
            return false;
        }
        MovilidadAcademicaPK other = (MovilidadAcademicaPK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if ((this.siglas == null && other.siglas != null) || (this.siglas != null && !this.siglas.equals(other.siglas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MovilidadAcademicaPK[ ciclo=" + ciclo + ", periodo=" + periodo + ", siglas=" + siglas + " ]";
    }
    
}
