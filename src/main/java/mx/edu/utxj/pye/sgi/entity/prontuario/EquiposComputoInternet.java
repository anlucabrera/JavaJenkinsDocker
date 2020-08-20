/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "equipos_computo_internet", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquiposComputoInternet.findAll", query = "SELECT e FROM EquiposComputoInternet e")
    , @NamedQuery(name = "EquiposComputoInternet.findByCiclo", query = "SELECT e FROM EquiposComputoInternet e WHERE e.ciclo = :ciclo")
    , @NamedQuery(name = "EquiposComputoInternet.findByDtcEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.dtcEsc = :dtcEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByDtcPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.dtcPor = :dtcPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByDasigEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.dasigEsc = :dasigEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByDasigPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.dasigPor = :dasigPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByEstEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.estEsc = :estEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByEstPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.estPor = :estPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByAdmEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.admEsc = :admEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByAdmPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.admPor = :admPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByMmysEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.mmysEsc = :mmysEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByMmysPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.mmysPor = :mmysPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByTotalEsc", query = "SELECT e FROM EquiposComputoInternet e WHERE e.totalEsc = :totalEsc")
    , @NamedQuery(name = "EquiposComputoInternet.findByTotalPor", query = "SELECT e FROM EquiposComputoInternet e WHERE e.totalPor = :totalPor")
    , @NamedQuery(name = "EquiposComputoInternet.findByTotal", query = "SELECT e FROM EquiposComputoInternet e WHERE e.total = :total")})
public class EquiposComputoInternet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo")
    private Integer ciclo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_esc")
    private short dtcEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_por")
    private short dtcPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dasig_esc")
    private short dasigEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dasig_por")
    private short dasigPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_esc")
    private short estEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_por")
    private short estPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_esc")
    private short admEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_por")
    private short admPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mmys_esc")
    private short mmysEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mmys_por")
    private short mmysPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_esc")
    private short totalEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_por")
    private short totalPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private short total;
    @JoinColumn(name = "ciclo", referencedColumnName = "ciclo", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private CiclosEscolares ciclosEscolares;
    @JoinColumn(name = "periodo", referencedColumnName = "periodo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PeriodosEscolares periodo;

    public EquiposComputoInternet() {
    }

    public EquiposComputoInternet(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public EquiposComputoInternet(Integer ciclo, short dtcEsc, short dtcPor, short dasigEsc, short dasigPor, short estEsc, short estPor, short admEsc, short admPor, short mmysEsc, short mmysPor, short totalEsc, short totalPor, short total) {
        this.ciclo = ciclo;
        this.dtcEsc = dtcEsc;
        this.dtcPor = dtcPor;
        this.dasigEsc = dasigEsc;
        this.dasigPor = dasigPor;
        this.estEsc = estEsc;
        this.estPor = estPor;
        this.admEsc = admEsc;
        this.admPor = admPor;
        this.mmysEsc = mmysEsc;
        this.mmysPor = mmysPor;
        this.totalEsc = totalEsc;
        this.totalPor = totalPor;
        this.total = total;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public short getDtcEsc() {
        return dtcEsc;
    }

    public void setDtcEsc(short dtcEsc) {
        this.dtcEsc = dtcEsc;
    }

    public short getDtcPor() {
        return dtcPor;
    }

    public void setDtcPor(short dtcPor) {
        this.dtcPor = dtcPor;
    }

    public short getDasigEsc() {
        return dasigEsc;
    }

    public void setDasigEsc(short dasigEsc) {
        this.dasigEsc = dasigEsc;
    }

    public short getDasigPor() {
        return dasigPor;
    }

    public void setDasigPor(short dasigPor) {
        this.dasigPor = dasigPor;
    }

    public short getEstEsc() {
        return estEsc;
    }

    public void setEstEsc(short estEsc) {
        this.estEsc = estEsc;
    }

    public short getEstPor() {
        return estPor;
    }

    public void setEstPor(short estPor) {
        this.estPor = estPor;
    }

    public short getAdmEsc() {
        return admEsc;
    }

    public void setAdmEsc(short admEsc) {
        this.admEsc = admEsc;
    }

    public short getAdmPor() {
        return admPor;
    }

    public void setAdmPor(short admPor) {
        this.admPor = admPor;
    }

    public short getMmysEsc() {
        return mmysEsc;
    }

    public void setMmysEsc(short mmysEsc) {
        this.mmysEsc = mmysEsc;
    }

    public short getMmysPor() {
        return mmysPor;
    }

    public void setMmysPor(short mmysPor) {
        this.mmysPor = mmysPor;
    }

    public short getTotalEsc() {
        return totalEsc;
    }

    public void setTotalEsc(short totalEsc) {
        this.totalEsc = totalEsc;
    }

    public short getTotalPor() {
        return totalPor;
    }

    public void setTotalPor(short totalPor) {
        this.totalPor = totalPor;
    }

    public short getTotal() {
        return total;
    }

    public void setTotal(short total) {
        this.total = total;
    }

    public CiclosEscolares getCiclosEscolares() {
        return ciclosEscolares;
    }

    public void setCiclosEscolares(CiclosEscolares ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public PeriodosEscolares getPeriodo() {
        return periodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciclo != null ? ciclo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquiposComputoInternet)) {
            return false;
        }
        EquiposComputoInternet other = (EquiposComputoInternet) object;
        if ((this.ciclo == null && other.ciclo != null) || (this.ciclo != null && !this.ciclo.equals(other.ciclo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.EquiposComputoInternet[ ciclo=" + ciclo + " ]";
    }
    
}
