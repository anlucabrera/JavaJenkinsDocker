/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.poa;

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
@Table(name = "unidad_medidas", catalog = "poa", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMedidas.findAll", query = "SELECT u FROM UnidadMedidas u")
    , @NamedQuery(name = "UnidadMedidas.findByUnidadMedida", query = "SELECT u FROM UnidadMedidas u WHERE u.unidadMedida = :unidadMedida")
    , @NamedQuery(name = "UnidadMedidas.findByNombre", query = "SELECT u FROM UnidadMedidas u WHERE u.nombre = :nombre")})
public class UnidadMedidas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "unidad_medida")
    private Short unidadMedida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 90)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMedida")
    private List<ActividadesPoa> actividadesPoaList;

    public UnidadMedidas() {
    }

    public UnidadMedidas(Short unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public UnidadMedidas(Short unidadMedida, String nombre) {
        this.unidadMedida = unidadMedida;
        this.nombre = nombre;
    }

    public Short getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(Short unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ActividadesPoa> getActividadesPoaList() {
        return actividadesPoaList;
    }

    public void setActividadesPoaList(List<ActividadesPoa> actividadesPoaList) {
        this.actividadesPoaList = actividadesPoaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unidadMedida != null ? unidadMedida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMedidas)) {
            return false;
        }
        UnidadMedidas other = (UnidadMedidas) object;
        if ((this.unidadMedida == null && other.unidadMedida != null) || (this.unidadMedida != null && !this.unidadMedida.equals(other.unidadMedida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.poa.entyti.UnidadMedidas[ unidadMedida=" + unidadMedida + " ]";
    }
    
}
