/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Planeacion
 */
@Entity
@Table(name = "alumnos_onceavo", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlumnosOnceavo.findAll", query = "SELECT a FROM AlumnosOnceavo a")
    , @NamedQuery(name = "AlumnosOnceavo.findByCveAlumno", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cveAlumno = :cveAlumno")
    , @NamedQuery(name = "AlumnosOnceavo.findByCveUniversidad", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "AlumnosOnceavo.findByMatricula", query = "SELECT a FROM AlumnosOnceavo a WHERE a.matricula = :matricula")
    , @NamedQuery(name = "AlumnosOnceavo.findByCveStatus", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cveStatus = :cveStatus")
    , @NamedQuery(name = "AlumnosOnceavo.findByGradoActual", query = "SELECT a FROM AlumnosOnceavo a WHERE a.gradoActual = :gradoActual")
    , @NamedQuery(name = "AlumnosOnceavo.findByFechaAlta", query = "SELECT a FROM AlumnosOnceavo a WHERE a.fechaAlta = :fechaAlta")
    , @NamedQuery(name = "AlumnosOnceavo.findByBecado", query = "SELECT a FROM AlumnosOnceavo a WHERE a.becado = :becado")
    , @NamedQuery(name = "AlumnosOnceavo.findByNotas", query = "SELECT a FROM AlumnosOnceavo a WHERE a.notas = :notas")
    , @NamedQuery(name = "AlumnosOnceavo.findByActivo", query = "SELECT a FROM AlumnosOnceavo a WHERE a.activo = :activo")
    , @NamedQuery(name = "AlumnosOnceavo.findByCveGeneracion", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cveGeneracion = :cveGeneracion")
    , @NamedQuery(name = "AlumnosOnceavo.findByCvePeriodoInicio", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cvePeriodoInicio = :cvePeriodoInicio")
    , @NamedQuery(name = "AlumnosOnceavo.findByCveGeneracionActual", query = "SELECT a FROM AlumnosOnceavo a WHERE a.cveGeneracionActual = :cveGeneracionActual")
    , @NamedQuery(name = "AlumnosOnceavo.findByExMatricula", query = "SELECT a FROM AlumnosOnceavo a WHERE a.exMatricula = :exMatricula")})
public class AlumnosOnceavo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_alumno")
    private int cveAlumno;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private int cveUniversidad;
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

    public AlumnosOnceavo() {
    }

    public AlumnosOnceavo(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public int getCveAlumno() {
        return cveAlumno;
    }

    public void setCveAlumno(int cveAlumno) {
        this.cveAlumno = cveAlumno;
    }

    public int getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(int cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) cveUniversidad;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlumnosOnceavo)) {
            return false;
        }
        AlumnosOnceavo other = (AlumnosOnceavo) object;
        if (this.cveUniversidad != other.cveUniversidad) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosOnceavo[ cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
