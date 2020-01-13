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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "tarifas_viajes", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TarifasViajes.findAll", query = "SELECT t FROM TarifasViajes t")
    , @NamedQuery(name = "TarifasViajes.findByTarifa", query = "SELECT t FROM TarifasViajes t WHERE t.tarifa = :tarifa")
    , @NamedQuery(name = "TarifasViajes.findByOrigen", query = "SELECT t FROM TarifasViajes t WHERE t.origen = :origen")
    , @NamedQuery(name = "TarifasViajes.findByDestino", query = "SELECT t FROM TarifasViajes t WHERE t.destino = :destino")
    , @NamedQuery(name = "TarifasViajes.findByLineasRecomendadas", query = "SELECT t FROM TarifasViajes t WHERE t.lineasRecomendadas = :lineasRecomendadas")
    , @NamedQuery(name = "TarifasViajes.findByCostoPasajesViajeRedondo", query = "SELECT t FROM TarifasViajes t WHERE t.costoPasajesViajeRedondo = :costoPasajesViajeRedondo")
    , @NamedQuery(name = "TarifasViajes.findByCostoCasetasViajeRedondo", query = "SELECT t FROM TarifasViajes t WHERE t.costoCasetasViajeRedondo = :costoCasetasViajeRedondo")})
public class TarifasViajes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tarifa")
    private Integer tarifa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "origen")
    private String origen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "destino")
    private String destino;
    @Basic(optional = false)
    @Size(min = 1, max = 500)
    @Column(name = "lineas_recomendadas")
    private String lineasRecomendadas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_pasajes_viaje_redondo")
    private double costoPasajesViajeRedondo;
    @Basic(optional = false)
    @Column(name = "costo_casetas_viaje_redondo")
    private double costoCasetasViajeRedondo;
    @JoinColumn(name = "tarifa", referencedColumnName = "tarifa", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Tarifas tarifas;

    public TarifasViajes() {
    }

    public TarifasViajes(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public TarifasViajes(Integer tarifa, String origen, String destino, String lineasRecomendadas, double costoPasajesViajeRedondo, double costoCasetasViajeRedondo) {
        this.tarifa = tarifa;
        this.origen = origen;
        this.destino = destino;
        this.lineasRecomendadas = lineasRecomendadas;
        this.costoPasajesViajeRedondo = costoPasajesViajeRedondo;
        this.costoCasetasViajeRedondo = costoCasetasViajeRedondo;
    }

    public Integer getTarifa() {
        return tarifa;
    }

    public void setTarifa(Integer tarifa) {
        this.tarifa = tarifa;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getLineasRecomendadas() {
        return lineasRecomendadas;
    }

    public void setLineasRecomendadas(String lineasRecomendadas) {
        this.lineasRecomendadas = lineasRecomendadas;
    }

    public double getCostoPasajesViajeRedondo() {
        return costoPasajesViajeRedondo;
    }

    public void setCostoPasajesViajeRedondo(double costoPasajesViajeRedondo) {
        this.costoPasajesViajeRedondo = costoPasajesViajeRedondo;
    }

    public double getCostoCasetasViajeRedondo() {
        return costoCasetasViajeRedondo;
    }

    public void setCostoCasetasViajeRedondo(double costoCasetasViajeRedondo) {
        this.costoCasetasViajeRedondo = costoCasetasViajeRedondo;
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
        if (!(object instanceof TarifasViajes)) {
            return false;
        }
        TarifasViajes other = (TarifasViajes) object;
        if ((this.tarifa == null && other.tarifa != null) || (this.tarifa != null && !this.tarifa.equals(other.tarifa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TarifasViajes{" +
                "tarifa=" + tarifa +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", lineasRecomendadas='" + lineasRecomendadas + '\'' +
                ", costoPasajesViajeRedondo=" + costoPasajesViajeRedondo +
                ", costoCasetasViajeRedondo=" + costoCasetasViajeRedondo +
                '}';
    }
}
