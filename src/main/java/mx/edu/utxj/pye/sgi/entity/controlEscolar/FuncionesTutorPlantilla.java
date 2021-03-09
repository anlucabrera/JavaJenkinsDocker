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
 * @author UTXJ
 */
@Entity
@Table(name = "funciones_tutor_plantilla", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuncionesTutorPlantilla.findAll", query = "SELECT f FROM FuncionesTutorPlantilla f")
    , @NamedQuery(name = "FuncionesTutorPlantilla.findByFuncionTutorPlantilla", query = "SELECT f FROM FuncionesTutorPlantilla f WHERE f.funcionTutorPlantilla = :funcionTutorPlantilla")
    , @NamedQuery(name = "FuncionesTutorPlantilla.findByNoSesion", query = "SELECT f FROM FuncionesTutorPlantilla f WHERE f.noSesion = :noSesion")
    , @NamedQuery(name = "FuncionesTutorPlantilla.findByMetaFuncionTutor", query = "SELECT f FROM FuncionesTutorPlantilla f WHERE f.metaFuncionTutor = :metaFuncionTutor")})
public class FuncionesTutorPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "funcion_tutor_plantilla")
    private Integer funcionTutorPlantilla;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_sesion")
    private short noSesion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "meta_funcion_tutor")
    private String metaFuncionTutor;
    @JoinColumn(name = "plan_accion_tutoria_plantilla", referencedColumnName = "grado")
    @ManyToOne(optional = false)
    private PlanAccionTutorialPlantilla planAccionTutoriaPlantilla;

    public FuncionesTutorPlantilla() {
    }

    public FuncionesTutorPlantilla(Integer funcionTutorPlantilla) {
        this.funcionTutorPlantilla = funcionTutorPlantilla;
    }

    public FuncionesTutorPlantilla(Integer funcionTutorPlantilla, short noSesion, String metaFuncionTutor) {
        this.funcionTutorPlantilla = funcionTutorPlantilla;
        this.noSesion = noSesion;
        this.metaFuncionTutor = metaFuncionTutor;
    }

    public Integer getFuncionTutorPlantilla() {
        return funcionTutorPlantilla;
    }

    public void setFuncionTutorPlantilla(Integer funcionTutorPlantilla) {
        this.funcionTutorPlantilla = funcionTutorPlantilla;
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

    public PlanAccionTutorialPlantilla getPlanAccionTutoriaPlantilla() {
        return planAccionTutoriaPlantilla;
    }

    public void setPlanAccionTutoriaPlantilla(PlanAccionTutorialPlantilla planAccionTutoriaPlantilla) {
        this.planAccionTutoriaPlantilla = planAccionTutoriaPlantilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcionTutorPlantilla != null ? funcionTutorPlantilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionesTutorPlantilla)) {
            return false;
        }
        FuncionesTutorPlantilla other = (FuncionesTutorPlantilla) object;
        if ((this.funcionTutorPlantilla == null && other.funcionTutorPlantilla != null) || (this.funcionTutorPlantilla != null && !this.funcionTutorPlantilla.equals(other.funcionTutorPlantilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutorPlantilla[ funcionTutorPlantilla=" + funcionTutorPlantilla + " ]";
    }
    
}
