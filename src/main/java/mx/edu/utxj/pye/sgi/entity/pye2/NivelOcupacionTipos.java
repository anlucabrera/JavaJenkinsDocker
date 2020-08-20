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
@Table(name = "nivel_ocupacion_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelOcupacionTipos.findAll", query = "SELECT n FROM NivelOcupacionTipos n")
    , @NamedQuery(name = "NivelOcupacionTipos.findByOcupacion", query = "SELECT n FROM NivelOcupacionTipos n WHERE n.ocupacion = :ocupacion")
    , @NamedQuery(name = "NivelOcupacionTipos.findByDescripcion", query = "SELECT n FROM NivelOcupacionTipos n WHERE n.descripcion = :descripcion")})
public class NivelOcupacionTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ocupacion")
    private Short ocupacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ocupacion", fetch = FetchType.LAZY)
    private List<NivelOcupacionEgresadosGeneracion> nivelOcupacionEgresadosGeneracionList;

    public NivelOcupacionTipos() {
    }

    public NivelOcupacionTipos(Short ocupacion) {
        this.ocupacion = ocupacion;
    }

    public NivelOcupacionTipos(Short ocupacion, String descripcion) {
        this.ocupacion = ocupacion;
        this.descripcion = descripcion;
    }

    public Short getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Short ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<NivelOcupacionEgresadosGeneracion> getNivelOcupacionEgresadosGeneracionList() {
        return nivelOcupacionEgresadosGeneracionList;
    }

    public void setNivelOcupacionEgresadosGeneracionList(List<NivelOcupacionEgresadosGeneracion> nivelOcupacionEgresadosGeneracionList) {
        this.nivelOcupacionEgresadosGeneracionList = nivelOcupacionEgresadosGeneracionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocupacion != null ? ocupacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelOcupacionTipos)) {
            return false;
        }
        NivelOcupacionTipos other = (NivelOcupacionTipos) object;
        if ((this.ocupacion == null && other.ocupacion != null) || (this.ocupacion != null && !this.ocupacion.equals(other.ocupacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.NivelOcupacionTipos[ ocupacion=" + ocupacion + " ]";
    }
    
}
