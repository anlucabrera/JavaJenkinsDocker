/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "fuente_informacion", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuenteInformacion.findAll", query = "SELECT f FROM FuenteInformacion f")
    , @NamedQuery(name = "FuenteInformacion.findByIndicador", query = "SELECT f FROM FuenteInformacion f WHERE f.fuenteInformacionPK.indicador = :indicador")
    , @NamedQuery(name = "FuenteInformacion.findByArea", query = "SELECT f FROM FuenteInformacion f WHERE f.fuenteInformacionPK.area = :area")})
public class FuenteInformacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FuenteInformacionPK fuenteInformacionPK;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private IndicadoresPide indicadoresPide;

    public FuenteInformacion() {
    }

    public FuenteInformacion(FuenteInformacionPK fuenteInformacionPK) {
        this.fuenteInformacionPK = fuenteInformacionPK;
    }

    public FuenteInformacion(int indicador, short area) {
        this.fuenteInformacionPK = new FuenteInformacionPK(indicador, area);
    }

    public FuenteInformacionPK getFuenteInformacionPK() {
        return fuenteInformacionPK;
    }

    public void setFuenteInformacionPK(FuenteInformacionPK fuenteInformacionPK) {
        this.fuenteInformacionPK = fuenteInformacionPK;
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
        hash += (fuenteInformacionPK != null ? fuenteInformacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuenteInformacion)) {
            return false;
        }
        FuenteInformacion other = (FuenteInformacion) object;
        if ((this.fuenteInformacionPK == null && other.fuenteInformacionPK != null) || (this.fuenteInformacionPK != null && !this.fuenteInformacionPK.equals(other.fuenteInformacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.FuenteInformacion[ fuenteInformacionPK=" + fuenteInformacionPK + " ]";
    }
    
}
