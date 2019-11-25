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
@Table(name = "permisos_captura_extemporanea_grupal", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findAll", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByPermisoGrupal", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.permisoGrupal = :permisoGrupal")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByPeriodo", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByDocente", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.docente = :docente")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByTipoEvaluacion", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.tipoEvaluacion = :tipoEvaluacion")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByFechaInicio", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByFechaFin", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.fechaFin = :fechaFin")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByPersonalGrabaPermiso", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.personalGrabaPermiso = :personalGrabaPermiso")
    , @NamedQuery(name = "PermisosCapturaExtemporaneaGrupal.findByFechaGrabaPermiso", query = "SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.fechaGrabaPermiso = :fechaGrabaPermiso")})
public class PermisosCapturaExtemporaneaGrupal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "permiso_grupal")
    private Integer permisoGrupal;
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
    @Size(min = 1, max = 18)
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "personal_graba_permiso")
    private int personalGrabaPermiso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_graba_permiso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGrabaPermiso;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")
    @ManyToOne(optional = false)
    private Grupo idGrupo;
    @JoinColumn(name = "justificacion_permiso", referencedColumnName = "justificacion")
    @ManyToOne(optional = false)
    private JustificacionPermisosExtemporaneos justificacionPermiso;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")
    @ManyToOne(optional = false)
    private PlanEstudioMateria idPlanMateria;
    @JoinColumn(name = "id_unidad_materia", referencedColumnName = "id_unidad_materia")
    @ManyToOne
    private UnidadMateria idUnidadMateria;

    public PermisosCapturaExtemporaneaGrupal() {
    }

    public PermisosCapturaExtemporaneaGrupal(Integer permisoGrupal) {
        this.permisoGrupal = permisoGrupal;
    }

    public PermisosCapturaExtemporaneaGrupal(Integer permisoGrupal, int periodo, int docente, String tipoEvaluacion, Date fechaInicio, Date fechaFin, int personalGrabaPermiso, Date fechaGrabaPermiso) {
        this.permisoGrupal = permisoGrupal;
        this.periodo = periodo;
        this.docente = docente;
        this.tipoEvaluacion = tipoEvaluacion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.personalGrabaPermiso = personalGrabaPermiso;
        this.fechaGrabaPermiso = fechaGrabaPermiso;
    }

    public Integer getPermisoGrupal() {
        return permisoGrupal;
    }

    public void setPermisoGrupal(Integer permisoGrupal) {
        this.permisoGrupal = permisoGrupal;
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

    public int getPersonalGrabaPermiso() {
        return personalGrabaPermiso;
    }

    public void setPersonalGrabaPermiso(int personalGrabaPermiso) {
        this.personalGrabaPermiso = personalGrabaPermiso;
    }

    public Date getFechaGrabaPermiso() {
        return fechaGrabaPermiso;
    }

    public void setFechaGrabaPermiso(Date fechaGrabaPermiso) {
        this.fechaGrabaPermiso = fechaGrabaPermiso;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
        this.idGrupo = idGrupo;
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
        hash += (permisoGrupal != null ? permisoGrupal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PermisosCapturaExtemporaneaGrupal)) {
            return false;
        }
        PermisosCapturaExtemporaneaGrupal other = (PermisosCapturaExtemporaneaGrupal) object;
        if ((this.permisoGrupal == null && other.permisoGrupal != null) || (this.permisoGrupal != null && !this.permisoGrupal.equals(other.permisoGrupal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PermisosCapturaExtemporaneaGrupal[ permisoGrupal=" + permisoGrupal + " ]";
    }
    
}
