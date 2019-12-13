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
@Table(name = "prestamos_documentos", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrestamosDocumentos.findAll", query = "SELECT p FROM PrestamosDocumentos p")
    , @NamedQuery(name = "PrestamosDocumentos.findByPrestamoDocumento", query = "SELECT p FROM PrestamosDocumentos p WHERE p.prestamoDocumento = :prestamoDocumento")
    , @NamedQuery(name = "PrestamosDocumentos.findByTipoPrestamo", query = "SELECT p FROM PrestamosDocumentos p WHERE p.tipoPrestamo = :tipoPrestamo")
    , @NamedQuery(name = "PrestamosDocumentos.findByTipoDocumento", query = "SELECT p FROM PrestamosDocumentos p WHERE p.tipoDocumento = :tipoDocumento")
    , @NamedQuery(name = "PrestamosDocumentos.findByFechaPrestamo", query = "SELECT p FROM PrestamosDocumentos p WHERE p.fechaPrestamo = :fechaPrestamo")
    , @NamedQuery(name = "PrestamosDocumentos.findByFechaDevolucionEstudiante", query = "SELECT p FROM PrestamosDocumentos p WHERE p.fechaDevolucionEstudiante = :fechaDevolucionEstudiante")
    , @NamedQuery(name = "PrestamosDocumentos.findByFechaDevolucion", query = "SELECT p FROM PrestamosDocumentos p WHERE p.fechaDevolucion = :fechaDevolucion")
    , @NamedQuery(name = "PrestamosDocumentos.findByMotivo", query = "SELECT p FROM PrestamosDocumentos p WHERE p.motivo = :motivo")
    , @NamedQuery(name = "PrestamosDocumentos.findByObservaciones", query = "SELECT p FROM PrestamosDocumentos p WHERE p.observaciones = :observaciones")
    , @NamedQuery(name = "PrestamosDocumentos.findByEntregadoEstudiante", query = "SELECT p FROM PrestamosDocumentos p WHERE p.entregadoEstudiante = :entregadoEstudiante")
    , @NamedQuery(name = "PrestamosDocumentos.findByPersona", query = "SELECT p FROM PrestamosDocumentos p WHERE p.persona = :persona")})
public class PrestamosDocumentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prestamo_documento")
    private Long prestamoDocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "tipo_prestamo")
    private String tipoPrestamo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 19)
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "fecha_prestamo")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;
    @Column(name = "fecha_devolucion_estudiante")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionEstudiante;
    @Column(name = "fecha_devolucion")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "motivo")
    private String motivo;
    @Size(max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "entregado_estudiante")
    private boolean entregadoEstudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "persona")
    private int persona;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;

    public PrestamosDocumentos() {
    }

    public PrestamosDocumentos(Long prestamoDocumento) {
        this.prestamoDocumento = prestamoDocumento;
    }

    public PrestamosDocumentos(Long prestamoDocumento, String tipoPrestamo, String tipoDocumento, String motivo, boolean entregadoEstudiante, int persona) {
        this.prestamoDocumento = prestamoDocumento;
        this.tipoPrestamo = tipoPrestamo;
        this.tipoDocumento = tipoDocumento;
        this.motivo = motivo;
        this.entregadoEstudiante = entregadoEstudiante;
        this.persona = persona;
    }

    public Long getPrestamoDocumento() {
        return prestamoDocumento;
    }

    public void setPrestamoDocumento(Long prestamoDocumento) {
        this.prestamoDocumento = prestamoDocumento;
    }

    public String getTipoPrestamo() {
        return tipoPrestamo;
    }

    public void setTipoPrestamo(String tipoPrestamo) {
        this.tipoPrestamo = tipoPrestamo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionEstudiante() {
        return fechaDevolucionEstudiante;
    }

    public void setFechaDevolucionEstudiante(Date fechaDevolucionEstudiante) {
        this.fechaDevolucionEstudiante = fechaDevolucionEstudiante;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean getEntregadoEstudiante() {
        return entregadoEstudiante;
    }

    public void setEntregadoEstudiante(boolean entregadoEstudiante) {
        this.entregadoEstudiante = entregadoEstudiante;
    }

    public int getPersona() {
        return persona;
    }

    public void setPersona(int persona) {
        this.persona = persona;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prestamoDocumento != null ? prestamoDocumento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrestamosDocumentos)) {
            return false;
        }
        PrestamosDocumentos other = (PrestamosDocumentos) object;
        if ((this.prestamoDocumento == null && other.prestamoDocumento != null) || (this.prestamoDocumento != null && !this.prestamoDocumento.equals(other.prestamoDocumento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PrestamosDocumentos[ prestamoDocumento=" + prestamoDocumento + " ]";
    }
    
}
