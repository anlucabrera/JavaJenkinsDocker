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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "localidad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidad.findAll", query = "SELECT l FROM Localidad l")
    , @NamedQuery(name = "Localidad.findByClaveEstado", query = "SELECT l FROM Localidad l WHERE l.localidadPK.claveEstado = :claveEstado")
    , @NamedQuery(name = "Localidad.findByClaveMunicipio", query = "SELECT l FROM Localidad l WHERE l.localidadPK.claveMunicipio = :claveMunicipio")
    , @NamedQuery(name = "Localidad.findByClaveLocalidad", query = "SELECT l FROM Localidad l WHERE l.localidadPK.claveLocalidad = :claveLocalidad")
    , @NamedQuery(name = "Localidad.findByNombre", query = "SELECT l FROM Localidad l WHERE l.nombre = :nombre")})
public class Localidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LocalidadPK localidadPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumns({
        @JoinColumn(name = "claveEstado", referencedColumnName = "claveEstado", insertable = false, updatable = false)
        , @JoinColumn(name = "claveMunicipio", referencedColumnName = "claveMunicipio", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Municipio municipio;
    @JoinColumn(name = "claveTipoLocalidad", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Tipolocalidad claveTipoLocalidad;
    @OneToMany(mappedBy = "localidad")
    private List<OrganismosVinculados> organismosVinculadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localidad")
    private List<FeriasProfesiograficas> feriasProfesiograficasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localidad")
    private List<Iems> iemsList;

    public Localidad() {
    }

    public Localidad(LocalidadPK localidadPK) {
        this.localidadPK = localidadPK;
    }

    public Localidad(LocalidadPK localidadPK, String nombre) {
        this.localidadPK = localidadPK;
        this.nombre = nombre;
    }

    public Localidad(int claveEstado, int claveMunicipio, int claveLocalidad) {
        this.localidadPK = new LocalidadPK(claveEstado, claveMunicipio, claveLocalidad);
    }

    public LocalidadPK getLocalidadPK() {
        return localidadPK;
    }

    public void setLocalidadPK(LocalidadPK localidadPK) {
        this.localidadPK = localidadPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Tipolocalidad getClaveTipoLocalidad() {
        return claveTipoLocalidad;
    }

    public void setClaveTipoLocalidad(Tipolocalidad claveTipoLocalidad) {
        this.claveTipoLocalidad = claveTipoLocalidad;
    }

    @XmlTransient
    public List<OrganismosVinculados> getOrganismosVinculadosList() {
        return organismosVinculadosList;
    }

    public void setOrganismosVinculadosList(List<OrganismosVinculados> organismosVinculadosList) {
        this.organismosVinculadosList = organismosVinculadosList;
    }

    @XmlTransient
    public List<FeriasProfesiograficas> getFeriasProfesiograficasList() {
        return feriasProfesiograficasList;
    }

    public void setFeriasProfesiograficasList(List<FeriasProfesiograficas> feriasProfesiograficasList) {
        this.feriasProfesiograficasList = feriasProfesiograficasList;
    }

    @XmlTransient
    public List<Iems> getIemsList() {
        return iemsList;
    }

    public void setIemsList(List<Iems> iemsList) {
        this.iemsList = iemsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (localidadPK != null ? localidadPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Localidad)) {
            return false;
        }
        Localidad other = (Localidad) object;
        if ((this.localidadPK == null && other.localidadPK != null) || (this.localidadPK != null && !this.localidadPK.equals(other.localidadPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Localidad[ localidadPK=" + localidadPK + " ]";
    }
    
}
