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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "indicador", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicador.findAll", query = "SELECT i FROM Indicador i")
    , @NamedQuery(name = "Indicador.findByIndicador", query = "SELECT i FROM Indicador i WHERE i.indicador = :indicador")
    , @NamedQuery(name = "Indicador.findByNombre", query = "SELECT i FROM Indicador i WHERE i.nombre = :nombre")
    , @NamedQuery(name = "Indicador.findByEstatus", query = "SELECT i FROM Indicador i WHERE i.estatus = :estatus")})
public class Indicador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indicador")
    private Integer indicador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estatus")
    private boolean estatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicador1", fetch = FetchType.LAZY)
    private List<CriterioIndicadorPeriodo> criterioIndicadorPeriodoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicador", fetch = FetchType.LAZY)
    private List<CalificacionNivelacion> calificacionNivelacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "indicador", fetch = FetchType.LAZY)
    private List<UnidadMateriaConfiguracionDetalle> unidadMateriaConfiguracionDetalleList;

    public Indicador() {
    }

    public Indicador(Integer indicador) {
        this.indicador = indicador;
    }

    public Indicador(Integer indicador, String nombre, boolean estatus) {
        this.indicador = indicador;
        this.nombre = nombre;
        this.estatus = estatus;
    }

    public Integer getIndicador() {
        return indicador;
    }

    public void setIndicador(Integer indicador) {
        this.indicador = indicador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    @XmlTransient
    public List<CriterioIndicadorPeriodo> getCriterioIndicadorPeriodoList() {
        return criterioIndicadorPeriodoList;
    }

    public void setCriterioIndicadorPeriodoList(List<CriterioIndicadorPeriodo> criterioIndicadorPeriodoList) {
        this.criterioIndicadorPeriodoList = criterioIndicadorPeriodoList;
    }

    @XmlTransient
    public List<CalificacionNivelacion> getCalificacionNivelacionList() {
        return calificacionNivelacionList;
    }

    public void setCalificacionNivelacionList(List<CalificacionNivelacion> calificacionNivelacionList) {
        this.calificacionNivelacionList = calificacionNivelacionList;
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
        hash += (indicador != null ? indicador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Indicador)) {
            return false;
        }
        Indicador other = (Indicador) object;
        if ((this.indicador == null && other.indicador != null) || (this.indicador != null && !this.indicador.equals(other.indicador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador[ indicador=" + indicador + " ]";
    }
    
}
