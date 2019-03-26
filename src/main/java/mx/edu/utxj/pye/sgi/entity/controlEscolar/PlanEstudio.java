/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "plan_estudio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanEstudio.findAll", query = "SELECT p FROM PlanEstudio p"),
    @NamedQuery(name = "PlanEstudio.findByIdPlanEstudio", query = "SELECT p FROM PlanEstudio p WHERE p.idPlanEstudio = :idPlanEstudio"),
    @NamedQuery(name = "PlanEstudio.findByDescripcion", query = "SELECT p FROM PlanEstudio p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "PlanEstudio.findByAnio", query = "SELECT p FROM PlanEstudio p WHERE p.anio = :anio"),
    @NamedQuery(name = "PlanEstudio.findByEstatus", query = "SELECT p FROM PlanEstudio p WHERE p.estatus = :estatus"),
    @NamedQuery(name = "PlanEstudio.findByIdPe", query = "SELECT p FROM PlanEstudio p WHERE p.idPe = :idPe")})
public class PlanEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_plan_estudio")
    private Integer idPlanEstudio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "anio")
    @Temporal(TemporalType.DATE)
    private Date anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @Column(name = "id_pe")
    private Integer idPe;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPlan")
    private List<Materia> materiaList;

    public PlanEstudio() {
    }

    public PlanEstudio(Integer idPlanEstudio) {
        this.idPlanEstudio = idPlanEstudio;
    }

    public PlanEstudio(Integer idPlanEstudio, String descripcion, Date anio, boolean estatus) {
        this.idPlanEstudio = idPlanEstudio;
        this.descripcion = descripcion;
        this.anio = anio;
        this.estatus = estatus;
    }

    public Integer getIdPlanEstudio() {
        return idPlanEstudio;
    }

    public void setIdPlanEstudio(Integer idPlanEstudio) {
        this.idPlanEstudio = idPlanEstudio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getAnio() {
        return anio;
    }

    public void setAnio(Date anio) {
        this.anio = anio;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Integer getIdPe() {
        return idPe;
    }

    public void setIdPe(Integer idPe) {
        this.idPe = idPe;
    }

    @XmlTransient
    public List<Materia> getMateriaList() {
        return materiaList;
    }

    public void setMateriaList(List<Materia> materiaList) {
        this.materiaList = materiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlanEstudio != null ? idPlanEstudio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanEstudio)) {
            return false;
        }
        PlanEstudio other = (PlanEstudio) object;
        if ((this.idPlanEstudio == null && other.idPlanEstudio != null) || (this.idPlanEstudio != null && !this.idPlanEstudio.equals(other.idPlanEstudio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio[ idPlanEstudio=" + idPlanEstudio + " ]";
    }
    
}
