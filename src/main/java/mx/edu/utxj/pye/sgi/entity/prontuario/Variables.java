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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "variables", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Variables.findAll", query = "SELECT v FROM Variables v")
    , @NamedQuery(name = "Variables.findByVariable", query = "SELECT v FROM Variables v WHERE v.variable = :variable")
    , @NamedQuery(name = "Variables.findByNombre", query = "SELECT v FROM Variables v WHERE v.nombre = :nombre")})
public class Variables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "variable")
    private Integer variable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "variable")
    private List<IndicadoresVariables> indicadoresVariablesList;
    @JoinColumn(name = "periodo_cumplimiento", referencedColumnName = "periodo_cumplimiento")
    @ManyToOne(optional = false)
    private VariableCumplimientoPeriodos periodoCumplimiento;
    @JoinColumn(name = "disponibilidad", referencedColumnName = "disponibilidad")
    @ManyToOne(optional = false)
    private VariableDisponibilidades disponibilidad;
    @JoinColumn(name = "forma_expresion", referencedColumnName = "forma_expresion")
    @ManyToOne(optional = false)
    private VariableExpresionFormas formaExpresion;
    @JoinColumn(name = "frecuencia", referencedColumnName = "frecuencia")
    @ManyToOne(optional = false)
    private VariableFrecuencias frecuencia;
    @JoinColumn(name = "unidad_medida", referencedColumnName = "unidad_medida")
    @ManyToOne(optional = false)
    private VariableMedidaUnidades unidadMedida;

    public Variables() {
    }

    public Variables(Integer variable) {
        this.variable = variable;
    }

    public Variables(Integer variable, String nombre) {
        this.variable = variable;
        this.nombre = nombre;
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

    @XmlTransient
    public List<IndicadoresVariables> getIndicadoresVariablesList() {
        return indicadoresVariablesList;
    }

    public void setIndicadoresVariablesList(List<IndicadoresVariables> indicadoresVariablesList) {
        this.indicadoresVariablesList = indicadoresVariablesList;
    }

    public VariableCumplimientoPeriodos getPeriodoCumplimiento() {
        return periodoCumplimiento;
    }

    public void setPeriodoCumplimiento(VariableCumplimientoPeriodos periodoCumplimiento) {
        this.periodoCumplimiento = periodoCumplimiento;
    }

    public VariableDisponibilidades getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(VariableDisponibilidades disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public VariableExpresionFormas getFormaExpresion() {
        return formaExpresion;
    }

    public void setFormaExpresion(VariableExpresionFormas formaExpresion) {
        this.formaExpresion = formaExpresion;
    }

    public VariableFrecuencias getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(VariableFrecuencias frecuencia) {
        this.frecuencia = frecuencia;
    }

    public VariableMedidaUnidades getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(VariableMedidaUnidades unidadMedida) {
        this.unidadMedida = unidadMedida;
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
        if (!(object instanceof Variables)) {
            return false;
        }
        Variables other = (Variables) object;
        if ((this.variable == null && other.variable != null) || (this.variable != null && !this.variable.equals(other.variable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Variables[ variable=" + variable + " ]";
    }
    
}
