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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "notificaciones_ce_imagenes", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificacionesCeImagenes.findAll", query = "SELECT n FROM NotificacionesCeImagenes n")
    , @NamedQuery(name = "NotificacionesCeImagenes.findByDetalleEvidencia", query = "SELECT n FROM NotificacionesCeImagenes n WHERE n.detalleEvidencia = :detalleEvidencia")
    , @NamedQuery(name = "NotificacionesCeImagenes.findByMime", query = "SELECT n FROM NotificacionesCeImagenes n WHERE n.mime = :mime")
    , @NamedQuery(name = "NotificacionesCeImagenes.findByTamanioBytes", query = "SELECT n FROM NotificacionesCeImagenes n WHERE n.tamanioBytes = :tamanioBytes")})
public class NotificacionesCeImagenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detalle_evidencia")
    private Integer detalleEvidencia;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "mime")
    private String mime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tamanio_bytes")
    private long tamanioBytes;
    @JoinColumn(name = "notificacion", referencedColumnName = "notificacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private NotificacionesCe notificacion;

    public NotificacionesCeImagenes() {
    }

    public NotificacionesCeImagenes(Integer detalleEvidencia) {
        this.detalleEvidencia = detalleEvidencia;
    }

    public NotificacionesCeImagenes(Integer detalleEvidencia, String ruta, String mime, long tamanioBytes) {
        this.detalleEvidencia = detalleEvidencia;
        this.ruta = ruta;
        this.mime = mime;
        this.tamanioBytes = tamanioBytes;
    }

    public Integer getDetalleEvidencia() {
        return detalleEvidencia;
    }

    public void setDetalleEvidencia(Integer detalleEvidencia) {
        this.detalleEvidencia = detalleEvidencia;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public long getTamanioBytes() {
        return tamanioBytes;
    }

    public void setTamanioBytes(long tamanioBytes) {
        this.tamanioBytes = tamanioBytes;
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
        hash += (detalleEvidencia != null ? detalleEvidencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificacionesCeImagenes)) {
            return false;
        }
        NotificacionesCeImagenes other = (NotificacionesCeImagenes) object;
        if ((this.detalleEvidencia == null && other.detalleEvidencia != null) || (this.detalleEvidencia != null && !this.detalleEvidencia.equals(other.detalleEvidencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCeImagenes[ detalleEvidencia=" + detalleEvidencia + " ]";
    }
    
}
