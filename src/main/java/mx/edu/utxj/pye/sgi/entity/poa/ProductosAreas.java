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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "productos_areas", catalog = "poa", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductosAreas.findAll", query = "SELECT p FROM ProductosAreas p")
    , @NamedQuery(name = "ProductosAreas.findByProductoArea", query = "SELECT p FROM ProductosAreas p WHERE p.productoArea = :productoArea")
    , @NamedQuery(name = "ProductosAreas.findByCapitulo", query = "SELECT p FROM ProductosAreas p WHERE p.capitulo = :capitulo")
    , @NamedQuery(name = "ProductosAreas.findByArea", query = "SELECT p FROM ProductosAreas p WHERE p.area = :area")})
public class ProductosAreas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "producto_area")
    private Integer productoArea;
    @Basic(optional = false)
    @NotNull
    @Column(name = "capitulo")
    private short capitulo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @JoinColumn(name = "partida", referencedColumnName = "partida")
    @ManyToOne(optional = false)
    private Partidas partida;
    @JoinColumns({
        @JoinColumn(name = "producto", referencedColumnName = "producto")
        , @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal")})
    @ManyToOne(optional = false)
    private Productos productos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoArea")
    private List<RecursosActividad> recursosActividadList;

    public ProductosAreas() {
    }

    public ProductosAreas(Integer productoArea) {
        this.productoArea = productoArea;
    }

    public ProductosAreas(Integer productoArea, short capitulo, short area) {
        this.productoArea = productoArea;
        this.capitulo = capitulo;
        this.area = area;
    }

    public Integer getProductoArea() {
        return productoArea;
    }

    public void setProductoArea(Integer productoArea) {
        this.productoArea = productoArea;
    }

    public short getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(short capitulo) {
        this.capitulo = capitulo;
    }

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
    }

    public Partidas getPartida() {
        return partida;
    }

    public void setPartida(Partidas partida) {
        this.partida = partida;
    }

    public Productos getProductos() {
        return productos;
    }

    public void setProductos(Productos productos) {
        this.productos = productos;
    }

    @XmlTransient
    public List<RecursosActividad> getRecursosActividadList() {
        return recursosActividadList;
    }

    public void setRecursosActividadList(List<RecursosActividad> recursosActividadList) {
        this.recursosActividadList = recursosActividadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productoArea != null ? productoArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductosAreas)) {
            return false;
        }
        ProductosAreas other = (ProductosAreas) object;
        if ((this.productoArea == null && other.productoArea != null) || (this.productoArea != null && !this.productoArea.equals(other.productoArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.poa.entyti.ProductosAreas[ productoArea=" + productoArea + " ]";
    }
    
}
