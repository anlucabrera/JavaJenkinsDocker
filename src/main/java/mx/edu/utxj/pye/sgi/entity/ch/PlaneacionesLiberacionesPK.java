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
 * @author HOME
 */
@Embeddable
public class PlaneacionesLiberacionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "director")
    private int director;

    public PlaneacionesLiberacionesPK() {
    }

    public PlaneacionesLiberacionesPK(int periodo, int director) {
        this.periodo = periodo;
        this.director = director;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getDirector() {
        return director;
    }

    public void setDirector(int director) {
        this.director = director;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) periodo;
        hash += (int) director;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaneacionesLiberacionesPK)) {
            return false;
        }
        PlaneacionesLiberacionesPK other = (PlaneacionesLiberacionesPK) object;
        if (this.periodo != other.periodo) {
            return false;
        }
        if (this.director != other.director) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.PlaneacionesLiberacionesPK[ periodo=" + periodo + ", director=" + director + " ]";
    }
    
}
