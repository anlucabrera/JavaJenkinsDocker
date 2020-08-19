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
import javax.persistence.FetchType;
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
 * @author Desarrollo
 */
@Entity
@Table(name = "documento", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Documento.findAll", query = "SELECT d FROM Documento d")
    , @NamedQuery(name = "Documento.findByDocumento", query = "SELECT d FROM Documento d WHERE d.documento = :documento")
    , @NamedQuery(name = "Documento.findByDescripcion", query = "SELECT d FROM Documento d WHERE d.descripcion = :descripcion")
    , @NamedQuery(name = "Documento.findByEspecificaciones", query = "SELECT d FROM Documento d WHERE d.especificaciones = :especificaciones")
    , @NamedQuery(name = "Documento.findByNomenclatura", query = "SELECT d FROM Documento d WHERE d.nomenclatura = :nomenclatura")
    , @NamedQuery(name = "Documento.findByActivo", query = "SELECT d FROM Documento d WHERE d.activo = :activo")})
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento")
    private Integer documento;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento", fetch = FetchType.LAZY)
    private List<DocumentoAspiranteProceso> documentoAspiranteProcesoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento", fetch = FetchType.LAZY)
    private List<DocumentoExpedienteTitulacion> documentoExpedienteTitulacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documento", fetch = FetchType.LAZY)
    private List<DocumentoProceso> documentoProcesoList;

    public Documento() {
    }

    public Documento(Integer documento) {
        this.documento = documento;
    }

    public Documento(Integer documento, String descripcion, String especificaciones, String nomenclatura, boolean activo) {
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
    public List<DocumentoAspiranteProceso> getDocumentoAspiranteProcesoList() {
        return documentoAspiranteProcesoList;
    }

    public void setDocumentoAspiranteProcesoList(List<DocumentoAspiranteProceso> documentoAspiranteProcesoList) {
        this.documentoAspiranteProcesoList = documentoAspiranteProcesoList;
    }

    @XmlTransient
    public List<DocumentoExpedienteTitulacion> getDocumentoExpedienteTitulacionList() {
        return documentoExpedienteTitulacionList;
    }

    public void setDocumentoExpedienteTitulacionList(List<DocumentoExpedienteTitulacion> documentoExpedienteTitulacionList) {
        this.documentoExpedienteTitulacionList = documentoExpedienteTitulacionList;
    }

    @XmlTransient
    public List<DocumentoProceso> getDocumentoProcesoList() {
        return documentoProcesoList;
    }

    public void setDocumentoProcesoList(List<DocumentoProceso> documentoProcesoList) {
        this.documentoProcesoList = documentoProcesoList;
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
        if (!(object instanceof Documento)) {
            return false;
        }
        Documento other = (Documento) object;
        if ((this.documento == null && other.documento != null) || (this.documento != null && !this.documento.equals(other.documento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento[ documento=" + documento + " ]";
    }
    
}
