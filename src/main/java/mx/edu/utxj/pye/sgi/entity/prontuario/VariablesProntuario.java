/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "variables_prontuario", schema = "prontuario", catalog = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariablesProntuario.findAll", query = "SELECT v FROM VariablesProntuario v")
    , @NamedQuery(name = "VariablesProntuario.findByVariable", query = "SELECT v FROM VariablesProntuario v WHERE v.variable = :variable")
    , @NamedQuery(name = "VariablesProntuario.findByNombre", query = "SELECT v FROM VariablesProntuario v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "VariablesProntuario.findByDescripcion", query = "SELECT v FROM VariablesProntuario v WHERE v.descripcion = :descripcion")
    , @NamedQuery(name = "VariablesProntuario.findByValor", query = "SELECT v FROM VariablesProntuario v WHERE v.valor = :valor")})
public class VariablesProntuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "variable")
    private Integer variable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "valor")
    private String valor;

    public VariablesProntuario() {
    }

    public VariablesProntuario(Integer variable) {
        this.variable = variable;
    }

    public VariablesProntuario(Integer variable, String nombre, String descripcion, String valor) {
        this.variable = variable;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.valor = valor;
    }

    public Integer getVariable() {
        return variable;
    }

    public void setVariable(Integer variable) {
        this.variable = variable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (variable != null ? variable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariablesProntuario)) {
            return false;
        }
        VariablesProntuario other = (VariablesProntuario) object;
        if ((this.variable == null && other.variable != null) || (this.variable != null && !this.variable.equals(other.variable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario[ variable=" + variable + " ]";
    }
    
}
