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
@Table(name = "cursos_tipo", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CursosTipo.findAll", query = "SELECT c FROM CursosTipo c")
    , @NamedQuery(name = "CursosTipo.findByTipo", query = "SELECT c FROM CursosTipo c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "CursosTipo.findByNombre", query = "SELECT c FROM CursosTipo c WHERE c.nombre = :nombre")})
public class CursosTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo")
    private Short tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", fetch = FetchType.LAZY)
    private List<Cursos> cursosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", fetch = FetchType.LAZY)
    private List<Capacitacionespersonal> capacitacionespersonalList;

    public CursosTipo() {
    }

    public CursosTipo(Short tipo) {
        this.tipo = tipo;
    }

    public CursosTipo(Short tipo, String nombre) {
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
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
        hash += (tipo != null ? tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CursosTipo)) {
            return false;
        }
        CursosTipo other = (CursosTipo) object;
        if ((this.tipo == null && other.tipo != null) || (this.tipo != null && !this.tipo.equals(other.tipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CursosTipo[ tipo=" + tipo + " ]";
    }
    
}
