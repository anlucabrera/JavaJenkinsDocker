/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "servicios_enfermeria", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiciosEnfermeria.findAll", query = "SELECT s FROM ServiciosEnfermeria s")
    , @NamedQuery(name = "ServiciosEnfermeria.findByCiclo", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.serviciosEnfermeriaPK.ciclo = :ciclo")
    , @NamedQuery(name = "ServiciosEnfermeria.findByPeriodo", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.serviciosEnfermeriaPK.periodo = :periodo")
    , @NamedQuery(name = "ServiciosEnfermeria.findByMes", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.serviciosEnfermeriaPK.mes = :mes")
    , @NamedQuery(name = "ServiciosEnfermeria.findByTipoServicio", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.serviciosEnfermeriaPK.tipoServicio = :tipoServicio")
    , @NamedQuery(name = "ServiciosEnfermeria.findByEstH", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.estH = :estH")
    , @NamedQuery(name = "ServiciosEnfermeria.findByEstM", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.estM = :estM")
    , @NamedQuery(name = "ServiciosEnfermeria.findByEstTotal", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.estTotal = :estTotal")
    , @NamedQuery(name = "ServiciosEnfermeria.findByPerH", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.perH = :perH")
    , @NamedQuery(name = "ServiciosEnfermeria.findByPerM", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.perM = :perM")
    , @NamedQuery(name = "ServiciosEnfermeria.findByPerTotal", query = "SELECT s FROM ServiciosEnfermeria s WHERE s.perTotal = :perTotal")})
public class ServiciosEnfermeria implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ServiciosEnfermeriaPK serviciosEnfermeriaPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_h")
    private int estH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_m")
    private int estM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_total")
    private int estTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_h")
    private int perH;
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_m")
    private int perM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "per_total")
    private int perTotal;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "mes", referencedColumnName = "numero", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Meses meses;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodosEscolares;

    public ServiciosEnfermeria() {
    }

    public ServiciosEnfermeria(ServiciosEnfermeriaPK serviciosEnfermeriaPK) {
        this.serviciosEnfermeriaPK = serviciosEnfermeriaPK;
    }

    public ServiciosEnfermeria(ServiciosEnfermeriaPK serviciosEnfermeriaPK, int estH, int estM, int estTotal, int perH, int perM, int perTotal) {
        this.serviciosEnfermeriaPK = serviciosEnfermeriaPK;
        this.estH = estH;
        this.estM = estM;
        this.estTotal = estTotal;
        this.perH = perH;
        this.perM = perM;
        this.perTotal = perTotal;
    }

    public ServiciosEnfermeria(int ciclo, int periodo, short mes, String tipoServicio) {
        this.serviciosEnfermeriaPK = new ServiciosEnfermeriaPK(ciclo, periodo, mes, tipoServicio);
    }

    public ServiciosEnfermeriaPK getServiciosEnfermeriaPK() {
        return serviciosEnfermeriaPK;
    }

    public void setServiciosEnfermeriaPK(ServiciosEnfermeriaPK serviciosEnfermeriaPK) {
        this.serviciosEnfermeriaPK = serviciosEnfermeriaPK;
    }

    public int getEstH() {
        return estH;
    }

    public void setEstH(int estH) {
        this.estH = estH;
    }

    public int getEstM() {
        return estM;
    }

    public void setEstM(int estM) {
        this.estM = estM;
    }

    public int getEstTotal() {
        return estTotal;
    }

    public void setEstTotal(int estTotal) {
        this.estTotal = estTotal;
    }

    public int getPerH() {
        return perH;
    }

    public void setPerH(int perH) {
        this.perH = perH;
    }

    public int getPerM() {
        return perM;
    }

    public void setPerM(int perM) {
        this.perM = perM;
    }

    public int getPerTotal() {
        return perTotal;
    }

    public void setPerTotal(int perTotal) {
        this.perTotal = perTotal;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public Meses getMeses() {
        return meses;
    }

    public void setMeses(Meses meses) {
        this.meses = meses;
    }

    public PeriodosEscolares getPeriodosEscolares() {
        return periodosEscolares;
    }

    public void setPeriodosEscolares(PeriodosEscolares periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (serviciosEnfermeriaPK != null ? serviciosEnfermeriaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiciosEnfermeria)) {
            return false;
        }
        ServiciosEnfermeria other = (ServiciosEnfermeria) object;
        if ((this.serviciosEnfermeriaPK == null && other.serviciosEnfermeriaPK != null) || (this.serviciosEnfermeriaPK != null && !this.serviciosEnfermeriaPK.equals(other.serviciosEnfermeriaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ServiciosEnfermeria[ serviciosEnfermeriaPK=" + serviciosEnfermeriaPK + " ]";
    }
    
}
