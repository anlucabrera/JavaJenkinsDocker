/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "indicador_alineacion_plan_materia", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadorAlineacionPlanMateria.findAll", query = "SELECT i FROM IndicadorAlineacionPlanMateria i")
    , @NamedQuery(name = "IndicadorAlineacionPlanMateria.findByIndicador", query = "SELECT i FROM IndicadorAlineacionPlanMateria i WHERE i.indicadorAlineacionPlanMateriaPK.indicador = :indicador")
    , @NamedQuery(name = "IndicadorAlineacionPlanMateria.findByIdPlanMateria", query = "SELECT i FROM IndicadorAlineacionPlanMateria i WHERE i.indicadorAlineacionPlanMateriaPK.idPlanMateria = :idPlanMateria")
    , @NamedQuery(name = "IndicadorAlineacionPlanMateria.findByMetaIndicador", query = "SELECT i FROM IndicadorAlineacionPlanMateria i WHERE i.metaIndicador = :metaIndicador")})
public class IndicadorAlineacionPlanMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IndicadorAlineacionPlanMateriaPK indicadorAlineacionPlanMateriaPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "meta_indicador")
    private Double metaIndicador;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador_pem", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private IndicadorAlineacion indicadorAlineacion;
    @JoinColumn(name = "id_plan_materia", referencedColumnName = "id_plan_materia", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanEstudioMateria planEstudioMateria;

    public IndicadorAlineacionPlanMateria() {
    }

    public IndicadorAlineacionPlanMateria(IndicadorAlineacionPlanMateriaPK indicadorAlineacionPlanMateriaPK) {
        this.indicadorAlineacionPlanMateriaPK = indicadorAlineacionPlanMateriaPK;
    }

    public IndicadorAlineacionPlanMateria(int indicador, int idPlanMateria) {
        this.indicadorAlineacionPlanMateriaPK = new IndicadorAlineacionPlanMateriaPK(indicador, idPlanMateria);
    }

    public IndicadorAlineacionPlanMateriaPK getIndicadorAlineacionPlanMateriaPK() {
        return indicadorAlineacionPlanMateriaPK;
    }

    public void setIndicadorAlineacionPlanMateriaPK(IndicadorAlineacionPlanMateriaPK indicadorAlineacionPlanMateriaPK) {
        this.indicadorAlineacionPlanMateriaPK = indicadorAlineacionPlanMateriaPK;
    }

    public Double getMetaIndicador() {
        return metaIndicador;
    }

    public void setMetaIndicador(Double metaIndicador) {
        this.metaIndicador = metaIndicador;
    }

    public IndicadorAlineacion getIndicadorAlineacion() {
        return indicadorAlineacion;
    }

    public void setIndicadorAlineacion(IndicadorAlineacion indicadorAlineacion) {
        this.indicadorAlineacion = indicadorAlineacion;
    }

    public PlanEstudioMateria getPlanEstudioMateria() {
        return planEstudioMateria;
    }

    public void setPlanEstudioMateria(PlanEstudioMateria planEstudioMateria) {
        this.planEstudioMateria = planEstudioMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadorAlineacionPlanMateriaPK != null ? indicadorAlineacionPlanMateriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorAlineacionPlanMateria)) {
            return false;
        }
        IndicadorAlineacionPlanMateria other = (IndicadorAlineacionPlanMateria) object;
        if ((this.indicadorAlineacionPlanMateriaPK == null && other.indicadorAlineacionPlanMateriaPK != null) || (this.indicadorAlineacionPlanMateriaPK != null && !this.indicadorAlineacionPlanMateriaPK.equals(other.indicadorAlineacionPlanMateriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.IndicadorAlineacionPlanMateria[ indicadorAlineacionPlanMateriaPK=" + indicadorAlineacionPlanMateriaPK + " ]";
    }
    
}
