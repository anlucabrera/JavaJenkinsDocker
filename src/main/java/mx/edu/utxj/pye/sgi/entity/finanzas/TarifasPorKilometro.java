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
@Table(name = "tarifas_por_kilometro", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarifasPorKilometro.findAll", query = "SELECT t FROM TarifasPorKilometro t")
    , @NamedQuery(name = "TarifasPorKilometro.findByTarifa", query = "SELECT t FROM TarifasPorKilometro t WHERE t.tarifa = :tarifa")
    , @NamedQuery(name = "TarifasPorKilometro.findByMinimo", query = "SELECT t FROM TarifasPorKilometro t WHERE t.minimo = :minimo")
    , @NamedQuery(name = "TarifasPorKilometro.findByMaximo", query = "SELECT t FROM TarifasPorKilometro t WHERE t.maximo = :maximo")
    , @NamedQuery(name = "TarifasPorKilometro.findBySinPernoctar", query = "SELECT t FROM TarifasPorKilometro t WHERE t.sinPernoctar = :sinPernoctar")
    , @NamedQuery(name = "TarifasPorKilometro.findByPernoctando", query = "SELECT t FROM TarifasPorKilometro t WHERE t.pernoctando = :pernoctando")})
public class TarifasPorKilometro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tarifa")
    private Integer tarifa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "minimo")
    private short minimo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "maximo")
    private short maximo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_pernoctar")
    private double sinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pernoctando")
    private double pernoctando;
    @JoinColumn(name = "nivel", referencedColumnName = "nivel")
    @ManyToOne(optional = false)
    private NivelServidores nivel;
    @JoinColumn(name = "tarifa", referencedColumnName = "tarifa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Tarifas tarifas;

    public TarifasPorKilometro() {
    }

    public TarifasPorKilometro(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public TarifasPorKilometro(Integer tarifa, short minimo, short maximo, double sinPernoctar, double pernoctando) {
        this.tarifa = tarifa;
        this.minimo = minimo;
        this.maximo = maximo;
        this.sinPernoctar = sinPernoctar;
        this.pernoctando = pernoctando;
    }

    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public short getMinimo() {
        return minimo;
    }

    public void setMinimo(short minimo) {
        this.minimo = minimo;
    }

    public short getMaximo() {
        return maximo;
    }

    public void setMaximo(short maximo) {
        this.maximo = maximo;
    }

    public double getSinPernoctar() {
        return sinPernoctar;
    }

    public void setSinPernoctar(double sinPernoctar) {
        this.sinPernoctar = sinPernoctar;
    }

    public double getPernoctando() {
        return pernoctando;
    }

    public void setPernoctando(double pernoctando) {
        this.pernoctando = pernoctando;
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
        if (!(object instanceof TarifasPorKilometro)) {
            return false;
        }
        TarifasPorKilometro other = (TarifasPorKilometro) object;
        if ((this.tarifa == null && other.tarifa != null) || (this.tarifa != null && !this.tarifa.equals(other.tarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.TarifasPorKilometro[ tarifa=" + tarifa + " ]";
    }
    
}
