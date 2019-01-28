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
@Table(name = "sectores_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SectoresTipo.findAll", query = "SELECT s FROM SectoresTipo s")
    , @NamedQuery(name = "SectoresTipo.findBySector", query = "SELECT s FROM SectoresTipo s WHERE s.sector = :sector")
    , @NamedQuery(name = "SectoresTipo.findByDescripcion", query = "SELECT s FROM SectoresTipo s WHERE s.descripcion = :descripcion")
    , @NamedQuery(name = "SectoresTipo.findByCorresponde", query = "SELECT s FROM SectoresTipo s WHERE s.corresponde = :corresponde")})
public class SectoresTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sector")
    private Short sector;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "corresponde")
    private String corresponde;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sector")
    private List<ActividadEconomicaEgresadoGeneracion> actividadEconomicaEgresadoGeneracionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sector")
    private List<OrganismosVinculados> organismosVinculadosList;

    public SectoresTipo() {
    }

    public SectoresTipo(Short sector) {
        this.sector = sector;
    }

    public SectoresTipo(Short sector, String descripcion, String corresponde) {
        this.sector = sector;
        this.descripcion = descripcion;
        this.corresponde = corresponde;
    }

    public Short getSector() {
        return sector;
    }

    public void setSector(Short sector) {
        this.sector = sector;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCorresponde() {
        return corresponde;
    }

    public void setCorresponde(String corresponde) {
        this.corresponde = corresponde;
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
        hash += (sector != null ? sector.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SectoresTipo)) {
            return false;
        }
        SectoresTipo other = (SectoresTipo) object;
        if ((this.sector == null && other.sector != null) || (this.sector != null && !this.sector.equals(other.sector))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.SectoresTipo[ sector=" + sector + " ]";
    }
    
}
