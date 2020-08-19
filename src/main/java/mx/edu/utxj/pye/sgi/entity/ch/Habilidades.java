/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "habilidades", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Habilidades.findAll", query = "SELECT h FROM Habilidades h")
    , @NamedQuery(name = "Habilidades.findByHabilidad", query = "SELECT h FROM Habilidades h WHERE h.habilidad = :habilidad")
    , @NamedQuery(name = "Habilidades.findByNombre", query = "SELECT h FROM Habilidades h WHERE h.nombre = :nombre")})
public class Habilidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "habilidad")
    private Integer habilidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @ManyToMany(mappedBy = "capital_humano.habilidadesList", fetch = FetchType.LAZY)
    private List<PersonalCategorias> personalCategoriasList;

    public Habilidades() {
    }

    public Habilidades(Integer habilidad) {
        this.habilidad = habilidad;
    }

    public Habilidades(Integer habilidad, String nombre) {
        this.habilidad = habilidad;
        this.nombre = nombre;
    }

    public Integer getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Integer habilidad) {
        this.habilidad = habilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<PersonalCategorias> getPersonalCategoriasList() {
        return personalCategoriasList;
    }

    public void setPersonalCategoriasList(List<PersonalCategorias> personalCategoriasList) {
        this.personalCategoriasList = personalCategoriasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (habilidad != null ? habilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Habilidades)) {
            return false;
        }
        Habilidades other = (Habilidades) object;
        if ((this.habilidad == null && other.habilidad != null) || (this.habilidad != null && !this.habilidad.equals(other.habilidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Habilidades[ habilidad=" + habilidad + " ]";
    }
    
}
