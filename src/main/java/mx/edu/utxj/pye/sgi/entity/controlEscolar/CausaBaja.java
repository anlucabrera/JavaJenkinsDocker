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
@Table(name = "causa_baja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CausaBaja.findAll", query = "SELECT c FROM CausaBaja c")
    , @NamedQuery(name = "CausaBaja.findByIdCausaBaja", query = "SELECT c FROM CausaBaja c WHERE c.idCausaBaja = :idCausaBaja")
    , @NamedQuery(name = "CausaBaja.findByNombre", query = "SELECT c FROM CausaBaja c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "CausaBaja.findByDescripcion", query = "SELECT c FROM CausaBaja c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "CausaBaja.findByEstatus", query = "SELECT c FROM CausaBaja c WHERE c.estatus = :estatus")})
public class CausaBaja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_causa_baja")
    private Short idCausaBaja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "causaBaja")
    private List<Baja> bajaList;

    public CausaBaja() {
    }

    public CausaBaja(Short idCausaBaja) {
        this.idCausaBaja = idCausaBaja;
    }

    public CausaBaja(Short idCausaBaja, String nombre, String descripcion, boolean estatus) {
        this.idCausaBaja = idCausaBaja;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estatus = estatus;
    }

    public Short getIdCausaBaja() {
        return idCausaBaja;
    }

    public void setIdCausaBaja(Short idCausaBaja) {
        this.idCausaBaja = idCausaBaja;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
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
        hash += (idCausaBaja != null ? idCausaBaja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CausaBaja)) {
            return false;
        }
        CausaBaja other = (CausaBaja) object;
        if ((this.idCausaBaja == null && other.idCausaBaja != null) || (this.idCausaBaja != null && !this.idCausaBaja.equals(other.idCausaBaja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CausaBaja[ idCausaBaja=" + idCausaBaja + " ]";
    }
    
}
