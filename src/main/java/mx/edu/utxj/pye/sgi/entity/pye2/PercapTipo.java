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
@Table(name = "percap_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PercapTipo.findAll", query = "SELECT p FROM PercapTipo p")
    , @NamedQuery(name = "PercapTipo.findByPercapTipo", query = "SELECT p FROM PercapTipo p WHERE p.percapTipo = :percapTipo")
    , @NamedQuery(name = "PercapTipo.findByTipo", query = "SELECT p FROM PercapTipo p WHERE p.tipo = :tipo")})
public class PercapTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "percap_tipo")
    private Short percapTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<PersonalCapacitado> personalCapacitadoList;

    public PercapTipo() {
    }

    public PercapTipo(Short percapTipo) {
        this.percapTipo = percapTipo;
    }

    public PercapTipo(Short percapTipo, String tipo) {
        this.percapTipo = percapTipo;
        this.tipo = tipo;
    }

    public Short getPercapTipo() {
        return percapTipo;
    }

    public void setPercapTipo(Short percapTipo) {
        this.percapTipo = percapTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<PersonalCapacitado> getPersonalCapacitadoList() {
        return personalCapacitadoList;
    }

    public void setPersonalCapacitadoList(List<PersonalCapacitado> personalCapacitadoList) {
        this.personalCapacitadoList = personalCapacitadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (percapTipo != null ? percapTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PercapTipo)) {
            return false;
        }
        PercapTipo other = (PercapTipo) object;
        if ((this.percapTipo == null && other.percapTipo != null) || (this.percapTipo != null && !this.percapTipo.equals(other.percapTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.PercapTipo[ percapTipo=" + percapTipo + " ]";
    }
    
}
