/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "marco_clasificacion", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarcoClasificacion.findAll", query = "SELECT m FROM MarcoClasificacion m")
    , @NamedQuery(name = "MarcoClasificacion.findById", query = "SELECT m FROM MarcoClasificacion m WHERE m.id = :id")
    , @NamedQuery(name = "MarcoClasificacion.findByNombre", query = "SELECT m FROM MarcoClasificacion m WHERE m.nombre = :nombre")})
public class MarcoClasificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clasificacion", fetch = FetchType.LAZY)
    private List<MarcoNormativo> marcoNormativoList;

    public MarcoClasificacion() {
    }

    public MarcoClasificacion(Integer id) {
        this.id = id;
    }

    public MarcoClasificacion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<MarcoNormativo> getMarcoNormativoList() {
        return marcoNormativoList;
    }

    public void setMarcoNormativoList(List<MarcoNormativo> marcoNormativoList) {
        this.marcoNormativoList = marcoNormativoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MarcoClasificacion)) {
            return false;
        }
        MarcoClasificacion other = (MarcoClasificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.MarcoClasificacion[ id=" + id + " ]";
    }
    
}
