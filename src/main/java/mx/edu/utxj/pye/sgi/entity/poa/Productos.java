/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.poa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "productos", catalog = "poa", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productos.findAll", query = "SELECT p FROM Productos p")
    , @NamedQuery(name = "Productos.findByProducto", query = "SELECT p FROM Productos p WHERE p.productosPK.producto = :producto")
    , @NamedQuery(name = "Productos.findByEjercicioFiscal", query = "SELECT p FROM Productos p WHERE p.productosPK.ejercicioFiscal = :ejercicioFiscal")
    , @NamedQuery(name = "Productos.findByDescripcion", query = "SELECT p FROM Productos p WHERE p.descripcion = :descripcion")})
public class Productos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProductosPK productosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productos")
    private List<ProductosAreas> productosAreasList;

    public Productos() {
    }

    public Productos(ProductosPK productosPK) {
        this.productosPK = productosPK;
    }

    public Productos(ProductosPK productosPK, String descripcion) {
        this.productosPK = productosPK;
        this.descripcion = descripcion;
    }

    public Productos(String producto, short ejercicioFiscal) {
        this.productosPK = new ProductosPK(producto, ejercicioFiscal);
    }

    public ProductosPK getProductosPK() {
        return productosPK;
    }

    public void setProductosPK(ProductosPK productosPK) {
        this.productosPK = productosPK;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ProductosAreas> getProductosAreasList() {
        return productosAreasList;
    }

    public void setProductosAreasList(List<ProductosAreas> productosAreasList) {
        this.productosAreasList = productosAreasList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productosPK != null ? productosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productos)) {
            return false;
        }
        Productos other = (Productos) object;
        if ((this.productosPK == null && other.productosPK != null) || (this.productosPK != null && !this.productosPK.equals(other.productosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.poa.entyti.Productos[ productosPK=" + productosPK + " ]";
    }
    
}
