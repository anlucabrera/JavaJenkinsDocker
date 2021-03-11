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
@Table(name = "permisos_captura_extemporanea_estudiante", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findAll", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByPermisoEstudiante", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.permisoEstudiante = :permisoEstudiante")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByPeriodo", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByDocente", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.docente = :docente")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByTipoEvaluacion", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.tipoEvaluacion = :tipoEvaluacion")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByFechaInicio", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByFechaFin", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByPersonalGrabaPermiso", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.personalGrabaPermiso = :personalGrabaPermiso")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByFechaGrabaPermiso", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.fechaGrabaPermiso = :fechaGrabaPermiso")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByTipoApertura", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.tipoApertura = :tipoApertura")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaEstudiante.findByValidada", query = "SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.validada = :validada")})
public class PermisosCapturaExtemporaneaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "permiso_estudiante")
    private Integer permisoEstudiante;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "docente")
    private int docente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "tipo_evaluacion")
    private String tipoEvaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "personal_graba_permiso")
    private Integer personalGrabaPermiso;
    @Column(name = "fecha_graba_permiso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGrabaPermiso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "tipo_apertura")
    private String tipoApertura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "validada")
    private int validada;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo idGrupo;
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumn(name = "justificacion_permiso", referencedColumnName = "justificacion")
    @ManyToOne(optional = false)
    private JustificacionPermisosExtemporaneos justificacionPermiso;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")
    @ManyToOne(optional = false)
    private PlanEstudioMateria idPlanMateria;
    @JoinColumn(name = "id_unidad_materia", referencedColumnName = "id_unidad_materia")
    @ManyToOne
    private UnidadMateria idUnidadMateria;

    public PermisosCapturaExtemporaneaEstudiante() {
    }

    public PermisosCapturaExtemporaneaEstudiante(Integer permisoEstudiante) {
        this.permisoEstudiante = permisoEstudiante;
    }

    public PermisosCapturaExtemporaneaEstudiante(Integer permisoEstudiante, int periodo, int docente, String tipoEvaluacion, Date fechaInicio, Date fechaFin, String tipoApertura, int validada) {
        this.permisoEstudiante = permisoEstudiante;
        this.periodo = periodo;
        this.docente = docente;
        this.tipoEvaluacion = tipoEvaluacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoApertura = tipoApertura;
        this.validada = validada;
    }

    public Integer getPermisoEstudiante() {
        return permisoEstudiante;
    }

    public void setPermisoEstudiante(Integer permisoEstudiante) {
        this.permisoEstudiante = permisoEstudiante;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getDocente() {
        return docente;
    }

    public void setDocente(int docente) {
        this.docente = docente;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
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

    public Integer getPersonalGrabaPermiso() {
        return personalGrabaPermiso;
    }

    public void setPersonalGrabaPermiso(Integer personalGrabaPermiso) {
        this.personalGrabaPermiso = personalGrabaPermiso;
    }

    public Date getFechaGrabaPermiso() {
        return fechaGrabaPermiso;
    }

    public void setFechaGrabaPermiso(Date fechaGrabaPermiso) {
        this.fechaGrabaPermiso = fechaGrabaPermiso;
    }

    public String getTipoApertura() {
        return tipoApertura;
    }

    public void setTipoApertura(String tipoApertura) {
        this.tipoApertura = tipoApertura;
    }

    public int getValidada() {
        return validada;
    }

    public void setValidada(int validada) {
        this.validada = validada;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public JustificacionPermisosExtemporaneos getJustificacionPermiso() {
        return justificacionPermiso;
    }

    public void setJustificacionPermiso(JustificacionPermisosExtemporaneos justificacionPermiso) {
        this.justificacionPermiso = justificacionPermiso;
    }

    public PlanEstudioMateria getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(PlanEstudioMateria idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    public UnidadMateria getIdUnidadMateria() {
        return idUnidadMateria;
    }

    public void setIdUnidadMateria(UnidadMateria idUnidadMateria) {
        this.idUnidadMateria = idUnidadMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (permisoEstudiante != null ? permisoEstudiante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisosCapturaExtemporaneaEstudiante)) {
            return false;
        }
        PermisosCapturaExtemporaneaEstudiante other = (PermisosCapturaExtemporaneaEstudiante) object;
        if ((this.permisoEstudiante == null && other.permisoEstudiante != null) || (this.permisoEstudiante != null && !this.permisoEstudiante.equals(other.permisoEstudiante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaEstudiante[ permisoEstudiante=" + permisoEstudiante + " ]";
    }
    
}
