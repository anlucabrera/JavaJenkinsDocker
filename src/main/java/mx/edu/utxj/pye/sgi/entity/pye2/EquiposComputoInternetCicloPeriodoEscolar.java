/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "equipos_computo_internet_ciclo_periodo_escolar", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findAll", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByRegistro", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.registro = :registro")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByCicloEscolar", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.cicloEscolar = :cicloEscolar")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByPeriodoEscolar", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.periodoEscolar = :periodoEscolar")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByDtcEsc", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.dtcEsc = :dtcEsc")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByDtcPort", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.dtcPort = :dtcPort")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByDaEsc", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.daEsc = :daEsc")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByDaPort", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.daPort = :daPort")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByEstEsc", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.estEsc = :estEsc")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByEstPor", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.estPor = :estPor")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByAdmEsc", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.admEsc = :admEsc")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByAdmPort", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.admPort = :admPort")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByMmsEsc", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.mmsEsc = :mmsEsc")
    , @NamedQuery(name = "EquiposComputoInternetCicloPeriodoEscolar.findByMmsPort", query = "SELECT e FROM EquiposComputoInternetCicloPeriodoEscolar e WHERE e.mmsPort = :mmsPort")})
public class EquiposComputoInternetCicloPeriodoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "registro")
    private Integer registro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ciclo_escolar")
    private int cicloEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "periodo_escolar")
    private int periodoEscolar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_esc")
    private int dtcEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_port")
    private int dtcPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "da_esc")
    private int daEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "da_port")
    private int daPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_esc")
    private int estEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "est_por")
    private int estPor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_esc")
    private int admEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "adm_port")
    private int admPort;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mms_esc")
    private int mmsEsc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mms_port")
    private int mmsPort;
    @JoinColumn(name = "registro", referencedColumnName = "registro", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Registros registros;

    public EquiposComputoInternetCicloPeriodoEscolar() {
    }

    public EquiposComputoInternetCicloPeriodoEscolar(Integer registro) {
        this.registro = registro;
    }

    public EquiposComputoInternetCicloPeriodoEscolar(Integer registro, int cicloEscolar, int periodoEscolar, int dtcEsc, int dtcPort, int daEsc, int daPort, int estEsc, int estPor, int admEsc, int admPort, int mmsEsc, int mmsPort) {
        this.registro = registro;
        this.cicloEscolar = cicloEscolar;
        this.periodoEscolar = periodoEscolar;
        this.dtcEsc = dtcEsc;
        this.dtcPort = dtcPort;
        this.daEsc = daEsc;
        this.daPort = daPort;
        this.estEsc = estEsc;
        this.estPor = estPor;
        this.admEsc = admEsc;
        this.admPort = admPort;
        this.mmsEsc = mmsEsc;
        this.mmsPort = mmsPort;
    }

    public Integer getRegistro() {
        return registro;
    }

    public void setRegistro(Integer registro) {
        this.registro = registro;
    }

    public int getCicloEscolar() {
        return cicloEscolar;
    }

    public void setCicloEscolar(int cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public int getPeriodoEscolar() {
        return periodoEscolar;
    }

    public void setPeriodoEscolar(int periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public int getDtcEsc() {
        return dtcEsc;
    }

    public void setDtcEsc(int dtcEsc) {
        this.dtcEsc = dtcEsc;
    }

    public int getDtcPort() {
        return dtcPort;
    }

    public void setDtcPort(int dtcPort) {
        this.dtcPort = dtcPort;
    }

    public int getDaEsc() {
        return daEsc;
    }

    public void setDaEsc(int daEsc) {
        this.daEsc = daEsc;
    }

    public int getDaPort() {
        return daPort;
    }

    public void setDaPort(int daPort) {
        this.daPort = daPort;
    }

    public int getEstEsc() {
        return estEsc;
    }

    public void setEstEsc(int estEsc) {
        this.estEsc = estEsc;
    }

    public int getEstPor() {
        return estPor;
    }

    public void setEstPor(int estPor) {
        this.estPor = estPor;
    }

    public int getAdmEsc() {
        return admEsc;
    }

    public void setAdmEsc(int admEsc) {
        this.admEsc = admEsc;
    }

    public int getAdmPort() {
        return admPort;
    }

    public void setAdmPort(int admPort) {
        this.admPort = admPort;
    }

    public int getMmsEsc() {
        return mmsEsc;
    }

    public void setMmsEsc(int mmsEsc) {
        this.mmsEsc = mmsEsc;
    }

    public int getMmsPort() {
        return mmsPort;
    }

    public void setMmsPort(int mmsPort) {
        this.mmsPort = mmsPort;
    }

    public Registros getRegistros() {
        return registros;
    }

    public void setRegistros(Registros registros) {
        this.registros = registros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registro != null ? registro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquiposComputoInternetCicloPeriodoEscolar)) {
            return false;
        }
        EquiposComputoInternetCicloPeriodoEscolar other = (EquiposComputoInternetCicloPeriodoEscolar) object;
        if ((this.registro == null && other.registro != null) || (this.registro != null && !this.registro.equals(other.registro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoInternetCicloPeriodoEscolar[ registro=" + registro + " ]";
    }
    
}
