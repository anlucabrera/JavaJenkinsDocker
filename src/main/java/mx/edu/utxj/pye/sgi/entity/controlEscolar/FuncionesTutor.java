/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "funciones_tutor", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuncionesTutor.findAll", query = "SELECT f FROM FuncionesTutor f")
    , @NamedQuery(name = "FuncionesTutor.findByFuncionTutor", query = "SELECT f FROM FuncionesTutor f WHERE f.funcionTutor = :funcionTutor")
    , @NamedQuery(name = "FuncionesTutor.findByNoSesion", query = "SELECT f FROM FuncionesTutor f WHERE f.noSesion = :noSesion")
    , @NamedQuery(name = "FuncionesTutor.findByMetaFuncionTutor", query = "SELECT f FROM FuncionesTutor f WHERE f.metaFuncionTutor = :metaFuncionTutor")})
public class FuncionesTutor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "funcion_tutor")
    private Integer funcionTutor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_sesion")
    private short noSesion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "meta_funcion_tutor")
    private String metaFuncionTutor;
    @JoinColumn(name = "plan_accion_tutoria", referencedColumnName = "plan_accion_tutoria")
    @ManyToOne(optional = false)
    private PlanAccionTutorial planAccionTutoria;

    public FuncionesTutor() {
    }

    public FuncionesTutor(Integer funcionTutor) {
        this.funcionTutor = funcionTutor;
    }

    public FuncionesTutor(Integer funcionTutor, short noSesion, String metaFuncionTutor) {
        this.funcionTutor = funcionTutor;
        this.noSesion = noSesion;
        this.metaFuncionTutor = metaFuncionTutor;
    }

    public Integer getFuncionTutor() {
        return funcionTutor;
    }

    public void setFuncionTutor(Integer funcionTutor) {
        this.funcionTutor = funcionTutor;
    }

    public short getNoSesion() {
        return noSesion;
    }

    public void setNoSesion(short noSesion) {
        this.noSesion = noSesion;
    }

    public String getMetaFuncionTutor() {
        return metaFuncionTutor;
    }

    public void setMetaFuncionTutor(String metaFuncionTutor) {
        this.metaFuncionTutor = metaFuncionTutor;
    }

    public PlanAccionTutorial getPlanAccionTutoria() {
        return planAccionTutoria;
    }

    public void setPlanAccionTutoria(PlanAccionTutorial planAccionTutoria) {
        this.planAccionTutoria = planAccionTutoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcionTutor != null ? funcionTutor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionesTutor)) {
            return false;
        }
        FuncionesTutor other = (FuncionesTutor) object;
        if ((this.funcionTutor == null && other.funcionTutor != null) || (this.funcionTutor != null && !this.funcionTutor.equals(other.funcionTutor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor[ funcionTutor=" + funcionTutor + " ]";
    }
    
}
