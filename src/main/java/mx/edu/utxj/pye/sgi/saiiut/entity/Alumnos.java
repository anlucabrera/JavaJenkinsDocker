/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "alumnos", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumnos.findAll", query = "SELECT a FROM Alumnos a")
    , @NamedQuery(name = "Alumnos.findByCveAlumno", query = "SELECT a FROM Alumnos a WHERE a.alumnosPK.cveAlumno = :cveAlumno")
    , @NamedQuery(name = "Alumnos.findByCveUniversidad", query = "SELECT a FROM Alumnos a WHERE a.alumnosPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Alumnos.findByMatricula", query = "SELECT a FROM Alumnos a WHERE a.matricula = :matricula")
    , @NamedQuery(name = "Alumnos.findByCveStatus", query = "SELECT a FROM Alumnos a WHERE a.cveStatus = :cveStatus")
    , @NamedQuery(name = "Alumnos.findByGradoActual", query = "SELECT a FROM Alumnos a WHERE a.gradoActual = :gradoActual")
    , @NamedQuery(name = "Alumnos.findByFechaAlta", query = "SELECT a FROM Alumnos a WHERE a.fechaAlta = :fechaAlta")
    , @NamedQuery(name = "Alumnos.findByBecado", query = "SELECT a FROM Alumnos a WHERE a.becado = :becado")
    , @NamedQuery(name = "Alumnos.findByNotas", query = "SELECT a FROM Alumnos a WHERE a.notas = :notas")
    , @NamedQuery(name = "Alumnos.findByActivo", query = "SELECT a FROM Alumnos a WHERE a.activo = :activo")
    , @NamedQuery(name = "Alumnos.findByCveGeneracion", query = "SELECT a FROM Alumnos a WHERE a.cveGeneracion = :cveGeneracion")
    , @NamedQuery(name = "Alumnos.findByCvePeriodoInicio", query = "SELECT a FROM Alumnos a WHERE a.cvePeriodoInicio = :cvePeriodoInicio")
    , @NamedQuery(name = "Alumnos.findByCveGeneracionActual", query = "SELECT a FROM Alumnos a WHERE a.cveGeneracionActual = :cveGeneracionActual")
    , @NamedQuery(name = "Alumnos.findByExMatricula", query = "SELECT a FROM Alumnos a WHERE a.exMatricula = :exMatricula")})
public class Alumnos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlumnosPK alumnosPK;
    @Size(max = 15)
    @Column(name = "matricula")
    private String matricula;
    @Column(name = "cve_status")
    private Integer cveStatus;
    @Column(name = "grado_actual")
    private Short gradoActual;
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "becado")
    private Boolean becado;
    @Size(max = 250)
    @Column(name = "notas")
    private String notas;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "cve_generacion")
    private Integer cveGeneracion;
    @Column(name = "cve_periodo_inicio")
    private Integer cvePeriodoInicio;
    @Column(name = "cve_generacion_actual")
    private Integer cveGeneracionActual;
    @Size(max = 11)
    @Column(name = "ex_matricula")
    private String exMatricula;
    @JoinColumns({
        @JoinColumn(name = "cve_grupo", referencedColumnName = "cve_grupo")
        , @JoinColumn(name = "cve_turno", referencedColumnName = "cve_turno")
        , @JoinColumn(name = "cve_plan", referencedColumnName = "cve_plan")
        , @JoinColumn(name = "cve_carrera", referencedColumnName = "cve_carrera")
        , @JoinColumn(name = "cve_division", referencedColumnName = "cve_division")
        , @JoinColumn(name = "cve_unidad_academica", referencedColumnName = "cve_unidad_academica")
        , @JoinColumn(name = "cve_universidad", referencedColumnName = "cve_universidad", insertable = false, updatable = false)
        , @JoinColumn(name = "cve_periodo_actual", referencedColumnName = "cve_periodo")})
    @ManyToOne(optional = false)
    private Grupos grupos;

    public Alumnos() {
    }

    public Alumnos(AlumnosPK alumnosPK) {
        this.alumnosPK = alumnosPK;
    }

    public Alumnos(int cveAlumno, int cveUniversidad) {
        this.alumnosPK = new AlumnosPK(cveAlumno, cveUniversidad);
    }

    public AlumnosPK getAlumnosPK() {
        return alumnosPK;
    }

    public void setAlumnosPK(AlumnosPK alumnosPK) {
        this.alumnosPK = alumnosPK;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getCveStatus() {
        return cveStatus;
    }

    public void setCveStatus(Integer cveStatus) {
        this.cveStatus = cveStatus;
    }

    public Short getGradoActual() {
        return gradoActual;
    }

    public void setGradoActual(Short gradoActual) {
        this.gradoActual = gradoActual;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Boolean getBecado() {
        return becado;
    }

    public void setBecado(Boolean becado) {
        this.becado = becado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getCveGeneracion() {
        return cveGeneracion;
    }

    public void setCveGeneracion(Integer cveGeneracion) {
        this.cveGeneracion = cveGeneracion;
    }

    public Integer getCvePeriodoInicio() {
        return cvePeriodoInicio;
    }

    public void setCvePeriodoInicio(Integer cvePeriodoInicio) {
        this.cvePeriodoInicio = cvePeriodoInicio;
    }

    public Integer getCveGeneracionActual() {
        return cveGeneracionActual;
    }

    public void setCveGeneracionActual(Integer cveGeneracionActual) {
        this.cveGeneracionActual = cveGeneracionActual;
    }

    public String getExMatricula() {
        return exMatricula;
    }

    public void setExMatricula(String exMatricula) {
        this.exMatricula = exMatricula;
    }

    public Grupos getGrupos() {
        return grupos;
    }

    public void setGrupos(Grupos grupos) {
        this.grupos = grupos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alumnosPK != null ? alumnosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumnos)) {
            return false;
        }
        Alumnos other = (Alumnos) object;
        if ((this.alumnosPK == null && other.alumnosPK != null) || (this.alumnosPK != null && !this.alumnosPK.equals(other.alumnosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos[ alumnosPK=" + alumnosPK + " ]";
    }
    
}
