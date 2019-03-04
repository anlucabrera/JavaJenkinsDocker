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
@Table(name = "reconocimiento_prodep_tipos_apoyo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReconocimientoProdepTiposApoyo.findAll", query = "SELECT r FROM ReconocimientoProdepTiposApoyo r")
    , @NamedQuery(name = "ReconocimientoProdepTiposApoyo.findByTipo", query = "SELECT r FROM ReconocimientoProdepTiposApoyo r WHERE r.tipo = :tipo")
    , @NamedQuery(name = "ReconocimientoProdepTiposApoyo.findByDescripcion", query = "SELECT r FROM ReconocimientoProdepTiposApoyo r WHERE r.descripcion = :descripcion")})
public class ReconocimientoProdepTiposApoyo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tipo")
    private Short tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoApoyo")
    private List<ReconocimientoProdepRegistros> reconocimientoProdepRegistrosList;

    public ReconocimientoProdepTiposApoyo() {
    }

    public ReconocimientoProdepTiposApoyo(Short tipo) {
        this.tipo = tipo;
    }

    public ReconocimientoProdepTiposApoyo(Short tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<ReconocimientoProdepRegistros> getReconocimientoProdepRegistrosList() {
        return reconocimientoProdepRegistrosList;
    }

    public void setReconocimientoProdepRegistrosList(List<ReconocimientoProdepRegistros> reconocimientoProdepRegistrosList) {
        this.reconocimientoProdepRegistrosList = reconocimientoProdepRegistrosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipo != null ? tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReconocimientoProdepTiposApoyo)) {
            return false;
        }
        ReconocimientoProdepTiposApoyo other = (ReconocimientoProdepTiposApoyo) object;
        if ((this.tipo == null && other.tipo != null) || (this.tipo != null && !this.tipo.equals(other.tipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ReconocimientoProdepTiposApoyo[ tipo=" + tipo + " ]";
    }
    
}
