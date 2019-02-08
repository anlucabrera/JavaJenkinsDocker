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
@Table(name = "cursos_modalidad", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursosModalidad.findAll", query = "SELECT c FROM CursosModalidad c")
    , @NamedQuery(name = "CursosModalidad.findByModalidad", query = "SELECT c FROM CursosModalidad c WHERE c.modalidad = :modalidad")
    , @NamedQuery(name = "CursosModalidad.findByNombre", query = "SELECT c FROM CursosModalidad c WHERE c.nombre = :nombre")})
public class CursosModalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "modalidad")
    private Short modalidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidad")
    private List<Cursos> cursosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidad")
    private List<Capacitacionespersonal> capacitacionespersonalList;

    public CursosModalidad() {
    }

    public CursosModalidad(Short modalidad) {
        this.modalidad = modalidad;
    }

    public CursosModalidad(Short modalidad, String nombre) {
        this.modalidad = modalidad;
        this.nombre = nombre;
    }

    public Short getModalidad() {
        return modalidad;
    }

    public void setModalidad(Short modalidad) {
        this.modalidad = modalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Cursos> getCursosList() {
        return cursosList;
    }

    public void setCursosList(List<Cursos> cursosList) {
        this.cursosList = cursosList;
    }

    @XmlTransient
    public List<Capacitacionespersonal> getCapacitacionespersonalList() {
        return capacitacionespersonalList;
    }

    public void setCapacitacionespersonalList(List<Capacitacionespersonal> capacitacionespersonalList) {
        this.capacitacionespersonalList = capacitacionespersonalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modalidad != null ? modalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursosModalidad)) {
            return false;
        }
        CursosModalidad other = (CursosModalidad) object;
        if ((this.modalidad == null && other.modalidad != null) || (this.modalidad != null && !this.modalidad.equals(other.modalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad[ modalidad=" + modalidad + " ]";
    }
    
}
