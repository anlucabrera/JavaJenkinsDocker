/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tarifas_por_zona", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarifasPorZona.findAll", query = "SELECT t FROM TarifasPorZona t")
    , @NamedQuery(name = "TarifasPorZona.findByTarifa", query = "SELECT t FROM TarifasPorZona t WHERE t.tarifa = :tarifa")
    , @NamedQuery(name = "TarifasPorZona.findByZona1SinPernoctar", query = "SELECT t FROM TarifasPorZona t WHERE t.zona1SinPernoctar = :zona1SinPernoctar")
    , @NamedQuery(name = "TarifasPorZona.findByZona2SinPernoctar", query = "SELECT t FROM TarifasPorZona t WHERE t.zona2SinPernoctar = :zona2SinPernoctar")
    , @NamedQuery(name = "TarifasPorZona.findByZona3SinPernoctar", query = "SELECT t FROM TarifasPorZona t WHERE t.zona3SinPernoctar = :zona3SinPernoctar")
    , @NamedQuery(name = "TarifasPorZona.findByZona4SinPernoctar", query = "SELECT t FROM TarifasPorZona t WHERE t.zona4SinPernoctar = :zona4SinPernoctar")
    , @NamedQuery(name = "TarifasPorZona.findByLocalSinPernoctar", query = "SELECT t FROM TarifasPorZona t WHERE t.localSinPernoctar = :localSinPernoctar")
    , @NamedQuery(name = "TarifasPorZona.findByZona1Pernoctando", query = "SELECT t FROM TarifasPorZona t WHERE t.zona1Pernoctando = :zona1Pernoctando")
    , @NamedQuery(name = "TarifasPorZona.findByZona2Pernoctando", query = "SELECT t FROM TarifasPorZona t WHERE t.zona2Pernoctando = :zona2Pernoctando")
    , @NamedQuery(name = "TarifasPorZona.findByZona3Pernoctando", query = "SELECT t FROM TarifasPorZona t WHERE t.zona3Pernoctando = :zona3Pernoctando")
    , @NamedQuery(name = "TarifasPorZona.findByZona4Pernoctando", query = "SELECT t FROM TarifasPorZona t WHERE t.zona4Pernoctando = :zona4Pernoctando")
    , @NamedQuery(name = "TarifasPorZona.findByLocalPernoctando", query = "SELECT t FROM TarifasPorZona t WHERE t.localPernoctando = :localPernoctando")})
public class TarifasPorZona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tarifa")
    private Integer tarifa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona1_sin_pernoctar")
    private double zona1SinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona2_sin_pernoctar")
    private double zona2SinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona3_sin_pernoctar")
    private double zona3SinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona4_sin_pernoctar")
    private double zona4SinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "local_sin_pernoctar")
    private double localSinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona1_pernoctando")
    private double zona1Pernoctando;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona2_pernoctando")
    private double zona2Pernoctando;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona3_pernoctando")
    private double zona3Pernoctando;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona4_pernoctando")
    private double zona4Pernoctando;
    @Basic(optional = false)
    @NotNull
    @Column(name = "local_pernoctando")
    private double localPernoctando;
    @JoinColumn(name = "nivel", referencedColumnName = "nivel")
    @ManyToOne(optional = false)
    private NivelServidores nivel;
    @JoinColumn(name = "tarifa", referencedColumnName = "tarifa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Tarifas tarifas;

    public TarifasPorZona() {
    }

    public TarifasPorZona(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public TarifasPorZona(Integer tarifa, double zona1SinPernoctar, double zona2SinPernoctar, double zona3SinPernoctar, double zona4SinPernoctar, double localSinPernoctar, double zona1Pernoctando, double zona2Pernoctando, double zona3Pernoctando, double zona4Pernoctando, double localPernoctando) {
        this.tarifa = tarifa;
        this.zona1SinPernoctar = zona1SinPernoctar;
        this.zona2SinPernoctar = zona2SinPernoctar;
        this.zona3SinPernoctar = zona3SinPernoctar;
        this.zona4SinPernoctar = zona4SinPernoctar;
        this.localSinPernoctar = localSinPernoctar;
        this.zona1Pernoctando = zona1Pernoctando;
        this.zona2Pernoctando = zona2Pernoctando;
        this.zona3Pernoctando = zona3Pernoctando;
        this.zona4Pernoctando = zona4Pernoctando;
        this.localPernoctando = localPernoctando;
    }

    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public double getZona1SinPernoctar() {
        return zona1SinPernoctar;
    }

    public void setZona1SinPernoctar(double zona1SinPernoctar) {
        this.zona1SinPernoctar = zona1SinPernoctar;
    }

    public double getZona2SinPernoctar() {
        return zona2SinPernoctar;
    }

    public void setZona2SinPernoctar(double zona2SinPernoctar) {
        this.zona2SinPernoctar = zona2SinPernoctar;
    }

    public double getZona3SinPernoctar() {
        return zona3SinPernoctar;
    }

    public void setZona3SinPernoctar(double zona3SinPernoctar) {
        this.zona3SinPernoctar = zona3SinPernoctar;
    }

    public double getZona4SinPernoctar() {
        return zona4SinPernoctar;
    }

    public void setZona4SinPernoctar(double zona4SinPernoctar) {
        this.zona4SinPernoctar = zona4SinPernoctar;
    }

    public double getLocalSinPernoctar() {
        return localSinPernoctar;
    }

    public void setLocalSinPernoctar(double localSinPernoctar) {
        this.localSinPernoctar = localSinPernoctar;
    }

    public double getZona1Pernoctando() {
        return zona1Pernoctando;
    }

    public void setZona1Pernoctando(double zona1Pernoctando) {
        this.zona1Pernoctando = zona1Pernoctando;
    }

    public double getZona2Pernoctando() {
        return zona2Pernoctando;
    }

    public void setZona2Pernoctando(double zona2Pernoctando) {
        this.zona2Pernoctando = zona2Pernoctando;
    }

    public double getZona3Pernoctando() {
        return zona3Pernoctando;
    }

    public void setZona3Pernoctando(double zona3Pernoctando) {
        this.zona3Pernoctando = zona3Pernoctando;
    }

    public double getZona4Pernoctando() {
        return zona4Pernoctando;
    }

    public void setZona4Pernoctando(double zona4Pernoctando) {
        this.zona4Pernoctando = zona4Pernoctando;
    }

    public double getLocalPernoctando() {
        return localPernoctando;
    }

    public void setLocalPernoctando(double localPernoctando) {
        this.localPernoctando = localPernoctando;
    }

    public NivelServidores getNivel() {
        return nivel;
    }

    public void setNivel(NivelServidores nivel) {
        this.nivel = nivel;
    }

    public Tarifas getTarifas() {
        return tarifas;
    }

    public void setTarifas(Tarifas tarifas) {
        this.tarifas = tarifas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarifa != null ? tarifa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarifasPorZona)) {
            return false;
        }
        TarifasPorZona other = (TarifasPorZona) object;
        if ((this.tarifa == null && other.tarifa != null) || (this.tarifa != null && !this.tarifa.equals(other.tarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TarifasPorZona{" +
                "tarifa=" + tarifa +
                ", zona1SinPernoctar=" + zona1SinPernoctar +
                ", zona2SinPernoctar=" + zona2SinPernoctar +
                ", zona3SinPernoctar=" + zona3SinPernoctar +
                ", zona4SinPernoctar=" + zona4SinPernoctar +
                ", localSinPernoctar=" + localSinPernoctar +
                ", zona1Pernoctando=" + zona1Pernoctando +
                ", zona2Pernoctando=" + zona2Pernoctando +
                ", zona3Pernoctando=" + zona3Pernoctando +
                ", zona4Pernoctando=" + zona4Pernoctando +
                ", localPernoctando=" + localPernoctando +
                ", nivel=" + nivel +
                '}';
    }
}
