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
@Table(name = "sesiones_grupales_tutorias_plantilla", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SesionesGrupalesTutoriasPlantilla.findAll", query = "SELECT s FROM SesionesGrupalesTutoriasPlantilla s")
    , @NamedQuery(name = "SesionesGrupalesTutoriasPlantilla.findBySesionGrupalPlantilla", query = "SELECT s FROM SesionesGrupalesTutoriasPlantilla s WHERE s.sesionGrupalPlantilla = :sesionGrupalPlantilla")
    , @NamedQuery(name = "SesionesGrupalesTutoriasPlantilla.findByNoSesion", query = "SELECT s FROM SesionesGrupalesTutoriasPlantilla s WHERE s.noSesion = :noSesion")
    , @NamedQuery(name = "SesionesGrupalesTutoriasPlantilla.findByActividadProgramada", query = "SELECT s FROM SesionesGrupalesTutoriasPlantilla s WHERE s.actividadProgramada = :actividadProgramada")
    , @NamedQuery(name = "SesionesGrupalesTutoriasPlantilla.findByObjetivos", query = "SELECT s FROM SesionesGrupalesTutoriasPlantilla s WHERE s.objetivos = :objetivos")})
public class SesionesGrupalesTutoriasPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sesion_grupal_plantilla")
    private Integer sesionGrupalPlantilla;
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
    @JoinColumn(name = "plan_accion_tutoria_plantilla", referencedColumnName = "grado")
    @ManyToOne(optional = false)
    private PlanAccionTutorialPlantilla planAccionTutoriaPlantilla;

    public SesionesGrupalesTutoriasPlantilla() {
    }

    public SesionesGrupalesTutoriasPlantilla(Integer sesionGrupalPlantilla) {
        this.sesionGrupalPlantilla = sesionGrupalPlantilla;
    }

    public SesionesGrupalesTutoriasPlantilla(Integer sesionGrupalPlantilla, short noSesion, String actividadProgramada, String objetivos) {
        this.sesionGrupalPlantilla = sesionGrupalPlantilla;
        this.noSesion = noSesion;
        this.actividadProgramada = actividadProgramada;
        this.objetivos = objetivos;
    }

    public Integer getSesionGrupalPlantilla() {
        return sesionGrupalPlantilla;
    }

    public void setSesionGrupalPlantilla(Integer sesionGrupalPlantilla) {
        this.sesionGrupalPlantilla = sesionGrupalPlantilla;
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

    public PlanAccionTutorialPlantilla getPlanAccionTutoriaPlantilla() {
        return planAccionTutoriaPlantilla;
    }

    public void setPlanAccionTutoriaPlantilla(PlanAccionTutorialPlantilla planAccionTutoriaPlantilla) {
        this.planAccionTutoriaPlantilla = planAccionTutoriaPlantilla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sesionGrupalPlantilla != null ? sesionGrupalPlantilla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SesionesGrupalesTutoriasPlantilla)) {
            return false;
        }
        SesionesGrupalesTutoriasPlantilla other = (SesionesGrupalesTutoriasPlantilla) object;
        if ((this.sesionGrupalPlantilla == null && other.sesionGrupalPlantilla != null) || (this.sesionGrupalPlantilla != null && !this.sesionGrupalPlantilla.equals(other.sesionGrupalPlantilla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutoriasPlantilla[ sesionGrupalPlantilla=" + sesionGrupalPlantilla + " ]";
    }
    
}
