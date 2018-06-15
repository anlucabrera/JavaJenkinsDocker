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
 * @author UTXJ
 */
@Entity
@Table(name = "desarrollo_software", catalog = "capital_humano", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DesarrolloSoftware.findAll", query = "SELECT d FROM DesarrolloSoftware d")
    , @NamedQuery(name = "DesarrolloSoftware.findByDesarrollo", query = "SELECT d FROM DesarrolloSoftware d WHERE d.desarrollo = :desarrollo")
    , @NamedQuery(name = "DesarrolloSoftware.findByTipoDesarrollo", query = "SELECT d FROM DesarrolloSoftware d WHERE d.tipoDesarrollo = :tipoDesarrollo")
    , @NamedQuery(name = "DesarrolloSoftware.findByDerechosAutor", query = "SELECT d FROM DesarrolloSoftware d WHERE d.derechosAutor = :derechosAutor")
    , @NamedQuery(name = "DesarrolloSoftware.findByLugar", query = "SELECT d FROM DesarrolloSoftware d WHERE d.lugar = :lugar")
    , @NamedQuery(name = "DesarrolloSoftware.findByHorasHombre", query = "SELECT d FROM DesarrolloSoftware d WHERE d.horasHombre = :horasHombre")
    , @NamedQuery(name = "DesarrolloSoftware.findByCosto", query = "SELECT d FROM DesarrolloSoftware d WHERE d.costo = :costo")
    , @NamedQuery(name = "DesarrolloSoftware.findByFechaInicio", query = "SELECT d FROM DesarrolloSoftware d WHERE d.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "DesarrolloSoftware.findByFechaFin", query = "SELECT d FROM DesarrolloSoftware d WHERE d.fechaFin = :fechaFin")
    , @NamedQuery(name = "DesarrolloSoftware.findByApoyoRecibido", query = "SELECT d FROM DesarrolloSoftware d WHERE d.apoyoRecibido = :apoyoRecibido")
    , @NamedQuery(name = "DesarrolloSoftware.findByNombreBeneficiario", query = "SELECT d FROM DesarrolloSoftware d WHERE d.nombreBeneficiario = :nombreBeneficiario")
    , @NamedQuery(name = "DesarrolloSoftware.findByLogros", query = "SELECT d FROM DesarrolloSoftware d WHERE d.logros = :logros")
    , @NamedQuery(name = "DesarrolloSoftware.findByEstatus", query = "SELECT d FROM DesarrolloSoftware d WHERE d.estatus = :estatus")})
public class DesarrolloSoftware implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "desarrollo")
    private Integer desarrollo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "tipo_desarrollo")
    private String tipoDesarrollo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "derechos_autor")
    private String derechosAutor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "lugar")
    private String lugar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "horas_hombre")
    private String horasHombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "costo")
    private String costo;
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
    @Size(min = 1, max = 3)
    @Column(name = "apoyo_recibido")
    private String apoyoRecibido;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_beneficiario")
    private String nombreBeneficiario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "logros")
    private String logros;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "estatus")
    private String estatus;
    @JoinColumn(name = "clave_personal", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Personal clavePersonal;

    public DesarrolloSoftware() {
    }

    public DesarrolloSoftware(Integer desarrollo) {
        this.desarrollo = desarrollo;
    }

    public DesarrolloSoftware(Integer desarrollo, String tipoDesarrollo, String derechosAutor, String lugar, String horasHombre, String costo, Date fechaInicio, Date fechaFin, String apoyoRecibido, String nombreBeneficiario, String logros, String estatus) {
        this.desarrollo = desarrollo;
        this.tipoDesarrollo = tipoDesarrollo;
        this.derechosAutor = derechosAutor;
        this.lugar = lugar;
        this.horasHombre = horasHombre;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.apoyoRecibido = apoyoRecibido;
        this.nombreBeneficiario = nombreBeneficiario;
        this.logros = logros;
        this.estatus = estatus;
    }

    public Integer getDesarrollo() {
        return desarrollo;
    }

    public void setDesarrollo(Integer desarrollo) {
        this.desarrollo = desarrollo;
    }

    public String getTipoDesarrollo() {
        return tipoDesarrollo;
    }

    public void setTipoDesarrollo(String tipoDesarrollo) {
        this.tipoDesarrollo = tipoDesarrollo;
    }

    public String getDerechosAutor() {
        return derechosAutor;
    }

    public void setDerechosAutor(String derechosAutor) {
        this.derechosAutor = derechosAutor;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getHorasHombre() {
        return horasHombre;
    }

    public void setHorasHombre(String horasHombre) {
        this.horasHombre = horasHombre;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
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

    public String getApoyoRecibido() {
        return apoyoRecibido;
    }

    public void setApoyoRecibido(String apoyoRecibido) {
        this.apoyoRecibido = apoyoRecibido;
    }

    public String getNombreBeneficiario() {
        return nombreBeneficiario;
    }

    public void setNombreBeneficiario(String nombreBeneficiario) {
        this.nombreBeneficiario = nombreBeneficiario;
    }

    public String getLogros() {
        return logros;
    }

    public void setLogros(String logros) {
        this.logros = logros;
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
        hash += (desarrollo != null ? desarrollo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DesarrolloSoftware)) {
            return false;
        }
        DesarrolloSoftware other = (DesarrolloSoftware) object;
        if ((this.desarrollo == null && other.desarrollo != null) || (this.desarrollo != null && !this.desarrollo.equals(other.desarrollo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware[ desarrollo=" + desarrollo + " ]";
    }
    
}
