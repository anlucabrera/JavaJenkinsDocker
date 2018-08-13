/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "zonificaciones_estado", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZonificacionesEstado.findAll", query = "SELECT z FROM ZonificacionesEstado z")
    , @NamedQuery(name = "ZonificacionesEstado.findByEstado", query = "SELECT z FROM ZonificacionesEstado z WHERE z.estado = :estado")
    , @NamedQuery(name = "ZonificacionesEstado.findByZona", query = "SELECT z FROM ZonificacionesEstado z WHERE z.zona = :zona")
    , @NamedQuery(name = "ZonificacionesEstado.findByAplicable", query = "SELECT z FROM ZonificacionesEstado z WHERE z.aplicable = :aplicable")})
public class ZonificacionesEstado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private Integer estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona")
    private short zona;
    @Size(max = 7)
    @Column(name = "aplicable")
    private String aplicable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonificacionesEstado")
    private List<ZonificacionesMunicipio> zonificacionesMunicipioList;

    public ZonificacionesEstado() {
    }

    public ZonificacionesEstado(Integer estado) {
        this.estado = estado;
    }

    public ZonificacionesEstado(Integer estado, short zona) {
        this.estado = estado;
        this.zona = zona;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public String getAplicable() {
        return aplicable;
    }

    public void setAplicable(String aplicable) {
        this.aplicable = aplicable;
    }

    @XmlTransient
    public List<ZonificacionesMunicipio> getZonificacionesMunicipioList() {
        return zonificacionesMunicipioList;
    }

    public void setZonificacionesMunicipioList(List<ZonificacionesMunicipio> zonificacionesMunicipioList) {
        this.zonificacionesMunicipioList = zonificacionesMunicipioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estado != null ? estado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZonificacionesEstado)) {
            return false;
        }
        ZonificacionesEstado other = (ZonificacionesEstado) object;
        if ((this.estado == null && other.estado != null) || (this.estado != null && !this.estado.equals(other.estado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.ZonificacionesEstado[ estado=" + estado + " ]";
    }
    
}
