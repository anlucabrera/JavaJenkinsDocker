/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "domicilios", catalog = "saiiut", schema = "SAIIUT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domicilios.findAll", query = "SELECT d FROM Domicilios d")
    , @NamedQuery(name = "Domicilios.findByCvePersona", query = "SELECT d FROM Domicilios d WHERE d.domiciliosPK.cvePersona = :cvePersona")
    , @NamedQuery(name = "Domicilios.findByCveUniversidad", query = "SELECT d FROM Domicilios d WHERE d.domiciliosPK.cveUniversidad = :cveUniversidad")
    , @NamedQuery(name = "Domicilios.findByConsecutivoDomicilio", query = "SELECT d FROM Domicilios d WHERE d.domiciliosPK.consecutivoDomicilio = :consecutivoDomicilio")
    , @NamedQuery(name = "Domicilios.findByCodigoPostal", query = "SELECT d FROM Domicilios d WHERE d.codigoPostal = :codigoPostal")
    , @NamedQuery(name = "Domicilios.findByConsecutivoCp", query = "SELECT d FROM Domicilios d WHERE d.consecutivoCp = :consecutivoCp")
    , @NamedQuery(name = "Domicilios.findByCveEstado", query = "SELECT d FROM Domicilios d WHERE d.cveEstado = :cveEstado")
    , @NamedQuery(name = "Domicilios.findByCveMunicipio", query = "SELECT d FROM Domicilios d WHERE d.cveMunicipio = :cveMunicipio")
    , @NamedQuery(name = "Domicilios.findByCalle", query = "SELECT d FROM Domicilios d WHERE d.calle = :calle")
    , @NamedQuery(name = "Domicilios.findByNumeroExterior", query = "SELECT d FROM Domicilios d WHERE d.numeroExterior = :numeroExterior")
    , @NamedQuery(name = "Domicilios.findByNumeroInterior", query = "SELECT d FROM Domicilios d WHERE d.numeroInterior = :numeroInterior")
    , @NamedQuery(name = "Domicilios.findByNumeroDepto", query = "SELECT d FROM Domicilios d WHERE d.numeroDepto = :numeroDepto")
    , @NamedQuery(name = "Domicilios.findByComentario", query = "SELECT d FROM Domicilios d WHERE d.comentario = :comentario")
    , @NamedQuery(name = "Domicilios.findByActivo", query = "SELECT d FROM Domicilios d WHERE d.activo = :activo")
    , @NamedQuery(name = "Domicilios.findByReferencias", query = "SELECT d FROM Domicilios d WHERE d.referencias = :referencias")})
public class Domicilios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected DomiciliosPK domiciliosPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codigo_postal")
    private String codigoPostal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "consecutivo_cp")
    private int consecutivoCp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_estado")
    private int cveEstado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cve_municipio")
    private int cveMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "calle")
    private String calle;
    @Size(max = 10)
    @Column(name = "numero_exterior")
    private String numeroExterior;
    @Size(max = 10)
    @Column(name = "numero_interior")
    private String numeroInterior;
    @Size(max = 10)
    @Column(name = "numero_depto")
    private String numeroDepto;
    @Size(max = 150)
    @Column(name = "comentario")
    private String comentario;
    @Column(name = "activo")
    private Boolean activo;
    @Size(max = 300)
    @Column(name = "referencias")
    private String referencias;
    
    public Domicilios() {
    }
    
    public Domicilios(DomiciliosPK domiciliosPK) {
        this.domiciliosPK = domiciliosPK;
    }
    
    public Domicilios(Integer cvePersona, int cveUniversidad, int consecutivoDomicilio) {
        this.domiciliosPK = new  DomiciliosPK(cvePersona, cveUniversidad, consecutivoDomicilio);
        
    }
    
    public DomiciliosPK getDomiciliosPK() {
        return domiciliosPK;
    }

    public void setDomiciliosPK(DomiciliosPK domiciliosPK) {
        this.domiciliosPK = domiciliosPK;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getConsecutivoCp() {
        return consecutivoCp;
    }

    public void setConsecutivoCp(int consecutivoCp) {
        this.consecutivoCp = consecutivoCp;
    }

    public int getCveEstado() {
        return cveEstado;
    }

    public void setCveEstado(int cveEstado) {
        this.cveEstado = cveEstado;
    }

    public int getCveMunicipio() {
        return cveMunicipio;
    }

    public void setCveMunicipio(int cveMunicipio) {
        this.cveMunicipio = cveMunicipio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getNumeroDepto() {
        return numeroDepto;
    }

    public void setNumeroDepto(String numeroDepto) {
        this.numeroDepto = numeroDepto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (domiciliosPK != null ? domiciliosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domicilios)) {
            return false;
        }
        Domicilios other = (Domicilios) object;
        if ((this.domiciliosPK == null && other.domiciliosPK != null) || (this.domiciliosPK != null && !this.domiciliosPK.equals(other.domiciliosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.saiiut.entity.Domicilios[ domiciliosPK=" + domiciliosPK + " ]";
    }
    
}
