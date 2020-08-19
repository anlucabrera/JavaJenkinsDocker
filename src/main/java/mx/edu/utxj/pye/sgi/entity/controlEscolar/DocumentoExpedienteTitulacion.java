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
@Table(name = "documento_expediente_titulacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoExpedienteTitulacion.findAll", query = "SELECT d FROM DocumentoExpedienteTitulacion d")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByDocumentoExpediente", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.documentoExpediente = :documentoExpediente")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByRuta", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.ruta = :ruta")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByFechaCarga", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByObservaciones", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.observaciones = :observaciones")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByValidado", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.validado = :validado")
    , @NamedQuery(name = "DocumentoExpedienteTitulacion.findByFechaValidacion", query = "SELECT d FROM DocumentoExpedienteTitulacion d WHERE d.fechaValidacion = :fechaValidacion")})
public class DocumentoExpedienteTitulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_expediente")
    private Integer documentoExpediente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_carga")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validado")
    private boolean validado;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documento documento;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExpedienteTitulacion expediente;

    public DocumentoExpedienteTitulacion() {
    }

    public DocumentoExpedienteTitulacion(Integer documentoExpediente) {
        this.documentoExpediente = documentoExpediente;
    }

    public DocumentoExpedienteTitulacion(Integer documentoExpediente, String ruta, Date fechaCarga, String observaciones, boolean validado) {
        this.documentoExpediente = documentoExpediente;
        this.ruta = ruta;
        this.fechaCarga = fechaCarga;
        this.observaciones = observaciones;
        this.validado = validado;
    }

    public Integer getDocumentoExpediente() {
        return documentoExpediente;
    }

    public void setDocumentoExpediente(Integer documentoExpediente) {
        this.documentoExpediente = documentoExpediente;
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

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public ExpedienteTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedienteTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoExpediente != null ? documentoExpediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoExpedienteTitulacion)) {
            return false;
        }
        DocumentoExpedienteTitulacion other = (DocumentoExpedienteTitulacion) object;
        if ((this.documentoExpediente == null && other.documentoExpediente != null) || (this.documentoExpediente != null && !this.documentoExpediente.equals(other.documentoExpediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion[ documentoExpediente=" + documentoExpediente + " ]";
    }
    
}
