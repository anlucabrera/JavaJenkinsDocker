/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jonny
 */
@Embeddable
public class EncuestaServiciosResultadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador")
    private int evaluador;

    public EncuestaServiciosResultadosPK() {
    }

    public EncuestaServiciosResultadosPK(int evaluacion, int evaluador) {
        this.evaluacion = evaluacion;
        this.evaluador = evaluador;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(int evaluador) {
        this.evaluador = evaluador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) evaluacion;
        hash += (int) evaluador;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaServiciosResultadosPK)) {
            return false;
        }
        EncuestaServiciosResultadosPK other = (EncuestaServiciosResultadosPK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.evaluador != other.evaluador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultadosPK[ evaluacion=" + evaluacion + ", evaluador=" + evaluador + " ]";
    }
    
}
