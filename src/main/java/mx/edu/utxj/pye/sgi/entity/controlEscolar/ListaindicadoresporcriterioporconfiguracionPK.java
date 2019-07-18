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
public class ListaindicadoresporcriterioporconfiguracionPK implements Serializable{
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "configuracion")
    private int configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveCriterio")
    private int claveCriterio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "claveIndicador")
    private int claveIndicador;

    public ListaindicadoresporcriterioporconfiguracionPK() {
    }

    public ListaindicadoresporcriterioporconfiguracionPK(int configuracion, int periodo, int claveCriterio, int claveIndicador) {
        this.configuracion = configuracion;
        this.periodo = periodo;
        this.claveCriterio = claveCriterio;
        this.claveIndicador = claveIndicador;
    }

    public int getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(int configuracion) {
        this.configuracion = configuracion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getClaveCriterio() {
        return claveCriterio;
    }

    public void setClaveCriterio(int claveCriterio) {
        this.claveCriterio = claveCriterio;
    }

    public int getClaveIndicador() {
        return claveIndicador;
    }

    public void setClaveIndicador(int claveIndicador) {
        this.claveIndicador = claveIndicador;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.configuracion;
        hash = 83 * hash + this.periodo;
        hash = 83 * hash + this.claveCriterio;
        hash = 83 * hash + this.claveIndicador;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListaindicadoresporcriterioporconfiguracionPK other = (ListaindicadoresporcriterioporconfiguracionPK) obj;
        if (this.configuracion != other.configuracion) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if (this.claveCriterio != other.claveCriterio) {
            return false;
        }
        if (this.claveIndicador != other.claveIndicador) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ListaindicadoresporcriterioporconfiguracionPK{" + "configuracion=" + configuracion + ", periodo=" + periodo + ", claveCriterio=" + claveCriterio + ", claveIndicador=" + claveIndicador + '}';
    }
    
    
    
}
