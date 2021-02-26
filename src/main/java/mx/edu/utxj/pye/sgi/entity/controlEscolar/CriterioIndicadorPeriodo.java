/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
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
@Table(name = "criterio_indicador_periodo", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CriterioIndicadorPeriodo.findAll", query = "SELECT c FROM CriterioIndicadorPeriodo c")
    , @NamedQuery(name = "CriterioIndicadorPeriodo.findByCriterio", query = "SELECT c FROM CriterioIndicadorPeriodo c WHERE c.criterioIndicadorPeriodoPK.criterio = :criterio")
    , @NamedQuery(name = "CriterioIndicadorPeriodo.findByIndicador", query = "SELECT c FROM CriterioIndicadorPeriodo c WHERE c.criterioIndicadorPeriodoPK.indicador = :indicador")
    , @NamedQuery(name = "CriterioIndicadorPeriodo.findByPeriodo", query = "SELECT c FROM CriterioIndicadorPeriodo c WHERE c.criterioIndicadorPeriodoPK.periodo = :periodo")})
public class CriterioIndicadorPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CriterioIndicadorPeriodoPK criterioIndicadorPeriodoPK;
    @JoinColumn(name = "criterio", referencedColumnName = "criterio", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Criterio criterio1;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Indicador indicador1;

    public CriterioIndicadorPeriodo() {
    }

    public CriterioIndicadorPeriodo(CriterioIndicadorPeriodoPK criterioIndicadorPeriodoPK) {
        this.criterioIndicadorPeriodoPK = criterioIndicadorPeriodoPK;
    }

    public CriterioIndicadorPeriodo(int criterio, int indicador, int periodo) {
        this.criterioIndicadorPeriodoPK = new CriterioIndicadorPeriodoPK(criterio, indicador, periodo);
    }

    public CriterioIndicadorPeriodoPK getCriterioIndicadorPeriodoPK() {
        return criterioIndicadorPeriodoPK;
    }

    public void setCriterioIndicadorPeriodoPK(CriterioIndicadorPeriodoPK criterioIndicadorPeriodoPK) {
        this.criterioIndicadorPeriodoPK = criterioIndicadorPeriodoPK;
    }

    public Criterio getCriterio1() {
        return criterio1;
    }

    public void setCriterio1(Criterio criterio1) {
        this.criterio1 = criterio1;
    }

    public Indicador getIndicador1() {
        return indicador1;
    }

    public void setIndicador1(Indicador indicador1) {
        this.indicador1 = indicador1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (criterioIndicadorPeriodoPK != null ? criterioIndicadorPeriodoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CriterioIndicadorPeriodo)) {
            return false;
        }
        CriterioIndicadorPeriodo other = (CriterioIndicadorPeriodo) object;
        if ((this.criterioIndicadorPeriodoPK == null && other.criterioIndicadorPeriodoPK != null) || (this.criterioIndicadorPeriodoPK != null && !this.criterioIndicadorPeriodoPK.equals(other.criterioIndicadorPeriodoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioIndicadorPeriodo[ criterioIndicadorPeriodoPK=" + criterioIndicadorPeriodoPK + " ]";
    }
    
}
