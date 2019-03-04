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
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class UsuariosRegistroEspecificoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_personal")
    private int clavePersonal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "registro")
    private String registro;

    public UsuariosRegistroEspecificoPK() {
    }

    public UsuariosRegistroEspecificoPK(int clavePersonal, String registro) {
        this.clavePersonal = clavePersonal;
        this.registro = registro;
    }

    public int getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(int clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) clavePersonal;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosRegistroEspecificoPK)) {
            return false;
        }
        UsuariosRegistroEspecificoPK other = (UsuariosRegistroEspecificoPK) object;
        if (this.clavePersonal != other.clavePersonal) {
            return false;
        }
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.UsuariosRegistroEspecificoPK[ clavePersonal=" + clavePersonal + ", registro=" + registro + " ]";
    }
    
}
