/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
 * @author jonny
 */
@Entity
@Table(name = "generos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generos.findAll", query = "SELECT g FROM Generos g")
    , @NamedQuery(name = "Generos.findByGenero", query = "SELECT g FROM Generos g WHERE g.genero = :genero")
    , @NamedQuery(name = "Generos.findByAbreviacion", query = "SELECT g FROM Generos g WHERE g.abreviacion = :abreviacion")
    , @NamedQuery(name = "Generos.findByNombre", query = "SELECT g FROM Generos g WHERE g.nombre = :nombre")})
public class Generos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "genero")
    private Short genero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "abreviacion")
    private Character abreviacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genero")
    private List<Personal> personalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genero")
    private List<PersonalBitacora> personalBitacoraList;

    public Generos() {
    }

    public Generos(Short genero) {
        this.genero = genero;
    }

    public Generos(Short genero, Character abreviacion, String nombre) {
        this.genero = genero;
        this.abreviacion = abreviacion;
        this.nombre = nombre;
    }

    public Short getGenero() {
        return genero;
    }

    public void setGenero(Short genero) {
        this.genero = genero;
    }

    public Character getAbreviacion() {
        return abreviacion;
    }

    public void setAbreviacion(Character abreviacion) {
        this.abreviacion = abreviacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Personal> getPersonalList() {
        return personalList;
    }

    public void setPersonalList(List<Personal> personalList) {
        this.personalList = personalList;
    }

    @XmlTransient
    public List<PersonalBitacora> getPersonalBitacoraList() {
        return personalBitacoraList;
    }

    public void setPersonalBitacoraList(List<PersonalBitacora> personalBitacoraList) {
        this.personalBitacoraList = personalBitacoraList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (genero != null ? genero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Generos)) {
            return false;
        }
        Generos other = (Generos) object;
        if ((this.genero == null && other.genero != null) || (this.genero != null && !this.genero.equals(other.genero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Generos[ genero=" + genero + " ]";
    }
    
}
