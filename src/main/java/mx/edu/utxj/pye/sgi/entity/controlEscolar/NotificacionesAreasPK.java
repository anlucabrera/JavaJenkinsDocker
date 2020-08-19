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
 * @author Desarrollo
 */
@Embeddable
public class NotificacionesAreasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "notificacion")
    private int notificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;

    public NotificacionesAreasPK() {
    }

    public NotificacionesAreasPK(int notificacion, short area) {
        this.notificacion = notificacion;
        this.area = area;
    }

    public int getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(int notificacion) {
        this.notificacion = notificacion;
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
        hash += (int) notificacion;
        hash += (int) area;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionesAreasPK)) {
            return false;
        }
        NotificacionesAreasPK other = (NotificacionesAreasPK) object;
        if (this.notificacion != other.notificacion) {
            return false;
        }
        if (this.area != other.area) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesAreasPK[ notificacion=" + notificacion + ", area=" + area + " ]";
    }
    
}
