/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "objetivo_educacional_plan_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetivoEducacionalPlanMateria.findAll", query = "SELECT o FROM ObjetivoEducacionalPlanMateria o")
    , @NamedQuery(name = "ObjetivoEducacionalPlanMateria.findByObjetivoEducacional", query = "SELECT o FROM ObjetivoEducacionalPlanMateria o WHERE o.objetivoEducacionalPlanMateriaPK.objetivoEducacional = :objetivoEducacional")
    , @NamedQuery(name = "ObjetivoEducacionalPlanMateria.findByIdPlanMateria", query = "SELECT o FROM ObjetivoEducacionalPlanMateria o WHERE o.objetivoEducacionalPlanMateriaPK.idPlanMateria = :idPlanMateria")
    , @NamedQuery(name = "ObjetivoEducacionalPlanMateria.findByNivelAportacion", query = "SELECT o FROM ObjetivoEducacionalPlanMateria o WHERE o.nivelAportacion = :nivelAportacion")})
public class ObjetivoEducacionalPlanMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjetivoEducacionalPlanMateriaPK objetivoEducacionalPlanMateriaPK;
    @Size(max = 13)
    @Column(name = "nivel_aportacion")
    private String nivelAportacion;
    @JoinColumn(name = "objetivo_educacional", referencedColumnName = "objetivo_educacional", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ObjetivoEducacional objetivoEducacional1;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudioMateria planEstudioMateria;

    public ObjetivoEducacionalPlanMateria() {
    }

    public ObjetivoEducacionalPlanMateria(ObjetivoEducacionalPlanMateriaPK objetivoEducacionalPlanMateriaPK) {
        this.objetivoEducacionalPlanMateriaPK = objetivoEducacionalPlanMateriaPK;
    }

    public ObjetivoEducacionalPlanMateria(int objetivoEducacional, int idPlanMateria) {
        this.objetivoEducacionalPlanMateriaPK = new ObjetivoEducacionalPlanMateriaPK(objetivoEducacional, idPlanMateria);
    }

    public ObjetivoEducacionalPlanMateriaPK getObjetivoEducacionalPlanMateriaPK() {
        return objetivoEducacionalPlanMateriaPK;
    }

    public void setObjetivoEducacionalPlanMateriaPK(ObjetivoEducacionalPlanMateriaPK objetivoEducacionalPlanMateriaPK) {
        this.objetivoEducacionalPlanMateriaPK = objetivoEducacionalPlanMateriaPK;
    }

    public String getNivelAportacion() {
        return nivelAportacion;
    }

    public void setNivelAportacion(String nivelAportacion) {
        this.nivelAportacion = nivelAportacion;
    }

    public ObjetivoEducacional getObjetivoEducacional1() {
        return objetivoEducacional1;
    }

    public void setObjetivoEducacional1(ObjetivoEducacional objetivoEducacional1) {
        this.objetivoEducacional1 = objetivoEducacional1;
    }

    public PlanEstudioMateria getPlanEstudioMateria() {
        return planEstudioMateria;
    }

    public void setPlanEstudioMateria(PlanEstudioMateria planEstudioMateria) {
        this.planEstudioMateria = planEstudioMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objetivoEducacionalPlanMateriaPK != null ? objetivoEducacionalPlanMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetivoEducacionalPlanMateria)) {
            return false;
        }
        ObjetivoEducacionalPlanMateria other = (ObjetivoEducacionalPlanMateria) object;
        if ((this.objetivoEducacionalPlanMateriaPK == null && other.objetivoEducacionalPlanMateriaPK != null) || (this.objetivoEducacionalPlanMateriaPK != null && !this.objetivoEducacionalPlanMateriaPK.equals(other.objetivoEducacionalPlanMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacionalPlanMateria[ objetivoEducacionalPlanMateriaPK=" + objetivoEducacionalPlanMateriaPK + " ]";
    }
    
}
