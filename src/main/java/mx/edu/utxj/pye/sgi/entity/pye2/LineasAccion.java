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
@Table(name = "lineas_accion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineasAccion.findAll", query = "SELECT l FROM LineasAccion l")
    , @NamedQuery(name = "LineasAccion.findByLineaAccion", query = "SELECT l FROM LineasAccion l WHERE l.lineaAccion = :lineaAccion")
    , @NamedQuery(name = "LineasAccion.findByNumero", query = "SELECT l FROM LineasAccion l WHERE l.numero = :numero")
    , @NamedQuery(name = "LineasAccion.findByNombre", query = "SELECT l FROM LineasAccion l WHERE l.nombre = :nombre")})
public class LineasAccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "linea_accion")
    private Short lineaAccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private short numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lineaAccion")
    private List<CuadroMandoIntegral> cuadroMandoIntegralList;

    public LineasAccion() {
    }

    public LineasAccion(Short lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    public LineasAccion(Short lineaAccion, short numero, String nombre) {
        this.lineaAccion = lineaAccion;
        this.numero = numero;
        this.nombre = nombre;
    }

    public Short getLineaAccion() {
        return lineaAccion;
    }

    public void setLineaAccion(Short lineaAccion) {
        this.lineaAccion = lineaAccion;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        hash += (lineaAccion != null ? lineaAccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LineasAccion)) {
            return false;
        }
        LineasAccion other = (LineasAccion) object;
        if ((this.lineaAccion == null && other.lineaAccion != null) || (this.lineaAccion != null && !this.lineaAccion.equals(other.lineaAccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion[ lineaAccion=" + lineaAccion + " ]";
    }
    
}
