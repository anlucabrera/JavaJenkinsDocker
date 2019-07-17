/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "unidad_materia_configuracion_criterio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaConfiguracionCriterio.findAll", query = "SELECT u FROM UnidadMateriaConfiguracionCriterio u")
    , @NamedQuery(name = "UnidadMateriaConfiguracionCriterio.findByConfiguracion", query = "SELECT u FROM UnidadMateriaConfiguracionCriterio u WHERE u.unidadMateriaConfiguracionCriterioPK.configuracion = :configuracion")
    , @NamedQuery(name = "UnidadMateriaConfiguracionCriterio.findByCriterio", query = "SELECT u FROM UnidadMateriaConfiguracionCriterio u WHERE u.unidadMateriaConfiguracionCriterioPK.criterio = :criterio")
    , @NamedQuery(name = "UnidadMateriaConfiguracionCriterio.findByPorcentaje", query = "SELECT u FROM UnidadMateriaConfiguracionCriterio u WHERE u.porcentaje = :porcentaje")})
public class UnidadMateriaConfiguracionCriterio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UnidadMateriaConfiguracionCriterioPK unidadMateriaConfiguracionCriterioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UnidadMateriaConfiguracion unidadMateriaConfiguracion;
    @JoinColumn(name = "criterio", referencedColumnName = "criterio", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Criterio criterio1;

    public UnidadMateriaConfiguracionCriterio() {
    }

    public UnidadMateriaConfiguracionCriterio(UnidadMateriaConfiguracionCriterioPK unidadMateriaConfiguracionCriterioPK) {
        this.unidadMateriaConfiguracionCriterioPK = unidadMateriaConfiguracionCriterioPK;
    }

    public UnidadMateriaConfiguracionCriterio(UnidadMateriaConfiguracionCriterioPK unidadMateriaConfiguracionCriterioPK, double porcentaje) {
        this.unidadMateriaConfiguracionCriterioPK = unidadMateriaConfiguracionCriterioPK;
        this.porcentaje = porcentaje;
    }

    public UnidadMateriaConfiguracionCriterio(int configuracion, int criterio) {
        this.unidadMateriaConfiguracionCriterioPK = new UnidadMateriaConfiguracionCriterioPK(configuracion, criterio);
    }

    public UnidadMateriaConfiguracionCriterioPK getUnidadMateriaConfiguracionCriterioPK() {
        return unidadMateriaConfiguracionCriterioPK;
    }

    public void setUnidadMateriaConfiguracionCriterioPK(UnidadMateriaConfiguracionCriterioPK unidadMateriaConfiguracionCriterioPK) {
        this.unidadMateriaConfiguracionCriterioPK = unidadMateriaConfiguracionCriterioPK;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public UnidadMateriaConfiguracion getUnidadMateriaConfiguracion() {
        return unidadMateriaConfiguracion;
    }

    public void setUnidadMateriaConfiguracion(UnidadMateriaConfiguracion unidadMateriaConfiguracion) {
        this.unidadMateriaConfiguracion = unidadMateriaConfiguracion;
    }

    public Criterio getCriterio1() {
        return criterio1;
    }

    public void setCriterio1(Criterio criterio1) {
        this.criterio1 = criterio1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unidadMateriaConfiguracionCriterioPK != null ? unidadMateriaConfiguracionCriterioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaConfiguracionCriterio)) {
            return false;
        }
        UnidadMateriaConfiguracionCriterio other = (UnidadMateriaConfiguracionCriterio) object;
        if ((this.unidadMateriaConfiguracionCriterioPK == null && other.unidadMateriaConfiguracionCriterioPK != null) || (this.unidadMateriaConfiguracionCriterioPK != null && !this.unidadMateriaConfiguracionCriterioPK.equals(other.unidadMateriaConfiguracionCriterioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionCriterio[ unidadMateriaConfiguracionCriterioPK=" + unidadMateriaConfiguracionCriterioPK + " ]";
    }
    
}
