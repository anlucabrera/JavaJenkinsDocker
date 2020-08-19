/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.ch;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Desarrollo
 */
@Entity
@Table(name = "experiencias_laborales", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExperienciasLaborales.findAll", query = "SELECT e FROM ExperienciasLaborales e")
    , @NamedQuery(name = "ExperienciasLaborales.findByEmpleo", query = "SELECT e FROM ExperienciasLaborales e WHERE e.empleo = :empleo")
    , @NamedQuery(name = "ExperienciasLaborales.findByInstitucionEmpresa", query = "SELECT e FROM ExperienciasLaborales e WHERE e.institucionEmpresa = :institucionEmpresa")
    , @NamedQuery(name = "ExperienciasLaborales.findByFechaInicio", query = "SELECT e FROM ExperienciasLaborales e WHERE e.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "ExperienciasLaborales.findByFechaFin", query = "SELECT e FROM ExperienciasLaborales e WHERE e.fechaFin = :fechaFin")
    , @NamedQuery(name = "ExperienciasLaborales.findByPuestoDesepenado", query = "SELECT e FROM ExperienciasLaborales e WHERE e.puestoDesepenado = :puestoDesepenado")
    , @NamedQuery(name = "ExperienciasLaborales.findByEvidenciaNombremiento", query = "SELECT e FROM ExperienciasLaborales e WHERE e.evidenciaNombremiento = :evidenciaNombremiento")
    , @NamedQuery(name = "ExperienciasLaborales.findByFuncionesDesempenio", query = "SELECT e FROM ExperienciasLaborales e WHERE e.funcionesDesempenio = :funcionesDesempenio")
    , @NamedQuery(name = "ExperienciasLaborales.findByEstatus", query = "SELECT e FROM ExperienciasLaborales e WHERE e.estatus = :estatus")})
public class ExperienciasLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "empleo")
    private Integer empleo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "institucion_empresa")
    private String institucionEmpresa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "puesto_desepenado")
    private String puestoDesepenado;
    @Size(max = 255)
    @Column(name = "evidencia_nombremiento")
    private String evidenciaNombremiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "funciones_desempenio")
    private String funcionesDesempenio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Personal clavePersonal;

    public ExperienciasLaborales() {
    }

    public ExperienciasLaborales(Integer empleo) {
        this.empleo = empleo;
    }

    public ExperienciasLaborales(Integer empleo, String institucionEmpresa, Date fechaInicio, String puestoDesepenado, String funcionesDesempenio, String estatus) {
        this.empleo = empleo;
        this.institucionEmpresa = institucionEmpresa;
        this.fechaInicio = fechaInicio;
        this.puestoDesepenado = puestoDesepenado;
        this.funcionesDesempenio = funcionesDesempenio;
        this.estatus = estatus;
    }

    public Integer getEmpleo() {
        return empleo;
    }

    public void setEmpleo(Integer empleo) {
        this.empleo = empleo;
    }

    public String getInstitucionEmpresa() {
        return institucionEmpresa;
    }

    public void setInstitucionEmpresa(String institucionEmpresa) {
        this.institucionEmpresa = institucionEmpresa;
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

    public String getPuestoDesepenado() {
        return puestoDesepenado;
    }

    public void setPuestoDesepenado(String puestoDesepenado) {
        this.puestoDesepenado = puestoDesepenado;
    }

    public String getEvidenciaNombremiento() {
        return evidenciaNombremiento;
    }

    public void setEvidenciaNombremiento(String evidenciaNombremiento) {
        this.evidenciaNombremiento = evidenciaNombremiento;
    }

    public String getFuncionesDesempenio() {
        return funcionesDesempenio;
    }

    public void setFuncionesDesempenio(String funcionesDesempenio) {
        this.funcionesDesempenio = funcionesDesempenio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Personal getClavePersonal() {
        return clavePersonal;
    }

    public void setClavePersonal(Personal clavePersonal) {
        this.clavePersonal = clavePersonal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empleo != null ? empleo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExperienciasLaborales)) {
            return false;
        }
        ExperienciasLaborales other = (ExperienciasLaborales) object;
        if ((this.empleo == null && other.empleo != null) || (this.empleo != null && !this.empleo.equals(other.empleo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales[ empleo=" + empleo + " ]";
    }
    
}
