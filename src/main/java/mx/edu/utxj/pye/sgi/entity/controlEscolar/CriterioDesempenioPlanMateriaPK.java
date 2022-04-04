/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Planeacion
 */
@Embeddable
public class CriterioDesempenioPlanMateriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio_desempenio")
    private int criterioDesempenio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plan_materia")
    private int idPlanMateria;

    public CriterioDesempenioPlanMateriaPK() {
    }

    public CriterioDesempenioPlanMateriaPK(int criterioDesempenio, int idPlanMateria) {
        this.criterioDesempenio = criterioDesempenio;
        this.idPlanMateria = idPlanMateria;
    }

    public int getCriterioDesempenio() {
        return criterioDesempenio;
    }

    public void setCriterioDesempenio(int criterioDesempenio) {
        this.criterioDesempenio = criterioDesempenio;
    }

    public int getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(int idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) criterioDesempenio;
        hash += (int) idPlanMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioDesempenioPlanMateriaPK)) {
            return false;
        }
        CriterioDesempenioPlanMateriaPK other = (CriterioDesempenioPlanMateriaPK) object;
        if (this.criterioDesempenio != other.criterioDesempenio) {
            return false;
        }
        if (this.idPlanMateria != other.idPlanMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenioPlanMateriaPK[ criterioDesempenio=" + criterioDesempenio + ", idPlanMateria=" + idPlanMateria + " ]";
    }
    
}
