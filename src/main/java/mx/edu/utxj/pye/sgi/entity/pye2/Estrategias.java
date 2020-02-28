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
@Table(name = "estrategias", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estrategias.findAll", query = "SELECT e FROM Estrategias e")
    , @NamedQuery(name = "Estrategias.findByEstrategia", query = "SELECT e FROM Estrategias e WHERE e.estrategia = :estrategia")
    , @NamedQuery(name = "Estrategias.findByNumero", query = "SELECT e FROM Estrategias e WHERE e.numero = :numero")
    , @NamedQuery(name = "Estrategias.findByNombre", query = "SELECT e FROM Estrategias e WHERE e.nombre = :nombre")})
public class Estrategias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "estrategia")
    private Short estrategia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private short numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estrategia")
    private List<CuadroMandoIntegral> cuadroMandoIntegralList;

    public Estrategias() {
    }

    public Estrategias(Short estrategia) {
        this.estrategia = estrategia;
    }

    public Estrategias(Short estrategia, short numero, String nombre) {
        this.estrategia = estrategia;
        this.numero = numero;
        this.nombre = nombre;
    }

    public Short getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(Short estrategia) {
        this.estrategia = estrategia;
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
        hash += (estrategia != null ? estrategia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estrategias)) {
            return false;
        }
        Estrategias other = (Estrategias) object;
        if ((this.estrategia == null && other.estrategia != null) || (this.estrategia != null && !this.estrategia.equals(other.estrategia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Estrategias[ estrategia=" + estrategia + " ]";
    }
    
}
