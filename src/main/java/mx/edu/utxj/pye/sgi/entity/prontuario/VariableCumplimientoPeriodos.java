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
@Table(name = "variable_cumplimiento_periodos", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableCumplimientoPeriodos.findAll", query = "SELECT v FROM VariableCumplimientoPeriodos v")
    , @NamedQuery(name = "VariableCumplimientoPeriodos.findByPeriodoCumplimiento", query = "SELECT v FROM VariableCumplimientoPeriodos v WHERE v.periodoCumplimiento = :periodoCumplimiento")
    , @NamedQuery(name = "VariableCumplimientoPeriodos.findByNombre", query = "SELECT v FROM VariableCumplimientoPeriodos v WHERE v.nombre = :nombre")})
public class VariableCumplimientoPeriodos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "periodo_cumplimiento")
    private Short periodoCumplimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "periodoCumplimiento", fetch = FetchType.LAZY)
    private List<Variables> variablesList;

    public VariableCumplimientoPeriodos() {
    }

    public VariableCumplimientoPeriodos(Short periodoCumplimiento) {
        this.periodoCumplimiento = periodoCumplimiento;
    }

    public VariableCumplimientoPeriodos(Short periodoCumplimiento, String nombre) {
        this.periodoCumplimiento = periodoCumplimiento;
        this.nombre = nombre;
    }

    public Short getPeriodoCumplimiento() {
        return periodoCumplimiento;
    }

    public void setPeriodoCumplimiento(Short periodoCumplimiento) {
        this.periodoCumplimiento = periodoCumplimiento;
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
        hash += (periodoCumplimiento != null ? periodoCumplimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableCumplimientoPeriodos)) {
            return false;
        }
        VariableCumplimientoPeriodos other = (VariableCumplimientoPeriodos) object;
        if ((this.periodoCumplimiento == null && other.periodoCumplimiento != null) || (this.periodoCumplimiento != null && !this.periodoCumplimiento.equals(other.periodoCumplimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.VariableCumplimientoPeriodos[ periodoCumplimiento=" + periodoCumplimiento + " ]";
    }
    
}
