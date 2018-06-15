/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Planeación
 */
@Entity
@Table(name = "universidades", catalog = "saiiut", schema = "saiiut")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Universidades.findAll", query = "SELECT u FROM Universidades u")
    , @NamedQuery(name = "Universidades.findByCveUniversidad", query = "SELECT u FROM Universidades u WHERE u.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Universidades.findByCveCgut", query = "SELECT u FROM Universidades u WHERE u.cveCgut = :cveCgut")
    , @NamedQuery(name = "Universidades.findByNombre", query = "SELECT u FROM Universidades u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "Universidades.findByAbreviatura", query = "SELECT u FROM Universidades u WHERE u.abreviatura = :abreviatura")
    , @NamedQuery(name = "Universidades.findByRfc", query = "SELECT u FROM Universidades u WHERE u.rfc = :rfc")
    , @NamedQuery(name = "Universidades.findByCertificada", query = "SELECT u FROM Universidades u WHERE u.certificada = :certificada")
    , @NamedQuery(name = "Universidades.findByFechaAcreditacion", query = "SELECT u FROM Universidades u WHERE u.fechaAcreditacion = :fechaAcreditacion")
    , @NamedQuery(name = "Universidades.findByActivo", query = "SELECT u FROM Universidades u WHERE u.activo = :activo")
    , @NamedQuery(name = "Universidades.findByCveRector", query = "SELECT u FROM Universidades u WHERE u.cveRector = :cveRector")
    , @NamedQuery(name = "Universidades.findByCve1instDGP", query = "SELECT u FROM Universidades u WHERE u.cve1instDGP = :cve1instDGP")
    , @NamedQuery(name = "Universidades.findByCve2instDGP", query = "SELECT u FROM Universidades u WHERE u.cve2instDGP = :cve2instDGP")})
public class Universidades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_universidad")
    private Integer cveUniversidad;
    @Size(max = 20)
    @Column(name = "cve_cgut")
    private String cveCgut;
    @Size(max = 60)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 20)
    @Column(name = "abreviatura")
    private String abreviatura;
    @Size(max = 15)
    @Column(name = "rfc")
    private String rfc;
    @Column(name = "certificada")
    private Boolean certificada;
    @Column(name = "fecha_acreditacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAcreditacion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "cve_rector")
    private Integer cveRector;
    @Size(max = 2)
    @Column(name = "cve1_inst_DGP")
    private String cve1instDGP;
    @Size(max = 4)
    @Column(name = "cve2_inst_DGP")
    private String cve2instDGP;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "universidades")
    private List<Puestos> puestosList;

    public Universidades() {
    }

    public Universidades(Integer cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public Integer getCveUniversidad() {
        return cveUniversidad;
    }

    public void setCveUniversidad(Integer cveUniversidad) {
        this.cveUniversidad = cveUniversidad;
    }

    public String getCveCgut() {
        return cveCgut;
    }

    public void setCveCgut(String cveCgut) {
        this.cveCgut = cveCgut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Boolean getCertificada() {
        return certificada;
    }

    public void setCertificada(Boolean certificada) {
        this.certificada = certificada;
    }

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getCveRector() {
        return cveRector;
    }

    public void setCveRector(Integer cveRector) {
        this.cveRector = cveRector;
    }

    public String getCve1instDGP() {
        return cve1instDGP;
    }

    public void setCve1instDGP(String cve1instDGP) {
        this.cve1instDGP = cve1instDGP;
    }

    public String getCve2instDGP() {
        return cve2instDGP;
    }

    public void setCve2instDGP(String cve2instDGP) {
        this.cve2instDGP = cve2instDGP;
    }

    @XmlTransient
    public List<Puestos> getPuestosList() {
        return puestosList;
    }

    public void setPuestosList(List<Puestos> puestosList) {
        this.puestosList = puestosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveUniversidad != null ? cveUniversidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Universidades)) {
            return false;
        }
        Universidades other = (Universidades) object;
        if ((this.cveUniversidad == null && other.cveUniversidad != null) || (this.cveUniversidad != null && !this.cveUniversidad.equals(other.cveUniversidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Universidades[ cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
