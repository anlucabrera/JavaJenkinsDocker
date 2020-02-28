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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "modulos_registro", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModulosRegistro.findAll", query = "SELECT m FROM ModulosRegistro m")
    , @NamedQuery(name = "ModulosRegistro.findByClave", query = "SELECT m FROM ModulosRegistro m WHERE m.clave = :clave")
    , @NamedQuery(name = "ModulosRegistro.findByClase", query = "SELECT m FROM ModulosRegistro m WHERE m.clase = :clase")
    , @NamedQuery(name = "ModulosRegistro.findByTamanioFuente", query = "SELECT m FROM ModulosRegistro m WHERE m.tamanioFuente = :tamanioFuente")
    , @NamedQuery(name = "ModulosRegistro.findByTituloPrincipal", query = "SELECT m FROM ModulosRegistro m WHERE m.tituloPrincipal = :tituloPrincipal")
    , @NamedQuery(name = "ModulosRegistro.findByTipo", query = "SELECT m FROM ModulosRegistro m WHERE m.tipo = :tipo")
    , @NamedQuery(name = "ModulosRegistro.findByIcono", query = "SELECT m FROM ModulosRegistro m WHERE m.icono = :icono")
    , @NamedQuery(name = "ModulosRegistro.findByLink", query = "SELECT m FROM ModulosRegistro m WHERE m.link = :link")
    , @NamedQuery(name = "ModulosRegistro.findByDescripcionLink", query = "SELECT m FROM ModulosRegistro m WHERE m.descripcionLink = :descripcionLink")
    , @NamedQuery(name = "ModulosRegistro.findByEstado", query = "SELECT m FROM ModulosRegistro m WHERE m.estado = :estado")})
public class ModulosRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "clave")
    private Short clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "clase")
    private String clase;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tamanio_fuente")
    private short tamanioFuente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "titulo_principal")
    private String tituloPrincipal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "icono")
    private String icono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "link")
    private String link;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "descripcion_link")
    private String descripcionLink;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modulosRegistro")
    private List<ModulosRegistroEspecifico> modulosRegistroEspecificoList;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false)
    private EjesRegistro eje;

    public ModulosRegistro() {
    }

    public ModulosRegistro(Short clave) {
        this.clave = clave;
    }

    public ModulosRegistro(Short clave, String clase, short tamanioFuente, String tituloPrincipal, String tipo, String icono, String link, String descripcionLink, String estado) {
        this.clave = clave;
        this.clase = clase;
        this.tamanioFuente = tamanioFuente;
        this.tituloPrincipal = tituloPrincipal;
        this.tipo = tipo;
        this.icono = icono;
        this.link = link;
        this.descripcionLink = descripcionLink;
        this.estado = estado;
    }

    public Short getClave() {
        return clave;
    }

    public void setClave(Short clave) {
        this.clave = clave;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public short getTamanioFuente() {
        return tamanioFuente;
    }

    public void setTamanioFuente(short tamanioFuente) {
        this.tamanioFuente = tamanioFuente;
    }

    public String getTituloPrincipal() {
        return tituloPrincipal;
    }

    public void setTituloPrincipal(String tituloPrincipal) {
        this.tituloPrincipal = tituloPrincipal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescripcionLink() {
        return descripcionLink;
    }

    public void setDescripcionLink(String descripcionLink) {
        this.descripcionLink = descripcionLink;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<ModulosRegistroEspecifico> getModulosRegistroEspecificoList() {
        return modulosRegistroEspecificoList;
    }

    public void setModulosRegistroEspecificoList(List<ModulosRegistroEspecifico> modulosRegistroEspecificoList) {
        this.modulosRegistroEspecificoList = modulosRegistroEspecificoList;
    }

    public EjesRegistro getEje() {
        return eje;
    }

    public void setEje(EjesRegistro eje) {
        this.eje = eje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModulosRegistro)) {
            return false;
        }
        ModulosRegistro other = (ModulosRegistro) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistro[ clave=" + clave + " ]";
    }
    
}
