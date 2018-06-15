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
@Table(name = "variable_disponibilidades", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableDisponibilidades.findAll", query = "SELECT v FROM VariableDisponibilidades v")
    , @NamedQuery(name = "VariableDisponibilidades.findByDisponibilidad", query = "SELECT v FROM VariableDisponibilidades v WHERE v.disponibilidad = :disponibilidad")
    , @NamedQuery(name = "VariableDisponibilidades.findByNombre", query = "SELECT v FROM VariableDisponibilidades v WHERE v.nombre = :nombre")})
public class VariableDisponibilidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "disponibilidad")
    private Short disponibilidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disponibilidad")
    private List<Variables> variablesList;

    public VariableDisponibilidades() {
    }

    public VariableDisponibilidades(Short disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public VariableDisponibilidades(Short disponibilidad, String nombre) {
        this.disponibilidad = disponibilidad;
        this.nombre = nombre;
    }

    public Short getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Short disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Variables> getVariablesList() {
        return variablesList;
    }

    public void setVariablesList(List<Variables> variablesList) {
        this.variablesList = variablesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (disponibilidad != null ? disponibilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableDisponibilidades)) {
            return false;
        }
        VariableDisponibilidades other = (VariableDisponibilidades) object;
        if ((this.disponibilidad == null && other.disponibilidad != null) || (this.disponibilidad != null && !this.disponibilidad.equals(other.disponibilidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariableDisponibilidades[ disponibilidad=" + disponibilidad + " ]";
    }
    
}
