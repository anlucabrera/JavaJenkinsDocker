/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "notificaciones", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notificaciones.findAll", query = "SELECT n FROM Notificaciones n")
    , @NamedQuery(name = "Notificaciones.findByNotificacion", query = "SELECT n FROM Notificaciones n WHERE n.notificacion = :notificacion")
    , @NamedQuery(name = "Notificaciones.findByMensaje", query = "SELECT n FROM Notificaciones n WHERE n.mensaje = :mensaje")
    , @NamedQuery(name = "Notificaciones.findByFecha", query = "SELECT n FROM Notificaciones n WHERE n.fecha = :fecha")
    , @NamedQuery(name = "Notificaciones.findByStatus", query = "SELECT n FROM Notificaciones n WHERE n.status = :status")
    , @NamedQuery(name = "Notificaciones.findByTipo", query = "SELECT n FROM Notificaciones n WHERE n.tipo = :tipo")})
public class Notificaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notificacion")
    private Integer notificacion;
    @Size(max = 1000)
    @Column(name = "mensaje")
    private String mensaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    private int tipo;
    @JoinColumn(name = "claveTRemitente", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal claveTRemitente;
    @JoinColumn(name = "claveTDestino", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal claveTDestino;

    public Notificaciones() {
    }

    public Notificaciones(Integer notificacion) {
        this.notificacion = notificacion;
    }

    public Notificaciones(Integer notificacion, Date fecha, int status, int tipo) {
        this.notificacion = notificacion;
        this.fecha = fecha;
        this.status = status;
        this.tipo = tipo;
    }

    public Integer getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Integer notificacion) {
        this.notificacion = notificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Personal getClaveTRemitente() {
        return claveTRemitente;
    }

    public void setClaveTRemitente(Personal claveTRemitente) {
        this.claveTRemitente = claveTRemitente;
    }

    public Personal getClaveTDestino() {
        return claveTDestino;
    }

    public void setClaveTDestino(Personal claveTDestino) {
        this.claveTDestino = claveTDestino;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificacion != null ? notificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notificaciones)) {
            return false;
        }
        Notificaciones other = (Notificaciones) object;
        if ((this.notificacion == null && other.notificacion != null) || (this.notificacion != null && !this.notificacion.equals(other.notificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Notificaciones[ notificacion=" + notificacion + " ]";
    }
    
}
