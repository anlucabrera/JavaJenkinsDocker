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
public class CalificacionNivelacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "carga")
    private int carga;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;

    public CalificacionNivelacionPK() {
    }

    public CalificacionNivelacionPK(int carga, int idEstudiante) {
        this.carga = carga;
        this.idEstudiante = idEstudiante;
    }

    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
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
        hash += (int) carga;
        hash += (int) idEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionNivelacionPK)) {
            return false;
        }
        CalificacionNivelacionPK other = (CalificacionNivelacionPK) object;
        if (this.carga != other.carga) {
            return false;
        }
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacionPK[ carga=" + carga + ", idEstudiante=" + idEstudiante + " ]";
    }
    
}
