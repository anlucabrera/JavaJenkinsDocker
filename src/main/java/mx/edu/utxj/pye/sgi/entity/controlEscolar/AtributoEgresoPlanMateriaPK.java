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
public class AtributoEgresoPlanMateriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "atributo_egreso")
    private int atributoEgreso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plan_materia")
    private int idPlanMateria;

    public AtributoEgresoPlanMateriaPK() {
    }

    public AtributoEgresoPlanMateriaPK(int atributoEgreso, int idPlanMateria) {
        this.atributoEgreso = atributoEgreso;
        this.idPlanMateria = idPlanMateria;
    }

    public int getAtributoEgreso() {
        return atributoEgreso;
    }

    public void setAtributoEgreso(int atributoEgreso) {
        this.atributoEgreso = atributoEgreso;
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
        hash += (int) atributoEgreso;
        hash += (int) idPlanMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributoEgresoPlanMateriaPK)) {
            return false;
        }
        AtributoEgresoPlanMateriaPK other = (AtributoEgresoPlanMateriaPK) object;
        if (this.atributoEgreso != other.atributoEgreso) {
            return false;
        }
        if (this.idPlanMateria != other.idPlanMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgresoPlanMateriaPK[ atributoEgreso=" + atributoEgreso + ", idPlanMateria=" + idPlanMateria + " ]";
    }
    
}
