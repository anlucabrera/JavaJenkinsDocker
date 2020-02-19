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
 * @author Desarrollo
 */
@Embeddable
public class CuestionarioPsicopedagogicoResultadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;

    public CuestionarioPsicopedagogicoResultadosPK() {
    }

    public CuestionarioPsicopedagogicoResultadosPK(int evaluacion, int idEstudiante) {
        this.evaluacion = evaluacion;
        this.idEstudiante = idEstudiante;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
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
        hash += (int) evaluacion;
        hash += (int) idEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuestionarioPsicopedagogicoResultadosPK)) {
            return false;
        }
        CuestionarioPsicopedagogicoResultadosPK other = (CuestionarioPsicopedagogicoResultadosPK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultadosPK[ evaluacion=" + evaluacion + ", idEstudiante=" + idEstudiante + " ]";
    }
    
}
