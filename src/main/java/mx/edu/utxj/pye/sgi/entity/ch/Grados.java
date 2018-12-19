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
 * @author UTXJ
 */
@Entity
@Table(name = "grados", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grados.findAll", query = "SELECT g FROM Grados g")
    , @NamedQuery(name = "Grados.findByGrado", query = "SELECT g FROM Grados g WHERE g.grado = :grado")
    , @NamedQuery(name = "Grados.findByNombre", query = "SELECT g FROM Grados g WHERE g.nombre = :nombre")})
public class Grados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "grado")
    private Short grado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivelEscolaridad")
    private List<FormacionAcademica> formacionAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grado")
    private List<Personal> personalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grado")
    private List<PersonalBitacora> personalBitacoraList;

    public Grados() {
    }

    public Grados(Short grado) {
        this.grado = grado;
    }

    public Grados(Short grado, String nombre) {
        this.grado = grado;
        this.nombre = nombre;
    }

    public Short getGrado() {
        return grado;
    }

    public void setGrado(Short grado) {
        this.grado = grado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<FormacionAcademica> getFormacionAcademicaList() {
        return formacionAcademicaList;
    }

    public void setFormacionAcademicaList(List<FormacionAcademica> formacionAcademicaList) {
        this.formacionAcademicaList = formacionAcademicaList;
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
        hash += (grado != null ? grado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grados)) {
            return false;
        }
        Grados other = (Grados) object;
        if ((this.grado == null && other.grado != null) || (this.grado != null && !this.grado.equals(other.grado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Grados[ grado=" + grado + " ]";
    }
    
}
