/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "seguimiento_estadia_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeguimientoEstadiaEstudiante.findAll", query = "SELECT s FROM SeguimientoEstadiaEstudiante s")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findBySeguimiento", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.seguimiento = :seguimiento")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaRegistro", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaRegistro = :fechaRegistro")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByEmpresa", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.empresa = :empresa")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByProyecto", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.proyecto = :proyecto")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaInicio", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaFin", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaFin = :fechaFin")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByValidacion", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.validacion = :validacion")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaValidacion", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaValidacion = :fechaValidacion")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByPersonalValidacion", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.personalValidacion = :personalValidacion")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByPromedioAsesorExterno", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.promedioAsesorExterno = :promedioAsesorExterno")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByPromedioAsesorInterno", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.promedioAsesorInterno = :promedioAsesorInterno")})
public class SeguimientoEstadiaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seguimiento")
    private Integer seguimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "empresa")
    private Integer empresa;
    @Size(max = 1000)
    @Column(name = "proyecto")
    private String proyecto;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion")
    private boolean validacion;
    @Column(name = "fecha_validacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacion;
    @Column(name = "personal_validacion")
    private Integer personalValidacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_asesor_externo")
    private float promedioAsesorExterno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_asesor_interno")
    private float promedioAsesorInterno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimientoEstadiaEstudiante", fetch = FetchType.LAZY)
    private List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimientoEstadia", fetch = FetchType.LAZY)
    private List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList;
    @JoinColumn(name = "asesor", referencedColumnName = "asesor_estadia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AsesorAcademicoEstadia asesor;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "matricula", referencedColumnName = "matricula")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante matricula;

    public SeguimientoEstadiaEstudiante() {
    }

    public SeguimientoEstadiaEstudiante(Integer seguimiento) {
        this.seguimiento = seguimiento;
    }

    public SeguimientoEstadiaEstudiante(Integer seguimiento, Date fechaRegistro, boolean validacion, float promedioAsesorExterno, float promedioAsesorInterno) {
        this.seguimiento = seguimiento;
        this.fechaRegistro = fechaRegistro;
        this.validacion = validacion;
        this.promedioAsesorExterno = promedioAsesorExterno;
        this.promedioAsesorInterno = promedioAsesorInterno;
    }

    public Integer getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(Integer seguimiento) {
        this.seguimiento = seguimiento;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean getValidacion() {
        return validacion;
    }

    public void setValidacion(boolean validacion) {
        this.validacion = validacion;
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

    public float getPromedioAsesorExterno() {
        return promedioAsesorExterno;
    }

    public void setPromedioAsesorExterno(float promedioAsesorExterno) {
        this.promedioAsesorExterno = promedioAsesorExterno;
    }

    public float getPromedioAsesorInterno() {
        return promedioAsesorInterno;
    }

    public void setPromedioAsesorInterno(float promedioAsesorInterno) {
        this.promedioAsesorInterno = promedioAsesorInterno;
    }

    @XmlTransient
    public List<CalificacionCriterioEstadia> getCalificacionCriterioEstadiaList() {
        return calificacionCriterioEstadiaList;
    }

    public void setCalificacionCriterioEstadiaList(List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList) {
        this.calificacionCriterioEstadiaList = calificacionCriterioEstadiaList;
    }

    @XmlTransient
    public List<DocumentoSeguimientoEstadia> getDocumentoSeguimientoEstadiaList() {
        return documentoSeguimientoEstadiaList;
    }

    public void setDocumentoSeguimientoEstadiaList(List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList) {
        this.documentoSeguimientoEstadiaList = documentoSeguimientoEstadiaList;
    }

    public AsesorAcademicoEstadia getAsesor() {
        return asesor;
    }

    public void setAsesor(AsesorAcademicoEstadia asesor) {
        this.asesor = asesor;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
    }

    public Estudiante getMatricula() {
        return matricula;
    }

    public void setMatricula(Estudiante matricula) {
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seguimiento != null ? seguimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SeguimientoEstadiaEstudiante)) {
            return false;
        }
        SeguimientoEstadiaEstudiante other = (SeguimientoEstadiaEstudiante) object;
        if ((this.seguimiento == null && other.seguimiento != null) || (this.seguimiento != null && !this.seguimiento.equals(other.seguimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante[ seguimiento=" + seguimiento + " ]";
    }
    
}
