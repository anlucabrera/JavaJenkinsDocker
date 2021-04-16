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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "objetivo_educacional", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjetivoEducacional.findAll", query = "SELECT o FROM ObjetivoEducacional o")
    , @NamedQuery(name = "ObjetivoEducacional.findByObjetivoEducacional", query = "SELECT o FROM ObjetivoEducacional o WHERE o.objetivoEducacional = :objetivoEducacional")
    , @NamedQuery(name = "ObjetivoEducacional.findByClave", query = "SELECT o FROM ObjetivoEducacional o WHERE o.clave = :clave")})
public class ObjetivoEducacional implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "objetivo_educacional")
    private Integer objetivoEducacional;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave")
    private String clave;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objetivoEducacional1")
    private List<ObjetivoEducacionalPlanMateria> objetivoEducacionalPlanMateriaList;
    @JoinColumn(name = "plan_estudio", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudio planEstudio;

    public ObjetivoEducacional() {
    }

    public ObjetivoEducacional(Integer objetivoEducacional) {
        this.objetivoEducacional = objetivoEducacional;
    }

    public ObjetivoEducacional(Integer objetivoEducacional, String clave) {
        this.objetivoEducacional = objetivoEducacional;
        this.clave = clave;
    }

    public Integer getObjetivoEducacional() {
        return objetivoEducacional;
    }

    public void setObjetivoEducacional(Integer objetivoEducacional) {
        this.objetivoEducacional = objetivoEducacional;
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
    public List<ObjetivoEducacionalPlanMateria> getObjetivoEducacionalPlanMateriaList() {
        return objetivoEducacionalPlanMateriaList;
    }

    public void setObjetivoEducacionalPlanMateriaList(List<ObjetivoEducacionalPlanMateria> objetivoEducacionalPlanMateriaList) {
        this.objetivoEducacionalPlanMateriaList = objetivoEducacionalPlanMateriaList;
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
        hash += (objetivoEducacional != null ? objetivoEducacional.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetivoEducacional)) {
            return false;
        }
        ObjetivoEducacional other = (ObjetivoEducacional) object;
        if ((this.objetivoEducacional == null && other.objetivoEducacional != null) || (this.objetivoEducacional != null && !this.objetivoEducacional.equals(other.objetivoEducacional))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ObjetivoEducacional[ objetivoEducacional=" + objetivoEducacional + " ]";
    }
    
}
