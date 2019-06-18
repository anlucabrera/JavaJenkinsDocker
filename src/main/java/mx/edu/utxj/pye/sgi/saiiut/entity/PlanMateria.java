/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "plan_materia", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanMateria.findAll", query = "SELECT p FROM PlanMateria p")
    , @NamedQuery(name = "PlanMateria.findByCvePlan", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cvePlan = :cvePlan")
    , @NamedQuery(name = "PlanMateria.findByCveCarrera", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveCarrera = :cveCarrera")
    , @NamedQuery(name = "PlanMateria.findByCveDivision", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveDivision = :cveDivision")
    , @NamedQuery(name = "PlanMateria.findByCveUnidadAcademica", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveUnidadAcademica = :cveUnidadAcademica")
    , @NamedQuery(name = "PlanMateria.findByCveUniversidad", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "PlanMateria.findByCveMateria", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveMateria = :cveMateria")
    , @NamedQuery(name = "PlanMateria.findByCveTurno", query = "SELECT p FROM PlanMateria p WHERE p.planMateriaPK.cveTurno = :cveTurno")
    , @NamedQuery(name = "PlanMateria.findByGrado", query = "SELECT p FROM PlanMateria p WHERE p.grado = :grado")
    , @NamedQuery(name = "PlanMateria.findByCalificacionDirecta", query = "SELECT p FROM PlanMateria p WHERE p.calificacionDirecta = :calificacionDirecta")
    , @NamedQuery(name = "PlanMateria.findByUnidades", query = "SELECT p FROM PlanMateria p WHERE p.unidades = :unidades")})
public class PlanMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanMateriaPK planMateriaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "grado")
    private int grado;
    @Column(name = "calificacion_directa")
    private Boolean calificacionDirecta;
    @Column(name = "unidades")
    private Integer unidades;

    public PlanMateria() {
    }

    public PlanMateria(PlanMateriaPK planMateriaPK) {
        this.planMateriaPK = planMateriaPK;
    }

    public PlanMateria(PlanMateriaPK planMateriaPK, int grado) {
        this.planMateriaPK = planMateriaPK;
        this.grado = grado;
    }

    public PlanMateria(int cvePlan, int cveCarrera, int cveDivision, int cveUnidadAcademica, int cveUniversidad, String cveMateria, int cveTurno) {
        this.planMateriaPK = new PlanMateriaPK(cvePlan, cveCarrera, cveDivision, cveUnidadAcademica, cveUniversidad, cveMateria, cveTurno);
    }

    public PlanMateriaPK getPlanMateriaPK() {
        return planMateriaPK;
    }

    public void setPlanMateriaPK(PlanMateriaPK planMateriaPK) {
        this.planMateriaPK = planMateriaPK;
    }

    public int getGrado() {
        return grado;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public Boolean getCalificacionDirecta() {
        return calificacionDirecta;
    }

    public void setCalificacionDirecta(Boolean calificacionDirecta) {
        this.calificacionDirecta = calificacionDirecta;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planMateriaPK != null ? planMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanMateria)) {
            return false;
        }
        PlanMateria other = (PlanMateria) object;
        if ((this.planMateriaPK == null && other.planMateriaPK != null) || (this.planMateriaPK != null && !this.planMateriaPK.equals(other.planMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.PlanMateria[ planMateriaPK=" + planMateriaPK + " ]";
    }
    
}
