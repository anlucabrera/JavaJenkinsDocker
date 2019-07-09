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

/**
 *
 * @author Planeacion
 */
@Embeddable
public class EvaluacionTutoresResultadosPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @Column(name = "evaluador")
    private int evaluador;
    @Basic(optional = false)
    @Column(name = "evaluado")
    private int evaluado;

    public EvaluacionTutoresResultadosPK() {
    }

    public EvaluacionTutoresResultadosPK(int evaluacion, int evaluador, int evaluado) {
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

    public int getEvaluador() {
        return evaluador;
    }

    public void setEvaluador(int evaluador) {
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
        hash += (int) evaluador;
        hash += (int) evaluado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionTutoresResultadosPK)) {
            return false;
        }
        EvaluacionTutoresResultadosPK other = (EvaluacionTutoresResultadosPK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.evaluador != other.evaluador) {
            return false;
        }
        if (this.evaluado != other.evaluado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultadosPK[ evaluacion=" + evaluacion + ", evaluador=" + evaluador + ", evaluado=" + evaluado + " ]";
    }
    
}
