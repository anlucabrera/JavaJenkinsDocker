/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.controlEscolar;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "configuracion_materia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionMateria.findAll", query = "SELECT c FROM ConfiguracionMateria c")
    , @NamedQuery(name = "ConfiguracionMateria.findByIdConfiguracionMateria", query = "SELECT c FROM ConfiguracionMateria c WHERE c.idConfiguracionMateria = :idConfiguracionMateria")
    , @NamedQuery(name = "ConfiguracionMateria.findByCargaAcademica", query = "SELECT c FROM ConfiguracionMateria c WHERE c.cargaAcademica = :cargaAcademica")
    , @NamedQuery(name = "ConfiguracionMateria.findByFechaInicio", query = "SELECT c FROM ConfiguracionMateria c WHERE c.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ConfiguracionMateria.findByFechaFin", query = "SELECT c FROM ConfiguracionMateria c WHERE c.fechaFin = :fechaFin")
    , @NamedQuery(name = "ConfiguracionMateria.findByPorcentaje", query = "SELECT c FROM ConfiguracionMateria c WHERE c.porcentaje = :porcentaje")
    , @NamedQuery(name = "ConfiguracionMateria.findByValidoDireccion", query = "SELECT c FROM ConfiguracionMateria c WHERE c.validoDireccion = :validoDireccion")})
public class ConfiguracionMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_configuracion_materia")
    private Integer idConfiguracionMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "carga_academica")
    private int cargaAcademica;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "porcentaje")
    private int porcentaje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valido_direccion")
    private boolean validoDireccion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "configuracionMateria")
    private IndicadorConfiguracion indicadorConfiguracion;
    @JoinColumn(name = "id_unidad_materia", referencedColumnName = "id_unidad_materia")
    @ManyToOne(optional = false)
    private UnidadMateria idUnidadMateria;
    @JoinColumns({
        @JoinColumn(name = "grupo", referencedColumnName = "cve_grupo")
        , @JoinColumn(name = "materia", referencedColumnName = "cve_materia")
        , @JoinColumn(name = "id_configuracion_materia", referencedColumnName = "id_carga_academica", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private CargaAcademica cargaAcademica1;

    public ConfiguracionMateria() {
    }

    public ConfiguracionMateria(Integer idConfiguracionMateria) {
        this.idConfiguracionMateria = idConfiguracionMateria;
    }

    public ConfiguracionMateria(Integer idConfiguracionMateria, int cargaAcademica, Date fechaInicio, Date fechaFin, int porcentaje, boolean validoDireccion) {
        this.idConfiguracionMateria = idConfiguracionMateria;
        this.cargaAcademica = cargaAcademica;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
        this.validoDireccion = validoDireccion;
    }

    public Integer getIdConfiguracionMateria() {
        return idConfiguracionMateria;
    }

    public void setIdConfiguracionMateria(Integer idConfiguracionMateria) {
        this.idConfiguracionMateria = idConfiguracionMateria;
    }

    public int getCargaAcademica() {
        return cargaAcademica;
    }

    public void setCargaAcademica(int cargaAcademica) {
        this.cargaAcademica = cargaAcademica;
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

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public boolean getValidoDireccion() {
        return validoDireccion;
    }

    public void setValidoDireccion(boolean validoDireccion) {
        this.validoDireccion = validoDireccion;
    }

    public IndicadorConfiguracion getIndicadorConfiguracion() {
        return indicadorConfiguracion;
    }

    public void setIndicadorConfiguracion(IndicadorConfiguracion indicadorConfiguracion) {
        this.indicadorConfiguracion = indicadorConfiguracion;
    }

    public UnidadMateria getIdUnidadMateria() {
        return idUnidadMateria;
    }

    public void setIdUnidadMateria(UnidadMateria idUnidadMateria) {
        this.idUnidadMateria = idUnidadMateria;
    }

    public CargaAcademica getCargaAcademica1() {
        return cargaAcademica1;
    }

    public void setCargaAcademica1(CargaAcademica cargaAcademica1) {
        this.cargaAcademica1 = cargaAcademica1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracionMateria != null ? idConfiguracionMateria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfiguracionMateria)) {
            return false;
        }
        ConfiguracionMateria other = (ConfiguracionMateria) object;
        if ((this.idConfiguracionMateria == null && other.idConfiguracionMateria != null) || (this.idConfiguracionMateria != null && !this.idConfiguracionMateria.equals(other.idConfiguracionMateria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.controlEscolar.ConfiguracionMateria[ idConfiguracionMateria=" + idConfiguracionMateria + " ]";
    }
    
}
