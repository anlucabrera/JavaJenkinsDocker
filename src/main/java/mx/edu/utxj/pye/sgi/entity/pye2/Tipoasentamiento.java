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
@Table(name = "tipoasentamiento", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoasentamiento.findAll", query = "SELECT t FROM Tipoasentamiento t")
    , @NamedQuery(name = "Tipoasentamiento.findByTipoAsentamiento", query = "SELECT t FROM Tipoasentamiento t WHERE t.tipoAsentamiento = :tipoAsentamiento")
    , @NamedQuery(name = "Tipoasentamiento.findByNombre", query = "SELECT t FROM Tipoasentamiento t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Tipoasentamiento.findByAbreviatura", query = "SELECT t FROM Tipoasentamiento t WHERE t.abreviatura = :abreviatura")
    , @NamedQuery(name = "Tipoasentamiento.findByActivo", query = "SELECT t FROM Tipoasentamiento t WHERE t.activo = :activo")})
public class Tipoasentamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipoAsentamiento")
    private Integer tipoAsentamiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAsentamiento")
    private List<Codigopostal> codigopostalList;

    public Tipoasentamiento() {
    }

    public Tipoasentamiento(Integer tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public Tipoasentamiento(Integer tipoAsentamiento, String nombre, String abreviatura, boolean activo) {
        this.tipoAsentamiento = tipoAsentamiento;
        this.nombre = nombre;
        this.abreviatura = abreviatura;
        this.activo = activo;
    }

    public Integer getTipoAsentamiento() {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(Integer tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<Codigopostal> getCodigopostalList() {
        return codigopostalList;
    }

    public void setCodigopostalList(List<Codigopostal> codigopostalList) {
        this.codigopostalList = codigopostalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoAsentamiento != null ? tipoAsentamiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoasentamiento)) {
            return false;
        }
        Tipoasentamiento other = (Tipoasentamiento) object;
        if ((this.tipoAsentamiento == null && other.tipoAsentamiento != null) || (this.tipoAsentamiento != null && !this.tipoAsentamiento.equals(other.tipoAsentamiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Tipoasentamiento[ tipoAsentamiento=" + tipoAsentamiento + " ]";
    }
    
}
