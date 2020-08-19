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
import javax.persistence.FetchType;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "permisos", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permisos.findAll", query = "SELECT p FROM Permisos p")
    , @NamedQuery(name = "Permisos.findByPermiso", query = "SELECT p FROM Permisos p WHERE p.permiso = :permiso")
    , @NamedQuery(name = "Permisos.findByArea", query = "SELECT p FROM Permisos p WHERE p.area = :area")
    , @NamedQuery(name = "Permisos.findByAreaSuperior", query = "SELECT p FROM Permisos p WHERE p.areaSuperior = :areaSuperior")
    , @NamedQuery(name = "Permisos.findByActividad", query = "SELECT p FROM Permisos p WHERE p.actividad = :actividad")
    , @NamedQuery(name = "Permisos.findByRuta", query = "SELECT p FROM Permisos p WHERE p.ruta = :ruta")
    , @NamedQuery(name = "Permisos.findByEtiqueta", query = "SELECT p FROM Permisos p WHERE p.etiqueta = :etiqueta")
    , @NamedQuery(name = "Permisos.findByAlcance", query = "SELECT p FROM Permisos p WHERE p.alcance = :alcance")
    , @NamedQuery(name = "Permisos.findByIconoPermiso", query = "SELECT p FROM Permisos p WHERE p.iconoPermiso = :iconoPermiso")
    , @NamedQuery(name = "Permisos.findByActivo", query = "SELECT p FROM Permisos p WHERE p.activo = :activo")})
public class Permisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "permiso")
    private Integer permiso;
    @Column(name = "area")
    private Short area;
    @Column(name = "areaSuperior")
    private Short areaSuperior;
    @Column(name = "actividad")
    private Integer actividad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "etiqueta")
    private String etiqueta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 82)
    @Column(name = "alcance")
    private String alcance;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "iconoPermiso")
    private String iconoPermiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria")
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonalCategorias categoria;
    @JoinColumn(name = "modulo", referencedColumnName = "modulo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MenuDinamico modulo;
    @JoinColumn(name = "clave", referencedColumnName = "clave")
    @ManyToOne(fetch = FetchType.LAZY)
    private Personal clave;

    public Permisos() {
    }

    public Permisos(Integer permiso) {
        this.permiso = permiso;
    }

    public Permisos(Integer permiso, String ruta, String etiqueta, String alcance, String iconoPermiso, boolean activo) {
        this.permiso = permiso;
        this.ruta = ruta;
        this.etiqueta = etiqueta;
        this.alcance = alcance;
        this.iconoPermiso = iconoPermiso;
        this.activo = activo;
    }

    public Integer getPermiso() {
        return permiso;
    }

    public void setPermiso(Integer permiso) {
        this.permiso = permiso;
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

    public Integer getActividad() {
        return actividad;
    }

    public void setActividad(Integer actividad) {
        this.actividad = actividad;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public String getIconoPermiso() {
        return iconoPermiso;
    }

    public void setIconoPermiso(String iconoPermiso) {
        this.iconoPermiso = iconoPermiso;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public PersonalCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(PersonalCategorias categoria) {
        this.categoria = categoria;
    }

    public MenuDinamico getModulo() {
        return modulo;
    }

    public void setModulo(MenuDinamico modulo) {
        this.modulo = modulo;
    }

    public Personal getClave() {
        return clave;
    }

    public void setClave(Personal clave) {
        this.clave = clave;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permiso != null ? permiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permisos)) {
            return false;
        }
        Permisos other = (Permisos) object;
        if ((this.permiso == null && other.permiso != null) || (this.permiso != null && !this.permiso.equals(other.permiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.Permisos[ permiso=" + permiso + " ]";
    }
    
}
