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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "indicadores_variables", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadoresVariables.findAll", query = "SELECT i FROM IndicadoresVariables i")
    , @NamedQuery(name = "IndicadoresVariables.findByIndicadorVariable", query = "SELECT i FROM IndicadoresVariables i WHERE i.indicadorVariable = :indicadorVariable")})
public class IndicadoresVariables implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indicador_variable")
    private Integer indicadorVariable;
    @JoinColumns({@JoinColumn(name = "indicador", referencedColumnName = "indicador"),@JoinColumn(name = "clave", referencedColumnName = "clave")})    
    @ManyToOne(optional = false)
    private Indicadores indicador;
    @JoinColumn(name = "variable", referencedColumnName = "variable")
    @ManyToOne(optional = false)
    private Variables variable;

    public IndicadoresVariables() {
    }

    public IndicadoresVariables(Integer indicadorVariable) {
        this.indicadorVariable = indicadorVariable;
    }

    public Integer getIndicadorVariable() {
        return indicadorVariable;
    }

    public void setIndicadorVariable(Integer indicadorVariable) {
        this.indicadorVariable = indicadorVariable;
    }

    public Indicadores getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicadores indicador) {
        this.indicador = indicador;
    }

    public Variables getVariable() {
        return variable;
    }

    public void setVariable(Variables variable) {
        this.variable = variable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadorVariable != null ? indicadorVariable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadoresVariables)) {
            return false;
        }
        IndicadoresVariables other = (IndicadoresVariables) object;
        if ((this.indicadorVariable == null && other.indicadorVariable != null) || (this.indicadorVariable != null && !this.indicadorVariable.equals(other.indicadorVariable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.IndicadoresVariables[ indicadorVariable=" + indicadorVariable + " ]";
    }
    
}
