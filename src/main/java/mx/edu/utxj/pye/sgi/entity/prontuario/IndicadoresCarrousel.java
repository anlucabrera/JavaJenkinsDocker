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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "indicadores_carrousel", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicadoresCarrousel.findAll", query = "SELECT i FROM IndicadoresCarrousel i")
    , @NamedQuery(name = "IndicadoresCarrousel.findByIndicador", query = "SELECT i FROM IndicadoresCarrousel i WHERE i.indicadoresCarrouselPK.indicador = :indicador")
    , @NamedQuery(name = "IndicadoresCarrousel.findByEjeprocedu", query = "SELECT i FROM IndicadoresCarrousel i WHERE i.ejeprocedu = :ejeprocedu")
    , @NamedQuery(name = "IndicadoresCarrousel.findByNombre", query = "SELECT i FROM IndicadoresCarrousel i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "IndicadoresCarrousel.findByFormula", query = "SELECT i FROM IndicadoresCarrousel i WHERE i.formula = :formula")
    , @NamedQuery(name = "IndicadoresCarrousel.findByClave", query = "SELECT i FROM IndicadoresCarrousel i WHERE i.indicadoresCarrouselPK.clave = :clave")})
public class IndicadoresCarrousel implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IndicadoresCarrouselPK indicadoresCarrouselPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ejeprocedu")
    private int ejeprocedu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 400)
    @Column(name = "formula")
    private String formula;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria")
    @ManyToOne(optional = false)
    private IndicadoresCategorias categoria;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false)
    private Ejes eje;

    public IndicadoresCarrousel() {
    }

    public IndicadoresCarrousel(IndicadoresCarrouselPK indicadoresCarrouselPK) {
        this.indicadoresCarrouselPK = indicadoresCarrouselPK;
    }

    public IndicadoresCarrousel(IndicadoresCarrouselPK indicadoresCarrouselPK, int ejeprocedu, String nombre, String formula) {
        this.indicadoresCarrouselPK = indicadoresCarrouselPK;
        this.ejeprocedu = ejeprocedu;
        this.nombre = nombre;
        this.formula = formula;
    }

    public IndicadoresCarrousel(int indicador, String clave) {
        this.indicadoresCarrouselPK = new IndicadoresCarrouselPK(indicador, clave);
    }

    public IndicadoresCarrouselPK getIndicadoresCarrouselPK() {
        return indicadoresCarrouselPK;
    }

    public void setIndicadoresCarrouselPK(IndicadoresCarrouselPK indicadoresCarrouselPK) {
        this.indicadoresCarrouselPK = indicadoresCarrouselPK;
    }

    public int getEjeprocedu() {
        return ejeprocedu;
    }

    public void setEjeprocedu(int ejeprocedu) {
        this.ejeprocedu = ejeprocedu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public IndicadoresCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(IndicadoresCategorias categoria) {
        this.categoria = categoria;
    }

    public Ejes getEje() {
        return eje;
    }

    public void setEje(Ejes eje) {
        this.eje = eje;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadoresCarrouselPK != null ? indicadoresCarrouselPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IndicadoresCarrousel)) {
            return false;
        }
        IndicadoresCarrousel other = (IndicadoresCarrousel) object;
        if ((this.indicadoresCarrouselPK == null && other.indicadoresCarrouselPK != null) || (this.indicadoresCarrouselPK != null && !this.indicadoresCarrouselPK.equals(other.indicadoresCarrouselPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.IndicadoresCarrousel[ indicadoresCarrouselPK=" + indicadoresCarrouselPK + " ]";
    }
    
}
