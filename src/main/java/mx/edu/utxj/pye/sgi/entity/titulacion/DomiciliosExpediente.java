/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.titulacion;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author UTXJ
 */
@Entity
@Table(name = "domicilios_expediente", catalog = "titulacion", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DomiciliosExpediente.findAll", query = "SELECT d FROM DomiciliosExpediente d")
    , @NamedQuery(name = "DomiciliosExpediente.findByDomicilio", query = "SELECT d FROM DomiciliosExpediente d WHERE d.domicilio = :domicilio")
    , @NamedQuery(name = "DomiciliosExpediente.findByCalle", query = "SELECT d FROM DomiciliosExpediente d WHERE d.calle = :calle")
    , @NamedQuery(name = "DomiciliosExpediente.findByColonia", query = "SELECT d FROM DomiciliosExpediente d WHERE d.colonia = :colonia")
    , @NamedQuery(name = "DomiciliosExpediente.findByNumExterior", query = "SELECT d FROM DomiciliosExpediente d WHERE d.numExterior = :numExterior")
    , @NamedQuery(name = "DomiciliosExpediente.findByNumInterior", query = "SELECT d FROM DomiciliosExpediente d WHERE d.numInterior = :numInterior")
    , @NamedQuery(name = "DomiciliosExpediente.findByEstado", query = "SELECT d FROM DomiciliosExpediente d WHERE d.estado = :estado")
    , @NamedQuery(name = "DomiciliosExpediente.findByMunicipio", query = "SELECT d FROM DomiciliosExpediente d WHERE d.municipio = :municipio")
    , @NamedQuery(name = "DomiciliosExpediente.findByLocalidad", query = "SELECT d FROM DomiciliosExpediente d WHERE d.localidad = :localidad")})
public class DomiciliosExpediente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "domicilio")
    private Integer domicilio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "colonia")
    private String colonia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "num_exterior")
    private String numExterior;
    @Size(max = 10)
    @Column(name = "num_interior")
    private String numInterior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private int estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "municipio")
    private int municipio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "localidad")
    private int localidad;
    @JoinColumn(name = "expediente", referencedColumnName = "expediente")
    @ManyToOne(optional = false)
    private ExpedientesTitulacion expediente;

    public DomiciliosExpediente() {
    }

    public DomiciliosExpediente(Integer domicilio) {
        this.domicilio = domicilio;
    }

    public DomiciliosExpediente(Integer domicilio, String calle, String colonia, String numExterior, int estado, int municipio, int localidad) {
        this.domicilio = domicilio;
        this.calle = calle;
        this.colonia = colonia;
        this.numExterior = numExterior;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
    }

    public Integer getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Integer domicilio) {
        this.domicilio = domicilio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getNumExterior() {
        return numExterior;
    }

    public void setNumExterior(String numExterior) {
        this.numExterior = numExterior;
    }

    public String getNumInterior() {
        return numInterior;
    }

    public void setNumInterior(String numInterior) {
        this.numInterior = numInterior;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public int getLocalidad() {
        return localidad;
    }

    public void setLocalidad(int localidad) {
        this.localidad = localidad;
    }

    public ExpedientesTitulacion getExpediente() {
        return expediente;
    }

    public void setExpediente(ExpedientesTitulacion expediente) {
        this.expediente = expediente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (domicilio != null ? domicilio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DomiciliosExpediente)) {
            return false;
        }
        DomiciliosExpediente other = (DomiciliosExpediente) object;
        if ((this.domicilio == null && other.domicilio != null) || (this.domicilio != null && !this.domicilio.equals(other.domicilio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente[ domicilio=" + domicilio + " ]";
    }
    
}
