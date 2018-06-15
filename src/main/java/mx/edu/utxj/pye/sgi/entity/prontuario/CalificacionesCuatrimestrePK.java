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
public class CalificacionesCuatrimestrePK implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "materia")
    private String materia;

    public CalificacionesCuatrimestrePK() {
    }

    public CalificacionesCuatrimestrePK(int ciclo, int periodo, String siglas, String materia) {
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.siglas = siglas;
        this.materia = materia;
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

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) periodo;
        hash += (siglas != null ? siglas.hashCode() : 0);
        hash += (materia != null ? materia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionesCuatrimestrePK)) {
            return false;
        }
        CalificacionesCuatrimestrePK other = (CalificacionesCuatrimestrePK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if ((this.siglas == null && other.siglas != null) || (this.siglas != null && !this.siglas.equals(other.siglas))) {
            return false;
        }
        if ((this.materia == null && other.materia != null) || (this.materia != null && !this.materia.equals(other.materia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.CalificacionesCuatrimestrePK[ ciclo=" + ciclo + ", periodo=" + periodo + ", siglas=" + siglas + ", materia=" + materia + " ]";
    }
    
}
