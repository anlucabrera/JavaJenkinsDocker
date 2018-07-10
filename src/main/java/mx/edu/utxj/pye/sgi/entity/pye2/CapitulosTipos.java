/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "capitulos_tipos", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CapitulosTipos.findAll", query = "SELECT c FROM CapitulosTipos c")
    , @NamedQuery(name = "CapitulosTipos.findByCapituloTipo", query = "SELECT c FROM CapitulosTipos c WHERE c.capituloTipo = :capituloTipo")
    , @NamedQuery(name = "CapitulosTipos.findByNumero", query = "SELECT c FROM CapitulosTipos c WHERE c.numero = :numero")
    , @NamedQuery(name = "CapitulosTipos.findByNombre", query = "SELECT c FROM CapitulosTipos c WHERE c.nombre = :nombre")})
public class CapitulosTipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "capitulo_tipo")
    private Short capituloTipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "numero")
    private String numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "capitulo")
    private List<ProductosAreas> productosAreasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "capituloTipo")
    private List<PretechoFinanciero> pretechoFinancieroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "capituloTipo")
    private List<Presupuestos> presupuestosList;

    public CapitulosTipos() {
    }

    public CapitulosTipos(Short capituloTipo) {
        this.capituloTipo = capituloTipo;
    }

    public CapitulosTipos(Short capituloTipo, String numero, String nombre) {
        this.capituloTipo = capituloTipo;
        this.numero = numero;
        this.nombre = nombre;
    }

    public Short getCapituloTipo() {
        return capituloTipo;
    }

    public void setCapituloTipo(Short capituloTipo) {
        this.capituloTipo = capituloTipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ProductosAreas> getProductosAreasList() {
        return productosAreasList;
    }

    public void setProductosAreasList(List<ProductosAreas> productosAreasList) {
        this.productosAreasList = productosAreasList;
    }

    @XmlTransient
    public List<PretechoFinanciero> getPretechoFinancieroList() {
        return pretechoFinancieroList;
    }

    public void setPretechoFinancieroList(List<PretechoFinanciero> pretechoFinancieroList) {
        this.pretechoFinancieroList = pretechoFinancieroList;
    }

    @XmlTransient
    public List<Presupuestos> getPresupuestosList() {
        return presupuestosList;
    }

    public void setPresupuestosList(List<Presupuestos> presupuestosList) {
        this.presupuestosList = presupuestosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (capituloTipo != null ? capituloTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CapitulosTipos)) {
            return false;
        }
        CapitulosTipos other = (CapitulosTipos) object;
        if ((this.capituloTipo == null && other.capituloTipo != null) || (this.capituloTipo != null && !this.capituloTipo.equals(other.capituloTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos[ capituloTipo=" + capituloTipo + " ]";
    }
    
}
