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
@Table(name = "variable_medida_unidades", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableMedidaUnidades.findAll", query = "SELECT v FROM VariableMedidaUnidades v")
    , @NamedQuery(name = "VariableMedidaUnidades.findByUnidadMedida", query = "SELECT v FROM VariableMedidaUnidades v WHERE v.unidadMedida = :unidadMedida")
    , @NamedQuery(name = "VariableMedidaUnidades.findByNombre", query = "SELECT v FROM VariableMedidaUnidades v WHERE v.nombre = :nombre")})
public class VariableMedidaUnidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "unidad_medida")
    private Short unidadMedida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMedida")
    private List<Variables> variablesList;

    public VariableMedidaUnidades() {
    }

    public VariableMedidaUnidades(Short unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public VariableMedidaUnidades(Short unidadMedida, String nombre) {
        this.unidadMedida = unidadMedida;
        this.nombre = nombre;
    }

    public Short getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(Short unidadMedida) {
        this.unidadMedida = unidadMedida;
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
        hash += (unidadMedida != null ? unidadMedida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableMedidaUnidades)) {
            return false;
        }
        VariableMedidaUnidades other = (VariableMedidaUnidades) object;
        if ((this.unidadMedida == null && other.unidadMedida != null) || (this.unidadMedida != null && !this.unidadMedida.equals(other.unidadMedida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariableMedidaUnidades[ unidadMedida=" + unidadMedida + " ]";
    }
    
}
