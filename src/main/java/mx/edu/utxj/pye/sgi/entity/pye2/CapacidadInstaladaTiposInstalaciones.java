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
@Table(name = "capacidad_instalada_tipos_instalaciones", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapacidadInstaladaTiposInstalaciones.findAll", query = "SELECT c FROM CapacidadInstaladaTiposInstalaciones c")
    , @NamedQuery(name = "CapacidadInstaladaTiposInstalaciones.findByInstalacion", query = "SELECT c FROM CapacidadInstaladaTiposInstalaciones c WHERE c.instalacion = :instalacion")
    , @NamedQuery(name = "CapacidadInstaladaTiposInstalaciones.findByDescripcion", query = "SELECT c FROM CapacidadInstaladaTiposInstalaciones c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "CapacidadInstaladaTiposInstalaciones.findByCapacidad", query = "SELECT c FROM CapacidadInstaladaTiposInstalaciones c WHERE c.capacidad = :capacidad")})
public class CapacidadInstaladaTiposInstalaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "instalacion")
    private Short instalacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "capacidad")
    private int capacidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instalacion")
    private List<CapacidadInstaladaCiclosEscolares> capacidadInstaladaCiclosEscolaresList;

    public CapacidadInstaladaTiposInstalaciones() {
    }

    public CapacidadInstaladaTiposInstalaciones(Short instalacion) {
        this.instalacion = instalacion;
    }

    public CapacidadInstaladaTiposInstalaciones(Short instalacion, String descripcion, int capacidad) {
        this.instalacion = instalacion;
        this.descripcion = descripcion;
        this.capacidad = capacidad;
    }

    public Short getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Short instalacion) {
        this.instalacion = instalacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @XmlTransient
    public List<CapacidadInstaladaCiclosEscolares> getCapacidadInstaladaCiclosEscolaresList() {
        return capacidadInstaladaCiclosEscolaresList;
    }

    public void setCapacidadInstaladaCiclosEscolaresList(List<CapacidadInstaladaCiclosEscolares> capacidadInstaladaCiclosEscolaresList) {
        this.capacidadInstaladaCiclosEscolaresList = capacidadInstaladaCiclosEscolaresList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (instalacion != null ? instalacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CapacidadInstaladaTiposInstalaciones)) {
            return false;
        }
        CapacidadInstaladaTiposInstalaciones other = (CapacidadInstaladaTiposInstalaciones) object;
        if ((this.instalacion == null && other.instalacion != null) || (this.instalacion != null && !this.instalacion.equals(other.instalacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaTiposInstalaciones[ instalacion=" + instalacion + " ]";
    }
    
}
