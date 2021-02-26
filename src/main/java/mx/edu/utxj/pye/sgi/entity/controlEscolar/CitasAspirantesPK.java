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
public class CitasAspirantesPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_cita")
    private int idCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tramite")
    private int idTramite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_aspirante")
    private int idAspirante;

    public CitasAspirantesPK() {
    }

    public CitasAspirantesPK(int idCita, int idTramite, int idAspirante) {
        this.idCita = idCita;
        this.idTramite = idTramite;
        this.idAspirante = idAspirante;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(int idTramite) {
        this.idTramite = idTramite;
    }

    public int getIdAspirante() {
        return idAspirante;
    }

    public void setIdAspirante(int idAspirante) {
        this.idAspirante = idAspirante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idCita;
        hash += (int) idTramite;
        hash += (int) idAspirante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CitasAspirantesPK)) {
            return false;
        }
        CitasAspirantesPK other = (CitasAspirantesPK) object;
        if (this.idCita != other.idCita) {
            return false;
        }
        if (this.idTramite != other.idTramite) {
            return false;
        }
        if (this.idAspirante != other.idAspirante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CitasAspirantesPK[ idCita=" + idCita + ", idTramite=" + idTramite + ", idAspirante=" + idAspirante + " ]";
    }
    
}
