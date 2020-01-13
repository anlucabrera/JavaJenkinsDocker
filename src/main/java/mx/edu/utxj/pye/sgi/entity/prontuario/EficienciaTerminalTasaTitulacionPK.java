/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
public class EficienciaTerminalTasaTitulacionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ettt")
    private int ettt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;

    public EficienciaTerminalTasaTitulacionPK() {
    }

    public EficienciaTerminalTasaTitulacionPK(int ettt, short generacion) {
        this.ettt = ettt;
        this.generacion = generacion;
    }

    public int getEttt() {
        return ettt;
    }

    public void setEttt(int ettt) {
        this.ettt = ettt;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ettt;
        hash += (int) generacion;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EficienciaTerminalTasaTitulacionPK)) {
            return false;
        }
        EficienciaTerminalTasaTitulacionPK other = (EficienciaTerminalTasaTitulacionPK) object;
        if (this.ettt != other.ettt) {
            return false;
        }
        if (this.generacion != other.generacion) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EficienciaTerminalTasaTitulacionPK[ ettt=" + ettt + ", generacion=" + generacion + " ]";
    }
    
}
