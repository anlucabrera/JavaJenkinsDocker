/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

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
@Table(name = "indicador_plazo", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadorPlazo.findAll", query = "SELECT i FROM IndicadorPlazo i")
    , @NamedQuery(name = "IndicadorPlazo.findByIndicador", query = "SELECT i FROM IndicadorPlazo i WHERE i.indicadorPlazoPK.indicador = :indicador")
    , @NamedQuery(name = "IndicadorPlazo.findByEjercicioFiscal", query = "SELECT i FROM IndicadorPlazo i WHERE i.indicadorPlazoPK.ejercicioFiscal = :ejercicioFiscal")
    , @NamedQuery(name = "IndicadorPlazo.findByMeta", query = "SELECT i FROM IndicadorPlazo i WHERE i.meta = :meta")})
public class IndicadorPlazo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IndicadorPlazoPK indicadorPlazoPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "meta")
    private double meta;
    @JoinColumn(name = "ejercicio_fiscal", referencedColumnName = "ejercicio_fiscal", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EjerciciosFiscales ejerciciosFiscales;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private IndicadoresPide indicadoresPide;

    public IndicadorPlazo() {
    }

    public IndicadorPlazo(IndicadorPlazoPK indicadorPlazoPK) {
        this.indicadorPlazoPK = indicadorPlazoPK;
    }

    public IndicadorPlazo(IndicadorPlazoPK indicadorPlazoPK, double meta) {
        this.indicadorPlazoPK = indicadorPlazoPK;
        this.meta = meta;
    }

    public IndicadorPlazo(int indicador, short ejercicioFiscal) {
        this.indicadorPlazoPK = new IndicadorPlazoPK(indicador, ejercicioFiscal);
    }

    public IndicadorPlazoPK getIndicadorPlazoPK() {
        return indicadorPlazoPK;
    }

    public void setIndicadorPlazoPK(IndicadorPlazoPK indicadorPlazoPK) {
        this.indicadorPlazoPK = indicadorPlazoPK;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public EjerciciosFiscales getEjerciciosFiscales() {
        return ejerciciosFiscales;
    }

    public void setEjerciciosFiscales(EjerciciosFiscales ejerciciosFiscales) {
        this.ejerciciosFiscales = ejerciciosFiscales;
    }

    public IndicadoresPide getIndicadoresPide() {
        return indicadoresPide;
    }

    public void setIndicadoresPide(IndicadoresPide indicadoresPide) {
        this.indicadoresPide = indicadoresPide;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadorPlazoPK != null ? indicadorPlazoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadorPlazo)) {
            return false;
        }
        IndicadorPlazo other = (IndicadorPlazo) object;
        if ((this.indicadorPlazoPK == null && other.indicadorPlazoPK != null) || (this.indicadorPlazoPK != null && !this.indicadorPlazoPK.equals(other.indicadorPlazoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.IndicadorPlazo[ indicadorPlazoPK=" + indicadorPlazoPK + " ]";
    }
    
}
