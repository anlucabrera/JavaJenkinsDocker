/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "plan_estudio_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanEstudioMateria.findAll", query = "SELECT p FROM PlanEstudioMateria p")
    , @NamedQuery(name = "PlanEstudioMateria.findByIdPlanMateria", query = "SELECT p FROM PlanEstudioMateria p WHERE p.idPlanMateria = :idPlanMateria")
    , @NamedQuery(name = "PlanEstudioMateria.findByClaveMateria", query = "SELECT p FROM PlanEstudioMateria p WHERE p.claveMateria = :claveMateria")
    , @NamedQuery(name = "PlanEstudioMateria.findByGrado", query = "SELECT p FROM PlanEstudioMateria p WHERE p.grado = :grado")
    , @NamedQuery(name = "PlanEstudioMateria.findByIntegradora", query = "SELECT p FROM PlanEstudioMateria p WHERE p.integradora = :integradora")
    , @NamedQuery(name = "PlanEstudioMateria.findByHorasTeoricas", query = "SELECT p FROM PlanEstudioMateria p WHERE p.horasTeoricas = :horasTeoricas")
    , @NamedQuery(name = "PlanEstudioMateria.findByHorasPracticas", query = "SELECT p FROM PlanEstudioMateria p WHERE p.horasPracticas = :horasPracticas")})
public class PlanEstudioMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_plan_materia")
    private Integer idPlanMateria;
    @Size(max = 90)
    @Column(name = "clave_materia")
    private String claveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "integradora")
    private boolean integradora;
    @Column(name = "horasTeoricas")
    private Integer horasTeoricas;
    @Column(name = "horasPracticas")
    private Integer horasPracticas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanMateria")
    private List<CalificacionesHistorialTsu> calificacionesHistorialTsuList;
    @ManyToMany(mappedBy = "planEstudioMateriaList", fetch = FetchType.LAZY)
    private List<Competencia> competenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanMateria", fetch = FetchType.LAZY)
    private List<MetasPropuestas> metasPropuestasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanMateria", fetch = FetchType.LAZY)
    private List<PermisosCapturaExtemporaneaEstudiante> permisosCapturaExtemporaneaEstudianteList;
    @JoinColumn(name = "id_materia", referencedColumnName = "id_materia")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Materia idMateria;
    @JoinColumn(name = "id_plan", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudio idPlan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanMateria", fetch = FetchType.LAZY)
    private List<CargaAcademica> cargaAcademicaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlanMateria", fetch = FetchType.LAZY)
    private List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planEstudioMateria", fetch = FetchType.LAZY)
    private List<ObjetivoEducacionalPlanMateria> objetivoEducacionalPlanMateriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planEstudioMateria", fetch = FetchType.LAZY)
    private List<IndicadorAlineacionPlanMateria> indicadorAlineacionPlanMateriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planEstudioMateria")
    private List<CriterioDesempenioPlanMateria> criterioDesempenioPlanMateriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planEstudioMateria")
    private List<AtributoEgresoPlanMateria> atributoEgresoPlanMateriaList;

    public PlanEstudioMateria() {
    }

    public PlanEstudioMateria(Integer idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    public PlanEstudioMateria(Integer idPlanMateria, int grado, boolean integradora) {
        this.idPlanMateria = idPlanMateria;
        this.grado = grado;
        this.integradora = integradora;
    }

    public Integer getIdPlanMateria() {
        return idPlanMateria;
    }

    public void setIdPlanMateria(Integer idPlanMateria) {
        this.idPlanMateria = idPlanMateria;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public int getGrado() {
        return grado;
    }
    
    public void setGrado(int grado) {
        this.grado = grado;
    }

    public boolean getIntegradora() {
        return integradora;
    }

    public void setIntegradora(boolean integradora) {
        this.integradora = integradora;
    }
    
    public Integer getHorasTeoricas() {
        return horasTeoricas;
    }

    public void setHorasTeoricas(Integer horasTeoricas) {
        this.horasTeoricas = horasTeoricas;
    }

    public Integer getHorasPracticas() {
        return horasPracticas;
    }

    public void setHorasPracticas(Integer horasPracticas) {
        this.horasPracticas = horasPracticas;
    }

    @XmlTransient
    public List<Competencia> getCompetenciaList() {
        return competenciaList;
    }

    public void setCompetenciaList(List<Competencia> competenciaList) {
        this.competenciaList = competenciaList;
    }

    @XmlTransient
    public List<MetasPropuestas> getMetasPropuestasList() {
        return metasPropuestasList;
    }

    public void setMetasPropuestasList(List<MetasPropuestas> metasPropuestasList) {
        this.metasPropuestasList = metasPropuestasList;
    }

    @XmlTransient
    public List<PermisosCapturaExtemporaneaEstudiante> getPermisosCapturaExtemporaneaEstudianteList() {
        return permisosCapturaExtemporaneaEstudianteList;
    }

    public void setPermisosCapturaExtemporaneaEstudianteList(List<PermisosCapturaExtemporaneaEstudiante> permisosCapturaExtemporaneaEstudianteList) {
        this.permisosCapturaExtemporaneaEstudianteList = permisosCapturaExtemporaneaEstudianteList;
    }

    public Materia getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Materia idMateria) {
        this.idMateria = idMateria;
    }

    public PlanEstudio getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(PlanEstudio idPlan) {
        this.idPlan = idPlan;
    }

    @XmlTransient
    public List<CargaAcademica> getCargaAcademicaList() {
        return cargaAcademicaList;
    }

    public void setCargaAcademicaList(List<CargaAcademica> cargaAcademicaList) {
        this.cargaAcademicaList = cargaAcademicaList;
    }

    @XmlTransient
    public List<PermisosCapturaExtemporaneaGrupal> getPermisosCapturaExtemporaneaGrupalList() {
        return permisosCapturaExtemporaneaGrupalList;
    }

    public void setPermisosCapturaExtemporaneaGrupalList(List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupalList) {
        this.permisosCapturaExtemporaneaGrupalList = permisosCapturaExtemporaneaGrupalList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlanMateria != null ? idPlanMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanEstudioMateria)) {
            return false;
        }
        PlanEstudioMateria other = (PlanEstudioMateria) object;
        if ((this.idPlanMateria == null && other.idPlanMateria != null) || (this.idPlanMateria != null && !this.idPlanMateria.equals(other.idPlanMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria[ idPlanMateria=" + idPlanMateria + " ]";
    }

    @XmlTransient
    public List<ObjetivoEducacionalPlanMateria> getObjetivoEducacionalPlanMateriaList() {
        return objetivoEducacionalPlanMateriaList;
    }

    public void setObjetivoEducacionalPlanMateriaList(List<ObjetivoEducacionalPlanMateria> objetivoEducacionalPlanMateriaList) {
        this.objetivoEducacionalPlanMateriaList = objetivoEducacionalPlanMateriaList;
    }

    @XmlTransient
    public List<IndicadorAlineacionPlanMateria> getIndicadorAlineacionPlanMateriaList() {
        return indicadorAlineacionPlanMateriaList;
    }

    public void setIndicadorAlineacionPlanMateriaList(List<IndicadorAlineacionPlanMateria> indicadorAlineacionPlanMateriaList) {
        this.indicadorAlineacionPlanMateriaList = indicadorAlineacionPlanMateriaList;
    }

    @XmlTransient
    public List<CalificacionesHistorialTsu> getCalificacionesHistorialTsuList() {
        return calificacionesHistorialTsuList;
    }

    public void setCalificacionesHistorialTsuList(List<CalificacionesHistorialTsu> calificacionesHistorialTsuList) {
        this.calificacionesHistorialTsuList = calificacionesHistorialTsuList;
    }

    @XmlTransient
    public List<CriterioDesempenioPlanMateria> getCriterioDesempenioPlanMateriaList() {
        return criterioDesempenioPlanMateriaList;
    }

    public void setCriterioDesempenioPlanMateriaList(List<CriterioDesempenioPlanMateria> criterioDesempenioPlanMateriaList) {
        this.criterioDesempenioPlanMateriaList = criterioDesempenioPlanMateriaList;
    }

    @XmlTransient
    public List<AtributoEgresoPlanMateria> getAtributoEgresoPlanMateriaList() {
        return atributoEgresoPlanMateriaList;
    }

    public void setAtributoEgresoPlanMateriaList(List<AtributoEgresoPlanMateria> atributoEgresoPlanMateriaList) {
        this.atributoEgresoPlanMateriaList = atributoEgresoPlanMateriaList;
    }
    
}
