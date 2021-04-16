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
public class IndicadorAlineacionPlanMateriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "indicador")
    private int indicador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plan_materia")
    private int idPlanMateria;

    public IndicadorAlineacionPlanMateriaPK() {
    }

    public IndicadorAlineacionPlanMateriaPK(int indicador, int idPlanMateria) {
        this.indicador = indicador;
        this.idPlanMateria = idPlanMateria;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
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
        hash += (int) indicador;
        hash += (int) idPlanMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorAlineacionPlanMateriaPK)) {
            return false;
        }
        IndicadorAlineacionPlanMateriaPK other = (IndicadorAlineacionPlanMateriaPK) object;
        if (this.indicador != other.indicador) {
            return false;
        }
        if (this.idPlanMateria != other.idPlanMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacionPlanMateriaPK[ indicador=" + indicador + ", idPlanMateria=" + idPlanMateria + " ]";
    }
    
}
