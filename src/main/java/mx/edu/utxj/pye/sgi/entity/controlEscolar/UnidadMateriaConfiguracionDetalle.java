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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "unidad_materia_configuracion_detalle", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaConfiguracionDetalle.findAll", query = "SELECT u FROM UnidadMateriaConfiguracionDetalle u")
    , @NamedQuery(name = "UnidadMateriaConfiguracionDetalle.findByConfiguracionDetalle", query = "SELECT u FROM UnidadMateriaConfiguracionDetalle u WHERE u.configuracionDetalle = :configuracionDetalle")
    , @NamedQuery(name = "UnidadMateriaConfiguracionDetalle.findByPorcentaje", query = "SELECT u FROM UnidadMateriaConfiguracionDetalle u WHERE u.porcentaje = :porcentaje")})
public class UnidadMateriaConfiguracionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "configuracion_detalle")
    private Long configuracionDetalle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuracionDetalle", fetch = FetchType.LAZY)
    private List<Calificacion> calificacionList;
    @JoinColumn(name = "criterio", referencedColumnName = "criterio")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Criterio criterio;
    @JoinColumn(name = "indicador", referencedColumnName = "indicador")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Indicador indicador;
    @JoinColumn(name = "configuracion", referencedColumnName = "configuracion")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UnidadMateriaConfiguracion configuracion;

    public UnidadMateriaConfiguracionDetalle() {
    }

    public UnidadMateriaConfiguracionDetalle(Long configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
    }

    public UnidadMateriaConfiguracionDetalle(Long configuracionDetalle, double porcentaje) {
        this.configuracionDetalle = configuracionDetalle;
        this.porcentaje = porcentaje;
    }

    public Long getConfiguracionDetalle() {
        return configuracionDetalle;
    }

    public void setConfiguracionDetalle(Long configuracionDetalle) {
        this.configuracionDetalle = configuracionDetalle;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    public Criterio getCriterio() {
        return criterio;
    }

    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
    }

    public Indicador getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicador indicador) {
        this.indicador = indicador;
    }

    public UnidadMateriaConfiguracion getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(UnidadMateriaConfiguracion configuracion) {
        this.configuracion = configuracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configuracionDetalle != null ? configuracionDetalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaConfiguracionDetalle)) {
            return false;
        }
        UnidadMateriaConfiguracionDetalle other = (UnidadMateriaConfiguracionDetalle) object;
        if ((this.configuracionDetalle == null && other.configuracionDetalle != null) || (this.configuracionDetalle != null && !this.configuracionDetalle.equals(other.configuracionDetalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracionDetalle[ configuracionDetalle=" + configuracionDetalle + " ]";
    }
    
}
