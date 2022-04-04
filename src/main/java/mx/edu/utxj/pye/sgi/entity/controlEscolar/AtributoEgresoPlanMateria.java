/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Planeacion
 */
@Entity
@Table(name = "atributo_egreso_plan_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributoEgresoPlanMateria.findAll", query = "SELECT a FROM AtributoEgresoPlanMateria a")
    , @NamedQuery(name = "AtributoEgresoPlanMateria.findByAtributoEgreso", query = "SELECT a FROM AtributoEgresoPlanMateria a WHERE a.atributoEgresoPlanMateriaPK.atributoEgreso = :atributoEgreso")
    , @NamedQuery(name = "AtributoEgresoPlanMateria.findByIdPlanMateria", query = "SELECT a FROM AtributoEgresoPlanMateria a WHERE a.atributoEgresoPlanMateriaPK.idPlanMateria = :idPlanMateria")
    , @NamedQuery(name = "AtributoEgresoPlanMateria.findByNotas", query = "SELECT a FROM AtributoEgresoPlanMateria a WHERE a.notas = :notas")})
public class AtributoEgresoPlanMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AtributoEgresoPlanMateriaPK atributoEgresoPlanMateriaPK;
    @Size(max = 255)
    @Column(name = "notas")
    private String notas;
    @JoinColumn(name = "atributo_egreso", referencedColumnName = "atributo_egreso", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AtributoEgreso atributoEgreso1;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PlanEstudioMateria planEstudioMateria;

    public AtributoEgresoPlanMateria() {
    }

    public AtributoEgresoPlanMateria(AtributoEgresoPlanMateriaPK atributoEgresoPlanMateriaPK) {
        this.atributoEgresoPlanMateriaPK = atributoEgresoPlanMateriaPK;
    }

    public AtributoEgresoPlanMateria(int atributoEgreso, int idPlanMateria) {
        this.atributoEgresoPlanMateriaPK = new AtributoEgresoPlanMateriaPK(atributoEgreso, idPlanMateria);
    }

    public AtributoEgresoPlanMateriaPK getAtributoEgresoPlanMateriaPK() {
        return atributoEgresoPlanMateriaPK;
    }

    public void setAtributoEgresoPlanMateriaPK(AtributoEgresoPlanMateriaPK atributoEgresoPlanMateriaPK) {
        this.atributoEgresoPlanMateriaPK = atributoEgresoPlanMateriaPK;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public AtributoEgreso getAtributoEgreso1() {
        return atributoEgreso1;
    }

    public void setAtributoEgreso1(AtributoEgreso atributoEgreso1) {
        this.atributoEgreso1 = atributoEgreso1;
    }

    public PlanEstudioMateria getPlanEstudioMateria() {
        return planEstudioMateria;
    }

    public void setPlanEstudioMateria(PlanEstudioMateria planEstudioMateria) {
        this.planEstudioMateria = planEstudioMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (atributoEgresoPlanMateriaPK != null ? atributoEgresoPlanMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributoEgresoPlanMateria)) {
            return false;
        }
        AtributoEgresoPlanMateria other = (AtributoEgresoPlanMateria) object;
        if ((this.atributoEgresoPlanMateriaPK == null && other.atributoEgresoPlanMateriaPK != null) || (this.atributoEgresoPlanMateriaPK != null && !this.atributoEgresoPlanMateriaPK.equals(other.atributoEgresoPlanMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgresoPlanMateria[ atributoEgresoPlanMateriaPK=" + atributoEgresoPlanMateriaPK + " ]";
    }
    
}
