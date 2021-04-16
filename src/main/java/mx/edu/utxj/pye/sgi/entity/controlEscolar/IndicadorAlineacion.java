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
@Table(name = "indicador_alineacion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadorAlineacion.findAll", query = "SELECT i FROM IndicadorAlineacion i")
    , @NamedQuery(name = "IndicadorAlineacion.findByIndicadorPem", query = "SELECT i FROM IndicadorAlineacion i WHERE i.indicadorPem = :indicadorPem")
    , @NamedQuery(name = "IndicadorAlineacion.findByClave", query = "SELECT i FROM IndicadorAlineacion i WHERE i.clave = :clave")})
public class IndicadorAlineacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indicador_pem")
    private Integer indicadorPem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "clave")
    private String clave;
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "plan_estudio", referencedColumnName = "id_plan_estudio")
    @ManyToOne(optional = false)
    private PlanEstudio planEstudio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicadorAlineacion", fetch = FetchType.LAZY)
    private List<IndicadorAlineacionPlanMateria> indicadorAlineacionPlanMateriaList;

    public IndicadorAlineacion() {
    }

    public IndicadorAlineacion(Integer indicadorPem) {
        this.indicadorPem = indicadorPem;
    }

    public IndicadorAlineacion(Integer indicadorPem, String clave) {
        this.indicadorPem = indicadorPem;
        this.clave = clave;
    }

    public Integer getIndicadorPem() {
        return indicadorPem;
    }

    public void setIndicadorPem(Integer indicadorPem) {
        this.indicadorPem = indicadorPem;
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

    @XmlTransient
    public List<IndicadorAlineacionPlanMateria> getIndicadorAlineacionPlanMateriaList() {
        return indicadorAlineacionPlanMateriaList;
    }

    public void setIndicadorAlineacionPlanMateriaList(List<IndicadorAlineacionPlanMateria> indicadorAlineacionPlanMateriaList) {
        this.indicadorAlineacionPlanMateriaList = indicadorAlineacionPlanMateriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadorPem != null ? indicadorPem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorAlineacion)) {
            return false;
        }
        IndicadorAlineacion other = (IndicadorAlineacion) object;
        if ((this.indicadorPem == null && other.indicadorPem != null) || (this.indicadorPem != null && !this.indicadorPem.equals(other.indicadorPem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacion[ indicadorPem=" + indicadorPem + " ]";
    }
    
}
