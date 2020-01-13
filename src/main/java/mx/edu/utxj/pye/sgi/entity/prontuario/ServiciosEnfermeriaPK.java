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
import javax.validation.constraints.Size;

/**
 *
 * @author UTXJ
 */
@Embeddable
public class ServiciosEnfermeriaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mes")
    private short mes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tipo_servicio")
    private String tipoServicio;

    public ServiciosEnfermeriaPK() {
    }

    public ServiciosEnfermeriaPK(int ciclo, int periodo, short mes, String tipoServicio) {
        this.ciclo = ciclo;
        this.periodo = periodo;
        this.mes = mes;
        this.tipoServicio = tipoServicio;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public short getMes() {
        return mes;
    }

    public void setMes(short mes) {
        this.mes = mes;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) periodo;
        hash += (int) mes;
        hash += (tipoServicio != null ? tipoServicio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosEnfermeriaPK)) {
            return false;
        }
        ServiciosEnfermeriaPK other = (ServiciosEnfermeriaPK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if (this.mes != other.mes) {
            return false;
        }
        if ((this.tipoServicio == null && other.tipoServicio != null) || (this.tipoServicio != null && !this.tipoServicio.equals(other.tipoServicio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ServiciosEnfermeriaPK[ ciclo=" + ciclo + ", periodo=" + periodo + ", mes=" + mes + ", tipoServicio=" + tipoServicio + " ]";
    }
    
}
