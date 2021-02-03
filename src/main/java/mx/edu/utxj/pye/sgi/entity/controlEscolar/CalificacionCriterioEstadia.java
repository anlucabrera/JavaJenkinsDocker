/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "calificacion_criterio_estadia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CalificacionCriterioEstadia.findAll", query = "SELECT c FROM CalificacionCriterioEstadia c")
    , @NamedQuery(name = "CalificacionCriterioEstadia.findBySeguimiento", query = "SELECT c FROM CalificacionCriterioEstadia c WHERE c.calificacionCriterioEstadiaPK.seguimiento = :seguimiento")
    , @NamedQuery(name = "CalificacionCriterioEstadia.findByCriterio", query = "SELECT c FROM CalificacionCriterioEstadia c WHERE c.calificacionCriterioEstadiaPK.criterio = :criterio")
    , @NamedQuery(name = "CalificacionCriterioEstadia.findByCalificacion", query = "SELECT c FROM CalificacionCriterioEstadia c WHERE c.calificacionCriterioEstadiaPK.calificacion = :calificacion")})
public class CalificacionCriterioEstadia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CalificacionCriterioEstadiaPK calificacionCriterioEstadiaPK;
    @JoinColumn(name = "criterio", referencedColumnName = "criterio", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CriterioEvaluacionEstadia criterioEvaluacionEstadia;
    @JoinColumn(name = "seguimiento", referencedColumnName = "seguimiento", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;

    public CalificacionCriterioEstadia() {
    }

    public CalificacionCriterioEstadia(CalificacionCriterioEstadiaPK calificacionCriterioEstadiaPK) {
        this.calificacionCriterioEstadiaPK = calificacionCriterioEstadiaPK;
    }

    public CalificacionCriterioEstadia(int seguimiento, int criterio, float calificacion) {
        this.calificacionCriterioEstadiaPK = new CalificacionCriterioEstadiaPK(seguimiento, criterio, calificacion);
    }

    public CalificacionCriterioEstadiaPK getCalificacionCriterioEstadiaPK() {
        return calificacionCriterioEstadiaPK;
    }

    public void setCalificacionCriterioEstadiaPK(CalificacionCriterioEstadiaPK calificacionCriterioEstadiaPK) {
        this.calificacionCriterioEstadiaPK = calificacionCriterioEstadiaPK;
    }

    public CriterioEvaluacionEstadia getCriterioEvaluacionEstadia() {
        return criterioEvaluacionEstadia;
    }

    public void setCriterioEvaluacionEstadia(CriterioEvaluacionEstadia criterioEvaluacionEstadia) {
        this.criterioEvaluacionEstadia = criterioEvaluacionEstadia;
    }

    public SeguimientoEstadiaEstudiante getSeguimientoEstadiaEstudiante() {
        return seguimientoEstadiaEstudiante;
    }

    public void setSeguimientoEstadiaEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante) {
        this.seguimientoEstadiaEstudiante = seguimientoEstadiaEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionCriterioEstadiaPK != null ? calificacionCriterioEstadiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalificacionCriterioEstadia)) {
            return false;
        }
        CalificacionCriterioEstadia other = (CalificacionCriterioEstadia) object;
        if ((this.calificacionCriterioEstadiaPK == null && other.calificacionCriterioEstadiaPK != null) || (this.calificacionCriterioEstadiaPK != null && !this.calificacionCriterioEstadiaPK.equals(other.calificacionCriterioEstadiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia[ calificacionCriterioEstadiaPK=" + calificacionCriterioEstadiaPK + " ]";
    }
    
}
