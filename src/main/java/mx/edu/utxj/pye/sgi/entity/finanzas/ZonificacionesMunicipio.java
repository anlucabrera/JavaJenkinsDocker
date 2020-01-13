/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "zonificaciones_municipio", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZonificacionesMunicipio.findAll", query = "SELECT z FROM ZonificacionesMunicipio z")
    , @NamedQuery(name = "ZonificacionesMunicipio.findByEstado", query = "SELECT z FROM ZonificacionesMunicipio z WHERE z.zonificacionesMunicipioPK.estado = :estado")
    , @NamedQuery(name = "ZonificacionesMunicipio.findByMunicipio", query = "SELECT z FROM ZonificacionesMunicipio z WHERE z.zonificacionesMunicipioPK.municipio = :municipio")
    , @NamedQuery(name = "ZonificacionesMunicipio.findByZona", query = "SELECT z FROM ZonificacionesMunicipio z WHERE z.zona = :zona")})
public class ZonificacionesMunicipio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ZonificacionesMunicipioPK zonificacionesMunicipioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona")
    private short zona;
    @JoinColumn(name = "estado", referencedColumnName = "estado", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ZonificacionesEstado zonificacionesEstado;

    public ZonificacionesMunicipio() {
    }

    public ZonificacionesMunicipio(ZonificacionesMunicipioPK zonificacionesMunicipioPK) {
        this.zonificacionesMunicipioPK = zonificacionesMunicipioPK;
    }

    public ZonificacionesMunicipio(ZonificacionesMunicipioPK zonificacionesMunicipioPK, short zona) {
        this.zonificacionesMunicipioPK = zonificacionesMunicipioPK;
        this.zona = zona;
    }

    public ZonificacionesMunicipio(int estado, int municipio) {
        this.zonificacionesMunicipioPK = new ZonificacionesMunicipioPK(estado, municipio);
    }

    public ZonificacionesMunicipioPK getZonificacionesMunicipioPK() {
        return zonificacionesMunicipioPK;
    }

    public void setZonificacionesMunicipioPK(ZonificacionesMunicipioPK zonificacionesMunicipioPK) {
        this.zonificacionesMunicipioPK = zonificacionesMunicipioPK;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public ZonificacionesEstado getZonificacionesEstado() {
        return zonificacionesEstado;
    }

    public void setZonificacionesEstado(ZonificacionesEstado zonificacionesEstado) {
        this.zonificacionesEstado = zonificacionesEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (zonificacionesMunicipioPK != null ? zonificacionesMunicipioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZonificacionesMunicipio)) {
            return false;
        }
        ZonificacionesMunicipio other = (ZonificacionesMunicipio) object;
        if ((this.zonificacionesMunicipioPK == null && other.zonificacionesMunicipioPK != null) || (this.zonificacionesMunicipioPK != null && !this.zonificacionesMunicipioPK.equals(other.zonificacionesMunicipioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.ZonificacionesMunicipio[ zonificacionesMunicipioPK=" + zonificacionesMunicipioPK + " ]";
    }
    
}
