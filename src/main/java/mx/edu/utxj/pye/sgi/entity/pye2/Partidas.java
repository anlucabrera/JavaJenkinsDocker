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
@Table(name = "partidas", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partidas.findAll", query = "SELECT p FROM Partidas p")
    , @NamedQuery(name = "Partidas.findByPartida", query = "SELECT p FROM Partidas p WHERE p.partida = :partida")
    , @NamedQuery(name = "Partidas.findByDenominacion", query = "SELECT p FROM Partidas p WHERE p.denominacion = :denominacion")})
public class Partidas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "partida")
    private Short partida;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "denominacion")
    private String denominacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partida")
    private List<ProductosAreas> productosAreasList;

    public Partidas() {
    }

    public Partidas(Short partida) {
        this.partida = partida;
    }

    public Partidas(Short partida, String denominacion) {
        this.partida = partida;
        this.denominacion = denominacion;
    }

    public Short getPartida() {
        return partida;
    }

    public void setPartida(Short partida) {
        this.partida = partida;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
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
        hash += (partida != null ? partida.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partidas)) {
            return false;
        }
        Partidas other = (Partidas) object;
        if ((this.partida == null && other.partida != null) || (this.partida != null && !this.partida.equals(other.partida))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Partidas[ partida=" + partida + " ]";
    }
    
}
