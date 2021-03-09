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
import javax.persistence.Id;
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
@Table(name = "evaluacion_estadia_descripcion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionEstadiaDescripcion.findAll", query = "SELECT e FROM EvaluacionEstadiaDescripcion e")
    , @NamedQuery(name = "EvaluacionEstadiaDescripcion.findByEvaluacion", query = "SELECT e FROM EvaluacionEstadiaDescripcion e WHERE e.evaluacion = :evaluacion")
    , @NamedQuery(name = "EvaluacionEstadiaDescripcion.findByDescripcion", query = "SELECT e FROM EvaluacionEstadiaDescripcion e WHERE e.descripcion = :descripcion")
    , @NamedQuery(name = "EvaluacionEstadiaDescripcion.findByAnioInicio", query = "SELECT e FROM EvaluacionEstadiaDescripcion e WHERE e.anioInicio = :anioInicio")
    , @NamedQuery(name = "EvaluacionEstadiaDescripcion.findByActiva", query = "SELECT e FROM EvaluacionEstadiaDescripcion e WHERE e.activa = :activa")})
public class EvaluacionEstadiaDescripcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "evaluacion")
    private Integer evaluacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio_inicio")
    private int anioInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activa")
    private boolean activa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacion")
    private List<EvaluacionEstadia> evaluacionEstadiaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacion")
    private List<CriterioEvaluacionEstadia> criterioEvaluacionEstadiaList;

    public EvaluacionEstadiaDescripcion() {
    }

    public EvaluacionEstadiaDescripcion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public EvaluacionEstadiaDescripcion(Integer evaluacion, String descripcion, int anioInicio, boolean activa) {
        this.evaluacion = evaluacion;
        this.descripcion = descripcion;
        this.anioInicio = anioInicio;
        this.activa = activa;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAnioInicio() {
        return anioInicio;
    }

    public void setAnioInicio(int anioInicio) {
        this.anioInicio = anioInicio;
    }

    public boolean getActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @XmlTransient
    public List<EvaluacionEstadia> getEvaluacionEstadiaList() {
        return evaluacionEstadiaList;
    }

    public void setEvaluacionEstadiaList(List<EvaluacionEstadia> evaluacionEstadiaList) {
        this.evaluacionEstadiaList = evaluacionEstadiaList;
    }
    
    
    @XmlTransient
    public List<CriterioEvaluacionEstadia> getCriterioEvaluacionEstadiaList() {
        return criterioEvaluacionEstadiaList;
    }

    public void setCriterioEvaluacionEstadiaList(List<CriterioEvaluacionEstadia> criterioEvaluacionEstadiaList) {
        this.criterioEvaluacionEstadiaList = criterioEvaluacionEstadiaList;
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
        if (!(object instanceof EvaluacionEstadiaDescripcion)) {
            return false;
        }
        EvaluacionEstadiaDescripcion other = (EvaluacionEstadiaDescripcion) object;
        if ((this.evaluacion == null && other.evaluacion != null) || (this.evaluacion != null && !this.evaluacion.equals(other.evaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadiaDescripcion[ evaluacion=" + evaluacion + " ]";
    }
    
}
