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
public class ServiciosTecnologicosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mes")
    private short mes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave_servicio")
    private int claveServicio;

    public ServiciosTecnologicosPK() {
    }

    public ServiciosTecnologicosPK(int ciclo, short mes, int claveServicio) {
        this.ciclo = ciclo;
        this.mes = mes;
        this.claveServicio = claveServicio;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public int getClaveServicio() {
        return claveServicio;
    }

    public void setClaveServicio(int claveServicio) {
        this.claveServicio = claveServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) mes;
        hash += (int) claveServicio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosTecnologicosPK)) {
            return false;
        }
        ServiciosTecnologicosPK other = (ServiciosTecnologicosPK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if (this.claveServicio != other.claveServicio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ServiciosTecnologicosPK[ ciclo=" + ciclo + ", mes=" + mes + ", claveServicio=" + claveServicio + " ]";
    }
    
}
