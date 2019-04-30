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
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 6)
    @Column(name = "evaluador")
    private String evaluador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado")
    private int evaluado;

    public EvaluacionEstadiaResultadosPK() {
    }

    public EvaluacionEstadiaResultadosPK(int evaluacion, String evaluador, int evaluado) {
        this.evaluacion = evaluacion;
        this.evaluador = evaluador;
        this.evaluado = evaluado;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
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
        hash += (evaluador != null ? evaluador.hashCode() : 0);
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
        if ((this.evaluador == null && other.evaluador != null) || (this.evaluador != null && !this.evaluador.equals(other.evaluador))) {
            return false;
        }
        if (this.evaluado != other.evaluado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstadiaResultadosPK[ evaluacion=" + evaluacion + ", evaluador=" + evaluador + ", evaluado=" + evaluado + " ]";
    }
    
}
