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
@Table(name = "percap_modalidad", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PercapModalidad.findAll", query = "SELECT p FROM PercapModalidad p")
    , @NamedQuery(name = "PercapModalidad.findByPercapMod", query = "SELECT p FROM PercapModalidad p WHERE p.percapMod = :percapMod")
    , @NamedQuery(name = "PercapModalidad.findByModalidad", query = "SELECT p FROM PercapModalidad p WHERE p.modalidad = :modalidad")})
public class PercapModalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "percap_mod")
    private Short percapMod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "modalidad")
    private String modalidad;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidad")
    private List<PersonalCapacitado> personalCapacitadoList;

    public PercapModalidad() {
    }

    public PercapModalidad(Short percapMod) {
        this.percapMod = percapMod;
    }

    public PercapModalidad(Short percapMod, String modalidad) {
        this.percapMod = percapMod;
        this.modalidad = modalidad;
    }

    public Short getPercapMod() {
        return percapMod;
    }

    public void setPercapMod(Short percapMod) {
        this.percapMod = percapMod;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
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
        hash += (percapMod != null ? percapMod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PercapModalidad)) {
            return false;
        }
        PercapModalidad other = (PercapModalidad) object;
        if ((this.percapMod == null && other.percapMod != null) || (this.percapMod != null && !this.percapMod.equals(other.percapMod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.PercapModalidad[ percapMod=" + percapMod + " ]";
    }
    
}
