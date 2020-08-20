/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "bajas_tipo", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BajasTipo.findAll", query = "SELECT b FROM BajasTipo b")
    , @NamedQuery(name = "BajasTipo.findByTipoBaja", query = "SELECT b FROM BajasTipo b WHERE b.tipoBaja = :tipoBaja")
    , @NamedQuery(name = "BajasTipo.findByDescripcion", query = "SELECT b FROM BajasTipo b WHERE b.descripcion = :descripcion")})
public class BajasTipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo_baja")
    private Integer tipoBaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoBaja", fetch = FetchType.LAZY)
    private List<DesercionPorEstudiante> desercionPorEstudianteList;

    public BajasTipo() {
    }

    public BajasTipo(Integer tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public BajasTipo(Integer tipoBaja, String descripcion) {
        this.tipoBaja = tipoBaja;
        this.descripcion = descripcion;
    }

    public Integer getTipoBaja() {
        return tipoBaja;
    }

    public void setTipoBaja(Integer tipoBaja) {
        this.tipoBaja = tipoBaja;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<DesercionPorEstudiante> getDesercionPorEstudianteList() {
        return desercionPorEstudianteList;
    }

    public void setDesercionPorEstudianteList(List<DesercionPorEstudiante> desercionPorEstudianteList) {
        this.desercionPorEstudianteList = desercionPorEstudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoBaja != null ? tipoBaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BajasTipo)) {
            return false;
        }
        BajasTipo other = (BajasTipo) object;
        if ((this.tipoBaja == null && other.tipoBaja != null) || (this.tipoBaja != null && !this.tipoBaja.equals(other.tipoBaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo[ tipoBaja=" + tipoBaja + " ]";
    }
    
}
