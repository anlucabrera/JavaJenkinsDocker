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
 * @author Desarrollo
 */
@Embeddable
public class ObjetivoEducacionalPlanMateriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "objetivo_educacional")
    private int objetivoEducacional;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plan_materia")
    private int idPlanMateria;

    public ObjetivoEducacionalPlanMateriaPK() {
    }

    public ObjetivoEducacionalPlanMateriaPK(int objetivoEducacional, int idPlanMateria) {
        this.objetivoEducacional = objetivoEducacional;
        this.idPlanMateria = idPlanMateria;
    }

    public int getObjetivoEducacional() {
        return objetivoEducacional;
    }

    public void setObjetivoEducacional(int objetivoEducacional) {
        this.objetivoEducacional = objetivoEducacional;
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
        hash += (int) objetivoEducacional;
        hash += (int) idPlanMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetivoEducacionalPlanMateriaPK)) {
            return false;
        }
        ObjetivoEducacionalPlanMateriaPK other = (ObjetivoEducacionalPlanMateriaPK) object;
        if (this.objetivoEducacional != other.objetivoEducacional) {
            return false;
        }
        if (this.idPlanMateria != other.idPlanMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacionalPlanMateriaPK[ objetivoEducacional=" + objetivoEducacional + ", idPlanMateria=" + idPlanMateria + " ]";
    }
    
}
