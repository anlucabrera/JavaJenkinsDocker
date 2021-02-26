/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "documento_aspirante_proceso", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoAspiranteProceso.findAll", query = "SELECT d FROM DocumentoAspiranteProceso d")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByDocumentoAspirante", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.documentoAspirante = :documentoAspirante")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByRuta", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.ruta = :ruta")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByFechaCarga", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByObservaciones", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.observaciones = :observaciones")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByValidado", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.validado = :validado")
    , @NamedQuery(name = "DocumentoAspiranteProceso.findByFechaValidacion", query = "SELECT d FROM DocumentoAspiranteProceso d WHERE d.fechaValidacion = :fechaValidacion")})
public class DocumentoAspiranteProceso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_aspirante")
    private Integer documentoAspirante;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_carga")
    @Temporal(TemporalType.DATE)
    private Date fechaCarga;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.DATE)
    private Date fechaValidacion;
    @JoinColumn(name = "aspirante", referencedColumnName = "id_aspirante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Aspirante aspirante;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documento documento;

    public DocumentoAspiranteProceso() {
    }

    public DocumentoAspiranteProceso(Integer documentoAspirante) {
        this.documentoAspirante = documentoAspirante;
    }

    public DocumentoAspiranteProceso(Integer documentoAspirante, String ruta, Date fechaCarga, String observaciones, boolean validado) {
        this.documentoAspirante = documentoAspirante;
        this.ruta = ruta;
        this.fechaCarga = fechaCarga;
        this.observaciones = observaciones;
        this.validado = validado;
    }

    public Integer getDocumentoAspirante() {
        return documentoAspirante;
    }

    public void setDocumentoAspirante(Integer documentoAspirante) {
        this.documentoAspirante = documentoAspirante;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean getValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Aspirante getAspirante() {
        return aspirante;
    }

    public void setAspirante(Aspirante aspirante) {
        this.aspirante = aspirante;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoAspirante != null ? documentoAspirante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoAspiranteProceso)) {
            return false;
        }
        DocumentoAspiranteProceso other = (DocumentoAspiranteProceso) object;
        if ((this.documentoAspirante == null && other.documentoAspirante != null) || (this.documentoAspirante != null && !this.documentoAspirante.equals(other.documentoAspirante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso[ documentoAspirante=" + documentoAspirante + " ]";
    }
    
}
