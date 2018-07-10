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
@Table(name = "servicios_enfermeria_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosEnfermeriaTipo.findAll", query = "SELECT s FROM ServiciosEnfermeriaTipo s")
    , @NamedQuery(name = "ServiciosEnfermeriaTipo.findByServicio", query = "SELECT s FROM ServiciosEnfermeriaTipo s WHERE s.servicio = :servicio")
    , @NamedQuery(name = "ServiciosEnfermeriaTipo.findByDescripcion", query = "SELECT s FROM ServiciosEnfermeriaTipo s WHERE s.descripcion = :descripcion")})
public class ServiciosEnfermeriaTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "servicio")
    private Short servicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicio")
    private List<ServiciosEnfermeriaCicloPeriodos> serviciosEnfermeriaCicloPeriodosList;

    public ServiciosEnfermeriaTipo() {
    }

    public ServiciosEnfermeriaTipo(Short servicio) {
        this.servicio = servicio;
    }

    public ServiciosEnfermeriaTipo(Short servicio, String descripcion) {
        this.servicio = servicio;
        this.descripcion = descripcion;
    }

    public Short getServicio() {
        return servicio;
    }

    public void setServicio(Short servicio) {
        this.servicio = servicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ServiciosEnfermeriaCicloPeriodos> getServiciosEnfermeriaCicloPeriodosList() {
        return serviciosEnfermeriaCicloPeriodosList;
    }

    public void setServiciosEnfermeriaCicloPeriodosList(List<ServiciosEnfermeriaCicloPeriodos> serviciosEnfermeriaCicloPeriodosList) {
        this.serviciosEnfermeriaCicloPeriodosList = serviciosEnfermeriaCicloPeriodosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (servicio != null ? servicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosEnfermeriaTipo)) {
            return false;
        }
        ServiciosEnfermeriaTipo other = (ServiciosEnfermeriaTipo) object;
        if ((this.servicio == null && other.servicio != null) || (this.servicio != null && !this.servicio.equals(other.servicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ServiciosEnfermeriaTipo[ servicio=" + servicio + " ]";
    }
    
}
