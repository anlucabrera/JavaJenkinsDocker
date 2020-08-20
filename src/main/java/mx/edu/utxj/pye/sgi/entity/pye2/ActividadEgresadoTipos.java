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
@Table(name = "actividad_egresado_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ActividadEgresadoTipos.findAll", query = "SELECT a FROM ActividadEgresadoTipos a")
    , @NamedQuery(name = "ActividadEgresadoTipos.findByActividad", query = "SELECT a FROM ActividadEgresadoTipos a WHERE a.actividad = :actividad")
    , @NamedQuery(name = "ActividadEgresadoTipos.findByDescripcion", query = "SELECT a FROM ActividadEgresadoTipos a WHERE a.descripcion = :descripcion")})
public class ActividadEgresadoTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "actividad")
    private Short actividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "actividad", fetch = FetchType.LAZY)
    private List<ActividadEgresadoGeneracion> actividadEgresadoGeneracionList;

    public ActividadEgresadoTipos() {
    }

    public ActividadEgresadoTipos(Short actividad) {
        this.actividad = actividad;
    }

    public ActividadEgresadoTipos(Short actividad, String descripcion) {
        this.actividad = actividad;
        this.descripcion = descripcion;
    }

    public Short getActividad() {
        return actividad;
    }

    public void setActividad(Short actividad) {
        this.actividad = actividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ActividadEgresadoGeneracion> getActividadEgresadoGeneracionList() {
        return actividadEgresadoGeneracionList;
    }

    public void setActividadEgresadoGeneracionList(List<ActividadEgresadoGeneracion> actividadEgresadoGeneracionList) {
        this.actividadEgresadoGeneracionList = actividadEgresadoGeneracionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (actividad != null ? actividad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadEgresadoTipos)) {
            return false;
        }
        ActividadEgresadoTipos other = (ActividadEgresadoTipos) object;
        if ((this.actividad == null && other.actividad != null) || (this.actividad != null && !this.actividad.equals(other.actividad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ActividadEgresadoTipos[ actividad=" + actividad + " ]";
    }
    
}
