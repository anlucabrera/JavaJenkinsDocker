/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "indicadores", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicadores.findAll", query = "SELECT i FROM Indicadores i")
    , @NamedQuery(name = "Indicadores.findByIndicador", query = "SELECT i FROM Indicadores i WHERE i.indicadoresPK.indicador = :indicador")
    , @NamedQuery(name = "Indicadores.findByEjeprocedu", query = "SELECT i FROM Indicadores i WHERE i.ejeprocedu = :ejeprocedu")
    , @NamedQuery(name = "Indicadores.findByNombre", query = "SELECT i FROM Indicadores i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "Indicadores.findByFormula", query = "SELECT i FROM Indicadores i WHERE i.formula = :formula")
    , @NamedQuery(name = "Indicadores.findByClave", query = "SELECT i FROM Indicadores i WHERE i.indicadoresPK.clave = :clave")})
public class Indicadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IndicadoresPK indicadoresPK;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicador")
    private List<IndicadoresVariables> indicadoresVariablesList;
    @JoinColumn(name = "categoria", referencedColumnName = "categoria")
    @ManyToOne(optional = false)
    private IndicadoresCategorias categoria;
    @JoinColumn(name = "eje", referencedColumnName = "eje")
    @ManyToOne(optional = false)
    private Ejes eje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicador")
    private List<Desagregados> desagregadosList;

    public Indicadores() {
    }

    public Indicadores(IndicadoresPK indicadoresPK) {
        this.indicadoresPK = indicadoresPK;
    }

    public Indicadores(IndicadoresPK indicadoresPK, int ejeprocedu, String nombre, String formula) {
        this.indicadoresPK = indicadoresPK;
        this.ejeprocedu = ejeprocedu;
        this.nombre = nombre;
        this.formula = formula;
    }

    public Indicadores(int indicador, String clave) {
        this.indicadoresPK = new IndicadoresPK(indicador, clave);
    }

    public IndicadoresPK getIndicadoresPK() {
        return indicadoresPK;
    }

    public void setIndicadoresPK(IndicadoresPK indicadoresPK) {
        this.indicadoresPK = indicadoresPK;
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

    @XmlTransient
    public List<IndicadoresVariables> getIndicadoresVariablesList() {
        return indicadoresVariablesList;
    }

    public void setIndicadoresVariablesList(List<IndicadoresVariables> indicadoresVariablesList) {
        this.indicadoresVariablesList = indicadoresVariablesList;
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

    @XmlTransient
    public List<Desagregados> getDesagregadosList() {
        return desagregadosList;
    }

    public void setDesagregadosList(List<Desagregados> desagregadosList) {
        this.desagregadosList = desagregadosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (indicadoresPK != null ? indicadoresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicadores)) {
            return false;
        }
        Indicadores other = (Indicadores) object;
        if ((this.indicadoresPK == null && other.indicadoresPK != null) || (this.indicadoresPK != null && !this.indicadoresPK.equals(other.indicadoresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.Indicadores[ indicadoresPK=" + indicadoresPK + " ]";
    }
    
}
