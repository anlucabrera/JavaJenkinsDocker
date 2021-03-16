/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "notificaciones_enlaces", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionesEnlaces.findAll", query = "SELECT n FROM NotificacionesEnlaces n")
    , @NamedQuery(name = "NotificacionesEnlaces.findByNotificacionEnlace", query = "SELECT n FROM NotificacionesEnlaces n WHERE n.notificacionEnlace = :notificacionEnlace")
    , @NamedQuery(name = "NotificacionesEnlaces.findByTituloEnlace", query = "SELECT n FROM NotificacionesEnlaces n WHERE n.tituloEnlace = :tituloEnlace")
    , @NamedQuery(name = "NotificacionesEnlaces.findByEnlace", query = "SELECT n FROM NotificacionesEnlaces n WHERE n.enlace = :enlace")})
public class NotificacionesEnlaces implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notificacion_enlace")
    private Integer notificacionEnlace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "titulo_enlace")
    private String tituloEnlace;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "enlace")
    private String enlace;
    @JoinColumn(name = "notificacion", referencedColumnName = "notificacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NotificacionesCe notificacion;

    public NotificacionesEnlaces() {
    }

    public NotificacionesEnlaces(Integer notificacionEnlace) {
        this.notificacionEnlace = notificacionEnlace;
    }

    public NotificacionesEnlaces(Integer notificacionEnlace, String tituloEnlace, String enlace) {
        this.notificacionEnlace = notificacionEnlace;
        this.tituloEnlace = tituloEnlace;
        this.enlace = enlace;
    }

    public Integer getNotificacionEnlace() {
        return notificacionEnlace;
    }

    public void setNotificacionEnlace(Integer notificacionEnlace) {
        this.notificacionEnlace = notificacionEnlace;
    }

    public String getTituloEnlace() {
        return tituloEnlace;
    }

    public void setTituloEnlace(String tituloEnlace) {
        this.tituloEnlace = tituloEnlace;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public NotificacionesCe getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(NotificacionesCe notificacion) {
        this.notificacion = notificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacionEnlace != null ? notificacionEnlace.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionesEnlaces)) {
            return false;
        }
        NotificacionesEnlaces other = (NotificacionesEnlaces) object;
        if ((this.notificacionEnlace == null && other.notificacionEnlace != null) || (this.notificacionEnlace != null && !this.notificacionEnlace.equals(other.notificacionEnlace))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesEnlaces[ notificacionEnlace=" + notificacionEnlace + " ]";
    }
    
}
