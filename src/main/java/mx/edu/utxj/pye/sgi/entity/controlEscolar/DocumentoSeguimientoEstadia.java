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
 * @author UTXJ
 */
@Entity
@Table(name = "documento_seguimiento_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoSeguimientoEstadia.findAll", query = "SELECT d FROM DocumentoSeguimientoEstadia d")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByDocumentoSeguimiento", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.documentoSeguimiento = :documentoSeguimiento")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByFechaCarga", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByRuta", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.ruta = :ruta")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByObservaciones", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.observaciones = :observaciones")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByValidado", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.validado = :validado")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByFechaValidacion", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.fechaValidacion = :fechaValidacion")
    , @NamedQuery(name = "DocumentoSeguimientoEstadia.findByPersonalValidacion", query = "SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.personalValidacion = :personalValidacion")})
public class DocumentoSeguimientoEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_seguimiento")
    private Integer documentoSeguimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_carga")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCarga;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ruta")
    private String ruta;
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
    @Column(name = "personal_validacion")
    private Integer personalValidacion;
    @JoinColumn(name = "documento", referencedColumnName = "documento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documento documento;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "seguimiento_estadia", referencedColumnName = "seguimiento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoEstadiaEstudiante seguimientoEstadia;

    public DocumentoSeguimientoEstadia() {
    }

    public DocumentoSeguimientoEstadia(Integer documentoSeguimiento) {
        this.documentoSeguimiento = documentoSeguimiento;
    }

    public DocumentoSeguimientoEstadia(Integer documentoSeguimiento, Date fechaCarga, String ruta, String observaciones, boolean validado) {
        this.documentoSeguimiento = documentoSeguimiento;
        this.fechaCarga = fechaCarga;
        this.ruta = ruta;
        this.observaciones = observaciones;
        this.validado = validado;
    }

    public Integer getDocumentoSeguimiento() {
        return documentoSeguimiento;
    }

    public void setDocumentoSeguimiento(Integer documentoSeguimiento) {
        this.documentoSeguimiento = documentoSeguimiento;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
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

    public Integer getPersonalValidacion() {
        return personalValidacion;
    }

    public void setPersonalValidacion(Integer personalValidacion) {
        this.personalValidacion = personalValidacion;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
    }

    public SeguimientoEstadiaEstudiante getSeguimientoEstadia() {
        return seguimientoEstadia;
    }

    public void setSeguimientoEstadia(SeguimientoEstadiaEstudiante seguimientoEstadia) {
        this.seguimientoEstadia = seguimientoEstadia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoSeguimiento != null ? documentoSeguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoSeguimientoEstadia)) {
            return false;
        }
        DocumentoSeguimientoEstadia other = (DocumentoSeguimientoEstadia) object;
        if ((this.documentoSeguimiento == null && other.documentoSeguimiento != null) || (this.documentoSeguimiento != null && !this.documentoSeguimiento.equals(other.documentoSeguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia[ documentoSeguimiento=" + documentoSeguimiento + " ]";
    }
    
}
