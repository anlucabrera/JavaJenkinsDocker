/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

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
@Table(name = "tipo_baja", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoBaja.findAll", query = "SELECT t FROM TipoBaja t")
    , @NamedQuery(name = "TipoBaja.findByIdTipoBaja", query = "SELECT t FROM TipoBaja t WHERE t.idTipoBaja = :idTipoBaja")
    , @NamedQuery(name = "TipoBaja.findByDescripcion", query = "SELECT t FROM TipoBaja t WHERE t.descripcion = :descripcion")})
public class TipoBaja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_tipo_baja")
    private Short idTipoBaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoBaja")
    private List<Baja> bajaList;

    public TipoBaja() {
    }

    public TipoBaja(Short idTipoBaja) {
        this.idTipoBaja = idTipoBaja;
    }

    public TipoBaja(Short idTipoBaja, String descripcion) {
        this.idTipoBaja = idTipoBaja;
        this.descripcion = descripcion;
    }

    public Short getIdTipoBaja() {
        return idTipoBaja;
    }

    public void setIdTipoBaja(Short idTipoBaja) {
        this.idTipoBaja = idTipoBaja;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Baja> getBajaList() {
        return bajaList;
    }

    public void setBajaList(List<Baja> bajaList) {
        this.bajaList = bajaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoBaja != null ? idTipoBaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoBaja)) {
            return false;
        }
        TipoBaja other = (TipoBaja) object;
        if ((this.idTipoBaja == null && other.idTipoBaja != null) || (this.idTipoBaja != null && !this.idTipoBaja.equals(other.idTipoBaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoBaja[ idTipoBaja=" + idTipoBaja + " ]";
    }
    
}
