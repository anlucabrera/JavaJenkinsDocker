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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "sesiones_grupales_tutorias", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SesionesGrupalesTutorias.findAll", query = "SELECT s FROM SesionesGrupalesTutorias s")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findBySesionGrupal", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.sesionGrupal = :sesionGrupal")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findByNoSesion", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.noSesion = :noSesion")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findByActividadProgramada", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.actividadProgramada = :actividadProgramada")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findByObjetivos", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.objetivos = :objetivos")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findByCumplimiento", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.cumplimiento = :cumplimiento")
    , @NamedQuery(name = "SesionesGrupalesTutorias.findByJustificacion", query = "SELECT s FROM SesionesGrupalesTutorias s WHERE s.justificacion = :justificacion")})
public class SesionesGrupalesTutorias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sesion_grupal")
    private Integer sesionGrupal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_sesion")
    private short noSesion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "actividad_programada")
    private String actividadProgramada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "objetivos")
    private String objetivos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cumplimiento")
    private boolean cumplimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "justificacion")
    private String justificacion;
    @JoinColumn(name = "plan_accion_tutoria", referencedColumnName = "plan_accion_tutoria")
    @ManyToOne(optional = false)
    private PlanAccionTutorial planAccionTutoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sesionGrupal")
    private List<TutoriasIndividuales> tutoriasIndividualesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sesionGrupal")
    private List<TutoriasGrupales> tutoriasGrupalesList;

    public SesionesGrupalesTutorias() {
    }

    public SesionesGrupalesTutorias(Integer sesionGrupal) {
        this.sesionGrupal = sesionGrupal;
    }

    public SesionesGrupalesTutorias(Integer sesionGrupal, short noSesion, String actividadProgramada, String objetivos, boolean cumplimiento, String justificacion) {
        this.sesionGrupal = sesionGrupal;
        this.noSesion = noSesion;
        this.actividadProgramada = actividadProgramada;
        this.objetivos = objetivos;
        this.cumplimiento = cumplimiento;
        this.justificacion = justificacion;
    }

    public Integer getSesionGrupal() {
        return sesionGrupal;
    }

    public void setSesionGrupal(Integer sesionGrupal) {
        this.sesionGrupal = sesionGrupal;
    }

    public short getNoSesion() {
        return noSesion;
    }

    public void setNoSesion(short noSesion) {
        this.noSesion = noSesion;
    }

    public String getActividadProgramada() {
        return actividadProgramada;
    }

    public void setActividadProgramada(String actividadProgramada) {
        this.actividadProgramada = actividadProgramada;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public boolean getCumplimiento() {
        return cumplimiento;
    }

    public void setCumplimiento(boolean cumplimiento) {
        this.cumplimiento = cumplimiento;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public PlanAccionTutorial getPlanAccionTutoria() {
        return planAccionTutoria;
    }

    public void setPlanAccionTutoria(PlanAccionTutorial planAccionTutoria) {
        this.planAccionTutoria = planAccionTutoria;
    }

    @XmlTransient
    public List<TutoriasIndividuales> getTutoriasIndividualesList() {
        return tutoriasIndividualesList;
    }

    public void setTutoriasIndividualesList(List<TutoriasIndividuales> tutoriasIndividualesList) {
        this.tutoriasIndividualesList = tutoriasIndividualesList;
    }

    @XmlTransient
    public List<TutoriasGrupales> getTutoriasGrupalesList() {
        return tutoriasGrupalesList;
    }

    public void setTutoriasGrupalesList(List<TutoriasGrupales> tutoriasGrupalesList) {
        this.tutoriasGrupalesList = tutoriasGrupalesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sesionGrupal != null ? sesionGrupal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SesionesGrupalesTutorias)) {
            return false;
        }
        SesionesGrupalesTutorias other = (SesionesGrupalesTutorias) object;
        if ((this.sesionGrupal == null && other.sesionGrupal != null) || (this.sesionGrupal != null && !this.sesionGrupal.equals(other.sesionGrupal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias[ sesionGrupal=" + sesionGrupal + " ]";
    }
    
}
