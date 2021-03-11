/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "metas_propuestas", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MetasPropuestas.findAll", query = "SELECT m FROM MetasPropuestas m")
    , @NamedQuery(name = "MetasPropuestas.findByIdMetapropuesta", query = "SELECT m FROM MetasPropuestas m WHERE m.idMetapropuesta = :idMetapropuesta")
    , @NamedQuery(name = "MetasPropuestas.findByPeriodo", query = "SELECT m FROM MetasPropuestas m WHERE m.periodo = :periodo")
    , @NamedQuery(name = "MetasPropuestas.findByValorPropuesto", query = "SELECT m FROM MetasPropuestas m WHERE m.valorPropuesto = :valorPropuesto")
    , @NamedQuery(name = "MetasPropuestas.findByValorAlcanzado", query = "SELECT m FROM MetasPropuestas m WHERE m.valorAlcanzado = :valorAlcanzado")})
public class MetasPropuestas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_metapropuesta")
    private Integer idMetapropuesta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_propuesto")
    private Double valorPropuesto;
    @Column(name = "valor_alcanzado")
    private Double valorAlcanzado;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")
    @ManyToOne(optional = false)
    private PlanEstudioMateria idPlanMateria;

    public MetasPropuestas() {
    }

    public MetasPropuestas(Integer idMetapropuesta) {
        this.idMetapropuesta = idMetapropuesta;
    }

    public MetasPropuestas(Integer idMetapropuesta, int periodo) {
        this.idMetapropuesta = idMetapropuesta;
        this.periodo = periodo;
    }

    public Integer getIdMetapropuesta() {
        return idMetapropuesta;
    }

    public void setIdMetapropuesta(Integer idMetapropuesta) {
        this.idMetapropuesta = idMetapropuesta;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Double getValorPropuesto() {
        return valorPropuesto;
    }

    public void setValorPropuesto(Double valorPropuesto) {
        this.valorPropuesto = valorPropuesto;
    }

    public Double getValorAlcanzado() {
        return valorAlcanzado;
    }

    public void setValorAlcanzado(Double valorAlcanzado) {
        this.valorAlcanzado = valorAlcanzado;
    }

    public PlanEstudioMateria getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(PlanEstudioMateria idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMetapropuesta != null ? idMetapropuesta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetasPropuestas)) {
            return false;
        }
        MetasPropuestas other = (MetasPropuestas) object;
        if ((this.idMetapropuesta == null && other.idMetapropuesta != null) || (this.idMetapropuesta != null && !this.idMetapropuesta.equals(other.idMetapropuesta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas[ idMetapropuesta=" + idMetapropuesta + " ]";
    }
    
}
