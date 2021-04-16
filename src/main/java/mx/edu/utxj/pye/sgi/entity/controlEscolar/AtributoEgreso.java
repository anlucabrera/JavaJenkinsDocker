/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "atributo_egreso", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AtributoEgreso.findAll", query = "SELECT a FROM AtributoEgreso a")
    , @NamedQuery(name = "AtributoEgreso.findByAtributoEgreso", query = "SELECT a FROM AtributoEgreso a WHERE a.atributoEgreso = :atributoEgreso")
    , @NamedQuery(name = "AtributoEgreso.findByClave", query = "SELECT a FROM AtributoEgreso a WHERE a.clave = :clave")})
public class AtributoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "atributo_egreso")
    private Integer atributoEgreso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave")
    private String clave;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinTable(name = "control_escolar.atributo_egreso_plan_materia", joinColumns = {
        @JoinColumn(name = "atributo_egreso", referencedColumnName = "atributo_egreso")}, inverseJoinColumns = {
        @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia")})
    @ManyToMany
    private List<PlanEstudioMateria> planEstudioMateriaList;
    @JoinColumn(name = "plan_estudio", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudio planEstudio;

    public AtributoEgreso() {
    }

    public AtributoEgreso(Integer atributoEgreso) {
        this.atributoEgreso = atributoEgreso;
    }

    public AtributoEgreso(Integer atributoEgreso, String clave) {
        this.atributoEgreso = atributoEgreso;
        this.clave = clave;
    }

    public Integer getAtributoEgreso() {
        return atributoEgreso;
    }

    public void setAtributoEgreso(Integer atributoEgreso) {
        this.atributoEgreso = atributoEgreso;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<PlanEstudioMateria> getPlanEstudioMateriaList() {
        return planEstudioMateriaList;
    }

    public void setPlanEstudioMateriaList(List<PlanEstudioMateria> planEstudioMateriaList) {
        this.planEstudioMateriaList = planEstudioMateriaList;
    }

    public PlanEstudio getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (atributoEgreso != null ? atributoEgreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AtributoEgreso)) {
            return false;
        }
        AtributoEgreso other = (AtributoEgreso) object;
        if ((this.atributoEgreso == null && other.atributoEgreso != null) || (this.atributoEgreso != null && !this.atributoEgreso.equals(other.atributoEgreso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.AtributoEgreso[ atributoEgreso=" + atributoEgreso + " ]";
    }
    
}
