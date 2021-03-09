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
 * @author UTXJ
 */
@Embeddable
public class ProyeccionAreasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "proceso_inscripcion")
    private int procesoInscripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pe")
    private short pe;

    public ProyeccionAreasPK() {
    }

    public ProyeccionAreasPK(int procesoInscripcion, short pe) {
        this.procesoInscripcion = procesoInscripcion;
        this.pe = pe;
    }

    public int getProcesoInscripcion() {
        return procesoInscripcion;
    }

    public void setProcesoInscripcion(int procesoInscripcion) {
        this.procesoInscripcion = procesoInscripcion;
    }

    public short getPe() {
        return pe;
    }

    public void setPe(short pe) {
        this.pe = pe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) procesoInscripcion;
        hash += (int) pe;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyeccionAreasPK)) {
            return false;
        }
        ProyeccionAreasPK other = (ProyeccionAreasPK) object;
        if (this.procesoInscripcion != other.procesoInscripcion) {
            return false;
        }
        if (this.pe != other.pe) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ProyeccionAreasPK[ procesoInscripcion=" + procesoInscripcion + ", pe=" + pe + " ]";
    }
    
}
