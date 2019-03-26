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
public class CalificacionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "estudiante")
    private int estudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grupo")
    private int grupo;

    public CalificacionesPK() {
    }

    public CalificacionesPK(int estudiante, int grupo) {
        this.estudiante = estudiante;
        this.grupo = grupo;
    }

    public int getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(int estudiante) {
        this.estudiante = estudiante;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) estudiante;
        hash += (int) grupo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionesPK)) {
            return false;
        }
        CalificacionesPK other = (CalificacionesPK) object;
        if (this.estudiante != other.estudiante) {
            return false;
        }
        if (this.grupo != other.grupo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionesPK[ estudiante=" + estudiante + ", grupo=" + grupo + " ]";
    }
    
}
