/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "actividades_vinculacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadesVinculacion.findAll", query = "SELECT a FROM ActividadesVinculacion a")
    , @NamedQuery(name = "ActividadesVinculacion.findByActividadVinculacion", query = "SELECT a FROM ActividadesVinculacion a WHERE a.actividadVinculacion = :actividadVinculacion")
    , @NamedQuery(name = "ActividadesVinculacion.findByNombre", query = "SELECT a FROM ActividadesVinculacion a WHERE a.nombre = :nombre")})
public class ActividadesVinculacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "actividad_vinculacion")
    private Short actividadVinculacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nombre")
    private String nombre;
    @JoinTable(name = "pye2.actividad_vinculacion_empresa", joinColumns = {
        @JoinColumn(name = "actividad_vinculacion", referencedColumnName = "actividad_vinculacion")}, inverseJoinColumns = {
        @JoinColumn(name = "empresa", referencedColumnName = "empresa")})
    @ManyToMany
    private List<OrganismosVinculados> organismosVinculadosList;

    public ActividadesVinculacion() {
    }

    public ActividadesVinculacion(Short actividadVinculacion) {
        this.actividadVinculacion = actividadVinculacion;
    }

    public ActividadesVinculacion(Short actividadVinculacion, String nombre) {
        this.actividadVinculacion = actividadVinculacion;
        this.nombre = nombre;
    }

    public Short getActividadVinculacion() {
        return actividadVinculacion;
    }

    public void setActividadVinculacion(Short actividadVinculacion) {
        this.actividadVinculacion = actividadVinculacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (actividadVinculacion != null ? actividadVinculacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadesVinculacion)) {
            return false;
        }
        ActividadesVinculacion other = (ActividadesVinculacion) object;
        if ((this.actividadVinculacion == null && other.actividadVinculacion != null) || (this.actividadVinculacion != null && !this.actividadVinculacion.equals(other.actividadVinculacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion[ actividadVinculacion=" + actividadVinculacion + " ]";
    }
    
}
