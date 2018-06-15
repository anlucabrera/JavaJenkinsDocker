/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.entity.prontuario;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author jonny
 */
@Entity
@Table(name = "configuracion_propiedades", catalog = "prontuario", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionPropiedades.findAll", query = "SELECT c FROM ConfiguracionPropiedades c")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByClave", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.clave = :clave")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByModulo", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.modulo = :modulo")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByTipo", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.tipo = :tipo")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByValorCadena", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.valorCadena = :valorCadena")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByValorEntero", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.valorEntero = :valorEntero")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByValorDecimal", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.valorDecimal = :valorDecimal")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByValorFecha", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.valorFecha = :valorFecha")
    , @NamedQuery(name = "ConfiguracionPropiedades.findByEjbs", query = "SELECT c FROM ConfiguracionPropiedades c WHERE c.ejbs = :ejbs")})
public class ConfiguracionPropiedades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "modulo")
    private String modulo;
    @Size(max = 7)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 500)
    @Column(name = "valor_cadena")
    private String valorCadena;
    @Column(name = "valor_entero")
    private Integer valorEntero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_decimal")
    private Double valorDecimal;
    @Column(name = "valor_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date valorFecha;
    @Size(max = 1500)
    @Column(name = "ejbs")
    private String ejbs;

    public ConfiguracionPropiedades() {
    }

    public ConfiguracionPropiedades(String clave) {
        this.clave = clave;
    }

    public ConfiguracionPropiedades(String clave, String modulo) {
        this.clave = clave;
        this.modulo = modulo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValorCadena() {
        return valorCadena;
    }

    public void setValorCadena(String valorCadena) {
        this.valorCadena = valorCadena;
    }

    public Integer getValorEntero() {
        return valorEntero;
    }

    public void setValorEntero(Integer valorEntero) {
        this.valorEntero = valorEntero;
    }

    public Double getValorDecimal() {
        return valorDecimal;
    }

    public void setValorDecimal(Double valorDecimal) {
        this.valorDecimal = valorDecimal;
    }

    public Date getValorFecha() {
        return valorFecha;
    }

    public void setValorFecha(Date valorFecha) {
        this.valorFecha = valorFecha;
    }

    public String getEjbs() {
        return ejbs;
    }

    public void setEjbs(String ejbs) {
        this.ejbs = ejbs;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfiguracionPropiedades)) {
            return false;
        }
        ConfiguracionPropiedades other = (ConfiguracionPropiedades) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades[ clave=" + clave + " ]";
    }
    
}
