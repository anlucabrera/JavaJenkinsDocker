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
@Table(name = "registros_tipo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistrosTipo.findAll", query = "SELECT r FROM RegistrosTipo r")
    , @NamedQuery(name = "RegistrosTipo.findByRegistroTipo", query = "SELECT r FROM RegistrosTipo r WHERE r.registroTipo = :registroTipo")
    , @NamedQuery(name = "RegistrosTipo.findByRegistroSuperior", query = "SELECT r FROM RegistrosTipo r WHERE r.registroSuperior = :registroSuperior")
    , @NamedQuery(name = "RegistrosTipo.findByNombre", query = "SELECT r FROM RegistrosTipo r WHERE r.nombre = :nombre")})
public class RegistrosTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "registro_tipo")
    private Short registroTipo;
    @Column(name = "registro_superior")
    private Short registroSuperior;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", fetch = FetchType.LAZY)
    private List<Registros> registrosList;

    public RegistrosTipo() {
    }

    public RegistrosTipo(Short registroTipo) {
        this.registroTipo = registroTipo;
    }

    public RegistrosTipo(Short registroTipo, String nombre) {
        this.registroTipo = registroTipo;
        this.nombre = nombre;
    }

    public Short getRegistroTipo() {
        return registroTipo;
    }

    public void setRegistroTipo(Short registroTipo) {
        this.registroTipo = registroTipo;
    }

    public Short getRegistroSuperior() {
        return registroSuperior;
    }

    public void setRegistroSuperior(Short registroSuperior) {
        this.registroSuperior = registroSuperior;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Registros> getRegistrosList() {
        return registrosList;
    }

    public void setRegistrosList(List<Registros> registrosList) {
        this.registrosList = registrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroTipo != null ? registroTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistrosTipo)) {
            return false;
        }
        RegistrosTipo other = (RegistrosTipo) object;
        if ((this.registroTipo == null && other.registroTipo != null) || (this.registroTipo != null && !this.registroTipo.equals(other.registroTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo[ registroTipo=" + registroTipo + " ]";
    }
    
}
