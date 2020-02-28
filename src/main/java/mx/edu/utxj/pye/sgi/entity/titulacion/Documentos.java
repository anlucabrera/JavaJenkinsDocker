/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

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
@Table(name = "documentos", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documentos.findAll", query = "SELECT d FROM Documentos d")
    , @NamedQuery(name = "Documentos.findByDocumento", query = "SELECT d FROM Documentos d WHERE d.documento = :documento")
    , @NamedQuery(name = "Documentos.findByDescripcion", query = "SELECT d FROM Documentos d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "Documentos.findByEspecificaciones", query = "SELECT d FROM Documentos d WHERE d.especificaciones = :especificaciones")
    , @NamedQuery(name = "Documentos.findByNomenclatura", query = "SELECT d FROM Documentos d WHERE d.nomenclatura = :nomenclatura")
    , @NamedQuery(name = "Documentos.findByActivo", query = "SELECT d FROM Documentos d WHERE d.activo = :activo")})
public class Documentos implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "especificaciones")
    private String especificaciones;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nomenclatura")
    private String nomenclatura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private List<TituloExpediente> tituloExpedienteList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento")
    private Integer documento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento")
    private List<DocumentosExpediente> documentosExpedienteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documentos")
    private List<DocumentosNivel> documentosNivelList;

    public Documentos() {
    }

    public Documentos(Integer documento) {
        this.documento = documento;
    }

    public Documentos(Integer documento, String descripcion, String especificaciones, String nomenclatura, boolean activo) {
        this.documento = documento;
        this.descripcion = descripcion;
        this.especificaciones = especificaciones;
        this.nomenclatura = nomenclatura;
        this.activo = activo;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }


    @XmlTransient
    public List<DocumentosExpediente> getDocumentosExpedienteList() {
        return documentosExpedienteList;
    }

    public void setDocumentosExpedienteList(List<DocumentosExpediente> documentosExpedienteList) {
        this.documentosExpedienteList = documentosExpedienteList;
    }

    @XmlTransient
    public List<DocumentosNivel> getDocumentosNivelList() {
        return documentosNivelList;
    }

    public void setDocumentosNivelList(List<DocumentosNivel> documentosNivelList) {
        this.documentosNivelList = documentosNivelList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documento != null ? documento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documentos)) {
            return false;
        }
        Documentos other = (Documentos) object;
        if ((this.documento == null && other.documento != null) || (this.documento != null && !this.documento.equals(other.documento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.Documentos[ documento=" + documento + " ]";
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getNomenclatura() {
        return nomenclatura;
    }

    public void setNomenclatura(String nomenclatura) {
        this.nomenclatura = nomenclatura;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<TituloExpediente> getTituloExpedienteList() {
        return tituloExpedienteList;
    }

    public void setTituloExpedienteList(List<TituloExpediente> tituloExpedienteList) {
        this.tituloExpedienteList = tituloExpedienteList;
    }
    
}
