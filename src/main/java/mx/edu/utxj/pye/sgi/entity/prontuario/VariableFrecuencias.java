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
@Table(name = "variable_frecuencias", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableFrecuencias.findAll", query = "SELECT v FROM VariableFrecuencias v")
    , @NamedQuery(name = "VariableFrecuencias.findByFrecuencia", query = "SELECT v FROM VariableFrecuencias v WHERE v.frecuencia = :frecuencia")
    , @NamedQuery(name = "VariableFrecuencias.findByNombre", query = "SELECT v FROM VariableFrecuencias v WHERE v.nombre = :nombre")})
public class VariableFrecuencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "frecuencia")
    private Short frecuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "frecuencia")
    private List<Variables> variablesList;

    public VariableFrecuencias() {
    }

    public VariableFrecuencias(Short frecuencia) {
        this.frecuencia = frecuencia;
    }

    public VariableFrecuencias(Short frecuencia, String nombre) {
        this.frecuencia = frecuencia;
        this.nombre = nombre;
    }

    public Short getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Short frecuencia) {
        this.frecuencia = frecuencia;
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
        hash += (frecuencia != null ? frecuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableFrecuencias)) {
            return false;
        }
        VariableFrecuencias other = (VariableFrecuencias) object;
        if ((this.frecuencia == null && other.frecuencia != null) || (this.frecuencia != null && !this.frecuencia.equals(other.frecuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariableFrecuencias[ frecuencia=" + frecuencia + " ]";
    }
    
}
