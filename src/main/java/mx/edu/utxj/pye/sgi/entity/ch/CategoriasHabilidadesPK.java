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
 * @author Desarrollo
 */
@Embeddable
public class CategoriasHabilidadesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private short categoria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "habilidad")
    private int habilidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;

    public CategoriasHabilidadesPK() {
    }

    public CategoriasHabilidadesPK(short categoria, int habilidad, int evaluacion) {
        this.categoria = categoria;
        this.habilidad = habilidad;
        this.evaluacion = evaluacion;
    }

    public short getCategoria() {
        return categoria;
    }

    public void setCategoria(short categoria) {
        this.categoria = categoria;
    }

    public int getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(int habilidad) {
        this.habilidad = habilidad;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) categoria;
        hash += (int) habilidad;
        hash += (int) evaluacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CategoriasHabilidadesPK)) {
            return false;
        }
        CategoriasHabilidadesPK other = (CategoriasHabilidadesPK) object;
        if (this.categoria != other.categoria) {
            return false;
        }
        if (this.habilidad != other.habilidad) {
            return false;
        }
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CategoriasHabilidadesPK[ categoria=" + categoria + ", habilidad=" + habilidad + ", evaluacion=" + evaluacion + " ]";
    }
    
}
