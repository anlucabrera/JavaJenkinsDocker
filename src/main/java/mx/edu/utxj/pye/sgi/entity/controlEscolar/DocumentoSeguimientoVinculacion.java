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
@Table(name = "documento_seguimiento_vinculacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoSeguimientoVinculacion.findAll", query = "SELECT d FROM DocumentoSeguimientoVinculacion d")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByDocumentoVinculacion", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.documentoVinculacion = :documentoVinculacion")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByRuta", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.ruta = :ruta")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByFechaCarga", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.fechaCarga = :fechaCarga")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByObservaciones", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.observaciones = :observaciones")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByValidado", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.validado = :validado")
    , @NamedQuery(name = "DocumentoSeguimientoVinculacion.findByFechaValidacion", query = "SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.fechaValidacion = :fechaValidacion")})
public class DocumentoSeguimientoVinculacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "documento_vinculacion")
    private Integer documentoVinculacion;
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
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoVinculacion evento;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento_vinculacion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoVinculacionEstudiante seguimiento;

    public DocumentoSeguimientoVinculacion() {
    }

    public DocumentoSeguimientoVinculacion(Integer documentoVinculacion) {
        this.documentoVinculacion = documentoVinculacion;
    }

    public DocumentoSeguimientoVinculacion(Integer documentoVinculacion, String ruta, Date fechaCarga, String observaciones, boolean validado) {
        this.documentoVinculacion = documentoVinculacion;
        this.ruta = ruta;
        this.fechaCarga = fechaCarga;
        this.observaciones = observaciones;
        this.validado = validado;
    }

    public Integer getDocumentoVinculacion() {
        return documentoVinculacion;
    }

    public void setDocumentoVinculacion(Integer documentoVinculacion) {
        this.documentoVinculacion = documentoVinculacion;
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

    public EventoVinculacion getEvento() {
        return evento;
    }

    public void setEvento(EventoVinculacion evento) {
        this.evento = evento;
    }

    public SeguimientoVinculacionEstudiante getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(SeguimientoVinculacionEstudiante seguimiento) {
        this.seguimiento = seguimiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentoVinculacion != null ? documentoVinculacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentoSeguimientoVinculacion)) {
            return false;
        }
        DocumentoSeguimientoVinculacion other = (DocumentoSeguimientoVinculacion) object;
        if ((this.documentoVinculacion == null && other.documentoVinculacion != null) || (this.documentoVinculacion != null && !this.documentoVinculacion.equals(other.documentoVinculacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion[ documentoVinculacion=" + documentoVinculacion + " ]";
    }
    
}
