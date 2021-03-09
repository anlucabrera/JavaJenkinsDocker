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
import javax.persistence.JoinColumn;
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
@Table(name = "criterio_evaluacion_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CriterioEvaluacionEstadia.findAll", query = "SELECT c FROM CriterioEvaluacionEstadia c")
    , @NamedQuery(name = "CriterioEvaluacionEstadia.findByCriterio", query = "SELECT c FROM CriterioEvaluacionEstadia c WHERE c.criterio = :criterio")
    , @NamedQuery(name = "CriterioEvaluacionEstadia.findByDescripcion", query = "SELECT c FROM CriterioEvaluacionEstadia c WHERE c.descripcion = :descripcion")})
public class CriterioEvaluacionEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio")
    private Integer criterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterioEvaluacionEstadia")
    private List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList;
    @JoinColumn(name = "evaluacion", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private EvaluacionEstadiaDescripcion evaluacion;

    public CriterioEvaluacionEstadia() {
    }

    public CriterioEvaluacionEstadia(Integer criterio) {
        this.criterio = criterio;
    }

    public CriterioEvaluacionEstadia(Integer criterio, String descripcion) {
        this.criterio = criterio;
        this.descripcion = descripcion;
    }

    public Integer getCriterio() {
        return criterio;
    }

    public void setCriterio(Integer criterio) {
        this.criterio = criterio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<CalificacionCriterioEstadia> getCalificacionCriterioEstadiaList() {
        return calificacionCriterioEstadiaList;
    }

    public void setCalificacionCriterioEstadiaList(List<CalificacionCriterioEstadia> calificacionCriterioEstadiaList) {
        this.calificacionCriterioEstadiaList = calificacionCriterioEstadiaList;
    }

    public EvaluacionEstadiaDescripcion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(EvaluacionEstadiaDescripcion evaluacion) {
        this.evaluacion = evaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (criterio != null ? criterio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioEvaluacionEstadia)) {
            return false;
        }
        CriterioEvaluacionEstadia other = (CriterioEvaluacionEstadia) object;
        if ((this.criterio == null && other.criterio != null) || (this.criterio != null && !this.criterio.equals(other.criterio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioEvaluacionEstadia[ criterio=" + criterio + " ]";
    }
    
}
