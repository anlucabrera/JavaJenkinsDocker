/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "documentos_expediente", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosExpediente.findAll", query = "SELECT d FROM DocumentosExpediente d")
    , @NamedQuery(name = "DocumentosExpediente.findByDocExpediente", query = "SELECT d FROM DocumentosExpediente d WHERE d.docExpediente = :docExpediente")
    , @NamedQuery(name = "DocumentosExpediente.findByRuta", query = "SELECT d FROM DocumentosExpediente d WHERE d.ruta = :ruta")
    , @NamedQuery(name = "DocumentosExpediente.findByFechaCarga", query = "SELECT d FROM DocumentosExpediente d WHERE d.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "DocumentosExpediente.findByValidadoTitulacion", query = "SELECT d FROM DocumentosExpediente d WHERE d.validadoTitulacion = :validadoTitulacion")
    , @NamedQuery(name = "DocumentosExpediente.findByObservaciones", query = "SELECT d FROM DocumentosExpediente d WHERE d.observaciones = :observaciones")
    , @NamedQuery(name = "DocumentosExpediente.findByFechaValidacion", query = "SELECT d FROM DocumentosExpediente d WHERE d.fechaValidacion = :fechaValidacion")})
public class DocumentosExpediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "doc_expediente")
    private Integer docExpediente;
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
    @Column(name = "validado_titulacion")
    private boolean validadoTitulacion;
    @Size(max = 255)
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.DATE)
    private Date fechaValidacion;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false)
    private Documentos documento;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false)
    private ExpedientesTitulacion expediente;

    public DocumentosExpediente() {
    }

    public DocumentosExpediente(Integer docExpediente) {
        this.docExpediente = docExpediente;
    }

    public DocumentosExpediente(Integer docExpediente, String ruta, Date fechaCarga, boolean validadoTitulacion) {
        this.docExpediente = docExpediente;
        this.ruta = ruta;
        this.fechaCarga = fechaCarga;
        this.validadoTitulacion = validadoTitulacion;
    }

    public Integer getDocExpediente() {
        return docExpediente;
    }

    public void setDocExpediente(Integer docExpediente) {
        this.docExpediente = docExpediente;
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

    public boolean getValidadoTitulacion() {
        return validadoTitulacion;
    }

    public void setValidadoTitulacion(boolean validadoTitulacion) {
        this.validadoTitulacion = validadoTitulacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Documentos getDocumento() {
        return documento;
    }

    public void setDocumento(Documentos documento) {
        this.documento = documento;
    }

    public ExpedientesTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedientesTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docExpediente != null ? docExpediente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosExpediente)) {
            return false;
        }
        DocumentosExpediente other = (DocumentosExpediente) object;
        if ((this.docExpediente == null && other.docExpediente != null) || (this.docExpediente != null && !this.docExpediente.equals(other.docExpediente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente[ docExpediente=" + docExpediente + " ]";
    }
    
}
