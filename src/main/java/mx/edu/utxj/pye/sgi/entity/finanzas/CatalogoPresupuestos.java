/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "catalogo_presupuestos", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoPresupuestos.findAll", query = "SELECT c FROM CatalogoPresupuestos c")
    , @NamedQuery(name = "CatalogoPresupuestos.findByPresupuesto", query = "SELECT c FROM CatalogoPresupuestos c WHERE c.presupuesto = :presupuesto")
    , @NamedQuery(name = "CatalogoPresupuestos.findByDenominacion", query = "SELECT c FROM CatalogoPresupuestos c WHERE c.denominacion = :denominacion")})
public class CatalogoPresupuestos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "presupuesto")
    private Integer presupuesto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "denominacion")
    private String denominacion;
    @ManyToMany(mappedBy = "catalogoPresupuestosList")
    private List<Facturas> facturasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "presupuesto")
    private List<PartidasFiscales> partidasFiscalesList;

    public CatalogoPresupuestos() {
    }

    public CatalogoPresupuestos(Integer presupuesto) {
        this.presupuesto = presupuesto;
    }

    public CatalogoPresupuestos(Integer presupuesto, String denominacion) {
        this.presupuesto = presupuesto;
        this.denominacion = denominacion;
    }

    public Integer getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Integer presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    @XmlTransient
    public List<Facturas> getFacturasList() {
        return facturasList;
    }

    public void setFacturasList(List<Facturas> facturasList) {
        this.facturasList = facturasList;
    }

    @XmlTransient
    public List<PartidasFiscales> getPartidasFiscalesList() {
        return partidasFiscalesList;
    }

    public void setPartidasFiscalesList(List<PartidasFiscales> partidasFiscalesList) {
        this.partidasFiscalesList = partidasFiscalesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (presupuesto != null ? presupuesto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoPresupuestos)) {
            return false;
        }
        CatalogoPresupuestos other = (CatalogoPresupuestos) object;
        if ((this.presupuesto == null && other.presupuesto != null) || (this.presupuesto != null && !this.presupuesto.equals(other.presupuesto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.CatalogoPresupuestos[ presupuesto=" + presupuesto + " ]";
    }
    
}
