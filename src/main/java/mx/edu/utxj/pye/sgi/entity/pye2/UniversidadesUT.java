/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "universidadesUT", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniversidadesUT.findAll", query = "SELECT u FROM UniversidadesUT u")
    , @NamedQuery(name = "UniversidadesUT.findByCveUniversidad", query = "SELECT u FROM UniversidadesUT u WHERE u.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "UniversidadesUT.findByCveCgut", query = "SELECT u FROM UniversidadesUT u WHERE u.cveCgut = :cveCgut")
    , @NamedQuery(name = "UniversidadesUT.findByNombre", query = "SELECT u FROM UniversidadesUT u WHERE u.nombre = :nombre")
    , @NamedQuery(name = "UniversidadesUT.findByAbreviatura", query = "SELECT u FROM UniversidadesUT u WHERE u.abreviatura = :abreviatura")
    , @NamedQuery(name = "UniversidadesUT.findByRfc", query = "SELECT u FROM UniversidadesUT u WHERE u.rfc = :rfc")
    , @NamedQuery(name = "UniversidadesUT.findByCertificada", query = "SELECT u FROM UniversidadesUT u WHERE u.certificada = :certificada")
    , @NamedQuery(name = "UniversidadesUT.findByFechaAcreditacion", query = "SELECT u FROM UniversidadesUT u WHERE u.fechaAcreditacion = :fechaAcreditacion")
    , @NamedQuery(name = "UniversidadesUT.findByActivo", query = "SELECT u FROM UniversidadesUT u WHERE u.activo = :activo")
    , @NamedQuery(name = "UniversidadesUT.findByCveRector", query = "SELECT u FROM UniversidadesUT u WHERE u.cveRector = :cveRector")
    , @NamedQuery(name = "UniversidadesUT.findByCve1instDGP", query = "SELECT u FROM UniversidadesUT u WHERE u.cve1instDGP = :cve1instDGP")
    , @NamedQuery(name = "UniversidadesUT.findByCve2instDGP", query = "SELECT u FROM UniversidadesUT u WHERE u.cve2instDGP = :cve2instDGP")})
public class UniversidadesUT implements Serializable {

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
    private Short certificada;
    @Column(name = "fecha_acreditacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAcreditacion;
    @Column(name = "activo")
    private Short activo;
    @Column(name = "cve_rector")
    private Integer cveRector;
    @Size(max = 2)
    @Column(name = "cve1_inst_DGP")
    private String cve1instDGP;
    @Size(max = 4)
    @Column(name = "cve2_inst_DGP")
    private String cve2instDGP;

    public UniversidadesUT() {
    }

    public UniversidadesUT(Integer cveUniversidad) {
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

    public Short getCertificada() {
        return certificada;
    }

    public void setCertificada(Short certificada) {
        this.certificada = certificada;
    }

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public Short getActivo() {
        return activo;
    }

    public void setActivo(Short activo) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cveUniversidad != null ? cveUniversidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniversidadesUT)) {
            return false;
        }
        UniversidadesUT other = (UniversidadesUT) object;
        if ((this.cveUniversidad == null && other.cveUniversidad != null) || (this.cveUniversidad != null && !this.cveUniversidad.equals(other.cveUniversidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Universidades[ cveUniversidad=" + cveUniversidad + " ]";
    }
    
}
