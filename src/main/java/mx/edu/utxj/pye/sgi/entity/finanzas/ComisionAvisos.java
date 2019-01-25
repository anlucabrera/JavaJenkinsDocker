/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.finanzas;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "comision_avisos", catalog = "finanzas", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComisionAvisos.findAll", query = "SELECT c FROM ComisionAvisos c")
    , @NamedQuery(name = "ComisionAvisos.findByTramite", query = "SELECT c FROM ComisionAvisos c WHERE c.tramite = :tramite")
    , @NamedQuery(name = "ComisionAvisos.findByZona", query = "SELECT c FROM ComisionAvisos c WHERE c.zona = :zona")
    , @NamedQuery(name = "ComisionAvisos.findByPernoctando", query = "SELECT c FROM ComisionAvisos c WHERE c.pernoctando = :pernoctando")
    , @NamedQuery(name = "ComisionAvisos.findBySinPernoctar", query = "SELECT c FROM ComisionAvisos c WHERE c.sinPernoctar = :sinPernoctar")
    , @NamedQuery(name = "ComisionAvisos.findByPasajes", query = "SELECT c FROM ComisionAvisos c WHERE c.pasajes = :pasajes")
    , @NamedQuery(name = "ComisionAvisos.findByCasetas", query = "SELECT c FROM ComisionAvisos c WHERE c.casetas = :casetas")
    , @NamedQuery(name = "ComisionAvisos.findByOtros", query = "SELECT c FROM ComisionAvisos c WHERE c.otros = :otros")
    , @NamedQuery(name = "ComisionAvisos.findByTotal", query = "SELECT c FROM ComisionAvisos c WHERE c.total = :total")
    , @NamedQuery(name = "ComisionAvisos.findByFormatosAutorizados", query = "SELECT c FROM ComisionAvisos c WHERE c.formatosAutorizados = :formatosAutorizados")
    , @NamedQuery(name = "ComisionAvisos.findByLiquidacionFecha", query = "SELECT c FROM ComisionAvisos c WHERE c.liquidacionFecha = :liquidacionFecha")})
public class ComisionAvisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tramite")
    private Integer tramite;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zona")
    private short zona;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pernoctando")
    private short pernoctando;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sin_pernoctar")
    private short sinPernoctar;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pasajes")
    private boolean pasajes;
    @Basic(optional = false)
    @NotNull
    @Column(name = "casetas")
    private boolean casetas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "otros")
    private double otros;
    @Lob
    @Size(max = 65535)
    @Column(name = "comentarios")
    private String comentarios;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private double total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "formatos_autorizados")
    private boolean formatosAutorizados;
    @Column(name = "liquidacion_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date liquidacionFecha;
    @JoinColumn(name = "tramite", referencedColumnName = "tramite", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private ComisionOficios comisionOficios;
    @JoinColumn(name = "tarifa_viaje", referencedColumnName = "tarifa")
    @ManyToOne
    private Tarifas tarifaViaje;
    @JoinColumn(name = "tarifa_viaticos", referencedColumnName = "tarifa")
    @ManyToOne
    private Tarifas tarifaViaticos;

    public ComisionAvisos() {
    }

    public ComisionAvisos(Integer tramite) {
        this.tramite = tramite;
    }

    public ComisionAvisos(Integer tramite, short zona, short pernoctando, short sinPernoctar, boolean pasajes, boolean casetas, double otros, double total, boolean formatosAutorizados) {
        this.tramite = tramite;
        this.zona = zona;
        this.pernoctando = pernoctando;
        this.sinPernoctar = sinPernoctar;
        this.pasajes = pasajes;
        this.casetas = casetas;
        this.otros = otros;
        this.total = total;
        this.formatosAutorizados = formatosAutorizados;
    }

    public Integer getTramite() {
        return tramite;
    }

    public void setTramite(Integer tramite) {
        this.tramite = tramite;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public short getPernoctando() {
        return pernoctando;
    }

    public void setPernoctando(short pernoctando) {
        this.pernoctando = pernoctando;
    }

    public short getSinPernoctar() {
        return sinPernoctar;
    }

    public void setSinPernoctar(short sinPernoctar) {
        this.sinPernoctar = sinPernoctar;
    }

    public boolean getPasajes() {
        return pasajes;
    }

    public void setPasajes(boolean pasajes) {
        this.pasajes = pasajes;
    }

    public boolean getCasetas() {
        return casetas;
    }

    public void setCasetas(boolean casetas) {
        this.casetas = casetas;
    }

    public double getOtros() {
        return otros;
    }

    public void setOtros(double otros) {
        this.otros = otros;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean getFormatosAutorizados() {
        return formatosAutorizados;
    }

    public void setFormatosAutorizados(boolean formatosAutorizados) {
        this.formatosAutorizados = formatosAutorizados;
    }

    public Date getLiquidacionFecha() {
        return liquidacionFecha;
    }

    public void setLiquidacionFecha(Date liquidacionFecha) {
        this.liquidacionFecha = liquidacionFecha;
    }

    public ComisionOficios getComisionOficios() {
        return comisionOficios;
    }

    public void setComisionOficios(ComisionOficios comisionOficios) {
        this.comisionOficios = comisionOficios;
    }

    public Tarifas getTarifaViaje() {
        return tarifaViaje;
    }

    public void setTarifaViaje(Tarifas tarifaViaje) {
        this.tarifaViaje = tarifaViaje;
    }

    public Tarifas getTarifaViaticos() {
        return tarifaViaticos;
    }

    public void setTarifaViaticos(Tarifas tarifaViaticos) {
        this.tarifaViaticos = tarifaViaticos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tramite != null ? tramite.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComisionAvisos)) {
            return false;
        }
        ComisionAvisos other = (ComisionAvisos) object;
        if ((this.tramite == null && other.tramite != null) || (this.tramite != null && !this.tramite.equals(other.tramite))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.finanzas.ComisionAvisos[ tramite=" + tramite + " ]";
    }
    
}
