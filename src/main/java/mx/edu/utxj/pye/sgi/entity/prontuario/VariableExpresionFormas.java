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
@Table(name = "variable_expresion_formas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableExpresionFormas.findAll", query = "SELECT v FROM VariableExpresionFormas v")
    , @NamedQuery(name = "VariableExpresionFormas.findByFormaExpresion", query = "SELECT v FROM VariableExpresionFormas v WHERE v.formaExpresion = :formaExpresion")
    , @NamedQuery(name = "VariableExpresionFormas.findByNombre", query = "SELECT v FROM VariableExpresionFormas v WHERE v.nombre = :nombre")})
public class VariableExpresionFormas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "forma_expresion")
    private Short formaExpresion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formaExpresion")
    private List<Variables> variablesList;

    public VariableExpresionFormas() {
    }

    public VariableExpresionFormas(Short formaExpresion) {
        this.formaExpresion = formaExpresion;
    }

    public VariableExpresionFormas(Short formaExpresion, String nombre) {
        this.formaExpresion = formaExpresion;
        this.nombre = nombre;
    }

    public Short getFormaExpresion() {
        return formaExpresion;
    }

    public void setFormaExpresion(Short formaExpresion) {
        this.formaExpresion = formaExpresion;
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
        hash += (formaExpresion != null ? formaExpresion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableExpresionFormas)) {
            return false;
        }
        VariableExpresionFormas other = (VariableExpresionFormas) object;
        if ((this.formaExpresion == null && other.formaExpresion != null) || (this.formaExpresion != null && !this.formaExpresion.equals(other.formaExpresion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariableExpresionFormas[ formaExpresion=" + formaExpresion + " ]";
    }
    
}
