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
@Table(name = "criterio_desempenio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CriterioDesempenio.findAll", query = "SELECT c FROM CriterioDesempenio c")
    , @NamedQuery(name = "CriterioDesempenio.findByCriteriDesempenio", query = "SELECT c FROM CriterioDesempenio c WHERE c.criteriDesempenio = :criteriDesempenio")
    , @NamedQuery(name = "CriterioDesempenio.findByClave", query = "SELECT c FROM CriterioDesempenio c WHERE c.clave = :clave")})
public class CriterioDesempenio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "criteri_desempenio")
    private Integer criteriDesempenio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave")
    private String clave;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterioDesempenio1")
    private List<CriterioDesempenioPlanMateria> criterioDesempenioPlanMateriaList;
    @JoinColumn(name = "plan_estudio", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudio planEstudio;

    public CriterioDesempenio() {
    }

    public CriterioDesempenio(Integer criteriDesempenio) {
        this.criteriDesempenio = criteriDesempenio;
    }

    public CriterioDesempenio(Integer criteriDesempenio, String clave) {
        this.criteriDesempenio = criteriDesempenio;
        this.clave = clave;
    }

    public Integer getCriteriDesempenio() {
        return criteriDesempenio;
    }

    public void setCriteriDesempenio(Integer criteriDesempenio) {
        this.criteriDesempenio = criteriDesempenio;
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

    public PlanEstudio getPlanEstudio() {
        return planEstudio;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (criteriDesempenio != null ? criteriDesempenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioDesempenio)) {
            return false;
        }
        CriterioDesempenio other = (CriterioDesempenio) object;
        if ((this.criteriDesempenio == null && other.criteriDesempenio != null) || (this.criteriDesempenio != null && !this.criteriDesempenio.equals(other.criteriDesempenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioDesempenio[ criteriDesempenio=" + criteriDesempenio + " ]";
    }

    @XmlTransient
    public List<CriterioDesempenioPlanMateria> getCriterioDesempenioPlanMateriaList() {
        return criterioDesempenioPlanMateriaList;
    }

    public void setCriterioDesempenioPlanMateriaList(List<CriterioDesempenioPlanMateria> criterioDesempenioPlanMateriaList) {
        this.criterioDesempenioPlanMateriaList = criterioDesempenioPlanMateriaList;
    }
    
}
