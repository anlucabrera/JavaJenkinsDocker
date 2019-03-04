/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
public class ModulosRegistroEspecificoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private short clave;
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal")
    private int personal;

    public ModulosRegistroEspecificoPK() {
    }

    public ModulosRegistroEspecificoPK(short clave, int personal) {
        this.clave = clave;
        this.personal = personal;
    }

    public short getClave() {
        return clave;
    }

    public void setClave(short clave) {
        this.clave = clave;
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
        hash += (int) clave;
        hash += (int) personal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModulosRegistroEspecificoPK)) {
            return false;
        }
        ModulosRegistroEspecificoPK other = (ModulosRegistroEspecificoPK) object;
        if (this.clave != other.clave) {
            return false;
        }
        if (this.personal != other.personal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroEspecificoPK[ clave=" + clave + ", personal=" + personal + " ]";
    }
    
}
