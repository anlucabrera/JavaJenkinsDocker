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
 * @author Planeacion
 */
@Entity
@Table(name = "plan_competencias", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanCompetencias.findAll", query = "SELECT p FROM PlanCompetencias p")
    , @NamedQuery(name = "PlanCompetencias.findByIdCompetencia", query = "SELECT p FROM PlanCompetencias p WHERE p.idCompetencia = :idCompetencia")
    , @NamedQuery(name = "PlanCompetencias.findByDescripcion", query = "SELECT p FROM PlanCompetencias p WHERE p.descripcion = :descripcion")
    , @NamedQuery(name = "PlanCompetencias.findByIdPlan", query = "SELECT p FROM PlanCompetencias p WHERE p.idPlan = :idPlan")
    , @NamedQuery(name = "PlanCompetencias.findByTipo", query = "SELECT p FROM PlanCompetencias p WHERE p.tipo = :tipo")})
public class PlanCompetencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_competencia")
    private Integer idCompetencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plan")
    private int idPlan;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "tipo")
    private String tipo;

    public PlanCompetencias() {
    }

    public PlanCompetencias(Integer idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public PlanCompetencias(Integer idCompetencia, String descripcion, int idPlan, String tipo) {
        this.idCompetencia = idCompetencia;
        this.descripcion = descripcion;
        this.idPlan = idPlan;
        this.tipo = tipo;
    }

    public Integer getIdCompetencia() {
        return idCompetencia;
    }

    public void setIdCompetencia(Integer idCompetencia) {
        this.idCompetencia = idCompetencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompetencia != null ? idCompetencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanCompetencias)) {
            return false;
        }
        PlanCompetencias other = (PlanCompetencias) object;
        if ((this.idCompetencia == null && other.idCompetencia != null) || (this.idCompetencia != null && !this.idCompetencia.equals(other.idCompetencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PlanCompetencias[ idCompetencia=" + idCompetencia + " ]";
    }
    
}
