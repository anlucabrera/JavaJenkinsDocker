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
@Table(name = "actividades_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadesTipos.findAll", query = "SELECT a FROM ActividadesTipos a")
    , @NamedQuery(name = "ActividadesTipos.findByActividadTipo", query = "SELECT a FROM ActividadesTipos a WHERE a.actividadTipo = :actividadTipo")
    , @NamedQuery(name = "ActividadesTipos.findByNombre", query = "SELECT a FROM ActividadesTipos a WHERE a.nombre = :nombre")})
public class ActividadesTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "actividad_tipo")
    private Short actividadTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividadTipo")
    private List<ActividadesFormacionIntegral> actividadesFormacionIntegralList;

    public ActividadesTipos() {
    }

    public ActividadesTipos(Short actividadTipo) {
        this.actividadTipo = actividadTipo;
    }

    public ActividadesTipos(Short actividadTipo, String nombre) {
        this.actividadTipo = actividadTipo;
        this.nombre = nombre;
    }

    public Short getActividadTipo() {
        return actividadTipo;
    }

    public void setActividadTipo(Short actividadTipo) {
        this.actividadTipo = actividadTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ActividadesFormacionIntegral> getActividadesFormacionIntegralList() {
        return actividadesFormacionIntegralList;
    }

    public void setActividadesFormacionIntegralList(List<ActividadesFormacionIntegral> actividadesFormacionIntegralList) {
        this.actividadesFormacionIntegralList = actividadesFormacionIntegralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actividadTipo != null ? actividadTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadesTipos)) {
            return false;
        }
        ActividadesTipos other = (ActividadesTipos) object;
        if ((this.actividadTipo == null && other.actividadTipo != null) || (this.actividadTipo != null && !this.actividadTipo.equals(other.actividadTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadesTipos[ actividadTipo=" + actividadTipo + " ]";
    }
    
}
