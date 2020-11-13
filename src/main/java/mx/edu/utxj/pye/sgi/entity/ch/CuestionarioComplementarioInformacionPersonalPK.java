/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

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
public class CuestionarioComplementarioInformacionPersonalPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private int evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;

    public CuestionarioComplementarioInformacionPersonalPK() {
    }

    public CuestionarioComplementarioInformacionPersonalPK(int evaluacion, int personal) {
        this.evaluacion = evaluacion;
        this.personal = personal;
    }

    public int getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(int evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) evaluacion;
        hash += (int) personal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuestionarioComplementarioInformacionPersonalPK)) {
            return false;
        }
        CuestionarioComplementarioInformacionPersonalPK other = (CuestionarioComplementarioInformacionPersonalPK) object;
        if (this.evaluacion != other.evaluacion) {
            return false;
        }
        if (this.personal != other.personal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.CuestionarioComplementarioInformacionPersonalPK[ evaluacion=" + evaluacion + ", personal=" + personal + " ]";
    }
    
}
