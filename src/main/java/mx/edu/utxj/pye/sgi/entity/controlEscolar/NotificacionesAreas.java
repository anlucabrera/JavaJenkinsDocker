/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "notificaciones_areas", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionesAreas.findAll", query = "SELECT n FROM NotificacionesAreas n")
    , @NamedQuery(name = "NotificacionesAreas.findByNotificacion", query = "SELECT n FROM NotificacionesAreas n WHERE n.notificacionesAreasPK.notificacion = :notificacion")
    , @NamedQuery(name = "NotificacionesAreas.findByArea", query = "SELECT n FROM NotificacionesAreas n WHERE n.notificacionesAreasPK.area = :area")})
public class NotificacionesAreas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NotificacionesAreasPK notificacionesAreasPK;
    @JoinColumn(name = "notificacion", referencedColumnName = "notificacion", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NotificacionesCe notificacionesCe;

    public NotificacionesAreas() {
    }

    public NotificacionesAreas(NotificacionesAreasPK notificacionesAreasPK) {
        this.notificacionesAreasPK = notificacionesAreasPK;
    }

    public NotificacionesAreas(int notificacion, short area) {
        this.notificacionesAreasPK = new NotificacionesAreasPK(notificacion, area);
    }

    public NotificacionesAreasPK getNotificacionesAreasPK() {
        return notificacionesAreasPK;
    }

    public void setNotificacionesAreasPK(NotificacionesAreasPK notificacionesAreasPK) {
        this.notificacionesAreasPK = notificacionesAreasPK;
    }

    public NotificacionesCe getNotificacionesCe() {
        return notificacionesCe;
    }

    public void setNotificacionesCe(NotificacionesCe notificacionesCe) {
        this.notificacionesCe = notificacionesCe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacionesAreasPK != null ? notificacionesAreasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionesAreas)) {
            return false;
        }
        NotificacionesAreas other = (NotificacionesAreas) object;
        if ((this.notificacionesAreasPK == null && other.notificacionesAreasPK != null) || (this.notificacionesAreasPK != null && !this.notificacionesAreasPK.equals(other.notificacionesAreasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesAreas[ notificacionesAreasPK=" + notificacionesAreasPK + " ]";
    }
    
}
