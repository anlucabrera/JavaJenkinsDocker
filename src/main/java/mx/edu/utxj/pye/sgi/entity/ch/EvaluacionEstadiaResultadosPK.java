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
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Planeacion
 */
@Embeddable
public class EvaluacionEstadiaResultadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado")
    private int evaluado;
    
    public EvaluacionEstadiaResultadosPK() {
    }

    public EvaluacionEstadiaResultadosPK(int evaluacion,int evaluado) {
        this.evaluacion = evaluacion;
        this.evaluado = evaluado;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getEvaluado() {
        return evaluado;
    }

    public void setEvaluado(int evaluado) {
        this.evaluado = evaluado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) evaluacion;
        hash += (int) evaluado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionEstadiaResultadosPK)) {
            return false;
        }
        EvaluacionEstadiaResultadosPK other = (EvaluacionEstadiaResultadosPK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.evaluado != other.evaluado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultadosPK[ evaluacion=" + evaluacion + ", evaluado=" + evaluado + " ]";
    }
    
}
