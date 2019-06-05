/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
public class ProcesosGeneracionesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "proceso")
    private int proceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "generacion")
    private short generacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private short grado;

    public ProcesosGeneracionesPK() {
    }

    public ProcesosGeneracionesPK(int proceso, short generacion, int periodo, short grado) {
        this.proceso = proceso;
        this.generacion = generacion;
        this.periodo = periodo;
        this.grado = grado;
    }

    public int getProceso() {
        return proceso;
    }

    public void setProceso(int proceso) {
        this.proceso = proceso;
    }

    public short getGeneracion() {
        return generacion;
    }

    public void setGeneracion(short generacion) {
        this.generacion = generacion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public short getGrado() {
        return grado;
    }

    public void setGrado(short grado) {
        this.grado = grado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) proceso;
        hash += (int) generacion;
        hash += (int) periodo;
        hash += (int) grado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProcesosGeneracionesPK)) {
            return false;
        }
        ProcesosGeneracionesPK other = (ProcesosGeneracionesPK) object;
        if (this.proceso != other.proceso) {
            return false;
        }
        if (this.generacion != other.generacion) {
            return false;
        }
        if (this.periodo != other.periodo) {
            return false;
        }
        if (this.grado != other.grado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneracionesPK[ proceso=" + proceso + ", generacion=" + generacion + ", periodo=" + periodo + ", grado=" + grado + " ]";
    }
    
}
