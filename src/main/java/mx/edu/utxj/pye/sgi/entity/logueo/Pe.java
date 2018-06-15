/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.logueo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "pe", catalog = "logueo", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pe.findAll", query = "SELECT p FROM Pe p")
    , @NamedQuery(name = "Pe.findByIdpe", query = "SELECT p FROM Pe p WHERE p.idpe = :idpe")
    , @NamedQuery(name = "Pe.findByDenominacion", query = "SELECT p FROM Pe p WHERE p.denominacion = :denominacion")
    , @NamedQuery(name = "Pe.findByEstatus", query = "SELECT p FROM Pe p WHERE p.estatus = :estatus")
    , @NamedQuery(name = "Pe.findByTipo", query = "SELECT p FROM Pe p WHERE p.tipo = :tipo")
    , @NamedQuery(name = "Pe.findBySiglas", query = "SELECT p FROM Pe p WHERE p.siglas = :siglas")})
public class Pe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpe")
    private Integer idpe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "denominacion")
    private String denominacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "estatus")
    private String estatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "siglas")
    private String siglas;
    @JoinColumn(name = "claveArea", referencedColumnName = "idareas")
    @ManyToOne(optional = false)
    private Areas claveArea;

    public Pe() {
    }

    public Pe(Integer idpe) {
        this.idpe = idpe;
    }

    public Pe(Integer idpe, String denominacion, String estatus, String tipo, String siglas) {
        this.idpe = idpe;
        this.denominacion = denominacion;
        this.estatus = estatus;
        this.tipo = tipo;
        this.siglas = siglas;
    }

    public Integer getIdpe() {
        return idpe;
    }

    public void setIdpe(Integer idpe) {
        this.idpe = idpe;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public Areas getClaveArea() {
        return claveArea;
    }

    public void setClaveArea(Areas claveArea) {
        this.claveArea = claveArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpe != null ? idpe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pe)) {
            return false;
        }
        Pe other = (Pe) object;
        if ((this.idpe == null && other.idpe != null) || (this.idpe != null && !this.idpe.equals(other.idpe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.logueo.Pe[ idpe=" + idpe + " ]";
    }
    
}
