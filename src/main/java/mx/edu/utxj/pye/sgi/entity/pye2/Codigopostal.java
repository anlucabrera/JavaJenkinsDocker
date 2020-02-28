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
import javax.persistence.Lob;
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
@Table(name = "codigopostal", catalog = "pye2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Codigopostal.findAll", query = "SELECT c FROM Codigopostal c")
    , @NamedQuery(name = "Codigopostal.findByCodigoPostal", query = "SELECT c FROM Codigopostal c WHERE c.codigopostalPK.codigoPostal = :codigoPostal")
    , @NamedQuery(name = "Codigopostal.findByConsecutivoCp", query = "SELECT c FROM Codigopostal c WHERE c.codigopostalPK.consecutivoCp = :consecutivoCp")
    , @NamedQuery(name = "Codigopostal.findByEstado", query = "SELECT c FROM Codigopostal c WHERE c.codigopostalPK.estado = :estado")
    , @NamedQuery(name = "Codigopostal.findByMunicipio", query = "SELECT c FROM Codigopostal c WHERE c.codigopostalPK.municipio = :municipio")
    , @NamedQuery(name = "Codigopostal.findByActivo", query = "SELECT c FROM Codigopostal c WHERE c.activo = :activo")})
public class Codigopostal implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CodigopostalPK codigopostalPK;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "asentamiento")
    private String asentamiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @JoinColumns({
        @JoinColumn(name = "estado", referencedColumnName = "claveEstado", insertable = false, updatable = false)
        , @JoinColumn(name = "municipio", referencedColumnName = "claveMunicipio", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Municipio municipio1;
    @JoinColumn(name = "tipo_asentamiento", referencedColumnName = "tipoAsentamiento")
    @ManyToOne(optional = false)
    private Tipoasentamiento tipoAsentamiento;

    public Codigopostal() {
    }

    public Codigopostal(CodigopostalPK codigopostalPK) {
        this.codigopostalPK = codigopostalPK;
    }

    public Codigopostal(CodigopostalPK codigopostalPK, String asentamiento, boolean activo) {
        this.codigopostalPK = codigopostalPK;
        this.asentamiento = asentamiento;
        this.activo = activo;
    }

    public Codigopostal(String codigoPostal, int consecutivoCp, int estado, int municipio) {
        this.codigopostalPK = new CodigopostalPK(codigoPostal, consecutivoCp, estado, municipio);
    }

    public CodigopostalPK getCodigopostalPK() {
        return codigopostalPK;
    }

    public void setCodigopostalPK(CodigopostalPK codigopostalPK) {
        this.codigopostalPK = codigopostalPK;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigopostalPK != null ? codigopostalPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Codigopostal)) {
            return false;
        }
        Codigopostal other = (Codigopostal) object;
        if ((this.codigopostalPK == null && other.codigopostalPK != null) || (this.codigopostalPK != null && !this.codigopostalPK.equals(other.codigopostalPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.pye2.Codigopostal[ codigopostalPK=" + codigopostalPK + " ]";
    }
    
}
