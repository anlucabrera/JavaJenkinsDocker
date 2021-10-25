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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
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
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByValidacionCoordinador", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.validacionCoordinador = :validacionCoordinador")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaValidacionCoordinador", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaValidacionCoordinador = :fechaValidacionCoordinador")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByComentariosCoordinador", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.comentariosCoordinador = :comentariosCoordinador")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByValidacionDirector", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.validacionDirector = :validacionDirector")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaValidacionDirector", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaValidacionDirector = :fechaValidacionDirector")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByPromedioAsesorExterno", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.promedioAsesorExterno = :promedioAsesorExterno")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByPromedioAsesorInterno", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.promedioAsesorInterno = :promedioAsesorInterno")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByValidacionVinculacion", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.validacionVinculacion = :validacionVinculacion")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByFechaValidacionVinculacion", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.fechaValidacionVinculacion = :fechaValidacionVinculacion")
    , @NamedQuery(name = "SeguimientoEstadiaEstudiante.findByActivo", query = "SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.activo = :activo")})
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
    @Column(name = "validacion_coordinador")
    private boolean validacionCoordinador;
    @Column(name = "fecha_validacion_coordinador")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionCoordinador;
    @Size(max = 500)
    @Column(name = "comentarios_coordinador")
    private String comentariosCoordinador;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_director")
    private boolean validacionDirector;
    @Column(name = "fecha_validacion_director")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionDirector;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_asesor_externo")
    private double promedioAsesorExterno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promedio_asesor_interno")
    private double promedioAsesorInterno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validacion_vinculacion")
    private boolean validacionVinculacion;
    @Column(name = "fecha_validacion_vinculacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaValidacionVinculacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimientoEstadiaEstudiante", fetch = FetchType.LAZY)
    private List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento", fetch = FetchType.LAZY)
    private List<AsesorEmpresarialEstadia> asesorEmpresarialEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimiento", fetch = FetchType.LAZY)
    private List<AperturaExtemporaneaEventoEstadia> aperturaExtemporaneaEventoEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seguimientoEstadia", fetch = FetchType.LAZY)
    private List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "seguimiento", fetch = FetchType.LAZY)
    private FolioAcreditacionEstadia folioAcreditacionEstadia;
    @JoinColumn(name = "asesor", referencedColumnName = "asesor_estadia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private AsesorAcademicoEstadia asesor;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EventoEstadia evento;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiante estudiante;

    public SeguimientoEstadiaEstudiante() {
    }

    public SeguimientoEstadiaEstudiante(Integer seguimiento) {
        this.seguimiento = seguimiento;
    }

    public SeguimientoEstadiaEstudiante(Integer seguimiento, Date fechaRegistro, boolean validacionCoordinador, boolean validacionDirector, double promedioAsesorExterno, double promedioAsesorInterno, boolean validacionVinculacion, boolean activo) {
        this.seguimiento = seguimiento;
        this.fechaRegistro = fechaRegistro;
        this.validacionCoordinador = validacionCoordinador;
        this.validacionDirector = validacionDirector;
        this.promedioAsesorExterno = promedioAsesorExterno;
        this.promedioAsesorInterno = promedioAsesorInterno;
        this.validacionVinculacion = validacionVinculacion;
        this.activo = activo;
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

    public boolean getValidacionCoordinador() {
        return validacionCoordinador;
    }

    public void setValidacionCoordinador(boolean validacionCoordinador) {
        this.validacionCoordinador = validacionCoordinador;
    }

    public Date getFechaValidacionCoordinador() {
        return fechaValidacionCoordinador;
    }

    public void setFechaValidacionCoordinador(Date fechaValidacionCoordinador) {
        this.fechaValidacionCoordinador = fechaValidacionCoordinador;
    }

    public String getComentariosCoordinador() {
        return comentariosCoordinador;
    }

    public void setComentariosCoordinador(String comentariosCoordinador) {
        this.comentariosCoordinador = comentariosCoordinador;
    }

    public boolean getValidacionDirector() {
        return validacionDirector;
    }

    public void setValidacionDirector(boolean validacionDirector) {
        this.validacionDirector = validacionDirector;
    }

    public Date getFechaValidacionDirector() {
        return fechaValidacionDirector;
    }

    public void setFechaValidacionDirector(Date fechaValidacionDirector) {
        this.fechaValidacionDirector = fechaValidacionDirector;
    }

    public double getPromedioAsesorExterno() {
        return promedioAsesorExterno;
    }

    public void setPromedioAsesorExterno(double promedioAsesorExterno) {
        this.promedioAsesorExterno = promedioAsesorExterno;
    }

    public double getPromedioAsesorInterno() {
        return promedioAsesorInterno;
    }

    public void setPromedioAsesorInterno(double promedioAsesorInterno) {
        this.promedioAsesorInterno = promedioAsesorInterno;
    }

    public boolean getValidacionVinculacion() {
        return validacionVinculacion;
    }

    public void setValidacionVinculacion(boolean validacionVinculacion) {
        this.validacionVinculacion = validacionVinculacion;
    }

    public Date getFechaValidacionVinculacion() {
        return fechaValidacionVinculacion;
    }

    public void setFechaValidacionVinculacion(Date fechaValidacionVinculacion) {
        this.fechaValidacionVinculacion = fechaValidacionVinculacion;
    }
    
    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public List<CalificacionCriterioEstadia> getCalificacionCriterioEstadiaList() {
        return calificacionCriterioEstadiaList;
    }

    public void setCalificacionCriterioEstadiaList(List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList) {
        this.calificacionCriterioEstadiaList = calificacionCriterioEstadiaList;
    }

    @XmlTransient
    public List<AsesorEmpresarialEstadia> getAsesorEmpresarialEstadiaList() {
        return asesorEmpresarialEstadiaList;
    }

    public void setAsesorEmpresarialEstadiaList(List<AsesorEmpresarialEstadia> asesorEmpresarialEstadiaList) {
        this.asesorEmpresarialEstadiaList = asesorEmpresarialEstadiaList;
    }

    @XmlTransient
    public List<AperturaExtemporaneaEventoEstadia> getAperturaExtemporaneaEventoEstadiaList() {
        return aperturaExtemporaneaEventoEstadiaList;
    }

    public void setAperturaExtemporaneaEventoEstadiaList(List<AperturaExtemporaneaEventoEstadia> aperturaExtemporaneaEventoEstadiaList) {
        this.aperturaExtemporaneaEventoEstadiaList = aperturaExtemporaneaEventoEstadiaList;
    }

    @XmlTransient
    public List<DocumentoSeguimientoEstadia> getDocumentoSeguimientoEstadiaList() {
        return documentoSeguimientoEstadiaList;
    }

    public void setDocumentoSeguimientoEstadiaList(List<DocumentoSeguimientoEstadia> documentoSeguimientoEstadiaList) {
        this.documentoSeguimientoEstadiaList = documentoSeguimientoEstadiaList;
    }

    public FolioAcreditacionEstadia getFolioAcreditacionEstadia() {
        return folioAcreditacionEstadia;
    }

    public void setFolioAcreditacionEstadia(FolioAcreditacionEstadia folioAcreditacionEstadia) {
        this.folioAcreditacionEstadia = folioAcreditacionEstadia;
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

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
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
