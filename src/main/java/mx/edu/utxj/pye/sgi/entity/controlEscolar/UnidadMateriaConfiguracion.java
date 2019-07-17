/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "unidad_materia_configuracion", catalog = "control_escolar", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadMateriaConfiguracion.findAll", query = "SELECT u FROM UnidadMateriaConfiguracion u")
    , @NamedQuery(name = "UnidadMateriaConfiguracion.findByConfiguracion", query = "SELECT u FROM UnidadMateriaConfiguracion u WHERE u.configuracion = :configuracion")
    , @NamedQuery(name = "UnidadMateriaConfiguracion.findByFechaInicio", query = "SELECT u FROM UnidadMateriaConfiguracion u WHERE u.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "UnidadMateriaConfiguracion.findByFechaFin", query = "SELECT u FROM UnidadMateriaConfiguracion u WHERE u.fechaFin = :fechaFin")
    , @NamedQuery(name = "UnidadMateriaConfiguracion.findByDirector", query = "SELECT u FROM UnidadMateriaConfiguracion u WHERE u.director = :director")
    , @NamedQuery(name = "UnidadMateriaConfiguracion.findByPorcentaje", query = "SELECT u FROM UnidadMateriaConfiguracion u WHERE u.porcentaje = :porcentaje")})
public class UnidadMateriaConfiguracion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "configuracion")
    private Integer configuracion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "director")
    private Integer director;
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private double porcentaje;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unidadMateriaConfiguracion")
    private List<UnidadMateriaConfiguracionCriterio> unidadMateriaConfiguracionCriterioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuracion")
    private List<UnidadMateriaConfiguracionDetalle> unidadMateriaConfiguracionDetalleList;
    @JoinColumn(name = "carga", referencedColumnName = "carga")
    @ManyToOne(optional = false)
    private CargaAcademica carga;
    @JoinColumn(name = "id_unidad_materia", referencedColumnName = "id_unidad_materia")
    @ManyToOne(optional = false)
    private UnidadMateria idUnidadMateria;

    public UnidadMateriaConfiguracion() {
    }

    public UnidadMateriaConfiguracion(Integer configuracion) {
        this.configuracion = configuracion;
    }

    public UnidadMateriaConfiguracion(Integer configuracion, Date fechaInicio, Date fechaFin, double porcentaje) {
        this.configuracion = configuracion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
    }

    public Integer getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Integer configuracion) {
        this.configuracion = configuracion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDirector() {
        return director;
    }

    public void setDirector(Integer director) {
        this.director = director;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracionCriterio> getUnidadMateriaConfiguracionCriterioList() {
        return unidadMateriaConfiguracionCriterioList;
    }

    public void setUnidadMateriaConfiguracionCriterioList(List<UnidadMateriaConfiguracionCriterio> unidadMateriaConfiguracionCriterioList) {
        this.unidadMateriaConfiguracionCriterioList = unidadMateriaConfiguracionCriterioList;
    }

    @XmlTransient
    public List<UnidadMateriaConfiguracionDetalle> getUnidadMateriaConfiguracionDetalleList() {
        return unidadMateriaConfiguracionDetalleList;
    }

    public void setUnidadMateriaConfiguracionDetalleList(List<UnidadMateriaConfiguracionDetalle> unidadMateriaConfiguracionDetalleList) {
        this.unidadMateriaConfiguracionDetalleList = unidadMateriaConfiguracionDetalleList;
    }

    public CargaAcademica getCarga() {
        return carga;
    }

    public void setCarga(CargaAcademica carga) {
        this.carga = carga;
    }

    public UnidadMateria getIdUnidadMateria() {
        return idUnidadMateria;
    }

    public void setIdUnidadMateria(UnidadMateria idUnidadMateria) {
        this.idUnidadMateria = idUnidadMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configuracion != null ? configuracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadMateriaConfiguracion)) {
            return false;
        }
        UnidadMateriaConfiguracion other = (UnidadMateriaConfiguracion) object;
        if ((this.configuracion == null && other.configuracion != null) || (this.configuracion != null && !this.configuracion.equals(other.configuracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaConfiguracion[ configuracion=" + configuracion + " ]";
    }
    
}
