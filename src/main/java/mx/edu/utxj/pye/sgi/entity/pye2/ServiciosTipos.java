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
@Table(name = "servicios_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosTipos.findAll", query = "SELECT s FROM ServiciosTipos s")
    , @NamedQuery(name = "ServiciosTipos.findByServtipo", query = "SELECT s FROM ServiciosTipos s WHERE s.servtipo = :servtipo")
    , @NamedQuery(name = "ServiciosTipos.findByDescripcion", query = "SELECT s FROM ServiciosTipos s WHERE s.descripcion = :descripcion")})
public class ServiciosTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "servtipo")
    private Short servtipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioTipo")
    private List<ServiciosTecnologicosAnioMes> serviciosTecnologicosAnioMesList;

    public ServiciosTipos() {
    }

    public ServiciosTipos(Short servtipo) {
        this.servtipo = servtipo;
    }

    public ServiciosTipos(Short servtipo, String descripcion) {
        this.servtipo = servtipo;
        this.descripcion = descripcion;
    }

    public Short getServtipo() {
        return servtipo;
    }

    public void setServtipo(Short servtipo) {
        this.servtipo = servtipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ServiciosTecnologicosAnioMes> getServiciosTecnologicosAnioMesList() {
        return serviciosTecnologicosAnioMesList;
    }

    public void setServiciosTecnologicosAnioMesList(List<ServiciosTecnologicosAnioMes> serviciosTecnologicosAnioMesList) {
        this.serviciosTecnologicosAnioMesList = serviciosTecnologicosAnioMesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servtipo != null ? servtipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosTipos)) {
            return false;
        }
        ServiciosTipos other = (ServiciosTipos) object;
        if ((this.servtipo == null && other.servtipo != null) || (this.servtipo != null && !this.servtipo.equals(other.servtipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ServiciosTipos[ servtipo=" + servtipo + " ]";
    }
    
}
