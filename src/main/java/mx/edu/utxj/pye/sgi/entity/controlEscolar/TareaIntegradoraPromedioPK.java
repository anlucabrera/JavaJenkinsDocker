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
 * @author UTXJ
 */
@Embeddable
public class TareaIntegradoraPromedioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tarea_integradora")
    private int idTareaIntegradora;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_estudiante")
    private int idEstudiante;

    public TareaIntegradoraPromedioPK() {
    }

    public TareaIntegradoraPromedioPK(int idTareaIntegradora, int idEstudiante) {
        this.idTareaIntegradora = idTareaIntegradora;
        this.idEstudiante = idEstudiante;
    }

    public int getIdTareaIntegradora() {
        return idTareaIntegradora;
    }

    public void setIdTareaIntegradora(int idTareaIntegradora) {
        this.idTareaIntegradora = idTareaIntegradora;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idTareaIntegradora;
        hash += (int) idEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TareaIntegradoraPromedioPK)) {
            return false;
        }
        TareaIntegradoraPromedioPK other = (TareaIntegradoraPromedioPK) object;
        if (this.idTareaIntegradora != other.idTareaIntegradora) {
            return false;
        }
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedioPK[ idTareaIntegradora=" + idTareaIntegradora + ", idEstudiante=" + idEstudiante + " ]";
    }
    
}
