/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tipolocalidad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipolocalidad.findAll", query = "SELECT t FROM Tipolocalidad t")
    , @NamedQuery(name = "Tipolocalidad.findByClave", query = "SELECT t FROM Tipolocalidad t WHERE t.clave = :clave")
    , @NamedQuery(name = "Tipolocalidad.findByTipoDescripcion", query = "SELECT t FROM Tipolocalidad t WHERE t.tipoDescripcion = :tipoDescripcion")})
public class Tipolocalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "clave")
    private Integer clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo_descripcion")
    private String tipoDescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zonaAsentamiento")
    private List<Asentamiento> asentamientoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "claveTipoLocalidad")
    private List<Localidad> localidadList;

    public Tipolocalidad() {
    }

    public Tipolocalidad(Integer clave) {
        this.clave = clave;
    }

    public Tipolocalidad(Integer clave, String tipoDescripcion) {
        this.clave = clave;
        this.tipoDescripcion = tipoDescripcion;
    }

    public Integer getClave() {
        return clave;
    }

    public void setClave(Integer clave) {
        this.clave = clave;
    }

    public String getTipoDescripcion() {
        return tipoDescripcion;
    }

    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }

    @XmlTransient
    public List<Asentamiento> getAsentamientoList() {
        return asentamientoList;
    }

    public void setAsentamientoList(List<Asentamiento> asentamientoList) {
        this.asentamientoList = asentamientoList;
    }

    @XmlTransient
    public List<Localidad> getLocalidadList() {
        return localidadList;
    }

    public void setLocalidadList(List<Localidad> localidadList) {
        this.localidadList = localidadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipolocalidad)) {
            return false;
        }
        Tipolocalidad other = (Tipolocalidad) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Tipolocalidad[ clave=" + clave + " ]";
    }
    
}
