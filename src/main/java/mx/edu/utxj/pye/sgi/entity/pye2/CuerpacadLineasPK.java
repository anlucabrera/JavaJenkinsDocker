/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class CuerpacadLineasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "cuerpo_academico")
    private String cuerpoAcademico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;

    public CuerpacadLineasPK() {
    }

    public CuerpacadLineasPK(String cuerpoAcademico, String nombre) {
        this.cuerpoAcademico = cuerpoAcademico;
        this.nombre = nombre;
    }

    public String getCuerpoAcademico() {
        return cuerpoAcademico;
    }

    public void setCuerpoAcademico(String cuerpoAcademico) {
        this.cuerpoAcademico = cuerpoAcademico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuerpoAcademico != null ? cuerpoAcademico.hashCode() : 0);
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuerpacadLineasPK)) {
            return false;
        }
        CuerpacadLineasPK other = (CuerpacadLineasPK) object;
        if ((this.cuerpoAcademico == null && other.cuerpoAcademico != null) || (this.cuerpoAcademico != null && !this.cuerpoAcademico.equals(other.cuerpoAcademico))) {
            return false;
        }
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineasPK[ cuerpoAcademico=" + cuerpoAcademico + ", nombre=" + nombre + " ]";
    }
    
}
