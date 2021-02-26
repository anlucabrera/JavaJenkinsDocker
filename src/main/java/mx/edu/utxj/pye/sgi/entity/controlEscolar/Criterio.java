/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "criterio", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Criterio.findAll", query = "SELECT c FROM Criterio c")
    , @NamedQuery(name = "Criterio.findByCriterio", query = "SELECT c FROM Criterio c WHERE c.criterio = :criterio")
    , @NamedQuery(name = "Criterio.findByTipo", query = "SELECT c FROM Criterio c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "Criterio.findByNivel", query = "SELECT c FROM Criterio c WHERE c.nivel = :nivel")
    , @NamedQuery(name = "Criterio.findByPorcentajeRecomendado", query = "SELECT c FROM Criterio c WHERE c.porcentajeRecomendado = :porcentajeRecomendado")})
public class Criterio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "criterio")
    private Integer criterio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "tipo")
    private String tipo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "nivel")
    private String nivel;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje_recomendado")
    private double porcentajeRecomendado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterio1")
    private List<UnidadMateriaConfiguracionCriterio> unidadMateriaConfiguracionCriterioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterio1")
    private List<CriterioIndicadorPeriodo> criterioIndicadorPeriodoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "criterio")
    private List<UnidadMateriaConfiguracionDetalle> unidadMateriaConfiguracionDetalleList;

    public Criterio() {
    }

    public Criterio(Integer criterio) {
        this.criterio = criterio;
    }

    public Criterio(Integer criterio, String tipo, String nivel, double porcentajeRecomendado) {
        this.criterio = criterio;
        this.tipo = tipo;
        this.nivel = nivel;
        this.porcentajeRecomendado = porcentajeRecomendado;
    }

    public Integer getCriterio() {
        return criterio;
    }

    public void setCriterio(Integer criterio) {
        this.criterio = criterio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public double getPorcentajeRecomendado() {
        return porcentajeRecomendado;
    }

    public void setPorcentajeRecomendado(double porcentajeRecomendado) {
        this.porcentajeRecomendado = porcentajeRecomendado;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracionCriterio> getUnidadMateriaConfiguracionCriterioList() {
        return unidadMateriaConfiguracionCriterioList;
    }

    public void setUnidadMateriaConfiguracionCriterioList(List<UnidadMateriaConfiguracionCriterio> unidadMateriaConfiguracionCriterioList) {
        this.unidadMateriaConfiguracionCriterioList = unidadMateriaConfiguracionCriterioList;
    }

    @XmlTransient
    public List<CriterioIndicadorPeriodo> getCriterioIndicadorPeriodoList() {
        return criterioIndicadorPeriodoList;
    }

    public void setCriterioIndicadorPeriodoList(List<CriterioIndicadorPeriodo> criterioIndicadorPeriodoList) {
        this.criterioIndicadorPeriodoList = criterioIndicadorPeriodoList;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracionDetalle> getUnidadMateriaConfiguracionDetalleList() {
        return unidadMateriaConfiguracionDetalleList;
    }

    public void setUnidadMateriaConfiguracionDetalleList(List<UnidadMateriaConfiguracionDetalle> unidadMateriaConfiguracionDetalleList) {
        this.unidadMateriaConfiguracionDetalleList = unidadMateriaConfiguracionDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (criterio != null ? criterio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Criterio)) {
            return false;
        }
        Criterio other = (Criterio) object;
        if ((this.criterio == null && other.criterio != null) || (this.criterio != null && !this.criterio.equals(other.criterio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Criterio[ criterio=" + criterio + " ]";
    }
    
}
