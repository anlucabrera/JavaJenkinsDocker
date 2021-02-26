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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "evaluacion_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EvaluacionEstadia.findAll", query = "SELECT e FROM EvaluacionEstadia e")
    , @NamedQuery(name = "EvaluacionEstadia.findByEvaluacion", query = "SELECT e FROM EvaluacionEstadia e WHERE e.evaluacion = :evaluacion")})
public class EvaluacionEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "evaluacion")
    private Integer evaluacion;
    @JoinColumn(name = "evento", referencedColumnName = "evento")
    @ManyToOne(optional = false)
    private EventoEstadia evento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "evaluacion")
    private List<CriterioEvaluacionEstadia> criterioEvaluacionEstadiaList;

    public EvaluacionEstadia() {
    }

    public EvaluacionEstadia(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Integer getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Integer evaluacion) {
        this.evaluacion = evaluacion;
    }

    public EventoEstadia getEvento() {
        return evento;
    }

    public void setEvento(EventoEstadia evento) {
        this.evento = evento;
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
        if (!(object instanceof EvaluacionEstadia)) {
            return false;
        }
        EvaluacionEstadia other = (EvaluacionEstadia) object;
        if ((this.evaluacion == null && other.evaluacion != null) || (this.evaluacion != null && !this.evaluacion.equals(other.evaluacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadia[ evaluacion=" + evaluacion + " ]";
    }
    
}
