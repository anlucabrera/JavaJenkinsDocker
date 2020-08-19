/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "evaluacion_docentes_materias", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionDocentesMaterias.findAll", query = "SELECT e FROM EvaluacionDocentesMaterias e")
    , @NamedQuery(name = "EvaluacionDocentesMaterias.findByEvaluacion", query = "SELECT e FROM EvaluacionDocentesMaterias e WHERE e.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionDocentesMaterias.findByPeriodo", query = "SELECT e FROM EvaluacionDocentesMaterias e WHERE e.periodo = :periodo")
    , @NamedQuery(name = "EvaluacionDocentesMaterias.findByFechaInicio", query = "SELECT e FROM EvaluacionDocentesMaterias e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "EvaluacionDocentesMaterias.findByFechaFin", query = "SELECT e FROM EvaluacionDocentesMaterias e WHERE e.fechaFin = :fechaFin")})
public class EvaluacionDocentesMaterias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacion")
    private Integer evaluacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private int periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    public EvaluacionDocentesMaterias() {
    }

    public EvaluacionDocentesMaterias(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public EvaluacionDocentesMaterias(Integer evaluacion, int periodo, Date fechaInicio, Date fechaFin) {
        this.evaluacion = evaluacion;
        this.periodo = periodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (evaluacion != null ? evaluacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EvaluacionDocentesMaterias)) {
            return false;
        }
        EvaluacionDocentesMaterias other = (EvaluacionDocentesMaterias) object;
        if ((this.evaluacion == null && other.evaluacion != null) || (this.evaluacion != null && !this.evaluacion.equals(other.evaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMaterias[ evaluacion=" + evaluacion + " ]";
    }
    
}
