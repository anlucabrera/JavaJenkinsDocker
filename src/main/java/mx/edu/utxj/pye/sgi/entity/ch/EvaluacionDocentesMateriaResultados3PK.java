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
 * @author Desarrollo
 */
@Embeddable
public class EvaluacionDocentesMateriaResultados3PK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluador")
    private int evaluador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cve_materia")
    private String cveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluado")
    private int evaluado;

    public EvaluacionDocentesMateriaResultados3PK() {
    }

    public EvaluacionDocentesMateriaResultados3PK(int evaluacion, int evaluador, String cveMateria, int evaluado) {
        this.evaluacion = evaluacion;
        this.evaluador = evaluador;
        this.cveMateria = cveMateria;
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

    public String getCveMateria() {
        return cveMateria;
    }

    public void setCveMateria(String cveMateria) {
        this.cveMateria = cveMateria;
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
        hash += (cveMateria != null ? cveMateria.hashCode() : 0);
        hash += (int) evaluado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDocentesMateriaResultados3PK)) {
            return false;
        }
        EvaluacionDocentesMateriaResultados3PK other = (EvaluacionDocentesMateriaResultados3PK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.evaluador != other.evaluador) {
            return false;
        }
        if ((this.cveMateria == null && other.cveMateria != null) || (this.cveMateria != null && !this.cveMateria.equals(other.cveMateria))) {
            return false;
        }
        if (this.evaluado != other.evaluado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados3PK[ evaluacion=" + evaluacion + ", evaluador=" + evaluador + ", cveMateria=" + cveMateria + ", evaluado=" + evaluado + " ]";
    }
    
}
