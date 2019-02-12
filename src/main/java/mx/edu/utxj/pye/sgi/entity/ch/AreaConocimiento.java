/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonny
 */
@Entity
@Table(name = "area_conocimiento", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaConocimiento.findAll", query = "SELECT a FROM AreaConocimiento a")
    , @NamedQuery(name = "AreaConocimiento.findByIdAreaConocimiento", query = "SELECT a FROM AreaConocimiento a WHERE a.idAreaConocimiento = :idAreaConocimiento")
    , @NamedQuery(name = "AreaConocimiento.findByNombre", query = "SELECT a FROM AreaConocimiento a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AreaConocimiento.findByEstatus", query = "SELECT a FROM AreaConocimiento a WHERE a.estatus = :estatus")})
public class AreaConocimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_area_conocimiento")
    private Short idAreaConocimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;

    public AreaConocimiento() {
    }

    public AreaConocimiento(Short idAreaConocimiento) {
        this.idAreaConocimiento = idAreaConocimiento;
    }

    public AreaConocimiento(Short idAreaConocimiento, String nombre, boolean estatus) {
        this.idAreaConocimiento = idAreaConocimiento;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Short getIdAreaConocimiento() {
        return idAreaConocimiento;
    }

    public void setIdAreaConocimiento(Short idAreaConocimiento) {
        this.idAreaConocimiento = idAreaConocimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAreaConocimiento != null ? idAreaConocimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreaConocimiento)) {
            return false;
        }
        AreaConocimiento other = (AreaConocimiento) object;
        if ((this.idAreaConocimiento == null && other.idAreaConocimiento != null) || (this.idAreaConocimiento != null && !this.idAreaConocimiento.equals(other.idAreaConocimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.AreaConocimiento[ idAreaConocimiento=" + idAreaConocimiento + " ]";
    }
    
}
