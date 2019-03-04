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

/**
 *
 * @author UTXJ
 */
@Embeddable
public class ProyectoAreaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "proyecto")
    private int proyecto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;

    public ProyectoAreaPK() {
    }

    public ProyectoAreaPK(int proyecto, short area) {
        this.proyecto = proyecto;
        this.area = area;
    }

    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) proyecto;
        hash += (int) area;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyectoAreaPK)) {
            return false;
        }
        ProyectoAreaPK other = (ProyectoAreaPK) object;
        if (this.proyecto != other.proyecto) {
            return false;
        }
        if (this.area != other.area) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ProyectoAreaPK[ proyecto=" + proyecto + ", area=" + area + " ]";
    }
    
}
