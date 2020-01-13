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
 * @author UTXJ
 */
@Embeddable
public class AtividadesvariasplaneacionescuatrimestralesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "actividad")
    private int actividad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "planeacion")
    private int planeacion;

    public AtividadesvariasplaneacionescuatrimestralesPK() {
    }

    public AtividadesvariasplaneacionescuatrimestralesPK(int actividad, int planeacion) {
        this.actividad = actividad;
        this.planeacion = planeacion;
    }

    public int getActividad() {
        return actividad;
    }

    public void setActividad(int actividad) {
        this.actividad = actividad;
    }

    public int getPlaneacion() {
        return planeacion;
    }

    public void setPlaneacion(int planeacion) {
        this.planeacion = planeacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) actividad;
        hash += (int) planeacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtividadesvariasplaneacionescuatrimestralesPK)) {
            return false;
        }
        AtividadesvariasplaneacionescuatrimestralesPK other = (AtividadesvariasplaneacionescuatrimestralesPK) object;
        if (this.actividad != other.actividad) {
            return false;
        }
        if (this.planeacion != other.planeacion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.AtividadesvariasplaneacionescuatrimestralesPK[ actividad=" + actividad + ", planeacion=" + planeacion + " ]";
    }
    
}
