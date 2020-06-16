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
@Table(name = "plan_accion_tutorial_plantilla", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanAccionTutorialPlantilla.findAll", query = "SELECT p FROM PlanAccionTutorialPlantilla p")
    , @NamedQuery(name = "PlanAccionTutorialPlantilla.findByGrado", query = "SELECT p FROM PlanAccionTutorialPlantilla p WHERE p.grado = :grado")
    , @NamedQuery(name = "PlanAccionTutorialPlantilla.findByObjetivo", query = "SELECT p FROM PlanAccionTutorialPlantilla p WHERE p.objetivo = :objetivo")
    , @NamedQuery(name = "PlanAccionTutorialPlantilla.findByComentarios", query = "SELECT p FROM PlanAccionTutorialPlantilla p WHERE p.comentarios = :comentarios")
    , @NamedQuery(name = "PlanAccionTutorialPlantilla.findBySugerencias", query = "SELECT p FROM PlanAccionTutorialPlantilla p WHERE p.sugerencias = :sugerencias")})
public class PlanAccionTutorialPlantilla implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "grado")
    private String grado;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planAccionTutoriaPlantilla")
    private List<SesionesGrupalesTutoriasPlantilla> sesionesGrupalesTutoriasPlantillaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planAccionTutoriaPlantilla")
    private List<FuncionesTutorPlantilla> funcionesTutorPlantillaList;

    public PlanAccionTutorialPlantilla() {
    }

    public PlanAccionTutorialPlantilla(String grado) {
        this.grado = grado;
    }

    public PlanAccionTutorialPlantilla(String grado, String objetivo, String comentarios, String sugerencias) {
        this.grado = grado;
        this.objetivo = objetivo;
        this.comentarios = comentarios;
        this.sugerencias = sugerencias;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
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

    @XmlTransient
    public List<SesionesGrupalesTutoriasPlantilla> getSesionesGrupalesTutoriasPlantillaList() {
        return sesionesGrupalesTutoriasPlantillaList;
    }

    public void setSesionesGrupalesTutoriasPlantillaList(List<SesionesGrupalesTutoriasPlantilla> sesionesGrupalesTutoriasPlantillaList) {
        this.sesionesGrupalesTutoriasPlantillaList = sesionesGrupalesTutoriasPlantillaList;
    }

    @XmlTransient
    public List<FuncionesTutorPlantilla> getFuncionesTutorPlantillaList() {
        return funcionesTutorPlantillaList;
    }

    public void setFuncionesTutorPlantillaList(List<FuncionesTutorPlantilla> funcionesTutorPlantillaList) {
        this.funcionesTutorPlantillaList = funcionesTutorPlantillaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grado != null ? grado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanAccionTutorialPlantilla)) {
            return false;
        }
        PlanAccionTutorialPlantilla other = (PlanAccionTutorialPlantilla) object;
        if ((this.grado == null && other.grado != null) || (this.grado != null && !this.grado.equals(other.grado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorialPlantilla[ grado=" + grado + " ]";
    }
    
}
