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
import javax.persistence.FetchType;
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
@Table(name = "giros_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GirosTipo.findAll", query = "SELECT g FROM GirosTipo g")
    , @NamedQuery(name = "GirosTipo.findByGiro", query = "SELECT g FROM GirosTipo g WHERE g.giro = :giro")
    , @NamedQuery(name = "GirosTipo.findByDescripcion", query = "SELECT g FROM GirosTipo g WHERE g.descripcion = :descripcion")})
public class GirosTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "giro")
    private Short giro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "giro", fetch = FetchType.LAZY)
    private List<ActividadEconomicaEgresadoGeneracion> actividadEconomicaEgresadoGeneracionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "giro", fetch = FetchType.LAZY)
    private List<OrganismosVinculados> organismosVinculadosList;

    public GirosTipo() {
    }

    public GirosTipo(Short giro) {
        this.giro = giro;
    }

    public GirosTipo(Short giro, String descripcion) {
        this.giro = giro;
        this.descripcion = descripcion;
    }

    public Short getGiro() {
        return giro;
    }

    public void setGiro(Short giro) {
        this.giro = giro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ActividadEconomicaEgresadoGeneracion> getActividadEconomicaEgresadoGeneracionList() {
        return actividadEconomicaEgresadoGeneracionList;
    }

    public void setActividadEconomicaEgresadoGeneracionList(List<ActividadEconomicaEgresadoGeneracion> actividadEconomicaEgresadoGeneracionList) {
        this.actividadEconomicaEgresadoGeneracionList = actividadEconomicaEgresadoGeneracionList;
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
        hash += (giro != null ? giro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GirosTipo)) {
            return false;
        }
        GirosTipo other = (GirosTipo) object;
        if ((this.giro == null && other.giro != null) || (this.giro != null && !this.giro.equals(other.giro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.GirosTipo[ giro=" + giro + " ]";
    }
    
}
