/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.pye2;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "asentamiento",catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asentamiento.findAll", query = "SELECT a FROM Asentamiento a")
    , @NamedQuery(name = "Asentamiento.findByEstado", query = "SELECT a FROM Asentamiento a WHERE a.asentamientoPK.estado = :estado")
    , @NamedQuery(name = "Asentamiento.findByMunicipio", query = "SELECT a FROM Asentamiento a WHERE a.asentamientoPK.municipio = :municipio")
    , @NamedQuery(name = "Asentamiento.findByAsentamiento", query = "SELECT a FROM Asentamiento a WHERE a.asentamientoPK.asentamiento = :asentamiento")
    , @NamedQuery(name = "Asentamiento.findByCodigoPostal", query = "SELECT a FROM Asentamiento a WHERE a.codigoPostal = :codigoPostal")
    , @NamedQuery(name = "Asentamiento.findByNombreAsentamiento", query = "SELECT a FROM Asentamiento a WHERE a.nombreAsentamiento = :nombreAsentamiento")})
public class Asentamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AsentamientoPK asentamientoPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "codigo_postal")
    private String codigoPostal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 350)
    @Column(name = "nombre_asentamiento")
    private String nombreAsentamiento;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado", insertable = false, updatable = false)
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Municipio municipio1;
    @JoinColumn(name = "tipo_asentamiento", referencedColumnName = "tipoAsentamiento")
    @ManyToOne(optional = false)
    private Tipoasentamiento tipoAsentamiento;
    @JoinColumn(name = "zona_asentamiento", referencedColumnName = "clave")
    @ManyToOne(optional = false)
    private Tipolocalidad zonaAsentamiento;

    public Asentamiento() {
    }

    public Asentamiento(AsentamientoPK asentamientoPK) {
        this.asentamientoPK = asentamientoPK;
    }

    public Asentamiento(AsentamientoPK asentamientoPK, String codigoPostal, String nombreAsentamiento) {
        this.asentamientoPK = asentamientoPK;
        this.codigoPostal = codigoPostal;
        this.nombreAsentamiento = nombreAsentamiento;
    }

    public Asentamiento(int estado, int municipio, int asentamiento) {
        this.asentamientoPK = new AsentamientoPK(estado, municipio, asentamiento);
    }

    public AsentamientoPK getAsentamientoPK() {
        return asentamientoPK;
    }

    public void setAsentamientoPK(AsentamientoPK asentamientoPK) {
        this.asentamientoPK = asentamientoPK;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNombreAsentamiento() {
        return nombreAsentamiento;
    }

    public void setNombreAsentamiento(String nombreAsentamiento) {
        this.nombreAsentamiento = nombreAsentamiento;
    }

    public Municipio getMunicipio1() {
        return municipio1;
    }

    public void setMunicipio1(Municipio municipio1) {
        this.municipio1 = municipio1;
    }

    public Tipoasentamiento getTipoAsentamiento() {
        return tipoAsentamiento;
    }

    public void setTipoAsentamiento(Tipoasentamiento tipoAsentamiento) {
        this.tipoAsentamiento = tipoAsentamiento;
    }

    public Tipolocalidad getZonaAsentamiento() {
        return zonaAsentamiento;
    }

    public void setZonaAsentamiento(Tipolocalidad zonaAsentamiento) {
        this.zonaAsentamiento = zonaAsentamiento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asentamientoPK != null ? asentamientoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asentamiento)) {
            return false;
        }
        Asentamiento other = (Asentamiento) object;
        if ((this.asentamientoPK == null && other.asentamientoPK != null) || (this.asentamientoPK != null && !this.asentamientoPK.equals(other.asentamientoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "asentamientoPK-" + asentamientoPK ;
    }
    
}
