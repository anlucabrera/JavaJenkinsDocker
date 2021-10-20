/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class NoAdeudoEstudiantePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 57)
    @Column(name = "area")
    private String area;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nivel")
    private String nivel;

    public NoAdeudoEstudiantePK() {
    }

    public NoAdeudoEstudiantePK(int idEstudiante, String area, String nivel) {
        this.idEstudiante = idEstudiante;
        this.area = area;
        this.nivel = nivel;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEstudiante;
        hash += (area != null ? area.hashCode() : 0);
        hash += (nivel != null ? nivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NoAdeudoEstudiantePK)) {
            return false;
        }
        NoAdeudoEstudiantePK other = (NoAdeudoEstudiantePK) object;
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        if ((this.area == null && other.area != null) || (this.area != null && !this.area.equals(other.area))) {
            return false;
        }
        if ((this.nivel == null && other.nivel != null) || (this.nivel != null && !this.nivel.equals(other.nivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NoAdeudoEstudiantePK[ idEstudiante=" + idEstudiante + ", area=" + area + ", nivel=" + nivel + " ]";
    }
    
}
