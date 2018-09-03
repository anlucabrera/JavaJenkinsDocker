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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "ejercicios_fiscales", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EjerciciosFiscales.findAll", query = "SELECT e FROM EjerciciosFiscales e")
    , @NamedQuery(name = "EjerciciosFiscales.findByEjercicioFiscal", query = "SELECT e FROM EjerciciosFiscales e WHERE e.ejercicioFiscal = :ejercicioFiscal")
    , @NamedQuery(name = "EjerciciosFiscales.findByAnio", query = "SELECT e FROM EjerciciosFiscales e WHERE e.anio = :anio")})
public class EjerciciosFiscales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ejercicio_fiscal")
    private Short ejercicioFiscal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    private short anio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ejerciciosFiscales")
    private List<Productos> productosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ejercicioFiscal")
    private List<PretechoFinanciero> pretechoFinancieroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ejercicioFiscal")
    private List<EventosRegistros> eventosRegistrosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ejercicioFiscal")
    private List<CuadroMandoIntegral> cuadroMandoIntegralList;

    public EjerciciosFiscales() {
    }

    public EjerciciosFiscales(Short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public EjerciciosFiscales(Short ejercicioFiscal, short anio) {
        this.ejercicioFiscal = ejercicioFiscal;
        this.anio = anio;
    }

    public Short getEjercicioFiscal() {
        return ejercicioFiscal;
    }

    public void setEjercicioFiscal(Short ejercicioFiscal) {
        this.ejercicioFiscal = ejercicioFiscal;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    @XmlTransient
    public List<Productos> getProductosList() {
        return productosList;
    }

    public void setProductosList(List<Productos> productosList) {
        this.productosList = productosList;
    }

    @XmlTransient
    public List<PretechoFinanciero> getPretechoFinancieroList() {
        return pretechoFinancieroList;
    }

    public void setPretechoFinancieroList(List<PretechoFinanciero> pretechoFinancieroList) {
        this.pretechoFinancieroList = pretechoFinancieroList;
    }

    @XmlTransient
    public List<EventosRegistros> getEventosRegistrosList() {
        return eventosRegistrosList;
    }

    public void setEventosRegistrosList(List<EventosRegistros> eventosRegistrosList) {
        this.eventosRegistrosList = eventosRegistrosList;
    }

    @XmlTransient
    public List<CuadroMandoIntegral> getCuadroMandoIntegralList() {
        return cuadroMandoIntegralList;
    }

    public void setCuadroMandoIntegralList(List<CuadroMandoIntegral> cuadroMandoIntegralList) {
        this.cuadroMandoIntegralList = cuadroMandoIntegralList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ejercicioFiscal != null ? ejercicioFiscal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EjerciciosFiscales)) {
            return false;
        }
        EjerciciosFiscales other = (EjerciciosFiscales) object;
        if ((this.ejercicioFiscal == null && other.ejercicioFiscal != null) || (this.ejercicioFiscal != null && !this.ejercicioFiscal.equals(other.ejercicioFiscal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales[ ejercicioFiscal=" + ejercicioFiscal + " ]";
    }
    
}
