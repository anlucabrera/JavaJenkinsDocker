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
public class UnidadMateriaComentarioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private int configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;

    public UnidadMateriaComentarioPK() {
    }

    public UnidadMateriaComentarioPK(int configuracion, int idEstudiante) {
        this.configuracion = configuracion;
        this.idEstudiante = idEstudiante;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) configuracion;
        hash += (int) idEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaComentarioPK)) {
            return false;
        }
        UnidadMateriaComentarioPK other = (UnidadMateriaComentarioPK) object;
        if (this.configuracion != other.configuracion) {
            return false;
        }
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaComentarioPK[ configuracion=" + configuracion + ", idEstudiante=" + idEstudiante + " ]";
    }
    
}
