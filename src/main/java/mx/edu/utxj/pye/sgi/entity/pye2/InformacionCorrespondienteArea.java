/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
 * @author UTXJ
 */
@Entity
@Table(name = "informacion_correspondiente_area", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InformacionCorrespondienteArea.findAll", query = "SELECT i FROM InformacionCorrespondienteArea i")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByInformacionCorrespondiente", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.informacionCorrespondiente = :informacionCorrespondiente")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByClase", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.clase = :clase")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByTamanioFuente", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.tamanioFuente = :tamanioFuente")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByTituloPrincipal", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.tituloPrincipal = :tituloPrincipal")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByTipo", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.tipo = :tipo")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByIcono", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.icono = :icono")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByLink", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.link = :link")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByDescripcionLink", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.descripcionLink = :descripcionLink")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByEstado", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.estado = :estado")
    , @NamedQuery(name = "InformacionCorrespondienteArea.findByArea", query = "SELECT i FROM InformacionCorrespondienteArea i WHERE i.area = :area")})
public class InformacionCorrespondienteArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "informacion_correspondiente")
    private Short informacionCorrespondiente;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "area")
    private short area;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EjesRegistro eje;

    public InformacionCorrespondienteArea() {
    }

    public InformacionCorrespondienteArea(Short informacionCorrespondiente) {
        this.informacionCorrespondiente = informacionCorrespondiente;
    }

    public InformacionCorrespondienteArea(Short informacionCorrespondiente, String clase, short tamanioFuente, String tituloPrincipal, String tipo, String icono, String link, String descripcionLink, String estado, short area) {
        this.informacionCorrespondiente = informacionCorrespondiente;
        this.clase = clase;
        this.tamanioFuente = tamanioFuente;
        this.tituloPrincipal = tituloPrincipal;
        this.tipo = tipo;
        this.icono = icono;
        this.link = link;
        this.descripcionLink = descripcionLink;
        this.estado = estado;
        this.area = area;
    }

    public Short getInformacionCorrespondiente() {
        return informacionCorrespondiente;
    }

    public void setInformacionCorrespondiente(Short informacionCorrespondiente) {
        this.informacionCorrespondiente = informacionCorrespondiente;
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

    public short getArea() {
        return area;
    }

    public void setArea(short area) {
        this.area = area;
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
        hash += (informacionCorrespondiente != null ? informacionCorrespondiente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InformacionCorrespondienteArea)) {
            return false;
        }
        InformacionCorrespondienteArea other = (InformacionCorrespondienteArea) object;
        if ((this.informacionCorrespondiente == null && other.informacionCorrespondiente != null) || (this.informacionCorrespondiente != null && !this.informacionCorrespondiente.equals(other.informacionCorrespondiente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.InformacionCorrespondienteArea[ informacionCorrespondiente=" + informacionCorrespondiente + " ]";
    }
    
}
