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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "plan_accion_tutorial", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanAccionTutorial.findAll", query = "SELECT p FROM PlanAccionTutorial p")
    , @NamedQuery(name = "PlanAccionTutorial.findByPlanAccionTutoria", query = "SELECT p FROM PlanAccionTutorial p WHERE p.planAccionTutoria = :planAccionTutoria")
    , @NamedQuery(name = "PlanAccionTutorial.findByObjetivo", query = "SELECT p FROM PlanAccionTutorial p WHERE p.objetivo = :objetivo")
    , @NamedQuery(name = "PlanAccionTutorial.findByComentarios", query = "SELECT p FROM PlanAccionTutorial p WHERE p.comentarios = :comentarios")
    , @NamedQuery(name = "PlanAccionTutorial.findBySugerencias", query = "SELECT p FROM PlanAccionTutorial p WHERE p.sugerencias = :sugerencias")
    , @NamedQuery(name = "PlanAccionTutorial.findByEstatus", query = "SELECT p FROM PlanAccionTutorial p WHERE p.estatus = :estatus")
    , @NamedQuery(name = "PlanAccionTutorial.findByComentariosDirector", query = "SELECT p FROM PlanAccionTutorial p WHERE p.comentariosDirector = :comentariosDirector")})
public class PlanAccionTutorial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "plan_accion_tutoria")
    private Integer planAccionTutoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "objetivo")
    private String objetivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "comentarios")
    private String comentarios;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "sugerencias")
    private String sugerencias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "estatus")
    private String estatus;
    @Size(max = 5000)
    @Column(name = "comentarios_director")
    private String comentariosDirector;
    @JoinColumn(name = "plan_accion_tutoria", referencedColumnName = "id_grupo", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Grupo grupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planAccionTutoria")
    private List<SesionesGrupalesTutorias> sesionesGrupalesTutoriasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planAccionTutoria")
    private List<FuncionesTutor> funcionesTutorList;

    public PlanAccionTutorial() {
    }

    public PlanAccionTutorial(Integer planAccionTutoria) {
        this.planAccionTutoria = planAccionTutoria;
    }

    public PlanAccionTutorial(Integer planAccionTutoria, String objetivo, String comentarios, String sugerencias, String estatus) {
        this.planAccionTutoria = planAccionTutoria;
        this.objetivo = objetivo;
        this.comentarios = comentarios;
        this.sugerencias = sugerencias;
        this.estatus = estatus;
    }

    public Integer getPlanAccionTutoria() {
        return planAccionTutoria;
    }

    public void setPlanAccionTutoria(Integer planAccionTutoria) {
        this.planAccionTutoria = planAccionTutoria;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(String sugerencias) {
        this.sugerencias = sugerencias;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getComentariosDirector() {
        return comentariosDirector;
    }

    public void setComentariosDirector(String comentariosDirector) {
        this.comentariosDirector = comentariosDirector;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    @XmlTransient
    public List<SesionesGrupalesTutorias> getSesionesGrupalesTutoriasList() {
        return sesionesGrupalesTutoriasList;
    }

    public void setSesionesGrupalesTutoriasList(List<SesionesGrupalesTutorias> sesionesGrupalesTutoriasList) {
        this.sesionesGrupalesTutoriasList = sesionesGrupalesTutoriasList;
    }

    @XmlTransient
    public List<FuncionesTutor> getFuncionesTutorList() {
        return funcionesTutorList;
    }

    public void setFuncionesTutorList(List<FuncionesTutor> funcionesTutorList) {
        this.funcionesTutorList = funcionesTutorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planAccionTutoria != null ? planAccionTutoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanAccionTutorial)) {
            return false;
        }
        PlanAccionTutorial other = (PlanAccionTutorial) object;
        if ((this.planAccionTutoria == null && other.planAccionTutoria != null) || (this.planAccionTutoria != null && !this.planAccionTutoria.equals(other.planAccionTutoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial[ planAccionTutoria=" + planAccionTutoria + " ]";
    }
    
}
