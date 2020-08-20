/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "periodo_escolar_fechas", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEscolarFechas.findAll", query = "SELECT p FROM PeriodoEscolarFechas p")
    , @NamedQuery(name = "PeriodoEscolarFechas.findByPeriodo", query = "SELECT p FROM PeriodoEscolarFechas p WHERE p.periodo = :periodo")
    , @NamedQuery(name = "PeriodoEscolarFechas.findByInicio", query = "SELECT p FROM PeriodoEscolarFechas p WHERE p.inicio = :inicio")
    , @NamedQuery(name = "PeriodoEscolarFechas.findByFin", query = "SELECT p FROM PeriodoEscolarFechas p WHERE p.fin = :fin")})
public class PeriodoEscolarFechas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo")
    private Integer periodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodosEscolares;

    public PeriodoEscolarFechas() {
    }

    public PeriodoEscolarFechas(Integer periodo) {
        this.periodo = periodo;
    }

    public PeriodoEscolarFechas(Integer periodo, Date inicio, Date fin) {
        this.periodo = periodo;
        this.inicio = inicio;
        this.fin = fin;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
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
        hash += (periodo != null ? periodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoEscolarFechas)) {
            return false;
        }
        PeriodoEscolarFechas other = (PeriodoEscolarFechas) object;
        if ((this.periodo == null && other.periodo != null) || (this.periodo != null && !this.periodo.equals(other.periodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas[ periodo=" + periodo + " ]";
    }
    
}
