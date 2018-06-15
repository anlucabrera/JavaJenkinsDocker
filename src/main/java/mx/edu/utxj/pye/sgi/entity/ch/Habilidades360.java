/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "habilidades_360", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Habilidades360.findAll", query = "SELECT h FROM Habilidades360 h")
    , @NamedQuery(name = "Habilidades360.findById", query = "SELECT h FROM Habilidades360 h WHERE h.id = :id")
    , @NamedQuery(name = "Habilidades360.findByAreaOperativa", query = "SELECT h FROM Habilidades360 h WHERE h.areaOperativa = :areaOperativa")
    , @NamedQuery(name = "Habilidades360.findByCategoriaOperativa", query = "SELECT h FROM Habilidades360 h WHERE h.categoriaOperativa = :categoriaOperativa")
    , @NamedQuery(name = "Habilidades360.findByHabilidad", query = "SELECT h FROM Habilidades360 h WHERE h.habilidad = :habilidad")})
public class Habilidades360 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "areaOperativa")
    private Integer areaOperativa;
    @Column(name = "categoriaOperativa")
    private Short categoriaOperativa;
    @Column(name = "habilidad")
    private Short habilidad;

    public Habilidades360() {
    }

    public Habilidades360(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaOperativa() {
        return areaOperativa;
    }

    public void setAreaOperativa(Integer areaOperativa) {
        this.areaOperativa = areaOperativa;
    }

    public Short getCategoriaOperativa() {
        return categoriaOperativa;
    }

    public void setCategoriaOperativa(Short categoriaOperativa) {
        this.categoriaOperativa = categoriaOperativa;
    }

    public Short getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Short habilidad) {
        this.habilidad = habilidad;
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
        if (!(object instanceof Habilidades360)) {
            return false;
        }
        Habilidades360 other = (Habilidades360) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Habilidades360[ id=" + id + " ]";
    }
    
}
