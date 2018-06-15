/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.poa;

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
public class ProductosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "producto")
    private String producto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejercicio_fiscal")
    private short ejercicioFiscal;

    public ProductosPK() {
    }

    public ProductosPK(String producto, short ejercicioFiscal) {
        this.producto = producto;
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public short getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (producto != null ? producto.hashCode() : 0);
        hash += (int) ejercicioFiscal;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosPK)) {
            return false;
        }
        ProductosPK other = (ProductosPK) object;
        if ((this.producto == null && other.producto != null) || (this.producto != null && !this.producto.equals(other.producto))) {
            return false;
        }
        if (this.ejercicioFiscal != other.ejercicioFiscal) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.poa.entyti.ProductosPK[ producto=" + producto + ", ejercicioFiscal=" + ejercicioFiscal + " ]";
    }
    
}
