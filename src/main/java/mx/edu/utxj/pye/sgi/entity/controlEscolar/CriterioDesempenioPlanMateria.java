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
@Table(name = "criterio_desempenio_plan_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CriterioDesempenioPlanMateria.findAll", query = "SELECT c FROM CriterioDesempenioPlanMateria c")
    , @NamedQuery(name = "CriterioDesempenioPlanMateria.findByCriterioDesempenio", query = "SELECT c FROM CriterioDesempenioPlanMateria c WHERE c.criterioDesempenioPlanMateriaPK.criterioDesempenio = :criterioDesempenio")
    , @NamedQuery(name = "CriterioDesempenioPlanMateria.findByIdPlanMateria", query = "SELECT c FROM CriterioDesempenioPlanMateria c WHERE c.criterioDesempenioPlanMateriaPK.idPlanMateria = :idPlanMateria")
    , @NamedQuery(name = "CriterioDesempenioPlanMateria.findByNotas", query = "SELECT c FROM CriterioDesempenioPlanMateria c WHERE c.notas = :notas")})
public class CriterioDesempenioPlanMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CriterioDesempenioPlanMateriaPK criterioDesempenioPlanMateriaPK;
    @Size(max = 255)
    @Column(name = "notas")
    private String notas;
    @JoinColumn(name = "criterio_desempenio", referencedColumnName = "criteri_desempenio", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private CriterioDesempenio criterioDesempenio1;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PlanEstudioMateria planEstudioMateria;

    public CriterioDesempenioPlanMateria() {
    }

    public CriterioDesempenioPlanMateria(CriterioDesempenioPlanMateriaPK criterioDesempenioPlanMateriaPK) {
        this.criterioDesempenioPlanMateriaPK = criterioDesempenioPlanMateriaPK;
    }

    public CriterioDesempenioPlanMateria(int criterioDesempenio, int idPlanMateria) {
        this.criterioDesempenioPlanMateriaPK = new CriterioDesempenioPlanMateriaPK(criterioDesempenio, idPlanMateria);
    }

    public CriterioDesempenioPlanMateriaPK getCriterioDesempenioPlanMateriaPK() {
        return criterioDesempenioPlanMateriaPK;
    }

    public void setCriterioDesempenioPlanMateriaPK(CriterioDesempenioPlanMateriaPK criterioDesempenioPlanMateriaPK) {
        this.criterioDesempenioPlanMateriaPK = criterioDesempenioPlanMateriaPK;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public CriterioDesempenio getCriterioDesempenio1() {
        return criterioDesempenio1;
    }

    public void setCriterioDesempenio1(CriterioDesempenio criterioDesempenio1) {
        this.criterioDesempenio1 = criterioDesempenio1;
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
        hash += (criterioDesempenioPlanMateriaPK != null ? criterioDesempenioPlanMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioDesempenioPlanMateria)) {
            return false;
        }
        CriterioDesempenioPlanMateria other = (CriterioDesempenioPlanMateria) object;
        if ((this.criterioDesempenioPlanMateriaPK == null && other.criterioDesempenioPlanMateriaPK != null) || (this.criterioDesempenioPlanMateriaPK != null && !this.criterioDesempenioPlanMateriaPK.equals(other.criterioDesempenioPlanMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenioPlanMateria[ criterioDesempenioPlanMateriaPK=" + criterioDesempenioPlanMateriaPK + " ]";
    }
    
}
