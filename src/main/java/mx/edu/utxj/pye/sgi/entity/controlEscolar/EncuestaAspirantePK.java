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
public class EncuestaAspirantePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_encuesta_aspirante")
    private int idEncuestaAspirante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_aspirante")
    private int cveAspirante;

    public EncuestaAspirantePK() {
    }

    public EncuestaAspirantePK(int idEncuestaAspirante, int cveAspirante) {
        this.idEncuestaAspirante = idEncuestaAspirante;
        this.cveAspirante = cveAspirante;
    }

    public int getIdEncuestaAspirante() {
        return idEncuestaAspirante;
    }

    public void setIdEncuestaAspirante(int idEncuestaAspirante) {
        this.idEncuestaAspirante = idEncuestaAspirante;
    }

    public int getCveAspirante() {
        return cveAspirante;
    }

    public void setCveAspirante(int cveAspirante) {
        this.cveAspirante = cveAspirante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEncuestaAspirante;
        hash += (int) cveAspirante;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncuestaAspirantePK)) {
            return false;
        }
        EncuestaAspirantePK other = (EncuestaAspirantePK) object;
        if (this.idEncuestaAspirante != other.idEncuestaAspirante) {
            return false;
        }
        if (this.cveAspirante != other.cveAspirante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirantePK[ idEncuestaAspirante=" + idEncuestaAspirante + ", cveAspirante=" + cveAspirante + " ]";
    }
    
}
