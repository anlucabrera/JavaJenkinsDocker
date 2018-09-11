/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

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
 * @author Planeacion
 */
@Entity
@Table(name = "areas_universidad", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreasUniversidad.findAll", query = "SELECT a FROM AreasUniversidad a")
    , @NamedQuery(name = "AreasUniversidad.findByArea", query = "SELECT a FROM AreasUniversidad a WHERE a.area = :area")
    , @NamedQuery(name = "AreasUniversidad.findByAreaSuperior", query = "SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior")
    , @NamedQuery(name = "AreasUniversidad.findByNombre", query = "SELECT a FROM AreasUniversidad a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "AreasUniversidad.findBySiglas", query = "SELECT a FROM AreasUniversidad a WHERE a.siglas = :siglas")
    , @NamedQuery(name = "AreasUniversidad.findByResponsable", query = "SELECT a FROM AreasUniversidad a WHERE a.responsable = :responsable")
    , @NamedQuery(name = "AreasUniversidad.findByVigente", query = "SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente")})
public class AreasUniversidad implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "siglas")
    private String siglas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "vigente")
    private String vigente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tiene_poa")
    private boolean tienePoa;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "area")
    private Short area;
    @Column(name = "area_superior")
    private Short areaSuperior;
    @Column(name = "responsable")
    private Integer responsable;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria")
    @ManyToOne(optional = false)
    private Categorias categoria;

    public AreasUniversidad() {
    }

    public AreasUniversidad(Short area) {
        this.area = area;
    }

    public AreasUniversidad(Short area, String nombre, String siglas, String vigente) {
        this.area = area;
        this.nombre = nombre;
        this.siglas = siglas;
        this.vigente = vigente;
    }

    public Short getArea() {
        return area;
    }

    public void setArea(Short area) {
        this.area = area;
    }

    public Short getAreaSuperior() {
        return areaSuperior;
    }

    public void setAreaSuperior(Short areaSuperior) {
        this.areaSuperior = areaSuperior;
    }


    public Integer getResponsable() {
        return responsable;
    }

    public void setResponsable(Integer responsable) {
        this.responsable = responsable;
    }


    public Categorias getCategoria() {
        return categoria;
    }

    public void setCategoria(Categorias categoria) {
        this.categoria = categoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (area != null ? area.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AreasUniversidad)) {
            return false;
        }
        AreasUniversidad other = (AreasUniversidad) object;
        if ((this.area == null && other.area != null) || (this.area != null && !this.area.equals(other.area))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.ejb.prontuario.AreasUniversidad[ area=" + area + " ]";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getVigente() {
        return vigente;
    }

    public void setVigente(String vigente) {
        this.vigente = vigente;
    }

    public boolean getTienePoa() {
        return tienePoa;
    }

    public void setTienePoa(boolean tienePoa) {
        this.tienePoa = tienePoa;
    }
    
}
