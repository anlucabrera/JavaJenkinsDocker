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
public class SatisfaccionHistoricoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private int ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @Basic(optional = false)
    @NotNull
    @Column(name = "apartado")
    private double apartado;

    public SatisfaccionHistoricoPK() {
    }

    public SatisfaccionHistoricoPK(int ciclo, short area, double apartado) {
        this.ciclo = ciclo;
        this.area = area;
        this.apartado = apartado;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public double getApartado() {
        return apartado;
    }

    public void setApartado(double apartado) {
        this.apartado = apartado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ciclo;
        hash += (int) area;
        hash += (int) apartado;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SatisfaccionHistoricoPK)) {
            return false;
        }
        SatisfaccionHistoricoPK other = (SatisfaccionHistoricoPK) object;
        if (this.ciclo != other.ciclo) {
            return false;
        }
        if (this.area != other.area) {
            return false;
        }
        if (this.apartado != other.apartado) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.SatisfaccionHistoricoPK[ ciclo=" + ciclo + ", area=" + area + ", apartado=" + apartado + " ]";
    }
    
}
