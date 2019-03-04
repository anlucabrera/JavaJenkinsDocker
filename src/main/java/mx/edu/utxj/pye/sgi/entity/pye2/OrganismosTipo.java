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
@Table(name = "organismos_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrganismosTipo.findAll", query = "SELECT o FROM OrganismosTipo o")
    , @NamedQuery(name = "OrganismosTipo.findByOrgtipo", query = "SELECT o FROM OrganismosTipo o WHERE o.orgtipo = :orgtipo")
    , @NamedQuery(name = "OrganismosTipo.findByDescripcion", query = "SELECT o FROM OrganismosTipo o WHERE o.descripcion = :descripcion")})
public class OrganismosTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "orgtipo")
    private Short orgtipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orgTip")
    private List<OrganismosVinculados> organismosVinculadosList;

    public OrganismosTipo() {
    }

    public OrganismosTipo(Short orgtipo) {
        this.orgtipo = orgtipo;
    }

    public OrganismosTipo(Short orgtipo, String descripcion) {
        this.orgtipo = orgtipo;
        this.descripcion = descripcion;
    }

    public Short getOrgtipo() {
        return orgtipo;
    }

    public void setOrgtipo(Short orgtipo) {
        this.orgtipo = orgtipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<OrganismosVinculados> getOrganismosVinculadosList() {
        return organismosVinculadosList;
    }

    public void setOrganismosVinculadosList(List<OrganismosVinculados> organismosVinculadosList) {
        this.organismosVinculadosList = organismosVinculadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgtipo != null ? orgtipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrganismosTipo)) {
            return false;
        }
        OrganismosTipo other = (OrganismosTipo) object;
        if ((this.orgtipo == null && other.orgtipo != null) || (this.orgtipo != null && !this.orgtipo.equals(other.orgtipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.OrganismosTipo[ orgtipo=" + orgtipo + " ]";
    }
    
}
