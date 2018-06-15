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
@Table(name = "beca_tipos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BecaTipos.findAll", query = "SELECT b FROM BecaTipos b")
    , @NamedQuery(name = "BecaTipos.findByBecaTipo", query = "SELECT b FROM BecaTipos b WHERE b.becaTipo = :becaTipo")
    , @NamedQuery(name = "BecaTipos.findByNombre", query = "SELECT b FROM BecaTipos b WHERE b.nombre = :nombre")})
public class BecaTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "beca_tipo")
    private Short becaTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "becaTipo")
    private List<Becas> becasList;

    public BecaTipos() {
    }

    public BecaTipos(Short becaTipo) {
        this.becaTipo = becaTipo;
    }

    public BecaTipos(Short becaTipo, String nombre) {
        this.becaTipo = becaTipo;
        this.nombre = nombre;
    }

    public Short getBecaTipo() {
        return becaTipo;
    }

    public void setBecaTipo(Short becaTipo) {
        this.becaTipo = becaTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Becas> getBecasList() {
        return becasList;
    }

    public void setBecasList(List<Becas> becasList) {
        this.becasList = becasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (becaTipo != null ? becaTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BecaTipos)) {
            return false;
        }
        BecaTipos other = (BecaTipos) object;
        if ((this.becaTipo == null && other.becaTipo != null) || (this.becaTipo != null && !this.becaTipo.equals(other.becaTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BecaTipos[ becaTipo=" + becaTipo + " ]";
    }
    
}
